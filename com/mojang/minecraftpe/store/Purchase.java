package com.mojang.minecraftpe.store;

public class Purchase
{
    public String mProductId;
    public boolean mPurchaseActive;
    public String mReceipt;
    
    public Purchase(final String mProductId, final String mReceipt, final boolean mPurchaseActive) {
        this.mProductId = mProductId;
        this.mReceipt = mReceipt;
        this.mPurchaseActive = mPurchaseActive;
    }
}
