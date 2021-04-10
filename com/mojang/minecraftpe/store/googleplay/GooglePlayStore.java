package com.mojang.minecraftpe.store.googleplay;

import com.mojang.minecraftpe.*;
import android.provider.*;
import com.googleplay.licensing.*;
import android.util.*;
import org.json.*;
import android.content.*;
import android.app.*;
import android.net.*;
import com.mojang.minecraftpe.store.*;
import com.googleplay.iab.*;
import java.util.*;

public class GooglePlayStore extends BroadcastReceiver implements Store, ActivityListener
{
    static final String IAB_BROADCAST_ACTION = "com.android.vending.billing.PURCHASES_UPDATED";
    private static final byte[] SALT;
    private static boolean mReceivedLicenseResponse = false;
    private static boolean mVerifiedLicense = true;
    MainActivity mActivity;
    private LicenseChecker mChecker;
    IabHelper mIabHelper;
    private LicenseCheckerCallback mLicenseCheckerCallback;
    StoreListener mListener;
    private ServerManagedPolicy mPolicy;
    int mPurchaseRequestCode;
    
    static {
        SALT = new byte[] { 75, 1, -16, -127, 42, 49, 19, -102, -88, 56, 121, 99, 23, -24, -18, -111, -11, 33, -62, 87 };
    }
    
    public GooglePlayStore(final MainActivity mActivity, final String s, final StoreListener mListener) {
        this.mListener = mListener;
        (this.mActivity = mActivity).addListener(this);
        this.mPurchaseRequestCode = MainActivity.RESULT_GOOGLEPLAY_PURCHASE;
        this.mIabHelper = new IabHelper((Context)this.mActivity, s);
        this.mPolicy = new ServerManagedPolicy((Context)mActivity, new AESObfuscator(GooglePlayStore.SALT, this.mActivity.getPackageName(), Settings$Secure.getString(this.mActivity.getContentResolver(), "android_id")));
        this.mLicenseCheckerCallback = new MinecraftLicenseCheckerCallback();
        (this.mChecker = new LicenseChecker((Context)mActivity, this.mPolicy, s)).checkAccess(this.mLicenseCheckerCallback);
        if (this.mActivity.isEduMode()) {
            GooglePlayStore.mReceivedLicenseResponse = true;
            GooglePlayStore.mVerifiedLicense = true;
        }
        this.mIabHelper.startSetup((IabHelper.OnIabSetupFinishedListener)new IabHelper.OnIabSetupFinishedListener() {
            final /* synthetic */ BroadcastReceiver val$iabBroadcastReceiver;
            
            @Override
            public void onIabSetupFinished(final IabResult iabResult) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onIabSetupFinished: ");
                sb.append(iabResult.getResponse());
                sb.append(", ");
                sb.append(iabResult.getMessage());
                Log.i("GooglePlayStore", sb.toString());
                GooglePlayStore.this.mActivity.registerReceiver(this.val$iabBroadcastReceiver, new IntentFilter("com.android.vending.billing.PURCHASES_UPDATED"));
                GooglePlayStore.this.mListener.onStoreInitialized(iabResult.isSuccess());
            }
        });
    }
    
    private String createReceipt(final Purchase purchase) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemtype", (Object)purchase.getItemType());
            jsonObject.put("originaljson", (Object)purchase.getOriginalJson());
            jsonObject.put("signature", (Object)purchase.getSignature());
            return jsonObject.toString();
        }
        catch (JSONException ex) {
            throw new RuntimeException((Throwable)ex);
        }
    }
    
    private Purchase parseReceipt(final String s) {
        try {
            final JSONObject jsonObject = new JSONObject(s);
            return new Purchase(jsonObject.getString("itemtype"), jsonObject.getString("originaljson"), jsonObject.getString("signature"));
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public void acknowledgePurchase(final String s, final String s2) {
        if (s2.equals("Consumable")) {
            this.mActivity.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Purchase access$400 = GooglePlayStore.this.parseReceipt(s);
                    if (access$400 != null) {
                        try {
                            GooglePlayStore.this.mIabHelper.consumeAsync(access$400, null);
                        }
                        catch (IabHelper.IabAsyncInProgressException ex) {
                            Log.i("GooglePlayStore", "Error consume purchase. Another async operation in progress");
                        }
                    }
                }
            });
        }
    }
    
    public void destructor() {
        this.onDestroy();
    }
    
    public ExtraLicenseResponseData getExtraLicenseData() {
        final long[] extraLicenseData = this.mPolicy.getExtraLicenseData();
        return new ExtraLicenseResponseData(extraLicenseData[0], extraLicenseData[1], extraLicenseData[2]);
    }
    
    public String getProductSkuPrefix() {
        return "";
    }
    
    public String getRealmsSkuPrefix() {
        return "";
    }
    
    public String getStoreId() {
        return "android.googleplay";
    }
    
    public boolean hasVerifiedLicense() {
        return GooglePlayStore.mVerifiedLicense;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        final IabHelper mIabHelper = this.mIabHelper;
        if (mIabHelper != null) {
            mIabHelper.handleActivityResult(n, n2, intent);
        }
    }
    
    public void onDestroy() {
        this.mActivity.removeListener(this);
        final IabHelper mIabHelper = this.mIabHelper;
        if (mIabHelper != null) {
            try {
                mIabHelper.dispose();
            }
            catch (IabHelper.IabAsyncInProgressException ex) {
                Log.i("GooglePlayStore", "Error destroying iabhelper. Another async operation in progress.");
            }
        }
        this.mIabHelper = null;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        Log.i("GooglePlayStore", "IabBroadcastReceiver received message PURCHASES_UPDATED");
        this.queryPurchases();
    }
    
    public void onResume() {
    }
    
    public void onStop() {
    }
    
    public void purchase(final String s, final boolean b, final String s2) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                String s;
                if (b) {
                    final IabHelper mIabHelper = GooglePlayStore.this.mIabHelper;
                    s = "subs";
                }
                else {
                    final IabHelper mIabHelper2 = GooglePlayStore.this.mIabHelper;
                    s = "inapp";
                }
                try {
                    GooglePlayStore.this.mIabHelper.launchPurchaseFlow((Activity)GooglePlayStore.this.mActivity, s, s, null, GooglePlayStore.this.mPurchaseRequestCode, (IabHelper.OnIabPurchaseFinishedListener)new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(final IabResult iabResult, final Purchase purchase) {
                            if (iabResult.isSuccess()) {
                                GooglePlayStore.this.mListener.onPurchaseSuccessful(s, GooglePlayStore.this.createReceipt(purchase));
                                return;
                            }
                            if (iabResult.getResponse() == -1005) {
                                GooglePlayStore.this.mListener.onPurchaseCanceled(s);
                                return;
                            }
                            GooglePlayStore.this.mListener.onPurchaseFailed(s);
                        }
                    }, s2);
                }
                catch (IabHelper.IabAsyncInProgressException ex) {
                    Log.i("GooglePlayStore", "Error purchasing product. Another async operation in progress.");
                }
            }
        });
    }
    
    public void purchaseGame() {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://market.android.com/details?id=");
        sb.append(this.mActivity.getPackageName());
        this.mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
    }
    
    public void queryProducts(final String[] array) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    GooglePlayStore.this.mIabHelper.queryInventoryAsync(true, Arrays.asList(array), Arrays.asList(array), (IabHelper.QueryInventoryFinishedListener)new IabHelper.QueryInventoryFinishedListener() {
                        @Override
                        public void onQueryInventoryFinished(final IabResult iabResult, final Inventory inventory) {
                            if (iabResult.isSuccess()) {
                                final ArrayList<Product> list = new ArrayList<Product>();
                                if (iabResult.isSuccess()) {
                                    final String[] val$productIds = array;
                                    for (int length = val$productIds.length, i = 0; i < length; ++i) {
                                        final String s = val$productIds[i];
                                        final SkuDetails skuDetails = inventory.getSkuDetails(s);
                                        if (skuDetails != null) {
                                            list.add(new Product(s, skuDetails.getPrice(), skuDetails.getCurrencyCode(), skuDetails.getUnformattedPrice()));
                                        }
                                    }
                                }
                                GooglePlayStore.this.mListener.onQueryProductsSuccess(list.toArray(new Product[0]));
                                return;
                            }
                            GooglePlayStore.this.mListener.onQueryProductsFail();
                        }
                    });
                }
                catch (IabHelper.IabAsyncInProgressException ex) {
                    Log.i("GooglePlayStore", "Error querying products. Another async operation in progress");
                }
            }
        });
    }
    
    public void queryPurchases() {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    GooglePlayStore.this.mIabHelper.queryInventoryAsync((IabHelper.QueryInventoryFinishedListener)new IabHelper.QueryInventoryFinishedListener() {
                        @Override
                        public void onQueryInventoryFinished(final IabResult iabResult, final Inventory inventory) {
                            if (iabResult.isSuccess()) {
                                final ArrayList<com.mojang.minecraftpe.store.Purchase> list = new ArrayList<com.mojang.minecraftpe.store.Purchase>();
                                if (iabResult.isSuccess()) {
                                    for (final Purchase purchase : inventory.getAllPurchases()) {
                                        list.add(new com.mojang.minecraftpe.store.Purchase(purchase.getSku(), GooglePlayStore.this.createReceipt(purchase), purchase.getPurchaseState() == 0));
                                    }
                                }
                                GooglePlayStore.this.mListener.onQueryPurchasesSuccess(list.toArray(new com.mojang.minecraftpe.store.Purchase[0]));
                                return;
                            }
                            GooglePlayStore.this.mListener.onQueryPurchasesFail();
                        }
                    });
                }
                catch (IabHelper.IabAsyncInProgressException ex) {
                    Log.i("GooglePlayStore", "Error querying purchases. Another async operation in progress.");
                }
            }
        });
    }
    
    public boolean receivedLicenseResponse() {
        return GooglePlayStore.mReceivedLicenseResponse;
    }
    
    private class MinecraftLicenseCheckerCallback implements LicenseCheckerCallback
    {
        @Override
        public void allow(final int n) {
            String s;
            if (n == 291) {
                s = new String("RETRY");
            }
            else if (n == 256) {
                s = new String("LICENSED");
            }
            else {
                s = new String("UNKNOWN REASON");
            }
            GooglePlayStore.mReceivedLicenseResponse = true;
            Log.i("MinecraftLicenseCheckerCallback", String.format("allowed reason: %s", s));
            GooglePlayStore.mVerifiedLicense = true;
        }
        
        @Override
        public void applicationError(final int n) {
            Log.i("MinecraftLicenseCheckerCallback", String.format("error: %d", n));
        }
        
        @Override
        public void dontAllow(final int n) {
            String s;
            if (n == 561) {
                s = new String("NOT LICENSED");
            }
            else if (n == 291) {
                s = new String("RETRY");
            }
            else {
                s = new String("UNKNOWN REASON");
            }
            GooglePlayStore.mReceivedLicenseResponse = true;
            Log.i("MinecraftLicenseCheckerCallback", String.format("denied reason: %s", s));
            GooglePlayStore.mVerifiedLicense = false;
        }
    }
}
