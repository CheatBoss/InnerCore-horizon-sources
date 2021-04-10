package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import android.util.*;
import android.graphics.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableProgressBar extends InflatableViewBase<ProgressBar>
{
    private ProgressBar progressBar;
    
    public InflatableProgressBar(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        ProgressBar progressBar;
        if (this.style == 0) {
            progressBar = new ProgressBar(context);
        }
        else {
            progressBar = new ProgressBar(context, (AttributeSet)null, this.style);
        }
        this.progressBar = progressBar;
        final String optString = jsonObject.optString("progress");
        if (!optString.isEmpty()) {
            this.progressBar.setProgress(this.getInt(optString));
        }
        final String optString2 = jsonObject.optString("indeterminateTint");
        if (!optString2.isEmpty()) {
            final Drawable indeterminateDrawable = this.progressBar.getIndeterminateDrawable();
            if (indeterminateDrawable != null) {
                indeterminateDrawable.setColorFilter(this.getColor(optString2), PorterDuff$Mode.SRC_IN);
            }
        }
        this.applyProperties(this.progressBar);
    }
    
    @Override
    public ProgressBar getView() {
        return this.progressBar;
    }
}
