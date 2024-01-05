package com.au5tie.minecraft.tobnet.game.time;

import java.util.concurrent.TimeUnit;

public class TobnetTimeUtils {

  /**
   *
   * Origin: https://stackoverflow.com/questions/25903354/java-convert-seconds-to-minutes-hours-and-days
   *
   * @param s
   * @author au5tie, sourced from Stack Overflow.
   */
  public static String secondsToDuration(long seconds) {
    StringBuilder stringBuilder = new StringBuilder();

    // Days.
    int days = (int) TimeUnit.SECONDS.toDays(seconds);

    if (days > 0) {
      stringBuilder.append(days).append(" days ");
    }

    // Hours.
    long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);

    if (hours > 0) {
      stringBuilder.append(hours).append(" hours ");
    }

    // Minutes.
    long minutes =
      TimeUnit.SECONDS.toMinutes(seconds) -
      (TimeUnit.SECONDS.toHours(seconds) * 60);

    if (minutes > 0) {
      stringBuilder.append(minutes).append(" minutes ");
    }

    // Seconds.
    long remainingSeconds =
      TimeUnit.SECONDS.toSeconds(seconds) -
      (TimeUnit.SECONDS.toMinutes(seconds) * 60);

    if (remainingSeconds > 0) {
      stringBuilder.append(remainingSeconds).append(" seconds ");
    }

    return stringBuilder.toString();
  }
}
