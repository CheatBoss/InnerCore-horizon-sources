package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class ThreefishEngine implements BlockCipher
{
    public static final int BLOCKSIZE_1024 = 1024;
    public static final int BLOCKSIZE_256 = 256;
    public static final int BLOCKSIZE_512 = 512;
    private static final long C_240 = 2004413935125273122L;
    private static final int MAX_ROUNDS = 80;
    private static int[] MOD17;
    private static int[] MOD3;
    private static int[] MOD5;
    private static int[] MOD9;
    private static final int ROUNDS_1024 = 80;
    private static final int ROUNDS_256 = 72;
    private static final int ROUNDS_512 = 72;
    private static final int TWEAK_SIZE_BYTES = 16;
    private static final int TWEAK_SIZE_WORDS = 2;
    private int blocksizeBytes;
    private int blocksizeWords;
    private ThreefishCipher cipher;
    private long[] currentBlock;
    private boolean forEncryption;
    private long[] kw;
    private long[] t;
    
    static {
        final int[] array = ThreefishEngine.MOD9 = new int[80];
        ThreefishEngine.MOD17 = new int[array.length];
        ThreefishEngine.MOD5 = new int[array.length];
        ThreefishEngine.MOD3 = new int[array.length];
        int n = 0;
        while (true) {
            final int[] mod9 = ThreefishEngine.MOD9;
            if (n >= mod9.length) {
                break;
            }
            ThreefishEngine.MOD17[n] = n % 17;
            mod9[n] = n % 9;
            ThreefishEngine.MOD5[n] = n % 5;
            ThreefishEngine.MOD3[n] = n % 3;
            ++n;
        }
    }
    
    public ThreefishEngine(final int n) {
        this.t = new long[5];
        final int blocksizeBytes = n / 8;
        this.blocksizeBytes = blocksizeBytes;
        final int blocksizeWords = blocksizeBytes / 8;
        this.blocksizeWords = blocksizeWords;
        this.currentBlock = new long[blocksizeWords];
        this.kw = new long[blocksizeWords * 2 + 1];
        ThreefishCipher cipher;
        if (n != 256) {
            if (n != 512) {
                if (n != 1024) {
                    throw new IllegalArgumentException("Invalid blocksize - Threefish is defined with block size of 256, 512, or 1024 bits");
                }
                cipher = new Threefish1024Cipher(this.kw, this.t);
            }
            else {
                cipher = new Threefish512Cipher(this.kw, this.t);
            }
        }
        else {
            cipher = new Threefish256Cipher(this.kw, this.t);
        }
        this.cipher = cipher;
    }
    
    public static long bytesToWord(final byte[] array, int n) {
        if (n + 8 <= array.length) {
            final int n2 = n + 1;
            final long n3 = array[n];
            n = n2 + 1;
            final long n4 = array[n2];
            final int n5 = n + 1;
            final long n6 = array[n];
            n = n5 + 1;
            final long n7 = array[n5];
            final int n8 = n + 1;
            final long n9 = array[n];
            n = n8 + 1;
            return ((long)array[n + 1] & 0xFFL) << 56 | ((n3 & 0xFFL) | (n4 & 0xFFL) << 8 | (n6 & 0xFFL) << 16 | (n7 & 0xFFL) << 24 | (n9 & 0xFFL) << 32 | ((long)array[n8] & 0xFFL) << 40 | ((long)array[n] & 0xFFL) << 48);
        }
        throw new IllegalArgumentException();
    }
    
    static long rotlXor(final long n, final int n2, final long n3) {
        return (n >>> -n2 | n << n2) ^ n3;
    }
    
    private void setKey(long[] kw) {
        if (kw.length == this.blocksizeWords) {
            long n = 2004413935125273122L;
            int n2 = 0;
            int blocksizeWords;
            while (true) {
                blocksizeWords = this.blocksizeWords;
                if (n2 >= blocksizeWords) {
                    break;
                }
                final long[] kw2 = this.kw;
                kw2[n2] = kw[n2];
                final long n3 = kw2[n2];
                ++n2;
                n ^= n3;
            }
            kw = this.kw;
            kw[blocksizeWords] = n;
            System.arraycopy(kw, 0, kw, blocksizeWords + 1, blocksizeWords);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Threefish key must be same size as block (");
        sb.append(this.blocksizeWords);
        sb.append(" words)");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void setTweak(final long[] array) {
        if (array.length == 2) {
            final long[] t = this.t;
            t[0] = array[0];
            t[1] = array[1];
            t[2] = (t[0] ^ t[1]);
            t[3] = t[0];
            t[4] = t[1];
            return;
        }
        throw new IllegalArgumentException("Tweak must be 2 words.");
    }
    
    public static void wordToBytes(final long n, final byte[] array, int n2) {
        if (n2 + 8 <= array.length) {
            final int n3 = n2 + 1;
            array[n2] = (byte)n;
            n2 = n3 + 1;
            array[n3] = (byte)(n >> 8);
            final int n4 = n2 + 1;
            array[n2] = (byte)(n >> 16);
            n2 = n4 + 1;
            array[n4] = (byte)(n >> 24);
            final int n5 = n2 + 1;
            array[n2] = (byte)(n >> 32);
            n2 = n5 + 1;
            array[n5] = (byte)(n >> 40);
            array[n2] = (byte)(n >> 48);
            array[n2 + 1] = (byte)(n >> 56);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    static long xorRotr(long n, final int n2, final long n3) {
        n ^= n3;
        return n << -n2 | n >>> n2;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Threefish-");
        sb.append(this.blocksizeBytes * 8);
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.blocksizeBytes;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        final boolean b2 = cipherParameters instanceof TweakableBlockCipherParameters;
        final long[] array = null;
        byte[] array2;
        byte[] tweak;
        if (b2) {
            final TweakableBlockCipherParameters tweakableBlockCipherParameters = (TweakableBlockCipherParameters)cipherParameters;
            array2 = tweakableBlockCipherParameters.getKey().getKey();
            tweak = tweakableBlockCipherParameters.getTweak();
        }
        else {
            if (!(cipherParameters instanceof KeyParameter)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid parameter passed to Threefish init - ");
                sb.append(cipherParameters.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            array2 = ((KeyParameter)cipherParameters).getKey();
            tweak = null;
        }
        long[] array4;
        if (array2 != null) {
            if (array2.length != this.blocksizeBytes) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Threefish key must be same size as block (");
                sb2.append(this.blocksizeBytes);
                sb2.append(" bytes)");
                throw new IllegalArgumentException(sb2.toString());
            }
            final int blocksizeWords = this.blocksizeWords;
            final long[] array3 = new long[blocksizeWords];
            int n = 0;
            while (true) {
                array4 = array3;
                if (n >= blocksizeWords) {
                    break;
                }
                array3[n] = bytesToWord(array2, n * 8);
                ++n;
            }
        }
        else {
            array4 = null;
        }
        long[] array5 = array;
        if (tweak != null) {
            if (tweak.length != 16) {
                throw new IllegalArgumentException("Threefish tweak must be 16 bytes");
            }
            array5 = new long[] { bytesToWord(tweak, 0), bytesToWord(tweak, 8) };
        }
        this.init(b, array4, array5);
    }
    
    public void init(final boolean forEncryption, final long[] key, final long[] tweak) {
        this.forEncryption = forEncryption;
        if (key != null) {
            this.setKey(key);
        }
        if (tweak != null) {
            this.setTweak(tweak);
        }
    }
    
    @Override
    public int processBlock(final byte[] array, int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        final int blocksizeBytes = this.blocksizeBytes;
        if (n + blocksizeBytes > array.length) {
            throw new DataLengthException("Input buffer too short");
        }
        if (blocksizeBytes + n2 <= array2.length) {
            final int n3 = 0;
            for (int i = 0; i < this.blocksizeBytes; i += 8) {
                this.currentBlock[i >> 3] = bytesToWord(array, n + i);
            }
            final long[] currentBlock = this.currentBlock;
            this.processBlock(currentBlock, currentBlock);
            n = n3;
            int blocksizeBytes2;
            while (true) {
                blocksizeBytes2 = this.blocksizeBytes;
                if (n >= blocksizeBytes2) {
                    break;
                }
                wordToBytes(this.currentBlock[n >> 3], array2, n2 + n);
                n += 8;
            }
            return blocksizeBytes2;
        }
        throw new OutputLengthException("Output buffer too short");
    }
    
    public int processBlock(final long[] array, final long[] array2) throws DataLengthException, IllegalStateException {
        final long[] kw = this.kw;
        final int blocksizeWords = this.blocksizeWords;
        if (kw[blocksizeWords] == 0L) {
            throw new IllegalStateException("Threefish engine not initialised");
        }
        if (array.length != blocksizeWords) {
            throw new DataLengthException("Input buffer too short");
        }
        if (array2.length == blocksizeWords) {
            if (this.forEncryption) {
                this.cipher.encryptBlock(array, array2);
            }
            else {
                this.cipher.decryptBlock(array, array2);
            }
            return this.blocksizeWords;
        }
        throw new OutputLengthException("Output buffer too short");
    }
    
    @Override
    public void reset() {
    }
    
    private static final class Threefish1024Cipher extends ThreefishCipher
    {
        private static final int ROTATION_0_0 = 24;
        private static final int ROTATION_0_1 = 13;
        private static final int ROTATION_0_2 = 8;
        private static final int ROTATION_0_3 = 47;
        private static final int ROTATION_0_4 = 8;
        private static final int ROTATION_0_5 = 17;
        private static final int ROTATION_0_6 = 22;
        private static final int ROTATION_0_7 = 37;
        private static final int ROTATION_1_0 = 38;
        private static final int ROTATION_1_1 = 19;
        private static final int ROTATION_1_2 = 10;
        private static final int ROTATION_1_3 = 55;
        private static final int ROTATION_1_4 = 49;
        private static final int ROTATION_1_5 = 18;
        private static final int ROTATION_1_6 = 23;
        private static final int ROTATION_1_7 = 52;
        private static final int ROTATION_2_0 = 33;
        private static final int ROTATION_2_1 = 4;
        private static final int ROTATION_2_2 = 51;
        private static final int ROTATION_2_3 = 13;
        private static final int ROTATION_2_4 = 34;
        private static final int ROTATION_2_5 = 41;
        private static final int ROTATION_2_6 = 59;
        private static final int ROTATION_2_7 = 17;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 20;
        private static final int ROTATION_3_2 = 48;
        private static final int ROTATION_3_3 = 41;
        private static final int ROTATION_3_4 = 47;
        private static final int ROTATION_3_5 = 28;
        private static final int ROTATION_3_6 = 16;
        private static final int ROTATION_3_7 = 25;
        private static final int ROTATION_4_0 = 41;
        private static final int ROTATION_4_1 = 9;
        private static final int ROTATION_4_2 = 37;
        private static final int ROTATION_4_3 = 31;
        private static final int ROTATION_4_4 = 12;
        private static final int ROTATION_4_5 = 47;
        private static final int ROTATION_4_6 = 44;
        private static final int ROTATION_4_7 = 30;
        private static final int ROTATION_5_0 = 16;
        private static final int ROTATION_5_1 = 34;
        private static final int ROTATION_5_2 = 56;
        private static final int ROTATION_5_3 = 51;
        private static final int ROTATION_5_4 = 4;
        private static final int ROTATION_5_5 = 53;
        private static final int ROTATION_5_6 = 42;
        private static final int ROTATION_5_7 = 41;
        private static final int ROTATION_6_0 = 31;
        private static final int ROTATION_6_1 = 44;
        private static final int ROTATION_6_2 = 47;
        private static final int ROTATION_6_3 = 46;
        private static final int ROTATION_6_4 = 19;
        private static final int ROTATION_6_5 = 42;
        private static final int ROTATION_6_6 = 44;
        private static final int ROTATION_6_7 = 25;
        private static final int ROTATION_7_0 = 9;
        private static final int ROTATION_7_1 = 48;
        private static final int ROTATION_7_2 = 35;
        private static final int ROTATION_7_3 = 52;
        private static final int ROTATION_7_4 = 23;
        private static final int ROTATION_7_5 = 31;
        private static final int ROTATION_7_6 = 37;
        private static final int ROTATION_7_7 = 20;
        
        public Threefish1024Cipher(final long[] array, final long[] array2) {
            super(array, array2);
        }
        
        @Override
        void decryptBlock(long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$300 = ThreefishEngine.MOD17;
            final int[] access$301 = ThreefishEngine.MOD3;
            if (kw.length != 33) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                long n = array[0];
                long xorRotr = array[1];
                long n2 = array[2];
                long xorRotr2 = array[3];
                long n3 = array[4];
                long xorRotr3 = array[5];
                long n4 = array[6];
                long xorRotr4 = array[7];
                long n5 = array[8];
                long xorRotr5 = array[9];
                long n6 = array[10];
                long xorRotr6 = array[11];
                long n7 = array[12];
                long xorRotr7 = array[13];
                long n8 = array[14];
                long xorRotr8 = array[15];
                int i = 19;
                array = t;
                while (i >= 1) {
                    final int n9 = access$300[i];
                    final int n10 = access$301[i];
                    final int n11 = n9 + 1;
                    final long n12 = n - kw[n11];
                    final int n13 = n9 + 2;
                    final long n14 = kw[n13];
                    final int n15 = n9 + 3;
                    final long n16 = n2 - kw[n15];
                    final int n17 = n9 + 4;
                    final long n18 = kw[n17];
                    final int n19 = n9 + 5;
                    final long n20 = n3 - kw[n19];
                    final int n21 = n9 + 6;
                    final long n22 = kw[n21];
                    final int n23 = n9 + 7;
                    final long n24 = n4 - kw[n23];
                    final int n25 = n9 + 8;
                    final long n26 = kw[n25];
                    final int n27 = n9 + 9;
                    final long n28 = n5 - kw[n27];
                    final int n29 = n9 + 10;
                    final long n30 = kw[n29];
                    final int n31 = n9 + 11;
                    final long n32 = n6 - kw[n31];
                    final int n33 = n9 + 12;
                    final long n34 = kw[n33];
                    final int n35 = n9 + 13;
                    final long n36 = n7 - kw[n35];
                    final int n37 = n9 + 14;
                    final long n38 = kw[n37];
                    final int n39 = n10 + 1;
                    final long n40 = array[n39];
                    final int n41 = n9 + 15;
                    final long n42 = n8 - (kw[n41] + array[n10 + 2]);
                    final long n43 = kw[n9 + 16];
                    final long n44 = i;
                    final long xorRotr9 = ThreefishEngine.xorRotr(xorRotr8 - (n43 + n44 + 1L), 9, n12);
                    final long n45 = n12 - xorRotr9;
                    final long xorRotr10 = ThreefishEngine.xorRotr(xorRotr6 - n34, 48, n16);
                    final long n46 = n16 - xorRotr10;
                    final long xorRotr11 = ThreefishEngine.xorRotr(xorRotr7 - (n38 + n40), 35, n24);
                    final long n47 = n24 - xorRotr11;
                    final long xorRotr12 = ThreefishEngine.xorRotr(xorRotr5 - n30, 52, n20);
                    final long n48 = n20 - xorRotr12;
                    final long xorRotr13 = ThreefishEngine.xorRotr(xorRotr - n14, 23, n42);
                    final long n49 = n42 - xorRotr13;
                    final long xorRotr14 = ThreefishEngine.xorRotr(xorRotr3 - n22, 31, n28);
                    final long n50 = n28 - xorRotr14;
                    final long xorRotr15 = ThreefishEngine.xorRotr(xorRotr2 - n18, 37, n32);
                    final long n51 = n32 - xorRotr15;
                    final long xorRotr16 = ThreefishEngine.xorRotr(xorRotr4 - n26, 20, n36);
                    final long n52 = n36 - xorRotr16;
                    final long xorRotr17 = ThreefishEngine.xorRotr(xorRotr16, 31, n45);
                    final long n53 = n45 - xorRotr17;
                    final long xorRotr18 = ThreefishEngine.xorRotr(xorRotr14, 44, n46);
                    final long n54 = n46 - xorRotr18;
                    final long xorRotr19 = ThreefishEngine.xorRotr(xorRotr15, 47, n48);
                    final long n55 = n48 - xorRotr19;
                    final long xorRotr20 = ThreefishEngine.xorRotr(xorRotr13, 46, n47);
                    final long n56 = n47 - xorRotr20;
                    final long xorRotr21 = ThreefishEngine.xorRotr(xorRotr9, 19, n52);
                    final long n57 = n52 - xorRotr21;
                    final long xorRotr22 = ThreefishEngine.xorRotr(xorRotr11, 42, n49);
                    final long n58 = n49 - xorRotr22;
                    final long xorRotr23 = ThreefishEngine.xorRotr(xorRotr10, 44, n50);
                    final long n59 = n50 - xorRotr23;
                    final long xorRotr24 = ThreefishEngine.xorRotr(xorRotr12, 25, n51);
                    final long n60 = n51 - xorRotr24;
                    final long xorRotr25 = ThreefishEngine.xorRotr(xorRotr24, 16, n53);
                    final long n61 = n53 - xorRotr25;
                    final long xorRotr26 = ThreefishEngine.xorRotr(xorRotr22, 34, n54);
                    final long n62 = n54 - xorRotr26;
                    final long xorRotr27 = ThreefishEngine.xorRotr(xorRotr23, 56, n56);
                    final long n63 = n56 - xorRotr27;
                    final long xorRotr28 = ThreefishEngine.xorRotr(xorRotr21, 51, n55);
                    final long n64 = n55 - xorRotr28;
                    final long xorRotr29 = ThreefishEngine.xorRotr(xorRotr17, 4, n60);
                    final long n65 = n60 - xorRotr29;
                    final long xorRotr30 = ThreefishEngine.xorRotr(xorRotr19, 53, n57);
                    final long n66 = n57 - xorRotr30;
                    final long xorRotr31 = ThreefishEngine.xorRotr(xorRotr18, 42, n58);
                    final long n67 = n58 - xorRotr31;
                    final long xorRotr32 = ThreefishEngine.xorRotr(xorRotr20, 41, n59);
                    final long n68 = n59 - xorRotr32;
                    final long xorRotr33 = ThreefishEngine.xorRotr(xorRotr32, 41, n61);
                    final long xorRotr34 = ThreefishEngine.xorRotr(xorRotr30, 9, n62);
                    final long xorRotr35 = ThreefishEngine.xorRotr(xorRotr31, 37, n64);
                    final long xorRotr36 = ThreefishEngine.xorRotr(xorRotr29, 31, n63);
                    final long xorRotr37 = ThreefishEngine.xorRotr(xorRotr25, 12, n68);
                    final long xorRotr38 = ThreefishEngine.xorRotr(xorRotr27, 47, n65);
                    final long xorRotr39 = ThreefishEngine.xorRotr(xorRotr26, 44, n66);
                    final long xorRotr40 = ThreefishEngine.xorRotr(xorRotr28, 30, n67);
                    final long n69 = n61 - xorRotr33 - kw[n9];
                    final long n70 = kw[n11];
                    final long n71 = n62 - xorRotr34 - kw[n13];
                    final long n72 = kw[n15];
                    final long n73 = n64 - xorRotr35 - kw[n17];
                    final long n74 = kw[n19];
                    final long n75 = n63 - xorRotr36 - kw[n21];
                    final long n76 = kw[n23];
                    final long n77 = n68 - xorRotr37 - kw[n25];
                    final long n78 = kw[n27];
                    final long n79 = n65 - xorRotr38 - kw[n29];
                    final long n80 = kw[n31];
                    final long n81 = n66 - xorRotr39 - kw[n33];
                    final long n82 = kw[n35];
                    final long n83 = array[n10];
                    final long n84 = n67 - xorRotr40 - (kw[n37] + array[n39]);
                    final long xorRotr41 = ThreefishEngine.xorRotr(xorRotr40 - (kw[n41] + n44), 5, n69);
                    final long n85 = n69 - xorRotr41;
                    final long xorRotr42 = ThreefishEngine.xorRotr(xorRotr38 - n80, 20, n71);
                    final long n86 = n71 - xorRotr42;
                    final long xorRotr43 = ThreefishEngine.xorRotr(xorRotr39 - (n82 + n83), 48, n75);
                    final long n87 = n75 - xorRotr43;
                    final long xorRotr44 = ThreefishEngine.xorRotr(xorRotr37 - n78, 41, n73);
                    final long n88 = n73 - xorRotr44;
                    final long xorRotr45 = ThreefishEngine.xorRotr(xorRotr33 - n70, 47, n84);
                    final long n89 = n84 - xorRotr45;
                    final long xorRotr46 = ThreefishEngine.xorRotr(xorRotr35 - n74, 28, n77);
                    final long n90 = n77 - xorRotr46;
                    final long xorRotr47 = ThreefishEngine.xorRotr(xorRotr34 - n72, 16, n79);
                    final long n91 = n79 - xorRotr47;
                    final long xorRotr48 = ThreefishEngine.xorRotr(xorRotr36 - n76, 25, n81);
                    final long n92 = n81 - xorRotr48;
                    final long xorRotr49 = ThreefishEngine.xorRotr(xorRotr48, 33, n85);
                    final long n93 = n85 - xorRotr49;
                    final long xorRotr50 = ThreefishEngine.xorRotr(xorRotr46, 4, n86);
                    final long n94 = n86 - xorRotr50;
                    final long xorRotr51 = ThreefishEngine.xorRotr(xorRotr47, 51, n88);
                    final long n95 = n88 - xorRotr51;
                    final long xorRotr52 = ThreefishEngine.xorRotr(xorRotr45, 13, n87);
                    final long n96 = n87 - xorRotr52;
                    final long xorRotr53 = ThreefishEngine.xorRotr(xorRotr41, 34, n92);
                    final long n97 = n92 - xorRotr53;
                    final long xorRotr54 = ThreefishEngine.xorRotr(xorRotr43, 41, n89);
                    final long n98 = n89 - xorRotr54;
                    final long xorRotr55 = ThreefishEngine.xorRotr(xorRotr42, 59, n90);
                    final long n99 = n90 - xorRotr55;
                    final long xorRotr56 = ThreefishEngine.xorRotr(xorRotr44, 17, n91);
                    final long n100 = n91 - xorRotr56;
                    final long xorRotr57 = ThreefishEngine.xorRotr(xorRotr56, 38, n93);
                    final long n101 = n93 - xorRotr57;
                    final long xorRotr58 = ThreefishEngine.xorRotr(xorRotr54, 19, n94);
                    final long n102 = n94 - xorRotr58;
                    final long xorRotr59 = ThreefishEngine.xorRotr(xorRotr55, 10, n96);
                    final long n103 = n96 - xorRotr59;
                    final long xorRotr60 = ThreefishEngine.xorRotr(xorRotr53, 55, n95);
                    final long n104 = n95 - xorRotr60;
                    final long xorRotr61 = ThreefishEngine.xorRotr(xorRotr49, 49, n100);
                    final long n105 = n100 - xorRotr61;
                    final long xorRotr62 = ThreefishEngine.xorRotr(xorRotr51, 18, n97);
                    final long n106 = n97 - xorRotr62;
                    final long xorRotr63 = ThreefishEngine.xorRotr(xorRotr50, 23, n98);
                    final long n107 = n98 - xorRotr63;
                    final long xorRotr64 = ThreefishEngine.xorRotr(xorRotr52, 52, n99);
                    final long n108 = n99 - xorRotr64;
                    xorRotr = ThreefishEngine.xorRotr(xorRotr64, 24, n101);
                    xorRotr2 = ThreefishEngine.xorRotr(xorRotr62, 13, n102);
                    xorRotr3 = ThreefishEngine.xorRotr(xorRotr63, 8, n104);
                    xorRotr4 = ThreefishEngine.xorRotr(xorRotr61, 47, n103);
                    xorRotr5 = ThreefishEngine.xorRotr(xorRotr57, 8, n108);
                    xorRotr6 = ThreefishEngine.xorRotr(xorRotr59, 17, n105);
                    xorRotr7 = ThreefishEngine.xorRotr(xorRotr58, 22, n106);
                    xorRotr8 = ThreefishEngine.xorRotr(xorRotr60, 37, n107);
                    n8 = n107 - xorRotr8;
                    n7 = n106 - xorRotr7;
                    n5 = n108 - xorRotr5;
                    n3 = n104 - xorRotr3;
                    i -= 2;
                    n6 = n105 - xorRotr6;
                    n4 = n103 - xorRotr4;
                    n = n101 - xorRotr;
                    n2 = n102 - xorRotr2;
                }
                final long n109 = kw[0];
                final long n110 = kw[1];
                final long n111 = kw[2];
                final long n112 = kw[3];
                final long n113 = kw[4];
                final long n114 = kw[5];
                final long n115 = kw[6];
                final long n116 = kw[7];
                final long n117 = kw[8];
                final long n118 = kw[9];
                final long n119 = kw[10];
                final long n120 = kw[11];
                final long n121 = kw[12];
                final long n122 = kw[13];
                final long n123 = array[0];
                final long n124 = kw[14];
                final long n125 = array[1];
                final long n126 = kw[15];
                array2[0] = n - n109;
                array2[1] = xorRotr - n110;
                array2[2] = n2 - n111;
                array2[3] = xorRotr2 - n112;
                array2[4] = n3 - n113;
                array2[5] = xorRotr3 - n114;
                array2[6] = n4 - n115;
                array2[7] = xorRotr4 - n116;
                array2[8] = n5 - n117;
                array2[9] = xorRotr5 - n118;
                array2[10] = n6 - n119;
                array2[11] = xorRotr6 - n120;
                array2[12] = n7 - n121;
                array2[13] = xorRotr7 - (n122 + n123);
                array2[14] = n8 - (n124 + n125);
                array2[15] = xorRotr8 - n126;
                return;
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        void encryptBlock(final long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$300 = ThreefishEngine.MOD17;
            final int[] access$301 = ThreefishEngine.MOD3;
            if (kw.length != 33) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                final long n = array[0];
                final long n2 = array[1];
                final long n3 = array[2];
                final long n4 = array[3];
                final long n5 = array[4];
                final long n6 = array[5];
                final long n7 = array[6];
                final long n8 = array[7];
                final long n9 = array[8];
                final long n10 = array[9];
                final long n11 = array[10];
                final long n12 = array[11];
                final long n13 = array[12];
                final long n14 = array[13];
                final long n15 = array[14];
                final long n16 = array[15];
                long n17 = n + kw[0];
                final long n18 = kw[1];
                long n19 = n3 + kw[2];
                final long n20 = kw[3];
                long n21 = n5 + kw[4];
                final long n22 = kw[5];
                long n23 = n7 + kw[6];
                final long n24 = kw[7];
                long n25 = n9 + kw[8];
                final long n26 = kw[9];
                long n27 = n11 + kw[10];
                final long n28 = kw[11];
                long n29 = n13 + kw[12];
                final long n30 = kw[13];
                final long n31 = t[0];
                long n32 = n15 + (kw[14] + t[1]);
                final long n33 = kw[15];
                long n34 = n6 + n22;
                long n35 = n8 + n24;
                long n36 = n10 + n26;
                long n37 = n12 + n28;
                long n38 = n16 + n33;
                long n39 = n14 + (n30 + n31);
                long n40 = n2 + n18;
                long n41 = n4 + n20;
                int i = 1;
                final int[] array3 = access$301;
                while (i < 20) {
                    final int n42 = access$300[i];
                    final int n43 = array3[i];
                    final long n44 = n17 + n40;
                    final long rotlXor = ThreefishEngine.rotlXor(n40, 24, n44);
                    final long n45 = n19 + n41;
                    final long rotlXor2 = ThreefishEngine.rotlXor(n41, 13, n45);
                    final long n46 = n21 + n34;
                    final long rotlXor3 = ThreefishEngine.rotlXor(n34, 8, n46);
                    final long n47 = n23 + n35;
                    final long rotlXor4 = ThreefishEngine.rotlXor(n35, 47, n47);
                    final long n48 = n25 + n36;
                    final long rotlXor5 = ThreefishEngine.rotlXor(n36, 8, n48);
                    final long n49 = n27 + n37;
                    final long rotlXor6 = ThreefishEngine.rotlXor(n37, 17, n49);
                    final long n50 = n29 + n39;
                    final long rotlXor7 = ThreefishEngine.rotlXor(n39, 22, n50);
                    final long n51 = n32 + n38;
                    final long rotlXor8 = ThreefishEngine.rotlXor(n38, 37, n51);
                    final long n52 = n44 + rotlXor5;
                    final long rotlXor9 = ThreefishEngine.rotlXor(rotlXor5, 38, n52);
                    final long n53 = n45 + rotlXor7;
                    final long rotlXor10 = ThreefishEngine.rotlXor(rotlXor7, 19, n53);
                    final long n54 = n47 + rotlXor6;
                    final long rotlXor11 = ThreefishEngine.rotlXor(rotlXor6, 10, n54);
                    final long n55 = n46 + rotlXor8;
                    final long rotlXor12 = ThreefishEngine.rotlXor(rotlXor8, 55, n55);
                    final long n56 = n49 + rotlXor4;
                    final long rotlXor13 = ThreefishEngine.rotlXor(rotlXor4, 49, n56);
                    final long n57 = n50 + rotlXor2;
                    final long rotlXor14 = ThreefishEngine.rotlXor(rotlXor2, 18, n57);
                    final long n58 = n51 + rotlXor3;
                    final long rotlXor15 = ThreefishEngine.rotlXor(rotlXor3, 23, n58);
                    final long n59 = n48 + rotlXor;
                    final long rotlXor16 = ThreefishEngine.rotlXor(rotlXor, 52, n59);
                    final long n60 = n52 + rotlXor13;
                    final long rotlXor17 = ThreefishEngine.rotlXor(rotlXor13, 33, n60);
                    final long n61 = n53 + rotlXor15;
                    final long rotlXor18 = ThreefishEngine.rotlXor(rotlXor15, 4, n61);
                    final long n62 = n55 + rotlXor14;
                    final long rotlXor19 = ThreefishEngine.rotlXor(rotlXor14, 51, n62);
                    final long n63 = n54 + rotlXor16;
                    final long rotlXor20 = ThreefishEngine.rotlXor(rotlXor16, 13, n63);
                    final long n64 = n57 + rotlXor12;
                    final long rotlXor21 = ThreefishEngine.rotlXor(rotlXor12, 34, n64);
                    final long n65 = n58 + rotlXor10;
                    final long rotlXor22 = ThreefishEngine.rotlXor(rotlXor10, 41, n65);
                    final long n66 = n59 + rotlXor11;
                    final long rotlXor23 = ThreefishEngine.rotlXor(rotlXor11, 59, n66);
                    final long n67 = n56 + rotlXor9;
                    final long rotlXor24 = ThreefishEngine.rotlXor(rotlXor9, 17, n67);
                    final long n68 = n60 + rotlXor21;
                    final long rotlXor25 = ThreefishEngine.rotlXor(rotlXor21, 5, n68);
                    final long n69 = n61 + rotlXor23;
                    final long rotlXor26 = ThreefishEngine.rotlXor(rotlXor23, 20, n69);
                    final long n70 = n63 + rotlXor22;
                    final long rotlXor27 = ThreefishEngine.rotlXor(rotlXor22, 48, n70);
                    final long n71 = n62 + rotlXor24;
                    final long rotlXor28 = ThreefishEngine.rotlXor(rotlXor24, 41, n71);
                    final long n72 = n65 + rotlXor20;
                    final long rotlXor29 = ThreefishEngine.rotlXor(rotlXor20, 47, n72);
                    final long n73 = n66 + rotlXor18;
                    final long rotlXor30 = ThreefishEngine.rotlXor(rotlXor18, 28, n73);
                    final long n74 = n67 + rotlXor19;
                    final long rotlXor31 = ThreefishEngine.rotlXor(rotlXor19, 16, n74);
                    final long n75 = n64 + rotlXor17;
                    final long rotlXor32 = ThreefishEngine.rotlXor(rotlXor17, 25, n75);
                    final long n76 = kw[n42];
                    final int n77 = n42 + 1;
                    final long n78 = rotlXor29 + kw[n77];
                    final int n79 = n42 + 2;
                    final long n80 = kw[n79];
                    final int n81 = n42 + 3;
                    final long n82 = rotlXor31 + kw[n81];
                    final int n83 = n42 + 4;
                    final long n84 = kw[n83];
                    final int n85 = n42 + 5;
                    final long n86 = rotlXor30 + kw[n85];
                    final int n87 = n42 + 6;
                    final long n88 = kw[n87];
                    final int n89 = n42 + 7;
                    final long n90 = rotlXor32 + kw[n89];
                    final int n91 = n42 + 8;
                    final long n92 = kw[n91];
                    final int n93 = n42 + 9;
                    final long n94 = rotlXor28 + kw[n93];
                    final int n95 = n42 + 10;
                    final long n96 = kw[n95];
                    final int n97 = n42 + 11;
                    final long n98 = rotlXor26 + kw[n97];
                    final int n99 = n42 + 12;
                    final long n100 = kw[n99];
                    final int n101 = n42 + 13;
                    final long n102 = rotlXor27 + (kw[n101] + t[n43]);
                    final int n103 = n42 + 14;
                    final long n104 = kw[n103];
                    final int n105 = n43 + 1;
                    final long n106 = t[n105];
                    final int n107 = n42 + 15;
                    final long n108 = kw[n107];
                    final long n109 = i;
                    final long n110 = rotlXor25 + (n108 + n109);
                    final long n111 = n68 + n76 + n78;
                    final long rotlXor33 = ThreefishEngine.rotlXor(n78, 41, n111);
                    final long n112 = n69 + n80 + n82;
                    final long rotlXor34 = ThreefishEngine.rotlXor(n82, 9, n112);
                    final long n113 = n71 + n84 + n86;
                    final long rotlXor35 = ThreefishEngine.rotlXor(n86, 37, n113);
                    final long n114 = n70 + n88 + n90;
                    final long rotlXor36 = ThreefishEngine.rotlXor(n90, 31, n114);
                    final long n115 = n73 + n92 + n94;
                    final long rotlXor37 = ThreefishEngine.rotlXor(n94, 12, n115);
                    final long n116 = n74 + n96 + n98;
                    final long rotlXor38 = ThreefishEngine.rotlXor(n98, 47, n116);
                    final long n117 = n75 + n100 + n102;
                    final long rotlXor39 = ThreefishEngine.rotlXor(n102, 44, n117);
                    final long n118 = n72 + (n104 + n106) + n110;
                    final long rotlXor40 = ThreefishEngine.rotlXor(n110, 30, n118);
                    final long n119 = n111 + rotlXor37;
                    final long rotlXor41 = ThreefishEngine.rotlXor(rotlXor37, 16, n119);
                    final long n120 = n112 + rotlXor39;
                    final long rotlXor42 = ThreefishEngine.rotlXor(rotlXor39, 34, n120);
                    final long n121 = n114 + rotlXor38;
                    final long rotlXor43 = ThreefishEngine.rotlXor(rotlXor38, 56, n121);
                    final long n122 = n113 + rotlXor40;
                    final long rotlXor44 = ThreefishEngine.rotlXor(rotlXor40, 51, n122);
                    final long n123 = n116 + rotlXor36;
                    final long rotlXor45 = ThreefishEngine.rotlXor(rotlXor36, 4, n123);
                    final long n124 = n117 + rotlXor34;
                    final long rotlXor46 = ThreefishEngine.rotlXor(rotlXor34, 53, n124);
                    final long n125 = n118 + rotlXor35;
                    final long rotlXor47 = ThreefishEngine.rotlXor(rotlXor35, 42, n125);
                    final long n126 = n115 + rotlXor33;
                    final long rotlXor48 = ThreefishEngine.rotlXor(rotlXor33, 41, n126);
                    final long n127 = n119 + rotlXor45;
                    final long rotlXor49 = ThreefishEngine.rotlXor(rotlXor45, 31, n127);
                    final long n128 = n120 + rotlXor47;
                    final long rotlXor50 = ThreefishEngine.rotlXor(rotlXor47, 44, n128);
                    final long n129 = n122 + rotlXor46;
                    final long rotlXor51 = ThreefishEngine.rotlXor(rotlXor46, 47, n129);
                    final long n130 = n121 + rotlXor48;
                    final long rotlXor52 = ThreefishEngine.rotlXor(rotlXor48, 46, n130);
                    final long n131 = n124 + rotlXor44;
                    final long rotlXor53 = ThreefishEngine.rotlXor(rotlXor44, 19, n131);
                    final long n132 = n125 + rotlXor42;
                    final long rotlXor54 = ThreefishEngine.rotlXor(rotlXor42, 42, n132);
                    final long n133 = n126 + rotlXor43;
                    final long rotlXor55 = ThreefishEngine.rotlXor(rotlXor43, 44, n133);
                    final long n134 = n123 + rotlXor41;
                    final long rotlXor56 = ThreefishEngine.rotlXor(rotlXor41, 25, n134);
                    final long n135 = n127 + rotlXor53;
                    final long rotlXor57 = ThreefishEngine.rotlXor(rotlXor53, 9, n135);
                    final long n136 = n128 + rotlXor55;
                    final long rotlXor58 = ThreefishEngine.rotlXor(rotlXor55, 48, n136);
                    final long n137 = n130 + rotlXor54;
                    final long rotlXor59 = ThreefishEngine.rotlXor(rotlXor54, 35, n137);
                    final long n138 = n129 + rotlXor56;
                    final long rotlXor60 = ThreefishEngine.rotlXor(rotlXor56, 52, n138);
                    final long n139 = n132 + rotlXor52;
                    final long rotlXor61 = ThreefishEngine.rotlXor(rotlXor52, 23, n139);
                    final long n140 = n133 + rotlXor50;
                    final long rotlXor62 = ThreefishEngine.rotlXor(rotlXor50, 31, n140);
                    final long n141 = n134 + rotlXor51;
                    final long rotlXor63 = ThreefishEngine.rotlXor(rotlXor51, 37, n141);
                    final long n142 = n131 + rotlXor49;
                    final long rotlXor64 = ThreefishEngine.rotlXor(rotlXor49, 20, n142);
                    final long n143 = kw[n77];
                    final long n144 = kw[n79];
                    final long n145 = kw[n81];
                    final long n146 = kw[n83];
                    final long n147 = kw[n85];
                    final long n148 = kw[n87];
                    final long n149 = kw[n89];
                    final long n150 = kw[n91];
                    final long n151 = kw[n93];
                    n36 = rotlXor60 + kw[n95];
                    n27 = n141 + kw[n97];
                    n37 = rotlXor58 + kw[n99];
                    final long n152 = kw[n101];
                    n39 = rotlXor59 + (kw[n103] + t[n105]);
                    final long n153 = kw[n107];
                    final long n154 = t[n43 + 2];
                    n38 = rotlXor57 + (kw[n42 + 16] + n109 + 1L);
                    n35 = rotlXor64 + n150;
                    n34 = rotlXor62 + n148;
                    n32 = n139 + (n153 + n154);
                    n25 = n140 + n151;
                    n23 = n137 + n149;
                    n29 = n142 + n152;
                    i += 2;
                    n21 = n138 + n147;
                    n17 = n135 + n143;
                    n19 = n136 + n145;
                    n41 = rotlXor63 + n146;
                    n40 = rotlXor61 + n144;
                }
                array2[0] = n17;
                array2[1] = n40;
                array2[2] = n19;
                array2[3] = n41;
                array2[4] = n21;
                array2[5] = n34;
                array2[6] = n23;
                array2[7] = n35;
                array2[8] = n25;
                array2[9] = n36;
                array2[10] = n27;
                array2[11] = n37;
                array2[12] = n29;
                array2[13] = n39;
                array2[14] = n32;
                array2[15] = n38;
                return;
            }
            throw new IllegalArgumentException();
        }
    }
    
    private static final class Threefish256Cipher extends ThreefishCipher
    {
        private static final int ROTATION_0_0 = 14;
        private static final int ROTATION_0_1 = 16;
        private static final int ROTATION_1_0 = 52;
        private static final int ROTATION_1_1 = 57;
        private static final int ROTATION_2_0 = 23;
        private static final int ROTATION_2_1 = 40;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 37;
        private static final int ROTATION_4_0 = 25;
        private static final int ROTATION_4_1 = 33;
        private static final int ROTATION_5_0 = 46;
        private static final int ROTATION_5_1 = 12;
        private static final int ROTATION_6_0 = 58;
        private static final int ROTATION_6_1 = 22;
        private static final int ROTATION_7_0 = 32;
        private static final int ROTATION_7_1 = 32;
        
        public Threefish256Cipher(final long[] array, final long[] array2) {
            super(array, array2);
        }
        
        @Override
        void decryptBlock(final long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$000 = ThreefishEngine.MOD5;
            final int[] access$2 = ThreefishEngine.MOD3;
            if (kw.length != 9) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                long n = array[0];
                long xorRotr = array[1];
                long n2 = array[2];
                long xorRotr2 = array[3];
                int i = 17;
                final int[] array3 = access$2;
                while (i >= 1) {
                    final int n3 = access$000[i];
                    final int n4 = array3[i];
                    final int n5 = n3 + 1;
                    final long n6 = n - kw[n5];
                    final int n7 = n3 + 2;
                    final long n8 = kw[n7];
                    final int n9 = n4 + 1;
                    final long n10 = t[n9];
                    final int n11 = n3 + 3;
                    final long n12 = n2 - (kw[n11] + t[n4 + 2]);
                    final long n13 = kw[n3 + 4];
                    final long n14 = i;
                    final long xorRotr3 = ThreefishEngine.xorRotr(xorRotr2 - (n13 + n14 + 1L), 32, n6);
                    final long n15 = n6 - xorRotr3;
                    final long xorRotr4 = ThreefishEngine.xorRotr(xorRotr - (n8 + n10), 32, n12);
                    final long n16 = n12 - xorRotr4;
                    final long xorRotr5 = ThreefishEngine.xorRotr(xorRotr4, 58, n15);
                    final long n17 = n15 - xorRotr5;
                    final long xorRotr6 = ThreefishEngine.xorRotr(xorRotr3, 22, n16);
                    final long n18 = n16 - xorRotr6;
                    final long xorRotr7 = ThreefishEngine.xorRotr(xorRotr6, 46, n17);
                    final long n19 = n17 - xorRotr7;
                    final long xorRotr8 = ThreefishEngine.xorRotr(xorRotr5, 12, n18);
                    final long n20 = n18 - xorRotr8;
                    final long xorRotr9 = ThreefishEngine.xorRotr(xorRotr8, 25, n19);
                    final long xorRotr10 = ThreefishEngine.xorRotr(xorRotr7, 33, n20);
                    final long n21 = n19 - xorRotr9 - kw[n3];
                    final long n22 = kw[n5];
                    final long n23 = t[n4];
                    final long n24 = n20 - xorRotr10 - (kw[n7] + t[n9]);
                    final long xorRotr11 = ThreefishEngine.xorRotr(xorRotr10 - (kw[n11] + n14), 5, n21);
                    final long n25 = n21 - xorRotr11;
                    final long xorRotr12 = ThreefishEngine.xorRotr(xorRotr9 - (n22 + n23), 37, n24);
                    final long n26 = n24 - xorRotr12;
                    final long xorRotr13 = ThreefishEngine.xorRotr(xorRotr12, 23, n25);
                    final long n27 = n25 - xorRotr13;
                    final long xorRotr14 = ThreefishEngine.xorRotr(xorRotr11, 40, n26);
                    final long n28 = n26 - xorRotr14;
                    final long xorRotr15 = ThreefishEngine.xorRotr(xorRotr14, 52, n27);
                    final long n29 = n27 - xorRotr15;
                    final long xorRotr16 = ThreefishEngine.xorRotr(xorRotr13, 57, n28);
                    final long n30 = n28 - xorRotr16;
                    xorRotr = ThreefishEngine.xorRotr(xorRotr16, 14, n29);
                    xorRotr2 = ThreefishEngine.xorRotr(xorRotr15, 16, n30);
                    n2 = n30 - xorRotr2;
                    i -= 2;
                    n = n29 - xorRotr;
                }
                final long n31 = kw[0];
                final long n32 = kw[1];
                final long n33 = t[0];
                final long n34 = kw[2];
                final long n35 = t[1];
                final long n36 = kw[3];
                array2[0] = n - n31;
                array2[1] = xorRotr - (n32 + n33);
                array2[2] = n2 - (n34 + n35);
                array2[3] = xorRotr2 - n36;
                return;
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        void encryptBlock(final long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$000 = ThreefishEngine.MOD5;
            final int[] access$2 = ThreefishEngine.MOD3;
            if (kw.length != 9) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                final long n = array[0];
                final long n2 = array[1];
                final long n3 = array[2];
                final long n4 = array[3];
                long n5 = n + kw[0];
                long n6 = n2 + (kw[1] + t[0]);
                long n7 = n3 + (kw[2] + t[1]);
                long n8 = n4 + kw[3];
                int i = 1;
                final int[] array3 = access$2;
                while (i < 18) {
                    final int n9 = access$000[i];
                    final int n10 = array3[i];
                    final long n11 = n5 + n6;
                    final long rotlXor = ThreefishEngine.rotlXor(n6, 14, n11);
                    final long n12 = n7 + n8;
                    final long rotlXor2 = ThreefishEngine.rotlXor(n8, 16, n12);
                    final long n13 = n11 + rotlXor2;
                    final long rotlXor3 = ThreefishEngine.rotlXor(rotlXor2, 52, n13);
                    final long n14 = n12 + rotlXor;
                    final long rotlXor4 = ThreefishEngine.rotlXor(rotlXor, 57, n14);
                    final long n15 = n13 + rotlXor4;
                    final long rotlXor5 = ThreefishEngine.rotlXor(rotlXor4, 23, n15);
                    final long n16 = n14 + rotlXor3;
                    final long rotlXor6 = ThreefishEngine.rotlXor(rotlXor3, 40, n16);
                    final long n17 = n15 + rotlXor6;
                    final long rotlXor7 = ThreefishEngine.rotlXor(rotlXor6, 5, n17);
                    final long n18 = n16 + rotlXor5;
                    final long rotlXor8 = ThreefishEngine.rotlXor(rotlXor5, 37, n18);
                    final long n19 = kw[n9];
                    final int n20 = n9 + 1;
                    final long n21 = rotlXor8 + (kw[n20] + t[n10]);
                    final int n22 = n9 + 2;
                    final long n23 = kw[n22];
                    final int n24 = n10 + 1;
                    final long n25 = t[n24];
                    final int n26 = n9 + 3;
                    final long n27 = kw[n26];
                    final long n28 = i;
                    final long n29 = rotlXor7 + (n27 + n28);
                    final long n30 = n17 + n19 + n21;
                    final long rotlXor9 = ThreefishEngine.rotlXor(n21, 25, n30);
                    final long n31 = n18 + (n23 + n25) + n29;
                    final long rotlXor10 = ThreefishEngine.rotlXor(n29, 33, n31);
                    final long n32 = n30 + rotlXor10;
                    final long rotlXor11 = ThreefishEngine.rotlXor(rotlXor10, 46, n32);
                    final long n33 = n31 + rotlXor9;
                    final long rotlXor12 = ThreefishEngine.rotlXor(rotlXor9, 12, n33);
                    final long n34 = n32 + rotlXor12;
                    final long rotlXor13 = ThreefishEngine.rotlXor(rotlXor12, 58, n34);
                    final long n35 = n33 + rotlXor11;
                    final long rotlXor14 = ThreefishEngine.rotlXor(rotlXor11, 22, n35);
                    final long n36 = n34 + rotlXor14;
                    final long rotlXor15 = ThreefishEngine.rotlXor(rotlXor14, 32, n36);
                    final long n37 = n35 + rotlXor13;
                    final long rotlXor16 = ThreefishEngine.rotlXor(rotlXor13, 32, n37);
                    final long n38 = kw[n20];
                    final long n39 = kw[n22];
                    final long n40 = t[n24];
                    n7 = n37 + (kw[n26] + t[n10 + 2]);
                    n8 = rotlXor15 + (kw[n9 + 4] + n28 + 1L);
                    i += 2;
                    n5 = n36 + n38;
                    n6 = rotlXor16 + (n39 + n40);
                }
                array2[0] = n5;
                array2[1] = n6;
                array2[2] = n7;
                array2[3] = n8;
                return;
            }
            throw new IllegalArgumentException();
        }
    }
    
    private static final class Threefish512Cipher extends ThreefishCipher
    {
        private static final int ROTATION_0_0 = 46;
        private static final int ROTATION_0_1 = 36;
        private static final int ROTATION_0_2 = 19;
        private static final int ROTATION_0_3 = 37;
        private static final int ROTATION_1_0 = 33;
        private static final int ROTATION_1_1 = 27;
        private static final int ROTATION_1_2 = 14;
        private static final int ROTATION_1_3 = 42;
        private static final int ROTATION_2_0 = 17;
        private static final int ROTATION_2_1 = 49;
        private static final int ROTATION_2_2 = 36;
        private static final int ROTATION_2_3 = 39;
        private static final int ROTATION_3_0 = 44;
        private static final int ROTATION_3_1 = 9;
        private static final int ROTATION_3_2 = 54;
        private static final int ROTATION_3_3 = 56;
        private static final int ROTATION_4_0 = 39;
        private static final int ROTATION_4_1 = 30;
        private static final int ROTATION_4_2 = 34;
        private static final int ROTATION_4_3 = 24;
        private static final int ROTATION_5_0 = 13;
        private static final int ROTATION_5_1 = 50;
        private static final int ROTATION_5_2 = 10;
        private static final int ROTATION_5_3 = 17;
        private static final int ROTATION_6_0 = 25;
        private static final int ROTATION_6_1 = 29;
        private static final int ROTATION_6_2 = 39;
        private static final int ROTATION_6_3 = 43;
        private static final int ROTATION_7_0 = 8;
        private static final int ROTATION_7_1 = 35;
        private static final int ROTATION_7_2 = 56;
        private static final int ROTATION_7_3 = 22;
        
        protected Threefish512Cipher(final long[] array, final long[] array2) {
            super(array, array2);
        }
        
        public void decryptBlock(long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$200 = ThreefishEngine.MOD9;
            final int[] access$201 = ThreefishEngine.MOD3;
            if (kw.length != 17) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                long n = array[0];
                long xorRotr = array[1];
                long n2 = array[2];
                long xorRotr2 = array[3];
                long n3 = array[4];
                long xorRotr3 = array[5];
                long n4 = array[6];
                long xorRotr4 = array[7];
                int i = 17;
                array = t;
                while (i >= 1) {
                    final int n5 = access$200[i];
                    final int n6 = access$201[i];
                    final int n7 = n5 + 1;
                    final long n8 = n - kw[n7];
                    final int n9 = n5 + 2;
                    final long n10 = kw[n9];
                    final int n11 = n5 + 3;
                    final long n12 = n2 - kw[n11];
                    final int n13 = n5 + 4;
                    final long n14 = kw[n13];
                    final int n15 = n5 + 5;
                    final long n16 = n3 - kw[n15];
                    final int n17 = n5 + 6;
                    final long n18 = kw[n17];
                    final int n19 = n6 + 1;
                    final long n20 = array[n19];
                    final int n21 = n5 + 7;
                    final long n22 = n4 - (kw[n21] + array[n6 + 2]);
                    final long n23 = kw[n5 + 8];
                    final long n24 = i;
                    final long xorRotr5 = ThreefishEngine.xorRotr(xorRotr - n10, 8, n22);
                    final long n25 = n22 - xorRotr5;
                    final long xorRotr6 = ThreefishEngine.xorRotr(xorRotr4 - (n23 + n24 + 1L), 35, n8);
                    final long n26 = n8 - xorRotr6;
                    final long xorRotr7 = ThreefishEngine.xorRotr(xorRotr3 - (n18 + n20), 56, n12);
                    final long n27 = n12 - xorRotr7;
                    final long xorRotr8 = ThreefishEngine.xorRotr(xorRotr2 - n14, 22, n16);
                    final long n28 = n16 - xorRotr8;
                    final long xorRotr9 = ThreefishEngine.xorRotr(xorRotr5, 25, n28);
                    final long n29 = n28 - xorRotr9;
                    final long xorRotr10 = ThreefishEngine.xorRotr(xorRotr8, 29, n25);
                    final long n30 = n25 - xorRotr10;
                    final long xorRotr11 = ThreefishEngine.xorRotr(xorRotr7, 39, n26);
                    final long n31 = n26 - xorRotr11;
                    final long xorRotr12 = ThreefishEngine.xorRotr(xorRotr6, 43, n27);
                    final long n32 = n27 - xorRotr12;
                    final long xorRotr13 = ThreefishEngine.xorRotr(xorRotr9, 13, n32);
                    final long n33 = n32 - xorRotr13;
                    final long xorRotr14 = ThreefishEngine.xorRotr(xorRotr12, 50, n29);
                    final long n34 = n29 - xorRotr14;
                    final long xorRotr15 = ThreefishEngine.xorRotr(xorRotr11, 10, n30);
                    final long n35 = n30 - xorRotr15;
                    final long xorRotr16 = ThreefishEngine.xorRotr(xorRotr10, 17, n31);
                    final long n36 = n31 - xorRotr16;
                    final long xorRotr17 = ThreefishEngine.xorRotr(xorRotr13, 39, n36);
                    final long xorRotr18 = ThreefishEngine.xorRotr(xorRotr16, 30, n33);
                    final long xorRotr19 = ThreefishEngine.xorRotr(xorRotr15, 34, n34);
                    final long xorRotr20 = ThreefishEngine.xorRotr(xorRotr14, 24, n35);
                    final long n37 = n36 - xorRotr17 - kw[n5];
                    final long n38 = kw[n7];
                    final long n39 = n33 - xorRotr18 - kw[n9];
                    final long n40 = kw[n11];
                    final long n41 = n34 - xorRotr19 - kw[n13];
                    final long n42 = kw[n15];
                    final long n43 = array[n6];
                    final long n44 = n35 - xorRotr20 - (kw[n17] + array[n19]);
                    final long n45 = kw[n21];
                    final long xorRotr21 = ThreefishEngine.xorRotr(xorRotr17 - n38, 44, n44);
                    final long n46 = n44 - xorRotr21;
                    final long xorRotr22 = ThreefishEngine.xorRotr(xorRotr20 - (n45 + n24), 9, n37);
                    final long n47 = n37 - xorRotr22;
                    final long xorRotr23 = ThreefishEngine.xorRotr(xorRotr19 - (n42 + n43), 54, n39);
                    final long n48 = n39 - xorRotr23;
                    final long xorRotr24 = ThreefishEngine.xorRotr(xorRotr18 - n40, 56, n41);
                    final long n49 = n41 - xorRotr24;
                    final long xorRotr25 = ThreefishEngine.xorRotr(xorRotr21, 17, n49);
                    final long n50 = n49 - xorRotr25;
                    final long xorRotr26 = ThreefishEngine.xorRotr(xorRotr24, 49, n46);
                    final long n51 = n46 - xorRotr26;
                    final long xorRotr27 = ThreefishEngine.xorRotr(xorRotr23, 36, n47);
                    final long n52 = n47 - xorRotr27;
                    final long xorRotr28 = ThreefishEngine.xorRotr(xorRotr22, 39, n48);
                    final long n53 = n48 - xorRotr28;
                    final long xorRotr29 = ThreefishEngine.xorRotr(xorRotr25, 33, n53);
                    final long n54 = n53 - xorRotr29;
                    final long xorRotr30 = ThreefishEngine.xorRotr(xorRotr28, 27, n50);
                    final long n55 = n50 - xorRotr30;
                    final long xorRotr31 = ThreefishEngine.xorRotr(xorRotr27, 14, n51);
                    final long n56 = n51 - xorRotr31;
                    final long xorRotr32 = ThreefishEngine.xorRotr(xorRotr26, 42, n52);
                    final long n57 = n52 - xorRotr32;
                    xorRotr = ThreefishEngine.xorRotr(xorRotr29, 46, n57);
                    xorRotr2 = ThreefishEngine.xorRotr(xorRotr32, 36, n54);
                    n2 = n54 - xorRotr2;
                    xorRotr3 = ThreefishEngine.xorRotr(xorRotr31, 19, n55);
                    xorRotr4 = ThreefishEngine.xorRotr(xorRotr30, 37, n56);
                    n4 = n56 - xorRotr4;
                    n3 = n55 - xorRotr3;
                    i -= 2;
                    n = n57 - xorRotr;
                }
                final long n58 = kw[0];
                final long n59 = kw[1];
                final long n60 = kw[2];
                final long n61 = kw[3];
                final long n62 = kw[4];
                final long n63 = kw[5];
                final long n64 = array[0];
                final long n65 = kw[6];
                final long n66 = array[1];
                final long n67 = kw[7];
                array2[0] = n - n58;
                array2[1] = xorRotr - n59;
                array2[2] = n2 - n60;
                array2[3] = xorRotr2 - n61;
                array2[4] = n3 - n62;
                array2[5] = xorRotr3 - (n63 + n64);
                array2[6] = n4 - (n65 + n66);
                array2[7] = xorRotr4 - n67;
                return;
            }
            throw new IllegalArgumentException();
        }
        
        public void encryptBlock(long[] array, final long[] array2) {
            final long[] kw = this.kw;
            final long[] t = this.t;
            final int[] access$200 = ThreefishEngine.MOD9;
            final int[] access$201 = ThreefishEngine.MOD3;
            if (kw.length != 17) {
                throw new IllegalArgumentException();
            }
            if (t.length == 5) {
                final long n = array[0];
                final long n2 = array[1];
                final long n3 = array[2];
                final long n4 = array[3];
                final long n5 = array[4];
                final long n6 = array[5];
                final long n7 = array[6];
                final long n8 = array[7];
                long n9 = n + kw[0];
                final long n10 = kw[1];
                long n11 = n3 + kw[2];
                final long n12 = kw[3];
                long n13 = n5 + kw[4];
                final long n14 = kw[5];
                final long n15 = t[0];
                long n16 = n7 + (kw[6] + t[1]);
                long n17 = n8 + kw[7];
                long n18 = n6 + (n14 + n15);
                long n19 = n2 + n10;
                long n20 = n4 + n12;
                int i = 1;
                array = t;
                while (i < 18) {
                    final int n21 = access$200[i];
                    final int n22 = access$201[i];
                    final long n23 = n9 + n19;
                    final long rotlXor = ThreefishEngine.rotlXor(n19, 46, n23);
                    final long n24 = n11 + n20;
                    final long rotlXor2 = ThreefishEngine.rotlXor(n20, 36, n24);
                    final long n25 = n13 + n18;
                    final long rotlXor3 = ThreefishEngine.rotlXor(n18, 19, n25);
                    final long n26 = n16 + n17;
                    final long rotlXor4 = ThreefishEngine.rotlXor(n17, 37, n26);
                    final long n27 = n24 + rotlXor;
                    final long rotlXor5 = ThreefishEngine.rotlXor(rotlXor, 33, n27);
                    final long n28 = n25 + rotlXor4;
                    final long rotlXor6 = ThreefishEngine.rotlXor(rotlXor4, 27, n28);
                    final long n29 = n26 + rotlXor3;
                    final long rotlXor7 = ThreefishEngine.rotlXor(rotlXor3, 14, n29);
                    final long n30 = n23 + rotlXor2;
                    final long rotlXor8 = ThreefishEngine.rotlXor(rotlXor2, 42, n30);
                    final long n31 = n28 + rotlXor5;
                    final long rotlXor9 = ThreefishEngine.rotlXor(rotlXor5, 17, n31);
                    final long n32 = n29 + rotlXor8;
                    final long rotlXor10 = ThreefishEngine.rotlXor(rotlXor8, 49, n32);
                    final long n33 = n30 + rotlXor7;
                    final long rotlXor11 = ThreefishEngine.rotlXor(rotlXor7, 36, n33);
                    final long n34 = n27 + rotlXor6;
                    final long rotlXor12 = ThreefishEngine.rotlXor(rotlXor6, 39, n34);
                    final long n35 = n32 + rotlXor9;
                    final long rotlXor13 = ThreefishEngine.rotlXor(rotlXor9, 44, n35);
                    final long n36 = n33 + rotlXor12;
                    final long rotlXor14 = ThreefishEngine.rotlXor(rotlXor12, 9, n36);
                    final long n37 = n34 + rotlXor11;
                    final long rotlXor15 = ThreefishEngine.rotlXor(rotlXor11, 54, n37);
                    final long n38 = n31 + rotlXor10;
                    final long rotlXor16 = ThreefishEngine.rotlXor(rotlXor10, 56, n38);
                    final long n39 = kw[n21];
                    final int n40 = n21 + 1;
                    final long n41 = rotlXor13 + kw[n40];
                    final int n42 = n21 + 2;
                    final long n43 = kw[n42];
                    final int n44 = n21 + 3;
                    final long n45 = rotlXor16 + kw[n44];
                    final int n46 = n21 + 4;
                    final long n47 = kw[n46];
                    final int n48 = n21 + 5;
                    final long n49 = rotlXor15 + (kw[n48] + array[n22]);
                    final int n50 = n21 + 6;
                    final long n51 = kw[n50];
                    final int n52 = n22 + 1;
                    final long n53 = array[n52];
                    final int n54 = n21 + 7;
                    final long n55 = kw[n54];
                    final long n56 = i;
                    final long n57 = rotlXor14 + (n55 + n56);
                    final long n58 = n36 + n39 + n41;
                    final long rotlXor17 = ThreefishEngine.rotlXor(n41, 39, n58);
                    final long n59 = n37 + n43 + n45;
                    final long rotlXor18 = ThreefishEngine.rotlXor(n45, 30, n59);
                    final long n60 = n38 + n47 + n49;
                    final long rotlXor19 = ThreefishEngine.rotlXor(n49, 34, n60);
                    final long n61 = n35 + (n51 + n53) + n57;
                    final long rotlXor20 = ThreefishEngine.rotlXor(n57, 24, n61);
                    final long n62 = n59 + rotlXor17;
                    final long rotlXor21 = ThreefishEngine.rotlXor(rotlXor17, 13, n62);
                    final long n63 = n60 + rotlXor20;
                    final long rotlXor22 = ThreefishEngine.rotlXor(rotlXor20, 50, n63);
                    final long n64 = n61 + rotlXor19;
                    final long rotlXor23 = ThreefishEngine.rotlXor(rotlXor19, 10, n64);
                    final long n65 = n58 + rotlXor18;
                    final long rotlXor24 = ThreefishEngine.rotlXor(rotlXor18, 17, n65);
                    final long n66 = n63 + rotlXor21;
                    final long rotlXor25 = ThreefishEngine.rotlXor(rotlXor21, 25, n66);
                    final long n67 = n64 + rotlXor24;
                    final long rotlXor26 = ThreefishEngine.rotlXor(rotlXor24, 29, n67);
                    final long n68 = n65 + rotlXor23;
                    final long rotlXor27 = ThreefishEngine.rotlXor(rotlXor23, 39, n68);
                    final long n69 = n62 + rotlXor22;
                    final long rotlXor28 = ThreefishEngine.rotlXor(rotlXor22, 43, n69);
                    final long n70 = n67 + rotlXor25;
                    final long rotlXor29 = ThreefishEngine.rotlXor(rotlXor25, 8, n70);
                    final long n71 = n68 + rotlXor28;
                    final long rotlXor30 = ThreefishEngine.rotlXor(rotlXor28, 35, n71);
                    final long n72 = n69 + rotlXor27;
                    final long rotlXor31 = ThreefishEngine.rotlXor(rotlXor27, 56, n72);
                    final long n73 = n66 + rotlXor26;
                    final long rotlXor32 = ThreefishEngine.rotlXor(rotlXor26, 22, n73);
                    final long n74 = kw[n40];
                    final long n75 = kw[n42];
                    final long n76 = kw[n44];
                    final long n77 = kw[n46];
                    final long n78 = kw[n48];
                    final long n79 = kw[n50];
                    final long n80 = array[n52];
                    n16 = n70 + (kw[n54] + array[n22 + 2]);
                    n17 = rotlXor30 + (kw[n21 + 8] + n56 + 1L);
                    n13 = n73 + n78;
                    n18 = rotlXor31 + (n79 + n80);
                    i += 2;
                    n9 = n71 + n74;
                    n11 = n72 + n76;
                    n19 = rotlXor29 + n75;
                    n20 = rotlXor32 + n77;
                }
                array2[0] = n9;
                array2[1] = n19;
                array2[2] = n11;
                array2[3] = n20;
                array2[4] = n13;
                array2[5] = n18;
                array2[6] = n16;
                array2[7] = n17;
                return;
            }
            throw new IllegalArgumentException();
        }
    }
    
    private abstract static class ThreefishCipher
    {
        protected final long[] kw;
        protected final long[] t;
        
        protected ThreefishCipher(final long[] kw, final long[] t) {
            this.kw = kw;
            this.t = t;
        }
        
        abstract void decryptBlock(final long[] p0, final long[] p1);
        
        abstract void encryptBlock(final long[] p0, final long[] p1);
    }
}
