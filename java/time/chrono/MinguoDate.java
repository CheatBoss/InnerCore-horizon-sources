package java.time.chrono;

import java.util.*;
import java.time.*;
import java.io.*;
import java.time.temporal.*;

public final class MinguoDate extends ChronoLocalDateImpl<MinguoDate> implements ChronoLocalDate, Serializable
{
    private static final long serialVersionUID = 1300372329181994526L;
    private final transient LocalDate isoDate;
    
    public static MinguoDate now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static MinguoDate now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static MinguoDate now(final Clock clock) {
        return new MinguoDate(LocalDate.now(clock));
    }
    
    public static MinguoDate of(final int n, final int n2, final int n3) {
        return new MinguoDate(LocalDate.of(n + 1911, n2, n3));
    }
    
    public static MinguoDate from(final TemporalAccessor temporalAccessor) {
        return MinguoChronology.INSTANCE.date(temporalAccessor);
    }
    
    MinguoDate(final LocalDate isoDate) {
        Objects.requireNonNull(isoDate, "isoDate");
        this.isoDate = isoDate;
    }
    
    @Override
    public MinguoChronology getChronology() {
        return MinguoChronology.INSTANCE;
    }
    
    @Override
    public MinguoEra getEra() {
        return (this.getProlepticYear() >= 1) ? MinguoEra.ROC : MinguoEra.BEFORE_ROC;
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
                return ValueRange.of(1L, (this.getProlepticYear() <= 0) ? (-range.getMinimum() + 1L + 1911L) : (range.getMaximum() - 1911L));
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
        return this.isoDate.getYear() - 1911;
    }
    
    @Override
    public MinguoDate with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return super.with(temporalField, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        if (this.getLong(chronoField) == n) {
            return this;
        }
        Label_0225: {
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
                            return this.with(this.isoDate.withYear((this.getProlepticYear() >= 1) ? (checkValidIntValue + 1911) : (1 - checkValidIntValue + 1911)));
                        }
                        case YEAR: {
                            return this.with(this.isoDate.withYear(checkValidIntValue + 1911));
                        }
                        case ERA: {
                            return this.with(this.isoDate.withYear(1 - this.getProlepticYear() + 1911));
                        }
                        default: {
                            break Label_0225;
                        }
                    }
                    break;
                }
            }
        }
        return this.with(this.isoDate.with(temporalField, n));
    }
    
    @Override
    public MinguoDate with(final TemporalAdjuster temporalAdjuster) {
        return super.with(temporalAdjuster);
    }
    
    @Override
    public MinguoDate plus(final TemporalAmount temporalAmount) {
        return super.plus(temporalAmount);
    }
    
    @Override
    public MinguoDate minus(final TemporalAmount temporalAmount) {
        return super.minus(temporalAmount);
    }
    
    @Override
    MinguoDate plusYears(final long n) {
        return this.with(this.isoDate.plusYears(n));
    }
    
    @Override
    MinguoDate plusMonths(final long n) {
        return this.with(this.isoDate.plusMonths(n));
    }
    
    @Override
    MinguoDate plusWeeks(final long n) {
        return super.plusWeeks(n);
    }
    
    @Override
    MinguoDate plusDays(final long n) {
        return this.with(this.isoDate.plusDays(n));
    }
    
    @Override
    public MinguoDate plus(final long n, final TemporalUnit temporalUnit) {
        return super.plus(n, temporalUnit);
    }
    
    @Override
    public MinguoDate minus(final long n, final TemporalUnit temporalUnit) {
        return super.minus(n, temporalUnit);
    }
    
    @Override
    MinguoDate minusYears(final long n) {
        return super.minusYears(n);
    }
    
    @Override
    MinguoDate minusMonths(final long n) {
        return super.minusMonths(n);
    }
    
    @Override
    MinguoDate minusWeeks(final long n) {
        return super.minusWeeks(n);
    }
    
    @Override
    MinguoDate minusDays(final long n) {
        return super.minusDays(n);
    }
    
    private MinguoDate with(final LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new MinguoDate(localDate);
    }
    
    @Override
    public final ChronoLocalDateTime<MinguoDate> atTime(final LocalTime localTime) {
        return (ChronoLocalDateTime<MinguoDate>)super.atTime(localTime);
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
        return this == o || (o instanceof MinguoDate && this.isoDate.equals(((MinguoDate)o).isoDate));
    }
    
    @Override
    public int hashCode() {
        return this.getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)7, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.get(ChronoField.YEAR));
        dataOutput.writeByte(this.get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(this.get(ChronoField.DAY_OF_MONTH));
    }
    
    static MinguoDate readExternal(final DataInput dataInput) throws IOException {
        return MinguoChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
