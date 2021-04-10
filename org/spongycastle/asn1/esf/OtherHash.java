package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.*;

public class OtherHash extends ASN1Object implements ASN1Choice
{
    private OtherHashAlgAndValue otherHash;
    private ASN1OctetString sha1Hash;
    
    private OtherHash(final ASN1OctetString sha1Hash) {
        this.sha1Hash = sha1Hash;
    }
    
    public OtherHash(final OtherHashAlgAndValue otherHash) {
        this.otherHash = otherHash;
    }
    
    public OtherHash(final byte[] array) {
        this.sha1Hash = new DEROctetString(array);
    }
    
    public static OtherHash getInstance(final Object o) {
        if (o instanceof OtherHash) {
            return (OtherHash)o;
        }
        if (o instanceof ASN1OctetString) {
            return new OtherHash((ASN1OctetString)o);
        }
        return new OtherHash(OtherHashAlgAndValue.getInstance(o));
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        final OtherHashAlgAndValue otherHash = this.otherHash;
        if (otherHash == null) {
            return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1);
        }
        return otherHash.getHashAlgorithm();
    }
    
    public byte[] getHashValue() {
        final OtherHashAlgAndValue otherHash = this.otherHash;
        ASN1OctetString asn1OctetString;
        if (otherHash == null) {
            asn1OctetString = this.sha1Hash;
        }
        else {
            asn1OctetString = otherHash.getHashValue();
        }
        return asn1OctetString.getOctets();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final OtherHashAlgAndValue otherHash = this.otherHash;
        if (otherHash == null) {
            return this.sha1Hash;
        }
        return otherHash.toASN1Primitive();
    }
}
