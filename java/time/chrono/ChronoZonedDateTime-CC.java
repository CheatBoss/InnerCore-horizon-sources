package java.time.chrono;

import java.time.format.*;
import java.time.temporal.*;
import java.time.*;
import java.util.*;

public final class ChronoZonedDateTime-CC
{
    public static int $default$compareTo(final ChronoZonedDateTime chronoZonedDateTime, final ChronoZonedDateTime<?> chronoZonedDateTime2) {
        long n;
        if ((n = lcmp(chronoZonedDateTime.toEpochSecond(), chronoZonedDateTime2.toEpochSecond())) == 0 && (n = chronoZonedDateTime.toLocalTime().getNano() - chronoZonedDateTime2.toLocalTime().getNano()) == 0 && (n = chronoZonedDateTime.toLocalDateTime().compareTo(chronoZonedDateTime2.toLocalDateTime())) == 0 && (n = chronoZonedDateTime.getZone().getId().compareTo(chronoZonedDateTime2.getZone().getId())) == 0) {
            n = chronoZonedDateTime.getChronology().compareTo(chronoZonedDateTime2.getChronology());
        }
        return (int)n;
    }
    
    public static String $default$format(final ChronoZonedDateTime chronoZonedDateTime, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(chronoZonedDateTime);
    }
    
    public static int $default$get(final ChronoZonedDateTime chronoZonedDateTime, final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return TemporalAccessor-CC.$default$get(chronoZonedDateTime, temporalField);
        }
        switch ((ChronoField)temporalField) {
            default: {
                return chronoZonedDateTime.toLocalDateTime().get(temporalField);
            }
            case OFFSET_SECONDS: {
                return chronoZonedDateTime.getOffset().getTotalSeconds();
            }
            case INSTANT_SECONDS: {
                throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
            }
        }
    }
    
    public static Chronology $default$getChronology(final ChronoZonedDateTime chronoZonedDateTime) {
        return chronoZonedDateTime.toLocalDate().getChronology();
    }
    
    public static long $default$getLong(final ChronoZonedDateTime chronoZonedDateTime, final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(chronoZonedDateTime);
        }
        switch ((ChronoField)temporalField) {
            default: {
                return chronoZonedDateTime.toLocalDateTime().getLong(temporalField);
            }
            case OFFSET_SECONDS: {
                return chronoZonedDateTime.getOffset().getTotalSeconds();
            }
            case INSTANT_SECONDS: {
                return chronoZonedDateTime.toEpochSecond();
            }
        }
    }
    
    public static boolean $default$isAfter(final ChronoZonedDateTime chronoZonedDateTime, final ChronoZonedDateTime<?> chronoZonedDateTime2) {
        final long epochSecond = chronoZonedDateTime.toEpochSecond();
        final long epochSecond2 = chronoZonedDateTime2.toEpochSecond();
        return epochSecond > epochSecond2 || (epochSecond == epochSecond2 && chronoZonedDateTime.toLocalTime().getNano() > chronoZonedDateTime2.toLocalTime().getNano());
    }
    
    public static boolean $default$isBefore(final ChronoZonedDateTime chronoZonedDateTime, final ChronoZonedDateTime<?> chronoZonedDateTime2) {
        final long epochSecond = chronoZonedDateTime.toEpochSecond();
        final long epochSecond2 = chronoZonedDateTime2.toEpochSecond();
        return epochSecond < epochSecond2 || (epochSecond == epochSecond2 && chronoZonedDateTime.toLocalTime().getNano() < chronoZonedDateTime2.toLocalTime().getNano());
    }
    
    public static boolean $default$isEqual(final ChronoZonedDateTime chronoZonedDateTime, final ChronoZonedDateTime<?> chronoZonedDateTime2) {
        return chronoZonedDateTime.toEpochSecond() == chronoZonedDateTime2.toEpochSecond() && chronoZonedDateTime.toLocalTime().getNano() == chronoZonedDateTime2.toLocalTime().getNano();
    }
    
    public static boolean $default$isSupported(final ChronoZonedDateTime chronoZonedDateTime, final TemporalUnit temporalUnit) {
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
            if (temporalUnit.isSupportedBy(chronoZonedDateTime)) {
                b4 = true;
            }
        }
        return b4;
    }
    
    public static ChronoZonedDateTime<Object> $default$minus(final ChronoZonedDateTime chronoZonedDateTime, final long n, final TemporalUnit temporalUnit) {
        return (ChronoZonedDateTime<Object>)ChronoZonedDateTimeImpl.ensureValid(chronoZonedDateTime.getChronology(), Temporal-CC.$default$minus(chronoZonedDateTime, n, temporalUnit));
    }
    
    public static ChronoZonedDateTime<Object> $default$minus(final ChronoZonedDateTime chronoZonedDateTime, final TemporalAmount temporalAmount) {
        return (ChronoZonedDateTime<Object>)ChronoZonedDateTimeImpl.ensureValid(chronoZonedDateTime.getChronology(), Temporal-CC.$default$minus(chronoZonedDateTime, temporalAmount));
    }
    
    public static ChronoZonedDateTime<Object> $default$plus(final ChronoZonedDateTime chronoZonedDateTime, final TemporalAmount temporalAmount) {
        return (ChronoZonedDateTime<Object>)ChronoZonedDateTimeImpl.ensureValid(chronoZonedDateTime.getChronology(), Temporal-CC.$default$plus(chronoZonedDateTime, temporalAmount));
    }
    
    public static <R> R $default$query(final ChronoZonedDateTime chronoZonedDateTime, final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId()) {
            return (R)chronoZonedDateTime.getZone();
        }
        if (temporalQuery == TemporalQueries.offset()) {
            return (R)chronoZonedDateTime.getOffset();
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)chronoZonedDateTime.toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)chronoZonedDateTime.getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(chronoZonedDateTime);
    }
    
    public static ValueRange $default$range(final ChronoZonedDateTime chronoZonedDateTime, final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(chronoZonedDateTime);
        }
        if (temporalField != ChronoField.INSTANT_SECONDS && temporalField != ChronoField.OFFSET_SECONDS) {
            return chronoZonedDateTime.toLocalDateTime().range(temporalField);
        }
        return temporalField.range();
    }
    
    public static long $default$toEpochSecond(final ChronoZonedDateTime chronoZonedDateTime) {
        return 86400L * chronoZonedDateTime.toLocalDate().toEpochDay() + chronoZonedDateTime.toLocalTime().toSecondOfDay() - chronoZonedDateTime.getOffset().getTotalSeconds();
    }
    
    public static Instant $default$toInstant(final ChronoZonedDateTime chronoZonedDateTime) {
        return Instant.ofEpochSecond(chronoZonedDateTime.toEpochSecond(), chronoZonedDateTime.toLocalTime().getNano());
    }
    
    public static ChronoLocalDate $default$toLocalDate(final ChronoZonedDateTime chronoZonedDateTime) {
        return chronoZonedDateTime.toLocalDateTime().toLocalDate();
    }
    
    public static LocalTime $default$toLocalTime(final ChronoZonedDateTime chronoZonedDateTime) {
        return chronoZonedDateTime.toLocalDateTime().toLocalTime();
    }
    
    public static ChronoZonedDateTime<Object> $default$with(final ChronoZonedDateTime chronoZonedDateTime, final TemporalAdjuster temporalAdjuster) {
        return (ChronoZonedDateTime<Object>)ChronoZonedDateTimeImpl.ensureValid(chronoZonedDateTime.getChronology(), Temporal-CC.$default$with(chronoZonedDateTime, temporalAdjuster));
    }
    
    public static ChronoZonedDateTime<?> from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoZonedDateTime) {
            return (ChronoZonedDateTime<?>)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to obtain ChronoZonedDateTime from TemporalAccessor: ");
            sb.append(temporalAccessor.getClass());
            throw new DateTimeException(sb.toString());
        }
        return chronology.zonedDateTime(temporalAccessor);
    }
    
    public static Comparator<ChronoZonedDateTime<?>> timeLineOrder() {
        return AbstractChronology.INSTANT_ORDER;
    }
}
