package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.generators.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class IESEngine
{
    private byte[] IV;
    byte[] V;
    BasicAgreement agree;
    BufferedBlockCipher cipher;
    boolean forEncryption;
    DerivationFunction kdf;
    private EphemeralKeyPairGenerator keyPairGenerator;
    private KeyParser keyParser;
    Mac mac;
    byte[] macBuf;
    IESParameters param;
    CipherParameters privParam;
    CipherParameters pubParam;
    
    public IESEngine(final BasicAgreement agree, final DerivationFunction kdf, final Mac mac) {
        this.agree = agree;
        this.kdf = kdf;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = null;
    }
    
    public IESEngine(final BasicAgreement agree, final DerivationFunction kdf, final Mac mac, final BufferedBlockCipher cipher) {
        this.agree = agree;
        this.kdf = kdf;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = cipher;
    }
    
    private byte[] decryptBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (n2 < this.V.length + this.mac.getMacSize()) {
            throw new InvalidCipherTextException("Length of input must be greater than the MAC and V combined");
        }
        byte[] array3;
        byte[] array5;
        int processBytes;
        if (this.cipher == null) {
            final int n3 = n2 - this.V.length - this.mac.getMacSize();
            final byte[] array2 = new byte[n3];
            final int n4 = this.param.getMacKeySize() / 8;
            array3 = new byte[n4];
            final int n5 = n3 + n4;
            final byte[] array4 = new byte[n5];
            this.kdf.generateBytes(array4, 0, n5);
            if (this.V.length != 0) {
                System.arraycopy(array4, 0, array3, 0, n4);
                System.arraycopy(array4, n4, array2, 0, n3);
            }
            else {
                System.arraycopy(array4, 0, array2, 0, n3);
                System.arraycopy(array4, n3, array3, 0, n4);
            }
            array5 = new byte[n3];
            for (int i = 0; i != n3; ++i) {
                array5[i] = (byte)(array[this.V.length + n + i] ^ array2[i]);
            }
            processBytes = 0;
        }
        else {
            final int n6 = ((IESWithCipherParameters)this.param).getCipherKeySize() / 8;
            final byte[] array6 = new byte[n6];
            final int n7 = this.param.getMacKeySize() / 8;
            array3 = new byte[n7];
            final int n8 = n6 + n7;
            final byte[] array7 = new byte[n8];
            this.kdf.generateBytes(array7, 0, n8);
            System.arraycopy(array7, 0, array6, 0, n6);
            System.arraycopy(array7, n6, array3, 0, n7);
            CipherParameters cipherParameters;
            final KeyParameter keyParameter = (KeyParameter)(cipherParameters = new KeyParameter(array6));
            if (this.IV != null) {
                cipherParameters = new ParametersWithIV(keyParameter, this.IV);
            }
            this.cipher.init(false, cipherParameters);
            array5 = new byte[this.cipher.getOutputSize(n2 - this.V.length - this.mac.getMacSize())];
            final BufferedBlockCipher cipher = this.cipher;
            final byte[] v = this.V;
            processBytes = cipher.processBytes(array, n + v.length, n2 - v.length - this.mac.getMacSize(), array5, 0);
        }
        final byte[] encodingV = this.param.getEncodingV();
        byte[] lengthTag = null;
        if (this.V.length != 0) {
            lengthTag = this.getLengthTag(encodingV);
        }
        final int n9 = n + n2;
        final byte[] copyOfRange = Arrays.copyOfRange(array, n9 - this.mac.getMacSize(), n9);
        final int length = copyOfRange.length;
        final byte[] array8 = new byte[length];
        this.mac.init(new KeyParameter(array3));
        final Mac mac = this.mac;
        final byte[] v2 = this.V;
        mac.update(array, n + v2.length, n2 - v2.length - length);
        if (encodingV != null) {
            this.mac.update(encodingV, 0, encodingV.length);
        }
        if (this.V.length != 0) {
            this.mac.update(lengthTag, 0, lengthTag.length);
        }
        this.mac.doFinal(array8, 0);
        if (!Arrays.constantTimeAreEqual(copyOfRange, array8)) {
            throw new InvalidCipherTextException("invalid MAC");
        }
        final BufferedBlockCipher cipher2 = this.cipher;
        if (cipher2 == null) {
            return array5;
        }
        return Arrays.copyOfRange(array5, 0, processBytes + cipher2.doFinal(array5, processBytes));
    }
    
    private byte[] encryptBlock(byte[] array, int n, int n2) throws InvalidCipherTextException {
        byte[] array3;
        if (this.cipher == null) {
            final byte[] array2 = new byte[n2];
            final int n3 = this.param.getMacKeySize() / 8;
            array3 = new byte[n3];
            final int n4 = n2 + n3;
            final byte[] array4 = new byte[n4];
            this.kdf.generateBytes(array4, 0, n4);
            if (this.V.length != 0) {
                System.arraycopy(array4, 0, array3, 0, n3);
                System.arraycopy(array4, n3, array2, 0, n2);
            }
            else {
                System.arraycopy(array4, 0, array2, 0, n2);
                System.arraycopy(array4, n2, array3, 0, n3);
            }
            final byte[] array5 = new byte[n2];
            for (int i = 0; i != n2; ++i) {
                array5[i] = (byte)(array[n + i] ^ array2[i]);
            }
            array = array5;
        }
        else {
            final int n5 = ((IESWithCipherParameters)this.param).getCipherKeySize() / 8;
            final byte[] array6 = new byte[n5];
            final int n6 = this.param.getMacKeySize() / 8;
            final byte[] array7 = new byte[n6];
            final int n7 = n5 + n6;
            final byte[] array8 = new byte[n7];
            this.kdf.generateBytes(array8, 0, n7);
            System.arraycopy(array8, 0, array6, 0, n5);
            System.arraycopy(array8, n5, array7, 0, n6);
            BufferedBlockCipher bufferedBlockCipher;
            CipherParameters cipherParameters;
            if (this.IV != null) {
                bufferedBlockCipher = this.cipher;
                cipherParameters = new ParametersWithIV(new KeyParameter(array6), this.IV);
            }
            else {
                bufferedBlockCipher = this.cipher;
                cipherParameters = new KeyParameter(array6);
            }
            bufferedBlockCipher.init(true, cipherParameters);
            final byte[] array9 = new byte[this.cipher.getOutputSize(n2)];
            n = this.cipher.processBytes(array, n, n2, array9, 0);
            n2 = n + this.cipher.doFinal(array9, n);
            array3 = array7;
            array = array9;
        }
        final byte[] encodingV = this.param.getEncodingV();
        byte[] lengthTag = null;
        if (this.V.length != 0) {
            lengthTag = this.getLengthTag(encodingV);
        }
        n = this.mac.getMacSize();
        final byte[] array10 = new byte[n];
        this.mac.init(new KeyParameter(array3));
        this.mac.update(array, 0, array.length);
        if (encodingV != null) {
            this.mac.update(encodingV, 0, encodingV.length);
        }
        if (this.V.length != 0) {
            this.mac.update(lengthTag, 0, lengthTag.length);
        }
        this.mac.doFinal(array10, 0);
        final byte[] v = this.V;
        final byte[] array11 = new byte[v.length + n2 + n];
        System.arraycopy(v, 0, array11, 0, v.length);
        System.arraycopy(array, 0, array11, this.V.length, n2);
        System.arraycopy(array10, 0, array11, this.V.length + n2, n);
        return array11;
    }
    
    private void extractParams(CipherParameters parameters) {
        if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
            this.IV = parametersWithIV.getIV();
            parameters = parametersWithIV.getParameters();
        }
        else {
            this.IV = null;
        }
        this.param = (IESParameters)parameters;
    }
    
    public BufferedBlockCipher getCipher() {
        return this.cipher;
    }
    
    protected byte[] getLengthTag(final byte[] array) {
        final byte[] array2 = new byte[8];
        if (array != null) {
            Pack.longToBigEndian(array.length * 8L, array2, 0);
        }
        return array2;
    }
    
    public Mac getMac() {
        return this.mac;
    }
    
    public void init(final AsymmetricKeyParameter privParam, final CipherParameters cipherParameters, final KeyParser keyParser) {
        this.forEncryption = false;
        this.privParam = privParam;
        this.keyParser = keyParser;
        this.extractParams(cipherParameters);
    }
    
    public void init(final AsymmetricKeyParameter pubParam, final CipherParameters cipherParameters, final EphemeralKeyPairGenerator keyPairGenerator) {
        this.forEncryption = true;
        this.pubParam = pubParam;
        this.keyPairGenerator = keyPairGenerator;
        this.extractParams(cipherParameters);
    }
    
    public void init(final boolean forEncryption, final CipherParameters privParam, final CipherParameters pubParam, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        this.privParam = privParam;
        this.pubParam = pubParam;
        this.V = new byte[0];
        this.extractParams(cipherParameters);
    }
    
    public byte[] processBlock(byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        Label_0190: {
            byte[] v = null;
            Label_0044: {
                if (this.forEncryption) {
                    final EphemeralKeyPairGenerator keyPairGenerator = this.keyPairGenerator;
                    if (keyPairGenerator == null) {
                        break Label_0190;
                    }
                    final EphemeralKeyPair generate = keyPairGenerator.generate();
                    this.privParam = generate.getKeyPair().getPrivate();
                    v = generate.getEncodedPublicKey();
                }
                else {
                    if (this.keyParser != null) {
                        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array, n, n2);
                        try {
                            this.pubParam = this.keyParser.readKey(byteArrayInputStream);
                            v = Arrays.copyOfRange(array, n, n2 - byteArrayInputStream.available() + n);
                            break Label_0044;
                        }
                        catch (IllegalArgumentException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unable to recover ephemeral public key: ");
                            sb.append(ex.getMessage());
                            throw new InvalidCipherTextException(sb.toString(), ex);
                        }
                        catch (IOException ex2) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("unable to recover ephemeral public key: ");
                            sb2.append(ex2.getMessage());
                            throw new InvalidCipherTextException(sb2.toString(), ex2);
                        }
                    }
                    break Label_0190;
                }
            }
            this.V = v;
        }
        this.agree.init(this.privParam);
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(this.agree.getFieldSize(), this.agree.calculateAgreement(this.pubParam));
        final byte[] v2 = this.V;
        byte[] concatenate = unsignedByteArray;
        if (v2.length != 0) {
            concatenate = Arrays.concatenate(v2, unsignedByteArray);
            Arrays.fill(unsignedByteArray, (byte)0);
        }
        try {
            this.kdf.init(new KDFParameters(concatenate, this.param.getDerivationV()));
            if (this.forEncryption) {
                array = this.encryptBlock(array, n, n2);
            }
            else {
                array = this.decryptBlock(array, n, n2);
            }
            return array;
        }
        finally {
            Arrays.fill(concatenate, (byte)0);
        }
    }
}
