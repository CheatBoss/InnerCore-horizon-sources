package org.spongycastle.asn1.bsi;

import org.spongycastle.asn1.*;

public interface BSIObjectIdentifiers
{
    public static final ASN1ObjectIdentifier bsi_de;
    public static final ASN1ObjectIdentifier ecdsa_plain_RIPEMD160 = BSIObjectIdentifiers.ecdsa_plain_signatures.branch("6");
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA1 = (ecdsa_plain_signatures = (id_ecc = (bsi_de = new ASN1ObjectIdentifier("0.4.0.127.0.7")).branch("1.1")).branch("4.1")).branch("1");
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA224 = BSIObjectIdentifiers.ecdsa_plain_signatures.branch("2");
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA256 = BSIObjectIdentifiers.ecdsa_plain_signatures.branch("3");
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA384 = BSIObjectIdentifiers.ecdsa_plain_signatures.branch("4");
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA512 = BSIObjectIdentifiers.ecdsa_plain_signatures.branch("5");
    public static final ASN1ObjectIdentifier ecdsa_plain_signatures;
    public static final ASN1ObjectIdentifier id_ecc;
}
