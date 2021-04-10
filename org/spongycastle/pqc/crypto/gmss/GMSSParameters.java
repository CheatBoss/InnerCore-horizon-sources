package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.util.*;

public class GMSSParameters
{
    private int[] K;
    private int[] heightOfTrees;
    private int numOfLayers;
    private int[] winternitzParameter;
    
    public GMSSParameters(final int n) throws IllegalArgumentException {
        if (n <= 10) {
            this.init(1, new int[] { 10 }, new int[] { 3 }, new int[] { 2 });
            return;
        }
        if (n <= 20) {
            this.init(2, new int[] { 10, 10 }, new int[] { 5, 4 }, new int[] { 2, 2 });
            return;
        }
        this.init(4, new int[] { 10, 10, 10, 10 }, new int[] { 9, 9, 9, 3 }, new int[] { 2, 2, 2, 2 });
    }
    
    public GMSSParameters(final int n, final int[] array, final int[] array2, final int[] array3) throws IllegalArgumentException {
        this.init(n, array, array2, array3);
    }
    
    private void init(int numOfLayers, final int[] array, final int[] array2, final int[] array3) throws IllegalArgumentException {
        this.numOfLayers = numOfLayers;
        String s;
        if (numOfLayers == array2.length && numOfLayers == array.length && numOfLayers == array3.length) {
            s = "";
            numOfLayers = 1;
        }
        else {
            s = "Unexpected parameterset format";
            numOfLayers = 0;
        }
        for (int i = 0; i < this.numOfLayers; ++i) {
            if (array3[i] < 2 || (array[i] - array3[i]) % 2 != 0) {
                s = "Wrong parameter K (K >= 2 and H-K even required)!";
                numOfLayers = 0;
            }
            if (array[i] < 4 || array2[i] < 2) {
                s = "Wrong parameter H or w (H > 3 and w > 1 required)!";
                numOfLayers = 0;
            }
        }
        if (numOfLayers != 0) {
            this.heightOfTrees = Arrays.clone(array);
            this.winternitzParameter = Arrays.clone(array2);
            this.K = Arrays.clone(array3);
            return;
        }
        throw new IllegalArgumentException(s);
    }
    
    public int[] getHeightOfTrees() {
        return Arrays.clone(this.heightOfTrees);
    }
    
    public int[] getK() {
        return Arrays.clone(this.K);
    }
    
    public int getNumOfLayers() {
        return this.numOfLayers;
    }
    
    public int[] getWinternitzParameter() {
        return Arrays.clone(this.winternitzParameter);
    }
}
