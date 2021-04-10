package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.io.*;

public abstract class DefaultTlsClient extends AbstractTlsClient
{
    public DefaultTlsClient() {
    }
    
    public DefaultTlsClient(final TlsCipherFactory tlsCipherFactory) {
        super(tlsCipherFactory);
    }
    
    protected TlsKeyExchange createDHEKeyExchange(final int n) {
        return new TlsDHEKeyExchange(n, this.supportedSignatureAlgorithms, null);
    }
    
    protected TlsKeyExchange createDHKeyExchange(final int n) {
        return new TlsDHKeyExchange(n, this.supportedSignatureAlgorithms, null);
    }
    
    protected TlsKeyExchange createECDHEKeyExchange(final int n) {
        return new TlsECDHEKeyExchange(n, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }
    
    protected TlsKeyExchange createECDHKeyExchange(final int n) {
        return new TlsECDHKeyExchange(n, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }
    
    protected TlsKeyExchange createRSAKeyExchange() {
        return new TlsRSAKeyExchange(this.supportedSignatureAlgorithms);
    }
    
    @Override
    public int[] getCipherSuites() {
        return new int[] { 49195, 49187, 49161, 49199, 49191, 49171, 162, 64, 50, 158, 103, 51, 156, 60, 47 };
    }
    
    @Override
    public TlsKeyExchange getKeyExchange() throws IOException {
        final int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        if (keyExchangeAlgorithm == 1) {
            return this.createRSAKeyExchange();
        }
        if (keyExchangeAlgorithm == 3 || keyExchangeAlgorithm == 5) {
            return this.createDHEKeyExchange(keyExchangeAlgorithm);
        }
        if (keyExchangeAlgorithm == 7 || keyExchangeAlgorithm == 9 || keyExchangeAlgorithm == 11) {
            return this.createDHKeyExchange(keyExchangeAlgorithm);
        }
        switch (keyExchangeAlgorithm) {
            default: {
                throw new TlsFatalAlert((short)80);
            }
            case 17:
            case 19: {
                return this.createECDHEKeyExchange(keyExchangeAlgorithm);
            }
            case 16:
            case 18:
            case 20: {
                return this.createECDHKeyExchange(keyExchangeAlgorithm);
            }
        }
    }
}
