package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.*;

class CombinedHash implements TlsHandshakeHash
{
    protected TlsContext context;
    protected Digest md5;
    protected Digest sha1;
    
    CombinedHash() {
        this.md5 = TlsUtils.createHash((short)1);
        this.sha1 = TlsUtils.createHash((short)2);
    }
    
    CombinedHash(final CombinedHash combinedHash) {
        this.context = combinedHash.context;
        this.md5 = TlsUtils.cloneHash((short)1, combinedHash.md5);
        this.sha1 = TlsUtils.cloneHash((short)2, combinedHash.sha1);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final TlsContext context = this.context;
        if (context != null && TlsUtils.isSSL(context)) {
            this.ssl3Complete(this.md5, SSL3Mac.IPAD, SSL3Mac.OPAD, 48);
            this.ssl3Complete(this.sha1, SSL3Mac.IPAD, SSL3Mac.OPAD, 40);
        }
        final int doFinal = this.md5.doFinal(array, n);
        return doFinal + this.sha1.doFinal(array, n + doFinal);
    }
    
    @Override
    public Digest forkPRFHash() {
        return new CombinedHash(this);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.md5.getAlgorithmName());
        sb.append(" and ");
        sb.append(this.sha1.getAlgorithmName());
        return sb.toString();
    }
    
    @Override
    public int getDigestSize() {
        return this.md5.getDigestSize() + this.sha1.getDigestSize();
    }
    
    @Override
    public byte[] getFinalHash(final short n) {
        throw new IllegalStateException("CombinedHash doesn't support multiple hashes");
    }
    
    @Override
    public void init(final TlsContext context) {
        this.context = context;
    }
    
    @Override
    public TlsHandshakeHash notifyPRFDetermined() {
        return this;
    }
    
    @Override
    public void reset() {
        this.md5.reset();
        this.sha1.reset();
    }
    
    @Override
    public void sealHashAlgorithms() {
    }
    
    protected void ssl3Complete(final Digest digest, byte[] array, final byte[] array2, final int n) {
        final byte[] masterSecret = this.context.getSecurityParameters().masterSecret;
        digest.update(masterSecret, 0, masterSecret.length);
        digest.update(array, 0, n);
        final int digestSize = digest.getDigestSize();
        array = new byte[digestSize];
        digest.doFinal(array, 0);
        digest.update(masterSecret, 0, masterSecret.length);
        digest.update(array2, 0, n);
        digest.update(array, 0, digestSize);
    }
    
    @Override
    public TlsHandshakeHash stopTracking() {
        return new CombinedHash(this);
    }
    
    @Override
    public void trackHashAlgorithm(final short n) {
        throw new IllegalStateException("CombinedHash only supports calculating the legacy PRF for handshake hash");
    }
    
    @Override
    public void update(final byte b) {
        this.md5.update(b);
        this.sha1.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.md5.update(array, n, n2);
        this.sha1.update(array, n, n2);
    }
}
