package org.spongycastle.jcajce.provider.asymmetric;

import java.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.rsa.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.teletrust.*;

public class RSA
{
    private static final String PREFIX = "org.spongycastle.jcajce.provider.asymmetric.rsa.";
    private static final Map<String, String> generalRsaAttributes;
    
    static {
        (generalRsaAttributes = new HashMap<String, String>()).put("SupportedKeyClasses", "javax.crypto.interfaces.RSAPublicKey|javax.crypto.interfaces.RSAPrivateKey");
        RSA.generalRsaAttributes.put("SupportedKeyFormats", "PKCS#8|X.509");
    }
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        private void addDigestSignature(final ConfigurableProvider configurableProvider, String string, final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append("WITHRSA");
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("withRSA");
            final String string3 = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("WithRSA");
            final String string4 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string);
            sb4.append("/RSA");
            final String string5 = sb4.toString();
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(string);
            sb5.append("WITHRSAENCRYPTION");
            final String string6 = sb5.toString();
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(string);
            sb6.append("withRSAEncryption");
            final String string7 = sb6.toString();
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(string);
            sb7.append("WithRSAEncryption");
            string = sb7.toString();
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Signature.");
            sb8.append(string2);
            configurableProvider.addAlgorithm(sb8.toString(), s);
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Alg.Alias.Signature.");
            sb9.append(string3);
            configurableProvider.addAlgorithm(sb9.toString(), string2);
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Alg.Alias.Signature.");
            sb10.append(string4);
            configurableProvider.addAlgorithm(sb10.toString(), string2);
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("Alg.Alias.Signature.");
            sb11.append(string6);
            configurableProvider.addAlgorithm(sb11.toString(), string2);
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("Alg.Alias.Signature.");
            sb12.append(string7);
            configurableProvider.addAlgorithm(sb12.toString(), string2);
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("Alg.Alias.Signature.");
            sb13.append(string);
            configurableProvider.addAlgorithm(sb13.toString(), string2);
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("Alg.Alias.Signature.");
            sb14.append(string5);
            configurableProvider.addAlgorithm(sb14.toString(), string2);
            if (asn1ObjectIdentifier != null) {
                final StringBuilder sb15 = new StringBuilder();
                sb15.append("Alg.Alias.Signature.");
                sb15.append(asn1ObjectIdentifier);
                configurableProvider.addAlgorithm(sb15.toString(), string2);
                final StringBuilder sb16 = new StringBuilder();
                sb16.append("Alg.Alias.Signature.OID.");
                sb16.append(asn1ObjectIdentifier);
                configurableProvider.addAlgorithm(sb16.toString(), string2);
            }
        }
        
        private void addISO9796Signature(final ConfigurableProvider configurableProvider, final String s, final String s2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.Signature.");
            sb.append(s);
            sb.append("withRSA/ISO9796-2");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("WITHRSA/ISO9796-2");
            configurableProvider.addAlgorithm(string, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.Signature.");
            sb3.append(s);
            sb3.append("WithRSA/ISO9796-2");
            final String string2 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("WITHRSA/ISO9796-2");
            configurableProvider.addAlgorithm(string2, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Signature.");
            sb5.append(s);
            sb5.append("WITHRSA/ISO9796-2");
            configurableProvider.addAlgorithm(sb5.toString(), s2);
        }
        
        private void addPSSSignature(final ConfigurableProvider configurableProvider, final String s, final String s2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.Signature.");
            sb.append(s);
            sb.append("withRSA/PSS");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("WITHRSAANDMGF1");
            configurableProvider.addAlgorithm(string, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.Signature.");
            sb3.append(s);
            sb3.append("WithRSA/PSS");
            final String string2 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("WITHRSAANDMGF1");
            configurableProvider.addAlgorithm(string2, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Alg.Alias.Signature.");
            sb5.append(s);
            sb5.append("withRSAandMGF1");
            final String string3 = sb5.toString();
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(s);
            sb6.append("WITHRSAANDMGF1");
            configurableProvider.addAlgorithm(string3, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Alg.Alias.Signature.");
            sb7.append(s);
            sb7.append("WithRSAAndMGF1");
            final String string4 = sb7.toString();
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(s);
            sb8.append("WITHRSAANDMGF1");
            configurableProvider.addAlgorithm(string4, sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Signature.");
            sb9.append(s);
            sb9.append("WITHRSAANDMGF1");
            configurableProvider.addAlgorithm(sb9.toString(), s2);
        }
        
        private void addX931Signature(final ConfigurableProvider configurableProvider, final String s, final String s2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.Signature.");
            sb.append(s);
            sb.append("withRSA/X9.31");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("WITHRSA/X9.31");
            configurableProvider.addAlgorithm(string, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.Signature.");
            sb3.append(s);
            sb3.append("WithRSA/X9.31");
            final String string2 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("WITHRSA/X9.31");
            configurableProvider.addAlgorithm(string2, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Signature.");
            sb5.append(s);
            sb5.append("WITHRSA/X9.31");
            configurableProvider.addAlgorithm(sb5.toString(), s2);
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.OAEP", "org.spongycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi$OAEP");
            configurableProvider.addAlgorithm("AlgorithmParameters.PSS", "org.spongycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi$PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RSASSA-PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA224withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA256withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA384withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA512withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA224WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA256WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA384WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA512WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA3-224WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA3-256WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA3-384WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA3-512WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RAWRSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSASSA-PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSAANDMGF1", "PSS");
            configurableProvider.addAttributes("Cipher.RSA", RSA.generalRsaAttributes);
            configurableProvider.addAlgorithm("Cipher.RSA", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/RAW", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/PKCS1", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher", PKCSObjectIdentifiers.rsaEncryption, "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher", X509ObjectIdentifiers.id_ea_rsa, "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher.RSA/1", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding_PrivateOnly");
            configurableProvider.addAlgorithm("Cipher.RSA/2", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding_PublicOnly");
            configurableProvider.addAlgorithm("Cipher.RSA/OAEP", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$OAEPPadding");
            configurableProvider.addAlgorithm("Cipher", PKCSObjectIdentifiers.id_RSAES_OAEP, "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$OAEPPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/ISO9796-1", "org.spongycastle.jcajce.provider.asymmetric.rsa.CipherSpi$ISO9796d1Padding");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//RAW", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//NOPADDING", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//PKCS1PADDING", "RSA/PKCS1");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//OAEPPADDING", "RSA/OAEP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//ISO9796-1PADDING", "RSA/ISO9796-1");
            configurableProvider.addAlgorithm("KeyFactory.RSA", "org.spongycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.RSA", "org.spongycastle.jcajce.provider.asymmetric.rsa.KeyPairGeneratorSpi");
            final KeyFactorySpi keyFactorySpi = new KeyFactorySpi();
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.rsaEncryption, "RSA", keyFactorySpi);
            this.registerOid(configurableProvider, X509ObjectIdentifiers.id_ea_rsa, "RSA", keyFactorySpi);
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.id_RSAES_OAEP, "RSA", keyFactorySpi);
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.id_RSASSA_PSS, "RSA", keyFactorySpi);
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.rsaEncryption, "RSA");
            this.registerOidAlgorithmParameters(configurableProvider, X509ObjectIdentifiers.id_ea_rsa, "RSA");
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.id_RSAES_OAEP, "OAEP");
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.id_RSASSA_PSS, "PSS");
            configurableProvider.addAlgorithm("Signature.RSASSA-PSS", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            final StringBuilder sb = new StringBuilder();
            sb.append("Signature.");
            sb.append(PKCSObjectIdentifiers.id_RSASSA_PSS);
            configurableProvider.addAlgorithm(sb.toString(), "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Signature.OID.");
            sb2.append(PKCSObjectIdentifiers.id_RSASSA_PSS);
            configurableProvider.addAlgorithm(sb2.toString(), "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            configurableProvider.addAlgorithm("Signature.RSA", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$noneRSA");
            configurableProvider.addAlgorithm("Signature.RAWRSASSA-PSS", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$nonePSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RAWRSA", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSA", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RAWRSAPSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSAPSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSASSA-PSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSAANDMGF1", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RSAPSS", "RSASSA-PSS");
            this.addPSSSignature(configurableProvider, "SHA224", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA224withRSA");
            this.addPSSSignature(configurableProvider, "SHA256", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA256withRSA");
            this.addPSSSignature(configurableProvider, "SHA384", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA384withRSA");
            this.addPSSSignature(configurableProvider, "SHA512", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512withRSA");
            this.addPSSSignature(configurableProvider, "SHA512(224)", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512_224withRSA");
            this.addPSSSignature(configurableProvider, "SHA512(256)", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512_256withRSA");
            this.addPSSSignature(configurableProvider, "SHA3-224", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA3_224withRSA");
            this.addPSSSignature(configurableProvider, "SHA3-256", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA3_256withRSA");
            this.addPSSSignature(configurableProvider, "SHA3-384", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA3_384withRSA");
            this.addPSSSignature(configurableProvider, "SHA3-512", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA3_512withRSA");
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD2")) {
                this.addDigestSignature(configurableProvider, "MD2", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD2", PKCSObjectIdentifiers.md2WithRSAEncryption);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD4")) {
                this.addDigestSignature(configurableProvider, "MD4", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD4", PKCSObjectIdentifiers.md4WithRSAEncryption);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD5")) {
                this.addDigestSignature(configurableProvider, "MD5", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD5", PKCSObjectIdentifiers.md5WithRSAEncryption);
                this.addISO9796Signature(configurableProvider, "MD5", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$MD5WithRSAEncryption");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "SHA1")) {
                configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA1withRSA/PSS", "PSS");
                configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA1WITHRSAANDMGF1", "PSS");
                this.addPSSSignature(configurableProvider, "SHA1", "org.spongycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA1withRSA");
                this.addDigestSignature(configurableProvider, "SHA1", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA1", PKCSObjectIdentifiers.sha1WithRSAEncryption);
                this.addISO9796Signature(configurableProvider, "SHA1", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA1WithRSAEncryption");
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Alg.Alias.Signature.");
                sb3.append(OIWObjectIdentifiers.sha1WithRSA);
                configurableProvider.addAlgorithm(sb3.toString(), "SHA1WITHRSA");
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Alg.Alias.Signature.OID.");
                sb4.append(OIWObjectIdentifiers.sha1WithRSA);
                configurableProvider.addAlgorithm(sb4.toString(), "SHA1WITHRSA");
                this.addX931Signature(configurableProvider, "SHA1", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA1WithRSAEncryption");
            }
            this.addDigestSignature(configurableProvider, "SHA224", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA224", PKCSObjectIdentifiers.sha224WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA256", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA256", PKCSObjectIdentifiers.sha256WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA384", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA384", PKCSObjectIdentifiers.sha384WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA512", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA512", PKCSObjectIdentifiers.sha512WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA512(224)", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA512_224", PKCSObjectIdentifiers.sha512_224WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA512(256)", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA512_256", PKCSObjectIdentifiers.sha512_256WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA3-224", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA3_224", NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_224);
            this.addDigestSignature(configurableProvider, "SHA3-256", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA3_256", NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_256);
            this.addDigestSignature(configurableProvider, "SHA3-384", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA3_384", NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_384);
            this.addDigestSignature(configurableProvider, "SHA3-512", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA3_512", NISTObjectIdentifiers.id_rsassa_pkcs1_v1_5_with_sha3_512);
            this.addISO9796Signature(configurableProvider, "SHA224", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA224WithRSAEncryption");
            this.addISO9796Signature(configurableProvider, "SHA256", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA256WithRSAEncryption");
            this.addISO9796Signature(configurableProvider, "SHA384", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA384WithRSAEncryption");
            this.addISO9796Signature(configurableProvider, "SHA512", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA512WithRSAEncryption");
            this.addISO9796Signature(configurableProvider, "SHA512(224)", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA512_224WithRSAEncryption");
            this.addISO9796Signature(configurableProvider, "SHA512(256)", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA512_256WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA224", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA224WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA256", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA256WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA384", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA384WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA512", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA512WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA512(224)", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA512_224WithRSAEncryption");
            this.addX931Signature(configurableProvider, "SHA512(256)", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA512_256WithRSAEncryption");
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD128")) {
                this.addDigestSignature(configurableProvider, "RIPEMD128", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD128", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
                this.addDigestSignature(configurableProvider, "RMD128", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD128", null);
                this.addX931Signature(configurableProvider, "RMD128", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD128WithRSAEncryption");
                this.addX931Signature(configurableProvider, "RIPEMD128", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD128WithRSAEncryption");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD160")) {
                this.addDigestSignature(configurableProvider, "RIPEMD160", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD160", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
                this.addDigestSignature(configurableProvider, "RMD160", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD160", null);
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD160WithRSA/ISO9796-2", "RIPEMD160withRSA/ISO9796-2");
                configurableProvider.addAlgorithm("Signature.RIPEMD160withRSA/ISO9796-2", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$RIPEMD160WithRSAEncryption");
                this.addX931Signature(configurableProvider, "RMD160", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD160WithRSAEncryption");
                this.addX931Signature(configurableProvider, "RIPEMD160", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD160WithRSAEncryption");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD256")) {
                this.addDigestSignature(configurableProvider, "RIPEMD256", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD256", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
                this.addDigestSignature(configurableProvider, "RMD256", "org.spongycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD256", null);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "WHIRLPOOL")) {
                this.addISO9796Signature(configurableProvider, "Whirlpool", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$WhirlpoolWithRSAEncryption");
                this.addISO9796Signature(configurableProvider, "WHIRLPOOL", "org.spongycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$WhirlpoolWithRSAEncryption");
                this.addX931Signature(configurableProvider, "Whirlpool", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$WhirlpoolWithRSAEncryption");
                this.addX931Signature(configurableProvider, "WHIRLPOOL", "org.spongycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$WhirlpoolWithRSAEncryption");
            }
        }
    }
}
