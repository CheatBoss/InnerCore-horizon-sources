package org.spongycastle.pqc.crypto.gmss;

import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.lang.reflect.*;
import java.io.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.pqc.crypto.gmss.util.*;

public class Treehash
{
    private byte[] firstNode;
    private int firstNodeHeight;
    private Vector heightOfNodes;
    private boolean isFinished;
    private boolean isInitialized;
    private int maxHeight;
    private Digest messDigestTree;
    private byte[] seedActive;
    private boolean seedInitialized;
    private byte[] seedNext;
    private int tailLength;
    private Vector tailStack;
    
    public Treehash(final Vector tailStack, final int maxHeight, final Digest messDigestTree) {
        this.tailStack = tailStack;
        this.maxHeight = maxHeight;
        this.firstNode = null;
        this.isInitialized = false;
        this.isFinished = false;
        this.seedInitialized = false;
        this.messDigestTree = messDigestTree;
        this.seedNext = new byte[messDigestTree.getDigestSize()];
        this.seedActive = new byte[this.messDigestTree.getDigestSize()];
    }
    
    public Treehash(final Digest messDigestTree, final byte[][] array, final int[] array2) {
        this.messDigestTree = messDigestTree;
        final int n = 0;
        this.maxHeight = array2[0];
        this.tailLength = array2[1];
        this.firstNodeHeight = array2[2];
        if (array2[3] == 1) {
            this.isFinished = true;
        }
        else {
            this.isFinished = false;
        }
        if (array2[4] == 1) {
            this.isInitialized = true;
        }
        else {
            this.isInitialized = false;
        }
        if (array2[5] == 1) {
            this.seedInitialized = true;
        }
        else {
            this.seedInitialized = false;
        }
        this.heightOfNodes = new Vector();
        for (int i = 0; i < this.tailLength; ++i) {
            this.heightOfNodes.addElement(Integers.valueOf(array2[i + 6]));
        }
        this.firstNode = array[0];
        this.seedActive = array[1];
        this.seedNext = array[2];
        this.tailStack = new Vector();
        for (int j = n; j < this.tailLength; ++j) {
            this.tailStack.addElement(array[j + 3]);
        }
    }
    
    public void destroy() {
        this.isInitialized = false;
        this.isFinished = false;
        this.firstNode = null;
        this.tailLength = 0;
        this.firstNodeHeight = -1;
    }
    
    public byte[] getFirstNode() {
        return this.firstNode;
    }
    
    public int getFirstNodeHeight() {
        if (this.firstNode == null) {
            return this.maxHeight;
        }
        return this.firstNodeHeight;
    }
    
    public int getLowestNodeHeight() {
        if (this.firstNode == null) {
            return this.maxHeight;
        }
        if (this.tailLength == 0) {
            return this.firstNodeHeight;
        }
        return Math.min(this.firstNodeHeight, this.heightOfNodes.lastElement());
    }
    
    public byte[] getSeedActive() {
        return this.seedActive;
    }
    
    public byte[][] getStatByte() {
        final int tailLength = this.tailLength;
        final int digestSize = this.messDigestTree.getDigestSize();
        final Class<Byte> type = Byte.TYPE;
        int i = 0;
        final byte[][] array = (byte[][])Array.newInstance(type, tailLength + 3, digestSize);
        array[0] = this.firstNode;
        array[1] = this.seedActive;
        array[2] = this.seedNext;
        while (i < this.tailLength) {
            array[i + 3] = this.tailStack.elementAt(i);
            ++i;
        }
        return array;
    }
    
    public int[] getStatInt() {
        final int tailLength = this.tailLength;
        final int[] array = new int[tailLength + 6];
        final int maxHeight = this.maxHeight;
        int i = 0;
        array[0] = maxHeight;
        array[1] = tailLength;
        array[2] = this.firstNodeHeight;
        if (this.isFinished) {
            array[3] = 1;
        }
        else {
            array[3] = 0;
        }
        if (this.isInitialized) {
            array[4] = 1;
        }
        else {
            array[4] = 0;
        }
        if (this.seedInitialized) {
            array[5] = 1;
        }
        else {
            array[5] = 0;
        }
        while (i < this.tailLength) {
            array[i + 6] = (int)this.heightOfNodes.elementAt(i);
            ++i;
        }
        return array;
    }
    
    public Vector getTailStack() {
        return this.tailStack;
    }
    
    public void initialize() {
        if (!this.seedInitialized) {
            final PrintStream err = System.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("Seed ");
            sb.append(this.maxHeight);
            sb.append(" not initialized");
            err.println(sb.toString());
            return;
        }
        this.heightOfNodes = new Vector();
        this.tailLength = 0;
        this.firstNode = null;
        this.firstNodeHeight = -1;
        this.isInitialized = true;
        System.arraycopy(this.seedNext, 0, this.seedActive, 0, this.messDigestTree.getDigestSize());
    }
    
    public void initializeSeed(final byte[] array) {
        System.arraycopy(array, 0, this.seedNext, 0, this.messDigestTree.getDigestSize());
        this.seedInitialized = true;
    }
    
    public void setFirstNode(final byte[] firstNode) {
        if (!this.isInitialized) {
            this.initialize();
        }
        this.firstNode = firstNode;
        this.firstNodeHeight = this.maxHeight;
        this.isFinished = true;
    }
    
    @Override
    public String toString() {
        String string = "Treehash    : ";
        final int n = 0;
        int n2 = 0;
        String string2;
        int i;
        while (true) {
            string2 = string;
            i = n;
            if (n2 >= this.tailLength + 6) {
                break;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.getStatInt()[n2]);
            sb.append(" ");
            string = sb.toString();
            ++n2;
        }
        while (i < this.tailLength + 3) {
            StringBuilder sb3;
            String s2;
            if (this.getStatByte()[i] != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string2);
                sb2.append(new String(Hex.encode(this.getStatByte()[i])));
                final String s = " ";
                sb3 = sb2;
                s2 = s;
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string2);
                s2 = "null ";
                sb3 = sb4;
            }
            sb3.append(s2);
            string2 = sb3.toString();
            ++i;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(string2);
        sb5.append("  ");
        sb5.append(this.messDigestTree.getDigestSize());
        return sb5.toString();
    }
    
    public void update(final GMSSRandom gmssRandom, byte[] firstNode) {
        PrintStream printStream;
        String s;
        if (this.isFinished) {
            printStream = System.err;
            s = "No more update possible for treehash instance!";
        }
        else {
            if (this.isInitialized) {
                final byte[] array = new byte[this.messDigestTree.getDigestSize()];
                gmssRandom.nextSeed(this.seedActive);
                if (this.firstNode == null) {
                    this.firstNode = firstNode;
                    this.firstNodeHeight = 0;
                }
                else {
                    int n;
                    for (n = 0; this.tailLength > 0 && n == this.heightOfNodes.lastElement(); ++n, --this.tailLength) {
                        final int n2 = this.messDigestTree.getDigestSize() << 1;
                        final byte[] array2 = new byte[n2];
                        System.arraycopy(this.tailStack.lastElement(), 0, array2, 0, this.messDigestTree.getDigestSize());
                        final Vector tailStack = this.tailStack;
                        tailStack.removeElementAt(tailStack.size() - 1);
                        final Vector heightOfNodes = this.heightOfNodes;
                        heightOfNodes.removeElementAt(heightOfNodes.size() - 1);
                        System.arraycopy(firstNode, 0, array2, this.messDigestTree.getDigestSize(), this.messDigestTree.getDigestSize());
                        this.messDigestTree.update(array2, 0, n2);
                        firstNode = new byte[this.messDigestTree.getDigestSize()];
                        this.messDigestTree.doFinal(firstNode, 0);
                    }
                    this.tailStack.addElement(firstNode);
                    this.heightOfNodes.addElement(Integers.valueOf(n));
                    ++this.tailLength;
                    if (this.heightOfNodes.lastElement() == this.firstNodeHeight) {
                        final int n3 = this.messDigestTree.getDigestSize() << 1;
                        final byte[] array3 = new byte[n3];
                        System.arraycopy(this.firstNode, 0, array3, 0, this.messDigestTree.getDigestSize());
                        System.arraycopy(this.tailStack.lastElement(), 0, array3, this.messDigestTree.getDigestSize(), this.messDigestTree.getDigestSize());
                        final Vector tailStack2 = this.tailStack;
                        tailStack2.removeElementAt(tailStack2.size() - 1);
                        final Vector heightOfNodes2 = this.heightOfNodes;
                        heightOfNodes2.removeElementAt(heightOfNodes2.size() - 1);
                        this.messDigestTree.update(array3, 0, n3);
                        final byte[] firstNode2 = new byte[this.messDigestTree.getDigestSize()];
                        this.firstNode = firstNode2;
                        this.messDigestTree.doFinal(firstNode2, 0);
                        ++this.firstNodeHeight;
                        this.tailLength = 0;
                    }
                }
                if (this.firstNodeHeight == this.maxHeight) {
                    this.isFinished = true;
                }
                return;
            }
            printStream = System.err;
            s = "Treehash instance not initialized before update";
        }
        printStream.println(s);
    }
    
    public void updateNextSeed(final GMSSRandom gmssRandom) {
        gmssRandom.nextSeed(this.seedNext);
    }
    
    public boolean wasFinished() {
        return this.isFinished;
    }
    
    public boolean wasInitialized() {
        return this.isInitialized;
    }
}
