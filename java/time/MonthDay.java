package java.time;

import java.util.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.io.*;
import java.time.format.*;

public final class MonthDay implements TemporalAccessor, TemporalAdjuster, Comparable<MonthDay>, Serializable
{
    private static final long serialVersionUID = -939150713474957432L;
    private static final DateTimeFormatter PARSER;
    private final int month;
    private final int day;
    
    public static MonthDay now() {
        return now(Clock.systemDefaultZone());
    }
    
    public static MonthDay now(final ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }
    
    public static MonthDay now(final Clock clock) {
        final LocalDate now = LocalDate.now(clock);
        return of(now.getMonth(), now.getDayOfMonth());
    }
    
    public static MonthDay of(final Month month, final int n) {
        Objects.requireNonNull(month, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue(n);
        if (n > month.maxLength()) {
            throw new DateTimeException("Illegal value for DayOfMonth field, value " + n + " is not valid for month " + month.name());
        }
        return new MonthDay(month.getValue(), n);
    }
    
    public static MonthDay of(final int n, final int n2) {
        return of(Month.of(n), n2);
    }
    
    public static MonthDay from(TemporalAccessor from) {
        if (from instanceof MonthDay) {
            return (MonthDay)from;
        }
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(from))) {
                from = LocalDate.from(from);
            }
            return of(from.get(ChronoField.MONTH_OF_YEAR), from.get(ChronoField.DAY_OF_MONTH));
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain MonthDay from TemporalAccessor: " + from + " of type " + from.getClass().getName(), ex);
        }
    }
    
    public static MonthDay parse(final CharSequence charSequence) {
        return parse(charSequence, MonthDay.PARSER);
    }
    
    public static MonthDay parse(final CharSequence charSequence, final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.parse(charSequence, MonthDay::from);
    }
    
    private MonthDay(final int month, final int day) {
        this.month = month;
        this.day = day;
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.MONTH_OF_YEAR || temporalField == ChronoField.DAY_OF_MONTH;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return temporalField.range();
        }
        if (temporalField == ChronoField.DAY_OF_MONTH) {
            return ValueRange.of(1L, this.getMonth().minLength(), this.getMonth().maxLength());
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
            case DAY_OF_MONTH: {
                return this.day;
            }
            case MONTH_OF_YEAR: {
                return this.month;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
        }
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
    
    public boolean isValidYear(final int n) {
        return this.day != 29 || this.month != 2 || Year.isLeap(n);
    }
    
    public MonthDay withMonth(final int n) {
        return this.with(Month.of(n));
    }
    
    public MonthDay with(final Month month) {
        Objects.requireNonNull(month, "month");
        if (month.getValue() == this.month) {
            return this;
        }
        return new MonthDay(month.getValue(), Math.min(this.day, month.maxLength()));
    }
    
    public MonthDay withDayOfMonth(final int n) {
        if (n == this.day) {
            return this;
        }
        return of(this.month, n);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R)IsoChronology.INSTANCE;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(Temporal with) {
        if (!Chronology.from(with).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        with = with.with(ChronoField.MONTH_OF_YEAR, this.month);
        return with.with(ChronoField.DAY_OF_MONTH, Math.min(with.range(ChronoField.DAY_OF_MONTH).getMaximum(), this.day));
    }
    
    public String format(final DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }
    
    public LocalDate atYear(final int n) {
        return LocalDate.of(n, this.month, this.isValidYear(n) ? this.day : 28);
    }
    
    @Override
    public int compareTo(final MonthDay monthDay) {
        int n = this.month - monthDay.month;
        if (n == 0) {
            n = this.day - monthDay.day;
        }
        return n;
    }
    
    public boolean isAfter(final MonthDay monthDay) {
        return this.compareTo(monthDay) > 0;
    }
    
    public boolean isBefore(final MonthDay monthDay) {
        return this.compareTo(monthDay) < 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof MonthDay) {
            final MonthDay monthDay = (MonthDay)o;
            return this.month == monthDay.month && this.day == monthDay.day;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.month << 6) + this.day;
    }
    
    @Override
    public String toString() {
        return new StringBuilder(10).append("--").append((this.month < 10) ? "0" : "").append(this.month).append((this.day < 10) ? "-0" : "-").append(this.day).toString();
    }
    
    private Object writeReplace() {
        return new Ser((byte)13, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }
    
    static MonthDay readExternal(final DataInput dataInput) throws IOException {
        return of(dataInput.readByte(), dataInput.readByte());
    }
    
    static {
        PARSER = new DateTimeFormatterBuilder().appendLiteral("--").appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter();
    }
}
