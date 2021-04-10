package org.spongycastle.crypto.signers;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class ISO9796d2PSSSigner implements SignerWithRecovery
{
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private boolean fullMessage;
    private int hLen;
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private int preMStart;
    private byte[] preSig;
    private int preTLength;
    private SecureRandom random;
    private byte[] recoveredMessage;
    private int saltLength;
    private byte[] standardSalt;
    private int trailer;
    
    public ISO9796d2PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final int n) {
        this(asymmetricBlockCipher, digest, n, false);
    }
    
    public ISO9796d2PSSSigner(final AsymmetricBlockCipher cipher, final Digest digest, int intValue, final boolean b) {
        this.cipher = cipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
        this.saltLength = intValue;
        if (b) {
            intValue = 188;
        }
        else {
            final Integer trailer = ISOTrailers.getTrailer(digest);
            if (trailer == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("no valid trailer for digest: ");
                sb.append(digest.getAlgorithmName());
                throw new IllegalArgumentException(sb.toString());
            }
            intValue = trailer;
        }
        this.trailer = intValue;
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    private void LtoOSP(final long n, final byte[] array) {
        array[0] = (byte)(n >>> 56);
        array[1] = (byte)(n >>> 48);
        array[2] = (byte)(n >>> 40);
        array[3] = (byte)(n >>> 32);
        array[4] = (byte)(n >>> 24);
        array[5] = (byte)(n >>> 16);
        array[6] = (byte)(n >>> 8);
        array[7] = (byte)(n >>> 0);
    }
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    private boolean isSameAs(final byte[] array, final byte[] array2) {
        boolean b = this.messageLength == array2.length;
        for (int i = 0; i != array2.length; ++i) {
            if (array[i] != array2[i]) {
                b = false;
            }
        }
        return b;
    }
    
    private byte[] maskGeneratorFunction1(final byte[] array, int n, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        final byte[] array3 = new byte[this.hLen];
        final byte[] array4 = new byte[4];
        this.digest.reset();
        int n4 = 0;
        int hLen;
        while (true) {
            hLen = this.hLen;
            if (n4 >= n3 / hLen) {
                break;
            }
            this.ItoOSP(n4, array4);
            this.digest.update(array, n, n2);
            this.digest.update(array4, 0, 4);
            this.digest.doFinal(array3, 0);
            final int hLen2 = this.hLen;
            System.arraycopy(array3, 0, array2, n4 * hLen2, hLen2);
            ++n4;
        }
        if (hLen * n4 < n3) {
            this.ItoOSP(n4, array4);
            this.digest.update(array, n, n2);
            this.digest.update(array4, 0, 4);
            this.digest.doFinal(array3, 0);
            n = n4 * this.hLen;
            System.arraycopy(array3, 0, array2, n, n3 - n);
        }
        return array2;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException {
        final int digestSize = this.digest.getDigestSize();
        final byte[] array = new byte[digestSize];
        this.digest.doFinal(array, 0);
        final byte[] array2 = new byte[8];
        this.LtoOSP(this.messageLength * 8, array2);
        this.digest.update(array2, 0, 8);
        this.digest.update(this.mBuf, 0, this.messageLength);
        this.digest.update(array, 0, digestSize);
        byte[] standardSalt = this.standardSalt;
        if (standardSalt == null) {
            standardSalt = new byte[this.saltLength];
            this.random.nextBytes(standardSalt);
        }
        this.digest.update(standardSalt, 0, standardSalt.length);
        final int digestSize2 = this.digest.getDigestSize();
        final byte[] array3 = new byte[digestSize2];
        this.digest.doFinal(array3, 0);
        final int trailer = this.trailer;
        boolean fullMessage = true;
        int n;
        if (trailer == 188) {
            n = 1;
        }
        else {
            n = 2;
        }
        final byte[] block = this.block;
        final int length = block.length;
        final int messageLength = this.messageLength;
        final int n2 = length - messageLength - standardSalt.length - this.hLen - n - 1;
        block[n2] = 1;
        final byte[] mBuf = this.mBuf;
        final int n3 = n2 + 1;
        System.arraycopy(mBuf, 0, block, n3, messageLength);
        System.arraycopy(standardSalt, 0, this.block, n3 + this.messageLength, standardSalt.length);
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array3, 0, digestSize2, this.block.length - this.hLen - n);
        for (int i = 0; i != maskGeneratorFunction1.length; ++i) {
            final byte[] block2 = this.block;
            block2[i] ^= maskGeneratorFunction1[i];
        }
        final byte[] block3 = this.block;
        final int length2 = block3.length;
        final int hLen = this.hLen;
        System.arraycopy(array3, 0, block3, length2 - hLen - n, hLen);
        final int trailer2 = this.trailer;
        if (trailer2 == 188) {
            final byte[] block4 = this.block;
            block4[block4.length - 1] = -68;
        }
        else {
            final byte[] block5 = this.block;
            block5[block5.length - 2] = (byte)(trailer2 >>> 8);
            block5[block5.length - 1] = (byte)trailer2;
        }
        final byte[] block6 = this.block;
        block6[0] &= 0x7F;
        final byte[] processBlock = this.cipher.processBlock(block6, 0, block6.length);
        final int messageLength2 = this.messageLength;
        this.recoveredMessage = new byte[messageLength2];
        if (messageLength2 > this.mBuf.length) {
            fullMessage = false;
        }
        this.fullMessage = fullMessage;
        final byte[] mBuf2 = this.mBuf;
        final byte[] recoveredMessage = this.recoveredMessage;
        System.arraycopy(mBuf2, 0, recoveredMessage, 0, recoveredMessage.length);
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        this.messageLength = 0;
        return processBlock;
    }
    
    @Override
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }
    
    @Override
    public boolean hasFullMessage() {
        return this.fullMessage;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        final int saltLength = this.saltLength;
        int length = 0;
        RSAKeyParameters rsaKeyParameters2 = null;
        Label_0154: {
            SecureRandom random;
            if (cipherParameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                final RSAKeyParameters rsaKeyParameters = (RSAKeyParameters)parametersWithRandom.getParameters();
                length = saltLength;
                rsaKeyParameters2 = rsaKeyParameters;
                if (!b) {
                    break Label_0154;
                }
                random = parametersWithRandom.getRandom();
                rsaKeyParameters2 = rsaKeyParameters;
            }
            else if (cipherParameters instanceof ParametersWithSalt) {
                final ParametersWithSalt parametersWithSalt = (ParametersWithSalt)cipherParameters;
                rsaKeyParameters2 = (RSAKeyParameters)parametersWithSalt.getParameters();
                final byte[] salt = parametersWithSalt.getSalt();
                this.standardSalt = salt;
                length = salt.length;
                if (salt.length == this.saltLength) {
                    break Label_0154;
                }
                throw new IllegalArgumentException("Fixed salt is of wrong length");
            }
            else {
                final RSAKeyParameters rsaKeyParameters3 = (RSAKeyParameters)cipherParameters;
                length = saltLength;
                rsaKeyParameters2 = rsaKeyParameters3;
                if (!b) {
                    break Label_0154;
                }
                random = new SecureRandom();
                rsaKeyParameters2 = rsaKeyParameters3;
            }
            this.random = random;
            length = saltLength;
        }
        this.cipher.init(b, rsaKeyParameters2);
        final int bitLength = rsaKeyParameters2.getModulus().bitLength();
        this.keyBits = bitLength;
        final byte[] block = new byte[(bitLength + 7) / 8];
        this.block = block;
        int n;
        if (this.trailer == 188) {
            n = block.length - this.digest.getDigestSize() - length - 1 - 1;
        }
        else {
            n = block.length - this.digest.getDigestSize() - length - 1 - 2;
        }
        this.mBuf = new byte[n];
        this.reset();
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        final byte[] mBuf = this.mBuf;
        if (mBuf != null) {
            this.clearBlock(mBuf);
        }
        final byte[] recoveredMessage = this.recoveredMessage;
        if (recoveredMessage != null) {
            this.clearBlock(recoveredMessage);
            this.recoveredMessage = null;
        }
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }
    
    @Override
    public void update(final byte b) {
        if (this.preSig == null) {
            final int messageLength = this.messageLength;
            final byte[] mBuf = this.mBuf;
            if (messageLength < mBuf.length) {
                this.messageLength = messageLength + 1;
                mBuf[messageLength] = b;
                return;
            }
        }
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        if (this.preSig == null) {
            while (true) {
                n3 = n;
                if ((n4 = n2) <= 0) {
                    break;
                }
                n3 = n;
                n4 = n2;
                if (this.messageLength >= this.mBuf.length) {
                    break;
                }
                this.update(array[n]);
                ++n;
                --n2;
            }
        }
        if (n4 > 0) {
            this.digest.update(array, n3, n4);
        }
    }
    
    @Override
    public void updateWithRecoveredMessage(final byte[] preSig) throws InvalidCipherTextException {
        final byte[] processBlock = this.cipher.processBlock(preSig, 0, preSig.length);
        final int length = processBlock.length;
        final int n = (this.keyBits + 7) / 8;
        byte[] preBlock = processBlock;
        if (length < n) {
            preBlock = new byte[n];
            System.arraycopy(processBlock, 0, preBlock, n - processBlock.length, processBlock.length);
            this.clearBlock(processBlock);
        }
        final int length2 = preBlock.length;
        boolean fullMessage = true;
        final byte b = preBlock[length2 - 1];
        int preTLength = 2;
        if (((b & 0xFF) ^ 0xBC) == 0x0) {
            preTLength = 1;
        }
        else {
            final int n2 = (preBlock[preBlock.length - 2] & 0xFF) << 8 | (preBlock[preBlock.length - 1] & 0xFF);
            final Integer trailer = ISOTrailers.getTrailer(this.digest);
            if (trailer == null) {
                throw new IllegalArgumentException("unrecognised hash in signature");
            }
            if (n2 != trailer) {
                final StringBuilder sb = new StringBuilder();
                sb.append("signer initialised with wrong digest for trailer ");
                sb.append(n2);
                throw new IllegalStateException(sb.toString());
            }
        }
        this.digest.doFinal(new byte[this.hLen], 0);
        final int length3 = preBlock.length;
        final int hLen = this.hLen;
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(preBlock, length3 - hLen - preTLength, hLen, preBlock.length - hLen - preTLength);
        for (int i = 0; i != maskGeneratorFunction1.length; ++i) {
            preBlock[i] ^= maskGeneratorFunction1[i];
        }
        preBlock[0] &= 0x7F;
        int n3;
        for (n3 = 0; n3 != preBlock.length && preBlock[n3] != 1; ++n3) {}
        final int preMStart = n3 + 1;
        if (preMStart >= preBlock.length) {
            this.clearBlock(preBlock);
        }
        if (preMStart <= 1) {
            fullMessage = false;
        }
        this.fullMessage = fullMessage;
        final byte[] recoveredMessage = new byte[maskGeneratorFunction1.length - preMStart - this.saltLength];
        System.arraycopy(preBlock, preMStart, this.recoveredMessage = recoveredMessage, 0, recoveredMessage.length);
        final byte[] recoveredMessage2 = this.recoveredMessage;
        System.arraycopy(recoveredMessage2, 0, this.mBuf, 0, recoveredMessage2.length);
        this.preSig = preSig;
        this.preBlock = preBlock;
        this.preMStart = preMStart;
        this.preTLength = preTLength;
    }
    
    @Override
    public boolean verifySignature(byte[] array) {
        final int hLen = this.hLen;
        final byte[] array2 = new byte[hLen];
        this.digest.doFinal(array2, 0);
        final byte[] preSig = this.preSig;
        Label_0054: {
            if (preSig == null) {
                try {
                    this.updateWithRecoveredMessage(array);
                    break Label_0054;
                }
                catch (Exception ex) {
                    return false;
                }
            }
            if (!Arrays.areEqual(preSig, array)) {
                throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
            }
        }
        array = this.preBlock;
        final int preMStart = this.preMStart;
        final int preTLength = this.preTLength;
        this.preSig = null;
        this.preBlock = null;
        final byte[] array3 = new byte[8];
        this.LtoOSP(this.recoveredMessage.length * 8, array3);
        this.digest.update(array3, 0, 8);
        final byte[] recoveredMessage = this.recoveredMessage;
        if (recoveredMessage.length != 0) {
            this.digest.update(recoveredMessage, 0, recoveredMessage.length);
        }
        this.digest.update(array2, 0, hLen);
        final byte[] standardSalt = this.standardSalt;
        if (standardSalt != null) {
            this.digest.update(standardSalt, 0, standardSalt.length);
        }
        else {
            this.digest.update(array, preMStart + this.recoveredMessage.length, this.saltLength);
        }
        final int digestSize = this.digest.getDigestSize();
        final byte[] array4 = new byte[digestSize];
        this.digest.doFinal(array4, 0);
        final int length = array.length;
        int i = 0;
        boolean b = true;
        while (i != digestSize) {
            if (array4[i] != array[length - preTLength - digestSize + i]) {
                b = false;
            }
            ++i;
        }
        this.clearBlock(array);
        this.clearBlock(array4);
        if (!b) {
            this.fullMessage = false;
            this.messageLength = 0;
            array = this.recoveredMessage;
        }
        else {
            if (this.messageLength == 0 || this.isSameAs(this.mBuf, this.recoveredMessage)) {
                this.messageLength = 0;
                this.clearBlock(this.mBuf);
                return true;
            }
            this.messageLength = 0;
            array = this.mBuf;
        }
        this.clearBlock(array);
        return false;
    }
}
