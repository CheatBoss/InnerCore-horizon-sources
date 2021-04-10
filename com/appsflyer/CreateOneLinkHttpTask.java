package com.appsflyer;

import android.content.*;
import org.json.*;
import java.util.*;
import javax.net.ssl.*;
import java.io.*;
import com.appsflyer.share.*;

public class CreateOneLinkHttpTask extends OneLinkHttpTask
{
    private static final String BRAND_DOMAIN = "brand_domain";
    private static final String TRACKING_LINK_DATA_KEY = "data";
    private static final String TRACKING_LINK_LIVE_TIME_KEY = "ttl";
    private String brandDomain;
    private Context context;
    private Map<String, String> data;
    private boolean mTrackingStopped;
    private String packageName;
    private ResponseListener responseListener;
    private String ttl;
    
    public CreateOneLinkHttpTask(final String oneLinkId, final Map<String, String> data, final AppsFlyerLibCore appsFlyerLibCore, final Context context, final boolean mTrackingStopped) {
        super(appsFlyerLibCore);
        this.packageName = "";
        this.mTrackingStopped = false;
        this.mTrackingStopped = mTrackingStopped;
        this.context = context;
        if (context != null) {
            this.packageName = context.getPackageName();
        }
        else {
            AFLogger.afWarnLog("CreateOneLinkHttpTask: context can't be null");
        }
        this.oneLinkId = oneLinkId;
        this.ttl = "-1";
        this.data = data;
    }
    
    String getOneLinkUrl() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ServerConfigHandler.getUrl("https://%sonelink.%s/shortlink-sdk/v1"));
        sb.append("/");
        sb.append(this.oneLinkId);
        return sb.toString();
    }
    
    void handleResponse(final String s) {
        try {
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                this.responseListener.onResponse(jsonObject.optString((String)keys.next()));
            }
        }
        catch (JSONException ex) {
            this.responseListener.onResponseError("Can't parse one link data");
            final StringBuilder sb = new StringBuilder();
            sb.append("Error while parsing to json ");
            sb.append(s);
            AFLogger.afErrorLog(sb.toString(), (Throwable)ex);
        }
    }
    
    void initRequest(final HttpsURLConnection httpsURLConnection) throws JSONException, IOException {
        if (this.mTrackingStopped) {
            return;
        }
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setUseCaches(false);
        final JSONObject jsonObject = new JSONObject();
        final JSONObject jsonObject2 = new JSONObject((Map)this.data);
        jsonObject.put("ttl", (Object)this.ttl);
        jsonObject.put("data", (Object)jsonObject2);
        final String brandDomain = this.brandDomain;
        if (brandDomain != null) {
            jsonObject.put("brand_domain", (Object)brandDomain);
        }
        httpsURLConnection.connect();
        final DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
    }
    
    void onErrorResponse() {
        final LinkGenerator addParameters = new LinkGenerator("af_app_invites").setBaseURL(this.oneLinkId, AppsFlyerProperties.getInstance().getString("onelinkDomain"), this.packageName).addParameter("af_siteid", this.packageName).addParameters(this.data);
        final String string = AppsFlyerProperties.getInstance().getString("AppUserId");
        if (string != null) {
            addParameters.setReferrerCustomerId(string);
        }
        this.responseListener.onResponse(addParameters.generateLink());
    }
    
    public void setBrandDomain(final String brandDomain) {
        this.brandDomain = brandDomain;
    }
    
    public void setListener(final ResponseListener responseListener) {
        this.responseListener = responseListener;
    }
    
    public interface ResponseListener
    {
        void onResponse(final String p0);
        
        void onResponseError(final String p0);
    }
}
