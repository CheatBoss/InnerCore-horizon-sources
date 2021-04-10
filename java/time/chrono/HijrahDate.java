package java.time.chrono;

import java.time.*;
import java.io.*;
import java.time.temporal.*;

public final class HijrahDate extends ChronoLocalDateImpl<HijrahDate> implements ChronoLocalDate, Serializable
{
    private static final long serialVersionUID = -5207853542612002020L;
    private final transient HijrahChronology chrono;
    private final transient int prolepticYear;
    private final transient int monthOfYear;
    private final transient int dayOfMonth;
    
    static HijrahDate of(final HijrahChronology hijrahChronology, final int n, final int n2, final int n3) {
        return new HijrahDate(hijrahChronology, n, n2, n3);
    }
    
    static HijrahDate ofEpochDay(final HijrahChronology hijrahChronology, final long n) {
        return new HijrahDate(hijrahChronology, n);
    }
    
    public static HijrahDate now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static HijrahDate now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static HijrahDate now(final Clock clock) {
        return ofEpochDay(HijrahChronology.INSTANCE, LocalDate.now(clock).toEpochDay());
    }
    
    public static HijrahDate of(final int n, final int n2, final int n3) {
        return HijrahChronology.INSTANCE.date(n, n2, n3);
    }
    
    public static HijrahDate from(final TemporalAccessor temporalAccessor) {
        return HijrahChronology.INSTANCE.date(temporalAccessor);
    }
    
    private HijrahDate(final HijrahChronology chrono, final int prolepticYear, final int monthOfYear, final int dayOfMonth) {
        chrono.getEpochDay(prolepticYear, monthOfYear, dayOfMonth);
        this.chrono = chrono;
        this.prolepticYear = prolepticYear;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }
    
    private HijrahDate(final HijrahChronology chrono, final long n) {
        final int[] hijrahDateInfo = chrono.getHijrahDateInfo((int)n);
        this.chrono = chrono;
        this.prolepticYear = hijrahDateInfo[0];
        this.monthOfYear = hijrahDateInfo[1];
        this.dayOfMonth = hijrahDateInfo[2];
    }
    
    @Override
    public HijrahChronology getChronology() {
        return this.chrono;
    }
    
    @Override
    public HijrahEra getEra() {
        return HijrahEra.AH;
    }
    
    @Override
    public int lengthOfMonth() {
        return this.chrono.getMonthLength(this.prolepticYear, this.monthOfYear);
    }
    
    @Override
    public int lengthOfYear() {
        return this.chrono.getYearLength(this.prolepticYear);
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
            case ALIGNED_WEEK_OF_MONTH: {
                return ValueRange.of(1L, 5L);
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
            case DAY_OF_WEEK: {
                return this.getDayOfWeek();
            }
            case ALIGNED_DAY_OF_WEEK_IN_MONTH: {
                return (this.getDayOfWeek() - 1) % 7 + 1;
            }
            case ALIGNED_DAY_OF_WEEK_IN_YEAR: {
                return (this.getDayOfYear() - 1) % 7 + 1;
            }
            case DAY_OF_MONTH: {
                return this.dayOfMonth;
            }
            case DAY_OF_YEAR: {
                return this.getDayOfYear();
            }
            case EPOCH_DAY: {
                return this.toEpochDay();
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return (this.dayOfMonth - 1) / 7 + 1;
            }
            case ALIGNED_WEEK_OF_YEAR: {
                return (this.getDayOfYear() - 1) / 7 + 1;
            }
            case MONTH_OF_YEAR: {
                return this.monthOfYear;
            }
            case PROLEPTIC_MONTH: {
                return this.getProlepticMonth();
            }
            case YEAR_OF_ERA: {
                return this.prolepticYear;
            }
            case YEAR: {
                return this.prolepticYear;
            }
            case ERA: {
                return this.getEraValue();
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    private long getProlepticMonth() {
        return this.prolepticYear * 12L + this.monthOfYear - 1L;
    }
    
    @Override
    public HijrahDate with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return super.with(temporalField, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        this.chrono.range(chronoField).checkValidValue(n, chronoField);
        final int n2 = (int)n;
        switch (chronoField) {
            case DAY_OF_WEEK: {
                return this.plusDays(n - this.getDayOfWeek());
            }
            case ALIGNED_DAY_OF_WEEK_IN_MONTH: {
                return this.plusDays(n - this.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            }
            case ALIGNED_DAY_OF_WEEK_IN_YEAR: {
                return this.plusDays(n - this.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            }
            case DAY_OF_MONTH: {
                return this.resolvePreviousValid(this.prolepticYear, this.monthOfYear, n2);
            }
            case DAY_OF_YEAR: {
                return this.plusDays(Math.min(n2, this.lengthOfYear()) - this.getDayOfYear());
            }
            case EPOCH_DAY: {
                return new HijrahDate(this.chrono, n);
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return this.plusDays((n - this.getLong(ChronoField.ALIGNED_WEEK_OF_MONTH)) * 7L);
            }
            case ALIGNED_WEEK_OF_YEAR: {
                return this.plusDays((n - this.getLong(ChronoField.ALIGNED_WEEK_OF_YEAR)) * 7L);
            }
            case MONTH_OF_YEAR: {
                return this.resolvePreviousValid(this.prolepticYear, n2, this.dayOfMonth);
            }
            case PROLEPTIC_MONTH: {
                return this.plusMonths(n - this.getProlepticMonth());
            }
            case YEAR_OF_ERA: {
                return this.resolvePreviousValid((this.prolepticYear >= 1) ? n2 : (1 - n2), this.monthOfYear, this.dayOfMonth);
            }
            case YEAR: {
                return this.resolvePreviousValid(n2, this.monthOfYear, this.dayOfMonth);
            }
            case ERA: {
                return this.resolvePreviousValid(1 - this.prolepticYear, this.monthOfYear, this.dayOfMonth);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    private HijrahDate resolvePreviousValid(final int n, final int n2, int n3) {
        final int monthLength = this.chrono.getMonthLength(n, n2);
        if (n3 > monthLength) {
            n3 = monthLength;
        }
        return of(this.chrono, n, n2, n3);
    }
    
    @Override
    public HijrahDate with(final TemporalAdjuster temporalAdjuster) {
        return super.with(temporalAdjuster);
    }
    
    public HijrahDate withVariant(final HijrahChronology hijrahChronology) {
        if (this.chrono == hijrahChronology) {
            return this;
        }
        final int dayOfYear = hijrahChronology.getDayOfYear(this.prolepticYear, this.monthOfYear);
        return of(hijrahChronology, this.prolepticYear, this.monthOfYear, (this.dayOfMonth > dayOfYear) ? dayOfYear : this.dayOfMonth);
    }
    
    @Override
    public HijrahDate plus(final TemporalAmount temporalAmount) {
        return super.plus(temporalAmount);
    }
    
    @Override
    public HijrahDate minus(final TemporalAmount temporalAmount) {
        return super.minus(temporalAmount);
    }
    
    @Override
    public long toEpochDay() {
        return this.chrono.getEpochDay(this.prolepticYear, this.monthOfYear, this.dayOfMonth);
    }
    
    private int getDayOfYear() {
        return this.chrono.getDayOfYear(this.prolepticYear, this.monthOfYear) + this.dayOfMonth;
    }
    
    private int getDayOfWeek() {
        return (int)Math.floorMod(this.toEpochDay() + 3L, 7L) + 1;
    }
    
    private int getEraValue() {
        return (this.prolepticYear > 1) ? 1 : 0;
    }
    
    @Override
    public boolean isLeapYear() {
        return this.chrono.isLeapYear(this.prolepticYear);
    }
    
    @Override
    HijrahDate plusYears(final long n) {
        if (n == 0L) {
            return this;
        }
        return this.resolvePreviousValid(Math.addExact(this.prolepticYear, (int)n), this.monthOfYear, this.dayOfMonth);
    }
    
    @Override
    HijrahDate plusMonths(final long n) {
        if (n == 0L) {
            return this;
        }
        final long n2 = this.prolepticYear * 12L + (this.monthOfYear - 1) + n;
        return this.resolvePreviousValid(this.chrono.checkValidYear(Math.floorDiv(n2, 12L)), (int)Math.floorMod(n2, 12L) + 1, this.dayOfMonth);
    }
    
    @Override
    HijrahDate plusWeeks(final long n) {
        return super.plusWeeks(n);
    }
    
    @Override
    HijrahDate plusDays(final long n) {
        return new HijrahDate(this.chrono, this.toEpochDay() + n);
    }
    
    @Override
    public HijrahDate plus(final long n, final TemporalUnit temporalUnit) {
        return super.plus(n, temporalUnit);
    }
    
    @Override
    public HijrahDate minus(final long n, final TemporalUnit temporalUnit) {
        return super.minus(n, temporalUnit);
    }
    
    @Override
    HijrahDate minusYears(final long n) {
        return super.minusYears(n);
    }
    
    @Override
    HijrahDate minusMonths(final long n) {
        return super.minusMonths(n);
    }
    
    @Override
    HijrahDate minusWeeks(final long n) {
        return super.minusWeeks(n);
    }
    
    @Override
    HijrahDate minusDays(final long n) {
        return super.minusDays(n);
    }
    
    @Override
    public final ChronoLocalDateTime<HijrahDate> atTime(final LocalTime localTime) {
        return (ChronoLocalDateTime<HijrahDate>)super.atTime(localTime);
    }
    
    @Override
    public ChronoPeriod until(final ChronoLocalDate chronoLocalDate) {
        final HijrahDate date = this.getChronology().date(chronoLocalDate);
        long n = (date.prolepticYear - this.prolepticYear) * 12 + (date.monthOfYear - this.monthOfYear);
        int n2 = date.dayOfMonth - this.dayOfMonth;
        if (n > 0L && n2 < 0) {
            --n;
            n2 = (int)(date.toEpochDay() - this.plusMonths(n).toEpochDay());
        }
        else if (n < 0L && n2 > 0) {
            ++n;
            n2 -= date.lengthOfMonth();
        }
        return this.getChronology().period(Math.toIntExact(n / 12L), (int)(n % 12L), n2);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof HijrahDate) {
            final HijrahDate hijrahDate = (HijrahDate)o;
            return this.prolepticYear == hijrahDate.prolepticYear && this.monthOfYear == hijrahDate.monthOfYear && this.dayOfMonth == hijrahDate.dayOfMonth && this.getChronology().equals(hijrahDate.getChronology());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prolepticYear = this.prolepticYear;
        return this.getChronology().getId().hashCode() ^ (prolepticYear & 0xFFFFF800) ^ (prolepticYear << 11) + (this.monthOfYear << 6) + this.dayOfMonth;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)6, this);
    }
    
    void writeExternal(final ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.getChronology());
        objectOutput.writeInt(this.get(ChronoField.YEAR));
        objectOutput.writeByte(this.get(ChronoField.MONTH_OF_YEAR));
        objectOutput.writeByte(this.get(ChronoField.DAY_OF_MONTH));
    }
    
    static HijrahDate readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((HijrahChronology)objectInput.readObject()).date(objectInput.readInt(), objectInput.readByte(), objectInput.readByte());
    }
}
