package org.spongycastle.pqc.crypto.gmss;

import java.util.*;
import java.lang.reflect.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.crypto.gmss.util.*;
import java.security.*;
import org.spongycastle.crypto.*;

public class GMSSKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.3";
    private int[] K;
    private byte[][] currentRootSigs;
    private byte[][] currentSeeds;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSKeyGenerationParameters gmssParams;
    private GMSSRandom gmssRandom;
    private int[] heightOfTrees;
    private boolean initialized;
    private int mdLength;
    private Digest messDigestTree;
    private byte[][] nextNextSeeds;
    private int numLayer;
    private int[] otsIndex;
    
    public GMSSKeyPairGenerator(final GMSSDigestProvider digestProvider) {
        this.initialized = false;
        this.digestProvider = digestProvider;
        final Digest value = digestProvider.get();
        this.messDigestTree = value;
        this.mdLength = value.getDigestSize();
        this.gmssRandom = new GMSSRandom(this.messDigestTree);
    }
    
    private AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        final int numLayer = this.numLayer;
        final byte[][][] array = new byte[numLayer][][];
        final int n = numLayer - 1;
        final byte[][][] array2 = new byte[n][][];
        final Treehash[][] array3 = new Treehash[numLayer][];
        final Treehash[][] array4 = new Treehash[n][];
        final Vector[] array5 = new Vector[numLayer];
        final Vector[] array6 = new Vector[n];
        Object generateNextAuthpathAndRoot = new Vector[numLayer][];
        final Vector[][] array7 = new Vector[n][];
        int n2 = 0;
        int numLayer2;
        while (true) {
            numLayer2 = this.numLayer;
            if (n2 >= numLayer2) {
                break;
            }
            array[n2] = (byte[][])Array.newInstance(Byte.TYPE, this.heightOfTrees[n2], this.mdLength);
            final Object o = this.heightOfTrees;
            array3[n2] = new Treehash[o[n2] - this.K[n2]];
            if (n2 > 0) {
                final int n3 = n2 - 1;
                array2[n3] = (byte[][])Array.newInstance(Byte.TYPE, new int[] { o[n2], this.mdLength });
                array4[n3] = new Treehash[this.heightOfTrees[n2] - this.K[n2]];
            }
            array5[n2] = new Vector();
            if (n2 > 0) {
                array6[n2 - 1] = new Vector();
            }
            ++n2;
        }
        final byte[][] array8 = (byte[][])Array.newInstance(Byte.TYPE, numLayer2, this.mdLength);
        final byte[][] array9 = (byte[][])Array.newInstance(Byte.TYPE, this.numLayer - 1, this.mdLength);
        final byte[][] array10 = (byte[][])Array.newInstance(Byte.TYPE, this.numLayer, this.mdLength);
        int n4 = 0;
        int numLayer3;
        while (true) {
            numLayer3 = this.numLayer;
            if (n4 >= numLayer3) {
                break;
            }
            System.arraycopy(this.currentSeeds[n4], 0, array10[n4], 0, this.mdLength);
            ++n4;
        }
        this.currentRootSigs = (byte[][])Array.newInstance(Byte.TYPE, numLayer3 - 1, this.mdLength);
        int n5 = this.numLayer - 1;
        Object o;
        int n6;
        int n7;
        Exception ex;
        final Exception ex2;
        Label_0481_Outer:Label_0565_Outer:
        while (true) {
            Block_12_Outer:Label_0476_Outer:
            while (true) {
                if (n5 >= 0) {
                    o = new GMSSRootCalc(this.heightOfTrees[n5], this.K[n5], this.digestProvider);
                    while (true) {
                        try {
                            if (n5 == this.numLayer - 1) {
                                o = this.generateCurrentAuthpathAndRoot(null, array5[n5], array10[n5], n5);
                            }
                            else {
                                o = this.generateCurrentAuthpathAndRoot(array8[n5 + 1], array5[n5], array10[n5], n5);
                            }
                            // iftrue(Label_0706:, n5 < 0)
                            // iftrue(Label_0644:, n7 >= this.heightOfTrees[n6])
                            while (true) {
                            Label_0605:
                                while (true) {
                                    for (int i = 0; i < this.heightOfTrees[n5]; ++i) {
                                        System.arraycopy(((GMSSRootCalc)o).getAuthPath()[i], 0, array[n5][i], 0, this.mdLength);
                                    }
                                    generateNextAuthpathAndRoot[n5] = ((GMSSRootCalc)o).getRetain();
                                    array3[n5] = ((GMSSRootCalc)o).getTreehash();
                                    System.arraycopy(((GMSSRootCalc)o).getRoot(), 0, array8[n5], 0, this.mdLength);
                                    --n5;
                                    continue Label_0481_Outer;
                                    n5 = this.numLayer - 2;
                                    o = generateNextAuthpathAndRoot;
                                    while (true) {
                                        Label_0576: {
                                            break Label_0576;
                                            Label_0644: {
                                                array7[n5] = ((GMSSRootCalc)generateNextAuthpathAndRoot).getRetain();
                                            }
                                            array4[n5] = ((GMSSRootCalc)generateNextAuthpathAndRoot).getTreehash();
                                            System.arraycopy(((GMSSRootCalc)generateNextAuthpathAndRoot).getRoot(), 0, array9[n5], 0, this.mdLength);
                                            System.arraycopy(array10[n6], 0, this.nextNextSeeds[n5], 0, this.mdLength);
                                            --n5;
                                            break Label_0576;
                                            generateNextAuthpathAndRoot = array6[n5];
                                            n6 = n5 + 1;
                                            generateNextAuthpathAndRoot = this.generateNextAuthpathAndRoot((Vector)generateNextAuthpathAndRoot, array10[n6], n6);
                                            n7 = 0;
                                            break Label_0605;
                                        }
                                        continue Block_12_Outer;
                                    }
                                    System.arraycopy(((GMSSRootCalc)generateNextAuthpathAndRoot).getAuthPath()[n7], 0, array2[n5][n7], 0, this.mdLength);
                                    ++n7;
                                    break Label_0605;
                                    ex.printStackTrace();
                                    continue Label_0565_Outer;
                                }
                                continue Label_0476_Outer;
                            }
                            Label_0706: {
                                return new AsymmetricCipherKeyPair(new GMSSPublicKeyParameters(array8[0], this.gmssPS), new GMSSPrivateKeyParameters(this.currentSeeds, this.nextNextSeeds, array, array2, array3, array4, array5, array6, (Vector[][])o, array7, array9, this.currentRootSigs, this.gmssPS, this.digestProvider));
                            }
                        }
                        catch (Exception ex2) {}
                        ex = ex2;
                        continue;
                    }
                }
                continue;
            }
        }
    }
    
    private GMSSRootCalc generateCurrentAuthpathAndRoot(byte[] array, final Vector vector, final byte[] array2, final int n) {
        final int mdLength = this.mdLength;
        final byte[] array3 = new byte[mdLength];
        final byte[] array4 = new byte[mdLength];
        final byte[] nextSeed = this.gmssRandom.nextSeed(array2);
        final GMSSRootCalc gmssRootCalc = new GMSSRootCalc(this.heightOfTrees[n], this.K[n], this.digestProvider);
        gmssRootCalc.initialize(vector);
        if (n == this.numLayer - 1) {
            array = new WinternitzOTSignature(nextSeed, this.digestProvider.get(), this.otsIndex[n]).getPublicKey();
        }
        else {
            this.currentRootSigs[n] = new WinternitzOTSignature(nextSeed, this.digestProvider.get(), this.otsIndex[n]).getSignature(array);
            array = new WinternitzOTSVerify(this.digestProvider.get(), this.otsIndex[n]).Verify(array, this.currentRootSigs[n]);
        }
        gmssRootCalc.update(array);
        int n2 = 1;
        int n3 = 3;
        int n4 = 0;
        while (true) {
            final int[] heightOfTrees = this.heightOfTrees;
            if (n2 >= 1 << heightOfTrees[n]) {
                break;
            }
            int n5 = n4;
            int n6;
            if (n2 == (n6 = n3)) {
                n5 = n4;
                n6 = n3;
                if (n4 < heightOfTrees[n] - this.K[n]) {
                    gmssRootCalc.initializeTreehashSeed(array2, n4);
                    n6 = n3 * 2;
                    n5 = n4 + 1;
                }
            }
            gmssRootCalc.update(new WinternitzOTSignature(this.gmssRandom.nextSeed(array2), this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
            ++n2;
            n4 = n5;
            n3 = n6;
        }
        if (gmssRootCalc.wasFinished()) {
            return gmssRootCalc;
        }
        System.err.println("Baum noch nicht fertig konstruiert!!!");
        return null;
    }
    
    private GMSSRootCalc generateNextAuthpathAndRoot(final Vector vector, final byte[] array, final int n) {
        final byte[] array2 = new byte[this.numLayer];
        final GMSSRootCalc gmssRootCalc = new GMSSRootCalc(this.heightOfTrees[n], this.K[n], this.digestProvider);
        gmssRootCalc.initialize(vector);
        int n2 = 0;
        int n3 = 0;
        int n4 = 3;
        while (true) {
            final int[] heightOfTrees = this.heightOfTrees;
            if (n2 >= 1 << heightOfTrees[n]) {
                break;
            }
            int n5 = n3;
            int n6;
            if (n2 == (n6 = n4)) {
                n5 = n3;
                n6 = n4;
                if (n3 < heightOfTrees[n] - this.K[n]) {
                    gmssRootCalc.initializeTreehashSeed(array, n3);
                    n6 = n4 * 2;
                    n5 = n3 + 1;
                }
            }
            gmssRootCalc.update(new WinternitzOTSignature(this.gmssRandom.nextSeed(array), this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
            ++n2;
            n3 = n5;
            n4 = n6;
        }
        if (gmssRootCalc.wasFinished()) {
            return gmssRootCalc;
        }
        System.err.println("N\ufffdchster Baum noch nicht fertig konstruiert!!!");
        return null;
    }
    
    private void initializeDefault() {
        this.initialize(new GMSSKeyGenerationParameters(new SecureRandom(), new GMSSParameters(4, new int[] { 10, 10, 10, 10 }, new int[] { 3, 3, 3, 3 }, new int[] { 2, 2, 2, 2 })));
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }
    
    public void initialize(final int n, final SecureRandom secureRandom) {
        GMSSKeyGenerationParameters gmssKeyGenerationParameters;
        if (n <= 10) {
            gmssKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(1, new int[] { 10 }, new int[] { 3 }, new int[] { 2 }));
        }
        else if (n <= 20) {
            gmssKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(2, new int[] { 10, 10 }, new int[] { 5, 4 }, new int[] { 2, 2 }));
        }
        else {
            gmssKeyGenerationParameters = new GMSSKeyGenerationParameters(secureRandom, new GMSSParameters(4, new int[] { 10, 10, 10, 10 }, new int[] { 9, 9, 9, 3 }, new int[] { 2, 2, 2, 2 }));
        }
        this.initialize(gmssKeyGenerationParameters);
    }
    
    public void initialize(final KeyGenerationParameters keyGenerationParameters) {
        this.gmssParams = (GMSSKeyGenerationParameters)keyGenerationParameters;
        final GMSSParameters gmssPS = new GMSSParameters(this.gmssParams.getParameters().getNumOfLayers(), this.gmssParams.getParameters().getHeightOfTrees(), this.gmssParams.getParameters().getWinternitzParameter(), this.gmssParams.getParameters().getK());
        this.gmssPS = gmssPS;
        this.numLayer = gmssPS.getNumOfLayers();
        this.heightOfTrees = this.gmssPS.getHeightOfTrees();
        this.otsIndex = this.gmssPS.getWinternitzParameter();
        this.K = this.gmssPS.getK();
        final int numLayer = this.numLayer;
        final int mdLength = this.mdLength;
        final Class<Byte> type = Byte.TYPE;
        int i = 0;
        this.currentSeeds = (byte[][])Array.newInstance(type, numLayer, mdLength);
        this.nextNextSeeds = (byte[][])Array.newInstance(Byte.TYPE, this.numLayer - 1, this.mdLength);
        final SecureRandom secureRandom = new SecureRandom();
        while (i < this.numLayer) {
            secureRandom.nextBytes(this.currentSeeds[i]);
            this.gmssRandom.nextSeed(this.currentSeeds[i]);
            ++i;
        }
        this.initialized = true;
    }
}
