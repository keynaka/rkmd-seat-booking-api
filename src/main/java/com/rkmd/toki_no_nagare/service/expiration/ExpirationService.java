package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.utils.Tools;

import java.time.LocalTime;
import java.time.ZonedDateTime;

public abstract class ExpirationService {
    // These FIXED_LIMIT_HOUR AND FIXED_LIMIT_MINUTE must be set before the ExpirationJob's cron
    private static final int FIXED_LIMIT_HOUR = 23;
    private static final int FIXED_LIMIT_MINUTE = 50;

    protected abstract Long adminExpireExtraDays();
    public abstract ZonedDateTime getExpirationDate(ZonedDateTime dateCreated);

    public boolean isExpiredForClient(ZonedDateTime date) {
        return Tools.getCurrentDate().isAfter(date);
    }
    public boolean isExpiredForAdmin(ZonedDateTime date) {
        return Tools.getCurrentDate().isAfter(date.plusDays(adminExpireExtraDays()));
    }

    /**
    * This method creates a new ZonedDateTime with the same date but the target time
    * */
    protected ZonedDateTime fixTargetTime(ZonedDateTime date) {
        // Uncomment for testing: To simulate the expiration date to 2 minutes later. Remember to edit the getExpirationDate() and adminExpireExtraDays() too
        // LocalTime targetTime = LocalTime.of(Tools.getCurrentDate().getHour(), Tools.getCurrentDate().plusMinutes(2).getMinute());
        LocalTime targetTime = LocalTime.of(FIXED_LIMIT_HOUR, FIXED_LIMIT_MINUTE);

        return date.with(targetTime);
    }
}
