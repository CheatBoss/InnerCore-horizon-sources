package org.spongycastle.jce.interfaces;

import java.security.*;

public interface MQVPrivateKey extends PrivateKey
{
    PrivateKey getEphemeralPrivateKey();
    
    PublicKey getEphemeralPublicKey();
    
    PrivateKey getStaticPrivateKey();
}
