package java.time.temporal;

import java.time.*;
import java.util.*;

public final class TemporalAccessor-CC
{
    public static int $default$get(final TemporalAccessor temporalAccessor, final TemporalField temporalField) {
        final ValueRange range = temporalAccessor.range(temporalField);
        if (!range.isIntValue()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid field ");
            sb.append(temporalField);
            sb.append(" for get() method, use getLong() instead");
            throw new UnsupportedTemporalTypeException(sb.toString());
        }
        final long long1 = temporalAccessor.getLong(temporalField);
        if (!range.isValidValue(long1)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid value for ");
            sb2.append(temporalField);
            sb2.append(" (valid values ");
            sb2.append(range);
            sb2.append("): ");
            sb2.append(long1);
            throw new DateTimeException(sb2.toString());
        }
        return (int)long1;
    }
    
    public static <R> R $default$query(final TemporalAccessor temporalAccessor, final TemporalQuery<R> temporalQuery) {
        if (temporalQuery != TemporalQueries.zoneId() && temporalQuery != TemporalQueries.chronology() && temporalQuery != TemporalQueries.precision()) {
            return (R)temporalQuery.queryFrom(temporalAccessor);
        }
        return null;
    }
    
    public static ValueRange $default$range(final TemporalAccessor temporalAccessor, final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            Objects.requireNonNull(temporalField, "field");
            return temporalField.rangeRefinedBy(temporalAccessor);
        }
        if (temporalAccessor.isSupported(temporalField)) {
            return temporalField.range();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unsupported field: ");
        sb.append(temporalField);
        throw new UnsupportedTemporalTypeException(sb.toString());
    }
}
