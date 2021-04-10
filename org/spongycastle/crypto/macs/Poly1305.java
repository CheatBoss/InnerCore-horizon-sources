package org.spongycastle.crypto.macs;

import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class Poly1305 implements Mac
{
    private static final int BLOCK_SIZE = 16;
    private final BlockCipher cipher;
    private final byte[] currentBlock;
    private int currentBlockOffset;
    private int h0;
    private int h1;
    private int h2;
    private int h3;
    private int h4;
    private int k0;
    private int k1;
    private int k2;
    private int k3;
    private int r0;
    private int r1;
    private int r2;
    private int r3;
    private int r4;
    private int s1;
    private int s2;
    private int s3;
    private int s4;
    private final byte[] singleByte;
    
    public Poly1305() {
        this.singleByte = new byte[1];
        this.currentBlock = new byte[16];
        this.currentBlockOffset = 0;
        this.cipher = null;
    }
    
    public Poly1305(final BlockCipher cipher) {
        this.singleByte = new byte[1];
        this.currentBlock = new byte[16];
        this.currentBlockOffset = 0;
        if (cipher.getBlockSize() == 16) {
            this.cipher = cipher;
            return;
        }
        throw new IllegalArgumentException("Poly1305 requires a 128 bit block cipher.");
    }
    
    private static final long mul32x32_64(final int n, final int n2) {
        return ((long)n & 0xFFFFFFFFL) * n2;
    }
    
    private void processBlock() {
        final int currentBlockOffset = this.currentBlockOffset;
        if (currentBlockOffset < 16) {
            this.currentBlock[currentBlockOffset] = 1;
            for (int i = currentBlockOffset + 1; i < 16; ++i) {
                this.currentBlock[i] = 0;
            }
        }
        final long n = (long)Pack.littleEndianToInt(this.currentBlock, 0) & 0xFFFFFFFFL;
        final long n2 = (long)Pack.littleEndianToInt(this.currentBlock, 4) & 0xFFFFFFFFL;
        final long n3 = (long)Pack.littleEndianToInt(this.currentBlock, 8) & 0xFFFFFFFFL;
        final long n4 = (long)Pack.littleEndianToInt(this.currentBlock, 12) & 0xFFFFFFFFL;
        this.h0 += (int)(n & 0x3FFFFFFL);
        this.h1 += (int)((n2 << 32 | n) >>> 26 & 0x3FFFFFFL);
        this.h2 += (int)((n2 | n3 << 32) >>> 20 & 0x3FFFFFFL);
        this.h3 += (int)((n4 << 32 | n3) >>> 14 & 0x3FFFFFFL);
        final int h4 = (int)(this.h4 + (n4 >>> 8));
        this.h4 = h4;
        if (this.currentBlockOffset == 16) {
            this.h4 = h4 + 16777216;
        }
        final long n5 = mul32x32_64(this.h0, this.r0) + mul32x32_64(this.h1, this.s4) + mul32x32_64(this.h2, this.s3) + mul32x32_64(this.h3, this.s2) + mul32x32_64(this.h4, this.s1);
        final long mul32x32_64 = mul32x32_64(this.h0, this.r1);
        final long mul32x32_65 = mul32x32_64(this.h1, this.r0);
        final long mul32x32_66 = mul32x32_64(this.h2, this.s4);
        final long mul32x32_67 = mul32x32_64(this.h3, this.s3);
        final long mul32x32_68 = mul32x32_64(this.h4, this.s2);
        final long mul32x32_69 = mul32x32_64(this.h0, this.r2);
        final long mul32x32_70 = mul32x32_64(this.h1, this.r1);
        final long mul32x32_71 = mul32x32_64(this.h2, this.r0);
        final long mul32x32_72 = mul32x32_64(this.h3, this.s4);
        final long mul32x32_73 = mul32x32_64(this.h4, this.s3);
        final long mul32x32_74 = mul32x32_64(this.h0, this.r3);
        final long mul32x32_75 = mul32x32_64(this.h1, this.r2);
        final long mul32x32_76 = mul32x32_64(this.h2, this.r1);
        final long mul32x32_77 = mul32x32_64(this.h3, this.r0);
        final long mul32x32_78 = mul32x32_64(this.h4, this.s4);
        final long mul32x32_79 = mul32x32_64(this.h0, this.r4);
        final long mul32x32_80 = mul32x32_64(this.h1, this.r3);
        final long mul32x32_81 = mul32x32_64(this.h2, this.r2);
        final long mul32x32_82 = mul32x32_64(this.h3, this.r1);
        final long mul32x32_83 = mul32x32_64(this.h4, this.r0);
        final int h5 = (int)n5 & 0x3FFFFFF;
        this.h0 = h5;
        final long n6 = mul32x32_64 + mul32x32_65 + mul32x32_66 + mul32x32_67 + mul32x32_68 + (n5 >>> 26);
        final int h6 = (int)n6 & 0x3FFFFFF;
        this.h1 = h6;
        final long n7 = mul32x32_69 + mul32x32_70 + mul32x32_71 + mul32x32_72 + mul32x32_73 + (n6 >>> 26);
        this.h2 = ((int)n7 & 0x3FFFFFF);
        final long n8 = mul32x32_74 + mul32x32_75 + mul32x32_76 + mul32x32_77 + mul32x32_78 + (n7 >>> 26);
        this.h3 = ((int)n8 & 0x3FFFFFF);
        final long n9 = mul32x32_79 + mul32x32_80 + mul32x32_81 + mul32x32_82 + mul32x32_83 + (n8 >>> 26);
        this.h4 = ((int)n9 & 0x3FFFFFF);
        final int h7 = h5 + (int)(n9 >>> 26) * 5;
        this.h0 = h7;
        this.h1 = h6 + (h7 >>> 26);
        this.h0 = (h7 & 0x3FFFFFF);
    }
    
    private void setKey(byte[] array, final byte[] array2) {
        if (array.length != 32) {
            throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
        }
        if (this.cipher != null && (array2 == null || array2.length != 16)) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit IV.");
        }
        int n = 0;
        final int littleEndianToInt = Pack.littleEndianToInt(array, 0);
        final int littleEndianToInt2 = Pack.littleEndianToInt(array, 4);
        final int littleEndianToInt3 = Pack.littleEndianToInt(array, 8);
        final int littleEndianToInt4 = Pack.littleEndianToInt(array, 12);
        this.r0 = (0x3FFFFFF & littleEndianToInt);
        final int r1 = (littleEndianToInt >>> 26 | littleEndianToInt2 << 6) & 0x3FFFF03;
        this.r1 = r1;
        final int r2 = (littleEndianToInt2 >>> 20 | littleEndianToInt3 << 12) & 0x3FFC0FF;
        this.r2 = r2;
        final int r3 = (littleEndianToInt3 >>> 14 | littleEndianToInt4 << 18) & 0x3F03FFF;
        this.r3 = r3;
        final int r4 = littleEndianToInt4 >>> 8 & 0xFFFFF;
        this.r4 = r4;
        this.s1 = r1 * 5;
        this.s2 = r2 * 5;
        this.s3 = r3 * 5;
        this.s4 = r4 * 5;
        final BlockCipher cipher = this.cipher;
        if (cipher == null) {
            n = 16;
        }
        else {
            final byte[] array3 = new byte[16];
            cipher.init(true, new KeyParameter(array, 16, 16));
            this.cipher.processBlock(array2, 0, array3, 0);
            array = array3;
        }
        this.k0 = Pack.littleEndianToInt(array, n + 0);
        this.k1 = Pack.littleEndianToInt(array, n + 4);
        this.k2 = Pack.littleEndianToInt(array, n + 8);
        this.k3 = Pack.littleEndianToInt(array, n + 12);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        if (n + 16 <= array.length) {
            if (this.currentBlockOffset > 0) {
                this.processBlock();
            }
            final int h1 = this.h1;
            final int h2 = this.h0;
            final int h3 = h1 + (h2 >>> 26);
            this.h1 = h3;
            final int h4 = h2 & 0x3FFFFFF;
            this.h0 = h4;
            final int h5 = this.h2 + (h3 >>> 26);
            this.h2 = h5;
            final int h6 = h3 & 0x3FFFFFF;
            this.h1 = h6;
            final int h7 = this.h3 + (h5 >>> 26);
            this.h3 = h7;
            final int h8 = h5 & 0x3FFFFFF;
            this.h2 = h8;
            final int h9 = this.h4 + (h7 >>> 26);
            this.h4 = h9;
            final int h10 = h7 & 0x3FFFFFF;
            this.h3 = h10;
            final int h11 = h4 + (h9 >>> 26) * 5;
            this.h0 = h11;
            final int h12 = h9 & 0x3FFFFFF;
            this.h4 = h12;
            final int h13 = h6 + (h11 >>> 26);
            this.h1 = h13;
            final int h14 = h11 & 0x3FFFFFF;
            this.h0 = h14;
            final int n2 = h14 + 5;
            final int n3 = (n2 >>> 26) + h13;
            final int n4 = (n3 >>> 26) + h8;
            final int n5 = (n4 >>> 26) + h10;
            final int n6 = (n5 >>> 26) + h12 - 67108864;
            final int n7 = (n6 >>> 31) - 1;
            final int n8 = ~n7;
            final int h15 = (h14 & n8) | (n2 & 0x3FFFFFF & n7);
            this.h0 = h15;
            final int h16 = (h13 & n8) | (n3 & 0x3FFFFFF & n7);
            this.h1 = h16;
            final int h17 = (h8 & n8) | (n4 & 0x3FFFFFF & n7);
            this.h2 = h17;
            final int h18 = (0x3FFFFFF & n5 & n7) | (h10 & n8);
            this.h3 = h18;
            final int h19 = (h12 & n8) | (n6 & n7);
            this.h4 = h19;
            final long n9 = ((long)(h15 | h16 << 26) & 0xFFFFFFFFL) + ((long)this.k0 & 0xFFFFFFFFL);
            final long n10 = h16 >>> 6 | h17 << 20;
            final long n11 = this.k1;
            final long n12 = h17 >>> 12 | h18 << 14;
            final long n13 = this.k2;
            final long n14 = h18 >>> 18 | h19 << 8;
            final long n15 = this.k3;
            Pack.intToLittleEndian((int)n9, array, n);
            final long n16 = (n10 & 0xFFFFFFFFL) + (n11 & 0xFFFFFFFFL) + (n9 >>> 32);
            Pack.intToLittleEndian((int)n16, array, n + 4);
            final long n17 = (n12 & 0xFFFFFFFFL) + (n13 & 0xFFFFFFFFL) + (n16 >>> 32);
            Pack.intToLittleEndian((int)n17, array, n + 8);
            Pack.intToLittleEndian((int)((n14 & 0xFFFFFFFFL) + (n15 & 0xFFFFFFFFL) + (n17 >>> 32)), array, n + 12);
            this.reset();
            return 16;
        }
        throw new OutputLengthException("Output buffer is too short.");
    }
    
    @Override
    public String getAlgorithmName() {
        if (this.cipher == null) {
            return "Poly1305";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Poly1305-");
        sb.append(this.cipher.getAlgorithmName());
        return sb.toString();
    }
    
    @Override
    public int getMacSize() {
        return 16;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        byte[] iv;
        CipherParameters parameters;
        if (this.cipher != null) {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("Poly1305 requires an IV when used with a block cipher.");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            iv = parametersWithIV.getIV();
            parameters = parametersWithIV.getParameters();
        }
        else {
            final byte[] array = null;
            parameters = cipherParameters;
            iv = array;
        }
        if (parameters instanceof KeyParameter) {
            this.setKey(((KeyParameter)parameters).getKey(), iv);
            this.reset();
            return;
        }
        throw new IllegalArgumentException("Poly1305 requires a key.");
    }
    
    @Override
    public void reset() {
        this.currentBlockOffset = 0;
        this.h4 = 0;
        this.h3 = 0;
        this.h2 = 0;
        this.h1 = 0;
        this.h0 = 0;
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        final byte[] singleByte = this.singleByte;
        singleByte[0] = b;
        this.update(singleByte, 0, 1);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int i) throws DataLengthException, IllegalStateException {
        int min;
        for (int n2 = 0; i > n2; n2 += min, this.currentBlockOffset += min) {
            if (this.currentBlockOffset == 16) {
                this.processBlock();
                this.currentBlockOffset = 0;
            }
            min = Math.min(i - n2, 16 - this.currentBlockOffset);
            System.arraycopy(array, n2 + n, this.currentBlock, this.currentBlockOffset, min);
        }
    }
}
