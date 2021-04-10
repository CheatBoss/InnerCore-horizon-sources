package com.googleplay.licensing;

import java.util.*;
import android.util.*;
import java.security.spec.*;
import com.googleplay.util.*;
import java.security.*;
import android.content.pm.*;
import android.content.*;
import android.os.*;

public class LicenseChecker implements ServiceConnection
{
    private static final boolean DEBUG_LICENSE_ERROR = false;
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final SecureRandom RANDOM;
    private static final String TAG = "LicenseChecker";
    private static final int TIMEOUT_MS = 10000;
    private final Set<LicenseValidator> mChecksInProgress;
    private final Context mContext;
    private Handler mHandler;
    private final String mPackageName;
    private final Queue<LicenseValidator> mPendingChecks;
    private final Policy mPolicy;
    private PublicKey mPublicKey;
    private ILicensingService mService;
    private final String mVersionCode;
    
    static {
        RANDOM = new SecureRandom();
    }
    
    public LicenseChecker(final Context mContext, final Policy mPolicy, final String s) {
        this.mChecksInProgress = new HashSet<LicenseValidator>();
        this.mPendingChecks = new LinkedList<LicenseValidator>();
        this.mContext = mContext;
        this.mPolicy = mPolicy;
        this.mPublicKey = generatePublicKey(s);
        this.mPackageName = this.mContext.getPackageName();
        this.mVersionCode = getVersionCode(mContext, this.mPackageName);
        final HandlerThread handlerThread = new HandlerThread("background thread");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }
    
    private void cleanupService() {
        if (this.mService != null) {
            try {
                this.mContext.unbindService((ServiceConnection)this);
            }
            catch (IllegalArgumentException ex) {
                Log.e("LicenseChecker", "Unable to unbind from licensing service (already unbound)");
            }
            this.mService = null;
        }
    }
    
    private void finishCheck(final LicenseValidator licenseValidator) {
        synchronized (this) {
            this.mChecksInProgress.remove(licenseValidator);
            if (this.mChecksInProgress.isEmpty()) {
                this.cleanupService();
            }
        }
    }
    
    private int generateNonce() {
        return LicenseChecker.RANDOM.nextInt();
    }
    
    private static PublicKey generatePublicKey(final String s) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(s)));
        }
        catch (InvalidKeySpecException ex) {
            Log.e("LicenseChecker", "Invalid key specification.");
            throw new IllegalArgumentException(ex);
        }
        catch (Base64DecoderException ex2) {
            Log.e("LicenseChecker", "Could not decode from Base64.");
            throw new IllegalArgumentException(ex2);
        }
        catch (NoSuchAlgorithmException ex3) {
            throw new RuntimeException(ex3);
        }
    }
    
    private static String getVersionCode(final Context context, final String s) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(s, 0).versionCode);
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("LicenseChecker", "Package not found. could not get version code.");
            return "";
        }
    }
    
    private void handleServiceConnectionError(final LicenseValidator licenseValidator) {
        synchronized (this) {
            licenseValidator.getCallback().allow(256);
        }
    }
    
    private void runChecks() {
        while (true) {
            final LicenseValidator licenseValidator = this.mPendingChecks.poll();
            if (licenseValidator == null) {
                break;
            }
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append("Calling checkLicense on service for ");
                sb.append(licenseValidator.getPackageName());
                Log.i("LicenseChecker", sb.toString());
                this.mService.checkLicense(licenseValidator.getNonce(), licenseValidator.getPackageName(), new ResultListener(licenseValidator));
                this.mChecksInProgress.add(licenseValidator);
            }
            catch (RemoteException ex) {
                Log.w("LicenseChecker", "RemoteException in checkLicense call.", (Throwable)ex);
                this.handleServiceConnectionError(licenseValidator);
            }
        }
    }
    
    public void checkAccess(final LicenseCheckerCallback licenseCheckerCallback) {
        synchronized (this) {
            licenseCheckerCallback.allow(256);
        }
    }
    
    public void onDestroy() {
        synchronized (this) {
            this.cleanupService();
            this.mHandler.getLooper().quit();
        }
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        synchronized (this) {
            this.mService = ILicensingService.Stub.asInterface(binder);
            this.runChecks();
        }
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
        synchronized (this) {
            Log.w("LicenseChecker", "Service unexpectedly disconnected.");
            this.mService = null;
        }
    }
    
    private class ResultListener extends Stub
    {
        private static final int ERROR_CONTACTING_SERVER = 257;
        private static final int ERROR_INVALID_PACKAGE_NAME = 258;
        private static final int ERROR_NON_MATCHING_UID = 259;
        private Runnable mOnTimeout;
        private final LicenseValidator mValidator;
        
        public ResultListener(final LicenseValidator mValidator) {
            this.mValidator = mValidator;
            this.mOnTimeout = new Runnable() {
                @Override
                public void run() {
                    Log.i("LicenseChecker", "Check timed out.");
                    LicenseChecker.this.handleServiceConnectionError(ResultListener.this.mValidator);
                    LicenseChecker.this.finishCheck(ResultListener.this.mValidator);
                }
            };
            this.startTimeout();
        }
        
        private void clearTimeout() {
            Log.i("LicenseChecker", "Clearing timeout.");
            LicenseChecker.this.mHandler.removeCallbacks(this.mOnTimeout);
        }
        
        private void startTimeout() {
            Log.i("LicenseChecker", "Start monitoring timeout.");
            LicenseChecker.this.mHandler.postDelayed(this.mOnTimeout, 10000L);
        }
        
        public void verifyLicense(final int n, final String s, final String s2) {
            LicenseChecker.this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    Log.i("LicenseChecker", "Received response.");
                    if (LicenseChecker.this.mChecksInProgress.contains(ResultListener.this.mValidator)) {
                        ResultListener.this.clearTimeout();
                        ResultListener.this.mValidator.verify(LicenseChecker.this.mPublicKey, n, s, s2);
                        LicenseChecker.this.finishCheck(ResultListener.this.mValidator);
                    }
                }
            });
        }
    }
}
