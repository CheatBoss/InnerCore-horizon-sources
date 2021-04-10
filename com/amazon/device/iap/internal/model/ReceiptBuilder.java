package com.amazon.device.iap.internal.model;

import java.util.*;
import com.amazon.device.iap.model.*;

public class ReceiptBuilder
{
    private Date cancelDate;
    private ProductType productType;
    private Date purchaseDate;
    private String receiptId;
    private String sku;
    
    public Receipt build() {
        return new Receipt(this);
    }
    
    public Date getCancelDate() {
        return this.cancelDate;
    }
    
    public ProductType getProductType() {
        return this.productType;
    }
    
    public Date getPurchaseDate() {
        return this.purchaseDate;
    }
    
    public String getReceiptId() {
        return this.receiptId;
    }
    
    public String getSku() {
        return this.sku;
    }
    
    public ReceiptBuilder setCancelDate(final Date cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }
    
    public ReceiptBuilder setProductType(final ProductType productType) {
        this.productType = productType;
        return this;
    }
    
    public ReceiptBuilder setPurchaseDate(final Date purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }
    
    public ReceiptBuilder setReceiptId(final String receiptId) {
        this.receiptId = receiptId;
        return this;
    }
    
    public ReceiptBuilder setSku(final String sku) {
        this.sku = sku;
        return this;
    }
}
