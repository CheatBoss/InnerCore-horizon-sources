package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.pqc.jcajce.provider.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import java.security.spec.*;
import org.spongycastle.crypto.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.*;

public class McEliecePKCSCipherSpi extends AsymmetricBlockCipher implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private McElieceCipher cipher;
    
    public McEliecePKCSCipherSpi(final McElieceCipher cipher) {
        this.cipher = cipher;
    }
    
    @Override
    public int getKeySize(final Key key) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (key instanceof PublicKey) {
            asymmetricKeyParameter = McElieceKeysToParams.generatePublicKeyParameter((PublicKey)key);
        }
        else {
            asymmetricKeyParameter = McElieceKeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        }
        return this.cipher.getKeySize((McElieceKeyParameters)asymmetricKeyParameter);
    }
    
    @Override
    public String getName() {
        return "McEliecePKCS";
    }
    
    @Override
    protected void initCipherDecrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.cipher.init(false, McElieceKeysToParams.generatePrivateKeyParameter((PrivateKey)key));
        this.maxPlainTextSize = this.cipher.maxPlainTextSize;
        this.cipherTextSize = this.cipher.cipherTextSize;
    }
    
    @Override
    protected void initCipherEncrypt(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.cipher.init(true, new ParametersWithRandom(McElieceKeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom));
        this.maxPlainTextSize = this.cipher.maxPlainTextSize;
        this.cipherTextSize = this.cipher.cipherTextSize;
    }
    
    @Override
    protected byte[] messageDecrypt(byte[] messageDecrypt) throws IllegalBlockSizeException, BadPaddingException {
        try {
            messageDecrypt = this.cipher.messageDecrypt(messageDecrypt);
            return messageDecrypt;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    protected byte[] messageEncrypt(byte[] messageEncrypt) throws IllegalBlockSizeException, BadPaddingException {
        try {
            messageEncrypt = this.cipher.messageEncrypt(messageEncrypt);
            return messageEncrypt;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static class McEliecePKCS extends McEliecePKCSCipherSpi
    {
        public McEliecePKCS() {
            super(new McElieceCipher());
        }
    }
}
