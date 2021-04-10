package org.spongycastle.pqc.jcajce.provider.util;

import java.io.*;
import java.security.spec.*;
import javax.crypto.*;
import java.security.*;

public abstract class AsymmetricBlockCipher extends CipherSpiExt
{
    protected ByteArrayOutputStream buf;
    protected int cipherTextSize;
    protected int maxPlainTextSize;
    protected AlgorithmParameterSpec paramSpec;
    
    public AsymmetricBlockCipher() {
        this.buf = new ByteArrayOutputStream();
    }
    
    protected void checkLength(int n) throws IllegalBlockSizeException {
        n += this.buf.size();
        if (this.opMode == 1) {
            if (n <= this.maxPlainTextSize) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The length of the plaintext (");
            sb.append(n);
            sb.append(" bytes) is not supported by the cipher (max. ");
            sb.append(this.maxPlainTextSize);
            sb.append(" bytes).");
            throw new IllegalBlockSizeException(sb.toString());
        }
        else {
            if (this.opMode != 2) {
                return;
            }
            if (n == this.cipherTextSize) {
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Illegal ciphertext length (expected ");
            sb2.append(this.cipherTextSize);
            sb2.append(" bytes, was ");
            sb2.append(n);
            sb2.append(" bytes).");
            throw new IllegalBlockSizeException(sb2.toString());
        }
    }
    
    @Override
    public final int doFinal(byte[] doFinal, final int n, final int n2, final byte[] array, final int n3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        if (array.length >= this.getOutputSize(n2)) {
            doFinal = this.doFinal(doFinal, n, n2);
            System.arraycopy(doFinal, 0, array, n3, doFinal.length);
            return doFinal.length;
        }
        throw new ShortBufferException("Output buffer too short.");
    }
    
    @Override
    public final byte[] doFinal(byte[] byteArray, int opMode, final int n) throws IllegalBlockSizeException, BadPaddingException {
        this.checkLength(n);
        this.update(byteArray, opMode, n);
        byteArray = this.buf.toByteArray();
        this.buf.reset();
        opMode = this.opMode;
        if (opMode == 1) {
            return this.messageEncrypt(byteArray);
        }
        if (opMode != 2) {
            return null;
        }
        return this.messageDecrypt(byteArray);
    }
    
    @Override
    public final int getBlockSize() {
        if (this.opMode == 1) {
            return this.maxPlainTextSize;
        }
        return this.cipherTextSize;
    }
    
    @Override
    public final byte[] getIV() {
        return null;
    }
    
    @Override
    public final int getOutputSize(final int n) {
        final int size = this.buf.size();
        final int blockSize = this.getBlockSize();
        if (n + size > blockSize) {
            return 0;
        }
        return blockSize;
    }
    
    @Override
    public final AlgorithmParameterSpec getParameters() {
        return this.paramSpec;
    }
    
    protected abstract void initCipherDecrypt(final Key p0, final AlgorithmParameterSpec p1) throws InvalidKeyException, InvalidAlgorithmParameterException;
    
    protected abstract void initCipherEncrypt(final Key p0, final AlgorithmParameterSpec p1, final SecureRandom p2) throws InvalidKeyException, InvalidAlgorithmParameterException;
    
    public final void initDecrypt(final Key key) throws InvalidKeyException {
        try {
            this.initDecrypt(key, null);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }
    
    @Override
    public final void initDecrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.opMode = 2;
        this.initCipherDecrypt(key, algorithmParameterSpec);
    }
    
    public final void initEncrypt(final Key key) throws InvalidKeyException {
        try {
            this.initEncrypt(key, null, new SecureRandom());
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }
    
    public final void initEncrypt(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.initEncrypt(key, null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }
    
    public final void initEncrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initEncrypt(key, algorithmParameterSpec, new SecureRandom());
    }
    
    @Override
    public final void initEncrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.opMode = 1;
        this.initCipherEncrypt(key, algorithmParameterSpec, secureRandom);
    }
    
    protected abstract byte[] messageDecrypt(final byte[] p0) throws IllegalBlockSizeException, BadPaddingException;
    
    protected abstract byte[] messageEncrypt(final byte[] p0) throws IllegalBlockSizeException, BadPaddingException;
    
    @Override
    protected final void setMode(final String s) {
    }
    
    @Override
    protected final void setPadding(final String s) {
    }
    
    @Override
    public final int update(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        this.update(array, n, n2);
        return 0;
    }
    
    @Override
    public final byte[] update(final byte[] array, final int n, final int n2) {
        if (n2 != 0) {
            this.buf.write(array, n, n2);
        }
        return new byte[0];
    }
}
