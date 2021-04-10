package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import java.io.*;

public class URLAndHash
{
    protected byte[] sha1Hash;
    protected String url;
    
    public URLAndHash(final String url, final byte[] sha1Hash) {
        if (url == null || url.length() < 1 || url.length() >= 65536) {
            throw new IllegalArgumentException("'url' must have length from 1 to (2^16 - 1)");
        }
        if (sha1Hash != null && sha1Hash.length != 20) {
            throw new IllegalArgumentException("'sha1Hash' must have length == 20, if present");
        }
        this.url = url;
        this.sha1Hash = sha1Hash;
    }
    
    public static URLAndHash parse(final TlsContext tlsContext, final InputStream inputStream) throws IOException {
        final byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
        if (opaque16.length >= 1) {
            final String fromByteArray = Strings.fromByteArray(opaque16);
            final byte[] array = null;
            final short uint8 = TlsUtils.readUint8(inputStream);
            byte[] fully;
            if (uint8 != 0) {
                if (uint8 != 1) {
                    throw new TlsFatalAlert((short)47);
                }
                fully = TlsUtils.readFully(20, inputStream);
            }
            else {
                if (TlsUtils.isTLSv12(tlsContext)) {
                    throw new TlsFatalAlert((short)47);
                }
                fully = array;
            }
            return new URLAndHash(fromByteArray, fully);
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque16(Strings.toByteArray(this.url), outputStream);
        if (this.sha1Hash == null) {
            TlsUtils.writeUint8(0, outputStream);
            return;
        }
        TlsUtils.writeUint8(1, outputStream);
        outputStream.write(this.sha1Hash);
    }
    
    public byte[] getSHA1Hash() {
        return this.sha1Hash;
    }
    
    public String getURL() {
        return this.url;
    }
}
