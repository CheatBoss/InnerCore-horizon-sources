package com.appboy.push;

import java.util.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.net.*;
import com.appboy.enums.*;
import com.appboy.support.*;
import android.graphics.*;
import android.util.*;
import android.support.v4.app.*;
import com.appboy.ui.*;
import android.widget.*;
import com.appboy.*;

public class AppboyNotificationStyleFactory
{
    public static final int BIG_PICTURE_STYLE_IMAGE_HEIGHT = 192;
    private static final String CENTER = "center";
    private static final String END = "end";
    private static final Map<String, Integer> GRAVITY_MAP;
    private static final String START = "start";
    private static final Integer[] STORY_FULL_VIEW_XML_IDS;
    private static final String STORY_SET_GRAVITY = "setGravity";
    private static final String STORY_SET_VISIBILITY = "setVisibility";
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyNotificationStyleFactory.class);
        STORY_FULL_VIEW_XML_IDS = new Integer[] { R$id.com_appboy_story_text_view, R$id.com_appboy_story_text_view_container, R$id.com_appboy_story_text_view_small, R$id.com_appboy_story_text_view_small_container, R$id.com_appboy_story_image_view, R$id.com_appboy_story_relative_layout };
        final HashMap<String, Integer> gravity_MAP = new HashMap<String, Integer>();
        gravity_MAP.put("start", 8388611);
        gravity_MAP.put("center", 17);
        gravity_MAP.put("end", 8388613);
        GRAVITY_MAP = gravity_MAP;
    }
    
    private static PendingIntent createStoryPageClickedPendingIntent(final Context context, final String s, final String s2, final String s3, final String s4) {
        final Intent setClass = new Intent("com.appboy.action.APPBOY_STORY_CLICKED").setClass(context, (Class)AppboyNotificationRoutingActivity.class);
        setClass.putExtra("appboy_action_uri", s);
        setClass.putExtra("appboy_action_use_webview", s2);
        setClass.putExtra("appboy_story_page_id", s3);
        setClass.putExtra("appboy_campaign_id", s4);
        return PendingIntent.getActivity(context, IntentUtils.getRequestCode(), setClass, 1073741824);
    }
    
    private static PendingIntent createStoryTraversedPendingIntent(final Context context, final Bundle bundle, final int n) {
        final Intent setClass = new Intent("com.appboy.action.STORY_TRAVERSE").setClass(context, (Class)AppboyNotificationUtils.getNotificationReceiverClass());
        if (bundle != null) {
            bundle.putInt("appboy_story_index", n);
            setClass.putExtras(bundle);
        }
        return PendingIntent.getBroadcast(context, IntentUtils.getRequestCode(), setClass, 1073741824);
    }
    
    public static NotificationCompat.Style getBigNotificationStyle(final Context context, final Bundle bundle, final Bundle bundle2, final NotificationCompat.Builder builder) {
        Object o;
        if (bundle.containsKey("ab_c")) {
            AppboyLogger.d(AppboyNotificationStyleFactory.TAG, "Rendering push notification with DecoratedCustomViewStyle (Story)");
            o = getStoryStyle(context, bundle, builder);
        }
        else if (bundle2 != null && bundle2.containsKey("appboy_image_url")) {
            AppboyLogger.d(AppboyNotificationStyleFactory.TAG, "Rendering push notification with BigPictureStyle");
            o = getBigPictureNotificationStyle(context, bundle, bundle2);
        }
        else {
            o = null;
        }
        NotificationCompat.Style bigTextNotificationStyle = (NotificationCompat.Style)o;
        if (o == null) {
            AppboyLogger.d(AppboyNotificationStyleFactory.TAG, "Rendering push notification with BigTextStyle");
            bigTextNotificationStyle = getBigTextNotificationStyle(bundle);
        }
        return bigTextNotificationStyle;
    }
    
    public static NotificationCompat.BigPictureStyle getBigPictureNotificationStyle(final Context context, final Bundle bundle, Bundle bitmap) {
        if (bitmap != null) {
            if (!bitmap.containsKey("appboy_image_url")) {
                return null;
            }
            final String string = bitmap.getString("appboy_image_url");
            if (StringUtils.isNullOrBlank(string)) {
                return null;
            }
            bitmap = (Bundle)AppboyImageUtils.getBitmap(context, Uri.parse(string), AppboyViewBounds.NOTIFICATION_EXPANDED_IMAGE);
            if (bitmap == null) {
                return null;
            }
            while (true) {
                while (true) {
                    Label_0176: {
                        try {
                            if (((Bitmap)bitmap).getWidth() <= ((Bitmap)bitmap).getHeight()) {
                                break Label_0176;
                            }
                            final DisplayMetrics defaultScreenDisplayMetrics = AppboyImageUtils.getDefaultScreenDisplayMetrics(context);
                            final int pixelsFromDensityAndDp = AppboyImageUtils.getPixelsFromDensityAndDp(defaultScreenDisplayMetrics.densityDpi, 192);
                            int widthPixels;
                            if ((widthPixels = pixelsFromDensityAndDp * 2) > defaultScreenDisplayMetrics.widthPixels) {
                                widthPixels = defaultScreenDisplayMetrics.widthPixels;
                            }
                            Object scaledBitmap;
                            try {
                                scaledBitmap = Bitmap.createScaledBitmap((Bitmap)bitmap, widthPixels, pixelsFromDensityAndDp, true);
                            }
                            catch (Exception ex) {
                                AppboyLogger.e(AppboyNotificationStyleFactory.TAG, "Failed to scale image bitmap, using original.", ex);
                                break Label_0176;
                            }
                            if (scaledBitmap == null) {
                                AppboyLogger.i(AppboyNotificationStyleFactory.TAG, "Bitmap download failed for push notification. No image will be included with the notification.");
                                return null;
                            }
                            final NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                            bigPictureStyle.bigPicture((Bitmap)scaledBitmap);
                            setBigPictureSummaryAndTitle(bigPictureStyle, bundle);
                            return bigPictureStyle;
                        }
                        catch (Exception ex2) {
                            AppboyLogger.e(AppboyNotificationStyleFactory.TAG, "Failed to create Big Picture Style.", ex2);
                        }
                        break;
                    }
                    Object scaledBitmap = bitmap;
                    continue;
                }
            }
        }
        return null;
    }
    
    public static NotificationCompat.BigTextStyle getBigTextNotificationStyle(final Bundle bundle) {
        CharSequence string = null;
        if (bundle != null) {
            final NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.bigText(bundle.getString("a"));
            String string2;
            if (bundle.containsKey("ab_bs")) {
                string2 = bundle.getString("ab_bs");
            }
            else {
                string2 = null;
            }
            if (bundle.containsKey("ab_bt")) {
                string = bundle.getString("ab_bt");
            }
            if (string2 != null) {
                bigTextStyle.setSummaryText(string2);
            }
            if (string != null) {
                bigTextStyle.setBigContentTitle(string);
            }
            return bigTextStyle;
        }
        return null;
    }
    
    static int getPushStoryPageCount(final Bundle bundle) {
        int n;
        for (n = 0; pushStoryPageExistsForIndex(bundle, n); ++n) {}
        return n;
    }
    
    static int getPushStoryPageIndex(final Bundle bundle) {
        if (!bundle.containsKey("appboy_story_index")) {
            return 0;
        }
        return bundle.getInt("appboy_story_index");
    }
    
    public static NotificationCompat$DecoratedCustomViewStyle getStoryStyle(final Context context, final Bundle bundle, final NotificationCompat.Builder builder) {
        final NotificationCompat$DecoratedCustomViewStyle notificationCompat$DecoratedCustomViewStyle = new NotificationCompat$DecoratedCustomViewStyle();
        final int pushStoryPageCount = getPushStoryPageCount(bundle);
        final int pushStoryPageIndex = getPushStoryPageIndex(bundle);
        final RemoteViews customBigContentView = new RemoteViews(context.getPackageName(), R$layout.com_appboy_notification_story_one_image);
        if (!populatePushStoryPage(customBigContentView, context, bundle, pushStoryPageIndex)) {
            AppboyLogger.w(AppboyNotificationStyleFactory.TAG, "Push story page was not populated correctly. Not using DecoratedCustomViewStyle.");
            return null;
        }
        customBigContentView.setOnClickPendingIntent(R$id.com_appboy_story_button_previous, createStoryTraversedPendingIntent(context, bundle, (pushStoryPageIndex - 1 + pushStoryPageCount) % pushStoryPageCount));
        customBigContentView.setOnClickPendingIntent(R$id.com_appboy_story_button_next, createStoryTraversedPendingIntent(context, bundle, (pushStoryPageIndex + 1) % pushStoryPageCount));
        builder.setCustomBigContentView(customBigContentView);
        builder.setOnlyAlertOnce(true);
        return notificationCompat$DecoratedCustomViewStyle;
    }
    
    private static boolean populatePushStoryPage(final RemoteViews remoteViews, final Context context, final Bundle bundle, final int n) {
        final String string = bundle.getString("cid");
        final String actionFieldAtIndex = AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_t");
        if (!StringUtils.isNullOrBlank(actionFieldAtIndex)) {
            remoteViews.setTextViewText((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[0], (CharSequence)actionFieldAtIndex);
            remoteViews.setInt((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[1], "setGravity", (int)AppboyNotificationStyleFactory.GRAVITY_MAP.get(AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_t_j", "center")));
        }
        else {
            remoteViews.setInt((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[1], "setVisibility", 8);
        }
        final String actionFieldAtIndex2 = AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_st");
        if (!StringUtils.isNullOrBlank(actionFieldAtIndex2)) {
            remoteViews.setTextViewText((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[2], (CharSequence)actionFieldAtIndex2);
            remoteViews.setInt((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[3], "setGravity", (int)AppboyNotificationStyleFactory.GRAVITY_MAP.get(AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_st_j", "center")));
        }
        else {
            remoteViews.setInt((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[3], "setVisibility", 8);
        }
        final Bitmap bitmapFromUrl = Appboy.getInstance(context).getAppboyImageLoader().getBitmapFromUrl(context, AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_i"), AppboyViewBounds.NOTIFICATION_ONE_IMAGE_STORY);
        if (bitmapFromUrl == null) {
            return false;
        }
        remoteViews.setImageViewBitmap((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[4], bitmapFromUrl);
        remoteViews.setOnClickPendingIntent((int)AppboyNotificationStyleFactory.STORY_FULL_VIEW_XML_IDS[5], createStoryPageClickedPendingIntent(context, AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_uri"), AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_use_webview"), AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_id", ""), string));
        return true;
    }
    
    static boolean pushStoryPageExistsForIndex(final Bundle bundle, final int n) {
        return AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_i", null) != null;
    }
    
    static void setBigPictureSummaryAndTitle(final NotificationCompat.BigPictureStyle bigPictureStyle, final Bundle bundle) {
        final boolean containsKey = bundle.containsKey("ab_bs");
        CharSequence string = null;
        String string2;
        if (containsKey) {
            string2 = bundle.getString("ab_bs");
        }
        else {
            string2 = null;
        }
        if (bundle.containsKey("ab_bt")) {
            string = bundle.getString("ab_bt");
        }
        if (string2 != null) {
            bigPictureStyle.setSummaryText(string2);
        }
        if (string != null) {
            bigPictureStyle.setBigContentTitle(string);
        }
        if (bundle.getString("s") == null && string2 == null) {
            bigPictureStyle.setSummaryText(bundle.getString("a"));
        }
    }
}
