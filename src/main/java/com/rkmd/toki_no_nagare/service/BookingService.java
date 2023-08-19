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
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.BookingRepository;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<Booking> get(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Map<String, Object> json) {
        Booking newBooking = new Booking();

        //TODO: CONTINUE HERE...
        /*newBooking.setName((String) json.get("name"));
        newBooking.setLastName((String) json.get("last_name"));
        newBooking.setPasswordHash((String) json.get("password"));*/

        try {
            return bookingRepository.save(newBooking);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }


    /** This method checks if a booking exists according to the 'dni' and 'bookingCode' passed as parameters.
     * If exists, returns the booking data. If it doesn't exist, it throws an exception.
     * @param bookingCode Booking code
     * @param dni User identity number
     * @throws BadRequestException Throw BadRequestException if booking not exists
     * @return BookingResponseDto
     */
    public BookingResponseDto getBookingByCodeAndDni(String bookingCode, Long dni){
        String hashedBookingCode = Tools.generateHashCode(dni, bookingCode);
        List<Booking> bookings = bookingRepository.findAll();
        Optional<Booking> booking = bookings.stream().filter(b -> b.getHashedBookingCode().equals(hashedBookingCode)).findFirst();

        if(booking.isEmpty()){
            throw new NotFoundException("booking_not_found", "The requested booking does not exist.");
        }

        return modelMapper.map(booking.get(), BookingResponseDto.class);
    }


    /** This method creates a booking according to the data entered by the user, if the seats are not already reserved.
     * If seat already reserved throws an exception.
     * @param request Data that is necessary to create a booking
     * @throws BadRequestException Throw BadRequestException if seats already reserved
     * @return CreateBookingResponseDto
     */
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto request){

        // Search in the database for the seats requested by the user and verify if the status is vacant.
        List<Seat> seats = seatService.getSeatsRequestedByUser(request.getSeats());
        seatService.validateSeatsStatus(seats);

        // Change the seat's status and then persisted in the database
        seatService.updateSeatStatus(seats);
        BigDecimal paymentAmount = seatService.getTotalPaymentAmount(seats);

        // Create the contact and then persisted in the database
        Contact newContact = contactService.create(
            request.getContact().getDni(),
            request.getContact().getName(),
            request.getContact().getLastName(),
            request.getContact().getEmail(),
            request.getContact().getPhone(),
            request.getContact().getPhoneType()
        );

        // Creates the payment and then persists it in the database
        Payment newPayment = paymentService.createPayment(request.getPaymentMethod(), paymentAmount);

        // Generates a random booking code and generates a booking hash code to persists in the database
        String bookingCode = Tools.generateRandomHash();  // TODO: This bookingCode should be sent to the user via email
        String hashedBookingCode = Tools.generateHashCode(request.getContact().getDni(), bookingCode);

        // Creates a booking with the data requested by the user and persists it in the database
        Booking newBooking = new Booking(newContact, newPayment, seats, hashedBookingCode);
        newBooking = bookingRepository.saveAndFlush(newBooking);

        // Create the response for the user
        CreateBookingResponseDto response = new CreateBookingResponseDto();
        response.setBookingCode(bookingCode);
        response.setDateCreated(newBooking.getDateCreated());
        response.setExpirationDate(newBooking.getExpirationDate());
        response.setContact(modelMapper.map(newBooking.getClient(), ContactDto.class));
        response.setPayment(modelMapper.map(newBooking.getPayment(), PaymentDto.class));
        response.setSeats(newBooking.getSeats()
            .stream()
            .map(seat -> modelMapper.map(seat, SeatDto.class))
            .toList());

        return response;
    }
}
