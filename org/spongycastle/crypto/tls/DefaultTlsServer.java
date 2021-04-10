package org.spongycastle.crypto.tls;

import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.agreement.*;

public abstract class DefaultTlsServer extends AbstractTlsServer
{
    public DefaultTlsServer() {
    }
    
    public DefaultTlsServer(final TlsCipherFactory tlsCipherFactory) {
        super(tlsCipherFactory);
    }
    
    protected TlsKeyExchange createDHEKeyExchange(final int n) {
        return new TlsDHEKeyExchange(n, this.supportedSignatureAlgorithms, this.getDHParameters());
    }
    
    protected TlsKeyExchange createDHKeyExchange(final int n) {
        return new TlsDHKeyExchange(n, this.supportedSignatureAlgorithms, this.getDHParameters());
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
    protected int[] getCipherSuites() {
        return new int[] { 49200, 49199, 49192, 49191, 49172, 49171, 159, 158, 107, 103, 57, 51, 157, 156, 61, 60, 53, 47 };
    }
    
    @Override
    public TlsCredentials getCredentials() throws IOException {
        final int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        if (keyExchangeAlgorithm == 1) {
            return this.getRSAEncryptionCredentials();
        }
        if (keyExchangeAlgorithm != 3) {
            if (keyExchangeAlgorithm != 5) {
                if (keyExchangeAlgorithm != 11) {
                    if (keyExchangeAlgorithm == 17) {
                        return this.getECDSASignerCredentials();
                    }
                    if (keyExchangeAlgorithm == 19) {
                        return this.getRSASignerCredentials();
                    }
                    if (keyExchangeAlgorithm != 20) {
                        throw new TlsFatalAlert((short)80);
                    }
                }
                return null;
            }
            return this.getRSASignerCredentials();
        }
        return this.getDSASignerCredentials();
    }
    
    protected DHParameters getDHParameters() {
        return DHStandardGroups.rfc7919_ffdhe2048;
    }
    
    protected TlsSignerCredentials getDSASignerCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    protected TlsSignerCredentials getECDSASignerCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
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
    
    protected TlsEncryptionCredentials getRSAEncryptionCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    protected TlsSignerCredentials getRSASignerCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
}
