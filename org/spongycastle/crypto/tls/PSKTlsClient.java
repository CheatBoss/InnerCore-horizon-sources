package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.io.*;

public class PSKTlsClient extends AbstractTlsClient
{
    protected TlsPSKIdentity pskIdentity;
    
    public PSKTlsClient(final TlsCipherFactory tlsCipherFactory, final TlsPSKIdentity pskIdentity) {
        super(tlsCipherFactory);
        this.pskIdentity = pskIdentity;
    }
    
    public PSKTlsClient(final TlsPSKIdentity tlsPSKIdentity) {
        this(new DefaultTlsCipherFactory(), tlsPSKIdentity);
    }
    
    protected TlsKeyExchange createPSKKeyExchange(final int n) {
        return new TlsPSKKeyExchange(n, this.supportedSignatureAlgorithms, this.pskIdentity, null, null, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }
    
    @Override
    public TlsAuthentication getAuthentication() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public int[] getCipherSuites() {
        return new int[] { 49207, 49205, 178, 144 };
    }
    
    @Override
    public TlsKeyExchange getKeyExchange() throws IOException {
        final int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        if (keyExchangeAlgorithm != 24) {
            switch (keyExchangeAlgorithm) {
                default: {
                    throw new TlsFatalAlert((short)80);
                }
                case 13:
                case 14:
                case 15: {
                    break;
                }
            }
        }
        return this.createPSKKeyExchange(keyExchangeAlgorithm);
    }
}
