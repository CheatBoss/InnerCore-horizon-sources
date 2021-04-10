package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jce.provider.*;
import java.security.spec.*;
import java.security.*;
import java.math.*;

public class SignatureSpi extends java.security.SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest digest;
    private SecureRandom random;
    private DSA signer;
    
    public SignatureSpi() {
        this.digest = new GOST3411Digest();
        this.signer = new GOST3410Signer();
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (privateKey instanceof ECKey) {
            asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        }
        else {
            asymmetricKeyParameter = GOST3410Util.generatePrivateKeyParameter(privateKey);
        }
        this.digest.reset();
        if (this.random != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameter, this.random));
            return;
        }
        this.signer.init(true, asymmetricKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        Label_0055: {
            if (publicKey instanceof ECPublicKey) {
                final AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKey);
                break Label_0055;
            }
            if (publicKey instanceof GOST3410Key) {
                final AsymmetricKeyParameter asymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(publicKey);
                break Label_0055;
            }
            try {
                publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
                if (publicKey instanceof ECPublicKey) {
                    final AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKey);
                    this.digest.reset();
                    this.signer.init(false, asymmetricKeyParameter);
                    return;
                }
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
            catch (Exception ex) {
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
        }
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
            final byte[] array2 = new byte[64];
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            final byte[] byteArray = generateSignature[0].toByteArray();
            final byte[] byteArray2 = generateSignature[1].toByteArray();
            if (byteArray2[0] != 0) {
                System.arraycopy(byteArray2, 0, array2, 32 - byteArray2.length, byteArray2.length);
            }
            else {
                System.arraycopy(byteArray2, 1, array2, 32 - (byteArray2.length - 1), byteArray2.length - 1);
            }
            if (byteArray[0] != 0) {
                System.arraycopy(byteArray, 0, array2, 64 - byteArray.length, byteArray.length);
                return array2;
            }
            System.arraycopy(byteArray, 1, array2, 64 - (byteArray.length - 1), byteArray.length - 1);
            return array2;
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
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        try {
            final byte[] array3 = new byte[32];
            final byte[] array4 = new byte[32];
            System.arraycopy(array, 0, array4, 0, 32);
            System.arraycopy(array, 32, array3, 0, 32);
            final BigInteger[] array5 = { new BigInteger(1, array3), new BigInteger(1, array4) };
            return this.signer.verifySignature(array2, array5[0], array5[1]);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
}
