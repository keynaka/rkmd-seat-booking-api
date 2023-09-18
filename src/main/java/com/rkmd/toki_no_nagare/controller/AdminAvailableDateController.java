package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.dto.admin_available_date.CreateAdminAvailableDateRequestDto;
import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDate;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.service.AdminAvailableDateService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationService;
import com.rkmd.toki_no_nagare.service.expiration.ExpirationServiceFactory;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/available-dates")
public class AdminAvailableDateController {
    @Autowired
    private AdminAvailableDateService adminAvailableDateService;

    @Autowired
    private ExpirationServiceFactory expirationServiceFactory;

    @GetMapping("")
    public ResponseEntity<List<AdminAvailableDate>> getAvailableDates(@RequestParam(value = "expirationDate", required = false) String expirationDate) {
        ZonedDateTime expirationDateFormatted = expirationDate != null ? Tools.formatDateStringToZonedDateTime(expirationDate) : null;
        List<AdminAvailableDate> availableDates = adminAvailableDateService.getAvailableDates(expirationDateFormatted);
        for (AdminAvailableDate availableDate : availableDates) {
            availableDate.setInitDate(Tools.changeToArgentinianZonedId(availableDate.getInitDate()));
            availableDate.setEndDate(Tools.changeToArgentinianZonedId(availableDate.getEndDate()));
        }
        return ResponseEntity.ok().body(availableDates);
    }

    @GetMapping("/{paymentMethod}")
    public ResponseEntity<List<AdminAvailableDate>> getAvailableDatesByPaymentMethod(@PathVariable("paymentMethod") String paymentMethodInput) {
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(paymentMethodInput.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid_payment_method", "Invalid payment method");
        }

        ZonedDateTime expirationDate = expirationServiceFactory
                .getExpirationService(paymentMethod)
                .getExpirationDate(Tools.getCurrentDate());
        List<AdminAvailableDate> availableDates = adminAvailableDateService.getAvailableDates(expirationDate);

        return ResponseEntity.ok().body(availableDates);
    }

    @PostMapping(value = "", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public AdminAvailableDate createAvailableDate(@Valid @RequestBody CreateAdminAvailableDateRequestDto request) {
        return adminAvailableDateService.createAvailableDate(request);
    }

    @DeleteMapping(value = "", produces = "application/json")
    public ResponseEntity<?> deleteAvailableDate(@RequestParam(value = "init_date") String initDate,
                                                  @RequestParam(value = "place") String place) {
        adminAvailableDateService.deleteAvailableDate(initDate, place);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

