package java.time;

import java.time.format.*;
import java.time.temporal.*;
import java.time.zone.*;
import java.io.*;
import java.util.*;

public abstract class ZoneId implements Serializable
{
    public static final Map<String, String> SHORT_IDS;
    private static final long serialVersionUID = 8352817235686L;
    
    public static ZoneId systemDefault() {
        return TimeZone.getDefault().toZoneId();
    }
    
    public static Set<String> getAvailableZoneIds() {
        return ZoneRulesProvider.getAvailableZoneIds();
    }
    
    public static ZoneId of(final String s, final Map<String, String> map) {
        Objects.requireNonNull(s, "zoneId");
        Objects.requireNonNull(map, "aliasMap");
        final String s2 = map.get(s);
        return of((s2 != null) ? s2 : s);
    }
    
    public static ZoneId of(final String s) {
        return of(s, true);
    }
    
    public static ZoneId ofOffset(String concat, final ZoneOffset zoneOffset) {
        Objects.requireNonNull(concat, "prefix");
        Objects.requireNonNull(zoneOffset, "offset");
        if (concat.length() == 0) {
            return zoneOffset;
        }
        if (!concat.equals("GMT") && !concat.equals("UTC") && !concat.equals("UT")) {
            throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: " + concat);
        }
        if (zoneOffset.getTotalSeconds() != 0) {
            concat = concat.concat(zoneOffset.getId());
        }
        return new ZoneRegion(concat, zoneOffset.getRules());
    }
    
    static ZoneId of(final String s, final boolean b) {
        Objects.requireNonNull(s, "zoneId");
        if (s.length() <= 1 || s.startsWith("+") || s.startsWith("-")) {
            return ZoneOffset.of(s);
        }
        if (s.startsWith("UTC") || s.startsWith("GMT")) {
            return ofWithPrefix(s, 3, b);
        }
        if (s.startsWith("UT")) {
            return ofWithPrefix(s, 2, b);
        }
        return ZoneRegion.ofId(s, b);
    }
    
    private static ZoneId ofWithPrefix(final String s, final int n, final boolean b) {
        final String substring = s.substring(0, n);
        if (s.length() == n) {
            return ofOffset(substring, ZoneOffset.UTC);
        }
        if (s.charAt(n) != '+' && s.charAt(n) != '-') {
            return ZoneRegion.ofId(s, b);
        }
        try {
            final ZoneOffset of = ZoneOffset.of(s.substring(n));
            if (of == ZoneOffset.UTC) {
                return ofOffset(substring, of);
            }
            return ofOffset(substring, of);
        }
        catch (DateTimeException ex) {
            throw new DateTimeException("Invalid ID for offset-based ZoneId: " + s, ex);
        }
    }
    
    public static ZoneId from(final TemporalAccessor temporalAccessor) {
        final ZoneId zoneId = temporalAccessor.query(TemporalQueries.zone());
        if (zoneId == null) {
            throw new DateTimeException("Unable to obtain ZoneId from TemporalAccessor: " + temporalAccessor + " of type " + temporalAccessor.getClass().getName());
        }
        return zoneId;
    }
    
    ZoneId() {
        if (this.getClass() != ZoneOffset.class && this.getClass() != ZoneRegion.class) {
            throw new AssertionError((Object)"Invalid subclass");
        }
    }
    
    public abstract String getId();
    
    public String getDisplayName(final TextStyle textStyle, final Locale locale) {
        return new DateTimeFormatterBuilder().appendZoneText(textStyle).toFormatter(locale).format(this.toTemporal());
    }
    
    private TemporalAccessor toTemporal() {
        return new TemporalAccessor() {
            @Override
            public boolean isSupported(final TemporalField temporalField) {
                return false;
            }
            
            @Override
            public long getLong(final TemporalField temporalField) {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + temporalField);
            }
            
            @Override
            public <R> R query(final TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return (R)ZoneId.this;
                }
                return super.query(temporalQuery);
            }
        };
    }
    
    public abstract ZoneRules getRules();
    
    public ZoneId normalized() {
        try {
            final ZoneRules rules = this.getRules();
            if (rules.isFixedOffset()) {
                return rules.getOffset(Instant.EPOCH);
            }
        }
        catch (ZoneRulesException ex) {}
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ZoneId && this.getId().equals(((ZoneId)o).getId()));
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    @Override
    public String toString() {
        return this.getId();
    }
    
    private Object writeReplace() {
        return new Ser((byte)7, this);
    }
    
    abstract void write(final DataOutput p0) throws IOException;
    
    static {
        final HashMap<String, String> hashMap = new HashMap<String, String>(64);
        hashMap.put("ACT", "Australia/Darwin");
        hashMap.put("AET", "Australia/Sydney");
        hashMap.put("AGT", "America/Argentina/Buenos_Aires");
        hashMap.put("ART", "Africa/Cairo");
        hashMap.put("AST", "America/Anchorage");
        hashMap.put("BET", "America/Sao_Paulo");
        hashMap.put("BST", "Asia/Dhaka");
        hashMap.put("CAT", "Africa/Harare");
        hashMap.put("CNT", "America/St_Johns");
        hashMap.put("CST", "America/Chicago");
        hashMap.put("CTT", "Asia/Shanghai");
        hashMap.put("EAT", "Africa/Addis_Ababa");
        hashMap.put("ECT", "Europe/Paris");
        hashMap.put("IET", "America/Indiana/Indianapolis");
        hashMap.put("IST", "Asia/Kolkata");
        hashMap.put("JST", "Asia/Tokyo");
        hashMap.put("MIT", "Pacific/Apia");
        hashMap.put("NET", "Asia/Yerevan");
        hashMap.put("NST", "Pacific/Auckland");
        hashMap.put("PLT", "Asia/Karachi");
        hashMap.put("PNT", "America/Phoenix");
        hashMap.put("PRT", "America/Puerto_Rico");
        hashMap.put("PST", "America/Los_Angeles");
        hashMap.put("SST", "Pacific/Guadalcanal");
        hashMap.put("VST", "Asia/Ho_Chi_Minh");
        hashMap.put("EST", "-05:00");
        hashMap.put("MST", "-07:00");
        hashMap.put("HST", "-10:00");
        SHORT_IDS = Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
}
