package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.model.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.support.v7.widget.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class ModList extends Displayable
{
    private RecyclerView modList;
    ModSourceAdapter modSourceAdapter;
    
    protected ModList(final Context context, final ModSource modSource) {
        super(context);
        this.modSourceAdapter = new ModSourceAdapter(context, modSource);
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            (this.modList = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "mod_list")).getViewByJsonId("list")).setLayoutManager((RecyclerView$LayoutManager)new LinearLayoutManager(this.context));
            this.modList.setAdapter((RecyclerView$Adapter)this.modSourceAdapter);
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public void setOnItemClickListener(final ModSourceAdapter.OnItemClickListener onItemClickListener) {
        this.modSourceAdapter.setOnItemClickListener(onItemClickListener);
    }
}
