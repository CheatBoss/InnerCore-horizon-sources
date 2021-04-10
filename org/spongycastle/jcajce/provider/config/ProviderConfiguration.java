package org.spongycastle.jcajce.provider.config;

import java.util.*;
import javax.crypto.spec.*;
import org.spongycastle.jce.spec.*;

public interface ProviderConfiguration
{
    Set getAcceptableNamedCurves();
    
    Map getAdditionalECParameters();
    
    DHParameterSpec getDHDefaultParameters(final int p0);
    
    ECParameterSpec getEcImplicitlyCa();
}
