package org.spongycastle.crypto.prng.drbg;

import org.spongycastle.crypto.prng.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class CTRSP800DRBG implements SP80090DRBG
{
    private static final int AES_MAX_BITS_REQUEST = 262144;
    private static final long AES_RESEED_MAX = 140737488355328L;
    private static final byte[] K_BITS;
    private static final int TDEA_MAX_BITS_REQUEST = 4096;
    private static final long TDEA_RESEED_MAX = 2147483648L;
    private byte[] _Key;
    private byte[] _V;
    private BlockCipher _engine;
    private EntropySource _entropySource;
    private boolean _isTDEA;
    private int _keySizeInBits;
    private long _reseedCounter;
    private int _securityStrength;
    private int _seedLength;
    
    static {
        K_BITS = Hex.decode("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F");
    }
    
    public CTRSP800DRBG(final BlockCipher engine, final int keySizeInBits, final int securityStrength, final EntropySource entropySource, final byte[] array, final byte[] array2) {
        this._reseedCounter = 0L;
        this._isTDEA = false;
        this._entropySource = entropySource;
        this._engine = engine;
        this._keySizeInBits = keySizeInBits;
        this._securityStrength = securityStrength;
        this._seedLength = engine.getBlockSize() * 8 + keySizeInBits;
        this._isTDEA = this.isTDEA(engine);
        if (securityStrength > 256) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (this.getMaxSecurityStrength(engine, keySizeInBits) < securityStrength) {
            throw new IllegalArgumentException("Requested security strength is not supported by block cipher and key size");
        }
        if (entropySource.entropySize() >= securityStrength) {
            this.CTR_DRBG_Instantiate_algorithm(this.getEntropy(), array2, array);
            return;
        }
        throw new IllegalArgumentException("Not enough entropy for security strength required");
    }
    
    private void BCC(final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4) {
        final int blockSize = this._engine.getBlockSize();
        final byte[] array5 = new byte[blockSize];
        final int n = array4.length / blockSize;
        final byte[] array6 = new byte[blockSize];
        this._engine.init(true, new KeyParameter(this.expandKey(array2)));
        this._engine.processBlock(array3, 0, array5, 0);
        for (int i = 0; i < n; ++i) {
            this.XOR(array6, array5, array4, i * blockSize);
            this._engine.processBlock(array6, 0, array5, 0);
        }
        System.arraycopy(array5, 0, array, 0, array.length);
    }
    
    private byte[] Block_Cipher_df(byte[] array, int n) {
        final int blockSize = this._engine.getBlockSize();
        final int length = array.length;
        final int n2 = n / 8;
        final int n3 = length + 8;
        final byte[] array2 = new byte[(n3 + 1 + blockSize - 1) / blockSize * blockSize];
        this.copyIntToByteArray(array2, length, 0);
        this.copyIntToByteArray(array2, n2, 4);
        System.arraycopy(array, 0, array2, 8, length);
        array2[n3] = -128;
        final int n4 = this._keySizeInBits / 8;
        final int n5 = n4 + blockSize;
        final byte[] array3 = new byte[n5];
        final byte[] array4 = new byte[blockSize];
        final byte[] array5 = new byte[blockSize];
        array = new byte[n4];
        System.arraycopy(CTRSP800DRBG.K_BITS, 0, array, 0, n4);
        int n6 = 0;
        while (true) {
            final int n7 = n6 * blockSize;
            if (n7 * 8 >= this._keySizeInBits + blockSize * 8) {
                break;
            }
            this.copyIntToByteArray(array5, n6, 0);
            this.BCC(array4, array, array5, array2);
            int n8;
            if ((n8 = n5 - n7) > blockSize) {
                n8 = blockSize;
            }
            System.arraycopy(array4, 0, array3, n7, n8);
            ++n6;
        }
        final byte[] array6 = new byte[blockSize];
        System.arraycopy(array3, 0, array, 0, n4);
        System.arraycopy(array3, n4, array6, 0, blockSize);
        final int n9 = n / 2;
        final byte[] array7 = new byte[n9];
        this._engine.init(true, new KeyParameter(this.expandKey(array)));
        n = 0;
        while (true) {
            final int n10 = n * blockSize;
            if (n10 >= n9) {
                break;
            }
            this._engine.processBlock(array6, 0, array6, 0);
            int n11;
            if ((n11 = n9 - n10) > blockSize) {
                n11 = blockSize;
            }
            System.arraycopy(array6, 0, array7, n10, n11);
            ++n;
        }
        return array7;
    }
    
    private void CTR_DRBG_Instantiate_algorithm(byte[] block_Cipher_df, byte[] key, byte[] v) {
        block_Cipher_df = this.Block_Cipher_df(Arrays.concatenate(block_Cipher_df, key, v), this._seedLength);
        final int blockSize = this._engine.getBlockSize();
        key = new byte[(this._keySizeInBits + 7) / 8];
        this._Key = key;
        v = new byte[blockSize];
        this.CTR_DRBG_Update(block_Cipher_df, key, this._V = v);
        this._reseedCounter = 1L;
    }
    
    private void CTR_DRBG_Reseed_algorithm(final byte[] array) {
        this.CTR_DRBG_Update(this.Block_Cipher_df(Arrays.concatenate(this.getEntropy(), array), this._seedLength), this._Key, this._V);
        this._reseedCounter = 1L;
    }
    
    private void CTR_DRBG_Update(final byte[] array, final byte[] array2, final byte[] array3) {
        final int length = array.length;
        final byte[] array4 = new byte[length];
        final byte[] array5 = new byte[this._engine.getBlockSize()];
        final int blockSize = this._engine.getBlockSize();
        this._engine.init(true, new KeyParameter(this.expandKey(array2)));
        int n = 0;
        while (true) {
            final int n2 = n * blockSize;
            if (n2 >= array.length) {
                break;
            }
            this.addOneTo(array3);
            this._engine.processBlock(array3, 0, array5, 0);
            int n3;
            if ((n3 = length - n2) > blockSize) {
                n3 = blockSize;
            }
            System.arraycopy(array5, 0, array4, n2, n3);
            ++n;
        }
        this.XOR(array4, array, array4, 0);
        System.arraycopy(array4, 0, array2, 0, array2.length);
        System.arraycopy(array4, array2.length, array3, 0, array3.length);
    }
    
    private void XOR(final byte[] array, final byte[] array2, final byte[] array3, final int n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)(array2[i] ^ array3[i + n]);
        }
    }
    
    private void addOneTo(final byte[] array) {
        int i = 1;
        int n = 1;
        while (i <= array.length) {
            final int n2 = (array[array.length - i] & 0xFF) + n;
            if (n2 > 255) {
                n = 1;
            }
            else {
                n = 0;
            }
            array[array.length - i] = (byte)n2;
            ++i;
        }
    }
    
    private void copyIntToByteArray(final byte[] array, final int n, final int n2) {
        array[n2 + 0] = (byte)(n >> 24);
        array[n2 + 1] = (byte)(n >> 16);
        array[n2 + 2] = (byte)(n >> 8);
        array[n2 + 3] = (byte)n;
    }
    
    private byte[] getEntropy() {
        final byte[] entropy = this._entropySource.getEntropy();
        if (entropy.length >= (this._securityStrength + 7) / 8) {
            return entropy;
        }
        throw new IllegalStateException("Insufficient entropy provided by entropy source");
    }
    
    private int getMaxSecurityStrength(final BlockCipher blockCipher, final int n) {
        if (this.isTDEA(blockCipher) && n == 168) {
            return 112;
        }
        if (blockCipher.getAlgorithmName().equals("AES")) {
            return n;
        }
        return -1;
    }
    
    private boolean isTDEA(final BlockCipher blockCipher) {
        return blockCipher.getAlgorithmName().equals("DESede") || blockCipher.getAlgorithmName().equals("TDEA");
    }
    
    private void padKey(final byte[] array, int n, final byte[] array2, int i) {
        final int n2 = n + 0;
        array2[i + 0] = (byte)(array[n2] & 0xFE);
        final byte b = array[n2];
        final int n3 = n + 1;
        array2[i + 1] = (byte)(b << 7 | (array[n3] & 0xFC) >>> 1);
        final byte b2 = array[n3];
        final int n4 = n + 2;
        array2[i + 2] = (byte)(b2 << 6 | (array[n4] & 0xF8) >>> 2);
        final byte b3 = array[n4];
        final int n5 = n + 3;
        array2[i + 3] = (byte)(b3 << 5 | (array[n5] & 0xF0) >>> 3);
        final byte b4 = array[n5];
        final int n6 = n + 4;
        array2[i + 4] = (byte)(b4 << 4 | (array[n6] & 0xE0) >>> 4);
        final byte b5 = array[n6];
        final int n7 = n + 5;
        array2[i + 5] = (byte)(b5 << 3 | (array[n7] & 0xC0) >>> 5);
        final byte b6 = array[n7];
        final int n8 = n + 6;
        array2[i + 6] = (byte)(b6 << 2 | (array[n8] & 0x80) >>> 6);
        n = i + 7;
        array2[n] = (byte)(array[n8] << 1);
        while (i <= n) {
            final byte b7 = array2[i];
            array2[i] = (byte)((b7 & 0xFE) | ((b7 >> 7 ^ (b7 >> 1 ^ b7 >> 2 ^ b7 >> 3 ^ b7 >> 4 ^ b7 >> 5 ^ b7 >> 6) ^ 0x1) & 0x1));
            ++i;
        }
    }
    
    byte[] expandKey(final byte[] array) {
        if (this._isTDEA) {
            final byte[] array2 = new byte[24];
            this.padKey(array, 0, array2, 0);
            this.padKey(array, 7, array2, 8);
            this.padKey(array, 14, array2, 16);
            return array2;
        }
        return array;
    }
    
    @Override
    public int generate(final byte[] array, byte[] block_Cipher_df, final boolean b) {
        if (this._isTDEA) {
            if (this._reseedCounter > 2147483648L) {
                return -1;
            }
            if (Utils.isTooLarge(array, 512)) {
                throw new IllegalArgumentException("Number of bits per request limited to 4096");
            }
        }
        else {
            if (this._reseedCounter > 140737488355328L) {
                return -1;
            }
            if (Utils.isTooLarge(array, 32768)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        byte[] array2 = block_Cipher_df;
        if (b) {
            this.CTR_DRBG_Reseed_algorithm(block_Cipher_df);
            array2 = null;
        }
        if (array2 != null) {
            block_Cipher_df = this.Block_Cipher_df(array2, this._seedLength);
            this.CTR_DRBG_Update(block_Cipher_df, this._Key, this._V);
        }
        else {
            block_Cipher_df = new byte[this._seedLength];
        }
        final int length = this._V.length;
        final byte[] array3 = new byte[length];
        this._engine.init(true, new KeyParameter(this.expandKey(this._Key)));
        for (int i = 0; i <= array.length / length; ++i) {
            final int length2 = array.length;
            final int n = i * length;
            int n2;
            if (length2 - n > length) {
                n2 = length;
            }
            else {
                n2 = array.length - this._V.length * i;
            }
            if (n2 != 0) {
                this.addOneTo(this._V);
                this._engine.processBlock(this._V, 0, array3, 0);
                System.arraycopy(array3, 0, array, n, n2);
            }
        }
        this.CTR_DRBG_Update(block_Cipher_df, this._Key, this._V);
        ++this._reseedCounter;
        return array.length * 8;
    }
    
    @Override
    public int getBlockSize() {
        return this._V.length * 8;
    }
    
    @Override
    public void reseed(final byte[] array) {
        this.CTR_DRBG_Reseed_algorithm(array);
    }
}
