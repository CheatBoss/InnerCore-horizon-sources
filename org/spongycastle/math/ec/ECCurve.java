package org.spongycastle.math.ec;

import java.math.*;
import org.spongycastle.math.ec.endo.*;
import org.spongycastle.util.*;
import org.spongycastle.math.field.*;
import java.util.*;

public abstract class ECCurve
{
    public static final int COORD_AFFINE = 0;
    public static final int COORD_HOMOGENEOUS = 1;
    public static final int COORD_JACOBIAN = 2;
    public static final int COORD_JACOBIAN_CHUDNOVSKY = 3;
    public static final int COORD_JACOBIAN_MODIFIED = 4;
    public static final int COORD_LAMBDA_AFFINE = 5;
    public static final int COORD_LAMBDA_PROJECTIVE = 6;
    public static final int COORD_SKEWED = 7;
    protected ECFieldElement a;
    protected ECFieldElement b;
    protected BigInteger cofactor;
    protected int coord;
    protected ECEndomorphism endomorphism;
    protected FiniteField field;
    protected ECMultiplier multiplier;
    protected BigInteger order;
    
    protected ECCurve(final FiniteField field) {
        this.coord = 0;
        this.endomorphism = null;
        this.multiplier = null;
        this.field = field;
    }
    
    public static int[] getAllCoordinateSystems() {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    }
    
    protected void checkPoint(final ECPoint ecPoint) {
        if (ecPoint != null && this == ecPoint.getCurve()) {
            return;
        }
        throw new IllegalArgumentException("'point' must be non-null and on this curve");
    }
    
    protected void checkPoints(final ECPoint[] array) {
        this.checkPoints(array, 0, array.length);
    }
    
    protected void checkPoints(final ECPoint[] array, final int n, final int n2) {
        if (array == null) {
            throw new IllegalArgumentException("'points' cannot be null");
        }
        if (n >= 0 && n2 >= 0 && n <= array.length - n2) {
            for (int i = 0; i < n2; ++i) {
                final ECPoint ecPoint = array[n + i];
                if (ecPoint != null && this != ecPoint.getCurve()) {
                    throw new IllegalArgumentException("'points' entries must be null or on this curve");
                }
            }
            return;
        }
        throw new IllegalArgumentException("invalid range specified for 'points'");
    }
    
    protected abstract ECCurve cloneCurve();
    
    public Config configure() {
        synchronized (this) {
            return new Config(this.coord, this.endomorphism, this.multiplier);
        }
    }
    
    protected ECMultiplier createDefaultMultiplier() {
        if (this.endomorphism instanceof GLVEndomorphism) {
            return new GLVMultiplier(this, (GLVEndomorphism)this.endomorphism);
        }
        return new WNafL2RMultiplier();
    }
    
    public ECPoint createPoint(final BigInteger bigInteger, final BigInteger bigInteger2) {
        return this.createPoint(bigInteger, bigInteger2, false);
    }
    
    public ECPoint createPoint(final BigInteger bigInteger, final BigInteger bigInteger2, final boolean b) {
        return this.createRawPoint(this.fromBigInteger(bigInteger), this.fromBigInteger(bigInteger2), b);
    }
    
    protected abstract ECPoint createRawPoint(final ECFieldElement p0, final ECFieldElement p1, final boolean p2);
    
    protected abstract ECPoint createRawPoint(final ECFieldElement p0, final ECFieldElement p1, final ECFieldElement[] p2, final boolean p3);
    
    public ECPoint decodePoint(final byte[] array) {
        final int n = (this.getFieldSize() + 7) / 8;
        int n2 = false ? 1 : 0;
        final byte b = array[0];
        ECPoint ecPoint;
        if (b != 0) {
            if (b != 2 && b != 3) {
                if (b != 4) {
                    if (b != 6 && b != 7) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid point encoding 0x");
                        sb.append(Integer.toString(b, 16));
                        throw new IllegalArgumentException(sb.toString());
                    }
                    if (array.length != n * 2 + 1) {
                        throw new IllegalArgumentException("Incorrect length for hybrid encoding");
                    }
                    final BigInteger fromUnsignedByteArray = BigIntegers.fromUnsignedByteArray(array, 1, n);
                    final BigInteger fromUnsignedByteArray2 = BigIntegers.fromUnsignedByteArray(array, n + 1, n);
                    final boolean testBit = fromUnsignedByteArray2.testBit(0);
                    if (b == 7) {
                        n2 = (true ? 1 : 0);
                    }
                    if ((testBit ? 1 : 0) != n2) {
                        throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
                    }
                    ecPoint = this.validatePoint(fromUnsignedByteArray, fromUnsignedByteArray2);
                }
                else {
                    if (array.length != n * 2 + 1) {
                        throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
                    }
                    ecPoint = this.validatePoint(BigIntegers.fromUnsignedByteArray(array, 1, n), BigIntegers.fromUnsignedByteArray(array, n + 1, n));
                }
            }
            else {
                if (array.length != n + 1) {
                    throw new IllegalArgumentException("Incorrect length for compressed encoding");
                }
                ecPoint = this.decompressPoint(b & 0x1, BigIntegers.fromUnsignedByteArray(array, 1, n));
                if (!ecPoint.satisfiesCofactor()) {
                    throw new IllegalArgumentException("Invalid point");
                }
            }
        }
        else {
            if (array.length != 1) {
                throw new IllegalArgumentException("Incorrect length for infinity encoding");
            }
            ecPoint = this.getInfinity();
        }
        if (b == 0) {
            return ecPoint;
        }
        if (!ecPoint.isInfinity()) {
            return ecPoint;
        }
        throw new IllegalArgumentException("Invalid infinity encoding");
    }
    
    protected abstract ECPoint decompressPoint(final int p0, final BigInteger p1);
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ECCurve && this.equals((ECCurve)o));
    }
    
    public boolean equals(final ECCurve ecCurve) {
        return this == ecCurve || (ecCurve != null && this.getField().equals(ecCurve.getField()) && this.getA().toBigInteger().equals(ecCurve.getA().toBigInteger()) && this.getB().toBigInteger().equals(ecCurve.getB().toBigInteger()));
    }
    
    public abstract ECFieldElement fromBigInteger(final BigInteger p0);
    
    public ECFieldElement getA() {
        return this.a;
    }
    
    public ECFieldElement getB() {
        return this.b;
    }
    
    public BigInteger getCofactor() {
        return this.cofactor;
    }
    
    public int getCoordinateSystem() {
        return this.coord;
    }
    
    public ECEndomorphism getEndomorphism() {
        return this.endomorphism;
    }
    
    public FiniteField getField() {
        return this.field;
    }
    
    public abstract int getFieldSize();
    
    public abstract ECPoint getInfinity();
    
    public ECMultiplier getMultiplier() {
        synchronized (this) {
            if (this.multiplier == null) {
                this.multiplier = this.createDefaultMultiplier();
            }
            return this.multiplier;
        }
    }
    
    public BigInteger getOrder() {
        return this.order;
    }
    
    public PreCompInfo getPreCompInfo(final ECPoint ecPoint, final String s) {
        this.checkPoint(ecPoint);
        synchronized (ecPoint) {
            final Hashtable preCompTable = ecPoint.preCompTable;
            PreCompInfo preCompInfo;
            if (preCompTable == null) {
                preCompInfo = null;
            }
            else {
                preCompInfo = preCompTable.get(s);
            }
            return preCompInfo;
        }
    }
    
    @Override
    public int hashCode() {
        return this.getField().hashCode() ^ Integers.rotateLeft(this.getA().toBigInteger().hashCode(), 8) ^ Integers.rotateLeft(this.getB().toBigInteger().hashCode(), 16);
    }
    
    public ECPoint importPoint(ECPoint normalize) {
        if (this == normalize.getCurve()) {
            return normalize;
        }
        if (normalize.isInfinity()) {
            return this.getInfinity();
        }
        normalize = normalize.normalize();
        return this.validatePoint(normalize.getXCoord().toBigInteger(), normalize.getYCoord().toBigInteger(), normalize.withCompression);
    }
    
    public abstract boolean isValidFieldElement(final BigInteger p0);
    
    public void normalizeAll(final ECPoint[] array) {
        this.normalizeAll(array, 0, array.length, null);
    }
    
    public void normalizeAll(final ECPoint[] array, int i, int n, final ECFieldElement ecFieldElement) {
        this.checkPoints(array, i, n);
        final int coordinateSystem = this.getCoordinateSystem();
        if (coordinateSystem != 0 && coordinateSystem != 5) {
            final ECFieldElement[] array2 = new ECFieldElement[n];
            final int[] array3 = new int[n];
            final int n2 = 0;
            int j = 0;
            int n3 = 0;
            while (j < n) {
                final int n4 = i + j;
                final ECPoint ecPoint = array[n4];
                int n5 = n3;
                Label_0112: {
                    if (ecPoint != null) {
                        if (ecFieldElement == null) {
                            n5 = n3;
                            if (ecPoint.isNormalized()) {
                                break Label_0112;
                            }
                        }
                        array2[n3] = ecPoint.getZCoord(0);
                        array3[n3] = n4;
                        n5 = n3 + 1;
                    }
                }
                ++j;
                n3 = n5;
            }
            if (n3 == 0) {
                return;
            }
            ECAlgorithms.montgomeryTrick(array2, 0, n3, ecFieldElement);
            for (i = n2; i < n3; ++i) {
                n = array3[i];
                array[n] = array[n].normalize(array2[i]);
            }
        }
        else {
            if (ecFieldElement == null) {
                return;
            }
            throw new IllegalArgumentException("'iso' not valid for affine coordinates");
        }
    }
    
    public void setPreCompInfo(final ECPoint ecPoint, final String s, final PreCompInfo preCompInfo) {
        this.checkPoint(ecPoint);
        synchronized (ecPoint) {
            Hashtable<String, PreCompInfo> preCompTable;
            if ((preCompTable = (Hashtable<String, PreCompInfo>)ecPoint.preCompTable) == null) {
                preCompTable = new Hashtable<String, PreCompInfo>(4);
                ecPoint.preCompTable = preCompTable;
            }
            preCompTable.put(s, preCompInfo);
        }
    }
    
    public boolean supportsCoordinateSystem(final int n) {
        return n == 0;
    }
    
    public ECPoint validatePoint(final BigInteger bigInteger, final BigInteger bigInteger2) {
        final ECPoint point = this.createPoint(bigInteger, bigInteger2);
        if (point.isValid()) {
            return point;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }
    
    public ECPoint validatePoint(final BigInteger bigInteger, final BigInteger bigInteger2, final boolean b) {
        final ECPoint point = this.createPoint(bigInteger, bigInteger2, b);
        if (point.isValid()) {
            return point;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }
    
    public abstract static class AbstractF2m extends ECCurve
    {
        private BigInteger[] si;
        
        protected AbstractF2m(final int n, final int n2, final int n3, final int n4) {
            super(buildField(n, n2, n3, n4));
            this.si = null;
        }
        
        private static FiniteField buildField(final int n, final int n2, final int n3, final int n4) {
            if (n2 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            }
            if (n3 == 0) {
                if (n4 == 0) {
                    return FiniteFields.getBinaryExtensionField(new int[] { 0, n2, n });
                }
                throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
            }
            else {
                if (n3 <= n2) {
                    throw new IllegalArgumentException("k2 must be > k1");
                }
                if (n4 > n3) {
                    return FiniteFields.getBinaryExtensionField(new int[] { 0, n2, n3, n4, n });
                }
                throw new IllegalArgumentException("k3 must be > k2");
            }
        }
        
        public static BigInteger inverse(final int n, final int[] array, final BigInteger bigInteger) {
            return new LongArray(bigInteger).modInverse(n, array).toBigInteger();
        }
        
        private ECFieldElement solveQuadraticEquation(final ECFieldElement ecFieldElement) {
            if (ecFieldElement.isZero()) {
                return ecFieldElement;
            }
            final ECFieldElement fromBigInteger = this.fromBigInteger(ECConstants.ZERO);
            final int fieldSize = this.getFieldSize();
            final Random random = new Random();
            ECFieldElement add;
            do {
                final ECFieldElement fromBigInteger2 = this.fromBigInteger(new BigInteger(fieldSize, random));
                int i = 1;
                ECFieldElement add2 = ecFieldElement;
                add = fromBigInteger;
                while (i < fieldSize) {
                    final ECFieldElement square = add2.square();
                    add = add.square().add(square.multiply(fromBigInteger2));
                    add2 = square.add(ecFieldElement);
                    ++i;
                }
                if (!add2.isZero()) {
                    return null;
                }
            } while (add.square().add(add).isZero());
            return add;
        }
        
        @Override
        public ECPoint createPoint(final BigInteger bigInteger, final BigInteger bigInteger2, final boolean b) {
            final ECFieldElement fromBigInteger = this.fromBigInteger(bigInteger);
            ECFieldElement ecFieldElement = this.fromBigInteger(bigInteger2);
            final int coordinateSystem = this.getCoordinateSystem();
            if (coordinateSystem == 5 || coordinateSystem == 6) {
                if (fromBigInteger.isZero()) {
                    if (!ecFieldElement.square().equals(this.getB())) {
                        throw new IllegalArgumentException();
                    }
                }
                else {
                    ecFieldElement = ecFieldElement.divide(fromBigInteger).add(fromBigInteger);
                }
            }
            return this.createRawPoint(fromBigInteger, ecFieldElement, b);
        }
        
        @Override
        protected ECPoint decompressPoint(int coordinateSystem, final BigInteger bigInteger) {
            final ECFieldElement fromBigInteger = this.fromBigInteger(bigInteger);
            ECFieldElement ecFieldElement;
            if (fromBigInteger.isZero()) {
                ecFieldElement = this.getB().sqrt();
            }
            else {
                final ECFieldElement solveQuadraticEquation = this.solveQuadraticEquation(fromBigInteger.square().invert().multiply(this.getB()).add(this.getA()).add(fromBigInteger));
                if (solveQuadraticEquation != null) {
                    final boolean testBitZero = solveQuadraticEquation.testBitZero();
                    final boolean b = coordinateSystem == 1;
                    ECFieldElement addOne = solveQuadraticEquation;
                    if (testBitZero != b) {
                        addOne = solveQuadraticEquation.addOne();
                    }
                    coordinateSystem = this.getCoordinateSystem();
                    if (coordinateSystem != 5 && coordinateSystem != 6) {
                        ecFieldElement = addOne.multiply(fromBigInteger);
                    }
                    else {
                        ecFieldElement = addOne.add(fromBigInteger);
                    }
                }
                else {
                    ecFieldElement = null;
                }
            }
            if (ecFieldElement != null) {
                return this.createRawPoint(fromBigInteger, ecFieldElement, true);
            }
            throw new IllegalArgumentException("Invalid point compression");
        }
        
        BigInteger[] getSi() {
            synchronized (this) {
                if (this.si == null) {
                    this.si = Tnaf.getSi(this);
                }
                return this.si;
            }
        }
        
        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }
        
        @Override
        public boolean isValidFieldElement(final BigInteger bigInteger) {
            return bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= this.getFieldSize();
        }
    }
    
    public abstract static class AbstractFp extends ECCurve
    {
        protected AbstractFp(final BigInteger bigInteger) {
            super(FiniteFields.getPrimeField(bigInteger));
        }
        
        @Override
        protected ECPoint decompressPoint(final int n, final BigInteger bigInteger) {
            final ECFieldElement fromBigInteger = this.fromBigInteger(bigInteger);
            final ECFieldElement sqrt = fromBigInteger.square().add(this.a).multiply(fromBigInteger).add(this.b).sqrt();
            if (sqrt != null) {
                final boolean testBitZero = sqrt.testBitZero();
                final boolean b = n == 1;
                ECFieldElement negate = sqrt;
                if (testBitZero != b) {
                    negate = sqrt.negate();
                }
                return this.createRawPoint(fromBigInteger, negate, true);
            }
            throw new IllegalArgumentException("Invalid point compression");
        }
        
        @Override
        public boolean isValidFieldElement(final BigInteger bigInteger) {
            return bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(this.getField().getCharacteristic()) < 0;
        }
    }
    
    public class Config
    {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;
        
        Config(final int coord, final ECEndomorphism endomorphism, final ECMultiplier multiplier) {
            this.coord = coord;
            this.endomorphism = endomorphism;
            this.multiplier = multiplier;
        }
        
        public ECCurve create() {
            if (ECCurve.this.supportsCoordinateSystem(this.coord)) {
                final ECCurve cloneCurve = ECCurve.this.cloneCurve();
                if (cloneCurve != ECCurve.this) {
                    synchronized (cloneCurve) {
                        cloneCurve.coord = this.coord;
                        cloneCurve.endomorphism = this.endomorphism;
                        cloneCurve.multiplier = this.multiplier;
                        return cloneCurve;
                    }
                }
                throw new IllegalStateException("implementation returned current curve");
            }
            throw new IllegalStateException("unsupported coordinate system");
        }
        
        public Config setCoordinateSystem(final int coord) {
            this.coord = coord;
            return this;
        }
        
        public Config setEndomorphism(final ECEndomorphism endomorphism) {
            this.endomorphism = endomorphism;
            return this;
        }
        
        public Config setMultiplier(final ECMultiplier multiplier) {
            this.multiplier = multiplier;
            return this;
        }
    }
    
    public static class F2m extends AbstractF2m
    {
        private static final int F2M_DEFAULT_COORDS = 6;
        private ECPoint.F2m infinity;
        private int k1;
        private int k2;
        private int k3;
        private int m;
        
        public F2m(final int n, final int n2, final int n3, final int n4, final BigInteger bigInteger, final BigInteger bigInteger2) {
            this(n, n2, n3, n4, bigInteger, bigInteger2, null, null);
        }
        
        public F2m(final int m, final int k1, final int k2, final int k3, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger order, final BigInteger cofactor) {
            super(m, k1, k2, k3);
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            this.order = order;
            this.cofactor = cofactor;
            this.infinity = new ECPoint.F2m(this, null, null);
            this.a = this.fromBigInteger(bigInteger);
            this.b = this.fromBigInteger(bigInteger2);
            this.coord = 6;
        }
        
        protected F2m(final int m, final int k1, final int k2, final int k3, final ECFieldElement a, final ECFieldElement b, final BigInteger order, final BigInteger cofactor) {
            super(m, k1, k2, k3);
            this.m = m;
            this.k1 = k1;
            this.k2 = k2;
            this.k3 = k3;
            this.order = order;
            this.cofactor = cofactor;
            this.infinity = new ECPoint.F2m(this, null, null);
            this.a = a;
            this.b = b;
            this.coord = 6;
        }
        
        public F2m(final int n, final int n2, final BigInteger bigInteger, final BigInteger bigInteger2) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, null, null);
        }
        
        public F2m(final int n, final int n2, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }
        
        @Override
        protected ECCurve cloneCurve() {
            return new F2m(this.m, this.k1, this.k2, this.k3, this.a, this.b, this.order, this.cofactor);
        }
        
        @Override
        protected ECMultiplier createDefaultMultiplier() {
            if (((AbstractF2m)this).isKoblitz()) {
                return new WTauNafMultiplier();
            }
            return super.createDefaultMultiplier();
        }
        
        @Override
        protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
            return new ECPoint.F2m(this, ecFieldElement, ecFieldElement2, b);
        }
        
        @Override
        protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
            return new ECPoint.F2m(this, ecFieldElement, ecFieldElement2, array, b);
        }
        
        @Override
        public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
            return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, bigInteger);
        }
        
        @Override
        public int getFieldSize() {
            return this.m;
        }
        
        public BigInteger getH() {
            return this.cofactor;
        }
        
        @Override
        public ECPoint getInfinity() {
            return this.infinity;
        }
        
        public int getK1() {
            return this.k1;
        }
        
        public int getK2() {
            return this.k2;
        }
        
        public int getK3() {
            return this.k3;
        }
        
        public int getM() {
            return this.m;
        }
        
        public BigInteger getN() {
            return this.order;
        }
        
        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
        }
        
        @Override
        public boolean supportsCoordinateSystem(final int n) {
            return n == 0 || n == 1 || n == 6;
        }
    }
    
    public static class Fp extends AbstractFp
    {
        private static final int FP_DEFAULT_COORDS = 4;
        ECPoint.Fp infinity;
        BigInteger q;
        BigInteger r;
        
        public Fp(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
            this(bigInteger, bigInteger2, bigInteger3, null, null);
        }
        
        public Fp(final BigInteger q, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger order, final BigInteger cofactor) {
            super(q);
            this.q = q;
            this.r = ECFieldElement.Fp.calculateResidue(q);
            this.infinity = new ECPoint.Fp(this, null, null);
            this.a = this.fromBigInteger(bigInteger);
            this.b = this.fromBigInteger(bigInteger2);
            this.order = order;
            this.cofactor = cofactor;
            this.coord = 4;
        }
        
        protected Fp(final BigInteger bigInteger, final BigInteger bigInteger2, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            this(bigInteger, bigInteger2, ecFieldElement, ecFieldElement2, null, null);
        }
        
        protected Fp(final BigInteger q, final BigInteger r, final ECFieldElement a, final ECFieldElement b, final BigInteger order, final BigInteger cofactor) {
            super(q);
            this.q = q;
            this.r = r;
            this.infinity = new ECPoint.Fp(this, null, null);
            this.a = a;
            this.b = b;
            this.order = order;
            this.cofactor = cofactor;
            this.coord = 4;
        }
        
        @Override
        protected ECCurve cloneCurve() {
            return new Fp(this.q, this.r, this.a, this.b, this.order, this.cofactor);
        }
        
        @Override
        protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
            return new ECPoint.Fp(this, ecFieldElement, ecFieldElement2, b);
        }
        
        @Override
        protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
            return new ECPoint.Fp(this, ecFieldElement, ecFieldElement2, array, b);
        }
        
        @Override
        public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
            return new ECFieldElement.Fp(this.q, this.r, bigInteger);
        }
        
        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }
        
        @Override
        public ECPoint getInfinity() {
            return this.infinity;
        }
        
        public BigInteger getQ() {
            return this.q;
        }
        
        @Override
        public ECPoint importPoint(final ECPoint ecPoint) {
            if (this != ecPoint.getCurve() && this.getCoordinateSystem() == 2 && !ecPoint.isInfinity()) {
                final int coordinateSystem = ecPoint.getCurve().getCoordinateSystem();
                if (coordinateSystem == 2 || coordinateSystem == 3 || coordinateSystem == 4) {
                    return new ECPoint.Fp(this, this.fromBigInteger(ecPoint.x.toBigInteger()), this.fromBigInteger(ecPoint.y.toBigInteger()), new ECFieldElement[] { this.fromBigInteger(ecPoint.zs[0].toBigInteger()) }, ecPoint.withCompression);
                }
            }
            return super.importPoint(ecPoint);
        }
        
        @Override
        public boolean supportsCoordinateSystem(final int n) {
            return n == 0 || n == 1 || n == 2 || n == 4;
        }
    }
}
