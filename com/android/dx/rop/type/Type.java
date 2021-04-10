package com.android.dx.rop.type;

import java.util.*;
import java.io.*;
import com.android.dx.util.*;

public final class Type implements TypeBearer, Comparable<Type>
{
    public static final Type ANNOTATION;
    public static final Type BOOLEAN;
    public static final Type BOOLEAN_ARRAY;
    public static final Type BOOLEAN_CLASS;
    public static final int BT_ADDR = 10;
    public static final int BT_BOOLEAN = 1;
    public static final int BT_BYTE = 2;
    public static final int BT_CHAR = 3;
    public static final int BT_COUNT = 11;
    public static final int BT_DOUBLE = 4;
    public static final int BT_FLOAT = 5;
    public static final int BT_INT = 6;
    public static final int BT_LONG = 7;
    public static final int BT_OBJECT = 9;
    public static final int BT_SHORT = 8;
    public static final int BT_VOID = 0;
    public static final Type BYTE;
    public static final Type BYTE_ARRAY;
    public static final Type BYTE_CLASS;
    public static final Type CHAR;
    public static final Type CHARACTER_CLASS;
    public static final Type CHAR_ARRAY;
    public static final Type CLASS;
    public static final Type CLONEABLE;
    public static final Type DOUBLE;
    public static final Type DOUBLE_ARRAY;
    public static final Type DOUBLE_CLASS;
    public static final Type FLOAT;
    public static final Type FLOAT_ARRAY;
    public static final Type FLOAT_CLASS;
    public static final Type INT;
    public static final Type INTEGER_CLASS;
    public static final Type INT_ARRAY;
    public static final Type KNOWN_NULL;
    public static final Type LONG;
    public static final Type LONG_ARRAY;
    public static final Type LONG_CLASS;
    public static final Type OBJECT;
    public static final Type OBJECT_ARRAY;
    public static final Type RETURN_ADDRESS;
    public static final Type SERIALIZABLE;
    public static final Type SHORT;
    public static final Type SHORT_ARRAY;
    public static final Type SHORT_CLASS;
    public static final Type STRING;
    public static final Type THROWABLE;
    public static final Type VOID;
    public static final Type VOID_CLASS;
    private static final HashMap<String, Type> internTable;
    private Type arrayType;
    private final int basicType;
    private String className;
    private Type componentType;
    private final String descriptor;
    private Type initializedType;
    private final int newAt;
    
    static {
        internTable = new HashMap<String, Type>(500);
        BOOLEAN = new Type("Z", 1);
        BYTE = new Type("B", 2);
        CHAR = new Type("C", 3);
        DOUBLE = new Type("D", 4);
        FLOAT = new Type("F", 5);
        INT = new Type("I", 6);
        LONG = new Type("J", 7);
        SHORT = new Type("S", 8);
        VOID = new Type("V", 0);
        KNOWN_NULL = new Type("<null>", 9);
        RETURN_ADDRESS = new Type("<addr>", 10);
        putIntern(Type.BOOLEAN);
        putIntern(Type.BYTE);
        putIntern(Type.CHAR);
        putIntern(Type.DOUBLE);
        putIntern(Type.FLOAT);
        putIntern(Type.INT);
        putIntern(Type.LONG);
        putIntern(Type.SHORT);
        ANNOTATION = intern("Ljava/lang/annotation/Annotation;");
        CLASS = intern("Ljava/lang/Class;");
        CLONEABLE = intern("Ljava/lang/Cloneable;");
        OBJECT = intern("Ljava/lang/Object;");
        SERIALIZABLE = intern("Ljava/io/Serializable;");
        STRING = intern("Ljava/lang/String;");
        THROWABLE = intern("Ljava/lang/Throwable;");
        BOOLEAN_CLASS = intern("Ljava/lang/Boolean;");
        BYTE_CLASS = intern("Ljava/lang/Byte;");
        CHARACTER_CLASS = intern("Ljava/lang/Character;");
        DOUBLE_CLASS = intern("Ljava/lang/Double;");
        FLOAT_CLASS = intern("Ljava/lang/Float;");
        INTEGER_CLASS = intern("Ljava/lang/Integer;");
        LONG_CLASS = intern("Ljava/lang/Long;");
        SHORT_CLASS = intern("Ljava/lang/Short;");
        VOID_CLASS = intern("Ljava/lang/Void;");
        BOOLEAN_ARRAY = Type.BOOLEAN.getArrayType();
        BYTE_ARRAY = Type.BYTE.getArrayType();
        CHAR_ARRAY = Type.CHAR.getArrayType();
        DOUBLE_ARRAY = Type.DOUBLE.getArrayType();
        FLOAT_ARRAY = Type.FLOAT.getArrayType();
        INT_ARRAY = Type.INT.getArrayType();
        LONG_ARRAY = Type.LONG.getArrayType();
        OBJECT_ARRAY = Type.OBJECT.getArrayType();
        SHORT_ARRAY = Type.SHORT.getArrayType();
    }
    
    private Type(final String s, final int n) {
        this(s, n, -1);
    }
    
    private Type(final String descriptor, final int basicType, final int newAt) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        if (basicType < 0 || basicType >= 11) {
            throw new IllegalArgumentException("bad basicType");
        }
        if (newAt < -1) {
            throw new IllegalArgumentException("newAt < -1");
        }
        this.descriptor = descriptor;
        this.basicType = basicType;
        this.newAt = newAt;
        this.arrayType = null;
        this.componentType = null;
        this.initializedType = null;
    }
    
    public static Type intern(final String s) {
        Serializable internTable = Type.internTable;
        synchronized (internTable) {
            final Type type = Type.internTable.get(s);
            // monitorexit(internTable)
            if (type != null) {
                return type;
            }
            try {
                final char char1 = s.charAt(0);
                if (char1 == '[') {
                    return intern(s.substring(1)).getArrayType();
                }
                final int length = s.length();
                if (char1 == 'L' && s.charAt(length - 1) == ';') {
                    for (int i = 1; i < length - 1; ++i) {
                        switch (s.charAt(i)) {
                            case '/': {
                                if (i == 1 || i == length - 1 || s.charAt(i - 1) == '/') {
                                    internTable = new StringBuilder();
                                    ((StringBuilder)internTable).append("bad descriptor: ");
                                    ((StringBuilder)internTable).append(s);
                                    throw new IllegalArgumentException(((StringBuilder)internTable).toString());
                                }
                                break;
                            }
                            case '(':
                            case ')':
                            case '.':
                            case ';':
                            case '[': {
                                internTable = new StringBuilder();
                                ((StringBuilder)internTable).append("bad descriptor: ");
                                ((StringBuilder)internTable).append(s);
                                throw new IllegalArgumentException(((StringBuilder)internTable).toString());
                            }
                        }
                    }
                    return putIntern(new Type(s, 9));
                }
                internTable = new StringBuilder();
                ((StringBuilder)internTable).append("bad descriptor: ");
                ((StringBuilder)internTable).append(s);
                throw new IllegalArgumentException(((StringBuilder)internTable).toString());
            }
            catch (NullPointerException ex) {
                throw new NullPointerException("descriptor == null");
            }
            catch (IndexOutOfBoundsException ex2) {
                throw new IllegalArgumentException("descriptor is empty");
            }
        }
    }
    
    public static Type internClassName(final String s) {
        if (s == null) {
            throw new NullPointerException("name == null");
        }
        if (s.startsWith("[")) {
            return intern(s);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('L');
        sb.append(s);
        sb.append(';');
        return intern(sb.toString());
    }
    
    public static Type internReturnType(final String s) {
        try {
            if (s.equals("V")) {
                return Type.VOID;
            }
            return intern(s);
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("descriptor == null");
        }
    }
    
    private static Type putIntern(final Type type) {
        synchronized (Type.internTable) {
            final String descriptor = type.getDescriptor();
            final Type type2 = Type.internTable.get(descriptor);
            if (type2 != null) {
                return type2;
            }
            Type.internTable.put(descriptor, type);
            return type;
        }
    }
    
    public Type asUninitialized(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("newAt < 0");
        }
        if (!this.isReference()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not a reference type: ");
            sb.append(this.descriptor);
            throw new IllegalArgumentException(sb.toString());
        }
        if (this.isUninitialized()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("already uninitialized: ");
            sb2.append(this.descriptor);
            throw new IllegalArgumentException(sb2.toString());
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append('N');
        sb3.append(Hex.u2(n));
        sb3.append(this.descriptor);
        final Type type = new Type(sb3.toString(), 9, n);
        type.initializedType = this;
        return putIntern(type);
    }
    
    @Override
    public int compareTo(final Type type) {
        return this.descriptor.compareTo(type.descriptor);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof Type && this.descriptor.equals(((Type)o).descriptor));
    }
    
    public Type getArrayType() {
        if (this.arrayType == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(this.descriptor);
            this.arrayType = putIntern(new Type(sb.toString(), 9));
        }
        return this.arrayType;
    }
    
    @Override
    public int getBasicFrameType() {
        final int basicType = this.basicType;
        if (basicType != 6 && basicType != 8) {
            switch (basicType) {
                default: {
                    return this.basicType;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return 6;
    }
    
    @Override
    public int getBasicType() {
        return this.basicType;
    }
    
    public int getCategory() {
        final int basicType = this.basicType;
        if (basicType != 4 && basicType != 7) {
            return 1;
        }
        return 2;
    }
    
    public String getClassName() {
        if (this.className == null) {
            if (!this.isReference()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("not an object type: ");
                sb.append(this.descriptor);
                throw new IllegalArgumentException(sb.toString());
            }
            if (this.descriptor.charAt(0) == '[') {
                this.className = this.descriptor;
            }
            else {
                this.className = this.descriptor.substring(1, this.descriptor.length() - 1);
            }
        }
        return this.className;
    }
    
    public Type getComponentType() {
        if (this.componentType == null) {
            if (this.descriptor.charAt(0) != '[') {
                final StringBuilder sb = new StringBuilder();
                sb.append("not an array type: ");
                sb.append(this.descriptor);
                throw new IllegalArgumentException(sb.toString());
            }
            this.componentType = intern(this.descriptor.substring(1));
        }
        return this.componentType;
    }
    
    public String getDescriptor() {
        return this.descriptor;
    }
    
    @Override
    public Type getFrameType() {
        final int basicType = this.basicType;
        if (basicType != 6 && basicType != 8) {
            switch (basicType) {
                default: {
                    return this;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return Type.INT;
    }
    
    public Type getInitializedType() {
        if (this.initializedType == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("initialized type: ");
            sb.append(this.descriptor);
            throw new IllegalArgumentException(sb.toString());
        }
        return this.initializedType;
    }
    
    public int getNewAt() {
        return this.newAt;
    }
    
    @Override
    public Type getType() {
        return this;
    }
    
    @Override
    public int hashCode() {
        return this.descriptor.hashCode();
    }
    
    public boolean isArray() {
        final String descriptor = this.descriptor;
        boolean b = false;
        if (descriptor.charAt(0) == '[') {
            b = true;
        }
        return b;
    }
    
    public boolean isArrayOrKnownNull() {
        return this.isArray() || this.equals(Type.KNOWN_NULL);
    }
    
    public boolean isCategory1() {
        final int basicType = this.basicType;
        return basicType != 4 && basicType != 7;
    }
    
    public boolean isCategory2() {
        final int basicType = this.basicType;
        return basicType == 4 || basicType == 7;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    public boolean isIntlike() {
        final int basicType = this.basicType;
        if (basicType != 6 && basicType != 8) {
            switch (basicType) {
                default: {
                    return false;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return true;
    }
    
    public boolean isPrimitive() {
        switch (this.basicType) {
            default: {
                return false;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                return true;
            }
        }
    }
    
    public boolean isReference() {
        return this.basicType == 9;
    }
    
    public boolean isUninitialized() {
        return this.newAt >= 0;
    }
    
    @Override
    public String toHuman() {
        switch (this.basicType) {
            default: {
                return this.descriptor;
            }
            case 9: {
                if (this.isArray()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getComponentType().toHuman());
                    sb.append("[]");
                    return sb.toString();
                }
                return this.getClassName().replace("/", ".");
            }
            case 8: {
                return "short";
            }
            case 7: {
                return "long";
            }
            case 6: {
                return "int";
            }
            case 5: {
                return "float";
            }
            case 4: {
                return "double";
            }
            case 3: {
                return "char";
            }
            case 2: {
                return "byte";
            }
            case 1: {
                return "boolean";
            }
            case 0: {
                return "void";
            }
        }
    }
    
    @Override
    public String toString() {
        return this.descriptor;
    }
}
