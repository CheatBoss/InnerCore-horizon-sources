package org.mineprogramming.horizon.innercore.view.item;

import com.android.tools.r8.annotations.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.support.v7.widget.*;
import java.util.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import android.view.*;
import android.net.*;
import android.content.*;
import android.graphics.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.view.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.mineprogramming.horizon.innercore.view.mod.*;
import org.mineprogramming.horizon.innercore.model.*;
import android.animation.*;
import android.os.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

@SynthesizedClassMap({ -$$Lambda$ItemPage$FG0CSq0I_UQrfxbwXJP_Av2dqv0.class, -$$Lambda$ItemPage$aauLdar4KVDqQyr84sn_UXHvWng.class, -$$Lambda$ItemPage$sCu7sS9PtwyfIm-VC1kx1MhPB3g.class })
public abstract class ItemPage<T extends Item> extends Page implements SearchHandler
{
    protected final T item;
    private ViewGroup page;
    private ViewGroup progress;
    private ProgressBar progressBar;
    private TextView progressLabel;
    private ViewGroup progressLayout;
    private ViewGroup statistics;
    protected TagsAdapter tagsAdapter;
    private TextView tvAuthor;
    private TextView tvDescription;
    private TextView tvDownloadCount;
    private TextView tvLikeCount;
    protected TextView tvTitle;
    private TextView tvUpdateTime;
    
    protected ItemPage(final PagesManager pagesManager, final T item) {
        super(pagesManager);
        this.item = item;
    }
    
    private boolean isPackModified() {
        return this.getModPack().getPreferences().getBoolean("modified", true);
    }
    
    private void setPackModified() {
        this.getModPack().getPreferences().setBoolean("modified", true).save();
    }
    
    protected abstract ModPack getModPack();
    
    protected void hideProgress() {
        this.progressLayout.setVisibility(8);
    }
    
    protected void initializeViews(final InflatedView inflatedView) {
        this.page = inflatedView.getViewByJsonId("page");
        this.progress = inflatedView.getViewByJsonId("progress_layout");
        this.statistics = inflatedView.getViewByJsonId("statistics");
        this.tvTitle = inflatedView.getViewByJsonId("title");
        this.tvAuthor = inflatedView.getViewByJsonId("author");
        this.tvDescription = inflatedView.getViewByJsonId("description");
        this.tvLikeCount = inflatedView.getViewByJsonId("likeCount");
        this.tvDownloadCount = inflatedView.getViewByJsonId("downloadCount");
        this.tvUpdateTime = inflatedView.getViewByJsonId("updateTime");
        final RecyclerView recyclerView = inflatedView.getViewByJsonId("tagList");
        recyclerView.setHasFixedSize(true);
        this.tagsAdapter = new TagsAdapter(this.context, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.context, 0, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager((RecyclerView$LayoutManager)layoutManager);
        recyclerView.setAdapter((RecyclerView$Adapter)this.tagsAdapter);
        this.progressLayout = inflatedView.getViewByJsonId("download_progress_layout");
        this.progressBar = inflatedView.getViewByJsonId("progress_bar");
        this.progressLabel = inflatedView.getViewByJsonId("progress_label");
    }
    
    protected void loadBasicInfo() {
        final String authorName = this.item.getAuthorName();
        if (authorName != null && !authorName.isEmpty()) {
            final TextView tvAuthor = this.tvAuthor;
            final StringBuilder sb = new StringBuilder();
            sb.append(JsonInflater.getString(this.context, "author"));
            sb.append(authorName);
            tvAuthor.setText((CharSequence)sb.toString());
        }
        final Iterator<Tag> iterator = this.item.getTags().iterator();
        while (iterator.hasNext()) {
            this.tagsAdapter.add(iterator.next());
        }
        this.tvDescription.setText((CharSequence)this.item.getDescriptionFull());
        this.page.setVisibility(0);
        this.progress.setVisibility(8);
    }
    
    protected void loadLocal() {
        this.loadBasicInfo();
        this.statistics.setVisibility(8);
    }
    
    protected void loadRemote() {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/description?horizon&id=");
        sb.append(this.item.getId());
        sb.append("&lang=");
        sb.append(this.language);
        sb.append("&comments_limit=4&dependency_tree=true");
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(final String... array) {
                try {
                    return DownloadHelper.downloadString(array[0]);
                }
                catch (Exception ex) {
                    return null;
                }
            }
            
            protected void onPostExecute(final String s) {
                super.onPostExecute((Object)s);
                if (s == null) {
                    ItemPage.this.onDownloadFailed();
                    return;
                }
                try {
                    ItemPage.this.onRemoteLoaded(new JSONObject(s));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    ItemPage.this.onDownloadFailed();
                }
            }
        }.execute((Object[])new String[] { sb.toString() });
    }
    
    protected abstract void onDownloadFailed();
    
    protected void onRemoteLoaded(final JSONObject jsonObject) throws JSONException {
        this.item.updateInfo(jsonObject);
        this.loadBasicInfo();
        this.tvAuthor.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ItemPage.this.searchAuthor(ItemPage.this.item.getAuthor(), ItemPage.this.item.getAuthorName());
            }
        });
        this.tvLikeCount.setText((CharSequence)jsonObject.optString("likes"));
        this.tvDownloadCount.setText((CharSequence)jsonObject.optString("downloads"));
        final String optString = jsonObject.optString("last_update");
        if (optString.equals("null")) {
            this.tvUpdateTime.setText((CharSequence)JsonInflater.getString(this.context, "long_ago"));
            return;
        }
        this.tvUpdateTime.setText((CharSequence)optString);
    }
    
    protected void openWebsite() {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/mod?id=");
        sb.append(this.item.getId());
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        if (intent.resolveActivity(this.context.getPackageManager()) != null) {
            this.context.startActivity(intent);
        }
    }
    
    protected void resetProgress() {
        this.setProgressLabel("");
        this.progressBar.setProgress(0);
        this.progressBar.setMax(100);
        InflaterUtils.setProgressBarColor(this.progressBar, Color.parseColor("#2895F1"));
        this.progressLayout.setVisibility(0);
    }
    
    protected void runIfPackModificationAllowed(final Runnable runnable) {
        if (this.isPackModified()) {
            runnable.run();
            return;
        }
        DialogHelper.assure(this.context, JsonInflater.formatString(this.context, "pack_modification", new Object[0]), new -$$Lambda$ItemPage$aauLdar4KVDqQyr84sn_UXHvWng(this, runnable));
    }
    
    @Override
    public void searchAuthor(final int n, final String s) {
        final String language = this.language;
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(n);
        final ModsPage modsPage = new ModsPage(this.pagesManager, new SearchModSource(language, "author", sb.toString()), ModPackContext.getInstance().getCurrentModPack());
        modsPage.setDisplayFilters(false);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(JsonInflater.getString(this.context, "author"));
        sb2.append(s);
        modsPage.setCustomTitle(sb2.toString());
        this.pagesManager.push(modsPage);
    }
    
    @Override
    public void searchTag(final String s) {
        final ModsPage modsPage = new ModsPage(this.pagesManager, new SearchModSource(this.language, "tag", s), ModPackContext.getInstance().getCurrentModPack());
        modsPage.setDisplayFilters(false);
        final StringBuilder sb = new StringBuilder();
        sb.append(JsonInflater.getString(this.context, "tag"));
        sb.append(s);
        modsPage.setCustomTitle(sb.toString());
        this.pagesManager.push(modsPage);
    }
    
    protected void setProgress(final int n) {
        ObjectAnimator.ofInt((Object)this.progressBar, "progress", new int[] { n }).setDuration(300L).start();
    }
    
    protected void setProgress(final String s, final int n, final int n2) {
        new Handler(Looper.getMainLooper()).post((Runnable)new -$$Lambda$ItemPage$FG0CSq0I_UQrfxbwXJP_Av2dqv0(this, s, n, n2));
    }
    
    protected void setProgressError(final Exception ex) {
        Logger.debug("error occured while downloading mod");
        ex.printStackTrace();
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
            @Override
            public void run() {
                final ItemPage this$0 = ItemPage.this;
                final StringBuilder sb = new StringBuilder();
                sb.append(JsonInflater.getString(ItemPage.this.context, "error"));
                sb.append(ex.getMessage());
                this$0.setProgressLabel(sb.toString());
                InflaterUtils.setProgressBarColor(ItemPage.this.progressBar, -65536);
            }
        });
    }
    
    protected void setProgressLabel(final String s) {
        new Handler(Looper.getMainLooper()).post((Runnable)new -$$Lambda$ItemPage$sCu7sS9PtwyfIm-VC1kx1MhPB3g(this, s));
    }
    
    protected void shareLink(final String s) {
        final Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/mod?id=");
        sb.append(this.item.getId());
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        this.context.startActivity(Intent.createChooser(intent, (CharSequence)s));
    }
}
