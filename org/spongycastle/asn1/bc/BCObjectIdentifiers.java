package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.*;

public interface BCObjectIdentifiers
{
    public static final ASN1ObjectIdentifier bc;
    public static final ASN1ObjectIdentifier bc_exch;
    public static final ASN1ObjectIdentifier bc_pbe;
    public static final ASN1ObjectIdentifier bc_pbe_sha1 = (bc_pbe = (bc = new ASN1ObjectIdentifier("1.3.6.1.4.1.22554")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12 = BCObjectIdentifiers.bc_pbe_sha1.branch("2");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes128_cbc = BCObjectIdentifiers.bc_pbe_sha1_pkcs12.branch("1.2");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes192_cbc = BCObjectIdentifiers.bc_pbe_sha1_pkcs12.branch("1.22");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes256_cbc = BCObjectIdentifiers.bc_pbe_sha1_pkcs12.branch("1.42");
    public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs5 = BCObjectIdentifiers.bc_pbe_sha1.branch("1");
    public static final ASN1ObjectIdentifier bc_pbe_sha224 = BCObjectIdentifiers.bc_pbe.branch("2.4");
    public static final ASN1ObjectIdentifier bc_pbe_sha256 = BCObjectIdentifiers.bc_pbe.branch("2.1");
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12 = BCObjectIdentifiers.bc_pbe_sha256.branch("2");
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes128_cbc = BCObjectIdentifiers.bc_pbe_sha256_pkcs12.branch("1.2");
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes192_cbc = BCObjectIdentifiers.bc_pbe_sha256_pkcs12.branch("1.22");
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes256_cbc = BCObjectIdentifiers.bc_pbe_sha256_pkcs12.branch("1.42");
    public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs5 = BCObjectIdentifiers.bc_pbe_sha256.branch("1");
    public static final ASN1ObjectIdentifier bc_pbe_sha384 = BCObjectIdentifiers.bc_pbe.branch("2.2");
    public static final ASN1ObjectIdentifier bc_pbe_sha512 = BCObjectIdentifiers.bc_pbe.branch("2.3");
    public static final ASN1ObjectIdentifier bc_sig;
    public static final ASN1ObjectIdentifier newHope = (bc_exch = BCObjectIdentifiers.bc.branch("3")).branch("1");
    public static final ASN1ObjectIdentifier sphincs256;
    public static final ASN1ObjectIdentifier sphincs256_with_BLAKE512 = (sphincs256 = (bc_sig = BCObjectIdentifiers.bc.branch("2")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier sphincs256_with_SHA3_512 = BCObjectIdentifiers.sphincs256.branch("3");
    public static final ASN1ObjectIdentifier sphincs256_with_SHA512 = BCObjectIdentifiers.sphincs256.branch("2");
    public static final ASN1ObjectIdentifier xmss;
    public static final ASN1ObjectIdentifier xmss_mt;
    public static final ASN1ObjectIdentifier xmss_mt_with_SHA256 = (xmss_mt = BCObjectIdentifiers.bc_sig.branch("3")).branch("1");
    public static final ASN1ObjectIdentifier xmss_mt_with_SHA512 = BCObjectIdentifiers.xmss_mt.branch("2");
    public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE128 = BCObjectIdentifiers.xmss_mt.branch("3");
    public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE256 = BCObjectIdentifiers.xmss_mt.branch("4");
    public static final ASN1ObjectIdentifier xmss_with_SHA256 = (xmss = BCObjectIdentifiers.bc_sig.branch("2")).branch("1");
    public static final ASN1ObjectIdentifier xmss_with_SHA512 = BCObjectIdentifiers.xmss.branch("2");
    public static final ASN1ObjectIdentifier xmss_with_SHAKE128 = BCObjectIdentifiers.xmss.branch("3");
    public static final ASN1ObjectIdentifier xmss_with_SHAKE256 = BCObjectIdentifiers.xmss.branch("4");
}
