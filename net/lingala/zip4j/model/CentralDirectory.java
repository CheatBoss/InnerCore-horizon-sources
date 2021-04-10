package net.lingala.zip4j.model;

import java.util.*;

public class CentralDirectory
{
    private DigitalSignature digitalSignature;
    private ArrayList fileHeaders;
    
    public DigitalSignature getDigitalSignature() {
        return this.digitalSignature;
    }
    
    public ArrayList getFileHeaders() {
        return this.fileHeaders;
    }
    
    public void setDigitalSignature(final DigitalSignature digitalSignature) {
        this.digitalSignature = digitalSignature;
    }
    
    public void setFileHeaders(final ArrayList fileHeaders) {
        this.fileHeaders = fileHeaders;
    }
}
