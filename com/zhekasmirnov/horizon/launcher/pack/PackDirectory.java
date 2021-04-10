package com.zhekasmirnov.horizon.launcher.pack;

import java.util.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class PackDirectory
{
    public final File directory;
    private IPackLocation location;
    private PackManifest localManifest;
    
    public PackDirectory(final File directory) {
        this.location = null;
        this.localManifest = null;
        this.directory = directory;
    }
    
    public void fetchFromRepo(final PackRepository repo) {
        final String uuid = this.getUUID();
        if (uuid != null) {
            final IPackLocation location = repo.getLocationForUUID(uuid);
            if (location != null) {
                this.setLocation(location);
            }
        }
    }
    
    public IPackLocation getLocation() {
        return this.location;
    }
    
    public void setLocation(final IPackLocation location) {
        this.location = location;
    }
    
    public String getExternalUUID() {
        return (this.location != null) ? this.location.getUUID() : null;
    }
    
    public String getLocalUUID() {
        return this.getInstallationInfo().getValue("uuid");
    }
    
    public String getUUID() {
        final String uuid = this.getExternalUUID();
        return (uuid != null) ? uuid : this.getLocalUUID();
    }
    
    public String getInternalID() {
        final PackInstallationInfo installationInfo = this.getInstallationInfo();
        String id = installationInfo.getValue("internalId");
        if (id == null) {
            id = UUID.randomUUID().toString();
            installationInfo.setValue("internalId", id);
        }
        return id;
    }
    
    public void generateNewInternalID() {
        this.getInstallationInfo().setValue("internalId", UUID.randomUUID().toString());
    }
    
    public void updateLocalUUID() {
        final String uuid = this.getExternalUUID();
        if (uuid != null) {
            this.getInstallationInfo().setValue("uuid", uuid);
        }
    }
    
    public PackInstallationInfo getInstallationInfo() {
        return new PackInstallationInfo(new File(this.directory, ".installation_info"));
    }
    
    public int getInstallationPackageSize() {
        return (this.location != null) ? this.location.getInstallationPackageSize() : -1;
    }
    
    public InputStream getInstallationPackageStream() {
        return (this.location != null) ? this.location.getInstallationPackageStream() : null;
    }
    
    public InputStream getVisualDataStream() {
        return (this.location != null) ? this.location.getVisualDataStream() : null;
    }
    
    public PackManifest getExternalManifest() {
        return (this.location != null) ? this.location.getManifest() : null;
    }
    
    public PackManifest getLocalManifest() {
        if (this.localManifest == null) {
            final File localManifestFile = new File(this.directory, "manifest.json");
            if (localManifestFile.exists()) {
                try {
                    final String manifestJson = FileUtils.readFileText(localManifestFile);
                    this.localManifest = new PackManifest(new JSONObject(manifestJson));
                }
                catch (IOException e) {
                    throw new RuntimeException("failed to read manifest: failed to download or string conversion failed", e);
                }
                catch (JSONException e2) {
                    throw new RuntimeException("failed to read manifest: failed to read json", (Throwable)e2);
                }
                catch (NullPointerException ex) {}
            }
        }
        return this.localManifest;
    }
    
    public void reloadLocalManifest() {
        this.localManifest = null;
        this.getLocalManifest();
    }
    
    public PackManifest getManifest() {
        final PackManifest local = this.getLocalManifest();
        return (local != null) ? local : this.getExternalManifest();
    }
    
    public int getNewestVersionCode() {
        return (this.location != null) ? this.location.getNewestVersionCode() : -1;
    }
    
    public boolean isUpdateAvailable() {
        if (this.location != null) {
            final PackManifest localManifest = this.getLocalManifest();
            if (localManifest != null) {
                final int newestVersion = this.getNewestVersionCode();
                return newestVersion != -1 && newestVersion > localManifest.packVersionCode;
            }
        }
        return false;
    }
    
    public void nominateForInstallation() {
        this.directory.mkdirs();
        this.updateLocalUUID();
        this.updateTimestamp();
        final PackManifest manifest = this.getExternalManifest();
        if (manifest != null) {
            try {
                FileUtils.writeJSON(new File(this.directory, "manifest.json"), manifest.getContent());
            }
            catch (IOException ex) {}
        }
    }
    
    public void updateTimestamp() {
        this.getInstallationInfo().setValue("timestamp", "" + System.currentTimeMillis());
    }
    
    public long getTimestamp() {
        try {
            return Long.valueOf(this.getInstallationInfo().getValue("timestamp"));
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    public void setCustomName(final String name) {
        this.getInstallationInfo().setValue("customName", name);
    }
    
    public String getCustomName() {
        return this.getInstallationInfo().getValue("customName");
    }
}
