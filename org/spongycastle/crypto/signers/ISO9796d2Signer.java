package org.spongycastle.crypto.signers;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class ISO9796d2Signer implements SignerWithRecovery
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
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private byte[] preSig;
    private byte[] recoveredMessage;
    private int trailer;
    
    public ISO9796d2Signer(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }
    
    public ISO9796d2Signer(final AsymmetricBlockCipher cipher, final Digest digest, final boolean b) {
        this.cipher = cipher;
        this.digest = digest;
        int intValue;
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
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    private boolean isSameAs(final byte[] array, final byte[] array2) {
        final int messageLength = this.messageLength;
        final byte[] mBuf = this.mBuf;
        final int length = mBuf.length;
        final boolean b = true;
        boolean b2 = true;
        boolean b3;
        if (messageLength > length) {
            if (mBuf.length > array2.length) {
                b2 = false;
            }
            int n = 0;
            while (true) {
                b3 = b2;
                if (n == this.mBuf.length) {
                    break;
                }
                if (array[n] != array2[n]) {
                    b2 = false;
                }
                ++n;
            }
        }
        else {
            boolean b4 = b;
            if (messageLength != array2.length) {
                b4 = false;
            }
            int n2 = 0;
            while (true) {
                b3 = b4;
                if (n2 == array2.length) {
                    break;
                }
                if (array[n2] != array2[n2]) {
                    b4 = false;
                }
                ++n2;
            }
        }
        return b3;
    }
    
    private boolean returnFalse(final byte[] array) {
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        this.clearBlock(array);
        return false;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException {
        final int digestSize = this.digest.getDigestSize();
        final int trailer = this.trailer;
        boolean fullMessage = true;
        int n;
        int n2;
        if (trailer == 188) {
            final byte[] block = this.block;
            n = block.length - digestSize - 1;
            this.digest.doFinal(block, n);
            final byte[] block2 = this.block;
            block2[block2.length - 1] = -68;
            n2 = 8;
        }
        else {
            n2 = 16;
            final byte[] block3 = this.block;
            n = block3.length - digestSize - 2;
            this.digest.doFinal(block3, n);
            final byte[] block4 = this.block;
            final int length = block4.length;
            final int trailer2 = this.trailer;
            block4[length - 2] = (byte)(trailer2 >>> 8);
            block4[block4.length - 1] = (byte)trailer2;
        }
        final int messageLength = this.messageLength;
        final int n3 = (digestSize + messageLength) * 8 + n2 + 4 - this.keyBits;
        int n5;
        byte[] recoveredMessage;
        byte b2;
        if (n3 > 0) {
            final int n4 = messageLength - (n3 + 7) / 8;
            final byte b = 96;
            n5 = n - n4;
            System.arraycopy(this.mBuf, 0, this.block, n5, n4);
            recoveredMessage = new byte[n4];
            b2 = b;
        }
        else {
            final byte b3 = 64;
            final int n6 = n - messageLength;
            System.arraycopy(this.mBuf, 0, this.block, n6, messageLength);
            recoveredMessage = new byte[this.messageLength];
            b2 = b3;
            n5 = n6;
        }
        this.recoveredMessage = recoveredMessage;
        final int n7 = n5 - 1;
        if (n7 > 0) {
            for (int i = n7; i != 0; --i) {
                this.block[i] = -69;
            }
            final byte[] block5 = this.block;
            block5[n7] ^= 0x1;
            block5[0] = 11;
            block5[0] |= b2;
        }
        else {
            final byte[] block6 = this.block;
            block6[0] = 10;
            block6[0] |= b2;
        }
        final AsymmetricBlockCipher cipher = this.cipher;
        final byte[] block7 = this.block;
        final byte[] processBlock = cipher.processBlock(block7, 0, block7.length);
        if ((b2 & 0x20) != 0x0) {
            fullMessage = false;
        }
        this.fullMessage = fullMessage;
        final byte[] mBuf = this.mBuf;
        final byte[] recoveredMessage2 = this.recoveredMessage;
        System.arraycopy(mBuf, 0, recoveredMessage2, 0, recoveredMessage2.length);
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
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
        final RSAKeyParameters rsaKeyParameters = (RSAKeyParameters)cipherParameters;
        this.cipher.init(b, rsaKeyParameters);
        final int bitLength = rsaKeyParameters.getModulus().bitLength();
        this.keyBits = bitLength;
        final byte[] block = new byte[(bitLength + 7) / 8];
        this.block = block;
        int n;
        if (this.trailer == 188) {
            n = block.length - this.digest.getDigestSize() - 2;
        }
        else {
            n = block.length - this.digest.getDigestSize() - 3;
        }
        this.mBuf = new byte[n];
        this.reset();
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        final byte[] recoveredMessage = this.recoveredMessage;
        if (recoveredMessage != null) {
            this.clearBlock(recoveredMessage);
        }
        this.recoveredMessage = null;
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
        final int messageLength = this.messageLength;
        final byte[] mBuf = this.mBuf;
        if (messageLength < mBuf.length) {
            mBuf[messageLength] = b;
        }
        ++this.messageLength;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        while (n2 > 0 && this.messageLength < this.mBuf.length) {
            this.update(array[n]);
            ++n;
            --n2;
        }
        this.digest.update(array, n, n2);
        this.messageLength += n2;
    }
    
    @Override
    public void updateWithRecoveredMessage(byte[] recoveredMessage) throws InvalidCipherTextException {
        final byte[] processBlock = this.cipher.processBlock(recoveredMessage, 0, recoveredMessage.length);
        if (((processBlock[0] & 0xC0) ^ 0x40) != 0x0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        if (((processBlock[processBlock.length - 1] & 0xF) ^ 0xC) != 0x0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        final byte b = processBlock[processBlock.length - 1];
        int n = 2;
        if (((b & 0xFF) ^ 0xBC) == 0x0) {
            n = 1;
        }
        else {
            final int n2 = (processBlock[processBlock.length - 2] & 0xFF) << 8 | (processBlock[processBlock.length - 1] & 0xFF);
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
        int n3;
        for (n3 = 0; n3 != processBlock.length && ((processBlock[n3] & 0xF) ^ 0xA) != 0x0; ++n3) {}
        final int n4 = n3 + 1;
        final int n5 = processBlock.length - n - this.digest.getDigestSize() - n4;
        if (n5 > 0) {
            byte[] array;
            int n6;
            if ((processBlock[0] & 0x20) == 0x0) {
                this.fullMessage = true;
                array = new byte[n5];
                this.recoveredMessage = array;
                n6 = array.length;
            }
            else {
                this.fullMessage = false;
                array = new byte[n5];
                this.recoveredMessage = array;
                n6 = array.length;
            }
            System.arraycopy(processBlock, n4, array, 0, n6);
            this.preSig = recoveredMessage;
            this.preBlock = processBlock;
            final Digest digest = this.digest;
            final byte[] recoveredMessage2 = this.recoveredMessage;
            digest.update(recoveredMessage2, 0, recoveredMessage2.length);
            recoveredMessage = this.recoveredMessage;
            this.messageLength = recoveredMessage.length;
            System.arraycopy(recoveredMessage, 0, this.mBuf, 0, recoveredMessage.length);
            return;
        }
        throw new InvalidCipherTextException("malformed block");
    }
    
    @Override
    public boolean verifySignature(byte[] array) {
        final byte[] preSig = this.preSig;
        Label_0055: {
            if (preSig == null) {
                try {
                    array = this.cipher.processBlock(array, 0, array.length);
                    break Label_0055;
                }
                catch (Exception ex) {
                    return false;
                }
            }
            if (!Arrays.areEqual(preSig, array)) {
                throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
            }
            array = this.preBlock;
            this.preSig = null;
            this.preBlock = null;
        }
        if (((array[0] & 0xC0) ^ 0x40) != 0x0) {
            return this.returnFalse(array);
        }
        if (((array[array.length - 1] & 0xF) ^ 0xC) != 0x0) {
            return this.returnFalse(array);
        }
        final byte b = array[array.length - 1];
        int n = 2;
        if (((b & 0xFF) ^ 0xBC) == 0x0) {
            n = 1;
        }
        else {
            final int n2 = (array[array.length - 2] & 0xFF) << 8 | (array[array.length - 1] & 0xFF);
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
        int n3;
        for (n3 = 0; n3 != array.length && ((array[n3] & 0xF) ^ 0xA) != 0x0; ++n3) {}
        final int n4 = n3 + 1;
        final int digestSize = this.digest.getDigestSize();
        final byte[] array2 = new byte[digestSize];
        final int n5 = array.length - n - digestSize;
        final int n6 = n5 - n4;
        if (n6 <= 0) {
            return this.returnFalse(array);
        }
        byte[] array3;
        int n8;
        if ((array[0] & 0x20) == 0x0) {
            this.fullMessage = true;
            if (this.messageLength > n6) {
                return this.returnFalse(array);
            }
            this.digest.reset();
            this.digest.update(array, n4, n6);
            this.digest.doFinal(array2, 0);
            int i = 0;
            boolean b2 = true;
            while (i != digestSize) {
                final int n7 = n5 + i;
                array[n7] ^= array2[i];
                if (array[n7] != 0) {
                    b2 = false;
                }
                ++i;
            }
            if (!b2) {
                return this.returnFalse(array);
            }
            array3 = new byte[n6];
            this.recoveredMessage = array3;
            n8 = array3.length;
        }
        else {
            this.fullMessage = false;
            this.digest.doFinal(array2, 0);
            int j = 0;
            boolean b3 = true;
            while (j != digestSize) {
                final int n9 = n5 + j;
                array[n9] ^= array2[j];
                if (array[n9] != 0) {
                    b3 = false;
                }
                ++j;
            }
            if (!b3) {
                return this.returnFalse(array);
            }
            array3 = new byte[n6];
            this.recoveredMessage = array3;
            n8 = array3.length;
        }
        System.arraycopy(array, n4, array3, 0, n8);
        if (this.messageLength != 0 && !this.isSameAs(this.mBuf, this.recoveredMessage)) {
            return this.returnFalse(array);
        }
        this.clearBlock(this.mBuf);
        this.clearBlock(array);
        this.messageLength = 0;
        return true;
    }
}
