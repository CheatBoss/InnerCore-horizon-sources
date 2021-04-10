package java.time.temporal;

import java.util.function.*;
import java.util.*;
import java.time.*;

public final class TemporalAdjusters
{
    private TemporalAdjusters() {
    }
    
    public static TemporalAdjuster ofDateAdjuster(final UnaryOperator<LocalDate> unaryOperator) {
        Objects.requireNonNull(unaryOperator, "dateBasedAdjuster");
        return temporal -> temporal.with((TemporalAdjuster)unaryOperator.apply(LocalDate.from((TemporalAccessor)temporal)));
    }
    
    public static TemporalAdjuster firstDayOfMonth() {
        return temporal -> temporal.with(ChronoField.DAY_OF_MONTH, 1L);
    }
    
    public static TemporalAdjuster lastDayOfMonth() {
        return temporal -> temporal.with(ChronoField.DAY_OF_MONTH, temporal.range(ChronoField.DAY_OF_MONTH).getMaximum());
    }
    
    public static TemporalAdjuster firstDayOfNextMonth() {
        return temporal -> temporal.with(ChronoField.DAY_OF_MONTH, 1L).plus(1L, ChronoUnit.MONTHS);
    }
    
    public static TemporalAdjuster firstDayOfYear() {
        return temporal -> temporal.with(ChronoField.DAY_OF_YEAR, 1L);
    }
    
    public static TemporalAdjuster lastDayOfYear() {
        return temporal -> temporal.with(ChronoField.DAY_OF_YEAR, temporal.range(ChronoField.DAY_OF_YEAR).getMaximum());
    }
    
    public static TemporalAdjuster firstDayOfNextYear() {
        return temporal -> temporal.with(ChronoField.DAY_OF_YEAR, 1L).plus(1L, ChronoUnit.YEARS);
    }
    
    public static TemporalAdjuster firstInMonth(final DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(1, dayOfWeek);
    }
    
    public static TemporalAdjuster lastInMonth(final DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(-1, dayOfWeek);
    }
    
    public static TemporalAdjuster dayOfWeekInMonth(final int n, final DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        dayOfWeek.getValue();
        if (n >= 0) {
            final Temporal temporal3;
            final int n2;
            return temporal -> {
                temporal.with(ChronoField.DAY_OF_MONTH, 1L);
                return temporal3.plus((int)((n2 - temporal3.get(ChronoField.DAY_OF_WEEK) + 7) % 7 + (n - 1L) * 7L), ChronoUnit.DAYS);
            };
        }
        final int n4;
        final Temporal temporal4;
        final int n3;
        return temporal2 -> {
            temporal2.with(ChronoField.DAY_OF_MONTH, temporal2.range(ChronoField.DAY_OF_MONTH).getMaximum());
            n3 = n4 - temporal4.get(ChronoField.DAY_OF_WEEK);
            return temporal4.plus((int)(((n3 == 0) ? 0 : ((n3 > 0) ? (n3 - 7) : n3)) - (-n - 1L) * 7L), ChronoUnit.DAYS);
        };
    }
    
    public static TemporalAdjuster next(final DayOfWeek dayOfWeek) {
        final int n;
        return temporal -> {
            n = temporal.get(ChronoField.DAY_OF_WEEK) - dayOfWeek.getValue();
            return temporal.plus((n >= 0) ? (7 - n) : ((long)(-n)), ChronoUnit.DAYS);
        };
    }
    
    public static TemporalAdjuster nextOrSame(final DayOfWeek dayOfWeek) {
        final int n;
        final int n2;
        final int n3;
        return temporal -> {
            dayOfWeek.getValue();
            temporal.get(ChronoField.DAY_OF_WEEK);
            if (n == n2) {
                return temporal;
            }
            else {
                return temporal.plus((n3 >= 0) ? (7 - n3) : ((long)(-n3)), ChronoUnit.DAYS);
            }
        };
    }
    
    public static TemporalAdjuster previous(final DayOfWeek dayOfWeek) {
        final int n;
        return temporal -> {
            n = dayOfWeek.getValue() - temporal.get(ChronoField.DAY_OF_WEEK);
            return temporal.minus((n >= 0) ? (7 - n) : ((long)(-n)), ChronoUnit.DAYS);
        };
    }
    
    public static TemporalAdjuster previousOrSame(final DayOfWeek dayOfWeek) {
        final int n;
        final int n2;
        final int n3;
        return temporal -> {
            dayOfWeek.getValue();
            temporal.get(ChronoField.DAY_OF_WEEK);
            if (n == n2) {
                return temporal;
            }
            else {
                return temporal.minus((n3 >= 0) ? (7 - n3) : ((long)(-n3)), ChronoUnit.DAYS);
            }
        };
    }
}
