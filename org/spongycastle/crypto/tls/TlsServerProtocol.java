package org.spongycastle.crypto.tls;

import java.security.*;
import java.util.*;
import java.io.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class TlsServerProtocol extends TlsProtocol
{
    protected CertificateRequest certificateRequest;
    protected short clientCertificateType;
    protected TlsKeyExchange keyExchange;
    protected TlsHandshakeHash prepareFinishHash;
    protected TlsCredentials serverCredentials;
    protected TlsServer tlsServer;
    TlsServerContextImpl tlsServerContext;
    
    public TlsServerProtocol(final InputStream inputStream, final OutputStream outputStream, final SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsServer = null;
        this.tlsServerContext = null;
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.clientCertificateType = -1;
        this.prepareFinishHash = null;
    }
    
    public TlsServerProtocol(final SecureRandom secureRandom) {
        super(secureRandom);
        this.tlsServer = null;
        this.tlsServerContext = null;
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.clientCertificateType = -1;
        this.prepareFinishHash = null;
    }
    
    public void accept(final TlsServer tlsServer) throws IOException {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'tlsServer' cannot be null");
        }
        if (this.tlsServer == null) {
            this.tlsServer = tlsServer;
            this.securityParameters = new SecurityParameters();
            this.securityParameters.entity = 0;
            this.tlsServerContext = new TlsServerContextImpl(this.secureRandom, this.securityParameters);
            this.securityParameters.serverRandom = TlsProtocol.createRandomBlock(tlsServer.shouldUseGMTUnixTime(), this.tlsServerContext.getNonceRandomGenerator());
            this.tlsServer.init(this.tlsServerContext);
            this.recordStream.init(this.tlsServerContext);
            this.recordStream.setRestrictReadVersion(false);
            this.blockForHandshake();
            return;
        }
        throw new IllegalStateException("'accept' can only be called once");
    }
    
    @Override
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.prepareFinishHash = null;
    }
    
    protected boolean expectCertificateVerifyMessage() {
        final short clientCertificateType = this.clientCertificateType;
        return clientCertificateType >= 0 && TlsUtils.hasSigningCapability(clientCertificateType);
    }
    
    @Override
    protected TlsContext getContext() {
        return this.tlsServerContext;
    }
    
    @Override
    AbstractTlsContext getContextAdmin() {
        return this.tlsServerContext;
    }
    
    @Override
    protected TlsPeer getPeer() {
        return this.tlsServer;
    }
    
    @Override
    protected void handleAlertWarningMessage(final short n) throws IOException {
        super.handleAlertWarningMessage(n);
        if (n != 41) {
            return;
        }
        if (TlsUtils.isSSL(this.getContext()) && this.certificateRequest != null) {
            final short connection_state = this.connection_state;
            if (connection_state != 8) {
                if (connection_state != 9) {
                    throw new TlsFatalAlert((short)10);
                }
            }
            else {
                this.tlsServer.processClientSupplementalData(null);
            }
            this.notifyClientCertificate(Certificate.EMPTY_CHAIN);
            this.connection_state = 10;
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    protected void handleHandshakeMessage(final short n, final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final Certificate certificate = null;
        if (n != 1) {
            if (n != 11) {
                if (n == 20) {
                    final short connection_state = this.connection_state;
                    if (connection_state != 11) {
                        if (connection_state != 12) {
                            throw new TlsFatalAlert((short)10);
                        }
                    }
                    else if (this.expectCertificateVerifyMessage()) {
                        throw new TlsFatalAlert((short)10);
                    }
                    this.processFinishedMessage(byteArrayInputStream);
                    this.connection_state = 13;
                    if (this.expectSessionTicket) {
                        this.sendNewSessionTicketMessage(this.tlsServer.getNewSessionTicket());
                        this.sendChangeCipherSpecMessage();
                    }
                    this.connection_state = 14;
                    this.sendFinishedMessage();
                    this.connection_state = 15;
                    this.completeHandshake();
                    return;
                }
                if (n != 23) {
                    if (n != 15) {
                        if (n != 16) {
                            throw new TlsFatalAlert((short)10);
                        }
                        Label_0154: {
                            switch (this.connection_state) {
                                default: {
                                    throw new TlsFatalAlert((short)10);
                                }
                                case 8: {
                                    this.tlsServer.processClientSupplementalData(null);
                                }
                                case 9: {
                                    if (this.certificateRequest == null) {
                                        this.keyExchange.skipClientCredentials();
                                        break Label_0154;
                                    }
                                    if (TlsUtils.isTLSv12(this.getContext())) {
                                        throw new TlsFatalAlert((short)10);
                                    }
                                    if (!TlsUtils.isSSL(this.getContext())) {
                                        this.notifyClientCertificate(Certificate.EMPTY_CHAIN);
                                        break Label_0154;
                                    }
                                    if (this.peerCertificate != null) {
                                        break Label_0154;
                                    }
                                    throw new TlsFatalAlert((short)10);
                                }
                                case 10: {
                                    this.receiveClientKeyExchangeMessage(byteArrayInputStream);
                                    this.connection_state = 11;
                                }
                            }
                        }
                    }
                    else {
                        if (this.connection_state != 11) {
                            throw new TlsFatalAlert((short)10);
                        }
                        if (this.expectCertificateVerifyMessage()) {
                            this.receiveCertificateVerifyMessage(byteArrayInputStream);
                            this.connection_state = 12;
                            return;
                        }
                        throw new TlsFatalAlert((short)10);
                    }
                }
                else {
                    if (this.connection_state == 8) {
                        this.tlsServer.processClientSupplementalData(TlsProtocol.readSupplementalDataMessage(byteArrayInputStream));
                        this.connection_state = 9;
                        return;
                    }
                    throw new TlsFatalAlert((short)10);
                }
            }
            else {
                final short connection_state2 = this.connection_state;
                if (connection_state2 != 8) {
                    if (connection_state2 != 9) {
                        throw new TlsFatalAlert((short)10);
                    }
                }
                else {
                    this.tlsServer.processClientSupplementalData(null);
                }
                if (this.certificateRequest != null) {
                    this.receiveCertificateMessage(byteArrayInputStream);
                    this.connection_state = 10;
                    return;
                }
                throw new TlsFatalAlert((short)10);
            }
        }
        else {
            final short connection_state3 = this.connection_state;
            if (connection_state3 == 0) {
                this.receiveClientHelloMessage(byteArrayInputStream);
                this.connection_state = 1;
                this.sendServerHelloMessage();
                this.connection_state = 2;
                this.recordStream.notifyHelloComplete();
                final Vector serverSupplementalData = this.tlsServer.getServerSupplementalData();
                if (serverSupplementalData != null) {
                    this.sendSupplementalDataMessage(serverSupplementalData);
                }
                this.connection_state = 3;
                (this.keyExchange = this.tlsServer.getKeyExchange()).init(this.getContext());
                final TlsCredentials credentials = this.tlsServer.getCredentials();
                Certificate certificate2;
                if ((this.serverCredentials = credentials) == null) {
                    this.keyExchange.skipServerCredentials();
                    certificate2 = certificate;
                }
                else {
                    this.keyExchange.processServerCredentials(credentials);
                    certificate2 = this.serverCredentials.getCertificate();
                    this.sendCertificateMessage(certificate2);
                }
                this.connection_state = 4;
                int n2 = false ? 1 : 0;
                if (certificate2 == null || certificate2.isEmpty()) {
                    this.allowCertificateStatus = false;
                }
                if (this.allowCertificateStatus) {
                    final CertificateStatus certificateStatus = this.tlsServer.getCertificateStatus();
                    if (certificateStatus != null) {
                        this.sendCertificateStatusMessage(certificateStatus);
                    }
                }
                this.connection_state = 5;
                final byte[] generateServerKeyExchange = this.keyExchange.generateServerKeyExchange();
                if (generateServerKeyExchange != null) {
                    this.sendServerKeyExchangeMessage(generateServerKeyExchange);
                }
                this.connection_state = 6;
                if (this.serverCredentials != null && (this.certificateRequest = this.tlsServer.getCertificateRequest()) != null) {
                    final boolean tlSv12 = TlsUtils.isTLSv12(this.getContext());
                    if (this.certificateRequest.getSupportedSignatureAlgorithms() != null) {
                        n2 = (true ? 1 : 0);
                    }
                    if ((tlSv12 ? 1 : 0) != n2) {
                        throw new TlsFatalAlert((short)80);
                    }
                    this.keyExchange.validateCertificateRequest(this.certificateRequest);
                    this.sendCertificateRequestMessage(this.certificateRequest);
                    TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                }
                this.connection_state = 7;
                this.sendServerHelloDoneMessage();
                this.connection_state = 8;
                this.recordStream.getHandshakeHash().sealHashAlgorithms();
                return;
            }
            if (connection_state3 == 16) {
                this.refuseRenegotiation();
                return;
            }
            throw new TlsFatalAlert((short)10);
        }
    }
    
    protected void notifyClientCertificate(final Certificate peerCertificate) throws IOException {
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        }
        if (this.peerCertificate == null) {
            this.peerCertificate = peerCertificate;
            if (peerCertificate.isEmpty()) {
                this.keyExchange.skipClientCredentials();
            }
            else {
                this.clientCertificateType = TlsUtils.getClientCertificateType(peerCertificate, this.serverCredentials.getCertificate());
                this.keyExchange.processClientCertificate(peerCertificate);
            }
            this.tlsServer.notifyClientCertificate(peerCertificate);
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    protected void receiveCertificateMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final Certificate parse = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        this.notifyClientCertificate(parse);
    }
    
    protected void receiveCertificateVerifyMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (this.certificateRequest != null) {
            final DigitallySigned parse = DigitallySigned.parse(this.getContext(), byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            try {
                final SignatureAndHashAlgorithm algorithm = parse.getAlgorithm();
                byte[] array;
                if (TlsUtils.isTLSv12(this.getContext())) {
                    TlsUtils.verifySupportedSignatureAlgorithm(this.certificateRequest.getSupportedSignatureAlgorithms(), algorithm);
                    array = this.prepareFinishHash.getFinalHash(algorithm.getHash());
                }
                else {
                    array = this.securityParameters.getSessionHash();
                }
                final AsymmetricKeyParameter key = PublicKeyFactory.createKey(this.peerCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
                final TlsSigner tlsSigner = TlsUtils.createTlsSigner(this.clientCertificateType);
                tlsSigner.init(this.getContext());
                if (tlsSigner.verifyRawSignature(algorithm, parse.getSignature(), key, array)) {
                    return;
                }
                throw new TlsFatalAlert((short)51);
            }
            catch (Exception ex) {
                throw new TlsFatalAlert((short)51, ex);
            }
            catch (TlsFatalAlert tlsFatalAlert) {
                throw tlsFatalAlert;
            }
        }
        throw new IllegalStateException();
    }
    
    protected void receiveClientHelloMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        this.recordStream.setWriteVersion(version);
        if (version.isDTLS()) {
            throw new TlsFatalAlert((short)47);
        }
        final byte[] fully = TlsUtils.readFully(32, byteArrayInputStream);
        if (TlsUtils.readOpaque8(byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert((short)47);
        }
        final int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (uint16 < 2 || (uint16 & 0x1) != 0x0) {
            throw new TlsFatalAlert((short)50);
        }
        this.offeredCipherSuites = TlsUtils.readUint16Array(uint16 / 2, byteArrayInputStream);
        final short uint17 = TlsUtils.readUint8(byteArrayInputStream);
        if (uint17 >= 1) {
            this.offeredCompressionMethods = TlsUtils.readUint8Array(uint17, byteArrayInputStream);
            this.clientExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
            this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
            this.getContextAdmin().setClientVersion(version);
            this.tlsServer.notifyClientVersion(version);
            this.tlsServer.notifyFallback(Arrays.contains(this.offeredCipherSuites, 22016));
            this.securityParameters.clientRandom = fully;
            this.tlsServer.notifyOfferedCipherSuites(this.offeredCipherSuites);
            this.tlsServer.notifyOfferedCompressionMethods(this.offeredCompressionMethods);
            if (Arrays.contains(this.offeredCipherSuites, 255)) {
                this.secure_renegotiation = true;
            }
            final byte[] extensionData = TlsUtils.getExtensionData(this.clientExtensions, TlsServerProtocol.EXT_RenegotiationInfo);
            if (extensionData != null) {
                this.secure_renegotiation = true;
                if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                    throw new TlsFatalAlert((short)40);
                }
            }
            this.tlsServer.notifySecureRenegotiation(this.secure_renegotiation);
            if (this.clientExtensions != null) {
                TlsExtensionsUtils.getPaddingExtension(this.clientExtensions);
                this.tlsServer.processClientExtensions(this.clientExtensions);
            }
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    protected void receiveClientKeyExchangeMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        this.keyExchange.processClientKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (TlsUtils.isSSL(this.getContext())) {
            TlsProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
        }
        this.prepareFinishHash = this.recordStream.prepareToFinish();
        this.securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(this.getContext(), this.prepareFinishHash, null);
        if (!TlsUtils.isSSL(this.getContext())) {
            TlsProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
        }
        this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
        if (!this.expectSessionTicket) {
            this.sendChangeCipherSpecMessage();
        }
    }
    
    protected void sendCertificateRequestMessage(final CertificateRequest certificateRequest) throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)13);
        certificateRequest.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void sendCertificateStatusMessage(final CertificateStatus certificateStatus) throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)22);
        certificateStatus.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void sendNewSessionTicketMessage(final NewSessionTicket newSessionTicket) throws IOException {
        if (newSessionTicket != null) {
            final HandshakeMessage handshakeMessage = new HandshakeMessage((short)4);
            newSessionTicket.encode(handshakeMessage);
            handshakeMessage.writeToRecordStream();
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected void sendServerHelloDoneMessage() throws IOException {
        final byte[] array = new byte[4];
        TlsUtils.writeUint8((short)14, array, 0);
        TlsUtils.writeUint24(0, array, 1);
        this.writeHandshakeMessage(array, 0, 4);
    }
    
    protected void sendServerHelloMessage() throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)2);
        final ProtocolVersion serverVersion = this.tlsServer.getServerVersion();
        if (!serverVersion.isEqualOrEarlierVersionOf(this.getContext().getClientVersion())) {
            throw new TlsFatalAlert((short)80);
        }
        this.recordStream.setReadVersion(serverVersion);
        this.recordStream.setWriteVersion(serverVersion);
        this.recordStream.setRestrictReadVersion(true);
        this.getContextAdmin().setServerVersion(serverVersion);
        TlsUtils.writeVersion(serverVersion, handshakeMessage);
        handshakeMessage.write(this.securityParameters.serverRandom);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, handshakeMessage);
        final int selectedCipherSuite = this.tlsServer.getSelectedCipherSuite();
        if (!Arrays.contains(this.offeredCipherSuites, selectedCipherSuite) || selectedCipherSuite == 0 || CipherSuite.isSCSV(selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(selectedCipherSuite, this.getContext().getServerVersion())) {
            throw new TlsFatalAlert((short)80);
        }
        this.securityParameters.cipherSuite = selectedCipherSuite;
        final short selectedCompressionMethod = this.tlsServer.getSelectedCompressionMethod();
        if (Arrays.contains(this.offeredCompressionMethods, selectedCompressionMethod)) {
            this.securityParameters.compressionAlgorithm = selectedCompressionMethod;
            TlsUtils.writeUint16(selectedCipherSuite, handshakeMessage);
            TlsUtils.writeUint8(selectedCompressionMethod, handshakeMessage);
            this.serverExtensions = this.tlsServer.getServerExtensions();
            final boolean secure_renegotiation = this.secure_renegotiation;
            final boolean b = false;
            if (secure_renegotiation && TlsUtils.getExtensionData(this.serverExtensions, TlsServerProtocol.EXT_RenegotiationInfo) == null) {
                (this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions)).put(TlsServerProtocol.EXT_RenegotiationInfo, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
            }
            if (this.securityParameters.extendedMasterSecret) {
                TlsExtensionsUtils.addExtendedMasterSecretExtension(this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions));
            }
            if (this.serverExtensions != null) {
                this.securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(this.serverExtensions);
                this.securityParameters.maxFragmentLength = this.processMaxFragmentLengthExtension(this.clientExtensions, this.serverExtensions, (short)80);
                this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(this.serverExtensions);
                this.allowCertificateStatus = (!this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short)80));
                boolean expectSessionTicket = b;
                if (!this.resumedSession) {
                    expectSessionTicket = b;
                    if (TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsProtocol.EXT_SessionTicket, (short)80)) {
                        expectSessionTicket = true;
                    }
                }
                this.expectSessionTicket = expectSessionTicket;
                TlsProtocol.writeExtensions(handshakeMessage, this.serverExtensions);
            }
            this.securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(this.getContext(), this.securityParameters.getCipherSuite());
            this.securityParameters.verifyDataLength = 12;
            this.applyMaxFragmentLengthExtension();
            handshakeMessage.writeToRecordStream();
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected void sendServerKeyExchangeMessage(final byte[] array) throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)12, array.length);
        handshakeMessage.write(array);
        handshakeMessage.writeToRecordStream();
    }
}
