package java.time;

import java.util.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.io.*;
import java.time.format.*;

public final class YearMonth implements Temporal, TemporalAdjuster, Comparable<YearMonth>, Serializable
{
    private static final long serialVersionUID = 4183400860270640070L;
    private static final DateTimeFormatter PARSER;
    private final int year;
    private final int month;
    
    public static YearMonth now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static YearMonth now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static YearMonth now(final Clock clock) {
        final LocalDate now = LocalDate.now(clock);
        return of(now.getYear(), now.getMonth());
    }
    
    public static YearMonth of(final int n, final Month month) {
        Objects.requireNonNull(month, "month");
        return of(n, month.getValue());
    }
    
    public static YearMonth of(final int n, final int n2) {
        ChronoField.YEAR.checkValidValue(n);
        ChronoField.MONTH_OF_YEAR.checkValidValue(n2);
        return new YearMonth(n, n2);
    }
    
    public static YearMonth from(TemporalAccessor from) {
        if (from instanceof YearMonth) {
            return (YearMonth)from;
        }
        Objects.requireNonNull(from, "temporal");
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(from))) {
                from = LocalDate.from(from);
            }
            return of(from.get(ChronoField.YEAR), from.get(ChronoField.MONTH_OF_YEAR));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain YearMonth from TemporalAccessor: " + from + " of type " + from.getClass().getName(), ex);
        }
    }
    
    public static YearMonth parse(final CharSequence charSequence) {
        return parse(charSequence, YearMonth.PARSER);
    }
    
    public static YearMonth parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, YearMonth::from);
    }
    
    private YearMonth(final int year, final int month) {
        this.year = year;
        this.month = month;
    }
    
    private YearMonth with(final int n, final int n2) {
        if (this.year == n && this.month == n2) {
            return this;
        }
        return new YearMonth(n, n2);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.YEAR || temporalField == ChronoField.MONTH_OF_YEAR || temporalField == ChronoField.PROLEPTIC_MONTH || temporalField == ChronoField.YEAR_OF_ERA || temporalField == ChronoField.ERA;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit == ChronoUnit.MONTHS || temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA || temporalUnit == ChronoUnit.ERAS;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.YEAR_OF_ERA) {
            return (this.getYear() <= 0) ? ValueRange.of(1L, 1000000000L) : ValueRange.of(1L, 999999999L);
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
            case MONTH_OF_YEAR: {
                return this.month;
            }
            case PROLEPTIC_MONTH: {
                return this.getProlepticMonth();
            }
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
    
    private long getProlepticMonth() {
        return this.year * 12L + this.month - 1L;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public int getMonthValue() {
        return this.month;
    }
    
    public Month getMonth() {
        return Month.of(this.month);
    }
    
    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear(this.year);
    }
    
    public boolean isValidDay(final int n) {
        return n >= 1 && n <= this.lengthOfMonth();
    }
    
    public int lengthOfMonth() {
        return this.getMonth().length(this.isLeapYear());
    }
    
    public int lengthOfYear() {
        return this.isLeapYear() ? 366 : 365;
    }
    
    @Override
    public YearMonth with(final TemporalAdjuster temporalAdjuster) {
        return (YearMonth)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public YearMonth with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        chronoField.checkValidValue(n);
        switch (chronoField) {
            case MONTH_OF_YEAR: {
                return this.withMonth((int)n);
            }
            case PROLEPTIC_MONTH: {
                return this.plusMonths(n - this.getProlepticMonth());
            }
            case YEAR_OF_ERA: {
                return this.withYear((int)((this.year < 1) ? (1L - n) : n));
            }
            case YEAR: {
                return this.withYear((int)n);
            }
            case ERA: {
                return (this.getLong(ChronoField.ERA) == n) ? this : this.withYear(1 - this.year);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
    }
    
    public YearMonth withYear(final int n) {
        ChronoField.YEAR.checkValidValue(n);
        return this.with(n, this.month);
    }
    
    public YearMonth withMonth(final int n) {
        ChronoField.MONTH_OF_YEAR.checkValidValue(n);
        return this.with(this.year, n);
    }
    
    @Override
    public YearMonth plus(final TemporalAmount temporalAmount) {
        return (YearMonth)temporalAmount.addTo(this);
    }
    
    @Override
    public YearMonth plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
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
    
    public YearMonth plusYears(final long n) {
        if (n == 0L) {
            return this;
        }
        return this.with(ChronoField.YEAR.checkValidIntValue(this.year + n), this.month);
    }
    
    public YearMonth plusMonths(final long n) {
        if (n == 0L) {
            return this;
        }
        final long n2 = this.year * 12L + (this.month - 1) + n;
        return this.with(ChronoField.YEAR.checkValidIntValue(Math.floorDiv(n2, 12L)), (int)Math.floorMod(n2, 12L) + 1);
    }
    
    @Override
    public YearMonth minus(final TemporalAmount temporalAmount) {
        return (YearMonth)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public YearMonth minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public YearMonth minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public YearMonth minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.MONTHS;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        if (!Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(ChronoField.PROLEPTIC_MONTH, this.getProlepticMonth());
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final YearMonth from = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        final long n = from.getProlepticMonth() - this.getProlepticMonth();
        switch ((ChronoUnit)temporalUnit) {
            case MONTHS: {
                return n;
            }
            case YEARS: {
                return n / 12L;
            }
            case DECADES: {
                return n / 120L;
            }
            case CENTURIES: {
                return n / 1200L;
            }
            case MILLENNIA: {
                return n / 12000L;
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
        return LocalDate.of(this.year, this.month, n);
    }
    
    public LocalDate atEndOfMonth() {
        return LocalDate.of(this.year, this.month, this.lengthOfMonth());
    }
    
    @Override
    public int compareTo(final YearMonth yearMonth) {
        int n = this.year - yearMonth.year;
        if (n == 0) {
            n = this.month - yearMonth.month;
        }
        return n;
    }
    
    public boolean isAfter(final YearMonth yearMonth) {
        return this.compareTo(yearMonth) > 0;
    }
    
    public boolean isBefore(final YearMonth yearMonth) {
        return this.compareTo(yearMonth) < 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof YearMonth) {
            final YearMonth yearMonth = (YearMonth)o;
            return this.year == yearMonth.year && this.month == yearMonth.month;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.year ^ this.month << 27;
    }
    
    @Override
    public String toString() {
        final int abs = Math.abs(this.year);
        final StringBuilder sb = new StringBuilder(9);
        if (abs < 1000) {
            if (this.year < 0) {
                sb.append(this.year - 10000).deleteCharAt(1);
            }
            else {
                sb.append(this.year + 10000).deleteCharAt(0);
            }
        }
        else {
            sb.append(this.year);
        }
        return sb.append((this.month < 10) ? "-0" : "-").append(this.month).toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)12, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
    }
    
    static YearMonth readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte());
    }
    
    static {
        PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).toFormatter();
    }
}
