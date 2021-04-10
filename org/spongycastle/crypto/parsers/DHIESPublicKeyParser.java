package org.spongycastle.crypto.parsers;

import org.spongycastle.crypto.*;
import org.spongycastle.util.io.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import java.io.*;

public class DHIESPublicKeyParser implements KeyParser
{
    private DHParameters dhParams;
    
    public DHIESPublicKeyParser(final DHParameters dhParams) {
        this.dhParams = dhParams;
    }
    
    @Override
    public AsymmetricKeyParameter readKey(final InputStream inputStream) throws IOException {
        final int n = (this.dhParams.getP().bitLength() + 7) / 8;
        final byte[] array = new byte[n];
        Streams.readFully(inputStream, array, 0, n);
        return new DHPublicKeyParameters(new BigInteger(1, array), this.dhParams);
    }
}
