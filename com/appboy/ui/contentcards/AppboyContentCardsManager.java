package com.appboy.ui.contentcards;

import com.appboy.ui.contentcards.listeners.*;

public class AppboyContentCardsManager
{
    private static volatile AppboyContentCardsManager sInstance;
    private IContentCardsActionListener mCustomContentCardsActionListener;
    private IContentCardsActionListener mDefaultContentCardsActionListener;
    
    public AppboyContentCardsManager() {
        this.mDefaultContentCardsActionListener = new AppboyContentCardsActionListener();
    }
    
    public static AppboyContentCardsManager getInstance() {
        if (AppboyContentCardsManager.sInstance == null) {
            synchronized (AppboyContentCardsManager.class) {
                if (AppboyContentCardsManager.sInstance == null) {
                    AppboyContentCardsManager.sInstance = new AppboyContentCardsManager();
                }
            }
        }
        return AppboyContentCardsManager.sInstance;
    }
    
    public IContentCardsActionListener getContentCardsActionListener() {
        final IContentCardsActionListener mCustomContentCardsActionListener = this.mCustomContentCardsActionListener;
        if (mCustomContentCardsActionListener != null) {
            return mCustomContentCardsActionListener;
        }
        return this.mDefaultContentCardsActionListener;
    }
    
    public void setContentCardsActionListener(final IContentCardsActionListener mCustomContentCardsActionListener) {
        this.mCustomContentCardsActionListener = mCustomContentCardsActionListener;
    }
}
