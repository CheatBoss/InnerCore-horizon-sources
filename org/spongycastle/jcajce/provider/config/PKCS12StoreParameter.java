package org.spongycastle.jcajce.provider.config;

import java.io.*;
import java.security.*;

public class PKCS12StoreParameter extends org.spongycastle.jcajce.PKCS12StoreParameter
{
    public PKCS12StoreParameter(final OutputStream outputStream, final ProtectionParameter protectionParameter) {
        super(outputStream, protectionParameter, false);
    }
    
    public PKCS12StoreParameter(final OutputStream outputStream, final ProtectionParameter protectionParameter, final boolean b) {
        super(outputStream, protectionParameter, b);
    }
    
    public PKCS12StoreParameter(final OutputStream outputStream, final char[] array) {
        super(outputStream, array, false);
    }
    
    public PKCS12StoreParameter(final OutputStream outputStream, final char[] array, final boolean b) {
        super(outputStream, (ProtectionParameter)new KeyStore.PasswordProtection(array), b);
    }
}
