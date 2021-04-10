package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;

public final class LocalTime implements Temporal, TemporalAdjuster, Comparable<LocalTime>, Serializable
{
    public static final LocalTime MIN;
    public static final LocalTime MAX;
    public static final LocalTime MIDNIGHT;
    public static final LocalTime NOON;
    private static final LocalTime[] HOURS;
    static final int HOURS_PER_DAY = 24;
    static final int MINUTES_PER_HOUR = 60;
    static final int MINUTES_PER_DAY = 1440;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = 3600;
    static final int SECONDS_PER_DAY = 86400;
    static final long MILLIS_PER_DAY = 86400000L;
    static final long MICROS_PER_DAY = 86400000000L;
    static final long NANOS_PER_SECOND = 1000000000L;
    static final long NANOS_PER_MINUTE = 60000000000L;
    static final long NANOS_PER_HOUR = 3600000000000L;
    static final long NANOS_PER_DAY = 86400000000000L;
    private static final long serialVersionUID = 6414437269572265201L;
    private final byte hour;
    private final byte minute;
    private final byte second;
    private final int nano;
    
    public static LocalTime now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static LocalTime now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static LocalTime now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant instant = clock.instant();
        return ofNanoOfDay((int)Math.floorMod(instant.getEpochSecond() + clock.getZone().getRules().getOffset(instant).getTotalSeconds(), 86400L) * 1000000000L + instant.getNano());
    }
    
    public static LocalTime of(final int n, final int n2) {
        ChronoField.HOUR_OF_DAY.checkValidValue(n);
        if (n2 == 0) {
            return LocalTime.HOURS[n];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(n2);
        return new LocalTime(n, n2, 0, 0);
    }
    
    public static LocalTime of(final int n, final int n2, final int n3) {
        ChronoField.HOUR_OF_DAY.checkValidValue(n);
        if ((n2 | n3) == 0x0) {
            return LocalTime.HOURS[n];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(n2);
        ChronoField.SECOND_OF_MINUTE.checkValidValue(n3);
        return new LocalTime(n, n2, n3, 0);
    }
    
    public static LocalTime of(final int n, final int n2, final int n3, final int n4) {
        ChronoField.HOUR_OF_DAY.checkValidValue(n);
        ChronoField.MINUTE_OF_HOUR.checkValidValue(n2);
        ChronoField.SECOND_OF_MINUTE.checkValidValue(n3);
        ChronoField.NANO_OF_SECOND.checkValidValue(n4);
        return create(n, n2, n3, n4);
    }
    
    public static LocalTime ofSecondOfDay(long n) {
        ChronoField.SECOND_OF_DAY.checkValidValue(n);
        final int n2 = (int)(n / 3600L);
        n -= n2 * 3600;
        final int n3 = (int)(n / 60L);
        n -= n3 * 60;
        return create(n2, n3, (int)n, 0);
    }
    
    public static LocalTime ofNanoOfDay(long n) {
        ChronoField.NANO_OF_DAY.checkValidValue(n);
        final int n2 = (int)(n / 3600000000000L);
        n -= n2 * 3600000000000L;
        final int n3 = (int)(n / 60000000000L);
        n -= n3 * 60000000000L;
        final int n4 = (int)(n / 1000000000L);
        n -= n4 * 1000000000L;
        return create(n2, n3, n4, (int)n);
    }
    
    public static LocalTime from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final LocalTime localTime = temporalAccessor.query(TemporalQueries.localTime());
        if (localTime == null) {
            throw new DateTimeException("Unable to obtain LocalTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
        }
        return localTime;
    }
    
    public static LocalTime parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_TIME);
    }
    
    public static LocalTime parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, LocalTime::from);
    }
    
    private static LocalTime create(final int n, final int n2, final int n3, final int n4) {
        if ((n2 | n3 | n4) == 0x0) {
            return LocalTime.HOURS[n];
        }
        return new LocalTime(n, n2, n3, n4);
    }
    
    private LocalTime(final int n, final int n2, final int n3, final int nano) {
        this.hour = (byte)n;
        this.minute = (byte)n2;
        this.second = (byte)n3;
        this.nano = nano;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isTimeBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isTimeBased();
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
            return this.get0(temporalField);
        }
        return super.get(temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.NANO_OF_DAY) {
            return this.toNanoOfDay();
        }
        if (temporalField == ChronoField.MICRO_OF_DAY) {
            return this.toNanoOfDay() / 1000L;
        }
        return this.get0(temporalField);
    }
    
    private int get0(final TemporalField temporalField) {
        switch ((ChronoField)temporalField) {
            case NANO_OF_SECOND: {
                return this.nano;
            }
            case NANO_OF_DAY: {
                throw new UnsupportedTemporalTypeException("Invalid field 'NanoOfDay' for get() method, use getLong() instead");
            }
            case MICRO_OF_SECOND: {
                return this.nano / 1000;
            }
            case MICRO_OF_DAY: {
                throw new UnsupportedTemporalTypeException("Invalid field 'MicroOfDay' for get() method, use getLong() instead");
            }
            case MILLI_OF_SECOND: {
                return this.nano / 1000000;
            }
            case MILLI_OF_DAY: {
                return (int)(this.toNanoOfDay() / 1000000L);
            }
            case SECOND_OF_MINUTE: {
                return this.second;
            }
            case SECOND_OF_DAY: {
                return this.toSecondOfDay();
            }
            case MINUTE_OF_HOUR: {
                return this.minute;
            }
            case MINUTE_OF_DAY: {
                return this.hour * 60 + this.minute;
            }
            case HOUR_OF_AMPM: {
                return this.hour % 12;
            }
            case CLOCK_HOUR_OF_AMPM: {
                final int n = this.hour % 12;
                return (n % 12 == 0) ? 12 : n;
            }
            case HOUR_OF_DAY: {
                return this.hour;
            }
            case CLOCK_HOUR_OF_DAY: {
                return (this.hour == 0) ? 24 : this.hour;
            }
            case AMPM_OF_DAY: {
                return this.hour / 12;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public int getMinute() {
        return this.minute;
    }
    
    public int getSecond() {
        return this.second;
    }
    
    public int getNano() {
        return this.nano;
    }
    
    @Override
    public LocalTime with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return (LocalTime)temporalAdjuster;
        }
        return (LocalTime)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public LocalTime with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        chronoField.checkValidValue(n);
        switch (chronoField) {
            case NANO_OF_SECOND: {
                return this.withNano((int)n);
            }
            case NANO_OF_DAY: {
                return ofNanoOfDay(n);
            }
            case MICRO_OF_SECOND: {
                return this.withNano((int)n * 1000);
            }
            case MICRO_OF_DAY: {
                return ofNanoOfDay(n * 1000L);
            }
            case MILLI_OF_SECOND: {
                return this.withNano((int)n * 1000000);
            }
            case MILLI_OF_DAY: {
                return ofNanoOfDay(n * 1000000L);
            }
            case SECOND_OF_MINUTE: {
                return this.withSecond((int)n);
            }
            case SECOND_OF_DAY: {
                return this.plusSeconds(n - this.toSecondOfDay());
            }
            case MINUTE_OF_HOUR: {
                return this.withMinute((int)n);
            }
            case MINUTE_OF_DAY: {
                return this.plusMinutes(n - (this.hour * 60 + this.minute));
            }
            case HOUR_OF_AMPM: {
                return this.plusHours(n - this.hour % 12);
            }
            case CLOCK_HOUR_OF_AMPM: {
                return this.plusHours(((n == 12L) ? 0L : n) - this.hour % 12);
            }
            case HOUR_OF_DAY: {
                return this.withHour((int)n);
            }
            case CLOCK_HOUR_OF_DAY: {
                return this.withHour((int)((n == 24L) ? 0L : n));
            }
            case AMPM_OF_DAY: {
                return this.plusHours((n - this.hour / 12) * 12L);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public LocalTime withHour(final int n) {
        if (this.hour == n) {
            return this;
        }
        ChronoField.HOUR_OF_DAY.checkValidValue(n);
        return create(n, this.minute, this.second, this.nano);
    }
    
    public LocalTime withMinute(final int n) {
        if (this.minute == n) {
            return this;
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(n);
        return create(this.hour, n, this.second, this.nano);
    }
    
    public LocalTime withSecond(final int n) {
        if (this.second == n) {
            return this;
        }
        ChronoField.SECOND_OF_MINUTE.checkValidValue(n);
        return create(this.hour, this.minute, n, this.nano);
    }
    
    public LocalTime withNano(final int n) {
        if (this.nano == n) {
            return this;
        }
        ChronoField.NANO_OF_SECOND.checkValidValue(n);
        return create(this.hour, this.minute, this.second, n);
    }
    
    public LocalTime truncatedTo(final TemporalUnit temporalUnit) {
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
        return ofNanoOfDay(this.toNanoOfDay() / nanos * nanos);
    }
    
    @Override
    public LocalTime plus(final TemporalAmount temporalAmount) {
        return (LocalTime)temporalAmount.addTo(this);
    }
    
    @Override
    public LocalTime plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.plusNanos(n);
            }
            case MICROS: {
                return this.plusNanos(n % 86400000000L * 1000L);
            }
            case MILLIS: {
                return this.plusNanos(n % 86400000L * 1000000L);
            }
            case SECONDS: {
                return this.plusSeconds(n);
            }
            case MINUTES: {
                return this.plusMinutes(n);
            }
            case HOURS: {
                return this.plusHours(n);
            }
            case HALF_DAYS: {
                return this.plusHours(n % 2L * 12L);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    public LocalTime plusHours(final long n) {
        if (n == 0L) {
            return this;
        }
        return create(((int)(n % 24L) + this.hour + 24) % 24, this.minute, this.second, this.nano);
    }
    
    public LocalTime plusMinutes(final long n) {
        if (n == 0L) {
            return this;
        }
        final int n2 = this.hour * 60 + this.minute;
        final int n3 = ((int)(n % 1440L) + n2 + 1440) % 1440;
        if (n2 == n3) {
            return this;
        }
        return create(n3 / 60, n3 % 60, this.second, this.nano);
    }
    
    public LocalTime plusSeconds(final long n) {
        if (n == 0L) {
            return this;
        }
        final int n2 = this.hour * 3600 + this.minute * 60 + this.second;
        final int n3 = ((int)(n % 86400L) + n2 + 86400) % 86400;
        if (n2 == n3) {
            return this;
        }
        return create(n3 / 3600, n3 / 60 % 60, n3 % 60, this.nano);
    }
    
    public LocalTime plusNanos(final long n) {
        if (n == 0L) {
            return this;
        }
        final long nanoOfDay = this.toNanoOfDay();
        final long n2 = (n % 86400000000000L + nanoOfDay + 86400000000000L) % 86400000000000L;
        if (nanoOfDay == n2) {
            return this;
        }
        return create((int)(n2 / 3600000000000L), (int)(n2 / 60000000000L % 60L), (int)(n2 / 1000000000L % 60L), (int)(n2 % 1000000000L));
    }
    
    @Override
    public LocalTime minus(final TemporalAmount temporalAmount) {
        return (LocalTime)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public LocalTime minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public LocalTime minusHours(final long n) {
        return this.plusHours(-(n % 24L));
    }
    
    public LocalTime minusMinutes(final long n) {
        return this.plusMinutes(-(n % 1440L));
    }
    
    public LocalTime minusSeconds(final long n) {
        return this.plusSeconds(-(n % 86400L));
    }
    
    public LocalTime minusNanos(final long n) {
        return this.plusNanos(-(n % 86400000000000L));
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)this;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, this.toNanoOfDay());
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final LocalTime from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        final long n = from.toNanoOfDay() - this.toNanoOfDay();
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return n;
            }
            case MICROS: {
                return n / 1000L;
            }
            case MILLIS: {
                return n / 1000000L;
            }
            case SECONDS: {
                return n / 1000000000L;
            }
            case MINUTES: {
                return n / 60000000000L;
            }
            case HOURS: {
                return n / 3600000000000L;
            }
            case HALF_DAYS: {
                return n / 43200000000000L;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public LocalDateTime atDate(final LocalDate localDate) {
        return LocalDateTime.of(localDate, this);
    }
    
    public OffsetTime atOffset(final ZoneOffset zoneOffset) {
        return OffsetTime.of(this, zoneOffset);
    }
    
    public int toSecondOfDay() {
        return this.hour * 3600 + this.minute * 60 + this.second;
    }
    
    public long toNanoOfDay() {
        return this.hour * 3600000000000L + this.minute * 60000000000L + this.second * 1000000000L + this.nano;
    }
    
    @Override
    public int compareTo(final LocalTime localTime) {
        int n = Integer.compare(this.hour, localTime.hour);
        if (n == 0) {
            n = Integer.compare(this.minute, localTime.minute);
            if (n == 0) {
                n = Integer.compare(this.second, localTime.second);
                if (n == 0) {
                    n = Integer.compare(this.nano, localTime.nano);
                }
            }
        }
        return n;
    }
    
    public boolean isAfter(final LocalTime localTime) {
        return this.compareTo(localTime) > 0;
    }
    
    public boolean isBefore(final LocalTime localTime) {
        return this.compareTo(localTime) < 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LocalTime) {
            final LocalTime localTime = (LocalTime)o;
            return this.hour == localTime.hour && this.minute == localTime.minute && this.second == localTime.second && this.nano == localTime.nano;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long nanoOfDay = this.toNanoOfDay();
        return (int)(nanoOfDay ^ nanoOfDay >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(18);
        final byte hour = this.hour;
        final byte minute = this.minute;
        final byte second = this.second;
        final int nano = this.nano;
        sb.append((hour < 10) ? "0" : "").append(hour).append((minute < 10) ? ":0" : ":").append(minute);
        if (second > 0 || nano > 0) {
            sb.append((second < 10) ? ":0" : ":").append(second);
            if (nano > 0) {
                sb.append('.');
                if (nano % 1000000 == 0) {
                    sb.append(Integer.toString(nano / 1000000 + 1000).substring(1));
                }
                else if (nano % 1000 == 0) {
                    sb.append(Integer.toString(nano / 1000 + 1000000).substring(1));
                }
                else {
                    sb.append(Integer.toString(nano + 1000000000).substring(1));
                }
            }
        }
        return sb.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)4, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        if (this.nano == 0) {
            if (this.second == 0) {
                if (this.minute == 0) {
                    dataOutput.writeByte(~this.hour);
                }
                else {
                    dataOutput.writeByte(this.hour);
                    dataOutput.writeByte(~this.minute);
                }
            }
            else {
                dataOutput.writeByte(this.hour);
                dataOutput.writeByte(this.minute);
                dataOutput.writeByte(~this.second);
            }
        }
        else {
            dataOutput.writeByte(this.hour);
            dataOutput.writeByte(this.minute);
            dataOutput.writeByte(this.second);
            dataOutput.writeInt(this.nano);
        }
    }
    
    static LocalTime readExternal(final DataInput dataInput) throws IOException {
        int byte1 = dataInput.readByte();
        int byte2 = 0;
        int byte3 = 0;
        int int1 = 0;
        if (byte1 < 0) {
            byte1 ^= -1;
        }
        else {
            byte2 = dataInput.readByte();
            if (byte2 < 0) {
                byte2 ^= -1;
            }
            else {
                byte3 = dataInput.readByte();
                if (byte3 < 0) {
                    byte3 ^= -1;
                }
                else {
                    int1 = dataInput.readInt();
                }
            }
        }
        return of(byte1, byte2, byte3, int1);
    }
    
    static {
        HOURS = new LocalTime[24];
        for (int i = 0; i < LocalTime.HOURS.length; ++i) {
            LocalTime.HOURS[i] = new LocalTime(i, 0, 0, 0);
        }
        MIDNIGHT = LocalTime.HOURS[0];
        NOON = LocalTime.HOURS[12];
        MIN = LocalTime.HOURS[0];
        MAX = new LocalTime(23, 59, 59, 999999999);
    }
}
