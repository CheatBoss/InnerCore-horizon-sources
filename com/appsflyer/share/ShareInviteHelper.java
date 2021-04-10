package com.appsflyer.share;

import android.content.*;
import com.appsflyer.*;
import android.text.*;
import java.util.*;

public class ShareInviteHelper
{
    public static LinkGenerator generateInviteUrl(final Context context) {
        final LinkGenerator addParameter = new LinkGenerator("af_app_invites").setBaseURL(AppsFlyerProperties.getInstance().getString("oneLinkSlug"), AppsFlyerProperties.getInstance().getString("onelinkDomain"), context.getPackageName()).setReferrerUID(AppsFlyerLib.getInstance().getAppsFlyerUID(context)).setReferrerCustomerId(AppsFlyerProperties.getInstance().getString("AppUserId")).addParameter("af_siteid", context.getPackageName());
        final String string = AppsFlyerProperties.getInstance().getString("onelinkScheme");
        if (string != null && string.length() > 3) {
            addParameter.setBaseDeeplink(string);
        }
        return addParameter;
    }
    
    @Deprecated
    public static void generateUserInviteLink(final Context context, final String s, final Map<String, String> map, final CreateOneLinkHttpTask.ResponseListener listener) {
        if (AppsFlyerProperties.getInstance().getBoolean("waitForCustomerId", false)) {
            AFLogger.afInfoLog("CustomerUserId not set, generate User Invite Link is disabled", true);
            return;
        }
        final CreateOneLinkHttpTask createOneLinkHttpTask = new CreateOneLinkHttpTask(s, map, AppsFlyerLibCore.getInstance(), context, AppsFlyerLib.getInstance().isTrackingStopped());
        createOneLinkHttpTask.setConnProvider(new OneLinkHttpTask.HttpsUrlConnectionProvider());
        createOneLinkHttpTask.setListener(listener);
        AFExecutor.getInstance().getThreadPoolExecutor().execute(createOneLinkHttpTask);
    }
    
    public static void trackInvite(final Context context, final String s, final Map<String, String> map) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            AFLogger.afWarnLog("[Invite] Cannot track App-Invite with null/empty channel");
            return;
        }
        if (AppsFlyerProperties.getInstance().getBoolean("waitForCustomerId", false)) {
            AFLogger.afInfoLog("CustomerUserId not set, track Invite is disabled", true);
            return;
        }
        final LinkGenerator generateInviteUrl = generateInviteUrl(context);
        generateInviteUrl.addParameters(map);
        AFLogger.afDebugLog("[Invite] Tracking App-Invite via channel: ".concat(String.valueOf(s)));
        final StringBuilder sb = new StringBuilder("[Invite] Generated URL: ");
        sb.append(generateInviteUrl.generateLink());
        AFLogger.afDebugLog(sb.toString());
        final String mediaSource = generateInviteUrl.getMediaSource();
        String s2;
        if ("af_app_invites".equals(mediaSource)) {
            s2 = "af_invite";
        }
        else {
            s2 = mediaSource;
            if ("af_user_share".equals(mediaSource)) {
                s2 = "af_share";
            }
        }
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (generateInviteUrl.getParameters() != null) {
            hashMap.putAll((Map<?, ?>)generateInviteUrl.getParameters());
        }
        hashMap.put("af_channel", s);
        AppsFlyerLib.getInstance().trackEvent(context, s2, hashMap);
    }
}
