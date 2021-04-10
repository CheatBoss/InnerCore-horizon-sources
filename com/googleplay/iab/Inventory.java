package com.googleplay.iab;

import java.util.*;

public class Inventory
{
    Map<String, Purchase> mPurchaseMap;
    Map<String, SkuDetails> mSkuMap;
    
    Inventory() {
        this.mSkuMap = new HashMap<String, SkuDetails>();
        this.mPurchaseMap = new HashMap<String, Purchase>();
    }
    
    void addPurchase(final Purchase purchase) {
        this.mPurchaseMap.put(purchase.getSku(), purchase);
    }
    
    void addSkuDetails(final SkuDetails skuDetails) {
        this.mSkuMap.put(skuDetails.getSku(), skuDetails);
    }
    
    public void erasePurchase(final String s) {
        if (this.mPurchaseMap.containsKey(s)) {
            this.mPurchaseMap.remove(s);
        }
    }
    
    List<String> getAllOwnedSkus() {
        return new ArrayList<String>(this.mPurchaseMap.keySet());
    }
    
    List<String> getAllOwnedSkus(final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final Purchase purchase : this.mPurchaseMap.values()) {
            if (purchase.getItemType().equals(s)) {
                list.add(purchase.getSku());
            }
        }
        return list;
    }
    
    public List<Purchase> getAllPurchases() {
        return new ArrayList<Purchase>(this.mPurchaseMap.values());
    }
    
    public Purchase getPurchase(final String s) {
        return this.mPurchaseMap.get(s);
    }
    
    public SkuDetails getSkuDetails(final String s) {
        return this.mSkuMap.get(s);
    }
    
    public boolean hasDetails(final String s) {
        return this.mSkuMap.containsKey(s);
    }
    
    public boolean hasPurchase(final String s) {
        return this.mPurchaseMap.containsKey(s);
    }
}
