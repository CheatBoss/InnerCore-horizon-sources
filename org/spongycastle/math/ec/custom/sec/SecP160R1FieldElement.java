package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP160R1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP160R1Curve.q;
    }
    
    public SecP160R1FieldElement() {
        this.x = Nat160.create();
    }
    
    public SecP160R1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP160R1FieldElement.Q) < 0) {
            this.x = SecP160R1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP160R1FieldElement");
    }
    
    protected SecP160R1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        SecP160R1Field.add(this.x, ((SecP160R1FieldElement)ecFieldElement).x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat160.create();
        SecP160R1Field.addOne(this.x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        Mod.invert(SecP160R1Field.P, ((SecP160R1FieldElement)ecFieldElement).x, create);
        SecP160R1Field.multiply(create, this.x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP160R1FieldElement && Nat160.eq(this.x, ((SecP160R1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP160R1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP160R1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP160R1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 5);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat160.create();
        Mod.invert(SecP160R1Field.P, this.x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat160.isOne(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat160.isZero(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        SecP160R1Field.multiply(this.x, ((SecP160R1FieldElement)ecFieldElement).x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat160.create();
        SecP160R1Field.negate(this.x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat160.isZero(x)) {
            return this;
        }
        if (Nat160.isOne(x)) {
            return this;
        }
        final int[] create = Nat160.create();
        SecP160R1Field.square(x, create);
        SecP160R1Field.multiply(create, x, create);
        final int[] create2 = Nat160.create();
        SecP160R1Field.squareN(create, 2, create2);
        SecP160R1Field.multiply(create2, create, create2);
        SecP160R1Field.squareN(create2, 4, create);
        SecP160R1Field.multiply(create, create2, create);
        SecP160R1Field.squareN(create, 8, create2);
        SecP160R1Field.multiply(create2, create, create2);
        SecP160R1Field.squareN(create2, 16, create);
        SecP160R1Field.multiply(create, create2, create);
        SecP160R1Field.squareN(create, 32, create2);
        SecP160R1Field.multiply(create2, create, create2);
        SecP160R1Field.squareN(create2, 64, create);
        SecP160R1Field.multiply(create, create2, create);
        SecP160R1Field.square(create, create2);
        SecP160R1Field.multiply(create2, x, create2);
        SecP160R1Field.squareN(create2, 29, create2);
        SecP160R1Field.square(create2, create);
        if (Nat160.eq(x, create)) {
            return new SecP160R1FieldElement(create2);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat160.create();
        SecP160R1Field.square(this.x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        SecP160R1Field.subtract(this.x, ((SecP160R1FieldElement)ecFieldElement).x, create);
        return new SecP160R1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat160.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat160.toBigInteger(this.x);
    }
}
