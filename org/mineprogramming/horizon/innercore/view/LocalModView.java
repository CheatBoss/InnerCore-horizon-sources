package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.model.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import android.view.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class LocalModView extends ModView
{
    private final BackPressedHandler backPressedHandler;
    private final Mod mod;
    private final OpenConfigHandler openConfigHandler;
    
    protected LocalModView(final Context context, final Mod mod, final BackPressedHandler backPressedHandler, final OpenConfigHandler openConfigHandler) {
        super(context);
        this.mod = mod;
        this.backPressedHandler = backPressedHandler;
        this.openConfigHandler = openConfigHandler;
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "mod_local"));
            ((TextView)inflateLayout.getViewByJsonId("title")).setText((CharSequence)this.mod.getTitle());
            final TextView textView = inflateLayout.getViewByJsonId("author");
            if (this.mod.getAuthor() != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(JsonInflater.getString(this.context, "author"));
                sb.append(this.mod.getAuthor());
                textView.setText((CharSequence)sb.toString());
            }
            ((TextView)inflateLayout.getViewByJsonId("descrition")).setText((CharSequence)this.mod.getDescription());
            Glide.with(this.context).load(this.mod.getImage()).transform(new BitmapTransformation[] { new NoAntialiasTransformation(this.context) }).into((ImageView)inflateLayout.getViewByJsonId("icon"));
            inflateLayout.getViewByJsonId("delete").setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    LocalModView.this.assureDelete(LocalModView.this.mod.getDirectory().getAbsolutePath());
                }
            });
            inflateLayout.getViewByJsonId("config").setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final OpenConfigHandler access$100 = LocalModView.this.openConfigHandler;
                    final String title = LocalModView.this.mod.getTitle();
                    final StringBuilder sb = new StringBuilder();
                    sb.append(LocalModView.this.mod.getDirectory().getAbsolutePath());
                    sb.append("/");
                    access$100.openConfig(title, sb.toString());
                }
            });
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    protected void onDelete() {
        this.backPressedHandler.onBackPressed();
    }
}
