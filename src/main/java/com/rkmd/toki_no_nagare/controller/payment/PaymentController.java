package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.BalanceResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentRequestDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.PaymentResponseDto;
import com.rkmd.toki_no_nagare.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class PaymentController implements PaymentControllerResources{

  @Autowired
  private PaymentService paymentService;

  @PutMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public ChangePaymentResponseDto changePaymentStatus(ChangePaymentRequestDto request) {
    return paymentService.changePaymentStatus(
        request.getBookingCode(),
        request.getContactDni(),
        request.getPaymentStatus()
    );
  }

  @GetMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public PaymentResponseDto getAllPayment() {
    return paymentService.getAllPayments();
  }

  @GetMapping(value = "/v1/payments/balance", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public BalanceResponseDto getBalance() {
    return paymentService.getBalance();
  }
}
