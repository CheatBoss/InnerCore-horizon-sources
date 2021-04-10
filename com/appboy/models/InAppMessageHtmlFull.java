package com.appboy.models;

import bo.app.*;
import com.appboy.enums.inappmessage.*;
import org.json.*;

public class InAppMessageHtmlFull extends InAppMessageHtmlBase
{
    public InAppMessageHtmlFull() {
    }
    
    public InAppMessageHtmlFull(final JSONObject jsonObject, final br br) {
        super(jsonObject, br);
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.put("type", (Object)MessageType.HTML_FULL.name());
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
