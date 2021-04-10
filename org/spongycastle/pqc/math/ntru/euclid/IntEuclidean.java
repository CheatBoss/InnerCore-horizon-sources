package org.spongycastle.pqc.math.ntru.euclid;

public class IntEuclidean
{
    public int gcd;
    public int x;
    public int y;
    
    private IntEuclidean() {
    }
    
    public static IntEuclidean calculate(int n, int n2) {
        int y = 0;
        int x = 1;
        final int n3 = 0;
        final int n4 = 1;
        int i = n2;
        int gcd = n;
        n2 = n4;
        n = n3;
        while (i != 0) {
            final int n5 = gcd / i;
            final int n6 = gcd % i;
            gcd = i;
            final int n7 = y - n5 * n2;
            final int n8 = x - n5 * n;
            y = n2;
            x = n;
            n = n8;
            n2 = n7;
            i = n6;
        }
        final IntEuclidean intEuclidean = new IntEuclidean();
        intEuclidean.x = x;
        intEuclidean.y = y;
        intEuclidean.gcd = gcd;
        return intEuclidean;
    }
}
