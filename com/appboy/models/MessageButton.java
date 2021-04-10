package com.appboy.models;

import com.appboy.enums.inappmessage.*;
import android.net.*;
import bo.app.*;
import com.appboy.support.*;
import org.json.*;

public class MessageButton implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private JSONObject b;
    private int c;
    private ClickAction d;
    private Uri e;
    private String f;
    private int g;
    private int h;
    private boolean i;
    
    static {
        a = AppboyLogger.getAppboyLogTag(MessageButton.class);
    }
    
    public MessageButton() {
        this.c = -1;
        this.d = ClickAction.NONE;
        this.g = 0;
        this.h = 0;
    }
    
    public MessageButton(final JSONObject jsonObject) {
        this(jsonObject, jsonObject.optInt("id", -1), ec.a(jsonObject, "click_action", ClickAction.class, ClickAction.NEWS_FEED), jsonObject.optString("uri"), jsonObject.optString("text"), jsonObject.optInt("bg_color"), jsonObject.optInt("text_color"), jsonObject.optBoolean("use_webview", false));
    }
    
    private MessageButton(final JSONObject b, final int c, final ClickAction d, final String s, final String f, final int g, final int h, final boolean i) {
        this.c = -1;
        this.d = ClickAction.NONE;
        this.g = 0;
        this.h = 0;
        this.b = b;
        this.c = c;
        this.d = d;
        if (d == ClickAction.URI && !StringUtils.isNullOrBlank(s)) {
            this.e = Uri.parse(s);
        }
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
    }
    
    @Override
    public JSONObject forJsonPut() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", this.c);
            jsonObject.put("click_action", (Object)this.d.toString());
            if (this.e != null) {
                jsonObject.put("uri", (Object)this.e.toString());
            }
            jsonObject.putOpt("text", (Object)this.f);
            jsonObject.put("bg_color", this.g);
            jsonObject.put("text_color", this.h);
            jsonObject.put("use_webview", this.i);
            return jsonObject;
        }
        catch (JSONException ex) {
            return this.b;
        }
    }
    
    public int getBackgroundColor() {
        return this.g;
    }
    
    public ClickAction getClickAction() {
        return this.d;
    }
    
    public int getId() {
        return this.c;
    }
    
    public boolean getOpenUriInWebview() {
        return this.i;
    }
    
    public String getText() {
        return this.f;
    }
    
    public int getTextColor() {
        return this.h;
    }
    
    public Uri getUri() {
        return this.e;
    }
    
    public void setBackgroundColor(final int g) {
        this.g = g;
    }
    
    public boolean setClickAction(final ClickAction d) {
        if (d != ClickAction.URI) {
            this.d = d;
            this.e = null;
            return true;
        }
        AppboyLogger.e(MessageButton.a, "A non-null URI is required in order to set the button ClickAction to URI.");
        return false;
    }
    
    public boolean setClickAction(final ClickAction clickAction, final Uri e) {
        if (e == null && clickAction == ClickAction.URI) {
            AppboyLogger.e(MessageButton.a, "A non-null URI is required in order to set the button ClickAction to URI.");
            return false;
        }
        if (e != null && clickAction == ClickAction.URI) {
            this.d = clickAction;
            this.e = e;
            return true;
        }
        return this.setClickAction(clickAction);
    }
    
    public void setOpenUriInWebview(final boolean i) {
        this.i = i;
    }
    
    public void setText(final String f) {
        this.f = f;
    }
    
    public void setTextColor(final int h) {
        this.h = h;
    }
}
