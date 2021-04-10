package java.time.chrono;

import java.util.*;
import java.time.*;
import java.io.*;
import java.time.temporal.*;

public final class ThaiBuddhistDate extends ChronoLocalDateImpl<ThaiBuddhistDate> implements ChronoLocalDate, Serializable
{
    private static final long serialVersionUID = -8722293800195731463L;
    private final transient LocalDate isoDate;
    
    public static ThaiBuddhistDate now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static ThaiBuddhistDate now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static ThaiBuddhistDate now(final Clock clock) {
        return new ThaiBuddhistDate(LocalDate.now(clock));
    }
    
    public static ThaiBuddhistDate of(final int n, final int n2, final int n3) {
        return new ThaiBuddhistDate(LocalDate.of(n - 543, n2, n3));
    }
    
    public static ThaiBuddhistDate from(final TemporalAccessor temporalAccessor) {
        return ThaiBuddhistChronology.INSTANCE.date(temporalAccessor);
    }
    
    ThaiBuddhistDate(final LocalDate isoDate) {
        Objects.requireNonNull(isoDate, "isoDate");
        this.isoDate = isoDate;
    }
    
    @Override
    public ThaiBuddhistChronology getChronology() {
        return ThaiBuddhistChronology.INSTANCE;
    }
    
    @Override
    public ThaiBuddhistEra getEra() {
        return (this.getProlepticYear() >= 1) ? ThaiBuddhistEra.BE : ThaiBuddhistEra.BEFORE_BE;
    }
    
    @Override
    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
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
            case DAY_OF_MONTH:
            case DAY_OF_YEAR:
            case ALIGNED_WEEK_OF_MONTH: {
                return this.isoDate.range(temporalField);
            }
            case YEAR_OF_ERA: {
                final ValueRange range = ChronoField.YEAR.range();
                return ValueRange.of(1L, (this.getProlepticYear() <= 0) ? (-(range.getMinimum() + 543L) + 1L) : (range.getMaximum() + 543L));
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
            case PROLEPTIC_MONTH: {
                return this.getProlepticMonth();
            }
            case YEAR_OF_ERA: {
                final int prolepticYear = this.getProlepticYear();
                return (prolepticYear >= 1) ? prolepticYear : (1 - prolepticYear);
            }
            case YEAR: {
                return this.getProlepticYear();
            }
            case ERA: {
                return (this.getProlepticYear() >= 1) ? 1 : 0;
            }
            default: {
                return this.isoDate.getLong(temporalField);
            }
        }
    }
    
    private long getProlepticMonth() {
        return this.getProlepticYear() * 12L + this.isoDate.getMonthValue() - 1L;
    }
    
    private int getProlepticYear() {
        return this.isoDate.getYear() + 543;
    }
    
    @Override
    public ThaiBuddhistDate with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return super.with(temporalField, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        if (this.getLong(chronoField) == n) {
            return this;
        }
        Label_0221: {
            switch (chronoField) {
                case PROLEPTIC_MONTH: {
                    this.getChronology().range(chronoField).checkValidValue(n, chronoField);
                    return this.plusMonths(n - this.getProlepticMonth());
                }
                case YEAR_OF_ERA:
                case YEAR:
                case ERA: {
                    final int checkValidIntValue = this.getChronology().range(chronoField).checkValidIntValue(n, chronoField);
                    switch (chronoField) {
                        case YEAR_OF_ERA: {
                            return this.with(this.isoDate.withYear(((this.getProlepticYear() >= 1) ? checkValidIntValue : (1 - checkValidIntValue)) - 543));
                        }
                        case YEAR: {
                            return this.with(this.isoDate.withYear(checkValidIntValue - 543));
                        }
                        case ERA: {
                            return this.with(this.isoDate.withYear(1 - this.getProlepticYear() - 543));
                        }
                        default: {
                            break Label_0221;
                        }
                    }
                    break;
                }
            }
        }
        return this.with(this.isoDate.with(temporalField, n));
    }
    
    @Override
    public ThaiBuddhistDate with(final TemporalAdjuster temporalAdjuster) {
        return super.with(temporalAdjuster);
    }
    
    @Override
    public ThaiBuddhistDate plus(final TemporalAmount temporalAmount) {
        return super.plus(temporalAmount);
    }
    
    @Override
    public ThaiBuddhistDate minus(final TemporalAmount temporalAmount) {
        return super.minus(temporalAmount);
    }
    
    @Override
    ThaiBuddhistDate plusYears(final long n) {
        return this.with(this.isoDate.plusYears(n));
    }
    
    @Override
    ThaiBuddhistDate plusMonths(final long n) {
        return this.with(this.isoDate.plusMonths(n));
    }
    
    @Override
    ThaiBuddhistDate plusWeeks(final long n) {
        return super.plusWeeks(n);
    }
    
    @Override
    ThaiBuddhistDate plusDays(final long n) {
        return this.with(this.isoDate.plusDays(n));
    }
    
    @Override
    public ThaiBuddhistDate plus(final long n, final TemporalUnit temporalUnit) {
        return super.plus(n, temporalUnit);
    }
    
    @Override
    public ThaiBuddhistDate minus(final long n, final TemporalUnit temporalUnit) {
        return super.minus(n, temporalUnit);
    }
    
    @Override
    ThaiBuddhistDate minusYears(final long n) {
        return super.minusYears(n);
    }
    
    @Override
    ThaiBuddhistDate minusMonths(final long n) {
        return super.minusMonths(n);
    }
    
    @Override
    ThaiBuddhistDate minusWeeks(final long n) {
        return super.minusWeeks(n);
    }
    
    @Override
    ThaiBuddhistDate minusDays(final long n) {
        return super.minusDays(n);
    }
    
    private ThaiBuddhistDate with(final LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new ThaiBuddhistDate(localDate);
    }
    
    @Override
    public final ChronoLocalDateTime<ThaiBuddhistDate> atTime(final LocalTime localTime) {
        return (ChronoLocalDateTime<ThaiBuddhistDate>)super.atTime(localTime);
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
        return this == o || (o instanceof ThaiBuddhistDate && this.isoDate.equals(((ThaiBuddhistDate)o).isoDate));
    }
    
    @Override
    public int hashCode() {
        return this.getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)8, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.get(ChronoField.YEAR));
        dataOutput.writeByte(this.get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(this.get(ChronoField.DAY_OF_MONTH));
    }
    
    static ThaiBuddhistDate readExternal(final DataInput dataInput) throws IOException {
        return ThaiBuddhistChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
