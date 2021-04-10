package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.modes.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RFC3211WrapEngine implements Wrapper
{
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private ParametersWithIV param;
    private SecureRandom rand;
    
    public RFC3211WrapEngine(final BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.engine.getUnderlyingCipher().getAlgorithmName());
        sb.append("/RFC3211Wrap");
        return sb.toString();
    }
    
    @Override
    public void init(final boolean forWrapping, final CipherParameters cipherParameters) {
        this.forWrapping = forWrapping;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            this.param = (ParametersWithIV)parametersWithRandom.getParameters();
            return;
        }
        if (forWrapping) {
            this.rand = new SecureRandom();
        }
        this.param = (ParametersWithIV)cipherParameters;
    }
    
    @Override
    public byte[] unwrap(byte[] array, int i, int n) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        final int blockSize = this.engine.getBlockSize();
        if (n < blockSize * 2) {
            throw new InvalidCipherTextException("input too short");
        }
        final byte[] array2 = new byte[n];
        final byte[] array3 = new byte[blockSize];
        final int n2 = 0;
        System.arraycopy(array, i, array2, 0, n);
        System.arraycopy(array, i, array3, 0, blockSize);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), array3));
        for (i = blockSize; i < n; i += blockSize) {
            this.engine.processBlock(array2, i, array2, i);
        }
        System.arraycopy(array2, n - blockSize, array3, 0, blockSize);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), array3));
        this.engine.processBlock(array2, 0, array2, 0);
        this.engine.init(false, this.param);
        for (i = 0; i < n; i += blockSize) {
            this.engine.processBlock(array2, i, array2, i);
        }
        if ((array2[0] & 0xFF) > n - 4) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        array = new byte[array2[0] & 0xFF];
        System.arraycopy(array2, 4, array, 0, array2[0]);
        n = 0;
        int n3;
        for (i = n2; i != 3; i = n3) {
            n3 = i + 1;
            n |= ((byte)~array2[n3] ^ array[i]);
        }
        if (n == 0) {
            return array;
        }
        throw new InvalidCipherTextException("wrapped key fails checksum");
    }
    
    @Override
    public byte[] wrap(byte[] array, int n, int i) {
        if (this.forWrapping) {
            this.engine.init(true, this.param);
            final int blockSize = this.engine.getBlockSize();
            final int n2 = i + 4;
            int n3 = blockSize * 2;
            if (n2 >= n3) {
                if (n2 % blockSize == 0) {
                    n3 = n2;
                }
                else {
                    n3 = (n2 / blockSize + 1) * blockSize;
                }
            }
            final byte[] array2 = new byte[n3];
            final byte b = (byte)i;
            final int n4 = 0;
            array2[0] = b;
            array2[1] = (byte)~array[n];
            array2[2] = (byte)~array[n + 1];
            array2[3] = (byte)~array[n + 2];
            System.arraycopy(array, n, array2, 4, i);
            n = array2.length - n2;
            array = new byte[n];
            this.rand.nextBytes(array);
            System.arraycopy(array, 0, array2, n2, n);
            n = 0;
            while (true) {
                i = n4;
                if (n >= array2.length) {
                    break;
                }
                this.engine.processBlock(array2, n, array2, n);
                n += blockSize;
            }
            while (i < array2.length) {
                this.engine.processBlock(array2, i, array2, i);
                i += blockSize;
            }
            return array2;
        }
        throw new IllegalStateException("not set for wrapping");
    }
}
