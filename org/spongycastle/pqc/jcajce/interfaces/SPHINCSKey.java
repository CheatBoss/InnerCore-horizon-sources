package org.spongycastle.pqc.jcajce.interfaces;

import java.security.*;

public interface SPHINCSKey extends Key
{
    byte[] getKeyData();
}
