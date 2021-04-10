package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.math.linearalgebra.*;

public class McEliecePublicKeyParameters extends McElieceKeyParameters
{
    private GF2Matrix g;
    private int n;
    private int t;
    
    public McEliecePublicKeyParameters(final int n, final int t, final GF2Matrix gf2Matrix) {
        super(false, null);
        this.n = n;
        this.t = t;
        this.g = new GF2Matrix(gf2Matrix);
    }
    
    public GF2Matrix getG() {
        return this.g;
    }
    
    public int getK() {
        return this.g.getNumRows();
    }
    
    public int getN() {
        return this.n;
    }
    
    public int getT() {
        return this.t;
    }
}
