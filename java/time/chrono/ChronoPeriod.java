package java.time.chrono;

import java.util.*;
import java.time.temporal.*;

public interface ChronoPeriod extends TemporalAmount
{
    default ChronoPeriod between(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        Objects.requireNonNull(chronoLocalDate, "startDateInclusive");
        Objects.requireNonNull(chronoLocalDate2, "endDateExclusive");
        return chronoLocalDate.until(chronoLocalDate2);
    }
    
    long get(final TemporalUnit p0);
    
    List<TemporalUnit> getUnits();
    
    Chronology getChronology();
    
    default boolean isZero() {
        final Iterator<TemporalUnit> iterator = this.getUnits().iterator();
        while (iterator.hasNext()) {
            if (this.get(iterator.next()) != 0L) {
                return false;
            }
        }
        return true;
    }
    
    default boolean isNegative() {
        final Iterator<TemporalUnit> iterator = this.getUnits().iterator();
        while (iterator.hasNext()) {
            if (this.get(iterator.next()) < 0L) {
                return true;
            }
        }
        return false;
    }
    
    ChronoPeriod plus(final TemporalAmount p0);
    
    ChronoPeriod minus(final TemporalAmount p0);
    
    ChronoPeriod multipliedBy(final int p0);
    
    default ChronoPeriod negated() {
        return this.multipliedBy(-1);
    }
    
    ChronoPeriod normalized();
    
    Temporal addTo(final Temporal p0);
    
    Temporal subtractFrom(final Temporal p0);
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    String toString();
}
