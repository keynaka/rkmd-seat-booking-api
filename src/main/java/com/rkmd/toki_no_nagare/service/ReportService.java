package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReportService {
    @Autowired
    private ExpirationServiceFactory expirationServiceFactory;

    public static final String PRE_EXPIRED = "PRE-EXPIRED";

    public String calculateStatus(Booking booking) {
        return isPreExpiredBooking(booking) ? PRE_EXPIRED : booking.getStatus().name();
    }

    private boolean isPreExpiredBooking(Booking booking) {
        ExpirationService expirationService = expirationServiceFactory.getExpirationService(booking.getPayment().getPaymentMethod());

        return booking.getStatus().equals(BookingStatus.PENDING) &&
                expirationService.isExpiredForClient(booking.getExpirationDate()) &&
                !expirationService.isExpiredForAdmin(booking.getExpirationDate());
    }
}
