package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import com.zhekasmirnov.innercore.api.*;
import org.mineprogramming.horizon.innercore.view.config.*;
import org.json.*;
import java.util.*;

public class AboutActivity extends Displayable implements View$OnClickListener
{
    FrameLayout container;
    private List<TextView> menuItems;
    private PagesManager pagesManager;
    private final Drawable selectedBackground;
    
    public AboutActivity(final Context context) {
        super(context);
        try {
            this.selectedBackground = JsonInflater.inflateDrawable(context, ResourceReader.readDrawable(context, "menu_item"));
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    private String readAsset(final String s) {
        final byte[] assetBytes = AssetPatch.getAssetBytes(this.context.getAssets(), s);
        if (assetBytes == null) {
            return null;
        }
        return new String(assetBytes);
    }
    
    private String readAssetLocalized(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("-");
        sb.append(this.language);
        sb.append(s2);
        final String asset = this.readAsset(sb.toString());
        if (asset != null) {
            return asset;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("-en");
        sb2.append(s2);
        final String asset2 = this.readAsset(sb2.toString());
        if (asset2 != null) {
            return asset2;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(s2);
        return this.readAsset(sb3.toString());
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "about"));
            this.menuItems = new ArrayList<TextView>(3);
            this.container = (FrameLayout)inflateLayout.getViewByJsonId("container");
            this.pagesManager = new PagesManager((ViewGroup)this.container);
            final TextView textView = (TextView)inflateLayout.getView().findViewWithTag((Object)"credits");
            textView.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView);
            final TextView textView2 = (TextView)inflateLayout.getView().findViewWithTag((Object)"about");
            textView2.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView2);
            final TextView textView3 = (TextView)inflateLayout.getView().findViewWithTag((Object)"preferences");
            textView3.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView3);
            this.onClick((View)textView3);
            final TextView textView4 = (TextView)inflateLayout.getView().findViewWithTag((Object)"links");
            textView4.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView4);
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public void onClick(View view) {
        final String s = (String)view.getTag();
        this.container.removeAllViews();
        final Iterator<TextView> iterator = this.menuItems.iterator();
        int n;
        while (true) {
            final boolean hasNext = iterator.hasNext();
            n = 0;
            if (!hasNext) {
                break;
            }
            final TextView textView = iterator.next();
            textView.setBackgroundResource(0);
            textView.setTextColor(-16777216);
        }
        view.setBackground(this.selectedBackground);
        ((TextView)view).setTextColor(-1);
        final String string = JsonInflater.getString(this.context, s);
        final int hashCode = s.hashCode();
        Label_0190: {
            if (hashCode != 92611469) {
                if (hashCode != 102977465) {
                    if (hashCode != 1028633754) {
                        if (hashCode == 1989861112) {
                            if (s.equals("preferences")) {
                                break Label_0190;
                            }
                        }
                    }
                    else if (s.equals("credits")) {
                        n = 3;
                        break Label_0190;
                    }
                }
                else if (s.equals("links")) {
                    n = 1;
                    break Label_0190;
                }
            }
            else if (s.equals("about")) {
                n = 2;
                break Label_0190;
            }
            n = -1;
        }
        switch (n) {
            default: {}
            case 1:
            case 2:
            case 3: {
                final StringBuilder sb = new StringBuilder();
                sb.append("res/");
                sb.append(s);
                this.pagesManager.reset(new InfoPage(this.pagesManager, string, this.readAssetLocalized(sb.toString(), ".html")));
            }
            case 0: {
                view = (View)new ConfigPage(this.pagesManager, string, InnerCoreConfig.getConfigFile().getAbsolutePath());
                try {
                    ((ConfigPage)view).loadInfo(new JSONObject(this.readAsset("res/config.info.json")));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                this.pagesManager.reset((Page)view);
            }
        }
    }
}
