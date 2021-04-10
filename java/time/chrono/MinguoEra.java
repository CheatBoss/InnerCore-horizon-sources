package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

public enum MinguoEra implements Era
{
    BEFORE_ROC, 
    ROC;
    
    public static MinguoEra of(final int n) {
        switch (n) {
            case 0: {
                return MinguoEra.BEFORE_ROC;
            }
            case 1: {
                return MinguoEra.ROC;
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
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).withChronology(MinguoChronology.INSTANCE).format((this == MinguoEra.ROC) ? MinguoDate.of(1, 1, 1) : MinguoDate.of(0, 1, 1));
    }
}
