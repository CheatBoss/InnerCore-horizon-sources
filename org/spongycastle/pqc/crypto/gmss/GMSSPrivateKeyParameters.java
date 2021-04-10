package org.spongycastle.pqc.crypto.gmss;

import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.lang.reflect.*;
import org.spongycastle.pqc.crypto.gmss.util.*;
import java.io.*;

public class GMSSPrivateKeyParameters extends GMSSKeyParameters
{
    private int[] K;
    private byte[][][] currentAuthPaths;
    private Vector[][] currentRetain;
    private byte[][] currentRootSig;
    private byte[][] currentSeeds;
    private Vector[] currentStack;
    private Treehash[][] currentTreehash;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSRandom gmssRandom;
    private int[] heightOfTrees;
    private int[] index;
    private byte[][][] keep;
    private int mdLength;
    private Digest messDigestTrees;
    private int[] minTreehash;
    private byte[][][] nextAuthPaths;
    private GMSSLeaf[] nextNextLeaf;
    private GMSSRootCalc[] nextNextRoot;
    private byte[][] nextNextSeeds;
    private Vector[][] nextRetain;
    private byte[][] nextRoot;
    private GMSSRootSig[] nextRootSig;
    private Vector[] nextStack;
    private Treehash[][] nextTreehash;
    private int numLayer;
    private int[] numLeafs;
    private int[] otsIndex;
    private GMSSLeaf[] upperLeaf;
    private GMSSLeaf[] upperTreehashLeaf;
    private boolean used;
    
    private GMSSPrivateKeyParameters(final GMSSPrivateKeyParameters gmssPrivateKeyParameters) {
        super(true, gmssPrivateKeyParameters.getParameters());
        this.used = false;
        this.index = Arrays.clone(gmssPrivateKeyParameters.index);
        this.currentSeeds = Arrays.clone(gmssPrivateKeyParameters.currentSeeds);
        this.nextNextSeeds = Arrays.clone(gmssPrivateKeyParameters.nextNextSeeds);
        this.currentAuthPaths = Arrays.clone(gmssPrivateKeyParameters.currentAuthPaths);
        this.nextAuthPaths = Arrays.clone(gmssPrivateKeyParameters.nextAuthPaths);
        this.currentTreehash = gmssPrivateKeyParameters.currentTreehash;
        this.nextTreehash = gmssPrivateKeyParameters.nextTreehash;
        this.currentStack = gmssPrivateKeyParameters.currentStack;
        this.nextStack = gmssPrivateKeyParameters.nextStack;
        this.currentRetain = gmssPrivateKeyParameters.currentRetain;
        this.nextRetain = gmssPrivateKeyParameters.nextRetain;
        this.keep = Arrays.clone(gmssPrivateKeyParameters.keep);
        this.nextNextLeaf = gmssPrivateKeyParameters.nextNextLeaf;
        this.upperLeaf = gmssPrivateKeyParameters.upperLeaf;
        this.upperTreehashLeaf = gmssPrivateKeyParameters.upperTreehashLeaf;
        this.minTreehash = gmssPrivateKeyParameters.minTreehash;
        this.gmssPS = gmssPrivateKeyParameters.gmssPS;
        this.nextRoot = Arrays.clone(gmssPrivateKeyParameters.nextRoot);
        this.nextNextRoot = gmssPrivateKeyParameters.nextNextRoot;
        this.currentRootSig = gmssPrivateKeyParameters.currentRootSig;
        this.nextRootSig = gmssPrivateKeyParameters.nextRootSig;
        this.digestProvider = gmssPrivateKeyParameters.digestProvider;
        this.heightOfTrees = gmssPrivateKeyParameters.heightOfTrees;
        this.otsIndex = gmssPrivateKeyParameters.otsIndex;
        this.K = gmssPrivateKeyParameters.K;
        this.numLayer = gmssPrivateKeyParameters.numLayer;
        this.messDigestTrees = gmssPrivateKeyParameters.messDigestTrees;
        this.mdLength = gmssPrivateKeyParameters.mdLength;
        this.gmssRandom = gmssPrivateKeyParameters.gmssRandom;
        this.numLeafs = gmssPrivateKeyParameters.numLeafs;
    }
    
    public GMSSPrivateKeyParameters(final int[] index, final byte[][] currentSeeds, final byte[][] nextNextSeeds, final byte[][][] currentAuthPaths, final byte[][][] nextAuthPaths, final byte[][][] keep, final Treehash[][] currentTreehash, final Treehash[][] nextTreehash, final Vector[] currentStack, final Vector[] nextStack, final Vector[][] currentRetain, final Vector[][] nextRetain, final GMSSLeaf[] nextNextLeaf, final GMSSLeaf[] upperLeaf, final GMSSLeaf[] upperTreehashLeaf, final int[] minTreehash, final byte[][] nextRoot, final GMSSRootCalc[] nextNextRoot, final byte[][] currentRootSig, final GMSSRootSig[] nextRootSig, final GMSSParameters gmssPS, final GMSSDigestProvider digestProvider) {
        super(true, gmssPS);
        this.used = false;
        final Digest value = digestProvider.get();
        this.messDigestTrees = value;
        this.mdLength = value.getDigestSize();
        this.gmssPS = gmssPS;
        this.otsIndex = gmssPS.getWinternitzParameter();
        this.K = gmssPS.getK();
        this.heightOfTrees = gmssPS.getHeightOfTrees();
        final int numOfLayers = this.gmssPS.getNumOfLayers();
        this.numLayer = numOfLayers;
        if (index == null) {
            this.index = new int[numOfLayers];
            for (int i = 0; i < this.numLayer; ++i) {
                this.index[i] = 0;
            }
        }
        else {
            this.index = index;
        }
        this.currentSeeds = currentSeeds;
        this.nextNextSeeds = nextNextSeeds;
        this.currentAuthPaths = currentAuthPaths;
        this.nextAuthPaths = nextAuthPaths;
        if (keep == null) {
            this.keep = new byte[this.numLayer][][];
            for (int j = 0; j < this.numLayer; ++j) {
                this.keep[j] = (byte[][])Array.newInstance(Byte.TYPE, (int)Math.floor(this.heightOfTrees[j] / 2), this.mdLength);
            }
        }
        else {
            this.keep = keep;
        }
        if (currentStack == null) {
            this.currentStack = new Vector[this.numLayer];
            for (int k = 0; k < this.numLayer; ++k) {
                this.currentStack[k] = new Vector();
            }
        }
        else {
            this.currentStack = currentStack;
        }
        if (nextStack == null) {
            this.nextStack = new Vector[this.numLayer - 1];
            for (int l = 0; l < this.numLayer - 1; ++l) {
                this.nextStack[l] = new Vector();
            }
        }
        else {
            this.nextStack = nextStack;
        }
        this.currentTreehash = currentTreehash;
        this.nextTreehash = nextTreehash;
        this.currentRetain = currentRetain;
        this.nextRetain = nextRetain;
        this.nextRoot = nextRoot;
        this.digestProvider = digestProvider;
        if (nextNextRoot == null) {
            this.nextNextRoot = new GMSSRootCalc[this.numLayer - 1];
            int n2;
            for (int n = 0; n < this.numLayer - 1; n = n2) {
                final GMSSRootCalc[] nextNextRoot2 = this.nextNextRoot;
                final int[] heightOfTrees = this.heightOfTrees;
                n2 = n + 1;
                nextNextRoot2[n] = new GMSSRootCalc(heightOfTrees[n2], this.K[n2], this.digestProvider);
            }
        }
        else {
            this.nextNextRoot = nextNextRoot;
        }
        this.currentRootSig = currentRootSig;
        this.numLeafs = new int[this.numLayer];
        for (int n3 = 0; n3 < this.numLayer; ++n3) {
            this.numLeafs[n3] = 1 << this.heightOfTrees[n3];
        }
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
        final int numLayer = this.numLayer;
        if (numLayer > 1) {
            if (nextNextLeaf == null) {
                this.nextNextLeaf = new GMSSLeaf[numLayer - 2];
                int n5;
                for (int n4 = 0; n4 < this.numLayer - 2; n4 = n5) {
                    final GMSSLeaf[] nextNextLeaf2 = this.nextNextLeaf;
                    final Digest value2 = digestProvider.get();
                    final int[] otsIndex = this.otsIndex;
                    n5 = n4 + 1;
                    nextNextLeaf2[n4] = new GMSSLeaf(value2, otsIndex[n5], this.numLeafs[n4 + 2], this.nextNextSeeds[n4]);
                }
            }
            else {
                this.nextNextLeaf = nextNextLeaf;
            }
        }
        else {
            this.nextNextLeaf = new GMSSLeaf[0];
        }
        if (upperLeaf == null) {
            this.upperLeaf = new GMSSLeaf[this.numLayer - 1];
            int n8;
            for (int n6 = 0; n6 < this.numLayer - 1; n6 = n8) {
                final GMSSLeaf[] upperLeaf2 = this.upperLeaf;
                final Digest value3 = digestProvider.get();
                final int n7 = this.otsIndex[n6];
                final int[] numLeafs = this.numLeafs;
                n8 = n6 + 1;
                upperLeaf2[n6] = new GMSSLeaf(value3, n7, numLeafs[n8], this.currentSeeds[n6]);
            }
        }
        else {
            this.upperLeaf = upperLeaf;
        }
        if (upperTreehashLeaf == null) {
            this.upperTreehashLeaf = new GMSSLeaf[this.numLayer - 1];
            int n11;
            for (int n9 = 0; n9 < this.numLayer - 1; n9 = n11) {
                final GMSSLeaf[] upperTreehashLeaf2 = this.upperTreehashLeaf;
                final Digest value4 = digestProvider.get();
                final int n10 = this.otsIndex[n9];
                final int[] numLeafs2 = this.numLeafs;
                n11 = n9 + 1;
                upperTreehashLeaf2[n9] = new GMSSLeaf(value4, n10, numLeafs2[n11]);
            }
        }
        else {
            this.upperTreehashLeaf = upperTreehashLeaf;
        }
        if (minTreehash == null) {
            this.minTreehash = new int[this.numLayer - 1];
            for (int n12 = 0; n12 < this.numLayer - 1; ++n12) {
                this.minTreehash[n12] = -1;
            }
        }
        else {
            this.minTreehash = minTreehash;
        }
        final int mdLength = this.mdLength;
        final byte[] array = new byte[mdLength];
        final byte[] array2 = new byte[mdLength];
        if (nextRootSig == null) {
            this.nextRootSig = new GMSSRootSig[this.numLayer - 1];
            int n15;
            for (int n13 = 0; n13 < this.numLayer - 1; n13 = n15) {
                System.arraycopy(currentSeeds[n13], 0, array, 0, this.mdLength);
                this.gmssRandom.nextSeed(array);
                final byte[] nextSeed = this.gmssRandom.nextSeed(array);
                final GMSSRootSig[] nextRootSig2 = this.nextRootSig;
                final Digest value5 = digestProvider.get();
                final int n14 = this.otsIndex[n13];
                final int[] heightOfTrees2 = this.heightOfTrees;
                n15 = n13 + 1;
                nextRootSig2[n13] = new GMSSRootSig(value5, n14, heightOfTrees2[n15]);
                this.nextRootSig[n13].initSign(nextSeed, nextRoot[n13]);
            }
        }
        else {
            this.nextRootSig = nextRootSig;
        }
    }
    
    public GMSSPrivateKeyParameters(final byte[][] array, final byte[][] array2, final byte[][][] array3, final byte[][][] array4, final Treehash[][] array5, final Treehash[][] array6, final Vector[] array7, final Vector[] array8, final Vector[][] array9, final Vector[][] array10, final byte[][] array11, final byte[][] array12, final GMSSParameters gmssParameters, final GMSSDigestProvider gmssDigestProvider) {
        this(null, array, array2, array3, array4, null, array5, array6, array7, array8, array9, array10, null, null, null, null, array11, null, array12, null, gmssParameters, gmssDigestProvider);
    }
    
    private void computeAuthPaths(final int n) {
        final int n2 = this.index[n];
        final int n3 = this.heightOfTrees[n];
        final int n4 = this.K[n];
        int n5 = 0;
        int n6;
        while (true) {
            n6 = n3 - n4;
            if (n5 >= n6) {
                break;
            }
            this.currentTreehash[n][n5].updateNextSeed(this.gmssRandom);
            ++n5;
        }
        final int heightOfPhi = this.heightOfPhi(n2);
        final byte[] array = new byte[this.mdLength];
        final byte[] nextSeed = this.gmssRandom.nextSeed(this.currentSeeds[n]);
        final int n7 = 1;
        final int n8 = n2 >>> heightOfPhi + 1 & 0x1;
        final int mdLength = this.mdLength;
        final byte[] array2 = new byte[mdLength];
        final int n9 = n3 - 1;
        if (heightOfPhi < n9 && n8 == 0) {
            System.arraycopy(this.currentAuthPaths[n][heightOfPhi], 0, array2, 0, mdLength);
        }
        final int mdLength2 = this.mdLength;
        final byte[] array3 = new byte[mdLength2];
        if (heightOfPhi == 0) {
            byte[] array4;
            if (n == this.numLayer - 1) {
                array4 = new WinternitzOTSignature(nextSeed, this.digestProvider.get(), this.otsIndex[n]).getPublicKey();
            }
            else {
                final byte[] array5 = new byte[mdLength2];
                System.arraycopy(this.currentSeeds[n], 0, array5, 0, mdLength2);
                this.gmssRandom.nextSeed(array5);
                array4 = this.upperLeaf[n].getLeaf();
                this.upperLeaf[n].initLeafCalc(array5);
            }
            System.arraycopy(array4, 0, this.currentAuthPaths[n][0], 0, this.mdLength);
        }
        else {
            final int n10 = mdLength2 << 1;
            final byte[] array6 = new byte[n10];
            final byte[][] array7 = this.currentAuthPaths[n];
            final int n11 = heightOfPhi - 1;
            System.arraycopy(array7[n11], 0, array6, 0, mdLength2);
            final byte[] array8 = this.keep[n][(int)Math.floor(n11 / 2)];
            final int mdLength3 = this.mdLength;
            System.arraycopy(array8, 0, array6, mdLength3, mdLength3);
            this.messDigestTrees.update(array6, 0, n10);
            this.currentAuthPaths[n][heightOfPhi] = new byte[this.messDigestTrees.getDigestSize()];
            this.messDigestTrees.doFinal(this.currentAuthPaths[n][heightOfPhi], 0);
            for (int i = 0; i < heightOfPhi; ++i) {
                if (i < n6) {
                    if (this.currentTreehash[n][i].wasFinished()) {
                        System.arraycopy(this.currentTreehash[n][i].getFirstNode(), 0, this.currentAuthPaths[n][i], 0, this.mdLength);
                        this.currentTreehash[n][i].destroy();
                    }
                    else {
                        final PrintStream err = System.err;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Treehash (");
                        sb.append(n);
                        sb.append(",");
                        sb.append(i);
                        sb.append(") not finished when needed in AuthPathComputation");
                        err.println(sb.toString());
                    }
                }
                if (i < n9 && i >= n6) {
                    final Vector[] array9 = this.currentRetain[n];
                    final int n12 = i - n6;
                    if (array9[n12].size() > 0) {
                        System.arraycopy(this.currentRetain[n][n12].lastElement(), 0, this.currentAuthPaths[n][i], 0, this.mdLength);
                        final Vector[][] currentRetain = this.currentRetain;
                        currentRetain[n][n12].removeElementAt(currentRetain[n][n12].size() - 1);
                    }
                }
                if (i < n6 && (1 << i) * 3 + n2 < this.numLeafs[n]) {
                    this.currentTreehash[n][i].initialize();
                }
            }
        }
        if (heightOfPhi < n9 && n8 == 0) {
            System.arraycopy(array2, 0, this.keep[n][(int)Math.floor(heightOfPhi / 2)], 0, this.mdLength);
        }
        if (n == this.numLayer - 1) {
            for (int j = n7; j <= n6 / 2; ++j) {
                final int minTreehashIndex = this.getMinTreehashIndex(n);
                if (minTreehashIndex >= 0) {
                    try {
                        final byte[] array10 = new byte[this.mdLength];
                        System.arraycopy(this.currentTreehash[n][minTreehashIndex].getSeedActive(), 0, array10, 0, this.mdLength);
                        this.currentTreehash[n][minTreehashIndex].update(this.gmssRandom, new WinternitzOTSignature(this.gmssRandom.nextSeed(array10), this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
                    }
                    catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        else {
            this.minTreehash[n] = this.getMinTreehashIndex(n);
        }
    }
    
    private int getMinTreehashIndex(final int n) {
        int i = 0;
        int n2 = -1;
        while (i < this.heightOfTrees[n] - this.K[n]) {
            int n3 = n2;
            Label_0094: {
                if (this.currentTreehash[n][i].wasInitialized()) {
                    n3 = n2;
                    if (!this.currentTreehash[n][i].wasFinished()) {
                        if (n2 != -1) {
                            n3 = n2;
                            if (this.currentTreehash[n][i].getLowestNodeHeight() >= this.currentTreehash[n][n2].getLowestNodeHeight()) {
                                break Label_0094;
                            }
                        }
                        n3 = i;
                    }
                }
            }
            ++i;
            n2 = n3;
        }
        return n2;
    }
    
    private int heightOfPhi(final int n) {
        if (n == 0) {
            return -1;
        }
        int n2;
        int n3;
        for (n2 = 1, n3 = 0; n % n2 == 0; n2 *= 2, ++n3) {}
        return n3 - 1;
    }
    
    private void nextKey(final int n) {
        if (n == this.numLayer - 1) {
            final int[] index = this.index;
            ++index[n];
        }
        if (this.index[n] == this.numLeafs[n]) {
            if (this.numLayer != 1) {
                this.nextTree(n);
                this.index[n] = 0;
            }
        }
        else {
            this.updateKey(n);
        }
    }
    
    private void nextTree(int mdLength) {
        if (mdLength > 0) {
            final int[] index = this.index;
            final int n = mdLength - 1;
            ++index[n];
            int n2 = mdLength;
            int n3 = 1;
            int i;
            int n4;
            do {
                i = n2 - 1;
                n4 = n3;
                if (this.index[i] < this.numLeafs[i]) {
                    n4 = 0;
                }
                if (n4 == 0) {
                    break;
                }
                n2 = i;
                n3 = n4;
            } while (i > 0);
            if (n4 == 0) {
                this.gmssRandom.nextSeed(this.currentSeeds[mdLength]);
                this.nextRootSig[n].updateSign();
                if (mdLength > 1) {
                    final GMSSLeaf[] nextNextLeaf = this.nextNextLeaf;
                    final int n5 = n - 1;
                    nextNextLeaf[n5] = nextNextLeaf[n5].nextLeaf();
                }
                final GMSSLeaf[] upperLeaf = this.upperLeaf;
                upperLeaf[n] = upperLeaf[n].nextLeaf();
                if (this.minTreehash[n] >= 0) {
                    final GMSSLeaf[] upperTreehashLeaf = this.upperTreehashLeaf;
                    upperTreehashLeaf[n] = upperTreehashLeaf[n].nextLeaf();
                    final byte[] leaf = this.upperTreehashLeaf[n].getLeaf();
                    try {
                        this.currentTreehash[n][this.minTreehash[n]].update(this.gmssRandom, leaf);
                        this.currentTreehash[n][this.minTreehash[n]].wasFinished();
                    }
                    catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                this.updateNextNextAuthRoot(mdLength);
                this.currentRootSig[n] = this.nextRootSig[n].getSig();
                for (int j = 0; j < this.heightOfTrees[mdLength] - this.K[mdLength]; ++j) {
                    final Treehash[] array = this.currentTreehash[mdLength];
                    final Treehash[][] nextTreehash = this.nextTreehash;
                    array[j] = nextTreehash[n][j];
                    nextTreehash[n][j] = this.nextNextRoot[n].getTreehash()[j];
                }
                for (int k = 0; k < this.heightOfTrees[mdLength]; ++k) {
                    System.arraycopy(this.nextAuthPaths[n][k], 0, this.currentAuthPaths[mdLength][k], 0, this.mdLength);
                    System.arraycopy(this.nextNextRoot[n].getAuthPath()[k], 0, this.nextAuthPaths[n][k], 0, this.mdLength);
                }
                for (int l = 0; l < this.K[mdLength] - 1; ++l) {
                    final Vector[] array2 = this.currentRetain[mdLength];
                    final Vector[][] nextRetain = this.nextRetain;
                    array2[l] = nextRetain[n][l];
                    nextRetain[n][l] = this.nextNextRoot[n].getRetain()[l];
                }
                final Vector[] currentStack = this.currentStack;
                final Vector[] nextStack = this.nextStack;
                currentStack[mdLength] = nextStack[n];
                nextStack[n] = this.nextNextRoot[n].getStack();
                this.nextRoot[n] = this.nextNextRoot[n].getRoot();
                mdLength = this.mdLength;
                final byte[] array3 = new byte[mdLength];
                final byte[] array4 = new byte[mdLength];
                System.arraycopy(this.currentSeeds[n], 0, array4, 0, mdLength);
                this.gmssRandom.nextSeed(array4);
                this.gmssRandom.nextSeed(array4);
                this.nextRootSig[n].initSign(this.gmssRandom.nextSeed(array4), this.nextRoot[n]);
                this.nextKey(n);
            }
        }
    }
    
    private void updateKey(final int n) {
        this.computeAuthPaths(n);
        if (n > 0) {
            if (n > 1) {
                final GMSSLeaf[] nextNextLeaf = this.nextNextLeaf;
                final int n2 = n - 1 - 1;
                nextNextLeaf[n2] = nextNextLeaf[n2].nextLeaf();
            }
            final GMSSLeaf[] upperLeaf = this.upperLeaf;
            final int n3 = n - 1;
            upperLeaf[n3] = upperLeaf[n3].nextLeaf();
            final double n4 = this.getNumLeafs(n) * 2;
            final double n5 = this.heightOfTrees[n3] - this.K[n3];
            Double.isNaN(n4);
            Double.isNaN(n5);
            final int n6 = (int)Math.floor(n4 / n5);
            final int[] index = this.index;
            if (index[n] % n6 == 1) {
                if (index[n] > 1 && this.minTreehash[n3] >= 0) {
                    final byte[] leaf = this.upperTreehashLeaf[n3].getLeaf();
                    try {
                        this.currentTreehash[n3][this.minTreehash[n3]].update(this.gmssRandom, leaf);
                        this.currentTreehash[n3][this.minTreehash[n3]].wasFinished();
                    }
                    catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
                this.minTreehash[n3] = this.getMinTreehashIndex(n3);
                final int[] minTreehash = this.minTreehash;
                if (minTreehash[n3] >= 0) {
                    this.upperTreehashLeaf[n3] = new GMSSLeaf(this.digestProvider.get(), this.otsIndex[n3], n6, this.currentTreehash[n3][minTreehash[n3]].getSeedActive());
                    final GMSSLeaf[] upperTreehashLeaf = this.upperTreehashLeaf;
                    upperTreehashLeaf[n3] = upperTreehashLeaf[n3].nextLeaf();
                }
            }
            else if (this.minTreehash[n3] >= 0) {
                final GMSSLeaf[] upperTreehashLeaf2 = this.upperTreehashLeaf;
                upperTreehashLeaf2[n3] = upperTreehashLeaf2[n3].nextLeaf();
            }
            this.nextRootSig[n3].updateSign();
            if (this.index[n] == 1) {
                this.nextNextRoot[n3].initialize(new Vector());
            }
            this.updateNextNextAuthRoot(n);
        }
    }
    
    private void updateNextNextAuthRoot(final int n) {
        final byte[] array = new byte[this.mdLength];
        final GMSSRandom gmssRandom = this.gmssRandom;
        final byte[][] nextNextSeeds = this.nextNextSeeds;
        final int n2 = n - 1;
        final byte[] nextSeed = gmssRandom.nextSeed(nextNextSeeds[n2]);
        if (n == this.numLayer - 1) {
            this.nextNextRoot[n2].update(this.nextNextSeeds[n2], new WinternitzOTSignature(nextSeed, this.digestProvider.get(), this.otsIndex[n]).getPublicKey());
            return;
        }
        this.nextNextRoot[n2].update(this.nextNextSeeds[n2], this.nextNextLeaf[n2].getLeaf());
        this.nextNextLeaf[n2].initLeafCalc(this.nextNextSeeds[n2]);
    }
    
    public byte[][][] getCurrentAuthPaths() {
        return Arrays.clone(this.currentAuthPaths);
    }
    
    public byte[][] getCurrentSeeds() {
        return Arrays.clone(this.currentSeeds);
    }
    
    public int getIndex(final int n) {
        return this.index[n];
    }
    
    public int[] getIndex() {
        return this.index;
    }
    
    public GMSSDigestProvider getName() {
        return this.digestProvider;
    }
    
    public int getNumLeafs(final int n) {
        return this.numLeafs[n];
    }
    
    public byte[] getSubtreeRootSig(final int n) {
        return this.currentRootSig[n];
    }
    
    public boolean isUsed() {
        return this.used;
    }
    
    public void markUsed() {
        this.used = true;
    }
    
    public GMSSPrivateKeyParameters nextKey() {
        final GMSSPrivateKeyParameters gmssPrivateKeyParameters = new GMSSPrivateKeyParameters(this);
        gmssPrivateKeyParameters.nextKey(this.gmssPS.getNumOfLayers() - 1);
        return gmssPrivateKeyParameters;
    }
}
