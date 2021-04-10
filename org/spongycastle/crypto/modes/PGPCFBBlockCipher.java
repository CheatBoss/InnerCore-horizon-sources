package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class PGPCFBBlockCipher implements BlockCipher
{
    private byte[] FR;
    private byte[] FRE;
    private byte[] IV;
    private int blockSize;
    private BlockCipher cipher;
    private int count;
    private boolean forEncryption;
    private boolean inlineIv;
    private byte[] tmp;
    
    public PGPCFBBlockCipher(final BlockCipher cipher, final boolean inlineIv) {
        this.cipher = cipher;
        this.inlineIv = inlineIv;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.IV = new byte[blockSize];
        this.FR = new byte[blockSize];
        this.FRE = new byte[blockSize];
        this.tmp = new byte[blockSize];
    }
    
    private int decryptBlock(final byte[] array, final int n, final byte[] array2, int blockSize) throws DataLengthException, IllegalStateException {
        final int blockSize2 = this.blockSize;
        if (n + blockSize2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (blockSize2 + blockSize <= array2.length) {
            final BlockCipher cipher = this.cipher;
            final byte[] fr = this.FR;
            final byte[] fre = this.FRE;
            final int n2 = 0;
            cipher.processBlock(fr, 0, fre, 0);
            int n3 = 0;
            int n4;
            while (true) {
                n4 = n2;
                if (n3 >= this.blockSize) {
                    break;
                }
                array2[blockSize + n3] = this.encryptByte(array[n + n3], n3);
                ++n3;
            }
            while (true) {
                blockSize = this.blockSize;
                if (n4 >= blockSize) {
                    break;
                }
                this.FR[n4] = array[n + n4];
                ++n4;
            }
            return blockSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    private int decryptBlockWithIV(byte[] array, int blockSize, final byte[] array2, int i) throws DataLengthException, IllegalStateException {
        final int blockSize2 = this.blockSize;
        if (blockSize + blockSize2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i + blockSize2 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        final int count = this.count;
        if (count == 0) {
            for (i = 0; i < this.blockSize; ++i) {
                this.FR[i] = array[blockSize + i];
            }
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            this.count += this.blockSize;
            return 0;
        }
        if (count == blockSize2) {
            System.arraycopy(array, blockSize, this.tmp, 0, blockSize2);
            array = this.FR;
            System.arraycopy(array, 2, array, 0, this.blockSize - 2);
            array = this.FR;
            blockSize = this.blockSize;
            final byte[] tmp = this.tmp;
            array[blockSize - 2] = tmp[0];
            array[blockSize - 1] = tmp[1];
            this.cipher.processBlock(array, 0, this.FRE, 0);
            blockSize = 0;
            int n;
            while (true) {
                n = this.blockSize - 2;
                if (blockSize >= n) {
                    break;
                }
                array2[i + blockSize] = this.encryptByte(this.tmp[blockSize + 2], blockSize);
                ++blockSize;
            }
            System.arraycopy(this.tmp, 2, this.FR, 0, n);
            this.count += 2;
            return this.blockSize - 2;
        }
        if (count >= blockSize2 + 2) {
            System.arraycopy(array, blockSize, this.tmp, 0, blockSize2);
            array2[i + 0] = this.encryptByte(this.tmp[0], this.blockSize - 2);
            array2[i + 1] = this.encryptByte(this.tmp[1], this.blockSize - 1);
            System.arraycopy(this.tmp, 0, this.FR, this.blockSize - 2, 2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            blockSize = 0;
            int n2;
            while (true) {
                n2 = this.blockSize - 2;
                if (blockSize >= n2) {
                    break;
                }
                array2[i + blockSize + 2] = this.encryptByte(this.tmp[blockSize + 2], blockSize);
                ++blockSize;
            }
            System.arraycopy(this.tmp, 2, this.FR, 0, n2);
        }
        return this.blockSize;
    }
    
    private int encryptBlock(final byte[] array, int blockSize, final byte[] array2, final int n) throws DataLengthException, IllegalStateException {
        final int blockSize2 = this.blockSize;
        if (blockSize + blockSize2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (blockSize2 + n <= array2.length) {
            final BlockCipher cipher = this.cipher;
            final byte[] fr = this.FR;
            final byte[] fre = this.FRE;
            final int n2 = 0;
            cipher.processBlock(fr, 0, fre, 0);
            int n3 = 0;
            int n4;
            while (true) {
                n4 = n2;
                if (n3 >= this.blockSize) {
                    break;
                }
                array2[n + n3] = this.encryptByte(array[blockSize + n3], n3);
                ++n3;
            }
            while (true) {
                blockSize = this.blockSize;
                if (n4 >= blockSize) {
                    break;
                }
                this.FR[n4] = array2[n + n4];
                ++n4;
            }
            return blockSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    private int encryptBlockWithIV(final byte[] array, int count, final byte[] array2, int n) throws DataLengthException, IllegalStateException {
        final int blockSize = this.blockSize;
        if (count + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        final int count2 = this.count;
        if (count2 != 0) {
            if (count2 >= blockSize + 2) {
                if (blockSize + n > array2.length) {
                    throw new OutputLengthException("output buffer too short");
                }
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                int n2 = 0;
                int blockSize2;
                while (true) {
                    blockSize2 = this.blockSize;
                    if (n2 >= blockSize2) {
                        break;
                    }
                    array2[n + n2] = this.encryptByte(array[count + n2], n2);
                    ++n2;
                }
                System.arraycopy(array2, n, this.FR, 0, blockSize2);
            }
            return this.blockSize;
        }
        if (blockSize * 2 + n + 2 <= array2.length) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            int n3 = 0;
            int blockSize3;
            while (true) {
                blockSize3 = this.blockSize;
                if (n3 >= blockSize3) {
                    break;
                }
                array2[n + n3] = this.encryptByte(this.IV[n3], n3);
                ++n3;
            }
            System.arraycopy(array2, n, this.FR, 0, blockSize3);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            final int blockSize4 = this.blockSize;
            array2[n + blockSize4] = this.encryptByte(this.IV[blockSize4 - 2], 0);
            final int blockSize5 = this.blockSize;
            array2[n + blockSize5 + 1] = this.encryptByte(this.IV[blockSize5 - 1], 1);
            System.arraycopy(array2, n + 2, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            int n4 = 0;
            int blockSize6;
            while (true) {
                blockSize6 = this.blockSize;
                if (n4 >= blockSize6) {
                    break;
                }
                array2[blockSize6 + n + 2 + n4] = this.encryptByte(array[count + n4], n4);
                ++n4;
            }
            System.arraycopy(array2, n + blockSize6 + 2, this.FR, 0, blockSize6);
            count = this.count;
            n = this.blockSize * 2 + 2;
            this.count = count + n;
            return n;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    private byte encryptByte(final byte b, final int n) {
        return (byte)(b ^ this.FRE[n]);
    }
    
    @Override
    public String getAlgorithmName() {
        StringBuilder sb;
        String s;
        if (this.inlineIv) {
            sb = new StringBuilder();
            sb.append(this.cipher.getAlgorithmName());
            s = "/PGPCFBwithIV";
        }
        else {
            sb = new StringBuilder();
            sb.append(this.cipher.getAlgorithmName());
            s = "/PGPCFB";
        }
        sb.append(s);
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean forEncryption, CipherParameters parameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        BlockCipher blockCipher;
        if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
            final byte[] iv = parametersWithIV.getIV();
            final int length = iv.length;
            final byte[] iv2 = this.IV;
            if (length < iv2.length) {
                System.arraycopy(iv, 0, iv2, iv2.length - iv.length, iv.length);
                int n = 0;
                while (true) {
                    final byte[] iv3 = this.IV;
                    if (n >= iv3.length - iv.length) {
                        break;
                    }
                    iv3[n] = 0;
                    ++n;
                }
            }
            else {
                System.arraycopy(iv, 0, iv2, 0, iv2.length);
            }
            this.reset();
            blockCipher = this.cipher;
            parameters = parametersWithIV.getParameters();
        }
        else {
            this.reset();
            blockCipher = this.cipher;
        }
        blockCipher.init(true, parameters);
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (this.inlineIv) {
            if (this.forEncryption) {
                return this.encryptBlockWithIV(array, n, array2, n2);
            }
            return this.decryptBlockWithIV(array, n, array2, n2);
        }
        else {
            if (this.forEncryption) {
                return this.encryptBlock(array, n, array2, n2);
            }
            return this.decryptBlock(array, n, array2, n2);
        }
    }
    
    @Override
    public void reset() {
        this.count = 0;
        int n = 0;
        while (true) {
            final byte[] fr = this.FR;
            if (n == fr.length) {
                break;
            }
            if (this.inlineIv) {
                fr[n] = 0;
            }
            else {
                fr[n] = this.IV[n];
            }
            ++n;
        }
        this.cipher.reset();
    }
}
