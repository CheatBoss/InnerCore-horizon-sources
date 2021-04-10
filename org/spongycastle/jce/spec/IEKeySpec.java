package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;

public class IEKeySpec implements KeySpec, IESKey
{
    private PrivateKey privKey;
    private PublicKey pubKey;
    
    public IEKeySpec(final PrivateKey privKey, final PublicKey pubKey) {
        this.privKey = privKey;
        this.pubKey = pubKey;
    }
    
    @Override
    public String getAlgorithm() {
        return "IES";
    }
    
    @Override
    public byte[] getEncoded() {
        return null;
    }
    
    @Override
    public String getFormat() {
        return null;
    }
    
    @Override
    public PrivateKey getPrivate() {
        return this.privKey;
    }
    
    @Override
    public PublicKey getPublic() {
        return this.pubKey;
    }
}
