package org.spongycastle.crypto.tls;

public interface TlsSRPIdentityManager
{
    TlsSRPLoginParameters getLoginParameters(final byte[] p0);
}
