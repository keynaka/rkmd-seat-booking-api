package com.rkmd.toki_no_nagare.service.expiration;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class MercadoPagoExpirationService extends ExpirationService{
    @Override
    protected ZonedDateTime getExpirationDate() {
        return null;
    }

    @Override
    protected Integer getExpirationLimit() {
        return null;
    }

}
