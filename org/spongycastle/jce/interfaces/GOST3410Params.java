package org.spongycastle.jce.interfaces;

import org.spongycastle.jce.spec.*;

public interface GOST3410Params
{
    String getDigestParamSetOID();
    
    String getEncryptionParamSetOID();
    
    String getPublicKeyParamSetOID();
    
    GOST3410PublicKeyParameterSetSpec getPublicKeyParameters();
}
