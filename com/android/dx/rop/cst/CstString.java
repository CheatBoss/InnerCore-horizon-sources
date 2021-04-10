package com.android.dx.rop.cst;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;

public final class CstString extends TypedConstant
{
    public static final CstString EMPTY_STRING;
    private final ByteArray bytes;
    private final String string;
    
    static {
        EMPTY_STRING = new CstString("");
    }
    
    public CstString(final ByteArray bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        this.bytes = bytes;
        this.string = utf8BytesToString(bytes).intern();
    }
    
    public CstString(final String s) {
        if (s == null) {
            throw new NullPointerException("string == null");
        }
        this.string = s.intern();
        this.bytes = new ByteArray(stringToUtf8Bytes(s));
    }
    
    public static byte[] stringToUtf8Bytes(final String s) {
        final int length = s.length();
        final byte[] array = new byte[length * 3];
        int n = 0;
        for (int i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\0' && char1 < '\u0080') {
                array[n] = (byte)char1;
                ++n;
            }
            else if (char1 < '\u0800') {
                array[n] = (byte)((char1 >> 6 & 0x1F) | 0xC0);
                array[n + 1] = (byte)(0x80 | (char1 & '?'));
                n += 2;
            }
            else {
                array[n] = (byte)((char1 >> 12 & 0xF) | 0xE0);
                array[n + 1] = (byte)((char1 >> 6 & 0x3F) | 0x80);
                array[n + 2] = (byte)(0x80 | (char1 & '?'));
                n += 3;
            }
        }
        final byte[] array2 = new byte[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static String throwBadUtf8(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bad utf-8 byte ");
        sb.append(Hex.u1(n));
        sb.append(" at offset ");
        sb.append(Hex.u4(n2));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static String utf8BytesToString(final ByteArray byteArray) {
        int i = byteArray.size();
        final char[] array = new char[i];
        int n = 0;
        int n2 = 0;
        while (i > 0) {
            final int unsignedByte = byteArray.getUnsignedByte(n2);
            final int n3 = unsignedByte >> 4;
            char c = '\0';
            Label_0361: {
                switch (n3) {
                    default: {
                        switch (n3) {
                            default: {
                                return throwBadUtf8(unsignedByte, n2);
                            }
                            case 14: {
                                i -= 3;
                                if (i < 0) {
                                    return throwBadUtf8(unsignedByte, n2);
                                }
                                final int unsignedByte2 = byteArray.getUnsignedByte(n2 + 1);
                                if ((unsignedByte2 & 0xC0) != 0x80) {
                                    return throwBadUtf8(unsignedByte2, n2 + 1);
                                }
                                final int unsignedByte3 = byteArray.getUnsignedByte(n2 + 2);
                                if ((unsignedByte2 & 0xC0) != 0x80) {
                                    return throwBadUtf8(unsignedByte3, n2 + 2);
                                }
                                final int n4 = (unsignedByte & 0xF) << 12 | (unsignedByte2 & 0x3F) << 6 | (unsignedByte3 & 0x3F);
                                if (n4 < 2048) {
                                    return throwBadUtf8(unsignedByte3, n2 + 2);
                                }
                                c = (char)n4;
                                n2 += 3;
                                break Label_0361;
                            }
                            case 12:
                            case 13: {
                                i -= 2;
                                if (i < 0) {
                                    return throwBadUtf8(unsignedByte, n2);
                                }
                                final int unsignedByte4 = byteArray.getUnsignedByte(n2 + 1);
                                if ((unsignedByte4 & 0xC0) != 0x80) {
                                    return throwBadUtf8(unsignedByte4, n2 + 1);
                                }
                                final int n5 = (unsignedByte & 0x1F) << 6 | (unsignedByte4 & 0x3F);
                                if (n5 != 0 && n5 < 128) {
                                    return throwBadUtf8(unsignedByte4, n2 + 1);
                                }
                                c = (char)n5;
                                n2 += 2;
                                break Label_0361;
                            }
                        }
                        break;
                    }
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7: {
                        --i;
                        if (unsignedByte == 0) {
                            return throwBadUtf8(unsignedByte, n2);
                        }
                        c = (char)unsignedByte;
                        ++n2;
                        break;
                    }
                }
            }
            array[n] = c;
            ++n;
        }
        return new String(array, 0, n);
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        return this.string.compareTo(((CstString)constant).string);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CstString && this.string.equals(((CstString)o).string);
    }
    
    public ByteArray getBytes() {
        return this.bytes;
    }
    
    public String getString() {
        return this.string;
    }
    
    @Override
    public Type getType() {
        return Type.STRING;
    }
    
    public int getUtf16Size() {
        return this.string.length();
    }
    
    public int getUtf8Size() {
        return this.bytes.size();
    }
    
    @Override
    public int hashCode() {
        return this.string.hashCode();
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    @Override
    public String toHuman() {
        final int length = this.string.length();
        final StringBuilder sb = new StringBuilder(length * 3 / 2);
        for (int i = 0; i < length; ++i) {
            final char char1 = this.string.charAt(i);
            if (char1 >= ' ' && char1 < '\u007f') {
                if (char1 == '\'' || char1 == '\"' || char1 == '\\') {
                    sb.append('\\');
                }
                sb.append(char1);
            }
            else if (char1 <= '\u007f') {
                if (char1 != '\r') {
                    switch (char1) {
                        default: {
                            char char2;
                            if (i < length - 1) {
                                char2 = this.string.charAt(i + 1);
                            }
                            else {
                                char2 = '\0';
                            }
                            final boolean b = char2 >= '0' && char2 <= '7';
                            sb.append('\\');
                            final int n = 6;
                            int n2 = b ? 1 : 0;
                            int n3;
                            for (int j = n; j >= 0; j -= 3, n2 = n3) {
                                final char c = (char)((char1 >> j & 0x7) + 48);
                                if (c != '0' || (n3 = n2) != 0) {
                                    sb.append(c);
                                    n3 = 1;
                                }
                            }
                            if (n2 == 0) {
                                sb.append('0');
                                break;
                            }
                            break;
                        }
                        case '\n': {
                            sb.append("\\n");
                            break;
                        }
                        case '\t': {
                            sb.append("\\t");
                            break;
                        }
                    }
                }
                else {
                    sb.append("\\r");
                }
            }
            else {
                sb.append("\\u");
                sb.append(Character.forDigit(char1 >> 12, 16));
                sb.append(Character.forDigit(char1 >> 8 & 0xF, 16));
                sb.append(Character.forDigit(char1 >> 4 & 0xF, 16));
                sb.append(Character.forDigit(char1 & '\u000f', 16));
            }
        }
        return sb.toString();
    }
    
    public String toQuoted() {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(this.toHuman());
        sb.append('\"');
        return sb.toString();
    }
    
    public String toQuoted(final int n) {
        String s = this.toHuman();
        String s2;
        if (s.length() <= n - 2) {
            s2 = "";
        }
        else {
            s = s.substring(0, n - 5);
            s2 = "...";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(s);
        sb.append(s2);
        sb.append('\"');
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("string{\"");
        sb.append(this.toHuman());
        sb.append("\"}");
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "utf8";
    }
}
