package com.shovon.tradingserver.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtils {

  public static LocalDateTime nowUtc() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }
}
