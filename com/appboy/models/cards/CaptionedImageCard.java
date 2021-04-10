package com.appboy.models.cards;

import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public final class CaptionedImageCard extends Card
{
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final float f;
    
    public CaptionedImageCard(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public CaptionedImageCard(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        super(jsonObject, provider, bl, dn, c);
        this.a = jsonObject.getString(provider.getKey(CardKey.CAPTIONED_IMAGE_IMAGE));
        this.b = jsonObject.getString(provider.getKey(CardKey.CAPTIONED_IMAGE_TITLE));
        this.c = jsonObject.getString(provider.getKey(CardKey.CAPTIONED_IMAGE_DESCRIPTION));
        this.d = ec.a(jsonObject, provider.getKey(CardKey.CAPTIONED_IMAGE_URL));
        this.e = ec.a(jsonObject, provider.getKey(CardKey.CAPTIONED_IMAGE_DOMAIN));
        this.f = (float)jsonObject.optDouble(provider.getKey(CardKey.CAPTIONED_IMAGE_ASPECT_RATIO), 0.0);
    }
    
    public float getAspectRatio() {
        return this.f;
    }
    
    @Override
    public CardType getCardType() {
        return CardType.CAPTIONED_IMAGE;
    }
    
    public String getDescription() {
        return this.c;
    }
    
    public String getDomain() {
        return this.e;
    }
    
    public String getImageUrl() {
        return this.a;
    }
    
    public String getTitle() {
        return this.b;
    }
    
    @Override
    public String getUrl() {
        return this.d;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CaptionedImageCard{");
        sb.append(super.toString());
        sb.append(", mImageUrl='");
        sb.append(this.a);
        sb.append('\'');
        sb.append(", mTitle='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mDescription='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mUrl='");
        sb.append(this.d);
        sb.append('\'');
        sb.append(", mDomain='");
        sb.append(this.e);
        sb.append('\'');
        sb.append(", mAspectRatio='");
        sb.append(this.f);
        sb.append('\'');
        sb.append("}");
        return sb.toString();
    }
}
