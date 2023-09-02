package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.utils.Tools;

import java.time.LocalTime;
import java.time.ZonedDateTime;

public abstract class ExpirationService {
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

    protected ZonedDateTime fixTargetTime(ZonedDateTime date) {
        LocalTime targetTime = LocalTime.of(FIXED_LIMIT_HOUR, FIXED_LIMIT_MINUTE);
        // Create a new ZonedDateTime with the same date but the target time
        return date.with(targetTime);
    }
}
