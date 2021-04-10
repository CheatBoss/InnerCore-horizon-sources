package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;
import java.security.*;

public class TlsBlockCipher implements TlsCipher
{
    protected TlsContext context;
    protected BlockCipher decryptCipher;
    protected BlockCipher encryptCipher;
    protected boolean encryptThenMAC;
    protected byte[] randomData;
    protected TlsMac readMac;
    protected boolean useExplicitIV;
    protected TlsMac writeMac;
    
    public TlsBlockCipher(final TlsContext context, final BlockCipher blockCipher, final BlockCipher blockCipher2, final Digest digest, final Digest digest2, int n) throws IOException {
        this.context = context;
        this.randomData = new byte[256];
        context.getNonceRandomGenerator().nextBytes(this.randomData);
        this.useExplicitIV = TlsUtils.isTLSv11(context);
        this.encryptThenMAC = context.getSecurityParameters().encryptThenMAC;
        int n3;
        final int n2 = n3 = n * 2 + digest.getDigestSize() + digest2.getDigestSize();
        if (!this.useExplicitIV) {
            n3 = n2 + (blockCipher.getBlockSize() + blockCipher2.getBlockSize());
        }
        final byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(context, n3);
        final TlsMac tlsMac = new TlsMac(context, digest, calculateKeyBlock, 0, digest.getDigestSize());
        final int n4 = digest.getDigestSize() + 0;
        final TlsMac tlsMac2 = new TlsMac(context, digest2, calculateKeyBlock, n4, digest2.getDigestSize());
        final int n5 = n4 + digest2.getDigestSize();
        final KeyParameter keyParameter = new KeyParameter(calculateKeyBlock, n5, n);
        final int n6 = n5 + n;
        final KeyParameter keyParameter2 = new KeyParameter(calculateKeyBlock, n6, n);
        n += n6;
        byte[] copyOfRange;
        byte[] copyOfRange2;
        if (this.useExplicitIV) {
            copyOfRange = new byte[blockCipher.getBlockSize()];
            copyOfRange2 = new byte[blockCipher2.getBlockSize()];
        }
        else {
            copyOfRange = Arrays.copyOfRange(calculateKeyBlock, n, blockCipher.getBlockSize() + n);
            n += blockCipher.getBlockSize();
            copyOfRange2 = Arrays.copyOfRange(calculateKeyBlock, n, blockCipher2.getBlockSize() + n);
            n += blockCipher2.getBlockSize();
        }
        if (n == n3) {
            ParametersWithIV parametersWithIV;
            ParametersWithIV parametersWithIV2;
            if (context.isServer()) {
                this.writeMac = tlsMac2;
                this.readMac = tlsMac;
                this.encryptCipher = blockCipher2;
                this.decryptCipher = blockCipher;
                parametersWithIV = new ParametersWithIV(keyParameter2, copyOfRange2);
                parametersWithIV2 = new ParametersWithIV(keyParameter, copyOfRange);
            }
            else {
                this.writeMac = tlsMac;
                this.readMac = tlsMac2;
                this.encryptCipher = blockCipher;
                this.decryptCipher = blockCipher2;
                parametersWithIV = new ParametersWithIV(keyParameter, copyOfRange);
                parametersWithIV2 = new ParametersWithIV(keyParameter2, copyOfRange2);
            }
            this.encryptCipher.init(true, parametersWithIV);
            this.decryptCipher.init(false, parametersWithIV2);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected int checkPaddingConstantTime(byte[] randomData, int i, int n, int n2, int n3) {
        final int n4 = i + n;
        final byte b = randomData[n4 - 1];
        i = (b & 0xFF) + 1;
        Label_0098: {
            if ((TlsUtils.isSSL(this.context) && i > n2) || n3 + i > n) {
                n = 0;
                i = 0;
            }
            else {
                n2 = n4 - i;
                n = 0;
                while (true) {
                    n3 = n2 + 1;
                    n = (byte)((randomData[n2] ^ b) | n);
                    if (n3 >= n4) {
                        break;
                    }
                    n2 = n3;
                }
                if (n == 0) {
                    n2 = i;
                    break Label_0098;
                }
            }
            n2 = 0;
        }
        randomData = this.randomData;
        while (i < 256) {
            n = (byte)((randomData[i] ^ b) | n);
            ++i;
        }
        randomData[0] ^= (byte)n;
        return n2;
    }
    
    protected int chooseExtraPadBlocks(final SecureRandom secureRandom, final int n) {
        return Math.min(this.lowestBitSet(secureRandom.nextInt()), n);
    }
    
    @Override
    public byte[] decodeCiphertext(final long n, final short n2, final byte[] array, int max, int i) throws IOException {
        final int n3 = max;
        final int blockSize = this.decryptCipher.getBlockSize();
        final int size = this.readMac.getSize();
        if (this.encryptThenMAC) {
            max = blockSize + size;
        }
        else {
            max = Math.max(blockSize, size + 1);
        }
        int n4 = max;
        if (this.useExplicitIV) {
            n4 = max + blockSize;
        }
        if (i < n4) {
            throw new TlsFatalAlert((short)50);
        }
        int n5;
        if (this.encryptThenMAC) {
            n5 = i - size;
        }
        else {
            n5 = i;
        }
        if (n5 % blockSize != 0) {
            throw new TlsFatalAlert((short)21);
        }
        if (this.encryptThenMAC) {
            max = n3 + i;
            if (Arrays.constantTimeAreEqual(this.readMac.calculateMac(n, n2, array, n3, i - size), Arrays.copyOfRange(array, max - size, max)) ^ true) {
                throw new TlsFatalAlert((short)20);
            }
        }
        max = n3;
        int n6 = n5;
        if (this.useExplicitIV) {
            this.decryptCipher.init(false, new ParametersWithIV(null, array, n3, blockSize));
            max = n3 + blockSize;
            n6 = n5 - blockSize;
        }
        BlockCipher decryptCipher;
        int n7;
        for (i = 0; i < n6; i += blockSize) {
            decryptCipher = this.decryptCipher;
            n7 = max + i;
            decryptCipher.processBlock(array, n7, array, n7);
        }
        if (this.encryptThenMAC) {
            i = 0;
        }
        else {
            i = size;
        }
        final int checkPaddingConstantTime = this.checkPaddingConstantTime(array, max, n6, blockSize, i);
        if (checkPaddingConstantTime == 0) {
            i = 1;
        }
        else {
            i = 0;
        }
        final int n8 = n6 - checkPaddingConstantTime;
        int n12;
        if (!this.encryptThenMAC) {
            final int n9 = n8 - size;
            final int n10 = max + n9;
            final int n11 = i | ((Arrays.constantTimeAreEqual(this.readMac.calculateMacConstantTime(n, n2, array, max, n9, n6 - size, this.randomData), Arrays.copyOfRange(array, n10, n10 + size)) ^ true) ? 1 : 0);
            i = n9;
            n12 = n11;
        }
        else {
            n12 = i;
            i = n8;
        }
        if (n12 == 0) {
            return Arrays.copyOfRange(array, max, max + i);
        }
        throw new TlsFatalAlert((short)20);
    }
    
    @Override
    public byte[] encodePlaintext(final long n, final short n2, byte[] array, int length, int n3) {
        final int blockSize = this.encryptCipher.getBlockSize();
        final int size = this.writeMac.getSize();
        final ProtocolVersion serverVersion = this.context.getServerVersion();
        int n4;
        if (!this.encryptThenMAC) {
            n4 = n3 + size;
        }
        else {
            n4 = n3;
        }
        final int n5 = blockSize - 1 - n4 % blockSize;
        int n6 = 0;
        Label_0148: {
            if (!this.encryptThenMAC) {
                n6 = n5;
                if (this.context.getSecurityParameters().truncatedHMac) {
                    break Label_0148;
                }
            }
            n6 = n5;
            if (!serverVersion.isDTLS()) {
                n6 = n5;
                if (!serverVersion.isSSL()) {
                    n6 = n5 + this.chooseExtraPadBlocks(this.context.getSecureRandom(), (255 - n5) / blockSize) * blockSize;
                }
            }
        }
        int n7 = size + n3 + n6 + 1;
        if (this.useExplicitIV) {
            n7 += blockSize;
        }
        final byte[] array2 = new byte[n7];
        int n8;
        if (this.useExplicitIV) {
            final byte[] array3 = new byte[blockSize];
            this.context.getNonceRandomGenerator().nextBytes(array3);
            this.encryptCipher.init(true, new ParametersWithIV(null, array3));
            System.arraycopy(array3, 0, array2, 0, blockSize);
            n8 = blockSize + 0;
        }
        else {
            n8 = 0;
        }
        final int n9 = length;
        System.arraycopy(array, n9, array2, n8, n3);
        final int n10 = length = n8 + n3;
        if (!this.encryptThenMAC) {
            array = this.writeMac.calculateMac(n, n2, array, n9, n3);
            System.arraycopy(array, 0, array2, n10, array.length);
            length = n10 + array.length;
        }
        n3 = 0;
        int i;
        while (true) {
            i = n8;
            if (n3 > n6) {
                break;
            }
            array2[length] = (byte)n6;
            ++n3;
            ++length;
        }
        while (i < length) {
            this.encryptCipher.processBlock(array2, i, array2, i);
            i += blockSize;
        }
        if (this.encryptThenMAC) {
            array = this.writeMac.calculateMac(n, n2, array2, 0, length);
            System.arraycopy(array, 0, array2, length, array.length);
            length = array.length;
            return array2;
        }
        return array2;
    }
    
    @Override
    public int getPlaintextLimit(int n) {
        final int blockSize = this.encryptCipher.getBlockSize();
        final int size = this.writeMac.getSize();
        int n2 = n;
        if (this.useExplicitIV) {
            n2 = n - blockSize;
        }
        if (this.encryptThenMAC) {
            n = n2 - size;
            n -= n % blockSize;
        }
        else {
            n = n2 - n2 % blockSize - size;
        }
        return n - 1;
    }
    
    public TlsMac getReadMac() {
        return this.readMac;
    }
    
    public TlsMac getWriteMac() {
        return this.writeMac;
    }
    
    protected int lowestBitSet(int n) {
        if (n == 0) {
            return 32;
        }
        int n2 = 0;
        while ((n & 0x1) == 0x0) {
            ++n2;
            n >>= 1;
        }
        return n2;
    }
}
