package com.rkmd.toki_no_nagare.service.expiration;

import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExpirationServiceFactory {
    @Autowired
    private CashExpirationService cashExpirationService;
    @Autowired
    private MercadoPagoExpirationService mercadoPagoExpirationService;

    public ExpirationService getExpirationService(PaymentMethod paymentMethod) {
        return paymentMethod == null || paymentMethod.equals(PaymentMethod.CASH) ?
                cashExpirationService : mercadoPagoExpirationService;
    }
}
