package org.spongycastle.crypto.tls;

public abstract class ServerOnlyTlsAuthentication implements TlsAuthentication
{
    @Override
    public final TlsCredentials getClientCredentials(final CertificateRequest certificateRequest) {
        return null;
    }
}
