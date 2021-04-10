package org.spongycastle.math.ec.custom.gm;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SM2P256V1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SM2P256V1Curve.q;
    }
    
    public SM2P256V1FieldElement() {
        this.x = Nat256.create();
    }
    
    public SM2P256V1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SM2P256V1FieldElement.Q) < 0) {
            this.x = SM2P256V1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SM2P256V1FieldElement");
    }
    
    protected SM2P256V1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        SM2P256V1Field.add(this.x, ((SM2P256V1FieldElement)ecFieldElement).x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat256.create();
        SM2P256V1Field.addOne(this.x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        Mod.invert(SM2P256V1Field.P, ((SM2P256V1FieldElement)ecFieldElement).x, create);
        SM2P256V1Field.multiply(create, this.x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SM2P256V1FieldElement && Nat256.eq(this.x, ((SM2P256V1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SM2P256V1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SM2P256V1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SM2P256V1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat256.create();
        Mod.invert(SM2P256V1Field.P, this.x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat256.isOne(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat256.isZero(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        SM2P256V1Field.multiply(this.x, ((SM2P256V1FieldElement)ecFieldElement).x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat256.create();
        SM2P256V1Field.negate(this.x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat256.isZero(x)) {
            return this;
        }
        if (Nat256.isOne(x)) {
            return this;
        }
        final int[] create = Nat256.create();
        SM2P256V1Field.square(x, create);
        SM2P256V1Field.multiply(create, x, create);
        final int[] create2 = Nat256.create();
        SM2P256V1Field.squareN(create, 2, create2);
        SM2P256V1Field.multiply(create2, create, create2);
        final int[] create3 = Nat256.create();
        SM2P256V1Field.squareN(create2, 2, create3);
        SM2P256V1Field.multiply(create3, create, create3);
        SM2P256V1Field.squareN(create3, 6, create);
        SM2P256V1Field.multiply(create, create3, create);
        final int[] create4 = Nat256.create();
        SM2P256V1Field.squareN(create, 12, create4);
        SM2P256V1Field.multiply(create4, create, create4);
        SM2P256V1Field.squareN(create4, 6, create);
        SM2P256V1Field.multiply(create, create3, create);
        SM2P256V1Field.square(create, create3);
        SM2P256V1Field.multiply(create3, x, create3);
        SM2P256V1Field.squareN(create3, 31, create4);
        SM2P256V1Field.multiply(create4, create3, create);
        SM2P256V1Field.squareN(create4, 32, create4);
        SM2P256V1Field.multiply(create4, create, create4);
        SM2P256V1Field.squareN(create4, 62, create4);
        SM2P256V1Field.multiply(create4, create, create4);
        SM2P256V1Field.squareN(create4, 4, create4);
        SM2P256V1Field.multiply(create4, create2, create4);
        SM2P256V1Field.squareN(create4, 32, create4);
        SM2P256V1Field.multiply(create4, x, create4);
        SM2P256V1Field.squareN(create4, 62, create4);
        SM2P256V1Field.square(create4, create2);
        if (Nat256.eq(x, create2)) {
            return new SM2P256V1FieldElement(create4);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat256.create();
        SM2P256V1Field.square(this.x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        SM2P256V1Field.subtract(this.x, ((SM2P256V1FieldElement)ecFieldElement).x, create);
        return new SM2P256V1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat256.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat256.toBigInteger(this.x);
    }
}
