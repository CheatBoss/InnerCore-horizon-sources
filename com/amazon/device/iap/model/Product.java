package com.amazon.device.iap.model;

import android.os.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;

public final class Product implements Parcelable
{
    public static final Parcelable$Creator<Product> CREATOR;
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String PRODUCT_TYPE = "productType";
    private static final String SKU = "sku";
    private static final String SMALL_ICON_URL = "smallIconUrl";
    private static final String TITLE = "title";
    private final String description;
    private final String price;
    private final ProductType productType;
    private final String sku;
    private final String smallIconUrl;
    private final String title;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Product>() {
            public Product createFromParcel(final Parcel parcel) {
                return new Product(parcel, null);
            }
            
            public Product[] newArray(final int n) {
                return new Product[n];
            }
        };
    }
    
    private Product(final Parcel parcel) {
        this.sku = parcel.readString();
        this.productType = ProductType.valueOf(parcel.readString());
        this.description = parcel.readString();
        this.price = parcel.readString();
        this.smallIconUrl = parcel.readString();
        this.title = parcel.readString();
    }
    
    public Product(final ProductBuilder productBuilder) {
        d.a((Object)productBuilder.getSku(), "sku");
        d.a(productBuilder.getProductType(), "productType");
        d.a((Object)productBuilder.getDescription(), "description");
        d.a((Object)productBuilder.getTitle(), "title");
        d.a((Object)productBuilder.getSmallIconUrl(), "smallIconUrl");
        if (ProductType.SUBSCRIPTION != productBuilder.getProductType()) {
            d.a((Object)productBuilder.getPrice(), "price");
        }
        this.sku = productBuilder.getSku();
        this.productType = productBuilder.getProductType();
        this.description = productBuilder.getDescription();
        this.price = productBuilder.getPrice();
        this.smallIconUrl = productBuilder.getSmallIconUrl();
        this.title = productBuilder.getTitle();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public ProductType getProductType() {
        return this.productType;
    }
    
    public String getSku() {
        return this.sku;
    }
    
    public String getSmallIconUrl() {
        return this.smallIconUrl;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public JSONObject toJSON() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("sku", (Object)this.sku);
        jsonObject.put("productType", (Object)this.productType);
        jsonObject.put("description", (Object)this.description);
        jsonObject.put("price", (Object)this.price);
        jsonObject.put("smallIconUrl", (Object)this.smallIconUrl);
        jsonObject.put("title", (Object)this.title);
        return jsonObject;
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
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.sku);
        parcel.writeString(this.productType.toString());
        parcel.writeString(this.description);
        parcel.writeString(this.price);
        parcel.writeString(this.smallIconUrl);
        parcel.writeString(this.title);
    }
}
