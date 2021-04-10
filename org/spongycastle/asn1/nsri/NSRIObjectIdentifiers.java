package org.spongycastle.asn1.nsri;

import org.spongycastle.asn1.*;

public interface NSRIObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_algorithm;
    public static final ASN1ObjectIdentifier id_aria128_cbc = NSRIObjectIdentifiers.id_sea.branch("2");
    public static final ASN1ObjectIdentifier id_aria128_ccm = NSRIObjectIdentifiers.id_sea.branch("37");
    public static final ASN1ObjectIdentifier id_aria128_cfb = NSRIObjectIdentifiers.id_sea.branch("3");
    public static final ASN1ObjectIdentifier id_aria128_cmac = NSRIObjectIdentifiers.id_sea.branch("21");
    public static final ASN1ObjectIdentifier id_aria128_ctr = NSRIObjectIdentifiers.id_sea.branch("5");
    public static final ASN1ObjectIdentifier id_aria128_ecb = NSRIObjectIdentifiers.id_sea.branch("1");
    public static final ASN1ObjectIdentifier id_aria128_gcm = NSRIObjectIdentifiers.id_sea.branch("34");
    public static final ASN1ObjectIdentifier id_aria128_kw = NSRIObjectIdentifiers.id_sea.branch("40");
    public static final ASN1ObjectIdentifier id_aria128_kwp = NSRIObjectIdentifiers.id_sea.branch("43");
    public static final ASN1ObjectIdentifier id_aria128_ocb2 = NSRIObjectIdentifiers.id_sea.branch("31");
    public static final ASN1ObjectIdentifier id_aria128_ofb = NSRIObjectIdentifiers.id_sea.branch("4");
    public static final ASN1ObjectIdentifier id_aria192_cbc = NSRIObjectIdentifiers.id_sea.branch("7");
    public static final ASN1ObjectIdentifier id_aria192_ccm = NSRIObjectIdentifiers.id_sea.branch("38");
    public static final ASN1ObjectIdentifier id_aria192_cfb = NSRIObjectIdentifiers.id_sea.branch("8");
    public static final ASN1ObjectIdentifier id_aria192_cmac = NSRIObjectIdentifiers.id_sea.branch("22");
    public static final ASN1ObjectIdentifier id_aria192_ctr = NSRIObjectIdentifiers.id_sea.branch("10");
    public static final ASN1ObjectIdentifier id_aria192_ecb = NSRIObjectIdentifiers.id_sea.branch("6");
    public static final ASN1ObjectIdentifier id_aria192_gcm = NSRIObjectIdentifiers.id_sea.branch("35");
    public static final ASN1ObjectIdentifier id_aria192_kw = NSRIObjectIdentifiers.id_sea.branch("41");
    public static final ASN1ObjectIdentifier id_aria192_kwp = NSRIObjectIdentifiers.id_sea.branch("44");
    public static final ASN1ObjectIdentifier id_aria192_ocb2 = NSRIObjectIdentifiers.id_sea.branch("32");
    public static final ASN1ObjectIdentifier id_aria192_ofb = NSRIObjectIdentifiers.id_sea.branch("9");
    public static final ASN1ObjectIdentifier id_aria256_cbc = NSRIObjectIdentifiers.id_sea.branch("12");
    public static final ASN1ObjectIdentifier id_aria256_ccm = NSRIObjectIdentifiers.id_sea.branch("39");
    public static final ASN1ObjectIdentifier id_aria256_cfb = NSRIObjectIdentifiers.id_sea.branch("13");
    public static final ASN1ObjectIdentifier id_aria256_cmac = NSRIObjectIdentifiers.id_sea.branch("23");
    public static final ASN1ObjectIdentifier id_aria256_ctr = NSRIObjectIdentifiers.id_sea.branch("15");
    public static final ASN1ObjectIdentifier id_aria256_ecb = NSRIObjectIdentifiers.id_sea.branch("11");
    public static final ASN1ObjectIdentifier id_aria256_gcm = NSRIObjectIdentifiers.id_sea.branch("36");
    public static final ASN1ObjectIdentifier id_aria256_kw = NSRIObjectIdentifiers.id_sea.branch("42");
    public static final ASN1ObjectIdentifier id_aria256_kwp = NSRIObjectIdentifiers.id_sea.branch("45");
    public static final ASN1ObjectIdentifier id_aria256_ocb2 = NSRIObjectIdentifiers.id_sea.branch("33");
    public static final ASN1ObjectIdentifier id_aria256_ofb = NSRIObjectIdentifiers.id_sea.branch("14");
    public static final ASN1ObjectIdentifier id_pad = NSRIObjectIdentifiers.id_algorithm.branch("2");
    public static final ASN1ObjectIdentifier id_pad_1 = NSRIObjectIdentifiers.id_algorithm.branch("1");
    public static final ASN1ObjectIdentifier id_pad_null = NSRIObjectIdentifiers.id_algorithm.branch("0");
    public static final ASN1ObjectIdentifier id_sea = (id_algorithm = (nsri = new ASN1ObjectIdentifier("1.2.410.200046")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier nsri;
}
