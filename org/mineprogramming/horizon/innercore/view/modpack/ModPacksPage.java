package org.mineprogramming.horizon.innercore.view.modpack;

import com.android.tools.r8.annotations.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import android.support.v7.widget.*;
import android.os.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$ModPacksPage$bmmvD0wufZQ3Lc74t0l7Vmia_nk.class })
public class ModPacksPage extends Page
{
    private RecyclerView packsList;
    
    public ModPacksPage(final PagesManager pagesManager) {
        super(pagesManager);
    }
    
    @Override
    public void display(final ViewGroup viewGroup, final PageState pageState) {
        try {
            this.packsList = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "recycler_list")).getViewByJsonId("list");
            final ModPackSourceAdapter adapter = new ModPackSourceAdapter(this.context, new LocalModPackSource(), new RemoteModPackSource(this.language), new ArchiveModPackSource());
            adapter.setOnItemClickListener(new -$$Lambda$ModPacksPage$bmmvD0wufZQ3Lc74t0l7Vmia_nk(this));
            this.packsList.setLayoutManager((RecyclerView$LayoutManager)new LinearLayoutManager(this.context));
            this.packsList.setAdapter((RecyclerView$Adapter)adapter);
            final Parcelable value = ((HashMap<K, Parcelable>)pageState).get("recyclerState");
            if (value != null) {
                this.packsList.getLayoutManager().onRestoreInstanceState((Parcelable)value);
            }
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    public void onPause(final PageState pageState) {
        super.onPause(pageState);
        ((HashMap<String, Parcelable>)pageState).put("recyclerState", this.packsList.getLayoutManager().onSaveInstanceState());
    }
}
