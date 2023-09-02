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
    public void testGetExpirationDateOnSaturday() {
        //This is a Saturday
        ZonedDateTime dateCreated = ZonedDateTime.of(2023, 9, 2, 12, 0, 0, 0, ZoneId.systemDefault());

        ZonedDateTime expirationDate = cashExpirationService.getExpirationDate(dateCreated);

        ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 9, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
        Assertions.assertEquals(expectedExpirationDate, expirationDate);
    }

    @Test
    public void testGetExpirationDateOnWednesday() {
        //This is a Wednesday
        ZonedDateTime dateCreated = ZonedDateTime.of(2023, 9, 6, 12, 0, 0, 0, ZoneId.systemDefault());

        ZonedDateTime expirationDate = cashExpirationService.getExpirationDate(dateCreated);

        ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 16, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
        Assertions.assertEquals(expectedExpirationDate, expirationDate);
    }
}
