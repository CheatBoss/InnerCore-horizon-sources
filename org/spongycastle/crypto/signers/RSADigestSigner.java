package org.spongycastle.crypto.signers;

import java.util.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.encodings.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class RSADigestSigner implements Signer
{
    private static final Hashtable oidMap;
    private final AlgorithmIdentifier algId;
    private final Digest digest;
    private boolean forSigning;
    private final AsymmetricBlockCipher rsaEngine;
    
    static {
        (oidMap = new Hashtable()).put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
        RSADigestSigner.oidMap.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
        RSADigestSigner.oidMap.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
        RSADigestSigner.oidMap.put("SHA-1", X509ObjectIdentifiers.id_SHA1);
        RSADigestSigner.oidMap.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        RSADigestSigner.oidMap.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        RSADigestSigner.oidMap.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        RSADigestSigner.oidMap.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        RSADigestSigner.oidMap.put("SHA-512/224", NISTObjectIdentifiers.id_sha512_224);
        RSADigestSigner.oidMap.put("SHA-512/256", NISTObjectIdentifiers.id_sha512_256);
        RSADigestSigner.oidMap.put("SHA3-224", NISTObjectIdentifiers.id_sha3_224);
        RSADigestSigner.oidMap.put("SHA3-256", NISTObjectIdentifiers.id_sha3_256);
        RSADigestSigner.oidMap.put("SHA3-384", NISTObjectIdentifiers.id_sha3_384);
        RSADigestSigner.oidMap.put("SHA3-512", NISTObjectIdentifiers.id_sha3_512);
        RSADigestSigner.oidMap.put("MD2", PKCSObjectIdentifiers.md2);
        RSADigestSigner.oidMap.put("MD4", PKCSObjectIdentifiers.md4);
        RSADigestSigner.oidMap.put("MD5", PKCSObjectIdentifiers.md5);
    }
    
    public RSADigestSigner(final Digest digest) {
        this(digest, RSADigestSigner.oidMap.get(digest.getAlgorithmName()));
    }
    
    public RSADigestSigner(final Digest digest, final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        this.rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
        this.digest = digest;
        this.algId = new AlgorithmIdentifier(asn1ObjectIdentifier, DERNull.INSTANCE);
    }
    
    private byte[] derEncode(final byte[] array) throws IOException {
        return new DigestInfo(this.algId, array).getEncoded("DER");
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (this.forSigning) {
            final byte[] array = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(array, 0);
            try {
                final byte[] derEncode = this.derEncode(array);
                return this.rsaEngine.processBlock(derEncode, 0, derEncode.length);
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unable to encode signature: ");
                sb.append(ex.getMessage());
                throw new CryptoException(sb.toString(), ex);
            }
        }
        throw new IllegalStateException("RSADigestSigner not initialised for signature generation.");
    }
    
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.digest.getAlgorithmName());
        sb.append("withRSA");
        return sb.toString();
    }
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            asymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        if (forSigning && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        }
        if (!forSigning && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        }
        this.reset();
        this.rsaEngine.init(forSigning, cipherParameters);
    }
    
    @Override
    public void reset() {
        this.digest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(byte[] processBlock) {
        if (!this.forSigning) {
            final int digestSize = this.digest.getDigestSize();
            final byte[] array = new byte[digestSize];
            final Digest digest = this.digest;
            boolean b = false;
            digest.doFinal(array, 0);
            try {
                processBlock = this.rsaEngine.processBlock(processBlock, 0, processBlock.length);
                final byte[] derEncode = this.derEncode(array);
                if (processBlock.length == derEncode.length) {
                    return Arrays.constantTimeAreEqual(processBlock, derEncode);
                }
                if (processBlock.length == derEncode.length - 2) {
                    final int n = processBlock.length - digestSize - 2;
                    final int length = derEncode.length;
                    derEncode[1] -= 2;
                    derEncode[3] -= 2;
                    int i = 0;
                    int n2 = 0;
                    while (i < digestSize) {
                        n2 |= (processBlock[n + i] ^ derEncode[length - digestSize - 2 + i]);
                        ++i;
                    }
                    final int n3 = 0;
                    int n4 = n2;
                    for (int j = n3; j < n; ++j) {
                        n4 |= (processBlock[j] ^ derEncode[j]);
                    }
                    if (n4 == 0) {
                        b = true;
                    }
                    return b;
                }
                Arrays.constantTimeAreEqual(derEncode, derEncode);
                return false;
            }
            catch (Exception ex) {
                return false;
            }
        }
        throw new IllegalStateException("RSADigestSigner not initialised for verification");
    }
}
