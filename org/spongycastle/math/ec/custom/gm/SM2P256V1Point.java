package org.spongycastle.math.ec.custom.gm;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SM2P256V1Point extends AbstractFp
{
    public SM2P256V1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SM2P256V1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
        super(ecCurve, ecFieldElement, ecFieldElement2);
        int n = false ? 1 : 0;
        final boolean b = ecFieldElement == null;
        if (ecFieldElement2 == null) {
            n = (true ? 1 : 0);
        }
        if ((b ? 1 : 0) == n) {
            this.withCompression = withCompression;
            return;
        }
        throw new IllegalArgumentException("Exactly one of the field elements is null");
    }
    
    SM2P256V1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
        super(ecCurve, ecFieldElement, ecFieldElement2, array);
        this.withCompression = withCompression;
    }
    
    @Override
    public ECPoint add(final ECPoint ecPoint) {
        if (this.isInfinity()) {
            return ecPoint;
        }
        if (ecPoint.isInfinity()) {
            return this;
        }
        if (this == ecPoint) {
            return this.twice();
        }
        final ECCurve curve = this.getCurve();
        final SM2P256V1FieldElement sm2P256V1FieldElement = (SM2P256V1FieldElement)this.x;
        final SM2P256V1FieldElement sm2P256V1FieldElement2 = (SM2P256V1FieldElement)this.y;
        final SM2P256V1FieldElement sm2P256V1FieldElement3 = (SM2P256V1FieldElement)ecPoint.getXCoord();
        final SM2P256V1FieldElement sm2P256V1FieldElement4 = (SM2P256V1FieldElement)ecPoint.getYCoord();
        final SM2P256V1FieldElement sm2P256V1FieldElement5 = (SM2P256V1FieldElement)this.zs[0];
        final SM2P256V1FieldElement sm2P256V1FieldElement6 = (SM2P256V1FieldElement)ecPoint.getZCoord(0);
        final int[] ext = Nat256.createExt();
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final int[] create3 = Nat256.create();
        final boolean one = sm2P256V1FieldElement5.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = sm2P256V1FieldElement3.x;
            x2 = sm2P256V1FieldElement4.x;
        }
        else {
            SM2P256V1Field.square(sm2P256V1FieldElement5.x, create2);
            SM2P256V1Field.multiply(create2, sm2P256V1FieldElement3.x, create);
            SM2P256V1Field.multiply(create2, sm2P256V1FieldElement5.x, create2);
            SM2P256V1Field.multiply(create2, sm2P256V1FieldElement4.x, create2);
            x = create;
            x2 = create2;
        }
        final boolean one2 = sm2P256V1FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = sm2P256V1FieldElement.x;
            x4 = sm2P256V1FieldElement2.x;
        }
        else {
            SM2P256V1Field.square(sm2P256V1FieldElement6.x, create3);
            SM2P256V1Field.multiply(create3, sm2P256V1FieldElement.x, ext);
            SM2P256V1Field.multiply(create3, sm2P256V1FieldElement6.x, create3);
            SM2P256V1Field.multiply(create3, sm2P256V1FieldElement2.x, create3);
            x3 = ext;
            x4 = create3;
        }
        final int[] create4 = Nat256.create();
        SM2P256V1Field.subtract(x3, x, create4);
        SM2P256V1Field.subtract(x4, x2, create);
        if (!Nat256.isZero(create4)) {
            SM2P256V1Field.square(create4, create2);
            final int[] create5 = Nat256.create();
            SM2P256V1Field.multiply(create2, create4, create5);
            SM2P256V1Field.multiply(create2, x3, create2);
            SM2P256V1Field.negate(create5, create5);
            Nat256.mul(x4, create5, ext);
            SM2P256V1Field.reduce32(Nat256.addBothTo(create2, create2, create5), create5);
            final SM2P256V1FieldElement sm2P256V1FieldElement7 = new SM2P256V1FieldElement(create3);
            SM2P256V1Field.square(create, sm2P256V1FieldElement7.x);
            SM2P256V1Field.subtract(sm2P256V1FieldElement7.x, create5, sm2P256V1FieldElement7.x);
            final SM2P256V1FieldElement sm2P256V1FieldElement8 = new SM2P256V1FieldElement(create5);
            SM2P256V1Field.subtract(create2, sm2P256V1FieldElement7.x, sm2P256V1FieldElement8.x);
            SM2P256V1Field.multiplyAddToExt(sm2P256V1FieldElement8.x, create, ext);
            SM2P256V1Field.reduce(ext, sm2P256V1FieldElement8.x);
            final SM2P256V1FieldElement sm2P256V1FieldElement9 = new SM2P256V1FieldElement(create4);
            if (!one) {
                SM2P256V1Field.multiply(sm2P256V1FieldElement9.x, sm2P256V1FieldElement5.x, sm2P256V1FieldElement9.x);
            }
            if (!one2) {
                SM2P256V1Field.multiply(sm2P256V1FieldElement9.x, sm2P256V1FieldElement6.x, sm2P256V1FieldElement9.x);
            }
            return new SM2P256V1Point(curve, sm2P256V1FieldElement7, sm2P256V1FieldElement8, new ECFieldElement[] { sm2P256V1FieldElement9 }, this.withCompression);
        }
        if (Nat256.isZero(create)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    @Override
    protected ECPoint detach() {
        return new SM2P256V1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SM2P256V1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }
    
    @Override
    public ECPoint threeTimes() {
        if (this.isInfinity()) {
            return this;
        }
        if (this.y.isZero()) {
            return this;
        }
        return this.twice().add(this);
    }
    
    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        final SM2P256V1FieldElement sm2P256V1FieldElement = (SM2P256V1FieldElement)this.y;
        if (sm2P256V1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SM2P256V1FieldElement sm2P256V1FieldElement2 = (SM2P256V1FieldElement)this.x;
        final SM2P256V1FieldElement sm2P256V1FieldElement3 = (SM2P256V1FieldElement)this.zs[0];
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final int[] create3 = Nat256.create();
        SM2P256V1Field.square(sm2P256V1FieldElement.x, create3);
        final int[] create4 = Nat256.create();
        SM2P256V1Field.square(create3, create4);
        final boolean one = sm2P256V1FieldElement3.isOne();
        int[] x = sm2P256V1FieldElement3.x;
        if (!one) {
            SM2P256V1Field.square(sm2P256V1FieldElement3.x, create2);
            x = create2;
        }
        SM2P256V1Field.subtract(sm2P256V1FieldElement2.x, x, create);
        SM2P256V1Field.add(sm2P256V1FieldElement2.x, x, create2);
        SM2P256V1Field.multiply(create2, create, create2);
        SM2P256V1Field.reduce32(Nat256.addBothTo(create2, create2, create2), create2);
        SM2P256V1Field.multiply(create3, sm2P256V1FieldElement2.x, create3);
        SM2P256V1Field.reduce32(Nat.shiftUpBits(8, create3, 2, 0), create3);
        SM2P256V1Field.reduce32(Nat.shiftUpBits(8, create4, 3, 0, create), create);
        final SM2P256V1FieldElement sm2P256V1FieldElement4 = new SM2P256V1FieldElement(create4);
        SM2P256V1Field.square(create2, sm2P256V1FieldElement4.x);
        SM2P256V1Field.subtract(sm2P256V1FieldElement4.x, create3, sm2P256V1FieldElement4.x);
        SM2P256V1Field.subtract(sm2P256V1FieldElement4.x, create3, sm2P256V1FieldElement4.x);
        final SM2P256V1FieldElement sm2P256V1FieldElement5 = new SM2P256V1FieldElement(create3);
        SM2P256V1Field.subtract(create3, sm2P256V1FieldElement4.x, sm2P256V1FieldElement5.x);
        SM2P256V1Field.multiply(sm2P256V1FieldElement5.x, create2, sm2P256V1FieldElement5.x);
        SM2P256V1Field.subtract(sm2P256V1FieldElement5.x, create, sm2P256V1FieldElement5.x);
        final SM2P256V1FieldElement sm2P256V1FieldElement6 = new SM2P256V1FieldElement(create2);
        SM2P256V1Field.twice(sm2P256V1FieldElement.x, sm2P256V1FieldElement6.x);
        if (!one) {
            SM2P256V1Field.multiply(sm2P256V1FieldElement6.x, sm2P256V1FieldElement3.x, sm2P256V1FieldElement6.x);
        }
        return new SM2P256V1Point(curve, sm2P256V1FieldElement4, sm2P256V1FieldElement5, new ECFieldElement[] { sm2P256V1FieldElement6 }, this.withCompression);
    }
    
    @Override
    public ECPoint twicePlus(final ECPoint ecPoint) {
        if (this == ecPoint) {
            return this.threeTimes();
        }
        if (this.isInfinity()) {
            return ecPoint;
        }
        if (ecPoint.isInfinity()) {
            return this.twice();
        }
        if (this.y.isZero()) {
            return ecPoint;
        }
        return this.twice().add(ecPoint);
    }
}
