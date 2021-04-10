package org.spongycastle.crypto.params;

public class GOST3410ValidationParameters
{
    private int c;
    private long cL;
    private int x0;
    private long x0L;
    
    public GOST3410ValidationParameters(final int x0, final int c) {
        this.x0 = x0;
        this.c = c;
    }
    
    public GOST3410ValidationParameters(final long x0L, final long cl) {
        this.x0L = x0L;
        this.cL = cl;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GOST3410ValidationParameters)) {
            return false;
        }
        final GOST3410ValidationParameters gost3410ValidationParameters = (GOST3410ValidationParameters)o;
        return gost3410ValidationParameters.c == this.c && gost3410ValidationParameters.x0 == this.x0 && gost3410ValidationParameters.cL == this.cL && gost3410ValidationParameters.x0L == this.x0L;
    }
    
    public int getC() {
        return this.c;
    }
    
    public long getCL() {
        return this.cL;
    }
    
    public int getX0() {
        return this.x0;
    }
    
    public long getX0L() {
        return this.x0L;
    }
    
    @Override
    public int hashCode() {
        final int x0 = this.x0;
        final int c = this.c;
        final long x0L = this.x0L;
        final int n = (int)x0L;
        final int n2 = (int)(x0L >> 32);
        final long cl = this.cL;
        return x0 ^ c ^ n ^ n2 ^ (int)cl ^ (int)(cl >> 32);
    }
}
