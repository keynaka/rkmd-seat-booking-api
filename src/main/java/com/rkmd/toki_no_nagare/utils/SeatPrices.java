package com.rkmd.toki_no_nagare.utils;

import com.rkmd.toki_no_nagare.entities.seat.SeatSector;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class SeatPrices {
  private static final Map<Long, BigDecimal> PLATEA_SEAT_PRICE = new HashMap<>();
  private static final Map<Long, BigDecimal> PULLMAN_SEAT_PRICE = new HashMap<>();

  static {
    //Respetan el mismo orden que lo visto en la imagen theater_map.png visto de izquierda a derecha. Esto es util luego para la logica de recomendacion de asientos
    PLATEA_SEAT_PRICE.put(1L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(2L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(3L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(4L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(5L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(6L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(7L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(8L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(9L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(10L, BigDecimal.valueOf(7000L));
    PLATEA_SEAT_PRICE.put(11L, BigDecimal.valueOf(6000L));
    PLATEA_SEAT_PRICE.put(12L, BigDecimal.valueOf(6000L));
    PLATEA_SEAT_PRICE.put(13L, BigDecimal.valueOf(6000L));
    PLATEA_SEAT_PRICE.put(14L, BigDecimal.valueOf(6000L));
    PLATEA_SEAT_PRICE.put(15L, BigDecimal.valueOf(6000L));
    PLATEA_SEAT_PRICE.put(16L, BigDecimal.valueOf(5000L));
    PLATEA_SEAT_PRICE.put(17L, BigDecimal.valueOf(5000L));
    PLATEA_SEAT_PRICE.put(18L, BigDecimal.valueOf(5000L));
    PLATEA_SEAT_PRICE.put(19L, BigDecimal.valueOf(5000L));
    PLATEA_SEAT_PRICE.put(20L, BigDecimal.valueOf(500L));
    PLATEA_SEAT_PRICE.put(21L, BigDecimal.valueOf(4000L));
    PLATEA_SEAT_PRICE.put(22L, BigDecimal.valueOf(4000L));
    PLATEA_SEAT_PRICE.put(23L, BigDecimal.valueOf(4000L));
    PLATEA_SEAT_PRICE.put(24L, BigDecimal.valueOf(4000L));

    PULLMAN_SEAT_PRICE.put(1L, BigDecimal.valueOf(6000L));
    PULLMAN_SEAT_PRICE.put(2L, BigDecimal.valueOf(6000L));
    PULLMAN_SEAT_PRICE.put(3L, BigDecimal.valueOf(6000L));
    PULLMAN_SEAT_PRICE.put(4L, BigDecimal.valueOf(6000L));
    PULLMAN_SEAT_PRICE.put(5L, BigDecimal.valueOf(5000L));
    PULLMAN_SEAT_PRICE.put(6L, BigDecimal.valueOf(5000L));
    PULLMAN_SEAT_PRICE.put(7L, BigDecimal.valueOf(5000L));
    PULLMAN_SEAT_PRICE.put(8L, BigDecimal.valueOf(5000L));
    PULLMAN_SEAT_PRICE.put(9L, BigDecimal.valueOf(4000L));
    PULLMAN_SEAT_PRICE.put(10L, BigDecimal.valueOf(4000L));
    PULLMAN_SEAT_PRICE.put(11L, BigDecimal.valueOf(4000L));
  }

  public static final Map<SeatSector, Map<Long, BigDecimal>> SEAT_PRICES = Map.of(
      SeatSector.PLATEA, PLATEA_SEAT_PRICE,
      SeatSector.PULLMAN, PULLMAN_SEAT_PRICE
  );
}
