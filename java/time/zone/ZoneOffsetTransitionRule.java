package java.time.zone;

import java.util.*;
import java.io.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.time.*;

public final class ZoneOffsetTransitionRule implements Serializable
{
    private static final long serialVersionUID = 6889046316657758795L;
    private final Month month;
    private final byte dom;
    private final DayOfWeek dow;
    private final LocalTime time;
    private final boolean timeEndOfDay;
    private final TimeDefinition timeDefinition;
    private final ZoneOffset standardOffset;
    private final ZoneOffset offsetBefore;
    private final ZoneOffset offsetAfter;
    
    public static ZoneOffsetTransitionRule of(final Month month, final int n, final DayOfWeek dayOfWeek, final LocalTime localTime, final boolean b, final TimeDefinition timeDefinition, final ZoneOffset zoneOffset, final ZoneOffset zoneOffset2, final ZoneOffset zoneOffset3) {
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(localTime, "time");
        Objects.requireNonNull(timeDefinition, "timeDefnition");
        Objects.requireNonNull(zoneOffset, "standardOffset");
        Objects.requireNonNull(zoneOffset2, "offsetBefore");
        Objects.requireNonNull(zoneOffset3, "offsetAfter");
        if (n < -28 || n > 31 || n == 0) {
            throw new IllegalArgumentException("Day of month indicator must be between -28 and 31 inclusive excluding zero");
        }
        if (b && !localTime.equals(LocalTime.MIDNIGHT)) {
            throw new IllegalArgumentException("Time must be midnight when end of day flag is true");
        }
        return new ZoneOffsetTransitionRule(month, n, dayOfWeek, localTime, b, timeDefinition, zoneOffset, zoneOffset2, zoneOffset3);
    }
    
    ZoneOffsetTransitionRule(final Month month, final int n, final DayOfWeek dow, final LocalTime time, final boolean timeEndOfDay, final TimeDefinition timeDefinition, final ZoneOffset standardOffset, final ZoneOffset offsetBefore, final ZoneOffset offsetAfter) {
        this.month = month;
        this.dom = (byte)n;
        this.dow = dow;
        this.time = time;
        this.timeEndOfDay = timeEndOfDay;
        this.timeDefinition = timeDefinition;
        this.standardOffset = standardOffset;
        this.offsetBefore = offsetBefore;
        this.offsetAfter = offsetAfter;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)3, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        final int n = this.timeEndOfDay ? 86400 : this.time.toSecondOfDay();
        final int totalSeconds = this.standardOffset.getTotalSeconds();
        final int n2 = this.offsetBefore.getTotalSeconds() - totalSeconds;
        final int n3 = this.offsetAfter.getTotalSeconds() - totalSeconds;
        final int n4 = (n % 3600 == 0) ? (this.timeEndOfDay ? 24 : this.time.getHour()) : 31;
        final int n5 = (totalSeconds % 900 == 0) ? (totalSeconds / 900 + 128) : 255;
        final int n6 = (n2 == 0 || n2 == 1800 || n2 == 3600) ? (n2 / 1800) : 3;
        final int n7 = (n3 == 0 || n3 == 1800 || n3 == 3600) ? (n3 / 1800) : 3;
        dataOutput.writeInt((this.month.getValue() << 28) + (this.dom + 32 << 22) + (((this.dow == null) ? 0 : this.dow.getValue()) << 19) + (n4 << 14) + (this.timeDefinition.ordinal() << 12) + (n5 << 4) + (n6 << 2) + n7);
        if (n4 == 31) {
            dataOutput.writeInt(n);
        }
        if (n5 == 255) {
            dataOutput.writeInt(totalSeconds);
        }
        if (n6 == 3) {
            dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
        }
        if (n7 == 3) {
            dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
        }
    }
    
    static ZoneOffsetTransitionRule readExternal(final DataInput dataInput) throws IOException {
        final int int1 = dataInput.readInt();
        final Month of = Month.of(int1 >>> 28);
        final int n = ((int1 & 0xFC00000) >>> 22) - 32;
        final int n2 = (int1 & 0x380000) >>> 19;
        final DayOfWeek dayOfWeek = (n2 == 0) ? null : DayOfWeek.of(n2);
        final int n3 = (int1 & 0x7C000) >>> 14;
        final TimeDefinition timeDefinition = TimeDefinition.values()[(int1 & 0x3000) >>> 12];
        final int n4 = (int1 & 0xFF0) >>> 4;
        final int n5 = (int1 & 0xC) >>> 2;
        final int n6 = int1 & 0x3;
        final LocalTime localTime = (n3 == 31) ? LocalTime.ofSecondOfDay(dataInput.readInt()) : LocalTime.of(n3 % 24, 0);
        final ZoneOffset zoneOffset = (n4 == 255) ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds((n4 - 128) * 900);
        return of(of, n, dayOfWeek, localTime, n3 == 24, timeDefinition, zoneOffset, (n5 == 3) ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(zoneOffset.getTotalSeconds() + n5 * 1800), (n6 == 3) ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(zoneOffset.getTotalSeconds() + n6 * 1800));
    }
    
    public Month getMonth() {
        return this.month;
    }
    
    public int getDayOfMonthIndicator() {
        return this.dom;
    }
    
    public DayOfWeek getDayOfWeek() {
        return this.dow;
    }
    
    public LocalTime getLocalTime() {
        return this.time;
    }
    
    public boolean isMidnightEndOfDay() {
        return this.timeEndOfDay;
    }
    
    public TimeDefinition getTimeDefinition() {
        return this.timeDefinition;
    }
    
    public ZoneOffset getStandardOffset() {
        return this.standardOffset;
    }
    
    public ZoneOffset getOffsetBefore() {
        return this.offsetBefore;
    }
    
    public ZoneOffset getOffsetAfter() {
        return this.offsetAfter;
    }
    
    public ZoneOffsetTransition createTransition(final int n) {
        LocalDate localDate;
        if (this.dom < 0) {
            localDate = LocalDate.of(n, this.month, this.month.length(IsoChronology.INSTANCE.isLeapYear(n)) + 1 + this.dom);
            if (this.dow != null) {
                localDate = localDate.with(TemporalAdjusters.previousOrSame(this.dow));
            }
        }
        else {
            localDate = LocalDate.of(n, this.month, this.dom);
            if (this.dow != null) {
                localDate = localDate.with(TemporalAdjusters.nextOrSame(this.dow));
            }
        }
        if (this.timeEndOfDay) {
            localDate = localDate.plusDays(1L);
        }
        return new ZoneOffsetTransition(this.timeDefinition.createDateTime(LocalDateTime.of(localDate, this.time), this.standardOffset, this.offsetBefore), this.offsetBefore, this.offsetAfter);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ZoneOffsetTransitionRule) {
            final ZoneOffsetTransitionRule zoneOffsetTransitionRule = (ZoneOffsetTransitionRule)o;
            return this.month == zoneOffsetTransitionRule.month && this.dom == zoneOffsetTransitionRule.dom && this.dow == zoneOffsetTransitionRule.dow && this.timeDefinition == zoneOffsetTransitionRule.timeDefinition && this.time.equals(zoneOffsetTransitionRule.time) && this.timeEndOfDay == zoneOffsetTransitionRule.timeEndOfDay && this.standardOffset.equals(zoneOffsetTransitionRule.standardOffset) && this.offsetBefore.equals(zoneOffsetTransitionRule.offsetBefore) && this.offsetAfter.equals(zoneOffsetTransitionRule.offsetAfter);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.time.toSecondOfDay() + (this.timeEndOfDay ? 1 : 0) << 15) + (this.month.ordinal() << 11) + (this.dom + 32 << 5) + (((this.dow == null) ? 7 : this.dow.ordinal()) << 2) + this.timeDefinition.ordinal() ^ this.standardOffset.hashCode() ^ this.offsetBefore.hashCode() ^ this.offsetAfter.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TransitionRule[").append((this.offsetBefore.compareTo(this.offsetAfter) > 0) ? "Gap " : "Overlap ").append(this.offsetBefore).append(" to ").append(this.offsetAfter).append(", ");
        if (this.dow != null) {
            if (this.dom == -1) {
                sb.append(this.dow.name()).append(" on or before last day of ").append(this.month.name());
            }
            else if (this.dom < 0) {
                sb.append(this.dow.name()).append(" on or before last day minus ").append(-this.dom - 1).append(" of ").append(this.month.name());
            }
            else {
                sb.append(this.dow.name()).append(" on or after ").append(this.month.name()).append(' ').append(this.dom);
            }
        }
        else {
            sb.append(this.month.name()).append(' ').append(this.dom);
        }
        sb.append(" at ").append(this.timeEndOfDay ? "24:00" : this.time.toString()).append(" ").append(this.timeDefinition).append(", standard offset ").append(this.standardOffset).append(']');
        return sb.toString();
    }
    
    public enum TimeDefinition
    {
        UTC, 
        WALL, 
        STANDARD;
        
        public LocalDateTime createDateTime(final LocalDateTime localDateTime, final ZoneOffset zoneOffset, final ZoneOffset zoneOffset2) {
            switch (this) {
                case UTC: {
                    return localDateTime.plusSeconds(zoneOffset2.getTotalSeconds() - ZoneOffset.UTC.getTotalSeconds());
                }
                case STANDARD: {
                    return localDateTime.plusSeconds(zoneOffset2.getTotalSeconds() - zoneOffset.getTotalSeconds());
                }
                default: {
                    return localDateTime;
                }
            }
        }
    }
}
