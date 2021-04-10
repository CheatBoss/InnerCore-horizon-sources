package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceCCA2PublicKeyParameters extends McElieceCCA2KeyParameters
{
    private GF2Matrix matrixG;
    private int n;
    private int t;
    
    public McElieceCCA2PublicKeyParameters(final int n, final int t, final GF2Matrix gf2Matrix, final String s) {
        super(false, s);
        this.n = n;
        this.t = t;
        this.matrixG = new GF2Matrix(gf2Matrix);
    }
    
    public GF2Matrix getG() {
        return this.matrixG;
    }
    
    public int getK() {
        return this.matrixG.getNumRows();
    }
    
    public int getN() {
        return this.n;
    }
    
    public int getT() {
        return this.t;
    }
}
