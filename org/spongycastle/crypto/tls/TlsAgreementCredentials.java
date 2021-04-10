package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.io.*;

public interface TlsAgreementCredentials extends TlsCredentials
{
    byte[] generateAgreement(final AsymmetricKeyParameter p0) throws IOException;
}
