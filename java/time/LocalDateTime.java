package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;
import java.time.chrono.*;

public final class LocalDateTime implements Temporal, TemporalAdjuster, ChronoLocalDateTime<LocalDate>, Serializable
{
    public static final LocalDateTime MIN;
    public static final LocalDateTime MAX;
    private static final long serialVersionUID = 6207766400415563566L;
    private final LocalDate date;
    private final LocalTime time;
    
    public static LocalDateTime now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static LocalDateTime now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static LocalDateTime now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant instant = clock.instant();
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), clock.getZone().getRules().getOffset(instant));
    }
    
    public static LocalDateTime of(final int n, final Month month, final int n2, final int n3, final int n4) {
        return new LocalDateTime(LocalDate.of(n, month, n2), LocalTime.of(n3, n4));
    }
    
    public static LocalDateTime of(final int n, final Month month, final int n2, final int n3, final int n4, final int n5) {
        return new LocalDateTime(LocalDate.of(n, month, n2), LocalTime.of(n3, n4, n5));
    }
    
    public static LocalDateTime of(final int n, final Month month, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return new LocalDateTime(LocalDate.of(n, month, n2), LocalTime.of(n3, n4, n5, n6));
    }
    
    public static LocalDateTime of(final int n, final int n2, final int n3, final int n4, final int n5) {
        return new LocalDateTime(LocalDate.of(n, n2, n3), LocalTime.of(n4, n5));
    }
    
    public static LocalDateTime of(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return new LocalDateTime(LocalDate.of(n, n2, n3), LocalTime.of(n4, n5, n6));
    }
    
    public static LocalDateTime of(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        return new LocalDateTime(LocalDate.of(n, n2, n3), LocalTime.of(n4, n5, n6, n7));
    }
    
    public static LocalDateTime of(final LocalDate localDate, final LocalTime localTime) {
        Objects.requireNonNull(localDate, "date");
        Objects.requireNonNull(localTime, "time");
        return new LocalDateTime(localDate, localTime);
    }
    
    public static LocalDateTime ofInstant(final Instant instant, final ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), zoneId.getRules().getOffset(instant));
    }
    
    public static LocalDateTime ofEpochSecond(final long n, final int n2, final ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        ChronoField.NANO_OF_SECOND.checkValidValue(n2);
        final long n3 = n + zoneOffset.getTotalSeconds();
        return new LocalDateTime(LocalDate.ofEpochDay(Math.floorDiv(n3, 86400L)), LocalTime.ofNanoOfDay((int)Math.floorMod(n3, 86400L) * 1000000000L + n2));
    }
    
    public static LocalDateTime from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof LocalDateTime) {
            return (LocalDateTime)temporalAccessor;
        }
        if (temporalAccessor instanceof ZonedDateTime) {
            return ((ZonedDateTime)temporalAccessor).toLocalDateTime();
        }
        if (temporalAccessor instanceof OffsetDateTime) {
            return ((OffsetDateTime)temporalAccessor).toLocalDateTime();
        }
        try {
            return new LocalDateTime(LocalDate.from(temporalAccessor), LocalTime.from(temporalAccessor));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain LocalDateTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), ex);
        }
    }
    
    public static LocalDateTime parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    public static LocalDateTime parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, LocalDateTime::from);
    }
    
    private LocalDateTime(final LocalDate date, final LocalTime time) {
        this.date = date;
        this.time = time;
    }
    
    private LocalDateTime with(final LocalDate localDate, final LocalTime localTime) {
        if (this.date == localDate && this.time == localTime) {
            return this;
        }
        return new LocalDateTime(localDate, localTime);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            final ChronoField chronoField = (ChronoField)temporalField;
            return chronoField.isDateBased() || chronoField.isTimeBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.range(temporalField) : this.date.range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.get(temporalField) : this.date.get(temporalField);
        }
        return super.get(temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField)temporalField).isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    @Override
    public LocalDate toLocalDate() {
        return this.date;
    }
    
    public int getYear() {
        return this.date.getYear();
    }
    
    public int getMonthValue() {
        return this.date.getMonthValue();
    }
    
    public Month getMonth() {
        return this.date.getMonth();
    }
    
    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }
    
    public int getDayOfYear() {
        return this.date.getDayOfYear();
    }
    
    public DayOfWeek getDayOfWeek() {
        return this.date.getDayOfWeek();
    }
    
    @Override
    public LocalTime toLocalTime() {
        return this.time;
    }
    
    public int getHour() {
        return this.time.getHour();
    }
    
    public int getMinute() {
        return this.time.getMinute();
    }
    
    public int getSecond() {
        return this.time.getSecond();
    }
    
    public int getNano() {
        return this.time.getNano();
    }
    
    @Override
    public LocalDateTime with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return this.with((LocalDate)temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return this.with(this.date, (LocalTime)temporalAdjuster);
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return (LocalDateTime)temporalAdjuster;
        }
        return (LocalDateTime)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public LocalDateTime with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        if (((ChronoField)temporalField).isTimeBased()) {
            return this.with(this.date, this.time.with(temporalField, n));
        }
        return this.with(this.date.with(temporalField, n), this.time);
    }
    
    public LocalDateTime withYear(final int n) {
        return this.with(this.date.withYear(n), this.time);
    }
    
    public LocalDateTime withMonth(final int n) {
        return this.with(this.date.withMonth(n), this.time);
    }
    
    public LocalDateTime withDayOfMonth(final int n) {
        return this.with(this.date.withDayOfMonth(n), this.time);
    }
    
    public LocalDateTime withDayOfYear(final int n) {
        return this.with(this.date.withDayOfYear(n), this.time);
    }
    
    public LocalDateTime withHour(final int n) {
        return this.with(this.date, this.time.withHour(n));
    }
    
    public LocalDateTime withMinute(final int n) {
        return this.with(this.date, this.time.withMinute(n));
    }
    
    public LocalDateTime withSecond(final int n) {
        return this.with(this.date, this.time.withSecond(n));
    }
    
    public LocalDateTime withNano(final int n) {
        return this.with(this.date, this.time.withNano(n));
    }
    
    public LocalDateTime truncatedTo(final TemporalUnit temporalUnit) {
        return this.with(this.date, this.time.truncatedTo(temporalUnit));
    }
    
    @Override
    public LocalDateTime plus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return this.with(this.date.plus((TemporalAmount)temporalAmount), this.time);
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (LocalDateTime)temporalAmount.addTo(this);
    }
    
    @Override
    public LocalDateTime plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                return this.plusNanos(n);
            }
            case MICROS: {
                return this.plusDays(n / 86400000000L).plusNanos(n % 86400000000L * 1000L);
            }
            case MILLIS: {
                return this.plusDays(n / 86400000L).plusNanos(n % 86400000L * 1000000L);
            }
            case SECONDS: {
                return this.plusSeconds(n);
            }
            case MINUTES: {
                return this.plusMinutes(n);
            }
            case HOURS: {
                return this.plusHours(n);
            }
            case HALF_DAYS: {
                return this.plusDays(n / 256L).plusHours(n % 256L * 12L);
            }
            default: {
                return this.with(this.date.plus(n, temporalUnit), this.time);
            }
        }
    }
    
    public LocalDateTime plusYears(final long n) {
        return this.with(this.date.plusYears(n), this.time);
    }
    
    public LocalDateTime plusMonths(final long n) {
        return this.with(this.date.plusMonths(n), this.time);
    }
    
    public LocalDateTime plusWeeks(final long n) {
        return this.with(this.date.plusWeeks(n), this.time);
    }
    
    public LocalDateTime plusDays(final long n) {
        return this.with(this.date.plusDays(n), this.time);
    }
    
    public LocalDateTime plusHours(final long n) {
        return this.plusWithOverflow(this.date, n, 0L, 0L, 0L, 1);
    }
    
    public LocalDateTime plusMinutes(final long n) {
        return this.plusWithOverflow(this.date, 0L, n, 0L, 0L, 1);
    }
    
    public LocalDateTime plusSeconds(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, n, 0L, 1);
    }
    
    public LocalDateTime plusNanos(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, 0L, n, 1);
    }
    
    @Override
    public LocalDateTime minus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return this.with(this.date.minus((TemporalAmount)temporalAmount), this.time);
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (LocalDateTime)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public LocalDateTime minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public LocalDateTime minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public LocalDateTime minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    public LocalDateTime minusWeeks(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : this.plusWeeks(-n);
    }
    
    public LocalDateTime minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    public LocalDateTime minusHours(final long n) {
        return this.plusWithOverflow(this.date, n, 0L, 0L, 0L, -1);
    }
    
    public LocalDateTime minusMinutes(final long n) {
        return this.plusWithOverflow(this.date, 0L, n, 0L, 0L, -1);
    }
    
    public LocalDateTime minusSeconds(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, n, 0L, -1);
    }
    
    public LocalDateTime minusNanos(final long n) {
        return this.plusWithOverflow(this.date, 0L, 0L, 0L, n, -1);
    }
    
    private LocalDateTime plusWithOverflow(final LocalDate localDate, final long n, final long n2, final long n3, final long n4, final int n5) {
        if ((n | n2 | n3 | n4) == 0x0L) {
            return this.with(localDate, this.time);
        }
        final long n6 = (n4 / 86400000000000L + n3 / 86400L + n2 / 1440L + n / 24L) * n5;
        final long n7 = n4 % 86400000000000L + n3 % 86400L * 1000000000L + n2 % 1440L * 60000000000L + n % 24L * 3600000000000L;
        final long nanoOfDay = this.time.toNanoOfDay();
        final long n8 = n7 * n5 + nanoOfDay;
        final long n9 = n6 + Math.floorDiv(n8, 86400000000000L);
        final long floorMod = Math.floorMod(n8, 86400000000000L);
        return this.with(localDate.plusDays(n9), (floorMod == nanoOfDay) ? this.time : LocalTime.ofNanoOfDay(floorMod));
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R)this.date;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return super.adjustInto(temporal);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final LocalDateTime from = from((TemporalAccessor)temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        if (!temporalUnit.isTimeBased()) {
            LocalDate localDate = from.date;
            if (localDate.isAfter(this.date) && from.time.isBefore(this.time)) {
                localDate = localDate.minusDays(1L);
            }
            else if (localDate.isBefore(this.date) && from.time.isAfter(this.time)) {
                localDate = localDate.plusDays(1L);
            }
            return this.date.until(localDate, temporalUnit);
        }
        final long daysUntil = this.date.daysUntil(from.date);
        if (daysUntil == 0L) {
            return this.time.until(from.time, temporalUnit);
        }
        final long n = from.time.toNanoOfDay() - this.time.toNanoOfDay();
        long n2;
        long n3;
        if (daysUntil > 0L) {
            n2 = daysUntil - 1L;
            n3 = n + 86400000000000L;
        }
        else {
            n2 = daysUntil + 1L;
            n3 = n - 86400000000000L;
        }
        switch ((ChronoUnit)temporalUnit) {
            case NANOS: {
                n2 = Math.multiplyExact(n2, 86400000000000L);
                break;
            }
            case MICROS: {
                n2 = Math.multiplyExact(n2, 86400000000L);
                n3 /= 1000L;
                break;
            }
            case MILLIS: {
                n2 = Math.multiplyExact(n2, 86400000L);
                n3 /= 1000000L;
                break;
            }
            case SECONDS: {
                n2 = Math.multiplyExact(n2, 86400L);
                n3 /= 1000000000L;
                break;
            }
            case MINUTES: {
                n2 = Math.multiplyExact(n2, 1440L);
                n3 /= 60000000000L;
                break;
            }
            case HOURS: {
                n2 = Math.multiplyExact(n2, 24L);
                n3 /= 3600000000000L;
                break;
            }
            case HALF_DAYS: {
                n2 = Math.multiplyExact(n2, 2L);
                n3 /= 43200000000000L;
                break;
            }
        }
        return Math.addExact(n2, n3);
    }
    
    @Override
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public OffsetDateTime atOffset(final ZoneOffset zoneOffset) {
        return OffsetDateTime.of(this, zoneOffset);
    }
    
    @Override
    public ZonedDateTime atZone(final ZoneId zoneId) {
        return ZonedDateTime.of(this, zoneId);
    }
    
    @Override
    public int compareTo(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return this.compareTo0((LocalDateTime)chronoLocalDateTime);
        }
        return super.compareTo(chronoLocalDateTime);
    }
    
    private int compareTo0(final LocalDateTime localDateTime) {
        int n = this.date.compareTo0(localDateTime.toLocalDate());
        if (n == 0) {
            n = this.time.compareTo(localDateTime.toLocalTime());
        }
        return n;
    }
    
    @Override
    public boolean isAfter(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return this.compareTo0((LocalDateTime)chronoLocalDateTime) > 0;
        }
        return super.isAfter(chronoLocalDateTime);
    }
    
    @Override
    public boolean isBefore(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return this.compareTo0((LocalDateTime)chronoLocalDateTime) < 0;
        }
        return super.isBefore(chronoLocalDateTime);
    }
    
    @Override
    public boolean isEqual(final ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return this.compareTo0((LocalDateTime)chronoLocalDateTime) == 0;
        }
        return super.isEqual(chronoLocalDateTime);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LocalDateTime) {
            final LocalDateTime localDateTime = (LocalDateTime)o;
            return this.date.equals(localDateTime.date) && this.time.equals(localDateTime.time);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.date.hashCode() ^ this.time.hashCode();
    }
    
    @Override
    public String toString() {
        return this.date.toString() + 'T' + this.time.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)5, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        this.date.writeExternal(dataOutput);
        this.time.writeExternal(dataOutput);
    }
    
    static LocalDateTime readExternal(final DataInput dataInput) throws IOException {
        return of(LocalDate.readExternal(dataInput), LocalTime.readExternal(dataInput));
    }
    
    static {
        MIN = of(LocalDate.MIN, LocalTime.MIN);
        MAX = of(LocalDate.MAX, LocalTime.MAX);
    }
}
