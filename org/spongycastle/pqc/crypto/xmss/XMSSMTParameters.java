package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;

public final class XMSSMTParameters
{
    private final int height;
    private final int layers;
    private final XMSSOid oid;
    private final XMSSParameters xmssParams;
    
    public XMSSMTParameters(final int height, final int layers, final Digest digest) {
        this.height = height;
        this.layers = layers;
        this.xmssParams = new XMSSParameters(xmssTreeHeight(height, layers), digest);
        this.oid = DefaultXMSSMTOid.lookup(this.getDigest().getAlgorithmName(), this.getDigestSize(), this.getWinternitzParameter(), this.getLen(), this.getHeight(), layers);
    }
    
    private static int xmssTreeHeight(int n, final int n2) throws IllegalArgumentException {
        if (n < 2) {
            throw new IllegalArgumentException("totalHeight must be > 1");
        }
        if (n % n2 != 0) {
            throw new IllegalArgumentException("layers must divide totalHeight without remainder");
        }
        n /= n2;
        if (n != 1) {
            return n;
        }
        throw new IllegalArgumentException("height / layers must be greater than 1");
    }
    
    protected Digest getDigest() {
        return this.xmssParams.getDigest();
    }
    
    public int getDigestSize() {
        return this.xmssParams.getDigestSize();
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getLayers() {
        return this.layers;
    }
    
    protected int getLen() {
        return this.xmssParams.getWOTSPlus().getParams().getLen();
    }
    
    protected WOTSPlus getWOTSPlus() {
        return this.xmssParams.getWOTSPlus();
    }
    
    public int getWinternitzParameter() {
        return this.xmssParams.getWinternitzParameter();
    }
    
    protected XMSSParameters getXMSSParameters() {
        return this.xmssParams;
    }
}
