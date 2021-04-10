package java.time.chrono;

import java.util.*;
import java.time.temporal.*;
import java.time.format.*;
import java.time.*;

public interface ChronoZonedDateTime<D extends ChronoLocalDate> extends Temporal, Comparable<ChronoZonedDateTime<?>>
{
    default Comparator<ChronoZonedDateTime<?>> timeLineOrder() {
        return AbstractChronology.INSTANT_ORDER;
    }
    
    default ChronoZonedDateTime<?> from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoZonedDateTime) {
            return (ChronoZonedDateTime<?>)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + temporalAccessor.getClass());
        }
        return chronology.zonedDateTime(temporalAccessor);
    }
    
    default ValueRange range(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return this.toLocalDateTime().range(temporalField);
    }
    
    default int get(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        switch ((ChronoField)temporalField) {
            case INSTANT_SECONDS: {
                throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
            }
            case OFFSET_SECONDS: {
                return this.getOffset().getTotalSeconds();
            }
            default: {
                return this.toLocalDateTime().get(temporalField);
            }
        }
    }
    
    default long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField)temporalField) {
            case INSTANT_SECONDS: {
                return this.toEpochSecond();
            }
            case OFFSET_SECONDS: {
                return this.getOffset().getTotalSeconds();
            }
            default: {
                return this.toLocalDateTime().getLong(temporalField);
            }
        }
    }
    
    default D toLocalDate() {
        return this.toLocalDateTime().toLocalDate();
    }
    
    default LocalTime toLocalTime() {
        return this.toLocalDateTime().toLocalTime();
    }
    
    ChronoLocalDateTime<D> toLocalDateTime();
    
    default Chronology getChronology() {
        return this.toLocalDate().getChronology();
    }
    
    ZoneOffset getOffset();
    
    ZoneId getZone();
    
    ChronoZonedDateTime<D> withEarlierOffsetAtOverlap();
    
    ChronoZonedDateTime<D> withLaterOffsetAtOverlap();
    
    ChronoZonedDateTime<D> withZoneSameLocal(final ZoneId p0);
    
    ChronoZonedDateTime<D> withZoneSameInstant(final ZoneId p0);
    
    boolean isSupported(final TemporalField p0);
    
    default boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit != ChronoUnit.FOREVER;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    default ChronoZonedDateTime<D> with(final TemporalAdjuster temporalAdjuster) {
        return (ChronoZonedDateTime<D>)ChronoZonedDateTimeImpl.ensureValid(this.getChronology(), super.with(temporalAdjuster));
    }
    
    ChronoZonedDateTime<D> with(final TemporalField p0, final long p1);
    
    default ChronoZonedDateTime<D> plus(final TemporalAmount temporalAmount) {
        return (ChronoZonedDateTime<D>)ChronoZonedDateTimeImpl.ensureValid(this.getChronology(), super.plus(temporalAmount));
    }
    
    ChronoZonedDateTime<D> plus(final long p0, final TemporalUnit p1);
    
    default ChronoZonedDateTime<D> minus(final TemporalAmount temporalAmount) {
        return (ChronoZonedDateTime<D>)ChronoZonedDateTimeImpl.ensureValid(this.getChronology(), super.minus(temporalAmount));
    }
    
    default ChronoZonedDateTime<D> minus(final long n, final TemporalUnit temporalUnit) {
        return (ChronoZonedDateTime<D>)ChronoZonedDateTimeImpl.ensureValid(this.getChronology(), super.minus(n, temporalUnit));
    }
    
    default <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId()) {
            return (R)this.getZone();
        }
        if (temporalQuery == TemporalQueries.offset()) {
            return (R)this.getOffset();
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
    
    default String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    default Instant toInstant() {
        return Instant.ofEpochSecond(this.toEpochSecond(), this.toLocalTime().getNano());
    }
    
    default long toEpochSecond() {
        return this.toLocalDate().toEpochDay() * 86400L + this.toLocalTime().toSecondOfDay() - this.getOffset().getTotalSeconds();
    }
    
    default int compareTo(final ChronoZonedDateTime<?> chronoZonedDateTime) {
        int n = Long.compare(this.toEpochSecond(), chronoZonedDateTime.toEpochSecond());
        if (n == 0) {
            n = this.toLocalTime().getNano() - chronoZonedDateTime.toLocalTime().getNano();
            if (n == 0) {
                n = this.toLocalDateTime().compareTo(chronoZonedDateTime.toLocalDateTime());
                if (n == 0) {
                    n = this.getZone().getId().compareTo(chronoZonedDateTime.getZone().getId());
                    if (n == 0) {
                        n = this.getChronology().compareTo(chronoZonedDateTime.getChronology());
                    }
                }
            }
        }
        return n;
    }
    
    default boolean isBefore(final ChronoZonedDateTime<?> chronoZonedDateTime) {
        final long epochSecond = this.toEpochSecond();
        final long epochSecond2 = chronoZonedDateTime.toEpochSecond();
        return epochSecond < epochSecond2 || (epochSecond == epochSecond2 && this.toLocalTime().getNano() < chronoZonedDateTime.toLocalTime().getNano());
    }
    
    default boolean isAfter(final ChronoZonedDateTime<?> chronoZonedDateTime) {
        final long epochSecond = this.toEpochSecond();
        final long epochSecond2 = chronoZonedDateTime.toEpochSecond();
        return epochSecond > epochSecond2 || (epochSecond == epochSecond2 && this.toLocalTime().getNano() > chronoZonedDateTime.toLocalTime().getNano());
    }
    
    default boolean isEqual(final ChronoZonedDateTime<?> chronoZonedDateTime) {
        return this.toEpochSecond() == chronoZonedDateTime.toEpochSecond() && this.toLocalTime().getNano() == chronoZonedDateTime.toLocalTime().getNano();
    }
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    String toString();
}
