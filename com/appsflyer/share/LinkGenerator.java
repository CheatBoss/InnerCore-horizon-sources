package com.appsflyer.share;

import java.util.*;
import java.net.*;
import java.io.*;
import android.content.*;
import com.appsflyer.*;

public class LinkGenerator
{
    private String \u0131;
    private String \u0196;
    private String \u01c3;
    private String \u0268;
    String \u0269;
    private String \u026a;
    private String \u0279;
    private Map<String, String> \u027e;
    private String \u0399;
    String \u03b9;
    private String \u0406;
    private String \u0456;
    private String \u04c0;
    private Map<String, String> \u04cf;
    
    public LinkGenerator(final String \u0131) {
        this.\u027e = new HashMap<String, String>();
        this.\u04cf = new HashMap<String, String>();
        this.\u0131 = \u0131;
    }
    
    private StringBuilder \u01c3() {
        final StringBuilder sb = new StringBuilder();
        final String \u0269 = this.\u0269;
        String s;
        if (\u0269 != null && \u0269.startsWith("http")) {
            s = this.\u0269;
        }
        else {
            s = ServerConfigHandler.getUrl(Constants.\u0269);
        }
        sb.append(s);
        if (this.\u03b9 != null) {
            sb.append('/');
            sb.append(this.\u03b9);
        }
        this.\u04cf.put("pid", this.\u0131);
        sb.append('?');
        sb.append("pid=");
        sb.append(\u0399(this.\u0131, "media source"));
        final String \u0456 = this.\u0456;
        if (\u0456 != null) {
            this.\u04cf.put("af_referrer_uid", \u0456);
            sb.append('&');
            sb.append("af_referrer_uid=");
            sb.append(\u0399(this.\u0456, "referrerUID"));
        }
        final String \u03b9 = this.\u0399;
        if (\u03b9 != null) {
            this.\u04cf.put("af_channel", \u03b9);
            sb.append('&');
            sb.append("af_channel=");
            sb.append(\u0399(this.\u0399, "channel"));
        }
        final String \u0279 = this.\u0279;
        if (\u0279 != null) {
            this.\u04cf.put("af_referrer_customer_id", \u0279);
            sb.append('&');
            sb.append("af_referrer_customer_id=");
            sb.append(\u0399(this.\u0279, "referrerCustomerId"));
        }
        final String \u01c3 = this.\u01c3;
        if (\u01c3 != null) {
            this.\u04cf.put("c", \u01c3);
            sb.append('&');
            sb.append("c=");
            sb.append(\u0399(this.\u01c3, "campaign"));
        }
        final String \u04562 = this.\u0406;
        if (\u04562 != null) {
            this.\u04cf.put("af_referrer_name", \u04562);
            sb.append('&');
            sb.append("af_referrer_name=");
            sb.append(\u0399(this.\u0406, "referrerName"));
        }
        final String \u04cf = this.\u04c0;
        if (\u04cf != null) {
            this.\u04cf.put("af_referrer_image_url", \u04cf);
            sb.append('&');
            sb.append("af_referrer_image_url=");
            sb.append(\u0399(this.\u04c0, "referrerImageURL"));
        }
        if (this.\u0268 != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.\u0268);
            final boolean endsWith = this.\u0268.endsWith("/");
            final String s2 = "";
            String s3;
            if (endsWith) {
                s3 = "";
            }
            else {
                s3 = "/";
            }
            sb2.append(s3);
            final String \u02692 = this.\u0196;
            if (\u02692 != null) {
                sb2.append(\u02692);
            }
            this.\u04cf.put("af_dp", sb2.toString());
            sb.append('&');
            sb.append("af_dp=");
            sb.append(\u0399(this.\u0268, "baseDeeplink"));
            if (this.\u0196 != null) {
                String s4;
                if (this.\u0268.endsWith("/")) {
                    s4 = s2;
                }
                else {
                    s4 = "%2F";
                }
                sb.append(s4);
                sb.append(\u0399(this.\u0196, "deeplinkPath"));
            }
        }
        for (final String s5 : this.\u027e.keySet()) {
            final String string = sb.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s5);
            sb3.append("=");
            sb3.append(\u0399(this.\u027e.get(s5), s5));
            if (!string.contains(sb3.toString())) {
                sb.append('&');
                sb.append(s5);
                sb.append('=');
                sb.append(\u0399(this.\u027e.get(s5), s5));
            }
        }
        return sb;
    }
    
    private static String \u0399(final String s, final String s2) {
        try {
            return URLEncoder.encode(s, "utf8");
        }
        catch (UnsupportedEncodingException ex) {
            final StringBuilder sb = new StringBuilder("Illegal ");
            sb.append(s2);
            sb.append(": ");
            sb.append(s);
            AFLogger.afInfoLog(sb.toString());
        }
        finally {
            return "";
        }
    }
    
    public LinkGenerator addParameter(final String s, final String s2) {
        this.\u027e.put(s, s2);
        return this;
    }
    
    public LinkGenerator addParameters(final Map<String, String> map) {
        if (map != null) {
            this.\u027e.putAll(map);
        }
        return this;
    }
    
    public String generateLink() {
        return this.\u01c3().toString();
    }
    
    public void generateLink(final Context context, final CreateOneLinkHttpTask.ResponseListener listener) {
        final String string = AppsFlyerProperties.getInstance().getString("oneLinkSlug");
        if (!this.\u027e.isEmpty()) {
            for (final Map.Entry<String, String> entry : this.\u027e.entrySet()) {
                this.\u04cf.put(entry.getKey(), entry.getValue());
            }
        }
        this.\u01c3();
        final String \u026a = this.\u026a;
        final Map<String, String> \u04cf = this.\u04cf;
        if (AppsFlyerProperties.getInstance().getBoolean("waitForCustomerId", false)) {
            AFLogger.afInfoLog("CustomerUserId not set, generate User Invite Link is disabled", true);
            return;
        }
        final CreateOneLinkHttpTask createOneLinkHttpTask = new CreateOneLinkHttpTask(string, \u04cf, AppsFlyerLibCore.getInstance(), context, AppsFlyerLib.getInstance().isTrackingStopped());
        createOneLinkHttpTask.setConnProvider(new OneLinkHttpTask.HttpsUrlConnectionProvider());
        createOneLinkHttpTask.setListener(listener);
        createOneLinkHttpTask.setBrandDomain(\u026a);
        AFExecutor.getInstance().getThreadPoolExecutor().execute(createOneLinkHttpTask);
    }
    
    public String getBrandDomain() {
        return this.\u026a;
    }
    
    public String getCampaign() {
        return this.\u01c3;
    }
    
    public String getChannel() {
        return this.\u0399;
    }
    
    public String getMediaSource() {
        return this.\u0131;
    }
    
    public Map<String, String> getParameters() {
        return this.\u027e;
    }
    
    public LinkGenerator setBaseDeeplink(final String \u0268) {
        this.\u0268 = \u0268;
        return this;
    }
    
    public LinkGenerator setBaseURL(String \u0269, final String s, String s2) {
        if (\u0269 != null && \u0269.length() > 0) {
            Label_0032: {
                if (s != null) {
                    s2 = s;
                    if (s.length() >= 5) {
                        break Label_0032;
                    }
                }
                s2 = "go.onelink.me";
            }
            \u0269 = String.format("https://%s/%s", s2, \u0269);
        }
        else {
            \u0269 = String.format("https://%s/%s", ServerConfigHandler.getUrl("%sapp.%s"), s2);
        }
        this.\u0269 = \u0269;
        return this;
    }
    
    public LinkGenerator setBrandDomain(final String \u026a) {
        this.\u026a = \u026a;
        return this;
    }
    
    public LinkGenerator setCampaign(final String \u01c3) {
        this.\u01c3 = \u01c3;
        return this;
    }
    
    public LinkGenerator setChannel(final String \u03b9) {
        this.\u0399 = \u03b9;
        return this;
    }
    
    public LinkGenerator setDeeplinkPath(final String \u0269) {
        this.\u0196 = \u0269;
        return this;
    }
    
    public LinkGenerator setReferrerCustomerId(final String \u0279) {
        this.\u0279 = \u0279;
        return this;
    }
    
    public LinkGenerator setReferrerImageURL(final String \u04cf) {
        this.\u04c0 = \u04cf;
        return this;
    }
    
    public LinkGenerator setReferrerName(final String \u0456) {
        this.\u0406 = \u0456;
        return this;
    }
    
    public LinkGenerator setReferrerUID(final String \u0456) {
        this.\u0456 = \u0456;
        return this;
    }
}
