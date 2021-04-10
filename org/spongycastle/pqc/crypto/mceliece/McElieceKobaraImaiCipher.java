package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceKobaraImaiCipher implements MessageEncryptor
{
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.3";
    public static final byte[] PUBLIC_CONSTANT;
    private boolean forEncryption;
    private int k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int n;
    private SecureRandom sr;
    private int t;
    
    static {
        PUBLIC_CONSTANT = "a predetermined public constant".getBytes();
    }
    
    private void initCipherDecrypt(final McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.messDigest = Utils.getDigest(mcElieceCCA2PrivateKeyParameters.getDigest());
        this.n = mcElieceCCA2PrivateKeyParameters.getN();
        this.k = mcElieceCCA2PrivateKeyParameters.getK();
        this.t = mcElieceCCA2PrivateKeyParameters.getT();
    }
    
    private void initCipherEncrypt(final McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this.messDigest = Utils.getDigest(mcElieceCCA2PublicKeyParameters.getDigest());
        this.n = mcElieceCCA2PublicKeyParameters.getN();
        this.k = mcElieceCCA2PublicKeyParameters.getK();
        this.t = mcElieceCCA2PublicKeyParameters.getT();
    }
    
    public int getKeySize(final McElieceCCA2KeyParameters mcElieceCCA2KeyParameters) {
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PublicKeyParameters) {
            return ((McElieceCCA2PublicKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PrivateKeyParameters) {
            return ((McElieceCCA2PrivateKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        throw new IllegalArgumentException("unsupported type");
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        if (!forEncryption) {
            final McElieceCCA2PrivateKeyParameters key = (McElieceCCA2PrivateKeyParameters)cipherParameters;
            this.key = key;
            this.initCipherDecrypt(key);
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.sr = parametersWithRandom.getRandom();
            final McElieceCCA2PublicKeyParameters key2 = (McElieceCCA2PublicKeyParameters)parametersWithRandom.getParameters();
            this.key = key2;
            this.initCipherEncrypt(key2);
            return;
        }
        this.sr = new SecureRandom();
        final McElieceCCA2PublicKeyParameters key3 = (McElieceCCA2PublicKeyParameters)cipherParameters;
        this.key = key3;
        this.initCipherEncrypt(key3);
    }
    
    @Override
    public byte[] messageDecrypt(byte[] concatenate) throws InvalidCipherTextException {
        if (this.forEncryption) {
            throw new IllegalStateException("cipher initialised for decryption");
        }
        final int n = this.n >> 3;
        if (concatenate.length < n) {
            throw new InvalidCipherTextException("Bad Padding: Ciphertext too short.");
        }
        final int digestSize = this.messDigest.getDigestSize();
        final int n2 = this.k >> 3;
        final int n3 = concatenate.length - n;
        byte[] array;
        if (n3 > 0) {
            final byte[][] split = ByteUtils.split(concatenate, n3);
            concatenate = split[0];
            array = split[1];
        }
        else {
            final byte[] array2 = new byte[0];
            array = concatenate;
            concatenate = array2;
        }
        final GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, GF2Vector.OS2VP(this.n, array));
        final byte[] encoded = decryptionPrimitive[0].getEncoded();
        final GF2Vector gf2Vector = decryptionPrimitive[1];
        byte[] subArray = encoded;
        if (encoded.length > n2) {
            subArray = ByteUtils.subArray(encoded, 0, n2);
        }
        concatenate = ByteUtils.concatenate(ByteUtils.concatenate(concatenate, Conversions.decode(this.n, this.t, gf2Vector)), subArray);
        final int n4 = concatenate.length - digestSize;
        final byte[][] split2 = ByteUtils.split(concatenate, digestSize);
        final byte[] array3 = split2[0];
        concatenate = split2[1];
        final byte[] array4 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(concatenate, 0, concatenate.length);
        this.messDigest.doFinal(array4, 0);
        for (int i = digestSize - 1; i >= 0; --i) {
            array4[i] ^= array3[i];
        }
        final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(array4);
        final byte[] array5 = new byte[n4];
        digestRandomGenerator.nextBytes(array5);
        for (int j = n4 - 1; j >= 0; --j) {
            array5[j] ^= concatenate[j];
        }
        final byte[][] split3 = ByteUtils.split(array5, n4 - McElieceKobaraImaiCipher.PUBLIC_CONSTANT.length);
        final byte[] array6 = split3[0];
        if (ByteUtils.equals(split3[1], McElieceKobaraImaiCipher.PUBLIC_CONSTANT)) {
            return array6;
        }
        throw new InvalidCipherTextException("Bad Padding: invalid ciphertext");
    }
    
    @Override
    public byte[] messageEncrypt(byte[] public_CONSTANT) {
        if (this.forEncryption) {
            final int digestSize = this.messDigest.getDigestSize();
            final int n = this.k >> 3;
            final int n2 = IntegerFunctions.binomial(this.n, this.t).bitLength() - 1 >> 3;
            int length;
            if (public_CONSTANT.length > (length = n + n2 - digestSize - McElieceKobaraImaiCipher.PUBLIC_CONSTANT.length)) {
                length = public_CONSTANT.length;
            }
            final int n3 = McElieceKobaraImaiCipher.PUBLIC_CONSTANT.length + length;
            final int n4 = n3 + digestSize - n - n2;
            final byte[] array = new byte[n3];
            System.arraycopy(public_CONSTANT, 0, array, 0, public_CONSTANT.length);
            public_CONSTANT = McElieceKobaraImaiCipher.PUBLIC_CONSTANT;
            System.arraycopy(public_CONSTANT, 0, array, length, public_CONSTANT.length);
            public_CONSTANT = new byte[digestSize];
            this.sr.nextBytes(public_CONSTANT);
            final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
            digestRandomGenerator.addSeedMaterial(public_CONSTANT);
            final byte[] array2 = new byte[n3];
            digestRandomGenerator.nextBytes(array2);
            for (int i = n3 - 1; i >= 0; --i) {
                array2[i] ^= array[i];
            }
            final byte[] array3 = new byte[this.messDigest.getDigestSize()];
            this.messDigest.update(array2, 0, n3);
            this.messDigest.doFinal(array3, 0);
            int n5 = digestSize;
            while (true) {
                --n5;
                if (n5 < 0) {
                    break;
                }
                array3[n5] ^= public_CONSTANT[n5];
            }
            final byte[] concatenate = ByteUtils.concatenate(array3, array2);
            public_CONSTANT = new byte[0];
            if (n4 > 0) {
                public_CONSTANT = new byte[n4];
                System.arraycopy(concatenate, 0, public_CONSTANT, 0, n4);
            }
            final byte[] array4 = new byte[n2];
            System.arraycopy(concatenate, n4, array4, 0, n2);
            final byte[] array5 = new byte[n];
            System.arraycopy(concatenate, n2 + n4, array5, 0, n);
            byte[] array6 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, GF2Vector.OS2VP(this.k, array5), Conversions.encode(this.n, this.t, array4)).getEncoded();
            if (n4 > 0) {
                array6 = ByteUtils.concatenate(public_CONSTANT, array6);
            }
            return array6;
        }
        throw new IllegalStateException("cipher initialised for decryption");
    }
}
