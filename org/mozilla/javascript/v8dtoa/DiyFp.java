package org.mozilla.javascript.v8dtoa;

class DiyFp
{
    static final int kSignificandSize = 64;
    static final long kUint64MSB = Long.MIN_VALUE;
    private int e;
    private long f;
    
    DiyFp() {
        this.f = 0L;
        this.e = 0;
    }
    
    DiyFp(final long f, final int e) {
        this.f = f;
        this.e = e;
    }
    
    static DiyFp minus(DiyFp diyFp, final DiyFp diyFp2) {
        diyFp = new DiyFp(diyFp.f, diyFp.e);
        diyFp.subtract(diyFp2);
        return diyFp;
    }
    
    static DiyFp normalize(DiyFp diyFp) {
        diyFp = new DiyFp(diyFp.f, diyFp.e);
        diyFp.normalize();
        return diyFp;
    }
    
    static DiyFp times(DiyFp diyFp, final DiyFp diyFp2) {
        diyFp = new DiyFp(diyFp.f, diyFp.e);
        diyFp.multiply(diyFp2);
        return diyFp;
    }
    
    private static boolean uint64_gte(final long n, final long n2) {
        return n == n2 || (n > n2 ^ n < 0L ^ n2 < 0L);
    }
    
    int e() {
        return this.e;
    }
    
    long f() {
        return this.f;
    }
    
    void multiply(final DiyFp diyFp) {
        final long n = this.f >>> 32;
        final long n2 = this.f & 0xFFFFFFFFL;
        final long n3 = diyFp.f >>> 32;
        final long n4 = diyFp.f & 0xFFFFFFFFL;
        final long n5 = n2 * n3;
        final long n6 = n * n4;
        this.e += diyFp.e + 64;
        this.f = n * n3 + (n6 >>> 32) + (n5 >>> 32) + ((n2 * n4 >>> 32) + (n6 & 0xFFFFFFFFL) + (n5 & 0xFFFFFFFFL) + 2147483648L >>> 32);
    }
    
    void normalize() {
        long f = this.f;
        int e = this.e;
        int e2;
        long f2;
        while (true) {
            e2 = e;
            f2 = f;
            if ((f & 0xFFC0000000000000L) != 0x0L) {
                break;
            }
            f <<= 10;
            e -= 10;
        }
        while ((f2 & Long.MIN_VALUE) == 0x0L) {
            f2 <<= 1;
            --e2;
        }
        this.f = f2;
        this.e = e2;
    }
    
    void setE(final int e) {
        this.e = e;
    }
    
    void setF(final long f) {
        this.f = f;
    }
    
    void subtract(final DiyFp diyFp) {
        this.f -= diyFp.f;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[DiyFp f:");
        sb.append(this.f);
        sb.append(", e:");
        sb.append(this.e);
        sb.append("]");
        return sb.toString();
    }
}
