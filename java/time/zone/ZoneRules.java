package java.time.zone;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import java.time.chrono.*;
import java.time.*;

public final class ZoneRules implements Serializable
{
    private static final long serialVersionUID = 3044319355680032515L;
    private static final int LAST_CACHED_YEAR = 2100;
    private final long[] standardTransitions;
    private final ZoneOffset[] standardOffsets;
    private final long[] savingsInstantTransitions;
    private final LocalDateTime[] savingsLocalTransitions;
    private final ZoneOffset[] wallOffsets;
    private final ZoneOffsetTransitionRule[] lastRules;
    private final transient ConcurrentMap<Integer, ZoneOffsetTransition[]> lastRulesCache;
    private static final long[] EMPTY_LONG_ARRAY;
    private static final ZoneOffsetTransitionRule[] EMPTY_LASTRULES;
    private static final LocalDateTime[] EMPTY_LDT_ARRAY;
    
    public static ZoneRules of(final ZoneOffset zoneOffset, final ZoneOffset zoneOffset2, final List<ZoneOffsetTransition> list, final List<ZoneOffsetTransition> list2, final List<ZoneOffsetTransitionRule> list3) {
        Objects.requireNonNull(zoneOffset, "baseStandardOffset");
        Objects.requireNonNull(zoneOffset2, "baseWallOffset");
        Objects.requireNonNull(list, "standardOffsetTransitionList");
        Objects.requireNonNull(list2, "transitionList");
        Objects.requireNonNull(list3, "lastRules");
        return new ZoneRules(zoneOffset, zoneOffset2, list, list2, list3);
    }
    
    public static ZoneRules of(final ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return new ZoneRules(zoneOffset);
    }
    
    ZoneRules(final ZoneOffset zoneOffset, final ZoneOffset zoneOffset2, final List<ZoneOffsetTransition> list, final List<ZoneOffsetTransition> list2, final List<ZoneOffsetTransitionRule> list3) {
        this.lastRulesCache = new ConcurrentHashMap<Integer, ZoneOffsetTransition[]>();
        this.standardTransitions = new long[list.size()];
        (this.standardOffsets = new ZoneOffset[list.size() + 1])[0] = zoneOffset;
        for (int i = 0; i < list.size(); ++i) {
            this.standardTransitions[i] = list.get(i).toEpochSecond();
            this.standardOffsets[i + 1] = list.get(i).getOffsetAfter();
        }
        final ArrayList<LocalDateTime> list4 = new ArrayList<LocalDateTime>();
        final ArrayList<ZoneOffset> list5 = new ArrayList<ZoneOffset>();
        list5.add(zoneOffset2);
        for (final ZoneOffsetTransition zoneOffsetTransition : list2) {
            if (zoneOffsetTransition.isGap()) {
                list4.add(zoneOffsetTransition.getDateTimeBefore());
                list4.add(zoneOffsetTransition.getDateTimeAfter());
            }
            else {
                list4.add(zoneOffsetTransition.getDateTimeAfter());
                list4.add(zoneOffsetTransition.getDateTimeBefore());
            }
            list5.add(zoneOffsetTransition.getOffsetAfter());
        }
        this.savingsLocalTransitions = list4.toArray(new LocalDateTime[list4.size()]);
        this.wallOffsets = list5.toArray(new ZoneOffset[list5.size()]);
        this.savingsInstantTransitions = new long[list2.size()];
        for (int j = 0; j < list2.size(); ++j) {
            this.savingsInstantTransitions[j] = list2.get(j).toEpochSecond();
        }
        if (list3.size() > 16) {
            throw new IllegalArgumentException("Too many transition rules");
        }
        this.lastRules = list3.toArray(new ZoneOffsetTransitionRule[list3.size()]);
    }
    
    private ZoneRules(final long[] standardTransitions, final ZoneOffset[] standardOffsets, final long[] savingsInstantTransitions, final ZoneOffset[] wallOffsets, final ZoneOffsetTransitionRule[] lastRules) {
        this.lastRulesCache = new ConcurrentHashMap<Integer, ZoneOffsetTransition[]>();
        this.standardTransitions = standardTransitions;
        this.standardOffsets = standardOffsets;
        this.savingsInstantTransitions = savingsInstantTransitions;
        this.wallOffsets = wallOffsets;
        this.lastRules = lastRules;
        if (savingsInstantTransitions.length == 0) {
            this.savingsLocalTransitions = ZoneRules.EMPTY_LDT_ARRAY;
        }
        else {
            final ArrayList<LocalDateTime> list = new ArrayList<LocalDateTime>();
            for (int i = 0; i < savingsInstantTransitions.length; ++i) {
                final ZoneOffsetTransition zoneOffsetTransition = new ZoneOffsetTransition(savingsInstantTransitions[i], wallOffsets[i], wallOffsets[i + 1]);
                if (zoneOffsetTransition.isGap()) {
                    list.add(zoneOffsetTransition.getDateTimeBefore());
                    list.add(zoneOffsetTransition.getDateTimeAfter());
                }
                else {
                    list.add(zoneOffsetTransition.getDateTimeAfter());
                    list.add(zoneOffsetTransition.getDateTimeBefore());
                }
            }
            this.savingsLocalTransitions = list.toArray(new LocalDateTime[list.size()]);
        }
    }
    
    private ZoneRules(final ZoneOffset zoneOffset) {
        this.lastRulesCache = new ConcurrentHashMap<Integer, ZoneOffsetTransition[]>();
        (this.standardOffsets = new ZoneOffset[1])[0] = zoneOffset;
        this.standardTransitions = ZoneRules.EMPTY_LONG_ARRAY;
        this.savingsInstantTransitions = ZoneRules.EMPTY_LONG_ARRAY;
        this.savingsLocalTransitions = ZoneRules.EMPTY_LDT_ARRAY;
        this.wallOffsets = this.standardOffsets;
        this.lastRules = ZoneRules.EMPTY_LASTRULES;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
    
    private Object writeReplace() {
        return new Ser((byte)1, this);
    }
    
    void writeExternal(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.standardTransitions.length);
        final long[] standardTransitions = this.standardTransitions;
        for (int length = standardTransitions.length, i = 0; i < length; ++i) {
            Ser.writeEpochSec(standardTransitions[i], dataOutput);
        }
        final ZoneOffset[] standardOffsets = this.standardOffsets;
        for (int length2 = standardOffsets.length, j = 0; j < length2; ++j) {
            Ser.writeOffset(standardOffsets[j], dataOutput);
        }
        dataOutput.writeInt(this.savingsInstantTransitions.length);
        final long[] savingsInstantTransitions = this.savingsInstantTransitions;
        for (int length3 = savingsInstantTransitions.length, k = 0; k < length3; ++k) {
            Ser.writeEpochSec(savingsInstantTransitions[k], dataOutput);
        }
        final ZoneOffset[] wallOffsets = this.wallOffsets;
        for (int length4 = wallOffsets.length, l = 0; l < length4; ++l) {
            Ser.writeOffset(wallOffsets[l], dataOutput);
        }
        dataOutput.writeByte(this.lastRules.length);
        final ZoneOffsetTransitionRule[] lastRules = this.lastRules;
        for (int length5 = lastRules.length, n = 0; n < length5; ++n) {
            lastRules[n].writeExternal(dataOutput);
        }
    }
    
    static ZoneRules readExternal(final DataInput dataInput) throws IOException, ClassNotFoundException {
        final int int1 = dataInput.readInt();
        final long[] array = (int1 == 0) ? ZoneRules.EMPTY_LONG_ARRAY : new long[int1];
        for (int i = 0; i < int1; ++i) {
            array[i] = Ser.readEpochSec(dataInput);
        }
        final ZoneOffset[] array2 = new ZoneOffset[int1 + 1];
        for (int j = 0; j < array2.length; ++j) {
            array2[j] = Ser.readOffset(dataInput);
        }
        final int int2 = dataInput.readInt();
        final long[] array3 = (int2 == 0) ? ZoneRules.EMPTY_LONG_ARRAY : new long[int2];
        for (int k = 0; k < int2; ++k) {
            array3[k] = Ser.readEpochSec(dataInput);
        }
        final ZoneOffset[] array4 = new ZoneOffset[int2 + 1];
        for (int l = 0; l < array4.length; ++l) {
            array4[l] = Ser.readOffset(dataInput);
        }
        final byte byte1 = dataInput.readByte();
        final ZoneOffsetTransitionRule[] array5 = (byte1 == 0) ? ZoneRules.EMPTY_LASTRULES : new ZoneOffsetTransitionRule[byte1];
        for (byte b = 0; b < byte1; ++b) {
            array5[b] = ZoneOffsetTransitionRule.readExternal(dataInput);
        }
        return new ZoneRules(array, array2, array3, array4, array5);
    }
    
    public boolean isFixedOffset() {
        return this.savingsInstantTransitions.length == 0;
    }
    
    public ZoneOffset getOffset(final Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        final long epochSecond = instant.getEpochSecond();
        if (this.lastRules.length > 0 && epochSecond > this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1]) {
            final ZoneOffsetTransition[] transitionArray = this.findTransitionArray(this.findYear(epochSecond, this.wallOffsets[this.wallOffsets.length - 1]));
            ZoneOffsetTransition zoneOffsetTransition = null;
            for (int i = 0; i < transitionArray.length; ++i) {
                zoneOffsetTransition = transitionArray[i];
                if (epochSecond < zoneOffsetTransition.toEpochSecond()) {
                    return zoneOffsetTransition.getOffsetBefore();
                }
            }
            return zoneOffsetTransition.getOffsetAfter();
        }
        int binarySearch = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
        if (binarySearch < 0) {
            binarySearch = -binarySearch - 2;
        }
        return this.wallOffsets[binarySearch + 1];
    }
    
    public ZoneOffset getOffset(final LocalDateTime localDateTime) {
        final Object offsetInfo = this.getOffsetInfo(localDateTime);
        if (offsetInfo instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition)offsetInfo).getOffsetBefore();
        }
        return (ZoneOffset)offsetInfo;
    }
    
    public List<ZoneOffset> getValidOffsets(final LocalDateTime localDateTime) {
        final Object offsetInfo = this.getOffsetInfo(localDateTime);
        if (offsetInfo instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition)offsetInfo).getValidOffsets();
        }
        return Collections.singletonList(offsetInfo);
    }
    
    public ZoneOffsetTransition getTransition(final LocalDateTime localDateTime) {
        final Object offsetInfo = this.getOffsetInfo(localDateTime);
        return (offsetInfo instanceof ZoneOffsetTransition) ? ((ZoneOffsetTransition)offsetInfo) : null;
    }
    
    private Object getOffsetInfo(final LocalDateTime localDateTime) {
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        if (this.lastRules.length > 0 && localDateTime.isAfter(this.savingsLocalTransitions[this.savingsLocalTransitions.length - 1])) {
            final ZoneOffsetTransition[] transitionArray = this.findTransitionArray(localDateTime.getYear());
            Object offsetInfo = null;
            for (final ZoneOffsetTransition zoneOffsetTransition : transitionArray) {
                offsetInfo = this.findOffsetInfo(localDateTime, zoneOffsetTransition);
                if (offsetInfo instanceof ZoneOffsetTransition || offsetInfo.equals(zoneOffsetTransition.getOffsetBefore())) {
                    return offsetInfo;
                }
            }
            return offsetInfo;
        }
        int binarySearch = Arrays.binarySearch(this.savingsLocalTransitions, localDateTime);
        if (binarySearch == -1) {
            return this.wallOffsets[0];
        }
        if (binarySearch < 0) {
            binarySearch = -binarySearch - 2;
        }
        else if (binarySearch < this.savingsLocalTransitions.length - 1 && this.savingsLocalTransitions[binarySearch].equals(this.savingsLocalTransitions[binarySearch + 1])) {
            ++binarySearch;
        }
        if ((binarySearch & 0x1) != 0x0) {
            return this.wallOffsets[binarySearch / 2 + 1];
        }
        final LocalDateTime localDateTime2 = this.savingsLocalTransitions[binarySearch];
        final LocalDateTime localDateTime3 = this.savingsLocalTransitions[binarySearch + 1];
        final ZoneOffset zoneOffset = this.wallOffsets[binarySearch / 2];
        final ZoneOffset zoneOffset2 = this.wallOffsets[binarySearch / 2 + 1];
        if (zoneOffset2.getTotalSeconds() > zoneOffset.getTotalSeconds()) {
            return new ZoneOffsetTransition(localDateTime2, zoneOffset, zoneOffset2);
        }
        return new ZoneOffsetTransition(localDateTime3, zoneOffset, zoneOffset2);
    }
    
    private Object findOffsetInfo(final LocalDateTime localDateTime, final ZoneOffsetTransition zoneOffsetTransition) {
        final LocalDateTime dateTimeBefore = zoneOffsetTransition.getDateTimeBefore();
        if (zoneOffsetTransition.isGap()) {
            if (localDateTime.isBefore(dateTimeBefore)) {
                return zoneOffsetTransition.getOffsetBefore();
            }
            if (localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter())) {
                return zoneOffsetTransition;
            }
            return zoneOffsetTransition.getOffsetAfter();
        }
        else {
            if (!localDateTime.isBefore(dateTimeBefore)) {
                return zoneOffsetTransition.getOffsetAfter();
            }
            if (localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter())) {
                return zoneOffsetTransition.getOffsetBefore();
            }
            return zoneOffsetTransition;
        }
    }
    
    private ZoneOffsetTransition[] findTransitionArray(final int n) {
        final Integer value = n;
        final ZoneOffsetTransition[] array = this.lastRulesCache.get(value);
        if (array != null) {
            return array;
        }
        final ZoneOffsetTransitionRule[] lastRules = this.lastRules;
        final ZoneOffsetTransition[] array2 = new ZoneOffsetTransition[lastRules.length];
        for (int i = 0; i < lastRules.length; ++i) {
            array2[i] = lastRules[i].createTransition(n);
        }
        if (n < 2100) {
            this.lastRulesCache.putIfAbsent(value, array2);
        }
        return array2;
    }
    
    public ZoneOffset getStandardOffset(final Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return this.standardOffsets[0];
        }
        int binarySearch = Arrays.binarySearch(this.standardTransitions, instant.getEpochSecond());
        if (binarySearch < 0) {
            binarySearch = -binarySearch - 2;
        }
        return this.standardOffsets[binarySearch + 1];
    }
    
    public Duration getDaylightSavings(final Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return Duration.ZERO;
        }
        return Duration.ofSeconds(this.getOffset(instant).getTotalSeconds() - this.getStandardOffset(instant).getTotalSeconds());
    }
    
    public boolean isDaylightSavings(final Instant instant) {
        return !this.getStandardOffset(instant).equals(this.getOffset(instant));
    }
    
    public boolean isValidOffset(final LocalDateTime localDateTime, final ZoneOffset zoneOffset) {
        return this.getValidOffsets(localDateTime).contains(zoneOffset);
    }
    
    public ZoneOffsetTransition nextTransition(final Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        final long epochSecond = instant.getEpochSecond();
        if (epochSecond < this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1]) {
            int binarySearch = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
            if (binarySearch < 0) {
                binarySearch = -binarySearch - 1;
            }
            else {
                ++binarySearch;
            }
            return new ZoneOffsetTransition(this.savingsInstantTransitions[binarySearch], this.wallOffsets[binarySearch], this.wallOffsets[binarySearch + 1]);
        }
        if (this.lastRules.length == 0) {
            return null;
        }
        final int year = this.findYear(epochSecond, this.wallOffsets[this.wallOffsets.length - 1]);
        for (final ZoneOffsetTransition zoneOffsetTransition : this.findTransitionArray(year)) {
            if (epochSecond < zoneOffsetTransition.toEpochSecond()) {
                return zoneOffsetTransition;
            }
        }
        if (year < 999999999) {
            return this.findTransitionArray(year + 1)[0];
        }
        return null;
    }
    
    public ZoneOffsetTransition previousTransition(final Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        long epochSecond = instant.getEpochSecond();
        if (instant.getNano() > 0 && epochSecond < Long.MAX_VALUE) {
            ++epochSecond;
        }
        final long n = this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1];
        if (this.lastRules.length > 0 && epochSecond > n) {
            final ZoneOffset zoneOffset = this.wallOffsets[this.wallOffsets.length - 1];
            int year = this.findYear(epochSecond, zoneOffset);
            final ZoneOffsetTransition[] transitionArray = this.findTransitionArray(year);
            for (int i = transitionArray.length - 1; i >= 0; --i) {
                if (epochSecond > transitionArray[i].toEpochSecond()) {
                    return transitionArray[i];
                }
            }
            if (--year > this.findYear(n, zoneOffset)) {
                final ZoneOffsetTransition[] transitionArray2 = this.findTransitionArray(year);
                return transitionArray2[transitionArray2.length - 1];
            }
        }
        int binarySearch = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
        if (binarySearch < 0) {
            binarySearch = -binarySearch - 1;
        }
        if (binarySearch <= 0) {
            return null;
        }
        return new ZoneOffsetTransition(this.savingsInstantTransitions[binarySearch - 1], this.wallOffsets[binarySearch - 1], this.wallOffsets[binarySearch]);
    }
    
    private int findYear(final long n, final ZoneOffset zoneOffset) {
        return LocalDate.ofEpochDay(Math.floorDiv(n + zoneOffset.getTotalSeconds(), 86400L)).getYear();
    }
    
    public List<ZoneOffsetTransition> getTransitions() {
        final ArrayList<ZoneOffsetTransition> list = new ArrayList<ZoneOffsetTransition>();
        for (int i = 0; i < this.savingsInstantTransitions.length; ++i) {
            list.add(new ZoneOffsetTransition(this.savingsInstantTransitions[i], this.wallOffsets[i], this.wallOffsets[i + 1]));
        }
        return (List<ZoneOffsetTransition>)Collections.unmodifiableList((List<?>)list);
    }
    
    public List<ZoneOffsetTransitionRule> getTransitionRules() {
        return Collections.unmodifiableList((List<? extends ZoneOffsetTransitionRule>)Arrays.asList((T[])this.lastRules));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ZoneRules) {
            final ZoneRules zoneRules = (ZoneRules)o;
            return Arrays.equals(this.standardTransitions, zoneRules.standardTransitions) && Arrays.equals(this.standardOffsets, zoneRules.standardOffsets) && Arrays.equals(this.savingsInstantTransitions, zoneRules.savingsInstantTransitions) && Arrays.equals(this.wallOffsets, zoneRules.wallOffsets) && Arrays.equals(this.lastRules, zoneRules.lastRules);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.standardTransitions) ^ Arrays.hashCode(this.standardOffsets) ^ Arrays.hashCode(this.savingsInstantTransitions) ^ Arrays.hashCode(this.wallOffsets) ^ Arrays.hashCode(this.lastRules);
    }
    
    @Override
    public String toString() {
        return "ZoneRules[currentStandardOffset=" + this.standardOffsets[this.standardOffsets.length - 1] + "]";
    }
    
    static {
        EMPTY_LONG_ARRAY = new long[0];
        EMPTY_LASTRULES = new ZoneOffsetTransitionRule[0];
        EMPTY_LDT_ARRAY = new LocalDateTime[0];
    }
}
