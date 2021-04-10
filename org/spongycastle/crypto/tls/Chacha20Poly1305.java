package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class Chacha20Poly1305 implements TlsCipher
{
    private static final byte[] ZEROES;
    protected TlsContext context;
    protected ChaCha7539Engine decryptCipher;
    protected byte[] decryptIV;
    protected ChaCha7539Engine encryptCipher;
    protected byte[] encryptIV;
    
    static {
        ZEROES = new byte[15];
    }
    
    public Chacha20Poly1305(final TlsContext context) throws IOException {
        if (TlsUtils.isTLSv12(context)) {
            this.context = context;
            final byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(context, 88);
            final KeyParameter keyParameter = new KeyParameter(calculateKeyBlock, 0, 32);
            KeyParameter keyParameter2 = new KeyParameter(calculateKeyBlock, 32, 32);
            final byte[] copyOfRange = Arrays.copyOfRange(calculateKeyBlock, 64, 76);
            final byte[] copyOfRange2 = Arrays.copyOfRange(calculateKeyBlock, 76, 88);
            this.encryptCipher = new ChaCha7539Engine();
            this.decryptCipher = new ChaCha7539Engine();
            KeyParameter keyParameter3;
            if (context.isServer()) {
                this.encryptIV = copyOfRange2;
                this.decryptIV = copyOfRange;
                keyParameter3 = keyParameter2;
                keyParameter2 = keyParameter;
            }
            else {
                this.encryptIV = copyOfRange;
                this.decryptIV = copyOfRange2;
                keyParameter3 = keyParameter;
            }
            this.encryptCipher.init(true, new ParametersWithIV(keyParameter3, this.encryptIV));
            this.decryptCipher.init(false, new ParametersWithIV(keyParameter2, this.decryptIV));
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected byte[] calculateNonce(final long n, final byte[] array) {
        final byte[] array2 = new byte[12];
        TlsUtils.writeUint64(n, array2, 4);
        for (int i = 0; i < 12; ++i) {
            array2[i] ^= array[i];
        }
        return array2;
    }
    
    protected byte[] calculateRecordMAC(final KeyParameter keyParameter, final byte[] array, final byte[] array2, final int n, final int n2) {
        final Poly1305 poly1305 = new Poly1305();
        poly1305.init(keyParameter);
        this.updateRecordMACText(poly1305, array, 0, array.length);
        this.updateRecordMACText(poly1305, array2, n, n2);
        this.updateRecordMACLength(poly1305, array.length);
        this.updateRecordMACLength(poly1305, n2);
        final byte[] array3 = new byte[poly1305.getMacSize()];
        poly1305.doFinal(array3, 0);
        return array3;
    }
    
    @Override
    public byte[] decodeCiphertext(final long n, final short n2, final byte[] array, final int n3, final int n4) throws IOException {
        if (this.getPlaintextLimit(n4) < 0) {
            throw new TlsFatalAlert((short)50);
        }
        final KeyParameter initRecord = this.initRecord(this.decryptCipher, false, n, this.decryptIV);
        final int n5 = n4 - 16;
        if (Arrays.constantTimeAreEqual(this.calculateRecordMAC(initRecord, this.getAdditionalData(n, n2, n5), array, n3, n5), Arrays.copyOfRange(array, n3 + n5, n3 + n4))) {
            final byte[] array2 = new byte[n5];
            this.decryptCipher.processBytes(array, n3, n5, array2, 0);
            return array2;
        }
        throw new TlsFatalAlert((short)20);
    }
    
    @Override
    public byte[] encodePlaintext(final long n, final short n2, byte[] calculateRecordMAC, final int n3, final int n4) throws IOException {
        final KeyParameter initRecord = this.initRecord(this.encryptCipher, true, n, this.encryptIV);
        final byte[] array = new byte[n4 + 16];
        this.encryptCipher.processBytes(calculateRecordMAC, n3, n4, array, 0);
        calculateRecordMAC = this.calculateRecordMAC(initRecord, this.getAdditionalData(n, n2, n4), array, 0, n4);
        System.arraycopy(calculateRecordMAC, 0, array, n4, calculateRecordMAC.length);
        return array;
    }
    
    protected KeyParameter generateRecordMACKey(final StreamCipher streamCipher) {
        final byte[] array = new byte[64];
        streamCipher.processBytes(array, 0, 64, array, 0);
        final KeyParameter keyParameter = new KeyParameter(array, 0, 32);
        Arrays.fill(array, (byte)0);
        return keyParameter;
    }
    
    protected byte[] getAdditionalData(final long n, final short n2, final int n3) throws IOException {
        final byte[] array = new byte[13];
        TlsUtils.writeUint64(n, array, 0);
        TlsUtils.writeUint8(n2, array, 8);
        TlsUtils.writeVersion(this.context.getServerVersion(), array, 9);
        TlsUtils.writeUint16(n3, array, 11);
        return array;
    }
    
    @Override
    public int getPlaintextLimit(final int n) {
        return n - 16;
    }
    
    protected KeyParameter initRecord(final StreamCipher streamCipher, final boolean b, final long n, final byte[] array) {
        streamCipher.init(b, new ParametersWithIV(null, this.calculateNonce(n, array)));
        return this.generateRecordMACKey(streamCipher);
    }
    
    protected void updateRecordMACLength(final Mac mac, final int n) {
        final byte[] longToLittleEndian = Pack.longToLittleEndian((long)n & 0xFFFFFFFFL);
        mac.update(longToLittleEndian, 0, longToLittleEndian.length);
    }
    
    protected void updateRecordMACText(final Mac mac, final byte[] array, int n, final int n2) {
        mac.update(array, n, n2);
        n = n2 % 16;
        if (n != 0) {
            mac.update(Chacha20Poly1305.ZEROES, 0, 16 - n);
        }
    }
}
