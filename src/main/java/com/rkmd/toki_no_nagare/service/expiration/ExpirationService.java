package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.utils.Tools;

import java.time.LocalTime;
import java.time.ZonedDateTime;

public abstract class ExpirationService {
    /* Este horario es importante que sea menor a las 21 hrs, asi cuando se guarda en la BD que lo convierte a UTC no queda en el dia siguiente
    *  Mas que nada para no marear a los admins.
    *  Con eso con el job que corre a las 00:00 GMT-3 va a expirarlas.
    *  Ejemplo:
    *      - Un pago vence el 12/09/2023. O sea el 14/09/2023 a las 20:50 GMT-3 sumandole las 48hs para admin
    *      - El job que corre el 14/09/2023 a las 00:00 GMT-3, NO lo expiraria porque:
    *                14/09/2023 a las 00:00 GMT-3 es menor a 14/09/2023 a las 20:50 GMT-3
    *      - El job que corre el 15/09/2023 a las 00:00 GMT-3, SI lo expiraria porque:
    *                15/09/2023 a las 00:00 GMT-3 es mayor a 14/09/2023 a las 20:50 GMT-3
     */
    public static final int FIXED_LIMIT_HOUR = 20;
    public static final int FIXED_LIMIT_MINUTE = 50;

    public abstract Long adminExpireExtraDays();
    public abstract ZonedDateTime getExpirationDate(ZonedDateTime dateCreated);

    public boolean isExpiredForClient(ZonedDateTime date) {
        return Tools.getCurrentDate().isAfter(date);
    }
    public boolean isExpiredForAdmin(ZonedDateTime date) {
        return Tools.getCurrentDate().isAfter(date.plusDays(adminExpireExtraDays()));
    }

    /**
    * This method creates a new ZonedDateTime with the same date but the target time
    * */
    public ZonedDateTime fixTargetTime(ZonedDateTime date) {
        // Uncomment for testing: To simulate the expiration date to 2 minutes later. Remember to edit the getExpirationDate() and adminExpireExtraDays() too
        // LocalTime targetTime = LocalTime.of(Tools.getCurrentDate().getHour(), Tools.getCurrentDate().plusMinutes(2).getMinute());
        LocalTime targetTime = LocalTime.of(FIXED_LIMIT_HOUR, FIXED_LIMIT_MINUTE);

        return date.with(targetTime);
    }
}
