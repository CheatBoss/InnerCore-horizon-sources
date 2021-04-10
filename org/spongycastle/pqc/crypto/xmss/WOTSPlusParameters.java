package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;

final class WOTSPlusParameters
{
    private final Digest digest;
    private final int digestSize;
    private final int len;
    private final int len1;
    private final int len2;
    private final XMSSOid oid;
    private final int winternitzParameter;
    
    protected WOTSPlusParameters(final Digest digest) {
        if (digest == null) {
            throw new NullPointerException("digest == null");
        }
        this.digest = digest;
        final int digestSize = XMSSUtil.getDigestSize(digest);
        this.digestSize = digestSize;
        this.winternitzParameter = 16;
        final double n = digestSize * 8;
        final double n2 = XMSSUtil.log2(16);
        Double.isNaN(n);
        Double.isNaN(n2);
        final int len1 = (int)Math.ceil(n / n2);
        this.len1 = len1;
        final int len2 = (int)Math.floor(XMSSUtil.log2(len1 * (this.winternitzParameter - 1)) / XMSSUtil.log2(this.winternitzParameter)) + 1;
        this.len2 = len2;
        this.len = this.len1 + len2;
        if ((this.oid = WOTSPlusOid.lookup(digest.getAlgorithmName(), this.digestSize, this.winternitzParameter, this.len)) != null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot find OID for digest algorithm: ");
        sb.append(digest.getAlgorithmName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    protected Digest getDigest() {
        return this.digest;
    }
    
    protected int getDigestSize() {
        return this.digestSize;
    }
    
    protected int getLen() {
        return this.len;
    }
    
    protected int getLen1() {
        return this.len1;
    }
    
    protected int getLen2() {
        return this.len2;
    }
    
    protected XMSSOid getOid() {
        return this.oid;
    }
    
    protected int getWinternitzParameter() {
        return this.winternitzParameter;
    }
}
