package java.time.chrono;

import java.util.*;
import sun.util.calendar.*;
import java.time.*;
import java.io.*;
import java.time.temporal.*;

public final class JapaneseDate extends ChronoLocalDateImpl<JapaneseDate> implements ChronoLocalDate, Serializable
{
    private static final long serialVersionUID = -305327627230580483L;
    private final transient LocalDate isoDate;
    private transient JapaneseEra era;
    private transient int yearOfEra;
    static final LocalDate MEIJI_6_ISODATE;
    
    public static JapaneseDate now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static JapaneseDate now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static JapaneseDate now(final Clock clock) {
        return new JapaneseDate(LocalDate.now(clock));
    }
    
    public static JapaneseDate of(final JapaneseEra japaneseEra, final int n, final int n2, final int n3) {
        Objects.requireNonNull(japaneseEra, "era");
        final LocalGregorianCalendar.Date calendarDate = JapaneseChronology.JCAL.newCalendarDate(null);
        calendarDate.setEra(japaneseEra.getPrivateEra()).setDate(n, n2, n3);
        if (!JapaneseChronology.JCAL.validate(calendarDate)) {
            throw new DateTimeException("year, month, and day not valid for Era");
        }
        return new JapaneseDate(japaneseEra, n, LocalDate.of(calendarDate.getNormalizedYear(), n2, n3));
    }
    
    public static JapaneseDate of(final int n, final int n2, final int n3) {
        return new JapaneseDate(LocalDate.of(n, n2, n3));
    }
    
    static JapaneseDate ofYearDay(final JapaneseEra japaneseEra, final int n, final int n2) {
        Objects.requireNonNull(japaneseEra, "era");
        final CalendarDate sinceDate = japaneseEra.getPrivateEra().getSinceDate();
        final LocalGregorianCalendar.Date calendarDate = JapaneseChronology.JCAL.newCalendarDate(null);
        calendarDate.setEra(japaneseEra.getPrivateEra());
        if (n == 1) {
            calendarDate.setDate(n, sinceDate.getMonth(), sinceDate.getDayOfMonth() + n2 - 1);
        }
        else {
            calendarDate.setDate(n, 1, n2);
        }
        JapaneseChronology.JCAL.normalize(calendarDate);
        if (japaneseEra.getPrivateEra() != calendarDate.getEra() || n != calendarDate.getYear()) {
            throw new DateTimeException("Invalid parameters");
        }
        return new JapaneseDate(japaneseEra, n, LocalDate.of(calendarDate.getNormalizedYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth()));
    }
    
    public static JapaneseDate from(final TemporalAccessor temporalAccessor) {
        return JapaneseChronology.INSTANCE.date(temporalAccessor);
    }
    
    JapaneseDate(final LocalDate isoDate) {
        if (isoDate.isBefore(JapaneseDate.MEIJI_6_ISODATE)) {
            throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
        }
        final LocalGregorianCalendar.Date privateJapaneseDate = toPrivateJapaneseDate(isoDate);
        this.era = JapaneseEra.toJapaneseEra(privateJapaneseDate.getEra());
        this.yearOfEra = privateJapaneseDate.getYear();
        this.isoDate = isoDate;
    }
    
    JapaneseDate(final JapaneseEra era, final int yearOfEra, final LocalDate isoDate) {
        if (isoDate.isBefore(JapaneseDate.MEIJI_6_ISODATE)) {
            throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
        }
        this.era = era;
        this.yearOfEra = yearOfEra;
        this.isoDate = isoDate;
    }
    
    @Override
    public JapaneseChronology getChronology() {
        return JapaneseChronology.INSTANCE;
    }
    
    @Override
    public JapaneseEra getEra() {
        return this.era;
    }
    
    @Override
    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }
    
    @Override
    public int lengthOfYear() {
        final Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
        instance.set(0, this.era.getValue() + 2);
        instance.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
        return instance.getActualMaximum(6);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return temporalField != ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH && temporalField != ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR && temporalField != ChronoField.ALIGNED_WEEK_OF_MONTH && temporalField != ChronoField.ALIGNED_WEEK_OF_YEAR && super.isSupported(temporalField);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (!this.isSupported(temporalField)) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        switch (chronoField) {
            case DAY_OF_MONTH: {
                return ValueRange.of(1L, this.lengthOfMonth());
            }
            case DAY_OF_YEAR: {
                return ValueRange.of(1L, this.lengthOfYear());
            }
            case YEAR_OF_ERA: {
                final Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
                instance.set(0, this.era.getValue() + 2);
                instance.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
                return ValueRange.of(1L, instance.getActualMaximum(1));
            }
            default: {
                return this.getChronology().range(chronoField);
            }
        }
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField)temporalField) {
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
            case ALIGNED_WEEK_OF_MONTH:
            case ALIGNED_WEEK_OF_YEAR: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
            case YEAR_OF_ERA: {
                return this.yearOfEra;
            }
            case ERA: {
                return this.era.getValue();
            }
            case DAY_OF_YEAR: {
                final Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
                instance.set(0, this.era.getValue() + 2);
                instance.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
                return instance.get(6);
            }
            default: {
                return this.isoDate.getLong(temporalField);
            }
        }
    }
    
    private static LocalGregorianCalendar.Date toPrivateJapaneseDate(final LocalDate localDate) {
        final LocalGregorianCalendar.Date calendarDate = JapaneseChronology.JCAL.newCalendarDate(null);
        final Era privateEra = JapaneseEra.privateEraFrom(localDate);
        int year = localDate.getYear();
        if (privateEra != null) {
            year -= privateEra.getSinceDate().getYear() - 1;
        }
        calendarDate.setEra(privateEra).setYear(year).setMonth(localDate.getMonthValue()).setDayOfMonth(localDate.getDayOfMonth());
        JapaneseChronology.JCAL.normalize(calendarDate);
        return calendarDate;
    }
    
    @Override
    public JapaneseDate with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return super.with(temporalField, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        if (this.getLong(chronoField) == n) {
            return this;
        }
        Label_0163: {
            switch (chronoField) {
                case YEAR_OF_ERA:
                case ERA:
                case YEAR: {
                    final int checkValidIntValue = this.getChronology().range(chronoField).checkValidIntValue(n, chronoField);
                    switch (chronoField) {
                        case YEAR_OF_ERA: {
                            return this.withYear(checkValidIntValue);
                        }
                        case YEAR: {
                            return this.with(this.isoDate.withYear(checkValidIntValue));
                        }
                        case ERA: {
                            return this.withYear(JapaneseEra.of(checkValidIntValue), this.yearOfEra);
                        }
                        default: {
                            break Label_0163;
                        }
                    }
                    break;
                }
            }
        }
        return this.with(this.isoDate.with(temporalField, n));
    }
    
    @Override
    public JapaneseDate with(final TemporalAdjuster temporalAdjuster) {
        return super.with(temporalAdjuster);
    }
    
    @Override
    public JapaneseDate plus(final TemporalAmount temporalAmount) {
        return super.plus(temporalAmount);
    }
    
    @Override
    public JapaneseDate minus(final TemporalAmount temporalAmount) {
        return super.minus(temporalAmount);
    }
    
    private JapaneseDate withYear(final JapaneseEra japaneseEra, final int n) {
        return this.with(this.isoDate.withYear(JapaneseChronology.INSTANCE.prolepticYear(japaneseEra, n)));
    }
    
    private JapaneseDate withYear(final int n) {
        return this.withYear(this.getEra(), n);
    }
    
    @Override
    JapaneseDate plusYears(final long n) {
        return this.with(this.isoDate.plusYears(n));
    }
    
    @Override
    JapaneseDate plusMonths(final long n) {
        return this.with(this.isoDate.plusMonths(n));
    }
    
    @Override
    JapaneseDate plusWeeks(final long n) {
        return this.with(this.isoDate.plusWeeks(n));
    }
    
    @Override
    JapaneseDate plusDays(final long n) {
        return this.with(this.isoDate.plusDays(n));
    }
    
    @Override
    public JapaneseDate plus(final long n, final TemporalUnit temporalUnit) {
        return super.plus(n, temporalUnit);
    }
    
    @Override
    public JapaneseDate minus(final long n, final TemporalUnit temporalUnit) {
        return super.minus(n, temporalUnit);
    }
    
    @Override
    JapaneseDate minusYears(final long n) {
        return super.minusYears(n);
    }
    
    @Override
    JapaneseDate minusMonths(final long n) {
        return super.minusMonths(n);
    }
    
    @Override
    JapaneseDate minusWeeks(final long n) {
        return super.minusWeeks(n);
    }
    
    @Override
    JapaneseDate minusDays(final long n) {
        return super.minusDays(n);
    }
    
    private JapaneseDate with(final LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new JapaneseDate(localDate);
    }
    
    @Override
    public final ChronoLocalDateTime<JapaneseDate> atTime(final LocalTime localTime) {
        return (ChronoLocalDateTime<JapaneseDate>)super.atTime(localTime);
    }
    
    @Override
    public ChronoPeriod until(final ChronoLocalDate chronoLocalDate) {
        final Period until = this.isoDate.until(chronoLocalDate);
        return this.getChronology().period(until.getYears(), until.getMonths(), until.getDays());
    }
    
    @Override
    public long toEpochDay() {
        return this.isoDate.toEpochDay();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof JapaneseDate && this.isoDate.equals(((JapaneseDate)o).isoDate));
    }
    
    @Override
    public int hashCode() {
        return this.getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)4, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.get(ChronoField.YEAR));
        dataOutput.writeByte(this.get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(this.get(ChronoField.DAY_OF_MONTH));
    }
    
    static JapaneseDate readExternal(final DataInput dataInput) throws IOException {
        return JapaneseChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
    
    static {
        MEIJI_6_ISODATE = LocalDate.of(1873, 1, 1);
    }
}
