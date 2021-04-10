package com.zhekasmirnov.horizon.launcher.pack;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.os.*;
import java.text.*;
import java.util.*;
import org.json.*;

public class PackSavesHolder
{
    public static final String ARCHIVE_PATH = "games/horizon/archived-saves";
    public static final Object LOCK;
    public final File packDirectory;
    public final File packSavesDirectory;
    public final File watchedSavesDirectory;
    
    public PackSavesHolder(final File packDirectory, final File packSavesDirectory, final File watchedSavesDirectory) {
        this.packDirectory = packDirectory;
        this.packSavesDirectory = packSavesDirectory;
        this.watchedSavesDirectory = watchedSavesDirectory;
    }
    
    private static void copySaves(final File src, final File dst, final List<String> ignore) throws IOException {
        if (src.isFile() && !ignore.contains(src.getName())) {
            FileUtils.copy(src, dst);
        }
        else if (src.isDirectory()) {
            dst.mkdirs();
            final List<String> empty = new ArrayList<String>();
            for (final File file : src.listFiles()) {
                copySaves(file, new File(dst, file.getName()), empty);
            }
        }
    }
    
    private boolean pullSavesToDirectory(final File directory) {
        synchronized (PackSavesHolder.LOCK) {
            final List<String> ignore = new ArrayList<String>();
            ignore.add(".horizon");
            try {
                if (directory.isDirectory()) {
                    FileUtils.clearFileTree(directory, false);
                }
                else {
                    if (directory.exists()) {
                        directory.delete();
                    }
                    directory.mkdirs();
                }
                copySaves(this.watchedSavesDirectory, directory, ignore);
                return true;
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    private File getSavesInfoFile() {
        return new File(this.watchedSavesDirectory, ".horizon/saves-info");
    }
    
    public boolean pullSavesToStoredDirectory() {
        final PackSavesInfo savesInfo = PackSavesInfo.fromFile(this.getSavesInfoFile());
        return savesInfo != null && savesInfo.packDirectory.isDirectory() && this.pullSavesToDirectory(savesInfo.packSavesDirectory);
    }
    
    public boolean pullSavesForThisPack() {
        final PackSavesInfo savesInfo = PackSavesInfo.fromFile(this.getSavesInfoFile());
        return savesInfo != null && savesInfo.packDirectory.equals(this.packDirectory) && this.pullSavesToDirectory(this.packSavesDirectory);
    }
    
    private void prepareToPush() {
        if (!this.pullSavesToStoredDirectory()) {
            this.archiveSaves();
        }
        if (this.watchedSavesDirectory.isDirectory()) {
            for (final File file : this.watchedSavesDirectory.listFiles()) {
                if (!file.getName().startsWith(".horizon")) {
                    if (file.isDirectory()) {
                        FileUtils.clearFileTree(file, true);
                    }
                    else {
                        file.delete();
                    }
                }
            }
        }
        else {
            if (this.watchedSavesDirectory.isFile()) {
                this.watchedSavesDirectory.delete();
            }
            this.watchedSavesDirectory.mkdirs();
        }
    }
    
    public void pushSavesIfRequired() {
        final PackSavesInfo info = PackSavesInfo.fromFile(this.getSavesInfoFile());
        if (info != null && info.packDirectory.equals(this.packDirectory)) {
            return;
        }
        synchronized (PackSavesHolder.LOCK) {
            this.prepareToPush();
            try {
                copySaves(this.packSavesDirectory, this.watchedSavesDirectory, new ArrayList<String>());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        final PackSavesInfo newInfo = new PackSavesInfo(this.packDirectory, this.packSavesDirectory, UUID.randomUUID().toString());
        final File infoFile = this.getSavesInfoFile();
        infoFile.getParentFile().mkdirs();
        try {
            FileUtils.writeJSON(infoFile, newInfo.toJson());
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private void showArchiveInfo(final PackSavesInfo info, final File archiveFile, final boolean success) {
        final Activity context = HorizonApplication.getTopRunningActivity();
        if (context != null) {
            if (success) {
                final String path = archiveFile.getAbsolutePath();
                context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)context, 2131689480);
                        builder.setTitle(2131624109);
                        builder.setMessage((CharSequence)context.getResources().getString(2131624108, new Object[] { path, PackSavesHolder.this.watchedSavesDirectory.getAbsolutePath() }));
                        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener)null);
                        builder.show();
                    }
                });
            }
            else {
                context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText((Context)context, (CharSequence)"failed to archive replaced game saves", 1).show();
                    }
                });
            }
        }
    }
    
    private void showArchiveInfo(final PackSavesInfo info, final File archiveFile, final boolean success, final int timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeout);
                }
                catch (InterruptedException ex) {}
                PackSavesHolder.this.showArchiveInfo(info, archiveFile, success);
            }
        }).start();
    }
    
    public void archiveSaves() {
        final File archiveDir = new File(Environment.getExternalStorageDirectory(), "games/horizon/archived-saves");
        if (archiveDir.isFile()) {
            archiveDir.delete();
        }
        archiveDir.mkdirs();
        final String formattedDate = new SimpleDateFormat("_yyyy-MM-dd_HH:mm", Locale.getDefault()).format(new Date());
        final File infoFile = this.getSavesInfoFile();
        final PackSavesInfo info = PackSavesInfo.fromFile(infoFile);
        infoFile.delete();
        final int attempt = 0;
        File archiveZip;
        do {
            archiveZip = new File(archiveDir, ((info != null) ? info.packDirectory.getName() : "unknown") + formattedDate + "." + attempt + ".zip");
        } while (archiveZip.exists());
        boolean success = true;
        try {
            synchronized (PackSavesHolder.LOCK) {
                if (!FileUtils.zipDirectory(this.watchedSavesDirectory, archiveZip)) {
                    return;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        this.showArchiveInfo(info, archiveZip, success, 1000);
    }
    
    static {
        LOCK = new Object();
    }
    
    public static class PackSavesInfo
    {
        public final File packDirectory;
        public final File packSavesDirectory;
        public final String uuid;
        
        private PackSavesInfo(final File packDirectory, final File packSavesDirectory, final String uuid) {
            this.packDirectory = packDirectory;
            this.packSavesDirectory = packSavesDirectory;
            this.uuid = uuid;
        }
        
        public JSONObject toJson() {
            final JSONObject json = new JSONObject();
            try {
                json.put("pack", (Object)this.packDirectory.getAbsolutePath());
                json.put("pack-saves", (Object)this.packSavesDirectory.getAbsolutePath());
                json.put("uuid", (Object)this.uuid);
            }
            catch (JSONException ex) {}
            return json;
        }
        
        public static PackSavesInfo fromJson(final JSONObject json) {
            final String pack = json.optString("pack", (String)null);
            final String packSaves = json.optString("pack-saves", (String)null);
            final String uuid = json.optString("uuid", (String)null);
            if (pack != null && packSaves != null) {
                return new PackSavesInfo(new File(pack), new File(packSaves), uuid);
            }
            return null;
        }
        
        public static PackSavesInfo fromFile(final File file) {
            try {
                return fromJson(FileUtils.readJSON(file));
            }
            catch (IOException ex) {}
            catch (JSONException ex2) {}
            return null;
        }
    }
}
