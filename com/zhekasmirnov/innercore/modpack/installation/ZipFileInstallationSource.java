package com.zhekasmirnov.innercore.modpack.installation;

import java.util.*;
import java.util.zip.*;
import java.io.*;
import com.zhekasmirnov.horizon.util.*;

public class ZipFileInstallationSource extends ModpackInstallationSource
{
    protected ZipFile file;
    protected String manifestContent;
    
    public ZipFileInstallationSource() {
    }
    
    public ZipFileInstallationSource(final ZipFile file) {
        this.setFile(file);
    }
    
    @Override
    public Enumeration<Entry> entries() {
        return new Enumeration<Entry>() {
            final /* synthetic */ Enumeration val$entries = ZipFileInstallationSource.this.file.entries();
            
            @Override
            public boolean hasMoreElements() {
                return this.val$entries.hasMoreElements();
            }
            
            @Override
            public Entry nextElement() {
                return new Entry() {
                    final /* synthetic */ ZipEntry val$entry = ZipFileInstallationSource$1.this.val$entries.nextElement();
                    
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return ZipFileInstallationSource.this.file.getInputStream(this.val$entry);
                    }
                    
                    @Override
                    public String getName() {
                        return this.val$entry.getName();
                    }
                };
            }
        };
    }
    
    @Override
    public int getEntryCount() {
        return this.file.size();
    }
    
    public ZipFile getFile() {
        return this.file;
    }
    
    @Override
    public String getManifestContent() throws IOException {
        if (this.manifestContent == null) {
            final ZipEntry entry = this.file.getEntry("modpack.json");
            if (entry == null) {
                throw new IOException("modpack zip file does not contain modpack.json");
            }
            this.manifestContent = FileUtils.convertStreamToString(this.file.getInputStream(entry));
        }
        return this.manifestContent;
    }
    
    public void setFile(final ZipFile file) {
        this.file = file;
    }
}
