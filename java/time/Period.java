package java.time;

import java.time.format.*;
import java.util.regex.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.io.*;
import java.util.*;

public final class Period implements ChronoPeriod, Serializable
{
    public static final Period ZERO;
    private static final long serialVersionUID = -3587258372562876L;
    private static final Pattern PATTERN;
    private static final List<TemporalUnit> SUPPORTED_UNITS;
    private final int years;
    private final int months;
    private final int days;
    
    public static Period ofYears(final int n) {
        return create(n, 0, 0);
    }
    
    public static Period ofMonths(final int n) {
        return create(0, n, 0);
    }
    
    public static Period ofWeeks(final int n) {
        return create(0, 0, Math.multiplyExact(n, 7));
    }
    
    public static Period ofDays(final int n) {
        return create(0, 0, n);
    }
    
    public static Period of(final int n, final int n2, final int n3) {
        return create(n, n2, n3);
    }
    
    public static Period from(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return (Period)temporalAmount;
        }
        if (temporalAmount instanceof ChronoPeriod && !IsoChronology.INSTANCE.equals(((ChronoPeriod)temporalAmount).getChronology())) {
            throw new DateTimeException("Period requires ISO chronology: " + temporalAmount);
        }
        Objects.requireNonNull(temporalAmount, "amount");
        int intExact = 0;
        int intExact2 = 0;
        int intExact3 = 0;
        for (final TemporalUnit temporalUnit : temporalAmount.getUnits()) {
            final long value = temporalAmount.get(temporalUnit);
            if (temporalUnit == ChronoUnit.YEARS) {
                intExact = Math.toIntExact(value);
            }
            else if (temporalUnit == ChronoUnit.MONTHS) {
                intExact2 = Math.toIntExact(value);
            }
            else {
                if (temporalUnit != ChronoUnit.DAYS) {
                    throw new DateTimeException("Unit must be Years, Months or Days, but was " + temporalUnit);
                }
                intExact3 = Math.toIntExact(value);
            }
        }
        return create(intExact, intExact2, intExact3);
    }
    
    public static Period parse(final CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        final Matcher matcher = Period.PATTERN.matcher(charSequence);
        if (matcher.matches()) {
            final int n = "-".equals(matcher.group(1)) ? -1 : 1;
            final String group = matcher.group(2);
            final String group2 = matcher.group(3);
            final String group3 = matcher.group(4);
            final String group4 = matcher.group(5);
            if (group == null && group2 == null && group4 == null) {
                if (group3 == null) {
                    throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0);
                }
            }
            try {
                return create(parseNumber(charSequence, group, n), parseNumber(charSequence, group2, n), Math.addExact(parseNumber(charSequence, group4, n), Math.multiplyExact(parseNumber(charSequence, group3, n), 7)));
            }
            catch (NumberFormatException ex) {
                throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, ex);
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0);
    }
    
    private static int parseNumber(final CharSequence charSequence, final String s, final int n) {
        if (s == null) {
            return 0;
        }
        final int int1 = Integer.parseInt(s);
        try {
            return Math.multiplyExact(int1, n);
        }
        catch (ArithmeticException ex) {
            throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, ex);
        }
    }
    
    public static Period between(final LocalDate localDate, final LocalDate localDate2) {
        return localDate.until((ChronoLocalDate)localDate2);
    }
    
    private static Period create(final int n, final int n2, final int n3) {
        if ((n | n2 | n3) == 0x0) {
            return Period.ZERO;
        }
        return new Period(n, n2, n3);
    }
    
    private Period(final int years, final int months, final int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }
    
    @Override
    public long get(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return this.getYears();
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return this.getMonths();
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return this.getDays();
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
    }
    
    @Override
    public List<TemporalUnit> getUnits() {
        return Period.SUPPORTED_UNITS;
    }
    
    @Override
    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }
    
    @Override
    public boolean isZero() {
        return this == Period.ZERO;
    }
    
    @Override
    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }
    
    public int getYears() {
        return this.years;
    }
    
    public int getMonths() {
        return this.months;
    }
    
    public int getDays() {
        return this.days;
    }
    
    public Period withYears(final int n) {
        if (n == this.years) {
            return this;
        }
        return create(n, this.months, this.days);
    }
    
    public Period withMonths(final int n) {
        if (n == this.months) {
            return this;
        }
        return create(this.years, n, this.days);
    }
    
    public Period withDays(final int n) {
        if (n == this.days) {
            return this;
        }
        return create(this.years, this.months, n);
    }
    
    @Override
    public Period plus(final TemporalAmount temporalAmount) {
        final Period from = from(temporalAmount);
        return create(Math.addExact(this.years, from.years), Math.addExact(this.months, from.months), Math.addExact(this.days, from.days));
    }
    
    public Period plusYears(final long n) {
        if (n == 0L) {
            return this;
        }
        return create(Math.toIntExact(Math.addExact(this.years, n)), this.months, this.days);
    }
    
    public Period plusMonths(final long n) {
        if (n == 0L) {
            return this;
        }
        return create(this.years, Math.toIntExact(Math.addExact(this.months, n)), this.days);
    }
    
    public Period plusDays(final long n) {
        if (n == 0L) {
            return this;
        }
        return create(this.years, this.months, Math.toIntExact(Math.addExact(this.days, n)));
    }
    
    @Override
    public Period minus(final TemporalAmount temporalAmount) {
        final Period from = from(temporalAmount);
        return create(Math.subtractExact(this.years, from.years), Math.subtractExact(this.months, from.months), Math.subtractExact(this.days, from.days));
    }
    
    public Period minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public Period minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    public Period minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    @Override
    public Period multipliedBy(final int n) {
        if (this == Period.ZERO || n == 1) {
            return this;
        }
        return create(Math.multiplyExact(this.years, n), Math.multiplyExact(this.months, n), Math.multiplyExact(this.days, n));
    }
    
    @Override
    public Period negated() {
        return this.multipliedBy(-1);
    }
    
    @Override
    public Period normalized() {
        final long totalMonths = this.toTotalMonths();
        final long n = totalMonths / 12L;
        final int n2 = (int)(totalMonths % 12L);
        if (n == this.years && n2 == this.months) {
            return this;
        }
        return create(Math.toIntExact(n), n2, this.days);
    }
    
    public long toTotalMonths() {
        return this.years * 12L + this.months;
    }
    
    @Override
    public Temporal addTo(Temporal temporal) {
        this.validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.plus(this.years, ChronoUnit.YEARS);
            }
        }
        else {
            final long totalMonths = this.toTotalMonths();
            if (totalMonths != 0L) {
                temporal = temporal.plus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.plus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }
    
    @Override
    public Temporal subtractFrom(Temporal temporal) {
        this.validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.minus(this.years, ChronoUnit.YEARS);
            }
        }
        else {
            final long totalMonths = this.toTotalMonths();
            if (totalMonths != 0L) {
                temporal = temporal.minus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.minus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }
    
    private void validateChrono(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final Chronology chronology = temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null && !IsoChronology.INSTANCE.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: ISO, actual: " + chronology.getId());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Period) {
            final Period period = (Period)o;
            return this.years == period.years && this.months == period.months && this.days == period.days;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.years + Integer.rotateLeft(this.months, 8) + Integer.rotateLeft(this.days, 16);
    }
    
    @Override
    public String toString() {
        if (this == Period.ZERO) {
            return "P0D";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('P');
        if (this.years != 0) {
            sb.append(this.years).append('Y');
        }
        if (this.months != 0) {
            sb.append(this.months).append('M');
        }
        if (this.days != 0) {
            sb.append(this.days).append('D');
        }
        return sb.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)14, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }
    
    static Period readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
    
    static {
        ZERO = new Period(0, 0, 0);
        PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", 2);
        SUPPORTED_UNITS = Collections.unmodifiableList((List<? extends TemporalUnit>)Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS));
    }
}
