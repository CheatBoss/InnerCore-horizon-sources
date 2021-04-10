package org.spongycastle.asn1.x509;

import java.math.*;
import org.spongycastle.asn1.*;

public class BasicConstraints extends ASN1Object
{
    ASN1Boolean cA;
    ASN1Integer pathLenConstraint;
    
    public BasicConstraints(final int n) {
        this.cA = ASN1Boolean.getInstance(false);
        this.pathLenConstraint = null;
        this.cA = ASN1Boolean.getInstance(true);
        this.pathLenConstraint = new ASN1Integer(n);
    }
    
    private BasicConstraints(final ASN1Sequence asn1Sequence) {
        this.cA = ASN1Boolean.getInstance(false);
        this.pathLenConstraint = null;
        if (asn1Sequence.size() == 0) {
            this.cA = null;
            this.pathLenConstraint = null;
            return;
        }
        if (asn1Sequence.getObjectAt(0) instanceof ASN1Boolean) {
            this.cA = ASN1Boolean.getInstance(asn1Sequence.getObjectAt(0));
        }
        else {
            this.cA = null;
            this.pathLenConstraint = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        }
        if (asn1Sequence.size() <= 1) {
            return;
        }
        if (this.cA != null) {
            this.pathLenConstraint = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        throw new IllegalArgumentException("wrong sequence in constructor");
    }
    
    public BasicConstraints(final boolean b) {
        this.cA = ASN1Boolean.getInstance(false);
        this.pathLenConstraint = null;
        if (b) {
            this.cA = ASN1Boolean.getInstance(true);
        }
        else {
            this.cA = null;
        }
        this.pathLenConstraint = null;
    }
    
    public static BasicConstraints fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.basicConstraints));
    }
    
    public static BasicConstraints getInstance(final Object o) {
        if (o instanceof BasicConstraints) {
            return (BasicConstraints)o;
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        if (o != null) {
            return new BasicConstraints(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static BasicConstraints getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getPathLenConstraint() {
        final ASN1Integer pathLenConstraint = this.pathLenConstraint;
        if (pathLenConstraint != null) {
            return pathLenConstraint.getValue();
        }
        return null;
    }
    
    public boolean isCA() {
        final ASN1Boolean ca = this.cA;
        return ca != null && ca.isTrue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1Boolean ca = this.cA;
        if (ca != null) {
            asn1EncodableVector.add(ca);
        }
        final ASN1Integer pathLenConstraint = this.pathLenConstraint;
        if (pathLenConstraint != null) {
            asn1EncodableVector.add(pathLenConstraint);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        StringBuilder sb;
        if (this.pathLenConstraint == null) {
            if (this.cA == null) {
                return "BasicConstraints: isCa(false)";
            }
            sb = new StringBuilder();
            sb.append("BasicConstraints: isCa(");
            sb.append(this.isCA());
            sb.append(")");
        }
        else {
            sb = new StringBuilder();
            sb.append("BasicConstraints: isCa(");
            sb.append(this.isCA());
            sb.append("), pathLenConstraint = ");
            sb.append(this.pathLenConstraint.getValue());
        }
        return sb.toString();
    }
}
