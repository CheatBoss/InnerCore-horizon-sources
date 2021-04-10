package org.mineprogramming.horizon.innercore.view.mod;

import android.support.v7.widget.*;
import com.android.tools.r8.annotations.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import org.mineprogramming.horizon.innercore.view.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

@SynthesizedClassMap({ -$$Lambda$ModSourceAdapter$OC9XuLy-FhaN5fgZszek1zEs0Pg.class, -$$Lambda$ModSourceAdapter$umoe5ivb8EPw3XGMxjgbr7bcM0M.class })
public class ModSourceAdapter extends RecyclerView$Adapter<RecyclerView$ViewHolder>
{
    private static final int VIEWTYPE_LOADER = 1;
    private static final int VIEWTYPE_MOD = 0;
    Context context;
    private boolean displaySettings;
    private OnItemClickListener itemClickListener;
    private OnItemClickListener itemSettingsClickListener;
    private boolean loadFailed;
    private boolean loadedLast;
    LoaderViewHolder loaderHolder;
    private ItemSource modSource;
    
    public ModSourceAdapter(final Context context, final ItemSource modSource) {
        this.loadedLast = false;
        this.loadFailed = false;
        this.displaySettings = true;
        this.context = context;
        (this.modSource = modSource).setOnChangeListener((ItemSource.ModSourceListener)new ItemSource.ModSourceListener() {
            @Override
            public void onChange() {
                ModSourceAdapter.this.notifyDataSetChanged();
            }
            
            @Override
            public void onLoadFailed() {
                ModSourceAdapter.this.loadFailed = true;
                if (ModSourceAdapter.this.loaderHolder != null) {
                    ModSourceAdapter.this.loaderHolder.onDownloadFailed();
                }
            }
            
            @Override
            public void onLoadInProgress() {
                ModSourceAdapter.this.loadFailed = false;
                if (ModSourceAdapter.this.loaderHolder != null) {
                    ModSourceAdapter.this.loaderHolder.onDownloadInProgress();
                }
            }
            
            @Override
            public void onLoadLast() {
                ModSourceAdapter.this.loadedLast = true;
                if (ModSourceAdapter.this.loaderHolder != null) {
                    ModSourceAdapter.this.loaderHolder.onDownloadFinished();
                    ModSourceAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }
    
    public int getItemCount() {
        return this.modSource.getItemCount() + ((this.loadedLast ^ true) ? 1 : 0);
    }
    
    public int getItemViewType(final int n) {
        if (n == this.modSource.getItemCount()) {
            return 1;
        }
        return 0;
    }
    
    public void onBindViewHolder(final RecyclerView$ViewHolder recyclerView$ViewHolder, final int n) {
        if (recyclerView$ViewHolder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder)recyclerView$ViewHolder;
            final Item value = this.modSource.get(n);
            String string;
            if (value.isOptimized()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("<font color='#");
                String s;
                if (value.isNetworkAdapted()) {
                    s = "1B3BDB";
                }
                else {
                    s = "1BDB68";
                }
                sb.append(s);
                sb.append("'>");
                final Context context = this.context;
                String s2;
                if (value.isNetworkAdapted()) {
                    s2 = "optimized_and_network_adapted";
                }
                else {
                    s2 = "optimized";
                }
                sb.append(JsonInflater.getString(context, s2));
                sb.append("</font>");
                string = sb.toString();
            }
            else {
                string = "";
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(value.getTitle());
            sb2.append(" ");
            sb2.append(string);
            itemViewHolder.setName(InflaterUtils.fromHtml(sb2.toString()));
            itemViewHolder.setDescription(value.getDescription());
            itemViewHolder.loadImage(value.getIcon());
            itemViewHolder.setOnClickListener((View$OnClickListener)new -$$Lambda$ModSourceAdapter$OC9XuLy-FhaN5fgZszek1zEs0Pg(this, value));
            itemViewHolder.setOnSettingsClickListener((View$OnClickListener)new -$$Lambda$ModSourceAdapter$umoe5ivb8EPw3XGMxjgbr7bcM0M(this, value));
            if (ModsManagerBanner.getInstance().shouldDisplayNativeAfter(n)) {
                itemViewHolder.showAd();
            }
            else {
                itemViewHolder.hideAd();
            }
        }
        else if (recyclerView$ViewHolder instanceof LoaderViewHolder) {
            (this.loaderHolder = (LoaderViewHolder)recyclerView$ViewHolder).setOnRetryClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ModSourceAdapter.this.modSource.retryLoad();
                }
            });
            if (this.loadFailed) {
                this.loaderHolder.onDownloadFailed();
            }
            if (this.loadedLast) {
                this.loaderHolder.onDownloadFinished();
            }
        }
        if (n == this.modSource.getItemCount() - 5) {
            this.modSource.requestMore();
        }
    }
    
    public RecyclerView$ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Label_0027: {
            switch (n) {
                default: {
                    break Label_0027;
                }
                case 1: {
                    break Label_0027;
                }
                case 0: {
                    Label_0054: {
                        break Label_0054;
                        try {
                            return new LoaderViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "include_progress")));
                            Label_0092: {
                                final InflatedView inflateLayout;
                                return new ItemViewHolder(this.context, inflateLayout);
                            }
                            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "mod_item"));
                            // iftrue(Label_0092:, this.displaySettings)
                            inflateLayout.getViewByJsonId("settings").setVisibility(8);
                            return new ItemViewHolder(this.context, inflateLayout);
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unable to create view holder of type ");
                            sb.append(n);
                            throw new RuntimeException(sb.toString());
                        }
                        catch (JSONException | JsonInflaterException ex) {
                            final Object o;
                            throw new RuntimeException((Throwable)o);
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public void setDisplaySettings(final boolean displaySettings) {
        this.displaySettings = displaySettings;
    }
    
    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    
    public void setOnItemSettingsClickListener(final OnItemClickListener itemSettingsClickListener) {
        this.itemSettingsClickListener = itemSettingsClickListener;
    }
}
