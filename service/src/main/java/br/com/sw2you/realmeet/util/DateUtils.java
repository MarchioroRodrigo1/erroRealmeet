package br.com.sw2you.realmeet.util;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;



public final class DateUtils {
    private static final ZoneOffset DEFAULT_TIMEZONE = ZoneOffset.of("-03:00");

    public DateUtils() {}

    public static OffsetDateTime now() {
        return OffsetDateTime.now(DEFAULT_TIMEZONE).truncatedTo(MILLIS);
    }
}
