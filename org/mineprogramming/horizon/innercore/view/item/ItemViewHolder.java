package org.mineprogramming.horizon.innercore.view.item;

import android.support.v7.widget.*;
import android.content.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.text.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.view.*;

public class ItemViewHolder extends RecyclerView$ViewHolder
{
    private final ViewGroup adView;
    private final ImageView btnSettings;
    protected final Context context;
    private final ImageView imageView;
    private final ViewGroup modLayout;
    private final TextView textViewDescription;
    private final TextView textViewName;
    
    public ItemViewHolder(final Context context, final InflatedView inflatedView) {
        super(inflatedView.getView());
        this.context = context;
        this.modLayout = inflatedView.getViewByJsonId("layout");
        this.textViewName = inflatedView.getViewByJsonId("name");
        this.textViewDescription = inflatedView.getViewByJsonId("description");
        this.imageView = inflatedView.getViewByJsonId("image");
        this.btnSettings = inflatedView.getViewByJsonId("settings");
        this.adView = inflatedView.getViewByJsonId("banner");
    }
    
    public void hideAd() {
        this.adView.setVisibility(8);
    }
    
    public void loadImage(final Uri uri) {
        Glide.with(this.context).load(uri).transform(new BitmapTransformation[] { new NoAntialiasTransformation(this.context) }).into(this.imageView);
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
    
    public void setOnSettingsClickListener(final View$OnClickListener onClickListener) {
        if (this.btnSettings != null) {
            this.btnSettings.setOnClickListener(onClickListener);
        }
    }
    
    public void showAd() {
        this.adView.setVisibility(0);
        this.adView.removeAllViews();
        ModsManagerBanner.getInstance().showNative(this.adView);
    }
}
