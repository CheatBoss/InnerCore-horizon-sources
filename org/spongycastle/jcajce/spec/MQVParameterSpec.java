package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import java.security.*;
import org.spongycastle.util.*;

public class MQVParameterSpec implements AlgorithmParameterSpec
{
    private final PrivateKey ephemeralPrivateKey;
    private final PublicKey ephemeralPublicKey;
    private final PublicKey otherPartyEphemeralKey;
    private final byte[] userKeyingMaterial;
    
    public MQVParameterSpec(final KeyPair keyPair, final PublicKey publicKey) {
        this(keyPair.getPublic(), keyPair.getPrivate(), publicKey, null);
    }
    
    public MQVParameterSpec(final KeyPair keyPair, final PublicKey publicKey, final byte[] array) {
        this(keyPair.getPublic(), keyPair.getPrivate(), publicKey, array);
    }
    
    public MQVParameterSpec(final PrivateKey privateKey, final PublicKey publicKey) {
        this(null, privateKey, publicKey, null);
    }
    
    public MQVParameterSpec(final PrivateKey privateKey, final PublicKey publicKey, final byte[] array) {
        this(null, privateKey, publicKey, array);
    }
    
    public MQVParameterSpec(final PublicKey publicKey, final PrivateKey privateKey, final PublicKey publicKey2) {
        this(publicKey, privateKey, publicKey2, null);
    }
    
    public MQVParameterSpec(final PublicKey ephemeralPublicKey, final PrivateKey ephemeralPrivateKey, final PublicKey otherPartyEphemeralKey, final byte[] array) {
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.ephemeralPrivateKey = ephemeralPrivateKey;
        this.otherPartyEphemeralKey = otherPartyEphemeralKey;
        this.userKeyingMaterial = Arrays.clone(array);
    }
    
    public PrivateKey getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    public PublicKey getOtherPartyEphemeralKey() {
        return this.otherPartyEphemeralKey;
    }
    
    public byte[] getUserKeyingMaterial() {
        return Arrays.clone(this.userKeyingMaterial);
    }
}
