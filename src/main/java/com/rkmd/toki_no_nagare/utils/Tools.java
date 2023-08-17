package com.rkmd.toki_no_nagare.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Tools {

  public static ZonedDateTime getCurrentDate(){
    ZoneId zoneIdArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
    return ZonedDateTime.now(zoneIdArgentina);
  }
}
