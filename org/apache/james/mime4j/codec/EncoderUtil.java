package org.apache.james.mime4j.codec;

import java.nio.charset.*;
import org.apache.james.mime4j.util.*;
import java.nio.*;
import java.util.*;

public class EncoderUtil
{
    private static final BitSet ATEXT_CHARS;
    private static final char BASE64_PAD = '=';
    private static final byte[] BASE64_TABLE;
    private static final int ENCODED_WORD_MAX_LENGTH = 75;
    private static final String ENC_WORD_PREFIX = "=?";
    private static final String ENC_WORD_SUFFIX = "?=";
    private static final int MAX_USED_CHARACTERS = 50;
    private static final BitSet Q_REGULAR_CHARS;
    private static final BitSet Q_RESTRICTED_CHARS;
    private static final BitSet TOKEN_CHARS;
    
    static {
        BASE64_TABLE = Base64OutputStream.BASE64_TABLE;
        Q_REGULAR_CHARS = initChars("=_?");
        Q_RESTRICTED_CHARS = initChars("=_?\"#$%&'(),.:;<>@[\\]^`{|}~");
        TOKEN_CHARS = initChars("()<>@,;:\\\"/[]?=");
        ATEXT_CHARS = initChars("()<>@.,;:\\\"[]");
    }
    
    private EncoderUtil() {
    }
    
    private static int bEncodedLength(final byte[] array) {
        return (array.length + 2) / 3 * 4;
    }
    
    private static Charset determineCharset(final String s) {
        final int length = s.length();
        int i = 0;
        boolean b = true;
        while (i < length) {
            final char char1 = s.charAt(i);
            if (char1 > '\u00ff') {
                return CharsetUtil.UTF_8;
            }
            if (char1 > '\u007f') {
                b = false;
            }
            ++i;
        }
        if (b) {
            return CharsetUtil.US_ASCII;
        }
        return CharsetUtil.ISO_8859_1;
    }
    
    private static Encoding determineEncoding(final byte[] array, final Usage usage) {
        if (array.length == 0) {
            return Encoding.Q;
        }
        BitSet set;
        if (usage == Usage.TEXT_TOKEN) {
            set = EncoderUtil.Q_REGULAR_CHARS;
        }
        else {
            set = EncoderUtil.Q_RESTRICTED_CHARS;
        }
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final int n2 = array[i] & 0xFF;
            int n3 = n;
            if (n2 != 32) {
                n3 = n;
                if (!set.get(n2)) {
                    n3 = n + 1;
                }
            }
            ++i;
            n = n3;
        }
        if (n * 100 / array.length > 30) {
            return Encoding.B;
        }
        return Encoding.Q;
    }
    
    private static byte[] encode(final String s, final Charset charset) {
        final ByteBuffer encode = charset.encode(s);
        final byte[] array = new byte[encode.limit()];
        encode.get(array);
        return array;
    }
    
    public static String encodeAddressDisplayName(final String s) {
        if (isAtomPhrase(s)) {
            return s;
        }
        if (hasToBeEncoded(s, 0)) {
            return encodeEncodedWord(s, Usage.WORD_ENTITY);
        }
        return quote(s);
    }
    
    public static String encodeAddressLocalPart(final String s) {
        if (isDotAtomText(s)) {
            return s;
        }
        return quote(s);
    }
    
    private static String encodeB(String encodeB, String substring, final int n, final Charset charset, final byte[] array) {
        StringBuilder sb;
        if (encodeB.length() + bEncodedLength(array) + 2 <= 75 - n) {
            sb = new StringBuilder();
            sb.append(encodeB);
            sb.append(encodeB(array));
            encodeB = "?=";
        }
        else {
            final String substring2 = substring.substring(0, substring.length() / 2);
            final String encodeB2 = encodeB(encodeB, substring2, n, charset, encode(substring2, charset));
            substring = substring.substring(substring.length() / 2);
            encodeB = encodeB(encodeB, substring, 0, charset, encode(substring, charset));
            sb = new StringBuilder();
            sb.append(encodeB2);
            sb.append(" ");
        }
        sb.append(encodeB);
        return sb.toString();
    }
    
    public static String encodeB(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        final int length = array.length;
        int n = 0;
        int n2;
        while (true) {
            n2 = length - 2;
            if (n >= n2) {
                break;
            }
            final int n3 = (array[n] & 0xFF) << 16 | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF);
            sb.append((char)EncoderUtil.BASE64_TABLE[n3 >> 18 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n3 >> 12 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n3 >> 6 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n3 & 0x3F]);
            n += 3;
        }
        if (n == n2) {
            final int n4 = (array[n + 1] & 0xFF) << 8 | (array[n] & 0xFF) << 16;
            sb.append((char)EncoderUtil.BASE64_TABLE[n4 >> 18 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n4 >> 12 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n4 >> 6 & 0x3F]);
        }
        else {
            if (n != length - 1) {
                return sb.toString();
            }
            final int n5 = (array[n] & 0xFF) << 16;
            sb.append((char)EncoderUtil.BASE64_TABLE[n5 >> 18 & 0x3F]);
            sb.append((char)EncoderUtil.BASE64_TABLE[n5 >> 12 & 0x3F]);
            sb.append('=');
        }
        sb.append('=');
        return sb.toString();
    }
    
    public static String encodeEncodedWord(final String s, final Usage usage) {
        return encodeEncodedWord(s, usage, 0, null, null);
    }
    
    public static String encodeEncodedWord(final String s, final Usage usage, final int n) {
        return encodeEncodedWord(s, usage, n, null, null);
    }
    
    public static String encodeEncodedWord(final String s, final Usage usage, final int n, final Charset charset, final Encoding encoding) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (n < 0 || n > 50) {
            throw new IllegalArgumentException();
        }
        Charset determineCharset;
        if ((determineCharset = charset) == null) {
            determineCharset = determineCharset(s);
        }
        final String mimeCharset = CharsetUtil.toMimeCharset(determineCharset.name());
        if (mimeCharset == null) {
            throw new IllegalArgumentException("Unsupported charset");
        }
        final byte[] encode = encode(s, determineCharset);
        Encoding determineEncoding;
        if ((determineEncoding = encoding) == null) {
            determineEncoding = determineEncoding(encode, usage);
        }
        if (determineEncoding == Encoding.B) {
            final StringBuilder sb = new StringBuilder();
            sb.append("=?");
            sb.append(mimeCharset);
            sb.append("?B?");
            return encodeB(sb.toString(), s, n, determineCharset, encode);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("=?");
        sb2.append(mimeCharset);
        sb2.append("?Q?");
        return encodeQ(sb2.toString(), s, usage, n, determineCharset, encode);
    }
    
    public static String encodeHeaderParameter(final String s, final String s2) {
        final String lowerCase = s.toLowerCase(Locale.US);
        StringBuilder sb;
        if (isToken(s2)) {
            sb = new StringBuilder();
            sb.append(lowerCase);
            sb.append("=");
            sb.append(s2);
        }
        else {
            sb = new StringBuilder();
            sb.append(lowerCase);
            sb.append("=");
            sb.append(quote(s2));
        }
        return sb.toString();
    }
    
    public static String encodeIfNecessary(final String s, final Usage usage, final int n) {
        String encodeEncodedWord = s;
        if (hasToBeEncoded(s, n)) {
            encodeEncodedWord = encodeEncodedWord(s, usage, n);
        }
        return encodeEncodedWord;
    }
    
    private static String encodeQ(String encodeQ, String substring, final Usage usage, final int n, final Charset charset, final byte[] array) {
        StringBuilder sb;
        if (encodeQ.length() + qEncodedLength(array, usage) + 2 <= 75 - n) {
            sb = new StringBuilder();
            sb.append(encodeQ);
            sb.append(encodeQ(array, usage));
            encodeQ = "?=";
        }
        else {
            final String substring2 = substring.substring(0, substring.length() / 2);
            final String encodeQ2 = encodeQ(encodeQ, substring2, usage, n, charset, encode(substring2, charset));
            substring = substring.substring(substring.length() / 2);
            encodeQ = encodeQ(encodeQ, substring, usage, 0, charset, encode(substring, charset));
            sb = new StringBuilder();
            sb.append(encodeQ2);
            sb.append(" ");
        }
        sb.append(encodeQ);
        return sb.toString();
    }
    
    public static String encodeQ(final byte[] array, final Usage usage) {
        BitSet set;
        if (usage == Usage.TEXT_TOKEN) {
            set = EncoderUtil.Q_REGULAR_CHARS;
        }
        else {
            set = EncoderUtil.Q_RESTRICTED_CHARS;
        }
        final StringBuilder sb = new StringBuilder();
        for (int length = array.length, i = 0; i < length; ++i) {
            final int n = array[i] & 0xFF;
            char hexDigit;
            if (n == 32) {
                hexDigit = '_';
            }
            else if (!set.get(n)) {
                sb.append('=');
                sb.append(hexDigit(n >>> 4));
                hexDigit = hexDigit(n & 0xF);
            }
            else {
                hexDigit = (char)n;
            }
            sb.append(hexDigit);
        }
        return sb.toString();
    }
    
    public static boolean hasToBeEncoded(final String s, int n) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (n >= 0 && n <= 50) {
            for (int i = 0; i < s.length(); ++i) {
                final char char1 = s.charAt(i);
                if (char1 != '\t' && char1 != ' ') {
                    ++n;
                    if (n > 77) {
                        return true;
                    }
                    if (char1 < ' ' || char1 >= '\u007f') {
                        return true;
                    }
                }
                else {
                    n = 0;
                }
            }
            return false;
        }
        throw new IllegalArgumentException();
    }
    
    private static char hexDigit(int n) {
        if (n < 10) {
            n += 48;
        }
        else {
            n = n - 10 + 65;
        }
        return (char)n;
    }
    
    private static BitSet initChars(final String s) {
        final BitSet set = new BitSet(128);
        for (int i = 33; i < 127; i = (char)(i + 1)) {
            if (s.indexOf(i) == -1) {
                set.set(i);
            }
        }
        return set;
    }
    
    private static boolean isAtomPhrase(final String s) {
        final int length = s.length();
        int i = 0;
        boolean b = false;
        while (i < length) {
            final char char1 = s.charAt(i);
            if (EncoderUtil.ATEXT_CHARS.get(char1)) {
                b = true;
            }
            else if (!CharsetUtil.isWhitespace(char1)) {
                return false;
            }
            ++i;
        }
        return b;
    }
    
    private static boolean isDotAtomText(final String s) {
        final int length = s.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        int n = 46;
        while (i < length) {
            final char char1 = s.charAt(i);
            if (char1 == '.') {
                if (n == 46 || i == length - 1) {
                    return false;
                }
            }
            else if (!EncoderUtil.ATEXT_CHARS.get(char1)) {
                return false;
            }
            ++i;
            n = char1;
        }
        return true;
    }
    
    public static boolean isToken(final String s) {
        final int length = s.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            if (!EncoderUtil.TOKEN_CHARS.get(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static int qEncodedLength(final byte[] array, final Usage usage) {
        BitSet set;
        if (usage == Usage.TEXT_TOKEN) {
            set = EncoderUtil.Q_REGULAR_CHARS;
        }
        else {
            set = EncoderUtil.Q_RESTRICTED_CHARS;
        }
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final int n2 = array[i] & 0xFF;
            Label_0067: {
                if (n2 != 32) {
                    if (!set.get(n2)) {
                        n += 3;
                        break Label_0067;
                    }
                }
                ++n;
            }
            ++i;
        }
        return n;
    }
    
    private static String quote(String replaceAll) {
        replaceAll = replaceAll.replaceAll("[\\\\\"]", "\\\\$0");
        final StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(replaceAll);
        sb.append("\"");
        return sb.toString();
    }
    
    public enum Encoding
    {
        B, 
        Q;
    }
    
    public enum Usage
    {
        TEXT_TOKEN, 
        WORD_ENTITY;
    }
}
