package java.time.zone;

import java.io.*;
import java.time.*;
import java.util.*;

public final class ZoneOffsetTransition implements Comparable<ZoneOffsetTransition>, Serializable
{
    private static final long serialVersionUID = -6946044323557704546L;
    private final LocalDateTime transition;
    private final ZoneOffset offsetBefore;
    private final ZoneOffset offsetAfter;
    
    public static ZoneOffsetTransition of(final LocalDateTime localDateTime, final ZoneOffset zoneOffset, final ZoneOffset zoneOffset2) {
        Objects.requireNonNull(localDateTime, "transition");
        Objects.requireNonNull(zoneOffset, "offsetBefore");
        Objects.requireNonNull(zoneOffset2, "offsetAfter");
        if (zoneOffset.equals(zoneOffset2)) {
            throw new IllegalArgumentException("Offsets must not be equal");
        }
        if (localDateTime.getNano() != 0) {
            throw new IllegalArgumentException("Nano-of-second must be zero");
        }
        return new ZoneOffsetTransition(localDateTime, zoneOffset, zoneOffset2);
    }
    
    ZoneOffsetTransition(final LocalDateTime transition, final ZoneOffset offsetBefore, final ZoneOffset offsetAfter) {
        this.transition = transition;
        this.offsetBefore = offsetBefore;
        this.offsetAfter = offsetAfter;
    }
    
    ZoneOffsetTransition(final long n, final ZoneOffset offsetBefore, final ZoneOffset offsetAfter) {
        this.transition = LocalDateTime.ofEpochSecond(n, 0, offsetBefore);
        this.offsetBefore = offsetBefore;
        this.offsetAfter = offsetAfter;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)2, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        Ser.writeEpochSec(this.toEpochSecond(), dataOutput);
        Ser.writeOffset(this.offsetBefore, dataOutput);
        Ser.writeOffset(this.offsetAfter, dataOutput);
    }
    
    static ZoneOffsetTransition readExternal(final DataInput dataInput) throws IOException {
        final long epochSec = Ser.readEpochSec(dataInput);
        final ZoneOffset offset = Ser.readOffset(dataInput);
        final ZoneOffset offset2 = Ser.readOffset(dataInput);
        if (offset.equals(offset2)) {
            throw new IllegalArgumentException("Offsets must not be equal");
        }
        return new ZoneOffsetTransition(epochSec, offset, offset2);
    }
    
    public Instant getInstant() {
        return this.transition.toInstant(this.offsetBefore);
    }
    
    public long toEpochSecond() {
        return this.transition.toEpochSecond(this.offsetBefore);
    }
    
    public LocalDateTime getDateTimeBefore() {
        return this.transition;
    }
    
    public LocalDateTime getDateTimeAfter() {
        return this.transition.plusSeconds(this.getDurationSeconds());
    }
    
    public ZoneOffset getOffsetBefore() {
        return this.offsetBefore;
    }
    
    public ZoneOffset getOffsetAfter() {
        return this.offsetAfter;
    }
    
    public Duration getDuration() {
        return Duration.ofSeconds(this.getDurationSeconds());
    }
    
    private int getDurationSeconds() {
        return this.getOffsetAfter().getTotalSeconds() - this.getOffsetBefore().getTotalSeconds();
    }
    
    public boolean isGap() {
        return this.getOffsetAfter().getTotalSeconds() > this.getOffsetBefore().getTotalSeconds();
    }
    
    public boolean isOverlap() {
        return this.getOffsetAfter().getTotalSeconds() < this.getOffsetBefore().getTotalSeconds();
    }
    
    public boolean isValidOffset(final ZoneOffset zoneOffset) {
        return !this.isGap() && (this.getOffsetBefore().equals(zoneOffset) || this.getOffsetAfter().equals(zoneOffset));
    }
    
    List<ZoneOffset> getValidOffsets() {
        if (this.isGap()) {
            return Collections.emptyList();
        }
        return Arrays.asList(this.getOffsetBefore(), this.getOffsetAfter());
    }
    
    @Override
    public int compareTo(final ZoneOffsetTransition zoneOffsetTransition) {
        return this.getInstant().compareTo(zoneOffsetTransition.getInstant());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ZoneOffsetTransition) {
            final ZoneOffsetTransition zoneOffsetTransition = (ZoneOffsetTransition)o;
            return this.transition.equals(zoneOffsetTransition.transition) && this.offsetBefore.equals(zoneOffsetTransition.offsetBefore) && this.offsetAfter.equals(zoneOffsetTransition.offsetAfter);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.transition.hashCode() ^ this.offsetBefore.hashCode() ^ Integer.rotateLeft(this.offsetAfter.hashCode(), 16);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Transition[").append(this.isGap() ? "Gap" : "Overlap").append(" at ").append(this.transition).append(this.offsetBefore).append(" to ").append(this.offsetAfter).append(']');
        return sb.toString();
    }
}
