package java.time.chrono;

import java.time.*;
import java.time.temporal.*;
import java.io.*;
import java.util.*;

final class ChronoPeriodImpl implements ChronoPeriod, Serializable
{
    private static final long serialVersionUID = 57387258289L;
    private static final List<TemporalUnit> SUPPORTED_UNITS;
    private final Chronology chrono;
    final int years;
    final int months;
    final int days;
    
    ChronoPeriodImpl(final Chronology chrono, final int years, final int months, final int days) {
        Objects.requireNonNull(chrono, "chrono");
        this.chrono = chrono;
        this.years = years;
        this.months = months;
        this.days = days;
    }
    
    @Override
    public long get(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return this.years;
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return this.months;
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return this.days;
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
    }
    
    @Override
    public List<TemporalUnit> getUnits() {
        return ChronoPeriodImpl.SUPPORTED_UNITS;
    }
    
    @Override
    public Chronology getChronology() {
        return this.chrono;
    }
    
    @Override
    public boolean isZero() {
        return this.years == 0 && this.months == 0 && this.days == 0;
    }
    
    @Override
    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }
    
    @Override
    public ChronoPeriod plus(final TemporalAmount temporalAmount) {
        final ChronoPeriodImpl validateAmount = this.validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.addExact(this.years, validateAmount.years), Math.addExact(this.months, validateAmount.months), Math.addExact(this.days, validateAmount.days));
    }
    
    @Override
    public ChronoPeriod minus(final TemporalAmount temporalAmount) {
        final ChronoPeriodImpl validateAmount = this.validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.subtractExact(this.years, validateAmount.years), Math.subtractExact(this.months, validateAmount.months), Math.subtractExact(this.days, validateAmount.days));
    }
    
    private ChronoPeriodImpl validateAmount(final TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, "amount");
        if (!(temporalAmount instanceof ChronoPeriodImpl)) {
            throw new DateTimeException("Unable to obtain ChronoPeriod from TemporalAmount: " + temporalAmount.getClass());
        }
        final ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl)temporalAmount;
        if (!this.chrono.equals(chronoPeriodImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronoPeriodImpl.getChronology().getId());
        }
        return chronoPeriodImpl;
    }
    
    @Override
    public ChronoPeriod multipliedBy(final int n) {
        if (this.isZero() || n == 1) {
            return this;
        }
        return new ChronoPeriodImpl(this.chrono, Math.multiplyExact(this.years, n), Math.multiplyExact(this.months, n), Math.multiplyExact(this.days, n));
    }
    
    @Override
    public ChronoPeriod normalized() {
        final long monthRange = this.monthRange();
        if (monthRange <= 0L) {
            return this;
        }
        final long n = this.years * monthRange + this.months;
        final long n2 = n / monthRange;
        final int n3 = (int)(n % monthRange);
        if (n2 == this.years && n3 == this.months) {
            return this;
        }
        return new ChronoPeriodImpl(this.chrono, Math.toIntExact(n2), n3, this.days);
    }
    
    private long monthRange() {
        final ValueRange range = this.chrono.range(ChronoField.MONTH_OF_YEAR);
        if (range.isFixed() && range.isIntValue()) {
            return range.getMaximum() - range.getMinimum() + 1L;
        }
        return -1L;
    }
    
    @Override
    public Temporal addTo(Temporal temporal) {
        this.validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.plus(this.years, ChronoUnit.YEARS);
            }
        }
        else {
            final long monthRange = this.monthRange();
            if (monthRange > 0L) {
                temporal = temporal.plus(this.years * monthRange + this.months, ChronoUnit.MONTHS);
            }
            else {
                if (this.years != 0) {
                    temporal = temporal.plus(this.years, ChronoUnit.YEARS);
                }
                temporal = temporal.plus(this.months, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.plus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }
    
    @Override
    public Temporal subtractFrom(Temporal temporal) {
        this.validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.minus(this.years, ChronoUnit.YEARS);
            }
        }
        else {
            final long monthRange = this.monthRange();
            if (monthRange > 0L) {
                temporal = temporal.minus(this.years * monthRange + this.months, ChronoUnit.MONTHS);
            }
            else {
                if (this.years != 0) {
                    temporal = temporal.minus(this.years, ChronoUnit.YEARS);
                }
                temporal = temporal.minus(this.months, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.minus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }
    
    private void validateChrono(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null && !this.chrono.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronology.getId());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ChronoPeriodImpl) {
            final ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl)o;
            return this.years == chronoPeriodImpl.years && this.months == chronoPeriodImpl.months && this.days == chronoPeriodImpl.days && this.chrono.equals(chronoPeriodImpl.chrono);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.years + Integer.rotateLeft(this.months, 8) + Integer.rotateLeft(this.days, 16) ^ this.chrono.hashCode();
    }
    
    @Override
    public String toString() {
        if (this.isZero()) {
            return this.getChronology().toString() + " P0D";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getChronology().toString()).append(' ').append('P');
        if (this.years != 0) {
            sb.append(this.years).append('Y');
        }
        if (this.months != 0) {
            sb.append(this.months).append('M');
        }
        if (this.days != 0) {
            sb.append(this.days).append('D');
        }
        return sb.toString();
    }
    
    protected Object writeReplace() {
        return new Ser((byte)9, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.chrono.getId());
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }
    
    static ChronoPeriodImpl readExternal(final DataInput dataInput) throws IOException {
        return new ChronoPeriodImpl(Chronology.of(dataInput.readUTF()), dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
    
    static {
        SUPPORTED_UNITS = Collections.unmodifiableList((List<? extends TemporalUnit>)Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS));
    }
}
