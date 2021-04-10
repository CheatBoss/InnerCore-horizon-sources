package com.appboy.models;

import bo.app.*;
import com.appboy.enums.inappmessage.*;
import org.json.*;

public class InAppMessageModal extends InAppMessageImmersiveBase
{
    public InAppMessageModal() {
    }
    
    public InAppMessageModal(final JSONObject jsonObject, final br br) {
        super(jsonObject, br);
        Class<CropType> clazz;
        CropType cropType;
        if (this.j.equals(ImageStyle.GRAPHIC)) {
            clazz = CropType.class;
            cropType = CropType.CENTER_CROP;
        }
        else {
            clazz = CropType.class;
            cropType = CropType.FIT_CENTER;
        }
        this.e = ec.a(jsonObject, "crop_type", clazz, cropType);
    }
    
    @Override
    public JSONObject forJsonPut() {
        if (this.h != null) {
            return this.h;
        }
        try {
            final JSONObject forJsonPut = super.forJsonPut();
            forJsonPut.put("type", (Object)MessageType.MODAL.name());
            return forJsonPut;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
