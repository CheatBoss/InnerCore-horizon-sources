package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;

public final class OffsetTime implements Temporal, TemporalAdjuster, Comparable<OffsetTime>, Serializable
{
    public static final OffsetTime MIN;
    public static final OffsetTime MAX;
    private static final long serialVersionUID = 7264499704384272492L;
    private final LocalTime time;
    private final ZoneOffset offset;
    
    public static OffsetTime now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static OffsetTime now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static OffsetTime now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }
    
    public static OffsetTime of(final LocalTime localTime, final ZoneOffset zoneOffset) {
        return new OffsetTime(localTime, zoneOffset);
    }
    
    public static OffsetTime of(final int n, final int n2, final int n3, final int n4, final ZoneOffset zoneOffset) {
        return new OffsetTime(LocalTime.of(n, n2, n3, n4), zoneOffset);
    }
    
    public static OffsetTime ofInstant(final Instant instant, final ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        final ZoneOffset offset = zoneId.getRules().getOffset(instant);
        return new OffsetTime(LocalTime.ofNanoOfDay((int)Math.floorMod(instant.getEpochSecond() + offset.getTotalSeconds(), 86400L) * 1000000000L + instant.getNano()), offset);
    }
    
    public static OffsetTime from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof OffsetTime) {
            return (OffsetTime)temporalAccessor;
        }
        try {
            return new OffsetTime(LocalTime.from(temporalAccessor), ZoneOffset.from(temporalAccessor));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain OffsetTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), ex);
        }
    }
    
    public static OffsetTime parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_TIME);
    }
    
    public static OffsetTime parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, OffsetTime::from);
    }
    
    private OffsetTime(final LocalTime localTime, final ZoneOffset zoneOffset) {
        this.time = Objects.requireNonNull(localTime, "time");
        this.offset = Objects.requireNonNull(zoneOffset, "offset");
    }
    
    private OffsetTime with(final LocalTime localTime, final ZoneOffset zoneOffset) {
        if (this.time == localTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetTime(localTime, zoneOffset);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isTimeBased() || temporalField == ChronoField.OFFSET_SECONDS;
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
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return this.time.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        return super.get(temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.offset.getTotalSeconds();
        }
        return this.time.getLong(temporalField);
    }
    
    public ZoneOffset getOffset() {
        return this.offset;
    }
    
    public OffsetTime withOffsetSameLocal(final ZoneOffset zoneOffset) {
        return (zoneOffset != null && zoneOffset.equals(this.offset)) ? this : new OffsetTime(this.time, zoneOffset);
    }
    
    public OffsetTime withOffsetSameInstant(final ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetTime(this.time.plusSeconds(zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds()), zoneOffset);
    }
    
    public LocalTime toLocalTime() {
        return this.time;
    }
    
    public int getHour() {
        return this.time.getHour();
    }
    
    public int getMinute() {
        return this.time.getMinute();
    }
    
    public int getSecond() {
        return this.time.getSecond();
    }
    
    public int getNano() {
        return this.time.getNano();
    }
    
    @Override
    public OffsetTime with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return this.with((LocalTime)temporalAdjuster, this.offset);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return this.with(this.time, (ZoneOffset)temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetTime) {
            return (OffsetTime)temporalAdjuster;
        }
        return (OffsetTime)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public OffsetTime with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.with(this.time, ZoneOffset.ofTotalSeconds(((ChronoField)temporalField).checkValidIntValue(n)));
        }
        return this.with(this.time.with(temporalField, n), this.offset);
    }
    
    public OffsetTime withHour(final int n) {
        return this.with(this.time.withHour(n), this.offset);
    }
    
    public OffsetTime withMinute(final int n) {
        return this.with(this.time.withMinute(n), this.offset);
    }
    
    public OffsetTime withSecond(final int n) {
        return this.with(this.time.withSecond(n), this.offset);
    }
    
    public OffsetTime withNano(final int n) {
        return this.with(this.time.withNano(n), this.offset);
    }
    
    public OffsetTime truncatedTo(final TemporalUnit temporalUnit) {
        return this.with(this.time.truncatedTo(temporalUnit), this.offset);
    }
    
    @Override
    public OffsetTime plus(final TemporalAmount temporalAmount) {
        return (OffsetTime)temporalAmount.addTo(this);
    }
    
    @Override
    public OffsetTime plus(final long n, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return this.with(this.time.plus(n, temporalUnit), this.offset);
        }
        return temporalUnit.addTo(this, n);
    }
    
    public OffsetTime plusHours(final long n) {
        return this.with(this.time.plusHours(n), this.offset);
    }
    
    public OffsetTime plusMinutes(final long n) {
        return this.with(this.time.plusMinutes(n), this.offset);
    }
    
    public OffsetTime plusSeconds(final long n) {
        return this.with(this.time.plusSeconds(n), this.offset);
    }
    
    public OffsetTime plusNanos(final long n) {
        return this.with(this.time.plusNanos(n), this.offset);
    }
    
    @Override
    public OffsetTime minus(final TemporalAmount temporalAmount) {
        return (OffsetTime)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public OffsetTime minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public OffsetTime minusHours(final long n) {
        return this.with(this.time.minusHours(n), this.offset);
    }
    
    public OffsetTime minusMinutes(final long n) {
        return this.with(this.time.minusMinutes(n), this.offset);
    }
    
    public OffsetTime minusSeconds(final long n) {
        return this.with(this.time.minusSeconds(n), this.offset);
    }
    
    public OffsetTime minusNanos(final long n) {
        return this.with(this.time.minusNanos(n), this.offset);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return (R)this.offset;
        }
        if ((temporalQuery == TemporalQueries.zoneId() | temporalQuery == TemporalQueries.chronology()) || temporalQuery == TemporalQueries.localDate()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)this.time;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, this.time.toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, this.offset.getTotalSeconds());
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final OffsetTime from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        final long n = from.toEpochNano() - this.toEpochNano();
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
    
    public OffsetDateTime atDate(final LocalDate localDate) {
        return OffsetDateTime.of(localDate, this.time, this.offset);
    }
    
    private long toEpochNano() {
        return this.time.toNanoOfDay() - this.offset.getTotalSeconds() * 1000000000L;
    }
    
    @Override
    public int compareTo(final OffsetTime offsetTime) {
        if (this.offset.equals(offsetTime.offset)) {
            return this.time.compareTo(offsetTime.time);
        }
        int n = Long.compare(this.toEpochNano(), offsetTime.toEpochNano());
        if (n == 0) {
            n = this.time.compareTo(offsetTime.time);
        }
        return n;
    }
    
    public boolean isAfter(final OffsetTime offsetTime) {
        return this.toEpochNano() > offsetTime.toEpochNano();
    }
    
    public boolean isBefore(final OffsetTime offsetTime) {
        return this.toEpochNano() < offsetTime.toEpochNano();
    }
    
    public boolean isEqual(final OffsetTime offsetTime) {
        return this.toEpochNano() == offsetTime.toEpochNano();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof OffsetTime) {
            final OffsetTime offsetTime = (OffsetTime)o;
            return this.time.equals(offsetTime.time) && this.offset.equals(offsetTime.offset);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.time.hashCode() ^ this.offset.hashCode();
    }
    
    @Override
    public String toString() {
        return this.time.toString() + this.offset.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)9, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final ObjectOutput objectOutput) throws IOException {
        this.time.writeExternal(objectOutput);
        this.offset.writeExternal(objectOutput);
    }
    
    static OffsetTime readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return of(LocalTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput));
    }
    
    static {
        MIN = LocalTime.MIN.atOffset(ZoneOffset.MAX);
        MAX = LocalTime.MAX.atOffset(ZoneOffset.MIN);
    }
}
