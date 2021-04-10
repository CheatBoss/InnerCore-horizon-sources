package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.isismtt.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class NamingAuthority extends ASN1Object
{
    public static final ASN1ObjectIdentifier id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern;
    private ASN1ObjectIdentifier namingAuthorityId;
    private DirectoryString namingAuthorityText;
    private String namingAuthorityUrl;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(ISISMTTObjectIdentifiers.id_isismtt_at_namingAuthorities);
        sb.append(".1");
        id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern = new ASN1ObjectIdentifier(sb.toString());
    }
    
    public NamingAuthority(final ASN1ObjectIdentifier namingAuthorityId, final String namingAuthorityUrl, final DirectoryString namingAuthorityText) {
        this.namingAuthorityId = namingAuthorityId;
        this.namingAuthorityUrl = namingAuthorityUrl;
        this.namingAuthorityText = namingAuthorityText;
    }
    
    private NamingAuthority(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        if (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable = objects.nextElement();
            if (asn1Encodable instanceof ASN1ObjectIdentifier) {
                this.namingAuthorityId = (ASN1ObjectIdentifier)asn1Encodable;
            }
            else if (asn1Encodable instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(asn1Encodable).getString();
            }
            else {
                if (!(asn1Encodable instanceof ASN1String)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bad object encountered: ");
                    sb2.append(((ASN1ObjectIdentifier)asn1Encodable).getClass());
                    throw new IllegalArgumentException(sb2.toString());
                }
                this.namingAuthorityText = DirectoryString.getInstance(asn1Encodable);
            }
        }
        if (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable2 = objects.nextElement();
            if (asn1Encodable2 instanceof DERIA5String) {
                this.namingAuthorityUrl = DERIA5String.getInstance(asn1Encodable2).getString();
            }
            else {
                if (!(asn1Encodable2 instanceof ASN1String)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Bad object encountered: ");
                    sb3.append(asn1Encodable2.getClass());
                    throw new IllegalArgumentException(sb3.toString());
                }
                this.namingAuthorityText = DirectoryString.getInstance(asn1Encodable2);
            }
        }
        if (!objects.hasMoreElements()) {
            return;
        }
        final ASN1Encodable asn1Encodable3 = objects.nextElement();
        if (asn1Encodable3 instanceof ASN1String) {
            this.namingAuthorityText = DirectoryString.getInstance(asn1Encodable3);
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Bad object encountered: ");
        sb4.append(asn1Encodable3.getClass());
        throw new IllegalArgumentException(sb4.toString());
    }
    
    public static NamingAuthority getInstance(final Object o) {
        if (o == null || o instanceof NamingAuthority) {
            return (NamingAuthority)o;
        }
        if (o instanceof ASN1Sequence) {
            return new NamingAuthority((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static NamingAuthority getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1ObjectIdentifier getNamingAuthorityId() {
        return this.namingAuthorityId;
    }
    
    public DirectoryString getNamingAuthorityText() {
        return this.namingAuthorityText;
    }
    
    public String getNamingAuthorityUrl() {
        return this.namingAuthorityUrl;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1ObjectIdentifier namingAuthorityId = this.namingAuthorityId;
        if (namingAuthorityId != null) {
            asn1EncodableVector.add(namingAuthorityId);
        }
        if (this.namingAuthorityUrl != null) {
            asn1EncodableVector.add(new DERIA5String(this.namingAuthorityUrl, true));
        }
        final DirectoryString namingAuthorityText = this.namingAuthorityText;
        if (namingAuthorityText != null) {
            asn1EncodableVector.add(namingAuthorityText);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
