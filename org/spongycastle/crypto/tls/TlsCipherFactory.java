package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsCipherFactory
{
    TlsCipher createCipher(final TlsContext p0, final int p1, final int p2) throws IOException;
}
