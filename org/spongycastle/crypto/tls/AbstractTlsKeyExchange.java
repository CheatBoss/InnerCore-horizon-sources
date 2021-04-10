package org.spongycastle.crypto.tls;

import java.util.*;
import java.io.*;

public abstract class AbstractTlsKeyExchange implements TlsKeyExchange
{
    protected TlsContext context;
    protected int keyExchange;
    protected Vector supportedSignatureAlgorithms;
    
    protected AbstractTlsKeyExchange(final int keyExchange, final Vector supportedSignatureAlgorithms) {
        this.keyExchange = keyExchange;
        this.supportedSignatureAlgorithms = supportedSignatureAlgorithms;
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        if (!this.requiresServerKeyExchange()) {
            return null;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void init(final TlsContext context) {
        this.context = context;
        final ProtocolVersion clientVersion = context.getClientVersion();
        if (TlsUtils.isSignatureAlgorithmsExtensionAllowed(clientVersion)) {
            if (this.supportedSignatureAlgorithms == null) {
                final int keyExchange = this.keyExchange;
                while (true) {
                    Label_0159: {
                        if (keyExchange == 1) {
                            break Label_0159;
                        }
                        Label_0152: {
                            if (keyExchange != 3) {
                                if (keyExchange == 5) {
                                    break Label_0159;
                                }
                                if (keyExchange != 7) {
                                    if (keyExchange == 9) {
                                        break Label_0159;
                                    }
                                    switch (keyExchange) {
                                        default: {
                                            switch (keyExchange) {
                                                default: {
                                                    throw new IllegalStateException("unsupported key exchange algorithm");
                                                }
                                                case 22: {
                                                    break Label_0152;
                                                }
                                                case 23: {
                                                    break Label_0159;
                                                }
                                                case 21:
                                                case 24: {
                                                    return;
                                                }
                                            }
                                            break;
                                        }
                                        case 16:
                                        case 17: {
                                            final Vector supportedSignatureAlgorithms = TlsUtils.getDefaultECDSASignatureAlgorithms();
                                            break Label_0146;
                                        }
                                        case 15:
                                        case 18:
                                        case 19: {
                                            break Label_0159;
                                        }
                                        case 13:
                                        case 14: {
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        final Vector supportedSignatureAlgorithms = TlsUtils.getDefaultDSSSignatureAlgorithms();
                        this.supportedSignatureAlgorithms = supportedSignatureAlgorithms;
                        return;
                    }
                    final Vector supportedSignatureAlgorithms = TlsUtils.getDefaultRSASignatureAlgorithms();
                    continue;
                }
            }
        }
        else if (this.supportedSignatureAlgorithms != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("supported_signature_algorithms not allowed for ");
            sb.append(clientVersion);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    protected DigitallySigned parseSignature(final InputStream inputStream) throws IOException {
        final DigitallySigned parse = DigitallySigned.parse(this.context, inputStream);
        final SignatureAndHashAlgorithm algorithm = parse.getAlgorithm();
        if (algorithm != null) {
            TlsUtils.verifySupportedSignatureAlgorithm(this.supportedSignatureAlgorithms, algorithm);
        }
        return parse;
    }
    
    @Override
    public void processClientCertificate(final Certificate certificate) throws IOException {
    }
    
    @Override
    public void processClientKeyExchange(final InputStream inputStream) throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
    }
    
    @Override
    public void processServerCredentials(final TlsCredentials tlsCredentials) throws IOException {
        this.processServerCertificate(tlsCredentials.getCertificate());
    }
    
    @Override
    public void processServerKeyExchange(final InputStream inputStream) throws IOException {
        if (this.requiresServerKeyExchange()) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public boolean requiresServerKeyExchange() {
        return false;
    }
    
    @Override
    public void skipClientCredentials() throws IOException {
    }
    
    @Override
    public void skipServerKeyExchange() throws IOException {
        if (!this.requiresServerKeyExchange()) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
}
