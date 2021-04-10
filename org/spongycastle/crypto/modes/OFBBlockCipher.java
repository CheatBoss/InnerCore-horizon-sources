package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class OFBBlockCipher extends StreamBlockCipher
{
    private byte[] IV;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    private byte[] ofbOutV;
    private byte[] ofbV;
    
    public OFBBlockCipher(final BlockCipher cipher, final int n) {
        super(cipher);
        this.cipher = cipher;
        this.blockSize = n / 8;
        this.IV = new byte[cipher.getBlockSize()];
        this.ofbV = new byte[cipher.getBlockSize()];
        this.ofbOutV = new byte[cipher.getBlockSize()];
    }
    
    @Override
    protected byte calculateByte(final byte b) throws DataLengthException, IllegalStateException {
        if (this.byteCount == 0) {
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
        sb.append("/OFB");
        sb.append(this.blockSize * 8);
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.blockSize;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
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
        final byte[] iv = this.IV;
        System.arraycopy(iv, 0, this.ofbV, 0, iv.length);
        this.byteCount = 0;
        this.cipher.reset();
    }
}
