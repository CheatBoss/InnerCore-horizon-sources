package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP160R2FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP160R2Curve.q;
    }
    
    public SecP160R2FieldElement() {
        this.x = Nat160.create();
    }
    
    public SecP160R2FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP160R2FieldElement.Q) < 0) {
            this.x = SecP160R2Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP160R2FieldElement");
    }
    
    protected SecP160R2FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        SecP160R2Field.add(this.x, ((SecP160R2FieldElement)ecFieldElement).x, create);
        return new SecP160R2FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat160.create();
        SecP160R2Field.addOne(this.x, create);
        return new SecP160R2FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        Mod.invert(SecP160R2Field.P, ((SecP160R2FieldElement)ecFieldElement).x, create);
        SecP160R2Field.multiply(create, this.x, create);
        return new SecP160R2FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP160R2FieldElement && Nat160.eq(this.x, ((SecP160R2FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP160R2Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP160R2FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP160R2FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 5);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat160.create();
        Mod.invert(SecP160R2Field.P, this.x, create);
        return new SecP160R2FieldElement(create);
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
        SecP160R2Field.multiply(this.x, ((SecP160R2FieldElement)ecFieldElement).x, create);
        return new SecP160R2FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat160.create();
        SecP160R2Field.negate(this.x, create);
        return new SecP160R2FieldElement(create);
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
        SecP160R2Field.square(x, create);
        SecP160R2Field.multiply(create, x, create);
        final int[] create2 = Nat160.create();
        SecP160R2Field.square(create, create2);
        SecP160R2Field.multiply(create2, x, create2);
        final int[] create3 = Nat160.create();
        SecP160R2Field.square(create2, create3);
        SecP160R2Field.multiply(create3, x, create3);
        final int[] create4 = Nat160.create();
        SecP160R2Field.squareN(create3, 3, create4);
        SecP160R2Field.multiply(create4, create2, create4);
        SecP160R2Field.squareN(create4, 7, create3);
        SecP160R2Field.multiply(create3, create4, create3);
        SecP160R2Field.squareN(create3, 3, create4);
        SecP160R2Field.multiply(create4, create2, create4);
        final int[] create5 = Nat160.create();
        SecP160R2Field.squareN(create4, 14, create5);
        SecP160R2Field.multiply(create5, create3, create5);
        SecP160R2Field.squareN(create5, 31, create3);
        SecP160R2Field.multiply(create3, create5, create3);
        SecP160R2Field.squareN(create3, 62, create5);
        SecP160R2Field.multiply(create5, create3, create5);
        SecP160R2Field.squareN(create5, 3, create3);
        SecP160R2Field.multiply(create3, create2, create3);
        SecP160R2Field.squareN(create3, 18, create3);
        SecP160R2Field.multiply(create3, create4, create3);
        SecP160R2Field.squareN(create3, 2, create3);
        SecP160R2Field.multiply(create3, x, create3);
        SecP160R2Field.squareN(create3, 3, create3);
        SecP160R2Field.multiply(create3, create, create3);
        SecP160R2Field.squareN(create3, 6, create3);
        SecP160R2Field.multiply(create3, create2, create3);
        SecP160R2Field.squareN(create3, 2, create3);
        SecP160R2Field.multiply(create3, x, create3);
        SecP160R2Field.square(create3, create);
        if (Nat160.eq(x, create)) {
            return new SecP160R2FieldElement(create3);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat160.create();
        SecP160R2Field.square(this.x, create);
        return new SecP160R2FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat160.create();
        SecP160R2Field.subtract(this.x, ((SecP160R2FieldElement)ecFieldElement).x, create);
        return new SecP160R2FieldElement(create);
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
