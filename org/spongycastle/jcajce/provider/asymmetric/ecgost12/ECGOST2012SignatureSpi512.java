package org.spongycastle.jcajce.provider.asymmetric.ecgost12;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jce.provider.*;
import java.security.spec.*;
import java.security.*;
import java.math.*;

public class ECGOST2012SignatureSpi512 extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest digest;
    private int halfSize;
    private DSA signer;
    private int size;
    
    public ECGOST2012SignatureSpi512() {
        this.size = 128;
        this.halfSize = 64;
        this.digest = new GOST3411_2012_512Digest();
        this.signer = new ECGOST3410_2012Signer();
    }
    
    static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCECGOST3410_2012PublicKey) {
            return ((BCECGOST3410_2012PublicKey)publicKey).engineGetKeyParameters();
        }
        return ECUtil.generatePublicKeyParameter(publicKey);
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof ECKey)) {
            throw new InvalidKeyException("cannot recognise key type in ECGOST-2012-512 signer");
        }
        final AsymmetricKeyParameter generatePrivateKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        this.digest.reset();
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(generatePrivateKeyParameter, this.appRandom));
            return;
        }
        this.signer.init(true, generatePrivateKeyParameter);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        Label_0031: {
            if (publicKey instanceof ECPublicKey) {
                final AsymmetricKeyParameter asymmetricKeyParameter = generatePublicKeyParameter(publicKey);
                break Label_0031;
            }
            try {
                final AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())));
                this.digest.reset();
                this.signer.init(false, asymmetricKeyParameter);
            }
            catch (Exception ex) {
                throw new InvalidKeyException("cannot recognise key type in ECGOST-2012-512 signer");
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
            final byte[] array2 = new byte[this.size];
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            final byte[] byteArray = generateSignature[0].toByteArray();
            final byte[] byteArray2 = generateSignature[1].toByteArray();
            if (byteArray2[0] != 0) {
                System.arraycopy(byteArray2, 0, array2, this.halfSize - byteArray2.length, byteArray2.length);
            }
            else {
                System.arraycopy(byteArray2, 1, array2, this.halfSize - (byteArray2.length - 1), byteArray2.length - 1);
            }
            if (byteArray[0] != 0) {
                System.arraycopy(byteArray, 0, array2, this.size - byteArray.length, byteArray.length);
                return array2;
            }
            System.arraycopy(byteArray, 1, array2, this.size - (byteArray.length - 1), byteArray.length - 1);
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
            final byte[] array3 = new byte[this.halfSize];
            final byte[] array4 = new byte[this.halfSize];
            System.arraycopy(array, 0, array4, 0, this.halfSize);
            System.arraycopy(array, this.halfSize, array3, 0, this.halfSize);
            final BigInteger[] array5 = { new BigInteger(1, array3), new BigInteger(1, array4) };
            return this.signer.verifySignature(array2, array5[0], array5[1]);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
}
