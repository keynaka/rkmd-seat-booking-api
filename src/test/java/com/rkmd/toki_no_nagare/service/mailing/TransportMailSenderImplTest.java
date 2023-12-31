package com.rkmd.toki_no_nagare.service.mailing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class TransportMailSenderImplTest {

    @InjectMocks
    private TransportMailSenderImpl mailingService;

    @BeforeEach
    void setup() {
        mailingService = new TransportMailSenderImpl();
    }

    @Test
    void givenReservationCashTemplatePath_whenReadMailTemplate_thenThrowNoException(){

        // given - preconditions or setup
        String[] resultEmailTemplate = new String[1];

        // when - action or behaviour that we are going to test
        Assertions.assertDoesNotThrow( () -> resultEmailTemplate[0] = mailingService.readMailTemplate(mailingService.RESERVATION_CASH_TEMPLATE_PATH));

        // then - verify the output
        assertThat(resultEmailTemplate[0]).isNotNull();
        assertThat(resultEmailTemplate[0]).contains("Te agradecemos por haber realizado una reserva para nuestro show.");
        assertThat(resultEmailTemplate[0]).doesNotContain("Una vez realizado el pago, te pedimos que respondas este e-mail con el comprobante de pago así podemos coordinar la entrega de entradas.");
    }

    @Test
    void givenReservationMpTemplatePath_whenReadMailTemplate_thenThrowNoException(){

        // given - preconditions or setup
        String[] resultEmailTemplate = new String[1];

        // when - action or behaviour that we are going to test
        Assertions.assertDoesNotThrow( () -> resultEmailTemplate[0] = mailingService.readMailTemplate(mailingService.RESERVATION_MP_TEMPLATE_PATH));

        // then - verify the output
        assertThat(resultEmailTemplate[0]).isNotNull();
        assertThat(resultEmailTemplate[0]).contains("Te agradecemos por haber realizado una reserva para nuestro show.");
        assertThat(resultEmailTemplate[0]).contains("Una vez realizado el pago, te pedimos que respondas este e-mail con el comprobante de pago así podemos coordinar la entrega de entradas.");
    }

    @Test
    void givenConfirmationCashTemplatePath_whenReadMailTemplate_thenThrowNoException(){

        // given - preconditions or setup
        String[] resultEmailTemplate = new String[1];

        // when - action or behaviour that we are going to test
        Assertions.assertDoesNotThrow( () -> resultEmailTemplate[0] = mailingService.readMailTemplate(mailingService.CONFIRMATION_CASH_TEMPLATE_PATH));

        // then - verify the output
        assertThat(resultEmailTemplate[0]).isNotNull();
        assertThat(resultEmailTemplate[0]).contains("<p>Te agradecemos por haber concretado el pago.</p>");
        assertThat(resultEmailTemplate[0]).doesNotContain("Ryukyukoku Matsuridaiko se contactará con vos para coordinar la entrega de entradas.");
    }

    @Test
    void givenConfirmationMpTemplatePath_whenReadMailTemplate_thenThrowNoException(){

        // given - preconditions or setup
        String[] resultEmailTemplate = new String[1];

        // when - action or behaviour that we are going to test
        Assertions.assertDoesNotThrow( () -> resultEmailTemplate[0] = mailingService.readMailTemplate(mailingService.CONFIRMATION_MP_TEMPLATE_PATH));

        // then - verify the output
        assertThat(resultEmailTemplate[0]).isNotNull();
        assertThat(resultEmailTemplate[0]).contains("¡Nos vemos pronto en el show!");
        assertThat(resultEmailTemplate[0]).contains("Ryukyukoku Matsuridaiko se contactará con vos para coordinar la entrega de entradas.");
    }

    @Test
    void givenProductionAppProfile_whenGetReservationSubject_thenReturnReservationSubject(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "PRODUCTION");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"reservationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getReservationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).doesNotContain("(TESTING) ");
        assertThat(resultSubject).contains(subject);
    }

    @Test
    void givenTestingAppProfile_whenGetReservationSubject_thenReturnReservationSubjectWithProfile(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "TESTING");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"reservationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getReservationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).contains("(TESTING) " + subject);
    }

    @Test
    void givenProductionAppProfile_whenGetConfirmationSubject_thenReturnConfirmationSubject(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "PRODUCTION");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"confirmationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getConfirmationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).doesNotContain("(TESTING) ");
        assertThat(resultSubject).contains(subject);
    }

    @Test
    void givenTestingAppProfile_whenGetConfirmationSubject_thenReturnConfirmationSubjectWithProfile(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "TESTING");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"confirmationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getConfirmationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).contains("(TESTING) " + subject);
    }

    @Test
    void givenProductionAppProfile_whenGetExpirationSubject_thenReturnExpirationSubject(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "PRODUCTION");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"expirationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getExpirationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).doesNotContain("(TESTING) ");
        assertThat(resultSubject).contains(subject);
    }

    @Test
    void givenTestingAppProfile_whenGetExpirationSubject_thenReturnExpirationSubjectWithProfile(){

        // given - preconditions or setup
        ReflectionTestUtils.setField(mailingService, "appProfile", "TESTING");
        String subject = (String) ReflectionTestUtils.getField(mailingService,"expirationSubject");

        // when - action or behaviour that we are going to test
        String resultSubject = mailingService.getExpirationSubject();

        // then - verify the output
        assertThat(resultSubject).doesNotContain("PRODUCTION");
        assertThat(resultSubject).contains("(TESTING) " + subject);
    }
}