package java.time;

import java.time.chrono.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;

public enum Month implements TemporalAccessor, TemporalAdjuster
{
    JANUARY, 
    FEBRUARY, 
    MARCH, 
    APRIL, 
    MAY, 
    JUNE, 
    JULY, 
    AUGUST, 
    SEPTEMBER, 
    OCTOBER, 
    NOVEMBER, 
    DECEMBER;
    
    private static final Month[] ENUMS;
    
    public static Month of(final int n) {
        if (n < 1 || n > 12) {
            throw new DateTimeException("Invalid value for MonthOfYear: " + n);
        }
        return Month.ENUMS[n - 1];
    }
    
    public static Month from(TemporalAccessor from) {
        if (from instanceof Month) {
            return (Month)from;
        }
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(from))) {
                from = LocalDate.from(from);
            }
            return of(from.get(ChronoField.MONTH_OF_YEAR));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain Month from TemporalAccessor: " + from + " of type " + from.getClass().getName(), ex);
        }
    }
    
    public int getValue() {
        return this.ordinal() + 1;
    }
    
    public String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.MONTH_OF_YEAR, textStyle).toFormatter(locale).format(this);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.MONTH_OF_YEAR;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return temporalField.range();
        }
        return super.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return this.getValue();
        }
        return super.get(temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return this.getValue();
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    public Month plus(final long n) {
        return Month.ENUMS[(this.ordinal() + ((int)(n % 12L) + 12)) % 12];
    }
    
    public Month minus(final long n) {
        return this.plus(-(n % 12L));
    }
    
    public int length(final boolean b) {
        switch (this) {
            case FEBRUARY: {
                return b ? 29 : 28;
            }
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER: {
                return 30;
            }
            default: {
                return 31;
            }
        }
    }
    
    public int minLength() {
        switch (this) {
            case FEBRUARY: {
                return 28;
            }
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER: {
                return 30;
            }
            default: {
                return 31;
            }
        }
    }
    
    public int maxLength() {
        switch (this) {
            case FEBRUARY: {
                return 29;
            }
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER: {
                return 30;
            }
            default: {
                return 31;
            }
        }
    }
    
    public int firstDayOfYear(final boolean b) {
        final int n = b ? 1 : 0;
        switch (this) {
            case JANUARY: {
                return 1;
            }
            case FEBRUARY: {
                return 32;
            }
            case MARCH: {
                return 60 + n;
            }
            case APRIL: {
                return 91 + n;
            }
            case MAY: {
                return 121 + n;
            }
            case JUNE: {
                return 152 + n;
            }
            case JULY: {
                return 182 + n;
            }
            case AUGUST: {
                return 213 + n;
            }
            case SEPTEMBER: {
                return 244 + n;
            }
            case OCTOBER: {
                return 274 + n;
            }
            case NOVEMBER: {
                return 305 + n;
            }
            default: {
                return 335 + n;
            }
        }
    }
    
    public Month firstMonthOfQuarter() {
        return Month.ENUMS[this.ordinal() / 3 * 3];
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.MONTHS;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        if (!Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(ChronoField.MONTH_OF_YEAR, this.getValue());
    }
    
    static {
        ENUMS = values();
    }
}
