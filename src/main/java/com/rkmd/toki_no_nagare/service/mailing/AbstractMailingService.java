package com.rkmd.toki_no_nagare.service.mailing;

import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractMailingService {

    @Value("${payment.mercadoPagoAccount}")
    @Getter
    private String mercadoPagoAccount;

    /** Asunto a utilizar en el e-mail de reserva provisoria. */
    public static final String RESERVATION_SUBJECT = "Toki no Nagare - Tenés una reserva pendiente de pago.";

    /** Notifica por e-mail la reserva provisoria de entradas. */
    public abstract String notifyReservation(String recipient, String name, String lastname, String bookingCode,
                                             PaymentMethod paymentMethod, ZonedDateTime expirationTime,
                                             List<SeatDto> seats);

    public String readMailTemplate(String filePath){

        InputStream inputStream = this.getClass().getResourceAsStream(filePath);

        String data = null;
        try {
            data = readFromInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    protected Map<String, String> getReservationData(List<SeatDto> seats){
        Map<String, String> reservationData = new HashMap<>();

        String seatList = "";
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SeatDto s: seats) {
            seatList = seatList.concat("\n- Sector " + s.getSector() + " - Fila " + s.getRow() + " - Asiento " + s.getColumn() + " - $" + s.getPrice().toString());
            totalAmount = totalAmount.add(s.getPrice());
        }
        seatList = seatList.replaceFirst("\n", "");

        reservationData.put("seats", seatList);
        reservationData.put("totalAmount", totalAmount.toString());

        return reservationData;
    }

}
