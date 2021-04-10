package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.paddings.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class ISO9797Alg3Mac implements Mac
{
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private KeyParameter lastKey2;
    private KeyParameter lastKey3;
    private byte[] mac;
    private int macSize;
    private BlockCipherPadding padding;
    
    public ISO9797Alg3Mac(final BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8, null);
    }
    
    public ISO9797Alg3Mac(final BlockCipher blockCipher, final int n) {
        this(blockCipher, n, null);
    }
    
    public ISO9797Alg3Mac(final BlockCipher blockCipher, final int n, final BlockCipherPadding padding) {
        if (n % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (blockCipher instanceof DESEngine) {
            this.cipher = new CBCBlockCipher(blockCipher);
            this.padding = padding;
            this.macSize = n / 8;
            this.mac = new byte[blockCipher.getBlockSize()];
            this.buf = new byte[blockCipher.getBlockSize()];
            this.bufOff = 0;
            return;
        }
        throw new IllegalArgumentException("cipher must be instance of DESEngine");
    }
    
    public ISO9797Alg3Mac(final BlockCipher blockCipher, final BlockCipherPadding blockCipherPadding) {
        this(blockCipher, blockCipher.getBlockSize() * 8, blockCipherPadding);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final int blockSize = this.cipher.getBlockSize();
        if (this.padding == null) {
            while (true) {
                final int bufOff = this.bufOff;
                if (bufOff >= blockSize) {
                    break;
                }
                this.buf[bufOff] = 0;
                this.bufOff = bufOff + 1;
            }
        }
        else {
            if (this.bufOff == blockSize) {
                this.cipher.processBlock(this.buf, 0, this.mac, 0);
                this.bufOff = 0;
            }
            this.padding.addPadding(this.buf, this.bufOff);
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        final DESEngine desEngine = new DESEngine();
        desEngine.init(false, this.lastKey2);
        final byte[] mac = this.mac;
        desEngine.processBlock(mac, 0, mac, 0);
        desEngine.init(true, this.lastKey3);
        final byte[] mac2 = this.mac;
        desEngine.processBlock(mac2, 0, mac2, 0);
        System.arraycopy(this.mac, 0, array, n, this.macSize);
        this.reset();
        return this.macSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return "ISO9797Alg3";
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.reset();
        final boolean b = cipherParameters instanceof KeyParameter;
        if (!b && !(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("params must be an instance of KeyParameter or ParametersWithIV");
        }
        KeyParameter keyParameter;
        if (b) {
            keyParameter = (KeyParameter)cipherParameters;
        }
        else {
            keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
        }
        final byte[] key = keyParameter.getKey();
        KeyParameter lastKey3;
        if (key.length == 16) {
            lastKey3 = new KeyParameter(key, 0, 8);
            this.lastKey2 = new KeyParameter(key, 8, 8);
            this.lastKey3 = lastKey3;
        }
        else {
            if (key.length != 24) {
                throw new IllegalArgumentException("Key must be either 112 or 168 bit long");
            }
            lastKey3 = new KeyParameter(key, 0, 8);
            this.lastKey2 = new KeyParameter(key, 8, 8);
            this.lastKey3 = new KeyParameter(key, 16, 8);
        }
        if (cipherParameters instanceof ParametersWithIV) {
            this.cipher.init(true, new ParametersWithIV(lastKey3, ((ParametersWithIV)cipherParameters).getIV()));
            return;
        }
        this.cipher.init(true, lastKey3);
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
