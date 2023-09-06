package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.admin_available_date.CreateAdminAvailableDateRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDate;
import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDateId;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.AdminAvailableDateRepository;
import com.rkmd.toki_no_nagare.utils.Tools;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminAvailableDateService {
    // Este limite esta pensado en que el que reserva un Miercoles es el que mas tiempo se le da, dado que le da hasta
    // el otro Sabado, que serian 10 dias
    public static final int DELETE_THRESHOLD = 11;
    @Autowired
    private AdminAvailableDateRepository adminAvailableDateRepository;

    @Autowired
    private BookingService bookingService;

    public List<AdminAvailableDate> getAvailableDates(ZonedDateTime expirationDate) {
        List<AdminAvailableDate> availableDates = adminAvailableDateRepository.findAll();
        if(expirationDate == null) return availableDates;

        List<AdminAvailableDate> availableDatesForBooking = availableDates
                .stream()
                .filter(adminAvailableDate ->
                        adminAvailableDate.getInitDate().isAfter(Tools.getCurrentDate()) &&
                                adminAvailableDate.getEndDate().isBefore(expirationDate)
                )
                .collect(Collectors.toList());

        return availableDatesForBooking;
    }

    public AdminAvailableDate createAvailableDate(CreateAdminAvailableDateRequestDto request) {
        ZonedDateTime initDate = Tools.formatDateStringToZonedDateTime(request.getInitDate());
        AdminAvailableDateId id = new AdminAvailableDateId(initDate, request.getPlace());
        if (adminAvailableDateRepository.findById(id).isPresent())
            throw new BadRequestException("date_plate_already_exists", "This init date and place is already created");

        ZonedDateTime endDate = Tools.formatDateStringToZonedDateTime(request.getEndDate());
        if (endDate.isBefore(initDate) || endDate.getDayOfYear() != initDate.getDayOfYear())
            throw new BadRequestException("invalid_end_date", "The end date must be the same of the init and later");

        AdminAvailableDate newAvailableDate = new AdminAvailableDate();
        newAvailableDate.setInitDate(initDate);
        newAvailableDate.setEndDate(endDate);
        newAvailableDate.setPlace(request.getPlace());

        try {
            return adminAvailableDateRepository.save(newAvailableDate);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    public void deleteAvailableDate(String initDateStr, String place) {
        ZonedDateTime initDate = Tools.formatDateStringToZonedDateTime(initDateStr);

        if (initDate.isBefore(Tools.getCurrentDate().plusDays(DELETE_THRESHOLD)))
            throw new BadRequestException("bad_request", "This date-place is too close to the date to be deleted");

        Optional<AdminAvailableDate> availableDateToDelete = adminAvailableDateRepository.findById(new AdminAvailableDateId(initDate, place));
        ValidationUtils.checkFound(availableDateToDelete.isPresent(), "available_date_not_found", "The date-place selected was not found");

        try {
            adminAvailableDateRepository.delete(availableDateToDelete.get());
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }
}
