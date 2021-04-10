package com.googleplay.licensing;

import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import java.io.*;
import javax.crypto.*;
import com.googleplay.util.*;

public class AESObfuscator implements Obfuscator
{
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] IV;
    private static final String KEYGEN_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
    private static final String UTF8 = "UTF-8";
    private static final String header = "com.android.vending.licensing.AESObfuscator-1|";
    private Cipher mDecryptor;
    private Cipher mEncryptor;
    
    static {
        IV = new byte[] { 16, 74, 71, -80, 32, 101, -47, 72, 117, -14, 0, -29, 70, 65, -12, 74 };
    }
    
    public AESObfuscator(final byte[] array, final String s, final String s2) {
        try {
            final SecretKeyFactory instance = SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC");
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(s2);
            final SecretKeySpec secretKeySpec = new SecretKeySpec(instance.generateSecret(new PBEKeySpec(sb.toString().toCharArray(), array, 1024, 256)).getEncoded(), "AES");
            (this.mEncryptor = Cipher.getInstance("AES/CBC/PKCS5Padding")).init(1, secretKeySpec, new IvParameterSpec(AESObfuscator.IV));
            (this.mDecryptor = Cipher.getInstance("AES/CBC/PKCS5Padding")).init(2, secretKeySpec, new IvParameterSpec(AESObfuscator.IV));
        }
        catch (GeneralSecurityException ex) {
            throw new RuntimeException("Invalid environment", ex);
        }
    }
    
    @Override
    public String obfuscate(String encode, final String s) {
        if (encode == null) {
            return null;
        }
        try {
            final Cipher mEncryptor = this.mEncryptor;
            final StringBuilder sb = new StringBuilder();
            sb.append("com.android.vending.licensing.AESObfuscator-1|");
            sb.append(s);
            sb.append(encode);
            encode = Base64.encode(mEncryptor.doFinal(sb.toString().getBytes("UTF-8")));
            return encode;
        }
        catch (GeneralSecurityException ex) {
            throw new RuntimeException("Invalid environment", ex);
        }
        catch (UnsupportedEncodingException ex2) {
            throw new RuntimeException("Invalid environment", ex2);
        }
    }
    
    @Override
    public String unobfuscate(final String s, final String s2) throws ValidationException {
        if (s == null) {
            return null;
        }
        try {
            final String s3 = new String(this.mDecryptor.doFinal(Base64.decode(s)), "UTF-8");
            final StringBuilder sb = new StringBuilder();
            sb.append("com.android.vending.licensing.AESObfuscator-1|");
            sb.append(s2);
            if (s3.indexOf(sb.toString()) == 0) {
                return s3.substring(s2.length() + 46, s3.length());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Header not found (invalid data or key):");
            sb2.append(s);
            throw new ValidationException(sb2.toString());
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Invalid environment", ex);
        }
        catch (BadPaddingException ex2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(ex2.getMessage());
            sb3.append(":");
            sb3.append(s);
            throw new ValidationException(sb3.toString());
        }
        catch (IllegalBlockSizeException ex3) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(ex3.getMessage());
            sb4.append(":");
            sb4.append(s);
            throw new ValidationException(sb4.toString());
        }
        catch (Base64DecoderException ex4) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(ex4.getMessage());
            sb5.append(":");
            sb5.append(s);
            throw new ValidationException(sb5.toString());
        }
    }
}
