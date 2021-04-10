package com.android.dx.util;

public final class Hex
{
    private Hex() {
    }
    
    public static String dump(final byte[] array, int n, int i, int n2, final int n3, final int n4) {
        final int n5 = n + i;
        if ((n | i | n5) < 0 || n5 > array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("arr.length ");
            sb.append(array.length);
            sb.append("; ");
            sb.append(n);
            sb.append("..!");
            sb.append(n5);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("outOffset < 0");
        }
        if (i == 0) {
            return "";
        }
        final StringBuffer sb2 = new StringBuffer(i * 4 + 6);
        final int n6 = 0;
        int n7 = n2;
        n2 = n;
        n = n6;
        while (i > 0) {
            if (n == 0) {
                String s;
                if (n4 != 2) {
                    if (n4 != 4) {
                        if (n4 != 6) {
                            s = u4(n7);
                        }
                        else {
                            s = u3(n7);
                        }
                    }
                    else {
                        s = u2(n7);
                    }
                }
                else {
                    s = u1(n7);
                }
                sb2.append(s);
                sb2.append(": ");
            }
            else if ((n & 0x1) == 0x0) {
                sb2.append(' ');
            }
            sb2.append(u1(array[n2]));
            ++n7;
            ++n2;
            if (++n == n3) {
                sb2.append('\n');
                n = 0;
            }
            --i;
        }
        if (n != 0) {
            sb2.append('\n');
        }
        return sb2.toString();
    }
    
    public static String s1(int i) {
        final char[] array = new char[3];
        final int n = 0;
        if (i < 0) {
            array[0] = '-';
            i = -i;
        }
        else {
            array[0] = '+';
        }
        int n2 = i;
        for (i = n; i < 2; ++i) {
            array[2 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String s2(int i) {
        final char[] array = new char[5];
        final int n = 0;
        if (i < 0) {
            array[0] = '-';
            i = -i;
        }
        else {
            array[0] = '+';
        }
        int n2 = i;
        for (i = n; i < 4; ++i) {
            array[4 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String s4(int i) {
        final char[] array = new char[9];
        final int n = 0;
        if (i < 0) {
            array[0] = '-';
            i = -i;
        }
        else {
            array[0] = '+';
        }
        int n2 = i;
        for (i = n; i < 8; ++i) {
            array[8 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String s8(long n) {
        final char[] array = new char[17];
        int i = 0;
        if (n < 0L) {
            array[0] = '-';
            n = -n;
        }
        else {
            array[0] = '+';
        }
        while (i < 16) {
            array[16 - i] = Character.forDigit((int)n & 0xF, 16);
            n >>= 4;
            ++i;
        }
        return new String(array);
    }
    
    public static String u1(int i) {
        final char[] array = new char[2];
        final int n = 0;
        int n2 = i;
        for (i = n; i < 2; ++i) {
            array[1 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String u2(int i) {
        final char[] array = new char[4];
        final int n = 0;
        int n2 = i;
        for (i = n; i < 4; ++i) {
            array[3 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String u2or4(final int n) {
        if (n == (char)n) {
            return u2(n);
        }
        return u4(n);
    }
    
    public static String u3(int i) {
        final char[] array = new char[6];
        final int n = 0;
        int n2 = i;
        for (i = n; i < 6; ++i) {
            array[5 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String u4(int i) {
        final char[] array = new char[8];
        final int n = 0;
        int n2 = i;
        for (i = n; i < 8; ++i) {
            array[7 - i] = Character.forDigit(n2 & 0xF, 16);
            n2 >>= 4;
        }
        return new String(array);
    }
    
    public static String u8(long n) {
        final char[] array = new char[16];
        for (int i = 0; i < 16; ++i) {
            array[15 - i] = Character.forDigit((int)n & 0xF, 16);
            n >>= 4;
        }
        return new String(array);
    }
    
    public static String uNibble(final int n) {
        return new String(new char[] { Character.forDigit(n & 0xF, 16) });
    }
}
