package java.time.temporal;

public final class Temporal-CC
{
    public static Temporal $default$minus(final Temporal temporal, final long n, final TemporalUnit temporalUnit) {
        if (n == Long.MIN_VALUE) {
            return temporal.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit);
        }
        return temporal.plus(-n, temporalUnit);
    }
    
    public static Temporal $default$minus(final Temporal temporal, final TemporalAmount temporalAmount) {
        return temporalAmount.subtractFrom(temporal);
    }
    
    public static Temporal $default$plus(final Temporal temporal, final TemporalAmount temporalAmount) {
        return temporalAmount.addTo(temporal);
    }
    
    public static Temporal $default$with(final Temporal temporal, final TemporalAdjuster temporalAdjuster) {
        return temporalAdjuster.adjustInto(temporal);
    }
}
