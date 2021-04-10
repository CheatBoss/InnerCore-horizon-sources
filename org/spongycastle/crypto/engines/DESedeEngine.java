package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class DESedeEngine extends DESEngine
{
    protected static final int BLOCK_SIZE = 8;
    private boolean forEncryption;
    private int[] workingKey1;
    private int[] workingKey2;
    private int[] workingKey3;
    
    public DESedeEngine() {
        this.workingKey1 = null;
        this.workingKey2 = null;
        this.workingKey3 = null;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DESede";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid parameter passed to DESede init - ");
            sb.append(cipherParameters.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        final byte[] key = ((KeyParameter)cipherParameters).getKey();
        if (key.length != 24 && key.length != 16) {
            throw new IllegalArgumentException("key size must be 16 or 24 bytes.");
        }
        this.forEncryption = forEncryption;
        final byte[] array = new byte[8];
        System.arraycopy(key, 0, array, 0, 8);
        this.workingKey1 = this.generateWorkingKey(forEncryption, array);
        final byte[] array2 = new byte[8];
        System.arraycopy(key, 8, array2, 0, 8);
        this.workingKey2 = this.generateWorkingKey(forEncryption ^ true, array2);
        int[] workingKey3;
        if (key.length == 24) {
            final byte[] array3 = new byte[8];
            System.arraycopy(key, 16, array3, 0, 8);
            workingKey3 = this.generateWorkingKey(forEncryption, array3);
        }
        else {
            workingKey3 = this.workingKey1;
        }
        this.workingKey3 = workingKey3;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] workingKey1 = this.workingKey1;
        if (workingKey1 == null) {
            throw new IllegalStateException("DESede engine not initialised");
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 <= array2.length) {
            final byte[] array3 = new byte[8];
            int[] array4;
            if (this.forEncryption) {
                this.desFunc(workingKey1, array, n, array3, 0);
                this.desFunc(this.workingKey2, array3, 0, array3, 0);
                array4 = this.workingKey3;
            }
            else {
                this.desFunc(this.workingKey3, array, n, array3, 0);
                this.desFunc(this.workingKey2, array3, 0, array3, 0);
                array4 = this.workingKey1;
            }
            this.desFunc(array4, array3, 0, array2, n2);
            return 8;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
