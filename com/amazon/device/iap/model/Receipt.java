package com.amazon.device.iap.model;

import java.util.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;

public final class Receipt
{
    private static final String CANCEL_DATE = "endDate";
    private static final Date DATE_CANCELED;
    private static final String PRODUCT_TYPE = "itemType";
    private static final String PURCHASE_DATE = "purchaseDate";
    private static final String RECEIPT_ID = "receiptId";
    private static final String SKU = "sku";
    private final Date cancelDate;
    private final ProductType productType;
    private final Date purchaseDate;
    private final String receiptId;
    private final String sku;
    
    static {
        DATE_CANCELED = new Date(1L);
    }
    
    public Receipt(final ReceiptBuilder receiptBuilder) {
        d.a((Object)receiptBuilder.getSku(), "sku");
        d.a(receiptBuilder.getProductType(), "productType");
        if (ProductType.SUBSCRIPTION == receiptBuilder.getProductType()) {
            d.a(receiptBuilder.getPurchaseDate(), "purchaseDate");
        }
        this.receiptId = receiptBuilder.getReceiptId();
        this.sku = receiptBuilder.getSku();
        this.productType = receiptBuilder.getProductType();
        this.purchaseDate = receiptBuilder.getPurchaseDate();
        this.cancelDate = receiptBuilder.getCancelDate();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final Receipt receipt = (Receipt)o;
        final Date cancelDate = this.cancelDate;
        if (cancelDate == null) {
            if (receipt.cancelDate != null) {
                return false;
            }
        }
        else if (!cancelDate.equals(receipt.cancelDate)) {
            return false;
        }
        if (this.productType != receipt.productType) {
            return false;
        }
        final Date purchaseDate = this.purchaseDate;
        if (purchaseDate == null) {
            if (receipt.purchaseDate != null) {
                return false;
            }
        }
        else if (!purchaseDate.equals(receipt.purchaseDate)) {
            return false;
        }
        final String receiptId = this.receiptId;
        if (receiptId == null) {
            if (receipt.receiptId != null) {
                return false;
            }
        }
        else if (!receiptId.equals(receipt.receiptId)) {
            return false;
        }
        final String sku = this.sku;
        if (sku == null) {
            if (receipt.sku != null) {
                return false;
            }
        }
        else if (!sku.equals(receipt.sku)) {
            return false;
        }
        return true;
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
    
    @Override
    public int hashCode() {
        final Date cancelDate = this.cancelDate;
        int hashCode = 0;
        int hashCode2;
        if (cancelDate == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = cancelDate.hashCode();
        }
        final ProductType productType = this.productType;
        int hashCode3;
        if (productType == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = productType.hashCode();
        }
        final Date purchaseDate = this.purchaseDate;
        int hashCode4;
        if (purchaseDate == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = purchaseDate.hashCode();
        }
        final String receiptId = this.receiptId;
        int hashCode5;
        if (receiptId == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = receiptId.hashCode();
        }
        final String sku = this.sku;
        if (sku != null) {
            hashCode = sku.hashCode();
        }
        return ((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode;
    }
    
    public boolean isCanceled() {
        return this.cancelDate != null;
    }
    
    public JSONObject toJSON() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("receiptId", (Object)this.receiptId);
            jsonObject.put("sku", (Object)this.sku);
            jsonObject.put("itemType", (Object)this.productType);
            jsonObject.put("purchaseDate", (Object)this.purchaseDate);
            jsonObject.put("endDate", (Object)this.cancelDate);
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    @Override
    public String toString() {
        try {
            return this.toJSON().toString(4);
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
