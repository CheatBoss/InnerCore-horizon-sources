package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import android.graphics.drawable.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class DrawableInflater
{
    public static Drawable getDrawable(final Context context, final String s) throws JsonInflaterException {
        final JsonInflater.IncludeListener includeListener = JsonInflater.getIncludeListener();
        final JSONObject drawableJson = includeListener.getDrawableJson(context, s);
        if (drawableJson != null) {
            return inflateDrawable(context, drawableJson);
        }
        return (Drawable)new BitmapDrawable(context.getResources(), includeListener.getDrawableBitmap(context, s));
    }
    
    public static Drawable inflateDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        final String optString = jsonObject.optString("type");
        Inflatable inflatable;
        if (optString.equals("shape")) {
            inflatable = new InflatableGradientDrawable(context, jsonObject);
        }
        else if (optString.equals("source")) {
            inflatable = new InflatableSourceDrawable(context, jsonObject);
        }
        else if (optString.equals("layer-list")) {
            inflatable = new InflatableLayerDrawable(context, jsonObject);
        }
        else if (optString.equals("selector")) {
            inflatable = new InflatableStateListDrawable(context, jsonObject);
        }
        else if (optString.equals("rotate")) {
            inflatable = new InflatableRotateDrawable(context, jsonObject);
        }
        else {
            if (!optString.equals("vector")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid drawable type: ");
                sb.append(optString);
                throw new JsonInflaterException(sb.toString());
            }
            inflatable = new InflatableVectorDrawable(context, jsonObject);
        }
        return ((InflatableDrawable<Drawable>)inflatable).getDrawable();
    }
}
