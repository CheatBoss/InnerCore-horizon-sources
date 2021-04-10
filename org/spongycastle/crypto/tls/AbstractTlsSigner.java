package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public abstract class AbstractTlsSigner implements TlsSigner
{
    protected TlsContext context;
    
    @Override
    public Signer createSigner(final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.createSigner(null, asymmetricKeyParameter);
    }
    
    @Override
    public Signer createVerifyer(final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.createVerifyer(null, asymmetricKeyParameter);
    }
    
    @Override
    public byte[] generateRawSignature(final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array) throws CryptoException {
        return this.generateRawSignature(null, asymmetricKeyParameter, array);
    }
    
    @Override
    public void init(final TlsContext context) {
        this.context = context;
    }
    
    @Override
    public boolean verifyRawSignature(final byte[] array, final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array2) throws CryptoException {
        return this.verifyRawSignature(null, array, asymmetricKeyParameter, array2);
    }
}
