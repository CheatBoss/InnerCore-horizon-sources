package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.io.*;

public final class OffsetDateTime implements Temporal, TemporalAdjuster, Comparable<OffsetDateTime>, Serializable
{
    public static final OffsetDateTime MIN;
    public static final OffsetDateTime MAX;
    private static final long serialVersionUID = 2287754244819255394L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;
    
    public static Comparator<OffsetDateTime> timeLineOrder() {
        return OffsetDateTime::compareInstant;
    }
    
    private static int compareInstant(final OffsetDateTime offsetDateTime, final OffsetDateTime offsetDateTime2) {
        if (offsetDateTime.getOffset().equals(offsetDateTime2.getOffset())) {
            return offsetDateTime.toLocalDateTime().compareTo((ChronoLocalDateTime<?>)offsetDateTime2.toLocalDateTime());
        }
        int compare = Long.compare(offsetDateTime.toEpochSecond(), offsetDateTime2.toEpochSecond());
        if (compare == 0) {
            compare = offsetDateTime.toLocalTime().getNano() - offsetDateTime2.toLocalTime().getNano();
        }
        return compare;
    }
    
    public static OffsetDateTime now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static OffsetDateTime now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static OffsetDateTime now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }
    
    public static OffsetDateTime of(final LocalDate localDate, final LocalTime localTime, final ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(localDate, localTime), zoneOffset);
    }
    
    public static OffsetDateTime of(final LocalDateTime localDateTime, final ZoneOffset zoneOffset) {
        return new OffsetDateTime(localDateTime, zoneOffset);
    }
    
    public static OffsetDateTime of(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(n, n2, n3, n4, n5, n6, n7), zoneOffset);
    }
    
    public static OffsetDateTime ofInstant(final Instant instant, final ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        final ZoneOffset offset = zoneId.getRules().getOffset(instant);
        return new OffsetDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset), offset);
    }
    
    public static OffsetDateTime from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof OffsetDateTime) {
            return (OffsetDateTime)temporalAccessor;
        }
        try {
            final ZoneOffset from = ZoneOffset.from(temporalAccessor);
            final LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
            final LocalTime localTime = temporalAccessor.query(TemporalQueries.localTime());
            if (localDate != null && localTime != null) {
                return of(localDate, localTime, from);
            }
            return ofInstant(Instant.from(temporalAccessor), from);
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain OffsetDateTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), ex);
        }
    }
    
    public static OffsetDateTime parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    
    public static OffsetDateTime parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, OffsetDateTime::from);
    }
    
    private OffsetDateTime(final LocalDateTime localDateTime, final ZoneOffset zoneOffset) {
        this.dateTime = Objects.requireNonNull(localDateTime, "dateTime");
        this.offset = Objects.requireNonNull(zoneOffset, "offset");
    }
    
    private OffsetDateTime with(final LocalDateTime localDateTime, final ZoneOffset zoneOffset) {
        if (this.dateTime == localDateTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetDateTime(localDateTime, zoneOffset);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return temporalField instanceof ChronoField || (temporalField != null && temporalField.isSupportedBy(this));
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit != ChronoUnit.FOREVER;
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return this.dateTime.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        switch ((ChronoField)temporalField) {
            case INSTANT_SECONDS: {
                throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
            }
            case OFFSET_SECONDS: {
                return this.getOffset().getTotalSeconds();
            }
            default: {
                return this.dateTime.get(temporalField);
            }
        }
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField)temporalField) {
            case INSTANT_SECONDS: {
                return this.toEpochSecond();
            }
            case OFFSET_SECONDS: {
                return this.getOffset().getTotalSeconds();
            }
            default: {
                return this.dateTime.getLong(temporalField);
            }
        }
    }
    
    public ZoneOffset getOffset() {
        return this.offset;
    }
    
    public OffsetDateTime withOffsetSameLocal(final ZoneOffset zoneOffset) {
        return this.with(this.dateTime, zoneOffset);
    }
    
    public OffsetDateTime withOffsetSameInstant(final ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetDateTime(this.dateTime.plusSeconds(zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds()), zoneOffset);
    }
    
    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }
    
    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }
    
    public int getYear() {
        return this.dateTime.getYear();
    }
    
    public int getMonthValue() {
        return this.dateTime.getMonthValue();
    }
    
    public Month getMonth() {
        return this.dateTime.getMonth();
    }
    
    public int getDayOfMonth() {
        return this.dateTime.getDayOfMonth();
    }
    
    public int getDayOfYear() {
        return this.dateTime.getDayOfYear();
    }
    
    public DayOfWeek getDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }
    
    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }
    
    public int getHour() {
        return this.dateTime.getHour();
    }
    
    public int getMinute() {
        return this.dateTime.getMinute();
    }
    
    public int getSecond() {
        return this.dateTime.getSecond();
    }
    
    public int getNano() {
        return this.dateTime.getNano();
    }
    
    @Override
    public OffsetDateTime with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate || temporalAdjuster instanceof LocalTime || temporalAdjuster instanceof LocalDateTime) {
            return this.with(this.dateTime.with(temporalAdjuster), this.offset);
        }
        if (temporalAdjuster instanceof Instant) {
            return ofInstant((Instant)temporalAdjuster, this.offset);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return this.with(this.dateTime, (ZoneOffset)temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetDateTime) {
            return (OffsetDateTime)temporalAdjuster;
        }
        return (OffsetDateTime)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public OffsetDateTime with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS: {
                return ofInstant(Instant.ofEpochSecond(n, this.getNano()), this.offset);
            }
            case OFFSET_SECONDS: {
                return this.with(this.dateTime, ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(n)));
            }
            default: {
                return this.with(this.dateTime.with(temporalField, n), this.offset);
            }
        }
    }
    
    public OffsetDateTime withYear(final int n) {
        return this.with(this.dateTime.withYear(n), this.offset);
    }
    
    public OffsetDateTime withMonth(final int n) {
        return this.with(this.dateTime.withMonth(n), this.offset);
    }
    
    public OffsetDateTime withDayOfMonth(final int n) {
        return this.with(this.dateTime.withDayOfMonth(n), this.offset);
    }
    
    public OffsetDateTime withDayOfYear(final int n) {
        return this.with(this.dateTime.withDayOfYear(n), this.offset);
    }
    
    public OffsetDateTime withHour(final int n) {
        return this.with(this.dateTime.withHour(n), this.offset);
    }
    
    public OffsetDateTime withMinute(final int n) {
        return this.with(this.dateTime.withMinute(n), this.offset);
    }
    
    public OffsetDateTime withSecond(final int n) {
        return this.with(this.dateTime.withSecond(n), this.offset);
    }
    
    public OffsetDateTime withNano(final int n) {
        return this.with(this.dateTime.withNano(n), this.offset);
    }
    
    public OffsetDateTime truncatedTo(final TemporalUnit temporalUnit) {
        return this.with(this.dateTime.truncatedTo(temporalUnit), this.offset);
    }
    
    @Override
    public OffsetDateTime plus(final TemporalAmount temporalAmount) {
        return (OffsetDateTime)temporalAmount.addTo(this);
    }
    
    @Override
    public OffsetDateTime plus(final long n, final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return this.with(this.dateTime.plus(n, temporalUnit), this.offset);
        }
        return temporalUnit.addTo(this, n);
    }
    
    public OffsetDateTime plusYears(final long n) {
        return this.with(this.dateTime.plusYears(n), this.offset);
    }
    
    public OffsetDateTime plusMonths(final long n) {
        return this.with(this.dateTime.plusMonths(n), this.offset);
    }
    
    public OffsetDateTime plusWeeks(final long n) {
        return this.with(this.dateTime.plusWeeks(n), this.offset);
    }
    
    public OffsetDateTime plusDays(final long n) {
        return this.with(this.dateTime.plusDays(n), this.offset);
    }
    
    public OffsetDateTime plusHours(final long n) {
        return this.with(this.dateTime.plusHours(n), this.offset);
    }
    
    public OffsetDateTime plusMinutes(final long n) {
        return this.with(this.dateTime.plusMinutes(n), this.offset);
    }
    
    public OffsetDateTime plusSeconds(final long n) {
        return this.with(this.dateTime.plusSeconds(n), this.offset);
    }
    
    public OffsetDateTime plusNanos(final long n) {
        return this.with(this.dateTime.plusNanos(n), this.offset);
    }
    
    @Override
    public OffsetDateTime minus(final TemporalAmount temporalAmount) {
        return (OffsetDateTime)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public OffsetDateTime minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public OffsetDateTime minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public OffsetDateTime minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    public OffsetDateTime minusWeeks(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : this.plusWeeks(-n);
    }
    
    public OffsetDateTime minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    public OffsetDateTime minusHours(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusHours(Long.MAX_VALUE).plusHours(1L) : this.plusHours(-n);
    }
    
    public OffsetDateTime minusMinutes(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMinutes(Long.MAX_VALUE).plusMinutes(1L) : this.plusMinutes(-n);
    }
    
    public OffsetDateTime minusSeconds(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusSeconds(Long.MAX_VALUE).plusSeconds(1L) : this.plusSeconds(-n);
    }
    
    public OffsetDateTime minusNanos(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusNanos(Long.MAX_VALUE).plusNanos(1L) : this.plusNanos(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return (R)this.getOffset();
        }
        if (temporalQuery == TemporalQueries.zoneId()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R)this.toLocalDate();
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R)this.toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        return (R)temporalQuery.queryFrom(this);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, this.toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, this.toLocalTime().toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, this.getOffset().getTotalSeconds());
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final OffsetDateTime from = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return this.dateTime.until(from.withOffsetSameInstant(this.offset).dateTime, temporalUnit);
        }
        return temporalUnit.between(this, from);
    }
    
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public ZonedDateTime atZoneSameInstant(final ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this.dateTime, this.offset, zoneId);
    }
    
    public ZonedDateTime atZoneSimilarLocal(final ZoneId zoneId) {
        return ZonedDateTime.ofLocal(this.dateTime, zoneId, this.offset);
    }
    
    public OffsetTime toOffsetTime() {
        return OffsetTime.of(this.dateTime.toLocalTime(), this.offset);
    }
    
    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(this.dateTime, this.offset);
    }
    
    public Instant toInstant() {
        return this.dateTime.toInstant(this.offset);
    }
    
    public long toEpochSecond() {
        return this.dateTime.toEpochSecond(this.offset);
    }
    
    @Override
    public int compareTo(final OffsetDateTime offsetDateTime) {
        int n = compareInstant(this, offsetDateTime);
        if (n == 0) {
            n = this.toLocalDateTime().compareTo((ChronoLocalDateTime<?>)offsetDateTime.toLocalDateTime());
        }
        return n;
    }
    
    public boolean isAfter(final OffsetDateTime offsetDateTime) {
        final long epochSecond = this.toEpochSecond();
        final long epochSecond2 = offsetDateTime.toEpochSecond();
        return epochSecond > epochSecond2 || (epochSecond == epochSecond2 && this.toLocalTime().getNano() > offsetDateTime.toLocalTime().getNano());
    }
    
    public boolean isBefore(final OffsetDateTime offsetDateTime) {
        final long epochSecond = this.toEpochSecond();
        final long epochSecond2 = offsetDateTime.toEpochSecond();
        return epochSecond < epochSecond2 || (epochSecond == epochSecond2 && this.toLocalTime().getNano() < offsetDateTime.toLocalTime().getNano());
    }
    
    public boolean isEqual(final OffsetDateTime offsetDateTime) {
        return this.toEpochSecond() == offsetDateTime.toEpochSecond() && this.toLocalTime().getNano() == offsetDateTime.toLocalTime().getNano();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof OffsetDateTime) {
            final OffsetDateTime offsetDateTime = (OffsetDateTime)o;
            return this.dateTime.equals(offsetDateTime.dateTime) && this.offset.equals(offsetDateTime.offset);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.dateTime.hashCode() ^ this.offset.hashCode();
    }
    
    @Override
    public String toString() {
        return this.dateTime.toString() + this.offset.toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)10, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final ObjectOutput objectOutput) throws IOException {
        this.dateTime.writeExternal(objectOutput);
        this.offset.writeExternal(objectOutput);
    }
    
    static OffsetDateTime readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return of(LocalDateTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput));
    }
    
    static {
        MIN = LocalDateTime.MIN.atOffset(ZoneOffset.MAX);
        MAX = LocalDateTime.MAX.atOffset(ZoneOffset.MIN);
    }
}
