package org.spongycastle.math.ec;

import java.util.*;
import java.math.*;

public abstract class ECPoint
{
    protected static ECFieldElement[] EMPTY_ZS;
    protected ECCurve curve;
    protected Hashtable preCompTable;
    protected boolean withCompression;
    protected ECFieldElement x;
    protected ECFieldElement y;
    protected ECFieldElement[] zs;
    
    static {
        ECPoint.EMPTY_ZS = new ECFieldElement[0];
    }
    
    protected ECPoint(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, getInitialZCoords(ecCurve));
    }
    
    protected ECPoint(final ECCurve curve, final ECFieldElement x, final ECFieldElement y, final ECFieldElement[] zs) {
        this.preCompTable = null;
        this.curve = curve;
        this.x = x;
        this.y = y;
        this.zs = zs;
    }
    
    protected static ECFieldElement[] getInitialZCoords(final ECCurve ecCurve) {
        int coordinateSystem;
        if (ecCurve == null) {
            coordinateSystem = 0;
        }
        else {
            coordinateSystem = ecCurve.getCoordinateSystem();
        }
        if (coordinateSystem != 0 && coordinateSystem != 5) {
            final ECFieldElement fromBigInteger = ecCurve.fromBigInteger(ECConstants.ONE);
            if (coordinateSystem != 1 && coordinateSystem != 2) {
                if (coordinateSystem == 3) {
                    return new ECFieldElement[] { fromBigInteger, fromBigInteger, fromBigInteger };
                }
                if (coordinateSystem == 4) {
                    return new ECFieldElement[] { fromBigInteger, ecCurve.getA() };
                }
                if (coordinateSystem != 6) {
                    throw new IllegalArgumentException("unknown coordinate system");
                }
            }
            return new ECFieldElement[] { fromBigInteger };
        }
        return ECPoint.EMPTY_ZS;
    }
    
    public abstract ECPoint add(final ECPoint p0);
    
    protected void checkNormalized() {
        if (this.isNormalized()) {
            return;
        }
        throw new IllegalStateException("point not in normal form");
    }
    
    protected ECPoint createScaledPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.getCurve().createRawPoint(this.getRawXCoord().multiply(ecFieldElement), this.getRawYCoord().multiply(ecFieldElement2), this.withCompression);
    }
    
    protected abstract ECPoint detach();
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ECPoint && this.equals((ECPoint)o));
    }
    
    public boolean equals(ECPoint normalize) {
        final boolean b = false;
        final boolean b2 = false;
        if (normalize == null) {
            return false;
        }
        final ECCurve curve = this.getCurve();
        final ECCurve curve2 = normalize.getCurve();
        final boolean b3 = curve == null;
        final boolean b4 = curve2 == null;
        final boolean infinity = this.isInfinity();
        final boolean infinity2 = normalize.isInfinity();
        if (!infinity && !infinity2) {
            ECPoint normalize2 = null;
            Label_0163: {
                if (!b3 || !b4) {
                    if (b3) {
                        normalize = normalize.normalize();
                    }
                    else {
                        if (b4) {
                            normalize2 = this.normalize();
                            break Label_0163;
                        }
                        if (!curve.equals(curve2)) {
                            return false;
                        }
                        final ECPoint[] array = { this, curve.importPoint(normalize) };
                        curve.normalizeAll(array);
                        normalize2 = array[0];
                        normalize = array[1];
                        break Label_0163;
                    }
                }
                normalize2 = this;
            }
            boolean b5 = b2;
            if (normalize2.getXCoord().equals(normalize.getXCoord())) {
                b5 = b2;
                if (normalize2.getYCoord().equals(normalize.getYCoord())) {
                    b5 = true;
                }
            }
            return b5;
        }
        boolean b6 = b;
        if (infinity) {
            b6 = b;
            if (infinity2) {
                if (!b3 && !b4) {
                    b6 = b;
                    if (!curve.equals(curve2)) {
                        return b6;
                    }
                }
                b6 = true;
            }
        }
        return b6;
    }
    
    public ECFieldElement getAffineXCoord() {
        this.checkNormalized();
        return this.getXCoord();
    }
    
    public ECFieldElement getAffineYCoord() {
        this.checkNormalized();
        return this.getYCoord();
    }
    
    protected abstract boolean getCompressionYTilde();
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    protected int getCurveCoordinateSystem() {
        final ECCurve curve = this.curve;
        if (curve == null) {
            return 0;
        }
        return curve.getCoordinateSystem();
    }
    
    public final ECPoint getDetachedPoint() {
        return this.normalize().detach();
    }
    
    public byte[] getEncoded() {
        return this.getEncoded(this.withCompression);
    }
    
    public byte[] getEncoded(final boolean b) {
        if (this.isInfinity()) {
            return new byte[1];
        }
        final ECPoint normalize = this.normalize();
        final byte[] encoded = normalize.getXCoord().getEncoded();
        if (b) {
            final byte[] array = new byte[encoded.length + 1];
            int n;
            if (normalize.getCompressionYTilde()) {
                n = 3;
            }
            else {
                n = 2;
            }
            array[0] = (byte)n;
            System.arraycopy(encoded, 0, array, 1, encoded.length);
            return array;
        }
        final byte[] encoded2 = normalize.getYCoord().getEncoded();
        final byte[] array2 = new byte[encoded.length + encoded2.length + 1];
        array2[0] = 4;
        System.arraycopy(encoded, 0, array2, 1, encoded.length);
        System.arraycopy(encoded2, 0, array2, encoded.length + 1, encoded2.length);
        return array2;
    }
    
    public final ECFieldElement getRawXCoord() {
        return this.x;
    }
    
    public final ECFieldElement getRawYCoord() {
        return this.y;
    }
    
    protected final ECFieldElement[] getRawZCoords() {
        return this.zs;
    }
    
    public ECFieldElement getX() {
        return this.normalize().getXCoord();
    }
    
    public ECFieldElement getXCoord() {
        return this.x;
    }
    
    public ECFieldElement getY() {
        return this.normalize().getYCoord();
    }
    
    public ECFieldElement getYCoord() {
        return this.y;
    }
    
    public ECFieldElement getZCoord(final int n) {
        if (n >= 0) {
            final ECFieldElement[] zs = this.zs;
            if (n < zs.length) {
                return zs[n];
            }
        }
        return null;
    }
    
    public ECFieldElement[] getZCoords() {
        final ECFieldElement[] zs = this.zs;
        final int length = zs.length;
        if (length == 0) {
            return ECPoint.EMPTY_ZS;
        }
        final ECFieldElement[] array = new ECFieldElement[length];
        System.arraycopy(zs, 0, array, 0, length);
        return array;
    }
    
    @Override
    public int hashCode() {
        final ECCurve curve = this.getCurve();
        int n;
        if (curve == null) {
            n = 0;
        }
        else {
            n = ~curve.hashCode();
        }
        int n2 = n;
        if (!this.isInfinity()) {
            final ECPoint normalize = this.normalize();
            n2 = (n ^ normalize.getXCoord().hashCode() * 17 ^ normalize.getYCoord().hashCode() * 257);
        }
        return n2;
    }
    
    public boolean isCompressed() {
        return this.withCompression;
    }
    
    public boolean isInfinity() {
        final ECFieldElement x = this.x;
        final boolean b = false;
        if (x != null && this.y != null) {
            final ECFieldElement[] zs = this.zs;
            boolean b2 = b;
            if (zs.length <= 0) {
                return b2;
            }
            b2 = b;
            if (!zs[0].isZero()) {
                return b2;
            }
        }
        return true;
    }
    
    public boolean isNormalized() {
        final int curveCoordinateSystem = this.getCurveCoordinateSystem();
        boolean b = false;
        if (curveCoordinateSystem == 0 || curveCoordinateSystem == 5 || this.isInfinity() || this.zs[0].isOne()) {
            b = true;
        }
        return b;
    }
    
    public boolean isValid() {
        if (this.isInfinity()) {
            return true;
        }
        if (this.getCurve() != null) {
            if (!this.satisfiesCurveEquation()) {
                return false;
            }
            if (!this.satisfiesCofactor()) {
                return false;
            }
        }
        return true;
    }
    
    public ECPoint multiply(final BigInteger bigInteger) {
        return this.getCurve().getMultiplier().multiply(this, bigInteger);
    }
    
    public abstract ECPoint negate();
    
    public ECPoint normalize() {
        if (this.isInfinity()) {
            return this;
        }
        final int curveCoordinateSystem = this.getCurveCoordinateSystem();
        if (curveCoordinateSystem == 0 || curveCoordinateSystem == 5) {
            return this;
        }
        final ECFieldElement zCoord = this.getZCoord(0);
        if (zCoord.isOne()) {
            return this;
        }
        return this.normalize(zCoord.invert());
    }
    
    ECPoint normalize(final ECFieldElement ecFieldElement) {
        final int curveCoordinateSystem = this.getCurveCoordinateSystem();
        if (curveCoordinateSystem != 1) {
            if (curveCoordinateSystem == 2 || curveCoordinateSystem == 3 || curveCoordinateSystem == 4) {
                final ECFieldElement square = ecFieldElement.square();
                return this.createScaledPoint(square, square.multiply(ecFieldElement));
            }
            if (curveCoordinateSystem != 6) {
                throw new IllegalStateException("not a projective coordinate system");
            }
        }
        return this.createScaledPoint(ecFieldElement, ecFieldElement);
    }
    
    protected boolean satisfiesCofactor() {
        final BigInteger cofactor = this.curve.getCofactor();
        return cofactor == null || cofactor.equals(ECConstants.ONE) || !ECAlgorithms.referenceMultiply(this, cofactor).isInfinity();
    }
    
    protected abstract boolean satisfiesCurveEquation();
    
    public ECPoint scaleX(final ECFieldElement ecFieldElement) {
        if (this.isInfinity()) {
            return this;
        }
        return this.getCurve().createRawPoint(this.getRawXCoord().multiply(ecFieldElement), this.getRawYCoord(), this.getRawZCoords(), this.withCompression);
    }
    
    public ECPoint scaleY(final ECFieldElement ecFieldElement) {
        if (this.isInfinity()) {
            return this;
        }
        return this.getCurve().createRawPoint(this.getRawXCoord(), this.getRawYCoord().multiply(ecFieldElement), this.getRawZCoords(), this.withCompression);
    }
    
    public abstract ECPoint subtract(final ECPoint p0);
    
    public ECPoint threeTimes() {
        return this.twicePlus(this);
    }
    
    public ECPoint timesPow2(int n) {
        if (n >= 0) {
            ECPoint twice = this;
            while (true) {
                --n;
                if (n < 0) {
                    break;
                }
                twice = twice.twice();
            }
            return twice;
        }
        throw new IllegalArgumentException("'e' cannot be negative");
    }
    
    @Override
    public String toString() {
        if (this.isInfinity()) {
            return "INF";
        }
        final StringBuffer sb = new StringBuffer();
        sb.append('(');
        sb.append(this.getRawXCoord());
        sb.append(',');
        sb.append(this.getRawYCoord());
        for (int i = 0; i < this.zs.length; ++i) {
            sb.append(',');
            sb.append(this.zs[i]);
        }
        sb.append(')');
        return sb.toString();
    }
    
    public abstract ECPoint twice();
    
    public ECPoint twicePlus(final ECPoint ecPoint) {
        return this.twice().add(ecPoint);
    }
    
    public abstract static class AbstractF2m extends ECPoint
    {
        protected AbstractF2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
        }
        
        protected AbstractF2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array) {
            super(ecCurve, ecFieldElement, ecFieldElement2, array);
        }
        
        @Override
        protected boolean satisfiesCurveEquation() {
            final ECCurve curve = this.getCurve();
            final ECFieldElement x = this.x;
            final ECFieldElement a = curve.getA();
            final ECFieldElement b = curve.getB();
            final int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem != 6) {
                final ECFieldElement y = this.y;
                final ECFieldElement multiply = y.add(x).multiply(y);
                ECFieldElement multiply2 = a;
                ECFieldElement multiply3 = b;
                ECFieldElement multiply4 = multiply;
                if (coordinateSystem != 0) {
                    if (coordinateSystem != 1) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                    final ECFieldElement ecFieldElement = this.zs[0];
                    multiply2 = a;
                    multiply3 = b;
                    multiply4 = multiply;
                    if (!ecFieldElement.isOne()) {
                        final ECFieldElement multiply5 = ecFieldElement.multiply(ecFieldElement.square());
                        multiply4 = multiply.multiply(ecFieldElement);
                        multiply2 = a.multiply(ecFieldElement);
                        multiply3 = b.multiply(multiply5);
                    }
                }
                return multiply4.equals(x.add(multiply2).multiply(x.square()).add(multiply3));
            }
            final ECFieldElement ecFieldElement2 = this.zs[0];
            final boolean one = ecFieldElement2.isOne();
            if (x.isZero()) {
                final ECFieldElement square = this.y.square();
                ECFieldElement multiply6 = b;
                if (!one) {
                    multiply6 = b.multiply(ecFieldElement2.square());
                }
                return square.equals(multiply6);
            }
            final ECFieldElement y2 = this.y;
            final ECFieldElement square2 = x.square();
            ECFieldElement ecFieldElement3;
            ECFieldElement squarePlusProduct;
            if (one) {
                final ECFieldElement add = y2.square().add(y2).add(a);
                final ECFieldElement add2 = square2.square().add(b);
                ecFieldElement3 = add;
                squarePlusProduct = add2;
            }
            else {
                final ECFieldElement square3 = ecFieldElement2.square();
                final ECFieldElement square4 = square3.square();
                final ECFieldElement multiplyPlusProduct = y2.add(ecFieldElement2).multiplyPlusProduct(y2, a, square3);
                squarePlusProduct = square2.squarePlusProduct(b, square4);
                ecFieldElement3 = multiplyPlusProduct;
            }
            return ecFieldElement3.multiply(square2).equals(squarePlusProduct);
        }
        
        @Override
        public ECPoint scaleX(ECFieldElement ecFieldElement) {
            if (this.isInfinity()) {
                return this;
            }
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            if (curveCoordinateSystem == 5) {
                final ECFieldElement rawXCoord = this.getRawXCoord();
                ecFieldElement = this.getRawYCoord().add(rawXCoord).divide(ecFieldElement).add(rawXCoord.multiply(ecFieldElement));
                return this.getCurve().createRawPoint(rawXCoord, ecFieldElement, this.getRawZCoords(), this.withCompression);
            }
            if (curveCoordinateSystem != 6) {
                return super.scaleX(ecFieldElement);
            }
            final ECFieldElement rawXCoord2 = this.getRawXCoord();
            final ECFieldElement rawYCoord = this.getRawYCoord();
            final ECFieldElement ecFieldElement2 = this.getRawZCoords()[0];
            final ECFieldElement multiply = rawXCoord2.multiply(ecFieldElement.square());
            final ECFieldElement add = rawYCoord.add(rawXCoord2).add(multiply);
            ecFieldElement = ecFieldElement2.multiply(ecFieldElement);
            return this.getCurve().createRawPoint(multiply, add, new ECFieldElement[] { ecFieldElement }, this.withCompression);
        }
        
        @Override
        public ECPoint scaleY(ECFieldElement add) {
            if (this.isInfinity()) {
                return this;
            }
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            if (curveCoordinateSystem != 5 && curveCoordinateSystem != 6) {
                return super.scaleY(add);
            }
            final ECFieldElement rawXCoord = this.getRawXCoord();
            add = this.getRawYCoord().add(rawXCoord).multiply(add).add(rawXCoord);
            return this.getCurve().createRawPoint(rawXCoord, add, this.getRawZCoords(), this.withCompression);
        }
        
        @Override
        public ECPoint subtract(final ECPoint ecPoint) {
            if (ecPoint.isInfinity()) {
                return this;
            }
            return this.add(ecPoint.negate());
        }
        
        public AbstractF2m tau() {
            if (this.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            final ECFieldElement x = this.x;
            Label_0122: {
                if (coordinateSystem == 0) {
                    break Label_0122;
                }
                if (coordinateSystem != 1) {
                    if (coordinateSystem == 5) {
                        break Label_0122;
                    }
                    if (coordinateSystem != 6) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                }
                final ECPoint ecPoint = curve.createRawPoint(x.square(), this.y.square(), new ECFieldElement[] { this.zs[0].square() }, this.withCompression);
                return (AbstractF2m)ecPoint;
            }
            final ECPoint ecPoint = curve.createRawPoint(x.square(), this.y.square(), this.withCompression);
            return (AbstractF2m)ecPoint;
        }
        
        public AbstractF2m tauPow(final int n) {
            if (this.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            final ECFieldElement x = this.x;
            Label_0130: {
                if (coordinateSystem == 0) {
                    break Label_0130;
                }
                if (coordinateSystem != 1) {
                    if (coordinateSystem == 5) {
                        break Label_0130;
                    }
                    if (coordinateSystem != 6) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                }
                final ECPoint ecPoint = curve.createRawPoint(x.squarePow(n), this.y.squarePow(n), new ECFieldElement[] { this.zs[0].squarePow(n) }, this.withCompression);
                return (AbstractF2m)ecPoint;
            }
            final ECPoint ecPoint = curve.createRawPoint(x.squarePow(n), this.y.squarePow(n), this.withCompression);
            return (AbstractF2m)ecPoint;
        }
    }
    
    public abstract static class AbstractFp extends ECPoint
    {
        protected AbstractFp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
        }
        
        protected AbstractFp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array) {
            super(ecCurve, ecFieldElement, ecFieldElement2, array);
        }
        
        @Override
        protected boolean getCompressionYTilde() {
            return this.getAffineYCoord().testBitZero();
        }
        
        @Override
        protected boolean satisfiesCurveEquation() {
            final ECFieldElement x = this.x;
            final ECFieldElement y = this.y;
            final ECFieldElement a = this.curve.getA();
            final ECFieldElement b = this.curve.getB();
            final ECFieldElement square = y.square();
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            ECFieldElement multiply = square;
            ECFieldElement ecFieldElement = a;
            ECFieldElement ecFieldElement2 = b;
            if (curveCoordinateSystem != 0) {
                if (curveCoordinateSystem != 1) {
                    if (curveCoordinateSystem != 2 && curveCoordinateSystem != 3 && curveCoordinateSystem != 4) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                    final ECFieldElement ecFieldElement3 = this.zs[0];
                    multiply = square;
                    ecFieldElement = a;
                    ecFieldElement2 = b;
                    if (!ecFieldElement3.isOne()) {
                        final ECFieldElement square2 = ecFieldElement3.square();
                        final ECFieldElement square3 = square2.square();
                        final ECFieldElement multiply2 = square2.multiply(square3);
                        ecFieldElement = a.multiply(square3);
                        ecFieldElement2 = b.multiply(multiply2);
                        multiply = square;
                    }
                }
                else {
                    final ECFieldElement ecFieldElement4 = this.zs[0];
                    multiply = square;
                    ecFieldElement = a;
                    ecFieldElement2 = b;
                    if (!ecFieldElement4.isOne()) {
                        final ECFieldElement square4 = ecFieldElement4.square();
                        final ECFieldElement multiply3 = ecFieldElement4.multiply(square4);
                        multiply = square.multiply(ecFieldElement4);
                        ecFieldElement = a.multiply(square4);
                        ecFieldElement2 = b.multiply(multiply3);
                    }
                }
            }
            return multiply.equals(x.square().add(ecFieldElement).multiply(x).add(ecFieldElement2));
        }
        
        @Override
        public ECPoint subtract(final ECPoint ecPoint) {
            if (ecPoint.isInfinity()) {
                return this;
            }
            return this.add(ecPoint.negate());
        }
    }
    
    public static class F2m extends AbstractF2m
    {
        public F2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            this(ecCurve, ecFieldElement, ecFieldElement2, false);
        }
        
        public F2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
            int n = false ? 1 : 0;
            final boolean b = ecFieldElement == null;
            if (ecFieldElement2 == null) {
                n = (true ? 1 : 0);
            }
            if ((b ? 1 : 0) == n) {
                if (ecFieldElement != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                    if (ecCurve != null) {
                        ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                    }
                }
                this.withCompression = withCompression;
                return;
            }
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        
        F2m(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2, array);
            this.withCompression = withCompression;
        }
        
        @Override
        public ECPoint add(final ECPoint ecPoint) {
            if (this.isInfinity()) {
                return ecPoint;
            }
            if (ecPoint.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement ecFieldElement = this.x;
            final ECFieldElement x = ecPoint.x;
            if (coordinateSystem != 0) {
                if (coordinateSystem != 1) {
                    if (coordinateSystem != 6) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                    if (ecFieldElement.isZero()) {
                        if (x.isZero()) {
                            return curve.getInfinity();
                        }
                        return ecPoint.add(this);
                    }
                    else {
                        final ECFieldElement y = this.y;
                        final ECFieldElement ecFieldElement2 = this.zs[0];
                        final ECFieldElement y2 = ecPoint.y;
                        final ECFieldElement ecFieldElement3 = ecPoint.zs[0];
                        final boolean one = ecFieldElement2.isOne();
                        ECFieldElement multiply;
                        ECFieldElement multiply2;
                        if (!one) {
                            multiply = x.multiply(ecFieldElement2);
                            multiply2 = y2.multiply(ecFieldElement2);
                        }
                        else {
                            multiply = x;
                            multiply2 = y2;
                        }
                        final boolean one2 = ecFieldElement3.isOne();
                        ECFieldElement multiply3;
                        if (!one2) {
                            ecFieldElement = ecFieldElement.multiply(ecFieldElement3);
                            multiply3 = y.multiply(ecFieldElement3);
                        }
                        else {
                            multiply3 = y;
                        }
                        final ECFieldElement add = multiply3.add(multiply2);
                        final ECFieldElement add2 = ecFieldElement.add(multiply);
                        if (!add2.isZero()) {
                            ECFieldElement ecFieldElement4;
                            ECFieldElement ecFieldElement5;
                            ECFieldElement ecFieldElement6;
                            if (x.isZero()) {
                                final ECPoint normalize = this.normalize();
                                final ECFieldElement xCoord = normalize.getXCoord();
                                final ECFieldElement yCoord = normalize.getYCoord();
                                final ECFieldElement divide = yCoord.add(y2).divide(xCoord);
                                ecFieldElement4 = divide.square().add(divide).add(xCoord).add(curve.getA());
                                if (ecFieldElement4.isZero()) {
                                    return new F2m(curve, ecFieldElement4, curve.getB().sqrt(), this.withCompression);
                                }
                                ecFieldElement5 = divide.multiply(xCoord.add(ecFieldElement4)).add(ecFieldElement4).add(yCoord).divide(ecFieldElement4).add(ecFieldElement4);
                                ecFieldElement6 = curve.fromBigInteger(ECConstants.ONE);
                            }
                            else {
                                final ECFieldElement square = add2.square();
                                final ECFieldElement multiply4 = add.multiply(ecFieldElement);
                                final ECFieldElement multiply5 = add.multiply(multiply);
                                ecFieldElement4 = multiply4.multiply(multiply5);
                                if (ecFieldElement4.isZero()) {
                                    return new F2m(curve, ecFieldElement4, curve.getB().sqrt(), this.withCompression);
                                }
                                final ECFieldElement ecFieldElement7 = ecFieldElement6 = add.multiply(square);
                                if (!one2) {
                                    ecFieldElement6 = ecFieldElement7.multiply(ecFieldElement3);
                                }
                                ecFieldElement5 = multiply5.add(square).squarePlusProduct(ecFieldElement6, y.add(ecFieldElement2));
                                if (!one) {
                                    ecFieldElement6 = ecFieldElement6.multiply(ecFieldElement2);
                                }
                            }
                            return new F2m(curve, ecFieldElement4, ecFieldElement5, new ECFieldElement[] { ecFieldElement6 }, this.withCompression);
                        }
                        if (add.isZero()) {
                            return this.twice();
                        }
                        return curve.getInfinity();
                    }
                }
                else {
                    final ECFieldElement y3 = this.y;
                    final ECFieldElement ecFieldElement8 = this.zs[0];
                    final ECFieldElement y4 = ecPoint.y;
                    final ECFieldElement ecFieldElement9 = ecPoint.zs[0];
                    final boolean one3 = ecFieldElement9.isOne();
                    final ECFieldElement multiply6 = ecFieldElement8.multiply(y4);
                    ECFieldElement multiply7;
                    if (one3) {
                        multiply7 = y3;
                    }
                    else {
                        multiply7 = y3.multiply(ecFieldElement9);
                    }
                    final ECFieldElement add3 = multiply6.add(multiply7);
                    final ECFieldElement multiply8 = ecFieldElement8.multiply(x);
                    ECFieldElement multiply9;
                    if (one3) {
                        multiply9 = ecFieldElement;
                    }
                    else {
                        multiply9 = ecFieldElement.multiply(ecFieldElement9);
                    }
                    final ECFieldElement add4 = multiply8.add(multiply9);
                    if (!add4.isZero()) {
                        final ECFieldElement square2 = add4.square();
                        final ECFieldElement multiply10 = square2.multiply(add4);
                        ECFieldElement multiply11;
                        if (one3) {
                            multiply11 = ecFieldElement8;
                        }
                        else {
                            multiply11 = ecFieldElement8.multiply(ecFieldElement9);
                        }
                        final ECFieldElement add5 = add3.add(add4);
                        final ECFieldElement add6 = add5.multiplyPlusProduct(add3, square2, curve.getA()).multiply(multiply11).add(multiply10);
                        final ECFieldElement multiply12 = add4.multiply(add6);
                        ECFieldElement multiply13;
                        if (one3) {
                            multiply13 = square2;
                        }
                        else {
                            multiply13 = square2.multiply(ecFieldElement9);
                        }
                        return new F2m(curve, multiply12, add3.multiplyPlusProduct(ecFieldElement, add4, y3).multiplyPlusProduct(multiply13, add5, add6), new ECFieldElement[] { multiply10.multiply(multiply11) }, this.withCompression);
                    }
                    if (add3.isZero()) {
                        return this.twice();
                    }
                    return curve.getInfinity();
                }
            }
            else {
                final ECFieldElement y5 = this.y;
                final ECFieldElement y6 = ecPoint.y;
                final ECFieldElement add7 = ecFieldElement.add(x);
                final ECFieldElement add8 = y5.add(y6);
                if (!add7.isZero()) {
                    final ECFieldElement divide2 = add8.divide(add7);
                    final ECFieldElement add9 = divide2.square().add(divide2).add(add7).add(curve.getA());
                    return new F2m(curve, add9, divide2.multiply(ecFieldElement.add(add9)).add(add9).add(y5), this.withCompression);
                }
                if (add8.isZero()) {
                    return this.twice();
                }
                return curve.getInfinity();
            }
        }
        
        @Override
        protected ECPoint detach() {
            return new F2m(null, this.getAffineXCoord(), this.getAffineYCoord());
        }
        
        @Override
        protected boolean getCompressionYTilde() {
            final ECFieldElement rawXCoord = this.getRawXCoord();
            final boolean zero = rawXCoord.isZero();
            boolean b = false;
            if (zero) {
                return false;
            }
            final ECFieldElement rawYCoord = this.getRawYCoord();
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            if (curveCoordinateSystem != 5 && curveCoordinateSystem != 6) {
                return rawYCoord.divide(rawXCoord).testBitZero();
            }
            if (rawYCoord.testBitZero() != rawXCoord.testBitZero()) {
                b = true;
            }
            return b;
        }
        
        @Override
        public ECFieldElement getYCoord() {
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            if (curveCoordinateSystem != 5 && curveCoordinateSystem != 6) {
                return this.y;
            }
            final ECFieldElement x = this.x;
            final ECFieldElement y = this.y;
            if (this.isInfinity()) {
                return y;
            }
            if (x.isZero()) {
                return y;
            }
            ECFieldElement ecFieldElement2;
            final ECFieldElement ecFieldElement = ecFieldElement2 = y.add(x).multiply(x);
            if (6 == curveCoordinateSystem) {
                final ECFieldElement ecFieldElement3 = this.zs[0];
                ecFieldElement2 = ecFieldElement;
                if (!ecFieldElement3.isOne()) {
                    ecFieldElement2 = ecFieldElement.divide(ecFieldElement3);
                }
            }
            return ecFieldElement2;
        }
        
        @Override
        public ECPoint negate() {
            if (this.isInfinity()) {
                return this;
            }
            final ECFieldElement x = this.x;
            if (x.isZero()) {
                return this;
            }
            final int curveCoordinateSystem = this.getCurveCoordinateSystem();
            if (curveCoordinateSystem == 0) {
                return new F2m(this.curve, x, this.y.add(x), this.withCompression);
            }
            if (curveCoordinateSystem == 1) {
                return new F2m(this.curve, x, this.y.add(x), new ECFieldElement[] { this.zs[0] }, this.withCompression);
            }
            if (curveCoordinateSystem == 5) {
                return new F2m(this.curve, x, this.y.addOne(), this.withCompression);
            }
            if (curveCoordinateSystem == 6) {
                final ECFieldElement y = this.y;
                final ECFieldElement ecFieldElement = this.zs[0];
                return new F2m(this.curve, x, y.add(ecFieldElement), new ECFieldElement[] { ecFieldElement }, this.withCompression);
            }
            throw new IllegalStateException("unsupported coordinate system");
        }
        
        @Override
        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            ECFieldElement ecFieldElement = this.x;
            if (ecFieldElement.isZero()) {
                return curve.getInfinity();
            }
            final int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem == 0) {
                final ECFieldElement add = this.y.divide(ecFieldElement).add(ecFieldElement);
                final ECFieldElement add2 = add.square().add(add).add(curve.getA());
                return new F2m(curve, add2, ecFieldElement.squarePlusProduct(add2, add.addOne()), this.withCompression);
            }
            if (coordinateSystem == 1) {
                ECFieldElement ecFieldElement2 = this.y;
                final ECFieldElement ecFieldElement3 = this.zs[0];
                final boolean one = ecFieldElement3.isOne();
                ECFieldElement multiply;
                if (one) {
                    multiply = ecFieldElement;
                }
                else {
                    multiply = ecFieldElement.multiply(ecFieldElement3);
                }
                if (!one) {
                    ecFieldElement2 = ecFieldElement2.multiply(ecFieldElement3);
                }
                final ECFieldElement square = ecFieldElement.square();
                final ECFieldElement add3 = square.add(ecFieldElement2);
                final ECFieldElement square2 = multiply.square();
                final ECFieldElement add4 = add3.add(multiply);
                final ECFieldElement multiplyPlusProduct = add4.multiplyPlusProduct(add3, square2, curve.getA());
                return new F2m(curve, multiply.multiply(multiplyPlusProduct), square.square().multiplyPlusProduct(multiply, multiplyPlusProduct, add4), new ECFieldElement[] { multiply.multiply(square2) }, this.withCompression);
            }
            if (coordinateSystem != 6) {
                throw new IllegalStateException("unsupported coordinate system");
            }
            final ECFieldElement y = this.y;
            final ECFieldElement ecFieldElement4 = this.zs[0];
            final boolean one2 = ecFieldElement4.isOne();
            ECFieldElement multiply2;
            if (one2) {
                multiply2 = y;
            }
            else {
                multiply2 = y.multiply(ecFieldElement4);
            }
            ECFieldElement square3;
            if (one2) {
                square3 = ecFieldElement4;
            }
            else {
                square3 = ecFieldElement4.square();
            }
            final ECFieldElement a = curve.getA();
            ECFieldElement multiply3;
            if (one2) {
                multiply3 = a;
            }
            else {
                multiply3 = a.multiply(square3);
            }
            final ECFieldElement add5 = y.square().add(multiply2).add(multiply3);
            if (add5.isZero()) {
                return new F2m(curve, add5, curve.getB().sqrt(), this.withCompression);
            }
            final ECFieldElement square4 = add5.square();
            ECFieldElement multiply4;
            if (one2) {
                multiply4 = add5;
            }
            else {
                multiply4 = add5.multiply(square3);
            }
            final ECFieldElement b = curve.getB();
            ECFieldElement ecFieldElement6;
            if (b.bitLength() < curve.getFieldSize() >> 1) {
                final ECFieldElement square5 = y.add(ecFieldElement).square();
                ECFieldElement ecFieldElement5;
                if (b.isOne()) {
                    ecFieldElement5 = multiply3.add(square3).square();
                }
                else {
                    ecFieldElement5 = multiply3.squarePlusProduct(b, square3.square());
                }
                ecFieldElement6 = square5.add(add5).add(square3).multiply(square5).add(ecFieldElement5).add(square4);
                if (!a.isZero()) {
                    ECFieldElement ecFieldElement7 = ecFieldElement6;
                    if (!a.isOne()) {
                        ecFieldElement7 = ecFieldElement6.add(a.addOne().multiply(multiply4));
                        return new F2m(curve, square4, ecFieldElement7, new ECFieldElement[] { multiply4 }, this.withCompression);
                    }
                    return new F2m(curve, square4, ecFieldElement7, new ECFieldElement[] { multiply4 }, this.withCompression);
                }
            }
            else {
                if (!one2) {
                    ecFieldElement = ecFieldElement.multiply(ecFieldElement4);
                }
                ecFieldElement6 = ecFieldElement.squarePlusProduct(add5, multiply2).add(square4);
            }
            ECFieldElement ecFieldElement7 = ecFieldElement6.add(multiply4);
            return new F2m(curve, square4, ecFieldElement7, new ECFieldElement[] { multiply4 }, this.withCompression);
        }
        
        @Override
        public ECPoint twicePlus(final ECPoint ecPoint) {
            if (this.isInfinity()) {
                return ecPoint;
            }
            if (ecPoint.isInfinity()) {
                return this.twice();
            }
            final ECCurve curve = this.getCurve();
            final ECFieldElement x = this.x;
            if (x.isZero()) {
                return ecPoint;
            }
            if (curve.getCoordinateSystem() != 6) {
                return this.twice().add(ecPoint);
            }
            final ECFieldElement x2 = ecPoint.x;
            final ECFieldElement ecFieldElement = ecPoint.zs[0];
            if (x2.isZero() || !ecFieldElement.isOne()) {
                return this.twice().add(ecPoint);
            }
            final ECFieldElement y = this.y;
            final ECFieldElement ecFieldElement2 = this.zs[0];
            final ECFieldElement y2 = ecPoint.y;
            final ECFieldElement square = x.square();
            final ECFieldElement square2 = y.square();
            final ECFieldElement square3 = ecFieldElement2.square();
            final ECFieldElement add = curve.getA().multiply(square3).add(square2).add(y.multiply(ecFieldElement2));
            final ECFieldElement addOne = y2.addOne();
            final ECFieldElement multiplyPlusProduct = curve.getA().add(addOne).multiply(square3).add(square2).multiplyPlusProduct(add, square, square3);
            final ECFieldElement multiply = x2.multiply(square3);
            final ECFieldElement square4 = multiply.add(add).square();
            if (square4.isZero()) {
                if (multiplyPlusProduct.isZero()) {
                    return ecPoint.twice();
                }
                return curve.getInfinity();
            }
            else {
                if (multiplyPlusProduct.isZero()) {
                    return new F2m(curve, multiplyPlusProduct, curve.getB().sqrt(), this.withCompression);
                }
                final ECFieldElement multiply2 = multiplyPlusProduct.square().multiply(multiply);
                final ECFieldElement multiply3 = multiplyPlusProduct.multiply(square4).multiply(square3);
                return new F2m(curve, multiply2, multiplyPlusProduct.add(square4).square().multiplyPlusProduct(add, addOne, multiply3), new ECFieldElement[] { multiply3 }, this.withCompression);
            }
        }
    }
    
    public static class Fp extends AbstractFp
    {
        public Fp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
            this(ecCurve, ecFieldElement, ecFieldElement2, false);
        }
        
        public Fp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2);
            int n = false ? 1 : 0;
            final boolean b = ecFieldElement == null;
            if (ecFieldElement2 == null) {
                n = (true ? 1 : 0);
            }
            if ((b ? 1 : 0) == n) {
                this.withCompression = withCompression;
                return;
            }
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        
        Fp(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
            super(ecCurve, ecFieldElement, ecFieldElement2, array);
            this.withCompression = withCompression;
        }
        
        @Override
        public ECPoint add(final ECPoint ecPoint) {
            if (this.isInfinity()) {
                return ecPoint;
            }
            if (ecPoint.isInfinity()) {
                return this;
            }
            if (this == ecPoint) {
                return this.twice();
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement ecFieldElement = this.x;
            ECFieldElement ecFieldElement2 = this.y;
            ECFieldElement ecFieldElement3 = ecPoint.x;
            ECFieldElement ecFieldElement4 = ecPoint.y;
            if (coordinateSystem != 0) {
                if (coordinateSystem != 1) {
                    if (coordinateSystem != 2 && coordinateSystem != 4) {
                        throw new IllegalStateException("unsupported coordinate system");
                    }
                    final ECFieldElement ecFieldElement5 = this.zs[0];
                    final ECFieldElement ecFieldElement6 = ecPoint.zs[0];
                    final boolean one = ecFieldElement5.isOne();
                    ECFieldElement ecFieldElement7 = null;
                    ECFieldElement ecFieldElement8 = null;
                    ECFieldElement ecFieldElement9 = null;
                    ECFieldElement square4 = null;
                    Label_0485: {
                        if (!one && ecFieldElement5.equals(ecFieldElement6)) {
                            final ECFieldElement subtract = ecFieldElement.subtract(ecFieldElement3);
                            final ECFieldElement subtract2 = ecFieldElement2.subtract(ecFieldElement4);
                            if (subtract.isZero()) {
                                if (subtract2.isZero()) {
                                    return this.twice();
                                }
                                return curve.getInfinity();
                            }
                            else {
                                final ECFieldElement square = subtract.square();
                                final ECFieldElement multiply = ecFieldElement.multiply(square);
                                final ECFieldElement multiply2 = ecFieldElement3.multiply(square);
                                final ECFieldElement multiply3 = multiply.subtract(multiply2).multiply(ecFieldElement2);
                                ecFieldElement7 = subtract2.square().subtract(multiply).subtract(multiply2);
                                ecFieldElement8 = multiply.subtract(ecFieldElement7).multiply(subtract2).subtract(multiply3);
                                ecFieldElement9 = subtract.multiply(ecFieldElement5);
                            }
                        }
                        else {
                            if (!one) {
                                final ECFieldElement square2 = ecFieldElement5.square();
                                ecFieldElement3 = square2.multiply(ecFieldElement3);
                                ecFieldElement4 = square2.multiply(ecFieldElement5).multiply(ecFieldElement4);
                            }
                            final boolean one2 = ecFieldElement6.isOne();
                            if (!one2) {
                                final ECFieldElement square3 = ecFieldElement6.square();
                                ecFieldElement = square3.multiply(ecFieldElement);
                                ecFieldElement2 = square3.multiply(ecFieldElement6).multiply(ecFieldElement2);
                            }
                            final ECFieldElement subtract3 = ecFieldElement.subtract(ecFieldElement3);
                            final ECFieldElement subtract4 = ecFieldElement2.subtract(ecFieldElement4);
                            if (subtract3.isZero()) {
                                if (subtract4.isZero()) {
                                    return this.twice();
                                }
                                return curve.getInfinity();
                            }
                            else {
                                square4 = subtract3.square();
                                final ECFieldElement multiply4 = square4.multiply(subtract3);
                                final ECFieldElement multiply5 = square4.multiply(ecFieldElement);
                                ecFieldElement7 = subtract4.square().add(multiply4).subtract(this.two(multiply5));
                                ecFieldElement8 = multiply5.subtract(ecFieldElement7).multiplyMinusProduct(subtract4, multiply4, ecFieldElement2);
                                if (!one) {
                                    ecFieldElement9 = subtract3.multiply(ecFieldElement5);
                                }
                                else {
                                    ecFieldElement9 = subtract3;
                                }
                                if (!one2) {
                                    ecFieldElement9 = ecFieldElement9.multiply(ecFieldElement6);
                                }
                                if (ecFieldElement9 == subtract3) {
                                    break Label_0485;
                                }
                            }
                        }
                        square4 = null;
                    }
                    ECFieldElement[] array;
                    if (coordinateSystem == 4) {
                        array = new ECFieldElement[] { ecFieldElement9, this.calculateJacobianModifiedW(ecFieldElement9, square4) };
                    }
                    else {
                        array = new ECFieldElement[] { ecFieldElement9 };
                    }
                    return new Fp(curve, ecFieldElement7, ecFieldElement8, array, this.withCompression);
                }
                else {
                    final ECFieldElement ecFieldElement10 = this.zs[0];
                    ECFieldElement multiply6 = ecPoint.zs[0];
                    final boolean one3 = ecFieldElement10.isOne();
                    final boolean one4 = multiply6.isOne();
                    if (!one3) {
                        ecFieldElement4 = ecFieldElement4.multiply(ecFieldElement10);
                    }
                    if (!one4) {
                        ecFieldElement2 = ecFieldElement2.multiply(multiply6);
                    }
                    final ECFieldElement subtract5 = ecFieldElement4.subtract(ecFieldElement2);
                    if (!one3) {
                        ecFieldElement3 = ecFieldElement3.multiply(ecFieldElement10);
                    }
                    if (!one4) {
                        ecFieldElement = ecFieldElement.multiply(multiply6);
                    }
                    final ECFieldElement subtract6 = ecFieldElement3.subtract(ecFieldElement);
                    if (!subtract6.isZero()) {
                        if (!one3) {
                            if (one4) {
                                multiply6 = ecFieldElement10;
                            }
                            else {
                                multiply6 = ecFieldElement10.multiply(multiply6);
                            }
                        }
                        final ECFieldElement square5 = subtract6.square();
                        final ECFieldElement multiply7 = square5.multiply(subtract6);
                        final ECFieldElement multiply8 = square5.multiply(ecFieldElement);
                        final ECFieldElement subtract7 = subtract5.square().multiply(multiply6).subtract(multiply7).subtract(this.two(multiply8));
                        return new Fp(curve, subtract6.multiply(subtract7), multiply8.subtract(subtract7).multiplyMinusProduct(subtract5, ecFieldElement2, multiply7), new ECFieldElement[] { multiply7.multiply(multiply6) }, this.withCompression);
                    }
                    if (subtract5.isZero()) {
                        return this.twice();
                    }
                    return curve.getInfinity();
                }
            }
            else {
                final ECFieldElement subtract8 = ecFieldElement3.subtract(ecFieldElement);
                final ECFieldElement subtract9 = ecFieldElement4.subtract(ecFieldElement2);
                if (!subtract8.isZero()) {
                    final ECFieldElement divide = subtract9.divide(subtract8);
                    final ECFieldElement subtract10 = divide.square().subtract(ecFieldElement).subtract(ecFieldElement3);
                    return new Fp(curve, subtract10, divide.multiply(ecFieldElement.subtract(subtract10)).subtract(ecFieldElement2), this.withCompression);
                }
                if (subtract9.isZero()) {
                    return this.twice();
                }
                return curve.getInfinity();
            }
        }
        
        protected ECFieldElement calculateJacobianModifiedW(ECFieldElement square, ECFieldElement negate) {
            final ECFieldElement a = this.getCurve().getA();
            if (a.isZero()) {
                return a;
            }
            if (square.isOne()) {
                return a;
            }
            ECFieldElement square2;
            if ((square2 = negate) == null) {
                square2 = square.square();
            }
            square = square2.square();
            negate = a.negate();
            if (negate.bitLength() < a.bitLength()) {
                return square.multiply(negate).negate();
            }
            return square.multiply(a);
        }
        
        @Override
        protected ECPoint detach() {
            return new Fp(null, this.getAffineXCoord(), this.getAffineYCoord());
        }
        
        protected ECFieldElement doubleProductFromSquares(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3, final ECFieldElement ecFieldElement4) {
            return ecFieldElement.add(ecFieldElement2).square().subtract(ecFieldElement3).subtract(ecFieldElement4);
        }
        
        protected ECFieldElement eight(final ECFieldElement ecFieldElement) {
            return this.four(this.two(ecFieldElement));
        }
        
        protected ECFieldElement four(final ECFieldElement ecFieldElement) {
            return this.two(this.two(ecFieldElement));
        }
        
        protected ECFieldElement getJacobianModifiedW() {
            ECFieldElement calculateJacobianModifiedW;
            if ((calculateJacobianModifiedW = this.zs[1]) == null) {
                final ECFieldElement[] zs = this.zs;
                calculateJacobianModifiedW = this.calculateJacobianModifiedW(this.zs[0], null);
                zs[1] = calculateJacobianModifiedW;
            }
            return calculateJacobianModifiedW;
        }
        
        @Override
        public ECFieldElement getZCoord(final int n) {
            if (n == 1 && 4 == this.getCurveCoordinateSystem()) {
                return this.getJacobianModifiedW();
            }
            return super.getZCoord(n);
        }
        
        @Override
        public ECPoint negate() {
            if (this.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            if (curve.getCoordinateSystem() != 0) {
                return new Fp(curve, this.x, this.y.negate(), this.zs, this.withCompression);
            }
            return new Fp(curve, this.x, this.y.negate(), this.withCompression);
        }
        
        protected ECFieldElement three(final ECFieldElement ecFieldElement) {
            return this.two(ecFieldElement).add(ecFieldElement);
        }
        
        @Override
        public ECPoint threeTimes() {
            if (this.isInfinity()) {
                return this;
            }
            final ECFieldElement y = this.y;
            if (y.isZero()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem != 0) {
                if (coordinateSystem != 4) {
                    return this.twice().add(this);
                }
                return this.twiceJacobianModified(false).add(this);
            }
            else {
                final ECFieldElement x = this.x;
                final ECFieldElement two = this.two(y);
                final ECFieldElement square = two.square();
                final ECFieldElement add = this.three(x.square()).add(this.getCurve().getA());
                final ECFieldElement subtract = this.three(x).multiply(square).subtract(add.square());
                if (subtract.isZero()) {
                    return this.getCurve().getInfinity();
                }
                final ECFieldElement invert = subtract.multiply(two).invert();
                final ECFieldElement multiply = subtract.multiply(invert).multiply(add);
                final ECFieldElement subtract2 = square.square().multiply(invert).subtract(multiply);
                final ECFieldElement add2 = subtract2.subtract(multiply).multiply(multiply.add(subtract2)).add(x);
                return new Fp(curve, add2, x.subtract(add2).multiply(subtract2).subtract(y), this.withCompression);
            }
        }
        
        @Override
        public ECPoint timesPow2(final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("'e' cannot be negative");
            }
            if (n == 0) {
                return this;
            }
            if (this.isInfinity()) {
                return this;
            }
            if (n == 1) {
                return this.twice();
            }
            final ECCurve curve = this.getCurve();
            final ECFieldElement y = this.y;
            if (y.isZero()) {
                return curve.getInfinity();
            }
            final int coordinateSystem = curve.getCoordinateSystem();
            final ECFieldElement a = curve.getA();
            final ECFieldElement x = this.x;
            ECFieldElement fromBigInteger;
            if (this.zs.length < 1) {
                fromBigInteger = curve.fromBigInteger(ECConstants.ONE);
            }
            else {
                fromBigInteger = this.zs[0];
            }
            ECFieldElement multiply = y;
            ECFieldElement ecFieldElement = a;
            ECFieldElement multiply2 = x;
            Label_0229: {
                if (!fromBigInteger.isOne()) {
                    multiply = y;
                    ecFieldElement = a;
                    multiply2 = x;
                    if (coordinateSystem != 0) {
                        ECFieldElement square;
                        if (coordinateSystem != 1) {
                            if (coordinateSystem != 2) {
                                if (coordinateSystem == 4) {
                                    ecFieldElement = this.getJacobianModifiedW();
                                    multiply = y;
                                    multiply2 = x;
                                    break Label_0229;
                                }
                                throw new IllegalStateException("unsupported coordinate system");
                            }
                            else {
                                square = null;
                                multiply = y;
                                multiply2 = x;
                            }
                        }
                        else {
                            square = fromBigInteger.square();
                            multiply2 = x.multiply(fromBigInteger);
                            multiply = y.multiply(square);
                        }
                        ecFieldElement = this.calculateJacobianModifiedW(fromBigInteger, square);
                    }
                }
            }
            final ECFieldElement ecFieldElement2 = fromBigInteger;
            final ECFieldElement ecFieldElement3 = ecFieldElement;
            final ECFieldElement ecFieldElement4 = multiply;
            int i = 0;
            ECFieldElement multiply3 = ecFieldElement2;
            ECFieldElement ecFieldElement5 = ecFieldElement3;
            ECFieldElement subtract = multiply2;
            ECFieldElement subtract2 = ecFieldElement4;
            while (i < n) {
                if (subtract2.isZero()) {
                    return curve.getInfinity();
                }
                final ECFieldElement three = this.three(subtract.square());
                final ECFieldElement two = this.two(subtract2);
                final ECFieldElement multiply4 = two.multiply(subtract2);
                final ECFieldElement two2 = this.two(subtract.multiply(multiply4));
                final ECFieldElement two3 = this.two(multiply4.square());
                ECFieldElement two4 = ecFieldElement5;
                ECFieldElement add = three;
                if (!ecFieldElement5.isZero()) {
                    add = three.add(ecFieldElement5);
                    two4 = this.two(two3.multiply(ecFieldElement5));
                }
                subtract = add.square().subtract(this.two(two2));
                subtract2 = add.multiply(two2.subtract(subtract)).subtract(two3);
                if (multiply3.isOne()) {
                    multiply3 = two;
                }
                else {
                    multiply3 = two.multiply(multiply3);
                }
                ++i;
                ecFieldElement5 = two4;
            }
            if (coordinateSystem == 0) {
                final ECFieldElement invert = multiply3.invert();
                final ECFieldElement square2 = invert.square();
                return new Fp(curve, subtract.multiply(square2), subtract2.multiply(square2.multiply(invert)), this.withCompression);
            }
            if (coordinateSystem == 1) {
                return new Fp(curve, subtract.multiply(multiply3), subtract2, new ECFieldElement[] { multiply3.multiply(multiply3.square()) }, this.withCompression);
            }
            if (coordinateSystem == 2) {
                return new Fp(curve, subtract, subtract2, new ECFieldElement[] { multiply3 }, this.withCompression);
            }
            if (coordinateSystem == 4) {
                return new Fp(curve, subtract, subtract2, new ECFieldElement[] { multiply3, ecFieldElement5 }, this.withCompression);
            }
            throw new IllegalStateException("unsupported coordinate system");
        }
        
        @Override
        public ECPoint twice() {
            if (this.isInfinity()) {
                return this;
            }
            final ECCurve curve = this.getCurve();
            final ECFieldElement y = this.y;
            if (y.isZero()) {
                return curve.getInfinity();
            }
            final int coordinateSystem = curve.getCoordinateSystem();
            final ECFieldElement x = this.x;
            if (coordinateSystem == 0) {
                final ECFieldElement divide = this.three(x.square()).add(this.getCurve().getA()).divide(this.two(y));
                final ECFieldElement subtract = divide.square().subtract(this.two(x));
                return new Fp(curve, subtract, divide.multiply(x.subtract(subtract)).subtract(y), this.withCompression);
            }
            if (coordinateSystem == 1) {
                final ECFieldElement ecFieldElement = this.zs[0];
                final boolean one = ecFieldElement.isOne();
                ECFieldElement ecFieldElement3;
                final ECFieldElement ecFieldElement2 = ecFieldElement3 = curve.getA();
                if (!ecFieldElement2.isZero()) {
                    ecFieldElement3 = ecFieldElement2;
                    if (!one) {
                        ecFieldElement3 = ecFieldElement2.multiply(ecFieldElement.square());
                    }
                }
                final ECFieldElement add = ecFieldElement3.add(this.three(x.square()));
                ECFieldElement multiply;
                if (one) {
                    multiply = y;
                }
                else {
                    multiply = y.multiply(ecFieldElement);
                }
                ECFieldElement ecFieldElement4;
                if (one) {
                    ecFieldElement4 = y.square();
                }
                else {
                    ecFieldElement4 = multiply.multiply(y);
                }
                final ECFieldElement four = this.four(x.multiply(ecFieldElement4));
                final ECFieldElement subtract2 = add.square().subtract(this.two(four));
                final ECFieldElement two = this.two(multiply);
                final ECFieldElement multiply2 = subtract2.multiply(two);
                final ECFieldElement two2 = this.two(ecFieldElement4);
                final ECFieldElement subtract3 = four.subtract(subtract2).multiply(add).subtract(this.two(two2.square()));
                ECFieldElement ecFieldElement5;
                if (one) {
                    ecFieldElement5 = this.two(two2);
                }
                else {
                    ecFieldElement5 = two.square();
                }
                return new Fp(curve, multiply2, subtract3, new ECFieldElement[] { this.two(ecFieldElement5).multiply(multiply) }, this.withCompression);
            }
            if (coordinateSystem == 2) {
                final ECFieldElement ecFieldElement6 = this.zs[0];
                final boolean one2 = ecFieldElement6.isOne();
                final ECFieldElement square = y.square();
                final ECFieldElement square2 = square.square();
                ECFieldElement ecFieldElement7 = curve.getA();
                final ECFieldElement negate = ecFieldElement7.negate();
                ECFieldElement ecFieldElement8 = null;
                ECFieldElement ecFieldElement9;
                if (negate.toBigInteger().equals(BigInteger.valueOf(3L))) {
                    ECFieldElement square3;
                    if (one2) {
                        square3 = ecFieldElement6;
                    }
                    else {
                        square3 = ecFieldElement6.square();
                    }
                    ecFieldElement8 = this.three(x.add(square3).multiply(x.subtract(square3)));
                    ecFieldElement9 = square.multiply(x);
                }
                else {
                    final ECFieldElement three = this.three(x.square());
                    Label_0278: {
                        if (!one2) {
                            if (ecFieldElement7.isZero()) {
                                ecFieldElement8 = three;
                                break Label_0278;
                            }
                            final ECFieldElement square4 = ecFieldElement6.square().square();
                            if (negate.bitLength() < ecFieldElement7.bitLength()) {
                                ecFieldElement8 = three.subtract(square4.multiply(negate));
                                break Label_0278;
                            }
                            ecFieldElement7 = square4.multiply(ecFieldElement7);
                        }
                        ecFieldElement8 = three.add(ecFieldElement7);
                    }
                    ecFieldElement9 = x.multiply(square);
                }
                final ECFieldElement four2 = this.four(ecFieldElement9);
                final ECFieldElement subtract4 = ecFieldElement8.square().subtract(this.two(four2));
                final ECFieldElement subtract5 = four2.subtract(subtract4).multiply(ecFieldElement8).subtract(this.eight(square2));
                ECFieldElement ecFieldElement10 = this.two(y);
                if (!one2) {
                    ecFieldElement10 = ecFieldElement10.multiply(ecFieldElement6);
                }
                return new Fp(curve, subtract4, subtract5, new ECFieldElement[] { ecFieldElement10 }, this.withCompression);
            }
            if (coordinateSystem == 4) {
                return this.twiceJacobianModified(true);
            }
            throw new IllegalStateException("unsupported coordinate system");
        }
        
        protected Fp twiceJacobianModified(final boolean b) {
            final ECFieldElement x = this.x;
            final ECFieldElement y = this.y;
            final ECFieldElement ecFieldElement = this.zs[0];
            final ECFieldElement jacobianModifiedW = this.getJacobianModifiedW();
            final ECFieldElement add = this.three(x.square()).add(jacobianModifiedW);
            ECFieldElement ecFieldElement2 = this.two(y);
            final ECFieldElement multiply = ecFieldElement2.multiply(y);
            final ECFieldElement two = this.two(x.multiply(multiply));
            final ECFieldElement subtract = add.square().subtract(this.two(two));
            final ECFieldElement two2 = this.two(multiply.square());
            final ECFieldElement subtract2 = add.multiply(two.subtract(subtract)).subtract(two2);
            ECFieldElement two3;
            if (b) {
                two3 = this.two(two2.multiply(jacobianModifiedW));
            }
            else {
                two3 = null;
            }
            if (!ecFieldElement.isOne()) {
                ecFieldElement2 = ecFieldElement2.multiply(ecFieldElement);
            }
            return new Fp(this.getCurve(), subtract, subtract2, new ECFieldElement[] { ecFieldElement2, two3 }, this.withCompression);
        }
        
        @Override
        public ECPoint twicePlus(final ECPoint ecPoint) {
            if (this == ecPoint) {
                return this.threeTimes();
            }
            if (this.isInfinity()) {
                return ecPoint;
            }
            if (ecPoint.isInfinity()) {
                return this.twice();
            }
            final ECFieldElement y = this.y;
            if (y.isZero()) {
                return ecPoint;
            }
            final ECCurve curve = this.getCurve();
            final int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem != 0) {
                if (coordinateSystem != 4) {
                    return this.twice().add(ecPoint);
                }
                return this.twiceJacobianModified(false).add(ecPoint);
            }
            else {
                final ECFieldElement x = this.x;
                final ECFieldElement x2 = ecPoint.x;
                final ECFieldElement y2 = ecPoint.y;
                final ECFieldElement subtract = x2.subtract(x);
                final ECFieldElement subtract2 = y2.subtract(y);
                if (subtract.isZero()) {
                    if (subtract2.isZero()) {
                        return this.threeTimes();
                    }
                    return this;
                }
                else {
                    final ECFieldElement square = subtract.square();
                    final ECFieldElement subtract3 = square.multiply(this.two(x).add(x2)).subtract(subtract2.square());
                    if (subtract3.isZero()) {
                        return curve.getInfinity();
                    }
                    final ECFieldElement invert = subtract3.multiply(subtract).invert();
                    final ECFieldElement multiply = subtract3.multiply(invert).multiply(subtract2);
                    final ECFieldElement subtract4 = this.two(y).multiply(square).multiply(subtract).multiply(invert).subtract(multiply);
                    final ECFieldElement add = subtract4.subtract(multiply).multiply(multiply.add(subtract4)).add(x2);
                    return new Fp(curve, add, x.subtract(add).multiply(subtract4).subtract(y), this.withCompression);
                }
            }
        }
        
        protected ECFieldElement two(final ECFieldElement ecFieldElement) {
            return ecFieldElement.add(ecFieldElement);
        }
    }
}
