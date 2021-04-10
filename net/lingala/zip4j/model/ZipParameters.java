package net.lingala.zip4j.model;

import java.util.*;
import net.lingala.zip4j.util.*;

public class ZipParameters implements Cloneable
{
    private int aesKeyStrength;
    private int compressionLevel;
    private int compressionMethod;
    private String defaultFolderPath;
    private boolean encryptFiles;
    private int encryptionMethod;
    private String fileNameInZip;
    private boolean includeRootFolder;
    private boolean isSourceExternalStream;
    private char[] password;
    private boolean readHiddenFiles;
    private String rootFolderInZip;
    private int sourceFileCRC;
    private TimeZone timeZone;
    
    public ZipParameters() {
        this.compressionMethod = 8;
        this.encryptFiles = false;
        this.readHiddenFiles = true;
        this.encryptionMethod = -1;
        this.aesKeyStrength = -1;
        this.includeRootFolder = true;
        this.timeZone = TimeZone.getDefault();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public int getAesKeyStrength() {
        return this.aesKeyStrength;
    }
    
    public int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    public int getCompressionMethod() {
        return this.compressionMethod;
    }
    
    public String getDefaultFolderPath() {
        return this.defaultFolderPath;
    }
    
    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }
    
    public String getFileNameInZip() {
        return this.fileNameInZip;
    }
    
    public char[] getPassword() {
        return this.password;
    }
    
    public String getRootFolderInZip() {
        return this.rootFolderInZip;
    }
    
    public int getSourceFileCRC() {
        return this.sourceFileCRC;
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    public boolean isEncryptFiles() {
        return this.encryptFiles;
    }
    
    public boolean isIncludeRootFolder() {
        return this.includeRootFolder;
    }
    
    public boolean isReadHiddenFiles() {
        return this.readHiddenFiles;
    }
    
    public boolean isSourceExternalStream() {
        return this.isSourceExternalStream;
    }
    
    public void setAesKeyStrength(final int aesKeyStrength) {
        this.aesKeyStrength = aesKeyStrength;
    }
    
    public void setCompressionLevel(final int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
    
    public void setCompressionMethod(final int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }
    
    public void setDefaultFolderPath(final String defaultFolderPath) {
        this.defaultFolderPath = defaultFolderPath;
    }
    
    public void setEncryptFiles(final boolean encryptFiles) {
        this.encryptFiles = encryptFiles;
    }
    
    public void setEncryptionMethod(final int encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }
    
    public void setFileNameInZip(final String fileNameInZip) {
        this.fileNameInZip = fileNameInZip;
    }
    
    public void setIncludeRootFolder(final boolean includeRootFolder) {
        this.includeRootFolder = includeRootFolder;
    }
    
    public void setPassword(final String s) {
        if (s == null) {
            return;
        }
        this.setPassword(s.toCharArray());
    }
    
    public void setPassword(final char[] password) {
        this.password = password;
    }
    
    public void setReadHiddenFiles(final boolean readHiddenFiles) {
        this.readHiddenFiles = readHiddenFiles;
    }
    
    public void setRootFolderInZip(final String s) {
        String replaceAll = s;
        if (Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            String string = s;
            if (!s.endsWith("\\")) {
                string = s;
                if (!s.endsWith("/")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(InternalZipConstants.FILE_SEPARATOR);
                    string = sb.toString();
                }
            }
            replaceAll = string.replaceAll("\\\\", "/");
        }
        this.rootFolderInZip = replaceAll;
    }
    
    public void setSourceExternalStream(final boolean isSourceExternalStream) {
        this.isSourceExternalStream = isSourceExternalStream;
    }
    
    public void setSourceFileCRC(final int sourceFileCRC) {
        this.sourceFileCRC = sourceFileCRC;
    }
    
    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
