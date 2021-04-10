package okio;

import java.io.*;
import java.security.*;
import java.util.*;

public class ByteString implements Serializable, Comparable<ByteString>
{
    public static final ByteString EMPTY;
    static final char[] HEX_DIGITS;
    final byte[] data;
    transient int hashCode;
    transient String utf8;
    
    static {
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        EMPTY = of(new byte[0]);
    }
    
    ByteString(final byte[] data) {
        this.data = data;
    }
    
    static int codePointIndexToCharIndex(final String s, final int n) {
        final int length = s.length();
        int i = 0;
        int n2 = 0;
        while (i < length) {
            if (n2 == n) {
                return i;
            }
            final int codePoint = s.codePointAt(i);
            if ((Character.isISOControl(codePoint) && codePoint != 10 && codePoint != 13) || codePoint == 65533) {
                return -1;
            }
            ++n2;
            i += Character.charCount(codePoint);
        }
        return s.length();
    }
    
    public static ByteString decodeHex(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("hex == null");
        }
        if (s.length() % 2 == 0) {
            final int n = s.length() / 2;
            final byte[] array = new byte[n];
            for (int i = 0; i < n; ++i) {
                final int n2 = i * 2;
                array[i] = (byte)((decodeHexDigit(s.charAt(n2)) << 4) + decodeHexDigit(s.charAt(n2 + 1)));
            }
            return of(array);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected hex string: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static int decodeHexDigit(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'A';
        if (c >= 'a' && c <= 'f') {
            c2 = 'a';
        }
        else if (c < 'A' || c > 'F') {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected hex digit: ");
            sb.append(c);
            throw new IllegalArgumentException(sb.toString());
        }
        return c - c2 + 10;
    }
    
    private ByteString digest(final String s) {
        try {
            return of(MessageDigest.getInstance(s).digest(this.data));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public static ByteString encodeUtf8(final String utf8) {
        if (utf8 != null) {
            final ByteString byteString = new ByteString(utf8.getBytes(Util.UTF_8));
            byteString.utf8 = utf8;
            return byteString;
        }
        throw new IllegalArgumentException("s == null");
    }
    
    public static ByteString of(final byte... array) {
        if (array != null) {
            return new ByteString(array.clone());
        }
        throw new IllegalArgumentException("data == null");
    }
    
    public String base64() {
        return Base64.encode(this.data);
    }
    
    @Override
    public int compareTo(final ByteString byteString) {
        final int size = this.size();
        final int size2 = byteString.size();
        final int min = Math.min(size, size2);
        int i = 0;
        while (i < min) {
            final int n = this.getByte(i) & 0xFF;
            final int n2 = byteString.getByte(i) & 0xFF;
            if (n == n2) {
                ++i;
            }
            else {
                if (n < n2) {
                    return -1;
                }
                return 1;
            }
        }
        if (size == size2) {
            return 0;
        }
        if (size < size2) {
            return -1;
        }
        return 1;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ByteString) {
            final ByteString byteString = (ByteString)o;
            final int size = byteString.size();
            final byte[] data = this.data;
            if (size == data.length && byteString.rangeEquals(0, data, 0, data.length)) {
                return true;
            }
        }
        return false;
    }
    
    public byte getByte(final int n) {
        return this.data[n];
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        return this.hashCode = Arrays.hashCode(this.data);
    }
    
    public String hex() {
        final byte[] data = this.data;
        final char[] array = new char[data.length * 2];
        final int length = data.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final byte b = data[i];
            final int n2 = n + 1;
            final char[] hex_DIGITS = ByteString.HEX_DIGITS;
            array[n] = hex_DIGITS[b >> 4 & 0xF];
            n = n2 + 1;
            array[n2] = hex_DIGITS[b & 0xF];
            ++i;
        }
        return new String(array);
    }
    
    public boolean rangeEquals(final int n, final ByteString byteString, final int n2, final int n3) {
        return byteString.rangeEquals(n2, this.data, n, n3);
    }
    
    public boolean rangeEquals(final int n, final byte[] array, final int n2, final int n3) {
        if (n >= 0) {
            final byte[] data = this.data;
            if (n <= data.length - n3 && n2 >= 0 && n2 <= array.length - n3 && Util.arrayRangeEquals(data, n, array, n2, n3)) {
                return true;
            }
        }
        return false;
    }
    
    public ByteString sha1() {
        return this.digest("SHA-1");
    }
    
    public ByteString sha256() {
        return this.digest("SHA-256");
    }
    
    public int size() {
        return this.data.length;
    }
    
    public final boolean startsWith(final ByteString byteString) {
        return this.rangeEquals(0, byteString, 0, byteString.size());
    }
    
    public ByteString substring(final int n, final int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("beginIndex < 0");
        }
        final byte[] data = this.data;
        if (n2 > data.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("endIndex > length(");
            sb.append(this.data.length);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        final int n3 = n2 - n;
        if (n3 < 0) {
            throw new IllegalArgumentException("endIndex < beginIndex");
        }
        if (n == 0 && n2 == data.length) {
            return this;
        }
        final byte[] array = new byte[n3];
        System.arraycopy(this.data, n, array, 0, n3);
        return new ByteString(array);
    }
    
    public ByteString toAsciiLowercase() {
        int n = 0;
        while (true) {
            final byte[] data = this.data;
            if (n >= data.length) {
                return this;
            }
            final byte b = data[n];
            if (b >= 65 && b <= 90) {
                final byte[] array = data.clone();
                final int n2 = n + 1;
                array[n] = (byte)(b + 32);
                for (int i = n2; i < array.length; ++i) {
                    final byte b2 = array[i];
                    if (b2 >= 65) {
                        if (b2 <= 90) {
                            array[i] = (byte)(b2 + 32);
                        }
                    }
                }
                return new ByteString(array);
            }
            ++n;
        }
    }
    
    public byte[] toByteArray() {
        return this.data.clone();
    }
    
    @Override
    public String toString() {
        if (this.data.length == 0) {
            return "[size=0]";
        }
        final String utf8 = this.utf8();
        final int codePointIndexToCharIndex = codePointIndexToCharIndex(utf8, 64);
        while (true) {
            StringBuilder sb = null;
            String s = null;
            Label_0121: {
                if (codePointIndexToCharIndex == -1) {
                    if (this.data.length > 64) {
                        sb = new StringBuilder();
                        sb.append("[size=");
                        sb.append(this.data.length);
                        sb.append(" hex=");
                        s = this.substring(0, 64).hex();
                        break Label_0121;
                    }
                    sb = new StringBuilder();
                    sb.append("[hex=");
                    s = this.hex();
                }
                else {
                    s = utf8.substring(0, codePointIndexToCharIndex).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
                    if (codePointIndexToCharIndex < utf8.length()) {
                        sb = new StringBuilder();
                        sb.append("[size=");
                        sb.append(this.data.length);
                        sb.append(" text=");
                        break Label_0121;
                    }
                    sb = new StringBuilder();
                    sb.append("[text=");
                }
                sb.append(s);
                final String s2 = "]";
                sb.append(s2);
                return sb.toString();
            }
            sb.append(s);
            final String s2 = "\u2026]";
            continue;
        }
    }
    
    public String utf8() {
        final String utf8 = this.utf8;
        if (utf8 != null) {
            return utf8;
        }
        return this.utf8 = new String(this.data, Util.UTF_8);
    }
    
    void write(final Buffer buffer) {
        final byte[] data = this.data;
        buffer.write(data, 0, data.length);
    }
}
