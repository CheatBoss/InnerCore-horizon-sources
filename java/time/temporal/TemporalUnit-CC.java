package java.time.temporal;

import java.time.*;
import java.time.chrono.*;

public final class TemporalUnit-CC
{
    public static boolean $default$isSupportedBy(final TemporalUnit temporalUnit, final Temporal temporal) {
        if (temporal instanceof LocalTime) {
            return temporalUnit.isTimeBased();
        }
        if (temporal instanceof ChronoLocalDate) {
            return temporalUnit.isDateBased();
        }
        if (!(temporal instanceof ChronoLocalDateTime)) {
            if (temporal instanceof ChronoZonedDateTime) {
                return true;
            }
            try {
                temporal.plus(1L, temporalUnit);
                return true;
            }
            catch (RuntimeException ex) {
                try {
                    temporal.plus(-1L, temporalUnit);
                    return true;
                }
                catch (RuntimeException ex2) {
                    return false;
                }
            }
            catch (UnsupportedTemporalTypeException ex3) {
                return false;
            }
        }
        return true;
    }
}
