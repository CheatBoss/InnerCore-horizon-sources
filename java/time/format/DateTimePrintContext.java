package java.time.format;

import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.*;

final class DateTimePrintContext
{
    private TemporalAccessor temporal;
    private DateTimeFormatter formatter;
    private int optional;
    
    DateTimePrintContext(final TemporalAccessor temporalAccessor, final DateTimeFormatter formatter) {
        this.temporal = adjust(temporalAccessor, formatter);
        this.formatter = formatter;
    }
    
    private static TemporalAccessor adjust(final TemporalAccessor temporalAccessor, final DateTimeFormatter dateTimeFormatter) {
        Chronology chronology = dateTimeFormatter.getChronology();
        ZoneId zone = dateTimeFormatter.getZone();
        if (chronology == null && zone == null) {
            return temporalAccessor;
        }
        final Chronology chronology2 = temporalAccessor.query(TemporalQueries.chronology());
        final ZoneId zoneId = temporalAccessor.query(TemporalQueries.zoneId());
        if (Objects.equals(chronology, chronology2)) {
            chronology = null;
        }
        if (Objects.equals(zone, zoneId)) {
            zone = null;
        }
        if (chronology == null && zone == null) {
            return temporalAccessor;
        }
        final IsoChronology isoChronology = (IsoChronology)((chronology != null) ? chronology : chronology2);
        if (zone != null) {
            if (temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                return ((isoChronology != null) ? isoChronology : IsoChronology.INSTANCE).zonedDateTime(Instant.from(temporalAccessor), zone);
            }
            if (zone.normalized() instanceof ZoneOffset && temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS) && temporalAccessor.get(ChronoField.OFFSET_SECONDS) != zone.getRules().getOffset(Instant.EPOCH).getTotalSeconds()) {
                throw new DateTimeException("Unable to apply override zone '" + zone + "' because the temporal object being formatted has a different offset but does not represent an instant: " + temporalAccessor);
            }
        }
        final ZoneId zoneId2 = (zone != null) ? zone : zoneId;
        ChronoLocalDate date;
        if (chronology != null) {
            if (temporalAccessor.isSupported(ChronoField.EPOCH_DAY)) {
                date = isoChronology.date(temporalAccessor);
            }
            else {
                if (chronology != IsoChronology.INSTANCE || chronology2 != null) {
                    for (final ChronoField chronoField : ChronoField.values()) {
                        if (chronoField.isDateBased() && temporalAccessor.isSupported(chronoField)) {
                            throw new DateTimeException("Unable to apply override chronology '" + chronology + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + temporalAccessor);
                        }
                    }
                }
                date = null;
            }
        }
        else {
            date = null;
        }
        return new TemporalAccessor() {
            @Override
            public boolean isSupported(final TemporalField temporalField) {
                if (date != null && temporalField.isDateBased()) {
                    return date.isSupported(temporalField);
                }
                return temporalAccessor.isSupported(temporalField);
            }
            
            @Override
            public ValueRange range(final TemporalField temporalField) {
                if (date != null && temporalField.isDateBased()) {
                    return date.range(temporalField);
                }
                return temporalAccessor.range(temporalField);
            }
            
            @Override
            public long getLong(final TemporalField temporalField) {
                if (date != null && temporalField.isDateBased()) {
                    return date.getLong(temporalField);
                }
                return temporalAccessor.getLong(temporalField);
            }
            
            @Override
            public <R> R query(final TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return (R)isoChronology;
                }
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return (R)zoneId2;
                }
                if (temporalQuery == TemporalQueries.precision()) {
                    return temporalAccessor.query(temporalQuery);
                }
                return (R)temporalQuery.queryFrom(this);
            }
        };
    }
    
    TemporalAccessor getTemporal() {
        return this.temporal;
    }
    
    Locale getLocale() {
        return this.formatter.getLocale();
    }
    
    DecimalStyle getDecimalStyle() {
        return this.formatter.getDecimalStyle();
    }
    
    void startOptional() {
        ++this.optional;
    }
    
    void endOptional() {
        --this.optional;
    }
    
     <R> R getValue(final TemporalQuery<R> temporalQuery) {
        final R query = this.temporal.query(temporalQuery);
        if (query == null && this.optional == 0) {
            throw new DateTimeException("Unable to extract value: " + this.temporal.getClass());
        }
        return query;
    }
    
    Long getValue(final TemporalField temporalField) {
        try {
            return this.temporal.getLong(temporalField);
        }
        catch (DateTimeException ex) {
            if (this.optional > 0) {
                return null;
            }
            throw ex;
        }
    }
    
    @Override
    public String toString() {
        return this.temporal.toString();
    }
}
