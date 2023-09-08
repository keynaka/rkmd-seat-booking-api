package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.Contact.ContactDto;
import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.PaymentRepository;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationServiceFactory;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
  @Autowired
  private ExpirationServiceFactory expirationServiceFactory;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private AbstractMailingService mailingService;

  /** This method creates a payment and stores it in the database. The expiration date is calculated according to the
   * payment method.
   * @param amount This is the final amount that the user must pay, it is the sum of the seat prices.
   * @param paymentMethod Payment method
   * @return Payment
   * */
  public Payment createPayment(PaymentMethod paymentMethod, BigDecimal amount){
    ZonedDateTime dateCreated = Tools.getCurrentDate();
    ZonedDateTime expirationDate = expirationDateByPaymentMethod(paymentMethod, dateCreated);
    Payment payment = new Payment(paymentMethod, amount, dateCreated, expirationDate);
    return paymentRepository.saveAndFlush(payment);
  }

  /** This method returns the expiration date according to the payment method chosen by the user.
   * The number of days to be added to the current date based on the payment method is defined in the application.yml.
   * @param dateCreated Payment creation date
   * @param paymentMethod Payment method
   * @return ZonedDateTime
   */
  public ZonedDateTime expirationDateByPaymentMethod(PaymentMethod paymentMethod, ZonedDateTime dateCreated){
    return expirationServiceFactory.getExpirationService(paymentMethod).getExpirationDate(dateCreated);
  }


  /** This method checks if a booking exists according to the 'contactDni' and 'bookingCode' passed as parameters.
   * If exists, updates the payment status passed as a parameter and change the booking status. If it doesn't exist,
   * it throws an exception.
   * @param bookingCode Booking code
   * @param paymentStatus Payment status
   * @throws BadRequestException Throw BadRequestException is booking not exists
   * @return ChangePaymentResponseDto
   */
  @Transactional
  public ChangePaymentResponseDto changePaymentStatus(String bookingCode, PaymentStatus paymentStatus, String username){
    // Step 1: Get the payment corresponding to the booking code
    Payment payment = getPaymentByBookingCode(bookingCode);

    // Step 2: update the booking status based on the payment status and set seller's username
    Booking booking = payment.getBooking();
    BookingService.updateBookingStatus(booking, paymentStatus);
    booking.setSeller(username);

    // Step 3: update the seat status based on the payment status
    List<Seat> seats = payment.getBooking().getSeats();
    SeatService.updateSeatStatus(seats, paymentStatus);

    // Step 4: update the payment status
    updatePaymentStatus(payment, paymentStatus);

    // Step 5: save the payment data
    paymentRepository.saveAndFlush(payment);

    // Step 6: notify confirmation/expiration by sending an e-mail to the client
    if (paymentStatus.equals(PaymentStatus.PAID)) {
      mailingService.notifyConfirmation(booking.getClient().getEmail(),
              booking.getClient().getName(), booking.getClient().getLastName(),
              bookingCode, booking.getPayment().getPaymentMethod(),
              payment.getExpirationDate(), Tools.convertSeatToSeatDto(booking.getSeats()));
    }

    if (paymentStatus.equals(PaymentStatus.EXPIRED)) {
      mailingService.notifyExpiration(booking.getClient().getEmail(),
              booking.getClient().getName(), booking.getClient().getLastName(),
              bookingCode, payment.getExpirationDate());
    }

    // Step 7: Create the response for the user  // TODO: This response should be sent to the user via email
    return createResponse(booking, bookingCode, seats);
  }


  /** This method returns all registered payments
   * @return PaymentResponseDto
   * */
  public PaymentResponseDto getAllPayments(){
    List<Payment> payments = paymentRepository.findAll();
    return new PaymentResponseDto(payments);
  }

  /** This method returns all registered payments with some status
   * @return PaymentResponseDto
   * */
  public PaymentResponseDto getPaymentsByStatus(PaymentStatus status){
    List<Payment> payments = paymentRepository.findAllByPaymentStatus(status);
    return new PaymentResponseDto(payments);
  }


  /** This method creates the user response according to the booking data passed as parameters.
   * @param booking Booking data
   * @param bookingCode Booking code
   * @param seats List of seats
   * @return ChangePaymentResponseDto
   * */
  public ChangePaymentResponseDto createResponse(Booking booking, String bookingCode, List<Seat> seats){
    ChangePaymentResponseDto response = new ChangePaymentResponseDto();
    response.setStatus(booking.getStatus());
    response.setBookingCode(bookingCode);
    response.setSeller(booking.getSeller());
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


  /** This method get all the user payments according to the dni passed as parameters.
   * @param dni User identity number
   * @return List<Payment>
   * @throws NotFoundException If payment not found
   * */
  public List<Payment> getUserPayments(Long dni){
    List<Payment> allPayments = paymentRepository.findAll();
    List<Payment> userPayments = allPayments.stream().filter(p -> p.getBooking().getClient().getDni().equals(dni)).toList();

    if(userPayments.isEmpty()){
      throw new NotFoundException("payment_not_found", "The requested payment does not exist.");
    }

    return userPayments;
  }


  /** This method filters the user payment by the DNI and booking code passed as parameters
   * @param bookingCode Booking code
   * @throws NotFoundException If booking is invalid
   * */
  public Payment getPaymentByBookingCode(String bookingCode){
    List<Payment> allPayments = paymentRepository.findAll();

    Optional<Payment> paymentOptional = allPayments.stream()
        .filter(p -> p.getBooking().getHashedBookingCode().equals(bookingCode))
        .findFirst();

    if(paymentOptional.isEmpty()){
      throw new NotFoundException("booking_code_invalid", "The booking code is invalid.");
    }

    return paymentOptional.get();
  }

  /** This method updates the payment status if it is a valid status transition
   * @param payment Booking data
   * @param newStatus Payment status
   * */
  public void updatePaymentStatus(Payment payment, PaymentStatus newStatus){
    if (!validStatusTransition(payment.getPaymentStatus(), newStatus))
      throw new BadRequestException("invalid_status", String.format("Invalid status transition from %s to %s", payment.getPaymentStatus().name(), newStatus.name()));

    payment.setPaymentStatus(newStatus);
    payment.setLastUpdated(Tools.getCurrentDate());
  }

  private boolean validStatusTransition(PaymentStatus currentStatus, PaymentStatus newStatus) {
    boolean response = false;
    switch (currentStatus) {
      case PENDING -> response = List.of(PaymentStatus.PENDING, PaymentStatus.PAID, PaymentStatus.EXPIRED, PaymentStatus.CANCELED).contains(newStatus);
      case PAID -> response = List.of(PaymentStatus.PAID, PaymentStatus.CANCELED).contains(newStatus);
      case EXPIRED -> response = List.of(PaymentStatus.EXPIRED).contains(newStatus);
      case CANCELED -> response = List.of(PaymentStatus.CANCELED).contains(newStatus);
    }

    return response;
  }

}
