package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import org.json.*;

public abstract class InflatableDrawable<T extends Drawable> extends Inflatable
{
    public InflatableDrawable(final Context context, final JSONObject jsonObject) {
        super(context, jsonObject);
    }
    
    public abstract T getDrawable();
}
