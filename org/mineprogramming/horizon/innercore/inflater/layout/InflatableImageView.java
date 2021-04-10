package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableImageView extends InflatableViewBase<ImageView>
{
    private ImageView imageView;
    
    public InflatableImageView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.imageView = new ImageView(context);
        final String optString = jsonObject.optString("src");
        if (!optString.isEmpty()) {
            this.imageView.setImageDrawable(this.getDrawable(optString));
        }
        this.applyProperties(this.imageView);
    }
    
    @Override
    public ImageView getView() {
        return this.imageView;
    }
}
