package com.mojang.minecraftpe.store.amazonappstore;

import android.content.*;
import java.text.*;
import android.util.*;
import com.amazon.device.iap.*;
import org.json.*;
import com.amazon.device.iap.model.*;
import com.mojang.minecraftpe.store.*;
import java.util.*;

public class AmazonAppStore implements Store
{
    private boolean mForFireTV;
    private StoreListener mListener;
    private Map<RequestId, String> mProductIdRequestMapping;
    private PurchasingListener mPurchasingListener;
    private final String subscriptionKey;
    private Currency userCurrency;
    private Locale userLocale;
    
    public AmazonAppStore(final Context context, final StoreListener mListener, final boolean mForFireTV) {
        this.mProductIdRequestMapping = new HashMap<RequestId, String>();
        this.subscriptionKey = ".subscription";
        this.userLocale = null;
        this.userCurrency = null;
        final PurchasingListener mPurchasingListener = new PurchasingListener() {
            @Override
            public void onProductDataResponse(ProductDataResponse currencyCode) {
                if (currencyCode.getRequestStatus() == ProductDataResponse.RequestStatus.SUCCESSFUL) {
                    final ArrayList<Product> list = new ArrayList<Product>();
                    final Set<String> unavailableSkus = currencyCode.getUnavailableSkus();
                    final Map<String, com.amazon.device.iap.model.Product> productData = currencyCode.getProductData();
                    for (final String s : productData.keySet()) {
                        if (!unavailableSkus.contains(s)) {
                            final com.amazon.device.iap.model.Product product = productData.get(s);
                            String replace;
                            if (product.getSku() != null) {
                                replace = product.getSku().replace(".child", "");
                            }
                            else {
                                replace = "";
                            }
                            String price;
                            if (product.getPrice() != null) {
                                price = product.getPrice();
                            }
                            else {
                                price = "";
                            }
                            final Locale access$000 = AmazonAppStore.this.userLocale;
                            final String s2 = "0";
                            String string;
                            if (access$000 != null && AmazonAppStore.this.userCurrency != null) {
                                try {
                                    currencyCode = (ProductDataResponse)AmazonAppStore.this.userCurrency.getCurrencyCode();
                                    try {
                                        string = NumberFormat.getCurrencyInstance(AmazonAppStore.this.userLocale).parse(price).toString();
                                    }
                                    catch (Exception ex) {}
                                }
                                catch (Exception ex) {
                                    currencyCode = (ProductDataResponse)"";
                                }
                                final Exception ex;
                                Log.i("AmazonAppStore", ex.getMessage());
                                string = s2;
                            }
                            else {
                                currencyCode = (ProductDataResponse)"";
                                string = s2;
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("--queryProductsResponse add sku[");
                            sb.append(replace);
                            sb.append("] price[");
                            sb.append(price);
                            sb.append("] currencyCode[");
                            sb.append((String)currencyCode);
                            sb.append("] unformatted[");
                            sb.append(string);
                            sb.append("]");
                            Log.i("AmazonAppStore", sb.toString());
                            list.add(new Product(replace, price, (String)currencyCode, string));
                        }
                    }
                    AmazonAppStore.this.mListener.onQueryProductsSuccess(list.toArray(new Product[0]));
                    return;
                }
                AmazonAppStore.this.mListener.onQueryProductsFail();
            }
            
            @Override
            public void onPurchaseResponse(final PurchaseResponse purchaseResponse) {
                final String s = AmazonAppStore.this.mProductIdRequestMapping.remove(purchaseResponse.getRequestId());
                if (purchaseResponse.getRequestStatus() == PurchaseResponse.RequestStatus.SUCCESSFUL) {
                    AmazonAppStore.this.mListener.onPurchaseSuccessful(s, AmazonAppStore.this.createReceipt(purchaseResponse));
                    return;
                }
                AmazonAppStore.this.mListener.onPurchaseFailed(s);
            }
            
            @Override
            public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse purchaseUpdatesResponse) {
                if (purchaseUpdatesResponse.getRequestStatus() == PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL) {
                    final ArrayList<Purchase> list = new ArrayList<Purchase>();
                    final String userId = purchaseUpdatesResponse.getUserData().getUserId();
                    for (final Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
                        list.add(new Purchase(receipt.getSku(), AmazonAppStore.this.createReceipt(userId, receipt.getReceiptId()), receipt.isCanceled() ^ true));
                    }
                    AmazonAppStore.this.mListener.onQueryPurchasesSuccess(list.toArray(new Purchase[0]));
                    return;
                }
                AmazonAppStore.this.mListener.onQueryPurchasesFail();
            }
            
            @Override
            public void onUserDataResponse(final UserDataResponse userDataResponse) {
                if (userDataResponse != null && userDataResponse.getUserData() != null) {
                    AmazonAppStore.this.userLocale = new Locale(Locale.getDefault().getLanguage(), userDataResponse.getUserData().getMarketplace());
                    final AmazonAppStore this$0 = AmazonAppStore.this;
                    this$0.userCurrency = Currency.getInstance(this$0.userLocale);
                }
            }
        };
        this.mPurchasingListener = mPurchasingListener;
        this.mListener = mListener;
        this.mForFireTV = mForFireTV;
        PurchasingService.registerListener(context, mPurchasingListener);
        mListener.onStoreInitialized(true);
    }
    
    private String createReceipt(final PurchaseResponse purchaseResponse) {
        return this.createReceipt(purchaseResponse.getUserData().getUserId(), purchaseResponse.getReceipt().getReceiptId());
    }
    
    private String createReceipt(final String s, final String s2) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", (Object)s);
            jsonObject.put("receiptId", (Object)s2);
            return jsonObject.toString();
        }
        catch (JSONException ex) {
            throw new RuntimeException((Throwable)ex);
        }
    }
    
    @Override
    public void acknowledgePurchase(final String s, final String s2) {
        try {
            PurchasingService.notifyFulfillment(new JSONObject(s).getString("receiptId"), FulfillmentResult.FULFILLED);
        }
        catch (JSONException ex) {}
    }
    
    @Override
    public void destructor() {
    }
    
    @Override
    public ExtraLicenseResponseData getExtraLicenseData() {
        return new ExtraLicenseResponseData(0L, 0L, 0L);
    }
    
    @Override
    public String getProductSkuPrefix() {
        if (this.mForFireTV) {
            return "firetv.";
        }
        return "";
    }
    
    @Override
    public String getRealmsSkuPrefix() {
        if (this.mForFireTV) {
            return "firetv.";
        }
        return "";
    }
    
    @Override
    public String getStoreId() {
        return "android.amazonappstore";
    }
    
    @Override
    public boolean hasVerifiedLicense() {
        return false;
    }
    
    @Override
    public void purchase(final String s, final boolean b, final String s2) {
        this.mProductIdRequestMapping.put(PurchasingService.purchase(s), s);
    }
    
    @Override
    public void purchaseGame() {
    }
    
    @Override
    public void queryProducts(final String[] array) {
        final String[] array2 = new String[array.length];
        for (int i = 0; i < array.length; ++i) {
            if (array[i].indexOf(".subscription") != -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(array[i]);
                sb.append(".child");
                array2[i] = sb.toString();
            }
            else {
                array2[i] = array[i];
            }
        }
        PurchasingService.getUserData();
        PurchasingService.getProductData(new HashSet<String>(Arrays.asList(array2)));
    }
    
    @Override
    public void queryPurchases() {
        PurchasingService.getPurchaseUpdates(true);
    }
    
    @Override
    public boolean receivedLicenseResponse() {
        return false;
    }
}
