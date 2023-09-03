package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.Contact.ContactDto;
import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.PaymentDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.exception.ApiException;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.exception.RequestTimeoutException;
import com.rkmd.toki_no_nagare.repositories.BookingRepository;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public Optional<Booking> get(Long id) {
        return bookingRepository.findById(id);
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
        List<Booking> userBookings = allBookings.stream().filter(b -> b.getClient().getDni().equals(dni)).toList();

        if(userBookings.isEmpty()){
            throw new NotFoundException("booking_not_found", "The requested booking does not exist.");
        }

        Booking reservedBooking = null;

        for(Booking booking : userBookings){
            boolean isValid = Tools.validateBookingCode(dni, bookingCode, booking.getHashedBookingCode());
            if(isValid) {
                reservedBooking = booking;
            }
        }

        if(reservedBooking == null){
            throw new BadRequestException("booking_code_invalid", "The booking code is invalid.");
        }

        return createBookingResponseDto(reservedBooking);
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

}
