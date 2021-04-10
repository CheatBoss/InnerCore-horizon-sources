package java.time.chrono;

import java.util.*;
import java.time.temporal.*;
import java.time.*;
import java.io.*;

final class ChronoLocalDateTimeImpl<D extends ChronoLocalDate> implements ChronoLocalDateTime<D>, Temporal, TemporalAdjuster, Serializable
{
    private static final long serialVersionUID = 4556003607393004514L;
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
    private final transient D date;
    private final transient LocalTime time;
    
    static <R extends ChronoLocalDate> ChronoLocalDateTimeImpl<R> of(final R r, final LocalTime localTime) {
        return new ChronoLocalDateTimeImpl<R>(r, localTime);
    }
    
    static <R extends ChronoLocalDate> ChronoLocalDateTimeImpl<R> ensureValid(final Chronology chronology, final Temporal temporal) {
        final ChronoLocalDateTimeImpl chronoLocalDateTimeImpl = (ChronoLocalDateTimeImpl)temporal;
        if (!chronology.equals(chronoLocalDateTimeImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, required: " + chronology.getId() + ", actual: " + chronoLocalDateTimeImpl.getChronology().getId());
        }
        return (ChronoLocalDateTimeImpl<R>)chronoLocalDateTimeImpl;
    }
    
    private ChronoLocalDateTimeImpl(final D date, final LocalTime time) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(time, "time");
        this.date = date;
        this.time = time;
    }
    
    private ChronoLocalDateTimeImpl<D> with(final Temporal temporal, final LocalTime localTime) {
        if (this.date == temporal && this.time == localTime) {
            return this;
        }
        return new ChronoLocalDateTimeImpl<D>(ChronoLocalDateImpl.ensureValid(this.date.getChronology(), temporal), localTime);
    }
    
    @Override
    public D toLocalDate() {
        return this.date;
    }
    
    @Override
    public LocalTime toLocalTime() {
        return this.time;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            final ChronoField chronoField = (ChronoField)temporalField;
            return chronoField.isDateBased() || chronoField.isTimeBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.range(temporalField) : this.date.range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.get(temporalField) : this.date.get(temporalField);
        }
        return this.range(temporalField).checkValidIntValue(this.getLong(temporalField), temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    @Override
    public ChronoLocalDateTimeImpl<D> with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof ChronoLocalDate) {
            return this.with((Temporal)temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return this.with(this.date, (LocalTime)temporalAdjuster);
        }
        if (temporalAdjuster instanceof ChronoLocalDateTimeImpl) {
            return ensureValid(this.date.getChronology(), (Temporal)temporalAdjuster);
        }
        return ensureValid(this.date.getChronology(), temporalAdjuster.adjustInto(this));
    }
    
    @Override
    public ChronoLocalDateTimeImpl<D> with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return ensureValid(this.date.getChronology(), temporalField.adjustInto(this, n));
        }
        if (((ChronoField)temporalField).isTimeBased()) {
            return this.with(this.date, this.time.with(temporalField, n));
        }
        return this.with(this.date.with(temporalField, n), this.time);
    }
    
    @Override
    public ChronoLocalDateTimeImpl<D> plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return ensureValid(this.date.getChronology(), temporalUnit.addTo(this, n));
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.plusNanos(n);
            }
            case MICROS: {
                return this.plusDays(n / 86400000000L).plusNanos(n % 86400000000L * 1000L);
            }
            case MILLIS: {
                return this.plusDays(n / 86400000L).plusNanos(n % 86400000L * 1000000L);
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
                return this.plusDays(n / 256L).plusHours(n % 256L * 12L);
            }
            default: {
                return this.with(this.date.plus(n, temporalUnit), this.time);
            }
        }
    }
    
    private ChronoLocalDateTimeImpl<D> plusDays(final long n) {
        return this.with(this.date.plus(n, (TemporalUnit)ChronoUnit.DAYS), this.time);
    }
    
    private ChronoLocalDateTimeImpl<D> plusHours(final long n) {
        return this.plusWithOverflow(this.date, n, 0L, 0L, 0L);
    }
    
    private ChronoLocalDateTimeImpl<D> plusMinutes(final long n) {
        return this.plusWithOverflow(this.date, 0L, n, 0L, 0L);
    }
    
    ChronoLocalDateTimeImpl<D> plusSeconds(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, n, 0L);
    }
    
    private ChronoLocalDateTimeImpl<D> plusNanos(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, 0L, n);
    }
    
    private ChronoLocalDateTimeImpl<D> plusWithOverflow(final D n, final long n2, final long n3, final long n4, final long n5) {
        if ((n2 | n3 | n4 | n5) == 0x0L) {
            return this.with(n, this.time);
        }
        final long n6 = n5 / 86400000000000L + n4 / 86400L + n3 / 1440L + n2 / 24L;
        final long n7 = n5 % 86400000000000L + n4 % 86400L * 1000000000L + n3 % 1440L * 60000000000L + n2 % 24L * 3600000000000L;
        final long nanoOfDay = this.time.toNanoOfDay();
        final long n8 = n7 + nanoOfDay;
        final long n9 = n6 + Math.floorDiv(n8, 86400000000000L);
        final long floorMod = Math.floorMod(n8, 86400000000000L);
        return this.with(n.plus(n9, (TemporalUnit)ChronoUnit.DAYS), (floorMod == nanoOfDay) ? this.time : LocalTime.ofNanoOfDay(floorMod));
    }
    
    @Override
    public ChronoZonedDateTime<D> atZone(final ZoneId zoneId) {
        return ChronoZonedDateTimeImpl.ofBest(this, zoneId, (ZoneOffset)null);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        final ChronoLocalDateTime<? extends ChronoLocalDate> localDateTime = this.getChronology().localDateTime(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            Objects.requireNonNull(temporalUnit, "unit");
            return temporalUnit.between(this, localDateTime);
        }
        if (temporalUnit.isTimeBased()) {
            long n = localDateTime.getLong(ChronoField.EPOCH_DAY) - this.date.getLong(ChronoField.EPOCH_DAY);
            switch ((ChronoUnit)temporalUnit) {
                case NANOS: {
                    n = Math.multiplyExact(n, 86400000000000L);
                    break;
                }
                case MICROS: {
                    n = Math.multiplyExact(n, 86400000000L);
                    break;
                }
                case MILLIS: {
                    n = Math.multiplyExact(n, 86400000L);
                    break;
                }
                case SECONDS: {
                    n = Math.multiplyExact(n, 86400L);
                    break;
                }
                case MINUTES: {
                    n = Math.multiplyExact(n, 1440L);
                    break;
                }
                case HOURS: {
                    n = Math.multiplyExact(n, 24L);
                    break;
                }
                case HALF_DAYS: {
                    n = Math.multiplyExact(n, 2L);
                    break;
                }
            }
            return Math.addExact(n, this.time.until(localDateTime.toLocalTime(), temporalUnit));
        }
        ChronoLocalDate chronoLocalDate = localDateTime.toLocalDate();
        if (localDateTime.toLocalTime().isBefore(this.time)) {
            chronoLocalDate = chronoLocalDate.minus(1L, (TemporalUnit)ChronoUnit.DAYS);
        }
        return this.date.until(chronoLocalDate, temporalUnit);
    }
    
    private Object writeReplace() {
        return new Ser((byte)2, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.date);
        objectOutput.writeObject(this.time);
    }
    
    static ChronoLocalDateTime<?> readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((ChronoLocalDate)objectInput.readObject()).atTime((LocalTime)objectInput.readObject());
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ChronoLocalDateTime && this.compareTo((ChronoLocalDateTime<?>)o) == 0);
    }
    
    @Override
    public int hashCode() {
        return this.toLocalDate().hashCode() ^ this.toLocalTime().hashCode();
    }
    
    @Override
    public String toString() {
        return this.toLocalDate().toString() + 'T' + this.toLocalTime().toString();
    }
}
