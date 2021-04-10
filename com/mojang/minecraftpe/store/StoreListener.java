package com.mojang.minecraftpe.store;

public interface StoreListener
{
    void onPurchaseCanceled(final String p0);
    
    void onPurchaseFailed(final String p0);
    
    void onPurchaseSuccessful(final String p0, final String p1);
    
    void onQueryProductsFail();
    
    void onQueryProductsSuccess(final Product[] p0);
    
    void onQueryPurchasesFail();
    
    void onQueryPurchasesSuccess(final Purchase[] p0);
    
    void onStoreInitialized(final boolean p0);
}
