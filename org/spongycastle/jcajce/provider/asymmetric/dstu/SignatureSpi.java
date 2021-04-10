package org.spongycastle.jcajce.provider.asymmetric.dstu;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import java.security.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class SignatureSpi extends java.security.SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private static byte[] DEFAULT_SBOX;
    private Digest digest;
    private DSA signer;
    
    static {
        SignatureSpi.DEFAULT_SBOX = new byte[] { 10, 9, 13, 6, 14, 11, 4, 5, 15, 1, 3, 12, 7, 0, 8, 2, 8, 0, 12, 4, 9, 6, 7, 11, 2, 3, 1, 15, 5, 14, 10, 13, 15, 6, 5, 8, 14, 11, 10, 4, 12, 0, 3, 7, 2, 9, 1, 13, 3, 8, 13, 9, 6, 11, 15, 0, 2, 5, 12, 10, 4, 14, 1, 7, 15, 8, 14, 9, 7, 2, 0, 13, 12, 6, 1, 5, 11, 4, 3, 10, 2, 8, 9, 7, 5, 15, 0, 11, 12, 1, 13, 14, 10, 3, 6, 4, 3, 8, 11, 5, 6, 4, 14, 10, 2, 12, 1, 7, 9, 15, 13, 0, 1, 2, 3, 14, 6, 13, 11, 8, 15, 10, 12, 5, 7, 9, 0, 4 };
    }
    
    public SignatureSpi() {
        this.signer = new DSTU4145Signer();
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        AsymmetricKeyParameter generatePrivateKeyParameter;
        if (privateKey instanceof ECKey) {
            generatePrivateKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        }
        else {
            generatePrivateKeyParameter = null;
        }
        this.digest = new GOST3411Digest(SignatureSpi.DEFAULT_SBOX);
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(generatePrivateKeyParameter, this.appRandom));
            return;
        }
        this.signer.init(true, generatePrivateKeyParameter);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (publicKey instanceof BCDSTU4145PublicKey) {
            asymmetricKeyParameter = ((BCDSTU4145PublicKey)publicKey).engineGetKeyParameters();
        }
        else {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKey);
        }
        this.digest = new GOST3411Digest(this.expandSbox(((BCDSTU4145PublicKey)publicKey).getSbox()));
        this.signer.init(false, asymmetricKeyParameter);
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
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            final byte[] byteArray = generateSignature[0].toByteArray();
            final byte[] byteArray2 = generateSignature[1].toByteArray();
            int n;
            if (byteArray.length > byteArray2.length) {
                n = byteArray.length;
            }
            else {
                n = byteArray2.length;
            }
            final int n2 = n * 2;
            final byte[] array2 = new byte[n2];
            System.arraycopy(byteArray2, 0, array2, n2 / 2 - byteArray2.length, byteArray2.length);
            System.arraycopy(byteArray, 0, array2, n2 - byteArray.length, byteArray.length);
            return new DEROctetString(array2).getEncoded();
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
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
    protected boolean engineVerify(byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        try {
            final byte[] octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(array)).getOctets();
            array = new byte[octets.length / 2];
            final byte[] array3 = new byte[octets.length / 2];
            System.arraycopy(octets, 0, array3, 0, octets.length / 2);
            System.arraycopy(octets, octets.length / 2, array, 0, octets.length / 2);
            final BigInteger[] array4 = { new BigInteger(1, array), new BigInteger(1, array3) };
            return this.signer.verifySignature(array2, array4[0], array4[1]);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
    
    byte[] expandSbox(final byte[] array) {
        final byte[] array2 = new byte[128];
        for (int i = 0; i < array.length; ++i) {
            final int n = i * 2;
            array2[n] = (byte)(array[i] >> 4 & 0xF);
            array2[n + 1] = (byte)(array[i] & 0xF);
        }
        return array2;
    }
}
