package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.crypto.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.crypto.*;

public class McElieceFujisakiCipher implements MessageEncryptor
{
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.1";
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
    public byte[] messageDecrypt(byte[] concatenate) throws InvalidCipherTextException {
        if (this.forEncryption) {
            throw new IllegalStateException("cipher initialised for decryption");
        }
        final int n = this.n + 7 >> 3;
        final int n2 = concatenate.length - n;
        final byte[][] split = ByteUtils.split(concatenate, n);
        final byte[] array = split[0];
        concatenate = split[1];
        final GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, GF2Vector.OS2VP(this.n, array));
        final byte[] encoded = decryptionPrimitive[0].getEncoded();
        final GF2Vector gf2Vector = decryptionPrimitive[1];
        final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(encoded);
        final byte[] array2 = new byte[n2];
        digestRandomGenerator.nextBytes(array2);
        for (int i = 0; i < n2; ++i) {
            array2[i] ^= concatenate[i];
        }
        concatenate = ByteUtils.concatenate(encoded, array2);
        final byte[] array3 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(concatenate, 0, concatenate.length);
        this.messDigest.doFinal(array3, 0);
        if (Conversions.encode(this.n, this.t, array3).equals(gf2Vector)) {
            return array2;
        }
        throw new InvalidCipherTextException("Bad Padding: invalid ciphertext");
    }
    
    @Override
    public byte[] messageEncrypt(final byte[] array) {
        if (this.forEncryption) {
            final GF2Vector gf2Vector = new GF2Vector(this.k, this.sr);
            final byte[] encoded = gf2Vector.getEncoded();
            final byte[] concatenate = ByteUtils.concatenate(encoded, array);
            final Digest messDigest = this.messDigest;
            final int length = concatenate.length;
            int i = 0;
            messDigest.update(concatenate, 0, length);
            final byte[] array2 = new byte[this.messDigest.getDigestSize()];
            this.messDigest.doFinal(array2, 0);
            final byte[] encoded2 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, gf2Vector, Conversions.encode(this.n, this.t, array2)).getEncoded();
            final DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
            digestRandomGenerator.addSeedMaterial(encoded);
            final byte[] array3 = new byte[array.length];
            digestRandomGenerator.nextBytes(array3);
            while (i < array.length) {
                array3[i] ^= array[i];
                ++i;
            }
            return ByteUtils.concatenate(encoded2, array3);
        }
        throw new IllegalStateException("cipher initialised for decryption");
    }
}
