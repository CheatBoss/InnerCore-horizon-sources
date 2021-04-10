package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class GOST28147Mac implements Mac
{
    private byte[] S;
    private int blockSize;
    private byte[] buf;
    private int bufOff;
    private boolean firstStep;
    private byte[] mac;
    private byte[] macIV;
    private int macSize;
    private int[] workingKey;
    
    public GOST28147Mac() {
        this.blockSize = 8;
        this.macSize = 4;
        this.firstStep = true;
        this.workingKey = null;
        this.macIV = null;
        this.S = new byte[] { 9, 6, 3, 2, 8, 11, 1, 7, 10, 4, 14, 15, 12, 0, 13, 5, 3, 7, 14, 9, 8, 10, 15, 0, 5, 2, 6, 12, 11, 4, 13, 1, 14, 4, 6, 2, 11, 3, 13, 8, 12, 15, 5, 10, 0, 7, 1, 9, 14, 7, 10, 12, 13, 1, 3, 9, 0, 2, 11, 4, 15, 8, 5, 6, 11, 5, 1, 9, 8, 13, 15, 0, 14, 4, 2, 3, 12, 7, 10, 6, 3, 10, 13, 12, 1, 2, 0, 11, 7, 5, 9, 4, 8, 15, 14, 6, 1, 13, 2, 9, 7, 10, 6, 0, 8, 12, 4, 5, 15, 3, 11, 14, 11, 10, 15, 5, 0, 12, 14, 8, 6, 2, 3, 9, 1, 7, 13, 4 };
        this.mac = new byte[8];
        this.buf = new byte[8];
        this.bufOff = 0;
    }
    
    private byte[] CM5func(final byte[] array, int i, final byte[] array2) {
        final byte[] array3 = new byte[array.length - i];
        final int length = array2.length;
        final int n = 0;
        System.arraycopy(array, i, array3, 0, length);
        for (i = n; i != array2.length; ++i) {
            array3[i] ^= array2[i];
        }
        return array3;
    }
    
    private int bytesToint(final byte[] array, final int n) {
        return (array[n + 3] << 24 & 0xFF000000) + (array[n + 2] << 16 & 0xFF0000) + (array[n + 1] << 8 & 0xFF00) + (array[n] & 0xFF);
    }
    
    private int[] generateWorkingKey(final byte[] array) {
        if (array.length == 32) {
            final int[] array2 = new int[8];
            for (int i = 0; i != 8; ++i) {
                array2[i] = this.bytesToint(array, i * 4);
            }
            return array2;
        }
        throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
    }
    
    private void gost28147MacFunc(final int[] array, final byte[] array2, int n, final byte[] array3, final int n2) {
        final int bytesToint = this.bytesToint(array2, n);
        int bytesToint2 = this.bytesToint(array2, n + 4);
        int i = 0;
        n = bytesToint;
        while (i < 2) {
            int gost28147_mainStep;
            int n3;
            for (int j = 0; j < 8; ++j, n3 = n, n = (bytesToint2 ^ gost28147_mainStep), bytesToint2 = n3) {
                gost28147_mainStep = this.gost28147_mainStep(n, array[j]);
            }
            ++i;
        }
        this.intTobytes(n, array3, n2);
        this.intTobytes(bytesToint2, array3, n2 + 4);
    }
    
    private int gost28147_mainStep(int n, final int n2) {
        n += n2;
        final byte[] s = this.S;
        n = (s[(n >> 0 & 0xF) + 0] << 0) + (s[(n >> 4 & 0xF) + 16] << 4) + (s[(n >> 8 & 0xF) + 32] << 8) + (s[(n >> 12 & 0xF) + 48] << 12) + (s[(n >> 16 & 0xF) + 64] << 16) + (s[(n >> 20 & 0xF) + 80] << 20) + (s[(n >> 24 & 0xF) + 96] << 24) + (s[(n >> 28 & 0xF) + 112] << 28);
        return n << 11 | n >>> 21;
    }
    
    private void intTobytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >>> 24);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2] = (byte)n;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        while (true) {
            final int bufOff = this.bufOff;
            if (bufOff >= this.blockSize) {
                break;
            }
            this.buf[bufOff] = 0;
            this.bufOff = bufOff + 1;
        }
        final byte[] buf = this.buf;
        byte[] cm5func = new byte[buf.length];
        System.arraycopy(buf, 0, cm5func, 0, this.mac.length);
        if (this.firstStep) {
            this.firstStep = false;
        }
        else {
            cm5func = this.CM5func(this.buf, 0, this.mac);
        }
        this.gost28147MacFunc(this.workingKey, cm5func, 0, this.mac, 0);
        final byte[] mac = this.mac;
        final int n2 = mac.length / 2;
        final int macSize = this.macSize;
        System.arraycopy(mac, n2 - macSize, array, n, macSize);
        this.reset();
        return this.macSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return "GOST28147Mac";
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void init(CipherParameters parameters) throws IllegalArgumentException {
        this.reset();
        this.buf = new byte[this.blockSize];
        this.macIV = null;
        if (parameters instanceof ParametersWithSBox) {
            final ParametersWithSBox parametersWithSBox = (ParametersWithSBox)parameters;
            System.arraycopy(parametersWithSBox.getSBox(), 0, this.S, 0, parametersWithSBox.getSBox().length);
            if (parametersWithSBox.getParameters() == null) {
                return;
            }
            parameters = parametersWithSBox.getParameters();
        }
        else if (!(parameters instanceof KeyParameter)) {
            if (parameters instanceof ParametersWithIV) {
                final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
                this.workingKey = this.generateWorkingKey(((KeyParameter)parametersWithIV.getParameters()).getKey());
                final byte[] iv = parametersWithIV.getIV();
                final byte[] mac = this.mac;
                System.arraycopy(iv, 0, mac, 0, mac.length);
                this.macIV = parametersWithIV.getIV();
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid parameter passed to GOST28147 init - ");
            sb.append(parameters.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        this.workingKey = this.generateWorkingKey(((KeyParameter)parameters).getKey());
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
        this.firstStep = true;
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        Label_0114: {
            if (bufOff == buf.length) {
                byte[] cm5func = new byte[buf.length];
                System.arraycopy(buf, 0, cm5func, 0, this.mac.length);
                while (true) {
                    byte[] array = null;
                    byte[] array2 = null;
                    Label_0082: {
                        if (!this.firstStep) {
                            array = this.buf;
                            array2 = this.mac;
                            break Label_0082;
                        }
                        this.firstStep = false;
                        array2 = this.macIV;
                        if (array2 != null) {
                            array = this.buf;
                            break Label_0082;
                        }
                        this.gost28147MacFunc(this.workingKey, cm5func, 0, this.mac, 0);
                        this.bufOff = 0;
                        break Label_0114;
                    }
                    cm5func = this.CM5func(array, 0, array2);
                    continue;
                }
            }
        }
        this.buf[this.bufOff++] = b;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) throws DataLengthException, IllegalStateException {
        if (n2 >= 0) {
            final int blockSize = this.blockSize;
            final int bufOff = this.bufOff;
            final int n3 = blockSize - bufOff;
            int n4 = n;
            int n5;
            if ((n5 = n2) > n3) {
                System.arraycopy(array, n, this.buf, bufOff, n3);
                final byte[] buf = this.buf;
                byte[] array2 = new byte[buf.length];
                System.arraycopy(buf, 0, array2, 0, this.mac.length);
                if (this.firstStep) {
                    this.firstStep = false;
                    final byte[] macIV = this.macIV;
                    if (macIV != null) {
                        array2 = this.CM5func(this.buf, 0, macIV);
                    }
                }
                else {
                    array2 = this.CM5func(this.buf, 0, this.mac);
                }
                this.gost28147MacFunc(this.workingKey, array2, 0, this.mac, 0);
                this.bufOff = 0;
                int n6 = n2;
                n2 = n;
                int blockSize2 = n3;
                while (true) {
                    n = n6 - blockSize2;
                    n2 = (n4 = n2 + blockSize2);
                    n5 = n;
                    if (n <= this.blockSize) {
                        break;
                    }
                    this.gost28147MacFunc(this.workingKey, this.CM5func(array, n2, this.mac), 0, this.mac, 0);
                    blockSize2 = this.blockSize;
                    n6 = n;
                }
            }
            System.arraycopy(array, n4, this.buf, this.bufOff, n5);
            this.bufOff += n5;
            return;
        }
        throw new IllegalArgumentException("Can't have a negative input length!");
    }
}
