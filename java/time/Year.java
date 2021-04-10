package java.time;

import java.util.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.io.*;
import java.time.format.*;

public final class Year implements Temporal, TemporalAdjuster, Comparable<Year>, Serializable
{
    public static final int MIN_VALUE = -999999999;
    public static final int MAX_VALUE = 999999999;
    private static final long serialVersionUID = -23038383694477807L;
    private static final DateTimeFormatter PARSER;
    private final int year;
    
    public static Year now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static Year now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static Year now(final Clock clock) {
        return of(LocalDate.now(clock).getYear());
    }
    
    public static Year of(final int n) {
        ChronoField.YEAR.checkValidValue(n);
        return new Year(n);
    }
    
    public static Year from(TemporalAccessor from) {
        if (from instanceof Year) {
            return (Year)from;
        }
        Objects.requireNonNull(from, "temporal");
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(from))) {
                from = LocalDate.from(from);
            }
            return of(from.get(ChronoField.YEAR));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain Year from TemporalAccessor: " + from + " of type " + from.getClass().getName(), ex);
        }
    }
    
    public static Year parse(final CharSequence charSequence) {
        return parse(charSequence, Year.PARSER);
    }
    
    public static Year parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, Year::from);
    }
    
    public static boolean isLeap(final long n) {
        return (n & 0x3L) == 0x0L && (n % 100L != 0L || n % 400L == 0L);
    }
    
    private Year(final int year) {
        this.year = year;
    }
    
    public int getValue() {
        return this.year;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.YEAR || temporalField == ChronoField.YEAR_OF_ERA || temporalField == ChronoField.ERA;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA || temporalUnit == ChronoUnit.ERAS;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.YEAR_OF_ERA) {
            return (this.year <= 0) ? ValueRange.of(1L, 1000000000L) : ValueRange.of(1L, 999999999L);
        }
        return super.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        return this.range(temporalField).checkValidIntValue(this.getLong(temporalField), temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField)temporalField) {
            case YEAR_OF_ERA: {
                return (this.year < 1) ? (1 - this.year) : this.year;
            }
            case YEAR: {
                return this.year;
            }
            case ERA: {
                return (this.year >= 1) ? 1 : 0;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public boolean isLeap() {
        return isLeap(this.year);
    }
    
    public boolean isValidMonthDay(final MonthDay monthDay) {
        return monthDay != null && monthDay.isValidYear(this.year);
    }
    
    public int length() {
        return this.isLeap() ? 366 : 365;
    }
    
    @Override
    public Year with(final TemporalAdjuster temporalAdjuster) {
        return (Year)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public Year with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        chronoField.checkValidValue(n);
        switch (chronoField) {
            case YEAR_OF_ERA: {
                return of((int)((this.year < 1) ? (1L - n) : n));
            }
            case YEAR: {
                return of((int)n);
            }
            case ERA: {
                return (this.getLong(ChronoField.ERA) == n) ? this : of(1 - this.year);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    @Override
    public Year plus(final TemporalAmount temporalAmount) {
        return (Year)temporalAmount.addTo(this);
    }
    
    @Override
    public Year plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
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
    
    public Year plusYears(final long n) {
        if (n == 0L) {
            return this;
        }
        return of(ChronoField.YEAR.checkValidIntValue(this.year + n));
    }
    
    @Override
    public Year minus(final TemporalAmount temporalAmount) {
        return (Year)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public Year minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public Year minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.YEARS;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        if (!Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(ChronoField.YEAR, this.year);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final Year from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        final long n = from.year - (long)this.year;
        switch ((ChronoUnit)temporalUnit) {
            case YEARS: {
                return n;
            }
            case DECADES: {
                return n / 10L;
            }
            case CENTURIES: {
                return n / 100L;
            }
            case MILLENNIA: {
                return n / 1000L;
            }
            case ERAS: {
                return from.getLong(ChronoField.ERA) - this.getLong(ChronoField.ERA);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public LocalDate atDay(final int n) {
        return LocalDate.ofYearDay(this.year, n);
    }
    
    public YearMonth atMonth(final Month month) {
        return YearMonth.of(this.year, month);
    }
    
    public YearMonth atMonth(final int n) {
        return YearMonth.of(this.year, n);
    }
    
    public LocalDate atMonthDay(final MonthDay monthDay) {
        return monthDay.atYear(this.year);
    }
    
    @Override
    public int compareTo(final Year year) {
        return this.year - year.year;
    }
    
    public boolean isAfter(final Year year) {
        return this.year > year.year;
    }
    
    public boolean isBefore(final Year year) {
        return this.year < year.year;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof Year && this.year == ((Year)o).year);
    }
    
    @Override
    public int hashCode() {
        return this.year;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.year);
    }
    
    private Object writeReplace() {
        return new Ser((byte)11, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
    }
    
    static Year readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readInt());
    }
    
    static {
        PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).toFormatter();
    }
}
