package org.spongycastle.pqc.jcajce.provider.util;

import java.security.spec.*;
import javax.crypto.*;
import java.security.*;

public abstract class AsymmetricHybridCipher extends CipherSpiExt
{
    protected AlgorithmParameterSpec paramSpec;
    
    protected abstract int decryptOutputSize(final int p0);
    
    @Override
    public final int doFinal(byte[] doFinal, final int n, final int n2, final byte[] array, final int n3) throws ShortBufferException, BadPaddingException {
        if (array.length >= this.getOutputSize(n2)) {
            doFinal = this.doFinal(doFinal, n, n2);
            System.arraycopy(doFinal, 0, array, n3, doFinal.length);
            return doFinal.length;
        }
        throw new ShortBufferException("Output buffer too short.");
    }
    
    @Override
    public abstract byte[] doFinal(final byte[] p0, final int p1, final int p2) throws BadPaddingException;
    
    protected abstract int encryptOutputSize(final int p0);
    
    @Override
    public final int getBlockSize() {
        return 0;
    }
    
    @Override
    public final byte[] getIV() {
        return null;
    }
    
    @Override
    public final int getOutputSize(final int n) {
        if (this.opMode == 1) {
            return this.encryptOutputSize(n);
        }
        return this.decryptOutputSize(n);
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
    
    @Override
    protected final void setMode(final String s) {
    }
    
    @Override
    protected final void setPadding(final String s) {
    }
    
    @Override
    public final int update(byte[] update, final int n, final int n2, final byte[] array, final int n3) throws ShortBufferException {
        if (array.length >= this.getOutputSize(n2)) {
            update = this.update(update, n, n2);
            System.arraycopy(update, 0, array, n3, update.length);
            return update.length;
        }
        throw new ShortBufferException("output");
    }
    
    @Override
    public abstract byte[] update(final byte[] p0, final int p1, final int p2);
}
