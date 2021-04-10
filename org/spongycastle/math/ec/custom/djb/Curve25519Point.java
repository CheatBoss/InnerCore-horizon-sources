package org.spongycastle.math.ec.custom.djb;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class Curve25519Point extends AbstractFp
{
    public Curve25519Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public Curve25519Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    Curve25519Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        final Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)this.x;
        final Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.y;
        final Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.zs[0];
        final Curve25519FieldElement curve25519FieldElement4 = (Curve25519FieldElement)ecPoint.getXCoord();
        final Curve25519FieldElement curve25519FieldElement5 = (Curve25519FieldElement)ecPoint.getYCoord();
        final Curve25519FieldElement curve25519FieldElement6 = (Curve25519FieldElement)ecPoint.getZCoord(0);
        final int[] ext = Nat256.createExt();
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final int[] create3 = Nat256.create();
        final boolean one = curve25519FieldElement3.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = curve25519FieldElement4.x;
            x2 = curve25519FieldElement5.x;
        }
        else {
            Curve25519Field.square(curve25519FieldElement3.x, create2);
            Curve25519Field.multiply(create2, curve25519FieldElement4.x, create);
            Curve25519Field.multiply(create2, curve25519FieldElement3.x, create2);
            Curve25519Field.multiply(create2, curve25519FieldElement5.x, create2);
            x = create;
            x2 = create2;
        }
        final boolean one2 = curve25519FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = curve25519FieldElement.x;
            x4 = curve25519FieldElement2.x;
        }
        else {
            Curve25519Field.square(curve25519FieldElement6.x, create3);
            Curve25519Field.multiply(create3, curve25519FieldElement.x, ext);
            Curve25519Field.multiply(create3, curve25519FieldElement6.x, create3);
            Curve25519Field.multiply(create3, curve25519FieldElement2.x, create3);
            x3 = ext;
            x4 = create3;
        }
        final int[] create4 = Nat256.create();
        Curve25519Field.subtract(x3, x, create4);
        Curve25519Field.subtract(x4, x2, create);
        if (!Nat256.isZero(create4)) {
            int[] create5 = Nat256.create();
            Curve25519Field.square(create4, create5);
            final int[] create6 = Nat256.create();
            Curve25519Field.multiply(create5, create4, create6);
            Curve25519Field.multiply(create5, x3, create2);
            Curve25519Field.negate(create6, create6);
            Nat256.mul(x4, create6, ext);
            Curve25519Field.reduce27(Nat256.addBothTo(create2, create2, create6), create6);
            final Curve25519FieldElement curve25519FieldElement7 = new Curve25519FieldElement(create3);
            Curve25519Field.square(create, curve25519FieldElement7.x);
            Curve25519Field.subtract(curve25519FieldElement7.x, create6, curve25519FieldElement7.x);
            final Curve25519FieldElement curve25519FieldElement8 = new Curve25519FieldElement(create6);
            Curve25519Field.subtract(create2, curve25519FieldElement7.x, curve25519FieldElement8.x);
            Curve25519Field.multiplyAddToExt(curve25519FieldElement8.x, create, ext);
            Curve25519Field.reduce(ext, curve25519FieldElement8.x);
            final Curve25519FieldElement curve25519FieldElement9 = new Curve25519FieldElement(create4);
            if (!one) {
                Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement3.x, curve25519FieldElement9.x);
            }
            if (!one2) {
                Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement6.x, curve25519FieldElement9.x);
            }
            if (!one || !one2) {
                create5 = null;
            }
            return new Curve25519Point(curve, curve25519FieldElement7, curve25519FieldElement8, new ECFieldElement[] { curve25519FieldElement9, this.calculateJacobianModifiedW(curve25519FieldElement9, create5) }, this.withCompression);
        }
        if (Nat256.isZero(create)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    protected Curve25519FieldElement calculateJacobianModifiedW(final Curve25519FieldElement curve25519FieldElement, final int[] array) {
        final Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.getCurve().getA();
        if (curve25519FieldElement.isOne()) {
            return curve25519FieldElement2;
        }
        final Curve25519FieldElement curve25519FieldElement3 = new Curve25519FieldElement();
        int[] x;
        if ((x = array) == null) {
            x = curve25519FieldElement3.x;
            Curve25519Field.square(curve25519FieldElement.x, x);
        }
        Curve25519Field.square(x, curve25519FieldElement3.x);
        Curve25519Field.multiply(curve25519FieldElement3.x, curve25519FieldElement2.x, curve25519FieldElement3.x);
        return curve25519FieldElement3;
    }
    
    @Override
    protected ECPoint detach() {
        return new Curve25519Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    protected Curve25519FieldElement getJacobianModifiedW() {
        Curve25519FieldElement calculateJacobianModifiedW;
        if ((calculateJacobianModifiedW = (Curve25519FieldElement)this.zs[1]) == null) {
            final ECFieldElement[] zs = this.zs;
            calculateJacobianModifiedW = this.calculateJacobianModifiedW((Curve25519FieldElement)this.zs[0], null);
            zs[1] = calculateJacobianModifiedW;
        }
        return calculateJacobianModifiedW;
    }
    
    @Override
    public ECFieldElement getZCoord(final int n) {
        if (n == 1) {
            return this.getJacobianModifiedW();
        }
        return super.getZCoord(n);
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new Curve25519Point(this.getCurve(), this.x, this.y.negate(), this.zs, this.withCompression);
    }
    
    @Override
    public ECPoint threeTimes() {
        if (this.isInfinity()) {
            return this;
        }
        if (this.y.isZero()) {
            return this;
        }
        return this.twiceJacobianModified(false).add(this);
    }
    
    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        if (this.y.isZero()) {
            return curve.getInfinity();
        }
        return this.twiceJacobianModified(true);
    }
    
    protected Curve25519Point twiceJacobianModified(final boolean b) {
        final Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)this.x;
        final Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.y;
        final Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.zs[0];
        final Curve25519FieldElement jacobianModifiedW = this.getJacobianModifiedW();
        final int[] create = Nat256.create();
        Curve25519Field.square(curve25519FieldElement.x, create);
        Curve25519Field.reduce27(Nat256.addBothTo(create, create, create) + Nat256.addTo(jacobianModifiedW.x, create), create);
        final int[] create2 = Nat256.create();
        Curve25519Field.twice(curve25519FieldElement2.x, create2);
        final int[] create3 = Nat256.create();
        Curve25519Field.multiply(create2, curve25519FieldElement2.x, create3);
        final int[] create4 = Nat256.create();
        Curve25519Field.multiply(create3, curve25519FieldElement.x, create4);
        Curve25519Field.twice(create4, create4);
        final int[] create5 = Nat256.create();
        Curve25519Field.square(create3, create5);
        Curve25519Field.twice(create5, create5);
        final Curve25519FieldElement curve25519FieldElement4 = new Curve25519FieldElement(create3);
        Curve25519Field.square(create, curve25519FieldElement4.x);
        Curve25519Field.subtract(curve25519FieldElement4.x, create4, curve25519FieldElement4.x);
        Curve25519Field.subtract(curve25519FieldElement4.x, create4, curve25519FieldElement4.x);
        final Curve25519FieldElement curve25519FieldElement5 = new Curve25519FieldElement(create4);
        Curve25519Field.subtract(create4, curve25519FieldElement4.x, curve25519FieldElement5.x);
        Curve25519Field.multiply(curve25519FieldElement5.x, create, curve25519FieldElement5.x);
        Curve25519Field.subtract(curve25519FieldElement5.x, create5, curve25519FieldElement5.x);
        final Curve25519FieldElement curve25519FieldElement6 = new Curve25519FieldElement(create2);
        if (!Nat256.isOne(curve25519FieldElement3.x)) {
            Curve25519Field.multiply(curve25519FieldElement6.x, curve25519FieldElement3.x, curve25519FieldElement6.x);
        }
        Curve25519FieldElement curve25519FieldElement7 = null;
        if (b) {
            curve25519FieldElement7 = new Curve25519FieldElement(create5);
            Curve25519Field.multiply(curve25519FieldElement7.x, jacobianModifiedW.x, curve25519FieldElement7.x);
            Curve25519Field.twice(curve25519FieldElement7.x, curve25519FieldElement7.x);
        }
        return new Curve25519Point(this.getCurve(), curve25519FieldElement4, curve25519FieldElement5, new ECFieldElement[] { curve25519FieldElement6, curve25519FieldElement7 }, this.withCompression);
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
        return this.twiceJacobianModified(false).add(ecPoint);
    }
}
