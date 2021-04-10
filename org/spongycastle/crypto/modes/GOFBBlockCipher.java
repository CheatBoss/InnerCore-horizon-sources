package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class GOFBBlockCipher extends StreamBlockCipher
{
    static final int C1 = 16843012;
    static final int C2 = 16843009;
    private byte[] IV;
    int N3;
    int N4;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    boolean firstStep;
    private byte[] ofbOutV;
    private byte[] ofbV;
    
    public GOFBBlockCipher(final BlockCipher cipher) {
        super(cipher);
        this.firstStep = true;
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        if (blockSize == 8) {
            this.IV = new byte[cipher.getBlockSize()];
            this.ofbV = new byte[cipher.getBlockSize()];
            this.ofbOutV = new byte[cipher.getBlockSize()];
            return;
        }
        throw new IllegalArgumentException("GCTR only for 64 bit block ciphers");
    }
    
    private int bytesToint(final byte[] array, final int n) {
        return (array[n + 3] << 24 & 0xFF000000) + (array[n + 2] << 16 & 0xFF0000) + (array[n + 1] << 8 & 0xFF00) + (array[n] & 0xFF);
    }
    
    private void intTobytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >>> 24);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2] = (byte)n;
    }
    
    @Override
    protected byte calculateByte(final byte b) {
        if (this.byteCount == 0) {
            if (this.firstStep) {
                this.firstStep = false;
                this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
                this.N3 = this.bytesToint(this.ofbOutV, 0);
                this.N4 = this.bytesToint(this.ofbOutV, 4);
            }
            this.N3 += 16843009;
            final int n4 = this.N4 + 16843012;
            this.N4 = n4;
            if (n4 < 16843012 && n4 > 0) {
                this.N4 = n4 + 1;
            }
            this.intTobytes(this.N3, this.ofbV, 0);
            this.intTobytes(this.N4, this.ofbV, 4);
            this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
        }
        final byte[] ofbOutV = this.ofbOutV;
        final int byteCount = this.byteCount;
        final int byteCount2 = byteCount + 1;
        this.byteCount = byteCount2;
        final byte b2 = (byte)(b ^ ofbOutV[byteCount]);
        final int blockSize = this.blockSize;
        if (byteCount2 == blockSize) {
            this.byteCount = 0;
            final byte[] ofbV = this.ofbV;
            System.arraycopy(ofbV, blockSize, ofbV, 0, ofbV.length - blockSize);
            final byte[] ofbOutV2 = this.ofbOutV;
            final byte[] ofbV2 = this.ofbV;
            final int length = ofbV2.length;
            final int blockSize2 = this.blockSize;
            System.arraycopy(ofbOutV2, 0, ofbV2, length - blockSize2, blockSize2);
        }
        return b2;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/GCTR");
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.blockSize;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.firstStep = true;
        this.N3 = 0;
        this.N4 = 0;
        if (cipherParameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            final byte[] iv = parametersWithIV.getIV();
            final int length = iv.length;
            final byte[] iv2 = this.IV;
            if (length < iv2.length) {
                System.arraycopy(iv, 0, iv2, iv2.length - iv.length, iv.length);
                int n = 0;
                while (true) {
                    final byte[] iv3 = this.IV;
                    if (n >= iv3.length - iv.length) {
                        break;
                    }
                    iv3[n] = 0;
                    ++n;
                }
            }
            else {
                System.arraycopy(iv, 0, iv2, 0, iv2.length);
            }
            this.reset();
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(true, parametersWithIV.getParameters());
            }
        }
        else {
            this.reset();
            if (cipherParameters != null) {
                this.cipher.init(true, cipherParameters);
            }
        }
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.processBytes(array, n, this.blockSize, array2, n2);
        return this.blockSize;
    }
    
    @Override
    public void reset() {
        this.firstStep = true;
        this.N3 = 0;
        this.N4 = 0;
        final byte[] iv = this.IV;
        System.arraycopy(iv, 0, this.ofbV, 0, iv.length);
        this.byteCount = 0;
        this.cipher.reset();
    }
}
