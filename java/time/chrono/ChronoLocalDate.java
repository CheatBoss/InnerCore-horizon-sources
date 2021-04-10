package java.time.chrono;

import java.util.*;
import java.time.temporal.*;
import java.time.format.*;
import java.time.*;

public interface ChronoLocalDate extends Temporal, TemporalAdjuster, Comparable<ChronoLocalDate>
{
    default Comparator<ChronoLocalDate> timeLineOrder() {
        return AbstractChronology.DATE_ORDER;
    }
    
    default ChronoLocalDate from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDate) {
            return (ChronoLocalDate)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            throw new DateTimeException("Unable to obtain ChronoLocalDate from TemporalAccessor: " + temporalAccessor.getClass());
        }
        return chronology.date(temporalAccessor);
    }
    
    Chronology getChronology();
    
    default Era getEra() {
        return this.getChronology().eraOf(this.get(ChronoField.ERA));
    }
    
    default boolean isLeapYear() {
        return this.getChronology().isLeapYear(this.getLong(ChronoField.YEAR));
    }
    
    int lengthOfMonth();
    
    default int lengthOfYear() {
        return this.isLeapYear() ? 366 : 365;
    }
    
    default boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isDateBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    default boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isDateBased();
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    default ChronoLocalDate with(final TemporalAdjuster temporalAdjuster) {
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), super.with(temporalAdjuster));
    }
    
    default ChronoLocalDate with(final TemporalField temporalField, final long n) {
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), temporalField.adjustInto(this, n));
    }
    
    default ChronoLocalDate plus(final TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), super.plus(temporalAmount));
    }
    
    default ChronoLocalDate plus(final long n, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
        }
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), temporalUnit.addTo(this, n));
    }
    
    default ChronoLocalDate minus(final TemporalAmount temporalAmount) {
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), super.minus(temporalAmount));
    }
    
    default ChronoLocalDate minus(final long n, final TemporalUnit temporalUnit) {
        return ChronoLocalDateImpl.ensureValid(this.getChronology(), super.minus(n, temporalUnit));
    }
    
    default <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)this.getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.DAYS;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    default Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, this.toEpochDay());
    }
    
    long until(final Temporal p0, final TemporalUnit p1);
    
    ChronoPeriod until(final ChronoLocalDate p0);
    
    default String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    default ChronoLocalDateTime<?> atTime(final LocalTime localTime) {
        return ChronoLocalDateTimeImpl.of(this, localTime);
    }
    
    default long toEpochDay() {
        return this.getLong(ChronoField.EPOCH_DAY);
    }
    
    default int compareTo(final ChronoLocalDate chronoLocalDate) {
        int n = Long.compare(this.toEpochDay(), chronoLocalDate.toEpochDay());
        if (n == 0) {
            n = this.getChronology().compareTo(chronoLocalDate.getChronology());
        }
        return n;
    }
    
    default boolean isAfter(final ChronoLocalDate chronoLocalDate) {
        return this.toEpochDay() > chronoLocalDate.toEpochDay();
    }
    
    default boolean isBefore(final ChronoLocalDate chronoLocalDate) {
        return this.toEpochDay() < chronoLocalDate.toEpochDay();
    }
    
    default boolean isEqual(final ChronoLocalDate chronoLocalDate) {
        return this.toEpochDay() == chronoLocalDate.toEpochDay();
    }
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    String toString();
}
