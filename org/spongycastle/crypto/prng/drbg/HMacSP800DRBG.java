package org.spongycastle.crypto.prng.drbg;

import org.spongycastle.crypto.prng.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class HMacSP800DRBG implements SP80090DRBG
{
    private static final int MAX_BITS_REQUEST = 262144;
    private static final long RESEED_MAX = 140737488355328L;
    private byte[] _K;
    private byte[] _V;
    private EntropySource _entropySource;
    private Mac _hMac;
    private long _reseedCounter;
    private int _securityStrength;
    
    public HMacSP800DRBG(final Mac hMac, final int securityStrength, final EntropySource entropySource, final byte[] array, final byte[] array2) {
        if (securityStrength > Utils.getMaxSecurityStrength(hMac)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (entropySource.entropySize() >= securityStrength) {
            this._securityStrength = securityStrength;
            this._entropySource = entropySource;
            this._hMac = hMac;
            final byte[] concatenate = Arrays.concatenate(this.getEntropy(), array2, array);
            final byte[] k = new byte[hMac.getMacSize()];
            this._K = k;
            Arrays.fill(this._V = new byte[k.length], (byte)1);
            this.hmac_DRBG_Update(concatenate);
            this._reseedCounter = 1L;
            return;
        }
        throw new IllegalArgumentException("Not enough entropy for security strength required");
    }
    
    private byte[] getEntropy() {
        final byte[] entropy = this._entropySource.getEntropy();
        if (entropy.length >= (this._securityStrength + 7) / 8) {
            return entropy;
        }
        throw new IllegalStateException("Insufficient entropy provided by entropy source");
    }
    
    private void hmac_DRBG_Update(final byte[] array) {
        this.hmac_DRBG_Update_Func(array, (byte)0);
        if (array != null) {
            this.hmac_DRBG_Update_Func(array, (byte)1);
        }
    }
    
    private void hmac_DRBG_Update_Func(final byte[] array, final byte b) {
        this._hMac.init(new KeyParameter(this._K));
        final Mac hMac = this._hMac;
        final byte[] v = this._V;
        hMac.update(v, 0, v.length);
        this._hMac.update(b);
        if (array != null) {
            this._hMac.update(array, 0, array.length);
        }
        this._hMac.doFinal(this._K, 0);
        this._hMac.init(new KeyParameter(this._K));
        final Mac hMac2 = this._hMac;
        final byte[] v2 = this._V;
        hMac2.update(v2, 0, v2.length);
        this._hMac.doFinal(this._V, 0);
    }
    
    @Override
    public int generate(final byte[] array, byte[] array2, final boolean b) {
        final int n = array.length * 8;
        if (n > 262144) {
            throw new IllegalArgumentException("Number of bits per request limited to 262144");
        }
        if (this._reseedCounter > 140737488355328L) {
            return -1;
        }
        byte[] array3 = array2;
        if (b) {
            this.reseed(array2);
            array3 = null;
        }
        if (array3 != null) {
            this.hmac_DRBG_Update(array3);
        }
        final int length = array.length;
        array2 = new byte[length];
        final int n2 = array.length / this._V.length;
        this._hMac.init(new KeyParameter(this._K));
        for (int i = 0; i < n2; ++i) {
            final Mac hMac = this._hMac;
            final byte[] v = this._V;
            hMac.update(v, 0, v.length);
            this._hMac.doFinal(this._V, 0);
            final byte[] v2 = this._V;
            System.arraycopy(v2, 0, array2, v2.length * i, v2.length);
        }
        final byte[] v3 = this._V;
        if (v3.length * n2 < length) {
            this._hMac.update(v3, 0, v3.length);
            this._hMac.doFinal(this._V, 0);
            final byte[] v4 = this._V;
            System.arraycopy(v4, 0, array2, v4.length * n2, length - n2 * v4.length);
        }
        this.hmac_DRBG_Update(array3);
        ++this._reseedCounter;
        System.arraycopy(array2, 0, array, 0, array.length);
        return n;
    }
    
    @Override
    public int getBlockSize() {
        return this._V.length * 8;
    }
    
    @Override
    public void reseed(final byte[] array) {
        this.hmac_DRBG_Update(Arrays.concatenate(this.getEntropy(), array));
        this._reseedCounter = 1L;
    }
}
