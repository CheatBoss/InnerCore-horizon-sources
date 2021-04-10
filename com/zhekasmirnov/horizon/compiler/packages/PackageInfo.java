package com.zhekasmirnov.horizon.compiler.packages;

public class PackageInfo
{
    private String name;
    private String fileName;
    private int size;
    private int filesize;
    private String version;
    private String description;
    private String depends;
    private String arch;
    private String replaces;
    
    public PackageInfo(final String name, final String fileName, final int size, final int filesize, final String version, final String description, final String depends, final String arch, final String replaces) {
        this.name = name;
        this.fileName = fileName;
        this.size = size;
        this.filesize = filesize;
        this.version = version;
        this.description = description;
        this.depends = depends;
        this.arch = arch;
        this.replaces = replaces;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return "PackageInfo{name='" + this.name + '\'' + ", file='" + this.fileName + '\'' + ", size=" + this.size + ", filesize=" + this.filesize + ", version='" + this.version + '\'' + ", description='" + this.description + '\'' + ", depends='" + this.depends + '\'' + ", arch='" + this.arch + '\'' + ", replaces='" + this.replaces + '\'' + '}';
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getFileSize() {
        return this.filesize;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getDepends() {
        return this.depends;
    }
    
    public String getArch() {
        return this.arch;
    }
    
    public String getReplaces() {
        return this.replaces;
    }
}
