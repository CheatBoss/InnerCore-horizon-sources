package org.spongycastle.asn1.misc;

import org.spongycastle.asn1.*;

public interface MiscObjectIdentifiers
{
    public static final ASN1ObjectIdentifier as_sys_sec_alg_ideaCBC = new ASN1ObjectIdentifier("1.3.6.1.4.1.188.7.1.1.2");
    public static final ASN1ObjectIdentifier blake2;
    public static final ASN1ObjectIdentifier cast5CBC = MiscObjectIdentifiers.entrust.branch("66.10");
    public static final ASN1ObjectIdentifier cryptlib;
    public static final ASN1ObjectIdentifier cryptlib_algorithm;
    public static final ASN1ObjectIdentifier cryptlib_algorithm_blowfish_CBC = MiscObjectIdentifiers.cryptlib_algorithm.branch("1.2");
    public static final ASN1ObjectIdentifier cryptlib_algorithm_blowfish_CFB = MiscObjectIdentifiers.cryptlib_algorithm.branch("1.3");
    public static final ASN1ObjectIdentifier cryptlib_algorithm_blowfish_ECB = (cryptlib_algorithm = (cryptlib = new ASN1ObjectIdentifier("1.3.6.1.4.1.3029")).branch("1")).branch("1.1");
    public static final ASN1ObjectIdentifier cryptlib_algorithm_blowfish_OFB = MiscObjectIdentifiers.cryptlib_algorithm.branch("1.4");
    public static final ASN1ObjectIdentifier entrust;
    public static final ASN1ObjectIdentifier entrustVersionExtension = (entrust = new ASN1ObjectIdentifier("1.2.840.113533.7")).branch("65.0");
    public static final ASN1ObjectIdentifier id_blake2b160 = (blake2 = new ASN1ObjectIdentifier("1.3.6.1.4.1.1722.12.2")).branch("1.5");
    public static final ASN1ObjectIdentifier id_blake2b256 = MiscObjectIdentifiers.blake2.branch("1.8");
    public static final ASN1ObjectIdentifier id_blake2b384 = MiscObjectIdentifiers.blake2.branch("1.12");
    public static final ASN1ObjectIdentifier id_blake2b512 = MiscObjectIdentifiers.blake2.branch("1.16");
    public static final ASN1ObjectIdentifier netscape;
    public static final ASN1ObjectIdentifier netscapeBaseURL = MiscObjectIdentifiers.netscape.branch("2");
    public static final ASN1ObjectIdentifier netscapeCARevocationURL = MiscObjectIdentifiers.netscape.branch("4");
    public static final ASN1ObjectIdentifier netscapeCApolicyURL = MiscObjectIdentifiers.netscape.branch("8");
    public static final ASN1ObjectIdentifier netscapeCertComment = MiscObjectIdentifiers.netscape.branch("13");
    public static final ASN1ObjectIdentifier netscapeCertType = (netscape = new ASN1ObjectIdentifier("2.16.840.1.113730.1")).branch("1");
    public static final ASN1ObjectIdentifier netscapeRenewalURL = MiscObjectIdentifiers.netscape.branch("7");
    public static final ASN1ObjectIdentifier netscapeRevocationURL = MiscObjectIdentifiers.netscape.branch("3");
    public static final ASN1ObjectIdentifier netscapeSSLServerName = MiscObjectIdentifiers.netscape.branch("12");
    public static final ASN1ObjectIdentifier novell;
    public static final ASN1ObjectIdentifier novellSecurityAttribs = (novell = new ASN1ObjectIdentifier("2.16.840.1.113719")).branch("1.9.4.1");
    public static final ASN1ObjectIdentifier verisign;
    public static final ASN1ObjectIdentifier verisignBitString_6_13 = MiscObjectIdentifiers.verisign.branch("6.13");
    public static final ASN1ObjectIdentifier verisignCzagExtension = (verisign = new ASN1ObjectIdentifier("2.16.840.1.113733.1")).branch("6.3");
    public static final ASN1ObjectIdentifier verisignDnbDunsNumber = MiscObjectIdentifiers.verisign.branch("6.15");
    public static final ASN1ObjectIdentifier verisignIssStrongCrypto = MiscObjectIdentifiers.verisign.branch("8.1");
    public static final ASN1ObjectIdentifier verisignOnSiteJurisdictionHash = MiscObjectIdentifiers.verisign.branch("6.11");
    public static final ASN1ObjectIdentifier verisignPrivate_6_9 = MiscObjectIdentifiers.verisign.branch("6.9");
}
