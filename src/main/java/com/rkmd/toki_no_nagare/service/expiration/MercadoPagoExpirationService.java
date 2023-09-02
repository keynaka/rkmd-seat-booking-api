package com.rkmd.toki_no_nagare.service.expiration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    /**
    * This method calculates the expirationDate by setting the Hour and minute to a fixed TargetTime and then
    * adding expirationLimit days.
    * E.g.
    *   Fixed hour = 22
    *   Fixed minute = 45
    *   expirationLimit = 7
    *   dateCreated = 02/09/2023 - X hours and Y minutes  (X and Y does not matter)
    *   ----------------------
    *   result = 09/09/2023 - 22:45
    * */
    @Override
    public ZonedDateTime getExpirationDate(ZonedDateTime dateCreated) {
        ZonedDateTime fixedCreationDate = fixTargetTime(dateCreated);

        return fixedCreationDate.plusDays(expirationLimit);
    }
}
