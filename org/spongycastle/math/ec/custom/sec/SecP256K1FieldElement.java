package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP256K1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP256K1Curve.q;
    }
    
    public SecP256K1FieldElement() {
        this.x = Nat256.create();
    }
    
    public SecP256K1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP256K1FieldElement.Q) < 0) {
            this.x = SecP256K1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP256K1FieldElement");
    }
    
    protected SecP256K1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        SecP256K1Field.add(this.x, ((SecP256K1FieldElement)ecFieldElement).x, create);
        return new SecP256K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat256.create();
        SecP256K1Field.addOne(this.x, create);
        return new SecP256K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        Mod.invert(SecP256K1Field.P, ((SecP256K1FieldElement)ecFieldElement).x, create);
        SecP256K1Field.multiply(create, this.x, create);
        return new SecP256K1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP256K1FieldElement && Nat256.eq(this.x, ((SecP256K1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP256K1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP256K1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP256K1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat256.create();
        Mod.invert(SecP256K1Field.P, this.x, create);
        return new SecP256K1FieldElement(create);
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
        SecP256K1Field.multiply(this.x, ((SecP256K1FieldElement)ecFieldElement).x, create);
        return new SecP256K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat256.create();
        SecP256K1Field.negate(this.x, create);
        return new SecP256K1FieldElement(create);
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
        SecP256K1Field.square(x, create);
        SecP256K1Field.multiply(create, x, create);
        final int[] create2 = Nat256.create();
        SecP256K1Field.square(create, create2);
        SecP256K1Field.multiply(create2, x, create2);
        final int[] create3 = Nat256.create();
        SecP256K1Field.squareN(create2, 3, create3);
        SecP256K1Field.multiply(create3, create2, create3);
        SecP256K1Field.squareN(create3, 3, create3);
        SecP256K1Field.multiply(create3, create2, create3);
        SecP256K1Field.squareN(create3, 2, create3);
        SecP256K1Field.multiply(create3, create, create3);
        final int[] create4 = Nat256.create();
        SecP256K1Field.squareN(create3, 11, create4);
        SecP256K1Field.multiply(create4, create3, create4);
        SecP256K1Field.squareN(create4, 22, create3);
        SecP256K1Field.multiply(create3, create4, create3);
        final int[] create5 = Nat256.create();
        SecP256K1Field.squareN(create3, 44, create5);
        SecP256K1Field.multiply(create5, create3, create5);
        final int[] create6 = Nat256.create();
        SecP256K1Field.squareN(create5, 88, create6);
        SecP256K1Field.multiply(create6, create5, create6);
        SecP256K1Field.squareN(create6, 44, create5);
        SecP256K1Field.multiply(create5, create3, create5);
        SecP256K1Field.squareN(create5, 3, create3);
        SecP256K1Field.multiply(create3, create2, create3);
        SecP256K1Field.squareN(create3, 23, create3);
        SecP256K1Field.multiply(create3, create4, create3);
        SecP256K1Field.squareN(create3, 6, create3);
        SecP256K1Field.multiply(create3, create, create3);
        SecP256K1Field.squareN(create3, 2, create3);
        SecP256K1Field.square(create3, create);
        if (Nat256.eq(x, create)) {
            return new SecP256K1FieldElement(create3);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat256.create();
        SecP256K1Field.square(this.x, create);
        return new SecP256K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        SecP256K1Field.subtract(this.x, ((SecP256K1FieldElement)ecFieldElement).x, create);
        return new SecP256K1FieldElement(create);
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
