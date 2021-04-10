package org.spongycastle.crypto.tls;

import java.io.*;
import java.util.*;

public class SRPTlsServer extends AbstractTlsServer
{
    protected TlsSRPLoginParameters loginParameters;
    protected byte[] srpIdentity;
    protected TlsSRPIdentityManager srpIdentityManager;
    
    public SRPTlsServer(final TlsCipherFactory tlsCipherFactory, final TlsSRPIdentityManager srpIdentityManager) {
        super(tlsCipherFactory);
        this.srpIdentity = null;
        this.loginParameters = null;
        this.srpIdentityManager = srpIdentityManager;
    }
    
    public SRPTlsServer(final TlsSRPIdentityManager tlsSRPIdentityManager) {
        this(new DefaultTlsCipherFactory(), tlsSRPIdentityManager);
    }
    
    protected TlsKeyExchange createSRPKeyExchange(final int n) {
        return new TlsSRPKeyExchange(n, this.supportedSignatureAlgorithms, this.srpIdentity, this.loginParameters);
    }
    
    @Override
    protected int[] getCipherSuites() {
        return new int[] { 49186, 49183, 49185, 49182, 49184, 49181 };
    }
    
    @Override
    public TlsCredentials getCredentials() throws IOException {
        switch (TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite)) {
            default: {
                throw new TlsFatalAlert((short)80);
            }
            case 23: {
                return this.getRSASignerCredentials();
            }
            case 22: {
                return this.getDSASignerCredentials();
            }
            case 21: {
                return null;
            }
        }
    }
    
    protected TlsSignerCredentials getDSASignerCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public TlsKeyExchange getKeyExchange() throws IOException {
        final int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        switch (keyExchangeAlgorithm) {
            default: {
                throw new TlsFatalAlert((short)80);
            }
            case 21:
            case 22:
            case 23: {
                return this.createSRPKeyExchange(keyExchangeAlgorithm);
            }
        }
    }
    
    protected TlsSignerCredentials getRSASignerCredentials() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public int getSelectedCipherSuite() throws IOException {
        final int selectedCipherSuite = super.getSelectedCipherSuite();
        if (!TlsSRPUtils.isSRPCipherSuite(selectedCipherSuite)) {
            return selectedCipherSuite;
        }
        final byte[] srpIdentity = this.srpIdentity;
        if (srpIdentity != null) {
            this.loginParameters = this.srpIdentityManager.getLoginParameters(srpIdentity);
        }
        if (this.loginParameters != null) {
            return selectedCipherSuite;
        }
        throw new TlsFatalAlert((short)115);
    }
    
    @Override
    public void processClientExtensions(final Hashtable hashtable) throws IOException {
        super.processClientExtensions(hashtable);
        this.srpIdentity = TlsSRPUtils.getSRPExtension(hashtable);
    }
}
