package com.microsoft.aad.adal;

import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import com.google.gson.*;
import android.util.*;
import java.security.cert.*;
import com.google.gson.annotations.*;

class JWSBuilder implements IJWSBuilder
{
    private static final String JWS_ALGORITHM = "SHA256withRSA";
    private static final String JWS_HEADER_ALG = "RS256";
    private static final long SECONDS_MS = 1000L;
    private static final String TAG = "JWSBuilder";
    
    private static String sign(final RSAPrivateKey rsaPrivateKey, final byte[] array) throws AuthenticationException {
        try {
            final Signature instance = Signature.getInstance("SHA256withRSA");
            instance.initSign(rsaPrivateKey);
            instance.update(array);
            return StringExtensions.encodeBase64URLSafeString(instance.sign());
        }
        catch (NoSuchAlgorithmException ex) {
            final ADALError device_NO_SUCH_ALGORITHM = ADALError.DEVICE_NO_SUCH_ALGORITHM;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported RSA algorithm: ");
            sb.append(ex.getMessage());
            throw new AuthenticationException(device_NO_SUCH_ALGORITHM, sb.toString(), ex);
        }
        catch (UnsupportedEncodingException ex4) {
            throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED);
        }
        catch (SignatureException ex2) {
            final ADALError signature_EXCEPTION = ADALError.SIGNATURE_EXCEPTION;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("RSA signature exception: ");
            sb2.append(ex2.getMessage());
            throw new AuthenticationException(signature_EXCEPTION, sb2.toString(), ex2);
        }
        catch (InvalidKeyException ex3) {
            final ADALError key_CHAIN_PRIVATE_KEY_EXCEPTION = ADALError.KEY_CHAIN_PRIVATE_KEY_EXCEPTION;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Invalid private RSA key: ");
            sb3.append(ex3.getMessage());
            throw new AuthenticationException(key_CHAIN_PRIVATE_KEY_EXCEPTION, sb3.toString(), ex3);
        }
    }
    
    @Override
    public String generateSignedJWT(String s, String s2, final RSAPrivateKey rsaPrivateKey, final RSAPublicKey rsaPublicKey, final X509Certificate x509Certificate) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("nonce");
        }
        if (StringExtensions.isNullOrBlank(s2)) {
            throw new IllegalArgumentException("audience");
        }
        if (rsaPrivateKey != null) {
            if (rsaPublicKey != null) {
                final Gson gson = new Gson();
                final Claims claims = new Claims();
                claims.mNonce = s;
                claims.mAudience = s2;
                claims.mIssueAt = System.currentTimeMillis() / 1000L;
                final JwsHeader jwsHeader = new JwsHeader();
                jwsHeader.mAlgorithm = "RS256";
                jwsHeader.mType = "JWT";
                try {
                    jwsHeader.mCert = new String[1];
                    jwsHeader.mCert[0] = new String(Base64.encode(x509Certificate.getEncoded(), 2), "UTF_8");
                    s = gson.toJson((Object)jwsHeader);
                    s2 = gson.toJson((Object)claims);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Header: ");
                    sb.append(s);
                    Logger.v("JWSBuilder:generateSignedJWT", "Generate client certificate challenge response JWS Header. ", sb.toString(), null);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(StringExtensions.encodeBase64URLSafeString(s.getBytes("UTF_8")));
                    sb2.append(".");
                    sb2.append(StringExtensions.encodeBase64URLSafeString(s2.getBytes("UTF_8")));
                    s = sb2.toString();
                    s2 = sign(rsaPrivateKey, s.getBytes("UTF_8"));
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(s);
                    sb3.append(".");
                    sb3.append(s2);
                    return sb3.toString();
                }
                catch (CertificateEncodingException ex) {
                    throw new AuthenticationException(ADALError.CERTIFICATE_ENCODING_ERROR, "Certificate encoding error", ex);
                }
                catch (UnsupportedEncodingException ex2) {
                    throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, "Unsupported encoding", ex2);
                }
            }
            throw new IllegalArgumentException("pubKey");
        }
        throw new IllegalArgumentException("privateKey");
    }
    
    final class Claims
    {
        @SerializedName("aud")
        private String mAudience;
        @SerializedName("iat")
        private long mIssueAt;
        @SerializedName("nonce")
        private String mNonce;
        
        private Claims() {
        }
    }
    
    final class JwsHeader
    {
        @SerializedName("alg")
        private String mAlgorithm;
        @SerializedName("x5c")
        private String[] mCert;
        @SerializedName("typ")
        private String mType;
        
        private JwsHeader() {
        }
    }
}
