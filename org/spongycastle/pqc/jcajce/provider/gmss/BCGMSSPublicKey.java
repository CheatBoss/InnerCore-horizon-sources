package org.spongycastle.pqc.jcajce.provider.gmss;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.pqc.crypto.gmss.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.jcajce.provider.util.*;
import org.spongycastle.util.encoders.*;

public class BCGMSSPublicKey implements PublicKey, CipherParameters
{
    private static final long serialVersionUID = 1L;
    private GMSSParameters gmssParameterSet;
    private GMSSParameters gmssParams;
    private byte[] publicKeyBytes;
    
    public BCGMSSPublicKey(final GMSSPublicKeyParameters gmssPublicKeyParameters) {
        this(gmssPublicKeyParameters.getPublicKey(), gmssPublicKeyParameters.getParameters());
    }
    
    public BCGMSSPublicKey(final byte[] publicKeyBytes, final GMSSParameters gmssParameterSet) {
        this.gmssParameterSet = gmssParameterSet;
        this.publicKeyBytes = publicKeyBytes;
    }
    
    @Override
    public String getAlgorithm() {
        return "GMSS";
    }
    
    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.gmss, new ParSet(this.gmssParameterSet.getNumOfLayers(), this.gmssParameterSet.getHeightOfTrees(), this.gmssParameterSet.getWinternitzParameter(), this.gmssParameterSet.getK()).toASN1Primitive()), new GMSSPublicKey(this.publicKeyBytes));
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    public GMSSParameters getParameterSet() {
        return this.gmssParameterSet;
    }
    
    public byte[] getPublicKeyBytes() {
        return this.publicKeyBytes;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GMSS public key : ");
        sb.append(new String(Hex.encode(this.publicKeyBytes)));
        sb.append("\nHeight of Trees: \n");
        String s = sb.toString();
        for (int i = 0; i < this.gmssParameterSet.getHeightOfTrees().length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("Layer ");
            sb2.append(i);
            sb2.append(" : ");
            sb2.append(this.gmssParameterSet.getHeightOfTrees()[i]);
            sb2.append(" WinternitzParameter: ");
            sb2.append(this.gmssParameterSet.getWinternitzParameter()[i]);
            sb2.append(" K: ");
            sb2.append(this.gmssParameterSet.getK()[i]);
            sb2.append("\n");
            s = sb2.toString();
        }
        return s;
    }
}
