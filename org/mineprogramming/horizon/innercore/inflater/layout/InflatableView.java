package org.mineprogramming.horizon.innercore.inflater.layout;

import android.content.*;
import android.view.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

class InflatableView extends InflatableViewBase<View>
{
    private View view;
    
    public InflatableView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.view = new View(context));
    }
    
    @Override
    public View getView() {
        return this.view;
    }
}
