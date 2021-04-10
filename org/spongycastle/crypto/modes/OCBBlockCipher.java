package org.spongycastle.crypto.modes;

import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class OCBBlockCipher implements AEADBlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private byte[] Checksum;
    private byte[] KtopInput;
    private Vector L;
    private byte[] L_Asterisk;
    private byte[] L_Dollar;
    private byte[] OffsetHASH;
    private byte[] OffsetMAIN;
    private byte[] OffsetMAIN_0;
    private byte[] Stretch;
    private byte[] Sum;
    private boolean forEncryption;
    private byte[] hashBlock;
    private long hashBlockCount;
    private int hashBlockPos;
    private BlockCipher hashCipher;
    private byte[] initialAssociatedText;
    private byte[] macBlock;
    private int macSize;
    private byte[] mainBlock;
    private long mainBlockCount;
    private int mainBlockPos;
    private BlockCipher mainCipher;
    
    public OCBBlockCipher(final BlockCipher hashCipher, final BlockCipher mainCipher) {
        this.KtopInput = null;
        this.Stretch = new byte[24];
        this.OffsetMAIN_0 = new byte[16];
        this.OffsetMAIN = new byte[16];
        if (hashCipher == null) {
            throw new IllegalArgumentException("'hashCipher' cannot be null");
        }
        if (hashCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("'hashCipher' must have a block size of 16");
        }
        if (mainCipher == null) {
            throw new IllegalArgumentException("'mainCipher' cannot be null");
        }
        if (mainCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("'mainCipher' must have a block size of 16");
        }
        if (hashCipher.getAlgorithmName().equals(mainCipher.getAlgorithmName())) {
            this.hashCipher = hashCipher;
            this.mainCipher = mainCipher;
            return;
        }
        throw new IllegalArgumentException("'hashCipher' and 'mainCipher' must be the same algorithm");
    }
    
    protected static byte[] OCB_double(final byte[] array) {
        final byte[] array2 = new byte[16];
        array2[15] ^= (byte)(135 >>> (1 - shiftLeft(array, array2) << 3));
        return array2;
    }
    
    protected static void OCB_extend(final byte[] array, int n) {
        array[n] = -128;
        while (true) {
            ++n;
            if (n >= 16) {
                break;
            }
            array[n] = 0;
        }
    }
    
    protected static int OCB_ntz(long n) {
        if (n == 0L) {
            return 64;
        }
        int n2 = 0;
        while ((n & 0x1L) == 0x0L) {
            ++n2;
            n >>>= 1;
        }
        return n2;
    }
    
    protected static int shiftLeft(final byte[] array, final byte[] array2) {
        int n = 16;
        int n2 = 0;
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n3 = array[n] & 0xFF;
            array2[n] = (byte)(n2 | n3 << 1);
            n2 = (n3 >>> 7 & 0x1);
        }
        return n2;
    }
    
    protected static void xor(final byte[] array, final byte[] array2) {
        for (int i = 15; i >= 0; --i) {
            array[i] ^= array2[i];
        }
    }
    
    protected void clear(final byte[] array) {
        if (array != null) {
            Arrays.fill(array, (byte)0);
        }
    }
    
    @Override
    public int doFinal(final byte[] array, int n) throws IllegalStateException, InvalidCipherTextException {
        byte[] array2;
        if (!this.forEncryption) {
            final int mainBlockPos = this.mainBlockPos;
            final int macSize = this.macSize;
            if (mainBlockPos < macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            final int mainBlockPos2 = mainBlockPos - macSize;
            this.mainBlockPos = mainBlockPos2;
            array2 = new byte[macSize];
            System.arraycopy(this.mainBlock, mainBlockPos2, array2, 0, macSize);
        }
        else {
            array2 = null;
        }
        final int hashBlockPos = this.hashBlockPos;
        if (hashBlockPos > 0) {
            OCB_extend(this.hashBlock, hashBlockPos);
            this.updateHASH(this.L_Asterisk);
        }
        final int mainBlockPos3 = this.mainBlockPos;
        if (mainBlockPos3 > 0) {
            if (this.forEncryption) {
                OCB_extend(this.mainBlock, mainBlockPos3);
                xor(this.Checksum, this.mainBlock);
            }
            xor(this.OffsetMAIN, this.L_Asterisk);
            final byte[] array3 = new byte[16];
            this.hashCipher.processBlock(this.OffsetMAIN, 0, array3, 0);
            xor(this.mainBlock, array3);
            final int length = array.length;
            final int mainBlockPos4 = this.mainBlockPos;
            if (length < n + mainBlockPos4) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy(this.mainBlock, 0, array, n, mainBlockPos4);
            if (!this.forEncryption) {
                OCB_extend(this.mainBlock, this.mainBlockPos);
                xor(this.Checksum, this.mainBlock);
            }
        }
        xor(this.Checksum, this.OffsetMAIN);
        xor(this.Checksum, this.L_Dollar);
        final BlockCipher hashCipher = this.hashCipher;
        final byte[] checksum = this.Checksum;
        hashCipher.processBlock(checksum, 0, checksum, 0);
        xor(this.Checksum, this.Sum);
        final int macSize2 = this.macSize;
        final byte[] macBlock = new byte[macSize2];
        this.macBlock = macBlock;
        System.arraycopy(this.Checksum, 0, macBlock, 0, macSize2);
        final int mainBlockPos5 = this.mainBlockPos;
        if (this.forEncryption) {
            final int length2 = array.length;
            n += mainBlockPos5;
            final int macSize3 = this.macSize;
            if (length2 < n + macSize3) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy(this.macBlock, 0, array, n, macSize3);
            n = mainBlockPos5 + this.macSize;
        }
        else {
            if (!Arrays.constantTimeAreEqual(this.macBlock, array2)) {
                throw new InvalidCipherTextException("mac check in OCB failed");
            }
            n = mainBlockPos5;
        }
        this.reset(false);
        return n;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mainCipher.getAlgorithmName());
        sb.append("/OCB");
        return sb.toString();
    }
    
    protected byte[] getLSub(final int i) {
        while (i >= this.L.size()) {
            final Vector l = this.L;
            l.addElement(OCB_double(l.lastElement()));
        }
        return this.L.elementAt(i);
    }
    
    @Override
    public byte[] getMac() {
        final byte[] macBlock = this.macBlock;
        if (macBlock == null) {
            return new byte[this.macSize];
        }
        return Arrays.clone(macBlock);
    }
    
    @Override
    public int getOutputSize(int n) {
        n += this.mainBlockPos;
        if (this.forEncryption) {
            return n + this.macSize;
        }
        final int macSize = this.macSize;
        if (n < macSize) {
            return 0;
        }
        return n - macSize;
    }
    
    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.mainCipher;
    }
    
    @Override
    public int getUpdateOutputSize(int macSize) {
        final int n = macSize += this.mainBlockPos;
        if (!this.forEncryption) {
            macSize = this.macSize;
            if (n < macSize) {
                return 0;
            }
            macSize = n - macSize;
        }
        return macSize - macSize % 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        final boolean forEncryption2 = this.forEncryption;
        this.forEncryption = forEncryption;
        this.macBlock = null;
        byte[] array;
        KeyParameter key;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            array = aeadParameters.getNonce();
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            final int macSize = aeadParameters.getMacSize();
            if (macSize < 64 || macSize > 128 || macSize % 8 != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid value for MAC size: ");
                sb.append(macSize);
                throw new IllegalArgumentException(sb.toString());
            }
            this.macSize = macSize / 8;
            key = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to OCB");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            array = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = 16;
            key = (KeyParameter)parametersWithIV.getParameters();
        }
        this.hashBlock = new byte[16];
        int n;
        if (forEncryption) {
            n = 16;
        }
        else {
            n = this.macSize + 16;
        }
        this.mainBlock = new byte[n];
        byte[] array2 = array;
        if (array == null) {
            array2 = new byte[0];
        }
        if (array2.length <= 15) {
            if (key != null) {
                this.hashCipher.init(true, key);
                this.mainCipher.init(forEncryption, key);
                this.KtopInput = null;
            }
            else if (forEncryption2 != forEncryption) {
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
            final byte[] l_Asterisk = new byte[16];
            this.L_Asterisk = l_Asterisk;
            this.hashCipher.processBlock(l_Asterisk, 0, l_Asterisk, 0);
            this.L_Dollar = OCB_double(this.L_Asterisk);
            (this.L = new Vector()).addElement(OCB_double(this.L_Dollar));
            final int processNonce = this.processNonce(array2);
            final int n2 = processNonce % 8;
            int n3 = processNonce / 8;
            if (n2 == 0) {
                System.arraycopy(this.Stretch, n3, this.OffsetMAIN_0, 0, 16);
            }
            else {
                for (int i = 0; i < 16; ++i) {
                    final byte[] stretch = this.Stretch;
                    final byte b = stretch[n3];
                    ++n3;
                    this.OffsetMAIN_0[i] = (byte)((stretch[n3] & 0xFF) >>> 8 - n2 | (b & 0xFF) << n2);
                }
            }
            this.hashBlockPos = 0;
            this.mainBlockPos = 0;
            this.hashBlockCount = 0L;
            this.mainBlockCount = 0L;
            this.OffsetHASH = new byte[16];
            this.Sum = new byte[16];
            System.arraycopy(this.OffsetMAIN_0, 0, this.OffsetMAIN, 0, 16);
            this.Checksum = new byte[16];
            final byte[] initialAssociatedText = this.initialAssociatedText;
            if (initialAssociatedText != null) {
                this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
            }
            return;
        }
        throw new IllegalArgumentException("IV must be no more than 15 bytes");
    }
    
    @Override
    public void processAADByte(final byte b) {
        final byte[] hashBlock = this.hashBlock;
        final int hashBlockPos = this.hashBlockPos;
        hashBlock[hashBlockPos] = b;
        final int hashBlockPos2 = hashBlockPos + 1;
        this.hashBlockPos = hashBlockPos2;
        if (hashBlockPos2 == hashBlock.length) {
            this.processHashBlock();
        }
    }
    
    @Override
    public void processAADBytes(final byte[] array, final int n, final int n2) {
        for (int i = 0; i < n2; ++i) {
            final byte[] hashBlock = this.hashBlock;
            final int hashBlockPos = this.hashBlockPos;
            hashBlock[hashBlockPos] = array[n + i];
            if ((this.hashBlockPos = hashBlockPos + 1) == hashBlock.length) {
                this.processHashBlock();
            }
        }
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
        final byte[] mainBlock = this.mainBlock;
        final int mainBlockPos = this.mainBlockPos;
        mainBlock[mainBlockPos] = b;
        final int mainBlockPos2 = mainBlockPos + 1;
        this.mainBlockPos = mainBlockPos2;
        if (mainBlockPos2 == mainBlock.length) {
            this.processMainBlock(array, n);
            return 16;
        }
        return 0;
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        if (array.length >= n + n2) {
            int i = 0;
            int n4 = 0;
            while (i < n2) {
                final byte[] mainBlock = this.mainBlock;
                final int mainBlockPos = this.mainBlockPos;
                mainBlock[mainBlockPos] = array[n + i];
                final int mainBlockPos2 = mainBlockPos + 1;
                this.mainBlockPos = mainBlockPos2;
                int n5 = n4;
                if (mainBlockPos2 == mainBlock.length) {
                    this.processMainBlock(array2, n3 + n4);
                    n5 = n4 + 16;
                }
                ++i;
                n4 = n5;
            }
            return n4;
        }
        throw new DataLengthException("Input buffer too short");
    }
    
    protected void processHashBlock() {
        final long hashBlockCount = this.hashBlockCount + 1L;
        this.hashBlockCount = hashBlockCount;
        this.updateHASH(this.getLSub(OCB_ntz(hashBlockCount)));
        this.hashBlockPos = 0;
    }
    
    protected void processMainBlock(byte[] mainBlock, final int n) {
        if (mainBlock.length >= n + 16) {
            if (this.forEncryption) {
                xor(this.Checksum, this.mainBlock);
                this.mainBlockPos = 0;
            }
            final byte[] offsetMAIN = this.OffsetMAIN;
            final long mainBlockCount = this.mainBlockCount + 1L;
            this.mainBlockCount = mainBlockCount;
            xor(offsetMAIN, this.getLSub(OCB_ntz(mainBlockCount)));
            xor(this.mainBlock, this.OffsetMAIN);
            final BlockCipher mainCipher = this.mainCipher;
            final byte[] mainBlock2 = this.mainBlock;
            mainCipher.processBlock(mainBlock2, 0, mainBlock2, 0);
            xor(this.mainBlock, this.OffsetMAIN);
            System.arraycopy(this.mainBlock, 0, mainBlock, n, 16);
            if (!this.forEncryption) {
                xor(this.Checksum, this.mainBlock);
                mainBlock = this.mainBlock;
                System.arraycopy(mainBlock, 16, mainBlock, 0, this.macSize);
                this.mainBlockPos = this.macSize;
            }
            return;
        }
        throw new OutputLengthException("Output buffer too short");
    }
    
    protected int processNonce(byte[] ktopInput) {
        final byte[] ktopInput2 = new byte[16];
        final int length = ktopInput.length;
        final int length2 = ktopInput.length;
        int i = 0;
        System.arraycopy(ktopInput, 0, ktopInput2, 16 - length, length2);
        ktopInput2[0] = (byte)(this.macSize << 4);
        final int n = 15 - ktopInput.length;
        ktopInput2[n] |= 0x1;
        final byte b = ktopInput2[15];
        ktopInput2[15] &= (byte)192;
        ktopInput = this.KtopInput;
        if (ktopInput == null || !Arrays.areEqual(ktopInput2, ktopInput)) {
            ktopInput = new byte[16];
            this.KtopInput = ktopInput2;
            this.hashCipher.processBlock(ktopInput2, 0, ktopInput, 0);
            System.arraycopy(ktopInput, 0, this.Stretch, 0, 16);
            while (i < 8) {
                final byte[] stretch = this.Stretch;
                final byte b2 = ktopInput[i];
                final int n2 = i + 1;
                stretch[i + 16] = (byte)(b2 ^ ktopInput[n2]);
                i = n2;
            }
        }
        return b & 0x3F;
    }
    
    @Override
    public void reset() {
        this.reset(true);
    }
    
    protected void reset(final boolean b) {
        this.hashCipher.reset();
        this.mainCipher.reset();
        this.clear(this.hashBlock);
        this.clear(this.mainBlock);
        this.hashBlockPos = 0;
        this.mainBlockPos = 0;
        this.hashBlockCount = 0L;
        this.mainBlockCount = 0L;
        this.clear(this.OffsetHASH);
        this.clear(this.Sum);
        System.arraycopy(this.OffsetMAIN_0, 0, this.OffsetMAIN, 0, 16);
        this.clear(this.Checksum);
        if (b) {
            this.macBlock = null;
        }
        final byte[] initialAssociatedText = this.initialAssociatedText;
        if (initialAssociatedText != null) {
            this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
        }
    }
    
    protected void updateHASH(final byte[] array) {
        xor(this.OffsetHASH, array);
        xor(this.hashBlock, this.OffsetHASH);
        final BlockCipher hashCipher = this.hashCipher;
        final byte[] hashBlock = this.hashBlock;
        hashCipher.processBlock(hashBlock, 0, hashBlock, 0);
        xor(this.Sum, this.hashBlock);
    }
}
