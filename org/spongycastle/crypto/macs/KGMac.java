package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class KGMac implements Mac
{
    private final KGCMBlockCipher cipher;
    private final int macSizeBits;
    
    public KGMac(final KGCMBlockCipher cipher) {
        this.cipher = cipher;
        this.macSizeBits = cipher.getUnderlyingCipher().getBlockSize() * 8;
    }
    
    public KGMac(final KGCMBlockCipher cipher, final int macSizeBits) {
        this.cipher = cipher;
        this.macSizeBits = macSizeBits;
    }
    
    @Override
    public int doFinal(final byte[] array, int doFinal) throws DataLengthException, IllegalStateException {
        try {
            doFinal = this.cipher.doFinal(array, doFinal);
            return doFinal;
        }
        catch (InvalidCipherTextException ex) {
            throw new IllegalStateException(ex.toString());
        }
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getUnderlyingCipher().getAlgorithmName());
        sb.append("-KGMAC");
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return this.macSizeBits / 8;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.cipher.init(true, new AEADParameters((KeyParameter)parametersWithIV.getParameters(), this.macSizeBits, parametersWithIV.getIV()));
            return;
        }
        throw new IllegalArgumentException("KGMAC requires ParametersWithIV");
    }
    
    @Override
    public void reset() {
        this.cipher.reset();
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        this.cipher.processAADByte(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalStateException {
        this.cipher.processAADBytes(array, n, n2);
    }
}
