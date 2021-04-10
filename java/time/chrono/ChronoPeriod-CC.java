package java.time.chrono;

import java.time.temporal.*;
import java.util.*;

public final class ChronoPeriod-CC
{
    public static boolean $default$isNegative(final ChronoPeriod chronoPeriod) {
        final Iterator<TemporalUnit> iterator = chronoPeriod.getUnits().iterator();
        while (iterator.hasNext()) {
            if (chronoPeriod.get(iterator.next()) < 0L) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean $default$isZero(final ChronoPeriod chronoPeriod) {
        final Iterator<TemporalUnit> iterator = chronoPeriod.getUnits().iterator();
        while (iterator.hasNext()) {
            if (chronoPeriod.get(iterator.next()) != 0L) {
                return false;
            }
        }
        return true;
    }
    
    public static ChronoPeriod $default$negated(final ChronoPeriod chronoPeriod) {
        return chronoPeriod.multipliedBy(-1);
    }
    
    public static ChronoPeriod between(final ChronoLocalDate chronoLocalDate, final ChronoLocalDate chronoLocalDate2) {
        Objects.requireNonNull(chronoLocalDate, "startDateInclusive");
        Objects.requireNonNull(chronoLocalDate2, "endDateExclusive");
        return chronoLocalDate.until(chronoLocalDate2);
    }
}
