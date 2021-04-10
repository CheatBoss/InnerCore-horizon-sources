package java.time.zone;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;

final class TzdbZoneRulesProvider extends ZoneRulesProvider
{
    private List<String> regionIds;
    private String versionId;
    private final Map<String, Object> regionToRules;
    
    public TzdbZoneRulesProvider() {
        this.regionToRules = new ConcurrentHashMap<String, Object>();
        try (final DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(System.getProperty("java.home") + File.separator + "lib", "tzdb.dat"))))) {
            this.load(dataInputStream);
        }
        catch (Exception ex) {
            throw new ZoneRulesException("Unable to load TZDB time-zone rules", ex);
        }
    }
    
    @Override
    protected Set<String> provideZoneIds() {
        return new HashSet<String>(this.regionIds);
    }
    
    @Override
    protected ZoneRules provideRules(final String s, final boolean b) {
        Object o = this.regionToRules.get(s);
        if (o == null) {
            throw new ZoneRulesException("Unknown time-zone ID: " + s);
        }
        try {
            if (o instanceof byte[]) {
                o = Ser.read(new DataInputStream(new ByteArrayInputStream((byte[])o)));
                this.regionToRules.put(s, o);
            }
            return (ZoneRules)o;
        }
        catch (Exception ex) {
            throw new ZoneRulesException("Invalid binary time-zone data: TZDB:" + s + ", version: " + this.versionId, ex);
        }
    }
    
    @Override
    protected NavigableMap<String, ZoneRules> provideVersions(final String s) {
        final TreeMap<String, ZoneRules> treeMap = new TreeMap<String, ZoneRules>();
        final ZoneRules rules = ZoneRulesProvider.getRules(s, false);
        if (rules != null) {
            treeMap.put(this.versionId, rules);
        }
        return treeMap;
    }
    
    private void load(final DataInputStream dataInputStream) throws Exception {
        if (dataInputStream.readByte() != 1) {
            throw new StreamCorruptedException("File format not recognised");
        }
        if (!"TZDB".equals(dataInputStream.readUTF())) {
            throw new StreamCorruptedException("File format not recognised");
        }
        final short short1 = dataInputStream.readShort();
        for (short n = 0; n < short1; ++n) {
            this.versionId = dataInputStream.readUTF();
        }
        final short short2 = dataInputStream.readShort();
        final String[] array = new String[short2];
        for (short n2 = 0; n2 < short2; ++n2) {
            array[n2] = dataInputStream.readUTF();
        }
        this.regionIds = Arrays.asList(array);
        final short short3 = dataInputStream.readShort();
        final Object[] array2 = new Object[short3];
        for (short n3 = 0; n3 < short3; ++n3) {
            final byte[] array3 = new byte[dataInputStream.readShort()];
            dataInputStream.readFully(array3);
            array2[n3] = array3;
        }
        for (short n4 = 0; n4 < short1; ++n4) {
            final short short4 = dataInputStream.readShort();
            this.regionToRules.clear();
            for (short n5 = 0; n5 < short4; ++n5) {
                this.regionToRules.put(array[dataInputStream.readShort()], array2[dataInputStream.readShort() & 0xFFFF]);
            }
        }
    }
    
    @Override
    public String toString() {
        return "TZDB[" + this.versionId + "]";
    }
}
