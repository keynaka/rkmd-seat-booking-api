package com.rkmd.toki_no_nagare.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Tools {

  public static ZonedDateTime getCurrentDate(){
    ZoneId zoneIdArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
    return ZonedDateTime.now(zoneIdArgentina);
  }

  public static String generateHashCode(Long contactDni, String bookingCode){
    String key = contactDni + bookingCode;
    String salt = BCrypt.gensalt();
    return BCrypt.hashpw(key, salt);
  }

}
