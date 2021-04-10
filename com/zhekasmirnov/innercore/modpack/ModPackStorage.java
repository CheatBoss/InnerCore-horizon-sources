package com.zhekasmirnov.innercore.modpack;

import com.android.tools.r8.annotations.*;
import java.util.function.*;
import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import org.json.*;
import java.io.*;
import java.util.zip.*;
import com.zhekasmirnov.innercore.modpack.installation.*;

@SynthesizedClassMap({ -$$Lambda$ModPackStorage$qCD284klC5JkVs3dwur_3v-6RjI.class, -$$Lambda$ModPackStorage$JeSbCIEk49ulaTDJ7XZdyG-9EHA.class })
public class ModPackStorage
{
    private final ModPackContext context;
    private final ModPack defaultModPack;
    private final File defaultModPackDirectory;
    private final ModPackFactory factory;
    private final List<ModPack> modPacks;
    private final File packsArchiveDirectory;
    private final File packsDirectory;
    
    public ModPackStorage(final File packsDirectory, final File packsArchiveDirectory, final File defaultModPackDirectory) {
        this.context = ModPackContext.getInstance();
        this.factory = ModPackFactory.getInstance();
        this.modPacks = new ArrayList<ModPack>();
        this.packsDirectory = packsDirectory;
        this.packsArchiveDirectory = packsArchiveDirectory;
        this.defaultModPackDirectory = defaultModPackDirectory;
        packsDirectory.mkdirs();
        this.defaultModPack = this.factory.createDefault(defaultModPackDirectory);
    }
    
    private static String getAvailablePackFileName(final ModPackManifest modPackManifest, final Predicate<String> predicate) {
        String s2;
        final String s = s2 = normalizeFileName(modPackManifest.getPackName());
        if (!predicate.test(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("-");
            sb.append(normalizeFileName(modPackManifest.getDisplayedName()));
            final String s3 = s2 = sb.toString();
            if (!predicate.test(s3)) {
                int n = 0;
                while (true) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s3);
                    sb2.append("-");
                    sb2.append(n);
                    if (predicate.test(sb2.toString())) {
                        break;
                    }
                    ++n;
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(s3);
                sb3.append("-");
                sb3.append(n);
                return sb3.toString();
            }
        }
        return s2;
    }
    
    private static String normalizeFileName(final String s) {
        if (s != null && !s.equals("")) {
            return s.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        }
        return "unnamed";
    }
    
    public File archiveAndDeletePack(final ModPack modPack, final ModPack.TaskReporter taskReporter) throws InterruptedException {
        if (this.isDefaultModPack(modPack)) {
            throw new IllegalArgumentException("default modpack cannot be deleted");
        }
        final File archivePack = this.archivePack(modPack, taskReporter);
        FileUtils.clearFileTree(modPack.getRootDirectory(), true);
        return archivePack;
    }
    
    public File archivePack(final ModPack modPack, final ModPack.TaskReporter taskReporter) throws InterruptedException {
        if (!modPack.reloadAndValidateManifest() || modPack.getManifest().getPackName() == null) {
            ModPack.interruptTask("failed to load pack manifest");
        }
        final File packsArchiveDirectory = this.packsArchiveDirectory;
        final StringBuilder sb = new StringBuilder();
        sb.append(getAvailablePackFileName(modPack.getManifest(), new -$$Lambda$ModPackStorage$JeSbCIEk49ulaTDJ7XZdyG-9EHA(this)));
        sb.append(".zip");
        final File file = new File(packsArchiveDirectory, sb.toString());
        file.getParentFile().mkdirs();
        try {
            final ZipFileExtractionTarget zipFileExtractionTarget = new ZipFileExtractionTarget(file);
            final Object o = null;
            try {
                try {
                    modPack.extract(zipFileExtractionTarget, taskReporter);
                    if (zipFileExtractionTarget != null) {
                        zipFileExtractionTarget.close();
                    }
                }
                finally {
                    if (zipFileExtractionTarget != null) {
                        if (o != null) {
                            final ZipFileExtractionTarget zipFileExtractionTarget2 = zipFileExtractionTarget;
                            zipFileExtractionTarget2.close();
                        }
                        else {
                            zipFileExtractionTarget.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final ZipFileExtractionTarget zipFileExtractionTarget2 = zipFileExtractionTarget;
                zipFileExtractionTarget2.close();
            }
            catch (Throwable t2) {}
        }
        catch (IOException ex) {
            file.delete();
            ModPack.interruptTask(ex, "failed to create extraction target");
        }
        taskReporter.reportResult(true);
        return file;
    }
    
    public void deletePack(final ModPack modPack) {
        if (this.isDefaultModPack(modPack)) {
            throw new IllegalArgumentException("default modpack cannot be deleted");
        }
        this.modPacks.remove(modPack);
        FileUtils.clearFileTree(modPack.getRootDirectory(), true);
    }
    
    public List<File> getAllArchivedPacks() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<File>();
        final File[] listFiles = this.packsArchiveDirectory.listFiles();
        if (listFiles != null) {
            list.addAll(Arrays.asList(listFiles));
        }
        return (List<File>)list;
    }
    
    public List<ModPack> getAllModPacks() {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.add(this.defaultModPack);
        list.addAll(this.getNonDefaultModPacks());
        return (List<ModPack>)list;
    }
    
    public ModPackContext getContext() {
        return this.context;
    }
    
    public ModPack getDefaultModPack() {
        return this.defaultModPack;
    }
    
    public File getDefaultModPackDirectory() {
        return this.defaultModPackDirectory;
    }
    
    public List<ModPack> getNonDefaultModPacks() {
        return this.modPacks;
    }
    
    public File getPacksArchiveDirectory() {
        return this.packsArchiveDirectory;
    }
    
    public File getPacksDirectory() {
        return this.packsDirectory;
    }
    
    public ModPack installNewModPack(final ModpackInstallationSource modpackInstallationSource, final ModPack.TaskReporter taskReporter) throws InterruptedException {
        try {
            final File file = new File(this.packsDirectory, getAvailablePackFileName(modpackInstallationSource.getTempManifest(), new -$$Lambda$ModPackStorage$qCD284klC5JkVs3dwur_3v-6RjI(this)));
            file.mkdirs();
            if (!file.isDirectory()) {
                taskReporter.reportError("failed to create pack directory", new IOException(), true);
                ModPack.interruptTask("failed to create pack directory");
            }
            final ModPack fromDirectory = ModPackFactory.getInstance().createFromDirectory(file);
            fromDirectory.installOrUpdate(modpackInstallationSource, taskReporter);
            if (fromDirectory.reloadAndValidateManifest()) {
                this.modPacks.add(fromDirectory);
                return fromDirectory;
            }
            this.rebuildModPackList();
            return fromDirectory;
        }
        catch (IOException | JSONException ex3) {
            final JSONException ex2;
            final Exception ex = (Exception)ex2;
            taskReporter.reportError("failed to get manifest from installation source", ex, true);
            ModPack.interruptTask(ex, "failed to get manifest");
            return null;
        }
    }
    
    public ModPack installNewModPack(InputStream t, final ModPack.TaskReporter taskReporter) throws InterruptedException {
        try {
            final ExternalZipFileInstallationSource externalZipFileInstallationSource = new ExternalZipFileInstallationSource((InputStream)t);
            Throwable t2;
            try {
                final ModPack installNewModPack = this.installNewModPack(externalZipFileInstallationSource, taskReporter);
                if (externalZipFileInstallationSource != null) {
                    externalZipFileInstallationSource.close();
                }
                return installNewModPack;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {
                    t2 = t;
                    final Throwable t3;
                    t = t3;
                }
            }
            finally {
                t2 = null;
            }
            if (externalZipFileInstallationSource != null) {
                if (t2 != null) {
                    try {
                        externalZipFileInstallationSource.close();
                    }
                    catch (Throwable t4) {}
                }
                else {
                    externalZipFileInstallationSource.close();
                }
            }
            throw t;
        }
        catch (IOException ex) {
            taskReporter.reportError("failed to create installation source", ex, false);
            ModPack.interruptTask(ex, "failed to create installation source");
            return null;
        }
    }
    
    public boolean isDefaultModPack(final ModPack modPack) {
        return this.defaultModPackDirectory.equals(modPack.getRootDirectory());
    }
    
    public void rebuildModPackList() {
        synchronized (this) {
            this.modPacks.clear();
            final File[] listFiles = this.packsDirectory.listFiles();
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    final File file = listFiles[i];
                    if (file.isDirectory()) {
                        final ModPack fromDirectory = this.factory.createFromDirectory(file);
                        if (fromDirectory.reloadAndValidateManifest()) {
                            this.modPacks.add(fromDirectory);
                        }
                    }
                }
            }
        }
    }
    
    public ModPack unarchivePack(final File file, ModPack.TaskReporter installNewModPack, final boolean b) throws InterruptedException {
        try {
            installNewModPack = (ModPack.TaskReporter)this.installNewModPack(new ZipFileInstallationSource(new ZipFile(file)), installNewModPack);
            if (b) {
                file.delete();
            }
            return (ModPack)installNewModPack;
        }
        catch (IOException ex) {
            installNewModPack.reportError("failed to create installation source", ex, true);
            ModPack.interruptTask(ex, "failed to create installation source");
            return null;
        }
    }
}
