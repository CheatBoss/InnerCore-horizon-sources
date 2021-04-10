package org.spongycastle.jce.interfaces;

import java.security.*;

public interface IESKey extends Key
{
    PrivateKey getPrivate();
    
    PublicKey getPublic();
}
