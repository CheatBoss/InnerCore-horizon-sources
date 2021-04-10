package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class RFC5649WrapEngine implements Wrapper
{
    private BlockCipher engine;
    private byte[] extractedAIV;
    private boolean forWrapping;
    private byte[] highOrderIV;
    private KeyParameter param;
    private byte[] preIV;
    
    public RFC5649WrapEngine(final BlockCipher engine) {
        final byte[] array2;
        final byte[] array = array2 = new byte[4];
        array2[0] = -90;
        array2[2] = (array2[1] = 89);
        array2[3] = -90;
        this.highOrderIV = array;
        this.preIV = array;
        this.extractedAIV = null;
        this.engine = engine;
    }
    
    private byte[] padPlaintext(final byte[] array) {
        final int length = array.length;
        final int n = (8 - length % 8) % 8;
        final byte[] array2 = new byte[length + n];
        System.arraycopy(array, 0, array2, 0, length);
        if (n != 0) {
            System.arraycopy(new byte[n], 0, array2, length, n);
        }
        return array2;
    }
    
    private byte[] rfc3394UnwrapNoIvCheck(final byte[] array, int i, int j) {
        final int n = j - 8;
        final byte[] array2 = new byte[n];
        final byte[] extractedAIV = new byte[8];
        final byte[] array3 = new byte[16];
        System.arraycopy(array, i, extractedAIV, 0, 8);
        System.arraycopy(array, i + 8, array2, 0, n);
        this.engine.init(false, this.param);
        final int n2 = j / 8 - 1;
        int n3;
        int n4;
        int k;
        byte b;
        int n5;
        for (i = 5; i >= 0; --i) {
            for (j = n2; j >= 1; j = n3) {
                System.arraycopy(extractedAIV, 0, array3, 0, 8);
                n3 = j - 1;
                n4 = n3 * 8;
                System.arraycopy(array2, n4, array3, 8, 8);
                for (k = n2 * i + j, j = 1; k != 0; k >>>= 8, ++j) {
                    b = (byte)k;
                    n5 = 8 - j;
                    array3[n5] ^= b;
                }
                this.engine.processBlock(array3, 0, array3, 0);
                System.arraycopy(array3, 0, extractedAIV, 0, 8);
                System.arraycopy(array3, 8, array2, n4, 8);
            }
        }
        this.extractedAIV = extractedAIV;
        return array2;
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
            this.preIV = this.highOrderIV;
            return;
        }
        if (!(parameters instanceof ParametersWithIV)) {
            return;
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
        this.preIV = parametersWithIV.getIV();
        this.param = (KeyParameter)parametersWithIV.getParameters();
        if (this.preIV.length == 4) {
            return;
        }
        throw new IllegalArgumentException("IV length not equal to 4");
    }
    
    @Override
    public byte[] unwrap(byte[] rfc3394UnwrapNoIvCheck, int i, int n) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        final int n2 = n / 8;
        if (n2 * 8 != n) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        if (n2 == 1) {
            throw new InvalidCipherTextException("unwrap data must be at least 16 bytes");
        }
        final byte[] array = new byte[n];
        System.arraycopy(rfc3394UnwrapNoIvCheck, i, array, 0, n);
        final byte[] array2 = new byte[n];
        if (n2 == 2) {
            this.engine.init(false, this.param);
            for (i = 0; i < n; i += this.engine.getBlockSize()) {
                this.engine.processBlock(array, i, array2, i);
            }
            rfc3394UnwrapNoIvCheck = new byte[8];
            System.arraycopy(array2, 0, this.extractedAIV = rfc3394UnwrapNoIvCheck, 0, rfc3394UnwrapNoIvCheck.length);
            final byte[] extractedAIV = this.extractedAIV;
            i = n - extractedAIV.length;
            rfc3394UnwrapNoIvCheck = new byte[i];
            System.arraycopy(array2, extractedAIV.length, rfc3394UnwrapNoIvCheck, 0, i);
        }
        else {
            rfc3394UnwrapNoIvCheck = this.rfc3394UnwrapNoIvCheck(rfc3394UnwrapNoIvCheck, i, n);
        }
        final byte[] array3 = new byte[4];
        final byte[] array4 = new byte[4];
        System.arraycopy(this.extractedAIV, 0, array3, 0, 4);
        System.arraycopy(this.extractedAIV, 4, array4, 0, 4);
        final int bigEndianToInt = Pack.bigEndianToInt(array4, 0);
        boolean constantTimeAreEqual = Arrays.constantTimeAreEqual(array3, this.preIV);
        i = rfc3394UnwrapNoIvCheck.length;
        if (bigEndianToInt <= i - 8) {
            constantTimeAreEqual = false;
        }
        if (bigEndianToInt > i) {
            constantTimeAreEqual = false;
        }
        n = i - bigEndianToInt;
        if ((i = n) >= rfc3394UnwrapNoIvCheck.length) {
            i = rfc3394UnwrapNoIvCheck.length;
            constantTimeAreEqual = false;
        }
        final byte[] array5 = new byte[i];
        final byte[] array6 = new byte[i];
        System.arraycopy(rfc3394UnwrapNoIvCheck, rfc3394UnwrapNoIvCheck.length - i, array6, 0, i);
        if (!Arrays.constantTimeAreEqual(array6, array5)) {
            constantTimeAreEqual = false;
        }
        if (constantTimeAreEqual) {
            final byte[] array7 = new byte[bigEndianToInt];
            System.arraycopy(rfc3394UnwrapNoIvCheck, 0, array7, 0, bigEndianToInt);
            return array7;
        }
        throw new InvalidCipherTextException("checksum failed");
    }
    
    @Override
    public byte[] wrap(byte[] padPlaintext, int i, int n) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        final byte[] array = new byte[8];
        final byte[] intToBigEndian = Pack.intToBigEndian(n);
        final byte[] preIV = this.preIV;
        final int length = preIV.length;
        final int n2 = 0;
        System.arraycopy(preIV, 0, array, 0, length);
        System.arraycopy(intToBigEndian, 0, array, this.preIV.length, intToBigEndian.length);
        final byte[] array2 = new byte[n];
        System.arraycopy(padPlaintext, i, array2, 0, n);
        padPlaintext = this.padPlaintext(array2);
        if (padPlaintext.length == 8) {
            n = padPlaintext.length + 8;
            final byte[] array3 = new byte[n];
            System.arraycopy(array, 0, array3, 0, 8);
            System.arraycopy(padPlaintext, 0, array3, 8, padPlaintext.length);
            this.engine.init(true, this.param);
            for (i = n2; i < n; i += this.engine.getBlockSize()) {
                this.engine.processBlock(array3, i, array3, i);
            }
            return array3;
        }
        final RFC3394WrapEngine rfc3394WrapEngine = new RFC3394WrapEngine(this.engine);
        rfc3394WrapEngine.init(true, new ParametersWithIV(this.param, array));
        return rfc3394WrapEngine.wrap(padPlaintext, 0, padPlaintext.length);
    }
}
