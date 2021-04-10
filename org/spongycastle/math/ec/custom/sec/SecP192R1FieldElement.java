package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP192R1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP192R1Curve.q;
    }
    
    public SecP192R1FieldElement() {
        this.x = Nat192.create();
    }
    
    public SecP192R1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP192R1FieldElement.Q) < 0) {
            this.x = SecP192R1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP192R1FieldElement");
    }
    
    protected SecP192R1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        SecP192R1Field.add(this.x, ((SecP192R1FieldElement)ecFieldElement).x, create);
        return new SecP192R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat192.create();
        SecP192R1Field.addOne(this.x, create);
        return new SecP192R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        Mod.invert(SecP192R1Field.P, ((SecP192R1FieldElement)ecFieldElement).x, create);
        SecP192R1Field.multiply(create, this.x, create);
        return new SecP192R1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP192R1FieldElement && Nat192.eq(this.x, ((SecP192R1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP192R1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP192R1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP192R1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 6);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat192.create();
        Mod.invert(SecP192R1Field.P, this.x, create);
        return new SecP192R1FieldElement(create);
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
        SecP192R1Field.multiply(this.x, ((SecP192R1FieldElement)ecFieldElement).x, create);
        return new SecP192R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat192.create();
        SecP192R1Field.negate(this.x, create);
        return new SecP192R1FieldElement(create);
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
        final int[] create2 = Nat192.create();
        SecP192R1Field.square(x, create);
        SecP192R1Field.multiply(create, x, create);
        SecP192R1Field.squareN(create, 2, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 4, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 8, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 16, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 32, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 64, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 62, create);
        SecP192R1Field.square(create, create2);
        if (Nat192.eq(x, create2)) {
            return new SecP192R1FieldElement(create);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat192.create();
        SecP192R1Field.square(this.x, create);
        return new SecP192R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat192.create();
        SecP192R1Field.subtract(this.x, ((SecP192R1FieldElement)ecFieldElement).x, create);
        return new SecP192R1FieldElement(create);
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
