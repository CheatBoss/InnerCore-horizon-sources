package com.android.dx.util;

public final class HexParser
{
    private HexParser() {
    }
    
    public static byte[] parse(final String s) {
        int length = s.length();
        final byte[] array = new byte[length / 2];
        int i = 0;
        int n = 0;
        while (i < length) {
            int index;
            if ((index = s.indexOf(10, i)) < 0) {
                index = length;
            }
            final int index2 = s.indexOf(35, i);
            String s2;
            if (index2 >= 0 && index2 < index) {
                s2 = s.substring(i, index2);
            }
            else {
                s2 = s.substring(i, index);
            }
            final int n2 = index + 1;
            final int index3 = s2.indexOf(58);
            String substring = s2;
            if (index3 != -1) {
                final int index4 = s2.indexOf(34);
                if (index4 != -1 && index4 < index3) {
                    substring = s2;
                }
                else {
                    final String trim = s2.substring(0, index3).trim();
                    substring = s2.substring(index3 + 1);
                    if (Integer.parseInt(trim, 16) != n) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("bogus offset marker: ");
                        sb.append(trim);
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
            final int length2 = substring.length();
            int n3 = -1;
            int n4 = 0;
            int j = 0;
            final int n5 = length;
            while (j < length2) {
                final char char1 = substring.charAt(j);
                int n6;
                if (n4 != 0) {
                    if (char1 == '\"') {
                        n6 = 0;
                    }
                    else {
                        array[n] = (byte)char1;
                        ++n;
                        n6 = n4;
                    }
                }
                else if (char1 <= ' ') {
                    n6 = n4;
                }
                else if (char1 == '\"') {
                    if (n3 != -1) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("spare digit around offset ");
                        sb2.append(Hex.u4(n));
                        throw new RuntimeException(sb2.toString());
                    }
                    n6 = 1;
                }
                else {
                    int digit = Character.digit(char1, 16);
                    if (digit == -1) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("bogus digit character: \"");
                        sb3.append(char1);
                        sb3.append("\"");
                        throw new RuntimeException(sb3.toString());
                    }
                    if (n3 != -1) {
                        array[n] = (byte)(n3 << 4 | digit);
                        ++n;
                        digit = -1;
                    }
                    n3 = digit;
                    n6 = n4;
                }
                ++j;
                n4 = n6;
            }
            if (n3 != -1) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("spare digit around offset ");
                sb4.append(Hex.u4(n));
                throw new RuntimeException(sb4.toString());
            }
            if (n4 != 0) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("unterminated quote around offset ");
                sb5.append(Hex.u4(n));
                throw new RuntimeException(sb5.toString());
            }
            length = n5;
            i = n2;
        }
        byte[] array2 = array;
        if (n < array.length) {
            array2 = new byte[n];
            System.arraycopy(array, 0, array2, 0, n);
        }
        return array2;
    }
}
