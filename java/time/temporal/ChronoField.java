package java.time.temporal;

import sun.util.locale.provider.*;
import java.util.*;

public enum ChronoField implements TemporalField
{
    NANO_OF_SECOND("NanoOfSecond", (TemporalUnit)ChronoUnit.NANOS, (TemporalUnit)ChronoUnit.SECONDS, ValueRange.of(0L, 999999999L)), 
    NANO_OF_DAY("NanoOfDay", (TemporalUnit)ChronoUnit.NANOS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 86399999999999L)), 
    MICRO_OF_SECOND("MicroOfSecond", (TemporalUnit)ChronoUnit.MICROS, (TemporalUnit)ChronoUnit.SECONDS, ValueRange.of(0L, 999999L)), 
    MICRO_OF_DAY("MicroOfDay", (TemporalUnit)ChronoUnit.MICROS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 86399999999L)), 
    MILLI_OF_SECOND("MilliOfSecond", (TemporalUnit)ChronoUnit.MILLIS, (TemporalUnit)ChronoUnit.SECONDS, ValueRange.of(0L, 999L)), 
    MILLI_OF_DAY("MilliOfDay", (TemporalUnit)ChronoUnit.MILLIS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 86399999L)), 
    SECOND_OF_MINUTE("SecondOfMinute", (TemporalUnit)ChronoUnit.SECONDS, (TemporalUnit)ChronoUnit.MINUTES, ValueRange.of(0L, 59L), "second"), 
    SECOND_OF_DAY("SecondOfDay", (TemporalUnit)ChronoUnit.SECONDS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 86399L)), 
    MINUTE_OF_HOUR("MinuteOfHour", (TemporalUnit)ChronoUnit.MINUTES, (TemporalUnit)ChronoUnit.HOURS, ValueRange.of(0L, 59L), "minute"), 
    MINUTE_OF_DAY("MinuteOfDay", (TemporalUnit)ChronoUnit.MINUTES, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 1439L)), 
    HOUR_OF_AMPM("HourOfAmPm", (TemporalUnit)ChronoUnit.HOURS, (TemporalUnit)ChronoUnit.HALF_DAYS, ValueRange.of(0L, 11L)), 
    CLOCK_HOUR_OF_AMPM("ClockHourOfAmPm", (TemporalUnit)ChronoUnit.HOURS, (TemporalUnit)ChronoUnit.HALF_DAYS, ValueRange.of(1L, 12L)), 
    HOUR_OF_DAY("HourOfDay", (TemporalUnit)ChronoUnit.HOURS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 23L), "hour"), 
    CLOCK_HOUR_OF_DAY("ClockHourOfDay", (TemporalUnit)ChronoUnit.HOURS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(1L, 24L)), 
    AMPM_OF_DAY("AmPmOfDay", (TemporalUnit)ChronoUnit.HALF_DAYS, (TemporalUnit)ChronoUnit.DAYS, ValueRange.of(0L, 1L), "dayperiod"), 
    DAY_OF_WEEK("DayOfWeek", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.WEEKS, ValueRange.of(1L, 7L), "weekday"), 
    ALIGNED_DAY_OF_WEEK_IN_MONTH("AlignedDayOfWeekInMonth", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.WEEKS, ValueRange.of(1L, 7L)), 
    ALIGNED_DAY_OF_WEEK_IN_YEAR("AlignedDayOfWeekInYear", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.WEEKS, ValueRange.of(1L, 7L)), 
    DAY_OF_MONTH("DayOfMonth", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.MONTHS, ValueRange.of(1L, 28L, 31L), "day"), 
    DAY_OF_YEAR("DayOfYear", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.YEARS, ValueRange.of(1L, 365L, 366L)), 
    EPOCH_DAY("EpochDay", (TemporalUnit)ChronoUnit.DAYS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(-365249999634L, 365249999634L)), 
    ALIGNED_WEEK_OF_MONTH("AlignedWeekOfMonth", (TemporalUnit)ChronoUnit.WEEKS, (TemporalUnit)ChronoUnit.MONTHS, ValueRange.of(1L, 4L, 5L)), 
    ALIGNED_WEEK_OF_YEAR("AlignedWeekOfYear", (TemporalUnit)ChronoUnit.WEEKS, (TemporalUnit)ChronoUnit.YEARS, ValueRange.of(1L, 53L)), 
    MONTH_OF_YEAR("MonthOfYear", (TemporalUnit)ChronoUnit.MONTHS, (TemporalUnit)ChronoUnit.YEARS, ValueRange.of(1L, 12L), "month"), 
    PROLEPTIC_MONTH("ProlepticMonth", (TemporalUnit)ChronoUnit.MONTHS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(-11999999988L, 11999999999L)), 
    YEAR_OF_ERA("YearOfEra", (TemporalUnit)ChronoUnit.YEARS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(1L, 999999999L, 1000000000L)), 
    YEAR("Year", (TemporalUnit)ChronoUnit.YEARS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(-999999999L, 999999999L), "year"), 
    ERA("Era", (TemporalUnit)ChronoUnit.ERAS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(0L, 1L), "era"), 
    INSTANT_SECONDS("InstantSeconds", (TemporalUnit)ChronoUnit.SECONDS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(Long.MIN_VALUE, Long.MAX_VALUE)), 
    OFFSET_SECONDS("OffsetSeconds", (TemporalUnit)ChronoUnit.SECONDS, (TemporalUnit)ChronoUnit.FOREVER, ValueRange.of(-64800L, 64800L));
    
    private final String name;
    private final TemporalUnit baseUnit;
    private final TemporalUnit rangeUnit;
    private final ValueRange range;
    private final String displayNameKey;
    
    private ChronoField(final String name, final TemporalUnit baseUnit, final TemporalUnit rangeUnit, final ValueRange range) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.rangeUnit = rangeUnit;
        this.range = range;
        this.displayNameKey = null;
    }
    
    private ChronoField(final String name, final TemporalUnit baseUnit, final TemporalUnit rangeUnit, final ValueRange range, final String displayNameKey) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.rangeUnit = rangeUnit;
        this.range = range;
        this.displayNameKey = displayNameKey;
    }
    
    @Override
    public String getDisplayName(final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        if (this.displayNameKey == null) {
            return this.name;
        }
        final ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
        final String string = "field." + this.displayNameKey;
        return javaTimeFormatData.containsKey(string) ? javaTimeFormatData.getString(string) : this.name;
    }
    
    @Override
    public TemporalUnit getBaseUnit() {
        return this.baseUnit;
    }
    
    @Override
    public TemporalUnit getRangeUnit() {
        return this.rangeUnit;
    }
    
    @Override
    public ValueRange range() {
        return this.range;
    }
    
    @Override
    public boolean isDateBased() {
        return this.ordinal() >= ChronoField.DAY_OF_WEEK.ordinal() && this.ordinal() <= ChronoField.ERA.ordinal();
    }
    
    @Override
    public boolean isTimeBased() {
        return this.ordinal() < ChronoField.DAY_OF_WEEK.ordinal();
    }
    
    public long checkValidValue(final long n) {
        return this.range().checkValidValue(n, this);
    }
    
    public int checkValidIntValue(final long n) {
        return this.range().checkValidIntValue(n, this);
    }
    
    @Override
    public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
        return temporalAccessor.isSupported(this);
    }
    
    @Override
    public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
        return temporalAccessor.range(this);
    }
    
    @Override
    public long getFrom(final TemporalAccessor temporalAccessor) {
        return temporalAccessor.getLong(this);
    }
    
    @Override
    public <R extends Temporal> R adjustInto(final R r, final long n) {
        return (R)r.with(this, n);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
