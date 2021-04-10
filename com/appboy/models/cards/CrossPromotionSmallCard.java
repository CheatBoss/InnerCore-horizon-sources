package com.appboy.models.cards;

import com.appboy.support.*;
import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public final class CrossPromotionSmallCard extends Card
{
    private static final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private double f;
    private int g;
    private final double h;
    private final String i;
    private String j;
    private String k;
    private AppStore l;
    private String m;
    
    static {
        a = AppboyLogger.getAppboyLogTag(CrossPromotionSmallCard.class);
    }
    
    public CrossPromotionSmallCard(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public CrossPromotionSmallCard(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        super(jsonObject, provider, bl, dn, c);
        this.b = jsonObject.getString("title");
        this.c = jsonObject.getString("subtitle");
        this.d = jsonObject.getString("caption");
        this.e = jsonObject.getString("image");
        try {
            this.f = jsonObject.getDouble("rating");
            this.g = jsonObject.getInt("reviews");
        }
        catch (Exception ex2) {
            this.f = 0.0;
            this.g = 0;
        }
        if (jsonObject.has("package")) {
            this.j = jsonObject.getString("package");
        }
        if (jsonObject.has("kindle_id")) {
            this.k = jsonObject.getString("kindle_id");
        }
        this.h = jsonObject.getDouble("price");
        if (jsonObject.has("display_price")) {
            this.m = jsonObject.getString("display_price");
        }
        this.i = jsonObject.getString("url");
        if (ec.a(jsonObject, "store") != null) {
            try {
                final String a = ec.a(jsonObject, "store");
                if (a != null) {
                    this.l = AppStore.valueOf(AppStore.serverStringToEnumString(a));
                    return;
                }
                this.l = AppStore.GOOGLE_PLAY_STORE;
            }
            catch (Exception ex) {
                AppboyLogger.e(CrossPromotionSmallCard.a, "Caught exception creating cross promotion small card Json.", ex);
                this.l = AppStore.GOOGLE_PLAY_STORE;
            }
        }
    }
    
    public AppStore getAppStore() {
        return this.l;
    }
    
    public String getCaption() {
        return this.d;
    }
    
    @Override
    public CardType getCardType() {
        return CardType.CROSS_PROMOTIONAL;
    }
    
    public String getDisplayPrice() {
        return this.m;
    }
    
    public String getImageUrl() {
        return this.e;
    }
    
    public String getKindleId() {
        return this.k;
    }
    
    public String getPackage() {
        return this.j;
    }
    
    public double getPrice() {
        return this.h;
    }
    
    public double getRating() {
        return this.f;
    }
    
    public int getReviewCount() {
        return this.g;
    }
    
    public String getSubtitle() {
        return this.c;
    }
    
    public String getTitle() {
        return this.b;
    }
    
    @Override
    public String getUrl() {
        return this.i;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CrossPromotionSmallCard{");
        sb.append(super.toString());
        sb.append(", mTitle='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mSubtitle='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mCaption='");
        sb.append(this.d);
        sb.append('\'');
        sb.append(", mImageUrl='");
        sb.append(this.e);
        sb.append('\'');
        sb.append(", mRating=");
        sb.append(this.f);
        sb.append(", mReviewCount=");
        sb.append(this.g);
        sb.append(", mPrice=");
        sb.append(this.h);
        sb.append(", mPackage=");
        sb.append(this.j);
        sb.append(", mUrl='");
        sb.append(this.i);
        sb.append('\'');
        sb.append(", mAppStore='");
        sb.append(this.l);
        sb.append('\'');
        sb.append(", mKindleId='");
        sb.append(this.k);
        sb.append('\'');
        sb.append(", mDisplayPrice='");
        sb.append(this.m);
        sb.append('\'');
        sb.append("}");
        return sb.toString();
    }
}
