package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.util.*;
import java.io.*;

class DTLSReliableHandshake
{
    private static final int MAX_RECEIVE_AHEAD = 16;
    private static final int MESSAGE_HEADER_LENGTH = 12;
    private Hashtable currentInboundFlight;
    private TlsHandshakeHash handshakeHash;
    private int message_seq;
    private int next_receive_seq;
    private Vector outboundFlight;
    private Hashtable previousInboundFlight;
    private DTLSRecordLayer recordLayer;
    private boolean sending;
    
    DTLSReliableHandshake(final TlsContext tlsContext, final DTLSRecordLayer recordLayer) {
        this.currentInboundFlight = new Hashtable();
        this.previousInboundFlight = null;
        this.outboundFlight = new Vector();
        this.sending = true;
        this.message_seq = 0;
        this.next_receive_seq = 0;
        this.recordLayer = recordLayer;
        (this.handshakeHash = new DeferredHash()).init(tlsContext);
    }
    
    private int backOff(final int n) {
        return Math.min(n * 2, 60000);
    }
    
    private static boolean checkAll(final Hashtable hashtable) {
        final Enumeration<DTLSReassembler> elements = hashtable.elements();
        while (elements.hasMoreElements()) {
            if (elements.nextElement().getBodyIfComplete() == null) {
                return false;
            }
        }
        return true;
    }
    
    private void checkInboundFlight() {
        final Enumeration<Integer> keys = this.currentInboundFlight.keys();
        while (keys.hasMoreElements()) {
            keys.nextElement();
        }
    }
    
    private Message getPendingMessage() throws IOException {
        final DTLSReassembler dtlsReassembler = this.currentInboundFlight.get(Integers.valueOf(this.next_receive_seq));
        if (dtlsReassembler != null) {
            final byte[] bodyIfComplete = dtlsReassembler.getBodyIfComplete();
            if (bodyIfComplete != null) {
                this.previousInboundFlight = null;
                return this.updateHandshakeMessagesDigest(new Message(this.next_receive_seq++, dtlsReassembler.getMsgType(), bodyIfComplete));
            }
        }
        return null;
    }
    
    private void prepareInboundFlight(final Hashtable currentInboundFlight) {
        resetAll(this.currentInboundFlight);
        this.previousInboundFlight = this.currentInboundFlight;
        this.currentInboundFlight = currentInboundFlight;
    }
    
    private boolean processRecord(final int n, final int n2, final byte[] array, int i, int n3) throws IOException {
        final boolean b = false;
        final int n4 = n3;
        int n5 = 0;
        n3 = i;
        int uint24;
        int n6;
        int uint25;
        int uint26;
        short uint27;
        int uint28;
        int next_receive_seq;
        int n7;
        DTLSReassembler dtlsReassembler;
        Hashtable previousInboundFlight;
        DTLSReassembler dtlsReassembler2;
        for (i = n4; i >= 12; i -= n6, n5 = n7) {
            uint24 = TlsUtils.readUint24(array, n3 + 9);
            n6 = uint24 + 12;
            if (i < n6) {
                break;
            }
            uint25 = TlsUtils.readUint24(array, n3 + 1);
            uint26 = TlsUtils.readUint24(array, n3 + 6);
            if (uint26 + uint24 > uint25) {
                break;
            }
            uint27 = TlsUtils.readUint8(array, n3 + 0);
            if (n2 != ((uint27 == 20) ? 1 : 0)) {
                break;
            }
            uint28 = TlsUtils.readUint16(array, n3 + 4);
            next_receive_seq = this.next_receive_seq;
            if (uint28 >= next_receive_seq + n) {
                n7 = n5;
            }
            else if (uint28 >= next_receive_seq) {
                if ((dtlsReassembler = this.currentInboundFlight.get(Integers.valueOf(uint28))) == null) {
                    dtlsReassembler = new DTLSReassembler(uint27, uint25);
                    this.currentInboundFlight.put(Integers.valueOf(uint28), dtlsReassembler);
                }
                dtlsReassembler.contributeFragment(uint27, uint25, array, n3 + 12, uint26, uint24);
                n7 = n5;
            }
            else {
                previousInboundFlight = this.previousInboundFlight;
                n7 = n5;
                if (previousInboundFlight != null) {
                    dtlsReassembler2 = previousInboundFlight.get(Integers.valueOf(uint28));
                    n7 = n5;
                    if (dtlsReassembler2 != null) {
                        dtlsReassembler2.contributeFragment(uint27, uint25, array, n3 + 12, uint26, uint24);
                        n7 = 1;
                    }
                }
            }
            n3 += n6;
        }
        boolean b2 = b;
        if (n5 != 0) {
            b2 = b;
            if (checkAll(this.previousInboundFlight)) {
                b2 = true;
            }
        }
        if (b2) {
            this.resendOutboundFlight();
            resetAll(this.previousInboundFlight);
        }
        return b2;
    }
    
    private void resendOutboundFlight() throws IOException {
        this.recordLayer.resetWriteEpoch();
        for (int i = 0; i < this.outboundFlight.size(); ++i) {
            this.writeMessage((Message)this.outboundFlight.elementAt(i));
        }
    }
    
    private static void resetAll(final Hashtable hashtable) {
        final Enumeration<DTLSReassembler> elements = hashtable.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().reset();
        }
    }
    
    private Message updateHandshakeMessagesDigest(final Message message) throws IOException {
        if (message.getType() != 0) {
            final byte[] body = message.getBody();
            final byte[] array = new byte[12];
            TlsUtils.writeUint8(message.getType(), array, 0);
            TlsUtils.writeUint24(body.length, array, 1);
            TlsUtils.writeUint16(message.getSeq(), array, 4);
            TlsUtils.writeUint24(0, array, 6);
            TlsUtils.writeUint24(body.length, array, 9);
            this.handshakeHash.update(array, 0, 12);
            this.handshakeHash.update(body, 0, body.length);
        }
        return message;
    }
    
    private void writeHandshakeFragment(final Message message, final int n, final int n2) throws IOException {
        final RecordLayerBuffer recordLayerBuffer = new RecordLayerBuffer(n2 + 12);
        TlsUtils.writeUint8(message.getType(), recordLayerBuffer);
        TlsUtils.writeUint24(message.getBody().length, recordLayerBuffer);
        TlsUtils.writeUint16(message.getSeq(), recordLayerBuffer);
        TlsUtils.writeUint24(n, recordLayerBuffer);
        TlsUtils.writeUint24(n2, recordLayerBuffer);
        recordLayerBuffer.write(message.getBody(), n, n2);
        recordLayerBuffer.sendToRecordLayer(this.recordLayer);
    }
    
    private void writeMessage(final Message message) throws IOException {
        final int n = this.recordLayer.getSendLimit() - 12;
        if (n >= 1) {
            final int length = message.getBody().length;
            int n2 = 0;
            int min;
            do {
                min = Math.min(length - n2, n);
                this.writeHandshakeFragment(message, n2, min);
            } while ((n2 += min) < length);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    void finish() {
        final boolean sending = this.sending;
        DTLSHandshakeRetransmit dtlsHandshakeRetransmit = null;
        if (!sending) {
            this.checkInboundFlight();
        }
        else {
            this.prepareInboundFlight(null);
            if (this.previousInboundFlight != null) {
                dtlsHandshakeRetransmit = new DTLSHandshakeRetransmit() {
                    @Override
                    public void receivedHandshakeRecord(final int n, final byte[] array, final int n2, final int n3) throws IOException {
                        DTLSReliableHandshake.this.processRecord(0, n, array, n2, n3);
                    }
                };
            }
        }
        this.recordLayer.handshakeSuccessful(dtlsHandshakeRetransmit);
    }
    
    TlsHandshakeHash getHandshakeHash() {
        return this.handshakeHash;
    }
    
    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }
    
    TlsHandshakeHash prepareToFinish() {
        final TlsHandshakeHash handshakeHash = this.handshakeHash;
        this.handshakeHash = handshakeHash.stopTracking();
        return handshakeHash;
    }
    
    Message receiveMessage() throws IOException {
        if (this.sending) {
            this.sending = false;
            this.prepareInboundFlight(new Hashtable());
        }
        byte[] array = null;
        int n = 1000;
        while (true) {
            byte[] array2 = array;
            try {
                final Message pendingMessage = this.getPendingMessage();
                if (pendingMessage != null) {
                    return pendingMessage;
                }
                array2 = array;
                final int receiveLimit = this.recordLayer.getReceiveLimit();
                byte[] array3 = null;
                Label_0081: {
                    if (array != null) {
                        array3 = array;
                        array2 = array;
                        if (array.length >= receiveLimit) {
                            break Label_0081;
                        }
                    }
                    array2 = array;
                    array3 = new byte[receiveLimit];
                }
                array2 = array3;
                final int receive = this.recordLayer.receive(array3, 0, receiveLimit, n);
                if (receive < 0) {
                    array = array3;
                }
                else {
                    array = array3;
                    array2 = array3;
                    if (this.processRecord(16, this.recordLayer.getReadEpoch(), array3, 0, receive)) {
                        array2 = array3;
                        n = this.backOff(n);
                        array = array3;
                        continue;
                    }
                    continue;
                }
            }
            catch (IOException ex) {
                array = array2;
            }
            this.resendOutboundFlight();
            n = this.backOff(n);
        }
    }
    
    byte[] receiveMessageBody(final short n) throws IOException {
        final Message receiveMessage = this.receiveMessage();
        if (receiveMessage.getType() == n) {
            return receiveMessage.getBody();
        }
        throw new TlsFatalAlert((short)10);
    }
    
    void resetHandshakeMessagesDigest() {
        this.handshakeHash.reset();
    }
    
    void sendMessage(final short n, final byte[] array) throws IOException {
        TlsUtils.checkUint24(array.length);
        if (!this.sending) {
            this.checkInboundFlight();
            this.sending = true;
            this.outboundFlight.removeAllElements();
        }
        final Message message = new Message(this.message_seq++, n, array);
        this.outboundFlight.addElement(message);
        this.writeMessage(message);
        this.updateHandshakeMessagesDigest(message);
    }
    
    static class Message
    {
        private final byte[] body;
        private final int message_seq;
        private final short msg_type;
        
        private Message(final int message_seq, final short msg_type, final byte[] body) {
            this.message_seq = message_seq;
            this.msg_type = msg_type;
            this.body = body;
        }
        
        public byte[] getBody() {
            return this.body;
        }
        
        public int getSeq() {
            return this.message_seq;
        }
        
        public short getType() {
            return this.msg_type;
        }
    }
    
    static class RecordLayerBuffer extends ByteArrayOutputStream
    {
        RecordLayerBuffer(final int n) {
            super(n);
        }
        
        void sendToRecordLayer(final DTLSRecordLayer dtlsRecordLayer) throws IOException {
            dtlsRecordLayer.send(this.buf, 0, this.count);
            this.buf = null;
        }
    }
}
