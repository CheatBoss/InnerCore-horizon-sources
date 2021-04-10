package com.appboy.enums;

import java.util.*;
import org.json.*;
import com.appboy.support.*;
import bo.app.*;

public enum CardKey
{
    BANNER_IMAGE_ASPECT_RATIO("aspect_ratio", "ar"), 
    BANNER_IMAGE_DOMAIN("domain", (String)null), 
    BANNER_IMAGE_IMAGE("image", "i"), 
    BANNER_IMAGE_URL("url", "u"), 
    CAPTIONED_IMAGE_ASPECT_RATIO("aspect_ratio", "ar"), 
    CAPTIONED_IMAGE_DESCRIPTION("description", "ds"), 
    CAPTIONED_IMAGE_DOMAIN("domain", "dm"), 
    CAPTIONED_IMAGE_IMAGE("image", "i"), 
    CAPTIONED_IMAGE_TITLE("title", "tt"), 
    CAPTIONED_IMAGE_URL("url", "u"), 
    CATEGORIES("categories", (String)null), 
    CREATED("created", "ca"), 
    DISMISSED((String)null, "d"), 
    DISMISSIBLE((String)null, "db"), 
    EXPIRES_AT("expires_at", "ea"), 
    EXTRAS("extras", "e"), 
    ID("id", "id"), 
    OPEN_URI_IN_WEBVIEW("use_webview", "uw"), 
    PINNED((String)null, "p"), 
    READ((String)null, "read"), 
    REMOVED((String)null, "r"), 
    SHORT_NEWS_DESCRIPTION("description", "ds"), 
    SHORT_NEWS_DOMAIN("domain", "dm"), 
    SHORT_NEWS_IMAGE("image", "i"), 
    SHORT_NEWS_TITLE("title", "tt"), 
    SHORT_NEWS_URL("url", "u"), 
    TEXT_ANNOUNCEMENT_DESCRIPTION("description", "ds"), 
    TEXT_ANNOUNCEMENT_DOMAIN("domain", "dm"), 
    TEXT_ANNOUNCEMENT_TITLE("title", "tt"), 
    TEXT_ANNOUNCEMENT_URL("url", "u"), 
    TYPE("type", "tp"), 
    UPDATED("updated", (String)null), 
    VIEWED("viewed", "v");
    
    private static final String a;
    private static final Map<String, CardType> b;
    private String c;
    private String d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(CardKey.class);
        (b = new HashMap<String, CardType>()).put("banner_image", CardType.BANNER);
        CardKey.b.put("captioned_image", CardType.CAPTIONED_IMAGE);
        CardKey.b.put("text_announcement", CardType.TEXT_ANNOUNCEMENT);
        CardKey.b.put("short_news", CardType.SHORT_NEWS);
        CardKey.b.put("cross_promotion_small", CardType.CROSS_PROMOTIONAL);
        CardKey.b.put("control", CardType.CONTROL);
    }
    
    private CardKey(final String c, final String d) {
        this.c = c;
        this.d = d;
    }
    
    public String getContentCardsKey() {
        return this.d;
    }
    
    public String getFeedKey() {
        return this.c;
    }
    
    public static class Provider
    {
        private final boolean a;
        
        public Provider(final boolean a) {
            this.a = a;
        }
        
        public CardType getCardTypeFromJson(final JSONObject jsonObject) {
            String optString;
            final String s = optString = jsonObject.optString(this.getKey(CardKey.TYPE), (String)null);
            if (!StringUtils.isNullOrEmpty(s)) {
                optString = s;
                if (this.a) {
                    optString = s;
                    if (s.equals("short_news")) {
                        optString = s;
                        if (StringUtils.isNullOrEmpty(ec.a(jsonObject, this.getKey(CardKey.SHORT_NEWS_IMAGE)))) {
                            AppboyLogger.v(CardKey.a, "Short News card doesn't contain image url, parsing type as Text Announcement");
                            optString = "text_announcement";
                        }
                    }
                }
            }
            if (CardKey.b.containsKey(optString)) {
                return (CardType)CardKey.b.get(optString);
            }
            return CardType.DEFAULT;
        }
        
        public String getKey(final CardKey cardKey) {
            if (this.a) {
                return cardKey.getContentCardsKey();
            }
            return cardKey.getFeedKey();
        }
    }
}
