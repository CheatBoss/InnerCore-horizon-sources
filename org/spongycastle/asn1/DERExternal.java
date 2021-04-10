package org.spongycastle.asn1;

import java.io.*;

public class DERExternal extends ASN1Primitive
{
    private ASN1Primitive dataValueDescriptor;
    private ASN1ObjectIdentifier directReference;
    private int encoding;
    private ASN1Primitive externalContent;
    private ASN1Integer indirectReference;
    
    public DERExternal(final ASN1EncodableVector asn1EncodableVector) {
        int n = 0;
        ASN1Primitive asn1Primitive2;
        final ASN1Primitive asn1Primitive = asn1Primitive2 = this.getObjFromVector(asn1EncodableVector, 0);
        if (asn1Primitive instanceof ASN1ObjectIdentifier) {
            this.directReference = (ASN1ObjectIdentifier)asn1Primitive;
            asn1Primitive2 = this.getObjFromVector(asn1EncodableVector, 1);
            n = 1;
        }
        int n2 = n;
        ASN1Primitive objFromVector = asn1Primitive2;
        if (asn1Primitive2 instanceof ASN1Integer) {
            this.indirectReference = (ASN1Integer)asn1Primitive2;
            n2 = n + 1;
            objFromVector = this.getObjFromVector(asn1EncodableVector, n2);
        }
        int n3 = n2;
        ASN1Primitive objFromVector2 = objFromVector;
        if (!(objFromVector instanceof ASN1TaggedObject)) {
            this.dataValueDescriptor = objFromVector;
            n3 = n2 + 1;
            objFromVector2 = this.getObjFromVector(asn1EncodableVector, n3);
        }
        if (asn1EncodableVector.size() != n3 + 1) {
            throw new IllegalArgumentException("input vector too large");
        }
        if (objFromVector2 instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)objFromVector2;
            this.setEncoding(asn1TaggedObject.getTagNo());
            this.externalContent = asn1TaggedObject.getObject();
            return;
        }
        throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
    }
    
    public DERExternal(final ASN1ObjectIdentifier directReference, final ASN1Integer indirectReference, final ASN1Primitive dataValueDescriptor, final int encoding, final ASN1Primitive asn1Primitive) {
        this.setDirectReference(directReference);
        this.setIndirectReference(indirectReference);
        this.setDataValueDescriptor(dataValueDescriptor);
        this.setEncoding(encoding);
        this.setExternalContent(asn1Primitive.toASN1Primitive());
    }
    
    public DERExternal(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Integer asn1Integer, final ASN1Primitive asn1Primitive, final DERTaggedObject derTaggedObject) {
        this(asn1ObjectIdentifier, asn1Integer, asn1Primitive, derTaggedObject.getTagNo(), derTaggedObject.toASN1Primitive());
    }
    
    private ASN1Primitive getObjFromVector(final ASN1EncodableVector asn1EncodableVector, final int n) {
        if (asn1EncodableVector.size() > n) {
            return asn1EncodableVector.get(n).toASN1Primitive();
        }
        throw new IllegalArgumentException("too few objects in input vector");
    }
    
    private void setDataValueDescriptor(final ASN1Primitive dataValueDescriptor) {
        this.dataValueDescriptor = dataValueDescriptor;
    }
    
    private void setDirectReference(final ASN1ObjectIdentifier directReference) {
        this.directReference = directReference;
    }
    
    private void setEncoding(final int encoding) {
        if (encoding >= 0 && encoding <= 2) {
            this.encoding = encoding;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid encoding value: ");
        sb.append(encoding);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void setExternalContent(final ASN1Primitive externalContent) {
        this.externalContent = externalContent;
    }
    
    private void setIndirectReference(final ASN1Integer indirectReference) {
        this.indirectReference = indirectReference;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        if (!(asn1Primitive instanceof DERExternal)) {
            return false;
        }
        if (this == asn1Primitive) {
            return true;
        }
        final DERExternal derExternal = (DERExternal)asn1Primitive;
        final ASN1ObjectIdentifier directReference = this.directReference;
        if (directReference != null) {
            final ASN1ObjectIdentifier directReference2 = derExternal.directReference;
            if (directReference2 == null || !directReference2.equals(directReference)) {
                return false;
            }
        }
        final ASN1Integer indirectReference = this.indirectReference;
        if (indirectReference != null) {
            final ASN1Integer indirectReference2 = derExternal.indirectReference;
            if (indirectReference2 == null || !indirectReference2.equals(indirectReference)) {
                return false;
            }
        }
        final ASN1Primitive dataValueDescriptor = this.dataValueDescriptor;
        if (dataValueDescriptor != null) {
            final ASN1Primitive dataValueDescriptor2 = derExternal.dataValueDescriptor;
            if (dataValueDescriptor2 == null || !dataValueDescriptor2.equals(dataValueDescriptor)) {
                return false;
            }
        }
        return this.externalContent.equals(derExternal.externalContent);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1ObjectIdentifier directReference = this.directReference;
        if (directReference != null) {
            byteArrayOutputStream.write(directReference.getEncoded("DER"));
        }
        final ASN1Integer indirectReference = this.indirectReference;
        if (indirectReference != null) {
            byteArrayOutputStream.write(indirectReference.getEncoded("DER"));
        }
        final ASN1Primitive dataValueDescriptor = this.dataValueDescriptor;
        if (dataValueDescriptor != null) {
            byteArrayOutputStream.write(dataValueDescriptor.getEncoded("DER"));
        }
        byteArrayOutputStream.write(new DERTaggedObject(true, this.encoding, this.externalContent).getEncoded("DER"));
        asn1OutputStream.writeEncoded(32, 8, byteArrayOutputStream.toByteArray());
    }
    
    @Override
    int encodedLength() throws IOException {
        return this.getEncoded().length;
    }
    
    public ASN1Primitive getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }
    
    public ASN1ObjectIdentifier getDirectReference() {
        return this.directReference;
    }
    
    public int getEncoding() {
        return this.encoding;
    }
    
    public ASN1Primitive getExternalContent() {
        return this.externalContent;
    }
    
    public ASN1Integer getIndirectReference() {
        return this.indirectReference;
    }
    
    @Override
    public int hashCode() {
        final ASN1ObjectIdentifier directReference = this.directReference;
        int hashCode;
        if (directReference != null) {
            hashCode = directReference.hashCode();
        }
        else {
            hashCode = 0;
        }
        final ASN1Integer indirectReference = this.indirectReference;
        int n = hashCode;
        if (indirectReference != null) {
            n = (hashCode ^ indirectReference.hashCode());
        }
        final ASN1Primitive dataValueDescriptor = this.dataValueDescriptor;
        int n2 = n;
        if (dataValueDescriptor != null) {
            n2 = (n ^ dataValueDescriptor.hashCode());
        }
        return n2 ^ this.externalContent.hashCode();
    }
    
    @Override
    boolean isConstructed() {
        return true;
    }
}
