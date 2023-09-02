package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;


@Service
public class CashExpirationService extends ExpirationService{
    public static final long ADMIN_EXTRA_DAYS = 1l;
    @Override
    protected Long adminExpireExtraDays() {
        return ADMIN_EXTRA_DAYS;
    }
    @Override
    public ZonedDateTime getExpirationDate(ZonedDateTime dateCreated) {
        ZonedDateTime fixedDate = fixTargetTime(dateCreated);
        DayOfWeek diaActual = Tools.getCurrentDate().getDayOfWeek();

        ZonedDateTime expirationLimit = fixedDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        if (diaActual == DayOfWeek.WEDNESDAY || diaActual == DayOfWeek.THURSDAY || diaActual == DayOfWeek.FRIDAY)
            expirationLimit = expirationLimit.plusWeeks(1);

        return expirationLimit;
    }
}
