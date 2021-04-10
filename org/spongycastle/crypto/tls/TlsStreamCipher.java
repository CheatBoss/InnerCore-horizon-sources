package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.*;

public class TlsStreamCipher implements TlsCipher
{
    protected TlsContext context;
    protected StreamCipher decryptCipher;
    protected StreamCipher encryptCipher;
    protected TlsMac readMac;
    protected boolean usesNonce;
    protected TlsMac writeMac;
    
    public TlsStreamCipher(final TlsContext context, final StreamCipher encryptCipher, final StreamCipher decryptCipher, final Digest digest, final Digest digest2, final int n, final boolean usesNonce) throws IOException {
        final boolean server = context.isServer();
        this.context = context;
        this.usesNonce = usesNonce;
        this.encryptCipher = encryptCipher;
        this.decryptCipher = decryptCipher;
        final int n2 = n * 2 + digest.getDigestSize() + digest2.getDigestSize();
        final byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(context, n2);
        final TlsMac tlsMac = new TlsMac(context, digest, calculateKeyBlock, 0, digest.getDigestSize());
        final int n3 = digest.getDigestSize() + 0;
        final TlsMac tlsMac2 = new TlsMac(context, digest2, calculateKeyBlock, n3, digest2.getDigestSize());
        final int n4 = n3 + digest2.getDigestSize();
        final KeyParameter keyParameter = new KeyParameter(calculateKeyBlock, n4, n);
        final int n5 = n4 + n;
        KeyParameter keyParameter2 = new KeyParameter(calculateKeyBlock, n5, n);
        if (n5 + n == n2) {
            KeyParameter keyParameter3;
            if (server) {
                this.writeMac = tlsMac2;
                this.readMac = tlsMac;
                this.encryptCipher = decryptCipher;
                this.decryptCipher = encryptCipher;
                keyParameter3 = keyParameter2;
                keyParameter2 = keyParameter;
            }
            else {
                this.writeMac = tlsMac;
                this.readMac = tlsMac2;
                this.encryptCipher = encryptCipher;
                this.decryptCipher = decryptCipher;
                keyParameter3 = keyParameter;
            }
            CipherParameters cipherParameters = keyParameter3;
            CipherParameters cipherParameters2 = keyParameter2;
            if (usesNonce) {
                final byte[] array = new byte[8];
                cipherParameters = new ParametersWithIV(keyParameter3, array);
                cipherParameters2 = new ParametersWithIV(keyParameter2, array);
            }
            this.encryptCipher.init(true, cipherParameters);
            this.decryptCipher.init(false, cipherParameters2);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected void checkMAC(final long n, final short n2, final byte[] array, final int n3, final int n4, final byte[] array2, final int n5, final int n6) throws IOException {
        if (Arrays.constantTimeAreEqual(Arrays.copyOfRange(array, n3, n4), this.readMac.calculateMac(n, n2, array2, n5, n6))) {
            return;
        }
        throw new TlsFatalAlert((short)20);
    }
    
    @Override
    public byte[] decodeCiphertext(final long n, final short n2, final byte[] array, final int n3, final int n4) throws IOException {
        if (this.usesNonce) {
            this.updateIV(this.decryptCipher, false, n);
        }
        final int size = this.readMac.getSize();
        if (n4 >= size) {
            final int n5 = n4 - size;
            final byte[] array2 = new byte[n4];
            this.decryptCipher.processBytes(array, n3, n4, array2, 0);
            this.checkMAC(n, n2, array2, n5, n4, array2, 0, n5);
            return Arrays.copyOfRange(array2, 0, n5);
        }
        throw new TlsFatalAlert((short)50);
    }
    
    @Override
    public byte[] encodePlaintext(final long n, final short n2, byte[] calculateMac, final int n3, final int n4) {
        if (this.usesNonce) {
            this.updateIV(this.encryptCipher, true, n);
        }
        final byte[] array = new byte[n4 + this.writeMac.getSize()];
        this.encryptCipher.processBytes(calculateMac, n3, n4, array, 0);
        calculateMac = this.writeMac.calculateMac(n, n2, calculateMac, n3, n4);
        this.encryptCipher.processBytes(calculateMac, 0, calculateMac.length, array, n4);
        return array;
    }
    
    @Override
    public int getPlaintextLimit(final int n) {
        return n - this.writeMac.getSize();
    }
    
    protected void updateIV(final StreamCipher streamCipher, final boolean b, final long n) {
        final byte[] array = new byte[8];
        TlsUtils.writeUint64(n, array, 0);
        streamCipher.init(b, new ParametersWithIV(null, array));
    }
}
