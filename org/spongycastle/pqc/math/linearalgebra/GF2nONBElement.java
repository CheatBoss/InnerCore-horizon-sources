package org.spongycastle.pqc.math.linearalgebra;

import java.math.*;
import java.security.*;

public class GF2nONBElement extends GF2nElement
{
    private static final int MAXLONG = 64;
    private static final long[] mBitmask;
    private static final int[] mIBY64;
    private static final long[] mMaxmask;
    private int mBit;
    private int mLength;
    private long[] mPol;
    
    static {
        mBitmask = new long[] { 1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L, 1048576L, 2097152L, 4194304L, 8388608L, 16777216L, 33554432L, 67108864L, 134217728L, 268435456L, 536870912L, 1073741824L, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE };
        mMaxmask = new long[] { 1L, 3L, 7L, 15L, 31L, 63L, 127L, 255L, 511L, 1023L, 2047L, 4095L, 8191L, 16383L, 32767L, 65535L, 131071L, 262143L, 524287L, 1048575L, 2097151L, 4194303L, 8388607L, 16777215L, 33554431L, 67108863L, 134217727L, 268435455L, 536870911L, 1073741823L, 2147483647L, 4294967295L, 8589934591L, 17179869183L, 34359738367L, 68719476735L, 137438953471L, 274877906943L, 549755813887L, 1099511627775L, 2199023255551L, 4398046511103L, 8796093022207L, 17592186044415L, 35184372088831L, 70368744177663L, 140737488355327L, 281474976710655L, 562949953421311L, 1125899906842623L, 2251799813685247L, 4503599627370495L, 9007199254740991L, 18014398509481983L, 36028797018963967L, 72057594037927935L, 144115188075855871L, 288230376151711743L, 576460752303423487L, 1152921504606846975L, 2305843009213693951L, 4611686018427387903L, Long.MAX_VALUE, -1L };
        mIBY64 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    }
    
    public GF2nONBElement(final GF2nONBElement gf2nONBElement) {
        this.mField = gf2nONBElement.mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = ((GF2nONBField)this.mField).getONBLength();
        this.mBit = ((GF2nONBField)this.mField).getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(gf2nONBElement.getElement());
    }
    
    public GF2nONBElement(final GF2nONBField mField, final BigInteger bigInteger) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = mField.getONBLength();
        this.mBit = mField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(bigInteger);
    }
    
    public GF2nONBElement(final GF2nONBField mField, final SecureRandom secureRandom) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = mField.getONBLength();
        this.mBit = mField.getONBBit();
        final int mLength = this.mLength;
        final long[] mPol = new long[mLength];
        this.mPol = mPol;
        int i = 0;
        if (mLength > 1) {
            while (i < this.mLength - 1) {
                this.mPol[i] = secureRandom.nextLong();
                ++i;
            }
            this.mPol[this.mLength - 1] = secureRandom.nextLong() >>> 64 - this.mBit;
            return;
        }
        mPol[0] = secureRandom.nextLong();
        final long[] mPol2 = this.mPol;
        mPol2[0] >>>= 64 - this.mBit;
    }
    
    public GF2nONBElement(final GF2nONBField mField, final byte[] array) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = mField.getONBLength();
        this.mBit = mField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(array);
    }
    
    private GF2nONBElement(final GF2nONBField mField, final long[] mPol) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = mField.getONBLength();
        this.mBit = mField.getONBBit();
        this.mPol = mPol;
    }
    
    public static GF2nONBElement ONE(final GF2nONBField gf2nONBField) {
        final int onbLength = gf2nONBField.getONBLength();
        final long[] array = new long[onbLength];
        int n = 0;
        int n2;
        while (true) {
            n2 = onbLength - 1;
            if (n >= n2) {
                break;
            }
            array[n] = -1L;
            ++n;
        }
        array[n2] = GF2nONBElement.mMaxmask[gf2nONBField.getONBBit() - 1];
        return new GF2nONBElement(gf2nONBField, array);
    }
    
    public static GF2nONBElement ZERO(final GF2nONBField gf2nONBField) {
        return new GF2nONBElement(gf2nONBField, new long[gf2nONBField.getONBLength()]);
    }
    
    private void assign(final BigInteger bigInteger) {
        this.assign(bigInteger.toByteArray());
    }
    
    private void assign(final byte[] array) {
        this.mPol = new long[this.mLength];
        for (int i = 0; i < array.length; ++i) {
            final long[] mPol = this.mPol;
            final int n = i >>> 3;
            mPol[n] |= ((long)array[array.length - 1 - i] & 0xFFL) << ((i & 0x7) << 3);
        }
    }
    
    private void assign(final long[] array) {
        System.arraycopy(array, 0, this.mPol, 0, this.mLength);
    }
    
    private long[] getElement() {
        final long[] mPol = this.mPol;
        final long[] array = new long[mPol.length];
        System.arraycopy(mPol, 0, array, 0, mPol.length);
        return array;
    }
    
    private long[] getElementReverseOrder() {
        final long[] array = new long[this.mPol.length];
        for (int i = 0; i < this.mDegree; ++i) {
            if (this.testBit(this.mDegree - i - 1)) {
                final int n = i >>> 6;
                array[n] |= GF2nONBElement.mBitmask[i & 0x3F];
            }
        }
        return array;
    }
    
    @Override
    public GFElement add(final GFElement gfElement) throws RuntimeException {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.addToThis(gfElement);
        return gf2nONBElement;
    }
    
    @Override
    public void addToThis(final GFElement gfElement) throws RuntimeException {
        if (!(gfElement instanceof GF2nONBElement)) {
            throw new RuntimeException();
        }
        final GF2nField mField = this.mField;
        final GF2nONBElement gf2nONBElement = (GF2nONBElement)gfElement;
        if (mField.equals(gf2nONBElement.mField)) {
            for (int i = 0; i < this.mLength; ++i) {
                final long[] mPol = this.mPol;
                mPol[i] ^= gf2nONBElement.mPol[i];
            }
            return;
        }
        throw new RuntimeException();
    }
    
    @Override
    void assignOne() {
        int n = 0;
        int n2;
        while (true) {
            n2 = this.mLength - 1;
            if (n >= n2) {
                break;
            }
            this.mPol[n] = -1L;
            ++n;
        }
        this.mPol[n2] = GF2nONBElement.mMaxmask[this.mBit - 1];
    }
    
    @Override
    void assignZero() {
        this.mPol = new long[this.mLength];
    }
    
    @Override
    public Object clone() {
        return new GF2nONBElement(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GF2nONBElement)) {
            return false;
        }
        final GF2nONBElement gf2nONBElement = (GF2nONBElement)o;
        for (int i = 0; i < this.mLength; ++i) {
            if (this.mPol[i] != gf2nONBElement.mPol[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.mPol.hashCode();
    }
    
    @Override
    public GF2nElement increase() {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.increaseThis();
        return gf2nONBElement;
    }
    
    @Override
    public void increaseThis() {
        this.addToThis(ONE((GF2nONBField)this.mField));
    }
    
    @Override
    public GFElement invert() throws ArithmeticException {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.invertThis();
        return gf2nONBElement;
    }
    
    public void invertThis() throws ArithmeticException {
        if (!this.isZero()) {
            int n = 31;
            for (int n2 = 0; n2 == 0 && n >= 0; --n) {
                if (((long)(this.mDegree - 1) & GF2nONBElement.mBitmask[n]) != 0x0L) {
                    n2 = 1;
                }
            }
            ZERO((GF2nONBField)this.mField);
            final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
            int i = n + 1 - 1;
            int n3 = 1;
            while (i >= 0) {
                final GF2nElement gf2nElement = (GF2nElement)gf2nONBElement.clone();
                for (int j = 1; j <= n3; ++j) {
                    gf2nElement.squareThis();
                }
                gf2nONBElement.multiplyThisBy(gf2nElement);
                final int n4 = n3 <<= 1;
                if (((long)(this.mDegree - 1) & GF2nONBElement.mBitmask[i]) != 0x0L) {
                    gf2nONBElement.squareThis();
                    gf2nONBElement.multiplyThisBy(this);
                    n3 = n4 + 1;
                }
                --i;
            }
            gf2nONBElement.squareThis();
            return;
        }
        throw new ArithmeticException();
    }
    
    @Override
    public boolean isOne() {
        final boolean b = false;
        int n;
        boolean b2;
        for (n = 0, b2 = true; n < this.mLength - 1 && b2; b2 = (b2 && (this.mPol[n] & -1L) == -1L), ++n) {}
        boolean b3;
        if (b2) {
            b3 = b;
            if (b2) {
                final long n2 = this.mPol[this.mLength - 1];
                final long[] mMaxmask = GF2nONBElement.mMaxmask;
                final int n3 = this.mBit - 1;
                b3 = b;
                if ((n2 & mMaxmask[n3]) == mMaxmask[n3]) {
                    return true;
                }
            }
        }
        else {
            b3 = b2;
        }
        return b3;
    }
    
    @Override
    public boolean isZero() {
        int n;
        boolean b;
        for (n = 0, b = true; n < this.mLength && b; b = (b && (this.mPol[n] & -1L) == 0x0L), ++n) {}
        return b;
    }
    
    @Override
    public GFElement multiply(final GFElement gfElement) throws RuntimeException {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.multiplyThisBy(gfElement);
        return gf2nONBElement;
    }
    
    @Override
    public void multiplyThisBy(final GFElement gfElement) throws RuntimeException {
        if (!(gfElement instanceof GF2nONBElement)) {
            throw new RuntimeException("The elements have different representation: not yet implemented");
        }
        final GF2nField mField = this.mField;
        final GF2nONBElement gf2nONBElement = (GF2nONBElement)gfElement;
        if (!mField.equals(gf2nONBElement.mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gfElement)) {
            this.squareThis();
            return;
        }
        final long[] mPol = this.mPol;
        final long[] mPol2 = gf2nONBElement.mPol;
        final long[] array = new long[this.mLength];
        final int[][] mMult = ((GF2nONBField)this.mField).mMult;
        final int n = this.mLength - 1;
        final int mBit = this.mBit;
        final long[] mBitmask = GF2nONBElement.mBitmask;
        final long n2 = mBitmask[63];
        final long n3 = mBitmask[mBit - 1];
        for (int i = 0; i < this.mDegree; ++i) {
            int j = 0;
            int n4 = 0;
            while (j < this.mDegree) {
                final int[] miby64 = GF2nONBElement.mIBY64;
                final int n5 = miby64[j];
                final int n6 = miby64[mMult[j][0]];
                final int n7 = mMult[j][0];
                final long n8 = mPol[n5];
                final long[] mBitmask2 = GF2nONBElement.mBitmask;
                int n9 = n4;
                if ((n8 & mBitmask2[j & 0x3F]) != 0x0L) {
                    int n10 = n4;
                    if ((mPol2[n6] & mBitmask2[n7 & 0x3F]) != 0x0L) {
                        n10 = (n4 ^ 0x1);
                    }
                    n9 = n10;
                    if (mMult[j][1] != -1) {
                        final int n11 = GF2nONBElement.mIBY64[mMult[j][1]];
                        final int n12 = mMult[j][1];
                        n9 = n10;
                        if ((mPol2[n11] & GF2nONBElement.mBitmask[n12 & 0x3F]) != 0x0L) {
                            n9 = (n10 ^ 0x1);
                        }
                    }
                }
                ++j;
                n4 = n9;
            }
            final int n13 = GF2nONBElement.mIBY64[i];
            if (n4 != 0) {
                array[n13] ^= GF2nONBElement.mBitmask[i & 0x3F];
            }
            if (this.mLength > 1) {
                final boolean b = (mPol[n] & 0x1L) == 0x1L;
                int k;
                final int n14 = k = n - 1;
                int n15 = b ? 1 : 0;
                while (k >= 0) {
                    final boolean b2 = (mPol[k] & 0x1L) != 0x0L;
                    mPol[k] >>>= 1;
                    if (n15 != 0) {
                        mPol[k] ^= n2;
                    }
                    --k;
                    n15 = (b2 ? 1 : 0);
                }
                mPol[n] >>>= 1;
                if (n15 != 0) {
                    mPol[n] ^= n3;
                }
                int n16;
                int l;
                if ((mPol2[n] & 0x1L) == 0x1L) {
                    n16 = 1;
                    l = n14;
                }
                else {
                    n16 = 0;
                    l = n14;
                }
                while (l >= 0) {
                    final boolean b3 = (mPol2[l] & 0x1L) != 0x0L;
                    mPol2[l] >>>= 1;
                    if (n16 != 0) {
                        mPol2[l] ^= n2;
                    }
                    --l;
                    n16 = (b3 ? 1 : 0);
                }
                mPol2[n] >>>= 1;
                if (n16 != 0) {
                    mPol2[n] ^= n3;
                }
            }
            else {
                final boolean b4 = (mPol[0] & 0x1L) == 0x1L;
                mPol[0] >>>= 1;
                if (b4) {
                    mPol[0] ^= n3;
                }
                final boolean b5 = (mPol2[0] & 0x1L) == 0x1L;
                mPol2[0] >>>= 1;
                if (b5) {
                    mPol2[0] ^= n3;
                }
            }
        }
        this.assign(array);
    }
    
    void reverseOrder() {
        this.mPol = this.getElementReverseOrder();
    }
    
    @Override
    public GF2nElement solveQuadraticEquation() throws RuntimeException {
        if (this.trace() != 1) {
            final long n = GF2nONBElement.mBitmask[63];
            final long[] array = new long[this.mLength];
            int i = 0;
            long n2 = 0L;
            while (i < this.mLength - 1) {
                long n3;
                for (int j = 1; j < 64; ++j, n2 = n3) {
                    final long[] mBitmask = GF2nONBElement.mBitmask;
                    if ((mBitmask[j] & this.mPol[i]) != 0x0L) {
                        n3 = n2;
                        if ((mBitmask[j - 1] & n2) != 0x0L) {
                            continue;
                        }
                    }
                    final long n4 = this.mPol[i];
                    final long[] mBitmask2 = GF2nONBElement.mBitmask;
                    if ((n4 & mBitmask2[j]) == 0x0L) {
                        n3 = n2;
                        if ((mBitmask2[j - 1] & n2) == 0x0L) {
                            continue;
                        }
                    }
                    n3 = (n2 ^ GF2nONBElement.mBitmask[j]);
                }
                array[i] = n2;
                final long n5 = n2 & n;
                if ((n5 != 0L && (this.mPol[i + 1] & 0x1L) == 0x1L) || (n5 == 0L && (this.mPol[i + 1] & 0x1L) == 0x0L)) {
                    n2 = 0L;
                }
                else {
                    n2 = 1L;
                }
                ++i;
            }
            final int mDegree = this.mDegree;
            final long n6 = this.mPol[this.mLength - 1];
            long n7;
            for (int k = 1; k < (mDegree & 0x3F); ++k, n2 = n7) {
                final long[] mBitmask3 = GF2nONBElement.mBitmask;
                if ((mBitmask3[k] & n6) != 0x0L) {
                    n7 = n2;
                    if ((mBitmask3[k - 1] & n2) != 0x0L) {
                        continue;
                    }
                }
                final long[] mBitmask4 = GF2nONBElement.mBitmask;
                if ((mBitmask4[k] & n6) == 0x0L) {
                    n7 = n2;
                    if ((mBitmask4[k - 1] & n2) == 0x0L) {
                        continue;
                    }
                }
                n7 = (GF2nONBElement.mBitmask[k] ^ n2);
            }
            array[this.mLength - 1] = n2;
            return new GF2nONBElement((GF2nONBField)this.mField, array);
        }
        throw new RuntimeException();
    }
    
    @Override
    public GF2nElement square() {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.squareThis();
        return gf2nONBElement;
    }
    
    @Override
    public GF2nElement squareRoot() {
        final GF2nONBElement gf2nONBElement = new GF2nONBElement(this);
        gf2nONBElement.squareRootThis();
        return gf2nONBElement;
    }
    
    @Override
    public void squareRootThis() {
        final long[] element = this.getElement();
        final int n = this.mLength - 1;
        final int mBit = this.mBit;
        final long n2 = GF2nONBElement.mBitmask[63];
        final boolean b = (element[0] & 0x1L) != 0x0L;
        int i = n;
        int n3 = b ? 1 : 0;
        while (i >= 0) {
            final boolean b2 = (element[i] & 0x1L) != 0x0L;
            element[i] >>>= 1;
            if (n3 != 0) {
                if (i == n) {
                    element[i] ^= GF2nONBElement.mBitmask[mBit - 1];
                }
                else {
                    element[i] ^= n2;
                }
            }
            --i;
            n3 = (b2 ? 1 : 0);
        }
        this.assign(element);
    }
    
    @Override
    public void squareThis() {
        final long[] element = this.getElement();
        final int n = this.mLength - 1;
        final int n2 = this.mBit - 1;
        final long[] mBitmask = GF2nONBElement.mBitmask;
        final long n3 = mBitmask[63];
        final long n4 = element[n];
        final long n5 = mBitmask[n2];
        final boolean b = false;
        final boolean b2 = (n4 & n5) != 0x0L;
        int i = 0;
        int n6 = b2 ? 1 : 0;
        while (i < n) {
            final boolean b3 = (element[i] & n3) != 0x0L;
            element[i] <<= 1;
            if (n6 != 0) {
                element[i] ^= 0x1L;
            }
            ++i;
            n6 = (b3 ? 1 : 0);
        }
        boolean b4 = b;
        if ((element[n] & GF2nONBElement.mBitmask[n2]) != 0x0L) {
            b4 = true;
        }
        element[n] <<= 1;
        if (n6 != 0) {
            element[n] ^= 0x1L;
        }
        if (b4) {
            element[n] ^= GF2nONBElement.mBitmask[n2 + 1];
        }
        this.assign(element);
    }
    
    @Override
    boolean testBit(final int n) {
        boolean b = false;
        if (n >= 0) {
            if (n > this.mDegree) {
                return false;
            }
            b = b;
            if ((this.mPol[n >>> 6] & GF2nONBElement.mBitmask[n & 0x3F]) != 0x0L) {
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public boolean testRightmostBit() {
        return (this.mPol[this.mLength - 1] & GF2nONBElement.mBitmask[this.mBit - 1]) != 0x0L;
    }
    
    @Override
    public byte[] toByteArray() {
        final int n = (this.mDegree - 1 >> 3) + 1;
        final byte[] array = new byte[n];
        for (int i = 0; i < n; ++i) {
            final long n2 = this.mPol[i >>> 3];
            final int n3 = (i & 0x7) << 3;
            array[n - i - 1] = (byte)((n2 & 255L << n3) >>> n3);
        }
        return array;
    }
    
    @Override
    public BigInteger toFlexiBigInt() {
        return new BigInteger(1, this.toByteArray());
    }
    
    @Override
    public String toString() {
        return this.toString(16);
    }
    
    @Override
    public String toString(int length) {
        final long[] element = this.getElement();
        final int mBit = this.mBit;
        String s = "";
        String s4;
        if (length == 2) {
            length = mBit;
            while (true) {
                --length;
                if (length < 0) {
                    break;
                }
                StringBuilder sb2;
                String s3;
                if ((element[element.length - 1] & 1L << length) == 0x0L) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    final String s2 = "0";
                    sb2 = sb;
                    s3 = s2;
                }
                else {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(s);
                    s3 = "1";
                    sb2 = sb3;
                }
                sb2.append(s3);
                s = sb2.toString();
            }
            length = element.length - 2;
            while (true) {
                s4 = s;
                if (length < 0) {
                    break;
                }
                for (int i = 63; i >= 0; --i) {
                    StringBuilder sb5;
                    String s6;
                    if ((element[length] & GF2nONBElement.mBitmask[i]) == 0x0L) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(s);
                        final String s5 = "0";
                        sb5 = sb4;
                        s6 = s5;
                    }
                    else {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append(s);
                        s6 = "1";
                        sb5 = sb6;
                    }
                    sb5.append(s6);
                    s = sb5.toString();
                }
                --length;
            }
        }
        else {
            s4 = s;
            if (length == 16) {
                final char[] array2;
                final char[] array = array2 = new char[16];
                array2[0] = '0';
                array2[1] = '1';
                array2[2] = '2';
                array2[3] = '3';
                array2[4] = '4';
                array2[5] = '5';
                array2[6] = '6';
                array2[7] = '7';
                array2[8] = '8';
                array2[9] = '9';
                array2[10] = 'a';
                array2[11] = 'b';
                array2[12] = 'c';
                array2[13] = 'd';
                array2[14] = 'e';
                array2[15] = 'f';
                length = element.length;
                while (true) {
                    --length;
                    s4 = s;
                    if (length < 0) {
                        break;
                    }
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append(s);
                    sb7.append(array[(int)(element[length] >>> 60) & 0xF]);
                    final String string = sb7.toString();
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(string);
                    sb8.append(array[(int)(element[length] >>> 56) & 0xF]);
                    final String string2 = sb8.toString();
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append(string2);
                    sb9.append(array[(int)(element[length] >>> 52) & 0xF]);
                    final String string3 = sb9.toString();
                    final StringBuilder sb10 = new StringBuilder();
                    sb10.append(string3);
                    sb10.append(array[(int)(element[length] >>> 48) & 0xF]);
                    final String string4 = sb10.toString();
                    final StringBuilder sb11 = new StringBuilder();
                    sb11.append(string4);
                    sb11.append(array[(int)(element[length] >>> 44) & 0xF]);
                    final String string5 = sb11.toString();
                    final StringBuilder sb12 = new StringBuilder();
                    sb12.append(string5);
                    sb12.append(array[(int)(element[length] >>> 40) & 0xF]);
                    final String string6 = sb12.toString();
                    final StringBuilder sb13 = new StringBuilder();
                    sb13.append(string6);
                    sb13.append(array[(int)(element[length] >>> 36) & 0xF]);
                    final String string7 = sb13.toString();
                    final StringBuilder sb14 = new StringBuilder();
                    sb14.append(string7);
                    sb14.append(array[(int)(element[length] >>> 32) & 0xF]);
                    final String string8 = sb14.toString();
                    final StringBuilder sb15 = new StringBuilder();
                    sb15.append(string8);
                    sb15.append(array[(int)(element[length] >>> 28) & 0xF]);
                    final String string9 = sb15.toString();
                    final StringBuilder sb16 = new StringBuilder();
                    sb16.append(string9);
                    sb16.append(array[(int)(element[length] >>> 24) & 0xF]);
                    final String string10 = sb16.toString();
                    final StringBuilder sb17 = new StringBuilder();
                    sb17.append(string10);
                    sb17.append(array[(int)(element[length] >>> 20) & 0xF]);
                    final String string11 = sb17.toString();
                    final StringBuilder sb18 = new StringBuilder();
                    sb18.append(string11);
                    sb18.append(array[(int)(element[length] >>> 16) & 0xF]);
                    final String string12 = sb18.toString();
                    final StringBuilder sb19 = new StringBuilder();
                    sb19.append(string12);
                    sb19.append(array[(int)(element[length] >>> 12) & 0xF]);
                    final String string13 = sb19.toString();
                    final StringBuilder sb20 = new StringBuilder();
                    sb20.append(string13);
                    sb20.append(array[(int)(element[length] >>> 8) & 0xF]);
                    final String string14 = sb20.toString();
                    final StringBuilder sb21 = new StringBuilder();
                    sb21.append(string14);
                    sb21.append(array[(int)(element[length] >>> 4) & 0xF]);
                    final String string15 = sb21.toString();
                    final StringBuilder sb22 = new StringBuilder();
                    sb22.append(string15);
                    sb22.append(array[(int)element[length] & 0xF]);
                    final String string16 = sb22.toString();
                    final StringBuilder sb23 = new StringBuilder();
                    sb23.append(string16);
                    sb23.append(" ");
                    s = sb23.toString();
                }
            }
        }
        return s4;
    }
    
    @Override
    public int trace() {
        final int n = this.mLength - 1;
        final int n2 = 0;
        int i = 0;
        int n3 = 0;
        while (i < n) {
            int n4;
            for (int j = 0; j < 64; ++j, n3 = n4) {
                n4 = n3;
                if ((this.mPol[i] & GF2nONBElement.mBitmask[j]) != 0x0L) {
                    n4 = (n3 ^ 0x1);
                }
            }
            ++i;
        }
        final int mBit = this.mBit;
        int n5 = n3;
        int n6;
        for (int k = n2; k < mBit; ++k, n5 = n6) {
            n6 = n5;
            if ((this.mPol[n] & GF2nONBElement.mBitmask[k]) != 0x0L) {
                n6 = (n5 ^ 0x1);
            }
        }
        return n5;
    }
}
