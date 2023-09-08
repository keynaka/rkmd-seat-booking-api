package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.Contact.ContactDto;
import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.PaymentDto;
import com.rkmd.toki_no_nagare.dto.report.BookingStatisticsDto;
import com.rkmd.toki_no_nagare.dto.report.RecentSalesDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.ApiException;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.exception.RequestTimeoutException;
import com.rkmd.toki_no_nagare.repositories.BookingRepository;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Constants;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private AbstractMailingService mailingService;

    @Autowired
    private AdminAvailableDateService adminAvailableDateService;

    public Optional<Booking> get(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public BookingResponseDto getBookingByCode(String code){
        List<Booking> allBookings = bookingRepository.findAll();

        Booking reservedBooking = null;

        for(Booking booking : allBookings){
            if(booking.getHashedBookingCode().equals(code)) {
                reservedBooking = booking;
            }
        }

        if(reservedBooking == null){
            throw new NotFoundException("booking_code_not_found", "The booking code does not exist");
        }

        return createBookingResponseDto(reservedBooking);
    }

    /** This method checks if a booking exists according to the 'dni' and 'bookingCode' passed as parameters.
     * If exists, returns the booking data. If it doesn't exist or 'bookingCode' is invalid, it throws an exception.
     * @param bookingCode Booking code
     * @param dni User identity number
     * @throws NotFoundException if booking not exists
     * @throws BadRequestException if booking code is invalid
     * @return BookingResponseDto
     */
    public BookingResponseDto getBookingByCodeAndDni(String bookingCode, Long dni){
        List<Booking> allBookings = bookingRepository.findAll();
        Optional<Booking> userBooking = allBookings.stream()
            .filter(b -> b.getClient().getDni().equals(dni) && b.getHashedBookingCode().equals(bookingCode)).findFirst();

        if(userBooking.isEmpty()){
            throw new NotFoundException("booking_not_found", "The requested booking not exist.");
        }

        return createBookingResponseDto(userBooking.get());
    }


    /** This method creates a booking according to the data entered by the user, if the seats are not already reserved.
     * If seat already reserved throws an exception.
     * @param request Data that is necessary to create a booking
     * @throws BadRequestException Throw BadRequestException if seats already reserved
     * @return CreateBookingResponseDto
     */
    @Transactional
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto request){
        try{
            // Step 1: Search in the database for the seats requested by the user
            List<Seat> seats = seatService.getSeatsRequestedByUser(request.getSeats());

            // Step 2: Verify if the seat is still available for booking
            seatService.validateAvailableSeatForBooking(seats);

            // Step 3: Create the contact if not exist or update if exist
            Contact contact = contactService.createOrUpdate(request.getContact());

            // Step 4: Calculate the total price to pay for the booking
            BigDecimal paymentAmount = seatService.getTotalPaymentAmount(seats);

            // Step 5: Creates the payment and then persists it in the database
            Payment payment = paymentService.createPayment(request.getPaymentMethod(), paymentAmount);

            // Step 6: Generates a random booking code and generates a booking hash code to persists in the database
            String bookingCode = generateBookingCode();

            // Step 7: Creates a booking with the data requested by the user and persists it in the database
            Booking booking = bookingRepository.saveAndFlush(new Booking(contact, payment, bookingCode));

            // Step 8: Associate the seat data with the booking, changes the seat's status and persists it in the database
            seatService.updateSeatData(seats, booking);

            // Step 6: notify reservation by sending an e-mail to the client
            mailingService.notifyReservation(contact.getEmail(),
                    contact.getName(), contact.getLastName(),
                    bookingCode, booking.getPayment().getPaymentMethod(),
                    booking.getExpirationDate(), Tools.convertSeatToSeatDto(seats));

            // Step 10: Create the response for the user // TODO: This response should be sent to the user via email
            return createResponse(booking, bookingCode, seats);

        } catch (DataIntegrityViolationException e){
            log.warn("booking_unique_key_constraint_violation: " + e.getMessage());
            throw new ApiException("booking_error", "The booking could not be processed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | RequestTimeoutException e){
            throw e;
        } catch (Exception e){
            log.warn("booking_internal_server_error: " + e.getMessage());
            throw new ApiException("booking_error", "The booking could not be processed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /** This method creates the user response according to the booking data passed as parameters.
     * @param booking Booking data
     * @param bookingCode Booking code
     * @param seats List of seats
     * @return CreateBookingResponseDto
     * */
    public CreateBookingResponseDto createResponse(Booking booking, String bookingCode, List<Seat> seats){
        CreateBookingResponseDto response = new CreateBookingResponseDto();
        response.setStatus(booking.getStatus());
        response.setBookingCode(bookingCode);
        response.setDateCreated(booking.getDateCreated());
        response.setExpirationDate(booking.getExpirationDate());
        response.setContact(modelMapper.map(booking.getClient(), ContactDto.class));
        response.setPayment(modelMapper.map(booking.getPayment(), PaymentDto.class));
        response.setSeats(seats
            .stream()
            .map(seat -> modelMapper.map(seat, SeatDto.class))
            .toList());
        response.setAdminAvailableDates(adminAvailableDateService.getAvailableDates(booking.getPayment().getExpirationDate()));

        return response;
    }


    /** This method creates a new booking code.
     * @return String
     * */
    public String generateBookingCode(){
        List<Booking> allBookings = bookingRepository.findAll();
        String newBookingCode = null;
        do {
            newBookingCode = Tools.generateRandomHash();
        } while (existsInDatabase(allBookings, newBookingCode));
        return newBookingCode;
    }


    /** This method validate if the new booking code generated exist on database. If exist returns true otherwise
     * return false.
     * @param bookings List of bookings
     * @param newBookingCode New booking code
     * @return boolean
     * */
    public boolean existsInDatabase(List<Booking> bookings, String newBookingCode){
        boolean isRepeated = bookings.stream().anyMatch(b -> b.getHashedBookingCode().equals(newBookingCode));
        if(isRepeated) log.info("Booking code generated is repeated: {}", newBookingCode);
        return isRepeated;
    }

    private BookingResponseDto createBookingResponseDto(Booking reservedBooking) {
        BookingResponseDto response = modelMapper.map(reservedBooking, BookingResponseDto.class);

        // This step is necessary because the "Contact" attribute of the "Booking" class was defined with the name client
        response.setContact(modelMapper.map(reservedBooking.getClient(), ContactDto.class));
        return response;
    }

    public String formatTitle(Booking booking) {
        String sector = "";
        Long row = 0l;
        String seats = "";

        List<Seat> sortedSeats = booking.getSeats().stream().sorted((a, b) -> a.getColumn().compareTo(b.getColumn())).collect(Collectors.toList());
        for (Seat seat : sortedSeats) {
            sector = seat.getSector().name();
            row = seat.getRow();
            seats = String.format("%s %s", seats, seat.getColumn().toString());
        }

        return String.format("Sector %s - Fila %s - Asiento %s - $ %s", sector, row, seats, booking.getPayment().getAmount().toString());
    }



  /** This method generates reports:
   * <li> General booking reports
   * <li> Pullman sector reports
   * <li> Platea sector reports
   * <li> Report of the last 20 sales
   * @return BookingStatisticsDto
   * */
  public BookingStatisticsDto getGeneralStatus() {
    List<Seat> seats = seatService.getAllSeats();
    String date = Tools.getCurrentDateAsString();

    BookingStatisticsDto statistics = new BookingStatisticsDto();
    statistics.setGeneral(getReportBySector(seats, null, date));
    statistics.setPullman(getReportBySector(seats, SeatSector.PULLMAN, date));
    statistics.setPlatea(getReportBySector(seats, SeatSector.PLATEA, date));
    statistics.setRecentSales(getLastSales());

    return statistics;
  }


  /** This method generates a report of the bookings according to the requested sector.
   * @param seats List of seats
   * @param sector Seat sector
   * @param date Current day
   * @return Map<String, String>>
   * */
  public Map<String, String> getReportBySector(List<Seat> seats, SeatSector sector, String date){

    List<Seat> seatsBySector = seats;
    int total_seats = Constants.TOTAL_SEATS;

    if(sector != null) {
      seatsBySector = seats.stream().filter(s -> sector.equals(s.getSector())).toList();
      switch (sector){
        case PULLMAN -> total_seats = Constants.TOTAL_PULLMAN_SEATS;
        case PLATEA -> total_seats = Constants.TOTAL_PLATEA_SEATS;
      }
    }

    Map<String, String> map = new HashMap<>();
    map.put(Constants.COLLECTED_FUNDS, String.valueOf(seatsBySector.stream()
        .filter(s -> SeatStatus.OCCUPIED.equals(s.getStatus()))
        .map(Seat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)));

    map.put(Constants.FUNDS_TO_BE_COLLECTED, String.valueOf(seatsBySector.stream()
        .filter(s -> s.getStatus() != SeatStatus.OCCUPIED)
        .map(Seat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)));

    map.put(Constants.CASH, String.valueOf(seatsBySector.stream()
        .filter(s -> SeatStatus.OCCUPIED.equals(s.getStatus()) && PaymentMethod.CASH.equals(s.getBooking().getPayment().getPaymentMethod()) )
        .map(Seat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)));

    map.put(Constants.MERCADO_PAGO, String.valueOf(seatsBySector.stream()
        .filter(s -> SeatStatus.OCCUPIED.equals(s.getStatus()) && PaymentMethod.MERCADO_PAGO.equals(s.getBooking().getPayment().getPaymentMethod()) )
        .map(Seat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)));

    Long occupiedSeats = seatsBySector.stream().filter(v -> SeatStatus.OCCUPIED.equals(v.getStatus())).count();
    DecimalFormat df = new DecimalFormat("0.00");
    map.put(Constants.CAPACITY_PERCENTAGE, String.valueOf( df.format(((double) occupiedSeats / total_seats) * 100)));
    map.put(Constants.OCCUPIED_SEATS, String.valueOf(occupiedSeats));
    map.put(Constants.RESERVED_SEATS, String.valueOf(seatsBySector.stream().filter(v -> SeatStatus.RESERVED.equals(v.getStatus())).count()));
    map.put(Constants.VACANT_SEATS, String.valueOf(seatsBySector.stream().filter(v -> SeatStatus.VACANT.equals(v.getStatus())).count()));

    map.put(Constants.DATE, date);

    return map;
  }


  /** This method get the last 20 bookings made. This method is used for booking generation. The map contains as key:
   * the booking code and as value, a concatenated string of date, payment method, amount.
   * @return List<RecentSales>
   * */
  public List<RecentSalesDto> getLastSales(){
    List<Booking> bookings = bookingRepository.findAll();
    List<Booking> lastBookings= bookings.stream().filter(b -> BookingStatus.PAID.equals(b.getStatus()))
        .sorted(Comparator.comparing(Booking::getLastUpdated).reversed())
        .limit(20).toList();

    List<RecentSalesDto> recentSales = new ArrayList<>();
    for (Booking booking : lastBookings){
      recentSales.add(new RecentSalesDto(
          booking.getHashedBookingCode(),
          Tools.formatArgentinianDate(booking.getLastUpdated()),
          booking.getPayment().getPaymentMethod().name().toLowerCase(),
          booking.getPayment().getAmount().toString()));
    }

    return recentSales;
  }

    /** This method updates the booking status based on the payment status if it is a valid status transition
     * @param booking Booking data
     * @param paymentStatus Payment status
     * */
    public static void updateBookingStatus(Booking booking, PaymentStatus paymentStatus){
        BookingStatus newBookingStatus = BookingStatus.PENDING;
        switch (paymentStatus) {
            case PENDING -> newBookingStatus = BookingStatus.PENDING;
            case PAID -> newBookingStatus = BookingStatus.PAID;
            case CANCELED -> newBookingStatus = BookingStatus.CANCELED;
            case EXPIRED -> newBookingStatus = BookingStatus.EXPIRED;
        }

        if (!validStatusTransition(booking.getStatus(), newBookingStatus))
            throw new BadRequestException("invalid_status", String.format("Invalid status transition from %s to %s", booking.getStatus().name(), newBookingStatus.name()));

        booking.setStatus(newBookingStatus);
        booking.setLastUpdated(Tools.getCurrentDate());
    }

  private static boolean validStatusTransition(BookingStatus currentStatus, BookingStatus newStatus) {
      boolean response = false;
      switch (currentStatus) {
          case PENDING -> response = List.of(BookingStatus.PENDING, BookingStatus.PAID, BookingStatus.EXPIRED, BookingStatus.CANCELED).contains(newStatus);
          case PAID -> response = List.of(BookingStatus.PAID).contains(newStatus);
          case EXPIRED -> response = List.of(BookingStatus.EXPIRED).contains(newStatus);
          case CANCELED -> response = List.of(BookingStatus.CANCELED).contains(newStatus);
      }

      return response;
  }

}
