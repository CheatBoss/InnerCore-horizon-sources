package org.spongycastle.crypto.tls;

import org.spongycastle.util.io.*;
import java.io.*;

class RecordStream
{
    private static int DEFAULT_PLAINTEXT_LIMIT = 16384;
    static final int TLS_HEADER_LENGTH_OFFSET = 3;
    static final int TLS_HEADER_SIZE = 5;
    static final int TLS_HEADER_TYPE_OFFSET = 0;
    static final int TLS_HEADER_VERSION_OFFSET = 1;
    private ByteArrayOutputStream buffer;
    private int ciphertextLimit;
    private int compressedLimit;
    private TlsProtocol handler;
    private TlsHandshakeHash handshakeHash;
    private SimpleOutputStream handshakeHashUpdater;
    private InputStream input;
    private OutputStream output;
    private TlsCipher pendingCipher;
    private TlsCompression pendingCompression;
    private int plaintextLimit;
    private TlsCipher readCipher;
    private TlsCompression readCompression;
    private SequenceNumber readSeqNo;
    private ProtocolVersion readVersion;
    private boolean restrictReadVersion;
    private TlsCipher writeCipher;
    private TlsCompression writeCompression;
    private SequenceNumber writeSeqNo;
    private ProtocolVersion writeVersion;
    
    RecordStream(final TlsProtocol handler, final InputStream input, final OutputStream output) {
        this.pendingCompression = null;
        this.readCompression = null;
        this.writeCompression = null;
        this.pendingCipher = null;
        this.readCipher = null;
        this.writeCipher = null;
        this.readSeqNo = new SequenceNumber();
        this.writeSeqNo = new SequenceNumber();
        this.buffer = new ByteArrayOutputStream();
        this.handshakeHash = null;
        this.handshakeHashUpdater = new SimpleOutputStream() {
            @Override
            public void write(final byte[] array, final int n, final int n2) throws IOException {
                RecordStream.this.handshakeHash.update(array, n, n2);
            }
        };
        this.readVersion = null;
        this.writeVersion = null;
        this.restrictReadVersion = true;
        this.handler = handler;
        this.input = input;
        this.output = output;
        final TlsNullCompression tlsNullCompression = new TlsNullCompression();
        this.readCompression = tlsNullCompression;
        this.writeCompression = tlsNullCompression;
    }
    
    private static void checkLength(final int n, final int n2, final short n3) throws IOException {
        if (n <= n2) {
            return;
        }
        throw new TlsFatalAlert(n3);
    }
    
    private static void checkType(final short n, final short n2) throws IOException {
        switch (n) {
            default: {
                throw new TlsFatalAlert(n2);
            }
            case 20:
            case 21:
            case 22:
            case 23: {}
        }
    }
    
    private byte[] getBufferContents() {
        final byte[] byteArray = this.buffer.toByteArray();
        this.buffer.reset();
        return byteArray;
    }
    
    void checkRecordHeader(final byte[] array) throws IOException {
        checkType(TlsUtils.readUint8(array, 0), (short)10);
        if (!this.restrictReadVersion) {
            if ((TlsUtils.readVersionRaw(array, 1) & 0xFFFFFF00) != 0x300) {
                throw new TlsFatalAlert((short)47);
            }
        }
        else {
            final ProtocolVersion version = TlsUtils.readVersion(array, 1);
            final ProtocolVersion readVersion = this.readVersion;
            if (readVersion != null) {
                if (!version.equals(readVersion)) {
                    throw new TlsFatalAlert((short)47);
                }
            }
        }
        checkLength(TlsUtils.readUint16(array, 3), this.ciphertextLimit, (short)22);
    }
    
    byte[] decodeAndVerify(final short n, final InputStream inputStream, final int n2) throws IOException {
        final byte[] fully = TlsUtils.readFully(n2, inputStream);
        final byte[] decodeCiphertext = this.readCipher.decodeCiphertext(this.readSeqNo.nextValue((short)10), n, fully, 0, fully.length);
        checkLength(decodeCiphertext.length, this.compressedLimit, (short)22);
        final OutputStream decompress = this.readCompression.decompress(this.buffer);
        byte[] bufferContents = decodeCiphertext;
        if (decompress != this.buffer) {
            decompress.write(decodeCiphertext, 0, decodeCiphertext.length);
            decompress.flush();
            bufferContents = this.getBufferContents();
        }
        checkLength(bufferContents.length, this.plaintextLimit, (short)30);
        if (bufferContents.length >= 1) {
            return bufferContents;
        }
        if (n == 23) {
            return bufferContents;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    void finaliseHandshake() throws IOException {
        final TlsCompression readCompression = this.readCompression;
        final TlsCompression pendingCompression = this.pendingCompression;
        if (readCompression == pendingCompression && this.writeCompression == pendingCompression) {
            final TlsCipher readCipher = this.readCipher;
            final TlsCipher pendingCipher = this.pendingCipher;
            if (readCipher == pendingCipher && this.writeCipher == pendingCipher) {
                this.pendingCompression = null;
                this.pendingCipher = null;
                return;
            }
        }
        throw new TlsFatalAlert((short)40);
    }
    
    void flush() throws IOException {
        this.output.flush();
    }
    
    TlsHandshakeHash getHandshakeHash() {
        return this.handshakeHash;
    }
    
    OutputStream getHandshakeHashUpdater() {
        return this.handshakeHashUpdater;
    }
    
    int getPlaintextLimit() {
        return this.plaintextLimit;
    }
    
    ProtocolVersion getReadVersion() {
        return this.readVersion;
    }
    
    void init(final TlsContext tlsContext) {
        final TlsNullCipher tlsNullCipher = new TlsNullCipher(tlsContext);
        this.readCipher = tlsNullCipher;
        this.writeCipher = tlsNullCipher;
        (this.handshakeHash = new DeferredHash()).init(tlsContext);
        this.setPlaintextLimit(RecordStream.DEFAULT_PLAINTEXT_LIMIT);
    }
    
    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }
    
    TlsHandshakeHash prepareToFinish() {
        final TlsHandshakeHash handshakeHash = this.handshakeHash;
        this.handshakeHash = handshakeHash.stopTracking();
        return handshakeHash;
    }
    
    boolean readRecord() throws IOException {
        final byte[] allOrNothing = TlsUtils.readAllOrNothing(5, this.input);
        if (allOrNothing == null) {
            return false;
        }
        final short uint8 = TlsUtils.readUint8(allOrNothing, 0);
        checkType(uint8, (short)10);
        if (!this.restrictReadVersion) {
            if ((TlsUtils.readVersionRaw(allOrNothing, 1) & 0xFFFFFF00) != 0x300) {
                throw new TlsFatalAlert((short)47);
            }
        }
        else {
            final ProtocolVersion version = TlsUtils.readVersion(allOrNothing, 1);
            final ProtocolVersion readVersion = this.readVersion;
            if (readVersion == null) {
                this.readVersion = version;
            }
            else if (!version.equals(readVersion)) {
                throw new TlsFatalAlert((short)47);
            }
        }
        final int uint9 = TlsUtils.readUint16(allOrNothing, 3);
        checkLength(uint9, this.ciphertextLimit, (short)22);
        final byte[] decodeAndVerify = this.decodeAndVerify(uint8, this.input, uint9);
        this.handler.processRecord(uint8, decodeAndVerify, 0, decodeAndVerify.length);
        return true;
    }
    
    void receivedReadCipherSpec() throws IOException {
        final TlsCompression pendingCompression = this.pendingCompression;
        if (pendingCompression != null) {
            final TlsCipher pendingCipher = this.pendingCipher;
            if (pendingCipher != null) {
                this.readCompression = pendingCompression;
                this.readCipher = pendingCipher;
                this.readSeqNo = new SequenceNumber();
                return;
            }
        }
        throw new TlsFatalAlert((short)40);
    }
    
    void safeClose() {
        try {
            this.input.close();
        }
        catch (IOException ex) {}
        try {
            this.output.close();
        }
        catch (IOException ex2) {}
    }
    
    void sentWriteCipherSpec() throws IOException {
        final TlsCompression pendingCompression = this.pendingCompression;
        if (pendingCompression != null) {
            final TlsCipher pendingCipher = this.pendingCipher;
            if (pendingCipher != null) {
                this.writeCompression = pendingCompression;
                this.writeCipher = pendingCipher;
                this.writeSeqNo = new SequenceNumber();
                return;
            }
        }
        throw new TlsFatalAlert((short)40);
    }
    
    void setPendingConnectionState(final TlsCompression pendingCompression, final TlsCipher pendingCipher) {
        this.pendingCompression = pendingCompression;
        this.pendingCipher = pendingCipher;
    }
    
    void setPlaintextLimit(int n) {
        this.plaintextLimit = n;
        n += 1024;
        this.compressedLimit = n;
        this.ciphertextLimit = n + 1024;
    }
    
    void setReadVersion(final ProtocolVersion readVersion) {
        this.readVersion = readVersion;
    }
    
    void setRestrictReadVersion(final boolean restrictReadVersion) {
        this.restrictReadVersion = restrictReadVersion;
    }
    
    void setWriteVersion(final ProtocolVersion writeVersion) {
        this.writeVersion = writeVersion;
    }
    
    void writeRecord(final short n, byte[] encodePlaintext, int n2, int length) throws IOException {
        if (this.writeVersion == null) {
            return;
        }
        checkType(n, (short)80);
        checkLength(length, this.plaintextLimit, (short)80);
        if (length < 1 && n != 23) {
            throw new TlsFatalAlert((short)80);
        }
        final OutputStream compress = this.writeCompression.compress(this.buffer);
        final long nextValue = this.writeSeqNo.nextValue((short)80);
        byte[] bufferContents;
        TlsCipher writeCipher2;
        if (compress == this.buffer) {
            final TlsCipher writeCipher = this.writeCipher;
            bufferContents = encodePlaintext;
            writeCipher2 = writeCipher;
        }
        else {
            compress.write(encodePlaintext, n2, length);
            compress.flush();
            bufferContents = this.getBufferContents();
            checkLength(bufferContents.length, length + 1024, (short)80);
            writeCipher2 = this.writeCipher;
            n2 = 0;
            length = bufferContents.length;
        }
        encodePlaintext = writeCipher2.encodePlaintext(nextValue, n, bufferContents, n2, length);
        checkLength(encodePlaintext.length, this.ciphertextLimit, (short)80);
        final byte[] array = new byte[encodePlaintext.length + 5];
        TlsUtils.writeUint8(n, array, 0);
        TlsUtils.writeVersion(this.writeVersion, array, 1);
        TlsUtils.writeUint16(encodePlaintext.length, array, 3);
        System.arraycopy(encodePlaintext, 0, array, 5, encodePlaintext.length);
        this.output.write(array);
        this.output.flush();
    }
    
    private static class SequenceNumber
    {
        private boolean exhausted;
        private long value;
        
        private SequenceNumber() {
            this.value = 0L;
            this.exhausted = false;
        }
        
        long nextValue(final short n) throws TlsFatalAlert {
            synchronized (this) {
                if (!this.exhausted) {
                    final long value = this.value;
                    final long value2 = this.value + 1L;
                    this.value = value2;
                    if (value2 == 0L) {
                        this.exhausted = true;
                    }
                    return value;
                }
                throw new TlsFatalAlert(n);
            }
        }
    }
}
