package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableFrameLayout extends InflatableViewBase<FrameLayout>
{
    private FrameLayout layout;
    
    public InflatableFrameLayout(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.layout = new FrameLayout(context));
        this.processChildren((ViewGroup)this.layout);
    }
    
    @Override
    public FrameLayout getView() {
        return this.layout;
    }
}
