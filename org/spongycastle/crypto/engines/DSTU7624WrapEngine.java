package org.spongycastle.crypto.engines;

import java.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class DSTU7624WrapEngine implements Wrapper
{
    private static final int BYTES_IN_INTEGER = 4;
    private byte[] B;
    private ArrayList<byte[]> Btemp;
    private byte[] checkSumArray;
    private DSTU7624Engine engine;
    private boolean forWrapping;
    private byte[] intArray;
    private byte[] zeroArray;
    
    public DSTU7624WrapEngine(final int n) {
        final DSTU7624Engine engine = new DSTU7624Engine(n);
        this.engine = engine;
        this.B = new byte[engine.getBlockSize() / 2];
        this.checkSumArray = new byte[this.engine.getBlockSize()];
        this.zeroArray = new byte[this.engine.getBlockSize()];
        this.Btemp = new ArrayList<byte[]>();
        this.intArray = new byte[4];
    }
    
    private void intToBytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >> 24);
        array[n2 + 2] = (byte)(n >> 16);
        array[n2 + 1] = (byte)(n >> 8);
        array[n2] = (byte)n;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DSTU7624WrapEngine";
    }
    
    @Override
    public void init(final boolean forWrapping, final CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        this.forWrapping = forWrapping;
        if (parameters instanceof KeyParameter) {
            this.engine.init(forWrapping, parameters);
            return;
        }
        throw new IllegalArgumentException("invalid parameters passed to DSTU7624WrapEngine");
    }
    
    @Override
    public byte[] unwrap(byte[] array, int i, final int n) throws InvalidCipherTextException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        if (n % this.engine.getBlockSize() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unwrap data must be a multiple of ");
            sb.append(this.engine.getBlockSize());
            sb.append(" bytes");
            throw new DataLengthException(sb.toString());
        }
        final int n2 = n * 2 / this.engine.getBlockSize();
        final int n3 = n2 - 1;
        final int n4 = n3 * 6;
        final byte[] array2 = new byte[n];
        System.arraycopy(array, i, array2, 0, n);
        array = new byte[this.engine.getBlockSize() / 2];
        System.arraycopy(array2, 0, array, 0, this.engine.getBlockSize() / 2);
        this.Btemp.clear();
        int j;
        byte[] array3;
        for (j = n - this.engine.getBlockSize() / 2, i = this.engine.getBlockSize() / 2; j != 0; j -= this.engine.getBlockSize() / 2, i += this.engine.getBlockSize() / 2) {
            array3 = new byte[this.engine.getBlockSize() / 2];
            System.arraycopy(array2, i, array3, 0, this.engine.getBlockSize() / 2);
            this.Btemp.add(array3);
        }
        int k;
        int n5;
        int l;
        ArrayList<byte[]> btemp;
        int n6;
        for (i = 0; i < n4; ++i) {
            System.arraycopy(this.Btemp.get(n2 - 2), 0, array2, 0, this.engine.getBlockSize() / 2);
            System.arraycopy(array, 0, array2, this.engine.getBlockSize() / 2, this.engine.getBlockSize() / 2);
            this.intToBytes(n4 - i, this.intArray, 0);
            for (k = 0; k < 4; ++k) {
                n5 = this.engine.getBlockSize() / 2 + k;
                array2[n5] ^= this.intArray[k];
            }
            this.engine.processBlock(array2, 0, array2, 0);
            System.arraycopy(array2, 0, array, 0, this.engine.getBlockSize() / 2);
            for (l = 2; l < n2; ++l) {
                btemp = this.Btemp;
                n6 = n2 - l;
                System.arraycopy(btemp.get(n6 - 1), 0, this.Btemp.get(n6), 0, this.engine.getBlockSize() / 2);
            }
            System.arraycopy(array2, this.engine.getBlockSize() / 2, this.Btemp.get(0), 0, this.engine.getBlockSize() / 2);
        }
        System.arraycopy(array, 0, array2, 0, this.engine.getBlockSize() / 2);
        int n7 = this.engine.getBlockSize() / 2;
        for (i = 0; i < n3; ++i) {
            System.arraycopy(this.Btemp.get(i), 0, array2, n7, this.engine.getBlockSize() / 2);
            n7 += this.engine.getBlockSize() / 2;
        }
        System.arraycopy(array2, n - this.engine.getBlockSize(), this.checkSumArray, 0, this.engine.getBlockSize());
        array = new byte[n - this.engine.getBlockSize()];
        if (Arrays.areEqual(this.checkSumArray, this.zeroArray)) {
            System.arraycopy(array2, 0, array, 0, n - this.engine.getBlockSize());
            return array;
        }
        throw new InvalidCipherTextException("checksum failed");
    }
    
    @Override
    public byte[] wrap(byte[] array, int i, int j) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        if (j % this.engine.getBlockSize() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("wrap data must be a multiple of ");
            sb.append(this.engine.getBlockSize());
            sb.append(" bytes");
            throw new DataLengthException(sb.toString());
        }
        if (i + j <= array.length) {
            final int n = (j / this.engine.getBlockSize() + 1) * 2;
            final int n2 = n - 1;
            final int n3 = this.engine.getBlockSize() + j;
            final byte[] array2 = new byte[n3];
            System.arraycopy(array, i, array2, 0, j);
            System.arraycopy(array2, 0, this.B, 0, this.engine.getBlockSize() / 2);
            this.Btemp.clear();
            for (j = n3 - this.engine.getBlockSize() / 2, i = this.engine.getBlockSize() / 2; j != 0; j -= this.engine.getBlockSize() / 2, i += this.engine.getBlockSize() / 2) {
                array = new byte[this.engine.getBlockSize() / 2];
                System.arraycopy(array2, i, array, 0, this.engine.getBlockSize() / 2);
                this.Btemp.add(array);
            }
            int n4;
            for (i = 0; i < n2 * 6; i = j) {
                System.arraycopy(this.B, 0, array2, 0, this.engine.getBlockSize() / 2);
                System.arraycopy(this.Btemp.get(0), 0, array2, this.engine.getBlockSize() / 2, this.engine.getBlockSize() / 2);
                this.engine.processBlock(array2, 0, array2, 0);
                j = i + 1;
                this.intToBytes(j, this.intArray, 0);
                for (i = 0; i < 4; ++i) {
                    n4 = this.engine.getBlockSize() / 2 + i;
                    array2[n4] ^= this.intArray[i];
                }
                System.arraycopy(array2, this.engine.getBlockSize() / 2, this.B, 0, this.engine.getBlockSize() / 2);
                for (i = 2; i < n; ++i) {
                    System.arraycopy(this.Btemp.get(i - 1), 0, this.Btemp.get(i - 2), 0, this.engine.getBlockSize() / 2);
                }
                System.arraycopy(array2, 0, this.Btemp.get(n - 2), 0, this.engine.getBlockSize() / 2);
            }
            System.arraycopy(this.B, 0, array2, 0, this.engine.getBlockSize() / 2);
            j = this.engine.getBlockSize() / 2;
            for (i = 0; i < n2; ++i) {
                System.arraycopy(this.Btemp.get(i), 0, array2, j, this.engine.getBlockSize() / 2);
                j += this.engine.getBlockSize() / 2;
            }
            return array2;
        }
        throw new DataLengthException("input buffer too short");
    }
}
