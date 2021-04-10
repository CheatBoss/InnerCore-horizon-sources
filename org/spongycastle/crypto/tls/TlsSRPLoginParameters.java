package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.math.*;

public class TlsSRPLoginParameters
{
    protected SRP6GroupParameters group;
    protected byte[] salt;
    protected BigInteger verifier;
    
    public TlsSRPLoginParameters(final SRP6GroupParameters group, final BigInteger verifier, final byte[] salt) {
        this.group = group;
        this.verifier = verifier;
        this.salt = salt;
    }
    
    public SRP6GroupParameters getGroup() {
        return this.group;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }
    
    public BigInteger getVerifier() {
        return this.verifier;
    }
}
