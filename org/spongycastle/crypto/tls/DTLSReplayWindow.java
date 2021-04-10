package org.spongycastle.crypto.tls;

class DTLSReplayWindow
{
    private static final long VALID_SEQ_MASK = 281474976710655L;
    private static final long WINDOW_SIZE = 64L;
    private long bitmap;
    private long latestConfirmedSeq;
    
    DTLSReplayWindow() {
        this.latestConfirmedSeq = -1L;
        this.bitmap = 0L;
    }
    
    void reportAuthenticated(long latestConfirmedSeq) {
        if ((latestConfirmedSeq & 0xFFFFFFFFFFFFL) == latestConfirmedSeq) {
            final long latestConfirmedSeq2 = this.latestConfirmedSeq;
            if (latestConfirmedSeq <= latestConfirmedSeq2) {
                latestConfirmedSeq = latestConfirmedSeq2 - latestConfirmedSeq;
                if (latestConfirmedSeq < 64L) {
                    this.bitmap |= 1L << (int)latestConfirmedSeq;
                }
            }
            else {
                final long n = latestConfirmedSeq - latestConfirmedSeq2;
                if (n >= 64L) {
                    this.bitmap = 1L;
                }
                else {
                    final long bitmap = this.bitmap << (int)n;
                    this.bitmap = bitmap;
                    this.bitmap = (bitmap | 0x1L);
                }
                this.latestConfirmedSeq = latestConfirmedSeq;
            }
            return;
        }
        throw new IllegalArgumentException("'seq' out of range");
    }
    
    void reset() {
        this.latestConfirmedSeq = -1L;
        this.bitmap = 0L;
    }
    
    boolean shouldDiscard(long n) {
        if ((n & 0xFFFFFFFFFFFFL) != n) {
            return true;
        }
        final long latestConfirmedSeq = this.latestConfirmedSeq;
        if (n <= latestConfirmedSeq) {
            n = latestConfirmedSeq - n;
            if (n >= 64L) {
                return true;
            }
            if ((this.bitmap & 1L << (int)n) != 0x0L) {
                return true;
            }
        }
        return false;
    }
}
