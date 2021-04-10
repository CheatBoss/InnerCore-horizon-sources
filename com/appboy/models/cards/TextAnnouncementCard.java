package com.appboy.models.cards;

import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public final class TextAnnouncementCard extends Card
{
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    
    public TextAnnouncementCard(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public TextAnnouncementCard(final JSONObject jsonObject, final CardKey.Provider provider, final bl bl, final dn dn, final c c) {
        super(jsonObject, provider, bl, dn, c);
        this.b = ec.a(jsonObject, provider.getKey(CardKey.TEXT_ANNOUNCEMENT_TITLE));
        this.a = jsonObject.getString(provider.getKey(CardKey.TEXT_ANNOUNCEMENT_DESCRIPTION));
        this.c = ec.a(jsonObject, provider.getKey(CardKey.TEXT_ANNOUNCEMENT_URL));
        this.d = ec.a(jsonObject, provider.getKey(CardKey.TEXT_ANNOUNCEMENT_DOMAIN));
    }
    
    @Override
    public CardType getCardType() {
        return CardType.TEXT_ANNOUNCEMENT;
    }
    
    public String getDescription() {
        return this.a;
    }
    
    public String getDomain() {
        return this.d;
    }
    
    public String getTitle() {
        return this.b;
    }
    
    @Override
    public String getUrl() {
        return this.c;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TextAnnouncementCard{");
        sb.append(super.toString());
        sb.append(", mDescription='");
        sb.append(this.a);
        sb.append('\'');
        sb.append(", mTitle='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mUrl='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mDomain='");
        sb.append(this.d);
        sb.append('\'');
        sb.append("}");
        return sb.toString();
    }
}
