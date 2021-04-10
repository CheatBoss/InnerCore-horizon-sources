package com.amazon.device.iap;

import com.amazon.device.iap.model.*;

public interface PurchasingListener
{
    void onProductDataResponse(final ProductDataResponse p0);
    
    void onPurchaseResponse(final PurchaseResponse p0);
    
    void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse p0);
    
    void onUserDataResponse(final UserDataResponse p0);
}
