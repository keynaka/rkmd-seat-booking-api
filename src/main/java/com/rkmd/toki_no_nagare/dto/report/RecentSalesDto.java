package com.rkmd.toki_no_nagare.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecentSalesDto {

  private String bookingCode;
  private String date;
  private String paymentMethod;
  private String amount;

}
