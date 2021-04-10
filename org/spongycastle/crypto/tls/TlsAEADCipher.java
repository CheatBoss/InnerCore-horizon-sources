package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.modes.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class TlsAEADCipher implements TlsCipher
{
    static final int NONCE_DRAFT_CHACHA20_POLY1305 = 2;
    public static final int NONCE_RFC5288 = 1;
    protected TlsContext context;
    protected AEADBlockCipher decryptCipher;
    protected byte[] decryptImplicitNonce;
    protected AEADBlockCipher encryptCipher;
    protected byte[] encryptImplicitNonce;
    protected int macSize;
    protected int nonceMode;
    protected int record_iv_length;
    
    public TlsAEADCipher(final TlsContext tlsContext, final AEADBlockCipher aeadBlockCipher, final AEADBlockCipher aeadBlockCipher2, final int n, final int n2) throws IOException {
        this(tlsContext, aeadBlockCipher, aeadBlockCipher2, n, n2, 1);
    }
    
    TlsAEADCipher(final TlsContext context, AEADBlockCipher encryptCipher, final AEADBlockCipher aeadBlockCipher, int n, final int macSize, int nonceMode) throws IOException {
        if (!TlsUtils.isTLSv12(context)) {
            throw new TlsFatalAlert((short)80);
        }
        if ((this.nonceMode = nonceMode) != 1) {
            if (nonceMode != 2) {
                throw new TlsFatalAlert((short)80);
            }
            nonceMode = 12;
            this.record_iv_length = 0;
        }
        else {
            nonceMode = 4;
            this.record_iv_length = 8;
        }
        this.context = context;
        this.macSize = macSize;
        final int n2 = n * 2 + nonceMode * 2;
        final byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(context, n2);
        KeyParameter keyParameter = new KeyParameter(calculateKeyBlock, 0, n);
        final int n3 = n + 0;
        KeyParameter keyParameter2 = new KeyParameter(calculateKeyBlock, n3, n);
        final int n4 = n3 + n;
        n = n4 + nonceMode;
        final byte[] copyOfRange = Arrays.copyOfRange(calculateKeyBlock, n4, n);
        final int n5 = n + nonceMode;
        final byte[] copyOfRange2 = Arrays.copyOfRange(calculateKeyBlock, n, n5);
        if (n5 == n2) {
            if (context.isServer()) {
                this.encryptCipher = aeadBlockCipher;
                this.decryptCipher = encryptCipher;
                this.encryptImplicitNonce = copyOfRange2;
                this.decryptImplicitNonce = copyOfRange;
                final KeyParameter keyParameter3 = keyParameter;
                keyParameter = keyParameter2;
                keyParameter2 = keyParameter3;
            }
            else {
                this.encryptCipher = encryptCipher;
                this.decryptCipher = aeadBlockCipher;
                this.encryptImplicitNonce = copyOfRange;
                this.decryptImplicitNonce = copyOfRange2;
            }
            final byte[] array = new byte[nonceMode + this.record_iv_length];
            encryptCipher = this.encryptCipher;
            n = macSize * 8;
            encryptCipher.init(true, new AEADParameters(keyParameter, n, array));
            this.decryptCipher.init(false, new AEADParameters(keyParameter2, n, array));
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public byte[] decodeCiphertext(final long n, final short n2, final byte[] array, int n3, int outputSize) throws IOException {
        if (this.getPlaintextLimit(outputSize) >= 0) {
            final byte[] decryptImplicitNonce = this.decryptImplicitNonce;
            final int n4 = decryptImplicitNonce.length + this.record_iv_length;
            final byte[] array2 = new byte[n4];
            final int nonceMode = this.nonceMode;
            if (nonceMode != 1) {
                if (nonceMode != 2) {
                    throw new TlsFatalAlert((short)80);
                }
                TlsUtils.writeUint64(n, array2, n4 - 8);
                int n5 = 0;
                while (true) {
                    final byte[] decryptImplicitNonce2 = this.decryptImplicitNonce;
                    if (n5 >= decryptImplicitNonce2.length) {
                        break;
                    }
                    array2[n5] ^= decryptImplicitNonce2[n5];
                    ++n5;
                }
            }
            else {
                System.arraycopy(decryptImplicitNonce, 0, array2, 0, decryptImplicitNonce.length);
                final int record_iv_length = this.record_iv_length;
                System.arraycopy(array, n3, array2, n4 - record_iv_length, record_iv_length);
            }
            final int record_iv_length2 = this.record_iv_length;
            final int n6 = outputSize - record_iv_length2;
            outputSize = this.decryptCipher.getOutputSize(n6);
            final byte[] array3 = new byte[outputSize];
            final AEADParameters aeadParameters = new AEADParameters(null, this.macSize * 8, array2, this.getAdditionalData(n, n2, outputSize));
            try {
                this.decryptCipher.init(false, aeadParameters);
                n3 = this.decryptCipher.processBytes(array, n3 + record_iv_length2, n6, array3, 0) + 0;
                if (n3 + this.decryptCipher.doFinal(array3, n3) == outputSize) {
                    return array3;
                }
                throw new TlsFatalAlert((short)80);
            }
            catch (Exception ex) {
                throw new TlsFatalAlert((short)20, ex);
            }
        }
        throw new TlsFatalAlert((short)50);
    }
    
    @Override
    public byte[] encodePlaintext(final long n, final short n2, final byte[] array, int n3, int doFinal) throws IOException {
        final byte[] encryptImplicitNonce = this.encryptImplicitNonce;
        final int n4 = encryptImplicitNonce.length + this.record_iv_length;
        final byte[] array2 = new byte[n4];
        final int nonceMode = this.nonceMode;
        if (nonceMode != 1) {
            if (nonceMode != 2) {
                throw new TlsFatalAlert((short)80);
            }
            TlsUtils.writeUint64(n, array2, n4 - 8);
            int n5 = 0;
            while (true) {
                final byte[] encryptImplicitNonce2 = this.encryptImplicitNonce;
                if (n5 >= encryptImplicitNonce2.length) {
                    break;
                }
                array2[n5] ^= encryptImplicitNonce2[n5];
                ++n5;
            }
        }
        else {
            System.arraycopy(encryptImplicitNonce, 0, array2, 0, encryptImplicitNonce.length);
            TlsUtils.writeUint64(n, array2, this.encryptImplicitNonce.length);
        }
        final int outputSize = this.encryptCipher.getOutputSize(doFinal);
        final int record_iv_length = this.record_iv_length;
        final int n6 = record_iv_length + outputSize;
        final byte[] array3 = new byte[n6];
        if (record_iv_length != 0) {
            System.arraycopy(array2, n4 - record_iv_length, array3, 0, record_iv_length);
        }
        final int record_iv_length2 = this.record_iv_length;
        final AEADParameters aeadParameters = new AEADParameters(null, this.macSize * 8, array2, this.getAdditionalData(n, n2, doFinal));
        try {
            this.encryptCipher.init(true, aeadParameters);
            n3 = record_iv_length2 + this.encryptCipher.processBytes(array, n3, doFinal, array3, record_iv_length2);
            doFinal = this.encryptCipher.doFinal(array3, n3);
            if (n3 + doFinal == n6) {
                return array3;
            }
            throw new TlsFatalAlert((short)80);
        }
        catch (Exception ex) {
            throw new TlsFatalAlert((short)80, ex);
        }
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
        return n - this.macSize - this.record_iv_length;
    }
}
