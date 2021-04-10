package org.spongycastle.jcajce;

import java.security.*;
import java.io.*;

public class PKCS12StoreParameter implements LoadStoreParameter
{
    private final boolean forDEREncoding;
    private final OutputStream out;
    private final ProtectionParameter protectionParameter;
    
    public PKCS12StoreParameter(final OutputStream outputStream, final ProtectionParameter protectionParameter) {
        this(outputStream, protectionParameter, false);
    }
    
    public PKCS12StoreParameter(final OutputStream out, final ProtectionParameter protectionParameter, final boolean forDEREncoding) {
        this.out = out;
        this.protectionParameter = protectionParameter;
        this.forDEREncoding = forDEREncoding;
    }
    
    public PKCS12StoreParameter(final OutputStream outputStream, final char[] array) {
        this(outputStream, array, false);
    }
    
    public PKCS12StoreParameter(final OutputStream outputStream, final char[] array, final boolean b) {
        this(outputStream, (ProtectionParameter)new KeyStore.PasswordProtection(array), b);
    }
    
    public OutputStream getOutputStream() {
        return this.out;
    }
    
    @Override
    public ProtectionParameter getProtectionParameter() {
        return this.protectionParameter;
    }
    
    public boolean isForDEREncoding() {
        return this.forDEREncoding;
    }
}
