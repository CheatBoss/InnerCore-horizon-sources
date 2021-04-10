package org.spongycastle.crypto.tls;

class DTLSEpoch
{
    private final TlsCipher cipher;
    private final int epoch;
    private final DTLSReplayWindow replayWindow;
    private long sequenceNumber;
    
    DTLSEpoch(final int epoch, final TlsCipher cipher) {
        this.replayWindow = new DTLSReplayWindow();
        this.sequenceNumber = 0L;
        if (epoch < 0) {
            throw new IllegalArgumentException("'epoch' must be >= 0");
        }
        if (cipher != null) {
            this.epoch = epoch;
            this.cipher = cipher;
            return;
        }
        throw new IllegalArgumentException("'cipher' cannot be null");
    }
    
    long allocateSequenceNumber() {
        return this.sequenceNumber++;
    }
    
    TlsCipher getCipher() {
        return this.cipher;
    }
    
    int getEpoch() {
        return this.epoch;
    }
    
    DTLSReplayWindow getReplayWindow() {
        return this.replayWindow;
    }
    
    long getSequenceNumber() {
        return this.sequenceNumber;
    }
}
