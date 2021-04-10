package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public interface TlsSigner
{
    Signer createSigner(final AsymmetricKeyParameter p0);
    
    Signer createSigner(final SignatureAndHashAlgorithm p0, final AsymmetricKeyParameter p1);
    
    Signer createVerifyer(final AsymmetricKeyParameter p0);
    
    Signer createVerifyer(final SignatureAndHashAlgorithm p0, final AsymmetricKeyParameter p1);
    
    byte[] generateRawSignature(final AsymmetricKeyParameter p0, final byte[] p1) throws CryptoException;
    
    byte[] generateRawSignature(final SignatureAndHashAlgorithm p0, final AsymmetricKeyParameter p1, final byte[] p2) throws CryptoException;
    
    void init(final TlsContext p0);
    
    boolean isValidPublicKey(final AsymmetricKeyParameter p0);
    
    boolean verifyRawSignature(final SignatureAndHashAlgorithm p0, final byte[] p1, final AsymmetricKeyParameter p2, final byte[] p3) throws CryptoException;
    
    boolean verifyRawSignature(final byte[] p0, final AsymmetricKeyParameter p1, final byte[] p2) throws CryptoException;
}
