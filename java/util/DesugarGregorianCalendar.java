package java.util;

import java.time.temporal.*;
import java.time.*;

public class DesugarGregorianCalendar
{
    private DesugarGregorianCalendar() {
    }
    
    public static GregorianCalendar from(final ZonedDateTime zonedDateTime) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(DesugarTimeZone.getTimeZone(zonedDateTime.getZone()));
        gregorianCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
        gregorianCalendar.setFirstDayOfWeek(2);
        gregorianCalendar.setMinimalDaysInFirstWeek(4);
        try {
            gregorianCalendar.setTimeInMillis(Math8.addExact(Math8.multiplyExact(zonedDateTime.toEpochSecond(), 1000), zonedDateTime.get(ChronoField.MILLI_OF_SECOND)));
            return gregorianCalendar;
        }
        catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    public static ZonedDateTime toZonedDateTime(final GregorianCalendar gregorianCalendar) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(gregorianCalendar.getTimeInMillis()), DesugarTimeZone.toZoneId(gregorianCalendar.getTimeZone()));
    }
}
