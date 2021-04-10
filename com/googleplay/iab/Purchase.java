package com.googleplay.iab;

import org.json.*;

public class Purchase
{
    String mDeveloperPayload;
    boolean mIsAutoRenewing;
    String mItemType;
    String mOrderId;
    String mOriginalJson;
    String mPackageName;
    int mPurchaseState;
    long mPurchaseTime;
    String mSignature;
    String mSku;
    String mToken;
    
    public Purchase(final String mItemType, final String mOriginalJson, final String mSignature) throws JSONException {
        this.mItemType = mItemType;
        this.mOriginalJson = mOriginalJson;
        final JSONObject jsonObject = new JSONObject(this.mOriginalJson);
        this.mOrderId = jsonObject.optString("orderId");
        this.mPackageName = jsonObject.optString("packageName");
        this.mSku = jsonObject.optString("productId");
        this.mPurchaseTime = jsonObject.optLong("purchaseTime");
        this.mPurchaseState = jsonObject.optInt("purchaseState");
        this.mDeveloperPayload = jsonObject.optString("developerPayload");
        this.mToken = jsonObject.optString("token", jsonObject.optString("purchaseToken"));
        this.mIsAutoRenewing = jsonObject.optBoolean("autoRenewing");
        this.mSignature = mSignature;
    }
    
    public String getDeveloperPayload() {
        return this.mDeveloperPayload;
    }
    
    public String getItemType() {
        return this.mItemType;
    }
    
    public String getOrderId() {
        return this.mOrderId;
    }
    
    public String getOriginalJson() {
        return this.mOriginalJson;
    }
    
    public String getPackageName() {
        return this.mPackageName;
    }
    
    public int getPurchaseState() {
        return this.mPurchaseState;
    }
    
    public long getPurchaseTime() {
        return this.mPurchaseTime;
    }
    
    public String getSignature() {
        return this.mSignature;
    }
    
    public String getSku() {
        return this.mSku;
    }
    
    public String getToken() {
        return this.mToken;
    }
    
    public boolean isAutoRenewing() {
        return this.mIsAutoRenewing;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PurchaseInfo(type:");
        sb.append(this.mItemType);
        sb.append("):");
        sb.append(this.mOriginalJson);
        return sb.toString();
    }
}
