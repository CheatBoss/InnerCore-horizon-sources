package org.spongycastle.crypto.tls;

import java.io.*;

public class HeartbeatExtension
{
    protected short mode;
    
    public HeartbeatExtension(final short mode) {
        if (HeartbeatMode.isValid(mode)) {
            this.mode = mode;
            return;
        }
        throw new IllegalArgumentException("'mode' is not a valid HeartbeatMode value");
    }
    
    public static HeartbeatExtension parse(final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (HeartbeatMode.isValid(uint8)) {
            return new HeartbeatExtension(uint8);
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.mode, outputStream);
    }
    
    public short getMode() {
        return this.mode;
    }
}
