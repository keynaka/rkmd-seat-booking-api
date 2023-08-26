package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;

public interface IMailingService {

    String notifyReservation(String recipient, String name, String lastname, String bookingCode, PaymentMethod paymentMethod);
}
