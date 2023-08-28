package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.email.EmailDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class TransportMailSenderImpl extends AbstractMailingService{

    /** Ruta al archivo que contiene el template para enviar un mail de reserva provisoria con pago en efectivo en formato html. */
    public static final String RESERVATION_CASH_TEMPLATE_PATH = "/mailing/templates/html/reservation-cash-template.html";

    /** Ruta al archivo que contiene el template para enviar un mail de reserva provisoria con pago por Mercado Pago en formato html. */
    public static final String RESERVATION_MP_TEMPLATE_PATH = "/mailing/templates/html/reservation-mp-template.html";

    private String RESERVATION_CASH_BODY_TEMPLATE = null;

    private String RESERVATION_MP_BODY_TEMPLATE = null;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private Properties props;
    private Session session;

    public TransportMailSenderImpl(){
        RESERVATION_CASH_BODY_TEMPLATE = super.readMailTemplate(RESERVATION_CASH_TEMPLATE_PATH);
        RESERVATION_MP_BODY_TEMPLATE = super.readMailTemplate(RESERVATION_MP_TEMPLATE_PATH);
    }

    @PostConstruct
    public void setup(){
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public String sendSimpleMail(EmailDto details) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(details.getRecipient()));
            message.setSubject(details.getSubject());
            message.setText(details.getMsgBody());

            Transport.send(message);

            return "Mail Sent Successfully...";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail...";
        }
    }

    public String sendHtmlEmail(EmailDto details) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(details.getRecipient()));
            message.setSubject(details.getSubject());

//            String htmlContent = "<html><body><h1 style='color: #3498db;'>¡Hola!</h1>"
//                    + "<p style='font-size: 16px;'>Este es un correo de ejemplo enviado desde JavaMail.</p>"
//                    + "<img src='https://ejemplo.com/imagen.png' alt='Imagen de ejemplo'>"
//                    + "</body></html>";

            // Asignar el contenido HTML al cuerpo del mensaje
            message.setContent(details.getHtmlBody(), "text/html; charset=utf-8");

            Transport.send(message);

            return "Mail Sent Successfully...";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail...";
        }
    }

    public String notifyReservation(String recipient, String name, String lastname, String bookingCode,
                                    PaymentMethod paymentMethod, ZonedDateTime expirationTime,
                                    List<SeatDto> seats){

        String htmlBody = null;

        switch (paymentMethod){
            case MERCADO_PAGO -> {
                htmlBody = RESERVATION_MP_BODY_TEMPLATE;
                htmlBody = htmlBody.replace("${MP_ACCOUNT}", super.getMercadoPagoAccount());
            }
            default -> htmlBody = RESERVATION_CASH_BODY_TEMPLATE;
        }

        htmlBody = htmlBody.replace("${NAME}", name);
        htmlBody = htmlBody.replace("${LASTNAME}", lastname);
        htmlBody = htmlBody.replace("${EVENT_NAME}", eventName);
        htmlBody = htmlBody.replace("${EVENT_DATE_TIME}", eventDateTime);
        htmlBody = htmlBody.replace("${EVENT_EVENT_PLACE}", eventPlace);
        htmlBody = htmlBody.replace("${EVENT_ADDRESS}", eventAddress);
        htmlBody = htmlBody.replace("${BOOKING_CODE}", bookingCode);
        htmlBody = htmlBody.replace("${RESERVATION_EXPIRATION}", Tools.formatArgentinianDate(expirationTime));

        Map<String, String> reservationData = getReservationData(seats);
        htmlBody = htmlBody.replace("${RESERVED_SEATS}", buildSeatsList(reservationData.get("seats")));
        htmlBody = htmlBody.replace("${TOTAL_AMOUNT}", "$" + reservationData.get("totalAmount"));

        return sendHtmlEmail(new EmailDto(recipient, htmlBody, RESERVATION_SUBJECT, null));
    }

}
