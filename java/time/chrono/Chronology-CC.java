package java.time.chrono;

import java.time.format.*;
import java.time.temporal.*;
import java.time.*;
import java.util.*;

public final class Chronology-CC
{
    public static ChronoLocalDate $default$date(final Chronology chronology, final Era era, final int n, final int n2, final int n3) {
        return chronology.date(chronology.prolepticYear(era, n), n2, n3);
    }
    
    public static ChronoLocalDate $default$dateNow(final Chronology chronology) {
        return chronology.dateNow(Clock.systemDefaultZone());
    }
    
    public static ChronoLocalDate $default$dateNow(final Chronology chronology, final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return chronology.date(LocalDate.now(clock));
    }
    
    public static ChronoLocalDate $default$dateNow(final Chronology chronology, final ZoneId zoneId) {
        return chronology.dateNow(Clock.system(zoneId));
    }
    
    public static ChronoLocalDate $default$dateYearDay(final Chronology chronology, final Era era, final int n, final int n2) {
        return chronology.dateYearDay(chronology.prolepticYear(era, n), n2);
    }
    
    public static String $default$getDisplayName(final Chronology chronology, final TextStyle textStyle, final Locale locale) {
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
                    return (R)chronology;
                }
                return super.query(temporalQuery);
            }
        });
    }
    
    public static ChronoLocalDateTime<? extends ChronoLocalDate> $default$localDateTime(final Chronology chronology, final TemporalAccessor temporalAccessor) {
        try {
            return (ChronoLocalDateTime<? extends ChronoLocalDate>)chronology.date(temporalAccessor).atTime(LocalTime.from(temporalAccessor));
        }
        catch (DateTimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to obtain ChronoLocalDateTime from TemporalAccessor: ");
            sb.append(temporalAccessor.getClass());
            throw new DateTimeException(sb.toString(), ex);
        }
    }
    
    public static ChronoPeriod $default$period(final Chronology chronology, final int n, final int n2, final int n3) {
        return new ChronoPeriodImpl(chronology, n, n2, n3);
    }
    
    public static ChronoZonedDateTime<? extends ChronoLocalDate> $default$zonedDateTime(final Chronology chronology, final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<? extends ChronoLocalDate>)ChronoZonedDateTimeImpl.ofInstant(chronology, instant, zoneId);
    }
    
    public static ChronoZonedDateTime<? extends ChronoLocalDate> $default$zonedDateTime(final Chronology chronology, final TemporalAccessor temporalAccessor) {
        try {
            final ZoneId from = ZoneId.from(temporalAccessor);
            try {
                return chronology.zonedDateTime(Instant.from(temporalAccessor), from);
            }
            catch (DateTimeException ex2) {
                return ChronoZonedDateTimeImpl.ofBest(ChronoLocalDateTimeImpl.ensureValid(chronology, chronology.localDateTime(temporalAccessor)), from, null);
            }
        }
        catch (DateTimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to obtain ChronoZonedDateTime from TemporalAccessor: ");
            sb.append(temporalAccessor.getClass());
            throw new DateTimeException(sb.toString(), ex);
        }
    }
    
    public static Chronology from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology;
        }
        return IsoChronology.INSTANCE;
    }
    
    public static Set<Chronology> getAvailableChronologies() {
        return AbstractChronology.getAvailableChronologies();
    }
    
    public static Chronology of(final String s) {
        return AbstractChronology.of(s);
    }
    
    public static Chronology ofLocale(final Locale locale) {
        return AbstractChronology.ofLocale(locale);
    }
}
