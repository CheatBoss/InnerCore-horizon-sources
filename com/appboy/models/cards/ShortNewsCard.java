package com.appboy.models.cards;

import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public final class ShortNewsCard extends Card
{
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    
    public ShortNewsCard(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public ShortNewsCard(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        super(jsonObject, provider, bl, dn, c);
        this.a = jsonObject.getString(provider.getKey(CardKey.SHORT_NEWS_DESCRIPTION));
        this.b = jsonObject.getString(provider.getKey(CardKey.SHORT_NEWS_IMAGE));
        this.c = ec.a(jsonObject, provider.getKey(CardKey.SHORT_NEWS_TITLE));
        this.d = ec.a(jsonObject, provider.getKey(CardKey.SHORT_NEWS_URL));
        this.e = ec.a(jsonObject, provider.getKey(CardKey.SHORT_NEWS_DOMAIN));
    }
    
    @Override
    public CardType getCardType() {
        return CardType.SHORT_NEWS;
    }
    
    public String getDescription() {
        return this.a;
    }
    
    public String getDomain() {
        return this.e;
    }
    
    public String getImageUrl() {
        return this.b;
    }
    
    public String getTitle() {
        return this.c;
    }
    
    @Override
    public String getUrl() {
        return this.d;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShortNewsCard{");
        sb.append(super.toString());
        sb.append(", mDescription='");
        sb.append(this.a);
        sb.append('\'');
        sb.append(", mImageUrl='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mTitle='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mUrl='");
        sb.append(this.d);
        sb.append('\'');
        sb.append(", mDomain='");
        sb.append(this.e);
        sb.append('\'');
        sb.append("}");
        return sb.toString();
    }
}
