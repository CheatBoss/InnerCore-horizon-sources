package org.spongycastle.crypto.encodings;

import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class OAEPEncoding implements AsymmetricBlockCipher
{
    private byte[] defHash;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private Digest mgf1Hash;
    private SecureRandom random;
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, DigestFactory.createSHA1(), null);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher engine, final Digest digest, final Digest mgf1Hash, final byte[] array) {
        this.engine = engine;
        this.mgf1Hash = mgf1Hash;
        this.defHash = new byte[digest.getDigestSize()];
        digest.reset();
        if (array != null) {
            digest.update(array, 0, array.length);
        }
        digest.doFinal(this.defHash, 0);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final byte[] array) {
        this(asymmetricBlockCipher, digest, digest, array);
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    private byte[] maskGeneratorFunction1(final byte[] array, final int n, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        final int digestSize = this.mgf1Hash.getDigestSize();
        final byte[] array3 = new byte[digestSize];
        final byte[] array4 = new byte[4];
        this.mgf1Hash.reset();
        int i;
        for (i = 0; i < n3 / digestSize; ++i) {
            this.ItoOSP(i, array4);
            this.mgf1Hash.update(array, n, n2);
            this.mgf1Hash.update(array4, 0, 4);
            this.mgf1Hash.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, i * digestSize, digestSize);
        }
        final int n4 = digestSize * i;
        if (n4 < n3) {
            this.ItoOSP(i, array4);
            this.mgf1Hash.update(array, n, n2);
            this.mgf1Hash.update(array4, 0, 4);
            this.mgf1Hash.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, n4, n3 - n4);
        }
        return array2;
    }
    
    public byte[] decodeBlock(byte[] array, int i, int n) throws InvalidCipherTextException {
        final byte[] processBlock = this.engine.processBlock(array, i, n);
        final int outputBlockSize = this.engine.getOutputBlockSize();
        array = new byte[outputBlockSize];
        System.arraycopy(processBlock, 0, array, outputBlockSize - processBlock.length, processBlock.length);
        if (outputBlockSize < this.defHash.length * 2 + 1) {
            n = 1;
        }
        else {
            n = 0;
        }
        final byte[] defHash = this.defHash;
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array, defHash.length, outputBlockSize - defHash.length, defHash.length);
        i = 0;
        byte[] defHash2;
        while (true) {
            defHash2 = this.defHash;
            if (i == defHash2.length) {
                break;
            }
            array[i] ^= maskGeneratorFunction1[i];
            ++i;
        }
        final byte[] maskGeneratorFunction2 = this.maskGeneratorFunction1(array, 0, defHash2.length, outputBlockSize - defHash2.length);
        for (i = this.defHash.length; i != outputBlockSize; ++i) {
            array[i] ^= maskGeneratorFunction2[i - this.defHash.length];
        }
        i = 0;
        boolean b = false;
        byte[] defHash3;
        while (true) {
            defHash3 = this.defHash;
            if (i == defHash3.length) {
                break;
            }
            if (defHash3[i] != array[defHash3.length + i]) {
                b = true;
            }
            ++i;
        }
        i = defHash3.length * 2;
        int n2 = outputBlockSize;
        while (i != outputBlockSize) {
            if (array[i] != 0 & n2 == outputBlockSize) {
                n2 = i;
            }
            ++i;
        }
        if (n2 > outputBlockSize - 1) {
            i = 1;
        }
        else {
            i = 0;
        }
        final boolean b2 = array[n2] != 1;
        final int n3 = n2 + 1;
        if ((n | (b ? 1 : 0) | (i | (b2 ? 1 : 0))) == 0x0) {
            i = outputBlockSize - n3;
            final byte[] array2 = new byte[i];
            System.arraycopy(array, n3, array2, 0, i);
            return array2;
        }
        Arrays.fill(array, (byte)0);
        throw new InvalidCipherTextException("data wrong");
    }
    
    public byte[] encodeBlock(byte[] array, int i, final int n) throws InvalidCipherTextException {
        if (n <= this.getInputBlockSize()) {
            final int n2 = this.getInputBlockSize() + 1 + this.defHash.length * 2;
            final byte[] array2 = new byte[n2];
            final int n3 = n2 - n;
            System.arraycopy(array, i, array2, n3, n);
            array2[n3 - 1] = 1;
            array = this.defHash;
            System.arraycopy(array, 0, array2, array.length, array.length);
            i = this.defHash.length;
            array = new byte[i];
            this.random.nextBytes(array);
            final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array, 0, i, n2 - this.defHash.length);
            for (i = this.defHash.length; i != n2; ++i) {
                array2[i] ^= maskGeneratorFunction1[i - this.defHash.length];
            }
            System.arraycopy(array, 0, array2, 0, this.defHash.length);
            array = this.defHash;
            array = this.maskGeneratorFunction1(array2, array.length, n2 - array.length, array.length);
            for (i = 0; i != this.defHash.length; ++i) {
                array2[i] ^= array[i];
            }
            return this.engine.processBlock(array2, 0, n2);
        }
        throw new DataLengthException("input data too long");
    }
    
    @Override
    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            inputBlockSize = inputBlockSize - 1 - this.defHash.length * 2;
        }
        return inputBlockSize;
    }
    
    @Override
    public int getOutputBlockSize() {
        final int outputBlockSize = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return outputBlockSize;
        }
        return outputBlockSize - 1 - this.defHash.length * 2;
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            random = ((ParametersWithRandom)cipherParameters).getRandom();
        }
        else {
            random = new SecureRandom();
        }
        this.random = random;
        this.engine.init(forEncryption, cipherParameters);
        this.forEncryption = forEncryption;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(array, n, n2);
        }
        return this.decodeBlock(array, n, n2);
    }
}
