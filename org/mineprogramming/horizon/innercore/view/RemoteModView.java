package org.mineprogramming.horizon.innercore.view;

import android.widget.*;
import org.mineprogramming.horizon.innercore.model.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.json.*;
import android.support.v4.view.*;
import android.view.*;
import com.bumptech.glide.*;
import java.util.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.support.v7.widget.*;
import android.net.*;
import android.app.*;
import android.content.*;
import android.os.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.mineprogramming.horizon.innercore.util.*;
import com.zhekasmirnov.innercore.mod.build.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import java.io.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.graphics.*;
import android.animation.*;

public class RemoteModView extends ModView
{
    private View btnConfig;
    private View btnDelete;
    private TextView btnInstall;
    private ImageView ivIcon;
    private LinearLayout llComments;
    private LinearLayout llPage;
    private LinearLayout llProgress;
    private LinearLayout llProgressLayout;
    private final String modId;
    private TextView noComments;
    private OpenConfigHandler openConfigHandler;
    private ProgressBar pbLoader;
    private ProgressBar pbProgress;
    private SearchHandler searchHandler;
    public List<Tag> tags;
    public TagsAdapter tagsAdapter;
    private String title;
    private ArrayList<ModDependency> toInstall;
    private TextView tvAuthor;
    private TextView tvDescription;
    private TextView tvDownloadCount;
    private TextView tvLikeCount;
    private TextView tvNoInternet;
    private TextView tvProgressLabel;
    private TextView tvRetry;
    private TextView tvTitle;
    private TextView tvUpdateTime;
    private final String url;
    private int versionCode;
    private final VersionManager versionManager;
    private ViewPager vpScreenshots;
    
    public RemoteModView(final Context context, final String modId, final VersionManager versionManager, final SearchHandler searchHandler, final OpenConfigHandler openConfigHandler) {
        super(context);
        this.tags = new ArrayList<Tag>();
        this.title = "";
        this.versionCode = 0;
        this.toInstall = new ArrayList<ModDependency>();
        this.modId = modId;
        this.versionManager = versionManager;
        this.searchHandler = searchHandler;
        this.openConfigHandler = openConfigHandler;
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/description?horizon&id=");
        sb.append(modId);
        sb.append("&lang=");
        sb.append(this.language);
        sb.append("&comments_limit=4&dependency_tree=true");
        this.url = sb.toString();
    }
    
    private void getModById(final String s) {
        new DownloadJSON().execute((Object[])new String[] { s });
    }
    
    private void installMods(final ModDependency[] array) {
        if (ModsManagerBanner.getInstance().shouldDisplayInterstitial()) {
            ModsManagerBanner.getInstance().showInterstitial();
        }
        new DownloadFileFromURL().execute((Object[])new String[0]);
    }
    
    private void loadIntoListView(String text) throws JSONException {
        this.llProgress.setVisibility(8);
        this.llPage.setVisibility(0);
        final JSONObject jsonObject = new JSONObject(text);
        try {
            this.versionCode = Integer.parseInt(jsonObject.optString("version"));
        }
        catch (Exception ex) {
            this.versionCode = 1;
        }
        final int version = this.versionManager.getVersion(this.modId);
        if (version != 0) {
            this.btnDelete.setVisibility(0);
            this.btnConfig.setVisibility(0);
            if (version < this.versionCode) {
                this.btnInstall.setText((CharSequence)JsonInflater.getString(this.context, "update"));
            }
        }
        text = jsonObject.optString("version_name");
        this.title = jsonObject.optString("title");
        if (text.isEmpty()) {
            this.tvTitle.setText((CharSequence)this.title);
        }
        else {
            final TextView tvTitle = this.tvTitle;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.title);
            sb.append(" [");
            sb.append(text);
            sb.append("]");
            tvTitle.setText((CharSequence)sb.toString());
        }
        this.btnConfig.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final String directory = RemoteModView.this.versionManager.getDirectory(RemoteModView.this.modId);
                final OpenConfigHandler access$3100 = RemoteModView.this.openConfigHandler;
                final String access$3101 = RemoteModView.this.title;
                final StringBuilder sb = new StringBuilder();
                sb.append(FileTools.DIR_WORK);
                sb.append("mods/");
                sb.append(directory);
                sb.append("/");
                access$3100.openConfig(access$3101, sb.toString());
            }
        });
        text = jsonObject.optString("author_name");
        final TextView tvAuthor = this.tvAuthor;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(JsonInflater.getString(this.context, "author"));
        sb2.append(text);
        tvAuthor.setText((CharSequence)sb2.toString());
        this.tvAuthor.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                RemoteModView.this.searchHandler.searchAuthor(jsonObject.optString("author"));
            }
        });
        this.tvLikeCount.setText((CharSequence)jsonObject.optString("likes"));
        this.tvDownloadCount.setText((CharSequence)jsonObject.optString("downloads"));
        text = jsonObject.optString("last_update");
        if (text.equals("null")) {
            this.tvUpdateTime.setText((CharSequence)JsonInflater.getString(this.context, "long_ago"));
        }
        else {
            final int index = text.indexOf(" ");
            final TextView tvUpdateTime = this.tvUpdateTime;
            if (index != -1) {
                text = text.substring(0, index);
            }
            tvUpdateTime.setText((CharSequence)text);
        }
        this.tvDescription.setText((CharSequence)jsonObject.optString("description_full"));
        final RequestManager with = Glide.with(this.context);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("https://icmods.mineprogramming.org/api/img/");
        sb3.append(jsonObject.getString("icon_full"));
        with.load(sb3.toString()).transform(new BitmapTransformation[] { new NoAntialiasTransformation(this.context) }).into(this.ivIcon);
        final JSONArray jsonArray = new JSONArray(jsonObject.optString("tags"));
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.tags.add(new Tag(jsonArray.getString(i)));
            this.tagsAdapter.notifyDataSetChanged();
        }
        text = jsonObject.getString("screenshots");
        ArrayList list;
        if (text.startsWith("{")) {
            final JSONObject jsonObject2 = new JSONObject(text);
            list = new ArrayList<String>(jsonObject2.names().length());
            final Iterator keys = jsonObject2.keys();
            while (keys.hasNext()) {
                list.add(jsonObject2.optString((String)keys.next()));
            }
        }
        else {
            final JSONArray jsonArray2 = new JSONArray(text);
            final ArrayList list2 = new ArrayList<String>(jsonArray2.length());
            int n = 0;
            while (true) {
                list = list2;
                if (n >= jsonArray2.length()) {
                    break;
                }
                list2.add(jsonArray2.optString(n));
                ++n;
            }
        }
        this.vpScreenshots.setAdapter((PagerAdapter)new ScreenshotsAdapter(this.context, list));
        final JSONArray optJSONArray = jsonObject.optJSONArray("comments");
        if (optJSONArray.length() != 0) {
            this.noComments.setVisibility(8);
        }
        int j = 0;
        text = (String)jsonArray;
        final JSONArray jsonArray3 = optJSONArray;
        while (j < jsonArray3.length()) {
            final JSONObject optJSONObject = jsonArray3.optJSONObject(j);
            final String optString = optJSONObject.optString("user");
            final String optString2 = optJSONObject.optString("comment");
            try {
                final JSONObject layout = ResourceReader.readLayout(this.context, "comment");
                final Context context = this.context;
                try {
                    final InflatedView inflateLayout = JsonInflater.inflateLayout(context, (ViewGroup)this.llComments, layout);
                    ((TextView)inflateLayout.getViewByJsonId("name")).setText((CharSequence)optString);
                    ((TextView)inflateLayout.getViewByJsonId("comment")).setText((CharSequence)optString2);
                    ++j;
                }
                catch (JSONException | JsonInflaterException ex2) {
                    final String s;
                    text = s;
                }
            }
            catch (JSONException ex3) {}
            catch (JsonInflaterException ex4) {}
            throw new RuntimeException((Throwable)text);
        }
        final JSONArray optJSONArray2 = jsonObject.optJSONArray("dependencies");
        for (int k = 0; k < optJSONArray2.length(); ++k) {
            final ModDependency modDependency = new ModDependency(optJSONArray2.optJSONObject(k));
            if (!this.versionManager.isInstalled(modDependency.getId())) {
                this.toInstall.add(modDependency);
            }
        }
        this.toInstall.add(new ModDependency(this.modId, this.title));
    }
    
    private void onDownloadFailed() {
        this.pbLoader.setVisibility(8);
        this.tvNoInternet.setVisibility(0);
        this.tvRetry.setVisibility(0);
    }
    
    private void onDownloadInProgress() {
        this.pbLoader.setVisibility(0);
        this.tvNoInternet.setVisibility(8);
        this.tvRetry.setVisibility(8);
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "mod_remote"));
            this.llProgress = (LinearLayout)inflateLayout.getViewByJsonId("progress_layout");
            this.llPage = (LinearLayout)inflateLayout.getViewByJsonId("page");
            this.pbLoader = (ProgressBar)inflateLayout.getViewByJsonId("loader");
            this.tvNoInternet = (TextView)inflateLayout.getViewByJsonId("no_internet");
            (this.tvRetry = (TextView)inflateLayout.getViewByJsonId("retry")).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    RemoteModView.this.onDownloadInProgress();
                    RemoteModView.this.getModById(RemoteModView.this.url);
                }
            });
            this.tvTitle = (TextView)inflateLayout.getViewByJsonId("title");
            this.tvAuthor = (TextView)inflateLayout.getViewByJsonId("author");
            this.tvLikeCount = (TextView)inflateLayout.getViewByJsonId("likeCount");
            this.tvDownloadCount = (TextView)inflateLayout.getViewByJsonId("downloadCount");
            this.tvUpdateTime = (TextView)inflateLayout.getViewByJsonId("updateTime");
            this.tvDescription = (TextView)inflateLayout.getViewByJsonId("descrition");
            this.ivIcon = (ImageView)inflateLayout.getViewByJsonId("icon");
            this.vpScreenshots = (ViewPager)inflateLayout.getViewByJsonId("screenshots");
            this.noComments = (TextView)inflateLayout.getViewByJsonId("no_comments");
            this.llComments = (LinearLayout)inflateLayout.getViewByJsonId("comments");
            this.llProgressLayout = (LinearLayout)inflateLayout.getViewByJsonId("progressLayout");
            this.pbProgress = (ProgressBar)inflateLayout.getViewByJsonId("progressBar");
            this.tvProgressLabel = (TextView)inflateLayout.getViewByJsonId("progressLabel");
            final RecyclerView recyclerView = inflateLayout.getViewByJsonId("tagList");
            recyclerView.setHasFixedSize(true);
            this.tagsAdapter = new TagsAdapter(this.tags, this.context, this.searchHandler);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this.context, 0, false);
            layoutManager.setAutoMeasureEnabled(false);
            recyclerView.setLayoutManager((RecyclerView$LayoutManager)layoutManager);
            recyclerView.setAdapter((RecyclerView$Adapter)this.tagsAdapter);
            final TextView textView = inflateLayout.getViewByJsonId("webpage");
            final TextView textView2 = inflateLayout.getViewByJsonId("leave_comment");
            final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("https://icmods.mineprogramming.org/mod?id=");
                    sb.append(RemoteModView.this.modId);
                    final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
                    if (intent.resolveActivity(RemoteModView.this.context.getPackageManager()) != null) {
                        RemoteModView.this.context.startActivity(intent);
                    }
                }
            };
            textView.setOnClickListener((View$OnClickListener)view$OnClickListener);
            textView2.setOnClickListener((View$OnClickListener)view$OnClickListener);
            (this.btnInstall = (TextView)inflateLayout.getViewByJsonId("install")).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final ModDependency modDependency = new ModDependency(RemoteModView.this.modId, RemoteModView.this.title);
                    if (RemoteModView.this.toInstall.size() == 1) {
                        RemoteModView.this.installMods(new ModDependency[] { modDependency });
                        return;
                    }
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(RemoteModView.this.context);
                    final StringBuilder sb = new StringBuilder();
                    for (final ModDependency modDependency2 : RemoteModView.this.toInstall) {
                        if (!modDependency2.getId().equals(RemoteModView.this.modId)) {
                            sb.append(modDependency2.getTitle());
                            sb.append("\n");
                        }
                    }
                    alertDialog$Builder.setMessage((CharSequence)String.format(JsonInflater.getString(RemoteModView.this.context, "dependencies_list"), sb.toString())).setPositiveButton((CharSequence)JsonInflater.getString(RemoteModView.this.context, "yes"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            RemoteModView.this.installMods(RemoteModView.this.toInstall.toArray(new ModDependency[RemoteModView.this.toInstall.size()]));
                        }
                    }).setNegativeButton((CharSequence)JsonInflater.getString(RemoteModView.this.context, "no"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            RemoteModView.this.installMods(new ModDependency[] { modDependency });
                        }
                    }).setNeutralButton((CharSequence)JsonInflater.getString(RemoteModView.this.context, "cancel"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                        }
                    });
                    alertDialog$Builder.show();
                }
            });
            (this.btnDelete = inflateLayout.getViewByJsonId("delete")).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final String directory = RemoteModView.this.versionManager.getDirectory(RemoteModView.this.modId);
                    final RemoteModView this$0 = RemoteModView.this;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(FileTools.DIR_WORK);
                    sb.append("mods/");
                    sb.append(directory);
                    this$0.assureDelete(sb.toString());
                }
            });
            this.btnConfig = inflateLayout.getViewByJsonId("config");
            this.getModById(this.url);
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    protected void onDelete() {
        this.btnDelete.setVisibility(8);
        this.btnConfig.setVisibility(8);
        this.btnInstall.setText((CharSequence)JsonInflater.getString(this.context, "install"));
        this.versionManager.removeVersion(this.modId);
    }
    
    public void setProgressLabel(final String s) {
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
            @Override
            public void run() {
                RemoteModView.this.tvProgressLabel.setText((CharSequence)s);
            }
        });
    }
    
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
                    RemoteModView.this.setProgressLabel(String.format(JsonInflater.getString(RemoteModView.this.context, "downloading"), this.val$toInstall[i].getTitle()));
                    final File cacheDir = RemoteModView.this.context.getCacheDir();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("mod");
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
                    RemoteModView.this.setProgressLabel(String.format(JsonInflater.getString(RemoteModView.this.context, "installing"), this.val$toInstall[i].getTitle()));
                    final ArrayList icModFile = ExtractionHelper.extractICModFile(file, (IMessageReceiver)messageReceiver, (Runnable)null);
                    this.publishProgress(97 / this.val$toInstall.length + n);
                    file.delete();
                    if (icModFile == null || icModFile.size() < 1) {
                        this.publishProgress(100.0);
                        RemoteModView.this.setProgressLabel(JsonInflater.getString(RemoteModView.this.context, "install_error"));
                        return null;
                    }
                    RemoteModView.this.versionManager.putVersion(this.val$toInstall[i].getId(), new File((String)icModFile.get(0)).getName(), RemoteModView.this.versionCode);
                    this.publishProgress(100 / this.val$toInstall.length + n);
                }
                RemoteModView.this.setProgressLabel(JsonInflater.getString(RemoteModView.this.context, "done"));
                return null;
            }
            catch (IOException ex) {
                final RemoteModView this$0 = RemoteModView.this;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(JsonInflater.getString(RemoteModView.this.context, "error"));
                sb3.append(ex.getMessage());
                this$0.setProgressLabel(sb3.toString());
                Util.setProgressBarColor(RemoteModView.this.pbProgress, -65536);
                Logger.debug("error occured while downloading mod");
                ex.printStackTrace();
                return null;
            }
        }
        
        protected void onPostExecute(final String s) {
            Logger.debug("downloading finished");
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    RemoteModView.this.btnInstall.setText((CharSequence)JsonInflater.getString(RemoteModView.this.context, "install"));
                    RemoteModView.this.btnDelete.setVisibility(0);
                    RemoteModView.this.btnConfig.setVisibility(0);
                    AdsManager.getInstance().closeAllRequests();
                    AdsManager.getInstance().closeInterstitialAds();
                }
            });
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            Logger.debug("downloading started");
            RemoteModView.this.setProgressLabel("");
            RemoteModView.this.pbProgress.setProgress(0);
            Util.setProgressBarColor(RemoteModView.this.pbProgress, Color.parseColor("#2895F1"));
            RemoteModView.this.llProgressLayout.setVisibility(0);
        }
        
        protected void onProgressUpdate(final Integer... array) {
            ObjectAnimator.ofInt((Object)RemoteModView.this.pbProgress, "progress", new int[] { array[0] }).setDuration(300L).start();
        }
        
        public void publishProgress(final double n) {
            super.publishProgress((Object[])new Integer[] { (int)n });
        }
    }
    
    class DownloadJSON extends AsyncTask<String, Void, String>
    {
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
                RemoteModView.this.onDownloadFailed();
                return;
            }
            try {
                RemoteModView.this.loadIntoListView(s);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                RemoteModView.this.onDownloadFailed();
            }
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            RemoteModView.this.pbProgress.setProgress(0);
        }
    }
}
