package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import org.spongycastle.crypto.*;

public class DESedeWrapEngine implements Wrapper
{
    private static final byte[] IV2;
    byte[] digest;
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private KeyParameter param;
    private ParametersWithIV paramPlusIV;
    Digest sha1;
    
    static {
        IV2 = new byte[] { 74, -35, -94, 44, 121, -24, 33, 5 };
    }
    
    public DESedeWrapEngine() {
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
    
    private static byte[] reverse(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        int n;
        for (int i = 0; i < array.length; i = n) {
            final int length = array.length;
            n = i + 1;
            array2[i] = array[length - n];
        }
        return array2;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DESede";
    }
    
    @Override
    public void init(final boolean forWrapping, final CipherParameters cipherParameters) {
        this.forWrapping = forWrapping;
        this.engine = new CBCBlockCipher(new DESedeEngine());
        CipherParameters parameters;
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            parameters = parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            final SecureRandom secureRandom = new SecureRandom();
            parameters = cipherParameters;
            random = secureRandom;
        }
        if (parameters instanceof KeyParameter) {
            this.param = (KeyParameter)parameters;
            if (this.forWrapping) {
                random.nextBytes(this.iv = new byte[8]);
                this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
            }
        }
        else if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV paramPlusIV = (ParametersWithIV)parameters;
            this.paramPlusIV = paramPlusIV;
            this.iv = paramPlusIV.getIV();
            this.param = (KeyParameter)this.paramPlusIV.getParameters();
            if (!this.forWrapping) {
                throw new IllegalArgumentException("You should not supply an IV for unwrapping");
            }
            final byte[] iv = this.iv;
            if (iv != null && iv.length == 8) {
                return;
            }
            throw new IllegalArgumentException("IV is not 8 octets");
        }
    }
    
    @Override
    public byte[] unwrap(byte[] array, int i, int n) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("Not set for unwrapping");
        }
        if (array == null) {
            throw new InvalidCipherTextException("Null pointer as ciphertext");
        }
        final int blockSize = this.engine.getBlockSize();
        if (n % blockSize != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ciphertext not multiple of ");
            sb.append(blockSize);
            throw new InvalidCipherTextException(sb.toString());
        }
        this.engine.init(false, new ParametersWithIV(this.param, DESedeWrapEngine.IV2));
        final byte[] array2 = new byte[n];
        for (int j = 0; j != n; j += blockSize) {
            this.engine.processBlock(array, i + j, array2, j);
        }
        final byte[] reverse = reverse(array2);
        final byte[] iv = new byte[8];
        this.iv = iv;
        n = reverse.length - 8;
        array = new byte[n];
        System.arraycopy(reverse, 0, iv, 0, 8);
        System.arraycopy(reverse, 8, array, 0, reverse.length - 8);
        final ParametersWithIV paramPlusIV = new ParametersWithIV(this.param, this.iv);
        this.paramPlusIV = paramPlusIV;
        this.engine.init(false, paramPlusIV);
        final byte[] array3 = new byte[n];
        for (i = 0; i != n; i += blockSize) {
            this.engine.processBlock(array, i, array3, i);
        }
        i = n - 8;
        array = new byte[i];
        final byte[] array4 = new byte[8];
        System.arraycopy(array3, 0, array, 0, i);
        System.arraycopy(array3, i, array4, 0, 8);
        if (this.checkCMSKeyChecksum(array, array4)) {
            return array;
        }
        throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
    }
    
    @Override
    public byte[] wrap(byte[] array, int i, int blockSize) {
        if (!this.forWrapping) {
            throw new IllegalStateException("Not initialized for wrapping");
        }
        final byte[] array2 = new byte[blockSize];
        final int n = 0;
        System.arraycopy(array, i, array2, 0, blockSize);
        final byte[] calculateCMSKeyChecksum = this.calculateCMSKeyChecksum(array2);
        final int n2 = calculateCMSKeyChecksum.length + blockSize;
        array = new byte[n2];
        System.arraycopy(array2, 0, array, 0, blockSize);
        System.arraycopy(calculateCMSKeyChecksum, 0, array, blockSize, calculateCMSKeyChecksum.length);
        blockSize = this.engine.getBlockSize();
        if (n2 % blockSize == 0) {
            this.engine.init(true, this.paramPlusIV);
            final byte[] array3 = new byte[n2];
            for (i = 0; i != n2; i += blockSize) {
                this.engine.processBlock(array, i, array3, i);
            }
            array = this.iv;
            final byte[] array4 = new byte[array.length + n2];
            System.arraycopy(array, 0, array4, 0, array.length);
            System.arraycopy(array3, 0, array4, this.iv.length, n2);
            array = reverse(array4);
            this.engine.init(true, new ParametersWithIV(this.param, DESedeWrapEngine.IV2));
            for (i = n; i != array.length; i += blockSize) {
                this.engine.processBlock(array, i, array, i);
            }
            return array;
        }
        throw new IllegalStateException("Not multiple of block length");
    }
}
