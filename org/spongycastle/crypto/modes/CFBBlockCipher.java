package org.spongycastle.crypto.modes;

import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class CFBBlockCipher extends StreamBlockCipher
{
    private byte[] IV;
    private int blockSize;
    private int byteCount;
    private byte[] cfbOutV;
    private byte[] cfbV;
    private BlockCipher cipher;
    private boolean encrypting;
    private byte[] inBuf;
    
    public CFBBlockCipher(final BlockCipher cipher, final int n) {
        super(cipher);
        this.cipher = null;
        this.cipher = cipher;
        this.blockSize = n / 8;
        this.IV = new byte[cipher.getBlockSize()];
        this.cfbV = new byte[cipher.getBlockSize()];
        this.cfbOutV = new byte[cipher.getBlockSize()];
        this.inBuf = new byte[this.blockSize];
    }
    
    private byte decryptByte(final byte b) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
        }
        final byte[] inBuf = this.inBuf;
        final int byteCount = this.byteCount;
        inBuf[byteCount] = b;
        final byte[] cfbOutV = this.cfbOutV;
        final int byteCount2 = byteCount + 1;
        this.byteCount = byteCount2;
        final byte b2 = (byte)(b ^ cfbOutV[byteCount]);
        final int blockSize = this.blockSize;
        if (byteCount2 == blockSize) {
            this.byteCount = 0;
            final byte[] cfbV = this.cfbV;
            System.arraycopy(cfbV, blockSize, cfbV, 0, cfbV.length - blockSize);
            final byte[] inBuf2 = this.inBuf;
            final byte[] cfbV2 = this.cfbV;
            final int length = cfbV2.length;
            final int blockSize2 = this.blockSize;
            System.arraycopy(inBuf2, 0, cfbV2, length - blockSize2, blockSize2);
        }
        return b2;
    }
    
    private byte encryptByte(final byte b) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
        }
        final byte[] cfbOutV = this.cfbOutV;
        final int byteCount = this.byteCount;
        final byte b2 = (byte)(b ^ cfbOutV[byteCount]);
        final byte[] inBuf = this.inBuf;
        final int byteCount2 = byteCount + 1;
        this.byteCount = byteCount2;
        inBuf[byteCount] = b2;
        final int blockSize = this.blockSize;
        if (byteCount2 == blockSize) {
            this.byteCount = 0;
            final byte[] cfbV = this.cfbV;
            System.arraycopy(cfbV, blockSize, cfbV, 0, cfbV.length - blockSize);
            final byte[] inBuf2 = this.inBuf;
            final byte[] cfbV2 = this.cfbV;
            final int length = cfbV2.length;
            final int blockSize2 = this.blockSize;
            System.arraycopy(inBuf2, 0, cfbV2, length - blockSize2, blockSize2);
        }
        return b2;
    }
    
    @Override
    protected byte calculateByte(final byte b) throws DataLengthException, IllegalStateException {
        if (this.encrypting) {
            return this.encryptByte(b);
        }
        return this.decryptByte(b);
    }
    
    public int decryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.processBytes(array, n, this.blockSize, array2, n2);
        return this.blockSize;
    }
    
    public int encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.processBytes(array, n, this.blockSize, array2, n2);
        return this.blockSize;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/CFB");
        sb.append(this.blockSize * 8);
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.blockSize;
    }
    
    public byte[] getCurrentIV() {
        return Arrays.clone(this.cfbV);
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.encrypting = encrypting;
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
        System.arraycopy(iv, 0, this.cfbV, 0, iv.length);
        Arrays.fill(this.inBuf, (byte)0);
        this.byteCount = 0;
        this.cipher.reset();
    }
}
