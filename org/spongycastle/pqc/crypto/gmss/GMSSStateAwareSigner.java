package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.pqc.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class GMSSStateAwareSigner implements StateAwareMessageSigner
{
    private final GMSSSigner gmssSigner;
    private GMSSPrivateKeyParameters key;
    
    public GMSSStateAwareSigner(final Digest digest) {
        if (digest instanceof Memoable) {
            this.gmssSigner = new GMSSSigner(new GMSSDigestProvider() {
                final /* synthetic */ Memoable val$dig = ((Memoable)digest).copy();
                
                @Override
                public Digest get() {
                    return (Digest)this.val$dig.copy();
                }
            });
            return;
        }
        throw new IllegalArgumentException("digest must implement Memoable");
    }
    
    @Override
    public byte[] generateSignature(byte[] generateSignature) {
        if (this.key != null) {
            generateSignature = this.gmssSigner.generateSignature(generateSignature);
            this.key = this.key.nextKey();
            return generateSignature;
        }
        throw new IllegalStateException("signing key no longer usable");
    }
    
    @Override
    public AsymmetricKeyParameter getUpdatedPrivateKey() {
        final GMSSPrivateKeyParameters key = this.key;
        this.key = null;
        return key;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (b) {
            GMSSPrivateKeyParameters key;
            if (cipherParameters instanceof ParametersWithRandom) {
                key = (GMSSPrivateKeyParameters)((ParametersWithRandom)cipherParameters).getParameters();
            }
            else {
                key = (GMSSPrivateKeyParameters)cipherParameters;
            }
            this.key = key;
        }
        this.gmssSigner.init(b, cipherParameters);
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final byte[] array2) {
        return this.gmssSigner.verifySignature(array, array2);
    }
}
