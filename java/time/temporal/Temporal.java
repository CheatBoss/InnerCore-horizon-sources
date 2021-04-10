package java.time.temporal;

public interface Temporal extends TemporalAccessor
{
    boolean isSupported(final TemporalUnit p0);
    
    default Temporal with(final TemporalAdjuster temporalAdjuster) {
        return temporalAdjuster.adjustInto(this);
    }
    
    Temporal with(final TemporalField p0, final long p1);
    
    default Temporal plus(final TemporalAmount temporalAmount) {
        return temporalAmount.addTo(this);
    }
    
    Temporal plus(final long p0, final TemporalUnit p1);
    
    default Temporal minus(final TemporalAmount temporalAmount) {
        return temporalAmount.subtractFrom(this);
    }
    
    default Temporal minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    long until(final Temporal p0, final TemporalUnit p1);
}
