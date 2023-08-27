package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.service.ContactService;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.service.mailing.JavaMailSenderImpl;
import com.rkmd.toki_no_nagare.service.mailing.TransportMailSenderImpl;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    @Autowired
    private TransportMailSenderImpl transportMailSenderImpl;

    @GetMapping("/{dni}")
    public ResponseEntity<Contact> getContact(@PathVariable("dni") Long dni) {
        Optional<Contact> contact = contactService.getContactByDni(dni);
        if (!contact.isPresent())
           return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(contact.get());
    }

    @PostMapping("")
    public ResponseEntity<Contact> createContact(@RequestBody @Valid Map<String, Object> json) {
        ValidationUtils.checkParam(json.containsKey("dni"), "dni_missing", "Dni is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("name"), "name_missing", "Name is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("last_name"), "last_name_missing", "Last Name is missing and is mandatory");

        if (json.containsKey("username") || json.containsKey("password"))
            ValidationUtils.checkParam(json.containsKey("username"), "username_missing", "Username is missing and is mandatory");
            ValidationUtils.checkParam(json.containsKey("password"), "password_missing", "Password is missing and is mandatory");

        Contact newContact = contactService.createContact(json);

        return ResponseEntity.ok().body(newContact);
    }

    /** Uso provisorio. Eliminar este endpoint al finalizar los tests de envío de e-mails. */
    @Deprecated
    @GetMapping("/test/notifications/reservations/{email}/{paymentMethod}/{name}/{lastname}")
    public ResponseEntity<String> testSendMail(@PathVariable("email") String email,
                                               @PathVariable("paymentMethod") String paymentMethod,
                                               @PathVariable("name") String name,
                                               @PathVariable("lastname") String lastname){

        PaymentMethod enumPaymentMethod;

        if(paymentMethod.equals("mercado_pago")){
            enumPaymentMethod = PaymentMethod.MERCADO_PAGO;
        } else {
            enumPaymentMethod = PaymentMethod.CASH;
        }

        AbstractMailingService mailingService = javaMailSenderImpl;

        List<SeatDto> seats = new ArrayList<>();
        SeatDto seat1 = new SeatDto();
        seat1.setRow(10L);
        seat1.setColumn(14L);
        seat1.setSector(SeatSector.PLATEA);
        seat1.setPrice(BigDecimal.valueOf(3500L));
        seats.add(seat1);

        SeatDto seat2 = new SeatDto();
        seat2.setRow(10L);
        seat2.setColumn(16L);
        seat2.setSector(SeatSector.PLATEA);
        seat2.setPrice(BigDecimal.valueOf(3500L));
        seats.add(seat2);

        SeatDto seat3 = new SeatDto();
        seat3.setRow(10L);
        seat3.setColumn(18L);
        seat3.setSector(SeatSector.PLATEA);
        seat3.setPrice(BigDecimal.valueOf(3500L));
        seats.add(seat3);

        return ResponseEntity.ok(mailingService.notifyReservation(email, name, lastname, "asdf1231",
                enumPaymentMethod, ZonedDateTime.now().plusDays(2),
                seats));
    }
}
