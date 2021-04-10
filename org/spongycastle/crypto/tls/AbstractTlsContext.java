package org.spongycastle.crypto.tls;

import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.crypto.*;

abstract class AbstractTlsContext implements TlsContext
{
    private static long counter;
    private ProtocolVersion clientVersion;
    private RandomGenerator nonceRandom;
    private SecureRandom secureRandom;
    private SecurityParameters securityParameters;
    private ProtocolVersion serverVersion;
    private TlsSession session;
    private Object userObject;
    
    static {
        AbstractTlsContext.counter = Times.nanoTime();
    }
    
    AbstractTlsContext(final SecureRandom secureRandom, final SecurityParameters securityParameters) {
        this.clientVersion = null;
        this.serverVersion = null;
        this.session = null;
        this.userObject = null;
        final Digest hash = TlsUtils.createHash((short)4);
        final byte[] array = new byte[hash.getDigestSize()];
        secureRandom.nextBytes(array);
        (this.nonceRandom = new DigestRandomGenerator(hash)).addSeedMaterial(nextCounterValue());
        this.nonceRandom.addSeedMaterial(Times.nanoTime());
        this.nonceRandom.addSeedMaterial(array);
        this.secureRandom = secureRandom;
        this.securityParameters = securityParameters;
    }
    
    private static long nextCounterValue() {
        synchronized (AbstractTlsContext.class) {
            return ++AbstractTlsContext.counter;
        }
    }
    
    @Override
    public byte[] exportKeyingMaterial(final String s, final byte[] array, final int n) {
        if (array != null && !TlsUtils.isValidUint16(array.length)) {
            throw new IllegalArgumentException("'context_value' must have length less than 2^16 (or be null)");
        }
        final SecurityParameters securityParameters = this.getSecurityParameters();
        final byte[] clientRandom = securityParameters.getClientRandom();
        final byte[] serverRandom = securityParameters.getServerRandom();
        int n3;
        final int n2 = n3 = clientRandom.length + serverRandom.length;
        if (array != null) {
            n3 = n2 + (array.length + 2);
        }
        final byte[] array2 = new byte[n3];
        System.arraycopy(clientRandom, 0, array2, 0, clientRandom.length);
        final int n4 = clientRandom.length + 0;
        System.arraycopy(serverRandom, 0, array2, n4, serverRandom.length);
        int n6;
        final int n5 = n6 = n4 + serverRandom.length;
        if (array != null) {
            TlsUtils.writeUint16(array.length, array2, n5);
            final int n7 = n5 + 2;
            System.arraycopy(array, 0, array2, n7, array.length);
            n6 = n7 + array.length;
        }
        if (n6 == n3) {
            return TlsUtils.PRF(this, securityParameters.getMasterSecret(), s, array2, n);
        }
        throw new IllegalStateException("error in calculation of seed for export");
    }
    
    @Override
    public ProtocolVersion getClientVersion() {
        return this.clientVersion;
    }
    
    @Override
    public RandomGenerator getNonceRandomGenerator() {
        return this.nonceRandom;
    }
    
    @Override
    public TlsSession getResumableSession() {
        return this.session;
    }
    
    @Override
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }
    
    @Override
    public SecurityParameters getSecurityParameters() {
        return this.securityParameters;
    }
    
    @Override
    public ProtocolVersion getServerVersion() {
        return this.serverVersion;
    }
    
    @Override
    public Object getUserObject() {
        return this.userObject;
    }
    
    void setClientVersion(final ProtocolVersion clientVersion) {
        this.clientVersion = clientVersion;
    }
    
    void setResumableSession(final TlsSession session) {
        this.session = session;
    }
    
    void setServerVersion(final ProtocolVersion serverVersion) {
        this.serverVersion = serverVersion;
    }
    
    @Override
    public void setUserObject(final Object userObject) {
        this.userObject = userObject;
    }
}
