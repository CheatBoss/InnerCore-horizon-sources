package org.spongycastle.asn1.x509;

import java.util.*;
import java.math.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.asn1.*;

public class AuthorityKeyIdentifier extends ASN1Object
{
    GeneralNames certissuer;
    ASN1Integer certserno;
    ASN1OctetString keyidentifier;
    
    protected AuthorityKeyIdentifier(final ASN1Sequence asn1Sequence) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            final int tagNo = instance.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        throw new IllegalArgumentException("illegal tag");
                    }
                    this.certserno = ASN1Integer.getInstance(instance, false);
                }
                else {
                    this.certissuer = GeneralNames.getInstance(instance, false);
                }
            }
            else {
                this.keyidentifier = ASN1OctetString.getInstance(instance, false);
            }
        }
    }
    
    public AuthorityKeyIdentifier(final GeneralNames generalNames, final BigInteger bigInteger) {
        this((byte[])null, generalNames, bigInteger);
    }
    
    public AuthorityKeyIdentifier(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        final SHA1Digest sha1Digest = new SHA1Digest();
        final byte[] array = new byte[sha1Digest.getDigestSize()];
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sha1Digest.update(bytes, 0, bytes.length);
        sha1Digest.doFinal(array, 0);
        this.keyidentifier = new DEROctetString(array);
    }
    
    public AuthorityKeyIdentifier(final SubjectPublicKeyInfo subjectPublicKeyInfo, final GeneralNames generalNames, final BigInteger bigInteger) {
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        final SHA1Digest sha1Digest = new SHA1Digest();
        final byte[] array = new byte[sha1Digest.getDigestSize()];
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sha1Digest.update(bytes, 0, bytes.length);
        sha1Digest.doFinal(array, 0);
        this.keyidentifier = new DEROctetString(array);
        this.certissuer = GeneralNames.getInstance(generalNames.toASN1Primitive());
        this.certserno = new ASN1Integer(bigInteger);
    }
    
    public AuthorityKeyIdentifier(final byte[] array) {
        this(array, null, null);
    }
    
    public AuthorityKeyIdentifier(final byte[] array, final GeneralNames certissuer, final BigInteger bigInteger) {
        final ASN1Integer asn1Integer = null;
        this.keyidentifier = null;
        this.certissuer = null;
        this.certserno = null;
        DEROctetString keyidentifier;
        if (array != null) {
            keyidentifier = new DEROctetString(array);
        }
        else {
            keyidentifier = null;
        }
        this.keyidentifier = keyidentifier;
        this.certissuer = certissuer;
        ASN1Integer certserno = asn1Integer;
        if (bigInteger != null) {
            certserno = new ASN1Integer(bigInteger);
        }
        this.certserno = certserno;
    }
    
    public static AuthorityKeyIdentifier fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.authorityKeyIdentifier));
    }
    
    public static AuthorityKeyIdentifier getInstance(final Object o) {
        if (o instanceof AuthorityKeyIdentifier) {
            return (AuthorityKeyIdentifier)o;
        }
        if (o != null) {
            return new AuthorityKeyIdentifier(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static AuthorityKeyIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralNames getAuthorityCertIssuer() {
        return this.certissuer;
    }
    
    public BigInteger getAuthorityCertSerialNumber() {
        final ASN1Integer certserno = this.certserno;
        if (certserno != null) {
            return certserno.getValue();
        }
        return null;
    }
    
    public byte[] getKeyIdentifier() {
        final ASN1OctetString keyidentifier = this.keyidentifier;
        if (keyidentifier != null) {
            return keyidentifier.getOctets();
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.keyidentifier != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.keyidentifier));
        }
        if (this.certissuer != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.certissuer));
        }
        if (this.certserno != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.certserno));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AuthorityKeyIdentifier: KeyID(");
        sb.append(this.keyidentifier.getOctets());
        sb.append(")");
        return sb.toString();
    }
}
