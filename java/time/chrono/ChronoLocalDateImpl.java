package java.time.chrono;

import java.io.*;
import java.util.*;
import java.time.temporal.*;

abstract class ChronoLocalDateImpl<D extends ChronoLocalDate> implements ChronoLocalDate, Temporal, TemporalAdjuster, Serializable
{
    private static final long serialVersionUID = 6282433883239719096L;
    
    static <D extends ChronoLocalDate> D ensureValid(final Chronology chronology, final Temporal temporal) {
        final ChronoLocalDate chronoLocalDate = (ChronoLocalDate)temporal;
        if (!chronology.equals(chronoLocalDate.getChronology())) {
            throw new ClassCastException("Chronology mismatch, expected: " + chronology.getId() + ", actual: " + chronoLocalDate.getChronology().getId());
        }
        return (D)chronoLocalDate;
    }
    
    @Override
    public D with(final TemporalAdjuster temporalAdjuster) {
        return (D)super.with(temporalAdjuster);
    }
    
    @Override
    public D with(final TemporalField temporalField, final long n) {
        return (D)super.with(temporalField, n);
    }
    
    @Override
    public D plus(final TemporalAmount temporalAmount) {
        return (D)super.plus(temporalAmount);
    }
    
    @Override
    public D plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (D)super.plus(n, temporalUnit);
        }
        switch ((ChronoUnit)temporalUnit) {
            case DAYS: {
                return this.plusDays(n);
            }
            case WEEKS: {
                return this.plusDays(Math.multiplyExact(n, 7L));
            }
            case MONTHS: {
                return this.plusMonths(n);
            }
            case YEARS: {
                return this.plusYears(n);
            }
            case DECADES: {
                return this.plusYears(Math.multiplyExact(n, 10L));
            }
            case CENTURIES: {
                return this.plusYears(Math.multiplyExact(n, 100L));
            }
            case MILLENNIA: {
                return this.plusYears(Math.multiplyExact(n, 1000L));
            }
            case ERAS: {
                return this.with((TemporalField)ChronoField.ERA, Math.addExact(this.getLong(ChronoField.ERA), n));
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    @Override
    public D minus(final TemporalAmount temporalAmount) {
        return (D)super.minus(temporalAmount);
    }
    
    @Override
    public D minus(final long n, final TemporalUnit temporalUnit) {
        return (D)super.minus(n, temporalUnit);
    }
    
    abstract D plusYears(final long p0);
    
    abstract D plusMonths(final long p0);
    
    D plusWeeks(final long n) {
        return this.plusDays(Math.multiplyExact(n, 7L));
    }
    
    abstract D plusDays(final long p0);
    
    D minusYears(final long n) {
        return (D)((n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n));
    }
    
    D minusMonths(final long n) {
        return (D)((n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n));
    }
    
    D minusWeeks(final long n) {
        return (D)((n == Long.MIN_VALUE) ? this.plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : this.plusWeeks(-n));
    }
    
    D minusDays(final long n) {
        return (D)((n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n));
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        final ChronoLocalDate date = this.getChronology().date(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            Objects.requireNonNull(temporalUnit, "unit");
            return temporalUnit.between(this, date);
        }
        switch ((ChronoUnit)temporalUnit) {
            case DAYS: {
                return this.daysUntil(date);
            }
            case WEEKS: {
                return this.daysUntil(date) / 7L;
            }
            case MONTHS: {
                return this.monthsUntil(date);
            }
            case YEARS: {
                return this.monthsUntil(date) / 12L;
            }
            case DECADES: {
                return this.monthsUntil(date) / 120L;
            }
            case CENTURIES: {
                return this.monthsUntil(date) / 1200L;
            }
            case MILLENNIA: {
                return this.monthsUntil(date) / 12000L;
            }
            case ERAS: {
                return date.getLong(ChronoField.ERA) - this.getLong(ChronoField.ERA);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    private long daysUntil(final ChronoLocalDate chronoLocalDate) {
        return chronoLocalDate.toEpochDay() - this.toEpochDay();
    }
    
    private long monthsUntil(final ChronoLocalDate chronoLocalDate) {
        if (this.getChronology().range(ChronoField.MONTH_OF_YEAR).getMaximum() != 12L) {
            throw new IllegalStateException("ChronoLocalDateImpl only supports Chronologies with 12 months per year");
        }
        return (chronoLocalDate.getLong(ChronoField.PROLEPTIC_MONTH) * 32L + chronoLocalDate.get(ChronoField.DAY_OF_MONTH) - (this.getLong(ChronoField.PROLEPTIC_MONTH) * 32L + this.get(ChronoField.DAY_OF_MONTH))) / 32L;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ChronoLocalDate && this.compareTo((ChronoLocalDate)o) == 0);
    }
    
    @Override
    public int hashCode() {
        final long epochDay = this.toEpochDay();
        return this.getChronology().hashCode() ^ (int)(epochDay ^ epochDay >>> 32);
    }
    
    @Override
    public String toString() {
        final long long1 = this.getLong(ChronoField.YEAR_OF_ERA);
        final long long2 = this.getLong(ChronoField.MONTH_OF_YEAR);
        final long long3 = this.getLong(ChronoField.DAY_OF_MONTH);
        final StringBuilder sb = new StringBuilder(30);
        sb.append(this.getChronology().toString()).append(" ").append(this.getEra()).append(" ").append(long1).append((long2 < 10L) ? "-0" : "-").append(long2).append((long3 < 10L) ? "-0" : "-").append(long3);
        return sb.toString();
    }
}
