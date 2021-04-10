package com.appboy.enums;

import java.util.*;

public enum CardCategory
{
    ADVERTISING, 
    ANNOUNCEMENTS, 
    NEWS, 
    NO_CATEGORY, 
    SOCIAL;
    
    private static final Map<String, CardCategory> a;
    
    static {
        a = new HashMap<String, CardCategory>();
        for (final CardCategory cardCategory : EnumSet.allOf(CardCategory.class)) {
            CardCategory.a.put(cardCategory.toString(), cardCategory);
        }
    }
    
    public static CardCategory get(final String s) {
        return CardCategory.a.get(s.toUpperCase(Locale.US));
    }
    
    public static EnumSet<CardCategory> getAllCategories() {
        return EnumSet.allOf(CardCategory.class);
    }
}
