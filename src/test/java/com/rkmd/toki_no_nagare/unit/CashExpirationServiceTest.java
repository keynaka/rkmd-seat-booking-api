package com.rkmd.toki_no_nagare.unit;

import com.rkmd.toki_no_nagare.service.expiration.CashExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CashExpirationServiceTest {
    private CashExpirationService cashExpirationService = new CashExpirationService();

    @Test
    public void testGetExpirationDateOnSaturdaySundayMondayTuesday() {
        ZonedDateTime dateCreated;
        ZonedDateTime expirationDate;
        // 02-09-2023 is Saturday
        for (int i = 0 ; i < 5 ; i++) {
            dateCreated = ZonedDateTime.of(2023, 9, 2 + i, 12, 0, 0, 0, ZoneId.systemDefault());

            expirationDate = cashExpirationService.getExpirationDate(dateCreated);

            if (i < 4) {
                ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 9, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
                Assertions.assertEquals(expectedExpirationDate, expirationDate);
            } else {
                // This case is when it is 06-09-2023 (Wednesday)
                ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 16, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
                Assertions.assertEquals(expectedExpirationDate, expirationDate);
            }
        }
    }

    @Test
    public void testGetExpirationDateOnWednesdayThursdayFriday() {
        ZonedDateTime dateCreated;
        ZonedDateTime expirationDate;
        // 06-09-2023 is Wednesday
        for (int i = 0 ; i < 5 ; i++) {
            dateCreated = ZonedDateTime.of(2023, 9, 6 + i, 12, 0, 0, 0, ZoneId.systemDefault());

            expirationDate = cashExpirationService.getExpirationDate(dateCreated);

            ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 16, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
            Assertions.assertEquals(expectedExpirationDate, expirationDate);
        }
    }

    @Test
    public void testGetExpirationDateShouldBeSameDayFromWednesdayToNextWeekFriday() {
        ZonedDateTime dateCreated;
        ZonedDateTime expirationDate;
        // 06-09-2023 is Wednesday
        for (int i = 0 ; i < 8 ; i++) {
            dateCreated = ZonedDateTime.of(2023, 9, 6 + i, 12, 0, 0, 0, ZoneId.systemDefault());

            expirationDate = cashExpirationService.getExpirationDate(dateCreated);

            if (i < 7) {
                ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 16, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
                Assertions.assertEquals(expectedExpirationDate, expirationDate);
            } else {
                // This case is when it is 13-09-2023 (Saturday)
                ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 23, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
                Assertions.assertEquals(expectedExpirationDate, expirationDate);
            }
        }
    }
}
