package com.rkmd.toki_no_nagare.service.expiration;

import java.time.ZonedDateTime;

public abstract class ExpirationService {
    protected abstract ZonedDateTime getExpirationDate();

    protected abstract Integer getExpirationLimit();
    protected boolean isExpired() {
        return true; //TODO: Here the expiration logic
    }
}
