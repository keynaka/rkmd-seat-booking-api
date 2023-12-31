package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;


@Service
public class CashExpirationService extends ExpirationService{
    public static final long ADMIN_EXTRA_DAYS = 2l;
    @Override
    public Long adminExpireExtraDays() {
        return ADMIN_EXTRA_DAYS;
    }

    /**
     * This method calculates the expirationDate depending on the current date. If the current date is wednesday, thursday,
     * friday then it will recommend the next saturday. Else it will recommend this saturday.
     * It will also set the hour and minute to a fixed targetTime
     * E.g.
     *   Fixed hour = 23
     *   Fixed minute = 50
     *   dateCreated = (Saturday) 02/09/2023 - X hours and Y minutes  (X and Y does not matter)
     *   ------------
     *   result = (Saturday) 09/09/2023 - 23:50
     * E.g. 2
     *   dateCreated = (Wednesday) 06/09/2023 - X hours and Y minutes  (X and Y does not matter)
     *   ------------
     *   result = (Saturday) 16/09/2023 - 23:50
     * */
    @Override
    public ZonedDateTime getExpirationDate(ZonedDateTime dateCreated) {
        ZonedDateTime fixedDate = fixTargetTime(dateCreated);
        DayOfWeek currentDay = fixedDate.getDayOfWeek();

        ZonedDateTime expirationLimit = fixedDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        if (currentDay == DayOfWeek.WEDNESDAY || currentDay == DayOfWeek.THURSDAY || currentDay == DayOfWeek.FRIDAY || currentDay == DayOfWeek.SATURDAY)
            expirationLimit = expirationLimit.plusWeeks(1);

        return expirationLimit;
    }
}
