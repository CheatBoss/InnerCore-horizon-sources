package com.microsoft.xal.crypto;

import org.spongycastle.jce.provider.*;
import android.util.*;
import java.security.interfaces.*;
import android.content.*;
import java.io.*;
import java.security.spec.*;
import java.security.*;

public class Ecdsa
{
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String ECDSA_SIGNATURE_NAME = "NONEwithECDSA";
    private static final String EC_ALGORITHM_NAME = "secp256r1";
    private static final String KEY_ALIAS_PREFIX = "xal_";
    private KeyPair keyPair;
    private String uniqueId;
    
    static {
        Security.insertProviderAt((Provider)new BouncyCastleProvider(), 1);
    }
    
    private static String getBase64StringFromBytes(final byte[] array) {
        return Base64.encodeToString(array, 0, array.length, 11);
    }
    
    private static byte[] getBytesFromBase64String(final String s) throws IllegalArgumentException {
        return Base64.decode(s, 11);
    }
    
    private static String getKeyAlias(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("xal_");
        sb.append(s);
        return sb.toString();
    }
    
    public static Ecdsa restoreKeyAndId(final Context context) throws ClassCastException, IllegalArgumentException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.microsoft.xal.crypto", 0);
        if (!sharedPreferences.contains("id") || !sharedPreferences.contains("public") || !sharedPreferences.contains("private")) {
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            edit.clear();
            edit.commit();
            return null;
        }
        final String string = sharedPreferences.getString("public", "");
        final String string2 = sharedPreferences.getString("private", "");
        final String string3 = sharedPreferences.getString("id", "");
        if (!string.isEmpty() && !string2.isEmpty() && !string3.isEmpty()) {
            final byte[] bytesFromBase64String = getBytesFromBase64String(string);
            final byte[] bytesFromBase64String2 = getBytesFromBase64String(string2);
            final KeyFactory instance = KeyFactory.getInstance("ECDSA", "SC");
            final ECPublicKey ecPublicKey = (ECPublicKey)instance.generatePublic(new X509EncodedKeySpec(bytesFromBase64String));
            final ECPrivateKey ecPrivateKey = (ECPrivateKey)instance.generatePrivate(new PKCS8EncodedKeySpec(bytesFromBase64String2));
            final Ecdsa ecdsa = new Ecdsa();
            ecdsa.uniqueId = string3;
            ecdsa.keyPair = new KeyPair(ecPublicKey, ecPrivateKey);
            return ecdsa;
        }
        final SharedPreferences$Editor edit2 = sharedPreferences.edit();
        edit2.clear();
        edit2.commit();
        return null;
    }
    
    private byte[] toP1363SignedBuffer(final byte[] array) {
        int n = array[3];
        final int n2 = n + 4 + 1;
        final int n3 = n2 + 1;
        final byte b = array[n2];
        int n4;
        if (n > 32) {
            n4 = 5;
            n = 32;
        }
        else {
            n4 = 4;
        }
        int n5 = b;
        int n6 = n3;
        if (b > 32) {
            n6 = n3 + 1;
            n5 = 32;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(array, n4, n);
        byteArrayOutputStream.write(array, n6, n5);
        return byteArrayOutputStream.toByteArray();
    }
    
    public void generateKey(final String uniqueId) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        final KeyPairGenerator instance = KeyPairGenerator.getInstance("ECDSA", "SC");
        instance.initialize(new ECGenParameterSpec("secp256r1"));
        this.uniqueId = uniqueId;
        this.keyPair = instance.generateKeyPair();
    }
    
    public EccPubKey getPublicKey() {
        return new EccPubKey((ECPublicKey)this.keyPair.getPublic());
    }
    
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    public byte[] hashAndSign(final byte[] array) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final ShaHasher shaHasher = new ShaHasher();
        shaHasher.AddBytes(array);
        return this.sign(shaHasher.SignHash());
    }
    
    public byte[] sign(final byte[] array) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature instance = Signature.getInstance("NONEwithECDSA");
        instance.initSign(this.keyPair.getPrivate());
        instance.update(array);
        return this.toP1363SignedBuffer(instance.sign());
    }
    
    public boolean storeKeyPairAndId(final Context context, final String s) {
        final SharedPreferences$Editor edit = context.getSharedPreferences("com.microsoft.xal.crypto", 0).edit();
        edit.putString("id", s);
        edit.putString("public", getBase64StringFromBytes(this.keyPair.getPublic().getEncoded()));
        edit.putString("private", getBase64StringFromBytes(this.keyPair.getPrivate().getEncoded()));
        return edit.commit();
    }
}
