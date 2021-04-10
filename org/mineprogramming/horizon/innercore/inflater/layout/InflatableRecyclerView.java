package org.mineprogramming.horizon.innercore.inflater.layout;

import android.support.v7.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableRecyclerView extends InflatableViewBase<RecyclerView>
{
    private RecyclerView recyclerView;
    
    public InflatableRecyclerView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.applyProperties(this.recyclerView = new RecyclerView(context));
    }
    
    @Override
    public RecyclerView getView() {
        return this.recyclerView;
    }
}
