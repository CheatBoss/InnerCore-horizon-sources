package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.*;

public class ProductBuilder
{
    private String description;
    private String price;
    private ProductType productType;
    private String sku;
    private String smallIconUrl;
    private String title;
    
    public Product build() {
        return new Product(this);
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
    
    public ProductBuilder setDescription(final String description) {
        this.description = description;
        return this;
    }
    
    public ProductBuilder setPrice(final String price) {
        this.price = price;
        return this;
    }
    
    public ProductBuilder setProductType(final ProductType productType) {
        this.productType = productType;
        return this;
    }
    
    public ProductBuilder setSku(final String sku) {
        this.sku = sku;
        return this;
    }
    
    public ProductBuilder setSmallIconUrl(final String smallIconUrl) {
        this.smallIconUrl = smallIconUrl;
        return this;
    }
    
    public ProductBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }
}
