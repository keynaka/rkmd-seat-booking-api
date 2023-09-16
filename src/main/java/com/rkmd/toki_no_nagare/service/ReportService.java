package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationServiceFactory;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {
    @Autowired
    private ExpirationServiceFactory expirationServiceFactory;

    public static final String PRE_EXPIRED = "PRE-EXPIRED";

    public String formatTitle(Booking booking) {
        String result =  String.format(
                "%s %s - Sector: %s - Precio: $%s",
                booking.getClient().getName(), booking.getClient().getLastName(),
                booking.getSeats() != null && !booking.getSeats().isEmpty() ? booking.getSeats().get(0).getSector().name() : "-",
                booking.getPayment().getAmount().toString()
        );

        if (isPreExpiredBooking(booking)) {
            Long adminExtraDays = expirationServiceFactory.getExpirationService(booking.getPayment().getPaymentMethod()).adminExpireExtraDays();
            result = String.format("(EXPIRES: %s) %s ", Tools.formatArgentinianDate(booking.getExpirationDate().plusDays(adminExtraDays)), result);
        }

        return result;
    }

    public String calculateStatus(Booking booking) {
        return isPreExpiredBooking(booking) ? PRE_EXPIRED : booking.getStatus().name();
    }

    private boolean isPreExpiredBooking(Booking booking) {
        ExpirationService expirationService = expirationServiceFactory.getExpirationService(booking.getPayment().getPaymentMethod());

        return booking.getStatus().equals(BookingStatus.PENDING) &&
                expirationService.isExpiredForClient(booking.getExpirationDate());
    }
}
