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

public class McElieceFujisakiCipherSpi extends AsymmetricHybridCipher implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private ByteArrayOutputStream buf;
    private McElieceFujisakiCipher cipher;
    private Digest digest;
    
    protected McElieceFujisakiCipherSpi(final Digest digest, final McElieceFujisakiCipher cipher) {
        this.digest = digest;
        this.cipher = cipher;
        this.buf = new ByteArrayOutputStream();
    }
    
    @Override
    protected int decryptOutputSize(final int n) {
        return 0;
    }
    
    @Override
    public byte[] doFinal(byte[] array, final int n, final int n2) throws BadPaddingException {
        this.update(array, n, n2);
        array = this.buf.toByteArray();
        this.buf.reset();
        if (this.opMode == 1) {
            return this.cipher.messageEncrypt(array);
        }
        if (this.opMode == 2) {
            try {
                array = this.cipher.messageDecrypt(array);
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
            asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        }
        return this.cipher.getKeySize((McElieceCCA2KeyParameters)asymmetricKeyParameter);
    }
    
    @Override
    public String getName() {
        return "McElieceFujisakiCipher";
    }
    
    @Override
    protected void initCipherDecrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        final AsymmetricKeyParameter generatePrivateKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, generatePrivateKeyParameter);
    }
    
    @Override
    protected void initCipherEncrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        final ParametersWithRandom parametersWithRandom = new ParametersWithRandom(McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom);
        this.digest.reset();
        this.cipher.init(true, parametersWithRandom);
    }
    
    @Override
    public byte[] update(final byte[] array, final int n, final int n2) {
        this.buf.write(array, n, n2);
        return new byte[0];
    }
    
    public static class McElieceFujisaki extends McElieceFujisakiCipherSpi
    {
        public McElieceFujisaki() {
            super(DigestFactory.createSHA1(), new McElieceFujisakiCipher());
        }
    }
}
