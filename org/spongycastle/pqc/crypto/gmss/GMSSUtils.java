package org.spongycastle.pqc.crypto.gmss;

import java.util.*;
import org.spongycastle.util.*;

class GMSSUtils
{
    static Vector[] clone(final Vector[] array) {
        if (array == null) {
            return null;
        }
        final Vector[] array2 = new Vector[array.length];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = new Vector();
            final Enumeration elements = array[i].elements();
            while (elements.hasMoreElements()) {
                array2[i].addElement(elements.nextElement());
            }
        }
        return array2;
    }
    
    static GMSSLeaf[] clone(final GMSSLeaf[] array) {
        if (array == null) {
            return null;
        }
        final GMSSLeaf[] array2 = new GMSSLeaf[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static GMSSRootCalc[] clone(final GMSSRootCalc[] array) {
        if (array == null) {
            return null;
        }
        final GMSSRootCalc[] array2 = new GMSSRootCalc[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static GMSSRootSig[] clone(final GMSSRootSig[] array) {
        if (array == null) {
            return null;
        }
        final GMSSRootSig[] array2 = new GMSSRootSig[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static Treehash[] clone(final Treehash[] array) {
        if (array == null) {
            return null;
        }
        final Treehash[] array2 = new Treehash[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static byte[][] clone(final byte[][] array) {
        if (array == null) {
            return null;
        }
        final byte[][] array2 = new byte[array.length][];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = Arrays.clone(array[i]);
        }
        return array2;
    }
    
    static Vector[][] clone(final Vector[][] array) {
        if (array == null) {
            return null;
        }
        final Vector[][] array2 = new Vector[array.length][];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = clone(array[i]);
        }
        return array2;
    }
    
    static Treehash[][] clone(final Treehash[][] array) {
        if (array == null) {
            return null;
        }
        final Treehash[][] array2 = new Treehash[array.length][];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = clone(array[i]);
        }
        return array2;
    }
    
    static byte[][][] clone(final byte[][][] array) {
        if (array == null) {
            return null;
        }
        final byte[][][] array2 = new byte[array.length][][];
        for (int i = 0; i != array.length; ++i) {
            array2[i] = clone(array[i]);
        }
        return array2;
    }
}
