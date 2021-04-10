package com.appboy.models;

import org.json.*;
import com.appboy.support.*;
import bo.app.*;

public abstract class InAppMessageHtmlBase extends InAppMessageBase implements IInAppMessageHtml
{
    private String j;
    private String k;
    private boolean l;
    private String m;
    
    protected InAppMessageHtmlBase() {
        this.l = false;
        this.m = null;
        this.g = true;
    }
    
    public InAppMessageHtmlBase(final JSONObject jsonObject, final br br) {
        super(jsonObject, br);
        this.l = false;
        this.m = null;
        if (!StringUtils.isNullOrBlank(jsonObject.optString("zipped_assets_url"))) {
            this.j = jsonObject.optString("zipped_assets_url");
        }
        this.g = jsonObject.optBoolean("use_webview", true);
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.putOpt("zipped_assets_url", (Object)this.j);
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public String getAssetsZipRemoteUrl() {
        return this.j;
    }
    
    @Override
    public String getLocalAssetsDirectoryUrl() {
        return this.k;
    }
    
    @Override
    public String getRemoteAssetPathForPrefetch() {
        return this.getAssetsZipRemoteUrl();
    }
    
    @Override
    public boolean logButtonClick(final String m) {
        if (StringUtils.isNullOrEmpty(this.b) && StringUtils.isNullOrEmpty(this.c) && StringUtils.isNullOrEmpty(this.d)) {
            AppboyLogger.d(InAppMessageHtmlBase.a, "Campaign, card, and trigger Ids not found. Not logging html in-app message click.");
            return false;
        }
        if (StringUtils.isNullOrBlank(m)) {
            AppboyLogger.i(InAppMessageHtmlBase.a, "Button Id was null or blank for this html in-app message. Ignoring.");
            return false;
        }
        if (this.l) {
            AppboyLogger.i(InAppMessageHtmlBase.a, "Button click already logged for this html in-app message. Ignoring.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageHtmlBase.a, "Cannot log an html in-app message button click because the AppboyManager is null.");
            return false;
        }
        try {
            this.i.a(cg.a(this.b, this.c, this.d, m));
            this.m = m;
            return this.l = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
    
    @Override
    public void onAfterClosed() {
        super.onAfterClosed();
        if (this.l && !StringUtils.isNullOrBlank(this.d) && !StringUtils.isNullOrBlank(this.m)) {
            this.i.a(new fm(this.d, this.m));
        }
    }
    
    @Override
    public void setAssetsZipRemoteUrl(final String j) {
        this.j = j;
    }
    
    @Override
    public void setLocalAssetPathForPrefetch(final String localAssetsDirectoryUrl) {
        this.setLocalAssetsDirectoryUrl(localAssetsDirectoryUrl);
    }
    
    @Override
    public void setLocalAssetsDirectoryUrl(final String k) {
        this.k = k;
    }
}
