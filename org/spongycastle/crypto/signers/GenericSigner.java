package org.spongycastle.crypto.signers;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class GenericSigner implements Signer
{
    private final Digest digest;
    private final AsymmetricBlockCipher engine;
    private boolean forSigning;
    
    public GenericSigner(final AsymmetricBlockCipher engine, final Digest digest) {
        this.engine = engine;
        this.digest = digest;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (this.forSigning) {
            final int digestSize = this.digest.getDigestSize();
            final byte[] array = new byte[digestSize];
            this.digest.doFinal(array, 0);
            return this.engine.processBlock(array, 0, digestSize);
        }
        throw new IllegalStateException("GenericSigner not initialised for signature generation.");
    }
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            asymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        if (forSigning && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        }
        if (!forSigning && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        }
        this.reset();
        this.engine.init(forSigning, cipherParameters);
    }
    
    @Override
    public void reset() {
        this.digest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(byte[] processBlock) {
        if (!this.forSigning) {
            final int digestSize = this.digest.getDigestSize();
            final byte[] array = new byte[digestSize];
            this.digest.doFinal(array, 0);
            try {
                final byte[] array2 = processBlock = this.engine.processBlock(processBlock, 0, processBlock.length);
                if (array2.length < digestSize) {
                    processBlock = new byte[digestSize];
                    System.arraycopy(array2, 0, processBlock, digestSize - array2.length, array2.length);
                }
                return Arrays.constantTimeAreEqual(processBlock, array);
            }
            catch (Exception ex) {
                return false;
            }
        }
        throw new IllegalStateException("GenericSigner not initialised for verification");
    }
}
