package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.BookingRepository;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<Booking> get(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Map<String, Object> json) {
        Booking newBooking = new Booking();

        //TODO: CONTINUE HERE...
        /*newBooking.setName((String) json.get("name"));
        newBooking.setLastName((String) json.get("last_name"));
        newBooking.setPasswordHash((String) json.get("password"));*/

        try {
            return bookingRepository.save(newBooking);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }


    /** This method checks if a booking exists according to the 'dni' and 'bookingCode' passed as parameters.
     * If exists, returns the booking data. If it doesn't exist, it throws an exception.
     * @param bookingCode Booking code
     * @param dni User identity number
     * @throws BadRequestException Throw BadRequestException is booking not exists
     * @return BookingResponseDto
     */
    public BookingResponseDto getBookingByCodeAndDni(String bookingCode, Long dni){
        String hashedBookingCode = Tools.generateHashCode(dni, bookingCode);
        List<Booking> bookings = bookingRepository.findAll();
        Optional<Booking> booking = bookings.stream().filter(b -> b.getHashedBookingCode().equals(hashedBookingCode)).findFirst();

        if(booking.isEmpty()){
            throw new NotFoundException("booking_not_found", "The requested booking does not exist.");
        }

        return modelMapper.map(booking.get(), BookingResponseDto.class);
    }
}
