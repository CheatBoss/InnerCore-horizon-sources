package org.spongycastle.asn1.eac;

import org.spongycastle.asn1.*;

public abstract class PublicKeyDataObject extends ASN1Object
{
    public static PublicKeyDataObject getInstance(final Object o) {
        if (o instanceof PublicKeyDataObject) {
            return (PublicKeyDataObject)o;
        }
        if (o == null) {
            return null;
        }
        final ASN1Sequence instance = ASN1Sequence.getInstance(o);
        if (ASN1ObjectIdentifier.getInstance(instance.getObjectAt(0)).on(EACObjectIdentifiers.id_TA_ECDSA)) {
            return new ECDSAPublicKey(instance);
        }
        return new RSAPublicKey(instance);
    }
    
    public abstract ASN1ObjectIdentifier getUsage();
}
