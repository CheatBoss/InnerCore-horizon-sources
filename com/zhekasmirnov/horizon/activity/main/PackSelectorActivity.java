package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.horizon.launcher.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import com.zhekasmirnov.horizon.activity.util.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.content.*;
import android.annotation.*;
import android.support.v7.widget.*;
import android.app.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import com.zhekasmirnov.horizon.util.*;
import android.text.*;
import android.support.annotation.*;
import java.util.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.util.*;
import java.io.*;

public class PackSelectorActivity extends AppCompatActivity
{
    private PackStorage packStorage;
    private PackRepository packRepo;
    private PackHolder currentSelectingPackHolder;
    private ViewAdapter mainScreenAdapter;
    private Set<String> favoritePacksUUIDs;
    
    public PackSelectorActivity() {
        this.currentSelectingPackHolder = null;
        this.mainScreenAdapter = null;
        this.favoritePacksUUIDs = new HashSet<String>();
    }
    
    @SuppressLint({ "ApplySharedPref" })
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean isMovedToBack = HorizonApplication.moveToBackgroundIfNotOnTop((Activity)this);
        this.loadFavoritePacksUUIDs();
        final ContextHolder contextHolder = new ContextHolder((Activity)this);
        this.packStorage = new PackStorage(contextHolder, new File(Environment.getExternalHorizonDirectory(), "packs"));
        this.packRepo = new ExternalPackRepository("https://gitlab.com/zhekasmirnov/horizon-cloud-config/raw/master/packs.json");
        boolean isLoadingList = true;
        final SharedPreferences preferences = this.getSharedPreferences("packs", 0);
        String path = this.getIntent().getStringExtra("pack_directory");
        path = ((path != null) ? path : preferences.getString("currently_selected_pack_path", (String)null));
        if (path != null && this.currentSelectingPackHolder == null) {
            final File installationDir = new File(path);
            if (installationDir.isDirectory()) {
                final PackHolder holder = new PackHolder(this.packStorage, new PackDirectory(installationDir));
                holder.initialize();
                if (holder.getState() == PackHolder.State.INSTALLED) {
                    isLoadingList = false;
                    contextHolder.getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                        @Override
                        public void run() {
                            if (HorizonApplication.getLock("pack_loading").tryLock()) {
                                PackSelectorActivity.this.packRepo.fetch();
                                holder.packDirectory.fetchFromRepo(PackSelectorActivity.this.packRepo);
                                holder.refreshUpdateInfoNow();
                                final boolean success = holder.selectAndLoadPack();
                                if (success) {
                                    AdsManager.getInstance().closeAllRequests();
                                    HorizonActivity.prepareForLaunch(holder);
                                    PackSelectorActivity.this.startActivity(new Intent((Context)PackSelectorActivity.this, (Class)HorizonActivity.class));
                                    PackSelectorActivity.this.finish();
                                }
                                HorizonApplication.getLock("pack_loading").unlock();
                                HorizonApplication.getLock("startup").unlock();
                            }
                        }
                    });
                }
            }
            if (isLoadingList) {
                Toast.makeText((Context)this, (CharSequence)"Failed to load previously selected pack, it is not installed or corrupted", 1).show();
                preferences.edit().putString("currently_selected_pack_path", (String)null).commit();
            }
        }
        this.setContentView(2131427361);
        if (isLoadingList) {
            HorizonApplication.getLock("startup").unlock();
            this.findViewById(2131230898).setVisibility(0);
            this.findViewById(2131230895).setVisibility(0);
            final RecyclerView recyclerView = (RecyclerView)this.findViewById(2131230899);
            final ViewAdapter adapter = new ViewAdapter();
            this.mainScreenAdapter = adapter;
            adapter.hideAllCards = true;
            recyclerView.setAdapter((RecyclerView.Adapter)adapter);
            final GridLayoutManager gridManager = new GridAutofitLayoutManager((Context)this, 202);
            gridManager.setOrientation(1);
            recyclerView.setLayoutManager((RecyclerView.LayoutManager)gridManager);
            contextHolder.getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    PackSelectorActivity.this.packRepo.fetch();
                    PackSelectorActivity.this.packStorage.fetchLocationsFromRepo(PackSelectorActivity.this.packRepo);
                    final List<PackHolder> allPackHolders = PackSelectorActivity.this.packStorage.reloadAll();
                    adapter.allPackHolders.clear();
                    adapter.allPackHolders.addAll(allPackHolders);
                    if (adapter.allPackHolders.size() == 0) {
                        final List<IPackLocation> suggestedPacksLocations = new ArrayList<IPackLocation>();
                        final StringBuilder message = new StringBuilder();
                        message.append(PackSelectorActivity.this.getResources().getString(2131624078)).append("\n");
                        for (final String suggestedUUID : PackSelectorActivity.this.packRepo.getPackSuggestions()) {
                            final IPackLocation location = PackSelectorActivity.this.packRepo.getLocationForUUID(suggestedUUID);
                            if (location != null) {
                                final PackManifest manifest = location.getManifest();
                                if (manifest == null) {
                                    continue;
                                }
                                suggestedPacksLocations.add(location);
                                message.append(" - ").append(manifest.pack).append("\n");
                            }
                        }
                        if (suggestedPacksLocations.size() > 0) {
                            PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)PackSelectorActivity.this, 2131689480);
                                    builder.setTitle(2131624113);
                                    builder.setMessage((CharSequence)message.toString());
                                    builder.setNegativeButton(2131624039, (DialogInterface.OnClickListener)null);
                                    builder.setPositiveButton(2131623978, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int which) {
                                            contextHolder.getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                                                @Override
                                                public void run() {
                                                    for (final IPackLocation location : suggestedPacksLocations) {
                                                        final PackHolder packHolder = PackSelectorActivity.this.packStorage.loadNewPackHolderFromLocation(location);
                                                        if (packHolder != null) {
                                                            PackSelectorActivity.this.mainScreenAdapter.allPackHolders.add(packHolder);
                                                        }
                                                    }
                                                    PackSelectorActivity.this.mainScreenAdapter.refreshPackHolderList();
                                                }
                                            });
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }
                        else {
                            DialogHelper.showTipDialog((Activity)PackSelectorActivity.this, 2131624113, 2131624077);
                        }
                    }
                    PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            adapter.hideAllCards = false;
                            PackSelectorActivity.this.findViewById(2131230895).setVisibility(4);
                            adapter.refreshPackHolderList();
                        }
                    });
                }
            });
        }
        else {
            this.findViewById(2131230896).setVisibility(0);
            if (!isMovedToBack && AdsManager.getInstance().runDesiredHorizonDensityRandom()) {
                AdsManager.getInstance().loadConcurrentAd(new String[] { "interstitial_video", "interstitial" }, 10, true, new AdsManager.AdListener() {
                    @Override
                    public void onAdLoaded(final AdContainer container) {
                        container.inflate(null);
                    }
                }, "horizon-dev");
            }
        }
        this.findViewById(2131230822).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View v) {
                PackSelectorActivity.this.openHelpDialog();
            }
        });
    }
    
    private void loadFavoritePacksUUIDs() {
        final SharedPreferences preferences = this.getSharedPreferences("packs", 0);
        final Set<String> favorites = (Set<String>)preferences.getStringSet("favorites", (Set)null);
        if (favorites != null) {
            this.favoritePacksUUIDs.addAll(favorites);
        }
    }
    
    @SuppressLint({ "ApplySharedPref" })
    private void saveFavoritePacksUUIDs() {
        final SharedPreferences preferences = this.getSharedPreferences("packs", 0);
        preferences.edit().putStringSet("favorites", (Set)this.favoritePacksUUIDs).commit();
    }
    
    private void addToFavorites(final ViewAdapter adapter, final PackHolder holder) {
        final String uuid = holder.getInternalPackID();
        if (uuid != null && !this.favoritePacksUUIDs.contains(uuid)) {
            this.favoritePacksUUIDs.add(uuid);
            this.saveFavoritePacksUUIDs();
            adapter.refreshPackHolderList();
        }
    }
    
    private void removeFromFavorites(final ViewAdapter adapter, final PackHolder holder) {
        final String uuid = holder.getInternalPackID();
        if (uuid != null && this.favoritePacksUUIDs.contains(uuid)) {
            this.favoritePacksUUIDs.remove(uuid);
            this.saveFavoritePacksUUIDs();
            adapter.refreshPackHolderList();
        }
    }
    
    private int[] getLargeDialogSize() {
        final Point screenSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new int[] { (int)(screenSize.x * 0.6), (int)(screenSize.y * 0.92) };
    }
    
    public void openAddPackDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this, 2131689480);
        final LayoutInflater inflater = LayoutInflater.from((Context)this);
        final View view = inflater.inflate(2131427365, (ViewGroup)null, false);
        builder.setView(view);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(2131230751);
        final View progressBar = view.findViewById(2131230752);
        final View failureText = view.findViewById(2131230750);
        progressBar.setVisibility(0);
        recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this));
        builder.setNegativeButton(17039360, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int which) {
            }
        });
        final AddPackListAdapter adapter = new AddPackListAdapter(this.packStorage.getContextHolder());
        recyclerView.setAdapter((RecyclerView.Adapter)adapter);
        final Dialog dialog = adapter.addPackDialog = (Dialog)builder.show();
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            final int[] dialogSize = this.getLargeDialogSize();
            dialogWindow.setLayout(dialogSize[0], dialogSize[1]);
        }
        final TaskManager taskManager = this.packStorage.contextHolder.getTaskManager();
        taskManager.addTask(new TaskSequence.AnonymousTask() {
            @Override
            public void run() {
                PackSelectorActivity.this.packRepo.fetch();
                final List<String> packsUUIDs = PackSelectorActivity.this.packRepo.getAllPacksUUIDs();
                final List<IPackLocation> locations = new ArrayList<IPackLocation>();
                for (final String uuid : packsUUIDs) {
                    final IPackLocation location = PackSelectorActivity.this.packRepo.getLocationForUUID(uuid);
                    if (location != null) {
                        locations.add(location);
                    }
                }
                if (dialog.isShowing()) {
                    adapter.addLocations(locations, new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(4);
                        }
                    });
                    if (locations.size() == 0) {
                        PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                failureText.setVisibility(0);
                            }
                        });
                    }
                }
            }
        });
    }
    
    public void openHelpDialog() {
        final String language = LocaleUtils.getLanguage(this.getResources());
        String instructions = "<b>Failed to read help instructions</b>";
        try {
            instructions = FileUtils.readStringFromAsset(this.getAssets(), "doc/help/" + language + ".html");
        }
        catch (IOException e) {
            try {
                instructions = FileUtils.readStringFromAsset(this.getAssets(), "doc/help/en.html");
            }
            catch (IOException ex) {}
        }
        instructions = instructions.replaceAll("\n", "<br>");
        final AlertDialog.Builder dialog = new AlertDialog.Builder((Context)this, 2131689480);
        dialog.setTitle(2131624025);
        dialog.setMessage((CharSequence)Html.fromHtml(instructions));
        final Window window = dialog.show().getWindow();
        if (window != null) {
            final int[] dialogSize = this.getLargeDialogSize();
            window.setLayout(dialogSize[0], dialogSize[1]);
        }
    }
    
    public class ViewAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        final List<PackHolder> allPackHolders;
        final List<PackHolder> packHoldersToDisplay;
        public boolean hideAllCards;
        
        public ViewAdapter() {
            this.allPackHolders = new ArrayList<PackHolder>();
            this.packHoldersToDisplay = new ArrayList<PackHolder>();
            this.hideAllCards = false;
        }
        
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from((Context)PackSelectorActivity.this);
            final View view = inflater.inflate(2131427362, parent, false);
            return new ViewHolder(view);
        }
        
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int index) {
            viewHolder.setShowProgress(false);
            viewHolder.setDisabled(false);
            if (index == this.packHoldersToDisplay.size()) {
                viewHolder.setMode(2131230886);
                viewHolder.setCollapseMode();
            }
            else {
                viewHolder.setMode(2131230890);
                viewHolder.setPackHolder(this.packHoldersToDisplay.get(index));
            }
        }
        
        public int getItemCount() {
            return this.hideAllCards ? 0 : (this.packHoldersToDisplay.size() + 1);
        }
        
        public void refreshPackHolderList() {
            synchronized (this.allPackHolders) {
                this.packHoldersToDisplay.clear();
                this.packHoldersToDisplay.addAll(this.allPackHolders);
                Collections.sort(this.packHoldersToDisplay, new Comparator<PackHolder>() {
                    @Override
                    public int compare(final PackHolder o1, final PackHolder o2) {
                        final int i1 = PackSelectorActivity.this.favoritePacksUUIDs.contains(o1.getInternalPackID()) ? 1 : 0;
                        final int i2 = PackSelectorActivity.this.favoritePacksUUIDs.contains(o2.getInternalPackID()) ? 1 : 0;
                        final long time1 = o1.packDirectory.getTimestamp();
                        final long time2 = o2.packDirectory.getTimestamp();
                        return (i2 - i1) * 10 + ((time1 > time2) ? 1 : ((time1 < time2) ? -1 : 0));
                    }
                });
            }
            PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ViewAdapter.this.notifyDataSetChanged();
                    PackSelectorActivity.this.findViewById(2131230822).setVisibility((ViewAdapter.this.packHoldersToDisplay.size() > 0 && !ViewAdapter.this.hideAllCards) ? 0 : 4);
                }
            });
        }
        
        public void refreshAfterPackRemoval(final PackHolder packHolder) {
            synchronized (this.allPackHolders) {
                for (int i = 0; i < this.packHoldersToDisplay.size(); ++i) {
                    if (this.packHoldersToDisplay.get(i) == packHolder) {
                        final int index = i;
                        this.allPackHolders.remove(packHolder);
                        this.packHoldersToDisplay.remove(packHolder);
                        PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                ViewAdapter.this.notifyItemRemoved(index);
                            }
                        });
                    }
                }
            }
        }
        
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            PackHolder packHolder;
            
            public ViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnLongClickListener((View.OnLongClickListener)new View.OnLongClickListener() {
                    public boolean onLongClick(final View v) {
                        if (ViewHolder.this.packHolder != null) {
                            final Context wrapper = (Context)new ContextThemeWrapper((Context)PackSelectorActivity.this, 2131689478);
                            final PopupMenu menu = new PopupMenu(wrapper, v);
                            ViewHolder.this.buildContextMenu(menu);
                            menu.show();
                            return true;
                        }
                        return false;
                    }
                });
            }
            
            private void buildContextMenu(final PopupMenu menu) {
                final int ID_ADD_FAVORITE = 1;
                final int ID_REMOVE_FAVORITE = 2;
                final int ID_INSTALL = 3;
                final int ID_REINSTALL = 4;
                final int ID_DELETE = 5;
                final int ID_UPDATE = 6;
                final int ID_CLONE = 7;
                final int ID_SHOW_INFO = 8;
                final int ID_RENAME = 9;
                final String uuid = this.packHolder.getInternalPackID();
                final PackHolder.State state = this.packHolder.getState();
                if (PackSelectorActivity.this.favoritePacksUUIDs.contains(uuid)) {
                    menu.getMenu().add(0, 2, 0, 2131624051);
                }
                else {
                    menu.getMenu().add(0, 1, 0, 2131624044);
                }
                if (state != PackHolder.State.INSTALLED) {
                    if (state == PackHolder.State.CORRUPT) {
                        menu.getMenu().add(0, 4, 0, 2131624050);
                        menu.getMenu().add(0, 5, 0, 2131624046);
                    }
                    else {
                        menu.getMenu().add(0, 3, 0, 2131624049);
                    }
                }
                else {
                    if (this.packHolder.isUpdateAvailable()) {
                        menu.getMenu().add(0, 6, 0, 2131624056);
                    }
                    menu.getMenu().add(0, 7, 0, 2131624045);
                    menu.getMenu().add(0, 4, 0, 2131624050);
                }
                menu.getMenu().add(0, 9, 0, 2131624052);
                menu.getMenu().add(0, 8, 0, 2131624053);
                menu.getMenu().add(0, 5, 0, 2131624046);
                menu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {
                        final int choice = item.getItemId();
                        ViewHolder.this.packHolder.getContextHolder().getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                            @Override
                            public void run() {
                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewHolder.this.setShowProgress(true);
                                    }
                                });
                                switch (choice) {
                                    case 1: {
                                        PackSelectorActivity.this.addToFavorites(ViewAdapter.this, ViewHolder.this.packHolder);
                                        break;
                                    }
                                    case 2: {
                                        PackSelectorActivity.this.removeFromFavorites(ViewAdapter.this, ViewHolder.this.packHolder);
                                        break;
                                    }
                                    case 3: {
                                        ViewHolder.this.packHolder.selectAndLoadPack();
                                        break;
                                    }
                                    case 4: {
                                        if (DialogHelper.awaitDecision((Activity)PackSelectorActivity.this, 2131624013, 2131624081, 2131624095, 17039360)) {
                                            ViewHolder.this.packHolder.reinstall();
                                            break;
                                        }
                                        break;
                                    }
                                    case 8: {
                                        ViewHolder.this.packHolder.showDialogWithPackInfo((Activity)PackSelectorActivity.this, true);
                                        break;
                                    }
                                    case 6: {
                                        String changelog = null;
                                        final IPackLocation location = ViewHolder.this.packHolder.packDirectory.getLocation();
                                        if (location != null) {
                                            changelog = location.getChangelog();
                                        }
                                        if (changelog == null || changelog.length() == 0) {
                                            changelog = PackSelectorActivity.this.getResources().getString(2131623984);
                                        }
                                        else {
                                            changelog = PackSelectorActivity.this.getResources().getString(2131624087) + "\n<h5>" + PackSelectorActivity.this.getResources().getString(2131623983) + "</h5>" + changelog;
                                            changelog = changelog.replaceAll("\n", "<br>");
                                        }
                                        if (DialogHelper.awaitDecision((Activity)PackSelectorActivity.this, 2131624088, Html.fromHtml(changelog), 2131624095, 17039360)) {
                                            ViewHolder.this.packHolder.update();
                                            break;
                                        }
                                        break;
                                    }
                                    case 7: {
                                        final DialogHelper.EditStringDialog dialog = new DialogHelper.EditStringDialog((Activity)PackSelectorActivity.this);
                                        final int[] dialogSize = PackSelectorActivity.this.getLargeDialogSize();
                                        dialog.setSize(dialogSize[0], dialogSize[1]);
                                        dialog.setLabels(2131624070, 2131624071).setDescription(2131624069).setListener(2131624006, null);
                                        final String customName = ViewHolder.this.packHolder.packDirectory.getCustomName();
                                        if (customName != null) {
                                            dialog.setDefaultValue(customName);
                                        }
                                        else {
                                            final PackManifest manifest = ViewHolder.this.packHolder.getManifest();
                                            if (manifest != null) {
                                                dialog.setDefaultValue(manifest.pack);
                                            }
                                        }
                                        dialog.open();
                                        final String clonedName = dialog.awaitResult();
                                        if (clonedName != null) {
                                            final PackHolder holder = ViewHolder.this.packHolder.clone(PackSelectorActivity.this.packRepo, clonedName);
                                            if (holder != null) {
                                                ViewAdapter.this.allPackHolders.add(holder);
                                                ViewAdapter.this.refreshPackHolderList();
                                            }
                                            else {
                                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText((Context)PackSelectorActivity.this, (CharSequence)"Failed to clone pack", 1).show();
                                                    }
                                                });
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    case 9: {
                                        final DialogHelper.EditStringDialog dialog = new DialogHelper.EditStringDialog((Activity)PackSelectorActivity.this);
                                        final int[] dialogSize = PackSelectorActivity.this.getLargeDialogSize();
                                        dialog.setSize(dialogSize[0], dialogSize[1]);
                                        dialog.setLabels(2131624083, 2131624084).setListener(2131624006, null);
                                        final String customName = ViewHolder.this.packHolder.packDirectory.getCustomName();
                                        if (customName != null) {
                                            dialog.setDefaultValue(customName);
                                        }
                                        else {
                                            final PackManifest manifest = ViewHolder.this.packHolder.getManifest();
                                            if (manifest != null) {
                                                dialog.setDefaultValue(manifest.pack);
                                            }
                                        }
                                        dialog.open();
                                        final String renamed = dialog.awaitResult();
                                        if (renamed != null) {
                                            ViewHolder.this.packHolder.packDirectory.setCustomName(renamed);
                                            ViewHolder.this.packHolder.getContextHolder().getTaskManager().addTask(ViewHolder.this.getNonVisualSetupTask());
                                            break;
                                        }
                                        break;
                                    }
                                    case 5: {
                                        if (DialogHelper.awaitDecision((Activity)PackSelectorActivity.this, 2131624013, 2131624073, 2131624095, 17039360)) {
                                            ViewHolder.this.packHolder.deletePack();
                                            synchronized (ViewAdapter.this.allPackHolders) {
                                                ViewAdapter.this.allPackHolders.remove(ViewHolder.this.packHolder);
                                                ViewAdapter.this.refreshPackHolderList();
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                }
                                ViewHolder.this.refreshPackHolder();
                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewHolder.this.setShowProgress(false);
                                    }
                                });
                            }
                        });
                        return true;
                    }
                });
            }
            
            private void onSelectRequested() {
                if (PackSelectorActivity.this.currentSelectingPackHolder == null) {
                    PackSelectorActivity.this.currentSelectingPackHolder = this.packHolder;
                    this.setShowProgress(true);
                    this.packHolder.getContextHolder().getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                        @Override
                        public void run() {
                            final boolean loaded = ViewHolder.this.packHolder.selectAndLoadPack();
                            if (loaded) {
                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @SuppressLint({ "ApplySharedPref" })
                                    @Override
                                    public void run() {
                                        if (PackSelectorActivity.this.isFinishing()) {
                                            return;
                                        }
                                        HorizonActivity.prepareForLaunch(ViewHolder.this.packHolder);
                                        PackSelectorActivity.this.startActivity(new Intent((Context)PackSelectorActivity.this, (Class)HorizonActivity.class));
                                        ViewHolder.this.setShowProgress(false);
                                        final SharedPreferences preferences = PackSelectorActivity.this.getSharedPreferences("packs", 0);
                                        preferences.edit().putString("currently_selected_pack_path", ViewHolder.this.packHolder.getInstallationDir().getAbsolutePath()).commit();
                                        new Handler().postDelayed((Runnable)new Runnable() {
                                            @Override
                                            public void run() {
                                                PackSelectorActivity.this.currentSelectingPackHolder = null;
                                            }
                                        }, 500L);
                                    }
                                });
                            }
                            else {
                                PackSelectorActivity.this.currentSelectingPackHolder = null;
                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewHolder.this.setShowProgress(false);
                                        Toast.makeText((Context)PackSelectorActivity.this, (CharSequence)"Failed to load selected pack", 1).show();
                                    }
                                });
                            }
                            ViewHolder.this.refreshPackHolder();
                        }
                    });
                }
            }
            
            public void setMode(final int id) {
                final int[] array;
                final int[] modes = array = new int[] { 2131230890, 2131230886 };
                for (final int mode : array) {
                    final View modeLayout = this.itemView.findViewById(mode);
                    if (modeLayout != null) {
                        modeLayout.setVisibility((mode == id) ? 0 : 4);
                    }
                }
            }
            
            public void setShowProgress(final boolean show) {
                final ProgressBar progressBar = (ProgressBar)this.itemView.findViewById(2131230891);
                progressBar.setVisibility(show ? 0 : 4);
            }
            
            public void setDisabled(final boolean disabled) {
                final ImageView disabledOverlay = (ImageView)this.itemView.findViewById(2131230888);
                disabledOverlay.setVisibility(disabled ? 0 : 4);
            }
            
            public void setShowBookmark(final boolean favorite) {
                final ImageView bookmark = (ImageView)this.itemView.findViewById(2131230885);
                bookmark.setVisibility(favorite ? 0 : 4);
            }
            
            public void setShowUpdate(final boolean updateAvailable) {
                final ImageView icon = (ImageView)this.itemView.findViewById(2131230894);
                icon.setVisibility(updateAvailable ? 0 : 4);
            }
            
            public void setCollapseMode() {
                final TextView collapseIcon = (TextView)this.itemView.findViewById(2131230887);
                collapseIcon.setText((CharSequence)"+");
                this.setShowBookmark(false);
                this.setShowUpdate(false);
                this.itemView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                    public void onClick(final View v) {
                        PackSelectorActivity.this.openAddPackDialog();
                    }
                });
            }
            
            private void refreshPackHolder() {
                if (this.packHolder != null) {
                    this.packHolder.runRefreshUpdateInfoTask(new Runnable() {
                        @Override
                        public void run() {
                            PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.this.setShowUpdate(ViewHolder.this.packHolder.isUpdateAvailable());
                                }
                            });
                        }
                    });
                }
                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (ViewHolder.this.packHolder != null) {
                            ViewHolder.this.setDisabled(ViewHolder.this.packHolder.getState() != PackHolder.State.INSTALLED);
                            ViewHolder.this.setShowBookmark(PackSelectorActivity.this.favoritePacksUUIDs.contains(ViewHolder.this.packHolder.getInternalPackID()));
                        }
                    }
                });
            }
            
            public void setPackHolder(final PackHolder holder) {
                if (this.packHolder != holder) {
                    PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            ViewHolder.this.clearAllViews();
                        }
                    });
                }
                this.packHolder = holder;
                final TaskManager taskManager = this.packHolder.getContextHolder().getTaskManager();
                if (taskManager != null) {
                    taskManager.addTask(this.getNonVisualSetupTask());
                    taskManager.addTask(this.getVisualSetupTask());
                }
                this.refreshPackHolder();
                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ViewHolder.this.itemView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                            public void onClick(final View v) {
                                if (ViewHolder.this.packHolder != null) {
                                    ViewHolder.this.onSelectRequested();
                                }
                            }
                        });
                    }
                });
            }
            
            public Task getNonVisualSetupTask() {
                return new Task() {
                    @Override
                    public Object getLock() {
                        return ViewAdapter.this;
                    }
                    
                    @Override
                    public void run() {
                        final PackManifest manifest = ViewHolder.this.packHolder.getManifest();
                        PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                ViewHolder.this.initNonVisualPart(manifest);
                            }
                        });
                    }
                };
            }
            
            @SuppressLint({ "SetTextI18n" })
            private void initNonVisualPart(final PackManifest manifest) {
                final TextView mainTitle = (TextView)this.itemView.findViewById(2131230889);
                final TextView secondTitle = (TextView)this.itemView.findViewById(2131230892);
                mainTitle.setText((CharSequence)(manifest.pack + ((manifest.packVersion != null) ? (" " + manifest.packVersion) : "")));
                secondTitle.setText((CharSequence)(manifest.game + ((manifest.gameVersion != null) ? (" " + manifest.gameVersion) : "")));
                if (this.packHolder != null) {
                    final String customName = this.packHolder.packDirectory.getCustomName();
                    if (customName != null) {
                        mainTitle.setText((CharSequence)customName);
                    }
                }
            }
            
            public Task getVisualSetupTask() {
                return new Task() {
                    @Override
                    public Object getLock() {
                        return ViewAdapter.this;
                    }
                    
                    @Override
                    public int getQueuePriority() {
                        return -1;
                    }
                    
                    @Override
                    public void run() {
                        final PackGraphics graphics = ViewHolder.this.packHolder.getGraphics();
                        PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                ViewHolder.this.initVisualPart(graphics);
                            }
                        });
                    }
                };
            }
            
            private void initVisualPart(final PackGraphics graphics) {
                final ImageView thumbnail = (ImageView)this.itemView.findViewById(2131230893);
                final Collection<Bitmap> thumbnails = graphics.getGroup("thumbnail");
                if (thumbnails != null) {
                    thumbnail.setImageDrawable((Drawable)new AnimatedBitmapCollectionDrawable(thumbnails, 3125, 500));
                }
                else {
                    thumbnail.setImageDrawable((Drawable)new ColorDrawable(PackSelectorActivity.this.getResources().getColor(2131034141)));
                }
            }
            
            private void clearAllViews() {
                final ImageView thumbnail = (ImageView)this.itemView.findViewById(2131230893);
                thumbnail.setImageDrawable((Drawable)new ColorDrawable(PackSelectorActivity.this.getResources().getColor(2131034141)));
                final TextView title = (TextView)this.itemView.findViewById(2131230749);
                if (title != null) {
                    title.setText((CharSequence)"");
                }
                final TextView description = (TextView)this.itemView.findViewById(2131230748);
                if (description != null) {
                    description.setText((CharSequence)"");
                }
            }
        }
    }
    
    public class GridAutofitLayoutManager extends GridLayoutManager
    {
        private int columnWidth;
        private boolean isColumnWidthChanged;
        private int lastWidth;
        private int lastHeight;
        
        public GridAutofitLayoutManager(final Context context, int columnWidth) {
            super(context, 1);
            this.isColumnWidthChanged = true;
            columnWidth = (int)TypedValue.applyDimension(1, (float)columnWidth, PackSelectorActivity.this.getResources().getDisplayMetrics());
            this.setColumnWidth(this.checkedColumnWidth(context, columnWidth));
        }
        
        public GridAutofitLayoutManager(final Context context, final int columnWidth, final int orientation, final boolean reverseLayout) {
            super(context, 1, orientation, reverseLayout);
            this.isColumnWidthChanged = true;
            this.setColumnWidth(this.checkedColumnWidth(context, columnWidth));
        }
        
        private int checkedColumnWidth(@NonNull final Context context, final int columnWidth) {
            return columnWidth;
        }
        
        public void setColumnWidth(final int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != this.columnWidth) {
                this.columnWidth = newColumnWidth;
                this.isColumnWidthChanged = true;
            }
        }
        
        public void onLayoutChildren(@NonNull final RecyclerView.Recycler recycler, @NonNull final RecyclerView.State state) {
            final int width = this.getWidth();
            final int height = this.getHeight();
            if (this.columnWidth > 0 && width > 0 && height > 0 && (this.isColumnWidthChanged || this.lastWidth != width || this.lastHeight != height)) {
                int totalSpace;
                if (this.getOrientation() == 1) {
                    totalSpace = width - this.getPaddingRight() - this.getPaddingLeft();
                }
                else {
                    totalSpace = height - this.getPaddingTop() - this.getPaddingBottom();
                }
                final int spanCount = Math.max(1, totalSpace / this.columnWidth);
                this.setSpanCount(spanCount);
                this.isColumnWidthChanged = false;
            }
            this.lastWidth = width;
            this.lastHeight = height;
            super.onLayoutChildren(recycler, state);
        }
    }
    
    public class AddPackListAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private Dialog addPackDialog;
        public final ContextHolder contextHolder;
        private final List<LocationWrap> locationsToShow;
        
        public AddPackListAdapter(final ContextHolder contextHolder) {
            this.locationsToShow = new ArrayList<LocationWrap>();
            this.contextHolder = contextHolder;
        }
        
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from((Context)PackSelectorActivity.this);
            final View view = inflater.inflate(2131427366, parent, false);
            return new ViewHolder(view);
        }
        
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int index) {
            final LocationWrap locationWrap = this.locationsToShow.get(index);
            viewHolder.setLocation(locationWrap.location);
            viewHolder.initNonVisualPart(locationWrap.manifest);
            this.contextHolder.getTaskManager().addTask(viewHolder.getVisualInitTask());
        }
        
        public int getItemCount() {
            return this.locationsToShow.size();
        }
        
        public void addLocations(final List<IPackLocation> locations, final Runnable onComplete) {
            for (final IPackLocation location : locations) {
                final LocationWrap wrap = new LocationWrap(location);
                wrap.downloadManifest();
                this.locationsToShow.add(wrap);
            }
            PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    AddPackListAdapter.this.notifyDataSetChanged();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            });
        }
        
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private IPackLocation location;
            
            public ViewHolder(final View itemView) {
                super(itemView);
            }
            
            public void setLocation(final IPackLocation location) {
                this.location = location;
            }
            
            private void initNonVisualPart(final PackManifest manifest) {
                final TextView title = (TextView)this.itemView.findViewById(2131230749);
                if (title != null) {
                    title.setText((CharSequence)manifest.pack);
                }
                final TextView description = (TextView)this.itemView.findViewById(2131230748);
                if (description != null) {
                    description.setText((CharSequence)(manifest.game + " " + manifest.gameVersion + "\n" + manifest.description));
                }
                this.itemView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                    public void onClick(final View v) {
                        final DialogHelper.EditStringDialog dialog = new DialogHelper.EditStringDialog((Activity)PackSelectorActivity.this);
                        final int[] dialogSize = PackSelectorActivity.this.getLargeDialogSize();
                        dialog.setSize(dialogSize[0], dialogSize[1]);
                        dialog.setHeading(manifest.pack + "\nFor " + manifest.game + " " + manifest.gameVersion).setDescription(manifest.description);
                        dialog.setDefaultValue(manifest.pack);
                        dialog.setLabels(2131624011, 2131624012);
                        dialog.setListener(2131624006, new DialogHelper.EditStringDialog.ResultListener() {
                            @Override
                            public void onConfirm(final String value) {
                                AddPackListAdapter.this.contextHolder.getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                                    @Override
                                    public void run() {
                                        final PackHolder packHolder = PackSelectorActivity.this.packStorage.loadNewPackHolderFromLocation(ViewHolder.this.location);
                                        if (packHolder != null) {
                                            packHolder.packDirectory.setCustomName(value);
                                            PackSelectorActivity.this.mainScreenAdapter.allPackHolders.add(packHolder);
                                            PackSelectorActivity.this.mainScreenAdapter.refreshPackHolderList();
                                        }
                                    }
                                });
                                if (AddPackListAdapter.this.addPackDialog != null) {
                                    AddPackListAdapter.this.addPackDialog.cancel();
                                }
                            }
                            
                            @Override
                            public void onCancel() {
                            }
                        });
                        dialog.open();
                    }
                });
            }
            
            private void initVisualPart(final PackGraphics graphics) {
                final ImageView iconView = (ImageView)this.itemView.findViewById(2131230747);
                iconView.setImageDrawable((Drawable)new AnimatedBitmapCollectionDrawable(graphics.getGroup("thumbnail"), 3125, 500));
            }
            
            public Task getVisualInitTask() {
                return new Task() {
                    @Override
                    public Object getLock() {
                        return ViewHolder.this;
                    }
                    
                    @Override
                    public void run() {
                        final InputStream visualStream = ViewHolder.this.location.getVisualDataStream();
                        if (visualStream != null) {
                            try {
                                final PackGraphics graphics = new PackGraphics(visualStream);
                                PackSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewHolder.this.initVisualPart(graphics);
                                    }
                                });
                            }
                            catch (IOException ex) {}
                        }
                    }
                };
            }
        }
        
        class LocationWrap
        {
            public final IPackLocation location;
            public PackManifest manifest;
            
            LocationWrap(final IPackLocation location) {
                this.manifest = null;
                this.location = location;
            }
            
            public void downloadManifest() {
                this.manifest = this.location.getManifest();
            }
        }
    }
}
