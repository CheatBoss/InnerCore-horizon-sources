package org.mineprogramming.horizon.innercore.inflater.layout;

import com.google.android.gms.ads.formats.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableMediaView extends InflatableViewBase<MediaView>
{
    private MediaView view;
    
    public InflatableMediaView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.view = new MediaView(context));
    }
    
    @Override
    public MediaView getView() {
        return this.view;
    }
}
