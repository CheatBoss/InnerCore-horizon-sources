package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.graphics.drawable.*;

class InflatableGradientDrawable extends InflatableDrawable<GradientDrawable>
{
    GradientDrawable drawable;
    
    public InflatableGradientDrawable(final Context context, JSONObject optJSONObject) throws JsonInflaterException {
        super(context, optJSONObject);
        this.drawable = new GradientDrawable();
        final String optString = optJSONObject.optString("shape");
        if (optString.equals("oval")) {
            this.drawable.setShape(1);
        }
        else if (optString.equals("line")) {
            this.drawable.setShape(2);
        }
        else if (optString.equals("ring")) {
            this.drawable.setShape(3);
        }
        else {
            this.drawable.setShape(0);
        }
        final JSONObject optJSONObject2 = optJSONObject.optJSONObject("solid");
        if (optJSONObject2 != null) {
            final String optString2 = optJSONObject2.optString("color");
            if (!optString2.isEmpty()) {
                this.drawable.setColor(this.getColor(optString2));
            }
        }
        final JSONObject optJSONObject3 = optJSONObject.optJSONObject("size");
        if (optJSONObject3 != null) {
            this.drawable.setSize(this.getSize(optJSONObject3.optString("width")), this.getSize(optJSONObject3.optString("height")));
        }
        final JSONObject optJSONObject4 = optJSONObject.optJSONObject("corners");
        if (optJSONObject4 != null) {
            final String optString3 = optJSONObject4.optString("radius");
            if (!optString3.isEmpty()) {
                this.drawable.setCornerRadius((float)this.getSize(optString3));
            }
        }
        optJSONObject = optJSONObject.optJSONObject("stroke");
        if (optJSONObject != null) {
            this.drawable.setStroke(this.getSize(optJSONObject.optString("width")), this.getColor(optJSONObject.optString("color")));
        }
    }
    
    @Override
    public GradientDrawable getDrawable() {
        return this.drawable;
    }
}
