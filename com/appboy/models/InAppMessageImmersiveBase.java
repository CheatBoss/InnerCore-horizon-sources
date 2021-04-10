package com.appboy.models;

import com.appboy.enums.inappmessage.*;
import org.json.*;
import java.util.*;
import com.appboy.support.*;
import bo.app.*;

public abstract class InAppMessageImmersiveBase extends InAppMessageBase implements IInAppMessageImmersive
{
    protected ImageStyle j;
    private String k;
    private int l;
    private int m;
    private List<MessageButton> n;
    private Integer o;
    private TextAlign p;
    private boolean q;
    private String r;
    
    protected InAppMessageImmersiveBase() {
        this.l = 0;
        this.m = 0;
        this.j = ImageStyle.TOP;
        this.o = null;
        this.p = TextAlign.CENTER;
        this.r = null;
    }
    
    public InAppMessageImmersiveBase(final JSONObject jsonObject, final br br) {
        this(jsonObject, br, jsonObject.optString("header"), jsonObject.optInt("header_text_color"), jsonObject.optInt("close_btn_color"), ec.a(jsonObject, "image_style", ImageStyle.class, ImageStyle.TOP), ec.a(jsonObject, "text_align_header", TextAlign.class, TextAlign.CENTER), ec.a(jsonObject, "text_align_message", TextAlign.class, TextAlign.CENTER));
        if (jsonObject.optJSONArray("btns") != null) {
            final ArrayList<MessageButton> messageButtons = new ArrayList<MessageButton>();
            final JSONArray optJSONArray = jsonObject.optJSONArray("btns");
            for (int i = 0; i < optJSONArray.length(); ++i) {
                messageButtons.add(new MessageButton(optJSONArray.optJSONObject(i)));
            }
            this.setMessageButtons(messageButtons);
        }
    }
    
    private InAppMessageImmersiveBase(final JSONObject jsonObject, final br br, final String k, final int l, final int m, final ImageStyle j, final TextAlign p8, final TextAlign f) {
        super(jsonObject, br);
        this.l = 0;
        this.m = 0;
        this.j = ImageStyle.TOP;
        this.o = null;
        this.p = TextAlign.CENTER;
        this.r = null;
        this.k = k;
        this.l = l;
        this.m = m;
        if (jsonObject.has("frame_color")) {
            this.o = jsonObject.optInt("frame_color");
        }
        this.j = j;
        this.p = p8;
        this.f = f;
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.putOpt("header", (Object)this.k);
            forJsonPut.put("header_text_color", this.l);
            forJsonPut.put("close_btn_color", this.m);
            forJsonPut.putOpt("image_style", (Object)this.j.toString());
            forJsonPut.putOpt("text_align_header", (Object)this.p.toString());
            if (this.o != null) {
                forJsonPut.put("frame_color", (int)this.o);
            }
            if (this.n != null) {
                final JSONArray jsonArray = new JSONArray();
                final Iterator<MessageButton> iterator = this.n.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put((Object)iterator.next().forJsonPut());
                }
                forJsonPut.put("btns", (Object)jsonArray);
            }
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public int getCloseButtonColor() {
        return this.m;
    }
    
    @Override
    public Integer getFrameColor() {
        return this.o;
    }
    
    @Override
    public String getHeader() {
        return this.k;
    }
    
    @Override
    public TextAlign getHeaderTextAlign() {
        return this.p;
    }
    
    @Override
    public int getHeaderTextColor() {
        return this.l;
    }
    
    @Override
    public ImageStyle getImageStyle() {
        return this.j;
    }
    
    @Override
    public List<MessageButton> getMessageButtons() {
        return this.n;
    }
    
    @Override
    public boolean logButtonClick(final MessageButton messageButton) {
        if (StringUtils.isNullOrBlank(this.b) && StringUtils.isNullOrBlank(this.c) && StringUtils.isNullOrBlank(this.d)) {
            AppboyLogger.d(InAppMessageImmersiveBase.a, "Campaign, trigger, and card Ids not found. Not logging button click.");
            return false;
        }
        if (messageButton == null) {
            AppboyLogger.w(InAppMessageImmersiveBase.a, "Message button was null. Ignoring button click.");
            return false;
        }
        if (this.q) {
            AppboyLogger.i(InAppMessageImmersiveBase.a, "Button click already logged for this message. Ignoring.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageImmersiveBase.a, "Cannot log a button click because the AppboyManager is null.");
            return false;
        }
        try {
            final cg a = cg.a(this.b, this.c, this.d, messageButton);
            this.r = cg.a(messageButton);
            this.i.a(a);
            return this.q = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
    
    @Override
    public void onAfterClosed() {
        super.onAfterClosed();
        if (this.q && !StringUtils.isNullOrBlank(this.d) && !StringUtils.isNullOrBlank(this.r)) {
            this.i.a(new fm(this.d, this.r));
        }
    }
    
    @Override
    public void setCloseButtonColor(final int m) {
        this.m = m;
    }
    
    @Override
    public void setFrameColor(final Integer o) {
        this.o = o;
    }
    
    @Override
    public void setHeader(final String k) {
        this.k = k;
    }
    
    @Override
    public void setHeaderTextAlign(final TextAlign p) {
        this.p = p;
    }
    
    @Override
    public void setHeaderTextColor(final int l) {
        this.l = l;
    }
    
    @Override
    public void setImageStyle(final ImageStyle j) {
        this.j = j;
    }
    
    @Override
    public void setMessageButtons(final List<MessageButton> n) {
        this.n = n;
    }
}
