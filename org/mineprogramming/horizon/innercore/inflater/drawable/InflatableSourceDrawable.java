package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.graphics.drawable.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

class InflatableSourceDrawable extends InflatableDrawable<Drawable>
{
    Drawable drawable;
    
    public InflatableSourceDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, jsonObject);
        this.drawable = this.getDrawable(jsonObject.optString("src"));
    }
    
    @Override
    public Drawable getDrawable() {
        return this.drawable;
    }
}
