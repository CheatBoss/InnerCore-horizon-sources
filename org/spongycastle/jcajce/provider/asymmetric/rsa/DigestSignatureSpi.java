package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.encodings.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.digests.*;

public class DigestSignatureSpi extends SignatureSpi
{
    private AlgorithmIdentifier algId;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    
    protected DigestSignatureSpi(final ASN1ObjectIdentifier asn1ObjectIdentifier, final Digest digest, final AsymmetricBlockCipher cipher) {
        this.digest = digest;
        this.cipher = cipher;
        this.algId = new AlgorithmIdentifier(asn1ObjectIdentifier, DERNull.INSTANCE);
    }
    
    protected DigestSignatureSpi(final Digest digest, final AsymmetricBlockCipher cipher) {
        this.digest = digest;
        this.cipher = cipher;
        this.algId = null;
    }
    
    private byte[] derEncode(final byte[] array) throws IOException {
        if (this.algId == null) {
            return array;
        }
        return new DigestInfo(this.algId, array).getEncoded("DER");
    }
    
    private String getType(final Object o) {
        if (o == null) {
            return null;
        }
        return o.getClass().getName();
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        return null;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateKey) {
            final RSAKeyParameters generatePrivateKeyParameter = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey);
            this.digest.reset();
            this.cipher.init(true, generatePrivateKeyParameter);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Supplied key (");
        sb.append(this.getType(privateKey));
        sb.append(") is not a RSAPrivateKey instance");
        throw new InvalidKeyException(sb.toString());
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof RSAPublicKey) {
            final RSAKeyParameters generatePublicKeyParameter = RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey);
            this.digest.reset();
            this.cipher.init(false, generatePublicKeyParameter);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Supplied key (");
        sb.append(this.getType(publicKey));
        sb.append(") is not a RSAPublicKey instance");
        throw new InvalidKeyException(sb.toString());
    }
    
    @Override
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        try {
            final byte[] derEncode = this.derEncode(array);
            return this.cipher.processBlock(derEncode, 0, derEncode.length);
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
        catch (ArrayIndexOutOfBoundsException ex2) {
            throw new SignatureException("key too small for signature type");
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.digest.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.digest.update(array, n, n2);
    }
    
    @Override
    protected boolean engineVerify(byte[] processBlock) throws SignatureException {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        try {
            processBlock = this.cipher.processBlock(processBlock, 0, processBlock.length);
            final byte[] derEncode = this.derEncode(array);
            if (processBlock.length == derEncode.length) {
                return Arrays.constantTimeAreEqual(processBlock, derEncode);
            }
            if (processBlock.length == derEncode.length - 2) {
                derEncode[1] -= 2;
                derEncode[3] -= 2;
                final int n = derEncode[3] + 4;
                final int n2 = n + 2;
                int i = 0;
                int n3 = 0;
                while (i < derEncode.length - n2) {
                    n3 |= (processBlock[n + i] ^ derEncode[n2 + i]);
                    ++i;
                }
                final int n4 = 0;
                int n5 = n3;
                for (int j = n4; j < n; ++j) {
                    n5 |= (processBlock[j] ^ derEncode[j]);
                }
                return n5 == 0;
            }
            Arrays.constantTimeAreEqual(derEncode, derEncode);
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static class MD2 extends DigestSignatureSpi
    {
        public MD2() {
            super(PKCSObjectIdentifiers.md2, new MD2Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class MD4 extends DigestSignatureSpi
    {
        public MD4() {
            super(PKCSObjectIdentifiers.md4, new MD4Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class MD5 extends DigestSignatureSpi
    {
        public MD5() {
            super(PKCSObjectIdentifiers.md5, DigestFactory.createMD5(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD128 extends DigestSignatureSpi
    {
        public RIPEMD128() {
            super(TeleTrusTObjectIdentifiers.ripemd128, new RIPEMD128Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD160 extends DigestSignatureSpi
    {
        public RIPEMD160() {
            super(TeleTrusTObjectIdentifiers.ripemd160, new RIPEMD160Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD256 extends DigestSignatureSpi
    {
        public RIPEMD256() {
            super(TeleTrusTObjectIdentifiers.ripemd256, new RIPEMD256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA1 extends DigestSignatureSpi
    {
        public SHA1() {
            super(OIWObjectIdentifiers.idSHA1, DigestFactory.createSHA1(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA224 extends DigestSignatureSpi
    {
        public SHA224() {
            super(NISTObjectIdentifiers.id_sha224, DigestFactory.createSHA224(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA256 extends DigestSignatureSpi
    {
        public SHA256() {
            super(NISTObjectIdentifiers.id_sha256, DigestFactory.createSHA256(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA384 extends DigestSignatureSpi
    {
        public SHA384() {
            super(NISTObjectIdentifiers.id_sha384, DigestFactory.createSHA384(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA3_224 extends DigestSignatureSpi
    {
        public SHA3_224() {
            super(NISTObjectIdentifiers.id_sha3_224, DigestFactory.createSHA3_224(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA3_256 extends DigestSignatureSpi
    {
        public SHA3_256() {
            super(NISTObjectIdentifiers.id_sha3_256, DigestFactory.createSHA3_256(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA3_384 extends DigestSignatureSpi
    {
        public SHA3_384() {
            super(NISTObjectIdentifiers.id_sha3_384, DigestFactory.createSHA3_384(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA3_512 extends DigestSignatureSpi
    {
        public SHA3_512() {
            super(NISTObjectIdentifiers.id_sha3_512, DigestFactory.createSHA3_512(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA512 extends DigestSignatureSpi
    {
        public SHA512() {
            super(NISTObjectIdentifiers.id_sha512, DigestFactory.createSHA512(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA512_224 extends DigestSignatureSpi
    {
        public SHA512_224() {
            super(NISTObjectIdentifiers.id_sha512_224, DigestFactory.createSHA512_224(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA512_256 extends DigestSignatureSpi
    {
        public SHA512_256() {
            super(NISTObjectIdentifiers.id_sha512_256, DigestFactory.createSHA512_256(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class noneRSA extends DigestSignatureSpi
    {
        public noneRSA() {
            super(new NullDigest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
}
