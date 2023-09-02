package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.email.EmailDto;
import com.rkmd.toki_no_nagare.dto.email.ImagesDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TransportMailSenderImpl extends AbstractMailingService{

    /** Ruta al archivo que contiene el template para enviar un e-mail de reserva provisoria con pago en efectivo en formato html. */
    public static final String RESERVATION_CASH_TEMPLATE_PATH = "/mailing/templates/html/reservation-cash-template.html";

    /** Ruta al archivo que contiene el template para enviar un e-mail de reserva provisoria con pago por Mercado Pago en formato html. */
    public static final String RESERVATION_MP_TEMPLATE_PATH = "/mailing/templates/html/reservation-mp-template.html";

    /** Ruta al archivo que contiene el template para enviar un e-mail de confirmación de pago en efectivo en formato html. */
    public static final String CONFIRMATION_CASH_TEMPLATE_PATH = "/mailing/templates/html/confirmation-cash-template.html";

    /** Ruta al archivo que contiene el template para enviar un e-mail de confirmación de pago por Mercado Pago en formato html. */
    public static final String CONFIRMATION_MP_TEMPLATE_PATH = "/mailing/templates/html/confirmation-mp-template.html";

    /** Ruta al archivo que contiene el template para enviar un e-mail de expiración de reserva provisoria en formato html. */
    public static final String EXPIRATION_TEMPLATE_PATH = "/mailing/templates/html/expiration-template.html";

    private String RESERVATION_CASH_BODY_TEMPLATE = null;

    private String RESERVATION_MP_BODY_TEMPLATE = null;

    private String CONFIRMATION_CASH_BODY_TEMPLATE = null;

    private String CONFIRMATION_MP_BODY_TEMPLATE = null;

    private String EXPIRATION_BODY_TEMPLATE = null;

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
        CONFIRMATION_CASH_BODY_TEMPLATE = super.readMailTemplate(CONFIRMATION_CASH_TEMPLATE_PATH);
        CONFIRMATION_MP_BODY_TEMPLATE = super.readMailTemplate(CONFIRMATION_MP_TEMPLATE_PATH);
        EXPIRATION_BODY_TEMPLATE = super.readMailTemplate(EXPIRATION_TEMPLATE_PATH);
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

    /** Envía un e-mail con contenido en formato texto. */
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

    /** Envía un e-mail con contenido en formato html. */
    public String sendHtmlEmail(EmailDto details) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(details.getRecipient()));
            message.setSubject(details.getSubject());

            // Asignar el contenido HTML al cuerpo del mensaje
            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            try {
                messageBodyPart.setContent(details.getHtmlBody(), "text/html");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            try {
                multipart.addBodyPart(messageBodyPart);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            // adds inline image attachments
            MimeBodyPart imagePart = new MimeBodyPart();
            try {
                imagePart.setHeader("Content-ID", details.getImagesDataMap().get("${HEADER_IMAGE_CODE}").getHeaderValue());
                imagePart.setDisposition(MimeBodyPart.INLINE);
                // attach the image file
                imagePart.attachFile(details.getImagesDataMap().get("${HEADER_IMAGE_CODE}").getAttachFilePath());
                multipart.addBodyPart(imagePart);
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }

            message.setContent(multipart);

            Transport.send(message);

            return "Mail Sent Successfully...";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail...";
        }
    }

    /** Selecciona el template html de e-mail de reserva y formatea el template con los datos específicos de la reserva. */
    public String notifyReservation(String recipient, String name, String lastname, String bookingCode,
                                    PaymentMethod paymentMethod, ZonedDateTime expirationTime,
                                    List<SeatDto> seats){

        // Agrego las imágenes
        Map<String, ImagesDto> imagesData = new HashMap<>();
        imagesData.put("${HEADER_IMAGE_CODE}", new ImagesDto("AbcXyz123", "./src/main/resources/mailing/images/toki-no-nagare-header-mail-2x.png"));

        String htmlBody = null;

        switch (paymentMethod){
            case MERCADO_PAGO -> {
                htmlBody = RESERVATION_MP_BODY_TEMPLATE;
                htmlBody = htmlBody.replace("${MP_ACCOUNT}", super.getMercadoPagoAccount());
            }
            default -> htmlBody = RESERVATION_CASH_BODY_TEMPLATE;
        }

        // Agrego imagen
        htmlBody = htmlBody.replace("${HEADER_IMAGE_CODE}", imagesData.get("${HEADER_IMAGE_CODE}").getHeaderValue());

        htmlBody = htmlBody.replace("${NAME}", name);
        htmlBody = htmlBody.replace("${LASTNAME}", lastname);
        htmlBody = htmlBody.replace("${EVENT_NAME}", eventName);
        htmlBody = htmlBody.replace("${EVENT_DATE}", eventDate);
        htmlBody = htmlBody.replace("${EVENT_TIME}", eventTime);
        htmlBody = htmlBody.replace("${EVENT_EVENT_PLACE}", eventPlace);
        htmlBody = htmlBody.replace("${EVENT_ADDRESS}", eventAddress);
        htmlBody = htmlBody.replace("${BOOKING_CODE}", bookingCode);
        htmlBody = htmlBody.replace("${RESERVATION_EXPIRATION}", Tools.formatArgentinianDate(expirationTime));

        Map<String, String> reservationData = getReservationData(seats);
        htmlBody = htmlBody.replace("${RESERVED_SEATS}", buildSeatsList(reservationData.get("seats")));
        htmlBody = htmlBody.replace("${TOTAL_AMOUNT}", "$" + reservationData.get("totalAmount"));

        return sendHtmlEmail(new EmailDto(recipient, htmlBody, RESERVATION_SUBJECT, imagesData));
    }

    /** Utiliza el template html de e-mail de confirmación de reserva y formatea el template con los datos específicos de la reserva. */
    public String notifyConfirmation(String recipient, String name, String lastname, String bookingCode,
                                     PaymentMethod paymentMethod, ZonedDateTime expirationTime,
                                     List<SeatDto> seats){

        // Agrego las imágenes
        Map<String, ImagesDto> imagesData = new HashMap<>();
        imagesData.put("${HEADER_IMAGE_CODE}", new ImagesDto("AbcXyz123", "./src/main/resources/mailing/images/toki-no-nagare-header-mail-2x.png"));

        String htmlBody = null;

        switch (paymentMethod){
            case MERCADO_PAGO -> {
                htmlBody = CONFIRMATION_MP_BODY_TEMPLATE;
                htmlBody = htmlBody.replace("${MP_ACCOUNT}", super.getMercadoPagoAccount());
            }
            default -> htmlBody = CONFIRMATION_CASH_BODY_TEMPLATE;
        }

        // Agrego imagen
        htmlBody = htmlBody.replace("${HEADER_IMAGE_CODE}", imagesData.get("${HEADER_IMAGE_CODE}").getHeaderValue());

        htmlBody = htmlBody.replace("${NAME}", name);
        htmlBody = htmlBody.replace("${LASTNAME}", lastname);
        htmlBody = htmlBody.replace("${EVENT_NAME}", eventName);
        htmlBody = htmlBody.replace("${EVENT_DATE}", eventDate);
        htmlBody = htmlBody.replace("${EVENT_TIME}", eventTime);
        htmlBody = htmlBody.replace("${EVENT_EVENT_PLACE}", eventPlace);
        htmlBody = htmlBody.replace("${EVENT_ADDRESS}", eventAddress);
        htmlBody = htmlBody.replace("${BOOKING_CODE}", bookingCode);
        htmlBody = htmlBody.replace("${RESERVATION_EXPIRATION}", Tools.formatArgentinianDate(expirationTime));

        Map<String, String> reservationData = getReservationData(seats);
        htmlBody = htmlBody.replace("${RESERVED_SEATS}", buildSeatsList(reservationData.get("seats")));
        htmlBody = htmlBody.replace("${TOTAL_AMOUNT}", "$" + reservationData.get("totalAmount"));

        return sendHtmlEmail(new EmailDto(recipient, htmlBody, CONFIRMATION_SUBJECT, imagesData));
    }

    @Override
    public String notifyExpiration(String recipient, String name, String lastname, String bookingCode, ZonedDateTime expirationTime) {
        // Agrego las imágenes
        Map<String, ImagesDto> imagesData = new HashMap<>();
        imagesData.put("${HEADER_IMAGE_CODE}", new ImagesDto("AbcXyz123", "./src/main/resources/mailing/images/toki-no-nagare-header-mail-2x.png"));

        String htmlBody = EXPIRATION_BODY_TEMPLATE;

        // Agrego imagen
        htmlBody = htmlBody.replace("${HEADER_IMAGE_CODE}", imagesData.get("${HEADER_IMAGE_CODE}").getHeaderValue());

        htmlBody = htmlBody.replace("${NAME}", name);
        htmlBody = htmlBody.replace("${LASTNAME}", lastname);
        htmlBody = htmlBody.replace("${EVENT_NAME}", eventName);
        htmlBody = htmlBody.replace("${BOOKING_CODE}", bookingCode);
        htmlBody = htmlBody.replace("${RESERVATION_EXPIRATION}", Tools.formatArgentinianDate(expirationTime));


        return sendHtmlEmail(new EmailDto(recipient, htmlBody, EXPIRATION_SUBJECT, imagesData));
    }
}
