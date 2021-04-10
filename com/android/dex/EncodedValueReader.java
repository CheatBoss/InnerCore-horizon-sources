package com.android.dex;

import com.android.dex.util.*;

public final class EncodedValueReader
{
    public static final int ENCODED_ANNOTATION = 29;
    public static final int ENCODED_ARRAY = 28;
    public static final int ENCODED_BOOLEAN = 31;
    public static final int ENCODED_BYTE = 0;
    public static final int ENCODED_CHAR = 3;
    public static final int ENCODED_DOUBLE = 17;
    public static final int ENCODED_ENUM = 27;
    public static final int ENCODED_FIELD = 25;
    public static final int ENCODED_FLOAT = 16;
    public static final int ENCODED_INT = 4;
    public static final int ENCODED_LONG = 6;
    public static final int ENCODED_METHOD = 26;
    public static final int ENCODED_NULL = 30;
    public static final int ENCODED_SHORT = 2;
    public static final int ENCODED_STRING = 23;
    public static final int ENCODED_TYPE = 24;
    private static final int MUST_READ = -1;
    private int annotationType;
    private int arg;
    protected final ByteInput in;
    private int type;
    
    public EncodedValueReader(final EncodedValue encodedValue) {
        this(encodedValue.asByteInput());
    }
    
    public EncodedValueReader(final EncodedValue encodedValue, final int n) {
        this(encodedValue.asByteInput(), n);
    }
    
    public EncodedValueReader(final ByteInput in) {
        this.type = -1;
        this.in = in;
    }
    
    public EncodedValueReader(final ByteInput in, final int type) {
        this.type = -1;
        this.in = in;
        this.type = type;
    }
    
    private void checkType(final int n) {
        if (this.peek() != n) {
            throw new IllegalStateException(String.format("Expected %x but was %x", n, this.peek()));
        }
    }
    
    public int getAnnotationType() {
        return this.annotationType;
    }
    
    public int peek() {
        if (this.type == -1) {
            final int n = this.in.readByte() & 0xFF;
            this.type = (n & 0x1F);
            this.arg = (n & 0xE0) >> 5;
        }
        return this.type;
    }
    
    public int readAnnotation() {
        this.checkType(29);
        this.type = -1;
        this.annotationType = Leb128.readUnsignedLeb128(this.in);
        return Leb128.readUnsignedLeb128(this.in);
    }
    
    public int readAnnotationName() {
        return Leb128.readUnsignedLeb128(this.in);
    }
    
    public int readArray() {
        this.checkType(28);
        this.type = -1;
        return Leb128.readUnsignedLeb128(this.in);
    }
    
    public boolean readBoolean() {
        this.checkType(31);
        this.type = -1;
        return this.arg != 0;
    }
    
    public byte readByte() {
        this.checkType(0);
        this.type = -1;
        return (byte)EncodedValueCodec.readSignedInt(this.in, this.arg);
    }
    
    public char readChar() {
        this.checkType(3);
        this.type = -1;
        return (char)EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public double readDouble() {
        this.checkType(17);
        this.type = -1;
        return Double.longBitsToDouble(EncodedValueCodec.readUnsignedLong(this.in, this.arg, true));
    }
    
    public int readEnum() {
        this.checkType(27);
        this.type = -1;
        return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public int readField() {
        this.checkType(25);
        this.type = -1;
        return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public float readFloat() {
        this.checkType(16);
        this.type = -1;
        return Float.intBitsToFloat(EncodedValueCodec.readUnsignedInt(this.in, this.arg, true));
    }
    
    public int readInt() {
        this.checkType(4);
        this.type = -1;
        return EncodedValueCodec.readSignedInt(this.in, this.arg);
    }
    
    public long readLong() {
        this.checkType(6);
        this.type = -1;
        return EncodedValueCodec.readSignedLong(this.in, this.arg);
    }
    
    public int readMethod() {
        this.checkType(26);
        this.type = -1;
        return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public void readNull() {
        this.checkType(30);
        this.type = -1;
    }
    
    public short readShort() {
        this.checkType(2);
        this.type = -1;
        return (short)EncodedValueCodec.readSignedInt(this.in, this.arg);
    }
    
    public int readString() {
        this.checkType(23);
        this.type = -1;
        return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public int readType() {
        this.checkType(24);
        this.type = -1;
        return EncodedValueCodec.readUnsignedInt(this.in, this.arg, false);
    }
    
    public void skipValue() {
        final int peek = this.peek();
        if (peek == 0) {
            this.readByte();
            return;
        }
        if (peek == 6) {
            this.readLong();
            return;
        }
        switch (peek) {
            default: {
                switch (peek) {
                    default: {
                        switch (peek) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unexpected type: ");
                                sb.append(Integer.toHexString(this.type));
                                throw new DexException(sb.toString());
                            }
                            case 31: {
                                this.readBoolean();
                                return;
                            }
                            case 30: {
                                this.readNull();
                                return;
                            }
                            case 29: {
                                for (int i = 0; i < this.readAnnotation(); ++i) {
                                    this.readAnnotationName();
                                    this.skipValue();
                                }
                                return;
                            }
                            case 28: {
                                for (int j = 0; j < this.readArray(); ++j) {
                                    this.skipValue();
                                }
                                return;
                            }
                            case 27: {
                                this.readEnum();
                                return;
                            }
                            case 26: {
                                this.readMethod();
                                return;
                            }
                            case 25: {
                                this.readField();
                                return;
                            }
                            case 24: {
                                this.readType();
                                return;
                            }
                            case 23: {
                                this.readString();
                                return;
                            }
                        }
                        break;
                    }
                    case 17: {
                        this.readDouble();
                        return;
                    }
                    case 16: {
                        this.readFloat();
                        return;
                    }
                }
                break;
            }
            case 4: {
                this.readInt();
            }
            case 3: {
                this.readChar();
            }
            case 2: {
                this.readShort();
            }
        }
    }
}
