package org.spongycastle.asn1.ua;

import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.field.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class DSTU4145ECBinary extends ASN1Object
{
    ASN1Integer a;
    ASN1OctetString b;
    ASN1OctetString bp;
    DSTU4145BinaryField f;
    ASN1Integer n;
    BigInteger version;
    
    private DSTU4145ECBinary(final ASN1Sequence asn1Sequence) {
        this.version = BigInteger.valueOf(0L);
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(0);
            if (!asn1TaggedObject.isExplicit() || asn1TaggedObject.getTagNo() != 0) {
                throw new IllegalArgumentException("object parse error");
            }
            this.version = ASN1Integer.getInstance(asn1TaggedObject.getLoadedObject()).getValue();
            n = 1;
        }
        this.f = DSTU4145BinaryField.getInstance(asn1Sequence.getObjectAt(n));
        final int n2 = n + 1;
        this.a = ASN1Integer.getInstance(asn1Sequence.getObjectAt(n2));
        final int n3 = n2 + 1;
        this.b = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n3));
        final int n4 = n3 + 1;
        this.n = ASN1Integer.getInstance(asn1Sequence.getObjectAt(n4));
        this.bp = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n4 + 1));
    }
    
    public DSTU4145ECBinary(final ECDomainParameters ecDomainParameters) {
        this.version = BigInteger.valueOf(0L);
        final ECCurve curve = ecDomainParameters.getCurve();
        if (ECAlgorithms.isF2mCurve(curve)) {
            final int[] exponentsPresent = ((PolynomialExtensionField)curve.getField()).getMinimalPolynomial().getExponentsPresent();
            DSTU4145BinaryField f;
            if (exponentsPresent.length == 3) {
                f = new DSTU4145BinaryField(exponentsPresent[2], exponentsPresent[1]);
            }
            else {
                if (exponentsPresent.length != 5) {
                    throw new IllegalArgumentException("curve must have a trinomial or pentanomial basis");
                }
                f = new DSTU4145BinaryField(exponentsPresent[4], exponentsPresent[1], exponentsPresent[2], exponentsPresent[3]);
            }
            this.f = f;
            this.a = new ASN1Integer(curve.getA().toBigInteger());
            this.b = new DEROctetString(curve.getB().getEncoded());
            this.n = new ASN1Integer(ecDomainParameters.getN());
            this.bp = new DEROctetString(DSTU4145PointEncoder.encodePoint(ecDomainParameters.getG()));
            return;
        }
        throw new IllegalArgumentException("only binary domain is possible");
    }
    
    public static DSTU4145ECBinary getInstance(final Object o) {
        if (o instanceof DSTU4145ECBinary) {
            return (DSTU4145ECBinary)o;
        }
        if (o != null) {
            return new DSTU4145ECBinary(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getA() {
        return this.a.getValue();
    }
    
    public byte[] getB() {
        return Arrays.clone(this.b.getOctets());
    }
    
    public DSTU4145BinaryField getField() {
        return this.f;
    }
    
    public byte[] getG() {
        return Arrays.clone(this.bp.getOctets());
    }
    
    public BigInteger getN() {
        return this.n.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.version.compareTo(BigInteger.valueOf(0L)) != 0) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, new ASN1Integer(this.version)));
        }
        asn1EncodableVector.add(this.f);
        asn1EncodableVector.add(this.a);
        asn1EncodableVector.add(this.b);
        asn1EncodableVector.add(this.n);
        asn1EncodableVector.add(this.bp);
        return new DERSequence(asn1EncodableVector);
    }
}
