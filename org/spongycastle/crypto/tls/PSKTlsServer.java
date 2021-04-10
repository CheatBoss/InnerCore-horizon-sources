package org.spongycastle.crypto.tls;

import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.agreement.*;

public class PSKTlsServer extends AbstractTlsServer
{
    protected TlsPSKIdentityManager pskIdentityManager;
    
    public PSKTlsServer(final TlsCipherFactory tlsCipherFactory, final TlsPSKIdentityManager pskIdentityManager) {
        super(tlsCipherFactory);
        this.pskIdentityManager = pskIdentityManager;
    }
    
    public PSKTlsServer(final TlsPSKIdentityManager tlsPSKIdentityManager) {
        this(new DefaultTlsCipherFactory(), tlsPSKIdentityManager);
    }
    
    protected TlsKeyExchange createPSKKeyExchange(final int n) {
        return new TlsPSKKeyExchange(n, this.supportedSignatureAlgorithms, null, this.pskIdentityManager, this.getDHParameters(), this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }
    
    @Override
    protected int[] getCipherSuites() {
        return new int[] { 49207, 49205, 178, 144 };
    }
    
    @Override
    public TlsCredentials getCredentials() throws IOException {
        final int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        if (keyExchangeAlgorithm != 24) {
            switch (keyExchangeAlgorithm) {
                default: {
                    throw new TlsFatalAlert((short)80);
                }
                case 15: {
                    return this.getRSAEncryptionCredentials();
                }
                case 13:
                case 14: {
                    break;
                }
            }
        }
        return null;
    }
    
    protected DHParameters getDHParameters() {
        return DHStandardGroups.rfc7919_ffdhe2048;
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
    
    protected TlsEncryptionCredentials getRSAEncryptionCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
}
