package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;

public final class Instant implements Temporal, TemporalAdjuster, Comparable<Instant>, Serializable
{
    public static final Instant EPOCH;
    private static final long MIN_SECOND = -31557014167219200L;
    private static final long MAX_SECOND = 31556889864403199L;
    public static final Instant MIN;
    public static final Instant MAX;
    private static final long serialVersionUID = -665713676816604388L;
    private final long seconds;
    private final int nanos;
    
    public static Instant now() {
        return Clock.systemUTC().instant();
    }
    
    public static Instant now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return clock.instant();
    }
    
    public static Instant ofEpochSecond(final long n) {
        return create(n, 0);
    }
    
    public static Instant ofEpochSecond(final long n, final long n2) {
        return create(Math.addExact(n, Math.floorDiv(n2, 1000000000L)), (int)Math.floorMod(n2, 1000000000L));
    }
    
    public static Instant ofEpochMilli(final long n) {
        return create(Math.floorDiv(n, 1000L), (int)Math.floorMod(n, 1000L) * 1000000);
    }
    
    public static Instant from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant) {
            return (Instant)temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        try {
            return ofEpochSecond(temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), temporalAccessor.get(ChronoField.NANO_OF_SECOND));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain Instant from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), ex);
        }
    }
    
    public static Instant parse(final CharSequence charSequence) {
        return DateTimeFormatter.ISO_INSTANT.parse(charSequence, Instant::from);
    }
    
    private static Instant create(final long n, final int n2) {
        if ((n | (long)n2) == 0x0L) {
            return Instant.EPOCH;
        }
        if (n < -31557014167219200L || n > 31556889864403199L) {
            throw new DateTimeException("Instant exceeds minimum or maximum instant");
        }
        return new Instant(n, n2);
    }
    
    private Instant(final long seconds, final int nanos) {
        this.seconds = seconds;
        this.nanos = nanos;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.NANO_OF_SECOND || temporalField == ChronoField.MICRO_OF_SECOND || temporalField == ChronoField.MILLI_OF_SECOND;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isTimeBased() || temporalUnit == ChronoUnit.DAYS;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        return super.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField)temporalField) {
                case NANO_OF_SECOND: {
                    return this.nanos;
                }
                case MICRO_OF_SECOND: {
                    return this.nanos / 1000;
                }
                case MILLI_OF_SECOND: {
                    return this.nanos / 1000000;
                }
                case INSTANT_SECONDS: {
                    ChronoField.INSTANT_SECONDS.checkValidIntValue(this.seconds);
                    break;
                }
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return this.range(temporalField).checkValidIntValue(temporalField.getFrom(this), temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField)temporalField) {
            case NANO_OF_SECOND: {
                return this.nanos;
            }
            case MICRO_OF_SECOND: {
                return this.nanos / 1000;
            }
            case MILLI_OF_SECOND: {
                return this.nanos / 1000000;
            }
            case INSTANT_SECONDS: {
                return this.seconds;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public long getEpochSecond() {
        return this.seconds;
    }
    
    public int getNano() {
        return this.nanos;
    }
    
    @Override
    public Instant with(final TemporalAdjuster temporalAdjuster) {
        return (Instant)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public Instant with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        chronoField.checkValidValue(n);
        switch (chronoField) {
            case MILLI_OF_SECOND: {
                final int n2 = (int)n * 1000000;
                return (n2 != this.nanos) ? create(this.seconds, n2) : this;
            }
            case MICRO_OF_SECOND: {
                final int n3 = (int)n * 1000;
                return (n3 != this.nanos) ? create(this.seconds, n3) : this;
            }
            case NANO_OF_SECOND: {
                return (n != this.nanos) ? create(this.seconds, (int)n) : this;
            }
            case INSTANT_SECONDS: {
                return (n != this.seconds) ? create(n, this.nanos) : this;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public Instant truncatedTo(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        final Duration duration = temporalUnit.getDuration();
        if (duration.getSeconds() > 86400L) {
            throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation");
        }
        final long nanos = duration.toNanos();
        if (86400000000000L % nanos != 0L) {
            throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder");
        }
        final long n = this.seconds % 86400L * 1000000000L + this.nanos;
        return this.plusNanos(n / nanos * nanos - n);
    }
    
    @Override
    public Instant plus(final TemporalAmount temporalAmount) {
        return (Instant)temporalAmount.addTo(this);
    }
    
    @Override
    public Instant plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.plusNanos(n);
            }
            case MICROS: {
                return this.plus(n / 1000000L, n % 1000000L * 1000L);
            }
            case MILLIS: {
                return this.plusMillis(n);
            }
            case SECONDS: {
                return this.plusSeconds(n);
            }
            case MINUTES: {
                return this.plusSeconds(Math.multiplyExact(n, 60L));
            }
            case HOURS: {
                return this.plusSeconds(Math.multiplyExact(n, 3600L));
            }
            case HALF_DAYS: {
                return this.plusSeconds(Math.multiplyExact(n, 43200L));
            }
            case DAYS: {
                return this.plusSeconds(Math.multiplyExact(n, 86400L));
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    public Instant plusSeconds(final long n) {
        return this.plus(n, 0L);
    }
    
    public Instant plusMillis(final long n) {
        return this.plus(n / 1000L, n % 1000L * 1000000L);
    }
    
    public Instant plusNanos(final long n) {
        return this.plus(0L, n);
    }
    
    private Instant plus(final long n, long n2) {
        if ((n | n2) == 0x0L) {
            return this;
        }
        final long addExact = Math.addExact(Math.addExact(this.seconds, n), n2 / 1000000000L);
        n2 %= 1000000000L;
        return ofEpochSecond(addExact, this.nanos + n2);
    }
    
    @Override
    public Instant minus(final TemporalAmount temporalAmount) {
        return (Instant)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public Instant minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public Instant minusSeconds(final long n) {
        if (n == Long.MIN_VALUE) {
            return this.plusSeconds(Long.MAX_VALUE).plusSeconds(1L);
        }
        return this.plusSeconds(-n);
    }
    
    public Instant minusMillis(final long n) {
        if (n == Long.MIN_VALUE) {
            return this.plusMillis(Long.MAX_VALUE).plusMillis(1L);
        }
        return this.plusMillis(-n);
    }
    
    public Instant minusNanos(final long n) {
        if (n == Long.MIN_VALUE) {
            return this.plusNanos(Long.MAX_VALUE).plusNanos(1L);
        }
        return this.plusNanos(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        if (temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.localDate() || temporalQuery == TemporalQueries.localTime()) {
            return null;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.INSTANT_SECONDS, this.seconds).with(ChronoField.NANO_OF_SECOND, this.nanos);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final Instant from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.nanosUntil(from);
            }
            case MICROS: {
                return this.nanosUntil(from) / 1000L;
            }
            case MILLIS: {
                return Math.subtractExact(from.toEpochMilli(), this.toEpochMilli());
            }
            case SECONDS: {
                return this.secondsUntil(from);
            }
            case MINUTES: {
                return this.secondsUntil(from) / 60L;
            }
            case HOURS: {
                return this.secondsUntil(from) / 3600L;
            }
            case HALF_DAYS: {
                return this.secondsUntil(from) / 43200L;
            }
            case DAYS: {
                return this.secondsUntil(from) / 86400L;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    private long nanosUntil(final Instant instant) {
        return Math.addExact(Math.multiplyExact(Math.subtractExact(instant.seconds, this.seconds), 1000000000L), instant.nanos - this.nanos);
    }
    
    private long secondsUntil(final Instant instant) {
        long subtractExact = Math.subtractExact(instant.seconds, this.seconds);
        final long n = instant.nanos - this.nanos;
        if (subtractExact > 0L && n < 0L) {
            --subtractExact;
        }
        else if (subtractExact < 0L && n > 0L) {
            ++subtractExact;
        }
        return subtractExact;
    }
    
    public OffsetDateTime atOffset(final ZoneOffset zoneOffset) {
        return OffsetDateTime.ofInstant(this, zoneOffset);
    }
    
    public ZonedDateTime atZone(final ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this, zoneId);
    }
    
    public long toEpochMilli() {
        if (this.seconds < 0L && this.nanos > 0) {
            return Math.addExact(Math.multiplyExact(this.seconds + 1L, 1000L), this.nanos / 1000000 - 1000);
        }
        return Math.addExact(Math.multiplyExact(this.seconds, 1000L), this.nanos / 1000000);
    }
    
    @Override
    public int compareTo(final Instant instant) {
        final int compare = Long.compare(this.seconds, instant.seconds);
        if (compare != 0) {
            return compare;
        }
        return this.nanos - instant.nanos;
    }
    
    public boolean isAfter(final Instant instant) {
        return this.compareTo(instant) > 0;
    }
    
    public boolean isBefore(final Instant instant) {
        return this.compareTo(instant) < 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Instant) {
            final Instant instant = (Instant)o;
            return this.seconds == instant.seconds && this.nanos == instant.nanos;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.seconds ^ this.seconds >>> 32) + 51 * this.nanos;
    }
    
    @Override
    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(this);
    }
    
    private Object writeReplace() {
        return new Ser((byte)2, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }
    
    static Instant readExternal(final DataInput dataInput) throws IOException {
        return ofEpochSecond(dataInput.readLong(), dataInput.readInt());
    }
    
    static {
        EPOCH = new Instant(0L, 0);
        MIN = ofEpochSecond(-31557014167219200L, 0L);
        MAX = ofEpochSecond(31556889864403199L, 999999999L);
    }
}
