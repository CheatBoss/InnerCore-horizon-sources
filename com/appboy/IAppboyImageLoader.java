package com.appboy;

import android.content.*;
import com.appboy.enums.*;
import android.graphics.*;
import android.widget.*;

public interface IAppboyImageLoader
{
    Bitmap getBitmapFromUrl(final Context p0, final String p1, final AppboyViewBounds p2);
    
    void renderUrlIntoView(final Context p0, final String p1, final ImageView p2, final AppboyViewBounds p3);
    
    void setOffline(final boolean p0);
}
