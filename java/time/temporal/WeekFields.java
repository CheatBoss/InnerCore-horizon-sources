package java.time.temporal;

import java.io.*;
import java.util.concurrent.*;
import java.time.chrono.*;
import java.time.format.*;
import java.time.*;
import sun.util.locale.provider.*;
import java.util.*;

public final class WeekFields implements Serializable
{
    private static final ConcurrentMap<String, WeekFields> CACHE;
    public static final WeekFields ISO;
    public static final WeekFields SUNDAY_START;
    public static final TemporalUnit WEEK_BASED_YEARS;
    private static final long serialVersionUID = -1177360819670808121L;
    private final DayOfWeek firstDayOfWeek;
    private final int minimalDays;
    private final transient TemporalField dayOfWeek;
    private final transient TemporalField weekOfMonth;
    private final transient TemporalField weekOfYear;
    private final transient TemporalField weekOfWeekBasedYear;
    private final transient TemporalField weekBasedYear;
    
    public static WeekFields of(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        locale = new Locale(locale.getLanguage(), locale.getCountry());
        return of(DayOfWeek.SUNDAY.plus(CalendarDataUtility.retrieveFirstDayOfWeek(locale) - 1), CalendarDataUtility.retrieveMinimalDaysInFirstWeek(locale));
    }
    
    public static WeekFields of(final DayOfWeek dayOfWeek, final int n) {
        final String string = dayOfWeek.toString() + n;
        WeekFields weekFields = WeekFields.CACHE.get(string);
        if (weekFields == null) {
            WeekFields.CACHE.putIfAbsent(string, new WeekFields(dayOfWeek, n));
            weekFields = WeekFields.CACHE.get(string);
        }
        return weekFields;
    }
    
    private WeekFields(final DayOfWeek firstDayOfWeek, final int minimalDays) {
        this.dayOfWeek = ComputedDayOfField.ofDayOfWeekField(this);
        this.weekOfMonth = ComputedDayOfField.ofWeekOfMonthField(this);
        this.weekOfYear = ComputedDayOfField.ofWeekOfYearField(this);
        this.weekOfWeekBasedYear = ComputedDayOfField.ofWeekOfWeekBasedYearField(this);
        this.weekBasedYear = ComputedDayOfField.ofWeekBasedYearField(this);
        Objects.requireNonNull(firstDayOfWeek, "firstDayOfWeek");
        if (minimalDays < 1 || minimalDays > 7) {
            throw new IllegalArgumentException("Minimal number of days is invalid");
        }
        this.firstDayOfWeek = firstDayOfWeek;
        this.minimalDays = minimalDays;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, InvalidObjectException {
        objectInputStream.defaultReadObject();
        if (this.firstDayOfWeek == null) {
            throw new InvalidObjectException("firstDayOfWeek is null");
        }
        if (this.minimalDays < 1 || this.minimalDays > 7) {
            throw new InvalidObjectException("Minimal number of days is invalid");
        }
    }
    
    private Object readResolve() throws InvalidObjectException {
        try {
            return of(this.firstDayOfWeek, this.minimalDays);
        }
        catch (IllegalArgumentException ex) {
            throw new InvalidObjectException("Invalid serialized WeekFields: " + ex.getMessage());
        }
    }
    
    public DayOfWeek getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }
    
    public int getMinimalDaysInFirstWeek() {
        return this.minimalDays;
    }
    
    public TemporalField dayOfWeek() {
        return this.dayOfWeek;
    }
    
    public TemporalField weekOfMonth() {
        return this.weekOfMonth;
    }
    
    public TemporalField weekOfYear() {
        return this.weekOfYear;
    }
    
    public TemporalField weekOfWeekBasedYear() {
        return this.weekOfWeekBasedYear;
    }
    
    public TemporalField weekBasedYear() {
        return this.weekBasedYear;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof WeekFields && this.hashCode() == o.hashCode());
    }
    
    @Override
    public int hashCode() {
        return this.firstDayOfWeek.ordinal() * 7 + this.minimalDays;
    }
    
    @Override
    public String toString() {
        return "WeekFields[" + this.firstDayOfWeek + ',' + this.minimalDays + ']';
    }
    
    static {
        CACHE = new ConcurrentHashMap<String, WeekFields>(4, 0.75f, 2);
        ISO = new WeekFields(DayOfWeek.MONDAY, 4);
        SUNDAY_START = of(DayOfWeek.SUNDAY, 1);
        WEEK_BASED_YEARS = IsoFields.WEEK_BASED_YEARS;
    }
    
    static class ComputedDayOfField implements TemporalField
    {
        private final String name;
        private final WeekFields weekDef;
        private final TemporalUnit baseUnit;
        private final TemporalUnit rangeUnit;
        private final ValueRange range;
        private static final ValueRange DAY_OF_WEEK_RANGE;
        private static final ValueRange WEEK_OF_MONTH_RANGE;
        private static final ValueRange WEEK_OF_YEAR_RANGE;
        private static final ValueRange WEEK_OF_WEEK_BASED_YEAR_RANGE;
        
        static ComputedDayOfField ofDayOfWeekField(final WeekFields weekFields) {
            return new ComputedDayOfField("DayOfWeek", weekFields, ChronoUnit.DAYS, ChronoUnit.WEEKS, ComputedDayOfField.DAY_OF_WEEK_RANGE);
        }
        
        static ComputedDayOfField ofWeekOfMonthField(final WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfMonth", weekFields, ChronoUnit.WEEKS, ChronoUnit.MONTHS, ComputedDayOfField.WEEK_OF_MONTH_RANGE);
        }
        
        static ComputedDayOfField ofWeekOfYearField(final WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfYear", weekFields, ChronoUnit.WEEKS, ChronoUnit.YEARS, ComputedDayOfField.WEEK_OF_YEAR_RANGE);
        }
        
        static ComputedDayOfField ofWeekOfWeekBasedYearField(final WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfWeekBasedYear", weekFields, ChronoUnit.WEEKS, IsoFields.WEEK_BASED_YEARS, ComputedDayOfField.WEEK_OF_WEEK_BASED_YEAR_RANGE);
        }
        
        static ComputedDayOfField ofWeekBasedYearField(final WeekFields weekFields) {
            return new ComputedDayOfField("WeekBasedYear", weekFields, IsoFields.WEEK_BASED_YEARS, ChronoUnit.FOREVER, ChronoField.YEAR.range());
        }
        
        private ChronoLocalDate ofWeekBasedYear(final Chronology chronology, final int n, int min, final int n2) {
            final ChronoLocalDate date = chronology.date(n, 1, 1);
            final int startOfWeekOffset = this.startOfWeekOffset(1, this.localizedDayOfWeek(date));
            min = Math.min(min, this.computeWeek(startOfWeekOffset, date.lengthOfYear() + this.weekDef.getMinimalDaysInFirstWeek()) - 1);
            return date.plus((long)(-startOfWeekOffset + (n2 - 1) + (min - 1) * 7), (TemporalUnit)ChronoUnit.DAYS);
        }
        
        private ComputedDayOfField(final String name, final WeekFields weekDef, final TemporalUnit baseUnit, final TemporalUnit rangeUnit, final ValueRange range) {
            this.name = name;
            this.weekDef = weekDef;
            this.baseUnit = baseUnit;
            this.rangeUnit = rangeUnit;
            this.range = range;
        }
        
        @Override
        public long getFrom(final TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return this.localizedDayOfWeek(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return this.localizedWeekOfMonth(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return this.localizedWeekOfYear(temporalAccessor);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return this.localizedWeekOfWeekBasedYear(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return this.localizedWeekBasedYear(temporalAccessor);
            }
            throw new IllegalStateException("unreachable, rangeUnit: " + this.rangeUnit + ", this: " + this);
        }
        
        private int localizedDayOfWeek(final TemporalAccessor temporalAccessor) {
            return Math.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }
        
        private int localizedDayOfWeek(final int n) {
            return Math.floorMod(n - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }
        
        private long localizedWeekOfMonth(final TemporalAccessor temporalAccessor) {
            final int localizedDayOfWeek = this.localizedDayOfWeek(temporalAccessor);
            final int value = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
            return this.computeWeek(this.startOfWeekOffset(value, localizedDayOfWeek), value);
        }
        
        private long localizedWeekOfYear(final TemporalAccessor temporalAccessor) {
            final int localizedDayOfWeek = this.localizedDayOfWeek(temporalAccessor);
            final int value = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            return this.computeWeek(this.startOfWeekOffset(value, localizedDayOfWeek), value);
        }
        
        private int localizedWeekBasedYear(final TemporalAccessor temporalAccessor) {
            final int localizedDayOfWeek = this.localizedDayOfWeek(temporalAccessor);
            final int value = temporalAccessor.get(ChronoField.YEAR);
            final int value2 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            final int startOfWeekOffset = this.startOfWeekOffset(value2, localizedDayOfWeek);
            final int computeWeek = this.computeWeek(startOfWeekOffset, value2);
            if (computeWeek == 0) {
                return value - 1;
            }
            if (computeWeek >= this.computeWeek(startOfWeekOffset, (int)temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum() + this.weekDef.getMinimalDaysInFirstWeek())) {
                return value + 1;
            }
            return value;
        }
        
        private int localizedWeekOfWeekBasedYear(final TemporalAccessor temporalAccessor) {
            final int localizedDayOfWeek = this.localizedDayOfWeek(temporalAccessor);
            final int value = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            final int startOfWeekOffset = this.startOfWeekOffset(value, localizedDayOfWeek);
            int computeWeek = this.computeWeek(startOfWeekOffset, value);
            if (computeWeek == 0) {
                return this.localizedWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus((long)value, (TemporalUnit)ChronoUnit.DAYS));
            }
            if (computeWeek > 50) {
                final int computeWeek2 = this.computeWeek(startOfWeekOffset, (int)temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum() + this.weekDef.getMinimalDaysInFirstWeek());
                if (computeWeek >= computeWeek2) {
                    computeWeek = computeWeek - computeWeek2 + 1;
                }
            }
            return computeWeek;
        }
        
        private int startOfWeekOffset(final int n, final int n2) {
            final int floorMod = Math.floorMod(n - n2, 7);
            int n3 = -floorMod;
            if (floorMod + 1 > this.weekDef.getMinimalDaysInFirstWeek()) {
                n3 = 7 - floorMod;
            }
            return n3;
        }
        
        private int computeWeek(final int n, final int n2) {
            return (7 + n + (n2 - 1)) / 7;
        }
        
        @Override
        public <R extends Temporal> R adjustInto(final R r, final long n) {
            final int checkValidIntValue = this.range.checkValidIntValue(n, this);
            final int value = r.get(this);
            if (checkValidIntValue == value) {
                return r;
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return (R)this.ofWeekBasedYear(Chronology.from(r), (int)n, r.get(this.weekDef.weekOfWeekBasedYear), r.get(this.weekDef.dayOfWeek));
            }
            return (R)r.plus(checkValidIntValue - value, this.baseUnit);
        }
        
        @Override
        public ChronoLocalDate resolve(final Map<TemporalField, Long> map, final TemporalAccessor temporalAccessor, final ResolverStyle resolverStyle) {
            final long longValue = map.get(this);
            final int intExact = Math.toIntExact(longValue);
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                final long n = Math.floorMod(this.weekDef.getFirstDayOfWeek().getValue() - 1 + (this.range.checkValidIntValue(longValue, this) - 1), 7) + 1;
                map.remove(this);
                map.put(ChronoField.DAY_OF_WEEK, n);
                return null;
            }
            if (!map.containsKey(ChronoField.DAY_OF_WEEK)) {
                return null;
            }
            final int localizedDayOfWeek = this.localizedDayOfWeek(ChronoField.DAY_OF_WEEK.checkValidIntValue(map.get(ChronoField.DAY_OF_WEEK)));
            final Chronology from = Chronology.from(temporalAccessor);
            if (map.containsKey(ChronoField.YEAR)) {
                final int checkValidIntValue = ChronoField.YEAR.checkValidIntValue(map.get(ChronoField.YEAR));
                if (this.rangeUnit == ChronoUnit.MONTHS && map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                    return this.resolveWoM(map, from, checkValidIntValue, map.get(ChronoField.MONTH_OF_YEAR), intExact, localizedDayOfWeek, resolverStyle);
                }
                if (this.rangeUnit == ChronoUnit.YEARS) {
                    return this.resolveWoY(map, from, checkValidIntValue, intExact, localizedDayOfWeek, resolverStyle);
                }
            }
            else if ((this.rangeUnit == WeekFields.WEEK_BASED_YEARS || this.rangeUnit == ChronoUnit.FOREVER) && map.containsKey(this.weekDef.weekBasedYear) && map.containsKey(this.weekDef.weekOfWeekBasedYear)) {
                return this.resolveWBY(map, from, localizedDayOfWeek, resolverStyle);
            }
            return null;
        }
        
        private ChronoLocalDate resolveWoM(final Map<TemporalField, Long> map, final Chronology chronology, final int n, final long n2, final long n3, final int n4, final ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDate;
            if (resolverStyle == ResolverStyle.LENIENT) {
                final ChronoLocalDate plus = chronology.date(n, 1, 1).plus(Math.subtractExact(n2, 1L), (TemporalUnit)ChronoUnit.MONTHS);
                chronoLocalDate = plus.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(n3, this.localizedWeekOfMonth(plus)), 7L), n4 - this.localizedDayOfWeek(plus)), (TemporalUnit)ChronoUnit.DAYS);
            }
            else {
                final ChronoLocalDate date = chronology.date(n, ChronoField.MONTH_OF_YEAR.checkValidIntValue(n2), 1);
                chronoLocalDate = date.plus((long)((int)(this.range.checkValidIntValue(n3, this) - this.localizedWeekOfMonth(date)) * 7 + (n4 - this.localizedDayOfWeek(date))), (TemporalUnit)ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDate.getLong(ChronoField.MONTH_OF_YEAR) != n2) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.MONTH_OF_YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }
        
        private ChronoLocalDate resolveWoY(final Map<TemporalField, Long> map, final Chronology chronology, final int n, final long n2, final int n3, final ResolverStyle resolverStyle) {
            final ChronoLocalDate date = chronology.date(n, 1, 1);
            ChronoLocalDate chronoLocalDate;
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDate = date.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(n2, this.localizedWeekOfYear(date)), 7L), n3 - this.localizedDayOfWeek(date)), (TemporalUnit)ChronoUnit.DAYS);
            }
            else {
                chronoLocalDate = date.plus((long)((int)(this.range.checkValidIntValue(n2, this) - this.localizedWeekOfYear(date)) * 7 + (n3 - this.localizedDayOfWeek(date))), (TemporalUnit)ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDate.getLong(ChronoField.YEAR) != n) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }
        
        private ChronoLocalDate resolveWBY(final Map<TemporalField, Long> map, final Chronology chronology, final int n, final ResolverStyle resolverStyle) {
            final int checkValidIntValue = this.weekDef.weekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekBasedYear), this.weekDef.weekBasedYear);
            ChronoLocalDate chronoLocalDate;
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDate = this.ofWeekBasedYear(chronology, checkValidIntValue, 1, n).plus(Math.subtractExact(map.get(this.weekDef.weekOfWeekBasedYear), 1L), (TemporalUnit)ChronoUnit.WEEKS);
            }
            else {
                chronoLocalDate = this.ofWeekBasedYear(chronology, checkValidIntValue, this.weekDef.weekOfWeekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekOfWeekBasedYear), this.weekDef.weekOfWeekBasedYear), n);
                if (resolverStyle == ResolverStyle.STRICT && this.localizedWeekBasedYear(chronoLocalDate) != checkValidIntValue) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different week-based-year");
                }
            }
            map.remove(this);
            map.remove(this.weekDef.weekBasedYear);
            map.remove(this.weekDef.weekOfWeekBasedYear);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDate;
        }
        
        @Override
        public String getDisplayName(final Locale locale) {
            Objects.requireNonNull(locale, "locale");
            if (this.rangeUnit == ChronoUnit.YEARS) {
                final ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
                return javaTimeFormatData.containsKey("field.week") ? javaTimeFormatData.getString("field.week") : this.name;
            }
            return this.name;
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
        public boolean isDateBased() {
            return true;
        }
        
        @Override
        public boolean isTimeBased() {
            return false;
        }
        
        @Override
        public ValueRange range() {
            return this.range;
        }
        
        @Override
        public boolean isSupportedBy(final TemporalAccessor temporalAccessor) {
            if (temporalAccessor.isSupported(ChronoField.DAY_OF_WEEK)) {
                if (this.rangeUnit == ChronoUnit.WEEKS) {
                    return true;
                }
                if (this.rangeUnit == ChronoUnit.MONTHS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH);
                }
                if (this.rangeUnit == ChronoUnit.YEARS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
                }
                if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
                }
                if (this.rangeUnit == ChronoUnit.FOREVER) {
                    return temporalAccessor.isSupported(ChronoField.YEAR);
                }
            }
            return false;
        }
        
        @Override
        public ValueRange rangeRefinedBy(final TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return this.range;
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return this.rangeByWeek(temporalAccessor, ChronoField.DAY_OF_MONTH);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return this.rangeByWeek(temporalAccessor, ChronoField.DAY_OF_YEAR);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return this.rangeWeekOfWeekBasedYear(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return ChronoField.YEAR.range();
            }
            throw new IllegalStateException("unreachable, rangeUnit: " + this.rangeUnit + ", this: " + this);
        }
        
        private ValueRange rangeByWeek(final TemporalAccessor temporalAccessor, final TemporalField temporalField) {
            final int startOfWeekOffset = this.startOfWeekOffset(temporalAccessor.get(temporalField), this.localizedDayOfWeek(temporalAccessor));
            final ValueRange range = temporalAccessor.range(temporalField);
            return ValueRange.of(this.computeWeek(startOfWeekOffset, (int)range.getMinimum()), this.computeWeek(startOfWeekOffset, (int)range.getMaximum()));
        }
        
        private ValueRange rangeWeekOfWeekBasedYear(final TemporalAccessor temporalAccessor) {
            if (!temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR)) {
                return ComputedDayOfField.WEEK_OF_YEAR_RANGE;
            }
            final int localizedDayOfWeek = this.localizedDayOfWeek(temporalAccessor);
            final int value = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            final int startOfWeekOffset = this.startOfWeekOffset(value, localizedDayOfWeek);
            final int computeWeek = this.computeWeek(startOfWeekOffset, value);
            if (computeWeek == 0) {
                return this.rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus((long)(value + 7), (TemporalUnit)ChronoUnit.DAYS));
            }
            final int n = (int)temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum();
            final int computeWeek2 = this.computeWeek(startOfWeekOffset, n + this.weekDef.getMinimalDaysInFirstWeek());
            if (computeWeek >= computeWeek2) {
                return this.rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).plus((long)(n - value + 1 + 7), (TemporalUnit)ChronoUnit.DAYS));
            }
            return ValueRange.of(1L, computeWeek2 - 1);
        }
        
        @Override
        public String toString() {
            return this.name + "[" + this.weekDef.toString() + "]";
        }
        
        static {
            DAY_OF_WEEK_RANGE = ValueRange.of(1L, 7L);
            WEEK_OF_MONTH_RANGE = ValueRange.of(0L, 1L, 4L, 6L);
            WEEK_OF_YEAR_RANGE = ValueRange.of(0L, 1L, 52L, 54L);
            WEEK_OF_WEEK_BASED_YEAR_RANGE = ValueRange.of(1L, 52L, 53L);
        }
    }
}
