package com.appboy.models;

import android.net.*;
import android.graphics.*;
import com.appboy.support.*;
import org.json.*;
import java.util.*;
import com.appboy.enums.inappmessage.*;
import bo.app.*;

public abstract class InAppMessageBase implements IInAppMessage
{
    public static final int INAPP_MESSAGE_DURATION_DEFAULT_MILLIS = 5000;
    public static final int INAPP_MESSAGE_DURATION_MIN_MILLIS = 999;
    public static final String IS_CONTROL = "is_control";
    public static final String TYPE = "type";
    protected static final String a;
    private boolean A;
    private boolean B;
    private boolean C;
    private boolean D;
    private String E;
    private long F;
    String b;
    String c;
    String d;
    protected CropType e;
    protected TextAlign f;
    protected boolean g;
    protected JSONObject h;
    protected br i;
    private String j;
    private Map<String, String> k;
    private boolean l;
    private boolean m;
    private ClickAction n;
    private Uri o;
    private DismissType p;
    private int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private String v;
    private String w;
    private Orientation x;
    private Bitmap y;
    private boolean z;
    
    static {
        a = AppboyLogger.getAppboyLogTag(InAppMessageBase.class);
    }
    
    protected InAppMessageBase() {
        this.l = true;
        this.m = true;
        this.n = ClickAction.NONE;
        this.p = DismissType.AUTO_DISMISS;
        this.q = 5000;
        this.r = 0;
        this.s = 0;
        this.t = 0;
        this.u = 0;
        this.x = Orientation.ANY;
        this.z = false;
        this.e = CropType.FIT_CENTER;
        this.f = TextAlign.CENTER;
        this.g = false;
        this.A = false;
        this.B = false;
        this.C = false;
        this.D = false;
        this.F = -1L;
    }
    
    private InAppMessageBase(final String j, final Map<String, String> k, final boolean l, final boolean m, final ClickAction n, final String s, final int r, final int t, final int u, final int s2, final String v, final String w, final DismissType p24, final int durationInMilliseconds, final String b, final String c, final String d, final boolean a, final boolean b2, final Orientation x, final boolean g, final boolean d2, final JSONObject h, final br i) {
        this.l = true;
        this.m = true;
        this.n = ClickAction.NONE;
        this.p = DismissType.AUTO_DISMISS;
        this.q = 5000;
        this.r = 0;
        this.s = 0;
        this.t = 0;
        this.u = 0;
        this.x = Orientation.ANY;
        this.z = false;
        this.e = CropType.FIT_CENTER;
        this.f = TextAlign.CENTER;
        this.g = false;
        this.A = false;
        this.B = false;
        this.C = false;
        this.D = false;
        this.F = -1L;
        this.j = j;
        this.k = k;
        this.l = l;
        this.m = m;
        this.n = n;
        if (n == ClickAction.URI && !StringUtils.isNullOrBlank(s)) {
            this.o = Uri.parse(s);
        }
        if (p24 == DismissType.SWIPE) {
            this.p = DismissType.MANUAL;
        }
        else {
            this.p = p24;
        }
        this.setDurationInMilliseconds(durationInMilliseconds);
        this.r = r;
        this.t = t;
        this.u = u;
        this.s = s2;
        this.v = v;
        this.w = w;
        this.x = x;
        this.b = b;
        this.c = c;
        this.d = d;
        this.A = a;
        this.B = b2;
        this.g = g;
        this.D = d2;
        this.h = h;
        this.i = i;
    }
    
    public InAppMessageBase(final JSONObject jsonObject, final br br) {
        this(jsonObject.optString("message"), ec.a(jsonObject.optJSONObject("extras"), new HashMap<String, String>()), jsonObject.optBoolean("animate_in", true), jsonObject.optBoolean("animate_out", true), ec.a(jsonObject, "click_action", ClickAction.class, ClickAction.NONE), jsonObject.optString("uri"), jsonObject.optInt("bg_color"), jsonObject.optInt("icon_color"), jsonObject.optInt("icon_bg_color"), jsonObject.optInt("text_color"), jsonObject.optString("icon"), jsonObject.optString("image_url"), ec.a(jsonObject, "message_close", DismissType.class, DismissType.AUTO_DISMISS), jsonObject.optInt("duration"), jsonObject.optString("campaign_id"), jsonObject.optString("card_id"), jsonObject.optString("trigger_id"), false, false, ec.a(jsonObject, "orientation", Orientation.class, Orientation.ANY), jsonObject.optBoolean("use_webview", false), jsonObject.optBoolean("is_control"), jsonObject, br);
    }
    
    @Override
    public JSONObject forJsonPut() {
        final JSONObject h = this.h;
        if (h != null) {
            return h;
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("message", (Object)this.j);
            jsonObject.put("duration", this.q);
            jsonObject.putOpt("campaign_id", (Object)this.b);
            jsonObject.putOpt("card_id", (Object)this.c);
            jsonObject.putOpt("trigger_id", (Object)this.d);
            jsonObject.putOpt("click_action", (Object)this.n.toString());
            jsonObject.putOpt("message_close", (Object)this.p.toString());
            if (this.o != null) {
                jsonObject.put("uri", (Object)this.o.toString());
            }
            jsonObject.put("use_webview", this.g);
            jsonObject.put("animate_in", this.l);
            jsonObject.put("animate_out", this.m);
            jsonObject.put("bg_color", this.r);
            jsonObject.put("text_color", this.s);
            jsonObject.put("icon_color", this.t);
            jsonObject.put("icon_bg_color", this.u);
            jsonObject.putOpt("icon", (Object)this.v);
            jsonObject.putOpt("image_url", (Object)this.w);
            jsonObject.putOpt("crop_type", (Object)this.e.toString());
            jsonObject.putOpt("orientation", (Object)this.x.toString());
            jsonObject.putOpt("text_align_message", (Object)this.f.toString());
            jsonObject.putOpt("is_control", (Object)this.D);
            if (this.k != null) {
                final JSONObject jsonObject2 = new JSONObject();
                for (final String s : this.k.keySet()) {
                    jsonObject2.put(s, (Object)this.k.get(s));
                }
                jsonObject.put("extras", (Object)jsonObject2);
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean getAnimateIn() {
        return this.l;
    }
    
    @Override
    public boolean getAnimateOut() {
        return this.m;
    }
    
    @Override
    public int getBackgroundColor() {
        return this.r;
    }
    
    @Override
    public Bitmap getBitmap() {
        return this.y;
    }
    
    @Override
    public ClickAction getClickAction() {
        return this.n;
    }
    
    @Override
    public CropType getCropType() {
        return this.e;
    }
    
    @Override
    public DismissType getDismissType() {
        return this.p;
    }
    
    @Override
    public int getDurationInMilliseconds() {
        return this.q;
    }
    
    @Override
    public long getExpirationTimestamp() {
        return this.F;
    }
    
    @Override
    public Map<String, String> getExtras() {
        return this.k;
    }
    
    @Override
    public String getIcon() {
        return this.v;
    }
    
    @Override
    public int getIconBackgroundColor() {
        return this.u;
    }
    
    @Override
    public int getIconColor() {
        return this.t;
    }
    
    @Override
    public boolean getImageDownloadSuccessful() {
        return this.z;
    }
    
    @Override
    public String getImageUrl() {
        return this.getRemoteImageUrl();
    }
    
    @Override
    public String getLocalImageUrl() {
        return this.E;
    }
    
    @Override
    public String getMessage() {
        return this.j;
    }
    
    @Override
    public TextAlign getMessageTextAlign() {
        return this.f;
    }
    
    @Override
    public int getMessageTextColor() {
        return this.s;
    }
    
    @Override
    public boolean getOpenUriInWebView() {
        return this.g;
    }
    
    @Override
    public Orientation getOrientation() {
        return this.x;
    }
    
    @Override
    public String getRemoteAssetPathForPrefetch() {
        return this.getRemoteImageUrl();
    }
    
    @Override
    public String getRemoteImageUrl() {
        return this.w;
    }
    
    @Override
    public Uri getUri() {
        return this.o;
    }
    
    @Override
    public boolean isControl() {
        return this.D;
    }
    
    @Override
    public boolean logClick() {
        if (StringUtils.isNullOrBlank(this.b) && StringUtils.isNullOrBlank(this.c) && StringUtils.isNullOrBlank(this.d)) {
            AppboyLogger.d(InAppMessageBase.a, "Campaign, card, and trigger Ids not found. Not logging in-app message click.");
            return false;
        }
        if (this.B) {
            AppboyLogger.i(InAppMessageBase.a, "Click already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.C) {
            AppboyLogger.i(InAppMessageBase.a, "Display failure already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageBase.a, "Cannot log an in-app message click because the AppboyManager is null.");
            return false;
        }
        try {
            this.i.a(cg.c(this.b, this.c, this.d));
            return this.B = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
    
    @Override
    public boolean logDisplayFailure(final InAppMessageFailureType inAppMessageFailureType) {
        if (StringUtils.isNullOrBlank(this.b) && StringUtils.isNullOrBlank(this.c) && StringUtils.isNullOrBlank(this.d)) {
            AppboyLogger.d(InAppMessageBase.a, "Campaign, card, and trigger Ids not found. Not logging in-app message display failure.");
            return false;
        }
        if (this.C) {
            AppboyLogger.i(InAppMessageBase.a, "Display failure already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.B) {
            AppboyLogger.i(InAppMessageBase.a, "Click already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.A) {
            AppboyLogger.i(InAppMessageBase.a, "Impression already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageBase.a, "Cannot log an in-app message display failure because the AppboyManager is null.");
            return false;
        }
        try {
            this.i.a(cg.a(this.b, this.c, this.d, inAppMessageFailureType));
            return this.C = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
    
    @Override
    public boolean logImpression() {
        if (StringUtils.isNullOrEmpty(this.b) && StringUtils.isNullOrEmpty(this.c) && StringUtils.isNullOrEmpty(this.d)) {
            AppboyLogger.d(InAppMessageBase.a, "Campaign, card, and trigger Ids not found. Not logging in-app message impression.");
            return false;
        }
        if (this.A) {
            AppboyLogger.i(InAppMessageBase.a, "Impression already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.C) {
            AppboyLogger.i(InAppMessageBase.a, "Display failure already logged for this in-app message. Ignoring.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageBase.a, "Cannot log an in-app message impression because the AppboyManager is null.");
            return false;
        }
        try {
            this.i.a(cg.b(this.b, this.c, this.d));
            return this.A = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
    
    @Override
    public void onAfterClosed() {
        if (this.B && !StringUtils.isNullOrEmpty(this.d)) {
            this.i.a(new fm(this.d));
        }
    }
    
    @Override
    public void setAnimateIn(final boolean l) {
        this.l = l;
    }
    
    @Override
    public void setAnimateOut(final boolean m) {
        this.m = m;
    }
    
    @Override
    public void setBackgroundColor(final int r) {
        this.r = r;
    }
    
    @Override
    public void setBitmap(final Bitmap y) {
        this.y = y;
    }
    
    @Override
    public boolean setClickAction(final ClickAction n) {
        if (n != ClickAction.URI) {
            this.n = n;
            this.o = null;
            return true;
        }
        AppboyLogger.e(InAppMessageBase.a, "A non-null URI is required in order to set the message ClickAction to URI.");
        return false;
    }
    
    @Override
    public boolean setClickAction(final ClickAction clickAction, final Uri o) {
        if (o == null && clickAction == ClickAction.URI) {
            AppboyLogger.e(InAppMessageBase.a, "A non-null URI is required in order to set the message ClickAction to URI.");
            return false;
        }
        if (o != null && clickAction == ClickAction.URI) {
            this.n = clickAction;
            this.o = o;
            return true;
        }
        return this.setClickAction(clickAction);
    }
    
    @Override
    public void setCropType(final CropType e) {
        this.e = e;
    }
    
    @Override
    public void setDismissType(final DismissType p) {
        this.p = p;
    }
    
    @Override
    public void setDurationInMilliseconds(final int q) {
        if (q < 999) {
            this.q = 5000;
            final String a = InAppMessageBase.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested in-app message duration ");
            sb.append(q);
            sb.append(" is lower than the minimum of ");
            sb.append(999);
            sb.append(". Defaulting to ");
            sb.append(this.q);
            sb.append(" milliseconds.");
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.q = q;
        final String a2 = InAppMessageBase.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Set in-app message duration to ");
        sb2.append(this.q);
        sb2.append(" milliseconds.");
        AppboyLogger.d(a2, sb2.toString());
    }
    
    @Override
    public void setExpirationTimestamp(final long f) {
        this.F = f;
    }
    
    @Override
    public void setIcon(final String v) {
        this.v = v;
    }
    
    @Override
    public void setIconBackgroundColor(final int u) {
        this.u = u;
    }
    
    @Override
    public void setIconColor(final int t) {
        this.t = t;
    }
    
    @Override
    public void setImageDownloadSuccessful(final boolean z) {
        this.z = z;
    }
    
    @Override
    public void setImageUrl(final String remoteImageUrl) {
        this.setRemoteImageUrl(remoteImageUrl);
    }
    
    @Override
    public void setLocalAssetPathForPrefetch(final String localImageUrl) {
        this.setLocalImageUrl(localImageUrl);
    }
    
    @Override
    public void setLocalImageUrl(final String e) {
        this.E = e;
    }
    
    @Override
    public void setMessage(final String j) {
        this.j = j;
    }
    
    @Override
    public void setMessageTextAlign(final TextAlign f) {
        this.f = f;
    }
    
    @Override
    public void setMessageTextColor(final int s) {
        this.s = s;
    }
    
    @Override
    public void setOpenUriInWebView(final boolean g) {
        this.g = g;
    }
    
    @Override
    public void setOrientation(final Orientation x) {
        this.x = x;
    }
    
    @Override
    public void setRemoteImageUrl(final String w) {
        this.w = w;
    }
}
