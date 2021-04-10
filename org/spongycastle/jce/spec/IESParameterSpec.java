package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.util.*;

public class IESParameterSpec implements AlgorithmParameterSpec
{
    private int cipherKeySize;
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;
    private byte[] nonce;
    private boolean usePointCompression;
    
    public IESParameterSpec(final byte[] array, final byte[] array2, final int n) {
        this(array, array2, n, -1, null, false);
    }
    
    public IESParameterSpec(final byte[] array, final byte[] array2, final int n, final int n2, final byte[] array3) {
        this(array, array2, n, n2, array3, false);
    }
    
    public IESParameterSpec(byte[] encoding, final byte[] array, final int macKeySize, final int cipherKeySize, final byte[] array2, final boolean usePointCompression) {
        if (encoding != null) {
            System.arraycopy(encoding, 0, this.derivation = new byte[encoding.length], 0, encoding.length);
        }
        else {
            this.derivation = null;
        }
        if (array != null) {
            encoding = new byte[array.length];
            System.arraycopy(array, 0, this.encoding = encoding, 0, array.length);
        }
        else {
            this.encoding = null;
        }
        this.macKeySize = macKeySize;
        this.cipherKeySize = cipherKeySize;
        this.nonce = Arrays.clone(array2);
        this.usePointCompression = usePointCompression;
    }
    
    public int getCipherKeySize() {
        return this.cipherKeySize;
    }
    
    public byte[] getDerivationV() {
        return Arrays.clone(this.derivation);
    }
    
    public byte[] getEncodingV() {
        return Arrays.clone(this.encoding);
    }
    
    public int getMacKeySize() {
        return this.macKeySize;
    }
    
    public byte[] getNonce() {
        return Arrays.clone(this.nonce);
    }
    
    public boolean getPointCompression() {
        return this.usePointCompression;
    }
    
    public void setPointCompression(final boolean usePointCompression) {
        this.usePointCompression = usePointCompression;
    }
}
