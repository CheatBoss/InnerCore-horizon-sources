package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.*;

public class TlsNullCipher implements TlsCipher
{
    protected TlsContext context;
    protected TlsMac readMac;
    protected TlsMac writeMac;
    
    public TlsNullCipher(final TlsContext context) {
        this.context = context;
        this.writeMac = null;
        this.readMac = null;
    }
    
    public TlsNullCipher(final TlsContext context, final Digest digest, final Digest digest2) throws IOException {
        int n = true ? 1 : 0;
        final boolean b = digest == null;
        if (digest2 != null) {
            n = (false ? 1 : 0);
        }
        if ((b ? 1 : 0) != n) {
            throw new TlsFatalAlert((short)80);
        }
        this.context = context;
        final TlsMac tlsMac = null;
        TlsMac tlsMac3;
        TlsMac tlsMac4;
        if (digest != null) {
            final int n2 = digest.getDigestSize() + digest2.getDigestSize();
            final byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(context, n2);
            final TlsMac tlsMac2 = new TlsMac(context, digest, calculateKeyBlock, 0, digest.getDigestSize());
            final int n3 = digest.getDigestSize() + 0;
            tlsMac3 = new TlsMac(context, digest2, calculateKeyBlock, n3, digest2.getDigestSize());
            if (n3 + digest2.getDigestSize() != n2) {
                throw new TlsFatalAlert((short)80);
            }
            tlsMac4 = tlsMac2;
        }
        else {
            tlsMac4 = null;
            tlsMac3 = tlsMac;
        }
        if (context.isServer()) {
            this.writeMac = tlsMac3;
            this.readMac = tlsMac4;
            return;
        }
        this.writeMac = tlsMac4;
        this.readMac = tlsMac3;
    }
    
    @Override
    public byte[] decodeCiphertext(final long n, final short n2, final byte[] array, final int n3, final int n4) throws IOException {
        final TlsMac readMac = this.readMac;
        if (readMac == null) {
            return Arrays.copyOfRange(array, n3, n4 + n3);
        }
        final int size = readMac.getSize();
        if (n4 < size) {
            throw new TlsFatalAlert((short)50);
        }
        final int n5 = n4 - size;
        final int n6 = n3 + n5;
        if (Arrays.constantTimeAreEqual(Arrays.copyOfRange(array, n6, n4 + n3), this.readMac.calculateMac(n, n2, array, n3, n5))) {
            return Arrays.copyOfRange(array, n3, n6);
        }
        throw new TlsFatalAlert((short)20);
    }
    
    @Override
    public byte[] encodePlaintext(final long n, final short n2, final byte[] array, final int n3, final int n4) throws IOException {
        final TlsMac writeMac = this.writeMac;
        if (writeMac == null) {
            return Arrays.copyOfRange(array, n3, n4 + n3);
        }
        final byte[] calculateMac = writeMac.calculateMac(n, n2, array, n3, n4);
        final byte[] array2 = new byte[calculateMac.length + n4];
        System.arraycopy(array, n3, array2, 0, n4);
        System.arraycopy(calculateMac, 0, array2, n4, calculateMac.length);
        return array2;
    }
    
    @Override
    public int getPlaintextLimit(final int n) {
        final TlsMac writeMac = this.writeMac;
        int n2 = n;
        if (writeMac != null) {
            n2 = n - writeMac.getSize();
        }
        return n2;
    }
}
