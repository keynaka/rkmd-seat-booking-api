package com.rkmd.toki_no_nagare.job;

import com.rkmd.toki_no_nagare.dto.payment.PaymentResponseDto;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.service.PaymentService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationServiceFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class ExpirationJob {
    public static final String AUTOMATIC_EXPIRATION_JOB = "AUTOMATIC_EXPIRATION_JOB";
    public static String SEPARATOR = ", ";
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ExpirationServiceFactory expirationServiceFactory;

    // These cron must be set after the FIXED_LIMIT_HOUR AND FIXED_LIMIT_MINUTE of ExpirationService
    // "0 * * * * *" // Every minute for testing
    // "0 */30 * * * *" // Every 30 minutes for testing
    // "0 0 0 * * *" // Every day at 00:00 PRODUCTIVE

    @Scheduled(cron = "${JOB_CRON}")
    public void expirateExpiredBookings() {
        PaymentResponseDto pendingPayments = paymentService.getPaymentsByStatus(PaymentStatus.PENDING);

        List<Payment> expiredPayments = new ArrayList<>();
        for (Payment payment : pendingPayments.getPayments()) {
            ExpirationService expirationService = expirationServiceFactory.getExpirationService(payment.getPaymentMethod());
            if (expirationService.isExpiredForAdmin(payment.getExpirationDate())) {
                expiredPayments.add(payment);
                // This step changes the payment status to EXPIRED
                paymentService.changePaymentStatus(payment.getBooking().getHashedBookingCode(), PaymentStatus.EXPIRED, AUTOMATIC_EXPIRATION_JOB);
            }
        }

        log.info(String.format("[ExpirationJob] - Automatic expiration job ended with %d bookings expired.", expiredPayments.size()));
        if (!expiredPayments.isEmpty()) {
            log.info(String.format(
                    "[ExpirationJob] - Expired bookings: %s",
                    expiredPayments
                            .stream()
                            .map(payment -> payment.getBooking().getHashedBookingCode())
                            .collect(Collectors.joining(SEPARATOR)))
            );
        }
    }
}
