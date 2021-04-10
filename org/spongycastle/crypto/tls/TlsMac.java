package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;

public class TlsMac
{
    protected TlsContext context;
    protected int digestBlockSize;
    protected int digestOverhead;
    protected Mac mac;
    protected int macLength;
    protected byte[] secret;
    
    public TlsMac(final TlsContext context, final Digest digest, final byte[] array, int digestOverhead, final int n) {
        this.context = context;
        final KeyParameter keyParameter = new KeyParameter(array, digestOverhead, n);
        this.secret = Arrays.clone(keyParameter.getKey());
        if (digest instanceof LongDigest) {
            this.digestBlockSize = 128;
            digestOverhead = 16;
        }
        else {
            this.digestBlockSize = 64;
            digestOverhead = 8;
        }
        this.digestOverhead = digestOverhead;
        if (TlsUtils.isSSL(context)) {
            this.mac = new SSL3Mac(digest);
            if (digest.getDigestSize() == 20) {
                this.digestOverhead = 4;
            }
        }
        else {
            this.mac = new HMac(digest);
        }
        this.mac.init(keyParameter);
        this.macLength = this.mac.getMacSize();
        if (context.getSecurityParameters().truncatedHMac) {
            this.macLength = Math.min(this.macLength, 10);
        }
    }
    
    public byte[] calculateMac(final long n, final short n2, byte[] array, final int n3, final int n4) {
        final ProtocolVersion serverVersion = this.context.getServerVersion();
        final boolean ssl = serverVersion.isSSL();
        int n5;
        if (ssl) {
            n5 = 11;
        }
        else {
            n5 = 13;
        }
        final byte[] array2 = new byte[n5];
        TlsUtils.writeUint64(n, array2, 0);
        TlsUtils.writeUint8(n2, array2, 8);
        if (!ssl) {
            TlsUtils.writeVersion(serverVersion, array2, 9);
        }
        TlsUtils.writeUint16(n4, array2, n5 - 2);
        this.mac.update(array2, 0, n5);
        this.mac.update(array, n3, n4);
        array = new byte[this.mac.getMacSize()];
        this.mac.doFinal(array, 0);
        return this.truncate(array);
    }
    
    public byte[] calculateMacConstantTime(final long n, final short n2, byte[] calculateMac, int n3, final int n4, final int n5, final byte[] array) {
        calculateMac = this.calculateMac(n, n2, calculateMac, n3, n4);
        if (TlsUtils.isSSL(this.context)) {
            n3 = 11;
        }
        else {
            n3 = 13;
        }
        n3 = this.getDigestBlockCount(n5 + n3) - this.getDigestBlockCount(n3 + n4);
        while (true) {
            --n3;
            if (n3 < 0) {
                break;
            }
            this.mac.update(array, 0, this.digestBlockSize);
        }
        this.mac.update(array[0]);
        this.mac.reset();
        return calculateMac;
    }
    
    protected int getDigestBlockCount(final int n) {
        return (n + this.digestOverhead) / this.digestBlockSize;
    }
    
    public byte[] getMACSecret() {
        return this.secret;
    }
    
    public int getSize() {
        return this.macLength;
    }
    
    protected byte[] truncate(final byte[] array) {
        final int length = array.length;
        final int macLength = this.macLength;
        if (length <= macLength) {
            return array;
        }
        return Arrays.copyOf(array, macLength);
    }
}
