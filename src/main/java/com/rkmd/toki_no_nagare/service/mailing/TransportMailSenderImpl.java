package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.email.EmailDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class TransportMailSenderImpl implements IMailingService{

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

    private String RESERVATION_BODY_HTML_TEMPLATE =
            "<!DOCTYPE html>" +
                    "<html>" +
                    "    <head>" +
                    "      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                    "    </head>" +
                    "    <body>" +
                    "      <h1 style='color: #3498db;'>Hola ${NAME} ${LASTNAME}!</h1>" +
                    "      <p style='font-size: 16px;'>Te agradecemos por haber realizado una reserva para nuestro show.</p>" +
                    "      <p style='font-size: 16px; font-weight: bold;'>Tu número de reserva es: ${BOOKING_CODE}</p>" +
                    "      <p style='font-size: 16px;'>${PAYMENT_METHOD_TEXT}</p>" +
                    "      <p style='font-size: 16px;'>" +
                    "        <em>¡Muchas gracias por confiar en nosotros!</em> <br />" +
                    "        <img src='cid:logo-min.png' alt='Imagen de logo'> <br />" +
                    "      </p>" +
                    "    </body>" +
                    "</html>"
            ;

    private String RESERVATION_PAYMENT_METHOD_MERCADO_PAGO_TEXT = "Nuestra cuenta de Mercado Pago es pepito1234NoSeSiSonNumerosOLetras.\n" +
            "Una vez realizado el pago, te pedimos que nos respondas a este mail con el comprobante de pago.\n";
    private String RESERVATION_PAYMENT_METHOD_CASH_TEXT = "Matsuridaiko se contactará con vos para coordinar el pago y entrega de entradas.";

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
            message.setContent(details.getHtmlContent(), "text/html; charset=utf-8");

            Transport.send(message);

            return "Mail Sent Successfully...";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail...";
        }
    }

    @Override
    public String notifyReservation(String recipient, String name, String lastname, String bookingCode, PaymentMethod paymentMethod) {

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

        msgBody = msgBody.replace("${PAYMENT_METHOD_TEXT}", paymentMethodText);

        EmailDto emailDto = new EmailDto(recipient, msgBody, RESERVATION_SUBJECT);


//        try {
//            String logMinPng = new String(Files.readAllBytes(Paths.get("./resources/mailing/images/")));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        String htmlContent = RESERVATION_BODY_HTML_TEMPLATE;
        htmlContent = htmlContent.replace("${NAME}", name);
        htmlContent = htmlContent.replace("${LASTNAME}", lastname);
        htmlContent = htmlContent.replace("${BOOKING_CODE}", bookingCode);
        htmlContent = htmlContent.replace("${PAYMENT_METHOD_TEXT}", paymentMethodText);

        emailDto.setHtmlContent(htmlContent);

        return sendHtmlEmail(emailDto);
    }
}
