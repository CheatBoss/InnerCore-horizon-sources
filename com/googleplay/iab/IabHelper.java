package com.googleplay.iab;

import com.android.vending.billing.*;
import org.json.*;
import android.app.*;
import android.util.*;
import android.text.*;
import java.util.*;
import android.content.*;
import android.os.*;

public class IabHelper
{
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    public static final int IABHELPER_INVALID_SERVICE = -1012;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE = -1011;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    boolean mAsyncInProgress;
    private final Object mAsyncInProgressLock;
    String mAsyncOperation;
    Context mContext;
    boolean mDebugLog;
    String mDebugTag;
    boolean mDisposeAfterAsync;
    boolean mDisposed;
    boolean mIsServiceBound;
    OnIabPurchaseFinishedListener mPurchaseListener;
    String mPurchasingItemType;
    int mRequestCode;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    boolean mSetupDone;
    String mSignatureBase64;
    boolean mSubscriptionUpdateSupported;
    boolean mSubscriptionsSupported;
    
    public IabHelper(final Context context, final String mSignatureBase64) {
        this.mDebugLog = false;
        this.mDebugTag = "IabHelper";
        this.mSetupDone = false;
        this.mDisposed = false;
        this.mDisposeAfterAsync = false;
        this.mSubscriptionsSupported = false;
        this.mSubscriptionUpdateSupported = false;
        this.mAsyncInProgress = false;
        this.mAsyncInProgressLock = new Object();
        this.mAsyncOperation = "";
        this.mIsServiceBound = false;
        this.mSignatureBase64 = null;
        this.mContext = context.getApplicationContext();
        this.mSignatureBase64 = mSignatureBase64;
        this.logDebug("IAB helper created.");
    }
    
    private void checkNotDisposed() {
        if (!this.mDisposed) {
            return;
        }
        throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
    }
    
    public static String getResponseDesc(final int n) {
        final String[] split = "0:OK/1:User Canceled/2:Unknown/3:Billing Unavailable/4:Item unavailable/5:Developer Error/6:Error/7:Item Already Owned/8:Item not owned".split("/");
        final String[] split2 = "0:OK/-1001:Remote exception during initialization/-1002:Bad response received/-1003:Purchase signature verification failed/-1004:Send intent failed/-1005:User cancelled/-1006:Unknown purchase response/-1007:Missing token/-1008:Unknown error/-1009:Subscriptions not available/-1010:Invalid consumption attempt".split("/");
        StringBuilder sb;
        String s;
        if (n <= -1000) {
            final int n2 = -1000 - n;
            if (n2 >= 0 && n2 < split2.length) {
                return split2[n2];
            }
            sb = new StringBuilder();
            sb.append(String.valueOf(n));
            s = ":Unknown IAB Helper Error";
        }
        else {
            if (n >= 0 && n < split.length) {
                return split[n];
            }
            sb = new StringBuilder();
            sb.append(String.valueOf(n));
            s = ":Unknown";
        }
        sb.append(s);
        return sb.toString();
    }
    
    void checkSetupDone(final String s) {
        if (this.mSetupDone) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal state for operation (");
        sb.append(s);
        sb.append("): IAB helper is not set up.");
        this.logError(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("IAB helper is not set up. Can't perform operation: ");
        sb2.append(s);
        throw new IllegalStateException(sb2.toString());
    }
    
    void consume(final Purchase purchase) throws IabException {
        this.checkNotDisposed();
        this.checkSetupDone("consume");
        if (purchase.mItemType.equals("inapp")) {
            try {
                final String token = purchase.getToken();
                final String sku = purchase.getSku();
                if (token == null || token.equals("")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Can't consume ");
                    sb.append(sku);
                    sb.append(". No token.");
                    this.logError(sb.toString());
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("PurchaseInfo is missing token for sku: ");
                    sb2.append(sku);
                    sb2.append(" ");
                    sb2.append(purchase);
                    throw new IabException(-1007, sb2.toString());
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Consuming sku: ");
                sb3.append(sku);
                sb3.append(", token: ");
                sb3.append(token);
                this.logDebug(sb3.toString());
                final int consumePurchase = this.mService.consumePurchase(3, this.mContext.getPackageName(), token);
                if (consumePurchase == 0) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Successfully consumed sku: ");
                    sb4.append(sku);
                    this.logDebug(sb4.toString());
                    return;
                }
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Error consuming consuming sku ");
                sb5.append(sku);
                sb5.append(". ");
                sb5.append(getResponseDesc(consumePurchase));
                this.logDebug(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Error consuming sku ");
                sb6.append(sku);
                throw new IabException(consumePurchase, sb6.toString());
            }
            catch (RemoteException ex) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("Remote exception while consuming. PurchaseInfo: ");
                sb7.append(purchase);
                throw new IabException(-1001, sb7.toString(), (Exception)ex);
            }
        }
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("Items of type '");
        sb8.append(purchase.mItemType);
        sb8.append("' can't be consumed.");
        throw new IabException(-1010, sb8.toString());
    }
    
    public void consumeAsync(final Purchase purchase, final OnConsumeFinishedListener onConsumeFinishedListener) throws IabAsyncInProgressException {
        this.checkNotDisposed();
        this.checkSetupDone("consume");
        final ArrayList<Purchase> list = new ArrayList<Purchase>();
        list.add(purchase);
        this.consumeAsyncInternal(list, onConsumeFinishedListener, null);
    }
    
    public void consumeAsync(final List<Purchase> list, final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) throws IabAsyncInProgressException {
        this.checkNotDisposed();
        this.checkSetupDone("consume");
        this.consumeAsyncInternal(list, null, onConsumeMultiFinishedListener);
    }
    
    void consumeAsyncInternal(final List<Purchase> list, final OnConsumeFinishedListener onConsumeFinishedListener, final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) throws IabAsyncInProgressException {
        final Handler handler = new Handler();
        this.flagStartAsync("consume");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<IabResult> list = new ArrayList<IabResult>();
                for (final Purchase purchase : list) {
                    try {
                        IabHelper.this.consume(purchase);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Successful consume of sku ");
                        sb.append(purchase.getSku());
                        list.add(new IabResult(0, sb.toString()));
                    }
                    catch (IabException ex) {
                        list.add(ex.getResult());
                    }
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && onConsumeFinishedListener != null) {
                    handler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            onConsumeFinishedListener.onConsumeFinished(list.get(0), list.get(0));
                        }
                    });
                }
                if (!IabHelper.this.mDisposed && onConsumeMultiFinishedListener != null) {
                    handler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            onConsumeMultiFinishedListener.onConsumeMultiFinished(list, list);
                        }
                    });
                }
            }
        }).start();
    }
    
    public void dispose() throws IabAsyncInProgressException {
        Object o = this.mAsyncInProgressLock;
        synchronized (o) {
            if (!this.mAsyncInProgress) {
                // monitorexit(o)
                this.logDebug("Disposing.");
                this.mSetupDone = false;
                if (this.mServiceConn != null) {
                    this.logDebug("Unbinding from service.");
                    if (this.mIsServiceBound) {
                        o = this.mContext;
                        if (o != null) {
                            ((Context)o).unbindService(this.mServiceConn);
                            this.mIsServiceBound = false;
                        }
                    }
                }
                this.mDisposed = true;
                this.mContext = null;
                this.mServiceConn = null;
                this.mService = null;
                this.mPurchaseListener = null;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't dispose because an async operation (");
            sb.append(this.mAsyncOperation);
            sb.append(") is in progres.");
            throw new IabAsyncInProgressException(sb.toString());
        }
    }
    
    public void disposeWhenFinished() {
        synchronized (this.mAsyncInProgressLock) {
            if (this.mAsyncInProgress) {
                this.logDebug("Will dispose after async operation finishes.");
                this.mDisposeAfterAsync = true;
            }
            else {
                try {
                    this.dispose();
                }
                catch (IabAsyncInProgressException ex) {}
            }
        }
    }
    
    public void enableDebugLogging(final boolean mDebugLog) {
        this.checkNotDisposed();
        this.mDebugLog = mDebugLog;
    }
    
    public void enableDebugLogging(final boolean mDebugLog, final String mDebugTag) {
        this.checkNotDisposed();
        this.mDebugLog = mDebugLog;
        this.mDebugTag = mDebugTag;
    }
    
    void flagEndAsync() {
        synchronized (this.mAsyncInProgressLock) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ending async operation: ");
            sb.append(this.mAsyncOperation);
            this.logDebug(sb.toString());
            this.mAsyncOperation = "";
            this.mAsyncInProgress = false;
            if (this.mDisposeAfterAsync) {
                try {
                    this.dispose();
                }
                catch (IabAsyncInProgressException ex) {}
            }
        }
    }
    
    void flagStartAsync(final String mAsyncOperation) throws IabAsyncInProgressException {
        synchronized (this.mAsyncInProgressLock) {
            if (!this.mAsyncInProgress) {
                this.mAsyncOperation = mAsyncOperation;
                this.mAsyncInProgress = true;
                final StringBuilder sb = new StringBuilder();
                sb.append("Starting async operation: ");
                sb.append(mAsyncOperation);
                this.logDebug(sb.toString());
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't start async operation (");
            sb2.append(mAsyncOperation);
            sb2.append(") because another async operation (");
            sb2.append(this.mAsyncOperation);
            sb2.append(") is in progress.");
            throw new IabAsyncInProgressException(sb2.toString());
        }
    }
    
    int getResponseCodeFromBundle(final Bundle bundle) {
        final Object value = bundle.get("RESPONSE_CODE");
        if (value == null) {
            this.logDebug("Bundle with null response code, assuming OK (known issue)");
            return 0;
        }
        if (value instanceof Integer) {
            return (int)value;
        }
        if (value instanceof Long) {
            return (int)(long)value;
        }
        this.logError("Unexpected type for bundle response code.");
        this.logError(((Long)value).getClass().getName());
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected type for bundle response code: ");
        sb.append(((Long)value).getClass().getName());
        throw new RuntimeException(sb.toString());
    }
    
    int getResponseCodeFromIntent(final Intent intent) {
        final Object value = intent.getExtras().get("RESPONSE_CODE");
        if (value == null) {
            this.logError("Intent with no response code, assuming OK (known issue)");
            return 0;
        }
        if (value instanceof Integer) {
            return (int)value;
        }
        if (value instanceof Long) {
            return (int)(long)value;
        }
        this.logError("Unexpected type for intent response code.");
        this.logError(((Long)value).getClass().getName());
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected type for intent response code: ");
        sb.append(((Long)value).getClass().getName());
        throw new RuntimeException(sb.toString());
    }
    
    public boolean handleActivityResult(int responseCodeFromIntent, final int n, final Intent intent) {
        if (responseCodeFromIntent != this.mRequestCode) {
            return false;
        }
        this.checkNotDisposed();
        this.checkSetupDone("handleActivityResult");
        this.flagEndAsync();
        if (intent == null) {
            this.logError("Null data in IAB activity result.");
            final IabResult iabResult = new IabResult(-1002, "Null data in IAB result");
            final OnIabPurchaseFinishedListener mPurchaseListener = this.mPurchaseListener;
            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(iabResult, null);
            }
            return true;
        }
        responseCodeFromIntent = this.getResponseCodeFromIntent(intent);
        final String stringExtra = intent.getStringExtra("INAPP_PURCHASE_DATA");
        final String stringExtra2 = intent.getStringExtra("INAPP_DATA_SIGNATURE");
        if (n == -1 && responseCodeFromIntent == 0) {
            this.logDebug("Successful resultcode from purchase activity.");
            final StringBuilder sb = new StringBuilder();
            sb.append("Purchase data: ");
            sb.append(stringExtra);
            this.logDebug(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Data signature: ");
            sb2.append(stringExtra2);
            this.logDebug(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Extras: ");
            sb3.append(intent.getExtras());
            this.logDebug(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Expected item type: ");
            sb4.append(this.mPurchasingItemType);
            this.logDebug(sb4.toString());
            if (stringExtra != null) {
                if (stringExtra2 != null) {
                    try {
                        final Purchase purchase = new Purchase(this.mPurchasingItemType, stringExtra, stringExtra2);
                        final String sku = purchase.getSku();
                        if (!Security.verifyPurchase(this.mSignatureBase64, stringExtra, stringExtra2)) {
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("Purchase signature verification FAILED for sku ");
                            sb5.append(sku);
                            this.logError(sb5.toString());
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("Signature verification failed for sku ");
                            sb6.append(sku);
                            final IabResult iabResult2 = new IabResult(-1003, sb6.toString());
                            if (this.mPurchaseListener != null) {
                                this.mPurchaseListener.onIabPurchaseFinished(iabResult2, purchase);
                                return true;
                            }
                            return true;
                        }
                        else {
                            this.logDebug("Purchase signature successfully verified.");
                            final OnIabPurchaseFinishedListener mPurchaseListener2 = this.mPurchaseListener;
                            if (mPurchaseListener2 != null) {
                                mPurchaseListener2.onIabPurchaseFinished(new IabResult(0, "Success"), purchase);
                                return true;
                            }
                            return true;
                        }
                    }
                    catch (JSONException ex) {
                        this.logError("Failed to parse purchase data.");
                        ex.printStackTrace();
                        final IabResult iabResult3 = new IabResult(-1002, "Failed to parse purchase data.");
                        final OnIabPurchaseFinishedListener mPurchaseListener3 = this.mPurchaseListener;
                        if (mPurchaseListener3 != null) {
                            mPurchaseListener3.onIabPurchaseFinished(iabResult3, null);
                        }
                        return true;
                    }
                }
            }
            this.logError("BUG: either purchaseData or dataSignature is null.");
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Extras: ");
            sb7.append(intent.getExtras().toString());
            this.logDebug(sb7.toString());
            final IabResult iabResult4 = new IabResult(-1008, "IAB returned null purchaseData or dataSignature");
            final OnIabPurchaseFinishedListener mPurchaseListener4 = this.mPurchaseListener;
            if (mPurchaseListener4 != null) {
                mPurchaseListener4.onIabPurchaseFinished(iabResult4, null);
            }
            return true;
        }
        if (n == -1) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Result code was OK but in-app billing response was not OK: ");
            sb8.append(getResponseDesc(responseCodeFromIntent));
            this.logDebug(sb8.toString());
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(new IabResult(responseCodeFromIntent, "Problem purchashing item."), null);
                return true;
            }
        }
        else if (n == 0) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Purchase canceled - Response: ");
            sb9.append(getResponseDesc(responseCodeFromIntent));
            this.logDebug(sb9.toString());
            final IabResult iabResult5 = new IabResult(-1005, "User canceled.");
            final OnIabPurchaseFinishedListener mPurchaseListener5 = this.mPurchaseListener;
            if (mPurchaseListener5 != null) {
                mPurchaseListener5.onIabPurchaseFinished(iabResult5, null);
                return true;
            }
        }
        else {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Purchase failed. Result code: ");
            sb10.append(Integer.toString(n));
            sb10.append(". Response: ");
            sb10.append(getResponseDesc(responseCodeFromIntent));
            this.logError(sb10.toString());
            final IabResult iabResult6 = new IabResult(-1006, "Unknown purchase response.");
            final OnIabPurchaseFinishedListener mPurchaseListener6 = this.mPurchaseListener;
            if (mPurchaseListener6 != null) {
                mPurchaseListener6.onIabPurchaseFinished(iabResult6, null);
            }
        }
        return true;
    }
    
    public void launchPurchaseFlow(final Activity activity, final String s, final int n, final OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) throws IabAsyncInProgressException {
        this.launchPurchaseFlow(activity, s, n, onIabPurchaseFinishedListener, "");
    }
    
    public void launchPurchaseFlow(final Activity activity, final String s, final int n, final OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, final String s2) throws IabAsyncInProgressException {
        this.launchPurchaseFlow(activity, s, "inapp", null, n, onIabPurchaseFinishedListener, s2);
    }
    
    public void launchPurchaseFlow(final Activity activity, final String s, final String mPurchasingItemType, final List<String> list, final int mRequestCode, final OnIabPurchaseFinishedListener mPurchaseListener, final String s2) throws IabAsyncInProgressException {
        this.checkNotDisposed();
        this.checkSetupDone("launchPurchaseFlow");
        this.flagStartAsync("launchPurchaseFlow");
        if (mPurchasingItemType.equals("subs") && !this.mSubscriptionsSupported) {
            final IabResult iabResult = new IabResult(-1009, "Subscriptions are not available.");
            this.flagEndAsync();
            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(iabResult, null);
            }
            return;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("Constructing buy intent for ");
            sb.append(s);
            sb.append(", item type: ");
            sb.append(mPurchasingItemType);
            this.logDebug(sb.toString());
            Bundle bundle;
            if (list != null && !list.isEmpty()) {
                if (!this.mSubscriptionUpdateSupported) {
                    final IabResult iabResult2 = new IabResult(-1011, "Subscription updates are not available.");
                    this.flagEndAsync();
                    if (mPurchaseListener != null) {
                        mPurchaseListener.onIabPurchaseFinished(iabResult2, null);
                    }
                    return;
                }
                else {
                    bundle = this.mService.getBuyIntentToReplaceSkus(5, this.mContext.getPackageName(), list, s, mPurchasingItemType, s2);
                }
            }
            else {
                bundle = this.mService.getBuyIntent(3, this.mContext.getPackageName(), s, mPurchasingItemType, s2);
            }
            final int responseCodeFromBundle = this.getResponseCodeFromBundle(bundle);
            if (responseCodeFromBundle == 0) {
                final PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable("BUY_INTENT");
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Launching buy intent for ");
                sb2.append(s);
                sb2.append(". Request code: ");
                sb2.append(mRequestCode);
                this.logDebug(sb2.toString());
                this.mRequestCode = mRequestCode;
                this.mPurchaseListener = mPurchaseListener;
                this.mPurchasingItemType = mPurchasingItemType;
                activity.startIntentSenderForResult(pendingIntent.getIntentSender(), mRequestCode, new Intent(), (int)0, (int)0, (int)0);
                return;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to buy item, Error response: ");
            sb3.append(getResponseDesc(responseCodeFromBundle));
            this.logError(sb3.toString());
            this.flagEndAsync();
            final IabResult iabResult3 = new IabResult(responseCodeFromBundle, "Unable to buy item");
            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(iabResult3, null);
            }
        }
        catch (RemoteException ex) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("RemoteException while launching purchase flow for sku ");
            sb4.append(s);
            this.logError(sb4.toString());
            ex.printStackTrace();
            this.flagEndAsync();
            final IabResult iabResult4 = new IabResult(-1001, "Remote exception while starting purchase flow");
            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(iabResult4, null);
            }
        }
        catch (IntentSender$SendIntentException ex2) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("SendIntentException while launching purchase flow for sku ");
            sb5.append(s);
            this.logError(sb5.toString());
            ex2.printStackTrace();
            this.flagEndAsync();
            final IabResult iabResult5 = new IabResult(-1004, "Failed to send intent.");
            if (mPurchaseListener != null) {
                mPurchaseListener.onIabPurchaseFinished(iabResult5, null);
            }
        }
    }
    
    public void launchSubscriptionPurchaseFlow(final Activity activity, final String s, final int n, final OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) throws IabAsyncInProgressException {
        this.launchSubscriptionPurchaseFlow(activity, s, n, onIabPurchaseFinishedListener, "");
    }
    
    public void launchSubscriptionPurchaseFlow(final Activity activity, final String s, final int n, final OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, final String s2) throws IabAsyncInProgressException {
        this.launchPurchaseFlow(activity, s, "subs", null, n, onIabPurchaseFinishedListener, s2);
    }
    
    void logDebug(final String s) {
        if (this.mDebugLog) {
            Log.d(this.mDebugTag, s);
        }
    }
    
    void logError(final String s) {
        final String mDebugTag = this.mDebugTag;
        final StringBuilder sb = new StringBuilder();
        sb.append("In-app billing error: ");
        sb.append(s);
        Log.e(mDebugTag, sb.toString());
    }
    
    void logWarn(final String s) {
        final String mDebugTag = this.mDebugTag;
        final StringBuilder sb = new StringBuilder();
        sb.append("In-app billing warning: ");
        sb.append(s);
        Log.w(mDebugTag, sb.toString());
    }
    
    public Inventory queryInventory() throws IabException {
        return this.queryInventory(false, null, null);
    }
    
    public Inventory queryInventory(final boolean b, final List<String> list, final List<String> list2) throws IabException {
        this.checkNotDisposed();
        this.checkSetupDone("queryInventory");
        Inventory inventory;
        try {
            inventory = new Inventory();
            final int queryPurchases = this.queryPurchases(inventory, "inapp");
            if (queryPurchases != 0) {
                throw new IabException(queryPurchases, "Error refreshing inventory (querying owned items).");
            }
            if (b) {
                final int querySkuDetails = this.querySkuDetails("inapp", inventory, list2);
                if (querySkuDetails != 0) {
                    throw new IabException(querySkuDetails, "Error refreshing inventory (querying prices of items).");
                }
            }
            if (this.mSubscriptionsSupported) {
                final int queryPurchases2 = this.queryPurchases(inventory, "subs");
                if (queryPurchases2 != 0) {
                    throw new IabException(queryPurchases2, "Error refreshing inventory (querying owned subscriptions).");
                }
                if (b) {
                    final int querySkuDetails2 = this.querySkuDetails("subs", inventory, list);
                    if (querySkuDetails2 == 0) {
                        return inventory;
                    }
                    throw new IabException(querySkuDetails2, "Error refreshing inventory (querying prices of subscriptions).");
                }
            }
        }
        catch (JSONException ex) {
            throw new IabException(-1002, "Error parsing JSON response while refreshing inventory.", (Exception)ex);
        }
        catch (RemoteException ex2) {
            throw new IabException(-1001, "Remote exception while refreshing inventory.", (Exception)ex2);
        }
        return inventory;
    }
    
    public void queryInventoryAsync(final QueryInventoryFinishedListener queryInventoryFinishedListener) throws IabAsyncInProgressException {
        this.queryInventoryAsync(false, null, null, queryInventoryFinishedListener);
    }
    
    public void queryInventoryAsync(final boolean b, final List<String> list, final List<String> list2, final QueryInventoryFinishedListener queryInventoryFinishedListener) throws IabAsyncInProgressException {
        final Handler handler = new Handler();
        this.checkNotDisposed();
        this.checkSetupDone("queryInventory");
        this.flagStartAsync("refresh inventory");
        new Thread(new Runnable() {
            @Override
            public void run() {
                IabResult result = new IabResult(0, "Inventory refresh successful.");
                Inventory queryInventory;
                try {
                    queryInventory = IabHelper.this.queryInventory(b, list, list2);
                }
                catch (IabException ex) {
                    result = ex.getResult();
                    queryInventory = null;
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && queryInventoryFinishedListener != null) {
                    handler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            queryInventoryFinishedListener.onQueryInventoryFinished(result, queryInventory);
                        }
                    });
                }
            }
        }).start();
    }
    
    int queryPurchases(final Inventory inventory, final String s) throws JSONException, RemoteException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Querying owned items, item type: ");
        sb.append(s);
        this.logDebug(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Package name: ");
        sb2.append(this.mContext.getPackageName());
        this.logDebug(sb2.toString());
        if (this.mService == null) {
            return -1012;
        }
        String string = null;
        final boolean b = false;
        boolean b2 = false;
        while (true) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Calling getPurchases with continuation token: ");
            sb3.append(string);
            this.logDebug(sb3.toString());
            final Bundle purchases = this.mService.getPurchases(3, this.mContext.getPackageName(), s, string);
            final int responseCodeFromBundle = this.getResponseCodeFromBundle(purchases);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Owned items response: ");
            sb4.append(String.valueOf(responseCodeFromBundle));
            this.logDebug(sb4.toString());
            if (responseCodeFromBundle != 0) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("getPurchases() failed: ");
                sb5.append(getResponseDesc(responseCodeFromBundle));
                this.logDebug(sb5.toString());
                return responseCodeFromBundle;
            }
            if (!purchases.containsKey("INAPP_PURCHASE_ITEM_LIST") || !purchases.containsKey("INAPP_PURCHASE_DATA_LIST") || !purchases.containsKey("INAPP_DATA_SIGNATURE_LIST")) {
                this.logError("Bundle returned from getPurchases() doesn't contain required fields.");
                return -1002;
            }
            final ArrayList stringArrayList = purchases.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            final ArrayList stringArrayList2 = purchases.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            final ArrayList stringArrayList3 = purchases.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            for (int i = 0; i < stringArrayList2.size(); ++i) {
                final String s2 = stringArrayList2.get(i);
                final String s3 = stringArrayList3.get(i);
                final String s4 = stringArrayList.get(i);
                if (Security.verifyPurchase(this.mSignatureBase64, s2, s3)) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Sku is owned: ");
                    sb6.append(s4);
                    this.logDebug(sb6.toString());
                    final Purchase purchase = new Purchase(s, s2, s3);
                    if (TextUtils.isEmpty((CharSequence)purchase.getToken())) {
                        this.logWarn("BUG: empty/null token!");
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("Purchase data: ");
                        sb7.append(s2);
                        this.logDebug(sb7.toString());
                    }
                    inventory.addPurchase(purchase);
                }
                else {
                    this.logWarn("Purchase signature verification **FAILED**. Not adding item.");
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("   Purchase data: ");
                    sb8.append(s2);
                    this.logDebug(sb8.toString());
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append("   Signature: ");
                    sb9.append(s3);
                    this.logDebug(sb9.toString());
                    b2 = true;
                }
            }
            string = purchases.getString("INAPP_CONTINUATION_TOKEN");
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Continuation token: ");
            sb10.append(string);
            this.logDebug(sb10.toString());
            if (TextUtils.isEmpty((CharSequence)string)) {
                int n = b ? 1 : 0;
                if (b2) {
                    n = -1003;
                }
                return n;
            }
        }
    }
    
    int querySkuDetails(final String s, final Inventory inventory, final List<String> list) throws RemoteException, JSONException {
        this.logDebug("Querying SKU details.");
        if (this.mService == null || this.mContext == null) {
            return -1012;
        }
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.addAll(inventory.getAllOwnedSkus(s));
        if (list != null) {
            for (final String s2 : list) {
                if (!list2.contains(s2)) {
                    list2.add(s2);
                }
            }
        }
        if (list2.size() == 0) {
            this.logDebug("queryPrices: nothing to do because there are no SKUs.");
            return 0;
        }
        final ArrayList<ArrayList<String>> list3 = new ArrayList<ArrayList<String>>();
        final int n = list2.size() / 20;
        final int n2 = list2.size() % 20;
        for (int i = 0; i < n; ++i) {
            final ArrayList<String> list4 = new ArrayList<String>();
            final int n3 = i * 20;
            final Iterator<String> iterator2 = list2.subList(n3, n3 + 20).iterator();
            while (iterator2.hasNext()) {
                list4.add(iterator2.next());
            }
            list3.add(list4);
        }
        if (n2 != 0) {
            final ArrayList<String> list5 = new ArrayList<String>();
            final int n4 = n * 20;
            final Iterator<String> iterator3 = list2.subList(n4, n2 + n4).iterator();
            while (iterator3.hasNext()) {
                list5.add(iterator3.next());
            }
            list3.add(list5);
        }
        for (final ArrayList<String> list6 : list3) {
            final Bundle bundle = new Bundle();
            bundle.putStringArrayList("ITEM_ID_LIST", (ArrayList)list6);
            final Bundle skuDetails = this.mService.getSkuDetails(3, this.mContext.getPackageName(), s, bundle);
            if (!skuDetails.containsKey("DETAILS_LIST")) {
                final int responseCodeFromBundle = this.getResponseCodeFromBundle(skuDetails);
                if (responseCodeFromBundle != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getSkuDetails() failed: ");
                    sb.append(getResponseDesc(responseCodeFromBundle));
                    this.logDebug(sb.toString());
                    return responseCodeFromBundle;
                }
                this.logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
                return -1002;
            }
            else {
                final Iterator iterator5 = skuDetails.getStringArrayList("DETAILS_LIST").iterator();
                while (iterator5.hasNext()) {
                    final SkuDetails skuDetails2 = new SkuDetails(s, iterator5.next());
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Got sku details: ");
                    sb2.append(skuDetails2);
                    this.logDebug(sb2.toString());
                    inventory.addSkuDetails(skuDetails2);
                }
            }
        }
        return 0;
    }
    
    public void startSetup(final OnIabSetupFinishedListener onIabSetupFinishedListener) {
        this.checkNotDisposed();
        if (!this.mSetupDone) {
            this.logDebug("Starting in-app billing setup.");
            this.mServiceConn = (ServiceConnection)new ServiceConnection() {
                public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                    if (IabHelper.this.mDisposed) {
                        return;
                    }
                    IabHelper.this.logDebug("Billing service connected.");
                    IabHelper.this.mService = IInAppBillingService.Stub.asInterface(binder);
                    final String packageName = IabHelper.this.mContext.getPackageName();
                    try {
                        IabHelper.this.logDebug("Checking for in-app billing 3 support.");
                        final int billingSupported = IabHelper.this.mService.isBillingSupported(3, packageName, "inapp");
                        if (billingSupported != 0) {
                            if (onIabSetupFinishedListener != null) {
                                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(billingSupported, "Error checking for billing v3 support."));
                            }
                            IabHelper.this.mSubscriptionsSupported = false;
                            IabHelper.this.mSubscriptionUpdateSupported = false;
                            return;
                        }
                        final IabHelper this$0 = IabHelper.this;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("In-app billing version 3 supported for ");
                        sb.append(packageName);
                        this$0.logDebug(sb.toString());
                        if (IabHelper.this.mService.isBillingSupported(5, packageName, "subs") == 0) {
                            IabHelper.this.logDebug("Subscription re-signup AVAILABLE.");
                            IabHelper.this.mSubscriptionUpdateSupported = true;
                        }
                        else {
                            IabHelper.this.logDebug("Subscription re-signup not available.");
                            IabHelper.this.mSubscriptionUpdateSupported = false;
                        }
                        Label_0320: {
                            IabHelper iabHelper;
                            if (IabHelper.this.mSubscriptionUpdateSupported) {
                                iabHelper = IabHelper.this;
                            }
                            else {
                                final int billingSupported2 = IabHelper.this.mService.isBillingSupported(3, packageName, "subs");
                                if (billingSupported2 != 0) {
                                    final IabHelper this$2 = IabHelper.this;
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Subscriptions NOT AVAILABLE. Response: ");
                                    sb2.append(billingSupported2);
                                    this$2.logDebug(sb2.toString());
                                    IabHelper.this.mSubscriptionsSupported = false;
                                    IabHelper.this.mSubscriptionUpdateSupported = false;
                                    break Label_0320;
                                }
                                IabHelper.this.logDebug("Subscriptions AVAILABLE.");
                                iabHelper = IabHelper.this;
                            }
                            iabHelper.mSubscriptionsSupported = true;
                        }
                        IabHelper.this.mSetupDone = true;
                        final OnIabSetupFinishedListener val$listener = onIabSetupFinishedListener;
                        if (val$listener != null) {
                            val$listener.onIabSetupFinished(new IabResult(0, "Setup successful."));
                        }
                    }
                    catch (RemoteException ex) {
                        final OnIabSetupFinishedListener val$listener2 = onIabSetupFinishedListener;
                        if (val$listener2 != null) {
                            val$listener2.onIabSetupFinished(new IabResult(-1001, "RemoteException while setting up in-app billing."));
                        }
                        ex.printStackTrace();
                    }
                }
                
                public void onServiceDisconnected(final ComponentName componentName) {
                    IabHelper.this.logDebug("Billing service disconnected.");
                    IabHelper.this.mService = null;
                }
            };
            final Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            final List queryIntentServices = this.mContext.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
                if (!(this.mIsServiceBound = this.mContext.bindService(intent, this.mServiceConn, 1))) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Problem in binding service intent '");
                    sb.append(intent.getAction());
                    sb.append("', this is unexpected.");
                    this.logDebug(sb.toString());
                }
            }
            else if (onIabSetupFinishedListener != null) {
                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(3, "Billing service unavailable on device."));
            }
            return;
        }
        throw new IllegalStateException("IAB helper is already set up.");
    }
    
    public boolean subscriptionsSupported() {
        this.checkNotDisposed();
        return this.mSubscriptionsSupported;
    }
    
    public static class IabAsyncInProgressException extends Exception
    {
        public IabAsyncInProgressException(final String s) {
            super(s);
        }
    }
    
    public interface OnConsumeFinishedListener
    {
        void onConsumeFinished(final Purchase p0, final IabResult p1);
    }
    
    public interface OnConsumeMultiFinishedListener
    {
        void onConsumeMultiFinished(final List<Purchase> p0, final List<IabResult> p1);
    }
    
    public interface OnIabPurchaseFinishedListener
    {
        void onIabPurchaseFinished(final IabResult p0, final Purchase p1);
    }
    
    public interface OnIabSetupFinishedListener
    {
        void onIabSetupFinished(final IabResult p0);
    }
    
    public interface QueryInventoryFinishedListener
    {
        void onQueryInventoryFinished(final IabResult p0, final Inventory p1);
    }
}
