package com.appboy.models;

import bo.app.*;
import com.appboy.enums.inappmessage.*;
import org.json.*;

public class InAppMessageFull extends InAppMessageImmersiveBase
{
    public InAppMessageFull() {
        this.e = CropType.CENTER_CROP;
    }
    
    public InAppMessageFull(final JSONObject jsonObject, final br br) {
        super(jsonObject, br);
        this.e = ec.a(jsonObject, "crop_type", CropType.class, CropType.CENTER_CROP);
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.put("type", (Object)MessageType.FULL.name());
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
