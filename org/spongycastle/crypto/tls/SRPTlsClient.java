package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import java.io.*;
import java.util.*;

public class SRPTlsClient extends AbstractTlsClient
{
    protected TlsSRPGroupVerifier groupVerifier;
    protected byte[] identity;
    protected byte[] password;
    
    public SRPTlsClient(final TlsCipherFactory tlsCipherFactory, final TlsSRPGroupVerifier groupVerifier, final byte[] array, final byte[] array2) {
        super(tlsCipherFactory);
        this.groupVerifier = groupVerifier;
        this.identity = Arrays.clone(array);
        this.password = Arrays.clone(array2);
    }
    
    public SRPTlsClient(final TlsCipherFactory tlsCipherFactory, final byte[] array, final byte[] array2) {
        this(tlsCipherFactory, new DefaultTlsSRPGroupVerifier(), array, array2);
    }
    
    public SRPTlsClient(final byte[] array, final byte[] array2) {
        this(new DefaultTlsCipherFactory(), new DefaultTlsSRPGroupVerifier(), array, array2);
    }
    
    protected TlsKeyExchange createSRPKeyExchange(final int n) {
        return new TlsSRPKeyExchange(n, this.supportedSignatureAlgorithms, this.groupVerifier, this.identity, this.password);
    }
    
    @Override
    public TlsAuthentication getAuthentication() throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public int[] getCipherSuites() {
        return new int[] { 49182 };
    }
    
    @Override
    public Hashtable getClientExtensions() throws IOException {
        final Hashtable ensureExtensionsInitialised = TlsExtensionsUtils.ensureExtensionsInitialised(super.getClientExtensions());
        TlsSRPUtils.addSRPExtension(ensureExtensionsInitialised, this.identity);
        return ensureExtensionsInitialised;
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
    
    @Override
    public void processServerExtensions(final Hashtable hashtable) throws IOException {
        if (!TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsSRPUtils.EXT_SRP, (short)47) && this.requireSRPServerExtension()) {
            throw new TlsFatalAlert((short)47);
        }
        super.processServerExtensions(hashtable);
    }
    
    protected boolean requireSRPServerExtension() {
        return false;
    }
}
