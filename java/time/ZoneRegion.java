package java.time;

import java.util.*;
import java.time.zone.*;
import java.io.*;

final class ZoneRegion extends ZoneId implements Serializable
{
    private static final long serialVersionUID = 8386373296231747096L;
    private final String id;
    private final transient ZoneRules rules;
    
    static ZoneRegion ofId(final String s, final boolean b) {
        Objects.requireNonNull(s, "zoneId");
        checkName(s);
        ZoneRules rules = null;
        try {
            rules = ZoneRulesProvider.getRules(s, true);
        }
        catch (ZoneRulesException ex) {
            if (b) {
                throw ex;
            }
        }
        return new ZoneRegion(s, rules);
    }
    
    private static void checkName(final String s) {
        final int length = s.length();
        if (length < 2) {
            throw new DateTimeException("Invalid ID for region-based ZoneId, invalid format: " + s);
        }
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 < 'a' || char1 > 'z') {
                if (char1 < 'A' || char1 > 'Z') {
                    if (char1 != '/' || i == 0) {
                        if (char1 < '0' || char1 > '9' || i == 0) {
                            if (char1 != '~' || i == 0) {
                                if (char1 != '.' || i == 0) {
                                    if (char1 != '_' || i == 0) {
                                        if (char1 != '+' || i == 0) {
                                            if (char1 != '-' || i == 0) {
                                                throw new DateTimeException("Invalid ID for region-based ZoneId, invalid format: " + s);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    ZoneRegion(final String id, final ZoneRules rules) {
        this.id = id;
        this.rules = rules;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public ZoneRules getRules() {
        return (this.rules != null) ? this.rules : ZoneRulesProvider.getRules(this.id, false);
    }
    
    private Object writeReplace() {
        return new Ser((byte)7, this);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    @Override
    void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(7);
        this.writeExternal(dataOutput);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.id);
    }
    
    static ZoneId readExternal(final DataInput dataInput) throws IOException {
        return ZoneId.of(dataInput.readUTF(), false);
    }
}
