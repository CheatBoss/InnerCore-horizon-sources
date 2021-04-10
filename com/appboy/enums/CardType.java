package com.appboy.enums;

public enum CardType
{
    BANNER, 
    CAPTIONED_IMAGE, 
    CONTROL, 
    CROSS_PROMOTIONAL, 
    DEFAULT, 
    SHORT_NEWS, 
    TEXT_ANNOUNCEMENT;
    
    public static CardType fromValue(final int n) {
        return values()[n];
    }
    
    public int getValue() {
        return this.ordinal();
    }
}
