package org.spongycastle.asn1.x9;

import org.spongycastle.math.ec.*;
import java.math.*;

public class X9IntegerConverter
{
    public int getByteLength(final ECCurve ecCurve) {
        return (ecCurve.getFieldSize() + 7) / 8;
    }
    
    public int getByteLength(final ECFieldElement ecFieldElement) {
        return (ecFieldElement.getFieldSize() + 7) / 8;
    }
    
    public byte[] integerToBytes(final BigInteger bigInteger, final int n) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (n < byteArray.length) {
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, byteArray.length - n, array, 0, n);
            return array;
        }
        if (n > byteArray.length) {
            final byte[] array2 = new byte[n];
            System.arraycopy(byteArray, 0, array2, n - byteArray.length, byteArray.length);
            return array2;
        }
        return byteArray;
    }
}
