package net.lingala.zip4j.model;

public class UnzipParameters
{
    private boolean ignoreAllFileAttributes;
    private boolean ignoreArchiveFileAttribute;
    private boolean ignoreDateTimeAttributes;
    private boolean ignoreHiddenFileAttribute;
    private boolean ignoreReadOnlyFileAttribute;
    private boolean ignoreSystemFileAttribute;
    
    public boolean isIgnoreAllFileAttributes() {
        return this.ignoreAllFileAttributes;
    }
    
    public boolean isIgnoreArchiveFileAttribute() {
        return this.ignoreArchiveFileAttribute;
    }
    
    public boolean isIgnoreDateTimeAttributes() {
        return this.ignoreDateTimeAttributes;
    }
    
    public boolean isIgnoreHiddenFileAttribute() {
        return this.ignoreHiddenFileAttribute;
    }
    
    public boolean isIgnoreReadOnlyFileAttribute() {
        return this.ignoreReadOnlyFileAttribute;
    }
    
    public boolean isIgnoreSystemFileAttribute() {
        return this.ignoreSystemFileAttribute;
    }
    
    public void setIgnoreAllFileAttributes(final boolean ignoreAllFileAttributes) {
        this.ignoreAllFileAttributes = ignoreAllFileAttributes;
    }
    
    public void setIgnoreArchiveFileAttribute(final boolean ignoreArchiveFileAttribute) {
        this.ignoreArchiveFileAttribute = ignoreArchiveFileAttribute;
    }
    
    public void setIgnoreDateTimeAttributes(final boolean ignoreDateTimeAttributes) {
        this.ignoreDateTimeAttributes = ignoreDateTimeAttributes;
    }
    
    public void setIgnoreHiddenFileAttribute(final boolean ignoreHiddenFileAttribute) {
        this.ignoreHiddenFileAttribute = ignoreHiddenFileAttribute;
    }
    
    public void setIgnoreReadOnlyFileAttribute(final boolean ignoreReadOnlyFileAttribute) {
        this.ignoreReadOnlyFileAttribute = ignoreReadOnlyFileAttribute;
    }
    
    public void setIgnoreSystemFileAttribute(final boolean ignoreSystemFileAttribute) {
        this.ignoreSystemFileAttribute = ignoreSystemFileAttribute;
    }
}
