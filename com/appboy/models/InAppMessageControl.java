package com.appboy.models;

import com.appboy.support.*;
import bo.app.*;
import org.json.*;

public class InAppMessageControl extends InAppMessageBase
{
    private boolean j;
    
    public InAppMessageControl(final JSONObject jsonObject, final br br) {
        super(jsonObject, br);
        this.j = false;
    }
    
    @Override
    public boolean logImpression() {
        if (this.j) {
            AppboyLogger.i(InAppMessageControl.a, "Control impression already logged for this in-app message. Ignoring.");
            return false;
        }
        if (StringUtils.isNullOrEmpty(this.d)) {
            AppboyLogger.w(InAppMessageControl.a, "Trigger Id not found. Not logging in-app message control impression.");
            return false;
        }
        if (this.i == null) {
            AppboyLogger.e(InAppMessageControl.a, "Cannot log an in-app message control impression because the AppboyManager is null.");
            return false;
        }
        try {
            AppboyLogger.v(InAppMessageControl.a, "Logging control in-app message impression event");
            this.i.a(cg.a(this.b, this.c, this.d));
            return this.j = true;
        }
        catch (JSONException ex) {
            this.i.a((Throwable)ex);
            return false;
        }
    }
}
