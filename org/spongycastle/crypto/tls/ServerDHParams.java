package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.io.*;
import java.math.*;

public class ServerDHParams
{
    protected DHPublicKeyParameters publicKey;
    
    public ServerDHParams(final DHPublicKeyParameters publicKey) {
        if (publicKey != null) {
            this.publicKey = publicKey;
            return;
        }
        throw new IllegalArgumentException("'publicKey' cannot be null");
    }
    
    public static ServerDHParams parse(final InputStream inputStream) throws IOException {
        return new ServerDHParams(TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), new DHParameters(TlsDHUtils.readDHParameter(inputStream), TlsDHUtils.readDHParameter(inputStream)))));
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final DHParameters parameters = this.publicKey.getParameters();
        final BigInteger y = this.publicKey.getY();
        TlsDHUtils.writeDHParameter(parameters.getP(), outputStream);
        TlsDHUtils.writeDHParameter(parameters.getG(), outputStream);
        TlsDHUtils.writeDHParameter(y, outputStream);
    }
    
    public DHPublicKeyParameters getPublicKey() {
        return this.publicKey;
    }
}
