package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SSL3Mac implements Mac
{
    static final byte[] IPAD;
    private static final byte IPAD_BYTE = 54;
    static final byte[] OPAD;
    private static final byte OPAD_BYTE = 92;
    private Digest digest;
    private int padLength;
    private byte[] secret;
    
    static {
        IPAD = genPad((byte)54, 48);
        OPAD = genPad((byte)92, 48);
    }
    
    public SSL3Mac(final Digest digest) {
        this.digest = digest;
        int padLength;
        if (digest.getDigestSize() == 20) {
            padLength = 40;
        }
        else {
            padLength = 48;
        }
        this.padLength = padLength;
    }
    
    private static byte[] genPad(final byte b, final int n) {
        final byte[] array = new byte[n];
        Arrays.fill(array, b);
        return array;
    }
    
    @Override
    public int doFinal(final byte[] array, int doFinal) {
        final int digestSize = this.digest.getDigestSize();
        final byte[] array2 = new byte[digestSize];
        this.digest.doFinal(array2, 0);
        final Digest digest = this.digest;
        final byte[] secret = this.secret;
        digest.update(secret, 0, secret.length);
        this.digest.update(SSL3Mac.OPAD, 0, this.padLength);
        this.digest.update(array2, 0, digestSize);
        doFinal = this.digest.doFinal(array, doFinal);
        this.reset();
        return doFinal;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.digest.getAlgorithmName());
        sb.append("/SSL3MAC");
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return this.digest.getDigestSize();
    }
    
    public Digest getUnderlyingDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.secret = Arrays.clone(((KeyParameter)cipherParameters).getKey());
        this.reset();
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        final Digest digest = this.digest;
        final byte[] secret = this.secret;
        digest.update(secret, 0, secret.length);
        this.digest.update(SSL3Mac.IPAD, 0, this.padLength);
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
}
