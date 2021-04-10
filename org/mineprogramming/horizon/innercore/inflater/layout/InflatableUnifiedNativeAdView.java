package org.mineprogramming.horizon.innercore.inflater.layout;

import com.google.android.gms.ads.formats.*;
import android.content.*;
import android.view.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

class InflatableUnifiedNativeAdView extends InflatableViewBase<View>
{
    private UnifiedNativeAdView view;
    
    public InflatableUnifiedNativeAdView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties((View)(this.view = new UnifiedNativeAdView(context)));
        this.processChildren((ViewGroup)this.view);
    }
    
    public UnifiedNativeAdView getView() {
        return this.view;
    }
}
