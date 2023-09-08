package com.rkmd.toki_no_nagare.utils;

import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Tools {

  public static final String ZONED_ID = "America/Argentina/Buenos_Aires";
  static int hashLength = 8;
  static String hashCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public static ZonedDateTime getCurrentDate(){
    ZoneId zoneIdArgentina = ZoneId.of(ZONED_ID);
    return ZonedDateTime.now(zoneIdArgentina);
  }

  public static String generateHashCode(Long contactDni, String bookingCode){
    String key = contactDni + bookingCode;
    String salt = BCrypt.gensalt();
    return BCrypt.hashpw(key, salt);
  }

  public static String generateRandomHash(){
    SecureRandom random = new SecureRandom();
    StringBuilder randomHash = new StringBuilder(hashLength);

    for (int i = 0; i < hashLength; i++) {
      int randomIndex = random.nextInt(hashCharacters.length());
      randomHash.append(hashCharacters.charAt(randomIndex));
    }
    return randomHash.toString();
  }

  public static boolean validateBookingCode(Long contactDni, String bookingCode, String hashedBookingCode){
    String key = contactDni + bookingCode;
    return BCrypt.checkpw(key, hashedBookingCode);
  }

  public static String formatArgentinianDate(ZonedDateTime zonedDateTime){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return zonedDateTime.format(formatter);
  }

  public static ZonedDateTime formatDateStringToZonedDateTime(String dateString){
    ZoneId zoneId = ZoneId.of(ZONED_ID);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter).withZoneSameLocal(zoneId);

    return zonedDateTime;
  }

  /** Converts a list of Seat into a list of SeatDto */
  public static List<SeatDto> convertSeatToSeatDto(List<Seat> seats){
    return seats.stream().map(seat -> {
      SeatDto seatDto = new SeatDto(seat.getRow(), seat.getColumn(), seat.getPrice(), seat.getSector(), seat.getStatus());
      return seatDto;
    })
            .collect(Collectors.toList());
  }

  public static String getCurrentDateAsString(){
    ZoneId zoneIdArgentina = ZoneId.of(ZONED_ID);
    ZonedDateTime date = ZonedDateTime.now(zoneIdArgentina);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    return date.format(formatter);
  }

}
