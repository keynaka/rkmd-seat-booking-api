package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.email.EmailDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class JavaMailSenderImpl extends AbstractMailingService{

    /** Ruta al archivo que contiene el template para enviar un mail de reserva provisoria con pago en efectivo. */
    public static final String RESERVATION_CASH_TEMPLATE_PATH = "/mailing/templates/text/reservation-cash-template.txt";

    /** Ruta al archivo que contiene el template para enviar un mail de reserva provisoria con pago por Mercado Pago. */
    public static final String RESERVATION_MP_TEMPLATE_PATH = "/mailing/templates/text/reservation-mp-template.txt";

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private String RESERVATION_CASH_BODY_TEMPLATE = null;

    private String RESERVATION_MP_BODY_TEMPLATE = null;

    public JavaMailSenderImpl(){
        RESERVATION_CASH_BODY_TEMPLATE = super.readMailTemplate(RESERVATION_CASH_TEMPLATE_PATH);
        RESERVATION_MP_BODY_TEMPLATE = super.readMailTemplate(RESERVATION_MP_TEMPLATE_PATH);
    }

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

    public String notifyReservation(String recipient, String name, String lastname, String bookingCode,
                                    PaymentMethod paymentMethod, ZonedDateTime expirationTime,
                                    List<SeatDto> seats){

        String msgBody = null;

        switch (paymentMethod){
            case MERCADO_PAGO -> {
                msgBody = RESERVATION_MP_BODY_TEMPLATE;
                msgBody = msgBody.replace("${MP_ACCOUNT}", super.getMercadoPagoAccount());
            }
            default -> msgBody = RESERVATION_CASH_BODY_TEMPLATE;
        }

        msgBody = msgBody.replace("${NAME}", name);
        msgBody = msgBody.replace("${LASTNAME}", lastname);
        msgBody = msgBody.replace("${BOOKING_CODE}", bookingCode);
        msgBody = msgBody.replace("${RESERVATION_EXPIRATION}", Tools.formatArgentinianDate(expirationTime));

        Map<String, String> reservationData = getReservationData(seats);
        msgBody = msgBody.replace("${RESERVED_SEATS}", reservationData.get("seats"));
        msgBody = msgBody.replace("${TOTAL_AMOUNT}", "$" + reservationData.get("totalAmount"));


        return sendSimpleMail(new EmailDto(recipient, msgBody, RESERVATION_SUBJECT));
    }

}
