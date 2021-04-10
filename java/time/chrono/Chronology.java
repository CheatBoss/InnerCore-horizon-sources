package java.time.chrono;

import java.time.*;
import java.time.temporal.*;
import java.util.*;
import java.time.format.*;

public interface Chronology extends Comparable<Chronology>
{
    default Chronology from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        return (chronology != null) ? chronology : IsoChronology.INSTANCE;
    }
    
    default Chronology ofLocale(final Locale locale) {
        return AbstractChronology.ofLocale(locale);
    }
    
    default Chronology of(final String s) {
        return AbstractChronology.of(s);
    }
    
    default Set<Chronology> getAvailableChronologies() {
        return AbstractChronology.getAvailableChronologies();
    }
    
    String getId();
    
    String getCalendarType();
    
    default ChronoLocalDate date(final Era era, final int n, final int n2, final int n3) {
        return this.date(this.prolepticYear(era, n), n2, n3);
    }
    
    ChronoLocalDate date(final int p0, final int p1, final int p2);
    
    default ChronoLocalDate dateYearDay(final Era era, final int n, final int n2) {
        return this.dateYearDay(this.prolepticYear(era, n), n2);
    }
    
    ChronoLocalDate dateYearDay(final int p0, final int p1);
    
    ChronoLocalDate dateEpochDay(final long p0);
    
    default ChronoLocalDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    default ChronoLocalDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    default ChronoLocalDate dateNow(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return this.date(LocalDate.now(clock));
    }
    
    ChronoLocalDate date(final TemporalAccessor p0);
    
    default ChronoLocalDateTime<? extends ChronoLocalDate> localDateTime(final TemporalAccessor temporalAccessor) {
        try {
            return (ChronoLocalDateTime<? extends ChronoLocalDate>)this.date(temporalAccessor).atTime(LocalTime.from(temporalAccessor));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporalAccessor.getClass(), ex);
        }
    }
    
    default ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime(final TemporalAccessor temporalAccessor) {
        try {
            final ZoneId from = ZoneId.from(temporalAccessor);
            try {
                return this.zonedDateTime(Instant.from(temporalAccessor), from);
            }
            catch (DateTimeException ex2) {
                return ChronoZonedDateTimeImpl.ofBest(ChronoLocalDateTimeImpl.ensureValid(this, this.localDateTime(temporalAccessor)), from, null);
            }
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + temporalAccessor.getClass(), ex);
        }
    }
    
    default ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<? extends ChronoLocalDate>)ChronoZonedDateTimeImpl.ofInstant(this, instant, zoneId);
    }
    
    boolean isLeapYear(final long p0);
    
    int prolepticYear(final Era p0, final int p1);
    
    Era eraOf(final int p0);
    
    List<Era> eras();
    
    ValueRange range(final ChronoField p0);
    
    default String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendChronologyText(textStyle).toFormatter(locale).format(new TemporalAccessor() {
            @Override
            public boolean isSupported(final TemporalField temporalField) {
                return false;
            }
            
            @Override
            public long getLong(final TemporalField temporalField) {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
            
            @Override
            public <R> R query(final TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return (R)Chronology.this;
                }
                return super.query(temporalQuery);
            }
        });
    }
    
    ChronoLocalDate resolveDate(final Map<TemporalField, Long> p0, final ResolverStyle p1);
    
    default ChronoPeriod period(final int n, final int n2, final int n3) {
        return new ChronoPeriodImpl(this, n, n2, n3);
    }
    
    int compareTo(final Chronology p0);
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    String toString();
}
