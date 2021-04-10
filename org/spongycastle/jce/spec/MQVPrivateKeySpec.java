package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;

public class MQVPrivateKeySpec implements KeySpec, MQVPrivateKey
{
    private PrivateKey ephemeralPrivateKey;
    private PublicKey ephemeralPublicKey;
    private PrivateKey staticPrivateKey;
    
    public MQVPrivateKeySpec(final PrivateKey privateKey, final PrivateKey privateKey2) {
        this(privateKey, privateKey2, null);
    }
    
    public MQVPrivateKeySpec(final PrivateKey staticPrivateKey, final PrivateKey ephemeralPrivateKey, final PublicKey ephemeralPublicKey) {
        this.staticPrivateKey = staticPrivateKey;
        this.ephemeralPrivateKey = ephemeralPrivateKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
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
    public PrivateKey getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    @Override
    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    @Override
    public String getFormat() {
        return null;
    }
    
    @Override
    public PrivateKey getStaticPrivateKey() {
        return this.staticPrivateKey;
    }
}
