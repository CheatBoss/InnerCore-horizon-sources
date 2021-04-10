package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.crypto.*;
import java.lang.reflect.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.util.encoders.*;

public class GMSSRootCalc
{
    private byte[][] AuthPath;
    private int K;
    private GMSSDigestProvider digestProvider;
    private int heightOfNextSeed;
    private Vector heightOfNodes;
    private int heightOfTree;
    private int[] index;
    private int indexForNextSeed;
    private boolean isFinished;
    private boolean isInitialized;
    private int mdLength;
    private Digest messDigestTree;
    private Vector[] retain;
    private byte[] root;
    private Vector tailStack;
    private Treehash[] treehash;
    
    public GMSSRootCalc(int i, final int k, final GMSSDigestProvider digestProvider) {
        this.heightOfTree = i;
        this.digestProvider = digestProvider;
        final Digest value = digestProvider.get();
        this.messDigestTree = value;
        final int digestSize = value.getDigestSize();
        this.mdLength = digestSize;
        this.K = k;
        this.index = new int[i];
        final Class<Byte> type = Byte.TYPE;
        final int n = 0;
        this.AuthPath = (byte[][])Array.newInstance(type, i, digestSize);
        this.root = new byte[this.mdLength];
        this.retain = new Vector[this.K - 1];
        for (i = n; i < k - 1; ++i) {
            this.retain[i] = new Vector();
        }
    }
    
    public GMSSRootCalc(final Digest digest, final byte[][] array, final int[] array2, final Treehash[] array3, final Vector[] array4) {
        this.messDigestTree = this.digestProvider.get();
        this.digestProvider = this.digestProvider;
        final int n = 0;
        this.heightOfTree = array2[0];
        this.mdLength = array2[1];
        this.K = array2[2];
        this.indexForNextSeed = array2[3];
        this.heightOfNextSeed = array2[4];
        if (array2[5] == 1) {
            this.isFinished = true;
        }
        else {
            this.isFinished = false;
        }
        if (array2[6] == 1) {
            this.isInitialized = true;
        }
        else {
            this.isInitialized = false;
        }
        final int n2 = array2[7];
        this.index = new int[this.heightOfTree];
        for (int i = 0; i < this.heightOfTree; ++i) {
            this.index[i] = array2[i + 8];
        }
        this.heightOfNodes = new Vector();
        for (int j = 0; j < n2; ++j) {
            this.heightOfNodes.addElement(Integers.valueOf(array2[this.heightOfTree + 8 + j]));
        }
        this.root = array[0];
        this.AuthPath = (byte[][])Array.newInstance(Byte.TYPE, this.heightOfTree, this.mdLength);
        int n3;
        for (int k = 0; k < this.heightOfTree; k = n3) {
            final byte[][] authPath = this.AuthPath;
            n3 = k + 1;
            authPath[k] = array[n3];
        }
        this.tailStack = new Vector();
        for (int l = n; l < n2; ++l) {
            this.tailStack.addElement(array[this.heightOfTree + 1 + l]);
        }
        this.treehash = GMSSUtils.clone(array3);
        this.retain = GMSSUtils.clone(array4);
    }
    
    public byte[][] getAuthPath() {
        return GMSSUtils.clone(this.AuthPath);
    }
    
    public Vector[] getRetain() {
        return GMSSUtils.clone(this.retain);
    }
    
    public byte[] getRoot() {
        return Arrays.clone(this.root);
    }
    
    public Vector getStack() {
        final Vector<Object> vector = new Vector<Object>();
        final Enumeration<Object> elements = this.tailStack.elements();
        while (elements.hasMoreElements()) {
            vector.addElement(elements.nextElement());
        }
        return vector;
    }
    
    public byte[][] getStatByte() {
        final Vector tailStack = this.tailStack;
        final int n = 0;
        int size;
        if (tailStack == null) {
            size = 0;
        }
        else {
            size = tailStack.size();
        }
        final byte[][] array = (byte[][])Array.newInstance(Byte.TYPE, this.heightOfTree + 1 + size, 64);
        array[0] = this.root;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= this.heightOfTree) {
                break;
            }
            final int n3 = n2 + 1;
            array[n3] = this.AuthPath[n2];
            n2 = n3;
        }
        while (i < size) {
            array[this.heightOfTree + 1 + i] = (byte[])this.tailStack.elementAt(i);
            ++i;
        }
        return array;
    }
    
    public int[] getStatInt() {
        final Vector tailStack = this.tailStack;
        final int n = 0;
        int size;
        if (tailStack == null) {
            size = 0;
        }
        else {
            size = tailStack.size();
        }
        final int heightOfTree = this.heightOfTree;
        final int[] array = new int[heightOfTree + 8 + size];
        array[0] = heightOfTree;
        array[1] = this.mdLength;
        array[2] = this.K;
        array[3] = this.indexForNextSeed;
        array[4] = this.heightOfNextSeed;
        if (this.isFinished) {
            array[5] = 1;
        }
        else {
            array[5] = 0;
        }
        if (this.isInitialized) {
            array[6] = 1;
        }
        else {
            array[6] = 0;
        }
        array[7] = size;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= this.heightOfTree) {
                break;
            }
            array[n2 + 8] = this.index[n2];
            ++n2;
        }
        while (i < size) {
            array[this.heightOfTree + 8 + i] = (int)this.heightOfNodes.elementAt(i);
            ++i;
        }
        return array;
    }
    
    public Treehash[] getTreehash() {
        return GMSSUtils.clone(this.treehash);
    }
    
    public void initialize(final Vector vector) {
        this.treehash = new Treehash[this.heightOfTree - this.K];
        int n = 0;
        int heightOfTree;
        while (true) {
            heightOfTree = this.heightOfTree;
            if (n >= heightOfTree - this.K) {
                break;
            }
            this.treehash[n] = new Treehash(vector, n, this.digestProvider.get());
            ++n;
        }
        this.index = new int[heightOfTree];
        this.AuthPath = (byte[][])Array.newInstance(Byte.TYPE, heightOfTree, this.mdLength);
        this.root = new byte[this.mdLength];
        this.tailStack = new Vector();
        this.heightOfNodes = new Vector();
        this.isInitialized = true;
        this.isFinished = false;
        for (int i = 0; i < this.heightOfTree; ++i) {
            this.index[i] = -1;
        }
        this.retain = new Vector[this.K - 1];
        for (int j = 0; j < this.K - 1; ++j) {
            this.retain[j] = new Vector();
        }
        this.indexForNextSeed = 3;
        this.heightOfNextSeed = 0;
    }
    
    public void initializeTreehashSeed(final byte[] array, final int n) {
        this.treehash[n].initializeSeed(array);
    }
    
    @Override
    public String toString() {
        final Vector tailStack = this.tailStack;
        final int n = 0;
        int size;
        if (tailStack == null) {
            size = 0;
        }
        else {
            size = tailStack.size();
        }
        String string = "";
        int n2 = 0;
        int i;
        String string2;
        while (true) {
            i = n;
            string2 = string;
            if (n2 >= this.heightOfTree + 8 + size) {
                break;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.getStatInt()[n2]);
            sb.append(" ");
            string = sb.toString();
            ++n2;
        }
        while (i < this.heightOfTree + 1 + size) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(new String(Hex.encode(this.getStatByte()[i])));
            sb2.append(" ");
            string2 = sb2.toString();
            ++i;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        sb3.append("  ");
        sb3.append(this.digestProvider.get().getDigestSize());
        return sb3.toString();
    }
    
    public void update(byte[] firstNode) {
        if (this.isFinished) {
            System.out.print("Too much updates for Tree!!");
            return;
        }
        if (!this.isInitialized) {
            System.err.println("GMSSRootCalc not initialized!");
            return;
        }
        final int[] index = this.index;
        ++index[0];
        if (index[0] == 1) {
            System.arraycopy(firstNode, 0, this.AuthPath[0], 0, this.mdLength);
        }
        else if (index[0] == 3 && this.heightOfTree > this.K) {
            this.treehash[0].setFirstNode(firstNode);
        }
        final int[] index2 = this.index;
        if ((index2[0] - 3) % 2 == 0 && index2[0] >= 3 && this.heightOfTree == this.K) {
            this.retain[0].insertElementAt(firstNode, 0);
        }
        if (this.index[0] == 0) {
            this.tailStack.addElement(firstNode);
            this.heightOfNodes.addElement(Integers.valueOf(0));
            return;
        }
        final int mdLength = this.mdLength;
        final byte[] array = new byte[mdLength];
        final int n = mdLength << 1;
        final byte[] array2 = new byte[n];
        System.arraycopy(firstNode, 0, array, 0, mdLength);
        int n2 = 0;
        firstNode = array;
        while (this.tailStack.size() > 0 && n2 == this.heightOfNodes.lastElement()) {
            System.arraycopy(this.tailStack.lastElement(), 0, array2, 0, this.mdLength);
            final Vector tailStack = this.tailStack;
            tailStack.removeElementAt(tailStack.size() - 1);
            final Vector heightOfNodes = this.heightOfNodes;
            heightOfNodes.removeElementAt(heightOfNodes.size() - 1);
            final int mdLength2 = this.mdLength;
            System.arraycopy(firstNode, 0, array2, mdLength2, mdLength2);
            this.messDigestTree.update(array2, 0, n);
            final byte[] firstNode2 = new byte[this.messDigestTree.getDigestSize()];
            this.messDigestTree.doFinal(firstNode2, 0);
            final int n3 = n2 + 1;
            firstNode = firstNode2;
            if ((n2 = n3) < this.heightOfTree) {
                final int[] index3 = this.index;
                ++index3[n3];
                if (index3[n3] == 1) {
                    System.arraycopy(firstNode2, 0, this.AuthPath[n3], 0, this.mdLength);
                }
                if (n3 >= this.heightOfTree - this.K) {
                    if (n3 == 0) {
                        System.out.println("M\ufffd\ufffd\ufffdP");
                    }
                    final int[] index4 = this.index;
                    firstNode = firstNode2;
                    n2 = n3;
                    if ((index4[n3] - 3) % 2 != 0) {
                        continue;
                    }
                    firstNode = firstNode2;
                    n2 = n3;
                    if (index4[n3] < 3) {
                        continue;
                    }
                    this.retain[n3 - (this.heightOfTree - this.K)].insertElementAt(firstNode2, 0);
                    firstNode = firstNode2;
                    n2 = n3;
                }
                else {
                    firstNode = firstNode2;
                    n2 = n3;
                    if (this.index[n3] != 3) {
                        continue;
                    }
                    this.treehash[n3].setFirstNode(firstNode2);
                    firstNode = firstNode2;
                    n2 = n3;
                }
            }
        }
        this.tailStack.addElement(firstNode);
        this.heightOfNodes.addElement(Integers.valueOf(n2));
        if (n2 == this.heightOfTree) {
            this.isFinished = true;
            this.isInitialized = false;
            this.root = this.tailStack.lastElement();
        }
    }
    
    public void update(final byte[] array, final byte[] array2) {
        final int heightOfNextSeed = this.heightOfNextSeed;
        if (heightOfNextSeed < this.heightOfTree - this.K && this.indexForNextSeed - 2 == this.index[0]) {
            this.initializeTreehashSeed(array, heightOfNextSeed);
            ++this.heightOfNextSeed;
            this.indexForNextSeed *= 2;
        }
        this.update(array2);
    }
    
    public boolean wasFinished() {
        return this.isFinished;
    }
    
    public boolean wasInitialized() {
        return this.isInitialized;
    }
}
