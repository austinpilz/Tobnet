package com.au5tie.minecraft.tobnet.game.time;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
public class TimeDifference {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime tempDateTime = LocalDateTime.from(startTime);

        // Years.
        long years = tempDateTime.until(endTime, ChronoUnit.YEARS );
        tempDateTime = tempDateTime.plusYears(years);

        if (years > 0) {
            stringBuilder.append(years).append(" years ");
        }

        // Months.
        long months = tempDateTime.until(endTime, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths(months);

        if (months > 0) {
            stringBuilder.append(months).append(" months ");
        }

        // Days.
        long days = tempDateTime.until(endTime, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays(days);

        if (days > 0) {
            stringBuilder.append(days).append(" days ");
        }

        // Hours.
        long hours = tempDateTime.until(endTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        if (hours > 0) {
            stringBuilder.append(hours).append(" hours ");
        }

        // Minutes.
        long minutes = tempDateTime.until(endTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        if (minutes > 0) {
            stringBuilder.append(minutes).append(" minutes ");
        }

        // Seconds.
        long seconds = tempDateTime.until(endTime, ChronoUnit.SECONDS);
        tempDateTime = tempDateTime.plusSeconds(seconds);

        if (seconds > 0) {
            stringBuilder.append(seconds).append(" seconds ");
        }

        // Seconds.
        long milliseconds = tempDateTime.until(endTime, ChronoUnit.MILLIS);
        tempDateTime = tempDateTime.plus(milliseconds, ChronoUnit.MILLIS);

        if (milliseconds > 0) {
            stringBuilder.append(seconds).append(" milliseconds ");
        }

        return stringBuilder.toString();
    }

    /**
     * @return The number of seconds between the two times.
     * @author au5tie
     */
    public long getSecondsBetween() {

        return startTime.until(endTime, ChronoUnit.SECONDS);
    }
}
