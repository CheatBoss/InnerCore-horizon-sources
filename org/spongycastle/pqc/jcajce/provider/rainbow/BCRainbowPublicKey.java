package org.spongycastle.pqc.jcajce.provider.rainbow;

import java.security.*;
import org.spongycastle.pqc.crypto.rainbow.*;
import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.jcajce.provider.util.*;

public class BCRainbowPublicKey implements PublicKey
{
    private static final long serialVersionUID = 1L;
    private short[][] coeffquadratic;
    private short[] coeffscalar;
    private short[][] coeffsingular;
    private int docLength;
    private RainbowParameters rainbowParams;
    
    public BCRainbowPublicKey(final int docLength, final short[][] coeffquadratic, final short[][] coeffsingular, final short[] coeffscalar) {
        this.docLength = docLength;
        this.coeffquadratic = coeffquadratic;
        this.coeffsingular = coeffsingular;
        this.coeffscalar = coeffscalar;
    }
    
    public BCRainbowPublicKey(final RainbowPublicKeyParameters rainbowPublicKeyParameters) {
        this(rainbowPublicKeyParameters.getDocLength(), rainbowPublicKeyParameters.getCoeffQuadratic(), rainbowPublicKeyParameters.getCoeffSingular(), rainbowPublicKeyParameters.getCoeffScalar());
    }
    
    public BCRainbowPublicKey(final RainbowPublicKeySpec rainbowPublicKeySpec) {
        this(rainbowPublicKeySpec.getDocLength(), rainbowPublicKeySpec.getCoeffQuadratic(), rainbowPublicKeySpec.getCoeffSingular(), rainbowPublicKeySpec.getCoeffScalar());
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o != null) {
            if (!(o instanceof BCRainbowPublicKey)) {
                return false;
            }
            final BCRainbowPublicKey bcRainbowPublicKey = (BCRainbowPublicKey)o;
            b2 = b;
            if (this.docLength == bcRainbowPublicKey.getDocLength()) {
                b2 = b;
                if (RainbowUtil.equals(this.coeffquadratic, bcRainbowPublicKey.getCoeffQuadratic())) {
                    b2 = b;
                    if (RainbowUtil.equals(this.coeffsingular, bcRainbowPublicKey.getCoeffSingular())) {
                        b2 = b;
                        if (RainbowUtil.equals(this.coeffscalar, bcRainbowPublicKey.getCoeffScalar())) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public final String getAlgorithm() {
        return "Rainbow";
    }
    
    public short[][] getCoeffQuadratic() {
        return this.coeffquadratic;
    }
    
    public short[] getCoeffScalar() {
        return Arrays.clone(this.coeffscalar);
    }
    
    public short[][] getCoeffSingular() {
        final short[][] array = new short[this.coeffsingular.length][];
        int n = 0;
        while (true) {
            final short[][] coeffsingular = this.coeffsingular;
            if (n == coeffsingular.length) {
                break;
            }
            array[n] = Arrays.clone(coeffsingular[n]);
            ++n;
        }
        return array;
    }
    
    public int getDocLength() {
        return this.docLength;
    }
    
    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, DERNull.INSTANCE), new RainbowPublicKey(this.docLength, this.coeffquadratic, this.coeffsingular, this.coeffscalar));
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public int hashCode() {
        return ((this.docLength * 37 + Arrays.hashCode(this.coeffquadratic)) * 37 + Arrays.hashCode(this.coeffsingular)) * 37 + Arrays.hashCode(this.coeffscalar);
    }
}
