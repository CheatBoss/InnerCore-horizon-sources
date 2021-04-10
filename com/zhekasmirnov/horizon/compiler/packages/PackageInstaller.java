package com.zhekasmirnov.horizon.compiler.packages;

import android.content.*;
import android.util.*;
import com.pdaxrom.utils.*;
import android.os.*;
import com.zhekasmirnov.horizon.compiler.exceptions.*;
import java.io.*;
import com.zhekasmirnov.horizon.compiler.*;
import android.support.annotation.*;

public class PackageInstaller
{
    private static final String TAG = "PackageInstaller";
    private final String mToolchainDir;
    private final String mInstalledDir;
    private final String mCCToolsDir;
    private final Context mContext;
    
    public PackageInstaller(final Context context) {
        this.mContext = context;
        this.mToolchainDir = Environment.getToolchainsDir(this.mContext);
        this.mInstalledDir = Environment.getInstalledPackageDir(this.mContext);
        this.mCCToolsDir = Environment.getCCtoolsDir(this.mContext);
    }
    
    public void install(final File zipFile, final PackageInfo packageInfo) throws NotEnoughCacheException, BadArchiveException {
        final String tempPath = zipFile.getAbsolutePath();
        final String toPath = this.mToolchainDir;
        Log.d("PackageInstaller", "Unpack file " + tempPath + " to " + toPath);
        final int needMem = Utils.unzippedSize(tempPath);
        if (needMem < 0) {}
        final StatFs stat = new StatFs(toPath);
        double cacheAvailSize = stat.getAvailableBlocks();
        Log.d("PackageInstaller", "Unzipped size " + needMem);
        Log.d("PackageInstaller", "Available (blocks) " + cacheAvailSize + "(" + stat.getBlockSize() + ")");
        cacheAvailSize *= stat.getBlockSize();
        if (cacheAvailSize < needMem) {
            throw new NotEnoughCacheException(needMem, (long)cacheAvailSize);
        }
        final File logFile = new File(this.mInstalledDir, packageInfo.getName() + ".list");
        if (Utils.unzip(tempPath, toPath, logFile.getAbsolutePath()) != 0) {
            if (logFile.exists()) {
                logFile.delete();
            }
            throw new BadArchiveException(zipFile.getName());
        }
        final String[] infoFiles = { "pkgdesc", "prerm", "postinst" };
        File postinst = null;
        for (final String infoFilePath : infoFiles) {
            final File file = new File(this.mToolchainDir, infoFilePath);
            if (file.exists()) {
                try {
                    final File infoFile = new File(this.mInstalledDir, packageInfo.getName() + "." + infoFilePath);
                    Log.i("PackageInstaller", "Copy file to " + infoFile);
                    Utils.copyDirectory(file, infoFile);
                    if (infoFilePath.equals("postinst")) {
                        postinst = infoFile;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.e("PackageInstaller", "Copy " + infoFilePath + " file failed " + e);
                }
                file.delete();
            }
        }
        try {
            this.finishInstallPackage(postinst);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    @WorkerThread
    private void finishInstallPackage(@Nullable final File postinstFile) throws IOException {
        final File examples = new File(this.mCCToolsDir, "Examples");
        if (examples.exists()) {
            try {
                Log.i("PackageInstaller", "Move Examples to SD card");
                Utils.copyDirectory(examples, new File(Environment.getSdCardExampleDir()));
                Utils.deleteDirectory(examples);
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e("PackageInstaller", "Can't copy examples directory " + e);
            }
        }
        Log.i("PackageInstaller", "Execute postinst file " + postinstFile);
        if (postinstFile != null) {
            Utils.chmod(postinstFile.getAbsolutePath(), 493);
            Shell.exec(this.mContext, postinstFile);
            Log.i("PackageInstaller", "Executed postinst file");
        }
    }
}
