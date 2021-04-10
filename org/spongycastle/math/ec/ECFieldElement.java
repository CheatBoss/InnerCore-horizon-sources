package org.spongycastle.math.ec;

import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.math.raw.*;
import java.util.*;

public abstract class ECFieldElement implements ECConstants
{
    public abstract ECFieldElement add(final ECFieldElement p0);
    
    public abstract ECFieldElement addOne();
    
    public int bitLength() {
        return this.toBigInteger().bitLength();
    }
    
    public abstract ECFieldElement divide(final ECFieldElement p0);
    
    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((this.getFieldSize() + 7) / 8, this.toBigInteger());
    }
    
    public abstract String getFieldName();
    
    public abstract int getFieldSize();
    
    public abstract ECFieldElement invert();
    
    public boolean isOne() {
        return this.bitLength() == 1;
    }
    
    public boolean isZero() {
        return this.toBigInteger().signum() == 0;
    }
    
    public abstract ECFieldElement multiply(final ECFieldElement p0);
    
    public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiply(ecFieldElement).subtract(ecFieldElement2.multiply(ecFieldElement3));
    }
    
    public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiply(ecFieldElement).add(ecFieldElement2.multiply(ecFieldElement3));
    }
    
    public abstract ECFieldElement negate();
    
    public abstract ECFieldElement sqrt();
    
    public abstract ECFieldElement square();
    
    public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.square().subtract(ecFieldElement.multiply(ecFieldElement2));
    }
    
    public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.square().add(ecFieldElement.multiply(ecFieldElement2));
    }
    
    public ECFieldElement squarePow(final int n) {
        int i = 0;
        ECFieldElement square = this;
        while (i < n) {
            square = square.square();
            ++i;
        }
        return square;
    }
    
    public abstract ECFieldElement subtract(final ECFieldElement p0);
    
    public boolean testBitZero() {
        return this.toBigInteger().testBit(0);
    }
    
    public abstract BigInteger toBigInteger();
    
    @Override
    public String toString() {
        return this.toBigInteger().toString(16);
    }
    
    public static class F2m extends ECFieldElement
    {
        public static final int GNB = 1;
        public static final int PPB = 3;
        public static final int TPB = 2;
        private int[] ks;
        private int m;
        private int representation;
        private LongArray x;
        
        public F2m(final int m, final int n, final int n2, final int n3, final BigInteger bigInteger) {
            if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= m) {
                if (n2 == 0 && n3 == 0) {
                    this.representation = 2;
                    this.ks = new int[] { n };
                }
                else {
                    if (n2 >= n3) {
                        throw new IllegalArgumentException("k2 must be smaller than k3");
                    }
                    if (n2 <= 0) {
                        throw new IllegalArgumentException("k2 must be larger than 0");
                    }
                    this.representation = 3;
                    this.ks = new int[] { n, n2, n3 };
                }
                this.m = m;
                this.x = new LongArray(bigInteger);
                return;
            }
            throw new IllegalArgumentException("x value invalid in F2m field element");
        }
        
        public F2m(final int n, final int n2, final BigInteger bigInteger) {
            this(n, n2, 0, 0, bigInteger);
        }
        
        private F2m(int n, final int[] ks, final LongArray x) {
            this.m = n;
            if (ks.length == 1) {
                n = 2;
            }
            else {
                n = 3;
            }
            this.representation = n;
            this.ks = ks;
            this.x = x;
        }
        
        public static void checkFieldElements(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            if (!(ecFieldElement instanceof F2m) || !(ecFieldElement2 instanceof F2m)) {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
            final F2m f2m = (F2m)ecFieldElement;
            final F2m f2m2 = (F2m)ecFieldElement2;
            if (f2m.representation != f2m2.representation) {
                throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
            }
            if (f2m.m == f2m2.m && Arrays.areEqual(f2m.ks, f2m2.ks)) {
                return;
            }
            throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
        }
        
        @Override
        public ECFieldElement add(final ECFieldElement ecFieldElement) {
            final LongArray longArray = (LongArray)this.x.clone();
            longArray.addShiftedByWords(((F2m)ecFieldElement).x, 0);
            return new F2m(this.m, this.ks, longArray);
        }
        
        @Override
        public ECFieldElement addOne() {
            return new F2m(this.m, this.ks, this.x.addOne());
        }
        
        @Override
        public int bitLength() {
            return this.x.degree();
        }
        
        @Override
        public ECFieldElement divide(final ECFieldElement ecFieldElement) {
            return this.multiply(ecFieldElement.invert());
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof F2m)) {
                return false;
            }
            final F2m f2m = (F2m)o;
            return this.m == f2m.m && this.representation == f2m.representation && Arrays.areEqual(this.ks, f2m.ks) && this.x.equals(f2m.x);
        }
        
        @Override
        public String getFieldName() {
            return "F2m";
        }
        
        @Override
        public int getFieldSize() {
            return this.m;
        }
        
        public int getK1() {
            return this.ks[0];
        }
        
        public int getK2() {
            final int[] ks = this.ks;
            if (ks.length >= 2) {
                return ks[1];
            }
            return 0;
        }
        
        public int getK3() {
            final int[] ks = this.ks;
            if (ks.length >= 3) {
                return ks[2];
            }
            return 0;
        }
        
        public int getM() {
            return this.m;
        }
        
        public int getRepresentation() {
            return this.representation;
        }
        
        @Override
        public int hashCode() {
            return this.x.hashCode() ^ this.m ^ Arrays.hashCode(this.ks);
        }
        
        @Override
        public ECFieldElement invert() {
            final int m = this.m;
            final int[] ks = this.ks;
            return new F2m(m, ks, this.x.modInverse(m, ks));
        }
        
        @Override
        public boolean isOne() {
            return this.x.isOne();
        }
        
        @Override
        public boolean isZero() {
            return this.x.isZero();
        }
        
        @Override
        public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
            final int m = this.m;
            final int[] ks = this.ks;
            return new F2m(m, ks, this.x.modMultiply(((F2m)ecFieldElement).x, m, ks));
        }
        
        @Override
        public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
            return this.multiplyPlusProduct(ecFieldElement, ecFieldElement2, ecFieldElement3);
        }
        
        @Override
        public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
            final LongArray x = this.x;
            final LongArray x2 = ((F2m)ecFieldElement).x;
            final LongArray x3 = ((F2m)ecFieldElement2).x;
            final LongArray x4 = ((F2m)ecFieldElement3).x;
            final LongArray multiply = x.multiply(x2, this.m, this.ks);
            final LongArray multiply2 = x3.multiply(x4, this.m, this.ks);
            LongArray longArray;
            if (multiply == x || (longArray = multiply) == x2) {
                longArray = (LongArray)multiply.clone();
            }
            longArray.addShiftedByWords(multiply2, 0);
            longArray.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, longArray);
        }
        
        @Override
        public ECFieldElement negate() {
            return this;
        }
        
        @Override
        public ECFieldElement sqrt() {
            if (this.x.isZero()) {
                return this;
            }
            if (this.x.isOne()) {
                return this;
            }
            return this.squarePow(this.m - 1);
        }
        
        @Override
        public ECFieldElement square() {
            final int m = this.m;
            final int[] ks = this.ks;
            return new F2m(m, ks, this.x.modSquare(m, ks));
        }
        
        @Override
        public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            return this.squarePlusProduct(ecFieldElement, ecFieldElement2);
        }
        
        @Override
        public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            final LongArray x = this.x;
            final LongArray x2 = ((F2m)ecFieldElement).x;
            final LongArray x3 = ((F2m)ecFieldElement2).x;
            final LongArray square = x.square(this.m, this.ks);
            final LongArray multiply = x2.multiply(x3, this.m, this.ks);
            LongArray longArray = square;
            if (square == x) {
                longArray = (LongArray)square.clone();
            }
            longArray.addShiftedByWords(multiply, 0);
            longArray.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, longArray);
        }
        
        @Override
        public ECFieldElement squarePow(final int n) {
            if (n < 1) {
                return this;
            }
            final int m = this.m;
            final int[] ks = this.ks;
            return new F2m(m, ks, this.x.modSquareN(n, m, ks));
        }
        
        @Override
        public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
            return this.add(ecFieldElement);
        }
        
        @Override
        public boolean testBitZero() {
            return this.x.testBitZero();
        }
        
        @Override
        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }
    }
    
    public static class Fp extends ECFieldElement
    {
        BigInteger q;
        BigInteger r;
        BigInteger x;
        
        public Fp(final BigInteger bigInteger, final BigInteger bigInteger2) {
            this(bigInteger, calculateResidue(bigInteger), bigInteger2);
        }
        
        Fp(final BigInteger q, final BigInteger r, final BigInteger x) {
            if (x != null && x.signum() >= 0 && x.compareTo(q) < 0) {
                this.q = q;
                this.r = r;
                this.x = x;
                return;
            }
            throw new IllegalArgumentException("x value invalid in Fp field element");
        }
        
        static BigInteger calculateResidue(final BigInteger bigInteger) {
            final int bitLength = bigInteger.bitLength();
            if (bitLength >= 96 && bigInteger.shiftRight(bitLength - 64).longValue() == -1L) {
                return Fp.ONE.shiftLeft(bitLength).subtract(bigInteger);
            }
            return null;
        }
        
        private ECFieldElement checkSqrt(final ECFieldElement ecFieldElement) {
            if (ecFieldElement.square().equals(this)) {
                return ecFieldElement;
            }
            return null;
        }
        
        private BigInteger[] lucasSequence(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            final int bitLength = bigInteger3.bitLength();
            final int lowestSetBit = bigInteger3.getLowestSetBit();
            BigInteger bigInteger4 = ECConstants.ONE;
            BigInteger bigInteger5 = ECConstants.TWO;
            BigInteger bigInteger6 = ECConstants.ONE;
            BigInteger bigInteger7 = ECConstants.ONE;
            int i = bitLength - 1;
            BigInteger bigInteger8 = bigInteger;
            while (i >= lowestSetBit + 1) {
                bigInteger6 = this.modMult(bigInteger6, bigInteger7);
                if (bigInteger3.testBit(i)) {
                    bigInteger7 = this.modMult(bigInteger6, bigInteger2);
                    bigInteger4 = this.modMult(bigInteger4, bigInteger8);
                    bigInteger5 = this.modReduce(bigInteger8.multiply(bigInteger5).subtract(bigInteger.multiply(bigInteger6)));
                    bigInteger8 = this.modReduce(bigInteger8.multiply(bigInteger8).subtract(bigInteger7.shiftLeft(1)));
                }
                else {
                    bigInteger4 = this.modReduce(bigInteger4.multiply(bigInteger5).subtract(bigInteger6));
                    bigInteger8 = this.modReduce(bigInteger8.multiply(bigInteger5).subtract(bigInteger.multiply(bigInteger6)));
                    bigInteger5 = this.modReduce(bigInteger5.multiply(bigInteger5).subtract(bigInteger6.shiftLeft(1)));
                    bigInteger7 = bigInteger6;
                }
                --i;
            }
            final BigInteger modMult = this.modMult(bigInteger6, bigInteger7);
            final BigInteger modMult2 = this.modMult(modMult, bigInteger2);
            bigInteger3 = this.modReduce(bigInteger4.multiply(bigInteger5).subtract(modMult));
            bigInteger2 = this.modReduce(bigInteger8.multiply(bigInteger5).subtract(bigInteger.multiply(modMult)));
            bigInteger = this.modMult(modMult, modMult2);
            for (int j = 1; j <= lowestSetBit; ++j) {
                bigInteger3 = this.modMult(bigInteger3, bigInteger2);
                bigInteger2 = this.modReduce(bigInteger2.multiply(bigInteger2).subtract(bigInteger.shiftLeft(1)));
                bigInteger = this.modMult(bigInteger, bigInteger);
            }
            return new BigInteger[] { bigInteger3, bigInteger2 };
        }
        
        @Override
        public ECFieldElement add(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.r, this.modAdd(this.x, ecFieldElement.toBigInteger()));
        }
        
        @Override
        public ECFieldElement addOne() {
            BigInteger bigInteger;
            if ((bigInteger = this.x.add(ECConstants.ONE)).compareTo(this.q) == 0) {
                bigInteger = ECConstants.ZERO;
            }
            return new Fp(this.q, this.r, bigInteger);
        }
        
        @Override
        public ECFieldElement divide(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.r, this.modMult(this.x, this.modInverse(ecFieldElement.toBigInteger())));
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Fp)) {
                return false;
            }
            final Fp fp = (Fp)o;
            return this.q.equals(fp.q) && this.x.equals(fp.x);
        }
        
        @Override
        public String getFieldName() {
            return "Fp";
        }
        
        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }
        
        public BigInteger getQ() {
            return this.q;
        }
        
        @Override
        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }
        
        @Override
        public ECFieldElement invert() {
            return new Fp(this.q, this.r, this.modInverse(this.x));
        }
        
        protected BigInteger modAdd(BigInteger bigInteger, BigInteger bigInteger2) {
            bigInteger2 = (bigInteger = bigInteger.add(bigInteger2));
            if (bigInteger2.compareTo(this.q) >= 0) {
                bigInteger = bigInteger2.subtract(this.q);
            }
            return bigInteger;
        }
        
        protected BigInteger modDouble(BigInteger bigInteger) {
            final BigInteger bigInteger2 = bigInteger = bigInteger.shiftLeft(1);
            if (bigInteger2.compareTo(this.q) >= 0) {
                bigInteger = bigInteger2.subtract(this.q);
            }
            return bigInteger;
        }
        
        protected BigInteger modHalf(final BigInteger bigInteger) {
            BigInteger add = bigInteger;
            if (bigInteger.testBit(0)) {
                add = this.q.add(bigInteger);
            }
            return add.shiftRight(1);
        }
        
        protected BigInteger modHalfAbs(final BigInteger bigInteger) {
            BigInteger subtract = bigInteger;
            if (bigInteger.testBit(0)) {
                subtract = this.q.subtract(bigInteger);
            }
            return subtract.shiftRight(1);
        }
        
        protected BigInteger modInverse(final BigInteger bigInteger) {
            final int fieldSize = this.getFieldSize();
            final int n = fieldSize + 31 >> 5;
            final int[] fromBigInteger = Nat.fromBigInteger(fieldSize, this.q);
            final int[] fromBigInteger2 = Nat.fromBigInteger(fieldSize, bigInteger);
            final int[] create = Nat.create(n);
            Mod.invert(fromBigInteger, fromBigInteger2, create);
            return Nat.toBigInteger(n, create);
        }
        
        protected BigInteger modMult(final BigInteger bigInteger, final BigInteger bigInteger2) {
            return this.modReduce(bigInteger.multiply(bigInteger2));
        }
        
        protected BigInteger modReduce(BigInteger bigInteger) {
            if (this.r != null) {
                final boolean b = bigInteger.signum() < 0;
                BigInteger abs = bigInteger;
                if (b) {
                    abs = bigInteger.abs();
                }
                final int bitLength = this.q.bitLength();
                final boolean equals = this.r.equals(ECConstants.ONE);
                bigInteger = abs;
                BigInteger subtract;
                while (true) {
                    subtract = bigInteger;
                    if (bigInteger.bitLength() <= bitLength + 1) {
                        break;
                    }
                    final BigInteger shiftRight = bigInteger.shiftRight(bitLength);
                    final BigInteger subtract2 = bigInteger.subtract(shiftRight.shiftLeft(bitLength));
                    bigInteger = shiftRight;
                    if (!equals) {
                        bigInteger = shiftRight.multiply(this.r);
                    }
                    bigInteger = bigInteger.add(subtract2);
                }
                while (subtract.compareTo(this.q) >= 0) {
                    subtract = subtract.subtract(this.q);
                }
                bigInteger = subtract;
                if (b) {
                    bigInteger = subtract;
                    if (subtract.signum() != 0) {
                        return this.q.subtract(subtract);
                    }
                }
            }
            else {
                bigInteger = bigInteger.mod(this.q);
            }
            return bigInteger;
        }
        
        protected BigInteger modSubtract(BigInteger bigInteger, BigInteger bigInteger2) {
            bigInteger2 = (bigInteger = bigInteger.subtract(bigInteger2));
            if (bigInteger2.signum() < 0) {
                bigInteger = bigInteger2.add(this.q);
            }
            return bigInteger;
        }
        
        @Override
        public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.r, this.modMult(this.x, ecFieldElement.toBigInteger()));
        }
        
        @Override
        public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
            return new Fp(this.q, this.r, this.modReduce(this.x.multiply(ecFieldElement.toBigInteger()).subtract(ecFieldElement2.toBigInteger().multiply(ecFieldElement3.toBigInteger()))));
        }
        
        @Override
        public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
            return new Fp(this.q, this.r, this.modReduce(this.x.multiply(ecFieldElement.toBigInteger()).add(ecFieldElement2.toBigInteger().multiply(ecFieldElement3.toBigInteger()))));
        }
        
        @Override
        public ECFieldElement negate() {
            if (this.x.signum() == 0) {
                return this;
            }
            final BigInteger q = this.q;
            return new Fp(q, this.r, q.subtract(this.x));
        }
        
        @Override
        public ECFieldElement sqrt() {
            if (this.isZero()) {
                return this;
            }
            if (this.isOne()) {
                return this;
            }
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            }
            if (this.q.testBit(1)) {
                final BigInteger add = this.q.shiftRight(2).add(ECConstants.ONE);
                final BigInteger q = this.q;
                return this.checkSqrt(new Fp(q, this.r, this.x.modPow(add, q)));
            }
            if (this.q.testBit(2)) {
                final BigInteger modPow = this.x.modPow(this.q.shiftRight(3), this.q);
                final BigInteger modMult = this.modMult(modPow, this.x);
                if (this.modMult(modMult, modPow).equals(ECConstants.ONE)) {
                    return this.checkSqrt(new Fp(this.q, this.r, modMult));
                }
                return this.checkSqrt(new Fp(this.q, this.r, this.modMult(modMult, ECConstants.TWO.modPow(this.q.shiftRight(2), this.q))));
            }
            else {
                final BigInteger shiftRight = this.q.shiftRight(1);
                if (!this.x.modPow(shiftRight, this.q).equals(ECConstants.ONE)) {
                    return null;
                }
                final BigInteger x = this.x;
                final BigInteger modDouble = this.modDouble(this.modDouble(x));
                final BigInteger add2 = shiftRight.add(ECConstants.ONE);
                final BigInteger subtract = this.q.subtract(ECConstants.ONE);
                final Random random = new Random();
                while (true) {
                    final BigInteger bigInteger = new BigInteger(this.q.bitLength(), random);
                    if (bigInteger.compareTo(this.q) < 0 && this.modReduce(bigInteger.multiply(bigInteger).subtract(modDouble)).modPow(shiftRight, this.q).equals(subtract)) {
                        final BigInteger[] lucasSequence = this.lucasSequence(bigInteger, x, add2);
                        final BigInteger bigInteger2 = lucasSequence[0];
                        final BigInteger bigInteger3 = lucasSequence[1];
                        if (this.modMult(bigInteger3, bigInteger3).equals(modDouble)) {
                            return new Fp(this.q, this.r, this.modHalfAbs(bigInteger3));
                        }
                        if (!bigInteger2.equals(ECConstants.ONE) && !bigInteger2.equals(subtract)) {
                            return null;
                        }
                        continue;
                    }
                }
            }
        }
        
        @Override
        public ECFieldElement square() {
            final BigInteger q = this.q;
            final BigInteger r = this.r;
            final BigInteger x = this.x;
            return new Fp(q, r, this.modMult(x, x));
        }
        
        @Override
        public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            final BigInteger x = this.x;
            return new Fp(this.q, this.r, this.modReduce(x.multiply(x).subtract(ecFieldElement.toBigInteger().multiply(ecFieldElement2.toBigInteger()))));
        }
        
        @Override
        public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            final BigInteger x = this.x;
            return new Fp(this.q, this.r, this.modReduce(x.multiply(x).add(ecFieldElement.toBigInteger().multiply(ecFieldElement2.toBigInteger()))));
        }
        
        @Override
        public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
            return new Fp(this.q, this.r, this.modSubtract(this.x, ecFieldElement.toBigInteger()));
        }
        
        @Override
        public BigInteger toBigInteger() {
            return this.x;
        }
    }
}
