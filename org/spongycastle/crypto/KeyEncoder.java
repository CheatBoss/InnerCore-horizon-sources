package org.spongycastle.crypto;

import org.spongycastle.crypto.params.*;

public interface KeyEncoder
{
    byte[] getEncoded(final AsymmetricKeyParameter p0);
}
