package com.rkmd.toki_no_nagare.job;

import com.rkmd.toki_no_nagare.service.BookingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ExpirationJob {

    @Autowired
    private BookingService bookingService;

    //@Scheduled(cron = "0 0 0 * * *") // Everyday at 00:00
    @Scheduled(cron = "0 * * * * *") // Every minute for testing
    public void expirateExpiredBookings() {
        log.info("[ExpirationJob] - Automatic expiration job started ...");

        log.info(String.format("[ExpirationJob] - Automatic expiration job ended with %d booking expired."));
    }
}
