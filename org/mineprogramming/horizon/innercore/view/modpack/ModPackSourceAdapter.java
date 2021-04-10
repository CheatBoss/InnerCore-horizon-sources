package org.mineprogramming.horizon.innercore.view.modpack;

import android.support.v7.widget.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class ModPackSourceAdapter extends RecyclerView$Adapter<RecyclerView$ViewHolder>
{
    private static final int VIEWTYPE_LOADER = 1;
    private static final int VIEWTYPE_PACK = 0;
    private static final int VIEWTYPE_SECTION = 2;
    private final ItemSource archived;
    private Context context;
    private OnItemClickListener itemClickListener;
    private boolean loadFailed;
    private boolean loadedLast;
    LoaderViewHolder loaderHolder;
    private final ItemSource local;
    private boolean packLocal;
    private final ItemSource remote;
    
    public ModPackSourceAdapter(final Context context, final ItemSource local, final ItemSource remote, final ItemSource archived) {
        this.loadedLast = false;
        this.loadFailed = false;
        this.packLocal = false;
        this.context = context;
        this.local = local;
        this.archived = archived;
        (this.remote = remote).setOnChangeListener((ItemSource.ModSourceListener)new ItemSource.ModSourceListener() {
            @Override
            public void onChange() {
                ModPackSourceAdapter.this.notifyDataSetChanged();
            }
            
            @Override
            public void onLoadFailed() {
                ModPackSourceAdapter.this.loadFailed = true;
                if (ModPackSourceAdapter.this.loaderHolder != null) {
                    ModPackSourceAdapter.this.loaderHolder.onDownloadFailed();
                }
            }
            
            @Override
            public void onLoadInProgress() {
                ModPackSourceAdapter.this.loadFailed = false;
                if (ModPackSourceAdapter.this.loaderHolder != null) {
                    ModPackSourceAdapter.this.loaderHolder.onDownloadInProgress();
                }
            }
            
            @Override
            public void onLoadLast() {
                ModPackSourceAdapter.this.loadedLast = true;
                if (ModPackSourceAdapter.this.loaderHolder != null) {
                    ModPackSourceAdapter.this.loaderHolder.onDownloadFinished();
                    ModPackSourceAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }
    
    private Item getPackByIndex(final int n) {
        this.packLocal = false;
        if (this.archived.getItemCount() > 0) {
            if (n < this.local.getItemCount() + 1) {
                return this.local.get(n - 1);
            }
            if (n < this.local.getItemCount() + this.archived.getItemCount() + 2) {
                return this.archived.get(n - 2 - this.local.getItemCount());
            }
            return this.remote.get(n - 3 - this.local.getItemCount() - this.archived.getItemCount());
        }
        else {
            if (n < this.local.getItemCount() + 1) {
                return this.local.get(n - 1);
            }
            return this.remote.get(n - 2 - this.local.getItemCount());
        }
    }
    
    private String getSectionTitle(final int n) {
        if (n == 0) {
            return "section_installed_modpacks";
        }
        if (n == this.local.getItemCount() + 1 && this.archived.getItemCount() > 0) {
            return "section_archived_modpacks";
        }
        return "section_download_modpacks";
    }
    
    public int getItemCount() {
        final int itemCount = this.local.getItemCount();
        final int itemCount2 = this.archived.getItemCount();
        final int itemCount3 = this.remote.getItemCount();
        int n;
        if (this.archived.getItemCount() > 0) {
            n = 3;
        }
        else {
            n = 2;
        }
        return itemCount + itemCount2 + itemCount3 + n + ((this.loadedLast ^ true) ? 1 : 0);
    }
    
    public int getItemViewType(final int n) {
        if (this.archived.getItemCount() > 0) {
            if (n == 0 || n == this.local.getItemCount() + 1) {
                return 2;
            }
            if (n == this.local.getItemCount() + this.archived.getItemCount() + 2) {
                return 2;
            }
            if (n == this.local.getItemCount() + this.archived.getItemCount() + this.remote.getItemCount() + 3) {
                return 1;
            }
        }
        else {
            if (n == 0) {
                return 2;
            }
            if (n == this.local.getItemCount() + 1) {
                return 2;
            }
            if (n == this.local.getItemCount() + this.remote.getItemCount() + 2) {
                return 1;
            }
        }
        return 0;
    }
    
    public void onBindViewHolder(final RecyclerView$ViewHolder recyclerView$ViewHolder, final int n) {
        if (recyclerView$ViewHolder instanceof ModPackViewHolder) {
            final ModPackViewHolder modPackViewHolder = (ModPackViewHolder)recyclerView$ViewHolder;
            final ModPackItem modPackItem = (ModPackItem)this.getPackByIndex(n);
            if (modPackItem.isDefault()) {
                modPackItem.setTitle(JsonInflater.getString(this.context, "default_pacK_title"));
                modPackItem.setDescriptionShort(JsonInflater.getString(this.context, "default_pacK_descr"));
            }
            final String title = modPackItem.getTitle();
            final String description = modPackItem.getDescription();
            String string;
            if (modPackItem.isOptimized()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("<font color='#");
                String s;
                if (modPackItem.isNetworkAdapted()) {
                    s = "1B3BDB";
                }
                else {
                    s = "1BDB68";
                }
                sb.append(s);
                sb.append("'>");
                final Context context = this.context;
                String s2;
                if (modPackItem.isNetworkAdapted()) {
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
            sb2.append(title);
            sb2.append(" ");
            sb2.append(string);
            modPackViewHolder.setName(InflaterUtils.fromHtml(sb2.toString()));
            modPackViewHolder.setDescription(description);
            modPackViewHolder.loadImage(modPackItem.getIcon());
            modPackViewHolder.setSelected(modPackItem.isSelected() && this.local.contains(modPackItem));
            modPackViewHolder.setModsCount(modPackItem.getModsCount());
            modPackViewHolder.setTags(modPackItem.getTags());
            modPackViewHolder.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (ModPackSourceAdapter.this.itemClickListener != null) {
                        ModPackSourceAdapter.this.itemClickListener.onItemClick(modPackItem);
                    }
                }
            });
            if (ModsManagerBanner.getInstance().shouldDisplayNativeAfter(n)) {
                modPackViewHolder.showAd();
            }
            else {
                modPackViewHolder.hideAd();
            }
        }
        else if (recyclerView$ViewHolder instanceof LoaderViewHolder) {
            (this.loaderHolder = (LoaderViewHolder)recyclerView$ViewHolder).setOnRetryClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ModPackSourceAdapter.this.remote.retryLoad();
                }
            });
            if (this.loadFailed) {
                this.loaderHolder.onDownloadFailed();
            }
            if (this.loadedLast) {
                this.loaderHolder.onDownloadFinished();
            }
        }
        else if (recyclerView$ViewHolder instanceof SectionViewHolder) {
            ((SectionViewHolder)recyclerView$ViewHolder).setTitle(JsonInflater.getString(this.context, this.getSectionTitle(n)));
        }
        if (n == this.getItemCount() - 6) {
            this.remote.requestMore();
        }
    }
    
    public RecyclerView$ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Label_0059: {
            switch (n) {
                default: {
                    break Label_0059;
                }
                case 2: {
                    break Label_0059;
                }
                case 1: {
                    break Label_0059;
                }
                case 0: {
                    Label_0087: {
                        break Label_0087;
                        try {
                            return new SectionViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "section_layout")));
                            return new LoaderViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "include_progress")));
                            return new ModPackViewHolder(this.context, JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "pack_item")));
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
    
    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
