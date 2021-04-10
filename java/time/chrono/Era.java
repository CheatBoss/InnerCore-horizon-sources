package java.time.chrono;

import java.time.temporal.*;
import java.util.*;
import java.time.format.*;

public interface Era extends TemporalAccessor, TemporalAdjuster
{
    int getValue();
    
    default boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.ERA;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    default ValueRange range(final TemporalField temporalField) {
        return super.range(temporalField);
    }
    
    default int get(final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return this.getValue();
        }
        return super.get(temporalField);
    }
    
    default long getLong(final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return this.getValue();
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    default <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.ERAS;
        }
        return super.query(temporalQuery);
    }
    
    default Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.ERA, this.getValue());
    }
    
    default String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).format(this);
    }
}
