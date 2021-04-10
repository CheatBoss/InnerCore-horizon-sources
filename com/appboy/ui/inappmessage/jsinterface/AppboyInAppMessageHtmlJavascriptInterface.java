package com.appboy.ui.inappmessage.jsinterface;

import android.content.*;
import com.appboy.support.*;
import android.webkit.*;
import com.appboy.*;
import java.math.*;
import com.appboy.models.outgoing.*;
import org.json.*;

public class AppboyInAppMessageHtmlJavascriptInterface
{
    private static final String TAG;
    private Context mContext;
    private AppboyInAppMessageHtmlUserJavascriptInterface mUserInterface;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageHtmlJavascriptInterface.class);
    }
    
    public AppboyInAppMessageHtmlJavascriptInterface(final Context mContext) {
        this.mContext = mContext;
        this.mUserInterface = new AppboyInAppMessageHtmlUserJavascriptInterface(mContext);
    }
    
    @JavascriptInterface
    public AppboyInAppMessageHtmlUserJavascriptInterface getUser() {
        return this.mUserInterface;
    }
    
    @JavascriptInterface
    public void logCustomEventWithJSON(final String s, final String s2) {
        Appboy.getInstance(this.mContext).logCustomEvent(s, this.parseProperties(s2));
    }
    
    @JavascriptInterface
    public void logPurchaseWithJSON(final String s, final double n, final String s2, final int n2, final String s3) {
        Appboy.getInstance(this.mContext).logPurchase(s, s2, new BigDecimal(Double.toString(n)), n2, this.parseProperties(s3));
    }
    
    AppboyProperties parseProperties(final String s) {
        if (s != null) {
            try {
                if (!s.equals("undefined") && !s.equals("null")) {
                    return new AppboyProperties(new JSONObject(s));
                }
            }
            catch (Exception ex) {
                final String tag = AppboyInAppMessageHtmlJavascriptInterface.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to parse properties JSON String: ");
                sb.append(s);
                AppboyLogger.e(tag, sb.toString(), ex);
            }
        }
        return null;
    }
    
    @JavascriptInterface
    public void requestImmediateDataFlush() {
        Appboy.getInstance(this.mContext).requestImmediateDataFlush();
    }
}
