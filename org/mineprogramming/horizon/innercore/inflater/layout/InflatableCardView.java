package org.mineprogramming.horizon.innercore.inflater.layout;

import android.support.v7.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableCardView extends InflatableViewBase<CardView>
{
    private CardView layout;
    
    public InflatableCardView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.layout = new CardView(context));
        this.processChildren((ViewGroup)this.layout);
    }
    
    @Override
    public CardView getView() {
        return this.layout;
    }
}
