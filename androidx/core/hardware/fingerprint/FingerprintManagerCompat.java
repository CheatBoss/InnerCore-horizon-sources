package androidx.core.hardware.fingerprint;

import android.content.*;
import android.hardware.fingerprint.*;
import androidx.core.os.*;
import android.os.*;
import androidx.annotation.*;
import javax.crypto.*;
import java.security.*;

@Deprecated
public final class FingerprintManagerCompat
{
    private final Context mContext;
    
    private FingerprintManagerCompat(final Context mContext) {
        this.mContext = mContext;
    }
    
    @NonNull
    public static FingerprintManagerCompat from(@NonNull final Context context) {
        return new FingerprintManagerCompat(context);
    }
    
    @Nullable
    @RequiresApi(23)
    private static FingerprintManager getFingerprintManagerOrNull(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT == 23) {
            return (FingerprintManager)context.getSystemService((Class)FingerprintManager.class);
        }
        if (Build$VERSION.SDK_INT > 23 && context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager)context.getSystemService((Class)FingerprintManager.class);
        }
        return null;
    }
    
    @RequiresApi(23)
    static CryptoObject unwrapCryptoObject(final FingerprintManager$CryptoObject fingerprintManager$CryptoObject) {
        if (fingerprintManager$CryptoObject == null) {
            return null;
        }
        if (fingerprintManager$CryptoObject.getCipher() != null) {
            return new CryptoObject(fingerprintManager$CryptoObject.getCipher());
        }
        if (fingerprintManager$CryptoObject.getSignature() != null) {
            return new CryptoObject(fingerprintManager$CryptoObject.getSignature());
        }
        if (fingerprintManager$CryptoObject.getMac() != null) {
            return new CryptoObject(fingerprintManager$CryptoObject.getMac());
        }
        return null;
    }
    
    @RequiresApi(23)
    private static FingerprintManager$AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new FingerprintManager$AuthenticationCallback() {
            public void onAuthenticationError(final int n, final CharSequence charSequence) {
                authenticationCallback.onAuthenticationError(n, charSequence);
            }
            
            public void onAuthenticationFailed() {
                authenticationCallback.onAuthenticationFailed();
            }
            
            public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
                authenticationCallback.onAuthenticationHelp(n, charSequence);
            }
            
            public void onAuthenticationSucceeded(final FingerprintManager$AuthenticationResult fingerprintManager$AuthenticationResult) {
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(fingerprintManager$AuthenticationResult.getCryptoObject())));
            }
        };
    }
    
    @RequiresApi(23)
    private static FingerprintManager$CryptoObject wrapCryptoObject(final CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new FingerprintManager$CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new FingerprintManager$CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new FingerprintManager$CryptoObject(cryptoObject.getMac());
        }
        return null;
    }
    
    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public void authenticate(@Nullable final CryptoObject cryptoObject, final int n, @Nullable final CancellationSignal cancellationSignal, @NonNull final AuthenticationCallback authenticationCallback, @Nullable final Handler handler) {
        if (Build$VERSION.SDK_INT >= 23) {
            final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(this.mContext);
            if (fingerprintManagerOrNull != null) {
                android.os.CancellationSignal cancellationSignal2;
                if (cancellationSignal != null) {
                    cancellationSignal2 = (android.os.CancellationSignal)cancellationSignal.getCancellationSignalObject();
                }
                else {
                    cancellationSignal2 = null;
                }
                fingerprintManagerOrNull.authenticate(wrapCryptoObject(cryptoObject), cancellationSignal2, n, wrapCallback(authenticationCallback), handler);
            }
        }
    }
    
    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public boolean hasEnrolledFingerprints() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        if (sdk_INT >= 23) {
            final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(this.mContext);
            boolean b2 = b;
            if (fingerprintManagerOrNull != null) {
                b2 = b;
                if (fingerprintManagerOrNull.hasEnrolledFingerprints()) {
                    b2 = true;
                }
            }
            return b2;
        }
        return false;
    }
    
    @RequiresPermission("android.permission.USE_FINGERPRINT")
    public boolean isHardwareDetected() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        if (sdk_INT >= 23) {
            final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(this.mContext);
            boolean b2 = b;
            if (fingerprintManagerOrNull != null) {
                b2 = b;
                if (fingerprintManagerOrNull.isHardwareDetected()) {
                    b2 = true;
                }
            }
            return b2;
        }
        return false;
    }
    
    public abstract static class AuthenticationCallback
    {
        public void onAuthenticationError(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationFailed() {
        }
        
        public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationSucceeded(final AuthenticationResult authenticationResult) {
        }
    }
    
    public static final class AuthenticationResult
    {
        private final CryptoObject mCryptoObject;
        
        public AuthenticationResult(final CryptoObject mCryptoObject) {
            this.mCryptoObject = mCryptoObject;
        }
        
        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }
    
    public static class CryptoObject
    {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;
        
        public CryptoObject(@NonNull final Signature mSignature) {
            this.mSignature = mSignature;
            this.mCipher = null;
            this.mMac = null;
        }
        
        public CryptoObject(@NonNull final Cipher mCipher) {
            this.mCipher = mCipher;
            this.mSignature = null;
            this.mMac = null;
        }
        
        public CryptoObject(@NonNull final Mac mMac) {
            this.mMac = mMac;
            this.mCipher = null;
            this.mSignature = null;
        }
        
        @Nullable
        public Cipher getCipher() {
            return this.mCipher;
        }
        
        @Nullable
        public Mac getMac() {
            return this.mMac;
        }
        
        @Nullable
        public Signature getSignature() {
            return this.mSignature;
        }
    }
}
