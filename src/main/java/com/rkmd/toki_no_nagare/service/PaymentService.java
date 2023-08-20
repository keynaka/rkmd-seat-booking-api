package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.PaymentRepository;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

  @Value("${paymentTimeLimitFor.mercadoPago}")
  private Long paymentTimeLimitForMercadoPago;

  @Value("${paymentTimeLimitFor.cash}")
  private Long paymentTimeLimitForCash;

  @Autowired
  private PaymentRepository paymentRepository;

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
    return (paymentMethod.equals(PaymentMethod.MERCADO_PAGO)) ?
        dateCreated.plusDays(paymentTimeLimitForMercadoPago) : dateCreated.plusDays(paymentTimeLimitForCash);
  }


  /** This method checks if a booking exists according to the 'contactDni' and 'bookingCode' passed as parameters.
   * If exists, updates the payment status passed as a parameter and change the booking status. If it doesn't exist,
   * it throws an exception.
   * @param bookingCode Booking code
   * @param contactDni User identity number
   * @param paymentStatus Payment status
   * @throws BadRequestException Throw BadRequestException is booking not exists
   * @return ChangePaymentResponseDto
   */
  public ChangePaymentResponseDto changePaymentStatus(String bookingCode, Long contactDni, PaymentStatus paymentStatus){
    String hashedBookingCode = Tools.generateHashCode(contactDni, bookingCode);
    List<Payment> payments = paymentRepository.findAll();
    Optional<Payment> payment = payments.stream().filter(p -> p.getBooking().getHashedBookingCode().equals(hashedBookingCode)).findFirst();

    if(payment.isEmpty()){
      throw new NotFoundException("booking_not_found", "The requested booking does not exist.");
    }

    // Change the payment status
    payment.get().setPaymentStatus(paymentStatus);
    payment.get().setLastUpdated(Tools.getCurrentDate());

    // Change the booking status
    switch (paymentStatus) {
      case PAID -> payment.get().getBooking().setStatus(BookingStatus.PAID);
      case CANCELED -> payment.get().getBooking().setStatus(BookingStatus.CANCELED);
      case EXPIRED -> payment.get().getBooking().setStatus(BookingStatus.EXPIRED);
    }
    payment.get().getBooking().setLastUpdated(Tools.getCurrentDate());

    // Change de seat status
    List<Seat> seats = payment.get().getBooking().getSeats();
    for(Seat seat : seats){
      if(paymentStatus.equals(PaymentStatus.PAID)){
        seat.setStatus(SeatStatus.OCCUPIED);
      } else {
        seat.setStatus(SeatStatus.VACANT);
      }
    }

    // Save changes
    BookingStatus finalStatus = paymentRepository.saveAndFlush(payment.get()).getBooking().getStatus();

    return new ChangePaymentResponseDto(bookingCode, finalStatus, "Modificación realizada exitosamente.");
  }


  /** This method returns all registered payments. * */
  public PaymentResponseDto getAllPayments(){
    List<Payment> payments = paymentRepository.findAll();
    return new PaymentResponseDto(payments);
  }


}
