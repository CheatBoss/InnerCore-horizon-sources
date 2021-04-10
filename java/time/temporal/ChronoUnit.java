package java.time.temporal;

import java.time.*;

public enum ChronoUnit implements TemporalUnit
{
    NANOS("Nanos", Duration.ofNanos(1L)), 
    MICROS("Micros", Duration.ofNanos(1000L)), 
    MILLIS("Millis", Duration.ofNanos(1000000L)), 
    SECONDS("Seconds", Duration.ofSeconds(1L)), 
    MINUTES("Minutes", Duration.ofSeconds(60L)), 
    HOURS("Hours", Duration.ofSeconds(3600L)), 
    HALF_DAYS("HalfDays", Duration.ofSeconds(43200L)), 
    DAYS("Days", Duration.ofSeconds(86400L)), 
    WEEKS("Weeks", Duration.ofSeconds(604800L)), 
    MONTHS("Months", Duration.ofSeconds(2629746L)), 
    YEARS("Years", Duration.ofSeconds(31556952L)), 
    DECADES("Decades", Duration.ofSeconds(315569520L)), 
    CENTURIES("Centuries", Duration.ofSeconds(3155695200L)), 
    MILLENNIA("Millennia", Duration.ofSeconds(31556952000L)), 
    ERAS("Eras", Duration.ofSeconds(31556952000000000L)), 
    FOREVER("Forever", Duration.ofSeconds(Long.MAX_VALUE, 999999999L));
    
    private final String name;
    private final Duration duration;
    
    private ChronoUnit(final String name, final Duration duration) {
        this.name = name;
        this.duration = duration;
    }
    
    @Override
    public Duration getDuration() {
        return this.duration;
    }
    
    @Override
    public boolean isDurationEstimated() {
        return this.compareTo(ChronoUnit.DAYS) >= 0;
    }
    
    @Override
    public boolean isDateBased() {
        return this.compareTo(ChronoUnit.DAYS) >= 0 && this != ChronoUnit.FOREVER;
    }
    
    @Override
    public boolean isTimeBased() {
        return this.compareTo(ChronoUnit.DAYS) < 0;
    }
    
    @Override
    public boolean isSupportedBy(final Temporal temporal) {
        return temporal.isSupported(this);
    }
    
    @Override
    public <R extends Temporal> R addTo(final R r, final long n) {
        return (R)r.plus(n, this);
    }
    
    @Override
    public long between(final Temporal temporal, final Temporal temporal2) {
        return temporal.until(temporal2, this);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
