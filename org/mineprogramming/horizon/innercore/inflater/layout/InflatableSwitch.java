package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableSwitch extends InflatableViewBase<Switch>
{
    private Switch mSwitch;
    
    public InflatableSwitch(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.mSwitch = new Switch(context));
    }
    
    @Override
    public Switch getView() {
        return this.mSwitch;
    }
}
