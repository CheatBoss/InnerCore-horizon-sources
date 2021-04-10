package org.spongycastle.crypto.tls;

import java.io.*;

public class DigitallySigned
{
    protected SignatureAndHashAlgorithm algorithm;
    protected byte[] signature;
    
    public DigitallySigned(final SignatureAndHashAlgorithm algorithm, final byte[] signature) {
        if (signature != null) {
            this.algorithm = algorithm;
            this.signature = signature;
            return;
        }
        throw new IllegalArgumentException("'signature' cannot be null");
    }
    
    public static DigitallySigned parse(final TlsContext tlsContext, final InputStream inputStream) throws IOException {
        SignatureAndHashAlgorithm parse;
        if (TlsUtils.isTLSv12(tlsContext)) {
            parse = SignatureAndHashAlgorithm.parse(inputStream);
        }
        else {
            parse = null;
        }
        return new DigitallySigned(parse, TlsUtils.readOpaque16(inputStream));
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final SignatureAndHashAlgorithm algorithm = this.algorithm;
        if (algorithm != null) {
            algorithm.encode(outputStream);
        }
        TlsUtils.writeOpaque16(this.signature, outputStream);
    }
    
    public SignatureAndHashAlgorithm getAlgorithm() {
        return this.algorithm;
    }
    
    public byte[] getSignature() {
        return this.signature;
    }
}
