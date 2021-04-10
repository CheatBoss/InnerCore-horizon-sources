package com.android.dx.rop.cst;

import java.util.*;
import com.android.dx.rop.type.*;

public final class CstType extends TypedConstant
{
    public static final CstType BOOLEAN;
    public static final CstType BOOLEAN_ARRAY;
    public static final CstType BYTE;
    public static final CstType BYTE_ARRAY;
    public static final CstType CHARACTER;
    public static final CstType CHAR_ARRAY;
    public static final CstType DOUBLE;
    public static final CstType DOUBLE_ARRAY;
    public static final CstType FLOAT;
    public static final CstType FLOAT_ARRAY;
    public static final CstType INTEGER;
    public static final CstType INT_ARRAY;
    public static final CstType LONG;
    public static final CstType LONG_ARRAY;
    public static final CstType OBJECT;
    public static final CstType SHORT;
    public static final CstType SHORT_ARRAY;
    public static final CstType VOID;
    private static final HashMap<Type, CstType> interns;
    private CstString descriptor;
    private final Type type;
    
    static {
        interns = new HashMap<Type, CstType>(100);
        OBJECT = intern(Type.OBJECT);
        BOOLEAN = intern(Type.BOOLEAN_CLASS);
        BYTE = intern(Type.BYTE_CLASS);
        CHARACTER = intern(Type.CHARACTER_CLASS);
        DOUBLE = intern(Type.DOUBLE_CLASS);
        FLOAT = intern(Type.FLOAT_CLASS);
        LONG = intern(Type.LONG_CLASS);
        INTEGER = intern(Type.INTEGER_CLASS);
        SHORT = intern(Type.SHORT_CLASS);
        VOID = intern(Type.VOID_CLASS);
        BOOLEAN_ARRAY = intern(Type.BOOLEAN_ARRAY);
        BYTE_ARRAY = intern(Type.BYTE_ARRAY);
        CHAR_ARRAY = intern(Type.CHAR_ARRAY);
        DOUBLE_ARRAY = intern(Type.DOUBLE_ARRAY);
        FLOAT_ARRAY = intern(Type.FLOAT_ARRAY);
        LONG_ARRAY = intern(Type.LONG_ARRAY);
        INT_ARRAY = intern(Type.INT_ARRAY);
        SHORT_ARRAY = intern(Type.SHORT_ARRAY);
    }
    
    public CstType(final Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (type == Type.KNOWN_NULL) {
            throw new UnsupportedOperationException("KNOWN_NULL is not representable");
        }
        this.type = type;
        this.descriptor = null;
    }
    
    public static CstType forBoxedPrimitiveType(final Type type) {
        switch (type.getBasicType()) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("not primitive: ");
                sb.append(type);
                throw new IllegalArgumentException(sb.toString());
            }
            case 8: {
                return CstType.SHORT;
            }
            case 7: {
                return CstType.LONG;
            }
            case 6: {
                return CstType.INTEGER;
            }
            case 5: {
                return CstType.FLOAT;
            }
            case 4: {
                return CstType.DOUBLE;
            }
            case 3: {
                return CstType.CHARACTER;
            }
            case 2: {
                return CstType.BYTE;
            }
            case 1: {
                return CstType.BOOLEAN;
            }
            case 0: {
                return CstType.VOID;
            }
        }
    }
    
    public static CstType intern(final Type type) {
        synchronized (CstType.interns) {
            CstType cstType;
            if ((cstType = CstType.interns.get(type)) == null) {
                cstType = new CstType(type);
                CstType.interns.put(type, cstType);
            }
            return cstType;
        }
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        return this.type.getDescriptor().compareTo(((CstType)constant).type.getDescriptor());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof CstType;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (this.type == ((CstType)o).type) {
            b2 = true;
        }
        return b2;
    }
    
    public Type getClassType() {
        return this.type;
    }
    
    public CstString getDescriptor() {
        if (this.descriptor == null) {
            this.descriptor = new CstString(this.type.getDescriptor());
        }
        return this.descriptor;
    }
    
    public String getPackageName() {
        final String string = this.getDescriptor().getString();
        final int lastIndex = string.lastIndexOf(47);
        final int lastIndex2 = string.lastIndexOf(91);
        if (lastIndex == -1) {
            return "default";
        }
        return string.substring(lastIndex2 + 2, lastIndex).replace('/', '.');
    }
    
    @Override
    public Type getType() {
        return Type.CLASS;
    }
    
    @Override
    public int hashCode() {
        return this.type.hashCode();
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    @Override
    public String toHuman() {
        return this.type.toHuman();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("type{");
        sb.append(this.toHuman());
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "type";
    }
}
