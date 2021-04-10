package java.time.chrono;

import java.time.*;
import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;
import sun.util.calendar.*;

public final class JapaneseChronology extends AbstractChronology implements Serializable
{
    static final LocalGregorianCalendar JCAL;
    static final Locale LOCALE;
    public static final JapaneseChronology INSTANCE;
    private static final long serialVersionUID = 459996390165777884L;
    
    private JapaneseChronology() {
    }
    
    @Override
    public String getId() {
        return "Japanese";
    }
    
    @Override
    public String getCalendarType() {
        return "japanese";
    }
    
    @Override
    public JapaneseDate date(final Era era, final int n, final int n2, final int n3) {
        if (!(era instanceof JapaneseEra)) {
            throw new ClassCastException("Era must be JapaneseEra");
        }
        return JapaneseDate.of((JapaneseEra)era, n, n2, n3);
    }
    
    @Override
    public JapaneseDate date(final int n, final int n2, final int n3) {
        return new JapaneseDate(LocalDate.of(n, n2, n3));
    }
    
    @Override
    public JapaneseDate dateYearDay(final Era era, final int n, final int n2) {
        return JapaneseDate.ofYearDay((JapaneseEra)era, n, n2);
    }
    
    @Override
    public JapaneseDate dateYearDay(final int n, final int n2) {
        return new JapaneseDate(LocalDate.ofYearDay(n, n2));
    }
    
    @Override
    public JapaneseDate dateEpochDay(final long n) {
        return new JapaneseDate(LocalDate.ofEpochDay(n));
    }
    
    @Override
    public JapaneseDate dateNow() {
        return this.dateNow(Clock.systemDefaultZone());
    }
    
    @Override
    public JapaneseDate dateNow(final ZoneId zoneId) {
        return this.dateNow(Clock.system(zoneId));
    }
    
    @Override
    public JapaneseDate dateNow(final Clock clock) {
        return this.date(LocalDate.now(clock));
    }
    
    @Override
    public JapaneseDate date(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof JapaneseDate) {
            return (JapaneseDate)temporalAccessor;
        }
        return new JapaneseDate(LocalDate.from(temporalAccessor));
    }
    
    @Override
    public ChronoLocalDateTime<JapaneseDate> localDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoLocalDateTime<JapaneseDate>)super.localDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(final TemporalAccessor temporalAccessor) {
        return (ChronoZonedDateTime<JapaneseDate>)super.zonedDateTime(temporalAccessor);
    }
    
    @Override
    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTime<JapaneseDate>)super.zonedDateTime(instant, zoneId);
    }
    
    @Override
    public boolean isLeapYear(final long n) {
        return IsoChronology.INSTANCE.isLeapYear(n);
    }
    
    @Override
    public int prolepticYear(final Era era, final int n) {
        if (!(era instanceof JapaneseEra)) {
            throw new ClassCastException("Era must be JapaneseEra");
        }
        final JapaneseEra japaneseEra = (JapaneseEra)era;
        final int n2 = japaneseEra.getPrivateEra().getSinceDate().getYear() + n - 1;
        if (n == 1) {
            return n2;
        }
        if (n2 >= -999999999 && n2 <= 999999999) {
            final LocalGregorianCalendar.Date calendarDate = JapaneseChronology.JCAL.newCalendarDate(null);
            calendarDate.setEra(japaneseEra.getPrivateEra()).setDate(n, 1, 1);
            if (JapaneseChronology.JCAL.validate(calendarDate)) {
                return n2;
            }
        }
        throw new DateTimeException("Invalid yearOfEra value");
    }
    
    @Override
    public JapaneseEra eraOf(final int n) {
        return JapaneseEra.of(n);
    }
    
    @Override
    public List<Era> eras() {
        return (List<Era>)Arrays.asList(JapaneseEra.values());
    }
    
    JapaneseEra getCurrentEra() {
        final JapaneseEra[] values = JapaneseEra.values();
        return values[values.length - 1];
    }
    
    @Override
    public ValueRange range(final ChronoField chronoField) {
        switch (chronoField) {
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
            case ALIGNED_WEEK_OF_MONTH:
            case ALIGNED_WEEK_OF_YEAR: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + chronoField);
            }
            case YEAR_OF_ERA: {
                final Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
                return ValueRange.of(1L, instance.getGreatestMinimum(1), instance.getLeastMaximum(1) + 1, 999999999 - this.getCurrentEra().getPrivateEra().getSinceDate().getYear());
            }
            case DAY_OF_YEAR: {
                final Calendar instance2 = Calendar.getInstance(JapaneseChronology.LOCALE);
                final int n = 6;
                return ValueRange.of(instance2.getMinimum(n), instance2.getGreatestMinimum(n), instance2.getLeastMaximum(n), instance2.getMaximum(n));
            }
            case YEAR: {
                return ValueRange.of(JapaneseDate.MEIJI_6_ISODATE.getYear(), 999999999L);
            }
            case ERA: {
                return ValueRange.of(JapaneseEra.MEIJI.getValue(), this.getCurrentEra().getValue());
            }
            default: {
                return chronoField.range();
            }
        }
    }
    
    @Override
    public JapaneseDate resolveDate(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        return (JapaneseDate)super.resolveDate(map, resolverStyle);
    }
    
    @Override
    ChronoLocalDate resolveYearOfEra(final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        final Long n = map.get(ChronoField.ERA);
        JapaneseEra era = null;
        if (n != null) {
            era = this.eraOf(this.range(ChronoField.ERA).checkValidIntValue(n, ChronoField.ERA));
        }
        final Long n2 = map.get(ChronoField.YEAR_OF_ERA);
        int checkValidIntValue = 0;
        if (n2 != null) {
            checkValidIntValue = this.range(ChronoField.YEAR_OF_ERA).checkValidIntValue(n2, ChronoField.YEAR_OF_ERA);
        }
        if (era == null && n2 != null && !map.containsKey(ChronoField.YEAR) && resolverStyle != ResolverStyle.STRICT) {
            era = JapaneseEra.values()[JapaneseEra.values().length - 1];
        }
        if (n2 != null && era != null) {
            if (map.containsKey(ChronoField.MONTH_OF_YEAR) && map.containsKey(ChronoField.DAY_OF_MONTH)) {
                return this.resolveYMD(era, checkValidIntValue, map, resolverStyle);
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                return this.resolveYD(era, checkValidIntValue, map, resolverStyle);
            }
        }
        return null;
    }
    
    private int prolepticYearLenient(final JapaneseEra japaneseEra, final int n) {
        return japaneseEra.getPrivateEra().getSinceDate().getYear() + n - 1;
    }
    
    private ChronoLocalDate resolveYMD(final JapaneseEra japaneseEra, final int n, final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        map.remove(ChronoField.ERA);
        map.remove(ChronoField.YEAR_OF_ERA);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.date(this.prolepticYearLenient(japaneseEra, n), 1, 1).plus(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR), 1L), ChronoUnit.MONTHS).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH), 1L), ChronoUnit.DAYS);
        }
        final int checkValidIntValue = this.range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR), ChronoField.MONTH_OF_YEAR);
        final int checkValidIntValue2 = this.range(ChronoField.DAY_OF_MONTH).checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH), ChronoField.DAY_OF_MONTH);
        if (resolverStyle != ResolverStyle.SMART) {
            return this.date(japaneseEra, n, checkValidIntValue, checkValidIntValue2);
        }
        if (n < 1) {
            throw new DateTimeException("Invalid YearOfEra: " + n);
        }
        final int prolepticYearLenient = this.prolepticYearLenient(japaneseEra, n);
        JapaneseDate japaneseDate;
        try {
            japaneseDate = this.date(prolepticYearLenient, checkValidIntValue, checkValidIntValue2);
        }
        catch (DateTimeException ex) {
            japaneseDate = this.date(prolepticYearLenient, checkValidIntValue, 1).with(TemporalAdjusters.lastDayOfMonth());
        }
        if (japaneseDate.getEra() != japaneseEra && japaneseDate.get(ChronoField.YEAR_OF_ERA) > 1 && n > 1) {
            throw new DateTimeException("Invalid YearOfEra for Era: " + japaneseEra + " " + n);
        }
        return japaneseDate;
    }
    
    private ChronoLocalDate resolveYD(final JapaneseEra japaneseEra, final int n, final Map<TemporalField, Long> map, final ResolverStyle resolverStyle) {
        map.remove(ChronoField.ERA);
        map.remove(ChronoField.YEAR_OF_ERA);
        if (resolverStyle == ResolverStyle.LENIENT) {
            return this.dateYearDay(this.prolepticYearLenient(japaneseEra, n), 1).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_YEAR), 1L), ChronoUnit.DAYS);
        }
        return this.dateYearDay(japaneseEra, n, this.range(ChronoField.DAY_OF_YEAR).checkValidIntValue(map.remove(ChronoField.DAY_OF_YEAR), ChronoField.DAY_OF_YEAR));
    }
    
    @Override
    Object writeReplace() {
        return super.writeReplace();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    static {
        JCAL = (LocalGregorianCalendar)CalendarSystem.forName("japanese");
        LOCALE = Locale.forLanguageTag("ja-JP-u-ca-japanese");
        INSTANCE = new JapaneseChronology();
    }
}
