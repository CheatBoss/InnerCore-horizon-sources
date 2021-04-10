package org.spongycastle.asn1.x509;

import java.util.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class SubjectPublicKeyInfo extends ASN1Object
{
    private AlgorithmIdentifier algId;
    private DERBitString keyData;
    
    public SubjectPublicKeyInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
            this.keyData = DERBitString.getInstance(objects.nextElement());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public SubjectPublicKeyInfo(final AlgorithmIdentifier algId, final ASN1Encodable asn1Encodable) throws IOException {
        this.keyData = new DERBitString(asn1Encodable);
        this.algId = algId;
    }
    
    public SubjectPublicKeyInfo(final AlgorithmIdentifier algId, final byte[] array) {
        this.keyData = new DERBitString(array);
        this.algId = algId;
    }
    
    public static SubjectPublicKeyInfo getInstance(final Object o) {
        if (o instanceof SubjectPublicKeyInfo) {
            return (SubjectPublicKeyInfo)o;
        }
        if (o != null) {
            return new SubjectPublicKeyInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static SubjectPublicKeyInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getAlgorithm() {
        return this.algId;
    }
    
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }
    
    public ASN1Primitive getPublicKey() throws IOException {
        return new ASN1InputStream(this.keyData.getOctets()).readObject();
    }
    
    public DERBitString getPublicKeyData() {
        return this.keyData;
    }
    
    public ASN1Primitive parsePublicKey() throws IOException {
        return new ASN1InputStream(this.keyData.getOctets()).readObject();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(this.keyData);
        return new DERSequence(asn1EncodableVector);
    }
}
