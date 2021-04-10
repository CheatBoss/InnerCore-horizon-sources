package com.mojang.minecraftpe.store;

public class NativeStoreListener implements StoreListener
{
    long mStoreListener;
    
    public NativeStoreListener(final long mStoreListener) {
        this.mStoreListener = mStoreListener;
    }
    
    public native void onPurchaseCanceled(final long p0, final String p1);
    
    @Override
    public void onPurchaseCanceled(final String s) {
        this.onPurchaseCanceled(this.mStoreListener, s);
    }
    
    public native void onPurchaseFailed(final long p0, final String p1);
    
    @Override
    public void onPurchaseFailed(final String s) {
        this.onPurchaseFailed(this.mStoreListener, s);
    }
    
    public native void onPurchaseSuccessful(final long p0, final String p1, final String p2);
    
    @Override
    public void onPurchaseSuccessful(final String s, final String s2) {
        this.onPurchaseSuccessful(this.mStoreListener, s, s2);
    }
    
    @Override
    public void onQueryProductsFail() {
        this.onQueryProductsFail(this.mStoreListener);
    }
    
    public native void onQueryProductsFail(final long p0);
    
    public native void onQueryProductsSuccess(final long p0, final Product[] p1);
    
    @Override
    public void onQueryProductsSuccess(final Product[] array) {
        this.onQueryProductsSuccess(this.mStoreListener, array);
    }
    
    @Override
    public void onQueryPurchasesFail() {
        this.onQueryPurchasesFail(this.mStoreListener);
    }
    
    public native void onQueryPurchasesFail(final long p0);
    
    public native void onQueryPurchasesSuccess(final long p0, final Purchase[] p1);
    
    @Override
    public void onQueryPurchasesSuccess(final Purchase[] array) {
        this.onQueryPurchasesSuccess(this.mStoreListener, array);
    }
    
    public native void onStoreInitialized(final long p0, final boolean p1);
    
    @Override
    public void onStoreInitialized(final boolean b) {
        this.onStoreInitialized(this.mStoreListener, b);
    }
}
