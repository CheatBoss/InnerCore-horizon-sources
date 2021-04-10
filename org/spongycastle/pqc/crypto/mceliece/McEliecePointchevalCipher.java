package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.crypto.*;

public class McEliecePointchevalCipher implements MessageEncryptor
{
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.2";
    private boolean forEncryption;
    private int k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int n;
    private SecureRandom sr;
    private int t;
    
    private void initCipherDecrypt(final McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.messDigest = Utils.getDigest(mcElieceCCA2PrivateKeyParameters.getDigest());
        this.n = mcElieceCCA2PrivateKeyParameters.getN();
        this.k = mcElieceCCA2PrivateKeyParameters.getK();
        this.t = mcElieceCCA2PrivateKeyParameters.getT();
    }
    
    private void initCipherEncrypt(final McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        SecureRandom sr = this.sr;
        if (sr == null) {
            sr = new SecureRandom();
        }
        this.sr = sr;
        this.messDigest = Utils.getDigest(mcElieceCCA2PublicKeyParameters.getDigest());
        this.n = mcElieceCCA2PublicKeyParameters.getN();
        this.k = mcElieceCCA2PublicKeyParameters.getK();
        this.t = mcElieceCCA2PublicKeyParameters.getT();
    }
    
    protected int decryptOutputSize(final int n) {
        return 0;
    }
    
    protected int encryptOutputSize(final int n) {
        return 0;
    }
    
    public int getKeySize(final McElieceCCA2KeyParameters mcElieceCCA2KeyParameters) throws IllegalArgumentException {
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
    public byte[] messageDecrypt(byte[] array) throws InvalidCipherTextException {
        if (this.forEncryption) {
            throw new IllegalStateException("cipher initialised for decryption");
        }
        final int n = this.n + 7 >> 3;
        final int n2 = array.length - n;
        final byte[][] split = ByteUtils.split(array, n);
        final byte[] array2 = split[0];
        array = split[1];
        final GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, GF2Vector.OS2VP(this.n, array2));
        final byte[] encoded = decryptionPrimitive[0].getEncoded();
        final GF2Vector gf2Vector = decryptionPrimitive[1];
        final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(encoded);
        final byte[] array3 = new byte[n2];
        digestRandomGenerator.nextBytes(array3);
        for (int i = 0; i < n2; ++i) {
            array3[i] ^= array[i];
        }
        this.messDigest.update(array3, 0, n2);
        array = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(array, 0);
        if (Conversions.encode(this.n, this.t, array).equals(gf2Vector)) {
            return ByteUtils.split(array3, n2 - (this.k >> 3))[0];
        }
        throw new InvalidCipherTextException("Bad Padding: Invalid ciphertext.");
    }
    
    @Override
    public byte[] messageEncrypt(final byte[] array) {
        if (this.forEncryption) {
            final int n = this.k >> 3;
            final byte[] array2 = new byte[n];
            this.sr.nextBytes(array2);
            final GF2Vector gf2Vector = new GF2Vector(this.k, this.sr);
            final byte[] encoded = gf2Vector.getEncoded();
            final byte[] concatenate = ByteUtils.concatenate(array, array2);
            final Digest messDigest = this.messDigest;
            final int length = concatenate.length;
            final int n2 = 0;
            messDigest.update(concatenate, 0, length);
            final byte[] array3 = new byte[this.messDigest.getDigestSize()];
            this.messDigest.doFinal(array3, 0);
            final byte[] encoded2 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, gf2Vector, Conversions.encode(this.n, this.t, array3)).getEncoded();
            final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
            digestRandomGenerator.addSeedMaterial(encoded);
            final byte[] array4 = new byte[array.length + n];
            digestRandomGenerator.nextBytes(array4);
            int n3 = 0;
            int i;
            while (true) {
                i = n2;
                if (n3 >= array.length) {
                    break;
                }
                array4[n3] ^= array[n3];
                ++n3;
            }
            while (i < n) {
                final int n4 = array.length + i;
                array4[n4] ^= array2[i];
                ++i;
            }
            return ByteUtils.concatenate(encoded2, array4);
        }
        throw new IllegalStateException("cipher initialised for decryption");
    }
}
