package net.lingala.zip4j.model;

import net.lingala.zip4j.crypto.*;
import java.io.*;
import net.lingala.zip4j.unzip.*;

public class UnzipEngineParameters
{
    private FileHeader fileHeader;
    private IDecrypter iDecryptor;
    private LocalFileHeader localFileHeader;
    private FileOutputStream outputStream;
    private UnzipEngine unzipEngine;
    private ZipModel zipModel;
    
    public FileHeader getFileHeader() {
        return this.fileHeader;
    }
    
    public IDecrypter getIDecryptor() {
        return this.iDecryptor;
    }
    
    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }
    
    public FileOutputStream getOutputStream() {
        return this.outputStream;
    }
    
    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }
    
    public ZipModel getZipModel() {
        return this.zipModel;
    }
    
    public void setFileHeader(final FileHeader fileHeader) {
        this.fileHeader = fileHeader;
    }
    
    public void setIDecryptor(final IDecrypter iDecryptor) {
        this.iDecryptor = iDecryptor;
    }
    
    public void setLocalFileHeader(final LocalFileHeader localFileHeader) {
        this.localFileHeader = localFileHeader;
    }
    
    public void setOutputStream(final FileOutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public void setUnzipEngine(final UnzipEngine unzipEngine) {
        this.unzipEngine = unzipEngine;
    }
    
    public void setZipModel(final ZipModel zipModel) {
        this.zipModel = zipModel;
    }
}
