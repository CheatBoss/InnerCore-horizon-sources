package com.appboy.ui.feed;

import android.widget.*;
import android.graphics.drawable.*;
import com.appboy.support.*;
import android.content.*;
import android.util.*;
import com.appboy.ui.*;
import android.content.res.*;

public class AppboyImageSwitcher extends ImageSwitcher
{
    private static final String TAG;
    private Drawable mReadIcon;
    private Drawable mUnReadIcon;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyImageSwitcher.class);
    }
    
    public AppboyImageSwitcher(final Context context, final AttributeSet set) {
        while (true) {
            super(context, set);
            while (true) {
                int n;
                try {
                    final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.com_appboy_ui_feed_AppboyImageSwitcher);
                    n = 0;
                    if (n >= obtainStyledAttributes.getIndexCount()) {
                        obtainStyledAttributes.recycle();
                        return;
                    }
                    final int index = obtainStyledAttributes.getIndex(n);
                    if (index == R$styleable.com_appboy_ui_feed_AppboyImageSwitcher_appboyFeedCustomReadIcon) {
                        final Drawable drawable = obtainStyledAttributes.getDrawable(index);
                        if (drawable != null) {
                            this.mReadIcon = drawable;
                        }
                    }
                    else if (obtainStyledAttributes.getIndex(n) == R$styleable.com_appboy_ui_feed_AppboyImageSwitcher_appboyFeedCustomUnReadIcon) {
                        final Drawable drawable2 = obtainStyledAttributes.getDrawable(index);
                        if (drawable2 != null) {
                            this.mUnReadIcon = drawable2;
                        }
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.w(AppboyImageSwitcher.TAG, "Error while checking for custom drawable.", ex);
                    return;
                }
                ++n;
                continue;
            }
        }
    }
    
    public Drawable getReadIcon() {
        return this.mReadIcon;
    }
    
    public Drawable getUnReadIcon() {
        return this.mUnReadIcon;
    }
    
    public void setReadIcon(final Drawable mReadIcon) {
        this.mReadIcon = mReadIcon;
    }
    
    public void setUnReadIcon(final Drawable mUnReadIcon) {
        this.mUnReadIcon = mUnReadIcon;
    }
}
