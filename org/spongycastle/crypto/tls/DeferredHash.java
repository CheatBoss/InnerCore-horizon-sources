package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.*;
import java.util.*;
import org.spongycastle.util.*;

class DeferredHash implements TlsHandshakeHash
{
    protected static final int BUFFERING_HASH_LIMIT = 4;
    private DigestInputBuffer buf;
    protected TlsContext context;
    private Hashtable hashes;
    private Short prfHashAlgorithm;
    
    DeferredHash() {
        this.buf = new DigestInputBuffer();
        this.hashes = new Hashtable();
        this.prfHashAlgorithm = null;
    }
    
    private DeferredHash(final Short prfHashAlgorithm, final Digest digest) {
        this.buf = null;
        (this.hashes = new Hashtable()).put(this.prfHashAlgorithm = prfHashAlgorithm, digest);
    }
    
    protected void checkStopBuffering() {
        if (this.buf != null && this.hashes.size() <= 4) {
            final Enumeration<Digest> elements = this.hashes.elements();
            while (elements.hasMoreElements()) {
                this.buf.updateDigest(elements.nextElement());
            }
            this.buf = null;
        }
    }
    
    protected void checkTrackingHash(final Short n) {
        if (!this.hashes.containsKey(n)) {
            this.hashes.put(n, TlsUtils.createHash(n));
        }
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }
    
    @Override
    public Digest forkPRFHash() {
        this.checkStopBuffering();
        if (this.buf != null) {
            final Digest hash = TlsUtils.createHash(this.prfHashAlgorithm);
            this.buf.updateDigest(hash);
            return hash;
        }
        return TlsUtils.cloneHash(this.prfHashAlgorithm, this.hashes.get(this.prfHashAlgorithm));
    }
    
    @Override
    public String getAlgorithmName() {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }
    
    @Override
    public int getDigestSize() {
        throw new IllegalStateException("Use fork() to get a definite Digest");
    }
    
    @Override
    public byte[] getFinalHash(final short n) {
        final Digest digest = this.hashes.get(Shorts.valueOf(n));
        if (digest != null) {
            final Digest cloneHash = TlsUtils.cloneHash(n, digest);
            final DigestInputBuffer buf = this.buf;
            if (buf != null) {
                buf.updateDigest(cloneHash);
            }
            final byte[] array = new byte[cloneHash.getDigestSize()];
            cloneHash.doFinal(array, 0);
            return array;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("HashAlgorithm.");
        sb.append(HashAlgorithm.getText(n));
        sb.append(" is not being tracked");
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void init(final TlsContext context) {
        this.context = context;
    }
    
    @Override
    public TlsHandshakeHash notifyPRFDetermined() {
        final int prfAlgorithm = this.context.getSecurityParameters().getPrfAlgorithm();
        if (prfAlgorithm == 0) {
            final CombinedHash combinedHash = new CombinedHash();
            combinedHash.init(this.context);
            this.buf.updateDigest(combinedHash);
            return combinedHash.notifyPRFDetermined();
        }
        this.checkTrackingHash(this.prfHashAlgorithm = Shorts.valueOf(TlsUtils.getHashAlgorithmForPRFAlgorithm(prfAlgorithm)));
        return this;
    }
    
    @Override
    public void reset() {
        final DigestInputBuffer buf = this.buf;
        if (buf != null) {
            buf.reset();
            return;
        }
        final Enumeration<Digest> elements = this.hashes.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().reset();
        }
    }
    
    @Override
    public void sealHashAlgorithms() {
        this.checkStopBuffering();
    }
    
    @Override
    public TlsHandshakeHash stopTracking() {
        final Digest cloneHash = TlsUtils.cloneHash(this.prfHashAlgorithm, this.hashes.get(this.prfHashAlgorithm));
        final DigestInputBuffer buf = this.buf;
        if (buf != null) {
            buf.updateDigest(cloneHash);
        }
        final DeferredHash deferredHash = new DeferredHash(this.prfHashAlgorithm, cloneHash);
        deferredHash.init(this.context);
        return deferredHash;
    }
    
    @Override
    public void trackHashAlgorithm(final short n) {
        if (this.buf != null) {
            this.checkTrackingHash(Shorts.valueOf(n));
            return;
        }
        throw new IllegalStateException("Too late to track more hash algorithms");
    }
    
    @Override
    public void update(final byte b) {
        final DigestInputBuffer buf = this.buf;
        if (buf != null) {
            buf.write(b);
            return;
        }
        final Enumeration<Digest> elements = this.hashes.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().update(b);
        }
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        final DigestInputBuffer buf = this.buf;
        if (buf != null) {
            buf.write(array, n, n2);
            return;
        }
        final Enumeration<Digest> elements = this.hashes.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().update(array, n, n2);
        }
    }
}
