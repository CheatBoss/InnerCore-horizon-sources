package com.appboy.models;

import bo.app.*;
import com.appboy.enums.inappmessage.*;
import org.json.*;

public class InAppMessageSlideup extends InAppMessageBase
{
    private SlideFrom j;
    private int k;
    
    public InAppMessageSlideup() {
        this.j = SlideFrom.BOTTOM;
        this.f = TextAlign.START;
    }
    
    public InAppMessageSlideup(final JSONObject jsonObject, final br br) {
        this(jsonObject, br, ec.a(jsonObject, "slide_from", SlideFrom.class, SlideFrom.BOTTOM), jsonObject.optInt("close_btn_color"));
    }
    
    private InAppMessageSlideup(final JSONObject jsonObject, final br br, final SlideFrom j, final int k) {
        super(jsonObject, br);
        this.j = SlideFrom.BOTTOM;
        this.j = j;
        if (j == null) {
            this.j = SlideFrom.BOTTOM;
        }
        this.k = k;
        this.e = ec.a(jsonObject, "crop_type", CropType.class, CropType.FIT_CENTER);
        this.f = ec.a(jsonObject, "text_align_message", TextAlign.class, TextAlign.START);
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.putOpt("slide_from", (Object)this.j.toString());
            forJsonPut.put("close_btn_color", this.k);
            forJsonPut.put("type", (Object)MessageType.SLIDEUP.name());
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public int getChevronColor() {
        return this.k;
    }
    
    public SlideFrom getSlideFrom() {
        return this.j;
    }
    
    public void setChevronColor(final int k) {
        this.k = k;
    }
    
    public void setSlideFrom(final SlideFrom j) {
        this.j = j;
    }
}
