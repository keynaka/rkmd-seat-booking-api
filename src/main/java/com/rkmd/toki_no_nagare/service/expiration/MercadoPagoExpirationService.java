package com.rkmd.toki_no_nagare.service.expiration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZonedDateTime;


@Service
public class MercadoPagoExpirationService extends ExpirationService{
    public static final long ADMIN_EXTRA_DAYS = 2l;
    @Value("${paymentTimeLimitFor.mercadoPago}")
    private Long expirationLimit;

    @Override
    protected Long adminExpireExtraDays() {
        return ADMIN_EXTRA_DAYS;
    }
    @Override
    public ZonedDateTime getExpirationDate(ZonedDateTime dateCreated) {
        ZonedDateTime fixedCreationDate = fixTargetTime(dateCreated);

        return fixedCreationDate.plusDays(expirationLimit);
    }
}
