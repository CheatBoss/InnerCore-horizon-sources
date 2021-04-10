package org.mineprogramming.horizon.innercore.view.mod;

import com.android.tools.r8.annotations.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.view.*;
import android.widget.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.view.config.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import android.app.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import android.support.v4.view.*;
import org.json.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.mineprogramming.horizon.innercore.util.*;
import com.zhekasmirnov.innercore.mod.build.*;
import java.io.*;
import java.util.*;
import android.os.*;

@SynthesizedClassMap({ -$$Lambda$ModPage$q1RN0LlG9-MDSfUKrX1njEPRaPM.class, -$$Lambda$ModPage$HZm7CV8HjfXjCwg3yy0OYkyocz0.class, -$$Lambda$ModPage$8e9BphXEn7_jVZnXZmd_FwFYqWE.class, -$$Lambda$ModPage$o1_jcEQurztra3e3ycWoJ9x5Qas.class, -$$Lambda$ModPage$I6FYGsPo4OhGOb2R8Jm29QHtPlQ.class, -$$Lambda$ModPage$iAzQ58oJ08_rzZh88ojuZEjP4Yc.class, -$$Lambda$ModPage$-5cV7ezipJm4QzIf6id43EsegHo.class, -$$Lambda$ModPage$g2ns55MuXOtDVKt0s9ix4xPTi50.class, -$$Lambda$ModPage$jOThL4SWL3BvBU6_ldeAOTI9LTI.class, -$$Lambda$ModPage$hj_X2mdbkFPOcOZMfkf5ocof8eA.class, -$$Lambda$ModPage$jipLSRLy9At2qpc2Wkg_I4dIBJE.class, -$$Lambda$ModPage$lUAJXPDMKvGR0te1K2C-pNFw0jI.class, -$$Lambda$ModPage$CmWXh25EGi33cocWI5zW03ysIEk.class, -$$Lambda$ModPage$_nxQ6spQ26TP6UXdb9kYKRsqaII.class, -$$Lambda$ModPage$Mv1k3WkkK679wXbqZvxuWu-GHTo.class })
public class ModPage extends ItemPage<ModItem>
{
    private TextView btnConfig;
    private TextView btnDelete;
    private TextView btnInstall;
    private TextView btnUpdate;
    private TextView btnWebpage;
    private ViewGroup commentsLayout;
    private ViewGroup icmodsData;
    private ImageView ivIcon;
    private TextView leaveComment;
    private final ModTracker modTracker;
    private TextView noComments;
    private TextView tvVersion;
    private final UpdateSource updateSource;
    private ViewPager vpScreenshots;
    
    public ModPage(final PagesManager pagesManager, final ModItem modItem) {
        super(pagesManager, modItem);
        this.modTracker = ModTracker.getCurrent();
        this.updateSource = UpdateSource.getInstance(this.language);
    }
    
    private void assureDelete() {
        DialogHelper.assure(this.context, JsonInflater.formatString(this.context, "assure_delete", ((ModItem)this.item).getTitle()), new -$$Lambda$ModPage$HZm7CV8HjfXjCwg3yy0OYkyocz0(this));
    }
    
    private void compile() {
        this.resetProgress();
        ((ModItem)this.item).compile(this.context, (ModPack$TaskReporter)new ModPack$TaskReporter() {
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
    
    private void initializeOptions(final View view) {
        view.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$lUAJXPDMKvGR0te1K2C-pNFw0jI(this, view));
    }
    
    private void installMods(final ModDependency[] array) {
        ModsManagerBanner.getInstance().showInterstitialIfRequired();
        new DownloadFileFromURL().execute((Object[])new String[0]);
    }
    
    private void reinstallMod() {
        FileTools.delete(((ModItem)this.item).getDirectory().getAbsolutePath());
        this.modTracker.onDeleted(((ModItem)this.item).getLocation(), ((ModItem)this.item).getId());
        this.installMods(new ModDependency[] { new ModDependency(((ModItem)this.item).getId(), ((ModItem)this.item).getTitle(), ((ModItem)this.item).getVersionCode()) });
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "page_mod"));
            this.initializeViews(inflateLayout);
            this.icmodsData = (ViewGroup)inflateLayout.getViewByJsonId("icmods");
            this.tvVersion = (TextView)inflateLayout.getViewByJsonId("version");
            this.ivIcon = (ImageView)inflateLayout.getViewByJsonId("icon");
            this.btnInstall = (TextView)inflateLayout.getViewByJsonId("install");
            this.btnUpdate = (TextView)inflateLayout.getViewByJsonId("update");
            this.btnDelete = (TextView)inflateLayout.getViewByJsonId("delete");
            this.btnConfig = (TextView)inflateLayout.getViewByJsonId("config");
            this.btnWebpage = (TextView)inflateLayout.getViewByJsonId("webpage");
            this.vpScreenshots = (ViewPager)inflateLayout.getViewByJsonId("screenshots");
            this.commentsLayout = (ViewGroup)inflateLayout.getViewByJsonId("comments");
            this.noComments = (TextView)inflateLayout.getViewByJsonId("no_comments");
            this.leaveComment = (TextView)inflateLayout.getViewByJsonId("leave_comment");
            this.btnDelete.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$o1_jcEQurztra3e3ycWoJ9x5Qas(this));
            this.btnConfig.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ModPage.this.pagesManager.push(ConfigPage.forMod(ModPage.this.pagesManager, (ModItem)ModPage.this.item));
                }
            });
            this.btnInstall.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$hj_X2mdbkFPOcOZMfkf5ocof8eA(this));
            this.btnUpdate.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$8e9BphXEn7_jVZnXZmd_FwFYqWE(this));
            this.btnWebpage.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$CmWXh25EGi33cocWI5zW03ysIEk(this));
            this.leaveComment.setOnClickListener((View$OnClickListener)new -$$Lambda$ModPage$-5cV7ezipJm4QzIf6id43EsegHo(this));
            if (((ModItem)this.item).isInstalled()) {
                if (((ModItem)this.item).getId() != 0) {
                    this.loadRemote();
                }
                else {
                    this.loadLocal();
                }
                if (this.updateSource.isReady() && this.updateSource.needsUpdate(((ModItem)this.item).getId())) {
                    this.setPageState(ModPageState.TO_UPDATE);
                }
                else {
                    this.setPageState(ModPageState.INSTALLED);
                }
            }
            else {
                this.loadRemote();
                this.setPageState(ModPageState.NOT_INSTALLED);
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
        return ModPackContext.getInstance().getCurrentModPack();
    }
    
    @Override
    protected void loadBasicInfo() {
        this.tvTitle.setText((CharSequence)((ModItem)this.item).getTitle());
        final String versionName = ((ModItem)this.item).getVersionName();
        if (versionName != null && !versionName.isEmpty()) {
            final TextView tvVersion = this.tvVersion;
            final StringBuilder sb = new StringBuilder();
            sb.append(JsonInflater.getString(this.context, "version"));
            sb.append(versionName);
            tvVersion.setText((CharSequence)sb.toString());
        }
        Glide.with(this.context).load(((ModItem)this.item).getIcon()).transform(new BitmapTransformation[] { new NoAntialiasTransformation(this.context) }).into(this.ivIcon);
        super.loadBasicInfo();
    }
    
    @Override
    protected void loadLocal() {
        super.loadLocal();
        this.icmodsData.setVisibility(8);
    }
    
    @Override
    protected void onDownloadFailed() {
        this.loadLocal();
        this.setPageState(ModPageState.INSTALLED);
    }
    
    @Override
    protected void onRemoteLoaded(final JSONObject jsonObject) throws JSONException {
        super.onRemoteLoaded(jsonObject);
        ((ModItem)this.item).updateDependencies(jsonObject.optJSONArray("dependencies"), this.modTracker);
        this.vpScreenshots.setAdapter((PagerAdapter)new ScreenshotsAdapter(this.context, jsonObject.getString("screenshots")));
        final JSONArray optJSONArray = jsonObject.optJSONArray("comments");
        if (optJSONArray.length() != 0) {
            this.noComments.setVisibility(8);
        }
        int i = 0;
        while (i < optJSONArray.length()) {
            final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            final String optString = optJSONObject.optString("user");
            final String optString2 = optJSONObject.optString("comment");
            try {
                final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, this.commentsLayout, ResourceReader.readLayout(this.context, "comment"));
                ((TextView)inflateLayout.getViewByJsonId("name")).setText((CharSequence)optString);
                ((TextView)inflateLayout.getViewByJsonId("comment")).setText((CharSequence)optString2);
                ++i;
                continue;
            }
            catch (JSONException | JsonInflaterException ex) {
                final Object o;
                throw new RuntimeException((Throwable)o);
            }
            break;
        }
    }
    
    public void setPageState(final ModPageState modPageState) {
        this.btnInstall.setVisibility(8);
        this.btnUpdate.setVisibility(8);
        this.btnConfig.setVisibility(8);
        this.btnDelete.setVisibility(8);
        this.btnWebpage.setVisibility(8);
        switch (modPageState) {
            default: {}
            case TO_UPDATE: {
                this.btnUpdate.setVisibility(0);
                this.btnDelete.setVisibility(0);
            }
            case INSTALLED: {
                this.btnConfig.setVisibility(0);
                this.btnDelete.setVisibility(0);
            }
            case NOT_INSTALLED: {
                this.btnInstall.setVisibility(0);
                this.btnWebpage.setVisibility(0);
            }
        }
    }
    
    @SynthesizedClassMap({ -$$Lambda$ModPage$1DownloadFileFromURL$mA02rjl8yOydPxeDXVH-AoKo4Nw.class })
    class DownloadFileFromURL extends AsyncTask<String, Integer, String>
    {
        final /* synthetic */ ModDependency[] val$toInstall;
        
        DownloadFileFromURL(final ModDependency[] val$toInstall) {
            this.val$toInstall = val$toInstall;
        }
        
        protected String doInBackground(final String... array) {
            try {
                final IMessageReceiver messageReceiver = (IMessageReceiver)new IMessageReceiver() {
                    public void message(final String s) {
                        Logger.debug(s);
                    }
                };
                for (int i = 0; i < this.val$toInstall.length; ++i) {
                    final double n = i * 100.0 / this.val$toInstall.length;
                    ItemPage.this.setProgressLabel(String.format(JsonInflater.getString(ModPage.this.context, "downloading"), this.val$toInstall[i].getTitle()));
                    final File cacheDir = ModPage.this.context.getCacheDir();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("item");
                    sb.append(this.val$toInstall[i].getId());
                    sb.append(".icmod");
                    final File file = new File(cacheDir, sb.toString());
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("https://icmods.mineprogramming.org/api/download.php?horizon&id=");
                    sb2.append(this.val$toInstall[i].getId());
                    DownloadHelper.downloadFile(sb2.toString(), file, (DownloadHelper.FileDownloadListener)new DownloadHelper.FileDownloadListener() {
                        @Override
                        public void onDownloadProgress(final long n, final long n2) {
                            DownloadFileFromURL.this.publishProgress(n + 73L * n / n2 / DownloadFileFromURL.this.val$toInstall.length);
                        }
                    });
                    this.publishProgress(73 / this.val$toInstall.length + n);
                    ItemPage.this.setProgressLabel(String.format(JsonInflater.getString(ModPage.this.context, "installing"), this.val$toInstall[i].getTitle()));
                    final ArrayList icModFile = ExtractionHelper.extractICModFile(file, (IMessageReceiver)messageReceiver, (Runnable)null);
                    this.publishProgress(97 / this.val$toInstall.length + n);
                    file.delete();
                    if (icModFile == null || icModFile.size() < 1) {
                        this.publishProgress(100.0);
                        ItemPage.this.setProgressLabel(JsonInflater.getString(ModPage.this.context, "install_error"));
                        return null;
                    }
                    final File file2 = new File(icModFile.get(0));
                    ModPage.this.modTracker.onInstalled(file2, ExtractionHelper.getLastLocation(), this.val$toInstall[i].getId(), this.val$toInstall[i].getVersion());
                    ModPage.this.updateSource.onUpdateOrDelete(this.val$toInstall[i].getId());
                    if (((ModItem)ModPage.this.item).getId() == this.val$toInstall[i].getId()) {
                        ((ModItem)ModPage.this.item).onInstalled(file2, ExtractionHelper.getLastLocation());
                    }
                    this.publishProgress(100 / this.val$toInstall.length + n);
                }
                ItemPage.this.setProgressLabel(JsonInflater.getString(ModPage.this.context, "done"));
            }
            catch (IOException ex) {
                ItemPage.this.setProgressError(ex);
            }
            return null;
        }
        
        protected void onPostExecute(final String s) {
            Logger.debug("downloading finished");
            new Handler(Looper.getMainLooper()).post((Runnable)new -$$Lambda$ModPage$1DownloadFileFromURL$mA02rjl8yOydPxeDXVH-AoKo4Nw(this));
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            Logger.debug("downloading started");
            ItemPage.this.resetProgress();
        }
        
        protected void onProgressUpdate(final Integer... array) {
            ItemPage.this.setProgress(array[0]);
        }
        
        public void publishProgress(final double n) {
            super.publishProgress((Object[])new Integer[] { (int)n });
        }
    }
}
