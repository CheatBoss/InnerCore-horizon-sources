package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class DSTU7564Digest implements ExtendedDigest, Memoable
{
    private static final int BITS_IN_BYTE = 8;
    private static final int NB_1024 = 16;
    private static final int NB_512 = 8;
    private static final int NR_1024 = 14;
    private static final int NR_512 = 10;
    private static final int REDUCTIONAL_POLYNOMIAL = 285;
    private static final int ROWS = 8;
    private static final int STATE_BYTES_SIZE_1024 = 128;
    private static final int STATE_BYTES_SIZE_512 = 64;
    private static final byte[][] mds_matrix;
    private static final byte[][] sBoxes;
    private int blockSize;
    private byte[] buf;
    private int bufOff;
    private int columns;
    private int hashSize;
    private long inputLength;
    private byte[] mixColumnsResult;
    private byte[] padded;
    private int rounds;
    private byte[][] state;
    private byte[] tempBuffer;
    private long[] tempLongBuffer;
    private byte[][] tempState1;
    private byte[][] tempState2;
    
    static {
        mds_matrix = new byte[][] { { 1, 1, 5, 1, 8, 6, 7, 4 }, { 4, 1, 1, 5, 1, 8, 6, 7 }, { 7, 4, 1, 1, 5, 1, 8, 6 }, { 6, 7, 4, 1, 1, 5, 1, 8 }, { 8, 6, 7, 4, 1, 1, 5, 1 }, { 1, 8, 6, 7, 4, 1, 1, 5 }, { 5, 1, 8, 6, 7, 4, 1, 1 }, { 1, 5, 1, 8, 6, 7, 4, 1 } };
        sBoxes = new byte[][] { { -88, 67, 95, 6, 107, 117, 108, 89, 113, -33, -121, -107, 23, -16, -40, 9, 109, -13, 29, -53, -55, 77, 44, -81, 121, -32, -105, -3, 111, 75, 69, 57, 62, -35, -93, 79, -76, -74, -102, 14, 31, -65, 21, -31, 73, -46, -109, -58, -110, 114, -98, 97, -47, 99, -6, -18, -12, 25, -43, -83, 88, -92, -69, -95, -36, -14, -125, 55, 66, -28, 122, 50, -100, -52, -85, 74, -113, 110, 4, 39, 46, -25, -30, 90, -106, 22, 35, 43, -62, 101, 102, 15, -68, -87, 71, 65, 52, 72, -4, -73, 106, -120, -91, 83, -122, -7, 91, -37, 56, 123, -61, 30, 34, 51, 36, 40, 54, -57, -78, 59, -114, 119, -70, -11, 20, -97, 8, 85, -101, 76, -2, 96, 92, -38, 24, 70, -51, 125, 33, -80, 63, 27, -119, -1, -21, -124, 105, 58, -99, -41, -45, 112, 103, 64, -75, -34, 93, 48, -111, -79, 120, 17, 1, -27, 0, 104, -104, -96, -59, 2, -90, 116, 45, 11, -94, 118, -77, -66, -50, -67, -82, -23, -118, 49, 28, -20, -15, -103, -108, -86, -10, 38, 47, -17, -24, -116, 53, 3, -44, 127, -5, 5, -63, 94, -112, 32, 61, -126, -9, -22, 10, 13, 126, -8, 80, 26, -60, 7, 87, -72, 60, 98, -29, -56, -84, 82, 100, 16, -48, -39, 19, 12, 18, 41, 81, -71, -49, -42, 115, -115, -127, 84, -64, -19, 78, 68, -89, 42, -123, 37, -26, -54, 124, -117, 86, -128 }, { -50, -69, -21, -110, -22, -53, 19, -63, -23, 58, -42, -78, -46, -112, 23, -8, 66, 21, 86, -76, 101, 28, -120, 67, -59, 92, 54, -70, -11, 87, 103, -115, 49, -10, 100, 88, -98, -12, 34, -86, 117, 15, 2, -79, -33, 109, 115, 77, 124, 38, 46, -9, 8, 93, 68, 62, -97, 20, -56, -82, 84, 16, -40, -68, 26, 107, 105, -13, -67, 51, -85, -6, -47, -101, 104, 78, 22, -107, -111, -18, 76, 99, -114, 91, -52, 60, 25, -95, -127, 73, 123, -39, 111, 55, 96, -54, -25, 43, 72, -3, -106, 69, -4, 65, 18, 13, 121, -27, -119, -116, -29, 32, 48, -36, -73, 108, 74, -75, 63, -105, -44, 98, 45, 6, -92, -91, -125, 95, 42, -38, -55, 0, 126, -94, 85, -65, 17, -43, -100, -49, 14, 10, 61, 81, 125, -109, 27, -2, -60, 71, 9, -122, 11, -113, -99, 106, 7, -71, -80, -104, 24, 50, 113, 75, -17, 59, 112, -96, -28, 64, -1, -61, -87, -26, 120, -7, -117, 70, -128, 30, 56, -31, -72, -88, -32, 12, 35, 118, 29, 37, 36, 5, -15, 110, -108, 40, -102, -124, -24, -93, 79, 119, -45, -123, -30, 82, -14, -126, 80, 122, 47, 116, 83, -77, 97, -81, 57, 53, -34, -51, 31, -103, -84, -83, 114, 44, -35, -48, -121, -66, 94, -90, -20, 4, -58, 3, 52, -5, -37, 89, -74, -62, 1, -16, 90, -19, -89, 102, 33, 127, -118, 39, -57, -64, 41, -41 }, { -109, -39, -102, -75, -104, 34, 69, -4, -70, 106, -33, 2, -97, -36, 81, 89, 74, 23, 43, -62, -108, -12, -69, -93, 98, -28, 113, -44, -51, 112, 22, -31, 73, 60, -64, -40, 92, -101, -83, -123, 83, -95, 122, -56, 45, -32, -47, 114, -90, 44, -60, -29, 118, 120, -73, -76, 9, 59, 14, 65, 76, -34, -78, -112, 37, -91, -41, 3, 17, 0, -61, 46, -110, -17, 78, 18, -99, 125, -53, 53, 16, -43, 79, -98, 77, -87, 85, -58, -48, 123, 24, -105, -45, 54, -26, 72, 86, -127, -113, 119, -52, -100, -71, -30, -84, -72, 47, 21, -92, 124, -38, 56, 30, 11, 5, -42, 20, 110, 108, 126, 102, -3, -79, -27, 96, -81, 94, 51, -121, -55, -16, 93, 109, 63, -120, -115, -57, -9, 29, -23, -20, -19, -128, 41, 39, -49, -103, -88, 80, 15, 55, 36, 40, 48, -107, -46, 62, 91, 64, -125, -77, 105, 87, 31, 7, 28, -118, -68, 32, -21, -50, -114, -85, -18, 49, -94, 115, -7, -54, 58, 26, -5, 13, -63, -2, -6, -14, 111, -67, -106, -35, 67, 82, -74, 8, -13, -82, -66, 25, -119, 50, 38, -80, -22, 75, 100, -124, -126, 107, -11, 121, -65, 1, 95, 117, 99, 27, 35, 61, 104, 42, 101, -24, -111, -10, -1, 19, 88, -15, 71, 10, 127, -59, -89, -25, 97, 90, 6, 70, 68, 66, 4, -96, -37, 57, -122, 84, -86, -116, 52, 33, -117, -8, 12, 116, 103 }, { 104, -115, -54, 77, 115, 75, 78, 42, -44, 82, 38, -77, 84, 30, 25, 31, 34, 3, 70, 61, 45, 74, 83, -125, 19, -118, -73, -43, 37, 121, -11, -67, 88, 47, 13, 2, -19, 81, -98, 17, -14, 62, 85, 94, -47, 22, 60, 102, 112, 93, -13, 69, 64, -52, -24, -108, 86, 8, -50, 26, 58, -46, -31, -33, -75, 56, 110, 14, -27, -12, -7, -122, -23, 79, -42, -123, 35, -49, 50, -103, 49, 20, -82, -18, -56, 72, -45, 48, -95, -110, 65, -79, 24, -60, 44, 113, 114, 68, 21, -3, 55, -66, 95, -86, -101, -120, -40, -85, -119, -100, -6, 96, -22, -68, 98, 12, 36, -90, -88, -20, 103, 32, -37, 124, 40, -35, -84, 91, 52, 126, 16, -15, 123, -113, 99, -96, 5, -102, 67, 119, 33, -65, 39, 9, -61, -97, -74, -41, 41, -62, -21, -64, -92, -117, -116, 29, -5, -1, -63, -78, -105, 46, -8, 101, -10, 117, 7, 4, 73, 51, -28, -39, -71, -48, 66, -57, 108, -112, 0, -114, 111, 80, 1, -59, -38, 71, 63, -51, 105, -94, -30, 122, -89, -58, -109, 15, 10, 6, -26, 43, -106, -93, 28, -81, 106, 18, -124, 57, -25, -80, -126, -9, -2, -99, -121, 92, -127, 53, -34, -76, -91, -4, -128, -17, -53, -69, 107, 118, -70, 90, 125, 120, 11, -107, -29, -83, 116, -104, 59, 54, 100, 109, -36, -16, 89, -87, 76, 23, 127, -111, -72, -55, 87, 27, -32, 97 } };
    }
    
    public DSTU7564Digest(int i) {
        if (i != 256 && i != 384 && i != 512) {
            throw new IllegalArgumentException("Hash size is not recommended. Use 256/384/512 instead");
        }
        this.hashSize = i / 8;
        byte[][] state;
        if (i > 256) {
            this.blockSize = 128;
            this.columns = 16;
            this.rounds = 14;
            state = new byte[128][];
        }
        else {
            this.blockSize = 64;
            this.columns = 8;
            this.rounds = 10;
            state = new byte[64][];
        }
        this.state = state;
        final int n = 0;
        i = 0;
        byte[][] state2;
        while (true) {
            state2 = this.state;
            if (i >= state2.length) {
                break;
            }
            state2[i] = new byte[this.columns];
            ++i;
        }
        state2[0][0] = (byte)state2.length;
        this.padded = null;
        this.tempState1 = new byte[128][];
        this.tempState2 = new byte[128][];
        for (i = n; i < this.state.length; ++i) {
            this.tempState1[i] = new byte[8];
            this.tempState2[i] = new byte[8];
        }
        this.tempBuffer = new byte[16];
        this.mixColumnsResult = new byte[8];
        this.tempLongBuffer = new long[this.columns];
        this.buf = new byte[this.blockSize];
    }
    
    public DSTU7564Digest(final DSTU7564Digest dstu7564Digest) {
        this.copyIn(dstu7564Digest);
    }
    
    private void P() {
        for (int i = 0; i < this.rounds; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                final byte[] array = this.tempState1[j];
                array[0] ^= (byte)(j * 16 ^ i);
            }
            for (int k = 0; k < 8; ++k) {
                for (int l = 0; l < this.columns; ++l) {
                    final byte[][] tempState1 = this.tempState1;
                    tempState1[l][k] = DSTU7564Digest.sBoxes[k % 4][tempState1[l][k] & 0xFF];
                }
            }
            int n = 0;
            int n2 = -1;
            while (n < 8) {
                if (n == 7 && this.columns == 16) {
                    n2 = 11;
                }
                else {
                    ++n2;
                }
                int n3 = 0;
                while (true) {
                    final int columns = this.columns;
                    if (n3 >= columns) {
                        break;
                    }
                    this.tempBuffer[(n3 + n2) % columns] = this.tempState1[n3][n];
                    ++n3;
                }
                for (int n4 = 0; n4 < this.columns; ++n4) {
                    this.tempState1[n4][n] = this.tempBuffer[n4];
                }
                ++n;
            }
            for (int n5 = 0; n5 < this.columns; ++n5) {
                Arrays.fill(this.mixColumnsResult, (byte)0);
                for (int n6 = 7; n6 >= 0; --n6) {
                    int n7 = 7;
                    byte b = 0;
                    while (n7 >= 0) {
                        b ^= this.multiplyGF(this.tempState1[n5][n7], DSTU7564Digest.mds_matrix[n6][n7]);
                        --n7;
                    }
                    this.mixColumnsResult[n6] = b;
                }
                for (int n8 = 0; n8 < 8; ++n8) {
                    this.tempState1[n5][n8] = this.mixColumnsResult[n8];
                }
            }
        }
    }
    
    private void Q() {
        for (int i = 0; i < this.rounds; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                this.tempLongBuffer[j] = Pack.littleEndianToLong(this.tempState2[j], 0);
                final long[] tempLongBuffer = this.tempLongBuffer;
                Pack.longToLittleEndian(tempLongBuffer[j] += (((this.columns - j - 1) * 16L ^ (long)i) << 56 ^ 0xF0F0F0F0F0F0F3L), this.tempState2[j], 0);
            }
            for (int k = 0; k < 8; ++k) {
                for (int l = 0; l < this.columns; ++l) {
                    final byte[][] tempState2 = this.tempState2;
                    tempState2[l][k] = DSTU7564Digest.sBoxes[k % 4][tempState2[l][k] & 0xFF];
                }
            }
            int n = 0;
            int n2 = -1;
            while (n < 8) {
                if (n == 7 && this.columns == 16) {
                    n2 = 11;
                }
                else {
                    ++n2;
                }
                int n3 = 0;
                while (true) {
                    final int columns = this.columns;
                    if (n3 >= columns) {
                        break;
                    }
                    this.tempBuffer[(n3 + n2) % columns] = this.tempState2[n3][n];
                    ++n3;
                }
                for (int n4 = 0; n4 < this.columns; ++n4) {
                    this.tempState2[n4][n] = this.tempBuffer[n4];
                }
                ++n;
            }
            for (int n5 = 0; n5 < this.columns; ++n5) {
                Arrays.fill(this.mixColumnsResult, (byte)0);
                for (int n6 = 7; n6 >= 0; --n6) {
                    int n7 = 7;
                    byte b = 0;
                    while (n7 >= 0) {
                        b ^= this.multiplyGF(this.tempState2[n5][n7], DSTU7564Digest.mds_matrix[n6][n7]);
                        --n7;
                    }
                    this.mixColumnsResult[n6] = b;
                }
                for (int n8 = 0; n8 < 8; ++n8) {
                    this.tempState2[n5][n8] = this.mixColumnsResult[n8];
                }
            }
        }
    }
    
    private void copyIn(final DSTU7564Digest dstu7564Digest) {
        this.hashSize = dstu7564Digest.hashSize;
        this.blockSize = dstu7564Digest.blockSize;
        this.columns = dstu7564Digest.columns;
        this.rounds = dstu7564Digest.rounds;
        this.padded = Arrays.clone(dstu7564Digest.padded);
        this.state = Arrays.clone(dstu7564Digest.state);
        this.tempState1 = Arrays.clone(dstu7564Digest.tempState1);
        this.tempState2 = Arrays.clone(dstu7564Digest.tempState2);
        this.tempBuffer = Arrays.clone(dstu7564Digest.tempBuffer);
        this.mixColumnsResult = Arrays.clone(dstu7564Digest.mixColumnsResult);
        this.tempLongBuffer = Arrays.clone(dstu7564Digest.tempLongBuffer);
        this.inputLength = dstu7564Digest.inputLength;
        this.bufOff = dstu7564Digest.bufOff;
        this.buf = Arrays.clone(dstu7564Digest.buf);
    }
    
    private byte multiplyGF(byte b, byte b2) {
        int i = 0;
        byte b3 = 0;
        while (i < 8) {
            byte b4 = b3;
            if ((b2 & 0x1) == 0x1) {
                b4 = (byte)(b3 ^ b);
            }
            final byte b5 = (byte)(b & 0xFFFFFF80);
            final byte b6 = b <<= 1;
            if (b5 == -128) {
                b = (byte)(b6 ^ 0x11D);
            }
            b2 >>= 1;
            ++i;
            b3 = b4;
        }
        return b3;
    }
    
    private byte[] pad(final byte[] array, final int n, final int n2) {
        int blockSize;
        final int n3 = blockSize = this.blockSize;
        if (n3 - n2 < 13) {
            blockSize = n3 * 2;
        }
        final byte[] array2 = new byte[blockSize];
        System.arraycopy(array, n, array2, 0, n2);
        array2[n2] = -128;
        Pack.longToLittleEndian(this.inputLength * 8L, array2, array2.length - 12);
        return array2;
    }
    
    private void processBlock(byte[] array, int i) {
        for (int j = 0; j < this.state.length; ++j) {
            Arrays.fill(this.tempState1[j], (byte)0);
            Arrays.fill(this.tempState2[j], (byte)0);
        }
        for (int k = 0; k < 8; ++k) {
            for (int l = 0; l < this.columns; ++l) {
                final byte[] array2 = this.tempState1[l];
                final byte b = this.state[l][k];
                final int n = l * 8 + k + i;
                array2[k] = (byte)(b ^ array[n]);
                this.tempState2[l][k] = array[n];
            }
        }
        this.P();
        this.Q();
        int n2;
        for (i = 0; i < 8; ++i) {
            for (n2 = 0; n2 < this.columns; ++n2) {
                array = this.state[n2];
                array[i] ^= (byte)(this.tempState1[n2][i] ^ this.tempState2[n2][i]);
            }
        }
    }
    
    @Override
    public Memoable copy() {
        return new DSTU7564Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] pad = this.pad(this.buf, 0, this.bufOff);
        this.padded = pad;
        int i = pad.length;
        int n2 = 0;
        while (i != 0) {
            this.processBlock(this.padded, n2);
            final int blockSize = this.blockSize;
            n2 += blockSize;
            i -= blockSize;
        }
        final byte[][] array2 = new byte[128][];
        int n3 = 0;
        while (true) {
            final byte[][] state = this.state;
            if (n3 >= state.length) {
                break;
            }
            array2[n3] = new byte[8];
            System.arraycopy(state[n3], 0, array2[n3], 0, 8);
            ++n3;
        }
        for (int j = 0; j < this.rounds; ++j) {
            for (int k = 0; k < this.columns; ++k) {
                final byte[] array3 = array2[k];
                array3[0] ^= (byte)(k * 16 ^ j);
            }
            for (int l = 0; l < 8; ++l) {
                for (int n4 = 0; n4 < this.columns; ++n4) {
                    array2[n4][l] = DSTU7564Digest.sBoxes[l % 4][array2[n4][l] & 0xFF];
                }
            }
            int n5 = 0;
            int n6 = -1;
            while (n5 < 8) {
                if (n5 == 7 && this.columns == 16) {
                    n6 = 11;
                }
                else {
                    ++n6;
                }
                int n7 = 0;
                while (true) {
                    final int columns = this.columns;
                    if (n7 >= columns) {
                        break;
                    }
                    this.tempBuffer[(n7 + n6) % columns] = array2[n7][n5];
                    ++n7;
                }
                for (int n8 = 0; n8 < this.columns; ++n8) {
                    array2[n8][n5] = this.tempBuffer[n8];
                }
                ++n5;
            }
            for (int n9 = 0; n9 < this.columns; ++n9) {
                Arrays.fill(this.mixColumnsResult, (byte)0);
                for (int n10 = 7; n10 >= 0; --n10) {
                    int n11 = 7;
                    byte b = 0;
                    while (n11 >= 0) {
                        b ^= this.multiplyGF(array2[n9][n11], DSTU7564Digest.mds_matrix[n10][n11]);
                        --n11;
                    }
                    this.mixColumnsResult[n10] = b;
                }
                for (int n12 = 0; n12 < 8; ++n12) {
                    array2[n9][n12] = this.mixColumnsResult[n12];
                }
            }
        }
        for (int n13 = 0; n13 < 8; ++n13) {
            for (int n14 = 0; n14 < this.columns; ++n14) {
                final byte[] array4 = this.state[n14];
                array4[n13] ^= array2[n14][n13];
            }
        }
        final int n15 = this.columns * 8;
        final byte[] array5 = new byte[n15];
        int n16 = 0;
        int n17 = 0;
        while (n16 < this.columns) {
            for (int n18 = 0; n18 < 8; ++n18) {
                array5[n17] = this.state[n16][n18];
                ++n17;
            }
            ++n16;
        }
        final int hashSize = this.hashSize;
        System.arraycopy(array5, n15 - hashSize, array, n, hashSize);
        this.reset();
        return this.hashSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DSTU7564";
    }
    
    @Override
    public int getByteLength() {
        return this.blockSize;
    }
    
    @Override
    public int getDigestSize() {
        return this.hashSize;
    }
    
    @Override
    public void reset() {
        int n = 0;
        byte[][] state;
        while (true) {
            state = this.state;
            if (n >= state.length) {
                break;
            }
            state[n] = new byte[this.columns];
            ++n;
        }
        state[0][0] = (byte)state.length;
        this.inputLength = 0L;
        this.bufOff = 0;
        Arrays.fill(this.buf, (byte)0);
        final byte[] padded = this.padded;
        if (padded != null) {
            Arrays.fill(padded, (byte)0);
        }
    }
    
    @Override
    public void reset(final Memoable memoable) {
        this.copyIn((DSTU7564Digest)memoable);
    }
    
    @Override
    public void update(final byte b) {
        final byte[] buf = this.buf;
        final int bufOff = this.bufOff;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        buf[bufOff] = b;
        if (bufOff2 == this.blockSize) {
            this.processBlock(buf, 0);
            this.bufOff = 0;
        }
        ++this.inputLength;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        int n3;
        for (n3 = n2, n2 = n; this.bufOff != 0 && n3 > 0; --n3, ++n2) {
            this.update(array[n2]);
        }
        if (n3 > 0) {
            n = n3;
            int n4;
            int i;
            while (true) {
                n4 = n2;
                i = n;
                if (n <= this.blockSize) {
                    break;
                }
                this.processBlock(array, n2);
                final int blockSize = this.blockSize;
                n2 += blockSize;
                this.inputLength += blockSize;
                n -= blockSize;
            }
            while (i > 0) {
                this.update(array[n4]);
                --i;
                ++n4;
            }
        }
    }
}
