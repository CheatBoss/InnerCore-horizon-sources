package java.time;

import java.util.*;
import java.io.*;
import java.time.temporal.*;

public abstract class Clock
{
    public static Clock systemUTC() {
        return new SystemClock(ZoneOffset.UTC);
    }
    
    public static Clock systemDefaultZone() {
        return new SystemClock(ZoneId.systemDefault());
    }
    
    public static Clock system(final ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return new SystemClock(zoneId);
    }
    
    public static Clock tickSeconds(final ZoneId zoneId) {
        return new TickClock(system(zoneId), 1000000000L);
    }
    
    public static Clock tickMinutes(final ZoneId zoneId) {
        return new TickClock(system(zoneId), 60000000000L);
    }
    
    public static Clock tick(final Clock clock, final Duration duration) {
        Objects.requireNonNull(clock, "baseClock");
        Objects.requireNonNull(duration, "tickDuration");
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Tick duration must not be negative");
        }
        final long nanos = duration.toNanos();
        if (nanos % 1000000L != 0L) {
            if (1000000000L % nanos != 0L) {
                throw new IllegalArgumentException("Invalid tick duration");
            }
        }
        if (nanos <= 1L) {
            return clock;
        }
        return new TickClock(clock, nanos);
    }
    
    public static Clock fixed(final Instant instant, final ZoneId zoneId) {
        Objects.requireNonNull(instant, "fixedInstant");
        Objects.requireNonNull(zoneId, "zone");
        return new FixedClock(instant, zoneId);
    }
    
    public static Clock offset(final Clock clock, final Duration duration) {
        Objects.requireNonNull(clock, "baseClock");
        Objects.requireNonNull(duration, "offsetDuration");
        if (duration.equals(Duration.ZERO)) {
            return clock;
        }
        return new OffsetClock(clock, duration);
    }
    
    protected Clock() {
    }
    
    public abstract ZoneId getZone();
    
    public abstract Clock withZone(final ZoneId p0);
    
    public long millis() {
        return this.instant().toEpochMilli();
    }
    
    public abstract Instant instant();
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    static final class FixedClock extends Clock implements Serializable
    {
        private static final long serialVersionUID = 7430389292664866958L;
        private final Instant instant;
        private final ZoneId zone;
        
        FixedClock(final Instant instant, final ZoneId zone) {
            this.instant = instant;
            this.zone = zone;
        }
        
        @Override
        public ZoneId getZone() {
            return this.zone;
        }
        
        @Override
        public Clock withZone(final ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new FixedClock(this.instant, zoneId);
        }
        
        @Override
        public long millis() {
            return this.instant.toEpochMilli();
        }
        
        @Override
        public Instant instant() {
            return this.instant;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof FixedClock) {
                final FixedClock fixedClock = (FixedClock)o;
                return this.instant.equals(fixedClock.instant) && this.zone.equals(fixedClock.zone);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.instant.hashCode() ^ this.zone.hashCode();
        }
        
        @Override
        public String toString() {
            return "FixedClock[" + this.instant + "," + this.zone + "]";
        }
    }
    
    static final class OffsetClock extends Clock implements Serializable
    {
        private static final long serialVersionUID = 2007484719125426256L;
        private final Clock baseClock;
        private final Duration offset;
        
        OffsetClock(final Clock baseClock, final Duration offset) {
            this.baseClock = baseClock;
            this.offset = offset;
        }
        
        @Override
        public ZoneId getZone() {
            return this.baseClock.getZone();
        }
        
        @Override
        public Clock withZone(final ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new OffsetClock(this.baseClock.withZone(zoneId), this.offset);
        }
        
        @Override
        public long millis() {
            return Math.addExact(this.baseClock.millis(), this.offset.toMillis());
        }
        
        @Override
        public Instant instant() {
            return this.baseClock.instant().plus((TemporalAmount)this.offset);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof OffsetClock) {
                final OffsetClock offsetClock = (OffsetClock)o;
                return this.baseClock.equals(offsetClock.baseClock) && this.offset.equals(offsetClock.offset);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.baseClock.hashCode() ^ this.offset.hashCode();
        }
        
        @Override
        public String toString() {
            return "OffsetClock[" + this.baseClock + "," + this.offset + "]";
        }
    }
    
    static final class SystemClock extends Clock implements Serializable
    {
        private static final long serialVersionUID = 6740630888130243051L;
        private final ZoneId zone;
        
        SystemClock(final ZoneId zone) {
            this.zone = zone;
        }
        
        @Override
        public ZoneId getZone() {
            return this.zone;
        }
        
        @Override
        public Clock withZone(final ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new SystemClock(zoneId);
        }
        
        @Override
        public long millis() {
            return System.currentTimeMillis();
        }
        
        @Override
        public Instant instant() {
            return Instant.ofEpochMilli(this.millis());
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof SystemClock && this.zone.equals(((SystemClock)o).zone);
        }
        
        @Override
        public int hashCode() {
            return this.zone.hashCode() + 1;
        }
        
        @Override
        public String toString() {
            return "SystemClock[" + this.zone + "]";
        }
    }
    
    static final class TickClock extends Clock implements Serializable
    {
        private static final long serialVersionUID = 6504659149906368850L;
        private final Clock baseClock;
        private final long tickNanos;
        
        TickClock(final Clock baseClock, final long tickNanos) {
            this.baseClock = baseClock;
            this.tickNanos = tickNanos;
        }
        
        @Override
        public ZoneId getZone() {
            return this.baseClock.getZone();
        }
        
        @Override
        public Clock withZone(final ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new TickClock(this.baseClock.withZone(zoneId), this.tickNanos);
        }
        
        @Override
        public long millis() {
            final long millis = this.baseClock.millis();
            return millis - Math.floorMod(millis, this.tickNanos / 1000000L);
        }
        
        @Override
        public Instant instant() {
            if (this.tickNanos % 1000000L == 0L) {
                final long millis = this.baseClock.millis();
                return Instant.ofEpochMilli(millis - Math.floorMod(millis, this.tickNanos / 1000000L));
            }
            final Instant instant = this.baseClock.instant();
            return instant.minusNanos(Math.floorMod(instant.getNano(), this.tickNanos));
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof TickClock) {
                final TickClock tickClock = (TickClock)o;
                return this.baseClock.equals(tickClock.baseClock) && this.tickNanos == tickClock.tickNanos;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.baseClock.hashCode() ^ (int)(this.tickNanos ^ this.tickNanos >>> 32);
        }
        
        @Override
        public String toString() {
            return "TickClock[" + this.baseClock + "," + Duration.ofNanos(this.tickNanos) + "]";
        }
    }
}
