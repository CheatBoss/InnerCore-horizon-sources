package org.spongycastle.asn1.eac;

import org.spongycastle.asn1.*;

public interface EACObjectIdentifiers
{
    public static final ASN1ObjectIdentifier bsi_de;
    public static final ASN1ObjectIdentifier id_CA;
    public static final ASN1ObjectIdentifier id_CA_DH;
    public static final ASN1ObjectIdentifier id_CA_DH_3DES_CBC_CBC = (id_CA_DH = (id_CA = EACObjectIdentifiers.bsi_de.branch("2.2.3")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier id_CA_ECDH;
    public static final ASN1ObjectIdentifier id_CA_ECDH_3DES_CBC_CBC = (id_CA_ECDH = EACObjectIdentifiers.id_CA.branch("2")).branch("1");
    public static final ASN1ObjectIdentifier id_EAC_ePassport = EACObjectIdentifiers.bsi_de.branch("3.1.2.1");
    public static final ASN1ObjectIdentifier id_PK;
    public static final ASN1ObjectIdentifier id_PK_DH = (id_PK = (bsi_de = new ASN1ObjectIdentifier("0.4.0.127.0.7")).branch("2.2.1")).branch("1");
    public static final ASN1ObjectIdentifier id_PK_ECDH = EACObjectIdentifiers.id_PK.branch("2");
    public static final ASN1ObjectIdentifier id_TA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_1 = (id_TA_ECDSA = EACObjectIdentifiers.id_TA.branch("2")).branch("1");
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_224 = EACObjectIdentifiers.id_TA_ECDSA.branch("2");
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_256 = EACObjectIdentifiers.id_TA_ECDSA.branch("3");
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_384 = EACObjectIdentifiers.id_TA_ECDSA.branch("4");
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_512 = EACObjectIdentifiers.id_TA_ECDSA.branch("5");
    public static final ASN1ObjectIdentifier id_TA_RSA;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_1 = EACObjectIdentifiers.id_TA_RSA.branch("3");
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_256 = EACObjectIdentifiers.id_TA_RSA.branch("4");
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_512 = EACObjectIdentifiers.id_TA_RSA.branch("6");
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_1 = (id_TA_RSA = (id_TA = EACObjectIdentifiers.bsi_de.branch("2.2.2")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_256 = EACObjectIdentifiers.id_TA_RSA.branch("2");
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_512 = EACObjectIdentifiers.id_TA_RSA.branch("5");
}
