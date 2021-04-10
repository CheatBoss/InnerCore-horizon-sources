package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.json.*;

class InflatableLayerDrawable extends InflatableDrawable<LayerDrawable>
{
    LayerDrawable drawable;
    
    public InflatableLayerDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, jsonObject);
        final JSONArray optJSONArray = jsonObject.optJSONArray("items");
        final int length = optJSONArray.length();
        final Drawable[] array = new Drawable[length];
        final int n = 0;
        int i = 0;
        while (i < length) {
            try {
                array[i] = DrawableInflater.inflateDrawable(context, optJSONArray.getJSONObject(i));
                ++i;
                continue;
            }
            catch (JSONException ex) {
                throw new JsonInflaterException("Unable to inflate layer item", (Throwable)ex);
            }
            break;
        }
        this.drawable = new LayerDrawable(array);
        int j = n;
        while (j < length) {
            try {
                final JSONObject jsonObject2 = optJSONArray.getJSONObject(j);
                if (this.api(23)) {
                    final String optString = jsonObject2.optString("top");
                    if (!optString.isEmpty()) {
                        this.drawable.setLayerInsetTop(j, this.getSize(optString));
                    }
                    final String optString2 = jsonObject2.optString("right");
                    if (!optString2.isEmpty()) {
                        this.drawable.setLayerInsetRight(j, this.getSize(optString2));
                    }
                    final String optString3 = jsonObject2.optString("bottom");
                    if (!optString3.isEmpty()) {
                        this.drawable.setLayerInsetBottom(j, this.getSize(optString3));
                    }
                    final String optString4 = jsonObject2.optString("left");
                    if (!optString4.isEmpty()) {
                        this.drawable.setLayerInsetLeft(j, this.getSize(optString4));
                    }
                }
                ++j;
                continue;
            }
            catch (JSONException ex2) {
                throw new JsonInflaterException("Unable to inflate layer item", (Throwable)ex2);
            }
            break;
        }
    }
    
    @Override
    public LayerDrawable getDrawable() {
        return this.drawable;
    }
}
