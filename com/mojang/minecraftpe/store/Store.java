package com.mojang.minecraftpe.store;

public interface Store
{
    void acknowledgePurchase(final String p0, final String p1);
    
    void destructor();
    
    ExtraLicenseResponseData getExtraLicenseData();
    
    String getProductSkuPrefix();
    
    String getRealmsSkuPrefix();
    
    String getStoreId();
    
    boolean hasVerifiedLicense();
    
    void purchase(final String p0, final boolean p1, final String p2);
    
    void purchaseGame();
    
    void queryProducts(final String[] p0);
    
    void queryPurchases();
    
    boolean receivedLicenseResponse();
}
