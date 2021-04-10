package org.spongycastle.crypto;

public class Commitment
{
    private final byte[] commitment;
    private final byte[] secret;
    
    public Commitment(final byte[] secret, final byte[] commitment) {
        this.secret = secret;
        this.commitment = commitment;
    }
    
    public byte[] getCommitment() {
        return this.commitment;
    }
    
    public byte[] getSecret() {
        return this.secret;
    }
}
