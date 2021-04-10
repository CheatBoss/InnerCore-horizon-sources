package com.appboy.ui;

import com.appboy.*;
import com.appboy.support.*;
import android.content.*;
import com.appboy.ui.actions.*;

public class AppboyNavigator implements IAppboyNavigator
{
    private static final String TAG;
    private static volatile IAppboyNavigator sCustomAppboyNavigator;
    private static volatile IAppboyNavigator sDefaultAppboyNavigator;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyNavigator.class);
        AppboyNavigator.sDefaultAppboyNavigator = new AppboyNavigator();
    }
    
    public static void executeNewsFeedAction(final Context context, final NewsfeedAction newsfeedAction) {
        if (newsfeedAction == null) {
            AppboyLogger.e(AppboyNavigator.TAG, "IAppboyNavigator cannot open News feed because the news feed action object was null.");
            return;
        }
        newsfeedAction.execute(context);
    }
    
    public static void executeUriAction(final Context context, final UriAction uriAction) {
        if (uriAction == null) {
            AppboyLogger.e(AppboyNavigator.TAG, "IAppboyNavigator cannot open Uri because the Uri action object was null.");
            return;
        }
        uriAction.execute(context);
    }
    
    public static IAppboyNavigator getAppboyNavigator() {
        if (AppboyNavigator.sCustomAppboyNavigator != null) {
            return AppboyNavigator.sCustomAppboyNavigator;
        }
        return AppboyNavigator.sDefaultAppboyNavigator;
    }
    
    public static void setAppboyNavigator(final IAppboyNavigator sCustomAppboyNavigator) {
        AppboyLogger.d(AppboyNavigator.TAG, "Custom IAppboyNavigator set");
        AppboyNavigator.sCustomAppboyNavigator = sCustomAppboyNavigator;
    }
    
    @Override
    public void gotoNewsFeed(final Context context, final NewsfeedAction newsfeedAction) {
        executeNewsFeedAction(context, newsfeedAction);
    }
    
    @Override
    public void gotoUri(final Context context, final UriAction uriAction) {
        executeUriAction(context, uriAction);
    }
}
