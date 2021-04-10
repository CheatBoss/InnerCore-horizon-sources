package java.time.temporal;

import java.util.*;
import java.time.*;

public interface TemporalAccessor
{
    boolean isSupported(final TemporalField p0);
    
    default ValueRange range(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            Objects.requireNonNull(temporalField, "field");
            return temporalField.rangeRefinedBy(this);
        }
        if (this.isSupported(temporalField)) {
            return temporalField.range();
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
    }
    
    default int get(final TemporalField temporalField) {
        final ValueRange range = this.range(temporalField);
        if (!range.isIntValue()) {
            throw new UnsupportedTemporalTypeException("Invalid field " + temporalField + " for get() method, use getLong() instead");
        }
        final long long1 = this.getLong(temporalField);
        if (!range.isValidValue(long1)) {
            throw new DateTimeException("Invalid value for " + temporalField + " (valid values " + range + "): " + long1);
        }
        return (int)long1;
    }
    
    long getLong(final TemporalField p0);
    
    default <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.precision()) {
            return null;
        }
        return (R)temporalQuery.queryFrom(this);
    }
}
