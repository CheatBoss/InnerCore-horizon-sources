package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableRelativeLayout extends InflatableViewBase<RelativeLayout>
{
    private RelativeLayout layout;
    
    public InflatableRelativeLayout(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.layout = new RelativeLayout(context));
        this.processChildren((ViewGroup)this.layout);
    }
    
    @Override
    public RelativeLayout getView() {
        return this.layout;
    }
}
