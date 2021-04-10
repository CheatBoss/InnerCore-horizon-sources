package java.time.temporal;

import java.time.*;
import java.time.chrono.*;

public interface TemporalUnit
{
    Duration getDuration();
    
    boolean isDurationEstimated();
    
    boolean isDateBased();
    
    boolean isTimeBased();
    
    default boolean isSupportedBy(final Temporal temporal) {
        if (temporal instanceof LocalTime) {
            return this.isTimeBased();
        }
        if (temporal instanceof ChronoLocalDate) {
            return this.isDateBased();
        }
        if (temporal instanceof ChronoLocalDateTime || temporal instanceof ChronoZonedDateTime) {
            return true;
        }
        try {
            temporal.plus(1L, this);
            return true;
        }
        catch (UnsupportedTemporalTypeException ex) {
            return false;
        }
        catch (RuntimeException ex2) {
            try {
                temporal.plus(-1L, this);
                return true;
            }
            catch (RuntimeException ex3) {
                return false;
            }
        }
    }
    
     <R extends Temporal> R addTo(final R p0, final long p1);
    
    long between(final Temporal p0, final Temporal p1);
    
    String toString();
}
