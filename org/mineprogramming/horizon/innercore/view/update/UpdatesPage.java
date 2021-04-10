package org.mineprogramming.horizon.innercore.view.update;

import com.android.tools.r8.annotations.*;
import org.mineprogramming.horizon.innercore.view.mod.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.modpack.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import android.support.v7.widget.*;
import android.os.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$UpdatesPage$s3UcVf66xgeIoTHcwAM3VSV4DQM.class })
public class UpdatesPage extends Page implements UpdatesListener
{
    private UpdateSourceAdapter adapter;
    private RecyclerView itemsList;
    private UpdateSource updatesSource;
    
    public UpdatesPage(final PagesManager pagesManager) {
        super(pagesManager);
        (this.updatesSource = UpdateSource.getInstance(this.language)).addUpdateListener((UpdateSource.UpdatesListener)this);
    }
    
    @Override
    public void display(final ViewGroup viewGroup, final PageState pageState) {
        try {
            this.itemsList = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "page_updates")).getViewByJsonId("list");
            (this.adapter = new UpdateSourceAdapter(this.context, this.updatesSource)).setOnItemClickListener(new -$$Lambda$UpdatesPage$s3UcVf66xgeIoTHcwAM3VSV4DQM(this));
            this.itemsList.setLayoutManager((RecyclerView$LayoutManager)new LinearLayoutManager(this.context));
            this.itemsList.setAdapter((RecyclerView$Adapter)this.adapter);
            final Parcelable value = ((HashMap<K, Parcelable>)pageState).get("recyclerState");
            if (value != null) {
                this.itemsList.getLayoutManager().onRestoreInstanceState((Parcelable)value);
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
        this.updatesSource.removeUpdateListener((UpdateSource.UpdatesListener)this);
        pageState.clear();
        ((HashMap<String, Parcelable>)pageState).put("recyclerState", this.itemsList.getLayoutManager().onSaveInstanceState());
    }
    
    @Override
    public void onUpdatesCountChanged(final int n) {
        this.adapter.notifyDataSetChanged();
    }
}
