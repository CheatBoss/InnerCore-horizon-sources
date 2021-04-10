package net.lingala.zip4j.crypto.PBKDF2;

class BinTools
{
    public static final String hex = "0123456789ABCDEF";
    
    public static String bin2hex(final byte[] array) {
        if (array == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer(array.length * 2);
        for (int i = 0; i < array.length; ++i) {
            final int n = (array[i] + 256) % 256;
            sb.append("0123456789ABCDEF".charAt(n / 16 & 0xF));
            sb.append("0123456789ABCDEF".charAt(n % 16 & 0xF));
        }
        return sb.toString();
    }
    
    public static int hex2bin(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Input string may only contain hex digits, but found '");
        sb.append(c);
        sb.append("'");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static byte[] hex2bin(final String s) {
        String string = s;
        if (s == null) {
            string = "";
        }
        else if (s.length() % 2 != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(s);
            string = sb.toString();
        }
        final byte[] array = new byte[string.length() / 2];
        int i = 0;
        int n = 0;
        while (i < string.length()) {
            final int n2 = i + 1;
            array[n] = (byte)(hex2bin(string.charAt(i)) * 16 + hex2bin(string.charAt(n2)));
            ++n;
            i = n2 + 1;
        }
        return array;
    }
}
