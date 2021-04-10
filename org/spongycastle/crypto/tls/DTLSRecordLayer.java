package org.spongycastle.crypto.tls;

import java.io.*;

class DTLSRecordLayer implements DatagramTransport
{
    private static final int MAX_FRAGMENT_LENGTH = 16384;
    private static final int RECORD_HEADER_LENGTH = 13;
    private static final long RETRANSMIT_TIMEOUT = 240000L;
    private static final long TCP_MSL = 120000L;
    private volatile boolean closed;
    private final TlsContext context;
    private DTLSEpoch currentEpoch;
    private volatile boolean failed;
    private volatile boolean inHandshake;
    private final TlsPeer peer;
    private DTLSEpoch pendingEpoch;
    private volatile int plaintextLimit;
    private DTLSEpoch readEpoch;
    private volatile ProtocolVersion readVersion;
    private final ByteQueue recordQueue;
    private DTLSHandshakeRetransmit retransmit;
    private DTLSEpoch retransmitEpoch;
    private long retransmitExpiry;
    private final DatagramTransport transport;
    private DTLSEpoch writeEpoch;
    private volatile ProtocolVersion writeVersion;
    
    DTLSRecordLayer(final DatagramTransport transport, final TlsContext context, final TlsPeer peer, final short n) {
        this.recordQueue = new ByteQueue();
        this.closed = false;
        this.failed = false;
        this.readVersion = null;
        this.writeVersion = null;
        this.retransmit = null;
        this.retransmitEpoch = null;
        this.retransmitExpiry = 0L;
        this.transport = transport;
        this.context = context;
        this.peer = peer;
        this.inHandshake = true;
        final DTLSEpoch writeEpoch = new DTLSEpoch(0, new TlsNullCipher(context));
        this.currentEpoch = writeEpoch;
        this.pendingEpoch = null;
        this.readEpoch = writeEpoch;
        this.writeEpoch = writeEpoch;
        this.setPlaintextLimit(16384);
    }
    
    private void closeTransport() {
        if (!this.closed) {
            try {
                if (!this.failed) {
                    this.warn((short)0, null);
                }
                this.transport.close();
            }
            catch (Exception ex) {}
            this.closed = true;
        }
    }
    
    private static long getMacSequenceNumber(final int n, final long n2) {
        return n2 | ((long)n & 0xFFFFFFFFL) << 48;
    }
    
    private void raiseAlert(final short n, final short n2, final String s, final Throwable t) throws IOException {
        this.peer.notifyAlertRaised(n, n2, s, t);
        this.sendRecord((short)21, new byte[] { (byte)n, (byte)n2 }, 0, 2);
    }
    
    private int receiveRecord(final byte[] array, final int n, int n2, int receive) throws IOException {
        if (this.recordQueue.available() > 0) {
            if (this.recordQueue.available() >= 13) {
                final byte[] array2 = new byte[2];
                this.recordQueue.read(array2, 0, 2, 11);
                n2 = TlsUtils.readUint16(array2, 0);
            }
            else {
                n2 = 0;
            }
            n2 = Math.min(this.recordQueue.available(), n2 + 13);
            this.recordQueue.removeData(array, n, n2, 0);
            return n2;
        }
        receive = this.transport.receive(array, n, n2, receive);
        if ((n2 = receive) >= 13) {
            final int n3 = TlsUtils.readUint16(array, n + 11) + 13;
            if ((n2 = receive) > n3) {
                this.recordQueue.addData(array, n + n3, receive - n3);
                n2 = n3;
            }
        }
        return n2;
    }
    
    private void sendRecord(final short n, byte[] encodePlaintext, int n2, final int n3) throws IOException {
        if (this.writeVersion == null) {
            return;
        }
        if (n3 > this.plaintextLimit) {
            throw new TlsFatalAlert((short)80);
        }
        if (n3 < 1 && n != 23) {
            throw new TlsFatalAlert((short)80);
        }
        final int epoch = this.writeEpoch.getEpoch();
        final long allocateSequenceNumber = this.writeEpoch.allocateSequenceNumber();
        encodePlaintext = this.writeEpoch.getCipher().encodePlaintext(getMacSequenceNumber(epoch, allocateSequenceNumber), n, encodePlaintext, n2, n3);
        n2 = encodePlaintext.length + 13;
        final byte[] array = new byte[n2];
        TlsUtils.writeUint8(n, array, 0);
        TlsUtils.writeVersion(this.writeVersion, array, 1);
        TlsUtils.writeUint16(epoch, array, 3);
        TlsUtils.writeUint48(allocateSequenceNumber, array, 5);
        TlsUtils.writeUint16(encodePlaintext.length, array, 11);
        System.arraycopy(encodePlaintext, 0, array, 13, encodePlaintext.length);
        this.transport.send(array, 0, n2);
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            if (this.inHandshake) {
                this.warn((short)90, "User canceled handshake");
            }
            this.closeTransport();
        }
    }
    
    void fail(final short n) {
        if (!this.closed) {
            try {
                this.raiseAlert((short)2, n, null, null);
            }
            catch (Exception ex) {}
            this.failed = true;
            this.closeTransport();
        }
    }
    
    void failed() {
        if (!this.closed) {
            this.failed = true;
            this.closeTransport();
        }
    }
    
    int getReadEpoch() {
        return this.readEpoch.getEpoch();
    }
    
    ProtocolVersion getReadVersion() {
        return this.readVersion;
    }
    
    @Override
    public int getReceiveLimit() throws IOException {
        return Math.min(this.plaintextLimit, this.readEpoch.getCipher().getPlaintextLimit(this.transport.getReceiveLimit() - 13));
    }
    
    @Override
    public int getSendLimit() throws IOException {
        return Math.min(this.plaintextLimit, this.writeEpoch.getCipher().getPlaintextLimit(this.transport.getSendLimit() - 13));
    }
    
    void handshakeSuccessful(final DTLSHandshakeRetransmit retransmit) {
        final DTLSEpoch readEpoch = this.readEpoch;
        final DTLSEpoch currentEpoch = this.currentEpoch;
        if (readEpoch != currentEpoch && this.writeEpoch != currentEpoch) {
            if (retransmit != null) {
                this.retransmit = retransmit;
                this.retransmitEpoch = currentEpoch;
                this.retransmitExpiry = System.currentTimeMillis() + 240000L;
            }
            this.inHandshake = false;
            this.currentEpoch = this.pendingEpoch;
            this.pendingEpoch = null;
            return;
        }
        throw new IllegalStateException();
    }
    
    void initPendingEpoch(final TlsCipher tlsCipher) {
        if (this.pendingEpoch == null) {
            this.pendingEpoch = new DTLSEpoch(this.writeEpoch.getEpoch() + 1, tlsCipher);
            return;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int receive(final byte[] array, int length, final int n, final int n2) throws IOException {
        DTLSEpoch dtlsEpoch = null;
        byte[] array2 = null;
    Label_0006:
        while (true) {
            final int n3 = Math.min(n, this.getReceiveLimit()) + 13;
            while (true) {
                Label_0036: {
                    if (array2 == null) {
                        break Label_0036;
                    }
                    final byte[] array3 = array2;
                    if (array2.length < n3) {
                        break Label_0036;
                    }
                Label_0471_Outer:
                    while (true) {
                    Label_0471:
                        while (true) {
                            int n6 = 0;
                        Label_0511_Outer:
                            while (true) {
                                int receiveRecord = 0;
                                short uint8 = 0;
                                int uint9;
                                long uint10;
                                DTLSEpoch dtlsEpoch2 = null;
                                ProtocolVersion version;
                                byte[] decodeCiphertext;
                                short n4;
                                short n5;
                                Label_0220_Outer:Label_0367_Outer:
                                while (true) {
                                Label_0357_Outer:
                                    while (true) {
                                    Label_0401_Outer:
                                        while (true) {
                                            while (true) {
                                            Label_0590:
                                                while (true) {
                                                    Label_0569:Label_0093_Outer:
                                                    while (true) {
                                                        Label_0575: {
                                                        Block_24_Outer:
                                                            while (true) {
                                                                Label_0562: {
                                                                    try {
                                                                        if (this.retransmit != null && System.currentTimeMillis() > this.retransmitExpiry) {
                                                                            this.retransmit = (DTLSHandshakeRetransmit)dtlsEpoch;
                                                                            this.retransmitEpoch = dtlsEpoch;
                                                                        }
                                                                        receiveRecord = this.receiveRecord(array3, 0, n3, n2);
                                                                        if (receiveRecord < 0) {
                                                                            return receiveRecord;
                                                                        }
                                                                        break Label_0562;
                                                                        array2 = array3;
                                                                        continue Label_0006;
                                                                        // iftrue(Label_0535:, this.inHandshake || this.retransmit == null)
                                                                        // iftrue(Label_0578:, uint8 != 22 || this.retransmitEpoch == null || uint9 != this.retransmitEpoch.getEpoch())
                                                                        // iftrue(Label_0244:, !dtlsEpoch2.getReplayWindow().shouldDiscard(uint10))
                                                                        // iftrue(Label_0263:, version.isDTLS())
                                                                        // iftrue(Label_0511:, this.inHandshake)
                                                                        // iftrue(Label_0111:, receiveRecord == TlsUtils.readUint16(array3, 11) + 13)
                                                                        // iftrue(Label_0511:, !this.inHandshake)
                                                                        // iftrue(Label_0457:, n4 == 2)
                                                                        // iftrue(Label_0493:, TlsUtils.readUint8(decodeCiphertext, n6) == 1)
                                                                        // iftrue(Label_0637:, this.pendingEpoch == null)
                                                                        // iftrue(Label_0646:, n5 != 0)
                                                                        // iftrue(Label_0341:, decodeCiphertext.length <= this.plaintextLimit)
                                                                        // iftrue(Label_0285:, this.readVersion == null || this.readVersion.equals(version))
                                                                        // iftrue(Label_0185:, uint9 != this.readEpoch.getEpoch())
                                                                        // iftrue(Label_0646:, decodeCiphertext.length != 2)
                                                                        // iftrue(Label_0646:, this.retransmit == null)
                                                                        // switch([Lcom.strobel.decompiler.ast.Label;@41a4cfda, uint8)
                                                                        // iftrue(Label_0590:, this.readVersion != null)
                                                                        // iftrue(Label_0646:, n6 >= decodeCiphertext.length)
                                                                        Block_20_Outer:Block_17_Outer:
                                                                        while (true) {
                                                                            while (true) {
                                                                                Block_26: {
                                                                                    while (true) {
                                                                                        Block_19: {
                                                                                        Block_8:
                                                                                            while (true) {
                                                                                                Block_28: {
                                                                                                Block_25:
                                                                                                    while (true) {
                                                                                                    Block_23:
                                                                                                        while (true) {
                                                                                                            while (true) {
                                                                                                                break Label_0220_Outer;
                                                                                                                break Block_28;
                                                                                                                Block_7: {
                                                                                                                    while (true) {
                                                                                                                        Block_11: {
                                                                                                                            break Block_11;
                                                                                                                            uint10 = TlsUtils.readUint48(array3, 5);
                                                                                                                            Block_12: {
                                                                                                                                break Block_12;
                                                                                                                                Label_0244: {
                                                                                                                                    version = TlsUtils.readVersion(array3, 1);
                                                                                                                                }
                                                                                                                                break Block_20_Outer;
                                                                                                                                break Block_19;
                                                                                                                            }
                                                                                                                            break Label_0569;
                                                                                                                            break Label_0569;
                                                                                                                            break Label_0220_Outer;
                                                                                                                            break Block_7;
                                                                                                                        }
                                                                                                                        dtlsEpoch2 = this.retransmitEpoch;
                                                                                                                        break Label_0575;
                                                                                                                        continue Label_0093_Outer;
                                                                                                                    }
                                                                                                                    n4 = decodeCiphertext[0];
                                                                                                                    n5 = decodeCiphertext[1];
                                                                                                                    this.peer.notifyAlertReceived(n4, n5);
                                                                                                                    Block_22: {
                                                                                                                        break Block_22;
                                                                                                                        break Block_25;
                                                                                                                        Label_0493: {
                                                                                                                            break Block_26;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    break Block_23;
                                                                                                                    System.arraycopy(decodeCiphertext, 0, array, length, decodeCiphertext.length);
                                                                                                                    length = decodeCiphertext.length;
                                                                                                                    return length;
                                                                                                                }
                                                                                                                break Label_0569;
                                                                                                                Label_0285: {
                                                                                                                    decodeCiphertext = dtlsEpoch2.getCipher().decodeCiphertext(getMacSequenceNumber(dtlsEpoch2.getEpoch(), uint10), uint8, array3, 13, receiveRecord - 13);
                                                                                                                }
                                                                                                                dtlsEpoch2.getReplayWindow().reportAuthenticated(uint10);
                                                                                                                continue Label_0511_Outer;
                                                                                                            }
                                                                                                            this.retransmit.receivedHandshakeRecord(uint9, decodeCiphertext, 0, decodeCiphertext.length);
                                                                                                            break Label_0220_Outer;
                                                                                                            Label_0263: {
                                                                                                                continue Label_0093_Outer;
                                                                                                            }
                                                                                                        }
                                                                                                        this.closeTransport();
                                                                                                        break Label_0220_Outer;
                                                                                                        Label_0156: {
                                                                                                            uint9 = TlsUtils.readUint16(array3, 3);
                                                                                                        }
                                                                                                        break Block_8;
                                                                                                        this.readVersion = version;
                                                                                                        break Label_0590;
                                                                                                        continue Block_24_Outer;
                                                                                                    }
                                                                                                    break Label_0511_Outer;
                                                                                                }
                                                                                                this.retransmit = null;
                                                                                                this.retransmitEpoch = null;
                                                                                                continue Block_20_Outer;
                                                                                            }
                                                                                            dtlsEpoch2 = this.readEpoch;
                                                                                            break Label_0575;
                                                                                        }
                                                                                        continue Block_17_Outer;
                                                                                    }
                                                                                }
                                                                                this.readEpoch = this.pendingEpoch;
                                                                                break Label_0511_Outer;
                                                                                Label_0111: {
                                                                                    uint8 = TlsUtils.readUint8(array3, 0);
                                                                                }
                                                                                Label_0341:
                                                                                continue Label_0401_Outer;
                                                                            }
                                                                            continue Block_17_Outer;
                                                                        }
                                                                        break Label_0569;
                                                                        Label_0457: {
                                                                            this.failed();
                                                                        }
                                                                        throw new TlsFatalAlert(n5);
                                                                    }
                                                                    catch (IOException ex) {
                                                                        throw ex;
                                                                    }
                                                                }
                                                                if (receiveRecord < 13) {
                                                                    break Label_0569;
                                                                }
                                                                continue Label_0357_Outer;
                                                            }
                                                            continue Label_0511_Outer;
                                                            Label_0572: {
                                                                continue Label_0569;
                                                            }
                                                        }
                                                        Label_0582: {
                                                            break Label_0582;
                                                            Label_0578: {
                                                                dtlsEpoch2 = dtlsEpoch;
                                                            }
                                                        }
                                                        if (dtlsEpoch2 == null) {
                                                            continue Label_0569;
                                                        }
                                                        break;
                                                    }
                                                    continue Label_0367_Outer;
                                                }
                                                switch (uint8) {
                                                    case 23: {
                                                        continue Label_0401_Outer;
                                                    }
                                                    case 22: {
                                                        continue Label_0357_Outer;
                                                    }
                                                    case 21: {
                                                        continue Label_0471_Outer;
                                                    }
                                                    default: {
                                                        continue Label_0220_Outer;
                                                    }
                                                    case 20: {
                                                        n6 = 0;
                                                        continue Label_0471;
                                                    }
                                                    case 24: {
                                                        dtlsEpoch = null;
                                                        continue Label_0511_Outer;
                                                    }
                                                }
                                                break;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            ++n6;
                            continue Label_0471;
                        }
                    }
                }
                final byte[] array3 = new byte[n3];
                continue;
            }
        }
    }
    
    void resetWriteEpoch() {
        final DTLSEpoch retransmitEpoch = this.retransmitEpoch;
        if (retransmitEpoch != null) {
            this.writeEpoch = retransmitEpoch;
            return;
        }
        this.writeEpoch = this.currentEpoch;
    }
    
    @Override
    public void send(final byte[] array, final int n, final int n2) throws IOException {
        short n3;
        if (!this.inHandshake && this.writeEpoch != this.retransmitEpoch) {
            n3 = 23;
        }
        else {
            n3 = 22;
            if (TlsUtils.readUint8(array, n) == 20) {
                DTLSEpoch writeEpoch = null;
                if (this.inHandshake) {
                    writeEpoch = this.pendingEpoch;
                }
                else if (this.writeEpoch == this.retransmitEpoch) {
                    writeEpoch = this.currentEpoch;
                }
                if (writeEpoch == null) {
                    throw new IllegalStateException();
                }
                this.sendRecord((short)20, new byte[] { 1 }, 0, 1);
                this.writeEpoch = writeEpoch;
                n3 = n3;
            }
        }
        this.sendRecord(n3, array, n, n2);
    }
    
    void setPlaintextLimit(final int plaintextLimit) {
        this.plaintextLimit = plaintextLimit;
    }
    
    void setReadVersion(final ProtocolVersion readVersion) {
        this.readVersion = readVersion;
    }
    
    void setWriteVersion(final ProtocolVersion writeVersion) {
        this.writeVersion = writeVersion;
    }
    
    void warn(final short n, final String s) throws IOException {
        this.raiseAlert((short)1, n, s, null);
    }
}
