package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;

public class BasicTlsPSKIdentity implements TlsPSKIdentity
{
    protected byte[] identity;
    protected byte[] psk;
    
    public BasicTlsPSKIdentity(final String s, final byte[] array) {
        this.identity = Strings.toUTF8ByteArray(s);
        this.psk = Arrays.clone(array);
    }
    
    public BasicTlsPSKIdentity(final byte[] array, final byte[] array2) {
        this.identity = Arrays.clone(array);
        this.psk = Arrays.clone(array2);
    }
    
    @Override
    public byte[] getPSK() {
        return this.psk;
    }
    
    @Override
    public byte[] getPSKIdentity() {
        return this.identity;
    }
    
    @Override
    public void notifyIdentityHint(final byte[] array) {
    }
    
    @Override
    public void skipIdentityHint() {
    }
}
