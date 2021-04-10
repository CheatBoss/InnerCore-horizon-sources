package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.graphics.drawable.*;

class InflatableVectorDrawable extends InflatableDrawable<VectorDrawable>
{
    VectorDrawable drawable;
    
    public InflatableVectorDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, jsonObject);
        this.drawable = new VectorDrawable();
    }
    
    @Override
    public VectorDrawable getDrawable() {
        return this.drawable;
    }
}
