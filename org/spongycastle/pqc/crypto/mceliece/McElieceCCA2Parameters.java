package org.spongycastle.pqc.crypto.mceliece;

public class McElieceCCA2Parameters extends McElieceParameters
{
    private final String digest;
    
    public McElieceCCA2Parameters() {
        this(11, 50, "SHA-256");
    }
    
    public McElieceCCA2Parameters(final int n) {
        this(n, "SHA-256");
    }
    
    public McElieceCCA2Parameters(final int n, final int n2) {
        this(n, n2, "SHA-256");
    }
    
    public McElieceCCA2Parameters(final int n, final int n2, final int n3) {
        this(n, n2, n3, "SHA-256");
    }
    
    public McElieceCCA2Parameters(final int n, final int n2, final int n3, final String digest) {
        super(n, n2, n3);
        this.digest = digest;
    }
    
    public McElieceCCA2Parameters(final int n, final int n2, final String digest) {
        super(n, n2);
        this.digest = digest;
    }
    
    public McElieceCCA2Parameters(final int n, final String digest) {
        super(n);
        this.digest = digest;
    }
    
    public McElieceCCA2Parameters(final String s) {
        this(11, 50, s);
    }
    
    public String getDigest() {
        return this.digest;
    }
}
