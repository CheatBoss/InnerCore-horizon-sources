package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class PrivateKeyInfo extends ASN1Object
{
    private AlgorithmIdentifier algId;
    private ASN1Set attributes;
    private ASN1OctetString privKey;
    
    public PrivateKeyInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        if (objects.nextElement().getValue().intValue() == 0) {
            this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
            this.privKey = ASN1OctetString.getInstance(objects.nextElement());
            if (objects.hasMoreElements()) {
                this.attributes = ASN1Set.getInstance((ASN1TaggedObject)objects.nextElement(), false);
            }
            return;
        }
        throw new IllegalArgumentException("wrong version for private key info");
    }
    
    public PrivateKeyInfo(final AlgorithmIdentifier algorithmIdentifier, final ASN1Encodable asn1Encodable) throws IOException {
        this(algorithmIdentifier, asn1Encodable, null);
    }
    
    public PrivateKeyInfo(final AlgorithmIdentifier algId, final ASN1Encodable asn1Encodable, final ASN1Set attributes) throws IOException {
        this.privKey = new DEROctetString(asn1Encodable.toASN1Primitive().getEncoded("DER"));
        this.algId = algId;
        this.attributes = attributes;
    }
    
    public static PrivateKeyInfo getInstance(final Object o) {
        if (o instanceof PrivateKeyInfo) {
            return (PrivateKeyInfo)o;
        }
        if (o != null) {
            return new PrivateKeyInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static PrivateKeyInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }
    
    public ASN1Set getAttributes() {
        return this.attributes;
    }
    
    public ASN1Primitive getPrivateKey() {
        try {
            return this.parsePrivateKey().toASN1Primitive();
        }
        catch (IOException ex) {
            throw new IllegalStateException("unable to parse private key");
        }
    }
    
    public AlgorithmIdentifier getPrivateKeyAlgorithm() {
        return this.algId;
    }
    
    public ASN1Encodable parsePrivateKey() throws IOException {
        return ASN1Primitive.fromByteArray(this.privKey.getOctets());
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(0L));
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(this.privKey);
        if (this.attributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.attributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
