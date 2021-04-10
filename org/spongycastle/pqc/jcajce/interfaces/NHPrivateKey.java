package org.spongycastle.pqc.jcajce.interfaces;

import java.security.*;

public interface NHPrivateKey extends PrivateKey, NHKey
{
    short[] getSecretData();
}
