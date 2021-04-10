package java.time.chrono;

import java.util.*;
import java.time.zone.*;
import java.time.*;
import java.time.temporal.*;
import java.io.*;

final class ChronoZonedDateTimeImpl<D extends ChronoLocalDate> implements ChronoZonedDateTime<D>, Serializable
{
    private static final long serialVersionUID = -5261813987200935591L;
    private final transient ChronoLocalDateTimeImpl<D> dateTime;
    private final transient ZoneOffset offset;
    private final transient ZoneId zone;
    
    static <R extends ChronoLocalDate> ChronoZonedDateTime<R> ofBest(ChronoLocalDateTimeImpl<R> plusSeconds, final ZoneId zoneId, final ZoneOffset zoneOffset) {
        Objects.requireNonNull(plusSeconds, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ChronoZonedDateTimeImpl<R>((ChronoLocalDateTimeImpl<ChronoLocalDate>)plusSeconds, (ZoneOffset)zoneId, zoneId);
        }
        final ZoneRules rules = zoneId.getRules();
        final LocalDateTime from = LocalDateTime.from((TemporalAccessor)plusSeconds);
        final List<ZoneOffset> validOffsets = rules.getValidOffsets(from);
        ZoneOffset offsetAfter;
        if (validOffsets.size() == 1) {
            offsetAfter = validOffsets.get(0);
        }
        else if (validOffsets.size() == 0) {
            final ZoneOffsetTransition transition = rules.getTransition(from);
            plusSeconds = plusSeconds.plusSeconds(transition.getDuration().getSeconds());
            offsetAfter = transition.getOffsetAfter();
        }
        else if (zoneOffset != null && validOffsets.contains(zoneOffset)) {
            offsetAfter = zoneOffset;
        }
        else {
            offsetAfter = validOffsets.get(0);
        }
        Objects.requireNonNull(offsetAfter, "offset");
        return new ChronoZonedDateTimeImpl<R>((ChronoLocalDateTimeImpl<ChronoLocalDate>)plusSeconds, offsetAfter, zoneId);
    }
    
    static ChronoZonedDateTimeImpl<?> ofInstant(final Chronology chronology, final Instant instant, final ZoneId zoneId) {
        final ZoneOffset offset = zoneId.getRules().getOffset(instant);
        Objects.requireNonNull(offset, "offset");
        return new ChronoZonedDateTimeImpl<Object>((ChronoLocalDateTimeImpl)chronology.localDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset)), offset, zoneId);
    }
    
    private ChronoZonedDateTimeImpl<D> create(final Instant instant, final ZoneId zoneId) {
        return (ChronoZonedDateTimeImpl<D>)ofInstant(this.getChronology(), instant, zoneId);
    }
    
    static <R extends ChronoLocalDate> ChronoZonedDateTimeImpl<R> ensureValid(final Chronology chronology, final Temporal temporal) {
        final ChronoZonedDateTimeImpl chronoZonedDateTimeImpl = (ChronoZonedDateTimeImpl)temporal;
        if (!chronology.equals(chronoZonedDateTimeImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, required: " + chronology.getId() + ", actual: " + chronoZonedDateTimeImpl.getChronology().getId());
        }
        return (ChronoZonedDateTimeImpl<R>)chronoZonedDateTimeImpl;
    }
    
    private ChronoZonedDateTimeImpl(final ChronoLocalDateTimeImpl<D> chronoLocalDateTimeImpl, final ZoneOffset zoneOffset, final ZoneId zoneId) {
        this.dateTime = Objects.requireNonNull(chronoLocalDateTimeImpl, "dateTime");
        this.offset = Objects.requireNonNull(zoneOffset, "offset");
        this.zone = Objects.requireNonNull(zoneId, "zone");
    }
    
    @Override
    public ZoneOffset getOffset() {
        return this.offset;
    }
    
    @Override
    public ChronoZonedDateTime<D> withEarlierOffsetAtOverlap() {
        final ZoneOffsetTransition transition = this.getZone().getRules().getTransition(LocalDateTime.from((TemporalAccessor)this));
        if (transition != null && transition.isOverlap()) {
            final ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ChronoZonedDateTimeImpl((ChronoLocalDateTimeImpl<ChronoLocalDate>)this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }
    
    @Override
    public ChronoZonedDateTime<D> withLaterOffsetAtOverlap() {
        final ZoneOffsetTransition transition = this.getZone().getRules().getTransition(LocalDateTime.from((TemporalAccessor)this));
        if (transition != null) {
            final ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(this.getOffset())) {
                return new ChronoZonedDateTimeImpl((ChronoLocalDateTimeImpl<ChronoLocalDate>)this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }
    
    @Override
    public ChronoLocalDateTime<D> toLocalDateTime() {
        return this.dateTime;
    }
    
    @Override
    public ZoneId getZone() {
        return this.zone;
    }
    
    @Override
    public ChronoZonedDateTime<D> withZoneSameLocal(final ZoneId zoneId) {
        return ofBest(this.dateTime, zoneId, this.offset);
    }
    
    @Override
    public ChronoZonedDateTime<D> withZoneSameInstant(final ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : this.create(this.dateTime.toInstant(this.offset), zoneId);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return temporalField instanceof ChronoField || (temporalField != null && temporalField.isSupportedBy(this));
    }
    
    @Override
    public ChronoZonedDateTime<D> with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return (ChronoZonedDateTime<D>)ensureValid(this.getChronology(), temporalField.adjustInto(this, n));
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS: {
                return this.plus(n - this.toEpochSecond(), (TemporalUnit)ChronoUnit.SECONDS);
            }
            case OFFSET_SECONDS: {
                return this.create(this.dateTime.toInstant(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(n))), this.zone);
            }
            default: {
                return ofBest(this.dateTime.with(temporalField, n), this.zone, this.offset);
            }
        }
    }
    
    @Override
    public ChronoZonedDateTime<D> plus(final long n, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return this.with((TemporalAdjuster)this.dateTime.plus(n, temporalUnit));
        }
        return (ChronoZonedDateTime<D>)ensureValid(this.getChronology(), temporalUnit.addTo(this, n));
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        final ChronoZonedDateTime<? extends ChronoLocalDate> zonedDateTime = this.getChronology().zonedDateTime(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return this.dateTime.until(zonedDateTime.withZoneSameInstant(this.offset).toLocalDateTime(), temporalUnit);
        }
        Objects.requireNonNull(temporalUnit, "unit");
        return temporalUnit.between(this, zonedDateTime);
    }
    
    private Object writeReplace() {
        return new Ser((byte)3, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.dateTime);
        objectOutput.writeObject(this.offset);
        objectOutput.writeObject(this.zone);
    }
    
    static ChronoZonedDateTime<?> readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((ChronoLocalDateTime)objectInput.readObject()).atZone((ZoneId)objectInput.readObject()).withZoneSameLocal((ZoneId)objectInput.readObject());
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ChronoZonedDateTime && this.compareTo((ChronoZonedDateTime<?>)o) == 0);
    }
    
    @Override
    public int hashCode() {
        return this.toLocalDateTime().hashCode() ^ this.getOffset().hashCode() ^ Integer.rotateLeft(this.getZone().hashCode(), 3);
    }
    
    @Override
    public String toString() {
        String s = this.toLocalDateTime().toString() + this.getOffset().toString();
        if (this.getOffset() != this.getZone()) {
            s = s + '[' + this.getZone().toString() + ']';
        }
        return s;
    }
}
