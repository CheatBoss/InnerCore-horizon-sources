package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.*;
import com.android.tools.r8.annotations.*;
import android.widget.*;
import android.graphics.drawable.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;
import android.net.*;
import android.content.*;
import android.os.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import org.mineprogramming.horizon.innercore.view.update.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import org.mineprogramming.horizon.innercore.view.modpack.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.mineprogramming.horizon.innercore.view.mod.*;
import org.mineprogramming.horizon.innercore.model.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$ModsManagerActivity$V7YT3NNA8N7TZY6yQclllWEkE9U.class, -$$Lambda$ModsManagerActivity$WFEXfrcO7x4Qo4gkPYfou4da6FM.class, -$$Lambda$ModsManagerActivity$2IrHfHwqY-_D4YcPaGEC9w7lPgg.class })
public class ModsManagerActivity extends Displayable implements View$OnClickListener
{
    private ViewGroup container;
    private List<TextView> menuItems;
    private PagesManager pagesManager;
    private final Drawable selectedBackground;
    private UpdateSource updatesSource;
    
    public ModsManagerActivity(final Context context) {
        super(context);
        this.menuItems = new ArrayList<TextView>(3);
        new VersionsJsonBackCompFix(new File(FileTools.DIR_WORK, "mods")).runFixIfRequired();
        try {
            this.selectedBackground = JsonInflater.inflateDrawable(context, ResourceReader.readDrawable(context, "menu_item"));
            ModPackSelector.restoreSelected();
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "mods_manager"));
            this.container = (ViewGroup)inflateLayout.getViewByJsonId("container");
            this.pagesManager = new PagesManager(this.container);
            final TextView textView = (TextView)inflateLayout.getView().findViewWithTag((Object)"download_mods");
            textView.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView);
            this.onClick((View)textView);
            final TextView textView2 = (TextView)inflateLayout.getView().findViewWithTag((Object)"my_mods");
            textView2.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView2);
            final TextView textView3 = (TextView)inflateLayout.getView().findViewWithTag((Object)"mod_packs");
            textView3.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView3);
            final TextView textView4 = (TextView)inflateLayout.getView().findViewWithTag((Object)"updates");
            textView4.setOnClickListener((View$OnClickListener)this);
            this.menuItems.add(textView4);
            inflateLayout.getViewByJsonId("visitWebsite").setOnClickListener((View$OnClickListener)new -$$Lambda$ModsManagerActivity$V7YT3NNA8N7TZY6yQclllWEkE9U(this));
            (this.updatesSource = UpdateSource.getInstance(this.language)).requestUpdates((UpdateSource.UpdatesListener)new -$$Lambda$ModsManagerActivity$2IrHfHwqY-_D4YcPaGEC9w7lPgg((TextView)inflateLayout.getViewByJsonId("updates_count")));
            this.container.setFocusable(true);
            this.container.setFocusableInTouchMode(true);
            this.container.requestFocus();
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public boolean onBackPressed() {
        return this.pagesManager.navigateBack();
    }
    
    public void onClick(final View view) {
        this.selectMenUItem((TextView)view);
        final String s = (String)view.getTag();
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0110: {
            if (hashCode != -234430262) {
                if (hashCode != 31670024) {
                    if (hashCode != 1508872292) {
                        if (hashCode == 2116624893) {
                            if (s.equals("mod_packs")) {
                                n = 2;
                                break Label_0110;
                            }
                        }
                    }
                    else if (s.equals("my_mods")) {
                        n = 1;
                        break Label_0110;
                    }
                }
                else if (s.equals("download_mods")) {
                    n = 0;
                    break Label_0110;
                }
            }
            else if (s.equals("updates")) {
                n = 3;
                break Label_0110;
            }
            n = -1;
        }
        switch (n) {
            default: {}
            case 3: {
                this.pagesManager.reset(new UpdatesPage(this.pagesManager));
            }
            case 2: {
                this.pagesManager.reset(new ModPacksPage(this.pagesManager));
            }
            case 1: {
                final ModsPage modsPage = new ModsPage(this.pagesManager, new LocalModSource(ModTracker.getCurrent()), ModPackContext.getInstance().getCurrentModPack());
                modsPage.setDisplayFilters(false);
                this.pagesManager.reset(modsPage);
            }
            case 0: {
                this.pagesManager.reset(new ModsPage(this.pagesManager, "popular", ModPackContext.getInstance().getCurrentModPack()));
            }
        }
    }
    
    public void selectMenUItem(final TextView textView) {
        for (final TextView textView2 : this.menuItems) {
            textView2.setBackgroundResource(0);
            textView2.setTextColor(-16777216);
        }
        textView.setBackground(this.selectedBackground);
        textView.setTextColor(-1);
    }
}
