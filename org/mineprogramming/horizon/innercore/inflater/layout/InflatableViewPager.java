package org.mineprogramming.horizon.innercore.inflater.layout;

import android.support.v4.view.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableViewPager extends InflatableViewBase<ViewPager>
{
    private ViewPager viewPager;
    
    public InflatableViewPager(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.viewPager = new ViewPager(context));
    }
    
    @Override
    public ViewPager getView() {
        return this.viewPager;
    }
}
