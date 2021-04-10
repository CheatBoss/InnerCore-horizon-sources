package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableSpinner extends InflatableViewBase<Spinner>
{
    private Spinner view;
    
    public InflatableSpinner(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.view = new Spinner(context));
    }
    
    @Override
    public Spinner getView() {
        return this.view;
    }
}
