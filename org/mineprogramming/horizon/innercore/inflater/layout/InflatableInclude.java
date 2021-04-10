package org.mineprogramming.horizon.innercore.inflater.layout;

import android.content.*;
import android.view.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

class InflatableInclude extends InflatableViewBase<View>
{
    private View view;
    
    protected InflatableInclude(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.view = LayoutInflater.inflateView(context, viewGroup, inflatedView, JsonInflater.getIncludeListener().includeLayout(context, jsonObject.optString("name")));
    }
    
    @Override
    public View getView() {
        return this.view;
    }
}
