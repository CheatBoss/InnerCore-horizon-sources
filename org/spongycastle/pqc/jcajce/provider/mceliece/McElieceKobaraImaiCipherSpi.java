package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.pqc.jcajce.provider.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import java.io.*;
import javax.crypto.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import java.security.spec.*;
import org.spongycastle.crypto.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.util.*;

public class McElieceKobaraImaiCipherSpi extends AsymmetricHybridCipher implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private ByteArrayOutputStream buf;
    private McElieceKobaraImaiCipher cipher;
    private Digest digest;
    
    public McElieceKobaraImaiCipherSpi() {
        this.buf = new ByteArrayOutputStream();
        this.buf = new ByteArrayOutputStream();
    }
    
    protected McElieceKobaraImaiCipherSpi(final Digest digest, final McElieceKobaraImaiCipher cipher) {
        this.buf = new ByteArrayOutputStream();
        this.digest = digest;
        this.cipher = cipher;
        this.buf = new ByteArrayOutputStream();
    }
    
    private byte[] pad() {
        this.buf.write(1);
        final byte[] byteArray = this.buf.toByteArray();
        this.buf.reset();
        return byteArray;
    }
    
    private byte[] unpad(final byte[] array) throws BadPaddingException {
        int n;
        for (n = array.length - 1; n >= 0 && array[n] == 0; --n) {}
        if (array[n] == 1) {
            final byte[] array2 = new byte[n];
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        throw new BadPaddingException("invalid ciphertext");
    }
    
    @Override
    protected int decryptOutputSize(final int n) {
        return 0;
    }
    
    @Override
    public byte[] doFinal(byte[] array, final int n, final int n2) throws BadPaddingException {
        this.update(array, n, n2);
        if (this.opMode == 1) {
            return this.cipher.messageEncrypt(this.pad());
        }
        if (this.opMode == 2) {
            try {
                array = this.buf.toByteArray();
                this.buf.reset();
                array = this.unpad(this.cipher.messageDecrypt(array));
                return array;
            }
            catch (InvalidCipherTextException ex) {
                throw new BadPaddingException(ex.getMessage());
            }
        }
        throw new IllegalStateException("unknown mode in doFinal");
    }
    
    @Override
    protected int encryptOutputSize(final int n) {
        return 0;
    }
    
    @Override
    public int getKeySize(final Key key) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (key instanceof PublicKey) {
            asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
        }
        else {
            if (!(key instanceof PrivateKey)) {
                throw new InvalidKeyException();
            }
            asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        }
        return this.cipher.getKeySize((McElieceCCA2KeyParameters)asymmetricKeyParameter);
    }
    
    @Override
    public String getName() {
        return "McElieceKobaraImaiCipher";
    }
    
    @Override
    protected void initCipherDecrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.buf.reset();
        final AsymmetricKeyParameter generatePrivateKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, generatePrivateKeyParameter);
    }
    
    @Override
    protected void initCipherEncrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.buf.reset();
        final ParametersWithRandom parametersWithRandom = new ParametersWithRandom(McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom);
        this.digest.reset();
        this.cipher.init(true, parametersWithRandom);
    }
    
    @Override
    public byte[] update(final byte[] array, final int n, final int n2) {
        this.buf.write(array, n, n2);
        return new byte[0];
    }
    
    public static class McElieceKobaraImai extends McElieceKobaraImaiCipherSpi
    {
        public McElieceKobaraImai() {
            super(DigestFactory.createSHA1(), new McElieceKobaraImaiCipher());
        }
    }
    
    public static class McElieceKobaraImai224 extends McElieceKobaraImaiCipherSpi
    {
        public McElieceKobaraImai224() {
            super(DigestFactory.createSHA224(), new McElieceKobaraImaiCipher());
        }
    }
    
    public static class McElieceKobaraImai256 extends McElieceKobaraImaiCipherSpi
    {
        public McElieceKobaraImai256() {
            super(DigestFactory.createSHA256(), new McElieceKobaraImaiCipher());
        }
    }
    
    public static class McElieceKobaraImai384 extends McElieceKobaraImaiCipherSpi
    {
        public McElieceKobaraImai384() {
            super(DigestFactory.createSHA384(), new McElieceKobaraImaiCipher());
        }
    }
    
    public static class McElieceKobaraImai512 extends McElieceKobaraImaiCipherSpi
    {
        public McElieceKobaraImai512() {
            super(DigestFactory.createSHA512(), new McElieceKobaraImaiCipher());
        }
    }
}
