package com.googleplay.licensing;

import android.util.*;
import android.text.*;
import com.googleplay.util.*;
import java.security.*;

class LicenseValidator
{
    private static final int ERROR_CONTACTING_SERVER = 257;
    private static final int ERROR_INVALID_PACKAGE_NAME = 258;
    private static final int ERROR_NON_MATCHING_UID = 259;
    private static final int ERROR_NOT_MARKET_MANAGED = 3;
    private static final int ERROR_OVER_QUOTA = 5;
    private static final int ERROR_SERVER_FAILURE = 4;
    private static final int LICENSED = 0;
    private static final int LICENSED_OLD_KEY = 2;
    private static final int NOT_LICENSED = 1;
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "LicenseValidator";
    private final LicenseCheckerCallback mCallback;
    private final DeviceLimiter mDeviceLimiter;
    private final int mNonce;
    private final String mPackageName;
    private final Policy mPolicy;
    private final String mVersionCode;
    
    LicenseValidator(final Policy mPolicy, final DeviceLimiter mDeviceLimiter, final LicenseCheckerCallback mCallback, final int mNonce, final String mPackageName, final String mVersionCode) {
        this.mPolicy = mPolicy;
        this.mDeviceLimiter = mDeviceLimiter;
        this.mCallback = mCallback;
        this.mNonce = mNonce;
        this.mPackageName = mPackageName;
        this.mVersionCode = mVersionCode;
    }
    
    private void handleApplicationError(final int n) {
        this.mCallback.applicationError(n);
    }
    
    private void handleInvalidResponse() {
        this.mCallback.dontAllow(561);
    }
    
    private void handleResponse(final int n, final ResponseData responseData) {
        this.mPolicy.processServerResponse(n, responseData);
        if (this.mPolicy.allowAccess()) {
            this.mCallback.allow(n);
            return;
        }
        this.mCallback.dontAllow(n);
    }
    
    public LicenseCheckerCallback getCallback() {
        return this.mCallback;
    }
    
    public int getNonce() {
        return this.mNonce;
    }
    
    public String getPackageName() {
        return this.mPackageName;
    }
    
    public void verify(final PublicKey publicKey, final int n, String userId, String s) {
        final ResponseData responseData = null;
        while (true) {
            Label_0212: {
                if (n != 0 && n != 1 && n != 2) {
                    userId = null;
                    final ResponseData parse = responseData;
                    break Label_0212;
                }
                Label_0045: {
                    if (userId != null) {
                        break Label_0045;
                    }
                    try {
                        Log.e("LicenseValidator", "Invalid signed data provided.");
                        this.handleInvalidResponse();
                        return;
                        final Signature instance = Signature.getInstance("SHA1withRSA");
                        instance.initVerify(publicKey);
                        instance.update(userId.getBytes());
                        // iftrue(Label_0093:, instance.verify(Base64.decode(s)))
                        Block_6: {
                            break Block_6;
                            final NoSuchAlgorithmException ex;
                            throw new RuntimeException(ex);
                        }
                        Log.e("LicenseValidator", "Signature verification failed.");
                        this.handleInvalidResponse();
                        return;
                        try {
                            final ResponseData parse;
                            Label_0093: {
                                parse = ResponseData.parse(userId);
                            }
                            if (parse.responseCode != n) {
                                Log.e("LicenseValidator", "Response codes don't match.");
                                this.handleInvalidResponse();
                                return;
                            }
                            if (parse.nonce != this.mNonce) {
                                Log.e("LicenseValidator", "Nonce doesn't match.");
                                this.handleInvalidResponse();
                                return;
                            }
                            if (!parse.packageName.equals(this.mPackageName)) {
                                Log.e("LicenseValidator", "Package name doesn't match.");
                                this.handleInvalidResponse();
                                return;
                            }
                            parse.versionCode.equals(this.mVersionCode);
                            s = (userId = parse.userId);
                            if (TextUtils.isEmpty((CharSequence)s)) {
                                Log.e("LicenseValidator", "User identifier is empty.");
                                this.handleInvalidResponse();
                                return;
                            }
                            if (n != 0) {
                                if (n == 1) {
                                    this.handleResponse(561, parse);
                                    return;
                                }
                                if (n != 2) {
                                    if (n == 3) {
                                        this.handleApplicationError(3);
                                        return;
                                    }
                                    if (n == 4) {
                                        Log.w("LicenseValidator", "An error has occurred on the licensing server.");
                                        this.handleResponse(291, parse);
                                        return;
                                    }
                                    if (n == 5) {
                                        Log.w("LicenseValidator", "Licensing server is refusing to talk to this device, over quota.");
                                        this.handleResponse(291, parse);
                                        return;
                                    }
                                    switch (n) {
                                        default: {
                                            Log.e("LicenseValidator", "Unknown response code for license check.");
                                            this.handleInvalidResponse();
                                            return;
                                        }
                                        case 259: {
                                            this.handleApplicationError(2);
                                            return;
                                        }
                                        case 258: {
                                            this.handleApplicationError(1);
                                            return;
                                        }
                                        case 257: {
                                            Log.w("LicenseValidator", "Error contacting licensing server.");
                                            this.handleResponse(291, parse);
                                            return;
                                        }
                                    }
                                }
                            }
                            this.handleResponse(this.mDeviceLimiter.isDeviceAllowed(userId), parse);
                            return;
                        }
                        catch (IllegalArgumentException ex3) {
                            Log.e("LicenseValidator", "Could not parse response.");
                            this.handleInvalidResponse();
                            return;
                        }
                        Log.e("LicenseValidator", "Could not Base64-decode signature.");
                        this.handleInvalidResponse();
                        return;
                    }
                    catch (Base64DecoderException ex4) {}
                    catch (SignatureException ex5) {}
                    catch (InvalidKeyException ex6) {}
                    catch (NoSuchAlgorithmException ex2) {}
                }
            }
            final NoSuchAlgorithmException ex2;
            final NoSuchAlgorithmException ex = ex2;
            continue;
        }
    }
}
