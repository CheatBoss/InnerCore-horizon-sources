package com.zhekasmirnov.innercore.modpack;

import com.zhekasmirnov.innercore.modpack.strategy.extract.*;
import com.zhekasmirnov.innercore.modpack.installation.*;
import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import java.io.*;
import java.util.*;

public class ModPack
{
    private final List<ModPackDirectory> declaredDirectories;
    private final List<ModPackDirectory> defaultDirectories;
    private final ModPackJsAdapter jsAdapter;
    private final ModPackManifest manifest;
    private final ModPackPreferences preferences;
    private final File rootDirectory;
    
    public ModPack(final File rootDirectory) {
        this.manifest = new ModPackManifest();
        this.defaultDirectories = new ArrayList<ModPackDirectory>();
        this.declaredDirectories = new ArrayList<ModPackDirectory>();
        this.rootDirectory = rootDirectory;
        this.preferences = new ModPackPreferences(this, "preferences.json");
        this.jsAdapter = new ModPackJsAdapter(this);
    }
    
    public static void interruptTask(final Exception ex, final String s) throws InterruptedException {
        throw (InterruptedException)new InterruptedException(s).initCause(ex);
    }
    
    public static void interruptTask(final String s) throws InterruptedException {
        throw new InterruptedException(s);
    }
    
    public ModPack addDirectory(final ModPackDirectory modPackDirectory) {
        modPackDirectory.assignToModPack(this);
        this.defaultDirectories.add(modPackDirectory);
        return this;
    }
    
    public void extract(final ModPackExtractionTarget modPackExtractionTarget, final TaskReporter taskReporter) throws InterruptedException {
        synchronized (this) {
            this.reloadAndValidateManifest();
            try {
                taskReporter.reportProgress("extracting modpack", 0, 0, 1);
                modPackExtractionTarget.writeFile("modpack.json", this.getManifestFile());
                final File iconFile = this.getIconFile();
                if (iconFile.exists()) {
                    modPackExtractionTarget.writeFile("pack_icon.png", iconFile);
                }
            }
            catch (IOException ex) {
                taskReporter.reportError("failed to extract manifest", ex, true);
                interruptTask(ex, "failed to extract manifest");
            }
            final ArrayList<ModPackDirectory> list = new ArrayList<ModPackDirectory>(this.defaultDirectories);
            list.addAll((Collection<?>)this.declaredDirectories);
            int n = 0;
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                final DirectoryExtractStrategy extractStrategy = iterator.next().getExtractStrategy();
                final List<File> filesToExtract = extractStrategy.getFilesToExtract();
                int n2 = 0;
                final int n3 = n + 1;
                for (final File file : filesToExtract) {
                    final String fullEntryName = extractStrategy.getFullEntryName(file);
                    int n4 = n2;
                    try {
                        final StringBuilder sb = new StringBuilder();
                        n4 = n2;
                        sb.append("extracting entry ");
                        n4 = n2;
                        sb.append(fullEntryName);
                        n4 = n2;
                        final String string = sb.toString();
                        final int n5 = n4 = n2 + 1;
                        taskReporter.reportProgress(string, n3, n5, filesToExtract.size());
                        n4 = n5;
                        modPackExtractionTarget.writeFile(fullEntryName, file);
                        n4 = n5;
                    }
                    catch (IOException ex2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("exception in extracting entry ");
                        sb2.append(fullEntryName);
                        taskReporter.reportError(sb2.toString(), ex2, false);
                    }
                    n2 = n4;
                }
                n = n3;
            }
        }
    }
    
    public List<ModPackDirectory> getAllDirectories() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<ModPackDirectory>(this.defaultDirectories);
        list.addAll(this.declaredDirectories);
        return (List<ModPackDirectory>)list;
    }
    
    public List<ModPackDirectory> getDirectoriesOfType(final ModPackDirectory.DirectoryType directoryType) {
        final ArrayList<ModPackDirectory> list = new ArrayList<ModPackDirectory>();
        for (final ModPackDirectory modPackDirectory : this.defaultDirectories) {
            if (directoryType == modPackDirectory.getType()) {
                list.add(modPackDirectory);
            }
        }
        for (final ModPackDirectory modPackDirectory2 : this.declaredDirectories) {
            if (directoryType == modPackDirectory2.getType()) {
                list.add(modPackDirectory2);
            }
        }
        return list;
    }
    
    public ModPackDirectory getDirectoryOfType(final ModPackDirectory.DirectoryType directoryType) {
        final List<ModPackDirectory> directoriesOfType = this.getDirectoriesOfType(directoryType);
        if (directoriesOfType.size() > 0) {
            return directoriesOfType.get(0);
        }
        return null;
    }
    
    public File getIconFile() {
        return new File(this.rootDirectory, "pack_icon.png");
    }
    
    public ModPackJsAdapter getJsAdapter() {
        return this.jsAdapter;
    }
    
    public ModPackManifest getManifest() {
        return this.manifest;
    }
    
    public File getManifestFile() {
        return new File(this.rootDirectory, "modpack.json");
    }
    
    public ModPackPreferences getPreferences() {
        return this.preferences;
    }
    
    public DirectorySetRequestHandler getRequestHandler(final ModPackDirectory.DirectoryType directoryType) {
        return new DirectorySetRequestHandler(this.getDirectoriesOfType(directoryType));
    }
    
    public File getRootDirectory() {
        return this.rootDirectory;
    }
    
    public void installOrUpdate(ModpackInstallationSource iterator, final TaskReporter taskReporter) throws InterruptedException {
        Object entries;
        Object o;
        final File file;
        ArrayList<ModPackDirectory> list;
        int entryCount;
        ModPackDirectory modPackDirectory;
        int n;
        Object string = null;
        Serializable name;
        StringBuilder sb;
        String string2;
        Iterator<Object> iterator2;
        ModPackDirectory modPackDirectory2;
        String localPathFromEntry;
        InputStream inputStream;
        Throwable t;
        StringBuilder sb2;
        String string3;
        Label_0286_Outer:Label_0575_Outer:
        while (true) {
            while (true) {
            Label_0369_Outer:
                while (true) {
                Label_0750:
                    while (true) {
                    Label_0747:
                        while (true) {
                            Label_0744: {
                                synchronized (this) {
                                    entries = ((ModpackInstallationSource)iterator).entries();
                                    try {
                                        o = this.getManifestFile();
                                        ((File)o).getParentFile().mkdirs();
                                        FileUtils.writeFileText((File)o, ((ModpackInstallationSource)iterator).getManifestContent());
                                        this.manifest.loadFile((File)o);
                                        taskReporter.reportProgress("loaded manifest", 0, 1, 1);
                                    }
                                    catch (IOException | JSONException ex3) {
                                        o = file;
                                        taskReporter.reportResult(false);
                                        taskReporter.reportError("failed to get pack manifest", (Exception)o, true);
                                        interruptTask((Exception)o, "failed to get pack manifest");
                                    }
                                    list = new ArrayList<ModPackDirectory>(this.defaultDirectories);
                                    list.addAll((Collection<?>)this.manifest.createDeclaredDirectoriesForModPack(this));
                                    entryCount = 0;
                                    o = list.iterator();
                                    if (((Iterator)o).hasNext()) {
                                        modPackDirectory = ((Iterator<ModPackDirectory>)o).next();
                                        n = entryCount;
                                        try {
                                            string = new StringBuilder();
                                            n = entryCount;
                                            ((StringBuilder)string).append("preparing directory ");
                                            n = entryCount;
                                            ((StringBuilder)string).append(modPackDirectory);
                                            n = entryCount;
                                            string = ((StringBuilder)string).toString();
                                            entryCount = (n = entryCount + 1);
                                            taskReporter.reportProgress((String)string, 1, entryCount, list.size());
                                            n = entryCount;
                                            modPackDirectory.getUpdateStrategy().beginUpdate();
                                            break Label_0744;
                                        }
                                        catch (IOException string) {
                                            name = new StringBuilder();
                                            ((StringBuilder)name).append("failed to begin installation for directory ");
                                            ((StringBuilder)name).append(modPackDirectory);
                                            taskReporter.reportError(((StringBuilder)name).toString(), (Exception)string, false);
                                            entryCount = n;
                                            break Label_0744;
                                        }
                                        break Label_0744;
                                    }
                                    n = 0;
                                    entryCount = ((ModpackInstallationSource)iterator).getEntryCount();
                                    if (((Enumeration)entries).hasMoreElements()) {
                                        string = ((Enumeration<ModpackInstallationSource.Entry>)entries).nextElement();
                                        name = ((ModpackInstallationSource.Entry)string).getName();
                                        sb = new StringBuilder();
                                        sb.append("updating entry ");
                                        sb.append((String)name);
                                        string2 = sb.toString();
                                        ++n;
                                        taskReporter.reportProgress(string2, 2, n, entryCount);
                                        iterator2 = list.iterator();
                                        if (!iterator2.hasNext()) {
                                            break Label_0750;
                                        }
                                        modPackDirectory2 = iterator2.next();
                                        localPathFromEntry = modPackDirectory2.getLocalPathFromEntry((String)name);
                                        if (localPathFromEntry == null) {
                                            break Label_0747;
                                        }
                                        try {
                                            inputStream = ((ModpackInstallationSource.Entry)string).getInputStream();
                                            try {
                                                modPackDirectory2.getUpdateStrategy().updateFile(localPathFromEntry, inputStream);
                                                Label_0436: {
                                                    if (inputStream != null) {
                                                        inputStream.close();
                                                        break Label_0436;
                                                    }
                                                    break Label_0436;
                                                }
                                                break Label_0747;
                                            }
                                            catch (Throwable iterator) {
                                                try {
                                                    throw iterator;
                                                }
                                                finally {
                                                    t = iterator;
                                                    iterator = (Throwable)o;
                                                }
                                            }
                                            finally {
                                                t = null;
                                            }
                                            if (inputStream != null) {
                                                if (t != null) {
                                                    try {
                                                        inputStream.close();
                                                    }
                                                    catch (Throwable o) {}
                                                }
                                                else {
                                                    inputStream.close();
                                                }
                                            }
                                            throw iterator;
                                        }
                                        catch (IOException ex) {
                                            o = new StringBuilder();
                                            ((StringBuilder)o).append("failed to update entry ");
                                            ((StringBuilder)o).append((String)name);
                                            ((StringBuilder)o).append(" (local path ");
                                            ((StringBuilder)o).append(localPathFromEntry);
                                            ((StringBuilder)o).append(") in directory ");
                                            ((StringBuilder)o).append(modPackDirectory2);
                                            taskReporter.reportError(((StringBuilder)o).toString(), ex, false);
                                            break Label_0747;
                                        }
                                    }
                                    entryCount = 0;
                                    iterator = (Throwable)list.iterator();
                                    if (((Iterator)iterator).hasNext()) {
                                        o = ((Iterator<ModPackDirectory>)iterator).next();
                                        n = entryCount;
                                        try {
                                            sb2 = new StringBuilder();
                                            n = entryCount;
                                            sb2.append("completing directory ");
                                            n = entryCount;
                                            sb2.append(o);
                                            n = entryCount;
                                            string3 = sb2.toString();
                                            entryCount = (n = entryCount + 1);
                                            taskReporter.reportProgress(string3, 3, entryCount, list.size());
                                            n = entryCount;
                                            ((ModPackDirectory)o).getUpdateStrategy().finishUpdate();
                                            break Label_0369_Outer;
                                        }
                                        catch (IOException ex2) {
                                            entries = new StringBuilder();
                                            ((StringBuilder)entries).append("failed to complete installation for directory ");
                                            ((StringBuilder)entries).append(o);
                                            taskReporter.reportError(((StringBuilder)entries).toString(), ex2, false);
                                            entryCount = n;
                                            break Label_0369_Outer;
                                        }
                                        break Label_0369_Outer;
                                    }
                                    taskReporter.reportResult(true);
                                    return;
                                }
                            }
                            continue Label_0286_Outer;
                        }
                        continue Label_0575_Outer;
                    }
                    continue Label_0369_Outer;
                }
                continue;
            }
        }
    }
    
    public boolean reloadAndValidateManifest() {
        boolean b = false;
        try {
            this.declaredDirectories.clear();
            this.manifest.loadFile(this.getManifestFile());
            this.declaredDirectories.addAll(this.manifest.createDeclaredDirectoriesForModPack(this));
            if (this.manifest.getPackName() != null) {
                b = true;
            }
            return b;
        }
        catch (IOException | JSONException ex) {
            final Throwable t;
            t.printStackTrace();
            return false;
        }
    }
    
    public interface TaskReporter
    {
        void reportError(final String p0, final Exception p1, final boolean p2) throws InterruptedException;
        
        void reportProgress(final String p0, final int p1, final int p2, final int p3) throws InterruptedException;
        
        void reportResult(final boolean p0);
    }
}
