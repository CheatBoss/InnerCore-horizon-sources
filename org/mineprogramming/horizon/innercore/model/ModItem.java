package org.mineprogramming.horizon.innercore.model;

import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import android.net.*;
import android.content.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.json.*;

public class ModItem extends Item
{
    private ModCompilationHelper compilationHelper;
    private List<ModDependency> installList;
    private String location;
    
    public ModItem(String directory) {
        this.installList = new ArrayList<ModDependency>();
        this.setLocation(directory);
        directory = (String)this.getDirectory();
        final File file = new File((File)directory, "mod.info");
        final File file2 = new File((File)directory, "mod_icon.png");
        try {
            final JSONObject json = FileTools.readJSON(file.getAbsolutePath());
            this.setTitle(json.optString("name"));
            this.setDescriptionShort(json.optString("description"));
            this.setAuthorName(json.optString("author"));
            this.setVersionName(json.optString("version"));
        }
        catch (IOException | JSONException ex) {
            this.setTitle(((File)directory).getName());
        }
        if (file2.exists()) {
            this.setIcon(Uri.fromFile(file2));
        }
        final ModPreferences modPreferences = new ModPreferences((File)directory);
        this.setId(modPreferences.getIcmodsId());
        this.setVersionCode(modPreferences.getIcmodsVersion());
    }
    
    public ModItem(final JSONObject jsonObject) {
        super(jsonObject);
        this.installList = new ArrayList<ModDependency>();
        this.setLocation(ModTracker.getCurrent().getLocation(this.getId()));
    }
    
    private void initializeCompilationHelper() {
        if (this.compilationHelper == null) {
            this.compilationHelper = new ModCompilationHelper(this.getDirectory());
        }
    }
    
    public void compile(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        this.initializeCompilationHelper();
        this.compilationHelper.compile(context, modPack$TaskReporter);
    }
    
    public String getConfigPath() {
        return ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory$DirectoryType.CONFIG).get(this.location, "config.json").getAbsolutePath();
    }
    
    public File getDirectory() {
        return ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory$DirectoryType.MODS).get(this.location);
    }
    
    public List<ModDependency> getInstallList() {
        return this.installList;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public boolean isCompiled() {
        this.initializeCompilationHelper();
        return this.compilationHelper.isCompiled();
    }
    
    public void onDeleted() {
        this.setLocation(null);
    }
    
    public void onInstalled(File file, final String location) {
        this.setLocation(location);
        final File file2 = new File(this.getConfigPath());
        file = new File(file, "config.json");
        if (!file2.equals(file)) {
            file2.delete();
            file2.getParentFile().mkdirs();
            file.renameTo(file2);
        }
    }
    
    public void setDevelop() {
        this.initializeCompilationHelper();
        this.compilationHelper.setDevelop();
    }
    
    public void setLocation(final String location) {
        this.location = location;
        this.setInstalled(location != null);
    }
    
    public void updateDependencies(final JSONArray jsonArray, final ModTracker modTracker) {
        for (int i = 0; i < jsonArray.length(); ++i) {
            final ModDependency modDependency = new ModDependency(jsonArray.optJSONObject(i));
            if (!modTracker.isInstalled(modDependency.getId())) {
                this.installList.add(modDependency);
            }
        }
        this.installList.add(new ModDependency(this.getId(), this.getTitle(), this.getVersionCode()));
    }
    
    @Override
    public void updateInfo(final JSONObject jsonObject) {
        super.updateInfo(jsonObject);
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/img/");
        sb.append(jsonObject.optString("icon_full"));
        this.setIcon(Uri.parse(sb.toString()));
    }
}
