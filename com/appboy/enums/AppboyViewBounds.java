package com.appboy.enums;

public enum AppboyViewBounds
{
    BASE_CARD_VIEW(512, 512), 
    IN_APP_MESSAGE_MODAL(580, 580), 
    IN_APP_MESSAGE_SLIDEUP(100, 100), 
    NOTIFICATION_EXPANDED_IMAGE(478, 256), 
    NOTIFICATION_LARGE_ICON(64, 64), 
    NOTIFICATION_ONE_IMAGE_STORY(256, 128), 
    NO_BOUNDS(0, 0);
    
    final int a;
    final int b;
    
    private AppboyViewBounds(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    public int getHeightDp() {
        return this.b;
    }
    
    public int getWidthDp() {
        return this.a;
    }
}
