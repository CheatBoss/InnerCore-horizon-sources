package org.spongycastle.util.test;

public final class NumberParsing
{
    private NumberParsing() {
    }
    
    public static int decodeIntFromHex(final String s) {
        if (s.charAt(1) != 'x' && s.charAt(1) != 'X') {
            return Integer.parseInt(s, 16);
        }
        return Integer.parseInt(s.substring(2), 16);
    }
    
    public static long decodeLongFromHex(final String s) {
        if (s.charAt(1) != 'x' && s.charAt(1) != 'X') {
            return Long.parseLong(s, 16);
        }
        return Long.parseLong(s.substring(2), 16);
    }
}
