package org.mozilla.classfile;

final class TypeInfo
{
    static final int DOUBLE = 3;
    static final int FLOAT = 2;
    static final int INTEGER = 1;
    static final int LONG = 4;
    static final int NULL = 5;
    static final int OBJECT_TAG = 7;
    static final int TOP = 0;
    static final int UNINITIALIZED_THIS = 6;
    static final int UNINITIALIZED_VAR_TAG = 8;
    
    private TypeInfo() {
    }
    
    static final int OBJECT(final int n) {
        return (0xFFFF & n) << 8 | 0x7;
    }
    
    static final int OBJECT(final String s, final ConstantPool constantPool) {
        return OBJECT(constantPool.addClass(s));
    }
    
    static final int UNINITIALIZED_VARIABLE(final int n) {
        return (0xFFFF & n) << 8 | 0x8;
    }
    
    static final int fromType(final String s, final ConstantPool constantPool) {
        if (s.length() != 1) {
            return OBJECT(s, constantPool);
        }
        final char char1 = s.charAt(0);
        if (char1 != 'F') {
            Label_0098: {
                if (char1 != 'S' && char1 != 'Z') {
                    switch (char1) {
                        default: {
                            switch (char1) {
                                default: {
                                    throw new IllegalArgumentException("bad type");
                                }
                                case 74: {
                                    return 4;
                                }
                                case 73: {
                                    break Label_0098;
                                }
                            }
                            break;
                        }
                        case 68: {
                            return 3;
                        }
                        case 66:
                        case 67: {
                            break;
                        }
                    }
                }
            }
            return 1;
        }
        return 2;
    }
    
    static Class<?> getClassFromInternalName(final String s) {
        try {
            return Class.forName(s.replace('/', '.'));
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    static final int getPayload(final int n) {
        return n >>> 8;
    }
    
    static final String getPayloadAsType(final int n, final ConstantPool constantPool) {
        if (getTag(n) == 7) {
            return (String)constantPool.getConstantData(getPayload(n));
        }
        throw new IllegalArgumentException("expecting object type");
    }
    
    static final int getTag(final int n) {
        return n & 0xFF;
    }
    
    static boolean isTwoWords(final int n) {
        return n == 3 || n == 4;
    }
    
    static int merge(final int n, final int n2, final ConstantPool constantPool) {
        final int tag = getTag(n);
        final int tag2 = getTag(n2);
        boolean b = true;
        final boolean b2 = tag == 7;
        if (tag2 != 7) {
            b = false;
        }
        if (n == n2) {
            return n;
        }
        if (b2 && n2 == 5) {
            return n;
        }
        if (tag == 0) {
            return 0;
        }
        if (tag2 == 0) {
            return 0;
        }
        if (n == 5 && b) {
            return n2;
        }
        if (b2 && b) {
            final String payloadAsType = getPayloadAsType(n, constantPool);
            final String payloadAsType2 = getPayloadAsType(n2, constantPool);
            final String s = (String)constantPool.getConstantData(2);
            final String s2 = (String)constantPool.getConstantData(4);
            String s3 = payloadAsType;
            if (payloadAsType.equals(s)) {
                s3 = s2;
            }
            String s4 = payloadAsType2;
            if (payloadAsType2.equals(s)) {
                s4 = s2;
            }
            final Class<?> classFromInternalName = getClassFromInternalName(s3);
            final Class<?> classFromInternalName2 = getClassFromInternalName(s4);
            if (classFromInternalName.isAssignableFrom(classFromInternalName2)) {
                return n;
            }
            if (classFromInternalName2.isAssignableFrom(classFromInternalName)) {
                return n2;
            }
            if (classFromInternalName2.isInterface() || classFromInternalName.isInterface()) {
                return OBJECT("java/lang/Object", constantPool);
            }
            for (Class<?> clazz = classFromInternalName2.getSuperclass(); clazz != null; clazz = clazz.getSuperclass()) {
                if (clazz.isAssignableFrom(classFromInternalName)) {
                    return OBJECT(ClassFileWriter.getSlashedForm(clazz.getName()), constantPool);
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("bad merge attempt between ");
        sb.append(toString(n, constantPool));
        sb.append(" and ");
        sb.append(toString(n2, constantPool));
        throw new IllegalArgumentException(sb.toString());
    }
    
    static void print(final int[] array, final int n, final int[] array2, final int n2, final ConstantPool constantPool) {
        System.out.print("locals: ");
        System.out.println(toString(array, n, constantPool));
        System.out.print("stack: ");
        System.out.println(toString(array2, n2, constantPool));
        System.out.println();
    }
    
    static void print(final int[] array, final int[] array2, final ConstantPool constantPool) {
        print(array, array.length, array2, array2.length, constantPool);
    }
    
    static String toString(final int n, final ConstantPool constantPool) {
        final int tag = getTag(n);
        switch (tag) {
            default: {
                if (tag == 7) {
                    return getPayloadAsType(n, constantPool);
                }
                if (tag == 8) {
                    return "uninitialized";
                }
                throw new IllegalArgumentException("bad type");
            }
            case 6: {
                return "uninitialized_this";
            }
            case 5: {
                return "null";
            }
            case 4: {
                return "long";
            }
            case 3: {
                return "double";
            }
            case 2: {
                return "float";
            }
            case 1: {
                return "int";
            }
            case 0: {
                return "top";
            }
        }
    }
    
    static String toString(final int[] array, final int n, final ConstantPool constantPool) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(toString(array[i], constantPool));
        }
        sb.append("]");
        return sb.toString();
    }
    
    static String toString(final int[] array, final ConstantPool constantPool) {
        return toString(array, array.length, constantPool);
    }
}
