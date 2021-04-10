package com.appboy.models.cards;

import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public final class BannerImageCard extends Card
{
    private final String a;
    private final String b;
    private final String c;
    private final float d;
    
    public BannerImageCard(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public BannerImageCard(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        super(jsonObject, provider, bl, dn, c);
        this.a = jsonObject.getString(provider.getKey(CardKey.BANNER_IMAGE_IMAGE));
        this.b = ec.a(jsonObject, provider.getKey(CardKey.BANNER_IMAGE_URL));
        this.c = ec.a(jsonObject, provider.getKey(CardKey.BANNER_IMAGE_DOMAIN));
        this.d = (float)jsonObject.optDouble(provider.getKey(CardKey.BANNER_IMAGE_ASPECT_RATIO), 0.0);
    }
    
    public float getAspectRatio() {
        return this.d;
    }
    
    @Override
    public CardType getCardType() {
        return CardType.BANNER;
    }
    
    public String getDomain() {
        return this.c;
    }
    
    public String getImageUrl() {
        return this.a;
    }
    
    @Override
    public String getUrl() {
        return this.b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BannerImageCard{");
        sb.append(super.toString());
        sb.append(", mImageUrl='");
        sb.append(this.a);
        sb.append('\'');
        sb.append(", mUrl='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mDomain='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mAspectRatio='");
        sb.append(this.d);
        sb.append('\'');
        sb.append("}");
        return sb.toString();
    }
}
