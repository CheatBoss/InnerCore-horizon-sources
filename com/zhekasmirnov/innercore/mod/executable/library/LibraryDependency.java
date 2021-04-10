package com.zhekasmirnov.innercore.mod.executable.library;

import com.zhekasmirnov.innercore.mod.build.*;

public class LibraryDependency
{
    public final String libName;
    public final int minVersion;
    private Mod parentMod;
    
    public LibraryDependency(final String libName) {
        final String[] split = libName.split(":");
        if (split.length == 1) {
            this.libName = libName;
            this.minVersion = -1;
            return;
        }
        if (split.length > 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid library dependency ");
            sb.append(libName);
            sb.append(", it should be formatted as <name>:<versionCode>");
            throw new IllegalArgumentException(sb.toString());
        }
        try {
            this.libName = split[0];
            this.minVersion = Integer.valueOf(split[1]);
        }
        catch (NumberFormatException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid library dependency ");
            sb2.append(libName);
            sb2.append(", it should be formatted as <name>:<versionCode>");
            throw new IllegalArgumentException(sb2.toString());
        }
    }
    
    public LibraryDependency(final String libName, final int minVersion) {
        this.libName = libName;
        this.minVersion = minVersion;
    }
    
    public Mod getParentMod() {
        return this.parentMod;
    }
    
    public boolean hasTargetVersion() {
        return this.minVersion != -1;
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public boolean isMatchesLib(final Library library) {
        return this.libName.equals(library.getLibName()) && library.getVersionCode() >= this.minVersion;
    }
    
    public void setParentMod(final Mod parentMod) {
        this.parentMod = parentMod;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.libName);
        String string;
        if (this.hasTargetVersion()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(":");
            sb2.append(this.minVersion);
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        return sb.toString();
    }
}
