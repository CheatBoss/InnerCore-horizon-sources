package org.spongycastle.jcajce.util;

import java.security.*;
import org.spongycastle.jce.provider.*;

public class BCJcaJceHelper extends ProviderJcaJceHelper
{
    private static volatile Provider bcProvider;
    
    public BCJcaJceHelper() {
        super(getBouncyCastleProvider());
    }
    
    private static Provider getBouncyCastleProvider() {
        if (Security.getProvider("SC") != null) {
            return Security.getProvider("SC");
        }
        if (BCJcaJceHelper.bcProvider != null) {
            return BCJcaJceHelper.bcProvider;
        }
        return BCJcaJceHelper.bcProvider = new BouncyCastleProvider();
    }
}
