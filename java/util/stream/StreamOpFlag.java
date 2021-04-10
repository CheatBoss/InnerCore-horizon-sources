package java.util.stream;

import java.util.*;

enum StreamOpFlag
{
    DISTINCT(0, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)), 
    SORTED(1, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)), 
    ORDERED(2, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP).clear(Type.TERMINAL_OP).clear(Type.UPSTREAM_TERMINAL_OP)), 
    SIZED(3, set(Type.SPLITERATOR).set(Type.STREAM).clear(Type.OP)), 
    SHORT_CIRCUIT(12, set(Type.OP).set(Type.TERMINAL_OP));
    
    private static final int SET_BITS = 1;
    private static final int CLEAR_BITS = 2;
    private static final int PRESERVE_BITS = 3;
    private final Map<Type, Integer> maskTable;
    private final int bitPosition;
    private final int set;
    private final int clear;
    private final int preserve;
    static final int SPLITERATOR_CHARACTERISTICS_MASK;
    static final int STREAM_MASK;
    static final int OP_MASK;
    static final int TERMINAL_OP_MASK;
    static final int UPSTREAM_TERMINAL_OP_MASK;
    private static final int FLAG_MASK;
    private static final int FLAG_MASK_IS;
    private static final int FLAG_MASK_NOT;
    static final int INITIAL_OPS_VALUE;
    static final int IS_DISTINCT;
    static final int NOT_DISTINCT;
    static final int IS_SORTED;
    static final int NOT_SORTED;
    static final int IS_ORDERED;
    static final int NOT_ORDERED;
    static final int IS_SIZED;
    static final int NOT_SIZED;
    static final int IS_SHORT_CIRCUIT;
    
    private static MaskBuilder set(final Type type) {
        return new MaskBuilder(new EnumMap<Type, Integer>(Type.class)).set(type);
    }
    
    private StreamOpFlag(int bitPosition, final MaskBuilder maskBuilder) {
        this.maskTable = maskBuilder.build();
        bitPosition *= 2;
        this.bitPosition = bitPosition;
        this.set = 1 << bitPosition;
        this.clear = 2 << bitPosition;
        this.preserve = 3 << bitPosition;
    }
    
    int set() {
        return this.set;
    }
    
    int clear() {
        return this.clear;
    }
    
    boolean isStreamFlag() {
        return this.maskTable.get(Type.STREAM) > 0;
    }
    
    boolean isKnown(final int n) {
        return (n & this.preserve) == this.set;
    }
    
    boolean isCleared(final int n) {
        return (n & this.preserve) == this.clear;
    }
    
    boolean isPreserved(final int n) {
        return (n & this.preserve) == this.preserve;
    }
    
    boolean canSet(final Type type) {
        return (this.maskTable.get(type) & 0x1) > 0;
    }
    
    private static int createMask(final Type type) {
        int n = 0;
        for (final StreamOpFlag streamOpFlag : values()) {
            n |= streamOpFlag.maskTable.get(type) << streamOpFlag.bitPosition;
        }
        return n;
    }
    
    private static int createFlagMask() {
        int n = 0;
        final StreamOpFlag[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            n |= values[i].preserve;
        }
        return n;
    }
    
    private static int getMask(final int n) {
        return (n == 0) ? StreamOpFlag.FLAG_MASK : (~(n | (StreamOpFlag.FLAG_MASK_IS & n) << 1 | (StreamOpFlag.FLAG_MASK_NOT & n) >> 1));
    }
    
    static int combineOpFlags(final int n, final int n2) {
        return (n2 & getMask(n)) | n;
    }
    
    static int toStreamFlags(final int n) {
        return ~n >> 1 & StreamOpFlag.FLAG_MASK_IS & n;
    }
    
    static int toCharacteristics(final int n) {
        return n & StreamOpFlag.SPLITERATOR_CHARACTERISTICS_MASK;
    }
    
    static int fromCharacteristics(final Spliterator<?> spliterator) {
        final int characteristics = spliterator.characteristics();
        if ((characteristics & 0x4) != 0x0 && spliterator.getComparator() != null) {
            return characteristics & StreamOpFlag.SPLITERATOR_CHARACTERISTICS_MASK & 0xFFFFFFFB;
        }
        return characteristics & StreamOpFlag.SPLITERATOR_CHARACTERISTICS_MASK;
    }
    
    static int fromCharacteristics(final int n) {
        return n & StreamOpFlag.SPLITERATOR_CHARACTERISTICS_MASK;
    }
    
    static {
        SPLITERATOR_CHARACTERISTICS_MASK = createMask(Type.SPLITERATOR);
        STREAM_MASK = createMask(Type.STREAM);
        OP_MASK = createMask(Type.OP);
        TERMINAL_OP_MASK = createMask(Type.TERMINAL_OP);
        UPSTREAM_TERMINAL_OP_MASK = createMask(Type.UPSTREAM_TERMINAL_OP);
        FLAG_MASK = createFlagMask();
        FLAG_MASK_IS = StreamOpFlag.STREAM_MASK;
        FLAG_MASK_NOT = StreamOpFlag.STREAM_MASK << 1;
        INITIAL_OPS_VALUE = (StreamOpFlag.FLAG_MASK_IS | StreamOpFlag.FLAG_MASK_NOT);
        IS_DISTINCT = StreamOpFlag.DISTINCT.set;
        NOT_DISTINCT = StreamOpFlag.DISTINCT.clear;
        IS_SORTED = StreamOpFlag.SORTED.set;
        NOT_SORTED = StreamOpFlag.SORTED.clear;
        IS_ORDERED = StreamOpFlag.ORDERED.set;
        NOT_ORDERED = StreamOpFlag.ORDERED.clear;
        IS_SIZED = StreamOpFlag.SIZED.set;
        NOT_SIZED = StreamOpFlag.SIZED.clear;
        IS_SHORT_CIRCUIT = StreamOpFlag.SHORT_CIRCUIT.set;
    }
    
    private static class MaskBuilder
    {
        final Map<Type, Integer> map;
        
        MaskBuilder(final Map<Type, Integer> map) {
            this.map = map;
        }
        
        MaskBuilder mask(final Type type, final Integer n) {
            this.map.put(type, n);
            return this;
        }
        
        MaskBuilder set(final Type type) {
            return this.mask(type, 1);
        }
        
        MaskBuilder clear(final Type type) {
            return this.mask(type, 2);
        }
        
        MaskBuilder setAndClear(final Type type) {
            return this.mask(type, 3);
        }
        
        Map<Type, Integer> build() {
            final Type[] values = Type.values();
            for (int length = values.length, i = 0; i < length; ++i) {
                this.map.putIfAbsent(values[i], 0);
            }
            return this.map;
        }
    }
    
    enum Type
    {
        SPLITERATOR, 
        STREAM, 
        OP, 
        TERMINAL_OP, 
        UPSTREAM_TERMINAL_OP;
    }
}
