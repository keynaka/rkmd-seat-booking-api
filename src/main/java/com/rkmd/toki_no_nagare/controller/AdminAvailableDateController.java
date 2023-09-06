package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.dto.admin_available_date.CreateAdminAvailableDateRequestDto;
import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDate;
import com.rkmd.toki_no_nagare.service.AdminAvailableDateService;
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

    @GetMapping("")
    public ResponseEntity<List<AdminAvailableDate>> getAvailableDates(@RequestParam(value = "expirationDate", required = false) String expirationDate) {
        ZonedDateTime expirationDateFormatted = expirationDate != null ? Tools.formatDateStringToZonedDateTime(expirationDate) : null;
        List<AdminAvailableDate> availableDates = adminAvailableDateService.getAvailableDates(expirationDateFormatted);
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

