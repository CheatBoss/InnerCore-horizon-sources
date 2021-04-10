package org.spongycastle.crypto.generators;

import java.util.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.*;

public class OpenBSDBCrypt
{
    private static final Set<String> allowedVersions;
    private static final byte[] decodingTable;
    private static final String defaultVersion = "2y";
    private static final byte[] encodingTable;
    
    static {
        encodingTable = new byte[] { 46, 47, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
        decodingTable = new byte[128];
        (allowedVersions = new HashSet<String>()).add("2a");
        OpenBSDBCrypt.allowedVersions.add("2y");
        OpenBSDBCrypt.allowedVersions.add("2b");
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            final byte[] decodingTable2 = OpenBSDBCrypt.decodingTable;
            n3 = n;
            if (n2 >= decodingTable2.length) {
                break;
            }
            decodingTable2[n2] = -1;
            ++n2;
        }
        while (true) {
            final byte[] encodingTable2 = OpenBSDBCrypt.encodingTable;
            if (n3 >= encodingTable2.length) {
                break;
            }
            OpenBSDBCrypt.decodingTable[encodingTable2[n3]] = (byte)n3;
            ++n3;
        }
    }
    
    public static boolean checkPassword(String s, final char[] array) {
        if (s.length() != 60) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bcrypt String length: ");
            sb.append(s.length());
            sb.append(", 60 required.");
            throw new DataLengthException(sb.toString());
        }
        if (s.charAt(0) == '$' && s.charAt(3) == '$' && s.charAt(6) == '$') {
            final String substring = s.substring(1, 3);
            if (OpenBSDBCrypt.allowedVersions.contains(substring)) {
                try {
                    final int int1 = Integer.parseInt(s.substring(4, 6));
                    if (int1 < 4 || int1 > 31) {
                        s = (String)new StringBuilder();
                        ((StringBuilder)s).append("Invalid cost factor: ");
                        ((StringBuilder)s).append(int1);
                        ((StringBuilder)s).append(", 4 < cost < 31 expected.");
                        throw new IllegalArgumentException(((StringBuilder)s).toString());
                    }
                    if (array != null) {
                        return s.equals(generate(substring, array, decodeSaltString(s.substring(s.lastIndexOf(36) + 1, s.length() - 31)), int1));
                    }
                    throw new IllegalArgumentException("Missing password.");
                }
                catch (NumberFormatException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid cost factor: ");
                    sb2.append(s.substring(4, 6));
                    throw new IllegalArgumentException(sb2.toString());
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Bcrypt version '");
            sb3.append(s.substring(1, 3));
            sb3.append("' is not supported by this implementation");
            throw new IllegalArgumentException(sb3.toString());
        }
        throw new IllegalArgumentException("Invalid Bcrypt String format.");
    }
    
    private static String createBcryptString(String s, final byte[] array, final byte[] array2, final int n) {
        if (OpenBSDBCrypt.allowedVersions.contains(s)) {
            final StringBuffer sb = new StringBuffer(60);
            sb.append('$');
            sb.append(s);
            sb.append('$');
            if (n < 10) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("0");
                sb2.append(n);
                s = sb2.toString();
            }
            else {
                s = Integer.toString(n);
            }
            sb.append(s);
            sb.append('$');
            sb.append(encodeData(array2));
            sb.append(encodeData(BCrypt.generate(array, array2, n)));
            return sb.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Version ");
        sb3.append(s);
        sb3.append(" is not accepted by this implementation.");
        throw new IllegalArgumentException(sb3.toString());
    }
    
    private static byte[] decodeSaltString(final String s) {
        final char[] charArray = s.toCharArray();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        if (charArray.length == 22) {
            for (int i = 0; i < charArray.length; ++i) {
                final char c = charArray[i];
                if (c > 'z' || c < '.' || (c > '9' && c < 'A')) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Salt string contains invalid character: ");
                    sb.append((int)c);
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final char[] array = new char[24];
            System.arraycopy(charArray, 0, array, 0, charArray.length);
            for (int j = 0; j < 24; j += 4) {
                final byte[] decodingTable = OpenBSDBCrypt.decodingTable;
                final byte b = decodingTable[array[j]];
                final byte b2 = decodingTable[array[j + 1]];
                final byte b3 = decodingTable[array[j + 2]];
                final byte b4 = decodingTable[array[j + 3]];
                byteArrayOutputStream.write(b << 2 | b2 >> 4);
                byteArrayOutputStream.write(b2 << 4 | b3 >> 2);
                byteArrayOutputStream.write(b4 | b3 << 6);
            }
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            final byte[] array2 = new byte[16];
            System.arraycopy(byteArray, 0, array2, 0, 16);
            return array2;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid base64 salt length: ");
        sb2.append(charArray.length);
        sb2.append(" , 22 required.");
        throw new DataLengthException(sb2.toString());
    }
    
    private static String encodeData(byte[] array) {
        if (array.length != 24 && array.length != 16) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid length: ");
            sb.append(array.length);
            sb.append(", 24 for key or 16 for salt expected");
            throw new DataLengthException(sb.toString());
        }
        int n;
        if (array.length == 16) {
            final byte[] array2 = new byte[18];
            System.arraycopy(array, 0, array2, 0, array.length);
            array = array2;
            n = 1;
        }
        else {
            array[array.length - 1] = 0;
            n = 0;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int length = array.length, i = 0; i < length; i += 3) {
            final int n2 = array[i] & 0xFF;
            final int n3 = array[i + 1] & 0xFF;
            final int n4 = array[i + 2] & 0xFF;
            byteArrayOutputStream.write(OpenBSDBCrypt.encodingTable[n2 >>> 2 & 0x3F]);
            byteArrayOutputStream.write(OpenBSDBCrypt.encodingTable[(n2 << 4 | n3 >>> 4) & 0x3F]);
            byteArrayOutputStream.write(OpenBSDBCrypt.encodingTable[(n3 << 2 | n4 >>> 6) & 0x3F]);
            byteArrayOutputStream.write(OpenBSDBCrypt.encodingTable[n4 & 0x3F]);
        }
        final String fromByteArray = Strings.fromByteArray(byteArrayOutputStream.toByteArray());
        int n5;
        if (n == 1) {
            n5 = 22;
        }
        else {
            n5 = fromByteArray.length() - 1;
        }
        return fromByteArray.substring(0, n5);
    }
    
    public static String generate(String bcryptString, final char[] array, final byte[] array2, final int n) {
        if (!OpenBSDBCrypt.allowedVersions.contains(bcryptString)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Version ");
            sb.append(bcryptString);
            sb.append(" is not accepted by this implementation.");
            throw new IllegalArgumentException(sb.toString());
        }
        if (array == null) {
            throw new IllegalArgumentException("Password required.");
        }
        if (array2 == null) {
            throw new IllegalArgumentException("Salt required.");
        }
        if (array2.length != 16) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("16 byte salt required: ");
            sb2.append(array2.length);
            throw new DataLengthException(sb2.toString());
        }
        if (n >= 4 && n <= 31) {
            final byte[] utf8ByteArray = Strings.toUTF8ByteArray(array);
            final int length = utf8ByteArray.length;
            int n2 = 72;
            if (length < 72) {
                n2 = utf8ByteArray.length + 1;
            }
            final byte[] array3 = new byte[n2];
            int length2 = n2;
            if (n2 > utf8ByteArray.length) {
                length2 = utf8ByteArray.length;
            }
            System.arraycopy(utf8ByteArray, 0, array3, 0, length2);
            Arrays.fill(utf8ByteArray, (byte)0);
            bcryptString = createBcryptString(bcryptString, array3, array2, n);
            Arrays.fill(array3, (byte)0);
            return bcryptString;
        }
        throw new IllegalArgumentException("Invalid cost factor.");
    }
    
    public static String generate(final char[] array, final byte[] array2, final int n) {
        return generate("2y", array, array2, n);
    }
}
