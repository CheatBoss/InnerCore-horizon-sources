package org.spongycastle.crypto.signers;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class PSSSigner implements Signer
{
    public static final byte TRAILER_IMPLICIT = -68;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest contentDigest;
    private int emBits;
    private int hLen;
    private byte[] mDash;
    private Digest mgfDigest;
    private int mgfhLen;
    private SecureRandom random;
    private int sLen;
    private boolean sSet;
    private byte[] salt;
    private byte trailer;
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final int n) {
        this(asymmetricBlockCipher, digest, n, (byte)(-68));
    }
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final int n, final byte b) {
        this(asymmetricBlockCipher, digest, digest, n, b);
    }
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final Digest digest2, final int n) {
        this(asymmetricBlockCipher, digest, digest2, n, (byte)(-68));
    }
    
    public PSSSigner(final AsymmetricBlockCipher cipher, final Digest contentDigest, final Digest mgfDigest, final int sLen, final byte trailer) {
        this.cipher = cipher;
        this.contentDigest = contentDigest;
        this.mgfDigest = mgfDigest;
        this.hLen = contentDigest.getDigestSize();
        this.mgfhLen = mgfDigest.getDigestSize();
        this.sSet = false;
        this.sLen = sLen;
        this.salt = new byte[sLen];
        this.mDash = new byte[sLen + 8 + this.hLen];
        this.trailer = trailer;
    }
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final Digest digest2, final byte[] array) {
        this(asymmetricBlockCipher, digest, digest2, array, (byte)(-68));
    }
    
    public PSSSigner(final AsymmetricBlockCipher cipher, final Digest contentDigest, final Digest mgfDigest, final byte[] salt, final byte trailer) {
        this.cipher = cipher;
        this.contentDigest = contentDigest;
        this.mgfDigest = mgfDigest;
        this.hLen = contentDigest.getDigestSize();
        this.mgfhLen = mgfDigest.getDigestSize();
        this.sSet = true;
        final int length = salt.length;
        this.sLen = length;
        this.salt = salt;
        this.mDash = new byte[length + 8 + this.hLen];
        this.trailer = trailer;
    }
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final byte[] array) {
        this(asymmetricBlockCipher, digest, digest, array, (byte)(-68));
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    private byte[] maskGeneratorFunction1(final byte[] array, int n, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        final byte[] array3 = new byte[this.mgfhLen];
        final byte[] array4 = new byte[4];
        this.mgfDigest.reset();
        int n4 = 0;
        int mgfhLen;
        while (true) {
            mgfhLen = this.mgfhLen;
            if (n4 >= n3 / mgfhLen) {
                break;
            }
            this.ItoOSP(n4, array4);
            this.mgfDigest.update(array, n, n2);
            this.mgfDigest.update(array4, 0, 4);
            this.mgfDigest.doFinal(array3, 0);
            final int mgfhLen2 = this.mgfhLen;
            System.arraycopy(array3, 0, array2, n4 * mgfhLen2, mgfhLen2);
            ++n4;
        }
        if (mgfhLen * n4 < n3) {
            this.ItoOSP(n4, array4);
            this.mgfDigest.update(array, n, n2);
            this.mgfDigest.update(array4, 0, 4);
            this.mgfDigest.doFinal(array3, 0);
            n = n4 * this.mgfhLen;
            System.arraycopy(array3, 0, array2, n, n3 - n);
        }
        return array2;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        final Digest contentDigest = this.contentDigest;
        final byte[] mDash = this.mDash;
        contentDigest.doFinal(mDash, mDash.length - this.hLen - this.sLen);
        if (this.sLen != 0) {
            if (!this.sSet) {
                this.random.nextBytes(this.salt);
            }
            final byte[] salt = this.salt;
            final byte[] mDash2 = this.mDash;
            final int length = mDash2.length;
            final int sLen = this.sLen;
            System.arraycopy(salt, 0, mDash2, length - sLen, sLen);
        }
        final int hLen = this.hLen;
        final byte[] array = new byte[hLen];
        final Digest contentDigest2 = this.contentDigest;
        final byte[] mDash3 = this.mDash;
        contentDigest2.update(mDash3, 0, mDash3.length);
        this.contentDigest.doFinal(array, 0);
        final byte[] block = this.block;
        final int length2 = block.length;
        final int sLen2 = this.sLen;
        final int hLen2 = this.hLen;
        block[length2 - sLen2 - 1 - hLen2 - 1] = 1;
        System.arraycopy(this.salt, 0, block, block.length - sLen2 - hLen2 - 1, sLen2);
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array, 0, hLen, this.block.length - this.hLen - 1);
        for (int i = 0; i != maskGeneratorFunction1.length; ++i) {
            final byte[] block2 = this.block;
            block2[i] ^= maskGeneratorFunction1[i];
        }
        final byte[] block3 = this.block;
        block3[0] &= (byte)(255 >> block3.length * 8 - this.emBits);
        final int length3 = block3.length;
        final int hLen3 = this.hLen;
        System.arraycopy(array, 0, block3, length3 - hLen3 - 1, hLen3);
        final byte[] block4 = this.block;
        block4[block4.length - 1] = this.trailer;
        final byte[] processBlock = this.cipher.processBlock(block4, 0, block4.length);
        this.clearBlock(this.block);
        return processBlock;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        CipherParameters parameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            parameters = parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        }
        else {
            if (b) {
                this.random = new SecureRandom();
            }
            parameters = cipherParameters;
        }
        RSAKeyParameters rsaKeyParameters;
        if (parameters instanceof RSABlindingParameters) {
            final RSAKeyParameters publicKey = ((RSABlindingParameters)parameters).getPublicKey();
            this.cipher.init(b, cipherParameters);
            rsaKeyParameters = publicKey;
        }
        else {
            rsaKeyParameters = (RSAKeyParameters)parameters;
            this.cipher.init(b, parameters);
        }
        final int emBits = rsaKeyParameters.getModulus().bitLength() - 1;
        this.emBits = emBits;
        if (emBits >= this.hLen * 8 + this.sLen * 8 + 9) {
            this.block = new byte[(emBits + 7) / 8];
            this.reset();
            return;
        }
        throw new IllegalArgumentException("key too small for specified hash and salt lengths");
    }
    
    @Override
    public void reset() {
        this.contentDigest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.contentDigest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.contentDigest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(byte[] array) {
        final Digest contentDigest = this.contentDigest;
        final byte[] mDash = this.mDash;
        contentDigest.doFinal(mDash, mDash.length - this.hLen - this.sLen);
        try {
            array = this.cipher.processBlock(array, 0, array.length);
            System.arraycopy(array, 0, this.block, this.block.length - array.length, array.length);
            array = this.block;
            if (array[array.length - 1] != this.trailer) {
                this.clearBlock(array);
                return false;
            }
            final int length = array.length;
            final int hLen = this.hLen;
            array = this.maskGeneratorFunction1(array, length - hLen - 1, hLen, array.length - hLen - 1);
            for (int i = 0; i != array.length; ++i) {
                final byte[] block = this.block;
                block[i] ^= array[i];
            }
            array = this.block;
            array[0] &= (byte)(255 >> array.length * 8 - this.emBits);
            int n = 0;
            while (true) {
                array = this.block;
                final int length2 = array.length;
                final int hLen2 = this.hLen;
                final int sLen = this.sLen;
                if (n != length2 - hLen2 - sLen - 2) {
                    if (array[n] != 0) {
                        this.clearBlock(array);
                        return false;
                    }
                    ++n;
                }
                else {
                    if (array[array.length - hLen2 - sLen - 2] != 1) {
                        this.clearBlock(array);
                        return false;
                    }
                    if (this.sSet) {
                        array = this.salt;
                        final byte[] mDash2 = this.mDash;
                        System.arraycopy(array, 0, mDash2, mDash2.length - sLen, sLen);
                    }
                    else {
                        final int length3 = array.length;
                        final byte[] mDash3 = this.mDash;
                        System.arraycopy(array, length3 - sLen - hLen2 - 1, mDash3, mDash3.length - sLen, sLen);
                    }
                    final Digest contentDigest2 = this.contentDigest;
                    final byte[] mDash4 = this.mDash;
                    contentDigest2.update(mDash4, 0, mDash4.length);
                    final Digest contentDigest3 = this.contentDigest;
                    final byte[] mDash5 = this.mDash;
                    contentDigest3.doFinal(mDash5, mDash5.length - this.hLen);
                    final int length4 = this.block.length;
                    final int hLen3 = this.hLen;
                    int n2 = length4 - hLen3 - 1;
                    int n3 = this.mDash.length - hLen3;
                    while (true) {
                        array = this.mDash;
                        if (n3 == array.length) {
                            this.clearBlock(array);
                            this.clearBlock(this.block);
                            return true;
                        }
                        if ((this.block[n2] ^ array[n3]) != 0x0) {
                            this.clearBlock(array);
                            this.clearBlock(this.block);
                            return false;
                        }
                        ++n2;
                        ++n3;
                    }
                }
            }
        }
        catch (Exception ex) {
            return false;
        }
    }
}
