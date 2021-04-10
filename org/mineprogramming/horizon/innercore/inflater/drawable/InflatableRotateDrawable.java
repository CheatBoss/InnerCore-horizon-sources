package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.graphics.drawable.*;

class InflatableRotateDrawable extends InflatableDrawable<RotateDrawable>
{
    RotateDrawable drawable;
    
    public InflatableRotateDrawable(final Context context, JSONObject optJSONObject) throws JsonInflaterException {
        super(context, optJSONObject);
        this.drawable = new RotateDrawable();
        final String optString = optJSONObject.optString("fromDegrees");
        if (!optString.isEmpty()) {
            this.drawable.setFromDegrees(this.getFloat(optString));
        }
        final String optString2 = optJSONObject.optString("toDegrees");
        if (!optString2.isEmpty()) {
            this.drawable.setToDegrees(this.getFloat(optString2));
        }
        final String optString3 = optJSONObject.optString("pivotX");
        if (!optString3.isEmpty()) {
            if (optString3.matches("^-?\\d+%$")) {
                this.drawable.setPivotXRelative(true);
                this.drawable.setPivotX(this.getFloat(optString3.substring(0, optString3.length() - 1)) / 100.0f);
            }
            else {
                this.drawable.setPivotXRelative(false);
                this.drawable.setPivotX(this.getFloat(optString3));
            }
        }
        final String optString4 = optJSONObject.optString("pivotY");
        if (!optString4.isEmpty()) {
            if (optString4.matches("^-?\\d+%$")) {
                this.drawable.setPivotYRelative(true);
                this.drawable.setPivotY(this.getFloat(optString4.substring(0, optString4.length() - 1)) / 100.0f);
            }
            else {
                this.drawable.setPivotYRelative(false);
                this.drawable.setPivotY(this.getFloat(optString4));
            }
        }
        optJSONObject = optJSONObject.optJSONObject("drawable");
        if (optJSONObject == null) {
            throw new JsonInflaterException("Unable to inflate rotate drawable, no child drawable specified");
        }
        this.drawable.setDrawable(DrawableInflater.inflateDrawable(context, optJSONObject));
    }
    
    @Override
    public RotateDrawable getDrawable() {
        return this.drawable;
    }
}
