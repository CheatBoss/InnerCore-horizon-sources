package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DigestingMessageSigner implements Signer
{
    private boolean forSigning;
    private final Digest messDigest;
    private final MessageSigner messSigner;
    
    public DigestingMessageSigner(final MessageSigner messSigner, final Digest messDigest) {
        this.messSigner = messSigner;
        this.messDigest = messDigest;
    }
    
    @Override
    public byte[] generateSignature() {
        if (this.forSigning) {
            final byte[] array = new byte[this.messDigest.getDigestSize()];
            this.messDigest.doFinal(array, 0);
            return this.messSigner.generateSignature(array);
        }
        throw new IllegalStateException("DigestingMessageSigner not initialised for signature generation.");
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
            throw new IllegalArgumentException("Signing Requires Private Key.");
        }
        if (!forSigning && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Verification Requires Public Key.");
        }
        this.reset();
        this.messSigner.init(forSigning, cipherParameters);
    }
    
    @Override
    public void reset() {
        this.messDigest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.messDigest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.messDigest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(final byte[] array) {
        if (!this.forSigning) {
            final byte[] array2 = new byte[this.messDigest.getDigestSize()];
            this.messDigest.doFinal(array2, 0);
            return this.messSigner.verifySignature(array2, array);
        }
        throw new IllegalStateException("DigestingMessageSigner not initialised for verification");
    }
}
