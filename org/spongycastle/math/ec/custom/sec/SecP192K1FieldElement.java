package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP192K1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP192K1Curve.q;
    }
    
    public SecP192K1FieldElement() {
        this.x = Nat192.create();
    }
    
    public SecP192K1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP192K1FieldElement.Q) < 0) {
            this.x = SecP192K1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP192K1FieldElement");
    }
    
    protected SecP192K1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        SecP192K1Field.add(this.x, ((SecP192K1FieldElement)ecFieldElement).x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat192.create();
        SecP192K1Field.addOne(this.x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        Mod.invert(SecP192K1Field.P, ((SecP192K1FieldElement)ecFieldElement).x, create);
        SecP192K1Field.multiply(create, this.x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP192K1FieldElement && Nat192.eq(this.x, ((SecP192K1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP192K1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP192K1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP192K1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 6);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat192.create();
        Mod.invert(SecP192K1Field.P, this.x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat192.isOne(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat192.isZero(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        SecP192K1Field.multiply(this.x, ((SecP192K1FieldElement)ecFieldElement).x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat192.create();
        SecP192K1Field.negate(this.x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat192.isZero(x)) {
            return this;
        }
        if (Nat192.isOne(x)) {
            return this;
        }
        final int[] create = Nat192.create();
        SecP192K1Field.square(x, create);
        SecP192K1Field.multiply(create, x, create);
        final int[] create2 = Nat192.create();
        SecP192K1Field.square(create, create2);
        SecP192K1Field.multiply(create2, x, create2);
        final int[] create3 = Nat192.create();
        SecP192K1Field.squareN(create2, 3, create3);
        SecP192K1Field.multiply(create3, create2, create3);
        SecP192K1Field.squareN(create3, 2, create3);
        SecP192K1Field.multiply(create3, create, create3);
        SecP192K1Field.squareN(create3, 8, create);
        SecP192K1Field.multiply(create, create3, create);
        SecP192K1Field.squareN(create, 3, create3);
        SecP192K1Field.multiply(create3, create2, create3);
        final int[] create4 = Nat192.create();
        SecP192K1Field.squareN(create3, 16, create4);
        SecP192K1Field.multiply(create4, create, create4);
        SecP192K1Field.squareN(create4, 35, create);
        SecP192K1Field.multiply(create, create4, create);
        SecP192K1Field.squareN(create, 70, create4);
        SecP192K1Field.multiply(create4, create, create4);
        SecP192K1Field.squareN(create4, 19, create);
        SecP192K1Field.multiply(create, create3, create);
        SecP192K1Field.squareN(create, 20, create);
        SecP192K1Field.multiply(create, create3, create);
        SecP192K1Field.squareN(create, 4, create);
        SecP192K1Field.multiply(create, create2, create);
        SecP192K1Field.squareN(create, 6, create);
        SecP192K1Field.multiply(create, create2, create);
        SecP192K1Field.square(create, create);
        SecP192K1Field.square(create, create2);
        if (Nat192.eq(x, create2)) {
            return new SecP192K1FieldElement(create);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat192.create();
        SecP192K1Field.square(this.x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        SecP192K1Field.subtract(this.x, ((SecP192K1FieldElement)ecFieldElement).x, create);
        return new SecP192K1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat192.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat192.toBigInteger(this.x);
    }
}
