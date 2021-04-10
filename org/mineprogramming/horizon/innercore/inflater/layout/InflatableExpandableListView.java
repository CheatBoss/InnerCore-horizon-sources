package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableExpandableListView extends InflatableViewBase<ExpandableListView>
{
    private ExpandableListView expandableListView;
    
    public InflatableExpandableListView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.expandableListView = new ExpandableListView(context);
        final String optString = jsonObject.optString("groupIndicator");
        if (!optString.isEmpty()) {
            this.expandableListView.setGroupIndicator(this.getDrawable(optString));
        }
        this.applyProperties(this.expandableListView);
    }
    
    @Override
    public ExpandableListView getView() {
        return this.expandableListView;
    }
}
