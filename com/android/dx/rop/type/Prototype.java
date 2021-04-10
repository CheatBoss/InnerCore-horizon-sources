package com.android.dx.rop.type;

import java.util.*;

public final class Prototype implements Comparable<Prototype>
{
    private static final HashMap<String, Prototype> internTable;
    private final String descriptor;
    private StdTypeList parameterFrameTypes;
    private final StdTypeList parameterTypes;
    private final Type returnType;
    
    static {
        internTable = new HashMap<String, Prototype>(500);
    }
    
    private Prototype(final String descriptor, final Type returnType, final StdTypeList parameterTypes) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        if (returnType == null) {
            throw new NullPointerException("returnType == null");
        }
        if (parameterTypes == null) {
            throw new NullPointerException("parameterTypes == null");
        }
        this.descriptor = descriptor;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterFrameTypes = null;
    }
    
    public static Prototype intern(final String s) {
        if (s == null) {
            throw new NullPointerException("descriptor == null");
        }
        Object o = Prototype.internTable;
        synchronized (o) {
            final Prototype prototype = Prototype.internTable.get(s);
            // monitorexit(o)
            if (prototype != null) {
                return prototype;
            }
            o = makeParameterArray(s);
            int n = 0;
            int n2 = 1;
            while (true) {
                final int n3 = n2;
                final char char1 = s.charAt(n3);
                int n4 = n3;
                char char2 = char1;
                if (char1 == ')') {
                    final Type internReturnType = Type.internReturnType(s.substring(n3 + 1));
                    final StdTypeList list = new StdTypeList(n);
                    for (int i = 0; i < n; ++i) {
                        list.set(i, o[i]);
                    }
                    return putIntern(new Prototype(s, internReturnType, list));
                }
                while (char2 == '[') {
                    ++n4;
                    char2 = s.charAt(n4);
                }
                if (char2 == 'L') {
                    final int index = s.indexOf(59, n4);
                    if (index == -1) {
                        throw new IllegalArgumentException("bad descriptor");
                    }
                    n2 = index + 1;
                }
                else {
                    n2 = n4 + 1;
                }
                o[n] = Type.intern(s.substring(n3, n2));
                ++n;
            }
        }
    }
    
    public static Prototype intern(final String s, final Type type, final boolean b, final boolean b2) {
        final Prototype intern = intern(s);
        if (b) {
            return intern;
        }
        Type uninitialized = type;
        if (b2) {
            uninitialized = type.asUninitialized(Integer.MAX_VALUE);
        }
        return intern.withFirstParameter(uninitialized);
    }
    
    public static Prototype internInts(final Type type, final int n) {
        final StringBuffer sb = new StringBuffer(100);
        sb.append('(');
        for (int i = 0; i < n; ++i) {
            sb.append('I');
        }
        sb.append(')');
        sb.append(type.getDescriptor());
        return intern(sb.toString());
    }
    
    private static Type[] makeParameterArray(final String s) {
        final int length = s.length();
        if (s.charAt(0) != '(') {
            throw new IllegalArgumentException("bad descriptor");
        }
        final int n = 0;
        int n2 = 0;
        int n3 = 1;
        int n4;
        while (true) {
            n4 = n;
            if (n3 >= length) {
                break;
            }
            final char char1 = s.charAt(n3);
            if (char1 == ')') {
                n4 = n3;
                break;
            }
            int n5 = n2;
            if (char1 >= 'A') {
                n5 = n2;
                if (char1 <= 'Z') {
                    n5 = n2 + 1;
                }
            }
            ++n3;
            n2 = n5;
        }
        if (n4 == 0 || n4 == length - 1) {
            throw new IllegalArgumentException("bad descriptor");
        }
        if (s.indexOf(41, n4 + 1) != -1) {
            throw new IllegalArgumentException("bad descriptor");
        }
        return new Type[n2];
    }
    
    private static Prototype putIntern(final Prototype prototype) {
        synchronized (Prototype.internTable) {
            final String descriptor = prototype.getDescriptor();
            final Prototype prototype2 = Prototype.internTable.get(descriptor);
            if (prototype2 != null) {
                return prototype2;
            }
            Prototype.internTable.put(descriptor, prototype);
            return prototype;
        }
    }
    
    @Override
    public int compareTo(final Prototype prototype) {
        if (this == prototype) {
            return 0;
        }
        final int compareTo = this.returnType.compareTo(prototype.returnType);
        if (compareTo != 0) {
            return compareTo;
        }
        final int size = this.parameterTypes.size();
        final int size2 = prototype.parameterTypes.size();
        for (int min = Math.min(size, size2), i = 0; i < min; ++i) {
            final int compareTo2 = this.parameterTypes.get(i).compareTo(prototype.parameterTypes.get(i));
            if (compareTo2 != 0) {
                return compareTo2;
            }
        }
        if (size < size2) {
            return -1;
        }
        if (size > size2) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof Prototype && this.descriptor.equals(((Prototype)o).descriptor));
    }
    
    public String getDescriptor() {
        return this.descriptor;
    }
    
    public StdTypeList getParameterFrameTypes() {
        if (this.parameterFrameTypes == null) {
            final int size = this.parameterTypes.size();
            final StdTypeList list = new StdTypeList(size);
            boolean b = false;
            for (int i = 0; i < size; ++i) {
                Type type;
                if ((type = this.parameterTypes.get(i)).isIntlike()) {
                    b = true;
                    type = Type.INT;
                }
                list.set(i, type);
            }
            StdTypeList parameterTypes;
            if (b) {
                parameterTypes = list;
            }
            else {
                parameterTypes = this.parameterTypes;
            }
            this.parameterFrameTypes = parameterTypes;
        }
        return this.parameterFrameTypes;
    }
    
    public StdTypeList getParameterTypes() {
        return this.parameterTypes;
    }
    
    public Type getReturnType() {
        return this.returnType;
    }
    
    @Override
    public int hashCode() {
        return this.descriptor.hashCode();
    }
    
    @Override
    public String toString() {
        return this.descriptor;
    }
    
    public Prototype withFirstParameter(final Type type) {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(type.getDescriptor());
        sb.append(this.descriptor.substring(1));
        final String string = sb.toString();
        final StdTypeList withFirst = this.parameterTypes.withFirst(type);
        withFirst.setImmutable();
        return putIntern(new Prototype(string, this.returnType, withFirst));
    }
}
