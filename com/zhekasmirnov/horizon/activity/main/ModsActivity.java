package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import android.os.*;
import android.support.v7.widget.*;
import android.content.*;
import android.text.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import java.util.*;
import com.zhekasmirnov.horizon.activity.util.*;
import android.app.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;
import android.graphics.drawable.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import android.graphics.*;

public class ModsActivity extends AppCompatActivity
{
    private static PackHolder pendingPackHolder;
    private PackHolder packHolder;
    private List<Mod> modsToDisplay;
    private Mod currentlySelectedMod;
    private boolean isUninterpretableTaskRunning;
    private ViewAdapter listAdapter;
    
    public ModsActivity() {
        this.modsToDisplay = new ArrayList<Mod>();
        this.isUninterpretableTaskRunning = false;
    }
    
    public static void prepareForStart(final PackHolder packHolder) {
        ModsActivity.pendingPackHolder = packHolder;
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131427359);
        if (this.packHolder == null) {
            this.packHolder = ModsActivity.pendingPackHolder;
            if (this.packHolder == null) {
                throw new RuntimeException("ModsActivity has not called prepareForStart");
            }
        }
        this.listAdapter = new ViewAdapter();
        final RecyclerView modListView = (RecyclerView)this.findViewById(2131230873);
        modListView.setAdapter((RecyclerView.Adapter)this.listAdapter);
        modListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this));
        this.refreshModList(null);
        this.refreshConfigureButton();
        this.refreshModLayout(this.currentlySelectedMod);
    }
    
    private void refreshModList(final Runnable onComplete) {
        this.packHolder.getModList().startRefreshTask(new Runnable() {
            @Override
            public void run() {
                ModsActivity.this.modsToDisplay.clear();
                ModsActivity.this.modsToDisplay.addAll(ModsActivity.this.packHolder.getModContext().getActiveMods());
                ModsActivity.this.modsToDisplay.addAll(ModsActivity.this.packHolder.getModContext().getDisabledMods());
                if (onComplete != null) {
                    onComplete.run();
                }
                ModsActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ModsActivity.this.listAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    
    private void refreshModLayout(final Mod mod) {
        final LinearLayout layout = (LinearLayout)this.findViewById(2131230871);
        final RelativeLayout layoutNoMods = (RelativeLayout)this.findViewById(2131230872);
        if (mod != null) {
            layout.setVisibility(0);
            layoutNoMods.setVisibility(4);
            this.findViewById(2131230866).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View v) {
                    final Context wrapper = (Context)new ContextThemeWrapper((Context)ModsActivity.this, 2131689478);
                    final PopupMenu menu = new PopupMenu(wrapper, v);
                    ModsActivity.this.buildContextMenu(menu, mod);
                    menu.show();
                }
            });
            final TextView modTitle = (TextView)layout.findViewById(2131230937);
            modTitle.setText((CharSequence)mod.getDisplayedName());
            final TextView modDescription = (TextView)layout.findViewById(2131230935);
            final ModManifest.Module info = mod.manifest.getMainModule();
            modDescription.setText((CharSequence)info.getDisplayedDescription());
            final TextView modStatus = (TextView)layout.findViewById(2131230936);
            modStatus.setText((CharSequence)Html.fromHtml(this.buildModStatus(mod)));
        }
        else {
            layout.setVisibility(4);
            layoutNoMods.setVisibility(0);
        }
    }
    
    private String buildModStatus(final Mod mod) {
        final StringBuilder builder = new StringBuilder("<br><br><b>-- STATUS --</b><br>");
        builder.append("<b>CPP libraries:</b><br>");
        for (final LibraryDirectory library : mod.libraries) {
            builder.append(library.getName());
            if (library.soFile.exists()) {
                if (library.isPreCompiled()) {
                    builder.append("<font color='#0000ff'><b>&nbsp[pre-compiled]</b></font>");
                }
                else {
                    builder.append("<font color='#008800'><b>&nbsp[compiled]</b></font>");
                }
            }
            builder.append("<br>");
        }
        builder.append("<b>Java libraries:</b><br>");
        for (final JavaDirectory javaDir : mod.java) {
            builder.append(javaDir.getName()).append("<br>");
        }
        builder.append("<b>Resource paths:</b><br>");
        for (final ResourceDirectory resourceDir : mod.resources) {
            builder.append(resourceDir.directory.getName()).append("<br>");
        }
        return builder.toString();
    }
    
    private void buildContextMenu(final PopupMenu menu, final Mod mod) {
        final int ID_ENABLE = 1;
        final int ID_DISABLE = 2;
        final int ID_TO_PRODUCTION_MODE = 3;
        final int ID_TO_DEVELOPER_MODE = 4;
        final int ID_DELETE = 5;
        final Mod.DeveloperInterface developerInterface = mod.getDeveloperInterface();
        final Mod.ConfigurationInterface configurationInterface = mod.getConfigurationInterface();
        final Mod.SafetyInterface safetyInterface = mod.getSafetyInterface();
        if (configurationInterface.isActive()) {
            menu.getMenu().add(0, 2, 0, 2131624047);
        }
        else {
            menu.getMenu().add(0, 1, 0, 2131624048);
        }
        if (developerInterface.anyForDeveloperModeTransfer()) {
            menu.getMenu().add(0, 4, 0, 2131624054);
        }
        if (developerInterface.anyForProductionModeTransfer()) {
            menu.getMenu().add(0, 3, 0, 2131624055);
        }
        menu.getMenu().add(0, 5, 0, 2131624046);
        menu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(final MenuItem item) {
                ModsActivity.this.isUninterpretableTaskRunning = true;
                ModsActivity.this.refreshConfigureButton();
                final int choice = item.getItemId();
                ModsActivity.this.packHolder.getContextHolder().getTaskManager().addTask(new TaskSequence.AnonymousTask() {
                    @Override
                    public void run() {
                        boolean rebuildModList = false;
                        boolean deselect = false;
                        switch (choice) {
                            case 1: {
                                configurationInterface.setActive(true);
                                rebuildModList = true;
                                break;
                            }
                            case 2: {
                                configurationInterface.setActive(false);
                                rebuildModList = true;
                                break;
                            }
                            case 4: {
                                developerInterface.toDeveloperMode();
                                break;
                            }
                            case 3: {
                                developerInterface.toProductModeUiProtocol();
                                break;
                            }
                            case 5: {
                                if (DialogHelper.awaitDecision((Activity)ModsActivity.this, 2131624013, 2131624060, 2131624095, 17039360)) {
                                    FileUtils.clearFileTree(mod.directory, true);
                                    rebuildModList = true;
                                    deselect = true;
                                    break;
                                }
                                break;
                            }
                        }
                        this.refreshAfterCompleted(rebuildModList, deselect);
                    }
                    
                    private void refreshAfterCompleted(final boolean rebuildModList, final boolean deselect) {
                        ModsActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                ModsActivity.this.refreshModLayout(deselect ? null : mod);
                                if (rebuildModList) {
                                    ModsActivity.this.refreshModList(new Runnable() {
                                        @Override
                                        public void run() {
                                            ModsActivity.this.isUninterpretableTaskRunning = false;
                                            ModsActivity.this.runOnUiThread((Runnable)new Runnable() {
                                                @Override
                                                public void run() {
                                                    ModsActivity.this.refreshConfigureButton();
                                                }
                                            });
                                        }
                                    });
                                }
                                else {
                                    ModsActivity.this.isUninterpretableTaskRunning = false;
                                    ModsActivity.this.refreshConfigureButton();
                                }
                            }
                        });
                    }
                });
                return true;
            }
        });
    }
    
    public void onBackPressed() {
        if (this.isUninterpretableTaskRunning) {
            Toast.makeText((Context)this, (CharSequence)"Cannot exit right now, important task is running", 1).show();
        }
        else {
            super.onBackPressed();
        }
    }
    
    private void refreshConfigureButton() {
        final View configureButton = this.findViewById(2131230866);
        configureButton.findViewById(2131230867).setVisibility(this.isUninterpretableTaskRunning ? 4 : 0);
        configureButton.findViewById(2131230868).setVisibility(this.isUninterpretableTaskRunning ? 0 : 4);
    }
    
    static {
        ModsActivity.pendingPackHolder = null;
    }
    
    private class ViewAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {
            final LayoutInflater inflater = LayoutInflater.from((Context)ModsActivity.this);
            final View view = inflater.inflate(2131427360, parent, false);
            return new ViewHolder(view);
        }
        
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
            viewHolder.setMod(ModsActivity.this.modsToDisplay.get(i));
        }
        
        public int getItemCount() {
            return ModsActivity.this.modsToDisplay.size();
        }
        
        private class ViewHolder extends RecyclerView.ViewHolder
        {
            private Mod mod;
            
            public ViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                    public void onClick(final View v) {
                        if (!ModsActivity.this.isUninterpretableTaskRunning && ModsActivity.this.modsToDisplay.contains(ViewHolder.this.mod)) {
                            ModsActivity.this.currentlySelectedMod = ViewHolder.this.mod;
                            ModsActivity.this.refreshModLayout(ViewHolder.this.mod);
                        }
                    }
                });
            }
            
            private void setMod(final Mod mod) {
                this.mod = mod;
                final TextView titleView = (TextView)this.itemView.findViewById(2131230870);
                String modTitle = mod.getDisplayedName();
                if (!mod.getConfigurationInterface().isActive()) {
                    modTitle = ModsActivity.this.getResources().getString(2131624010) + " " + modTitle;
                }
                titleView.setText((CharSequence)modTitle);
                final ImageView iconView = (ImageView)this.itemView.findViewById(2131230869);
                final ModGraphics graphics = mod.getGraphics();
                Collection<Bitmap> bitmaps = graphics.getGroup("wide");
                if (bitmaps == null || bitmaps.size() == 0) {
                    bitmaps = graphics.getGroup("icon");
                }
                if (bitmaps != null && bitmaps.size() > 0) {
                    iconView.setImageDrawable((Drawable)new AnimatedBitmapCollectionDrawable(bitmaps, 6000, 700));
                }
            }
        }
    }
}
