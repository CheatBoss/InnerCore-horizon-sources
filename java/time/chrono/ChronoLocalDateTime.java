package java.time.chrono;

import java.util.*;
import java.time.temporal.*;
import java.time.format.*;
import java.time.*;

public interface ChronoLocalDateTime<D extends ChronoLocalDate> extends Temporal, TemporalAdjuster, Comparable<ChronoLocalDateTime<?>>
{
    default Comparator<ChronoLocalDateTime<?>> timeLineOrder() {
        return (Comparator<ChronoLocalDateTime<?>>)AbstractChronology.DATE_TIME_ORDER;
    }
    
    default ChronoLocalDateTime<?> from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoLocalDateTime) {
            return (ChronoLocalDateTime<?>)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporalAccessor.getClass());
        }
        return chronology.localDateTime(temporalAccessor);
    }
    
    default Chronology getChronology() {
        return this.toLocalDate().getChronology();
    }
    
    D toLocalDate();
    
    LocalTime toLocalTime();
    
    boolean isSupported(final TemporalField p0);
    
    default boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit != ChronoUnit.FOREVER;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    default ChronoLocalDateTime<D> with(final TemporalAdjuster temporalAdjuster) {
        return (ChronoLocalDateTime<D>)ChronoLocalDateTimeImpl.ensureValid(this.getChronology(), super.with(temporalAdjuster));
    }
    
    ChronoLocalDateTime<D> with(final TemporalField p0, final long p1);
    
    default ChronoLocalDateTime<D> plus(final TemporalAmount temporalAmount) {
        return (ChronoLocalDateTime<D>)ChronoLocalDateTimeImpl.ensureValid(this.getChronology(), super.plus(temporalAmount));
    }
    
    ChronoLocalDateTime<D> plus(final long p0, final TemporalUnit p1);
    
    default ChronoLocalDateTime<D> minus(final TemporalAmount temporalAmount) {
        return (ChronoLocalDateTime<D>)ChronoLocalDateTimeImpl.ensureValid(this.getChronology(), super.minus(temporalAmount));
    }
    
    default ChronoLocalDateTime<D> minus(final long n, final TemporalUnit temporalUnit) {
        return (ChronoLocalDateTime<D>)ChronoLocalDateTimeImpl.ensureValid(this.getChronology(), super.minus(n, temporalUnit));
    }
    
    default <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)this.toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)this.getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    default Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, this.toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, this.toLocalTime().toNanoOfDay());
    }
    
    default String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    ChronoZonedDateTime<D> atZone(final ZoneId p0);
    
    default Instant toInstant(final ZoneOffset zoneOffset) {
        return Instant.ofEpochSecond(this.toEpochSecond(zoneOffset), this.toLocalTime().getNano());
    }
    
    default long toEpochSecond(final ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return this.toLocalDate().toEpochDay() * 86400L + this.toLocalTime().toSecondOfDay() - zoneOffset.getTotalSeconds();
    }
    
    default int compareTo(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        int n = this.toLocalDate().compareTo((ChronoLocalDate)chronoLocalDateTime.toLocalDate());
        if (n == 0) {
            n = this.toLocalTime().compareTo(chronoLocalDateTime.toLocalTime());
            if (n == 0) {
                n = this.getChronology().compareTo(chronoLocalDateTime.getChronology());
            }
        }
        return n;
    }
    
    default boolean isAfter(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        final long epochDay = this.toLocalDate().toEpochDay();
        final long epochDay2 = ((ChronoLocalDate)chronoLocalDateTime.toLocalDate()).toEpochDay();
        return epochDay > epochDay2 || (epochDay == epochDay2 && this.toLocalTime().toNanoOfDay() > chronoLocalDateTime.toLocalTime().toNanoOfDay());
    }
    
    default boolean isBefore(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        final long epochDay = this.toLocalDate().toEpochDay();
        final long epochDay2 = ((ChronoLocalDate)chronoLocalDateTime.toLocalDate()).toEpochDay();
        return epochDay < epochDay2 || (epochDay == epochDay2 && this.toLocalTime().toNanoOfDay() < chronoLocalDateTime.toLocalTime().toNanoOfDay());
    }
    
    default boolean isEqual(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        return this.toLocalTime().toNanoOfDay() == chronoLocalDateTime.toLocalTime().toNanoOfDay() && this.toLocalDate().toEpochDay() == ((ChronoLocalDate)chronoLocalDateTime.toLocalDate()).toEpochDay();
    }
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    String toString();
}
