package org.spongycastle.jce.interfaces;

import java.security.*;

public interface MQVPublicKey extends PublicKey
{
    PublicKey getEphemeralKey();
    
    PublicKey getStaticKey();
}
