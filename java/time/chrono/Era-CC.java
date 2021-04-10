package java.time.chrono;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

public final class Era-CC
{
    public static Temporal $default$adjustInto(final Era era, final Temporal temporal) {
        return temporal.with(ChronoField.ERA, era.getValue());
    }
    
    public static int $default$get(final Era era, final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return era.getValue();
        }
        return TemporalAccessor-CC.$default$get(era, temporalField);
    }
    
    public static String $default$getDisplayName(final Era era, final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).format(era);
    }
    
    public static long $default$getLong(final Era era, final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return era.getValue();
        }
        if (temporalField instanceof ChronoField) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported field: ");
            sb.append(temporalField);
            throw new UnsupportedTemporalTypeException(sb.toString());
        }
        return temporalField.getFrom(era);
    }
    
    public static boolean $default$isSupported(final Era era, final TemporalField temporalField) {
        final boolean b = temporalField instanceof ChronoField;
        final boolean b2 = false;
        boolean b3 = false;
        if (b) {
            if (temporalField == ChronoField.ERA) {
                b3 = true;
            }
            return b3;
        }
        boolean b4 = b2;
        if (temporalField != null) {
            b4 = b2;
            if (temporalField.isSupportedBy(era)) {
                b4 = true;
            }
        }
        return b4;
    }
    
    public static <R> R $default$query(final Era era, final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.ERAS;
        }
        return TemporalAccessor-CC.$default$query(temporalQuery);
    }
    
    public static ValueRange $default$range(final Era era, final TemporalField temporalField) {
        return TemporalAccessor-CC.$default$range(era, temporalField);
    }
}
