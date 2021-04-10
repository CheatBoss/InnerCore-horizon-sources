package org.spongycastle.math.raw;

public class Interleave
{
    private static final long M32 = 1431655765L;
    private static final long M64 = 6148914691236517205L;
    
    public static int expand16to32(int n) {
        n &= 0xFFFF;
        n = ((n | n << 8) & 0xFF00FF);
        n = ((n | n << 4) & 0xF0F0F0F);
        n = ((n | n << 2) & 0x33333333);
        return (n | n << 1) & 0x55555555;
    }
    
    public static long expand32to64(int n) {
        final int n2 = (n >>> 8 ^ n) & 0xFF00;
        n ^= (n2 ^ n2 << 8);
        final int n3 = (n >>> 4 ^ n) & 0xF000F0;
        n ^= (n3 ^ n3 << 4);
        final int n4 = (n >>> 2 ^ n) & 0xC0C0C0C;
        n ^= (n4 ^ n4 << 2);
        final int n5 = (n >>> 1 ^ n) & 0x22222222;
        n ^= (n5 ^ n5 << 1);
        return ((long)(n >>> 1) & 0x55555555L) << 32 | ((long)n & 0x55555555L);
    }
    
    public static void expand64To128(long n, final long[] array, final int n2) {
        final long n3 = (n >>> 16 ^ n) & 0xFFFF0000L;
        n ^= (n3 ^ n3 << 16);
        final long n4 = (n >>> 8 ^ n) & 0xFF000000FF00L;
        n ^= (n4 ^ n4 << 8);
        final long n5 = (n >>> 4 ^ n) & 0xF000F000F000F0L;
        n ^= (n5 ^ n5 << 4);
        final long n6 = (n >>> 2 ^ n) & 0xC0C0C0C0C0C0C0CL;
        n ^= (n6 ^ n6 << 2);
        final long n7 = (n >>> 1 ^ n) & 0x2222222222222222L;
        n ^= (n7 ^ n7 << 1);
        array[n2] = (n & 0x5555555555555555L);
        array[n2 + 1] = (n >>> 1 & 0x5555555555555555L);
    }
    
    public static int expand8to16(int n) {
        n &= 0xFF;
        n = ((n | n << 4) & 0xF0F);
        n = ((n | n << 2) & 0x3333);
        return (n | n << 1) & 0x5555;
    }
    
    public static long unshuffle(long n) {
        final long n2 = (n >>> 1 ^ n) & 0x2222222222222222L;
        n ^= (n2 ^ n2 << 1);
        final long n3 = (n >>> 2 ^ n) & 0xC0C0C0C0C0C0C0CL;
        n ^= (n3 ^ n3 << 2);
        final long n4 = (n >>> 4 ^ n) & 0xF000F000F000F0L;
        n ^= (n4 ^ n4 << 4);
        final long n5 = (n >>> 8 ^ n) & 0xFF000000FF00L;
        n ^= (n5 ^ n5 << 8);
        final long n6 = (n >>> 16 ^ n) & 0xFFFF0000L;
        return n ^ (n6 ^ n6 << 16);
    }
}
