package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SecP256R1Point extends AbstractFp
{
    public SecP256R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecP256R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    SecP256R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        final SecP256R1FieldElement secP256R1FieldElement = (SecP256R1FieldElement)this.x;
        final SecP256R1FieldElement secP256R1FieldElement2 = (SecP256R1FieldElement)this.y;
        final SecP256R1FieldElement secP256R1FieldElement3 = (SecP256R1FieldElement)ecPoint.getXCoord();
        final SecP256R1FieldElement secP256R1FieldElement4 = (SecP256R1FieldElement)ecPoint.getYCoord();
        final SecP256R1FieldElement secP256R1FieldElement5 = (SecP256R1FieldElement)this.zs[0];
        final SecP256R1FieldElement secP256R1FieldElement6 = (SecP256R1FieldElement)ecPoint.getZCoord(0);
        final int[] ext = Nat256.createExt();
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final int[] create3 = Nat256.create();
        final boolean one = secP256R1FieldElement5.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = secP256R1FieldElement3.x;
            x2 = secP256R1FieldElement4.x;
        }
        else {
            SecP256R1Field.square(secP256R1FieldElement5.x, create2);
            SecP256R1Field.multiply(create2, secP256R1FieldElement3.x, create);
            SecP256R1Field.multiply(create2, secP256R1FieldElement5.x, create2);
            SecP256R1Field.multiply(create2, secP256R1FieldElement4.x, create2);
            x = create;
            x2 = create2;
        }
        final boolean one2 = secP256R1FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = secP256R1FieldElement.x;
            x4 = secP256R1FieldElement2.x;
        }
        else {
            SecP256R1Field.square(secP256R1FieldElement6.x, create3);
            SecP256R1Field.multiply(create3, secP256R1FieldElement.x, ext);
            SecP256R1Field.multiply(create3, secP256R1FieldElement6.x, create3);
            SecP256R1Field.multiply(create3, secP256R1FieldElement2.x, create3);
            x3 = ext;
            x4 = create3;
        }
        final int[] create4 = Nat256.create();
        SecP256R1Field.subtract(x3, x, create4);
        SecP256R1Field.subtract(x4, x2, create);
        if (!Nat256.isZero(create4)) {
            SecP256R1Field.square(create4, create2);
            final int[] create5 = Nat256.create();
            SecP256R1Field.multiply(create2, create4, create5);
            SecP256R1Field.multiply(create2, x3, create2);
            SecP256R1Field.negate(create5, create5);
            Nat256.mul(x4, create5, ext);
            SecP256R1Field.reduce32(Nat256.addBothTo(create2, create2, create5), create5);
            final SecP256R1FieldElement secP256R1FieldElement7 = new SecP256R1FieldElement(create3);
            SecP256R1Field.square(create, secP256R1FieldElement7.x);
            SecP256R1Field.subtract(secP256R1FieldElement7.x, create5, secP256R1FieldElement7.x);
            final SecP256R1FieldElement secP256R1FieldElement8 = new SecP256R1FieldElement(create5);
            SecP256R1Field.subtract(create2, secP256R1FieldElement7.x, secP256R1FieldElement8.x);
            SecP256R1Field.multiplyAddToExt(secP256R1FieldElement8.x, create, ext);
            SecP256R1Field.reduce(ext, secP256R1FieldElement8.x);
            final SecP256R1FieldElement secP256R1FieldElement9 = new SecP256R1FieldElement(create4);
            if (!one) {
                SecP256R1Field.multiply(secP256R1FieldElement9.x, secP256R1FieldElement5.x, secP256R1FieldElement9.x);
            }
            if (!one2) {
                SecP256R1Field.multiply(secP256R1FieldElement9.x, secP256R1FieldElement6.x, secP256R1FieldElement9.x);
            }
            return new SecP256R1Point(curve, secP256R1FieldElement7, secP256R1FieldElement8, new ECFieldElement[] { secP256R1FieldElement9 }, this.withCompression);
        }
        if (Nat256.isZero(create)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    @Override
    protected ECPoint detach() {
        return new SecP256R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP256R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
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
        final SecP256R1FieldElement secP256R1FieldElement = (SecP256R1FieldElement)this.y;
        if (secP256R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SecP256R1FieldElement secP256R1FieldElement2 = (SecP256R1FieldElement)this.x;
        final SecP256R1FieldElement secP256R1FieldElement3 = (SecP256R1FieldElement)this.zs[0];
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final int[] create3 = Nat256.create();
        SecP256R1Field.square(secP256R1FieldElement.x, create3);
        final int[] create4 = Nat256.create();
        SecP256R1Field.square(create3, create4);
        final boolean one = secP256R1FieldElement3.isOne();
        int[] x = secP256R1FieldElement3.x;
        if (!one) {
            SecP256R1Field.square(secP256R1FieldElement3.x, create2);
            x = create2;
        }
        SecP256R1Field.subtract(secP256R1FieldElement2.x, x, create);
        SecP256R1Field.add(secP256R1FieldElement2.x, x, create2);
        SecP256R1Field.multiply(create2, create, create2);
        SecP256R1Field.reduce32(Nat256.addBothTo(create2, create2, create2), create2);
        SecP256R1Field.multiply(create3, secP256R1FieldElement2.x, create3);
        SecP256R1Field.reduce32(Nat.shiftUpBits(8, create3, 2, 0), create3);
        SecP256R1Field.reduce32(Nat.shiftUpBits(8, create4, 3, 0, create), create);
        final SecP256R1FieldElement secP256R1FieldElement4 = new SecP256R1FieldElement(create4);
        SecP256R1Field.square(create2, secP256R1FieldElement4.x);
        SecP256R1Field.subtract(secP256R1FieldElement4.x, create3, secP256R1FieldElement4.x);
        SecP256R1Field.subtract(secP256R1FieldElement4.x, create3, secP256R1FieldElement4.x);
        final SecP256R1FieldElement secP256R1FieldElement5 = new SecP256R1FieldElement(create3);
        SecP256R1Field.subtract(create3, secP256R1FieldElement4.x, secP256R1FieldElement5.x);
        SecP256R1Field.multiply(secP256R1FieldElement5.x, create2, secP256R1FieldElement5.x);
        SecP256R1Field.subtract(secP256R1FieldElement5.x, create, secP256R1FieldElement5.x);
        final SecP256R1FieldElement secP256R1FieldElement6 = new SecP256R1FieldElement(create2);
        SecP256R1Field.twice(secP256R1FieldElement.x, secP256R1FieldElement6.x);
        if (!one) {
            SecP256R1Field.multiply(secP256R1FieldElement6.x, secP256R1FieldElement3.x, secP256R1FieldElement6.x);
        }
        return new SecP256R1Point(curve, secP256R1FieldElement4, secP256R1FieldElement5, new ECFieldElement[] { secP256R1FieldElement6 }, this.withCompression);
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
