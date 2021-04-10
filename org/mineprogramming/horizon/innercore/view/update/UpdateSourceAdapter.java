package org.mineprogramming.horizon.innercore.view.update;

import android.support.v7.widget.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class UpdateSourceAdapter extends RecyclerView$Adapter<RecyclerView$ViewHolder>
{
    private static final int VIEWTYPE_ITEM = 0;
    private static final int VIEWTYPE_LOADER = 1;
    private static final int VIEWTYPE_SECTION = 2;
    private Context context;
    private OnItemClickListener itemClickListener;
    LoaderViewHolder loaderHolder;
    private final UpdateSource updateSource;
    
    public UpdateSourceAdapter(final Context context, final UpdateSource updateSource) {
        this.context = context;
        this.updateSource = updateSource;
    }
    
    private Item getItemByPosition(final int n) {
        final int packItemsCount = this.updateSource.getPackItemsCount();
        int n2 = 0;
        if (packItemsCount > 0 && n < (n2 = 0 + (packItemsCount + 1))) {
            return this.updateSource.getPackItem(n - 1);
        }
        return this.updateSource.getModItem(n - n2 - 1);
    }
    
    public int getItemCount() {
        if (!this.updateSource.isReady()) {
            return 1;
        }
        final int packItemsCount = this.updateSource.getPackItemsCount();
        final int modItemsCount = this.updateSource.getModItemsCount();
        int n = 0;
        int n2;
        if (packItemsCount > 0) {
            n2 = packItemsCount + 1;
        }
        else {
            n2 = 0;
        }
        if (modItemsCount > 0) {
            n = modItemsCount + 1;
        }
        return n2 + n;
    }
    
    public int getItemViewType(final int n) {
        if (!this.updateSource.isReady()) {
            return 1;
        }
        final int packItemsCount = this.updateSource.getPackItemsCount();
        final int modItemsCount = this.updateSource.getModItemsCount();
        int n2 = 0;
        if (packItemsCount > 0) {
            if (n == 0) {
                return 2;
            }
            if (n < (n2 = 0 + (packItemsCount + 1))) {
                return 0;
            }
        }
        if (modItemsCount > 0 && n == n2) {
            return 2;
        }
        return 0;
    }
    
    public void onBindViewHolder(final RecyclerView$ViewHolder recyclerView$ViewHolder, int n) {
        if (recyclerView$ViewHolder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder)recyclerView$ViewHolder;
            final Item itemByPosition = this.getItemByPosition(n);
            final String title = itemByPosition.getTitle();
            final String description = itemByPosition.getDescription();
            String string;
            if (itemByPosition.isOptimized()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("<font color='#");
                String s;
                if (itemByPosition.isNetworkAdapted()) {
                    s = "1B3BDB";
                }
                else {
                    s = "1BDB68";
                }
                sb.append(s);
                sb.append("'>");
                final Context context = this.context;
                String s2;
                if (itemByPosition.isNetworkAdapted()) {
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
            itemViewHolder.setName(InflaterUtils.fromHtml(sb2.toString()));
            itemViewHolder.setDescription(description);
            itemViewHolder.loadImage(itemByPosition.getIcon());
            itemViewHolder.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (UpdateSourceAdapter.this.itemClickListener != null) {
                        UpdateSourceAdapter.this.itemClickListener.onItemClick(itemByPosition);
                    }
                }
            });
            if (ModsManagerBanner.getInstance().shouldDisplayNativeAfter(n)) {
                itemViewHolder.showAd();
            }
            else {
                itemViewHolder.hideAd();
            }
            return;
        }
        if (recyclerView$ViewHolder instanceof LoaderViewHolder) {
            (this.loaderHolder = (LoaderViewHolder)recyclerView$ViewHolder).setOnRetryClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                }
            });
            return;
        }
        if (recyclerView$ViewHolder instanceof SectionViewHolder) {
            final SectionViewHolder sectionViewHolder = (SectionViewHolder)recyclerView$ViewHolder;
            if (this.updateSource.getPackItemsCount() > 0 && n == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            final Context context2 = this.context;
            String s3;
            if (n != 0) {
                s3 = "mod_packs";
            }
            else {
                s3 = "mods";
            }
            sectionViewHolder.setTitle(JsonInflater.getString(context2, s3));
        }
    }
    
    public RecyclerView$ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Label_0058: {
            switch (n) {
                default: {
                    break Label_0058;
                }
                case 2: {
                    break Label_0058;
                }
                case 1: {
                    break Label_0058;
                }
                case 0: {
                    Label_0085: {
                        break Label_0085;
                        try {
                            return new SectionViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "section_layout")));
                            return new ItemViewHolder(this.context, JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "mod_item")));
                            return new LoaderViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "include_progress")));
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
