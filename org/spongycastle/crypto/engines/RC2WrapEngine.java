package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.modes.*;
import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RC2WrapEngine implements Wrapper
{
    private static final byte[] IV2;
    byte[] digest;
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private CipherParameters param;
    private ParametersWithIV paramPlusIV;
    Digest sha1;
    private SecureRandom sr;
    
    static {
        IV2 = new byte[] { 74, -35, -94, 44, 121, -24, 33, 5 };
    }
    
    public RC2WrapEngine() {
        this.sha1 = DigestFactory.createSHA1();
        this.digest = new byte[20];
    }
    
    private byte[] calculateCMSKeyChecksum(final byte[] array) {
        final byte[] array2 = new byte[8];
        this.sha1.update(array, 0, array.length);
        this.sha1.doFinal(this.digest, 0);
        System.arraycopy(this.digest, 0, array2, 0, 8);
        return array2;
    }
    
    private boolean checkCMSKeyChecksum(final byte[] array, final byte[] array2) {
        return Arrays.constantTimeAreEqual(this.calculateCMSKeyChecksum(array), array2);
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC2";
    }
    
    @Override
    public void init(final boolean forWrapping, CipherParameters parameters) {
        this.forWrapping = forWrapping;
        this.engine = new CBCBlockCipher(new RC2Engine());
        if (parameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)parameters;
            this.sr = parametersWithRandom.getRandom();
            parameters = parametersWithRandom.getParameters();
        }
        else {
            this.sr = new SecureRandom();
        }
        if (!(parameters instanceof ParametersWithIV)) {
            this.param = parameters;
            if (this.forWrapping) {
                final byte[] iv = new byte[8];
                this.iv = iv;
                this.sr.nextBytes(iv);
                this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
            }
            return;
        }
        final ParametersWithIV paramPlusIV = (ParametersWithIV)parameters;
        this.paramPlusIV = paramPlusIV;
        this.iv = paramPlusIV.getIV();
        this.param = this.paramPlusIV.getParameters();
        if (!this.forWrapping) {
            throw new IllegalArgumentException("You should not supply an IV for unwrapping");
        }
        final byte[] iv2 = this.iv;
        if (iv2 != null && iv2.length == 8) {
            return;
        }
        throw new IllegalArgumentException("IV is not 8 octets");
    }
    
    @Override
    public byte[] unwrap(byte[] array, int i, int n) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("Not set for unwrapping");
        }
        if (array == null) {
            throw new InvalidCipherTextException("Null pointer as ciphertext");
        }
        if (n % this.engine.getBlockSize() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ciphertext not multiple of ");
            sb.append(this.engine.getBlockSize());
            throw new InvalidCipherTextException(sb.toString());
        }
        this.engine.init(false, new ParametersWithIV(this.param, RC2WrapEngine.IV2));
        final byte[] array2 = new byte[n];
        System.arraycopy(array, i, array2, 0, n);
        int n2;
        for (i = 0; i < n / this.engine.getBlockSize(); ++i) {
            n2 = this.engine.getBlockSize() * i;
            this.engine.processBlock(array2, n2, array2, n2);
        }
        final byte[] array3 = new byte[n];
        int n3;
        for (i = 0; i < n; i = n3) {
            n3 = i + 1;
            array3[i] = array2[n - n3];
        }
        final byte[] iv = new byte[8];
        this.iv = iv;
        n -= 8;
        array = new byte[n];
        System.arraycopy(array3, 0, iv, 0, 8);
        System.arraycopy(array3, 8, array, 0, n);
        final ParametersWithIV paramPlusIV = new ParametersWithIV(this.param, this.iv);
        this.paramPlusIV = paramPlusIV;
        this.engine.init(false, paramPlusIV);
        final byte[] array4 = new byte[n];
        System.arraycopy(array, 0, array4, 0, n);
        int n4;
        for (i = 0; i < n / this.engine.getBlockSize(); ++i) {
            n4 = this.engine.getBlockSize() * i;
            this.engine.processBlock(array4, n4, array4, n4);
        }
        i = n - 8;
        array = new byte[i];
        final byte[] array5 = new byte[8];
        System.arraycopy(array4, 0, array, 0, i);
        System.arraycopy(array4, i, array5, 0, 8);
        if (!this.checkCMSKeyChecksum(array, array5)) {
            throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        }
        if (i - ((array[0] & 0xFF) + 1) <= 7) {
            i = array[0];
            final byte[] array6 = new byte[i];
            System.arraycopy(array, 1, array6, 0, i);
            return array6;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("too many pad bytes (");
        sb2.append(i - ((array[0] & 0xFF) + 1));
        sb2.append(")");
        throw new InvalidCipherTextException(sb2.toString());
    }
    
    @Override
    public byte[] wrap(byte[] array, int i, int n) {
        if (!this.forWrapping) {
            throw new IllegalStateException("Not initialized for wrapping");
        }
        final int n2 = n + 1;
        final int n3 = n2 % 8;
        int n4;
        if (n3 != 0) {
            n4 = 8 - n3 + n2;
        }
        else {
            n4 = n2;
        }
        final byte[] array2 = new byte[n4];
        final byte b = (byte)n;
        final int n5 = 0;
        array2[0] = b;
        System.arraycopy(array, i, array2, 1, n);
        i = n4 - n - 1;
        array = new byte[i];
        if (i > 0) {
            this.sr.nextBytes(array);
            System.arraycopy(array, 0, array2, n2, i);
        }
        final byte[] calculateCMSKeyChecksum = this.calculateCMSKeyChecksum(array2);
        n = calculateCMSKeyChecksum.length + n4;
        array = new byte[n];
        System.arraycopy(array2, 0, array, 0, n4);
        System.arraycopy(calculateCMSKeyChecksum, 0, array, n4, calculateCMSKeyChecksum.length);
        final byte[] array3 = new byte[n];
        System.arraycopy(array, 0, array3, 0, n);
        final int n6 = n / this.engine.getBlockSize();
        if (n % this.engine.getBlockSize() == 0) {
            this.engine.init(true, this.paramPlusIV);
            int n7;
            for (i = 0; i < n6; ++i) {
                n7 = this.engine.getBlockSize() * i;
                this.engine.processBlock(array3, n7, array3, n7);
            }
            final byte[] iv = this.iv;
            final int n8 = iv.length + n;
            array = new byte[n8];
            System.arraycopy(iv, 0, array, 0, iv.length);
            System.arraycopy(array3, 0, array, this.iv.length, n);
            final byte[] array4 = new byte[n8];
            for (i = 0; i < n8; i = n) {
                n = i + 1;
                array4[i] = array[n8 - n];
            }
            this.engine.init(true, new ParametersWithIV(this.param, RC2WrapEngine.IV2));
            for (i = n5; i < n6 + 1; ++i) {
                n = this.engine.getBlockSize() * i;
                this.engine.processBlock(array4, n, array4, n);
            }
            return array4;
        }
        throw new IllegalStateException("Not multiple of block length");
    }
}
