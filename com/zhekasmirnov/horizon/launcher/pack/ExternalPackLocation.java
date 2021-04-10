package com.zhekasmirnov.horizon.launcher.pack;

import java.io.*;
import java.net.*;
import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import android.support.annotation.*;

public class ExternalPackLocation implements IPackLocation
{
    private final String packageUrl;
    private final String manifestUrl;
    private final String graphicsUrl;
    private String changelogUrl;
    private PackManifest externalManifest;
    private int installationPackageSize;
    private String changelog;
    private String uuid;
    
    public ExternalPackLocation(final String packageUrl, final String manifestUrl, final String graphicsUrl) {
        this.changelogUrl = null;
        this.installationPackageSize = -1;
        this.changelog = null;
        this.packageUrl = packageUrl;
        this.manifestUrl = manifestUrl;
        this.graphicsUrl = graphicsUrl;
    }
    
    private static InputStream openUrlStream(final String url) {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url).openStream();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public InputStream getInstallationPackageStream() {
        return openUrlStream(this.packageUrl);
    }
    
    @Override
    public int getInstallationPackageSize() {
        if (this.installationPackageSize == -1) {
            try {
                final URL url = new URL(this.packageUrl);
                final URLConnection connection = url.openConnection();
                connection.connect();
                this.installationPackageSize = connection.getContentLength();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.installationPackageSize;
    }
    
    @Override
    public InputStream getVisualDataStream() {
        return openUrlStream(this.graphicsUrl);
    }
    
    @Override
    public PackManifest getManifest() {
        if (this.externalManifest == null) {
            try {
                final String manifestJson = FileUtils.convertStreamToString(openUrlStream(this.manifestUrl));
                this.externalManifest = new PackManifest(new JSONObject(manifestJson));
            }
            catch (IOException e) {
                throw new RuntimeException("failed to read manifest: failed to download or string conversion failed", e);
            }
            catch (JSONException e2) {
                throw new RuntimeException("failed to read manifest: failed to read json", (Throwable)e2);
            }
            catch (NullPointerException ex) {}
        }
        return this.externalManifest;
    }
    
    @Override
    public int getNewestVersionCode() {
        this.getManifest();
        if (this.externalManifest != null) {
            return this.externalManifest.packVersionCode;
        }
        return -1;
    }
    
    @Override
    public String getChangelog() {
        if (this.changelogUrl == null) {
            return null;
        }
        if (this.changelog == null) {
            try {
                this.changelog = FileUtils.convertStreamToString(openUrlStream(this.changelogUrl));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e2) {
                e2.printStackTrace();
            }
        }
        return this.changelog;
    }
    
    public void setUUID(final String uuid) {
        this.uuid = uuid;
    }
    
    @Nullable
    @Override
    public String getUUID() {
        return this.uuid;
    }
    
    public void setChangelogUrl(final String changelogUrl) {
        this.changelogUrl = changelogUrl;
    }
}
