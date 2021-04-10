package org.spongycastle.math.ec.custom.djb;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class Curve25519FieldElement extends ECFieldElement
{
    private static final int[] PRECOMP_POW2;
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = Curve25519.q;
        PRECOMP_POW2 = new int[] { 1242472624, -991028441, -1389370248, 792926214, 1039914919, 726466713, 1338105611, 730014848 };
    }
    
    public Curve25519FieldElement() {
        this.x = Nat256.create();
    }
    
    public Curve25519FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(Curve25519FieldElement.Q) < 0) {
            this.x = Curve25519Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for Curve25519FieldElement");
    }
    
    protected Curve25519FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        Curve25519Field.add(this.x, ((Curve25519FieldElement)ecFieldElement).x, create);
        return new Curve25519FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat256.create();
        Curve25519Field.addOne(this.x, create);
        return new Curve25519FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        Mod.invert(Curve25519Field.P, ((Curve25519FieldElement)ecFieldElement).x, create);
        Curve25519Field.multiply(create, this.x, create);
        return new Curve25519FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof Curve25519FieldElement && Nat256.eq(this.x, ((Curve25519FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "Curve25519Field";
    }
    
    @Override
    public int getFieldSize() {
        return Curve25519FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return Curve25519FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat256.create();
        Mod.invert(Curve25519Field.P, this.x, create);
        return new Curve25519FieldElement(create);
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
        Curve25519Field.multiply(this.x, ((Curve25519FieldElement)ecFieldElement).x, create);
        return new Curve25519FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat256.create();
        Curve25519Field.negate(this.x, create);
        return new Curve25519FieldElement(create);
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
        Curve25519Field.square(x, create);
        Curve25519Field.multiply(create, x, create);
        Curve25519Field.square(create, create);
        Curve25519Field.multiply(create, x, create);
        final int[] create2 = Nat256.create();
        Curve25519Field.square(create, create2);
        Curve25519Field.multiply(create2, x, create2);
        final int[] create3 = Nat256.create();
        Curve25519Field.squareN(create2, 3, create3);
        Curve25519Field.multiply(create3, create, create3);
        Curve25519Field.squareN(create3, 4, create);
        Curve25519Field.multiply(create, create2, create);
        Curve25519Field.squareN(create, 4, create3);
        Curve25519Field.multiply(create3, create2, create3);
        Curve25519Field.squareN(create3, 15, create2);
        Curve25519Field.multiply(create2, create3, create2);
        Curve25519Field.squareN(create2, 30, create3);
        Curve25519Field.multiply(create3, create2, create3);
        Curve25519Field.squareN(create3, 60, create2);
        Curve25519Field.multiply(create2, create3, create2);
        Curve25519Field.squareN(create2, 11, create3);
        Curve25519Field.multiply(create3, create, create3);
        Curve25519Field.squareN(create3, 120, create);
        Curve25519Field.multiply(create, create2, create);
        Curve25519Field.square(create, create);
        Curve25519Field.square(create, create2);
        if (Nat256.eq(x, create2)) {
            return new Curve25519FieldElement(create);
        }
        Curve25519Field.multiply(create, Curve25519FieldElement.PRECOMP_POW2, create);
        Curve25519Field.square(create, create2);
        if (Nat256.eq(x, create2)) {
            return new Curve25519FieldElement(create);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat256.create();
        Curve25519Field.square(this.x, create);
        return new Curve25519FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat256.create();
        Curve25519Field.subtract(this.x, ((Curve25519FieldElement)ecFieldElement).x, create);
        return new Curve25519FieldElement(create);
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
