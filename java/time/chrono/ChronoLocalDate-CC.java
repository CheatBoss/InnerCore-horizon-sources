package java.time.chrono;

import java.time.format.*;
import java.time.temporal.*;
import java.time.*;
import java.util.*;

public final class ChronoLocalDate-CC
{
    public static Temporal $default$adjustInto(final ChronoLocalDate chronoLocalDate, final Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, chronoLocalDate.toEpochDay());
    }
    
    public static ChronoLocalDateTime<?> $default$atTime(final ChronoLocalDate chronoLocalDate, final LocalTime localTime) {
        return ChronoLocalDateTimeImpl.of(chronoLocalDate, localTime);
    }
    
    public static int $default$compareTo(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        long compareTo;
        if ((compareTo = lcmp(chronoLocalDate.toEpochDay(), chronoLocalDate2.toEpochDay())) == 0) {
            compareTo = chronoLocalDate.getChronology().compareTo(chronoLocalDate2.getChronology());
        }
        return (int)compareTo;
    }
    
    public static String $default$format(final ChronoLocalDate chronoLocalDate, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(chronoLocalDate);
    }
    
    public static Era $default$getEra(final ChronoLocalDate chronoLocalDate) {
        return chronoLocalDate.getChronology().eraOf(chronoLocalDate.get(ChronoField.ERA));
    }
    
    public static boolean $default$isAfter(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        return chronoLocalDate.toEpochDay() > chronoLocalDate2.toEpochDay();
    }
    
    public static boolean $default$isBefore(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        return chronoLocalDate.toEpochDay() < chronoLocalDate2.toEpochDay();
    }
    
    public static boolean $default$isEqual(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        return chronoLocalDate.toEpochDay() == chronoLocalDate2.toEpochDay();
    }
    
    public static boolean $default$isLeapYear(final ChronoLocalDate chronoLocalDate) {
        return chronoLocalDate.getChronology().isLeapYear(chronoLocalDate.getLong(ChronoField.YEAR));
    }
    
    public static boolean $default$isSupported(final ChronoLocalDate chronoLocalDate, final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isDateBased();
        }
        return temporalField != null && temporalField.isSupportedBy(chronoLocalDate);
    }
    
    public static boolean $default$isSupported(final ChronoLocalDate chronoLocalDate, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isDateBased();
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(chronoLocalDate);
    }
    
    public static int $default$lengthOfYear(final ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate.isLeapYear()) {
            return 366;
        }
        return 365;
    }
    
    public static ChronoLocalDate $default$minus(final ChronoLocalDate chronoLocalDate, final long n, final TemporalUnit temporalUnit) {
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), Temporal-CC.$default$minus(chronoLocalDate, n, temporalUnit));
    }
    
    public static ChronoLocalDate $default$minus(final ChronoLocalDate chronoLocalDate, final TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), Temporal-CC.$default$minus(chronoLocalDate, temporalAmount));
    }
    
    public static ChronoLocalDate $default$plus(final ChronoLocalDate chronoLocalDate, final long n, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported unit: ");
            sb.append(temporalUnit);
            throw new UnsupportedTemporalTypeException(sb.toString());
        }
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), temporalUnit.addTo(chronoLocalDate, n));
    }
    
    public static ChronoLocalDate $default$plus(final ChronoLocalDate chronoLocalDate, final TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), Temporal-CC.$default$plus(chronoLocalDate, temporalAmount));
    }
    
    public static <R> R $default$query(final ChronoLocalDate chronoLocalDate, final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)chronoLocalDate.getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.DAYS;
        }
        return (R)temporalQuery.queryFrom(chronoLocalDate);
    }
    
    public static long $default$toEpochDay(final ChronoLocalDate chronoLocalDate) {
        return chronoLocalDate.getLong(ChronoField.EPOCH_DAY);
    }
    
    public static ChronoLocalDate $default$with(final ChronoLocalDate chronoLocalDate, final TemporalAdjuster temporalAdjuster) {
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), Temporal-CC.$default$with(chronoLocalDate, temporalAdjuster));
    }
    
    public static ChronoLocalDate $default$with(final ChronoLocalDate chronoLocalDate, final TemporalField temporalField, final long n) {
        if (temporalField instanceof ChronoField) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported field: ");
            sb.append(temporalField);
            throw new UnsupportedTemporalTypeException(sb.toString());
        }
        return ChronoLocalDateImpl.ensureValid(chronoLocalDate.getChronology(), temporalField.adjustInto(chronoLocalDate, n));
    }
    
    public static ChronoLocalDate from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDate) {
            return (ChronoLocalDate)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to obtain ChronoLocalDate from TemporalAccessor: ");
            sb.append(temporalAccessor.getClass());
            throw new DateTimeException(sb.toString());
        }
        return chronology.date(temporalAccessor);
    }
    
    public static Comparator<ChronoLocalDate> timeLineOrder() {
        return AbstractChronology.DATE_ORDER;
    }
}
