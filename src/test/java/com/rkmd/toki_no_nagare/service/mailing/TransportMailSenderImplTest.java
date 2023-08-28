package com.rkmd.toki_no_nagare.service.mailing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThat(resultEmailTemplate[0]).contains("<p>\n" +
                "    Matsuridaiko se contactará con vos para coordinar el pago y entrega de entradas.<br>\n" +
                "    Recordá que tenés hasta la fecha ${RESERVATION_EXPIRATION} para realizar el pago. Pasado este tiempo, deberemos cancelar tu reserva.\n" +
                "</p>");
    }

    @Test
    void givenReservationMpTemplatePath_whenReadMailTemplate_thenThrowNoException(){

        // given - preconditions or setup
        String[] resultEmailTemplate = new String[1];

        // when - action or behaviour that we are going to test
        Assertions.assertDoesNotThrow( () -> resultEmailTemplate[0] = mailingService.readMailTemplate(mailingService.RESERVATION_MP_TEMPLATE_PATH));

        // then - verify the output
        assertThat(resultEmailTemplate[0]).isNotNull();
        assertThat(resultEmailTemplate[0]).contains("<p>\n" +
                "        Nuestra cuenta de Mercado Pago es ${MP_ACCOUNT}.<br>\n" +
                "        Una vez realizado el pago, te pedimos que nos respondas a este mail con el comprobante de pago así podemos coordinar la entrega de entradas.\n" +
                "    </p>");
    }


}