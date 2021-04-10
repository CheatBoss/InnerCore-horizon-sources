package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.math.linearalgebra.*;

final class McElieceCCA2Primitives
{
    private McElieceCCA2Primitives() {
    }
    
    public static GF2Vector[] decryptionPrimitive(final McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters, GF2Vector gf2Vector) {
        final int k = mcElieceCCA2PrivateKeyParameters.getK();
        final Permutation p2 = mcElieceCCA2PrivateKeyParameters.getP();
        final GF2mField field = mcElieceCCA2PrivateKeyParameters.getField();
        final PolynomialGF2mSmallM goppaPoly = mcElieceCCA2PrivateKeyParameters.getGoppaPoly();
        final GF2Matrix h = mcElieceCCA2PrivateKeyParameters.getH();
        final PolynomialGF2mSmallM[] qInv = mcElieceCCA2PrivateKeyParameters.getQInv();
        gf2Vector = (GF2Vector)gf2Vector.multiply(p2.computeInverse());
        final GF2Vector syndromeDecode = GoppaCode.syndromeDecode((GF2Vector)h.rightMultiply(gf2Vector), field, goppaPoly, qInv);
        gf2Vector = (GF2Vector)((GF2Vector)gf2Vector.add(syndromeDecode)).multiply(p2);
        return new GF2Vector[] { gf2Vector.extractRightVector(k), (GF2Vector)syndromeDecode.multiply(p2) };
    }
    
    public static GF2Vector encryptionPrimitive(final McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters, final GF2Vector gf2Vector, final GF2Vector gf2Vector2) {
        return (GF2Vector)mcElieceCCA2PublicKeyParameters.getG().leftMultiplyLeftCompactForm(gf2Vector).add(gf2Vector2);
    }
}
