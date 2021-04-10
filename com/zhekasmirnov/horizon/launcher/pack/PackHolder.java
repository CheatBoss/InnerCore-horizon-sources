package com.zhekasmirnov.horizon.launcher.pack;

import com.zhekasmirnov.horizon.*;
import org.json.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.zhekasmirnov.horizon.launcher.*;
import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.modloader.repo.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.activity.util.*;
import android.app.*;
import android.content.*;
import android.text.*;

public class PackHolder
{
    private static final String FLAG_INSTALLATION_STARTED = "installation_started";
    private static final String FLAG_INSTALLATION_COMPLETE = "installation_complete";
    public final PackStorage storage;
    public final PackDirectory packDirectory;
    public final List<PackSavesHolder> savesHolders;
    private State state;
    private File installationDir;
    private Pack pack;
    private boolean isUpdateAvailable;
    private PackGraphics graphics;
    private static final List<PackHolder> loadedPackHolders;
    private boolean isPreparedForLaunch;
    
    public PackHolder(final PackStorage storage, final PackDirectory directory) {
        this.savesHolders = new ArrayList<PackSavesHolder>();
        this.state = State.NOT_INITIALIZED;
        this.installationDir = null;
        this.pack = null;
        this.isUpdateAvailable = false;
        this.graphics = null;
        this.isPreparedForLaunch = false;
        this.storage = storage;
        this.packDirectory = directory;
        this.refreshSavesHolders(false);
    }
    
    private void refreshSavesHolders(final boolean fromManifest) {
        this.savesHolders.clear();
        this.savesHolders.add(new PackSavesHolder(this.packDirectory.directory, new File(this.packDirectory.directory, "saves/data"), HorizonApplication.getExternalDataDir()));
        if (fromManifest) {
            final PackManifest manifest = this.packDirectory.getLocalManifest();
            if (manifest != null) {
                for (final String name : manifest.savesHoldersInfo.keySet()) {
                    final String path = manifest.savesHoldersInfo.get(name);
                    if (path != null) {
                        this.savesHolders.add(new PackSavesHolder(this.packDirectory.directory, new File(this.packDirectory.directory, "saves/" + name), new File(path)));
                    }
                }
            }
        }
    }
    
    public PackManifest getManifest() {
        PackManifest manifest = this.packDirectory.getManifest();
        if (manifest == null && this.installationDir != null) {
            try {
                manifest = new PackManifest(new File(this.installationDir, "manifest.json"));
            }
            catch (IOException ex) {}
            catch (JSONException ex2) {}
        }
        return manifest;
    }
    
    public void runRefreshUpdateInfoTask(final Runnable onRefreshed) {
        this.getContextHolder().getTaskManager().addTask(new TaskSequence.AnonymousTask() {
            @Override
            public void run() {
                PackHolder.this.isUpdateAvailable = PackHolder.this.packDirectory.isUpdateAvailable();
                if (onRefreshed != null) {
                    onRefreshed.run();
                }
            }
        });
    }
    
    public void refreshUpdateInfoNow() {
        this.isUpdateAvailable = this.packDirectory.isUpdateAvailable();
    }
    
    public boolean isUpdateAvailable() {
        return this.isUpdateAvailable;
    }
    
    private PackGraphics tryToLoadGraphics(final InputStream visualData) {
        if (visualData != null) {
            try {
                return new PackGraphics(visualData);
            }
            catch (IOException e) {
                Logger.error("Failed to decode pack graphics stream: " + e);
            }
        }
        return null;
    }
    
    public void pullAllSaves() {
        this.refreshSavesHolders(true);
        for (final PackSavesHolder savesHolder : this.savesHolders) {
            savesHolder.pullSavesForThisPack();
        }
    }
    
    public void pushAllSaves() {
        this.refreshSavesHolders(true);
        for (final PackSavesHolder savesHolder : this.savesHolders) {
            savesHolder.pushSavesIfRequired();
        }
    }
    
    public synchronized PackGraphics getGraphics() {
        if (this.graphics == null) {
            final File local = new File(this.installationDir, "graphics.zip");
            try {
                return this.graphics = this.tryToLoadGraphics(new FileInputStream(local));
            }
            catch (FileNotFoundException ex) {
                PackGraphics cachedGraphics = null;
                final File cache = new File(this.installationDir, ".cached_graphics");
                try {
                    cachedGraphics = this.tryToLoadGraphics(new FileInputStream(cache));
                }
                catch (FileNotFoundException ex2) {}
                if (cachedGraphics == null) {
                    final InputStream externalGraphicsData = this.packDirectory.getVisualDataStream();
                    if (externalGraphicsData != null) {
                        try {
                            FileUtils.unpackInputStream(externalGraphicsData, cache);
                            this.graphics = this.tryToLoadGraphics(new FileInputStream(cache));
                        }
                        catch (IOException ex3) {}
                    }
                    if (this.graphics == null) {
                        this.graphics = this.tryToLoadGraphics(this.packDirectory.getVisualDataStream());
                        if (this.graphics == null) {
                            this.graphics = new PackGraphics();
                        }
                    }
                }
                else {
                    this.graphics = cachedGraphics;
                }
            }
        }
        return this.graphics;
    }
    
    public void initialize() {
        final File directory = this.packDirectory.directory;
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new RuntimeException("pack location returned non-directory file as local dir: " + directory);
            }
            if (FileUtils.getFileFlag(directory, "installation_complete")) {
                this.state = State.INSTALLED;
            }
            else if (FileUtils.getFileFlag(directory, "installation_started")) {
                this.state = State.CORRUPT;
            }
            else {
                this.state = State.PENDING;
            }
        }
        else {
            this.state = State.NOT_INSTALLED;
        }
        this.installationDir = directory;
    }
    
    public void uninstall(final List<String> whitelist) {
        if (this.installationDir != null && this.installationDir.exists()) {
            for (final File file : this.installationDir.listFiles()) {
                if (!whitelist.contains(file.getName())) {
                    Logger.debug("PackHolder", "deleting " + file);
                    if (file.isDirectory()) {
                        FileUtils.clearFileTree(file, true);
                    }
                    else {
                        file.delete();
                    }
                }
            }
            this.state = State.NOT_INSTALLED;
        }
    }
    
    public void deletePack() {
        this.uninstall(new ArrayList<String>());
        this.installationDir.delete();
    }
    
    private static String beautifyUnpackMessage(String message) {
        message = message.replaceAll("\\.dex", ".zip");
        message = message.replaceAll("classes", "resources");
        message = message.replaceAll("lib", "");
        message = message.replaceAll("\\.so", ".zip");
        message = message.replaceAll("so/", "");
        message = message.replaceAll("armeabi-v7a", "1");
        message = message.replaceAll("x86", "2");
        return message;
    }
    
    public InstallationResult install(final List<String> reinstallWhitelist) {
        if (this.installationDir == null || this.state == State.NOT_INITIALIZED) {
            throw new RuntimeException("installing pack without completed initialization");
        }
        final DialogHelper.ProgressDialogHolder dialog = new DialogHelper.ProgressDialogHolder(2131624064, 2131624067);
        dialog.open();
        dialog.setText(2131624079);
        final InputStream installationPackageStream = this.packDirectory.getInstallationPackageStream();
        if (installationPackageStream != null) {
            this.installationDir.mkdirs();
            final File packageFile = new File(this.installationDir, ".installation_package");
            if (packageFile.exists()) {
                packageFile.delete();
            }
            this.packDirectory.updateLocalUUID();
            dialog.setText(2131624074);
            try {
                int downloaded = 0;
                final int downloadSize = this.packDirectory.getInstallationPackageSize();
                final byte[] bytes = new byte[8192];
                final FileOutputStream outputStream = new FileOutputStream(packageFile);
                int read;
                while ((read = installationPackageStream.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, read);
                    downloaded += read;
                    dialog.onProgress(downloaded / (double)downloadSize);
                    dialog.onDownloadMessage("Downloading: " + Math.round(downloaded / (double)downloadSize * 100.0) + "%");
                    if (dialog.isTerminated()) {
                        outputStream.close();
                        installationPackageStream.close();
                        packageFile.delete();
                        dialog.close();
                        return InstallationResult.ABORT;
                    }
                }
                installationPackageStream.close();
                outputStream.close();
            }
            catch (IOException e) {
                dialog.close();
                e.printStackTrace();
                return InstallationResult.ERROR;
            }
            dialog.setText(2131624080);
            this.state = State.PENDING;
            final List<String> cleanupWhiteList = new ArrayList<String>(reinstallWhitelist);
            cleanupWhiteList.add(".installation_package");
            this.uninstall(cleanupWhiteList);
            FileUtils.setFileFlag(this.installationDir, "installation_started", true);
            FileUtils.setFileFlag(this.installationDir, "installation_complete", false);
            this.state = State.CORRUPT;
            try {
                dialog.setText(2131624085);
                final ZipInputStream packageZip = new ZipInputStream(new FileInputStream(packageFile));
                ZipEntry entry;
                while ((entry = packageZip.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        dialog.onDownloadMessage("Unpacking: " + beautifyUnpackMessage(entry.getName()));
                        final File output = new File(this.installationDir, entry.getName());
                        output.getParentFile().mkdirs();
                        FileUtils.unpackInputStream(packageZip, output, false);
                    }
                    if (dialog.isTerminated()) {
                        FileUtils.setFileFlag(this.installationDir, "installation_started", false);
                        dialog.setText(2131624068);
                        this.uninstall(reinstallWhitelist);
                        dialog.close();
                        return InstallationResult.ABORT;
                    }
                }
            }
            catch (IOException e2) {
                dialog.close();
                e2.printStackTrace();
                return InstallationResult.ERROR;
            }
            FileUtils.setFileFlag(this.installationDir, "installation_complete", true);
            dialog.setText(2131624076);
            this.graphics = null;
            this.getGraphics();
        }
        else {
            FileUtils.setFileFlag(this.installationDir, "installation_started", true);
            FileUtils.setFileFlag(this.installationDir, "installation_complete", true);
        }
        dialog.close();
        this.state = State.INSTALLED;
        return InstallationResult.SUCCESS;
    }
    
    public InstallationResult reinstall() {
        if (this.installationDir == null || this.state == State.NOT_INITIALIZED) {
            throw new RuntimeException("installing pack without completed initialization");
        }
        this.pullAllSaves();
        final List<String> whitelist = this.buildReinstallWhitelist(false);
        whitelist.add(".installation_package");
        this.uninstall(whitelist);
        final Activity context = this.getContextHolder().getContext();
        final File cachedPackageFile = new File(this.installationDir, ".installation_package");
        if (cachedPackageFile.exists()) {
            final DialogHelper.ProgressDialogHolder dialog = new DialogHelper.ProgressDialogHolder(context, 2131624064, 2131624067);
            dialog.open();
            FileUtils.setFileFlag(this.installationDir, "installation_complete", false);
            FileUtils.setFileFlag(this.installationDir, "installation_started", true);
            try {
                dialog.setText(2131624082);
                final ZipInputStream packageZip = new ZipInputStream(new FileInputStream(cachedPackageFile));
                ZipEntry entry;
                while ((entry = packageZip.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        dialog.onDownloadMessage("Unpacking: " + entry.getName());
                        final File output = new File(this.installationDir, entry.getName());
                        output.getParentFile().mkdirs();
                        FileUtils.unpackInputStream(packageZip, output, false);
                    }
                    if (dialog.isTerminated()) {
                        FileUtils.setFileFlag(this.installationDir, "installation_started", false);
                        dialog.setText(2131624068);
                        this.uninstall(whitelist);
                        dialog.close();
                        return InstallationResult.ABORT;
                    }
                }
                FileUtils.setFileFlag(this.installationDir, "installation_complete", true);
                this.state = State.INSTALLED;
                dialog.close();
                return InstallationResult.SUCCESS;
            }
            catch (IOException e) {
                e.printStackTrace();
                dialog.close();
            }
        }
        if (DialogHelper.awaitDecision(2131624013, 2131624075, 2131624095, 17039360)) {
            return this.install(whitelist);
        }
        return InstallationResult.ABORT;
    }
    
    private List<String> buildReinstallWhitelist(final boolean useExternalManifest) {
        final List<String> whitelist = new ArrayList<String>();
        if (useExternalManifest) {
            final PackManifest externalManifest = this.packDirectory.getExternalManifest();
            if (externalManifest != null) {
                whitelist.addAll(externalManifest.keepDirectories);
            }
        }
        final PackManifest localManifest = this.packDirectory.getLocalManifest();
        if (localManifest != null) {
            whitelist.addAll(localManifest.keepDirectories);
        }
        whitelist.add("mods");
        whitelist.add("native_mods");
        whitelist.add("saves");
        whitelist.add("worlds");
        whitelist.add("config");
        whitelist.add("visual");
        whitelist.add(".installation_package");
        whitelist.add(".installation_info");
        return whitelist;
    }
    
    public InstallationResult update() {
        if (this.installationDir == null || this.state == State.NOT_INITIALIZED) {
            throw new RuntimeException("installing pack without completed initialization");
        }
        this.pullAllSaves();
        final InstallationResult result = this.install(this.buildReinstallWhitelist(true));
        if (result == InstallationResult.SUCCESS) {
            this.packDirectory.reloadLocalManifest();
            this.isUpdateAvailable = false;
        }
        return result;
    }
    
    public PackHolder clone(final PackRepository repository, final String name) {
        if (this.state != State.INSTALLED) {
            return null;
        }
        this.pullAllSaves();
        final DialogHelper.ProgressDialogHolder dialog = new DialogHelper.ProgressDialogHolder(2131624064, 2131623981);
        dialog.setText(2131624072);
        dialog.open();
        final PackDirectory directory = this.storage.makeNewPackDirectory(name);
        if (directory == null) {
            dialog.close();
            return null;
        }
        try {
            FileUtils.copyFileTree(this.installationDir, directory.directory, dialog, "");
            if (dialog.isTerminated()) {
                FileUtils.clearFileTree(directory.directory, true);
                dialog.close();
                return null;
            }
        }
        catch (IOException e) {
            FileUtils.clearFileTree(directory.directory, true);
            dialog.close();
            return null;
        }
        this.storage.fetchLocationsFromRepo(repository);
        final PackHolder holder = this.storage.loadPackHolderFromDirectory(directory);
        if (holder != null) {
            holder.packDirectory.generateNewInternalID();
            holder.packDirectory.updateTimestamp();
            holder.packDirectory.setCustomName(name);
            dialog.close();
            return holder;
        }
        FileUtils.clearFileTree(directory.directory, true);
        dialog.close();
        return null;
    }
    
    public void deselectAndUnload() {
        synchronized (PackHolder.loadedPackHolders) {
            if (PackHolder.loadedPackHolders.contains(this)) {
                PackHolder.loadedPackHolders.remove(this);
                if (this.pack != null) {
                    this.pack.unload();
                }
            }
        }
    }
    
    public State getState() {
        return this.state;
    }
    
    public File getInstallationDir() {
        return this.installationDir;
    }
    
    public PackStorage getStorage() {
        return this.storage;
    }
    
    public String getPackUUID() {
        return this.packDirectory.getUUID();
    }
    
    public String getInternalPackID() {
        return this.packDirectory.getInternalID();
    }
    
    public ContextHolder getContextHolder() {
        return this.storage.contextHolder;
    }
    
    public ModContext getModContext() {
        if (this.pack != null) {
            return this.pack.modContext;
        }
        return null;
    }
    
    public ModList getModList() {
        if (this.pack != null) {
            return this.pack.modList;
        }
        return null;
    }
    
    public ResourceManager getResourceManager() {
        if (this.pack != null) {
            return this.pack.resourceManager;
        }
        return null;
    }
    
    public boolean isLoaded() {
        return PackHolder.loadedPackHolders.contains(this);
    }
    
    public boolean selectAndLoadPack() {
        this.initialize();
        if (this.state != State.INSTALLED) {
            InstallationResult result = InstallationResult.ABORT;
            final Activity context = this.getContextHolder().getContext();
            if (this.state == State.CORRUPT) {
                if (DialogHelper.awaitDecision(2131624013, 2131624066, 2131624095, 17039360)) {
                    result = this.reinstall();
                }
            }
            else if (DialogHelper.awaitDecision(2131624013, 2131624065, 2131624095, 17039360)) {
                final List<String> whitelist = new ArrayList<String>();
                whitelist.add(".installation_info");
                whitelist.add("manifest.json");
                result = this.install(whitelist);
            }
            if (result != InstallationResult.SUCCESS) {
                return false;
            }
        }
        if (this.packDirectory.getLocalManifest() == null) {
            return false;
        }
        while (PackHolder.loadedPackHolders.size() > 0) {
            PackHolder.loadedPackHolders.get(0).deselectAndUnload();
        }
        synchronized (PackHolder.loadedPackHolders) {
            PackHolder.loadedPackHolders.add(this);
            (this.pack = new Pack(this.storage.contextHolder, this.installationDir)).initialize();
            this.pushAllSaves();
            CompilerErrorDialogHelper.showCompilationErrors(this.pack.modContext);
        }
        return true;
    }
    
    public Pack getPack() {
        return this.pack;
    }
    
    public boolean isPreparedForLaunch() {
        return this.isPreparedForLaunch;
    }
    
    public synchronized void prepareForLaunch() {
        if (!this.isPreparedForLaunch && this.pack != null) {
            this.pack.load();
            this.isPreparedForLaunch = true;
        }
    }
    
    public void showDialogWithPackInfo(final Activity ctx, final boolean checkExternalManifest) {
        final PackManifest manifest = this.getManifest();
        final PackManifest external = checkExternalManifest ? this.packDirectory.getExternalManifest() : null;
        final IPackLocation location = this.packDirectory.getLocation();
        final String changelog = checkExternalManifest ? ((location != null) ? location.getChangelog() : null) : null;
        ctx.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final String externalUUID = PackHolder.this.getPackUUID();
                final State state = PackHolder.this.getState();
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ctx, 2131689480);
                builder.setTitle((CharSequence)(manifest.pack + ((manifest.packVersion != null) ? (" " + manifest.packVersion) : "")));
                builder.setMessage((CharSequence)(manifest.description + "\n\nGame: " + manifest.game + ((manifest.gameVersion != null) ? (" " + manifest.gameVersion) : "") + "\nVersion: " + ((manifest.packVersion != null) ? manifest.packVersion : Integer.valueOf(manifest.packVersionCode)) + ((external != null) ? (", newest is " + ((external.packVersion != null) ? external.packVersion : Integer.valueOf(external.packVersionCode))) : "") + "\nState: " + state.toString() + "\nLocal directory: " + PackHolder.this.packDirectory.directory.getName() + "\nExternal UUID: " + ((externalUUID != null) ? externalUUID : "this is local pack") + "\n"));
                builder.setPositiveButton(17039370, (DialogInterface.OnClickListener)null);
                if (changelog != null && changelog.length() > 0) {
                    builder.setNeutralButton(2131624121, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int which) {
                            PackHolder.this.showChangelogDialog(ctx, changelog, null);
                        }
                    });
                }
                builder.show();
            }
        });
    }
    
    private void showChangelogDialog(final Activity ctx, final String changelog, final Runnable updateAction) {
        if (changelog != null && changelog.length() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ctx, 2131689480);
            builder.setTitle(2131623983);
            builder.setMessage((CharSequence)Html.fromHtml(changelog.replaceAll("\n", "<br>")));
            builder.setPositiveButton((updateAction != null) ? 17039360 : 17039370, (DialogInterface.OnClickListener)null);
            if (updateAction != null) {
                builder.setNeutralButton(2131624114, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        updateAction.run();
                    }
                });
            }
            builder.show();
        }
    }
    
    public void showDialogWithChangelog(final Activity ctx, final Runnable updateAction) {
        final IPackLocation location = this.packDirectory.getLocation();
        final String changelog = (location != null) ? location.getChangelog() : null;
        if (changelog != null && changelog.length() > 0) {
            ctx.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    PackHolder.this.showChangelogDialog(ctx, changelog, updateAction);
                }
            });
        }
    }
    
    static {
        loadedPackHolders = new ArrayList<PackHolder>();
    }
    
    public enum State
    {
        NOT_INITIALIZED, 
        NOT_INSTALLED, 
        PENDING, 
        CORRUPT, 
        INSTALLED;
    }
    
    enum InstallationResult
    {
        SUCCESS, 
        ABORT, 
        ERROR;
    }
}
