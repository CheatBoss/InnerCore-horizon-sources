package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

public enum HijrahEra implements Era
{
    AH;
    
    public static HijrahEra of(final int n) {
        if (n == 1) {
            return HijrahEra.AH;
        }
        throw new DateTimeException("Invalid era: " + n);
    }
    
    @Override
    public int getValue() {
        return 1;
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return ValueRange.of(1L, 1L);
        }
        return super.range(temporalField);
    }
    
    @Override
    public String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).withChronology(HijrahChronology.INSTANCE).format(HijrahDate.now());
    }
}
