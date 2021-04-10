package org.spongycastle.asn1.iana;

import org.spongycastle.asn1.*;

public interface IANAObjectIdentifiers
{
    public static final ASN1ObjectIdentifier SNMPv2 = IANAObjectIdentifiers.internet.branch("6");
    public static final ASN1ObjectIdentifier _private = IANAObjectIdentifiers.internet.branch("4");
    public static final ASN1ObjectIdentifier directory = (internet = new ASN1ObjectIdentifier("1.3.6.1")).branch("1");
    public static final ASN1ObjectIdentifier experimental = IANAObjectIdentifiers.internet.branch("3");
    public static final ASN1ObjectIdentifier hmacMD5 = (isakmpOakley = (ipsec = IANAObjectIdentifiers.security_mechanisms.branch("8")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier hmacRIPEMD160 = IANAObjectIdentifiers.isakmpOakley.branch("4");
    public static final ASN1ObjectIdentifier hmacSHA1 = IANAObjectIdentifiers.isakmpOakley.branch("2");
    public static final ASN1ObjectIdentifier hmacTIGER = IANAObjectIdentifiers.isakmpOakley.branch("3");
    public static final ASN1ObjectIdentifier internet;
    public static final ASN1ObjectIdentifier ipsec;
    public static final ASN1ObjectIdentifier isakmpOakley;
    public static final ASN1ObjectIdentifier mail = IANAObjectIdentifiers.internet.branch("7");
    public static final ASN1ObjectIdentifier mgmt = IANAObjectIdentifiers.internet.branch("2");
    public static final ASN1ObjectIdentifier pkix = IANAObjectIdentifiers.security_mechanisms.branch("6");
    public static final ASN1ObjectIdentifier security = IANAObjectIdentifiers.internet.branch("5");
    public static final ASN1ObjectIdentifier security_mechanisms = IANAObjectIdentifiers.security.branch("5");
    public static final ASN1ObjectIdentifier security_nametypes = IANAObjectIdentifiers.security.branch("6");
}
