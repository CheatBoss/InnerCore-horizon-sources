package org.spongycastle.crypto.signers;

import org.spongycastle.crypto.params.*;
import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class X931Signer implements Signer
{
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA224 = 14540;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private RSAKeyParameters kParam;
    private int keyBits;
    private int trailer;
    
    public X931Signer(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }
    
    public X931Signer(final AsymmetricBlockCipher cipher, final Digest digest, final boolean b) {
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
    
    private void createSignatureBlock() {
        final int digestSize = this.digest.getDigestSize();
        int n;
        if (this.trailer == 188) {
            final byte[] block = this.block;
            n = block.length - digestSize - 1;
            this.digest.doFinal(block, n);
            final byte[] block2 = this.block;
            block2[block2.length - 1] = -68;
        }
        else {
            final byte[] block3 = this.block;
            n = block3.length - digestSize - 2;
            this.digest.doFinal(block3, n);
            final byte[] block4 = this.block;
            final int length = block4.length;
            final int trailer = this.trailer;
            block4[length - 2] = (byte)(trailer >>> 8);
            block4[block4.length - 1] = (byte)trailer;
        }
        this.block[0] = 107;
        for (int i = n - 2; i != 0; --i) {
            this.block[i] = -69;
        }
        this.block[n - 1] = -70;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException {
        this.createSignatureBlock();
        final AsymmetricBlockCipher cipher = this.cipher;
        final byte[] block = this.block;
        final BigInteger bigInteger = new BigInteger(1, cipher.processBlock(block, 0, block.length));
        this.clearBlock(this.block);
        return BigIntegers.asUnsignedByteArray((this.kParam.getModulus().bitLength() + 7) / 8, bigInteger.min(this.kParam.getModulus().subtract(bigInteger)));
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        final RSAKeyParameters kParam = (RSAKeyParameters)cipherParameters;
        this.kParam = kParam;
        this.cipher.init(b, kParam);
        final int bitLength = this.kParam.getModulus().bitLength();
        this.keyBits = bitLength;
        this.block = new byte[(bitLength + 7) / 8];
        this.reset();
    }
    
    @Override
    public void reset() {
        this.digest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(byte[] unsignedByteArray) {
        boolean constantTimeAreEqual = false;
        try {
            this.block = this.cipher.processBlock(unsignedByteArray, 0, unsignedByteArray.length);
            BigInteger subtract = new BigInteger(1, this.block);
            if ((subtract.intValue() & 0xF) != 0xC) {
                subtract = this.kParam.getModulus().subtract(subtract);
                if ((subtract.intValue() & 0xF) != 0xC) {
                    return constantTimeAreEqual;
                }
            }
            this.createSignatureBlock();
            unsignedByteArray = BigIntegers.asUnsignedByteArray(this.block.length, subtract);
            constantTimeAreEqual = Arrays.constantTimeAreEqual(this.block, unsignedByteArray);
            this.clearBlock(this.block);
            this.clearBlock(unsignedByteArray);
            return constantTimeAreEqual;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
