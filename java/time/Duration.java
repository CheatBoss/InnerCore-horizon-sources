package java.time;

import java.time.format.*;
import java.util.regex.*;
import java.time.temporal.*;
import java.math.*;
import java.io.*;
import java.util.*;

public final class Duration implements TemporalAmount, Comparable<Duration>, Serializable
{
    public static final Duration ZERO;
    private static final long serialVersionUID = 3078945930695997490L;
    private static final BigInteger BI_NANOS_PER_SECOND;
    private static final Pattern PATTERN;
    private final long seconds;
    private final int nanos;
    
    public static Duration ofDays(final long n) {
        return create(Math.multiplyExact(n, 86400L), 0);
    }
    
    public static Duration ofHours(final long n) {
        return create(Math.multiplyExact(n, 3600L), 0);
    }
    
    public static Duration ofMinutes(final long n) {
        return create(Math.multiplyExact(n, 60L), 0);
    }
    
    public static Duration ofSeconds(final long n) {
        return create(n, 0);
    }
    
    public static Duration ofSeconds(final long n, final long n2) {
        return create(Math.addExact(n, Math.floorDiv(n2, 1000000000L)), (int)Math.floorMod(n2, 1000000000L));
    }
    
    public static Duration ofMillis(final long n) {
        long n2 = n / 1000L;
        int n3 = (int)(n % 1000L);
        if (n3 < 0) {
            n3 += 1000;
            --n2;
        }
        return create(n2, n3 * 1000000);
    }
    
    public static Duration ofNanos(final long n) {
        long n2 = n / 1000000000L;
        int n3 = (int)(n % 1000000000L);
        if (n3 < 0) {
            n3 += (int)1000000000L;
            --n2;
        }
        return create(n2, n3);
    }
    
    public static Duration of(final long n, final TemporalUnit temporalUnit) {
        return Duration.ZERO.plus(n, temporalUnit);
    }
    
    public static Duration from(final TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, "amount");
        Duration duration = Duration.ZERO;
        for (final TemporalUnit temporalUnit : temporalAmount.getUnits()) {
            duration = duration.plus(temporalAmount.get(temporalUnit), temporalUnit);
        }
        return duration;
    }
    
    public static Duration parse(final CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        final Matcher matcher = Duration.PATTERN.matcher(charSequence);
        if (matcher.matches() && !"T".equals(matcher.group(3))) {
            final boolean equals = "-".equals(matcher.group(1));
            final String group = matcher.group(2);
            final String group2 = matcher.group(4);
            final String group3 = matcher.group(5);
            final String group4 = matcher.group(6);
            final String group5 = matcher.group(7);
            if (group != null || group2 != null || group3 != null || group4 != null) {
                final long number = parseNumber(charSequence, group, 86400, "days");
                final long number2 = parseNumber(charSequence, group2, 3600, "hours");
                final long number3 = parseNumber(charSequence, group3, 60, "minutes");
                final long number4 = parseNumber(charSequence, group4, 1, "seconds");
                final int fraction = parseFraction(charSequence, group5, (number4 < 0L) ? -1 : 1);
                try {
                    return create(equals, number, number2, number3, number4, fraction);
                }
                catch (ArithmeticException ex) {
                    throw (DateTimeParseException)new DateTimeParseException("Text cannot be parsed to a Duration: overflow", charSequence, 0).initCause(ex);
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Duration", charSequence, 0);
    }
    
    private static long parseNumber(final CharSequence charSequence, final String s, final int n, final String s2) {
        if (s == null) {
            return 0L;
        }
        try {
            return Math.multiplyExact(Long.parseLong(s), n);
        }
        catch (NumberFormatException | ArithmeticException ex) {
            final Object o;
            throw (DateTimeParseException)new DateTimeParseException("Text cannot be parsed to a Duration: " + s2, charSequence, 0).initCause((Throwable)o);
        }
    }
    
    private static int parseFraction(final CharSequence charSequence, String substring, final int n) {
        if (substring == null || substring.length() == 0) {
            return 0;
        }
        try {
            substring = (substring + "000000000").substring(0, 9);
            return Integer.parseInt(substring) * n;
        }
        catch (NumberFormatException | ArithmeticException ex) {
            final Object o;
            throw (DateTimeParseException)new DateTimeParseException("Text cannot be parsed to a Duration: fraction", charSequence, 0).initCause((Throwable)o);
        }
    }
    
    private static Duration create(final boolean b, final long n, final long n2, final long n3, final long n4, final int n5) {
        final long addExact = Math.addExact(n, Math.addExact(n2, Math.addExact(n3, n4)));
        if (b) {
            return ofSeconds(addExact, n5).negated();
        }
        return ofSeconds(addExact, n5);
    }
    
    public static Duration between(final Temporal temporal, final Temporal temporal2) {
        try {
            return ofNanos(temporal.until(temporal2, ChronoUnit.NANOS));
        }
        catch (DateTimeException | ArithmeticException ex) {
            long until = temporal.until(temporal2, ChronoUnit.SECONDS);
            long n;
            try {
                n = temporal2.getLong(ChronoField.NANO_OF_SECOND) - temporal.getLong(ChronoField.NANO_OF_SECOND);
                if (until > 0L && n < 0L) {
                    ++until;
                }
                else if (until < 0L && n > 0L) {
                    --until;
                }
            }
            catch (DateTimeException ex2) {
                n = 0L;
            }
            return ofSeconds(until, n);
        }
    }
    
    private static Duration create(final long n, final int n2) {
        if ((n | (long)n2) == 0x0L) {
            return Duration.ZERO;
        }
        return new Duration(n, n2);
    }
    
    private Duration(final long seconds, final int nanos) {
        this.seconds = seconds;
        this.nanos = nanos;
    }
    
    @Override
    public long get(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.SECONDS) {
            return this.seconds;
        }
        if (temporalUnit == ChronoUnit.NANOS) {
            return this.nanos;
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
    }
    
    @Override
    public List<TemporalUnit> getUnits() {
        return DurationUnits.UNITS;
    }
    
    public boolean isZero() {
        return (this.seconds | (long)this.nanos) == 0x0L;
    }
    
    public boolean isNegative() {
        return this.seconds < 0L;
    }
    
    public long getSeconds() {
        return this.seconds;
    }
    
    public int getNano() {
        return this.nanos;
    }
    
    public Duration withSeconds(final long n) {
        return create(n, this.nanos);
    }
    
    public Duration withNanos(final int n) {
        ChronoField.NANO_OF_SECOND.checkValidIntValue(n);
        return create(this.seconds, n);
    }
    
    public Duration plus(final Duration duration) {
        return this.plus(duration.getSeconds(), duration.getNano());
    }
    
    public Duration plus(final long n, final TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporalUnit, "unit");
        if (temporalUnit == ChronoUnit.DAYS) {
            return this.plus(Math.multiplyExact(n, 86400L), 0L);
        }
        if (temporalUnit.isDurationEstimated()) {
            throw new UnsupportedTemporalTypeException("Unit must not have an estimated duration");
        }
        if (n == 0L) {
            return this;
        }
        if (!(temporalUnit instanceof ChronoUnit)) {
            final Duration multipliedBy = temporalUnit.getDuration().multipliedBy(n);
            return this.plusSeconds(multipliedBy.getSeconds()).plusNanos(multipliedBy.getNano());
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.plusNanos(n);
            }
            case MICROS: {
                return this.plusSeconds(n / 1000000000L * 1000L).plusNanos(n % 1000000000L * 1000L);
            }
            case MILLIS: {
                return this.plusMillis(n);
            }
            case SECONDS: {
                return this.plusSeconds(n);
            }
            default: {
                return this.plusSeconds(Math.multiplyExact(temporalUnit.getDuration().seconds, n));
            }
        }
    }
    
    public Duration plusDays(final long n) {
        return this.plus(Math.multiplyExact(n, 86400L), 0L);
    }
    
    public Duration plusHours(final long n) {
        return this.plus(Math.multiplyExact(n, 3600L), 0L);
    }
    
    public Duration plusMinutes(final long n) {
        return this.plus(Math.multiplyExact(n, 60L), 0L);
    }
    
    public Duration plusSeconds(final long n) {
        return this.plus(n, 0L);
    }
    
    public Duration plusMillis(final long n) {
        return this.plus(n / 1000L, n % 1000L * 1000000L);
    }
    
    public Duration plusNanos(final long n) {
        return this.plus(0L, n);
    }
    
    private Duration plus(final long n, long n2) {
        if ((n | n2) == 0x0L) {
            return this;
        }
        final long addExact = Math.addExact(Math.addExact(this.seconds, n), n2 / 1000000000L);
        n2 %= 1000000000L;
        return ofSeconds(addExact, this.nanos + n2);
    }
    
    public Duration minus(final Duration duration) {
        final long seconds = duration.getSeconds();
        final int nano = duration.getNano();
        if (seconds == Long.MIN_VALUE) {
            return this.plus(Long.MAX_VALUE, -nano).plus(1L, 0L);
        }
        return this.plus(-seconds, -nano);
    }
    
    public Duration minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public Duration minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    public Duration minusHours(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusHours(Long.MAX_VALUE).plusHours(1L) : this.plusHours(-n);
    }
    
    public Duration minusMinutes(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMinutes(Long.MAX_VALUE).plusMinutes(1L) : this.plusMinutes(-n);
    }
    
    public Duration minusSeconds(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusSeconds(Long.MAX_VALUE).plusSeconds(1L) : this.plusSeconds(-n);
    }
    
    public Duration minusMillis(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMillis(Long.MAX_VALUE).plusMillis(1L) : this.plusMillis(-n);
    }
    
    public Duration minusNanos(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusNanos(Long.MAX_VALUE).plusNanos(1L) : this.plusNanos(-n);
    }
    
    public Duration multipliedBy(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        if (n == 1L) {
            return this;
        }
        return create(this.toSeconds().multiply(BigDecimal.valueOf(n)));
    }
    
    public Duration dividedBy(final long n) {
        if (n == 0L) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        if (n == 1L) {
            return this;
        }
        return create(this.toSeconds().divide(BigDecimal.valueOf(n), RoundingMode.DOWN));
    }
    
    private BigDecimal toSeconds() {
        return BigDecimal.valueOf(this.seconds).add(BigDecimal.valueOf(this.nanos, 9));
    }
    
    private static Duration create(final BigDecimal bigDecimal) {
        final BigInteger bigIntegerExact = bigDecimal.movePointRight(9).toBigIntegerExact();
        final BigInteger[] divideAndRemainder = bigIntegerExact.divideAndRemainder(Duration.BI_NANOS_PER_SECOND);
        if (divideAndRemainder[0].bitLength() > 63) {
            throw new ArithmeticException("Exceeds capacity of Duration: " + bigIntegerExact);
        }
        return ofSeconds(divideAndRemainder[0].longValue(), divideAndRemainder[1].intValue());
    }
    
    public Duration negated() {
        return this.multipliedBy(-1L);
    }
    
    public Duration abs() {
        return this.isNegative() ? this.negated() : this;
    }
    
    @Override
    public Temporal addTo(Temporal temporal) {
        if (this.seconds != 0L) {
            temporal = temporal.plus(this.seconds, ChronoUnit.SECONDS);
        }
        if (this.nanos != 0) {
            temporal = temporal.plus(this.nanos, ChronoUnit.NANOS);
        }
        return temporal;
    }
    
    @Override
    public Temporal subtractFrom(Temporal temporal) {
        if (this.seconds != 0L) {
            temporal = temporal.minus(this.seconds, ChronoUnit.SECONDS);
        }
        if (this.nanos != 0) {
            temporal = temporal.minus(this.nanos, ChronoUnit.NANOS);
        }
        return temporal;
    }
    
    public long toDays() {
        return this.seconds / 86400L;
    }
    
    public long toHours() {
        return this.seconds / 3600L;
    }
    
    public long toMinutes() {
        return this.seconds / 60L;
    }
    
    public long toMillis() {
        return Math.addExact(Math.multiplyExact(this.seconds, 1000L), this.nanos / 1000000);
    }
    
    public long toNanos() {
        return Math.addExact(Math.multiplyExact(this.seconds, 1000000000L), this.nanos);
    }
    
    @Override
    public int compareTo(final Duration duration) {
        final int compare = Long.compare(this.seconds, duration.seconds);
        if (compare != 0) {
            return compare;
        }
        return this.nanos - duration.nanos;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Duration) {
            final Duration duration = (Duration)o;
            return this.seconds == duration.seconds && this.nanos == duration.nanos;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.seconds ^ this.seconds >>> 32) + 51 * this.nanos;
    }
    
    @Override
    public String toString() {
        if (this == Duration.ZERO) {
            return "PT0S";
        }
        final long n = this.seconds / 3600L;
        final int n2 = (int)(this.seconds % 3600L / 60L);
        final int n3 = (int)(this.seconds % 60L);
        final StringBuilder sb = new StringBuilder(24);
        sb.append("PT");
        if (n != 0L) {
            sb.append(n).append('H');
        }
        if (n2 != 0) {
            sb.append(n2).append('M');
        }
        if (n3 == 0 && this.nanos == 0 && sb.length() > 2) {
            return sb.toString();
        }
        if (n3 < 0 && this.nanos > 0) {
            if (n3 == -1) {
                sb.append("-0");
            }
            else {
                sb.append(n3 + 1);
            }
        }
        else {
            sb.append(n3);
        }
        if (this.nanos > 0) {
            final int length = sb.length();
            if (n3 < 0) {
                sb.append(2000000000L - this.nanos);
            }
            else {
                sb.append(this.nanos + 1000000000L);
            }
            while (sb.charAt(sb.length() - 1) == '0') {
                sb.setLength(sb.length() - 1);
            }
            sb.setCharAt(length, '.');
        }
        sb.append('S');
        return sb.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)1, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }
    
    static Duration readExternal(final DataInput dataInput) throws IOException {
        return ofSeconds(dataInput.readLong(), dataInput.readInt());
    }
    
    static {
        ZERO = new Duration(0L, 0);
        BI_NANOS_PER_SECOND = BigInteger.valueOf(1000000000L);
        PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);
    }
    
    private static class DurationUnits
    {
        static final List<TemporalUnit> UNITS;
        
        static {
            UNITS = Collections.unmodifiableList((List<? extends TemporalUnit>)Arrays.asList(ChronoUnit.SECONDS, ChronoUnit.NANOS));
        }
    }
}
