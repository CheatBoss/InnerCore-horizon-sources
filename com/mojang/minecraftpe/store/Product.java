package com.mojang.minecraftpe.store;

public class Product
{
    public String mCurrencyCode;
    public String mId;
    public String mPrice;
    public String mUnformattedPrice;
    
    public Product(final String mId, final String mPrice, final String mCurrencyCode, final String mUnformattedPrice) {
        this.mId = mId;
        this.mPrice = mPrice;
        this.mCurrencyCode = mCurrencyCode;
        this.mUnformattedPrice = mUnformattedPrice;
    }
}
