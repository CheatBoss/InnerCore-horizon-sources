package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;

public interface TlsSRPGroupVerifier
{
    boolean accept(final SRP6GroupParameters p0);
}
