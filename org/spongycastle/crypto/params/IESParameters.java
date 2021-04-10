package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class IESParameters implements CipherParameters
{
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;
    
    public IESParameters(final byte[] derivation, final byte[] encoding, final int macKeySize) {
        this.derivation = derivation;
        this.encoding = encoding;
        this.macKeySize = macKeySize;
    }
    
    public byte[] getDerivationV() {
        return this.derivation;
    }
    
    public byte[] getEncodingV() {
        return this.encoding;
    }
    
    public int getMacKeySize() {
        return this.macKeySize;
    }
}
