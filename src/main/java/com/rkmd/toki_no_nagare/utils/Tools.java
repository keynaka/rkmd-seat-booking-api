package com.rkmd.toki_no_nagare.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Tools {

  static int hashLength = 6;
  static String hashCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%&*_+";

  public static ZonedDateTime getCurrentDate(){
    ZoneId zoneIdArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
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

}
