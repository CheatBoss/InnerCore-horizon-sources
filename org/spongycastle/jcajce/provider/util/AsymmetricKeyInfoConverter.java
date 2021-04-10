package org.spongycastle.jcajce.provider.util;

import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;

public interface AsymmetricKeyInfoConverter
{
    PrivateKey generatePrivate(final PrivateKeyInfo p0) throws IOException;
    
    PublicKey generatePublic(final SubjectPublicKeyInfo p0) throws IOException;
}
