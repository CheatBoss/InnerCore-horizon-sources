package org.spongycastle.pqc.math.ntru.euclid;

import java.math.*;

public class BigIntEuclidean
{
    public BigInteger gcd;
    public BigInteger x;
    public BigInteger y;
    
    private BigIntEuclidean() {
    }
    
    public static BigIntEuclidean calculate(BigInteger bigInteger, BigInteger bigInteger2) {
        final BigInteger zero = BigInteger.ZERO;
        BigInteger one = BigInteger.ONE;
        final BigInteger one2 = BigInteger.ONE;
        BigInteger zero2 = BigInteger.ZERO;
        BigInteger gcd = bigInteger;
        BigInteger bigInteger3;
        BigInteger[] divideAndRemainder;
        BigInteger bigInteger4;
        BigInteger bigInteger5;
        BigInteger subtract;
        BigInteger subtract2;
        for (bigInteger3 = bigInteger2, bigInteger = one2, bigInteger2 = zero; !bigInteger3.equals(BigInteger.ZERO); bigInteger3 = bigInteger5, one = bigInteger2, bigInteger2 = subtract, zero2 = bigInteger, bigInteger = subtract2) {
            divideAndRemainder = gcd.divideAndRemainder(bigInteger3);
            bigInteger4 = divideAndRemainder[0];
            bigInteger5 = divideAndRemainder[1];
            subtract = one.subtract(bigInteger4.multiply(bigInteger2));
            subtract2 = zero2.subtract(bigInteger4.multiply(bigInteger));
            gcd = bigInteger3;
        }
        final BigIntEuclidean bigIntEuclidean = new BigIntEuclidean();
        bigIntEuclidean.x = one;
        bigIntEuclidean.y = zero2;
        bigIntEuclidean.gcd = gcd;
        return bigIntEuclidean;
    }
}
