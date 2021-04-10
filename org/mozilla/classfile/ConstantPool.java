package org.mozilla.classfile;

import org.mozilla.javascript.*;

final class ConstantPool
{
    static final byte CONSTANT_Class = 7;
    static final byte CONSTANT_Double = 6;
    static final byte CONSTANT_Fieldref = 9;
    static final byte CONSTANT_Float = 4;
    static final byte CONSTANT_Integer = 3;
    static final byte CONSTANT_InterfaceMethodref = 11;
    static final byte CONSTANT_Long = 5;
    static final byte CONSTANT_Methodref = 10;
    static final byte CONSTANT_NameAndType = 12;
    static final byte CONSTANT_String = 8;
    static final byte CONSTANT_Utf8 = 1;
    private static final int ConstantPoolSize = 256;
    private static final int MAX_UTF_ENCODING_SIZE = 65535;
    private ClassFileWriter cfw;
    private ObjToIntMap itsClassHash;
    private UintMap itsConstantData;
    private ObjToIntMap itsFieldRefHash;
    private ObjToIntMap itsMethodRefHash;
    private byte[] itsPool;
    private UintMap itsPoolTypes;
    private UintMap itsStringConstHash;
    private int itsTop;
    private int itsTopIndex;
    private ObjToIntMap itsUtf8Hash;
    
    ConstantPool(final ClassFileWriter cfw) {
        this.itsStringConstHash = new UintMap();
        this.itsUtf8Hash = new ObjToIntMap();
        this.itsFieldRefHash = new ObjToIntMap();
        this.itsMethodRefHash = new ObjToIntMap();
        this.itsClassHash = new ObjToIntMap();
        this.itsConstantData = new UintMap();
        this.itsPoolTypes = new UintMap();
        this.cfw = cfw;
        this.itsTopIndex = 1;
        this.itsPool = new byte[256];
        this.itsTop = 0;
    }
    
    private short addNameAndType(final String s, final String s2) {
        final short addUtf8 = this.addUtf8(s);
        final short addUtf9 = this.addUtf8(s2);
        this.ensure(5);
        this.itsPool[this.itsTop++] = 12;
        this.itsTop = ClassFileWriter.putInt16(addUtf8, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(addUtf9, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 12);
        return (short)(this.itsTopIndex++);
    }
    
    short addClass(final String s) {
        int value2;
        int value = value2 = this.itsClassHash.get(s, -1);
        if (value == -1) {
            String s2 = s;
            if (s.indexOf(46) > 0) {
                final String slashedForm = ClassFileWriter.getSlashedForm(s);
                final int n = value = this.itsClassHash.get(slashedForm, -1);
                s2 = slashedForm;
                if (n != -1) {
                    this.itsClassHash.put(s, n);
                    s2 = slashedForm;
                    value = n;
                }
            }
            if ((value2 = value) == -1) {
                final short addUtf8 = this.addUtf8(s2);
                this.ensure(3);
                this.itsPool[this.itsTop++] = 7;
                this.itsTop = ClassFileWriter.putInt16(addUtf8, this.itsPool, this.itsTop);
                final int n2 = this.itsTopIndex++;
                this.itsClassHash.put(s2, n2);
                value2 = n2;
                if (s != s2) {
                    this.itsClassHash.put(s, n2);
                    value2 = n2;
                }
            }
        }
        this.setConstantData(value2, s);
        this.itsPoolTypes.put(value2, 7);
        return (short)value2;
    }
    
    int addConstant(final double n) {
        this.ensure(9);
        this.itsPool[this.itsTop++] = 6;
        this.itsTop = ClassFileWriter.putInt64(Double.doubleToLongBits(n), this.itsPool, this.itsTop);
        final int itsTopIndex = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(itsTopIndex, 6);
        return itsTopIndex;
    }
    
    int addConstant(final float n) {
        this.ensure(5);
        this.itsPool[this.itsTop++] = 4;
        this.itsTop = ClassFileWriter.putInt32(Float.floatToIntBits(n), this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 4);
        return this.itsTopIndex++;
    }
    
    int addConstant(int n) {
        this.ensure(5);
        this.itsPool[this.itsTop++] = 3;
        this.itsTop = ClassFileWriter.putInt32(n, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 3);
        n = this.itsTopIndex++;
        return (short)n;
    }
    
    int addConstant(final long n) {
        this.ensure(9);
        this.itsPool[this.itsTop++] = 5;
        this.itsTop = ClassFileWriter.putInt64(n, this.itsPool, this.itsTop);
        final int itsTopIndex = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(itsTopIndex, 5);
        return itsTopIndex;
    }
    
    int addConstant(final String s) {
        final int n = this.addUtf8(s) & 0xFFFF;
        int int1;
        if ((int1 = this.itsStringConstHash.getInt(n, -1)) == -1) {
            int1 = this.itsTopIndex++;
            this.ensure(3);
            this.itsPool[this.itsTop++] = 8;
            this.itsTop = ClassFileWriter.putInt16(n, this.itsPool, this.itsTop);
            this.itsStringConstHash.put(n, int1);
        }
        this.itsPoolTypes.put(int1, 8);
        return int1;
    }
    
    short addFieldRef(final String s, final String s2, final String s3) {
        final FieldOrMethodRef fieldOrMethodRef = new FieldOrMethodRef(s, s2, s3);
        int value;
        if ((value = this.itsFieldRefHash.get(fieldOrMethodRef, -1)) == -1) {
            final short addNameAndType = this.addNameAndType(s2, s3);
            final short addClass = this.addClass(s);
            this.ensure(5);
            this.itsPool[this.itsTop++] = 9;
            this.itsTop = ClassFileWriter.putInt16(addClass, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(addNameAndType, this.itsPool, this.itsTop);
            value = this.itsTopIndex++;
            this.itsFieldRefHash.put(fieldOrMethodRef, value);
        }
        this.setConstantData(value, fieldOrMethodRef);
        this.itsPoolTypes.put(value, 9);
        return (short)value;
    }
    
    short addInterfaceMethodRef(final String s, final String s2, final String s3) {
        final short addNameAndType = this.addNameAndType(s2, s3);
        final short addClass = this.addClass(s);
        this.ensure(5);
        this.itsPool[this.itsTop++] = 11;
        this.itsTop = ClassFileWriter.putInt16(addClass, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(addNameAndType, this.itsPool, this.itsTop);
        this.setConstantData(this.itsTopIndex, new FieldOrMethodRef(s, s2, s3));
        this.itsPoolTypes.put(this.itsTopIndex, 11);
        return (short)(this.itsTopIndex++);
    }
    
    short addMethodRef(final String s, final String s2, final String s3) {
        final FieldOrMethodRef fieldOrMethodRef = new FieldOrMethodRef(s, s2, s3);
        int value;
        if ((value = this.itsMethodRefHash.get(fieldOrMethodRef, -1)) == -1) {
            final short addNameAndType = this.addNameAndType(s2, s3);
            final short addClass = this.addClass(s);
            this.ensure(5);
            this.itsPool[this.itsTop++] = 10;
            this.itsTop = ClassFileWriter.putInt16(addClass, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(addNameAndType, this.itsPool, this.itsTop);
            value = this.itsTopIndex++;
            this.itsMethodRefHash.put(fieldOrMethodRef, value);
        }
        this.setConstantData(value, fieldOrMethodRef);
        this.itsPoolTypes.put(value, 10);
        return (short)value;
    }
    
    short addUtf8(final String s) {
        int value;
        final int n = value = this.itsUtf8Hash.get(s, -1);
        if (n == -1) {
            final int length = s.length();
            boolean b2;
            if (length > 65535) {
                final boolean b = true;
                value = n;
                b2 = b;
            }
            else {
                this.ensure(length * 3 + 3);
                final int itsTop = this.itsTop;
                this.itsPool[itsTop] = 1;
                int itsTop2 = itsTop + 1 + 2;
                final char[] charBuffer = this.cfw.getCharBuffer(length);
                int i = 0;
                s.getChars(0, length, charBuffer, 0);
                while (i != length) {
                    final char c = charBuffer[i];
                    if (c != '\0' && c <= '\u007f') {
                        final byte[] itsPool = this.itsPool;
                        final int n2 = itsTop2 + 1;
                        itsPool[itsTop2] = (byte)c;
                        itsTop2 = n2;
                    }
                    else if (c > '\u07ff') {
                        final byte[] itsPool2 = this.itsPool;
                        final int n3 = itsTop2 + 1;
                        itsPool2[itsTop2] = (byte)(c >> 12 | 0xE0);
                        final byte[] itsPool3 = this.itsPool;
                        final int n4 = n3 + 1;
                        itsPool3[n3] = (byte)((c >> 6 & 0x3F) | 0x80);
                        final byte[] itsPool4 = this.itsPool;
                        itsTop2 = n4 + 1;
                        itsPool4[n4] = (byte)((c & '?') | 0x80);
                    }
                    else {
                        final byte[] itsPool5 = this.itsPool;
                        final int n5 = itsTop2 + 1;
                        itsPool5[itsTop2] = (byte)(c >> 6 | 0xC0);
                        this.itsPool[n5] = (byte)((c & '?') | 0x80);
                        itsTop2 = n5 + 1;
                    }
                    ++i;
                }
                final int n6 = itsTop2 - (this.itsTop + 1 + 2);
                if (n6 > 65535) {
                    final boolean b3 = true;
                    value = n;
                    b2 = b3;
                }
                else {
                    this.itsPool[this.itsTop + 1] = (byte)(n6 >>> 8);
                    this.itsPool[this.itsTop + 2] = (byte)n6;
                    this.itsTop = itsTop2;
                    value = this.itsTopIndex++;
                    this.itsUtf8Hash.put(s, value);
                    b2 = false;
                }
            }
            if (b2) {
                throw new IllegalArgumentException("Too big string");
            }
        }
        this.setConstantData(value, s);
        this.itsPoolTypes.put(value, 1);
        return (short)value;
    }
    
    void ensure(final int n) {
        if (this.itsTop + n > this.itsPool.length) {
            int n2;
            if (this.itsTop + n > (n2 = this.itsPool.length * 2)) {
                n2 = this.itsTop + n;
            }
            final byte[] itsPool = new byte[n2];
            System.arraycopy(this.itsPool, 0, itsPool, 0, this.itsTop);
            this.itsPool = itsPool;
        }
    }
    
    Object getConstantData(final int n) {
        return this.itsConstantData.getObject(n);
    }
    
    byte getConstantType(final int n) {
        return (byte)this.itsPoolTypes.getInt(n, 0);
    }
    
    int getUtfEncodingLimit(final String s, int n, final int n2) {
        if ((n2 - n) * 3 <= 65535) {
            return n2;
        }
        final int n3 = 65535;
        int i = n;
        n = n3;
        while (i != n2) {
            final char char1 = s.charAt(i);
            if (char1 != '\0' && char1 <= '\u007f') {
                --n;
            }
            else if (char1 < '\u07ff') {
                n -= 2;
            }
            else {
                n -= 3;
            }
            if (n < 0) {
                return i;
            }
            ++i;
        }
        return n2;
    }
    
    int getWriteSize() {
        return this.itsTop + 2;
    }
    
    boolean isUnderUtfEncodingLimit(final String s) {
        final int length = s.length();
        if (length * 3 <= 65535) {
            return true;
        }
        boolean b = false;
        if (length > 65535) {
            return false;
        }
        if (length == this.getUtfEncodingLimit(s, 0, length)) {
            b = true;
        }
        return b;
    }
    
    void setConstantData(final int n, final Object o) {
        this.itsConstantData.put(n, o);
    }
    
    int write(final byte[] array, int putInt16) {
        putInt16 = ClassFileWriter.putInt16((short)this.itsTopIndex, array, putInt16);
        System.arraycopy(this.itsPool, 0, array, putInt16, this.itsTop);
        return putInt16 + this.itsTop;
    }
}
