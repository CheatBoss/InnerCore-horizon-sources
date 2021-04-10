package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class RFC3394WrapEngine implements Wrapper
{
    private BlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private KeyParameter param;
    private boolean wrapCipherMode;
    
    public RFC3394WrapEngine(final BlockCipher blockCipher) {
        this(blockCipher, false);
    }
    
    public RFC3394WrapEngine(final BlockCipher engine, final boolean b) {
        this.iv = new byte[] { -90, -90, -90, -90, -90, -90, -90, -90 };
        this.engine = engine;
        this.wrapCipherMode = (b ^ true);
    }
    
    @Override
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }
    
    @Override
    public void init(final boolean forWrapping, final CipherParameters cipherParameters) {
        this.forWrapping = forWrapping;
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        if (parameters instanceof KeyParameter) {
            this.param = (KeyParameter)parameters;
            return;
        }
        if (!(parameters instanceof ParametersWithIV)) {
            return;
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
        this.iv = parametersWithIV.getIV();
        this.param = (KeyParameter)parametersWithIV.getParameters();
        if (this.iv.length == 8) {
            return;
        }
        throw new IllegalArgumentException("IV not equal to 8");
    }
    
    @Override
    public byte[] unwrap(final byte[] array, int i, int j) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        final int n = j / 8;
        if (n * 8 != j) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        final byte[] iv = this.iv;
        final byte[] array2 = new byte[j - iv.length];
        final byte[] array3 = new byte[iv.length];
        final byte[] array4 = new byte[iv.length + 8];
        System.arraycopy(array, i, array3, 0, iv.length);
        final byte[] iv2 = this.iv;
        System.arraycopy(array, i + iv2.length, array2, 0, j - iv2.length);
        this.engine.init(this.wrapCipherMode ^ true, this.param);
        final int n2 = n - 1;
        int n3;
        int n4;
        int k;
        byte b;
        int n5;
        for (i = 5; i >= 0; --i) {
            for (j = n2; j >= 1; j = n3) {
                System.arraycopy(array3, 0, array4, 0, this.iv.length);
                n3 = j - 1;
                n4 = n3 * 8;
                System.arraycopy(array2, n4, array4, this.iv.length, 8);
                for (k = n2 * i + j, j = 1; k != 0; k >>>= 8, ++j) {
                    b = (byte)k;
                    n5 = this.iv.length - j;
                    array4[n5] ^= b;
                }
                this.engine.processBlock(array4, 0, array4, 0);
                System.arraycopy(array4, 0, array3, 0, 8);
                System.arraycopy(array4, 8, array2, n4, 8);
            }
        }
        if (Arrays.constantTimeAreEqual(array3, this.iv)) {
            return array2;
        }
        throw new InvalidCipherTextException("checksum failed");
    }
    
    @Override
    public byte[] wrap(final byte[] array, int i, int j) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        final int n = j / 8;
        if (n * 8 == j) {
            final byte[] iv = this.iv;
            final byte[] array2 = new byte[iv.length + j];
            final byte[] array3 = new byte[iv.length + 8];
            System.arraycopy(iv, 0, array2, 0, iv.length);
            System.arraycopy(array, i, array2, this.iv.length, j);
            this.engine.init(this.wrapCipherMode, this.param);
            int n2;
            int k;
            int n3;
            byte b;
            int n4;
            for (i = 0; i != 6; ++i) {
                for (j = 1; j <= n; ++j) {
                    System.arraycopy(array2, 0, array3, 0, this.iv.length);
                    n2 = j * 8;
                    System.arraycopy(array2, n2, array3, this.iv.length, 8);
                    this.engine.processBlock(array3, 0, array3, 0);
                    for (k = n * i + j, n3 = 1; k != 0; k >>>= 8, ++n3) {
                        b = (byte)k;
                        n4 = this.iv.length - n3;
                        array3[n4] ^= b;
                    }
                    System.arraycopy(array3, 0, array2, 0, 8);
                    System.arraycopy(array3, 8, array2, n2, 8);
                }
            }
            return array2;
        }
        throw new DataLengthException("wrap data must be a multiple of 8 bytes");
    }
}
