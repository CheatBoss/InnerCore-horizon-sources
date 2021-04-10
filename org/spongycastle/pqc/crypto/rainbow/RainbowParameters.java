package org.spongycastle.pqc.crypto.rainbow;

import org.spongycastle.crypto.*;

public class RainbowParameters implements CipherParameters
{
    private final int[] DEFAULT_VI;
    private int[] vi;
    
    public RainbowParameters() {
        final int[] array2;
        final int[] array = array2 = new int[5];
        array2[0] = 6;
        array2[1] = 12;
        array2[2] = 17;
        array2[3] = 22;
        array2[4] = 33;
        this.DEFAULT_VI = array;
        this.vi = array;
    }
    
    public RainbowParameters(final int[] vi) {
        this.DEFAULT_VI = new int[] { 6, 12, 17, 22, 33 };
        this.vi = vi;
        try {
            this.checkParams();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void checkParams() throws Exception {
        final int[] vi = this.vi;
        if (vi == null) {
            throw new Exception("no layers defined.");
        }
        if (vi.length <= 1) {
            throw new Exception("Rainbow needs at least 1 layer, such that v1 < v2.");
        }
        int n = 0;
        while (true) {
            final int[] vi2 = this.vi;
            if (n >= vi2.length - 1) {
                return;
            }
            final int n2 = vi2[n];
            ++n;
            if (n2 < vi2[n]) {
                continue;
            }
            throw new Exception("v[i] has to be smaller than v[i+1]");
        }
    }
    
    public int getDocLength() {
        final int[] vi = this.vi;
        return vi[vi.length - 1] - vi[0];
    }
    
    public int getNumOfLayers() {
        return this.vi.length - 1;
    }
    
    public int[] getVi() {
        return this.vi;
    }
}
