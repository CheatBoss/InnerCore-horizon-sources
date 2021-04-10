package com.appboy.ui.feed;

import com.appboy.ui.feed.listeners.*;

public class AppboyFeedManager
{
    private static volatile AppboyFeedManager sInstance;
    private IFeedClickActionListener mCustomFeedClickActionListener;
    private IFeedClickActionListener mDefaultFeedClickActionListener;
    
    public AppboyFeedManager() {
        this.mDefaultFeedClickActionListener = new AppboyDefaultFeedClickActionListener();
    }
    
    public static AppboyFeedManager getInstance() {
        if (AppboyFeedManager.sInstance == null) {
            synchronized (AppboyFeedManager.class) {
                if (AppboyFeedManager.sInstance == null) {
                    AppboyFeedManager.sInstance = new AppboyFeedManager();
                }
            }
        }
        return AppboyFeedManager.sInstance;
    }
    
    public IFeedClickActionListener getFeedCardClickActionListener() {
        final IFeedClickActionListener mCustomFeedClickActionListener = this.mCustomFeedClickActionListener;
        if (mCustomFeedClickActionListener != null) {
            return mCustomFeedClickActionListener;
        }
        return this.mDefaultFeedClickActionListener;
    }
    
    public void setFeedCardClickActionListener(final IFeedClickActionListener mCustomFeedClickActionListener) {
        this.mCustomFeedClickActionListener = mCustomFeedClickActionListener;
    }
}
