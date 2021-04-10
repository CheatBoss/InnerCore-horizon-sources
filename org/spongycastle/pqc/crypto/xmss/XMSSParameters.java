package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;

public final class XMSSParameters
{
    private final int height;
    private final int k;
    private final XMSSOid oid;
    private final WOTSPlus wotsPlus;
    
    public XMSSParameters(final int height, final Digest digest) {
        if (height < 2) {
            throw new IllegalArgumentException("height must be >= 2");
        }
        if (digest != null) {
            this.wotsPlus = new WOTSPlus(new WOTSPlusParameters(digest));
            this.height = height;
            this.k = this.determineMinK();
            this.oid = DefaultXMSSOid.lookup(this.getDigest().getAlgorithmName(), this.getDigestSize(), this.getWinternitzParameter(), this.wotsPlus.getParams().getLen(), height);
            return;
        }
        throw new NullPointerException("digest == null");
    }
    
    private int determineMinK() {
        int n = 2;
        while (true) {
            final int height = this.height;
            if (n > height) {
                throw new IllegalStateException("should never happen...");
            }
            if ((height - n) % 2 == 0) {
                return n;
            }
            ++n;
        }
    }
    
    protected Digest getDigest() {
        return this.wotsPlus.getParams().getDigest();
    }
    
    public int getDigestSize() {
        return this.wotsPlus.getParams().getDigestSize();
    }
    
    public int getHeight() {
        return this.height;
    }
    
    int getK() {
        return this.k;
    }
    
    WOTSPlus getWOTSPlus() {
        return this.wotsPlus;
    }
    
    public int getWinternitzParameter() {
        return this.wotsPlus.getParams().getWinternitzParameter();
    }
}
