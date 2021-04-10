package org.spongycastle.crypto.tls;

import java.io.*;

public class AbstractTlsCipherFactory implements TlsCipherFactory
{
    @Override
    public TlsCipher createCipher(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        throw new TlsFatalAlert((short)80);
    }
}
