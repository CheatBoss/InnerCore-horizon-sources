package com.googleplay.iab;

import org.json.*;

public class SkuDetails
{
    private final String mCurrencyCode;
    private final String mDescription;
    private final String mItemType;
    private final String mJson;
    private final String mPrice;
    private final long mPriceAmountMicros;
    private final String mPriceCurrencyCode;
    private final String mSku;
    private final String mTitle;
    private final String mType;
    private final String mUnformattedPrice;
    
    public SkuDetails(final String s) throws JSONException {
        this("inapp", s);
    }
    
    public SkuDetails(final String mItemType, final String mJson) throws JSONException {
        this.mItemType = mItemType;
        this.mJson = mJson;
        final JSONObject jsonObject = new JSONObject(this.mJson);
        this.mSku = jsonObject.optString("productId");
        this.mType = jsonObject.optString("type");
        this.mPrice = jsonObject.optString("price");
        this.mPriceAmountMicros = jsonObject.optLong("price_amount_micros");
        this.mPriceCurrencyCode = jsonObject.optString("price_currency_code");
        this.mTitle = jsonObject.optString("title");
        this.mDescription = jsonObject.optString("description");
        this.mCurrencyCode = jsonObject.optString("price_currency_code");
        final StringBuilder sb = new StringBuilder();
        final long optLong = jsonObject.optLong("price_amount_micros");
        if (optLong != 0L) {
            final long n = optLong / 1000000L;
            final long n2 = optLong - 1000000L * n;
            sb.append(Long.toString(n));
            if (n2 >= 0L) {
                sb.append(".");
                sb.append(Long.toString(n2));
            }
        }
        this.mUnformattedPrice = sb.toString();
    }
    
    public String getCurrencyCode() {
        return this.mCurrencyCode;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
    
    public String getPrice() {
        return this.mPrice;
    }
    
    public long getPriceAmountMicros() {
        return this.mPriceAmountMicros;
    }
    
    public String getPriceCurrencyCode() {
        return this.mPriceCurrencyCode;
    }
    
    public String getSku() {
        return this.mSku;
    }
    
    public String getTitle() {
        return this.mTitle;
    }
    
    public String getType() {
        return this.mType;
    }
    
    public String getUnformattedPrice() {
        return this.mUnformattedPrice;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SkuDetails:");
        sb.append(this.mJson);
        return sb.toString();
    }
}
