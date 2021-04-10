package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.modes.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.paddings.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class CMac implements Mac
{
    private byte[] Lu;
    private byte[] Lu2;
    private byte[] ZEROES;
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] mac;
    private int macSize;
    private byte[] poly;
    
    public CMac(final BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8);
    }
    
    public CMac(final BlockCipher blockCipher, final int n) {
        if (n % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (n <= blockCipher.getBlockSize() * 8) {
            this.cipher = new CBCBlockCipher(blockCipher);
            this.macSize = n / 8;
            this.poly = lookupPoly(blockCipher.getBlockSize());
            this.mac = new byte[blockCipher.getBlockSize()];
            this.buf = new byte[blockCipher.getBlockSize()];
            this.ZEROES = new byte[blockCipher.getBlockSize()];
            this.bufOff = 0;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("MAC size must be less or equal to ");
        sb.append(blockCipher.getBlockSize() * 8);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private byte[] doubleLu(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        final int n = -shiftLeft(array, array2) & 0xFF;
        final int n2 = array.length - 3;
        final byte b = array2[n2];
        final byte[] poly = this.poly;
        array2[n2] = (byte)(b ^ (poly[1] & n));
        final int n3 = array.length - 2;
        array2[n3] ^= (byte)(poly[2] & n);
        final int n4 = array.length - 1;
        array2[n4] ^= (byte)(n & poly[3]);
        return array2;
    }
    
    private static byte[] lookupPoly(int n) {
        n *= 8;
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown block size for CMAC: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            case 2048: {
                n = 548865;
                break;
            }
            case 1024: {
                n = 524355;
                break;
            }
            case 768: {
                n = 655377;
                break;
            }
            case 512: {
                n = 293;
                break;
            }
            case 448: {
                n = 2129;
                break;
            }
            case 384: {
                n = 4109;
                break;
            }
            case 256: {
                n = 1061;
                break;
            }
            case 224: {
                n = 777;
                break;
            }
            case 160: {
                n = 45;
                break;
            }
            case 128:
            case 192: {
                n = 135;
                break;
            }
            case 64:
            case 320: {
                n = 27;
                break;
            }
        }
        return Pack.intToBigEndian(n);
    }
    
    private static int shiftLeft(final byte[] array, final byte[] array2) {
        int length = array.length;
        int n = 0;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            final int n2 = array[length] & 0xFF;
            array2[length] = (byte)(n | n2 << 1);
            n = (n2 >>> 7 & 0x1);
        }
        return n;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        byte[] array2;
        if (this.bufOff == this.cipher.getBlockSize()) {
            array2 = this.Lu;
        }
        else {
            new ISO7816d4Padding().addPadding(this.buf, this.bufOff);
            array2 = this.Lu2;
        }
        int n2 = 0;
        byte[] mac;
        while (true) {
            mac = this.mac;
            if (n2 >= mac.length) {
                break;
            }
            final byte[] buf = this.buf;
            buf[n2] ^= array2[n2];
            ++n2;
        }
        this.cipher.processBlock(this.buf, 0, mac, 0);
        System.arraycopy(this.mac, 0, array, n, this.macSize);
        this.reset();
        return this.macSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.validate(cipherParameters);
        this.cipher.init(true, cipherParameters);
        final byte[] zeroes = this.ZEROES;
        final byte[] array = new byte[zeroes.length];
        this.cipher.processBlock(zeroes, 0, array, 0);
        final byte[] doubleLu = this.doubleLu(array);
        this.Lu = doubleLu;
        this.Lu2 = this.doubleLu(doubleLu);
        this.reset();
    }
    
    @Override
    public void reset() {
        int n = 0;
        while (true) {
            final byte[] buf = this.buf;
            if (n >= buf.length) {
                break;
            }
            buf[n] = 0;
            ++n;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
    
    @Override
    public void update(final byte b) {
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        if (bufOff == buf.length) {
            this.cipher.processBlock(buf, 0, this.mac, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = b;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        if (n2 >= 0) {
            final int blockSize = this.cipher.getBlockSize();
            final int bufOff = this.bufOff;
            final int n3 = blockSize - bufOff;
            int n4 = n;
            int n5;
            if ((n5 = n2) > n3) {
                System.arraycopy(array, n, this.buf, bufOff, n3);
                this.cipher.processBlock(this.buf, 0, this.mac, 0);
                this.bufOff = 0;
                n2 -= n3;
                n += n3;
                while (true) {
                    n4 = n;
                    n5 = n2;
                    if (n2 <= blockSize) {
                        break;
                    }
                    this.cipher.processBlock(array, n, this.mac, 0);
                    n2 -= blockSize;
                    n += blockSize;
                }
            }
            System.arraycopy(array, n4, this.buf, this.bufOff, n5);
            this.bufOff += n5;
            return;
        }
        throw new IllegalArgumentException("Can't have a negative input length!");
    }
    
    void validate(final CipherParameters cipherParameters) {
        if (cipherParameters == null) {
            return;
        }
        if (cipherParameters instanceof KeyParameter) {
            return;
        }
        throw new IllegalArgumentException("CMac mode only permits key to be set.");
    }
}
