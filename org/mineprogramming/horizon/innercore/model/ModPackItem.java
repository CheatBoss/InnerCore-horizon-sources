package org.mineprogramming.horizon.innercore.model;

import com.android.tools.r8.annotations.*;
import android.net.*;
import java.util.zip.*;
import com.zhekasmirnov.innercore.modpack.installation.*;
import java.io.*;
import org.json.*;
import android.content.*;
import java.util.*;
import java.util.concurrent.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import com.zhekasmirnov.innercore.modpack.*;
import android.graphics.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.bitmap.*;
import org.mineprogramming.horizon.innercore.util.*;
import com.zhekasmirnov.innercore.utils.*;

@SynthesizedClassMap({ -$$Lambda$ModPackItem$etpFr0o6mA3uLe6um_ary3X494A.class, -$$Lambda$ModPackItem$rkTIS72XaM8JEPevg-f2SBOkO-8.class })
public class ModPackItem extends Item
{
    private ModpackInstallationSource installationSource;
    private boolean isModified;
    private ModPack modPack;
    private int modsCount;
    private File packArchive;
    
    public ModPackItem(final ModPack modPack) {
        this.modPack = modPack;
        final ModPackManifest manifest = modPack.getManifest();
        this.setTitle(manifest.getDisplayedName());
        this.setVersionName(manifest.getVersionName());
        this.setAuthorName(manifest.getAuthor());
        this.setDescriptionShort(manifest.getDescription());
        this.setInstalled(true);
        if (modPack.getIconFile().exists()) {
            this.setIcon(Uri.fromFile(modPack.getIconFile()));
        }
        final ModPackPreferences preferences = modPack.getPreferences();
        this.setId(preferences.getInt("icmods_id", 0));
        this.setVersionCode(preferences.getInt("icmods_version", 0));
        this.setModified(preferences.getBoolean("modified", true));
        this.modsCount = ModTracker.forPack(modPack).getModsCount();
    }
    
    public ModPackItem(final File packArchive) throws IOException, JSONException {
        this.packArchive = packArchive;
        this.installationSource = (ModpackInstallationSource)new ZipFileInstallationSource(new ZipFile(packArchive));
        final ModPackManifest tempManifest = this.installationSource.getTempManifest();
        this.setTitle(tempManifest.getDisplayedName());
        this.setVersionName(tempManifest.getVersionName());
        this.setAuthorName(tempManifest.getAuthor());
        this.setDescriptionShort(tempManifest.getDescription());
        this.setInstalled(false);
    }
    
    public ModPackItem(final JSONObject jsonObject) {
        super(jsonObject);
        this.setTags(jsonObject.optJSONArray("tags"));
        this.modsCount = jsonObject.optInt("mod_count");
        this.modPack = this.findLocal();
        if (this.modPack != null) {
            this.setInstalled(true);
        }
    }
    
    private ModPack findLocal() {
        for (final ModPack modPack : ModPackContext.getInstance().getStorage().getAllModPacks()) {
            if (modPack.getPreferences().getInt("icmods_id", 0) == this.getId()) {
                return modPack;
            }
        }
        return null;
    }
    
    private void installFromIcmods(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/download.php?horizon&id=");
        sb.append(this.getId());
        final String string = sb.toString();
        final File cacheDir = context.getCacheDir();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("pack");
        sb2.append(this.getId());
        sb2.append(".zip");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            final /* synthetic */ File val$outputFile = new File(cacheDir, sb2.toString());
            
            private void reportTaskProgress(final String s, final float n) {
                try {
                    modPack$TaskReporter.reportProgress(s, 1, (int)n, 100);
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException("Unable to report pack installation progress", ex);
                }
            }
            
            @Override
            public void run() {
                try {
                    final String format = String.format(JsonInflater.getString(context, "downloading"), ModPackItem.this.getTitle());
                    this.reportTaskProgress(format, 0.0f);
                    DownloadHelper.downloadFile(string, this.val$outputFile, (DownloadHelper.FileDownloadListener)new DownloadHelper.FileDownloadListener() {
                        @Override
                        public void onDownloadProgress(final long n, final long n2) {
                            ModPackItem$2.this.reportTaskProgress(format, n * 73.0f / n2);
                        }
                    });
                    final ModPack$TaskReporter modPack$TaskReporter = (ModPack$TaskReporter)new ModPack$TaskReporter() {
                        public void reportError(final String s, final Exception ex, final boolean b) throws InterruptedException {
                            modPack$TaskReporter.reportError(s, ex, b);
                        }
                        
                        public void reportProgress(final String s, final int n, final int n2, final int n3) throws InterruptedException {
                            ModPackItem$2.this.reportTaskProgress(s, (n + 1) * 6 + 73.0f);
                        }
                        
                        public void reportResult(final boolean b) {
                            ModPackItem$2.this.reportTaskProgress(JsonInflater.getString(context, "done"), 100.0f);
                            modPack$TaskReporter.reportResult(b);
                        }
                    };
                    this.reportTaskProgress(format, 73.0f);
                    if (ModPackItem.this.modPack != null) {
                        Object o = null;
                        try {
                            o = new ZipFileInstallationSource(new ZipFile(this.val$outputFile));
                        }
                        catch (IOException ex) {
                            ((ModPack$TaskReporter)modPack$TaskReporter).reportError("failed to create installation source", ex, true);
                            ModPack.interruptTask((Exception)ex, "failed to create installation source");
                        }
                        ModPackItem.this.modPack.installOrUpdate((ModpackInstallationSource)o, (ModPack$TaskReporter)modPack$TaskReporter);
                        ModPackItem.this.modPack.reloadAndValidateManifest();
                        this.val$outputFile.delete();
                    }
                    else {
                        ModPackItem.this.modPack = ModPackContext.getInstance().getStorage().unarchivePack(this.val$outputFile, (ModPack$TaskReporter)modPack$TaskReporter, true);
                    }
                    if (ModPackItem.this.modPack != null) {
                        ModPackItem.this.onInstalled(ModPackItem.this.modPack, context);
                    }
                    UpdateSource.getInstance(null).onUpdateOrDelete(ModPackItem.this.getId());
                }
                catch (IOException | InterruptedException ex4) {
                    final InterruptedException ex3;
                    final InterruptedException ex2 = ex3;
                    try {
                        modPack$TaskReporter.reportError(ex2.getLocalizedMessage(), (Exception)ex2, true);
                    }
                    catch (InterruptedException ex5) {
                        throw new RuntimeException("Unable to report pack installation error", ex2);
                    }
                }
            }
        });
    }
    
    private void installFromSource(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        Executors.newSingleThreadExecutor().execute(new -$$Lambda$ModPackItem$etpFr0o6mA3uLe6um_ary3X494A(this, modPack$TaskReporter, context));
    }
    
    private void onInstalled(final ModPack modPack, final Context context) {
        this.setInstalled(true);
        final ModPackPreferences preferences = this.modPack.getPreferences();
        preferences.setInt("icmods_id", this.getId());
        preferences.setInt("icmods_version", this.getVersionCode());
        preferences.setBoolean("modified", this.isArchived());
        preferences.save();
        try {
            this.modPack.getManifest().edit().addIfMissing("displayedName", (Object)this.getTitle()).addIfMissing("versionName", (Object)this.getVersionName()).addIfMissing("versionCode", (Object)this.getVersionCode()).addIfMissing("author", (Object)this.getAuthorName()).addIfMissing("description", (Object)this.getDescription()).commit();
            final File iconFile = this.modPack.getIconFile();
            if (!iconFile.exists()) {
                try {
                    FileTools.writeBitmap(iconFile.getAbsolutePath(), (Bitmap)Glide.with(context).load(this.getIcon()).asBitmap().transform(new BitmapTransformation[] { new NoAntialiasTransformation(context) }).into(128, 128).get());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException | JSONException ex2) {
            final Throwable t;
            t.printStackTrace();
        }
        this.installationSource = null;
    }
    
    private void setModified(final boolean isModified) {
        this.isModified = isModified;
    }
    
    public void archive(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        Executors.newSingleThreadExecutor().execute(new -$$Lambda$ModPackItem$rkTIS72XaM8JEPevg-f2SBOkO-8(this, modPack$TaskReporter, context));
    }
    
    public void clonePack(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            private void reportTaskProgress(final String s, final float n) {
                try {
                    modPack$TaskReporter.reportProgress(s, 1, (int)n, 100);
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException("Unable to report pack installation progress", ex);
                }
            }
            
            @Override
            public void run() {
                final ModPack$TaskReporter modPack$TaskReporter = (ModPack$TaskReporter)new ModPack$TaskReporter() {
                    private int cloneStage = 0;
                    
                    public void reportError(final String s, final Exception ex, final boolean b) throws InterruptedException {
                        modPack$TaskReporter.reportError(s, ex, b);
                    }
                    
                    public void reportProgress(final String s, int n, final int n2, final int n3) throws InterruptedException {
                        final float n4 = n2 * 50.0f / n3;
                        final Runnable this$1 = Runnable.this;
                        if (this.cloneStage == 0) {
                            n = 0;
                        }
                        else {
                            n = 50;
                        }
                        this$1.reportTaskProgress(s, n + n4);
                    }
                    
                    public void reportResult(final boolean b) {
                        if (this.cloneStage > 0) {
                            modPack$TaskReporter.reportResult(b);
                            return;
                        }
                        ++this.cloneStage;
                    }
                };
                final ModPackStorage storage = ModPackContext.getInstance().getStorage();
                try {
                    final File archivePack = storage.archivePack(ModPackItem.this.modPack, (ModPack$TaskReporter)modPack$TaskReporter);
                    this.reportTaskProgress("", 50.0f);
                    ModPackContext.getInstance().getStorage().unarchivePack(archivePack, (ModPack$TaskReporter)modPack$TaskReporter, true);
                    this.reportTaskProgress(JsonInflater.getString(context, "done"), 100.0f);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    public void deleteArchive() {
        this.packArchive.delete();
    }
    
    public ModPack getModPack() {
        return this.modPack;
    }
    
    public int getModsCount() {
        return this.modsCount;
    }
    
    public void install(final Context context, final ModPack$TaskReporter modPack$TaskReporter) {
        if (this.isArchived()) {
            this.installFromSource(context, modPack$TaskReporter);
            return;
        }
        this.installFromIcmods(context, modPack$TaskReporter);
    }
    
    public boolean isArchived() {
        return this.installationSource != null;
    }
    
    public boolean isDefault() {
        return this.modPack == ModPackContext.getInstance().getStorage().getDefaultModPack();
    }
    
    public boolean isModified() {
        return this.isModified;
    }
    
    public boolean isSelected() {
        return this.modPack == ModPackContext.getInstance().getCurrentModPack();
    }
    
    public void onDeleted() {
        this.modPack = null;
        this.setInstalled(false);
    }
}
