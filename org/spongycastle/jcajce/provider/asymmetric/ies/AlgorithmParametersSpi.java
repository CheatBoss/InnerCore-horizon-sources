package org.spongycastle.jcajce.provider.asymmetric.ies;

import org.spongycastle.jce.spec.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;

public class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    IESParameterSpec currentSpec;
    
    @Override
    protected byte[] engineGetEncoded() {
        try {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            if (this.currentSpec.getDerivationV() != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 0, new DEROctetString(this.currentSpec.getDerivationV())));
            }
            if (this.currentSpec.getEncodingV() != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 1, new DEROctetString(this.currentSpec.getEncodingV())));
            }
            asn1EncodableVector.add(new ASN1Integer(this.currentSpec.getMacKeySize()));
            if (this.currentSpec.getNonce() != null) {
                final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
                asn1EncodableVector2.add(new ASN1Integer(this.currentSpec.getCipherKeySize()));
                asn1EncodableVector2.add(new ASN1Integer(this.currentSpec.getNonce()));
                asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
            }
            return new DERSequence(asn1EncodableVector).getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error encoding IESParameters");
        }
    }
    
    @Override
    protected byte[] engineGetEncoded(final String s) {
        if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
            return null;
        }
        return this.engineGetEncoded();
    }
    
    @Override
    protected AlgorithmParameterSpec engineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != null) {
            return this.localEngineGetParameterSpec(clazz);
        }
        throw new NullPointerException("argument to getParameterSpec must not be null");
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec instanceof IESParameterSpec) {
            this.currentSpec = (IESParameterSpec)algorithmParameterSpec;
            return;
        }
        throw new InvalidParameterSpecException("IESParameterSpec required to initialise a IES algorithm parameters object");
    }
    
    @Override
    protected void engineInit(final byte[] array) throws IOException {
        try {
            final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(array);
            if (asn1Sequence.size() == 1) {
                this.currentSpec = new IESParameterSpec(null, null, ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().intValue());
                return;
            }
            if (asn1Sequence.size() == 2) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(0));
                if (instance.getTagNo() == 0) {
                    this.currentSpec = new IESParameterSpec(ASN1OctetString.getInstance(instance, false).getOctets(), null, ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getValue().intValue());
                    return;
                }
                this.currentSpec = new IESParameterSpec(null, ASN1OctetString.getInstance(instance, false).getOctets(), ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getValue().intValue());
            }
            else {
                if (asn1Sequence.size() == 3) {
                    this.currentSpec = new IESParameterSpec(ASN1OctetString.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(0)), false).getOctets(), ASN1OctetString.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1)), false).getOctets(), ASN1Integer.getInstance(asn1Sequence.getObjectAt(2)).getValue().intValue());
                    return;
                }
                if (asn1Sequence.size() == 4) {
                    final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(0));
                    final ASN1TaggedObject instance3 = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1));
                    final ASN1Sequence instance4 = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(3));
                    this.currentSpec = new IESParameterSpec(ASN1OctetString.getInstance(instance2, false).getOctets(), ASN1OctetString.getInstance(instance3, false).getOctets(), ASN1Integer.getInstance(asn1Sequence.getObjectAt(2)).getValue().intValue(), ASN1Integer.getInstance(instance4.getObjectAt(0)).getValue().intValue(), ASN1OctetString.getInstance(instance4.getObjectAt(1)).getOctets());
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Not a valid IES Parameter encoding.");
        }
        catch (ClassCastException ex2) {
            throw new IOException("Not a valid IES Parameter encoding.");
        }
    }
    
    @Override
    protected void engineInit(final byte[] array, final String s) throws IOException {
        if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter format ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
        this.engineInit(array);
    }
    
    @Override
    protected String engineToString() {
        return "IES Parameters";
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != IESParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
            throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
        }
        return this.currentSpec;
    }
}
