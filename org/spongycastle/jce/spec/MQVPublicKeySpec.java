package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;

public class MQVPublicKeySpec implements KeySpec, MQVPublicKey
{
    private PublicKey ephemeralKey;
    private PublicKey staticKey;
    
    public MQVPublicKeySpec(final PublicKey staticKey, final PublicKey ephemeralKey) {
        this.staticKey = staticKey;
        this.ephemeralKey = ephemeralKey;
    }
    
    @Override
    public String getAlgorithm() {
        return "ECMQV";
    }
    
    @Override
    public byte[] getEncoded() {
        return null;
    }
    
    @Override
    public PublicKey getEphemeralKey() {
        return this.ephemeralKey;
    }
    
    @Override
    public String getFormat() {
        return null;
    }
    
    @Override
    public PublicKey getStaticKey() {
        return this.staticKey;
    }
}
