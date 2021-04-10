package org.spongycastle.crypto.tls;

import org.spongycastle.util.io.*;
import java.io.*;
import org.spongycastle.util.*;

public class HeartbeatMessage
{
    protected int paddingLength;
    protected byte[] payload;
    protected short type;
    
    public HeartbeatMessage(final short type, final byte[] payload, final int paddingLength) {
        if (!HeartbeatMessageType.isValid(type)) {
            throw new IllegalArgumentException("'type' is not a valid HeartbeatMessageType value");
        }
        if (payload == null || payload.length >= 65536) {
            throw new IllegalArgumentException("'payload' must have length < 2^16");
        }
        if (paddingLength >= 16) {
            this.type = type;
            this.payload = payload;
            this.paddingLength = paddingLength;
            return;
        }
        throw new IllegalArgumentException("'paddingLength' must be at least 16");
    }
    
    public static HeartbeatMessage parse(final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (!HeartbeatMessageType.isValid(uint8)) {
            throw new TlsFatalAlert((short)47);
        }
        final int uint9 = TlsUtils.readUint16(inputStream);
        final PayloadBuffer payloadBuffer = new PayloadBuffer();
        Streams.pipeAll(inputStream, payloadBuffer);
        final byte[] truncatedByteArray = payloadBuffer.toTruncatedByteArray(uint9);
        if (truncatedByteArray == null) {
            return null;
        }
        return new HeartbeatMessage(uint8, truncatedByteArray, payloadBuffer.size() - truncatedByteArray.length);
    }
    
    public void encode(final TlsContext tlsContext, final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.type, outputStream);
        TlsUtils.checkUint16(this.payload.length);
        TlsUtils.writeUint16(this.payload.length, outputStream);
        outputStream.write(this.payload);
        final byte[] array = new byte[this.paddingLength];
        tlsContext.getNonceRandomGenerator().nextBytes(array);
        outputStream.write(array);
    }
    
    static class PayloadBuffer extends ByteArrayOutputStream
    {
        byte[] toTruncatedByteArray(final int n) {
            if (this.count < n + 16) {
                return null;
            }
            return Arrays.copyOf(this.buf, n);
        }
    }
}
