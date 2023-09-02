package com.rkmd.toki_no_nagare.unit;

import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.MercadoPagoExpirationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
public class MercadoPagoExpirationServiceTest {
    private MercadoPagoExpirationService mercadoPagoExpirationService = new MercadoPagoExpirationService();

    @Test
    public void testGetExpirationDate() {
        mercadoPagoExpirationService.setExpirationLimit(7l);
        ZonedDateTime dateCreated = ZonedDateTime.of(2023, 9, 2, 12, 0, 0, 0, ZoneId.systemDefault());

        ZonedDateTime expirationDate = mercadoPagoExpirationService.getExpirationDate(dateCreated);

        ZonedDateTime expectedExpirationDate = ZonedDateTime.of(2023, 9, 9, ExpirationService.FIXED_LIMIT_HOUR, ExpirationService.FIXED_LIMIT_MINUTE, 0, 0, ZoneId.systemDefault());
        Assertions.assertEquals(expectedExpirationDate, expirationDate);
    }
}

