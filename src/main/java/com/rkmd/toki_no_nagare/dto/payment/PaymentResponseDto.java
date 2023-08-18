package com.rkmd.toki_no_nagare.dto.payment;

import com.rkmd.toki_no_nagare.entities.payment.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "Listado de todos los pagos registrados sin importar su estado.")
public class PaymentResponseDto {

  private List<Payment> payments;

  public PaymentResponseDto(List<Payment> paymentList){
    this.payments = paymentList;
  }

}
