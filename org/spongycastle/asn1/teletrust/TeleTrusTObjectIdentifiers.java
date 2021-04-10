package org.spongycastle.asn1.teletrust;

import org.spongycastle.asn1.*;

public interface TeleTrusTObjectIdentifiers
{
    public static final ASN1ObjectIdentifier brainpoolP160r1 = (versionOne = (ellipticCurve = (ecc_brainpool = TeleTrusTObjectIdentifiers.teleTrusTAlgorithm.branch("3.2.8")).branch("1")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier brainpoolP160t1 = TeleTrusTObjectIdentifiers.versionOne.branch("2");
    public static final ASN1ObjectIdentifier brainpoolP192r1 = TeleTrusTObjectIdentifiers.versionOne.branch("3");
    public static final ASN1ObjectIdentifier brainpoolP192t1 = TeleTrusTObjectIdentifiers.versionOne.branch("4");
    public static final ASN1ObjectIdentifier brainpoolP224r1 = TeleTrusTObjectIdentifiers.versionOne.branch("5");
    public static final ASN1ObjectIdentifier brainpoolP224t1 = TeleTrusTObjectIdentifiers.versionOne.branch("6");
    public static final ASN1ObjectIdentifier brainpoolP256r1 = TeleTrusTObjectIdentifiers.versionOne.branch("7");
    public static final ASN1ObjectIdentifier brainpoolP256t1 = TeleTrusTObjectIdentifiers.versionOne.branch("8");
    public static final ASN1ObjectIdentifier brainpoolP320r1 = TeleTrusTObjectIdentifiers.versionOne.branch("9");
    public static final ASN1ObjectIdentifier brainpoolP320t1 = TeleTrusTObjectIdentifiers.versionOne.branch("10");
    public static final ASN1ObjectIdentifier brainpoolP384r1 = TeleTrusTObjectIdentifiers.versionOne.branch("11");
    public static final ASN1ObjectIdentifier brainpoolP384t1 = TeleTrusTObjectIdentifiers.versionOne.branch("12");
    public static final ASN1ObjectIdentifier brainpoolP512r1 = TeleTrusTObjectIdentifiers.versionOne.branch("13");
    public static final ASN1ObjectIdentifier brainpoolP512t1 = TeleTrusTObjectIdentifiers.versionOne.branch("14");
    public static final ASN1ObjectIdentifier ecSign;
    public static final ASN1ObjectIdentifier ecSignWithRipemd160 = TeleTrusTObjectIdentifiers.ecSign.branch("2");
    public static final ASN1ObjectIdentifier ecSignWithSha1 = (ecSign = TeleTrusTObjectIdentifiers.teleTrusTAlgorithm.branch("3.2")).branch("1");
    public static final ASN1ObjectIdentifier ecc_brainpool;
    public static final ASN1ObjectIdentifier ellipticCurve;
    public static final ASN1ObjectIdentifier ripemd128 = TeleTrusTObjectIdentifiers.teleTrusTAlgorithm.branch("2.2");
    public static final ASN1ObjectIdentifier ripemd160 = (teleTrusTAlgorithm = new ASN1ObjectIdentifier("1.3.36.3")).branch("2.1");
    public static final ASN1ObjectIdentifier ripemd256 = TeleTrusTObjectIdentifiers.teleTrusTAlgorithm.branch("2.3");
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd128 = TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm.branch("3");
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd160 = (teleTrusTRSAsignatureAlgorithm = TeleTrusTObjectIdentifiers.teleTrusTAlgorithm.branch("3.1")).branch("2");
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd256 = TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm.branch("4");
    public static final ASN1ObjectIdentifier teleTrusTAlgorithm;
    public static final ASN1ObjectIdentifier teleTrusTRSAsignatureAlgorithm;
    public static final ASN1ObjectIdentifier versionOne;
}
