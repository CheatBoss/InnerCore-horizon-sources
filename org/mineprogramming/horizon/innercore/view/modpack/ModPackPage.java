package org.mineprogramming.horizon.innercore.view.modpack;

import org.mineprogramming.horizon.innercore.view.item.*;
import com.android.tools.r8.annotations.*;
import org.mineprogramming.horizon.innercore.view.*;
import org.mineprogramming.horizon.innercore.view.mod.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import android.widget.*;
import java.io.*;
import android.os.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.json.*;
import com.bumptech.glide.*;

@SynthesizedClassMap({ -$$Lambda$ModPackPage$vw6td6r-cVBa-nDrq2ehOXn_ltw.class, -$$Lambda$ModPackPage$86JH8e_r8eA-HFwdjK7NC7r1dMA.class, -$$Lambda$ModPackPage$J9Ro2gtr-LG1dZbBgx6lh4TrUgQ.class, -$$Lambda$ModPackPage$pCT4UVuiB1GImX2xQW2-z34Z9N0.class, -$$Lambda$ModPackPage$g21Q4YWify8jV6i-qBmT0gJ4zUc.class, -$$Lambda$ModPackPage$hTLOXuLAYXCs0nX9-TXzdaElz_M.class, -$$Lambda$ModPackPage$Aut2jFN0h3oyMtGNrYOhIgf4ego.class, -$$Lambda$ModPackPage$0DgdLZGrJ3cbvz5MCHQrbHwy_Rs.class, -$$Lambda$ModPackPage$KB1HE9nf3akHC1gqhtHKQlfvWl0.class, -$$Lambda$ModPackPage$YM3QctYvTkYiowYOTwsFhFrvw_o.class, -$$Lambda$ModPackPage$gQBobw2Il8KQFzzAWqmO1KY7ahU.class, -$$Lambda$ModPackPage$Lm6kSzi6LN73zojHOpf5uOACMTE.class, -$$Lambda$ModPackPage$tI3TANWvkeEuTACl96at51QTTjg.class, -$$Lambda$ModPackPage$7hYVket7PGIOQSfNccVbGzWf-78.class, -$$Lambda$ModPackPage$toHyIakheznfL4mPwaxO3MfM_20.class, -$$Lambda$ModPackPage$2hqGioEjqGnfjPZbBvK2BRqK92Y.class })
public class ModPackPage extends ItemPage<ModPackItem>
{
    private TextView btnDelete;
    private TextView btnInstall;
    private TextView btnMods;
    private TextView btnSelect;
    private TextView btnSelected;
    private TextView btnUpdate;
    private TextView btnWebpage;
    final ModPackContext modPackContext;
    final ModPackStorage modPackStorage;
    private ImageView screenshot;
    private final UpdateSource updateSource;
    
    public ModPackPage(final PagesManager pagesManager, final ModPackItem modPackItem) {
        super(pagesManager, modPackItem);
        this.modPackContext = ModPackContext.getInstance();
        this.modPackStorage = this.modPackContext.getStorage();
        this.updateSource = UpdateSource.getInstance(this.language);
    }
    
    private void archive() {
        this.resetProgress();
        ((ModPackItem)this.item).archive(this.context, (ModPack$TaskReporter)new ModPack$TaskReporter() {
            public void reportError(final String s, final Exception ex, final boolean b) throws InterruptedException {
                ItemPage.this.setProgressError(ex);
            }
            
            public void reportProgress(final String s, final int n, final int n2, final int n3) throws InterruptedException {
                ItemPage.this.setProgress(s, n2, n3);
            }
            
            public void reportResult(final boolean b) {
                ModsManagerBanner.getInstance().closeInterstitial();
            }
        });
        ModsManagerBanner.getInstance().showInterstitialIfRequired();
    }
    
    private void assureDelete() {
        DialogHelper.assure(this.context, JsonInflater.formatString(this.context, "assure_delete", ((ModPackItem)this.item).getTitle()), new -$$Lambda$ModPackPage$pCT4UVuiB1GImX2xQW2-z34Z9N0(this));
    }
    
    private void clonePack() {
        this.resetProgress();
        ((ModPackItem)this.item).clonePack(this.context, (ModPack$TaskReporter)new ModPack$TaskReporter() {
            public void reportError(final String s, final Exception ex, final boolean b) throws InterruptedException {
                ItemPage.this.setProgressError(ex);
            }
            
            public void reportProgress(final String s, final int n, final int n2, final int n3) throws InterruptedException {
                ItemPage.this.setProgress(s, n2, n3);
            }
            
            public void reportResult(final boolean b) {
                ModsManagerBanner.getInstance().closeInterstitial();
            }
        });
        ModsManagerBanner.getInstance().showInterstitialIfRequired();
    }
    
    private Tag getCountTag() {
        return new Tag(String.format(JsonInflater.getString(this.context, "mods_count"), ((ModPackItem)this.item).getModsCount()));
    }
    
    private ModPackPageState getPackState() {
        if (((ModPackItem)this.item).isInstalled()) {
            if (this.updateSource.isReady() && this.updateSource.needsUpdate(((ModPackItem)this.item).getId())) {
                return ModPackPageState.TO_UPDATE;
            }
            if (((ModPackItem)this.item).isSelected()) {
                return ModPackPageState.SELECTED;
            }
            return ModPackPageState.INSTALLED;
        }
        else {
            if (((ModPackItem)this.item).isArchived()) {
                return ModPackPageState.ARCHIVED;
            }
            return ModPackPageState.NOT_INSTALLED;
        }
    }
    
    private void initializeOptions(final View view) {
        view.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$KB1HE9nf3akHC1gqhtHKQlfvWl0(this, view));
    }
    
    private void install(final boolean b) {
        this.resetProgress();
        ((ModPackItem)this.item).install(this.context, (ModPack$TaskReporter)new ModPack$TaskReporter() {
            public void reportError(final String s, final Exception ex, final boolean b) throws InterruptedException {
                ItemPage.this.setProgressError(ex);
            }
            
            public void reportProgress(final String s, final int n, final int n2, final int n3) throws InterruptedException {
                ItemPage.this.setProgress(s, n2, n3);
            }
            
            public void reportResult(final boolean b) {
                if (b) {
                    if (((ModPackItem)ModPackPage.this.item).isSelected()) {
                        ModPackPage.this.setPageState(ModPackPageState.SELECTED);
                    }
                    else {
                        ModPackPage.this.setPageState(ModPackPageState.INSTALLED);
                    }
                }
                ModsManagerBanner.getInstance().closeInterstitial();
            }
        });
        ModsManagerBanner.getInstance().showInterstitialIfRequired();
    }
    
    private void setPageState(final ModPackPageState modPackPageState) {
        new Handler(Looper.getMainLooper()).post((Runnable)new -$$Lambda$ModPackPage$0DgdLZGrJ3cbvz5MCHQrbHwy_Rs(this, modPackPageState));
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "page_pack"));
            this.initializeViews(inflateLayout);
            this.screenshot = (ImageView)inflateLayout.getViewByJsonId("screenshot");
            this.btnInstall = (TextView)inflateLayout.getViewByJsonId("install");
            this.btnUpdate = (TextView)inflateLayout.getViewByJsonId("update");
            this.btnSelect = (TextView)inflateLayout.getViewByJsonId("select");
            this.btnDelete = (TextView)inflateLayout.getViewByJsonId("delete");
            this.btnSelected = (TextView)inflateLayout.getViewByJsonId("selected");
            this.btnMods = (TextView)inflateLayout.getViewByJsonId("mods");
            this.btnWebpage = (TextView)inflateLayout.getViewByJsonId("webpage");
            this.setPageState(this.getPackState());
            this.btnInstall.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$Aut2jFN0h3oyMtGNrYOhIgf4ego(this));
            this.btnUpdate.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$gQBobw2Il8KQFzzAWqmO1KY7ahU(this));
            this.btnSelect.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$g21Q4YWify8jV6i-qBmT0gJ4zUc(this));
            this.btnDelete.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$tI3TANWvkeEuTACl96at51QTTjg(this));
            this.btnMods.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$7hYVket7PGIOQSfNccVbGzWf-78(this));
            this.btnWebpage.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPackPage$86JH8e_r8eA-HFwdjK7NC7r1dMA(this));
            if (((ModPackItem)this.item).isInstalled()) {
                if (((ModPackItem)this.item).getId() != 0 && !((ModPackItem)this.item).isModified()) {
                    this.loadRemote();
                }
                else {
                    this.loadLocal();
                }
            }
            else if (((ModPackItem)this.item).isArchived()) {
                this.loadLocal();
            }
            else {
                this.loadRemote();
            }
            this.initializeOptions(inflateLayout.getViewByJsonId("options"));
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    protected ModPack getModPack() {
        return ((ModPackItem)this.item).getModPack();
    }
    
    @Override
    protected void loadBasicInfo() {
        final String title = ((ModPackItem)this.item).getTitle();
        final String versionName = ((ModPackItem)this.item).getVersionName();
        if (versionName != null && !versionName.isEmpty()) {
            final TextView tvTitle = this.tvTitle;
            final StringBuilder sb = new StringBuilder();
            sb.append(title);
            sb.append(" [");
            sb.append(versionName);
            sb.append("]");
            tvTitle.setText((CharSequence)sb.toString());
        }
        else {
            this.tvTitle.setText((CharSequence)title);
        }
        if (((ModPackItem)this.item).getModsCount() > 0) {
            this.tagsAdapter.add(this.getCountTag());
        }
        super.loadBasicInfo();
    }
    
    @Override
    protected void onDownloadFailed() {
        this.loadLocal();
        this.setPageState(this.getPackState());
    }
    
    @Override
    protected void onRemoteLoaded(final JSONObject jsonObject) throws JSONException {
        super.onRemoteLoaded(jsonObject);
        final String s = null;
        final String s2 = null;
        final String string = jsonObject.getString("screenshots");
        String s3;
        if (string.startsWith("{")) {
            final JSONObject jsonObject2 = new JSONObject(string);
            final JSONArray names = jsonObject2.names();
            s3 = s2;
            if (names.length() > 0) {
                s3 = jsonObject2.optString(names.optString(0));
            }
        }
        else {
            final JSONArray jsonArray = new JSONArray(string);
            s3 = s;
            if (jsonArray.length() > 0) {
                s3 = jsonArray.optString(0);
            }
        }
        if (s3 != null) {
            final RequestManager with = Glide.with(this.context);
            final StringBuilder sb = new StringBuilder();
            sb.append("https://icmods.mineprogramming.org/api/img/");
            sb.append(s3);
            with.load(sb.toString()).centerCrop().into(this.screenshot);
        }
    }
}
