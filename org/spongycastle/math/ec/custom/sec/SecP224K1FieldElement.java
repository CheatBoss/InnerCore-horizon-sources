package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP224K1FieldElement extends ECFieldElement
{
    private static final int[] PRECOMP_POW2;
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP224K1Curve.q;
        PRECOMP_POW2 = new int[] { 868209154, -587542221, 579297866, -1014948952, -1470801668, 514782679, -1897982644 };
    }
    
    public SecP224K1FieldElement() {
        this.x = Nat224.create();
    }
    
    public SecP224K1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP224K1FieldElement.Q) < 0) {
            this.x = SecP224K1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP224K1FieldElement");
    }
    
    protected SecP224K1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat224.create();
        SecP224K1Field.add(this.x, ((SecP224K1FieldElement)ecFieldElement).x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat224.create();
        SecP224K1Field.addOne(this.x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat224.create();
        Mod.invert(SecP224K1Field.P, ((SecP224K1FieldElement)ecFieldElement).x, create);
        SecP224K1Field.multiply(create, this.x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP224K1FieldElement && Nat224.eq(this.x, ((SecP224K1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP224K1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP224K1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP224K1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 7);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat224.create();
        Mod.invert(SecP224K1Field.P, this.x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat224.isOne(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat224.isZero(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat224.create();
        SecP224K1Field.multiply(this.x, ((SecP224K1FieldElement)ecFieldElement).x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat224.create();
        SecP224K1Field.negate(this.x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat224.isZero(x)) {
            return this;
        }
        if (Nat224.isOne(x)) {
            return this;
        }
        final int[] create = Nat224.create();
        SecP224K1Field.square(x, create);
        SecP224K1Field.multiply(create, x, create);
        SecP224K1Field.square(create, create);
        SecP224K1Field.multiply(create, x, create);
        final int[] create2 = Nat224.create();
        SecP224K1Field.square(create, create2);
        SecP224K1Field.multiply(create2, x, create2);
        final int[] create3 = Nat224.create();
        SecP224K1Field.squareN(create2, 4, create3);
        SecP224K1Field.multiply(create3, create2, create3);
        final int[] create4 = Nat224.create();
        SecP224K1Field.squareN(create3, 3, create4);
        SecP224K1Field.multiply(create4, create, create4);
        SecP224K1Field.squareN(create4, 8, create4);
        SecP224K1Field.multiply(create4, create3, create4);
        SecP224K1Field.squareN(create4, 4, create3);
        SecP224K1Field.multiply(create3, create2, create3);
        SecP224K1Field.squareN(create3, 19, create2);
        SecP224K1Field.multiply(create2, create4, create2);
        final int[] create5 = Nat224.create();
        SecP224K1Field.squareN(create2, 42, create5);
        SecP224K1Field.multiply(create5, create2, create5);
        SecP224K1Field.squareN(create5, 23, create2);
        SecP224K1Field.multiply(create2, create3, create2);
        SecP224K1Field.squareN(create2, 84, create3);
        SecP224K1Field.multiply(create3, create5, create3);
        SecP224K1Field.squareN(create3, 20, create3);
        SecP224K1Field.multiply(create3, create4, create3);
        SecP224K1Field.squareN(create3, 3, create3);
        SecP224K1Field.multiply(create3, x, create3);
        SecP224K1Field.squareN(create3, 2, create3);
        SecP224K1Field.multiply(create3, x, create3);
        SecP224K1Field.squareN(create3, 4, create3);
        SecP224K1Field.multiply(create3, create, create3);
        SecP224K1Field.square(create3, create3);
        SecP224K1Field.square(create3, create5);
        if (Nat224.eq(x, create5)) {
            return new SecP224K1FieldElement(create3);
        }
        SecP224K1Field.multiply(create3, SecP224K1FieldElement.PRECOMP_POW2, create3);
        SecP224K1Field.square(create3, create5);
        if (Nat224.eq(x, create5)) {
            return new SecP224K1FieldElement(create3);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat224.create();
        SecP224K1Field.square(this.x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat224.create();
        SecP224K1Field.subtract(this.x, ((SecP224K1FieldElement)ecFieldElement).x, create);
        return new SecP224K1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat224.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat224.toBigInteger(this.x);
    }
}
