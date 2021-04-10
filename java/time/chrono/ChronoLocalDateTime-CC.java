package java.time.chrono;

import java.time.format.*;
import java.time.temporal.*;
import java.time.*;
import java.util.*;

public final class ChronoLocalDateTime-CC
{
    public static Temporal $default$adjustInto(final ChronoLocalDateTime chronoLocalDateTime, final Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, chronoLocalDateTime.toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, chronoLocalDateTime.toLocalTime().toNanoOfDay());
    }
    
    public static int $default$compareTo(final ChronoLocalDateTime chronoLocalDateTime, final ChronoLocalDateTime<?> chronoLocalDateTime2) {
        int n;
        if ((n = chronoLocalDateTime.toLocalDate().compareTo(chronoLocalDateTime2.toLocalDate())) == 0 && (n = chronoLocalDateTime.toLocalTime().compareTo(chronoLocalDateTime2.toLocalTime())) == 0) {
            n = chronoLocalDateTime.getChronology().compareTo(chronoLocalDateTime2.getChronology());
        }
        return n;
    }
    
    public static String $default$format(final ChronoLocalDateTime chronoLocalDateTime, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(chronoLocalDateTime);
    }
    
    public static Chronology $default$getChronology(final ChronoLocalDateTime chronoLocalDateTime) {
        return chronoLocalDateTime.toLocalDate().getChronology();
    }
    
    public static boolean $default$isAfter(final ChronoLocalDateTime chronoLocalDateTime, final ChronoLocalDateTime<?> chronoLocalDateTime2) {
        final long epochDay = chronoLocalDateTime.toLocalDate().toEpochDay();
        final long epochDay2 = chronoLocalDateTime2.toLocalDate().toEpochDay();
        return epochDay > epochDay2 || (epochDay == epochDay2 && chronoLocalDateTime.toLocalTime().toNanoOfDay() > chronoLocalDateTime2.toLocalTime().toNanoOfDay());
    }
    
    public static boolean $default$isBefore(final ChronoLocalDateTime chronoLocalDateTime, final ChronoLocalDateTime<?> chronoLocalDateTime2) {
        final long epochDay = chronoLocalDateTime.toLocalDate().toEpochDay();
        final long epochDay2 = chronoLocalDateTime2.toLocalDate().toEpochDay();
        return epochDay < epochDay2 || (epochDay == epochDay2 && chronoLocalDateTime.toLocalTime().toNanoOfDay() < chronoLocalDateTime2.toLocalTime().toNanoOfDay());
    }
    
    public static boolean $default$isEqual(final ChronoLocalDateTime chronoLocalDateTime, final ChronoLocalDateTime<?> chronoLocalDateTime2) {
        return chronoLocalDateTime.toLocalTime().toNanoOfDay() == chronoLocalDateTime2.toLocalTime().toNanoOfDay() && chronoLocalDateTime.toLocalDate().toEpochDay() == chronoLocalDateTime2.toLocalDate().toEpochDay();
    }
    
    public static boolean $default$isSupported(final ChronoLocalDateTime chronoLocalDateTime, final TemporalUnit temporalUnit) {
        final boolean b = temporalUnit instanceof ChronoUnit;
        final boolean b2 = false;
        boolean b3 = false;
        if (b) {
            if (temporalUnit != ChronoUnit.FOREVER) {
                b3 = true;
            }
            return b3;
        }
        boolean b4 = b2;
        if (temporalUnit != null) {
            b4 = b2;
            if (temporalUnit.isSupportedBy(chronoLocalDateTime)) {
                b4 = true;
            }
        }
        return b4;
    }
    
    public static ChronoLocalDateTime<Object> $default$minus(final ChronoLocalDateTime chronoLocalDateTime, final long n, final TemporalUnit temporalUnit) {
        return (ChronoLocalDateTime<Object>)ChronoLocalDateTimeImpl.ensureValid(chronoLocalDateTime.getChronology(), Temporal-CC.$default$minus(chronoLocalDateTime, n, temporalUnit));
    }
    
    public static ChronoLocalDateTime<Object> $default$minus(final ChronoLocalDateTime chronoLocalDateTime, final TemporalAmount temporalAmount) {
        return (ChronoLocalDateTime<Object>)ChronoLocalDateTimeImpl.ensureValid(chronoLocalDateTime.getChronology(), Temporal-CC.$default$minus(chronoLocalDateTime, temporalAmount));
    }
    
    public static ChronoLocalDateTime<Object> $default$plus(final ChronoLocalDateTime chronoLocalDateTime, final TemporalAmount temporalAmount) {
        return (ChronoLocalDateTime<Object>)ChronoLocalDateTimeImpl.ensureValid(chronoLocalDateTime.getChronology(), Temporal-CC.$default$plus(chronoLocalDateTime, temporalAmount));
    }
    
    public static <R> R $default$query(final ChronoLocalDateTime chronoLocalDateTime, final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)chronoLocalDateTime.toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)chronoLocalDateTime.getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(chronoLocalDateTime);
    }
    
    public static long $default$toEpochSecond(final ChronoLocalDateTime chronoLocalDateTime, final ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return 86400L * chronoLocalDateTime.toLocalDate().toEpochDay() + chronoLocalDateTime.toLocalTime().toSecondOfDay() - zoneOffset.getTotalSeconds();
    }
    
    public static Instant $default$toInstant(final ChronoLocalDateTime chronoLocalDateTime, final ZoneOffset zoneOffset) {
        return Instant.ofEpochSecond(chronoLocalDateTime.toEpochSecond(zoneOffset), chronoLocalDateTime.toLocalTime().getNano());
    }
    
    public static ChronoLocalDateTime<Object> $default$with(final ChronoLocalDateTime chronoLocalDateTime, final TemporalAdjuster temporalAdjuster) {
        return (ChronoLocalDateTime<Object>)ChronoLocalDateTimeImpl.ensureValid(chronoLocalDateTime.getChronology(), Temporal-CC.$default$with(chronoLocalDateTime, temporalAdjuster));
    }
    
    public static ChronoLocalDateTime<?> from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDateTime) {
            return (ChronoLocalDateTime<?>)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to obtain ChronoLocalDateTime from TemporalAccessor: ");
            sb.append(temporalAccessor.getClass());
            throw new DateTimeException(sb.toString());
        }
        return chronology.localDateTime(temporalAccessor);
    }
    
    public static Comparator<ChronoLocalDateTime<?>> timeLineOrder() {
        return (Comparator<ChronoLocalDateTime<?>>)AbstractChronology.DATE_TIME_ORDER;
    }
}
