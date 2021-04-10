package org.spongycastle.jce.provider;

import java.security.*;
import java.io.*;

public class JDKPKCS12StoreParameter implements LoadStoreParameter
{
    private OutputStream outputStream;
    private ProtectionParameter protectionParameter;
    private boolean useDEREncoding;
    
    public OutputStream getOutputStream() {
        return this.outputStream;
    }
    
    @Override
    public ProtectionParameter getProtectionParameter() {
        return this.protectionParameter;
    }
    
    public boolean isUseDEREncoding() {
        return this.useDEREncoding;
    }
    
    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    
    public void setPassword(final char[] array) {
        this.protectionParameter = (ProtectionParameter)new KeyStore.PasswordProtection(array);
    }
    
    public void setProtectionParameter(final ProtectionParameter protectionParameter) {
        this.protectionParameter = protectionParameter;
    }
    
    public void setUseDEREncoding(final boolean useDEREncoding) {
        this.useDEREncoding = useDEREncoding;
    }
}
