package org.spongycastle.jce;

import java.util.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.oiw.*;
import javax.security.auth.x500.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.asn1.*;

public class PKCS10CertificationRequest extends CertificationRequest
{
    private static Hashtable algorithms;
    private static Hashtable keyAlgorithms;
    private static Set noParams;
    private static Hashtable oids;
    private static Hashtable params;
    
    static {
        PKCS10CertificationRequest.algorithms = new Hashtable();
        PKCS10CertificationRequest.params = new Hashtable();
        PKCS10CertificationRequest.keyAlgorithms = new Hashtable();
        PKCS10CertificationRequest.oids = new Hashtable();
        PKCS10CertificationRequest.noParams = new HashSet();
        PKCS10CertificationRequest.algorithms.put("MD2WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
        PKCS10CertificationRequest.algorithms.put("MD2WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
        PKCS10CertificationRequest.algorithms.put("MD5WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        PKCS10CertificationRequest.algorithms.put("MD5WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        PKCS10CertificationRequest.algorithms.put("RSAWITHMD5", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        PKCS10CertificationRequest.algorithms.put("SHA1WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
        PKCS10CertificationRequest.algorithms.put("SHA1WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
        PKCS10CertificationRequest.algorithms.put("SHA224WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha224WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA224WITHRSA", PKCSObjectIdentifiers.sha224WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA256WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA256WITHRSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA384WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha384WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA384WITHRSA", PKCSObjectIdentifiers.sha384WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA512WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA512WITHRSA", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        PKCS10CertificationRequest.algorithms.put("SHA1WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
        PKCS10CertificationRequest.algorithms.put("SHA224WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
        PKCS10CertificationRequest.algorithms.put("SHA256WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
        PKCS10CertificationRequest.algorithms.put("SHA384WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
        PKCS10CertificationRequest.algorithms.put("SHA512WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
        PKCS10CertificationRequest.algorithms.put("RSAWITHSHA1", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
        PKCS10CertificationRequest.algorithms.put("RIPEMD128WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        PKCS10CertificationRequest.algorithms.put("RIPEMD128WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        PKCS10CertificationRequest.algorithms.put("RIPEMD160WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        PKCS10CertificationRequest.algorithms.put("RIPEMD160WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        PKCS10CertificationRequest.algorithms.put("RIPEMD256WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        PKCS10CertificationRequest.algorithms.put("RIPEMD256WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        PKCS10CertificationRequest.algorithms.put("SHA1WITHDSA", new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
        PKCS10CertificationRequest.algorithms.put("DSAWITHSHA1", new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
        PKCS10CertificationRequest.algorithms.put("SHA224WITHDSA", NISTObjectIdentifiers.dsa_with_sha224);
        PKCS10CertificationRequest.algorithms.put("SHA256WITHDSA", NISTObjectIdentifiers.dsa_with_sha256);
        PKCS10CertificationRequest.algorithms.put("SHA384WITHDSA", NISTObjectIdentifiers.dsa_with_sha384);
        PKCS10CertificationRequest.algorithms.put("SHA512WITHDSA", NISTObjectIdentifiers.dsa_with_sha512);
        PKCS10CertificationRequest.algorithms.put("SHA1WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA1);
        PKCS10CertificationRequest.algorithms.put("SHA224WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA224);
        PKCS10CertificationRequest.algorithms.put("SHA256WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA256);
        PKCS10CertificationRequest.algorithms.put("SHA384WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA384);
        PKCS10CertificationRequest.algorithms.put("SHA512WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA512);
        PKCS10CertificationRequest.algorithms.put("ECDSAWITHSHA1", X9ObjectIdentifiers.ecdsa_with_SHA1);
        PKCS10CertificationRequest.algorithms.put("GOST3411WITHGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        PKCS10CertificationRequest.algorithms.put("GOST3410WITHGOST3411", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        PKCS10CertificationRequest.algorithms.put("GOST3411WITHECGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        PKCS10CertificationRequest.algorithms.put("GOST3411WITHECGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        PKCS10CertificationRequest.algorithms.put("GOST3411WITHGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        PKCS10CertificationRequest.oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"), "SHA1WITHRSA");
        PKCS10CertificationRequest.oids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224WITHRSA");
        PKCS10CertificationRequest.oids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256WITHRSA");
        PKCS10CertificationRequest.oids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384WITHRSA");
        PKCS10CertificationRequest.oids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512WITHRSA");
        PKCS10CertificationRequest.oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3411WITHGOST3410");
        PKCS10CertificationRequest.oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, "GOST3411WITHECGOST3410");
        PKCS10CertificationRequest.oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"), "MD5WITHRSA");
        PKCS10CertificationRequest.oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"), "MD2WITHRSA");
        PKCS10CertificationRequest.oids.put(new ASN1ObjectIdentifier("1.2.840.10040.4.3"), "SHA1WITHDSA");
        PKCS10CertificationRequest.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1WITHECDSA");
        PKCS10CertificationRequest.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224WITHECDSA");
        PKCS10CertificationRequest.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256WITHECDSA");
        PKCS10CertificationRequest.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384WITHECDSA");
        PKCS10CertificationRequest.oids.put(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512WITHECDSA");
        PKCS10CertificationRequest.oids.put(OIWObjectIdentifiers.sha1WithRSA, "SHA1WITHRSA");
        PKCS10CertificationRequest.oids.put(OIWObjectIdentifiers.dsaWithSHA1, "SHA1WITHDSA");
        PKCS10CertificationRequest.oids.put(NISTObjectIdentifiers.dsa_with_sha224, "SHA224WITHDSA");
        PKCS10CertificationRequest.oids.put(NISTObjectIdentifiers.dsa_with_sha256, "SHA256WITHDSA");
        PKCS10CertificationRequest.keyAlgorithms.put(PKCSObjectIdentifiers.rsaEncryption, "RSA");
        PKCS10CertificationRequest.keyAlgorithms.put(X9ObjectIdentifiers.id_dsa, "DSA");
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA1);
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA224);
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA256);
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA384);
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA512);
        PKCS10CertificationRequest.noParams.add(X9ObjectIdentifiers.id_dsa_with_sha1);
        PKCS10CertificationRequest.noParams.add(NISTObjectIdentifiers.dsa_with_sha224);
        PKCS10CertificationRequest.noParams.add(NISTObjectIdentifiers.dsa_with_sha256);
        PKCS10CertificationRequest.noParams.add(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        PKCS10CertificationRequest.noParams.add(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        PKCS10CertificationRequest.params.put("SHA1WITHRSAANDMGF1", creatPSSParams(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE), 20));
        PKCS10CertificationRequest.params.put("SHA224WITHRSAANDMGF1", creatPSSParams(new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, DERNull.INSTANCE), 28));
        PKCS10CertificationRequest.params.put("SHA256WITHRSAANDMGF1", creatPSSParams(new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, DERNull.INSTANCE), 32));
        PKCS10CertificationRequest.params.put("SHA384WITHRSAANDMGF1", creatPSSParams(new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, DERNull.INSTANCE), 48));
        PKCS10CertificationRequest.params.put("SHA512WITHRSAANDMGF1", creatPSSParams(new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, DERNull.INSTANCE), 64));
    }
    
    public PKCS10CertificationRequest(final String s, final X500Principal x500Principal, final PublicKey publicKey, final ASN1Set set, final PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        this(s, convertName(x500Principal), publicKey, set, privateKey, "SC");
    }
    
    public PKCS10CertificationRequest(final String s, final X500Principal x500Principal, final PublicKey publicKey, final ASN1Set set, final PrivateKey privateKey, final String s2) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        this(s, convertName(x500Principal), publicKey, set, privateKey, s2);
    }
    
    public PKCS10CertificationRequest(final String s, final X509Name x509Name, final PublicKey publicKey, final ASN1Set set, final PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        this(s, x509Name, publicKey, set, privateKey, "SC");
    }
    
    public PKCS10CertificationRequest(final String s, final X509Name x509Name, final PublicKey publicKey, final ASN1Set set, final PrivateKey privateKey, final String s2) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        final String upperCase = Strings.toUpperCase(s);
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        if ((asn1ObjectIdentifier = PKCS10CertificationRequest.algorithms.get(upperCase)) == null) {
            try {
                asn1ObjectIdentifier = new ASN1ObjectIdentifier(upperCase);
            }
            catch (Exception ex2) {
                throw new IllegalArgumentException("Unknown signature type requested");
            }
        }
        if (x509Name != null) {
            if (publicKey != null) {
                Label_0154: {
                    AlgorithmIdentifier sigAlgId;
                    if (PKCS10CertificationRequest.noParams.contains(asn1ObjectIdentifier)) {
                        sigAlgId = new AlgorithmIdentifier(asn1ObjectIdentifier);
                    }
                    else {
                        if (PKCS10CertificationRequest.params.containsKey(upperCase)) {
                            this.sigAlgId = new AlgorithmIdentifier(asn1ObjectIdentifier, PKCS10CertificationRequest.params.get(upperCase));
                            break Label_0154;
                        }
                        sigAlgId = new AlgorithmIdentifier(asn1ObjectIdentifier, DERNull.INSTANCE);
                    }
                    this.sigAlgId = sigAlgId;
                    try {
                        this.reqInfo = new CertificationRequestInfo(x509Name, SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded())), set);
                        Signature signature;
                        if (s2 == null) {
                            signature = Signature.getInstance(s);
                        }
                        else {
                            signature = Signature.getInstance(s, s2);
                        }
                        signature.initSign(privateKey);
                        try {
                            signature.update(this.reqInfo.getEncoded("DER"));
                            this.sigBits = new DERBitString(signature.sign());
                            return;
                        }
                        catch (Exception ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("exception encoding TBS cert request - ");
                            sb.append(ex);
                            throw new IllegalArgumentException(sb.toString());
                        }
                    }
                    catch (IOException ex3) {
                        throw new IllegalArgumentException("can't encode public key");
                    }
                }
            }
            throw new IllegalArgumentException("public key must not be null");
        }
        throw new IllegalArgumentException("subject must not be null");
    }
    
    public PKCS10CertificationRequest(final ASN1Sequence asn1Sequence) {
        super(asn1Sequence);
    }
    
    public PKCS10CertificationRequest(final byte[] array) {
        super(toDERSequence(array));
    }
    
    private static X509Name convertName(final X500Principal x500Principal) {
        try {
            return new X509Principal(x500Principal.getEncoded());
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("can't convert name");
        }
    }
    
    private static RSASSAPSSparams creatPSSParams(final AlgorithmIdentifier algorithmIdentifier, final int n) {
        return new RSASSAPSSparams(algorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, algorithmIdentifier), new ASN1Integer(n), new ASN1Integer(1L));
    }
    
    private static String getDigestAlgName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        if (PKCSObjectIdentifiers.md5.equals(asn1ObjectIdentifier)) {
            return "MD5";
        }
        if (OIWObjectIdentifiers.idSHA1.equals(asn1ObjectIdentifier)) {
            return "SHA1";
        }
        if (NISTObjectIdentifiers.id_sha224.equals(asn1ObjectIdentifier)) {
            return "SHA224";
        }
        if (NISTObjectIdentifiers.id_sha256.equals(asn1ObjectIdentifier)) {
            return "SHA256";
        }
        if (NISTObjectIdentifiers.id_sha384.equals(asn1ObjectIdentifier)) {
            return "SHA384";
        }
        if (NISTObjectIdentifiers.id_sha512.equals(asn1ObjectIdentifier)) {
            return "SHA512";
        }
        if (TeleTrusTObjectIdentifiers.ripemd128.equals(asn1ObjectIdentifier)) {
            return "RIPEMD128";
        }
        if (TeleTrusTObjectIdentifiers.ripemd160.equals(asn1ObjectIdentifier)) {
            return "RIPEMD160";
        }
        if (TeleTrusTObjectIdentifiers.ripemd256.equals(asn1ObjectIdentifier)) {
            return "RIPEMD256";
        }
        if (CryptoProObjectIdentifiers.gostR3411.equals(asn1ObjectIdentifier)) {
            return "GOST3411";
        }
        return asn1ObjectIdentifier.getId();
    }
    
    static String getSignatureName(final AlgorithmIdentifier algorithmIdentifier) {
        final ASN1Encodable parameters = algorithmIdentifier.getParameters();
        if (parameters != null && !DERNull.INSTANCE.equals(parameters) && algorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
            final RSASSAPSSparams instance = RSASSAPSSparams.getInstance(parameters);
            final StringBuilder sb = new StringBuilder();
            sb.append(getDigestAlgName(instance.getHashAlgorithm().getAlgorithm()));
            sb.append("withRSAandMGF1");
            return sb.toString();
        }
        return algorithmIdentifier.getAlgorithm().getId();
    }
    
    private void setSignatureParameters(final Signature signature, final ASN1Encodable asn1Encodable) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (asn1Encodable != null && !DERNull.INSTANCE.equals(asn1Encodable)) {
            final AlgorithmParameters instance = AlgorithmParameters.getInstance(signature.getAlgorithm(), signature.getProvider());
            try {
                instance.init(asn1Encodable.toASN1Primitive().getEncoded("DER"));
                if (signature.getAlgorithm().endsWith("MGF1")) {
                    try {
                        signature.setParameter(instance.getParameterSpec(PSSParameterSpec.class));
                    }
                    catch (GeneralSecurityException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Exception extracting parameters: ");
                        sb.append(ex.getMessage());
                        throw new SignatureException(sb.toString());
                    }
                }
            }
            catch (IOException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IOException decoding parameters: ");
                sb2.append(ex2.getMessage());
                throw new SignatureException(sb2.toString());
            }
        }
    }
    
    private static ASN1Sequence toDERSequence(final byte[] array) {
        try {
            return (ASN1Sequence)new ASN1InputStream(array).readObject();
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("badly encoded request");
        }
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return this.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }
    
    public PublicKey getPublicKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        return this.getPublicKey("SC");
    }
    
    public PublicKey getPublicKey(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/spongycastle/jce/PKCS10CertificationRequest.reqInfo:Lorg/spongycastle/asn1/pkcs/CertificationRequestInfo;
        //     4: invokevirtual   org/spongycastle/asn1/pkcs/CertificationRequestInfo.getSubjectPublicKeyInfo:()Lorg/spongycastle/asn1/x509/SubjectPublicKeyInfo;
        //     7: astore_3       
        //     8: new             Ljava/security/spec/X509EncodedKeySpec;
        //    11: dup            
        //    12: new             Lorg/spongycastle/asn1/DERBitString;
        //    15: dup            
        //    16: aload_3        
        //    17: invokespecial   org/spongycastle/asn1/DERBitString.<init>:(Lorg/spongycastle/asn1/ASN1Encodable;)V
        //    20: invokevirtual   org/spongycastle/asn1/DERBitString.getOctets:()[B
        //    23: invokespecial   java/security/spec/X509EncodedKeySpec.<init>:([B)V
        //    26: astore_2       
        //    27: aload_3        
        //    28: invokevirtual   org/spongycastle/asn1/x509/SubjectPublicKeyInfo.getAlgorithm:()Lorg/spongycastle/asn1/x509/AlgorithmIdentifier;
        //    31: astore_3       
        //    32: aload_1        
        //    33: ifnonnull       51
        //    36: aload_3        
        //    37: invokevirtual   org/spongycastle/asn1/x509/AlgorithmIdentifier.getAlgorithm:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //    40: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //    43: invokestatic    java/security/KeyFactory.getInstance:(Ljava/lang/String;)Ljava/security/KeyFactory;
        //    46: aload_2        
        //    47: invokevirtual   java/security/KeyFactory.generatePublic:(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
        //    50: areturn        
        //    51: aload_3        
        //    52: invokevirtual   org/spongycastle/asn1/x509/AlgorithmIdentifier.getAlgorithm:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //    55: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //    58: aload_1        
        //    59: invokestatic    java/security/KeyFactory.getInstance:(Ljava/lang/String;Ljava/lang/String;)Ljava/security/KeyFactory;
        //    62: aload_2        
        //    63: invokevirtual   java/security/KeyFactory.generatePublic:(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
        //    66: astore          4
        //    68: aload           4
        //    70: areturn        
        //    71: getstatic       org/spongycastle/jce/PKCS10CertificationRequest.keyAlgorithms:Ljava/util/Hashtable;
        //    74: aload_3        
        //    75: invokevirtual   org/spongycastle/asn1/x509/AlgorithmIdentifier.getAlgorithm:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //    78: invokevirtual   java/util/Hashtable.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    81: ifnull          121
        //    84: getstatic       org/spongycastle/jce/PKCS10CertificationRequest.keyAlgorithms:Ljava/util/Hashtable;
        //    87: aload_3        
        //    88: invokevirtual   org/spongycastle/asn1/x509/AlgorithmIdentifier.getAlgorithm:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //    91: invokevirtual   java/util/Hashtable.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    94: checkcast       Ljava/lang/String;
        //    97: astore_3       
        //    98: aload_1        
        //    99: ifnonnull       111
        //   102: aload_3        
        //   103: invokestatic    java/security/KeyFactory.getInstance:(Ljava/lang/String;)Ljava/security/KeyFactory;
        //   106: aload_2        
        //   107: invokevirtual   java/security/KeyFactory.generatePublic:(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
        //   110: areturn        
        //   111: aload_3        
        //   112: aload_1        
        //   113: invokestatic    java/security/KeyFactory.getInstance:(Ljava/lang/String;Ljava/lang/String;)Ljava/security/KeyFactory;
        //   116: aload_2        
        //   117: invokevirtual   java/security/KeyFactory.generatePublic:(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
        //   120: areturn        
        //   121: aload           4
        //   123: athrow         
        //   124: astore_1       
        //   125: new             Ljava/security/InvalidKeyException;
        //   128: dup            
        //   129: ldc_w           "error decoding public key"
        //   132: invokespecial   java/security/InvalidKeyException.<init>:(Ljava/lang/String;)V
        //   135: athrow         
        //   136: astore_1       
        //   137: new             Ljava/security/InvalidKeyException;
        //   140: dup            
        //   141: ldc_w           "error decoding public key"
        //   144: invokespecial   java/security/InvalidKeyException.<init>:(Ljava/lang/String;)V
        //   147: athrow         
        //   148: astore          4
        //   150: goto            71
        //    Exceptions:
        //  throws java.security.NoSuchAlgorithmException
        //  throws java.security.NoSuchProviderException
        //  throws java.security.InvalidKeyException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                        
        //  -----  -----  -----  -----  --------------------------------------------
        //  8      32     136    148    Ljava/security/spec/InvalidKeySpecException;
        //  8      32     124    136    Ljava/io/IOException;
        //  36     51     148    124    Ljava/security/NoSuchAlgorithmException;
        //  36     51     136    148    Ljava/security/spec/InvalidKeySpecException;
        //  36     51     124    136    Ljava/io/IOException;
        //  51     68     148    124    Ljava/security/NoSuchAlgorithmException;
        //  51     68     136    148    Ljava/security/spec/InvalidKeySpecException;
        //  51     68     124    136    Ljava/io/IOException;
        //  71     98     136    148    Ljava/security/spec/InvalidKeySpecException;
        //  71     98     124    136    Ljava/io/IOException;
        //  102    111    136    148    Ljava/security/spec/InvalidKeySpecException;
        //  102    111    124    136    Ljava/io/IOException;
        //  111    121    136    148    Ljava/security/spec/InvalidKeySpecException;
        //  111    121    124    136    Ljava/io/IOException;
        //  121    124    136    148    Ljava/security/spec/InvalidKeySpecException;
        //  121    124    124    136    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean verify() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        return this.verify("SC");
    }
    
    public boolean verify(final String s) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        return this.verify(this.getPublicKey(s), s);
    }
    
    public boolean verify(final PublicKey ex, String s) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Label_0020: {
            if (s != null) {
                break Label_0020;
            }
            while (true) {
                try {
                    s = (String)Signature.getInstance(getSignatureName(this.sigAlgId));
                    // iftrue(Label_0082:, s != null)
                    while (true) {
                        while (true) {
                            while (true) {
                                this.setSignatureParameters((Signature)s, this.sigAlgId.getParameters());
                                ((Signature)s).initVerify((PublicKey)ex);
                                try {
                                    ((Signature)s).update(this.reqInfo.getEncoded("DER"));
                                    return ((Signature)s).verify(this.sigBits.getOctets());
                                }
                                catch (Exception ex) {
                                    s = (String)new StringBuilder();
                                    ((StringBuilder)s).append("exception encoding TBS cert request - ");
                                    ((StringBuilder)s).append(ex);
                                    throw new SignatureException(((StringBuilder)s).toString());
                                }
                                Label_0166: {
                                    throw;
                                }
                                final String s2;
                                Label_0082:
                                s = (String)Signature.getInstance(s2, s);
                                continue;
                                s = (String)Signature.getInstance(s2);
                                continue;
                                s = (String)Signature.getInstance(getSignatureName(this.sigAlgId), s);
                                continue;
                            }
                            final String s2 = PKCS10CertificationRequest.oids.get(this.sigAlgId.getAlgorithm());
                            continue;
                        }
                        continue;
                    }
                }
                // iftrue(Label_0166:, PKCS10CertificationRequest.oids.get((Object)this.sigAlgId.getAlgorithm()) == null)
                catch (NoSuchAlgorithmException ex2) {
                    continue;
                }
                break;
            }
        }
    }
}
