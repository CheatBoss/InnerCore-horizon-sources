package java.time.temporal;

import java.time.chrono.*;
import java.time.*;

public final class TemporalQueries
{
    static final TemporalQuery<ZoneId> ZONE_ID;
    static final TemporalQuery<Chronology> CHRONO;
    static final TemporalQuery<TemporalUnit> PRECISION;
    static final TemporalQuery<ZoneOffset> OFFSET;
    static final TemporalQuery<ZoneId> ZONE;
    static final TemporalQuery<LocalDate> LOCAL_DATE;
    static final TemporalQuery<LocalTime> LOCAL_TIME;
    
    private TemporalQueries() {
    }
    
    public static TemporalQuery<ZoneId> zoneId() {
        return TemporalQueries.ZONE_ID;
    }
    
    public static TemporalQuery<Chronology> chronology() {
        return TemporalQueries.CHRONO;
    }
    
    public static TemporalQuery<TemporalUnit> precision() {
        return TemporalQueries.PRECISION;
    }
    
    public static TemporalQuery<ZoneId> zone() {
        return TemporalQueries.ZONE;
    }
    
    public static TemporalQuery<ZoneOffset> offset() {
        return TemporalQueries.OFFSET;
    }
    
    public static TemporalQuery<LocalDate> localDate() {
        return TemporalQueries.LOCAL_DATE;
    }
    
    public static TemporalQuery<LocalTime> localTime() {
        return TemporalQueries.LOCAL_TIME;
    }
    
    static {
        ZONE_ID = (temporalAccessor -> temporalAccessor.query(TemporalQueries.ZONE_ID));
        CHRONO = (temporalAccessor2 -> temporalAccessor2.query(TemporalQueries.CHRONO));
        PRECISION = (temporalAccessor3 -> temporalAccessor3.query(TemporalQueries.PRECISION));
        OFFSET = (temporalAccessor4 -> {
            if (temporalAccessor4.isSupported(ChronoField.OFFSET_SECONDS)) {
                return ZoneOffset.ofTotalSeconds(temporalAccessor4.get(ChronoField.OFFSET_SECONDS));
            }
            else {
                return null;
            }
        });
        final ZoneId zoneId;
        ZONE = (temporalAccessor5 -> {
            zoneId = temporalAccessor5.query(TemporalQueries.ZONE_ID);
            return (ZoneOffset)((zoneId != null) ? zoneId : ((ZoneOffset)temporalAccessor5.query(TemporalQueries.OFFSET)));
        });
        LOCAL_DATE = (temporalAccessor6 -> {
            if (temporalAccessor6.isSupported(ChronoField.EPOCH_DAY)) {
                return LocalDate.ofEpochDay(temporalAccessor6.getLong(ChronoField.EPOCH_DAY));
            }
            else {
                return null;
            }
        });
        LOCAL_TIME = (temporalAccessor7 -> {
            if (temporalAccessor7.isSupported(ChronoField.NANO_OF_DAY)) {
                return LocalTime.ofNanoOfDay(temporalAccessor7.getLong(ChronoField.NANO_OF_DAY));
            }
            else {
                return null;
            }
        });
    }
}
