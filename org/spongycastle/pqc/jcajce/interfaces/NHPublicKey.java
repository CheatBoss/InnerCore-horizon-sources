package org.spongycastle.pqc.jcajce.interfaces;

import java.security.*;

public interface NHPublicKey extends PublicKey, NHKey
{
    byte[] getPublicData();
}
