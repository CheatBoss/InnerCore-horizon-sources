package org.spongycastle.jcajce.provider.symmetric.util;

import org.spongycastle.crypto.*;

public interface BlockCipherProvider
{
    BlockCipher get();
}
