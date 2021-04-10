package org.spongycastle.pqc.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.util.*;

public class RainbowParameterSpec implements AlgorithmParameterSpec
{
    private static final int[] DEFAULT_VI;
    private int[] vi;
    
    static {
        DEFAULT_VI = new int[] { 6, 12, 17, 22, 33 };
    }
    
    public RainbowParameterSpec() {
        this.vi = RainbowParameterSpec.DEFAULT_VI;
    }
    
    public RainbowParameterSpec(final int[] vi) {
        this.vi = vi;
        this.checkParams();
    }
    
    private void checkParams() {
        final int[] vi = this.vi;
        if (vi == null) {
            throw new IllegalArgumentException("no layers defined.");
        }
        if (vi.length <= 1) {
            throw new IllegalArgumentException("Rainbow needs at least 1 layer, such that v1 < v2.");
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
            throw new IllegalArgumentException("v[i] has to be smaller than v[i+1]");
        }
    }
    
    public int getDocumentLength() {
        final int[] vi = this.vi;
        return vi[vi.length - 1] - vi[0];
    }
    
    public int getNumOfLayers() {
        return this.vi.length - 1;
    }
    
    public int[] getVi() {
        return Arrays.clone(this.vi);
    }
}
