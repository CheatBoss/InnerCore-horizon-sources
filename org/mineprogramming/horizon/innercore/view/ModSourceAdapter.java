package org.mineprogramming.horizon.innercore.view;

import android.support.v7.widget.*;
import android.content.*;
import android.os.*;
import org.mineprogramming.horizon.innercore.model.*;
import android.view.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.text.*;

public class ModSourceAdapter extends RecyclerView$Adapter<RecyclerView$ViewHolder>
{
    private static final int VIEWTYPE_LOADER = 1;
    private static final int VIEWTYPE_MOD = 0;
    private Context context;
    private OnItemClickListener itemClickListener;
    private boolean loadFailed;
    private boolean loadedLast;
    LoaderViewHolder loaderHolder;
    private ModSource modSource;
    
    public ModSourceAdapter(final Context context, final ModSource modSource) {
        this.loadedLast = false;
        this.loadFailed = false;
        this.context = context;
        (this.modSource = modSource).setOnChangeListener((ModSource.ModSourceListener)new ModSource.ModSourceListener() {
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
        if (recyclerView$ViewHolder instanceof ModViewHolder) {
            final ModViewHolder modViewHolder = (ModViewHolder)recyclerView$ViewHolder;
            final Mod value = this.modSource.get(n);
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
            final String string2 = sb2.toString();
            if (Build$VERSION.SDK_INT >= 24) {
                modViewHolder.setName(Html.fromHtml(string2, 63));
            }
            else {
                modViewHolder.setName(Html.fromHtml(string2));
            }
            modViewHolder.setDescription(value.getDescription());
            modViewHolder.loadImage(value.getImage());
            modViewHolder.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (ModSourceAdapter.this.itemClickListener != null) {
                        ModSourceAdapter.this.itemClickListener.onItemClick(value);
                    }
                }
            });
            if (ModsManagerBanner.getInstance().shouldDisplayNativeAfter(n)) {
                modViewHolder.showAd();
            }
            else {
                modViewHolder.hideAd();
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
                            return new LoaderViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "progress_layout")));
                            return new ModViewHolder(JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "mod_item")));
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
    
    private class ModViewHolder extends RecyclerView$ViewHolder
    {
        private ViewGroup adView;
        private ImageView imageView;
        private ViewGroup modLayout;
        private TextView textViewDescription;
        private TextView textViewName;
        
        public ModViewHolder(final InflatedView inflatedView) {
            super(inflatedView.getView());
            this.modLayout = inflatedView.getViewByJsonId("layout");
            this.textViewName = inflatedView.getViewByJsonId("name");
            this.textViewDescription = inflatedView.getViewByJsonId("description");
            this.imageView = inflatedView.getViewByJsonId("image");
            this.adView = inflatedView.getViewByJsonId("banner");
        }
        
        public void hideAd() {
            this.adView.setVisibility(8);
        }
        
        public void loadImage(final Uri uri) {
            Glide.with(ModSourceAdapter.this.context).load(uri).transform(new BitmapTransformation[] { new NoAntialiasTransformation(ModSourceAdapter.this.context) }).into(this.imageView);
        }
        
        public void setDescription(final String text) {
            this.textViewDescription.setText((CharSequence)text);
        }
        
        public void setName(final Spanned text) {
            this.textViewName.setText((CharSequence)text);
        }
        
        public void setOnClickListener(final View$OnClickListener onClickListener) {
            this.modLayout.setOnClickListener(onClickListener);
        }
        
        public void showAd() {
            this.adView.setVisibility(0);
            this.adView.removeAllViews();
            ModsManagerBanner.getInstance().showNative(this.adView);
        }
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(final Mod p0);
    }
}
