package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

public enum ThaiBuddhistEra implements Era
{
    BEFORE_BE, 
    BE;
    
    public static ThaiBuddhistEra of(final int n) {
        switch (n) {
            case 0: {
                return ThaiBuddhistEra.BEFORE_BE;
            }
            case 1: {
                return ThaiBuddhistEra.BE;
            }
            default: {
                throw new DateTimeException("Invalid era: " + n);
            }
        }
    }
    
    @Override
    public int getValue() {
        return this.ordinal();
    }
    
    @Override
    public String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).withChronology(ThaiBuddhistChronology.INSTANCE).format((this == ThaiBuddhistEra.BE) ? ThaiBuddhistDate.of(1, 1, 1) : ThaiBuddhistDate.of(0, 1, 1));
    }
}
