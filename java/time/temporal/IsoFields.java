package java.time.temporal;

import java.time.format.*;
import java.time.chrono.*;
import sun.util.locale.provider.*;
import java.util.*;
import java.time.*;

public final class IsoFields
{
    public static final TemporalField DAY_OF_QUARTER;
    public static final TemporalField QUARTER_OF_YEAR;
    public static final TemporalField WEEK_OF_WEEK_BASED_YEAR;
    public static final TemporalField WEEK_BASED_YEAR;
    public static final TemporalUnit WEEK_BASED_YEARS;
    public static final TemporalUnit QUARTER_YEARS;
    
    private IsoFields() {
        throw new AssertionError((Object)"Not instantiable");
    }
    
    static {
        DAY_OF_QUARTER = Field.DAY_OF_QUARTER;
        QUARTER_OF_YEAR = Field.QUARTER_OF_YEAR;
        WEEK_OF_WEEK_BASED_YEAR = Field.WEEK_OF_WEEK_BASED_YEAR;
        WEEK_BASED_YEAR = Field.WEEK_BASED_YEAR;
        WEEK_BASED_YEARS = Unit.WEEK_BASED_YEARS;
        QUARTER_YEARS = Unit.QUARTER_YEARS;
    }
    
    private enum Field implements TemporalField
    {
        DAY_OF_QUARTER {
            @Override
            public TemporalUnit getBaseUnit() {
                return ChronoUnit.DAYS;
            }
            
            @Override
            public TemporalUnit getRangeUnit() {
                return IsoFields.QUARTER_YEARS;
            }
            
            @Override
            public ValueRange range() {
                return ValueRange.of(1L, 90L, 92L);
            }
            
            @Override
            public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR) && temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && temporalAccessor.isSupported(ChronoField.YEAR) && isIso(temporalAccessor);
            }
            
            @Override
            public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
                }
                final long long1 = temporalAccessor.getLong(IsoFields$Field$1.QUARTER_OF_YEAR);
                if (long1 == 1L) {
                    return IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? ValueRange.of(1L, 91L) : ValueRange.of(1L, 90L);
                }
                if (long1 == 2L) {
                    return ValueRange.of(1L, 91L);
                }
                if (long1 == 3L || long1 == 4L) {
                    return ValueRange.of(1L, 92L);
                }
                return this.range();
            }
            
            @Override
            public long getFrom(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
                }
                return temporalAccessor.get(ChronoField.DAY_OF_YEAR) - Field.QUARTER_DAYS[(temporalAccessor.get(ChronoField.MONTH_OF_YEAR) - 1) / 3 + (IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? 4 : 0)];
            }
            
            @Override
            public <R extends Temporal> R adjustInto(final R r, final long n) {
                final long from = this.getFrom(r);
                this.range().checkValidValue(n, this);
                return (R)r.with(ChronoField.DAY_OF_YEAR, r.getLong(ChronoField.DAY_OF_YEAR) + (n - from));
            }
            
            @Override
            public ChronoLocalDate resolve(final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
                final Long n = map.get(ChronoField.YEAR);
                final Long n2 = map.get(IsoFields$Field$1.QUARTER_OF_YEAR);
                if (n == null || n2 == null) {
                    return null;
                }
                final int checkValidIntValue = ChronoField.YEAR.checkValidIntValue(n);
                final long longValue = map.get(IsoFields$Field$1.DAY_OF_QUARTER);
                ensureIso(temporalAccessor);
                LocalDate localDate;
                long subtractExact;
                if (resolverStyle == ResolverStyle.LENIENT) {
                    localDate = LocalDate.of(checkValidIntValue, 1, 1).plusMonths(Math.multiplyExact(Math.subtractExact(n2, 1L), 3L));
                    subtractExact = Math.subtractExact(longValue, 1L);
                }
                else {
                    localDate = LocalDate.of(checkValidIntValue, (IsoFields$Field$1.QUARTER_OF_YEAR.range().checkValidIntValue(n2, IsoFields$Field$1.QUARTER_OF_YEAR) - 1) * 3 + 1, 1);
                    if (longValue < 1L || longValue > 90L) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            this.rangeRefinedBy(localDate).checkValidValue(longValue, this);
                        }
                        else {
                            this.range().checkValidValue(longValue, this);
                        }
                    }
                    subtractExact = longValue - 1L;
                }
                map.remove(this);
                map.remove(ChronoField.YEAR);
                map.remove(IsoFields$Field$1.QUARTER_OF_YEAR);
                return localDate.plusDays(subtractExact);
            }
            
            @Override
            public String toString() {
                return "DayOfQuarter";
            }
        }, 
        QUARTER_OF_YEAR {
            @Override
            public TemporalUnit getBaseUnit() {
                return IsoFields.QUARTER_YEARS;
            }
            
            @Override
            public TemporalUnit getRangeUnit() {
                return ChronoUnit.YEARS;
            }
            
            @Override
            public ValueRange range() {
                return ValueRange.of(1L, 4L);
            }
            
            @Override
            public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && isIso(temporalAccessor);
            }
            
            @Override
            public long getFrom(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: QuarterOfYear");
                }
                return (temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR) + 2L) / 3L;
            }
            
            @Override
            public <R extends Temporal> R adjustInto(final R r, final long n) {
                final long from = this.getFrom(r);
                this.range().checkValidValue(n, this);
                return (R)r.with(ChronoField.MONTH_OF_YEAR, r.getLong(ChronoField.MONTH_OF_YEAR) + (n - from) * 3L);
            }
            
            @Override
            public String toString() {
                return "QuarterOfYear";
            }
        }, 
        WEEK_OF_WEEK_BASED_YEAR {
            @Override
            public String getDisplayName(final Locale locale) {
                Objects.requireNonNull(locale, "locale");
                final ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
                return javaTimeFormatData.containsKey("field.week") ? javaTimeFormatData.getString("field.week") : this.toString();
            }
            
            @Override
            public TemporalUnit getBaseUnit() {
                return ChronoUnit.WEEKS;
            }
            
            @Override
            public TemporalUnit getRangeUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }
            
            @Override
            public ValueRange range() {
                return ValueRange.of(1L, 52L, 53L);
            }
            
            @Override
            public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && isIso(temporalAccessor);
            }
            
            @Override
            public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
                }
                return getWeekRange(LocalDate.from(temporalAccessor));
            }
            
            @Override
            public long getFrom(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
                }
                return getWeek(LocalDate.from(temporalAccessor));
            }
            
            @Override
            public <R extends Temporal> R adjustInto(final R r, final long n) {
                this.range().checkValidValue(n, this);
                return (R)r.plus(Math.subtractExact(n, this.getFrom(r)), ChronoUnit.WEEKS);
            }
            
            @Override
            public ChronoLocalDate resolve(final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
                final Long n = map.get(IsoFields$Field$3.WEEK_BASED_YEAR);
                final Long n2 = map.get(ChronoField.DAY_OF_WEEK);
                if (n == null || n2 == null) {
                    return null;
                }
                final int checkValidIntValue = IsoFields$Field$3.WEEK_BASED_YEAR.range().checkValidIntValue(n, IsoFields$Field$3.WEEK_BASED_YEAR);
                final long longValue = map.get(IsoFields$Field$3.WEEK_OF_WEEK_BASED_YEAR);
                ensureIso(temporalAccessor);
                LocalDate localDate = LocalDate.of(checkValidIntValue, 1, 4);
                LocalDate localDate2;
                if (resolverStyle == ResolverStyle.LENIENT) {
                    long longValue2 = n2;
                    if (longValue2 > 7L) {
                        localDate = localDate.plusWeeks((longValue2 - 1L) / 7L);
                        longValue2 = (longValue2 - 1L) % 7L + 1L;
                    }
                    else if (longValue2 < 1L) {
                        localDate = localDate.plusWeeks(Math.subtractExact(longValue2, 7L) / 7L);
                        longValue2 = (longValue2 + 6L) % 7L + 1L;
                    }
                    localDate2 = localDate.plusWeeks(Math.subtractExact(longValue, 1L)).with((TemporalField)ChronoField.DAY_OF_WEEK, longValue2);
                }
                else {
                    final int checkValidIntValue2 = ChronoField.DAY_OF_WEEK.checkValidIntValue(n2);
                    if (longValue < 1L || longValue > 52L) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            getWeekRange(localDate).checkValidValue(longValue, this);
                        }
                        else {
                            this.range().checkValidValue(longValue, this);
                        }
                    }
                    localDate2 = localDate.plusWeeks(longValue - 1L).with((TemporalField)ChronoField.DAY_OF_WEEK, (long)checkValidIntValue2);
                }
                map.remove(this);
                map.remove(IsoFields$Field$3.WEEK_BASED_YEAR);
                map.remove(ChronoField.DAY_OF_WEEK);
                return localDate2;
            }
            
            @Override
            public String toString() {
                return "WeekOfWeekBasedYear";
            }
        }, 
        WEEK_BASED_YEAR {
            @Override
            public TemporalUnit getBaseUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }
            
            @Override
            public TemporalUnit getRangeUnit() {
                return ChronoUnit.FOREVER;
            }
            
            @Override
            public ValueRange range() {
                return ChronoField.YEAR.range();
            }
            
            @Override
            public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && isIso(temporalAccessor);
            }
            
            @Override
            public long getFrom(final TemporalAccessor temporalAccessor) {
                if (!this.isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
                }
                return getWeekBasedYear(LocalDate.from(temporalAccessor));
            }
            
            @Override
            public <R extends Temporal> R adjustInto(final R r, final long n) {
                if (!this.isSupportedBy(r)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
                }
                final int checkValidIntValue = this.range().checkValidIntValue(n, IsoFields$Field$4.WEEK_BASED_YEAR);
                final LocalDate from = LocalDate.from((TemporalAccessor)r);
                final int value = from.get(ChronoField.DAY_OF_WEEK);
                int access$500 = getWeek(from);
                if (access$500 == 53 && getWeekRange(checkValidIntValue) == 52) {
                    access$500 = 52;
                }
                final LocalDate of = LocalDate.of(checkValidIntValue, 1, 4);
                return (R)r.with(of.plusDays(value - of.get(ChronoField.DAY_OF_WEEK) + (access$500 - 1) * 7));
            }
            
            @Override
            public String toString() {
                return "WeekBasedYear";
            }
        };
        
        private static final int[] QUARTER_DAYS;
        
        @Override
        public boolean isDateBased() {
            return true;
        }
        
        @Override
        public boolean isTimeBased() {
            return false;
        }
        
        @Override
        public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
            return this.range();
        }
        
        private static boolean isIso(final TemporalAccessor temporalAccessor) {
            return Chronology.from(temporalAccessor).equals(IsoChronology.INSTANCE);
        }
        
        private static void ensureIso(final TemporalAccessor temporalAccessor) {
            if (!isIso(temporalAccessor)) {
                throw new DateTimeException("Resolve requires IsoChronology");
            }
        }
        
        private static ValueRange getWeekRange(final LocalDate localDate) {
            return ValueRange.of(1L, getWeekRange(getWeekBasedYear(localDate)));
        }
        
        private static int getWeekRange(final int n) {
            final LocalDate of = LocalDate.of(n, 1, 1);
            if (of.getDayOfWeek() == DayOfWeek.THURSDAY || (of.getDayOfWeek() == DayOfWeek.WEDNESDAY && of.isLeapYear())) {
                return 53;
            }
            return 52;
        }
        
        private static int getWeek(final LocalDate localDate) {
            final int ordinal = localDate.getDayOfWeek().ordinal();
            final int n = localDate.getDayOfYear() - 1;
            final int n2 = n + (3 - ordinal);
            int n3 = n2 - n2 / 7 * 7 - 3;
            if (n3 < -3) {
                n3 += 7;
            }
            if (n < n3) {
                return (int)getWeekRange(localDate.withDayOfYear(180).minusYears(1L)).getMaximum();
            }
            int n4 = (n - n3) / 7 + 1;
            if (n4 == 53 && n3 != -3 && (n3 != -2 || !localDate.isLeapYear())) {
                n4 = 1;
            }
            return n4;
        }
        
        private static int getWeekBasedYear(final LocalDate localDate) {
            int year = localDate.getYear();
            final int dayOfYear = localDate.getDayOfYear();
            if (dayOfYear <= 3) {
                if (dayOfYear - localDate.getDayOfWeek().ordinal() < -2) {
                    --year;
                }
            }
            else if (dayOfYear >= 363 && dayOfYear - 363 - (localDate.isLeapYear() ? 1 : 0) - localDate.getDayOfWeek().ordinal() >= 0) {
                ++year;
            }
            return year;
        }
        
        static {
            QUARTER_DAYS = new int[] { 0, 90, 181, 273, 0, 91, 182, 274 };
        }
    }
    
    private enum Unit implements TemporalUnit
    {
        WEEK_BASED_YEARS("WeekBasedYears", Duration.ofSeconds(31556952L)), 
        QUARTER_YEARS("QuarterYears", Duration.ofSeconds(7889238L));
        
        private final String name;
        private final Duration duration;
        
        private Unit(final String name, final Duration duration) {
            this.name = name;
            this.duration = duration;
        }
        
        @Override
        public Duration getDuration() {
            return this.duration;
        }
        
        @Override
        public boolean isDurationEstimated() {
            return true;
        }
        
        @Override
        public boolean isDateBased() {
            return true;
        }
        
        @Override
        public boolean isTimeBased() {
            return false;
        }
        
        @Override
        public boolean isSupportedBy(final Temporal temporal) {
            return temporal.isSupported(ChronoField.EPOCH_DAY);
        }
        
        @Override
        public <R extends Temporal> R addTo(final R r, final long n) {
            switch (this) {
                case WEEK_BASED_YEARS: {
                    return (R)r.with(IsoFields.WEEK_BASED_YEAR, Math.addExact(r.get(IsoFields.WEEK_BASED_YEAR), n));
                }
                case QUARTER_YEARS: {
                    return (R)r.plus(n / 4L, ChronoUnit.YEARS).plus(n % 4L * 3L, ChronoUnit.MONTHS);
                }
                default: {
                    throw new IllegalStateException("Unreachable");
                }
            }
        }
        
        @Override
        public long between(final Temporal temporal, final Temporal temporal2) {
            if (temporal.getClass() != temporal2.getClass()) {
                return temporal.until(temporal2, this);
            }
            switch (this) {
                case WEEK_BASED_YEARS: {
                    return Math.subtractExact(temporal2.getLong(IsoFields.WEEK_BASED_YEAR), temporal.getLong(IsoFields.WEEK_BASED_YEAR));
                }
                case QUARTER_YEARS: {
                    return temporal.until(temporal2, ChronoUnit.MONTHS) / 3L;
                }
                default: {
                    throw new IllegalStateException("Unreachable");
                }
            }
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
