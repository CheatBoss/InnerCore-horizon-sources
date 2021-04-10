package org.spongycastle.asn1.ua;

import java.math.*;
import org.spongycastle.math.ec.*;
import java.util.*;

public abstract class DSTU4145PointEncoder
{
    public static ECPoint decodePoint(final ECCurve ecCurve, final byte[] array) {
        final ECFieldElement fromBigInteger = ecCurve.fromBigInteger(BigInteger.valueOf(array[array.length - 1] & 0x1));
        ECFieldElement ecFieldElement2;
        final ECFieldElement ecFieldElement = ecFieldElement2 = ecCurve.fromBigInteger(new BigInteger(1, array));
        if (!trace(ecFieldElement).equals(ecCurve.getA())) {
            ecFieldElement2 = ecFieldElement.addOne();
        }
        ECFieldElement ecFieldElement3 = null;
        if (ecFieldElement2.isZero()) {
            ecFieldElement3 = ecCurve.getB().sqrt();
        }
        else {
            final ECFieldElement solveQuadraticEquation = solveQuadraticEquation(ecCurve, ecFieldElement2.square().invert().multiply(ecCurve.getB()).add(ecCurve.getA()).add(ecFieldElement2));
            if (solveQuadraticEquation != null) {
                ECFieldElement addOne = solveQuadraticEquation;
                if (!trace(solveQuadraticEquation).equals(fromBigInteger)) {
                    addOne = solveQuadraticEquation.addOne();
                }
                ecFieldElement3 = ecFieldElement2.multiply(addOne);
            }
        }
        if (ecFieldElement3 != null) {
            return ecCurve.validatePoint(ecFieldElement2.toBigInteger(), ecFieldElement3.toBigInteger());
        }
        throw new IllegalArgumentException("Invalid point compression");
    }
    
    public static byte[] encodePoint(ECPoint normalize) {
        normalize = normalize.normalize();
        final ECFieldElement affineXCoord = normalize.getAffineXCoord();
        final byte[] encoded = affineXCoord.getEncoded();
        if (!affineXCoord.isZero()) {
            if (trace(normalize.getAffineYCoord().divide(affineXCoord)).isOne()) {
                final int n = encoded.length - 1;
                encoded[n] |= 0x1;
                return encoded;
            }
            final int n2 = encoded.length - 1;
            encoded[n2] &= (byte)254;
        }
        return encoded;
    }
    
    private static ECFieldElement solveQuadraticEquation(final ECCurve ecCurve, final ECFieldElement ecFieldElement) {
        if (ecFieldElement.isZero()) {
            return ecFieldElement;
        }
        final ECFieldElement fromBigInteger = ecCurve.fromBigInteger(ECConstants.ZERO);
        final Random random = new Random();
        final int fieldSize = ecFieldElement.getFieldSize();
        ECFieldElement add;
        do {
            final ECFieldElement fromBigInteger2 = ecCurve.fromBigInteger(new BigInteger(fieldSize, random));
            int i = 1;
            ECFieldElement add2 = ecFieldElement;
            add = fromBigInteger;
            while (i <= fieldSize - 1) {
                final ECFieldElement square = add2.square();
                add = add.square().add(square.multiply(fromBigInteger2));
                add2 = square.add(ecFieldElement);
                ++i;
            }
            if (!add2.isZero()) {
                return null;
            }
        } while (add.square().add(add).isZero());
        return add;
    }
    
    private static ECFieldElement trace(final ECFieldElement ecFieldElement) {
        int i = 1;
        ECFieldElement add = ecFieldElement;
        while (i < ecFieldElement.getFieldSize()) {
            add = add.square().add(ecFieldElement);
            ++i;
        }
        return add;
    }
}
