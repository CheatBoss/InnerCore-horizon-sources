package org.spongycastle.crypto.prng.drbg;

import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.util.*;

public class HashSP800DRBG implements SP80090DRBG
{
    private static final int MAX_BITS_REQUEST = 262144;
    private static final byte[] ONE;
    private static final long RESEED_MAX = 140737488355328L;
    private static final Hashtable seedlens;
    private byte[] _C;
    private byte[] _V;
    private Digest _digest;
    private EntropySource _entropySource;
    private long _reseedCounter;
    private int _securityStrength;
    private int _seedLength;
    
    static {
        ONE = new byte[] { 1 };
        (seedlens = new Hashtable()).put("SHA-1", Integers.valueOf(440));
        HashSP800DRBG.seedlens.put("SHA-224", Integers.valueOf(440));
        HashSP800DRBG.seedlens.put("SHA-256", Integers.valueOf(440));
        HashSP800DRBG.seedlens.put("SHA-512/256", Integers.valueOf(440));
        HashSP800DRBG.seedlens.put("SHA-512/224", Integers.valueOf(440));
        HashSP800DRBG.seedlens.put("SHA-384", Integers.valueOf(888));
        HashSP800DRBG.seedlens.put("SHA-512", Integers.valueOf(888));
    }
    
    public HashSP800DRBG(final Digest digest, final int securityStrength, final EntropySource entropySource, final byte[] array, final byte[] array2) {
        if (securityStrength > Utils.getMaxSecurityStrength(digest)) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (entropySource.entropySize() >= securityStrength) {
            this._digest = digest;
            this._entropySource = entropySource;
            this._securityStrength = securityStrength;
            this._seedLength = HashSP800DRBG.seedlens.get(digest.getAlgorithmName());
            final byte[] hash_df = Utils.hash_df(this._digest, Arrays.concatenate(this.getEntropy(), array2, array), this._seedLength);
            this._V = hash_df;
            final byte[] array3 = new byte[hash_df.length + 1];
            System.arraycopy(hash_df, 0, array3, 1, hash_df.length);
            this._C = Utils.hash_df(this._digest, array3, this._seedLength);
            this._reseedCounter = 1L;
            return;
        }
        throw new IllegalArgumentException("Not enough entropy for security strength required");
    }
    
    private void addTo(final byte[] array, final byte[] array2) {
        int i = 1;
        int n = 0;
        while (i <= array2.length) {
            final int n2 = (array[array.length - i] & 0xFF) + (array2[array2.length - i] & 0xFF) + n;
            if (n2 > 255) {
                n = 1;
            }
            else {
                n = 0;
            }
            array[array.length - i] = (byte)n2;
            ++i;
        }
        for (int j = array2.length + 1; j <= array.length; ++j) {
            final int n3 = (array[array.length - j] & 0xFF) + n;
            if (n3 > 255) {
                n = 1;
            }
            else {
                n = 0;
            }
            array[array.length - j] = (byte)n3;
        }
    }
    
    private void doHash(final byte[] array, final byte[] array2) {
        this._digest.update(array, 0, array.length);
        this._digest.doFinal(array2, 0);
    }
    
    private byte[] getEntropy() {
        final byte[] entropy = this._entropySource.getEntropy();
        if (entropy.length >= (this._securityStrength + 7) / 8) {
            return entropy;
        }
        throw new IllegalStateException("Insufficient entropy provided by entropy source");
    }
    
    private byte[] hash(final byte[] array) {
        final byte[] array2 = new byte[this._digest.getDigestSize()];
        this.doHash(array, array2);
        return array2;
    }
    
    private byte[] hashgen(byte[] array, int i) {
        final int digestSize = this._digest.getDigestSize();
        final int n = i / 8;
        final int n2 = n / digestSize;
        final byte[] array2 = new byte[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        array = new byte[n];
        final int digestSize2 = this._digest.getDigestSize();
        final byte[] array3 = new byte[digestSize2];
        int n3;
        int n4;
        for (i = 0; i <= n2; ++i) {
            this.doHash(array2, array3);
            n3 = i * digestSize2;
            if ((n4 = n - n3) > digestSize2) {
                n4 = digestSize2;
            }
            System.arraycopy(array3, 0, array, n3, n4);
            this.addTo(array2, HashSP800DRBG.ONE);
        }
        return array;
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
            array2 = this._V;
            final byte[] array4 = new byte[array2.length + 1 + array3.length];
            array4[0] = 2;
            System.arraycopy(array2, 0, array4, 1, array2.length);
            System.arraycopy(array3, 0, array4, this._V.length + 1, array3.length);
            array2 = this.hash(array4);
            this.addTo(this._V, array2);
        }
        array2 = this.hashgen(this._V, n);
        final byte[] v = this._V;
        final byte[] array5 = new byte[v.length + 1];
        System.arraycopy(v, 0, array5, 1, v.length);
        array5[0] = 3;
        this.addTo(this._V, this.hash(array5));
        this.addTo(this._V, this._C);
        final long reseedCounter = this._reseedCounter;
        this.addTo(this._V, new byte[] { (byte)(reseedCounter >> 24), (byte)(reseedCounter >> 16), (byte)(reseedCounter >> 8), (byte)reseedCounter });
        ++this._reseedCounter;
        System.arraycopy(array2, 0, array, 0, array.length);
        return n;
    }
    
    @Override
    public int getBlockSize() {
        return this._digest.getDigestSize() * 8;
    }
    
    @Override
    public void reseed(byte[] v) {
        v = Arrays.concatenate(HashSP800DRBG.ONE, this._V, this.getEntropy(), v);
        v = Utils.hash_df(this._digest, v, this._seedLength);
        this._V = v;
        final byte[] array = new byte[v.length + 1];
        array[0] = 0;
        System.arraycopy(v, 0, array, 1, v.length);
        this._C = Utils.hash_df(this._digest, array, this._seedLength);
        this._reseedCounter = 1L;
    }
}
