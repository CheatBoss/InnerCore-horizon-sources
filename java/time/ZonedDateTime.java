package java.time;

import java.util.*;
import java.time.zone.*;
import java.time.format.*;
import java.time.temporal.*;
import java.io.*;
import java.time.chrono.*;

public final class ZonedDateTime implements Temporal, ChronoZonedDateTime<LocalDate>, Serializable
{
    private static final long serialVersionUID = -6260982410461394882L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;
    private final ZoneId zone;
    
    public static ZonedDateTime now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static ZonedDateTime now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static ZonedDateTime now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return ofInstant(clock.instant(), clock.getZone());
    }
    
    public static ZonedDateTime of(final LocalDate localDate, final LocalTime localTime, final ZoneId zoneId) {
        return of(LocalDateTime.of(localDate, localTime), zoneId);
    }
    
    public static ZonedDateTime of(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return ofLocal(localDateTime, zoneId, null);
    }
    
    public static ZonedDateTime of(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final ZoneId zoneId) {
        return ofLocal(LocalDateTime.of(n, n2, n3, n4, n5, n6, n7), zoneId, null);
    }
    
    public static ZonedDateTime ofLocal(LocalDateTime plusSeconds, final ZoneId zoneId, final ZoneOffset zoneOffset) {
        Objects.requireNonNull(plusSeconds, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ZonedDateTime(plusSeconds, (ZoneOffset)zoneId, zoneId);
        }
        final ZoneRules rules = zoneId.getRules();
        final List<ZoneOffset> validOffsets = rules.getValidOffsets(plusSeconds);
        ZoneOffset offsetAfter;
        if (validOffsets.size() == 1) {
            offsetAfter = validOffsets.get(0);
        }
        else if (validOffsets.size() == 0) {
            final ZoneOffsetTransition transition = rules.getTransition(plusSeconds);
            plusSeconds = plusSeconds.plusSeconds(transition.getDuration().getSeconds());
            offsetAfter = transition.getOffsetAfter();
        }
        else if (zoneOffset != null && validOffsets.contains(zoneOffset)) {
            offsetAfter = zoneOffset;
        }
        else {
            offsetAfter = Objects.requireNonNull(validOffsets.get(0), "offset");
        }
        return new ZonedDateTime(plusSeconds, offsetAfter, zoneId);
    }
    
    public static ZonedDateTime ofInstant(final Instant instant, final ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return create(instant.getEpochSecond(), instant.getNano(), zoneId);
    }
    
    public static ZonedDateTime ofInstant(final LocalDateTime localDateTime, final ZoneOffset zoneOffset, final ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, "offset");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId.getRules().isValidOffset(localDateTime, zoneOffset)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        return create(localDateTime.toEpochSecond(zoneOffset), localDateTime.getNano(), zoneId);
    }
    
    private static ZonedDateTime create(final long n, final int n2, final ZoneId zoneId) {
        final ZoneOffset offset = zoneId.getRules().getOffset(Instant.ofEpochSecond(n, n2));
        return new ZonedDateTime(LocalDateTime.ofEpochSecond(n, n2, offset), offset, zoneId);
    }
    
    public static ZonedDateTime ofStrict(final LocalDateTime localDateTime, final ZoneOffset zoneOffset, final ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, "offset");
        Objects.requireNonNull(zoneId, "zone");
        final ZoneRules rules = zoneId.getRules();
        if (rules.isValidOffset(localDateTime, zoneOffset)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        final ZoneOffsetTransition transition = rules.getTransition(localDateTime);
        if (transition != null && transition.isGap()) {
            throw new DateTimeException("LocalDateTime '" + localDateTime + "' does not exist in zone '" + zoneId + "' due to a gap in the local time-line, typically caused by daylight savings");
        }
        throw new DateTimeException("ZoneOffset '" + zoneOffset + "' is not valid for LocalDateTime '" + localDateTime + "' in zone '" + zoneId + "'");
    }
    
    private static ZonedDateTime ofLenient(final LocalDateTime localDateTime, final ZoneOffset zoneOffset, final ZoneId zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneOffset, "offset");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset && !zoneOffset.equals(zoneId)) {
            throw new IllegalArgumentException("ZoneId must match ZoneOffset");
        }
        return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
    }
    
    public static ZonedDateTime from(final TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ZonedDateTime) {
            return (ZonedDateTime)temporalAccessor;
        }
        try {
            final ZoneId from = ZoneId.from(temporalAccessor);
            if (temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                return create(temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), temporalAccessor.get(ChronoField.NANO_OF_SECOND), from);
            }
            return of(LocalDate.from(temporalAccessor), LocalTime.from(temporalAccessor), from);
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain ZonedDateTime from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName(), ex);
        }
    }
    
    public static ZonedDateTime parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
    
    public static ZonedDateTime parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, ZonedDateTime::from);
    }
    
    private ZonedDateTime(final LocalDateTime dateTime, final ZoneOffset offset, final ZoneId zone) {
        this.dateTime = dateTime;
        this.offset = offset;
        this.zone = zone;
    }
    
    private ZonedDateTime resolveLocal(final LocalDateTime localDateTime) {
        return ofLocal(localDateTime, this.zone, this.offset);
    }
    
    private ZonedDateTime resolveInstant(final LocalDateTime localDateTime) {
        return ofInstant(localDateTime, this.offset, this.zone);
    }
    
    private ZonedDateTime resolveOffset(final ZoneOffset zoneOffset) {
        if (!zoneOffset.equals(this.offset) && this.zone.getRules().isValidOffset(this.dateTime, zoneOffset)) {
            return new ZonedDateTime(this.dateTime, zoneOffset, this.zone);
        }
        return this;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return temporalField instanceof ChronoField || (temporalField != null && temporalField.isSupportedBy(this));
    }
    
    @Override
    public boolean isSupported(final TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
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
    
    @Override
    public ZoneOffset getOffset() {
        return this.offset;
    }
    
    @Override
    public ZonedDateTime withEarlierOffsetAtOverlap() {
        final ZoneOffsetTransition transition = this.getZone().getRules().getTransition(this.dateTime);
        if (transition != null && transition.isOverlap()) {
            final ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }
    
    @Override
    public ZonedDateTime withLaterOffsetAtOverlap() {
        final ZoneOffsetTransition transition = this.getZone().getRules().getTransition(this.toLocalDateTime());
        if (transition != null) {
            final ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }
    
    @Override
    public ZoneId getZone() {
        return this.zone;
    }
    
    @Override
    public ZonedDateTime withZoneSameLocal(final ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : ofLocal(this.dateTime, zoneId, this.offset);
    }
    
    @Override
    public ZonedDateTime withZoneSameInstant(final ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toEpochSecond(this.offset), this.dateTime.getNano(), zoneId);
    }
    
    public ZonedDateTime withFixedOffsetZone() {
        return this.zone.equals(this.offset) ? this : new ZonedDateTime(this.dateTime, this.offset, this.offset);
    }
    
    @Override
    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }
    
    @Override
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
    
    @Override
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
    public ZonedDateTime with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return this.resolveLocal(LocalDateTime.of((LocalDate)temporalAdjuster, this.dateTime.toLocalTime()));
        }
        if (temporalAdjuster instanceof LocalTime) {
            return this.resolveLocal(LocalDateTime.of(this.dateTime.toLocalDate(), (LocalTime)temporalAdjuster));
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return this.resolveLocal((LocalDateTime)temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetDateTime) {
            final OffsetDateTime offsetDateTime = (OffsetDateTime)temporalAdjuster;
            return ofLocal(offsetDateTime.toLocalDateTime(), this.zone, offsetDateTime.getOffset());
        }
        if (temporalAdjuster instanceof Instant) {
            final Instant instant = (Instant)temporalAdjuster;
            return create(instant.getEpochSecond(), instant.getNano(), this.zone);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return this.resolveOffset((ZoneOffset)temporalAdjuster);
        }
        return (ZonedDateTime)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public ZonedDateTime with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS: {
                return create(n, this.getNano(), this.zone);
            }
            case OFFSET_SECONDS: {
                return this.resolveOffset(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(n)));
            }
            default: {
                return this.resolveLocal(this.dateTime.with(temporalField, n));
            }
        }
    }
    
    public ZonedDateTime withYear(final int n) {
        return this.resolveLocal(this.dateTime.withYear(n));
    }
    
    public ZonedDateTime withMonth(final int n) {
        return this.resolveLocal(this.dateTime.withMonth(n));
    }
    
    public ZonedDateTime withDayOfMonth(final int n) {
        return this.resolveLocal(this.dateTime.withDayOfMonth(n));
    }
    
    public ZonedDateTime withDayOfYear(final int n) {
        return this.resolveLocal(this.dateTime.withDayOfYear(n));
    }
    
    public ZonedDateTime withHour(final int n) {
        return this.resolveLocal(this.dateTime.withHour(n));
    }
    
    public ZonedDateTime withMinute(final int n) {
        return this.resolveLocal(this.dateTime.withMinute(n));
    }
    
    public ZonedDateTime withSecond(final int n) {
        return this.resolveLocal(this.dateTime.withSecond(n));
    }
    
    public ZonedDateTime withNano(final int n) {
        return this.resolveLocal(this.dateTime.withNano(n));
    }
    
    public ZonedDateTime truncatedTo(final TemporalUnit temporalUnit) {
        return this.resolveLocal(this.dateTime.truncatedTo(temporalUnit));
    }
    
    @Override
    public ZonedDateTime plus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return this.resolveLocal(this.dateTime.plus((TemporalAmount)temporalAmount));
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (ZonedDateTime)temporalAmount.addTo(this);
    }
    
    @Override
    public ZonedDateTime plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        if (temporalUnit.isDateBased()) {
            return this.resolveLocal(this.dateTime.plus(n, temporalUnit));
        }
        return this.resolveInstant(this.dateTime.plus(n, temporalUnit));
    }
    
    public ZonedDateTime plusYears(final long n) {
        return this.resolveLocal(this.dateTime.plusYears(n));
    }
    
    public ZonedDateTime plusMonths(final long n) {
        return this.resolveLocal(this.dateTime.plusMonths(n));
    }
    
    public ZonedDateTime plusWeeks(final long n) {
        return this.resolveLocal(this.dateTime.plusWeeks(n));
    }
    
    public ZonedDateTime plusDays(final long n) {
        return this.resolveLocal(this.dateTime.plusDays(n));
    }
    
    public ZonedDateTime plusHours(final long n) {
        return this.resolveInstant(this.dateTime.plusHours(n));
    }
    
    public ZonedDateTime plusMinutes(final long n) {
        return this.resolveInstant(this.dateTime.plusMinutes(n));
    }
    
    public ZonedDateTime plusSeconds(final long n) {
        return this.resolveInstant(this.dateTime.plusSeconds(n));
    }
    
    public ZonedDateTime plusNanos(final long n) {
        return this.resolveInstant(this.dateTime.plusNanos(n));
    }
    
    @Override
    public ZonedDateTime minus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return this.resolveLocal(this.dateTime.minus((TemporalAmount)temporalAmount));
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (ZonedDateTime)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public ZonedDateTime minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public ZonedDateTime minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public ZonedDateTime minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    public ZonedDateTime minusWeeks(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : this.plusWeeks(-n);
    }
    
    public ZonedDateTime minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    public ZonedDateTime minusHours(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusHours(Long.MAX_VALUE).plusHours(1L) : this.plusHours(-n);
    }
    
    public ZonedDateTime minusMinutes(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMinutes(Long.MAX_VALUE).plusMinutes(1L) : this.plusMinutes(-n);
    }
    
    public ZonedDateTime minusSeconds(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusSeconds(Long.MAX_VALUE).plusSeconds(1L) : this.plusSeconds(-n);
    }
    
    public ZonedDateTime minusNanos(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusNanos(Long.MAX_VALUE).plusNanos(1L) : this.plusNanos(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R)this.toLocalDate();
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final ZonedDateTime from = from((TemporalAccessor)temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        final ZonedDateTime withZoneSameInstant = from.withZoneSameInstant(this.zone);
        if (temporalUnit.isDateBased()) {
            return this.dateTime.until(withZoneSameInstant.dateTime, temporalUnit);
        }
        return this.toOffsetDateTime().until(withZoneSameInstant.toOffsetDateTime(), temporalUnit);
    }
    
    @Override
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public OffsetDateTime toOffsetDateTime() {
        return OffsetDateTime.of(this.dateTime, this.offset);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ZonedDateTime) {
            final ZonedDateTime zonedDateTime = (ZonedDateTime)o;
            return this.dateTime.equals(zonedDateTime.dateTime) && this.offset.equals(zonedDateTime.offset) && this.zone.equals(zonedDateTime.zone);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.dateTime.hashCode() ^ this.offset.hashCode() ^ Integer.rotateLeft(this.zone.hashCode(), 3);
    }
    
    @Override
    public String toString() {
        String s = this.dateTime.toString() + this.offset.toString();
        if (this.offset != this.zone) {
            s = s + '[' + this.zone.toString() + ']';
        }
        return s;
    }
    
    private Object writeReplace() {
        return new Ser((byte)6, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        this.dateTime.writeExternal(dataOutput);
        this.offset.writeExternal(dataOutput);
        this.zone.write(dataOutput);
    }
    
    static ZonedDateTime readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ofLenient(LocalDateTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput), (ZoneId)Ser.read(objectInput));
    }
}
