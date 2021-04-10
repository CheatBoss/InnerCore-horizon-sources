package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;

class TlsSessionImpl implements TlsSession
{
    final byte[] sessionID;
    SessionParameters sessionParameters;
    
    TlsSessionImpl(final byte[] array, final SessionParameters sessionParameters) {
        if (array == null) {
            throw new IllegalArgumentException("'sessionID' cannot be null");
        }
        if (array.length >= 1 && array.length <= 32) {
            this.sessionID = Arrays.clone(array);
            this.sessionParameters = sessionParameters;
            return;
        }
        throw new IllegalArgumentException("'sessionID' must have length between 1 and 32 bytes, inclusive");
    }
    
    @Override
    public SessionParameters exportSessionParameters() {
        synchronized (this) {
            SessionParameters copy;
            if (this.sessionParameters == null) {
                copy = null;
            }
            else {
                copy = this.sessionParameters.copy();
            }
            return copy;
        }
    }
    
    @Override
    public byte[] getSessionID() {
        synchronized (this) {
            return this.sessionID;
        }
    }
    
    @Override
    public void invalidate() {
        synchronized (this) {
            if (this.sessionParameters != null) {
                this.sessionParameters.clear();
                this.sessionParameters = null;
            }
        }
    }
    
    @Override
    public boolean isResumable() {
        synchronized (this) {
            return this.sessionParameters != null;
        }
    }
}
