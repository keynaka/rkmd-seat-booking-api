package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.email.EmailDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailSenderImpl implements IMailingService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private String RESERVATION_SUBJECT = "Toki no Nagare - Tenés una reserva pendiente de pago.";

    private String RESERVATION_BODY_TEMPLATE =
            "Hola {NAME} {LASTNAME}! \n" +
                    "\n" +
                    "Te agradecemos por haber realizado una reserva para nuestro show. \n" +
                    "\n" +
                    "Tu número de reserva es: {BOOKING_CODE}\n" +
                    "\n" +
                    "{PAYMENT_METHOD_TEXT}" +
                    "\n" +
                    "¡Muchas gracias por confiar en nosotros!";

    private String RESERVATION_PAYMENT_METHOD_MERCADO_PAGO_TEXT = "Nuestra cuenta de Mercado Pago es pepito1234NoSeSiSonNumerosOLetras.\n" +
            "Una vez realizado el pago, te pedimos que nos respondas a este mail con el comprobante de pago.\n";
    private String RESERVATION_PAYMENT_METHOD_CASH_TEXT = "Matsuridaiko se contactará con vos para coordinar el pago y entrega de entradas.";

    /** Envía un email solo con texto. */
    public String sendSimpleMail(EmailDto details)
    {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());
            mailMessage.setText(details.getMsgBody());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail: " + e.getMessage();
        }
    }

    public String notifyReservation(String recipient, String name, String lastname, String bookingCode, PaymentMethod paymentMethod){

        String msgBody = RESERVATION_BODY_TEMPLATE;
        msgBody = msgBody.replace("{NAME}", name);
        msgBody = msgBody.replace("{LASTNAME}", lastname);
        msgBody = msgBody.replace("{BOOKING_CODE}", bookingCode);

        // Agregar

        String paymentMethodText = "";

        switch (paymentMethod){
            case MERCADO_PAGO -> paymentMethodText = RESERVATION_PAYMENT_METHOD_MERCADO_PAGO_TEXT;
            default -> paymentMethodText = RESERVATION_PAYMENT_METHOD_CASH_TEXT;
        }

        msgBody = msgBody.replace("{PAYMENT_METHOD_TEXT}", paymentMethodText);

        return sendSimpleMail(new EmailDto(recipient, msgBody, RESERVATION_SUBJECT));
    }

}
