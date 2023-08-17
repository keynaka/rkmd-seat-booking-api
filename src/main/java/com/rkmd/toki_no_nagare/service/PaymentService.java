package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.repositories.PaymentRepository;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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

}
