package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceCipher implements MessageEncryptor
{
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";
    public int cipherTextSize;
    private boolean forEncryption;
    private int k;
    private McElieceKeyParameters key;
    public int maxPlainTextSize;
    private int n;
    private SecureRandom sr;
    private int t;
    
    private byte[] computeMessage(final GF2Vector gf2Vector) throws InvalidCipherTextException {
        byte[] encoded;
        int n;
        for (encoded = gf2Vector.getEncoded(), n = encoded.length - 1; n >= 0 && encoded[n] == 0; --n) {}
        if (n >= 0 && encoded[n] == 1) {
            final byte[] array = new byte[n];
            System.arraycopy(encoded, 0, array, 0, n);
            return array;
        }
        throw new InvalidCipherTextException("Bad Padding: invalid ciphertext");
    }
    
    private GF2Vector computeMessageRepresentative(final byte[] array) {
        final int maxPlainTextSize = this.maxPlainTextSize;
        int n;
        if ((this.k & 0x7) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        final byte[] array2 = new byte[maxPlainTextSize + n];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array.length] = 1;
        return GF2Vector.OS2VP(this.k, array2);
    }
    
    private void initCipherDecrypt(final McEliecePrivateKeyParameters mcEliecePrivateKeyParameters) {
        this.n = mcEliecePrivateKeyParameters.getN();
        final int k = mcEliecePrivateKeyParameters.getK();
        this.k = k;
        this.maxPlainTextSize = k >> 3;
        this.cipherTextSize = this.n >> 3;
    }
    
    private void initCipherEncrypt(final McEliecePublicKeyParameters mcEliecePublicKeyParameters) {
        SecureRandom sr = this.sr;
        if (sr == null) {
            sr = new SecureRandom();
        }
        this.sr = sr;
        this.n = mcEliecePublicKeyParameters.getN();
        this.k = mcEliecePublicKeyParameters.getK();
        this.t = mcEliecePublicKeyParameters.getT();
        this.cipherTextSize = this.n >> 3;
        this.maxPlainTextSize = this.k >> 3;
    }
    
    public int getKeySize(final McElieceKeyParameters mcElieceKeyParameters) {
        if (mcElieceKeyParameters instanceof McEliecePublicKeyParameters) {
            return ((McEliecePublicKeyParameters)mcElieceKeyParameters).getN();
        }
        if (mcElieceKeyParameters instanceof McEliecePrivateKeyParameters) {
            return ((McEliecePrivateKeyParameters)mcElieceKeyParameters).getN();
        }
        throw new IllegalArgumentException("unsupported type");
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        if (!forEncryption) {
            final McEliecePrivateKeyParameters key = (McEliecePrivateKeyParameters)cipherParameters;
            this.key = key;
            this.initCipherDecrypt(key);
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.sr = parametersWithRandom.getRandom();
            final McEliecePublicKeyParameters key2 = (McEliecePublicKeyParameters)parametersWithRandom.getParameters();
            this.key = key2;
            this.initCipherEncrypt(key2);
            return;
        }
        this.sr = new SecureRandom();
        final McEliecePublicKeyParameters key3 = (McEliecePublicKeyParameters)cipherParameters;
        this.key = key3;
        this.initCipherEncrypt(key3);
    }
    
    @Override
    public byte[] messageDecrypt(final byte[] array) throws InvalidCipherTextException {
        if (!this.forEncryption) {
            final GF2Vector os2VP = GF2Vector.OS2VP(this.n, array);
            final McEliecePrivateKeyParameters mcEliecePrivateKeyParameters = (McEliecePrivateKeyParameters)this.key;
            final GF2mField field = mcEliecePrivateKeyParameters.getField();
            final PolynomialGF2mSmallM goppaPoly = mcEliecePrivateKeyParameters.getGoppaPoly();
            final GF2Matrix sInv = mcEliecePrivateKeyParameters.getSInv();
            final Permutation p = mcEliecePrivateKeyParameters.getP1();
            final Permutation p2 = mcEliecePrivateKeyParameters.getP2();
            final GF2Matrix h = mcEliecePrivateKeyParameters.getH();
            final PolynomialGF2mSmallM[] qInv = mcEliecePrivateKeyParameters.getQInv();
            final Permutation rightMultiply = p.rightMultiply(p2);
            final GF2Vector gf2Vector = (GF2Vector)os2VP.multiply(rightMultiply.computeInverse());
            final GF2Vector syndromeDecode = GoppaCode.syndromeDecode((GF2Vector)h.rightMultiply(gf2Vector), field, goppaPoly, qInv);
            final GF2Vector gf2Vector2 = (GF2Vector)((GF2Vector)gf2Vector.add(syndromeDecode)).multiply(p);
            final GF2Vector gf2Vector3 = (GF2Vector)syndromeDecode.multiply(rightMultiply);
            return this.computeMessage((GF2Vector)sInv.leftMultiply(gf2Vector2.extractRightVector(this.k)));
        }
        throw new IllegalStateException("cipher initialised for decryption");
    }
    
    @Override
    public byte[] messageEncrypt(final byte[] array) {
        if (this.forEncryption) {
            return ((GF2Vector)((McEliecePublicKeyParameters)this.key).getG().leftMultiply(this.computeMessageRepresentative(array)).add(new GF2Vector(this.n, this.t, this.sr))).getEncoded();
        }
        throw new IllegalStateException("cipher initialised for decryption");
    }
}
