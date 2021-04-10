package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsCompression
{
    OutputStream compress(final OutputStream p0);
    
    OutputStream decompress(final OutputStream p0);
}
