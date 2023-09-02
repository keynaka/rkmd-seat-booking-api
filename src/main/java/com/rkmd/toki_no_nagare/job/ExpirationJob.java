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

@Component
@Log4j2
public class ExpirationJob {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ExpirationServiceFactory expirationServiceFactory;

    //@Scheduled(cron = "0 0 0 * * *") // Everyday at 00:00
    @Scheduled(cron = "0 * * * * *") // Every minute for testing
    public void expirateExpiredBookings() {
        log.info("[ExpirationJob] - Automatic expiration job started ...");

        PaymentResponseDto pendingPayments = paymentService.getPaymentsByStatus(PaymentStatus.PENDING);

        List<Payment> expiredPayments = new ArrayList<>();
        for (Payment payment : pendingPayments.getPayments()) {
            ExpirationService expirationService = expirationServiceFactory.getExpirationService(payment.getPaymentMethod());
            if (expirationService.isExpiredForAdmin(payment.getExpirationDate())) expiredPayments.add(payment);
        }

        log.info(String.format("[ExpirationJob] - Automatic expiration job ended with %d booking expired.", expiredPayments.size()));
    }
}
