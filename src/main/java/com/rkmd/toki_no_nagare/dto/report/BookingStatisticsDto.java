package com.rkmd.toki_no_nagare.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class BookingStatisticsDto {

  private Map<String, String> general;
  private Map<String, String> pullman;
  private Map<String, String> platea;
  private List<RecentSalesDto> recentSales;

}