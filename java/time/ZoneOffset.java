package java.time;

import java.util.*;
import java.time.zone.*;
import java.time.temporal.*;
import java.io.*;
import java.util.concurrent.*;

public final class ZoneOffset extends ZoneId implements TemporalAccessor, TemporalAdjuster, Comparable<ZoneOffset>, Serializable
{
    private static final ConcurrentMap<Integer, ZoneOffset> SECONDS_CACHE;
    private static final ConcurrentMap<String, ZoneOffset> ID_CACHE;
    private static final int MAX_SECONDS = 64800;
    private static final long serialVersionUID = 2357656521762053153L;
    public static final ZoneOffset UTC;
    public static final ZoneOffset MIN;
    public static final ZoneOffset MAX;
    private final int totalSeconds;
    private final transient String id;
    
    public static ZoneOffset of(String string) {
        Objects.requireNonNull(string, "offsetId");
        final ZoneOffset zoneOffset = ZoneOffset.ID_CACHE.get(string);
        if (zoneOffset != null) {
            return zoneOffset;
        }
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        switch (string.length()) {
            case 2: {
                string = string.charAt(0) + "0" + string.charAt(1);
            }
            case 3: {
                n = parseNumber(string, 1, false);
                n2 = 0;
                n3 = 0;
                break;
            }
            case 5: {
                n = parseNumber(string, 1, false);
                n2 = parseNumber(string, 3, false);
                n3 = 0;
                break;
            }
            case 6: {
                n = parseNumber(string, 1, false);
                n2 = parseNumber(string, 4, true);
                n3 = 0;
                break;
            }
            case 7: {
                n = parseNumber(string, 1, false);
                n2 = parseNumber(string, 3, false);
                n3 = parseNumber(string, 5, false);
                break;
            }
            case 9: {
                n = parseNumber(string, 1, false);
                n2 = parseNumber(string, 4, true);
                n3 = parseNumber(string, 7, true);
                break;
            }
            default: {
                throw new DateTimeException("Invalid ID for ZoneOffset, invalid format: " + string);
            }
        }
        final char char1 = string.charAt(0);
        if (char1 != '+' && char1 != '-') {
            throw new DateTimeException("Invalid ID for ZoneOffset, plus/minus not found when expected: " + string);
        }
        if (char1 == '-') {
            return ofHoursMinutesSeconds(-n, -n2, -n3);
        }
        return ofHoursMinutesSeconds(n, n2, n3);
    }
    
    private static int parseNumber(final CharSequence charSequence, final int n, final boolean b) {
        if (b && charSequence.charAt(n - 1) != ':') {
            throw new DateTimeException("Invalid ID for ZoneOffset, colon not found when expected: " + (Object)charSequence);
        }
        final char char1 = charSequence.charAt(n);
        final char char2 = charSequence.charAt(n + 1);
        if (char1 < '0' || char1 > '9' || char2 < '0' || char2 > '9') {
            throw new DateTimeException("Invalid ID for ZoneOffset, non numeric characters found: " + (Object)charSequence);
        }
        return (char1 - '0') * 10 + (char2 - '0');
    }
    
    public static ZoneOffset ofHours(final int n) {
        return ofHoursMinutesSeconds(n, 0, 0);
    }
    
    public static ZoneOffset ofHoursMinutes(final int n, final int n2) {
        return ofHoursMinutesSeconds(n, n2, 0);
    }
    
    public static ZoneOffset ofHoursMinutesSeconds(final int n, final int n2, final int n3) {
        validate(n, n2, n3);
        return ofTotalSeconds(totalSeconds(n, n2, n3));
    }
    
    public static ZoneOffset from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        final ZoneOffset zoneOffset = temporalAccessor.query(TemporalQueries.offset());
        if (zoneOffset == null) {
            throw new DateTimeException("Unable to obtain ZoneOffset from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
        }
        return zoneOffset;
    }
    
    private static void validate(final int n, final int n2, final int n3) {
        if (n < -18 || n > 18) {
            throw new DateTimeException("Zone offset hours not in valid range: value " + n + " is not in the range -18 to 18");
        }
        if (n > 0) {
            if (n2 < 0 || n3 < 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be positive because hours is positive");
            }
        }
        else if (n < 0) {
            if (n2 > 0 || n3 > 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be negative because hours is negative");
            }
        }
        else if ((n2 > 0 && n3 < 0) || (n2 < 0 && n3 > 0)) {
            throw new DateTimeException("Zone offset minutes and seconds must have the same sign");
        }
        if (n2 < -59 || n2 > 59) {
            throw new DateTimeException("Zone offset minutes not in valid range: value " + n2 + " is not in the range -59 to 59");
        }
        if (n3 < -59 || n3 > 59) {
            throw new DateTimeException("Zone offset seconds not in valid range: value " + n3 + " is not in the range -59 to 59");
        }
        if (Math.abs(n) == 18 && (n2 | n3) != 0x0) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        }
    }
    
    private static int totalSeconds(final int n, final int n2, final int n3) {
        return n * 3600 + n2 * 60 + n3;
    }
    
    public static ZoneOffset ofTotalSeconds(final int n) {
        if (n < -64800 || n > 64800) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        }
        if (n % 900 == 0) {
            final Integer value = n;
            ZoneOffset zoneOffset = ZoneOffset.SECONDS_CACHE.get(value);
            if (zoneOffset == null) {
                ZoneOffset.SECONDS_CACHE.putIfAbsent(value, new ZoneOffset(n));
                zoneOffset = ZoneOffset.SECONDS_CACHE.get(value);
                ZoneOffset.ID_CACHE.putIfAbsent(zoneOffset.getId(), zoneOffset);
            }
            return zoneOffset;
        }
        return new ZoneOffset(n);
    }
    
    private ZoneOffset(final int totalSeconds) {
        this.totalSeconds = totalSeconds;
        this.id = buildId(totalSeconds);
    }
    
    private static String buildId(final int n) {
        if (n == 0) {
            return "Z";
        }
        final int abs = Math.abs(n);
        final StringBuilder sb = new StringBuilder();
        final int n2 = abs / 3600;
        final int n3 = abs / 60 % 60;
        sb.append((n < 0) ? "-" : "+").append((n2 < 10) ? "0" : "").append(n2).append((n3 < 10) ? ":0" : ":").append(n3);
        final int n4 = abs % 60;
        if (n4 != 0) {
            sb.append((n4 < 10) ? ":0" : ":").append(n4);
        }
        return sb.toString();
    }
    
    public int getTotalSeconds() {
        return this.totalSeconds;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public ZoneRules getRules() {
        return ZoneRules.of(this);
    }
    
    @Override
    public boolean isSupported(final TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField == ChronoField.OFFSET_SECONDS;
        }
        return temporalField != null && temporalField.isSupportedBy(this);
    }
    
    @Override
    public ValueRange range(final TemporalField temporalField) {
        return super.range(temporalField);
    }
    
    @Override
    public int get(final TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return this.range(temporalField).checkValidIntValue(this.getLong(temporalField), temporalField);
    }
    
    @Override
    public long getLong(final TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
        }
        return temporalField.getFrom(this);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return (R)this;
        }
        return super.query(temporalQuery);
    }
    
    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return temporal.with(ChronoField.OFFSET_SECONDS, this.totalSeconds);
    }
    
    @Override
    public int compareTo(final ZoneOffset zoneOffset) {
        return zoneOffset.totalSeconds - this.totalSeconds;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ZoneOffset && this.totalSeconds == ((ZoneOffset)o).totalSeconds);
    }
    
    @Override
    public int hashCode() {
        return this.totalSeconds;
    }
    
    @Override
    public String toString() {
        return this.id;
    }
    
    private Object writeReplace() {
        return new Ser((byte)8, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    @Override
    void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(8);
        this.writeExternal(dataOutput);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        final int totalSeconds = this.totalSeconds;
        final int n = (totalSeconds % 900 == 0) ? (totalSeconds / 900) : 127;
        dataOutput.writeByte(n);
        if (n == 127) {
            dataOutput.writeInt(totalSeconds);
        }
    }
    
    static ZoneOffset readExternal(final DataInput dataInput) throws IOException {
        final byte byte1 = dataInput.readByte();
        return (byte1 == 127) ? ofTotalSeconds(dataInput.readInt()) : ofTotalSeconds(byte1 * 900);
    }
    
    static {
        SECONDS_CACHE = new ConcurrentHashMap<Integer, ZoneOffset>(16, 0.75f, 4);
        ID_CACHE = new ConcurrentHashMap<String, ZoneOffset>(16, 0.75f, 4);
        UTC = ofTotalSeconds(0);
        MIN = ofTotalSeconds(-64800);
        MAX = ofTotalSeconds(64800);
    }
}
