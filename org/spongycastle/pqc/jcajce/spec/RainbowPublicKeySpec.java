package org.spongycastle.pqc.jcajce.spec;

import java.security.spec.*;

public class RainbowPublicKeySpec implements KeySpec
{
    private short[][] coeffquadratic;
    private short[] coeffscalar;
    private short[][] coeffsingular;
    private int docLength;
    
    public RainbowPublicKeySpec(final int docLength, final short[][] coeffquadratic, final short[][] coeffsingular, final short[] coeffscalar) {
        this.docLength = docLength;
        this.coeffquadratic = coeffquadratic;
        this.coeffsingular = coeffsingular;
        this.coeffscalar = coeffscalar;
    }
    
    public short[][] getCoeffQuadratic() {
        return this.coeffquadratic;
    }
    
    public short[] getCoeffScalar() {
        return this.coeffscalar;
    }
    
    public short[][] getCoeffSingular() {
        return this.coeffsingular;
    }
    
    public int getDocLength() {
        return this.docLength;
    }
}
