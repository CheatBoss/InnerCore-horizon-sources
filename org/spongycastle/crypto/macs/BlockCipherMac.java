package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;

public class BlockCipherMac implements Mac
{
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] mac;
    private int macSize;
    
    public BlockCipherMac(final BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8 / 2);
    }
    
    public BlockCipherMac(final BlockCipher blockCipher, final int n) {
        if (n % 8 == 0) {
            this.cipher = new CBCBlockCipher(blockCipher);
            this.macSize = n / 8;
            this.mac = new byte[blockCipher.getBlockSize()];
            this.buf = new byte[blockCipher.getBlockSize()];
            this.bufOff = 0;
            return;
        }
        throw new IllegalArgumentException("MAC size must be multiple of 8");
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final int blockSize = this.cipher.getBlockSize();
        while (true) {
            final int bufOff = this.bufOff;
            if (bufOff >= blockSize) {
                break;
            }
            this.buf[bufOff] = 0;
            this.bufOff = bufOff + 1;
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
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
        this.reset();
        this.cipher.init(true, cipherParameters);
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
}
