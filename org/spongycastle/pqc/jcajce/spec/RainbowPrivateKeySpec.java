package org.spongycastle.pqc.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.pqc.crypto.rainbow.*;

public class RainbowPrivateKeySpec implements KeySpec
{
    private short[][] A1inv;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private Layer[] layers;
    private int[] vi;
    
    public RainbowPrivateKeySpec(final short[][] a1inv, final short[] b1, final short[][] a2inv, final short[] b2, final int[] vi, final Layer[] layers) {
        this.A1inv = a1inv;
        this.b1 = b1;
        this.A2inv = a2inv;
        this.b2 = b2;
        this.vi = vi;
        this.layers = layers;
    }
    
    public short[] getB1() {
        return this.b1;
    }
    
    public short[] getB2() {
        return this.b2;
    }
    
    public short[][] getInvA1() {
        return this.A1inv;
    }
    
    public short[][] getInvA2() {
        return this.A2inv;
    }
    
    public Layer[] getLayers() {
        return this.layers;
    }
    
    public int[] getVi() {
        return this.vi;
    }
}
