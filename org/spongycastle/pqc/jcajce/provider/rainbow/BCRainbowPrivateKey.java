package org.spongycastle.pqc.jcajce.provider.rainbow;

import java.security.*;
import org.spongycastle.pqc.crypto.rainbow.*;
import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;
import java.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;

public class BCRainbowPrivateKey implements PrivateKey
{
    private static final long serialVersionUID = 1L;
    private short[][] A1inv;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private Layer[] layers;
    private int[] vi;
    
    public BCRainbowPrivateKey(final RainbowPrivateKeyParameters rainbowPrivateKeyParameters) {
        this(rainbowPrivateKeyParameters.getInvA1(), rainbowPrivateKeyParameters.getB1(), rainbowPrivateKeyParameters.getInvA2(), rainbowPrivateKeyParameters.getB2(), rainbowPrivateKeyParameters.getVi(), rainbowPrivateKeyParameters.getLayers());
    }
    
    public BCRainbowPrivateKey(final RainbowPrivateKeySpec rainbowPrivateKeySpec) {
        this(rainbowPrivateKeySpec.getInvA1(), rainbowPrivateKeySpec.getB1(), rainbowPrivateKeySpec.getInvA2(), rainbowPrivateKeySpec.getB2(), rainbowPrivateKeySpec.getVi(), rainbowPrivateKeySpec.getLayers());
    }
    
    public BCRainbowPrivateKey(final short[][] a1inv, final short[] b1, final short[][] a2inv, final short[] b2, final int[] vi, final Layer[] layers) {
        this.A1inv = a1inv;
        this.b1 = b1;
        this.A2inv = a2inv;
        this.b2 = b2;
        this.vi = vi;
        this.layers = layers;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BCRainbowPrivateKey)) {
            return false;
        }
        final BCRainbowPrivateKey bcRainbowPrivateKey = (BCRainbowPrivateKey)o;
        boolean b = RainbowUtil.equals(this.A1inv, bcRainbowPrivateKey.getInvA1()) && RainbowUtil.equals(this.A2inv, bcRainbowPrivateKey.getInvA2()) && RainbowUtil.equals(this.b1, bcRainbowPrivateKey.getB1()) && RainbowUtil.equals(this.b2, bcRainbowPrivateKey.getB2()) && Arrays.equals(this.vi, bcRainbowPrivateKey.getVi());
        if (this.layers.length != bcRainbowPrivateKey.getLayers().length) {
            return false;
        }
        for (int i = this.layers.length - 1; i >= 0; --i) {
            b &= this.layers[i].equals(bcRainbowPrivateKey.getLayers()[i]);
        }
        return b;
    }
    
    @Override
    public final String getAlgorithm() {
        return "Rainbow";
    }
    
    public short[] getB1() {
        return this.b1;
    }
    
    public short[] getB2() {
        return this.b2;
    }
    
    @Override
    public byte[] getEncoded() {
        final RainbowPrivateKey rainbowPrivateKey = new RainbowPrivateKey(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, DERNull.INSTANCE), rainbowPrivateKey).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
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
    
    @Override
    public int hashCode() {
        int n = ((((this.layers.length * 37 + org.spongycastle.util.Arrays.hashCode(this.A1inv)) * 37 + org.spongycastle.util.Arrays.hashCode(this.b1)) * 37 + org.spongycastle.util.Arrays.hashCode(this.A2inv)) * 37 + org.spongycastle.util.Arrays.hashCode(this.b2)) * 37 + org.spongycastle.util.Arrays.hashCode(this.vi);
        int length = this.layers.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = n * 37 + this.layers[length].hashCode();
        }
        return n;
    }
}
