package org.spongycastle.crypto.tls;

import java.io.*;

public class TlsNullCompression implements TlsCompression
{
    @Override
    public OutputStream compress(final OutputStream outputStream) {
        return outputStream;
    }
    
    @Override
    public OutputStream decompress(final OutputStream outputStream) {
        return outputStream;
    }
}
