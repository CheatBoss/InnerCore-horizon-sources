package com.zhekasmirnov.horizon.compiler.holder;

import android.app.*;
import android.content.*;
import com.zhekasmirnov.horizon.activity.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.net.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import com.zhekasmirnov.horizon.compiler.exceptions.*;
import java.util.zip.*;

public class CompilerInstaller
{
    private static final String REPOSITORY_URL = "https://gitlab.com/zheka2304/horizon-assets/raw/master/toolchain/gcc/";
    private static final String MANIFEST = "manifest.json";
    private final Activity context;
    private final File downloadDir;
    private final File installationDir;
    private final List<ZipFile> blocks;
    private File packageXmlFile;
    public static final int TASK_CLEANUP = 1;
    public static final int TASK_SEARCH_PACKAGES = 2;
    public static final int TASK_DOWNLOAD = 4;
    public static final int TASK_INSTALL = 8;
    
    public CompilerInstaller(final Activity context, final File downloadDir) {
        this.blocks = new ArrayList<ZipFile>();
        this.packageXmlFile = null;
        this.context = context;
        this.downloadDir = downloadDir;
        this.installationDir = new File(Environment.getToolchainsDir((Context)context));
    }
    
    private static void downloadFile(final String sUrl, final File outputFile, final DialogHelper.ProgressInterface downloadInterface) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        if (outputFile.exists()) {
            outputFile.delete();
        }
        Logger.debug("CompilerInstaller", "downloading file " + outputFile + " from " + sUrl);
        try {
            final URL url = new URL(sUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("invalid http-code returned code=" + connection.getResponseCode() + " message=" + connection.getResponseMessage() + " url=" + sUrl);
            }
            final int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            output = new FileOutputStream(outputFile);
            final byte[] data = new byte[1048576];
            long total = 0L;
            int count;
            while ((count = input.read(data)) != -1) {
                if (downloadInterface.isTerminated()) {
                    input.close();
                    outputFile.delete();
                    break;
                }
                total += count;
                if (fileLength > 0) {
                    downloadInterface.onProgress(total / (float)fileLength);
                }
                output.write(data, 0, count);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            }
            catch (IOException ex) {}
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    private void downloadFile(final String url, final File output) {
        downloadFile(url, output, new DialogHelper.ProgressInterface() {
            @Override
            public boolean isTerminated() {
                return false;
            }
            
            @Override
            public void onProgress(final double progress) {
            }
        });
    }
    
    private JSONObject getManifest(final boolean download) {
        final File manifest = new File(this.downloadDir, "manifest");
        if (download) {
            JSONObject json = null;
            try {
                this.downloadFile("https://gitlab.com/zheka2304/horizon-assets/raw/master/toolchain/gcc/manifest.json", manifest);
                json = FileUtils.readJSON(manifest);
            }
            catch (Throwable err) {
                manifest.delete();
                throw new RuntimeException(err);
            }
            return json;
        }
        if (manifest.exists()) {
            try {
                return FileUtils.readJSON(manifest);
            }
            catch (IOException ex) {}
            catch (JSONException ex2) {}
        }
        return null;
    }
    
    private void clearInstallation(final DialogHelper.ProgressDialogHolder dialog) {
        FileUtils.clearFileTree(this.installationDir, false);
    }
    
    private void clearDownload(final DialogHelper.ProgressDialogHolder dialog) {
        this.blocks.clear();
        this.packageXmlFile = null;
        FileUtils.clearFileTree(this.downloadDir, false);
    }
    
    private void clear(final DialogHelper.ProgressDialogHolder dialog) {
        this.clearInstallation(dialog);
        this.clearDownload(dialog);
    }
    
    private void download(final DialogHelper.ProgressDialogHolder dialog) {
        Logger.debug("CompilerInstaller", "starting download");
        this.clearDownload(dialog);
        final JSONObject manifest = this.getManifest(true);
        this.packageXmlFile = new File(this.downloadDir, "packages.xml");
        try {
            this.downloadFile("https://gitlab.com/zheka2304/horizon-assets/raw/master/toolchain/gcc/" + manifest.getString("packages-xml"), this.packageXmlFile);
        }
        catch (Throwable err) {
            this.packageXmlFile.delete();
            this.packageXmlFile = null;
            throw new RuntimeException(err);
        }
        Logger.debug("CompilerInstaller", "blocks to download: " + manifest.optJSONArray("blocks"));
        for (final String name : new JsonIterator<String>(manifest.optJSONArray("blocks"))) {
            final File block = new File(this.downloadDir, name);
            if (block.exists()) {
                block.delete();
            }
            try {
                if (dialog.isTerminated()) {
                    break;
                }
                dialog.onDownloadMessage("downloading " + name);
                downloadFile("https://gitlab.com/zheka2304/horizon-assets/raw/master/toolchain/gcc/" + name, block, dialog);
                if (dialog.isTerminated()) {
                    this.packageXmlFile = null;
                    return;
                }
                this.blocks.add(new ZipFile(block));
            }
            catch (Throwable err2) {
                block.delete();
                this.packageXmlFile = null;
                throw new RuntimeException(err2);
            }
        }
    }
    
    private void search(final DialogHelper.ProgressDialogHolder dialog) {
        this.packageXmlFile = new File(this.downloadDir, "packages.xml");
        if (!this.packageXmlFile.exists()) {
            this.packageXmlFile = null;
        }
        this.blocks.clear();
        final JSONObject manifest = this.getManifest(false);
        if (manifest != null) {
            for (final String name : new JsonIterator<String>(manifest.optJSONArray("blocks"))) {
                final File block = new File(this.downloadDir, name);
                try {
                    this.blocks.add(new ZipFile(block));
                }
                catch (Throwable err) {
                    this.packageXmlFile = null;
                    throw new RuntimeException(err);
                }
            }
        }
    }
    
    public boolean areInstallationPackagesFound() {
        return this.packageXmlFile != null;
    }
    
    private void install(final DialogHelper.ProgressDialogHolder dialog) {
        assert this.packageXmlFile != null;
        this.clearInstallation(dialog);
        final File temp = new File(this.downloadDir, ".temp.zip");
        try {
            RepoUtils.setVersion();
            final PackageInstaller installer = new PackageInstaller((Context)this.context);
            final List<PackageInfo> packages = new RepoParser().parseRepoXml(FileUtils.readFileText(this.packageXmlFile));
            int index = 0;
            for (final PackageInfo info : packages) {
                if (dialog.isTerminated()) {
                    return;
                }
                dialog.onDownloadMessage("installing " + info.getFileName());
                dialog.onProgress(index++ / (double)packages.size());
                InputStream stream = null;
                for (final ZipFile block : this.blocks) {
                    final ZipEntry entry = block.getEntry("repo/" + info.getFileName());
                    if (entry != null) {
                        Logger.debug("CompilerInstaller", "unpacking package " + info.getFileName() + " from " + block.getName() + ":" + entry.getName() + " to " + temp);
                        stream = block.getInputStream(entry);
                        break;
                    }
                }
                if (stream != null) {
                    temp.getParentFile().mkdirs();
                    FileUtils.unpackInputStream(stream, temp);
                    Logger.debug("CompilerInstaller", "installing package from " + temp);
                    try {
                        installer.install(temp, info);
                        Logger.debug("CompilerInstaller", "installed package " + info.getFileName());
                    }
                    catch (BadArchiveException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Logger.error("CompilerInstaller", "failed to find package " + info.getFileName());
                }
            }
            new File(this.installationDir, ".installation-lock").createNewFile();
        }
        catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }
    
    public boolean deleteInstallationLock() {
        return new File(this.installationDir, ".installation-lock").delete();
    }
    
    public boolean getInstallationLock() {
        return new File(this.installationDir, ".installation-lock").exists();
    }
    
    public InstallationStatus runInstallationSequence(final boolean repair) {
        if (repair) {
            if (DialogHelper.awaitDecision(2131623976, "GCC Compiler is missing or corrupt, to proceed, you should reinstall it from already downloaded packages", 2131624095, 17039360)) {
                DialogHelper.ProgressDialogHolder dialog = new DialogHelper.ProgressDialogHolder(2131624029, 2131623981);
                dialog.open();
                dialog.setText("Installing GCC compiler packages...");
                this.deleteInstallationLock();
                Throwable result = this.runInstallationTasks(dialog, 10);
                dialog.close();
                if (result != null) {
                    Logger.error("CompilerInstaller", "installation task finished with error: " + result);
                    if (DialogHelper.awaitDecision(2131623976, "GCC Compiler and its installation packages are missing or corrupt, to proceed, you should download and install it again", 2131624095, 17039360)) {
                        dialog = new DialogHelper.ProgressDialogHolder(2131624029, 2131623981);
                        dialog.open();
                        dialog.setText("Installing GCC compiler packages...");
                        result = this.runInstallationTasks(dialog, 13);
                        dialog.close();
                        if (result != null) {
                            Logger.error("CompilerInstaller", "installation task finished with error: " + result);
                            return InstallationStatus.FAILED;
                        }
                    }
                }
                if (this.getInstallationLock()) {
                    return InstallationStatus.COMPLETED;
                }
            }
            return InstallationStatus.TERMINATED;
        }
        if (DialogHelper.awaitDecision(2131623976, "To compile C and C++ you need to install GCC compiler (additional 35 MB should be downloaded)", 2131624095, 17039360)) {
            final DialogHelper.ProgressDialogHolder dialog = new DialogHelper.ProgressDialogHolder(2131624029, 2131623981);
            dialog.setText("Installing GCC compiler packages...");
            dialog.open();
            this.deleteInstallationLock();
            final Throwable result = this.runInstallationTasks(dialog, 13);
            dialog.close();
            if (result != null) {
                Logger.error("CompilerInstaller", "installation task finished with error: " + result);
                return InstallationStatus.FAILED;
            }
            if (this.getInstallationLock()) {
                return InstallationStatus.COMPLETED;
            }
        }
        return InstallationStatus.TERMINATED;
    }
    
    public Throwable runSilentInstallationTasks(final int tasks) {
        return this.runInstallationTasks(new DialogHelper.ProgressDialogHolder(2131624029, 2131623981), tasks);
    }
    
    public Throwable runInstallationTasks(final DialogHelper.ProgressDialogHolder dialog, final int tasks) {
        try {
            if ((tasks & 0x1) != 0x0) {
                if (dialog.isTerminated()) {
                    return null;
                }
                this.clear(dialog);
            }
            if ((tasks & 0x4) != 0x0) {
                if (dialog.isTerminated()) {
                    return null;
                }
                this.download(dialog);
            }
            if ((tasks & 0x2) != 0x0) {
                if (dialog.isTerminated()) {
                    return null;
                }
                this.search(dialog);
            }
            if ((tasks & 0x8) != 0x0) {
                if (dialog.isTerminated()) {
                    return null;
                }
                this.install(dialog);
            }
            return null;
        }
        catch (Throwable err) {
            return err;
        }
    }
    
    enum InstallationStatus
    {
        COMPLETED, 
        TERMINATED, 
        FAILED;
    }
}
