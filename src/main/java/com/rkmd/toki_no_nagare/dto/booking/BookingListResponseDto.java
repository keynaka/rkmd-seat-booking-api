package com.rkmd.toki_no_nagare.dto.booking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingListResponseDto {
    private String bookingCode;
    private Long dni;
    private String title;
    private String status;
    private String paymentMethod;
}
