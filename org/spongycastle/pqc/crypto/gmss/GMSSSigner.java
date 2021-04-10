package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import java.lang.reflect.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.crypto.gmss.util.*;
import org.spongycastle.util.*;

public class GMSSSigner implements MessageSigner
{
    private byte[][][] currentAuthPaths;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSRandom gmssRandom;
    private GMSSUtil gmssUtil;
    private int[] index;
    GMSSKeyParameters key;
    private int mdLength;
    private Digest messDigestOTS;
    private Digest messDigestTrees;
    private int numLayer;
    private WinternitzOTSignature ots;
    private byte[] pubKeyBytes;
    private SecureRandom random;
    private byte[][] subtreeRootSig;
    
    public GMSSSigner(final GMSSDigestProvider digestProvider) {
        this.gmssUtil = new GMSSUtil();
        this.digestProvider = digestProvider;
        final Digest value = digestProvider.get();
        this.messDigestTrees = value;
        this.messDigestOTS = value;
        this.mdLength = value.getDigestSize();
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
    }
    
    private void initSign() {
        this.messDigestTrees.reset();
        final GMSSPrivateKeyParameters gmssPrivateKeyParameters = (GMSSPrivateKeyParameters)this.key;
        if (gmssPrivateKeyParameters.isUsed()) {
            throw new IllegalStateException("Private key already used");
        }
        if (gmssPrivateKeyParameters.getIndex(0) < gmssPrivateKeyParameters.getNumLeafs(0)) {
            final GMSSParameters parameters = gmssPrivateKeyParameters.getParameters();
            this.gmssPS = parameters;
            this.numLayer = parameters.getNumOfLayers();
            final byte[] array = gmssPrivateKeyParameters.getCurrentSeeds()[this.numLayer - 1];
            final int mdLength = this.mdLength;
            final byte[] array2 = new byte[mdLength];
            final byte[] array3 = new byte[mdLength];
            System.arraycopy(array, 0, array3, 0, mdLength);
            this.ots = new WinternitzOTSignature(this.gmssRandom.nextSeed(array3), this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[this.numLayer - 1]);
            final byte[][][] currentAuthPaths = gmssPrivateKeyParameters.getCurrentAuthPaths();
            this.currentAuthPaths = new byte[this.numLayer][][];
            int n = 0;
            int numLayer;
            while (true) {
                numLayer = this.numLayer;
                if (n >= numLayer) {
                    break;
                }
                this.currentAuthPaths[n] = (byte[][])Array.newInstance(Byte.TYPE, currentAuthPaths[n].length, this.mdLength);
                for (int i = 0; i < currentAuthPaths[n].length; ++i) {
                    System.arraycopy(currentAuthPaths[n][i], 0, this.currentAuthPaths[n][i], 0, this.mdLength);
                }
                ++n;
            }
            this.index = new int[numLayer];
            System.arraycopy(gmssPrivateKeyParameters.getIndex(), 0, this.index, 0, this.numLayer);
            this.subtreeRootSig = new byte[this.numLayer - 1][];
            for (int j = 0; j < this.numLayer - 1; ++j) {
                final byte[] subtreeRootSig = gmssPrivateKeyParameters.getSubtreeRootSig(j);
                System.arraycopy(subtreeRootSig, 0, this.subtreeRootSig[j] = new byte[subtreeRootSig.length], 0, subtreeRootSig.length);
            }
            gmssPrivateKeyParameters.markUsed();
            return;
        }
        throw new IllegalStateException("No more signatures can be generated");
    }
    
    private void initVerify() {
        this.messDigestTrees.reset();
        final GMSSPublicKeyParameters gmssPublicKeyParameters = (GMSSPublicKeyParameters)this.key;
        this.pubKeyBytes = gmssPublicKeyParameters.getPublicKey();
        final GMSSParameters parameters = gmssPublicKeyParameters.getParameters();
        this.gmssPS = parameters;
        this.numLayer = parameters.getNumOfLayers();
    }
    
    @Override
    public byte[] generateSignature(byte[] signature) {
        final byte[] array = new byte[this.mdLength];
        signature = this.ots.getSignature(signature);
        final byte[] concatenateArray = this.gmssUtil.concatenateArray(this.currentAuthPaths[this.numLayer - 1]);
        final byte[] intToBytesLittleEndian = this.gmssUtil.intToBytesLittleEndian(this.index[this.numLayer - 1]);
        final int n = intToBytesLittleEndian.length + signature.length + concatenateArray.length;
        final byte[] array2 = new byte[n];
        System.arraycopy(intToBytesLittleEndian, 0, array2, 0, intToBytesLittleEndian.length);
        System.arraycopy(signature, 0, array2, intToBytesLittleEndian.length, signature.length);
        System.arraycopy(concatenateArray, 0, array2, intToBytesLittleEndian.length + signature.length, concatenateArray.length);
        signature = new byte[0];
        int n2 = this.numLayer - 1;
        while (true) {
            --n2;
            if (n2 < 0) {
                break;
            }
            final byte[] concatenateArray2 = this.gmssUtil.concatenateArray(this.currentAuthPaths[n2]);
            final byte[] intToBytesLittleEndian2 = this.gmssUtil.intToBytesLittleEndian(this.index[n2]);
            final int length = signature.length;
            final byte[] array3 = new byte[length];
            System.arraycopy(signature, 0, array3, 0, signature.length);
            signature = new byte[intToBytesLittleEndian2.length + length + this.subtreeRootSig[n2].length + concatenateArray2.length];
            System.arraycopy(array3, 0, signature, 0, length);
            System.arraycopy(intToBytesLittleEndian2, 0, signature, length, intToBytesLittleEndian2.length);
            final byte[][] subtreeRootSig = this.subtreeRootSig;
            System.arraycopy(subtreeRootSig[n2], 0, signature, intToBytesLittleEndian2.length + length, subtreeRootSig[n2].length);
            System.arraycopy(concatenateArray2, 0, signature, length + intToBytesLittleEndian2.length + this.subtreeRootSig[n2].length, concatenateArray2.length);
        }
        final byte[] array4 = new byte[signature.length + n];
        System.arraycopy(array2, 0, array4, 0, n);
        System.arraycopy(signature, 0, array4, n, signature.length);
        return array4;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!b) {
            this.key = (GMSSPublicKeyParameters)cipherParameters;
            this.initVerify();
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (GMSSPrivateKeyParameters)parametersWithRandom.getParameters();
            this.initSign();
            return;
        }
        this.random = new SecureRandom();
        this.key = (GMSSPrivateKeyParameters)cipherParameters;
        this.initSign();
    }
    
    @Override
    public boolean verifySignature(byte[] verify, final byte[] array) {
        this.messDigestOTS.reset();
        int i = this.numLayer - 1;
        int n = 0;
        while (i >= 0) {
            final WinternitzOTSVerify winternitzOTSVerify = new WinternitzOTSVerify(this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[i]);
            final int signatureLength = winternitzOTSVerify.getSignatureLength();
            final int bytesToIntLittleEndian = this.gmssUtil.bytesToIntLittleEndian(array, n);
            final int n2 = n + 4;
            final byte[] array2 = new byte[signatureLength];
            System.arraycopy(array, n2, array2, 0, signatureLength);
            verify = winternitzOTSVerify.Verify(verify, array2);
            if (verify == null) {
                System.err.println("OTS Public Key is null in GMSSSignature.verify");
                return false;
            }
            final byte[][] array3 = (byte[][])Array.newInstance(Byte.TYPE, this.gmssPS.getHeightOfTrees()[i], this.mdLength);
            n = n2 + signatureLength;
            for (int j = 0; j < array3.length; ++j) {
                System.arraycopy(array, n, array3[j], 0, this.mdLength);
                n += this.mdLength;
            }
            final byte[] array4 = new byte[this.mdLength];
            int n3 = (1 << array3.length) + bytesToIntLittleEndian;
            for (int k = 0; k < array3.length; ++k) {
                final int mdLength = this.mdLength;
                final int n4 = mdLength << 1;
                final byte[] array5 = new byte[n4];
                if (n3 % 2 == 0) {
                    System.arraycopy(verify, 0, array5, 0, mdLength);
                    verify = array3[k];
                    final int mdLength2 = this.mdLength;
                    System.arraycopy(verify, 0, array5, mdLength2, mdLength2);
                    n3 /= 2;
                }
                else {
                    System.arraycopy(array3[k], 0, array5, 0, mdLength);
                    System.arraycopy(verify, 0, array5, this.mdLength, verify.length);
                    n3 = (n3 - 1) / 2;
                }
                this.messDigestTrees.update(array5, 0, n4);
                verify = new byte[this.messDigestTrees.getDigestSize()];
                this.messDigestTrees.doFinal(verify, 0);
            }
            --i;
        }
        return Arrays.areEqual(this.pubKeyBytes, verify);
    }
}
