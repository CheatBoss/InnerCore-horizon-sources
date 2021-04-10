package org.spongycastle.asn1.gm;

import org.spongycastle.asn1.*;

public interface GMObjectIdentifiers
{
    public static final ASN1ObjectIdentifier hmac_sm3 = (sm3 = GMObjectIdentifiers.sm_scheme.branch("401")).branch("2");
    public static final ASN1ObjectIdentifier id_sm9PublicKey = GMObjectIdentifiers.sm_scheme.branch("302");
    public static final ASN1ObjectIdentifier sm1_cbc = GMObjectIdentifiers.sm_scheme.branch("102.2");
    public static final ASN1ObjectIdentifier sm1_cfb1 = GMObjectIdentifiers.sm_scheme.branch("102.5");
    public static final ASN1ObjectIdentifier sm1_cfb128 = GMObjectIdentifiers.sm_scheme.branch("102.4");
    public static final ASN1ObjectIdentifier sm1_cfb8 = GMObjectIdentifiers.sm_scheme.branch("102.6");
    public static final ASN1ObjectIdentifier sm1_ecb = GMObjectIdentifiers.sm_scheme.branch("102.1");
    public static final ASN1ObjectIdentifier sm1_ofb128 = GMObjectIdentifiers.sm_scheme.branch("102.3");
    public static final ASN1ObjectIdentifier sm2encrypt = GMObjectIdentifiers.sm_scheme.branch("301.3");
    public static final ASN1ObjectIdentifier sm2encrypt_recommendedParameters = GMObjectIdentifiers.sm2encrypt.branch("1");
    public static final ASN1ObjectIdentifier sm2encrypt_specifiedParameters = GMObjectIdentifiers.sm2encrypt.branch("2");
    public static final ASN1ObjectIdentifier sm2encrypt_with_blake2b512 = GMObjectIdentifiers.sm2encrypt.branch("2.9");
    public static final ASN1ObjectIdentifier sm2encrypt_with_blake2s256 = GMObjectIdentifiers.sm2encrypt.branch("2.10");
    public static final ASN1ObjectIdentifier sm2encrypt_with_md5 = GMObjectIdentifiers.sm2encrypt.branch("2.11");
    public static final ASN1ObjectIdentifier sm2encrypt_with_rmd160 = GMObjectIdentifiers.sm2encrypt.branch("2.7");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sha1 = GMObjectIdentifiers.sm2encrypt.branch("2.2");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sha224 = GMObjectIdentifiers.sm2encrypt.branch("2.3");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sha256 = GMObjectIdentifiers.sm2encrypt.branch("2.4");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sha384 = GMObjectIdentifiers.sm2encrypt.branch("2.5");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sha512 = GMObjectIdentifiers.sm2encrypt.branch("2.6");
    public static final ASN1ObjectIdentifier sm2encrypt_with_sm3 = GMObjectIdentifiers.sm2encrypt.branch("2.1");
    public static final ASN1ObjectIdentifier sm2encrypt_with_whirlpool = GMObjectIdentifiers.sm2encrypt.branch("2.8");
    public static final ASN1ObjectIdentifier sm2exchange = GMObjectIdentifiers.sm_scheme.branch("301.2");
    public static final ASN1ObjectIdentifier sm2p256v1 = GMObjectIdentifiers.sm_scheme.branch("301");
    public static final ASN1ObjectIdentifier sm2sign = GMObjectIdentifiers.sm_scheme.branch("301.1");
    public static final ASN1ObjectIdentifier sm2sign_with_blake2b512 = GMObjectIdentifiers.sm_scheme.branch("521");
    public static final ASN1ObjectIdentifier sm2sign_with_blake2s256 = GMObjectIdentifiers.sm_scheme.branch("522");
    public static final ASN1ObjectIdentifier sm2sign_with_rmd160 = GMObjectIdentifiers.sm_scheme.branch("507");
    public static final ASN1ObjectIdentifier sm2sign_with_sha1 = GMObjectIdentifiers.sm_scheme.branch("502");
    public static final ASN1ObjectIdentifier sm2sign_with_sha224 = GMObjectIdentifiers.sm_scheme.branch("505");
    public static final ASN1ObjectIdentifier sm2sign_with_sha256 = GMObjectIdentifiers.sm_scheme.branch("503");
    public static final ASN1ObjectIdentifier sm2sign_with_sha384 = GMObjectIdentifiers.sm_scheme.branch("506");
    public static final ASN1ObjectIdentifier sm2sign_with_sha512 = GMObjectIdentifiers.sm_scheme.branch("504");
    public static final ASN1ObjectIdentifier sm2sign_with_sm3 = GMObjectIdentifiers.sm_scheme.branch("501");
    public static final ASN1ObjectIdentifier sm2sign_with_whirlpool = GMObjectIdentifiers.sm_scheme.branch("520");
    public static final ASN1ObjectIdentifier sm3;
    public static final ASN1ObjectIdentifier sm5 = GMObjectIdentifiers.sm_scheme.branch("201");
    public static final ASN1ObjectIdentifier sm6_cbc = GMObjectIdentifiers.sm_scheme.branch("101.2");
    public static final ASN1ObjectIdentifier sm6_cfb128 = GMObjectIdentifiers.sm_scheme.branch("101.4");
    public static final ASN1ObjectIdentifier sm6_ecb = (sm_scheme = new ASN1ObjectIdentifier("1.2.156.10197.1")).branch("101.1");
    public static final ASN1ObjectIdentifier sm6_ofb128 = GMObjectIdentifiers.sm_scheme.branch("101.3");
    public static final ASN1ObjectIdentifier sm9encrypt = GMObjectIdentifiers.sm_scheme.branch("302.3");
    public static final ASN1ObjectIdentifier sm9keyagreement = GMObjectIdentifiers.sm_scheme.branch("302.2");
    public static final ASN1ObjectIdentifier sm9sign = GMObjectIdentifiers.sm_scheme.branch("302.1");
    public static final ASN1ObjectIdentifier sm_scheme;
    public static final ASN1ObjectIdentifier sms4_cbc = GMObjectIdentifiers.sm_scheme.branch("104.2");
    public static final ASN1ObjectIdentifier sms4_ccm = GMObjectIdentifiers.sm_scheme.branch("104.9");
    public static final ASN1ObjectIdentifier sms4_cfb1 = GMObjectIdentifiers.sm_scheme.branch("104.5");
    public static final ASN1ObjectIdentifier sms4_cfb128 = GMObjectIdentifiers.sm_scheme.branch("104.4");
    public static final ASN1ObjectIdentifier sms4_cfb8 = GMObjectIdentifiers.sm_scheme.branch("104.6");
    public static final ASN1ObjectIdentifier sms4_ctr = GMObjectIdentifiers.sm_scheme.branch("104.7");
    public static final ASN1ObjectIdentifier sms4_ecb = GMObjectIdentifiers.sm_scheme.branch("104.1");
    public static final ASN1ObjectIdentifier sms4_gcm = GMObjectIdentifiers.sm_scheme.branch("104.8");
    public static final ASN1ObjectIdentifier sms4_ocb = GMObjectIdentifiers.sm_scheme.branch("104.100");
    public static final ASN1ObjectIdentifier sms4_ofb128 = GMObjectIdentifiers.sm_scheme.branch("104.3");
    public static final ASN1ObjectIdentifier sms4_wrap = GMObjectIdentifiers.sm_scheme.branch("104.11");
    public static final ASN1ObjectIdentifier sms4_wrap_pad = GMObjectIdentifiers.sm_scheme.branch("104.12");
    public static final ASN1ObjectIdentifier sms4_xts = GMObjectIdentifiers.sm_scheme.branch("104.10");
    public static final ASN1ObjectIdentifier ssf33_cbc = GMObjectIdentifiers.sm_scheme.branch("103.2");
    public static final ASN1ObjectIdentifier ssf33_cfb1 = GMObjectIdentifiers.sm_scheme.branch("103.5");
    public static final ASN1ObjectIdentifier ssf33_cfb128 = GMObjectIdentifiers.sm_scheme.branch("103.4");
    public static final ASN1ObjectIdentifier ssf33_cfb8 = GMObjectIdentifiers.sm_scheme.branch("103.6");
    public static final ASN1ObjectIdentifier ssf33_ecb = GMObjectIdentifiers.sm_scheme.branch("103.1");
    public static final ASN1ObjectIdentifier ssf33_ofb128 = GMObjectIdentifiers.sm_scheme.branch("103.3");
    public static final ASN1ObjectIdentifier wapip192v1 = GMObjectIdentifiers.sm_scheme.branch("301.101");
}
