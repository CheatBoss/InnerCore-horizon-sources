package java.time;

import java.util.*;
import java.time.format.*;
import java.time.temporal.*;
import java.time.zone.*;
import java.io.*;
import java.time.chrono.*;

public final class LocalDate implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable
{
    public static final LocalDate MIN;
    public static final LocalDate MAX;
    private static final long serialVersionUID = 2942565459149668126L;
    private static final int DAYS_PER_CYCLE = 146097;
    static final long DAYS_0000_TO_1970 = 719528L;
    private final int year;
    private final short month;
    private final short day;
    
    public static LocalDate now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static LocalDate now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static LocalDate now(final Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant instant = clock.instant();
        return ofEpochDay(Math.floorDiv(instant.getEpochSecond() + clock.getZone().getRules().getOffset(instant).getTotalSeconds(), 86400L));
    }
    
    public static LocalDate of(final int n, final Month month, final int n2) {
        ChronoField.YEAR.checkValidValue(n);
        Objects.requireNonNull(month, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue(n2);
        return create(n, month.getValue(), n2);
    }
    
    public static LocalDate of(final int n, final int n2, final int n3) {
        ChronoField.YEAR.checkValidValue(n);
        ChronoField.MONTH_OF_YEAR.checkValidValue(n2);
        ChronoField.DAY_OF_MONTH.checkValidValue(n3);
        return create(n, n2, n3);
    }
    
    public static LocalDate ofYearDay(final int n, final int n2) {
        ChronoField.YEAR.checkValidValue(n);
        ChronoField.DAY_OF_YEAR.checkValidValue(n2);
        final boolean leapYear = IsoChronology.INSTANCE.isLeapYear(n);
        if (n2 == 366 && !leapYear) {
            throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + n + "' is not a leap year");
        }
        Month month = Month.of((n2 - 1) / 31 + 1);
        if (n2 > month.firstDayOfYear(leapYear) + month.length(leapYear) - 1) {
            month = month.plus(1L);
        }
        return new LocalDate(n, month.getValue(), n2 - month.firstDayOfYear(leapYear) + 1);
    }
    
    public static LocalDate ofEpochDay(final long n) {
        long n2 = n + 719528L - 60L;
        long n3 = 0L;
        if (n2 < 0L) {
            final long n4 = (n2 + 1L) / 146097L - 1L;
            n3 = n4 * 400L;
            n2 += -n4 * 146097L;
        }
        long n5 = (400L * n2 + 591L) / 146097L;
        long n6 = n2 - (365L * n5 + n5 / 4L - n5 / 100L + n5 / 400L);
        if (n6 < 0L) {
            --n5;
            n6 = n2 - (365L * n5 + n5 / 4L - n5 / 100L + n5 / 400L);
        }
        final long n7 = n5 + n3;
        final int n8 = (int)n6;
        final int n9 = (n8 * 5 + 2) / 153;
        return new LocalDate(ChronoField.YEAR.checkValidIntValue(n7 + n9 / 10), (n9 + 2) % 12 + 1, n8 - (n9 * 306 + 5) / 10 + 1);
    }
    
    public static LocalDate from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
        if (localDate == null) {
            throw new DateTimeException("Unable to obtain LocalDate from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
        }
        return localDate;
    }
    
    public static LocalDate parse(final CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    public static LocalDate parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, LocalDate::from);
    }
    
    private static LocalDate create(final int n, final int n2, final int n3) {
        if (n3 > 28) {
            int n4 = 31;
            switch (n2) {
                case 2: {
                    n4 = (IsoChronology.INSTANCE.isLeapYear(n) ? 29 : 28);
                    break;
                }
                case 4:
                case 6:
                case 9:
                case 11: {
                    n4 = 30;
                    break;
                }
            }
            if (n3 > n4) {
                if (n3 == 29) {
                    throw new DateTimeException("Invalid date 'February 29' as '" + n + "' is not a leap year");
                }
                throw new DateTimeException("Invalid date '" + Month.of(n2).name() + " " + n3 + "'");
            }
        }
        return new LocalDate(n, n2, n3);
    }
    
    private static LocalDate resolvePreviousValid(final int n, final int n2, int n3) {
        switch (n2) {
            case 2: {
                n3 = Math.min(n3, IsoChronology.INSTANCE.isLeapYear(n) ? 29 : 28);
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                n3 = Math.min(n3, 30);
                break;
            }
        }
        return new LocalDate(n, n2, n3);
    }
    
    private LocalDate(final int year, final int n, final int n2) {
        this.year = year;
        this.month = (short)n;
        this.day = (short)n2;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        return super.isSupported(temporalField);
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
        final ChronoField chronoField = (ChronoField)temporalField;
        if (!chronoField.isDateBased()) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        switch (chronoField) {
            case DAY_OF_MONTH: {
                return ValueRange.of(1L, this.lengthOfMonth());
            }
            case DAY_OF_YEAR: {
                return ValueRange.of(1L, this.lengthOfYear());
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return ValueRange.of(1L, (this.getMonth() == Month.FEBRUARY && !this.isLeapYear()) ? 4L : 5L);
            }
            case YEAR_OF_ERA: {
                return (this.getYear() <= 0) ? ValueRange.of(1L, 1000000000L) : ValueRange.of(1L, 999999999L);
            }
            default: {
                return temporalField.range();
            }
        }
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return this.get0(temporalField);
        }
        return super.get(temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.EPOCH_DAY) {
            return this.toEpochDay();
        }
        if (temporalField == ChronoField.PROLEPTIC_MONTH) {
            return this.getProlepticMonth();
        }
        return this.get0(temporalField);
    }
    
    private int get0(final TemporalField temporalField) {
        switch ((ChronoField)temporalField) {
            case DAY_OF_WEEK: {
                return this.getDayOfWeek().getValue();
            }
            case ALIGNED_DAY_OF_WEEK_IN_MONTH: {
                return (this.day - 1) % 7 + 1;
            }
            case ALIGNED_DAY_OF_WEEK_IN_YEAR: {
                return (this.getDayOfYear() - 1) % 7 + 1;
            }
            case DAY_OF_MONTH: {
                return this.day;
            }
            case DAY_OF_YEAR: {
                return this.getDayOfYear();
            }
            case EPOCH_DAY: {
                throw new UnsupportedTemporalTypeException("Invalid field 'EpochDay' for get() method, use getLong() instead");
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return (this.day - 1) / 7 + 1;
            }
            case ALIGNED_WEEK_OF_YEAR: {
                return (this.getDayOfYear() - 1) / 7 + 1;
            }
            case MONTH_OF_YEAR: {
                return this.month;
            }
            case PROLEPTIC_MONTH: {
                throw new UnsupportedTemporalTypeException("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            }
            case YEAR_OF_ERA: {
                return (this.year >= 1) ? this.year : (1 - this.year);
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
    
    @Override
    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }
    
    @Override
    public Era getEra() {
        return super.getEra();
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
    
    public int getDayOfMonth() {
        return this.day;
    }
    
    public int getDayOfYear() {
        return this.getMonth().firstDayOfYear(this.isLeapYear()) + this.day - 1;
    }
    
    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of((int)Math.floorMod(this.toEpochDay() + 3L, 7L) + 1);
    }
    
    @Override
    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear(this.year);
    }
    
    @Override
    public int lengthOfMonth() {
        switch (this.month) {
            case 2: {
                return this.isLeapYear() ? 29 : 28;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                return 30;
            }
            default: {
                return 31;
            }
        }
    }
    
    @Override
    public int lengthOfYear() {
        return this.isLeapYear() ? 366 : 365;
    }
    
    @Override
    public LocalDate with(final TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return (LocalDate)temporalAdjuster;
        }
        return (LocalDate)temporalAdjuster.adjustInto(this);
    }
    
    @Override
    public LocalDate with(final TemporalField temporalField, final long n) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.adjustInto(this, n);
        }
        final ChronoField chronoField = (ChronoField)temporalField;
        chronoField.checkValidValue(n);
        switch (chronoField) {
            case DAY_OF_WEEK: {
                return this.plusDays(n - this.getDayOfWeek().getValue());
            }
            case ALIGNED_DAY_OF_WEEK_IN_MONTH: {
                return this.plusDays(n - this.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            }
            case ALIGNED_DAY_OF_WEEK_IN_YEAR: {
                return this.plusDays(n - this.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            }
            case DAY_OF_MONTH: {
                return this.withDayOfMonth((int)n);
            }
            case DAY_OF_YEAR: {
                return this.withDayOfYear((int)n);
            }
            case EPOCH_DAY: {
                return ofEpochDay(n);
            }
            case ALIGNED_WEEK_OF_MONTH: {
                return this.plusWeeks(n - this.getLong(ChronoField.ALIGNED_WEEK_OF_MONTH));
            }
            case ALIGNED_WEEK_OF_YEAR: {
                return this.plusWeeks(n - this.getLong(ChronoField.ALIGNED_WEEK_OF_YEAR));
            }
            case MONTH_OF_YEAR: {
                return this.withMonth((int)n);
            }
            case PROLEPTIC_MONTH: {
                return this.plusMonths(n - this.getProlepticMonth());
            }
            case YEAR_OF_ERA: {
                return this.withYear((int)((this.year >= 1) ? n : (1L - n)));
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
    
    public LocalDate withYear(final int n) {
        if (this.year == n) {
            return this;
        }
        ChronoField.YEAR.checkValidValue(n);
        return resolvePreviousValid(n, this.month, this.day);
    }
    
    public LocalDate withMonth(final int n) {
        if (this.month == n) {
            return this;
        }
        ChronoField.MONTH_OF_YEAR.checkValidValue(n);
        return resolvePreviousValid(this.year, n, this.day);
    }
    
    public LocalDate withDayOfMonth(final int n) {
        if (this.day == n) {
            return this;
        }
        return of(this.year, this.month, n);
    }
    
    public LocalDate withDayOfYear(final int n) {
        if (this.getDayOfYear() == n) {
            return this;
        }
        return ofYearDay(this.year, n);
    }
    
    @Override
    public LocalDate plus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            final Period period = (Period)temporalAmount;
            return this.plusMonths(period.toTotalMonths()).plusDays(period.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (LocalDate)temporalAmount.addTo(this);
    }
    
    @Override
    public LocalDate plus(final long n, final TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.addTo(this, n);
        }
        switch ((ChronoUnit)temporalUnit) {
            case DAYS: {
                return this.plusDays(n);
            }
            case WEEKS: {
                return this.plusWeeks(n);
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
    
    public LocalDate plusYears(final long n) {
        if (n == 0L) {
            return this;
        }
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(this.year + n), this.month, this.day);
    }
    
    public LocalDate plusMonths(final long n) {
        if (n == 0L) {
            return this;
        }
        final long n2 = this.year * 12L + (this.month - 1) + n;
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(Math.floorDiv(n2, 12L)), (int)Math.floorMod(n2, 12L) + 1, this.day);
    }
    
    public LocalDate plusWeeks(final long n) {
        return this.plusDays(Math.multiplyExact(n, 7L));
    }
    
    public LocalDate plusDays(final long n) {
        if (n == 0L) {
            return this;
        }
        return ofEpochDay(Math.addExact(this.toEpochDay(), n));
    }
    
    @Override
    public LocalDate minus(final TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            final Period period = (Period)temporalAmount;
            return this.minusMonths(period.toTotalMonths()).minusDays(period.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (LocalDate)temporalAmount.subtractFrom(this);
    }
    
    @Override
    public LocalDate minus(final long n, final TemporalUnit temporalUnit) {
        return (n == Long.MIN_VALUE) ? this.plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : this.plus(-n, temporalUnit);
    }
    
    public LocalDate minusYears(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusYears(Long.MAX_VALUE).plusYears(1L) : this.plusYears(-n);
    }
    
    public LocalDate minusMonths(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusMonths(Long.MAX_VALUE).plusMonths(1L) : this.plusMonths(-n);
    }
    
    public LocalDate minusWeeks(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : this.plusWeeks(-n);
    }
    
    public LocalDate minusDays(final long n) {
        return (n == Long.MIN_VALUE) ? this.plusDays(Long.MAX_VALUE).plusDays(1L) : this.plusDays(-n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R)this;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return super.adjustInto(temporal);
    }
    
    @Override
    public long until(final Temporal temporal, final TemporalUnit temporalUnit) {
        final LocalDate from = from((TemporalAccessor)temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, from);
        }
        switch ((ChronoUnit)temporalUnit) {
            case DAYS: {
                return this.daysUntil(from);
            }
            case WEEKS: {
                return this.daysUntil(from) / 7L;
            }
            case MONTHS: {
                return this.monthsUntil(from);
            }
            case YEARS: {
                return this.monthsUntil(from) / 12L;
            }
            case DECADES: {
                return this.monthsUntil(from) / 120L;
            }
            case CENTURIES: {
                return this.monthsUntil(from) / 1200L;
            }
            case MILLENNIA: {
                return this.monthsUntil(from) / 12000L;
            }
            case ERAS: {
                return from.getLong(ChronoField.ERA) - this.getLong(ChronoField.ERA);
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported unit: " + temporalUnit);
            }
        }
    }
    
    long daysUntil(final LocalDate localDate) {
        return localDate.toEpochDay() - this.toEpochDay();
    }
    
    private long monthsUntil(final LocalDate localDate) {
        return (localDate.getProlepticMonth() * 32L + localDate.getDayOfMonth() - (this.getProlepticMonth() * 32L + this.getDayOfMonth())) / 32L;
    }
    
    @Override
    public Period until(final ChronoLocalDate chronoLocalDate) {
        final LocalDate from = from((TemporalAccessor)chronoLocalDate);
        long n = from.getProlepticMonth() - this.getProlepticMonth();
        int n2 = from.day - this.day;
        if (n > 0L && n2 < 0) {
            --n;
            n2 = (int)(from.toEpochDay() - this.plusMonths(n).toEpochDay());
        }
        else if (n < 0L && n2 > 0) {
            ++n;
            n2 -= from.lengthOfMonth();
        }
        return Period.of(Math.toIntExact(n / 12L), (int)(n % 12L), n2);
    }
    
    @Override
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    @Override
    public LocalDateTime atTime(final LocalTime localTime) {
        return LocalDateTime.of(this, localTime);
    }
    
    public LocalDateTime atTime(final int n, final int n2) {
        return this.atTime(LocalTime.of(n, n2));
    }
    
    public LocalDateTime atTime(final int n, final int n2, final int n3) {
        return this.atTime(LocalTime.of(n, n2, n3));
    }
    
    public LocalDateTime atTime(final int n, final int n2, final int n3, final int n4) {
        return this.atTime(LocalTime.of(n, n2, n3, n4));
    }
    
    public OffsetDateTime atTime(final OffsetTime offsetTime) {
        return OffsetDateTime.of(LocalDateTime.of(this, offsetTime.toLocalTime()), offsetTime.getOffset());
    }
    
    public LocalDateTime atStartOfDay() {
        return LocalDateTime.of(this, LocalTime.MIDNIGHT);
    }
    
    public ZonedDateTime atStartOfDay(final ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        LocalDateTime localDateTime = this.atTime(LocalTime.MIDNIGHT);
        if (!(zoneId instanceof ZoneOffset)) {
            final ZoneOffsetTransition transition = zoneId.getRules().getTransition(localDateTime);
            if (transition != null && transition.isGap()) {
                localDateTime = transition.getDateTimeAfter();
            }
        }
        return ZonedDateTime.of(localDateTime, zoneId);
    }
    
    @Override
    public long toEpochDay() {
        final long n = this.year;
        final long n2 = this.month;
        final long n3 = 0L + 365L * n;
        long n4;
        if (n >= 0L) {
            n4 = n3 + ((n + 3L) / 4L - (n + 99L) / 100L + (n + 399L) / 400L);
        }
        else {
            n4 = n3 - (n / -4L - n / -100L + n / -400L);
        }
        long n5 = n4 + (367L * n2 - 362L) / 12L + (this.day - 1);
        if (n2 > 2L) {
            --n5;
            if (!this.isLeapYear()) {
                --n5;
            }
        }
        return n5 - 719528L;
    }
    
    @Override
    public int compareTo(final ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return this.compareTo0((LocalDate)chronoLocalDate);
        }
        return super.compareTo(chronoLocalDate);
    }
    
    int compareTo0(final LocalDate localDate) {
        int n = this.year - localDate.year;
        if (n == 0) {
            n = this.month - localDate.month;
            if (n == 0) {
                n = this.day - localDate.day;
            }
        }
        return n;
    }
    
    @Override
    public boolean isAfter(final ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return this.compareTo0((LocalDate)chronoLocalDate) > 0;
        }
        return super.isAfter(chronoLocalDate);
    }
    
    @Override
    public boolean isBefore(final ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return this.compareTo0((LocalDate)chronoLocalDate) < 0;
        }
        return super.isBefore(chronoLocalDate);
    }
    
    @Override
    public boolean isEqual(final ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return this.compareTo0((LocalDate)chronoLocalDate) == 0;
        }
        return super.isEqual(chronoLocalDate);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof LocalDate && this.compareTo0((LocalDate)o) == 0);
    }
    
    @Override
    public int hashCode() {
        final int year = this.year;
        return (year & 0xFFFFF800) ^ (year << 11) + (this.month << 6) + this.day;
    }
    
    @Override
    public String toString() {
        final int year = this.year;
        final short month = this.month;
        final short day = this.day;
        final int abs = Math.abs(year);
        final StringBuilder sb = new StringBuilder(10);
        if (abs < 1000) {
            if (year < 0) {
                sb.append(year - 10000).deleteCharAt(1);
            }
            else {
                sb.append(year + 10000).deleteCharAt(0);
            }
        }
        else {
            if (year > 9999) {
                sb.append('+');
            }
            sb.append(year);
        }
        return sb.append((month < 10) ? "-0" : "-").append(month).append((day < 10) ? "-0" : "-").append(day).toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)3, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }
    
    static LocalDate readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
    
    static {
        MIN = of(-999999999, 1, 1);
        MAX = of(999999999, 12, 31);
    }
}
