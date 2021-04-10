package com.appboy;

import android.content.*;
import com.appboy.ui.actions.*;

public interface IAppboyNavigator
{
    void gotoNewsFeed(final Context p0, final NewsfeedAction p1);
    
    void gotoUri(final Context p0, final UriAction p1);
}
