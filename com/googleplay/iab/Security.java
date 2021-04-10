package com.googleplay.iab;

import android.util.*;
import java.security.spec.*;
import java.security.*;
import android.text.*;

public class Security
{
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "IABUtil/Security";
    
    public static PublicKey generatePublicKey(final String s) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(s, 0)));
        }
        catch (InvalidKeySpecException ex) {
            Log.e("IABUtil/Security", "Invalid key specification.");
            throw new IllegalArgumentException(ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new RuntimeException(ex2);
        }
    }
    
    public static boolean verify(final PublicKey publicKey, final String s, final String s2) {
        try {
            final byte[] decode = Base64.decode(s2, 0);
            try {
                final Signature instance = Signature.getInstance("SHA1withRSA");
                instance.initVerify(publicKey);
                instance.update(s.getBytes());
                if (!instance.verify(decode)) {
                    Log.e("IABUtil/Security", "Signature verification failed.");
                    return false;
                }
                return true;
            }
            catch (SignatureException ex) {}
            catch (InvalidKeyException ex2) {}
            catch (NoSuchAlgorithmException ex3) {}
        }
        catch (IllegalArgumentException ex4) {
            goto Label_0049;
        }
    }
    
    public static boolean verifyPurchase(final String s, final String s2, final String s3) {
        if (!TextUtils.isEmpty((CharSequence)s2) && !TextUtils.isEmpty((CharSequence)s) && !TextUtils.isEmpty((CharSequence)s3)) {
            return verify(generatePublicKey(s), s2, s3);
        }
        Log.e("IABUtil/Security", "Purchase verification failed: missing data.");
        return false;
    }
}
