package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableScrollView extends InflatableViewBase<ScrollView>
{
    private ScrollView scrollView;
    
    public InflatableScrollView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.scrollView = new ScrollView(context));
        this.processChildren((ViewGroup)this.scrollView);
    }
    
    @Override
    public ScrollView getView() {
        return this.scrollView;
    }
}
