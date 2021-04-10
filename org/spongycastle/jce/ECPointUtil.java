package org.spongycastle.jce;

import org.spongycastle.math.ec.*;
import java.security.spec.*;

public class ECPointUtil
{
    public static ECPoint decodePoint(final EllipticCurve ellipticCurve, final byte[] array) {
        ECCurve ecCurve;
        if (ellipticCurve.getField() instanceof ECFieldFp) {
            ecCurve = new ECCurve.Fp(((ECFieldFp)ellipticCurve.getField()).getP(), ellipticCurve.getA(), ellipticCurve.getB());
        }
        else {
            final int[] midTermsOfReductionPolynomial = ((ECFieldF2m)ellipticCurve.getField()).getMidTermsOfReductionPolynomial();
            if (midTermsOfReductionPolynomial.length == 3) {
                ecCurve = new ECCurve.F2m(((ECFieldF2m)ellipticCurve.getField()).getM(), midTermsOfReductionPolynomial[2], midTermsOfReductionPolynomial[1], midTermsOfReductionPolynomial[0], ellipticCurve.getA(), ellipticCurve.getB());
            }
            else {
                ecCurve = new ECCurve.F2m(((ECFieldF2m)ellipticCurve.getField()).getM(), midTermsOfReductionPolynomial[0], ellipticCurve.getA(), ellipticCurve.getB());
            }
        }
        final org.spongycastle.math.ec.ECPoint decodePoint = ecCurve.decodePoint(array);
        return new ECPoint(decodePoint.getAffineXCoord().toBigInteger(), decodePoint.getAffineYCoord().toBigInteger());
    }
}
