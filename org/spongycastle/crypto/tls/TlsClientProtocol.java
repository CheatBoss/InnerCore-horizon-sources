package org.spongycastle.crypto.tls;

import java.security.*;
import java.io.*;
import org.spongycastle.util.*;
import java.util.*;

public class TlsClientProtocol extends TlsProtocol
{
    protected TlsAuthentication authentication;
    protected CertificateRequest certificateRequest;
    protected CertificateStatus certificateStatus;
    protected TlsKeyExchange keyExchange;
    protected byte[] selectedSessionID;
    protected TlsClient tlsClient;
    TlsClientContextImpl tlsClientContext;
    
    public TlsClientProtocol(final InputStream inputStream, final OutputStream outputStream, final SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsClient = null;
        this.tlsClientContext = null;
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }
    
    public TlsClientProtocol(final SecureRandom secureRandom) {
        super(secureRandom);
        this.tlsClient = null;
        this.tlsClientContext = null;
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }
    
    @Override
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }
    
    public void connect(final TlsClient tlsClient) throws IOException {
        if (tlsClient == null) {
            throw new IllegalArgumentException("'tlsClient' cannot be null");
        }
        if (this.tlsClient == null) {
            this.tlsClient = tlsClient;
            this.securityParameters = new SecurityParameters();
            this.securityParameters.entity = 1;
            this.tlsClientContext = new TlsClientContextImpl(this.secureRandom, this.securityParameters);
            this.securityParameters.clientRandom = TlsProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), this.tlsClientContext.getNonceRandomGenerator());
            this.tlsClient.init(this.tlsClientContext);
            this.recordStream.init(this.tlsClientContext);
            final TlsSession sessionToResume = tlsClient.getSessionToResume();
            if (sessionToResume != null && sessionToResume.isResumable()) {
                final SessionParameters exportSessionParameters = sessionToResume.exportSessionParameters();
                if (exportSessionParameters != null) {
                    this.tlsSession = sessionToResume;
                    this.sessionParameters = exportSessionParameters;
                }
            }
            this.sendClientHelloMessage();
            this.connection_state = 1;
            this.blockForHandshake();
            return;
        }
        throw new IllegalStateException("'connect' can only be called once");
    }
    
    @Override
    protected TlsContext getContext() {
        return this.tlsClientContext;
    }
    
    @Override
    AbstractTlsContext getContextAdmin() {
        return this.tlsClientContext;
    }
    
    @Override
    protected TlsPeer getPeer() {
        return this.tlsClient;
    }
    
    @Override
    protected void handleHandshakeMessage(final short n, final ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (!this.resumedSession) {
            if (n != 0) {
                if (n != 2) {
                    if (n != 4) {
                        if (n == 20) {
                            final short connection_state = this.connection_state;
                            if (connection_state != 13) {
                                if (connection_state != 14) {
                                    throw new TlsFatalAlert((short)10);
                                }
                            }
                            else if (this.expectSessionTicket) {
                                throw new TlsFatalAlert((short)10);
                            }
                            this.processFinishedMessage(byteArrayInputStream);
                            this.connection_state = 15;
                            this.completeHandshake();
                            return;
                        }
                        if (n != 22) {
                            if (n != 23) {
                                switch (n) {
                                    default: {
                                        throw new TlsFatalAlert((short)10);
                                    }
                                    case 14: {
                                        switch (this.connection_state) {
                                            default: {
                                                throw new TlsFatalAlert((short)10);
                                            }
                                            case 2: {
                                                this.handleSupplementalData(null);
                                            }
                                            case 3: {
                                                this.keyExchange.skipServerCredentials();
                                                this.authentication = null;
                                            }
                                            case 4:
                                            case 5: {
                                                this.keyExchange.skipServerKeyExchange();
                                            }
                                            case 6:
                                            case 7: {
                                                TlsProtocol.assertEmpty(byteArrayInputStream);
                                                this.connection_state = 8;
                                                this.recordStream.getHandshakeHash().sealHashAlgorithms();
                                                final Vector clientSupplementalData = this.tlsClient.getClientSupplementalData();
                                                if (clientSupplementalData != null) {
                                                    this.sendSupplementalDataMessage(clientSupplementalData);
                                                }
                                                this.connection_state = 9;
                                                final CertificateRequest certificateRequest = this.certificateRequest;
                                                TlsCredentials tlsCredentials;
                                                if (certificateRequest == null) {
                                                    this.keyExchange.skipClientCredentials();
                                                    tlsCredentials = null;
                                                }
                                                else {
                                                    final TlsCredentials clientCredentials = this.authentication.getClientCredentials(certificateRequest);
                                                    Certificate certificate;
                                                    if (clientCredentials == null) {
                                                        this.keyExchange.skipClientCredentials();
                                                        certificate = Certificate.EMPTY_CHAIN;
                                                    }
                                                    else {
                                                        this.keyExchange.processClientCredentials(clientCredentials);
                                                        certificate = clientCredentials.getCertificate();
                                                    }
                                                    this.sendCertificateMessage(certificate);
                                                    tlsCredentials = clientCredentials;
                                                }
                                                this.connection_state = 10;
                                                this.sendClientKeyExchangeMessage();
                                                this.connection_state = 11;
                                                if (TlsUtils.isSSL(this.getContext())) {
                                                    TlsProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
                                                }
                                                final TlsHandshakeHash prepareToFinish = this.recordStream.prepareToFinish();
                                                this.securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(this.getContext(), prepareToFinish, null);
                                                if (!TlsUtils.isSSL(this.getContext())) {
                                                    TlsProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
                                                }
                                                this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
                                                if (tlsCredentials != null && tlsCredentials instanceof TlsSignerCredentials) {
                                                    final TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials)tlsCredentials;
                                                    final SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.getContext(), tlsSignerCredentials);
                                                    byte[] array;
                                                    if (signatureAndHashAlgorithm == null) {
                                                        array = this.securityParameters.getSessionHash();
                                                    }
                                                    else {
                                                        array = prepareToFinish.getFinalHash(signatureAndHashAlgorithm.getHash());
                                                    }
                                                    this.sendCertificateVerifyMessage(new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(array)));
                                                    this.connection_state = 12;
                                                }
                                                this.sendChangeCipherSpecMessage();
                                                this.sendFinishedMessage();
                                                this.connection_state = 13;
                                                return;
                                            }
                                        }
                                        break;
                                    }
                                    case 13: {
                                        final short connection_state2 = this.connection_state;
                                        if (connection_state2 != 4 && connection_state2 != 5) {
                                            if (connection_state2 != 6) {
                                                throw new TlsFatalAlert((short)10);
                                            }
                                        }
                                        else {
                                            this.keyExchange.skipServerKeyExchange();
                                        }
                                        if (this.authentication != null) {
                                            this.certificateRequest = CertificateRequest.parse(this.getContext(), byteArrayInputStream);
                                            TlsProtocol.assertEmpty(byteArrayInputStream);
                                            this.keyExchange.validateCertificateRequest(this.certificateRequest);
                                            TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                                            this.connection_state = 7;
                                            return;
                                        }
                                        throw new TlsFatalAlert((short)40);
                                    }
                                    case 12: {
                                        final short connection_state3 = this.connection_state;
                                        Label_0713: {
                                            if (connection_state3 != 2) {
                                                if (connection_state3 != 3) {
                                                    if (connection_state3 == 4) {
                                                        break Label_0713;
                                                    }
                                                    if (connection_state3 == 5) {
                                                        break Label_0713;
                                                    }
                                                    throw new TlsFatalAlert((short)10);
                                                }
                                            }
                                            else {
                                                this.handleSupplementalData(null);
                                            }
                                            this.keyExchange.skipServerCredentials();
                                            this.authentication = null;
                                        }
                                        this.keyExchange.processServerKeyExchange(byteArrayInputStream);
                                        TlsProtocol.assertEmpty(byteArrayInputStream);
                                        this.connection_state = 6;
                                    }
                                    case 11: {
                                        final short connection_state4 = this.connection_state;
                                        if (connection_state4 != 2) {
                                            if (connection_state4 != 3) {
                                                throw new TlsFatalAlert((short)10);
                                            }
                                        }
                                        else {
                                            this.handleSupplementalData(null);
                                        }
                                        this.peerCertificate = Certificate.parse(byteArrayInputStream);
                                        TlsProtocol.assertEmpty(byteArrayInputStream);
                                        if (this.peerCertificate == null || this.peerCertificate.isEmpty()) {
                                            this.allowCertificateStatus = false;
                                        }
                                        this.keyExchange.processServerCertificate(this.peerCertificate);
                                        (this.authentication = this.tlsClient.getAuthentication()).notifyServerCertificate(this.peerCertificate);
                                        this.connection_state = 4;
                                    }
                                }
                            }
                            else {
                                if (this.connection_state == 2) {
                                    this.handleSupplementalData(TlsProtocol.readSupplementalDataMessage(byteArrayInputStream));
                                    return;
                                }
                                throw new TlsFatalAlert((short)10);
                            }
                        }
                        else {
                            if (this.connection_state != 4) {
                                throw new TlsFatalAlert((short)10);
                            }
                            if (this.allowCertificateStatus) {
                                this.certificateStatus = CertificateStatus.parse(byteArrayInputStream);
                                TlsProtocol.assertEmpty(byteArrayInputStream);
                                this.connection_state = 5;
                                return;
                            }
                            throw new TlsFatalAlert((short)10);
                        }
                    }
                    else {
                        if (this.connection_state != 13) {
                            throw new TlsFatalAlert((short)10);
                        }
                        if (this.expectSessionTicket) {
                            this.invalidateSession();
                            this.receiveNewSessionTicketMessage(byteArrayInputStream);
                            this.connection_state = 14;
                            return;
                        }
                        throw new TlsFatalAlert((short)10);
                    }
                }
                else {
                    if (this.connection_state != 1) {
                        throw new TlsFatalAlert((short)10);
                    }
                    this.receiveServerHelloMessage(byteArrayInputStream);
                    this.connection_state = 2;
                    this.recordStream.notifyHelloComplete();
                    this.applyMaxFragmentLengthExtension();
                    if (this.resumedSession) {
                        this.securityParameters.masterSecret = Arrays.clone(this.sessionParameters.getMasterSecret());
                        this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
                        this.sendChangeCipherSpecMessage();
                        return;
                    }
                    this.invalidateSession();
                    if (this.selectedSessionID.length > 0) {
                        this.tlsSession = new TlsSessionImpl(this.selectedSessionID, null);
                    }
                }
            }
            else {
                TlsProtocol.assertEmpty(byteArrayInputStream);
                if (this.connection_state == 16) {
                    this.refuseRenegotiation();
                }
            }
            return;
        }
        if (n == 20 && this.connection_state == 2) {
            this.processFinishedMessage(byteArrayInputStream);
            this.connection_state = 15;
            this.sendFinishedMessage();
            this.connection_state = 13;
            this.completeHandshake();
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    protected void handleSupplementalData(final Vector vector) throws IOException {
        this.tlsClient.processServerSupplementalData(vector);
        this.connection_state = 3;
        (this.keyExchange = this.tlsClient.getKeyExchange()).init(this.getContext());
    }
    
    protected void receiveNewSessionTicketMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final NewSessionTicket parse = NewSessionTicket.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        this.tlsClient.notifyNewSessionTicket(parse);
    }
    
    protected void receiveServerHelloMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        if (version.isDTLS()) {
            throw new TlsFatalAlert((short)47);
        }
        if (!version.equals(this.recordStream.getReadVersion())) {
            throw new TlsFatalAlert((short)47);
        }
        if (!version.isEqualOrEarlierVersionOf(this.getContext().getClientVersion())) {
            throw new TlsFatalAlert((short)47);
        }
        this.recordStream.setWriteVersion(version);
        this.getContextAdmin().setServerVersion(version);
        this.tlsClient.notifyServerVersion(version);
        this.securityParameters.serverRandom = TlsUtils.readFully(32, byteArrayInputStream);
        final byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
        this.selectedSessionID = opaque8;
        if (opaque8.length > 32) {
            throw new TlsFatalAlert((short)47);
        }
        this.tlsClient.notifySessionID(opaque8);
        final int length = this.selectedSessionID.length;
        final boolean b = false;
        this.resumedSession = (length > 0 && this.tlsSession != null && Arrays.areEqual(this.selectedSessionID, this.tlsSession.getSessionID()));
        final int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (!Arrays.contains(this.offeredCipherSuites, uint16) || uint16 == 0 || CipherSuite.isSCSV(uint16) || !TlsUtils.isValidCipherSuiteForVersion(uint16, this.getContext().getServerVersion())) {
            throw new TlsFatalAlert((short)47);
        }
        this.tlsClient.notifySelectedCipherSuite(uint16);
        final short uint17 = TlsUtils.readUint8(byteArrayInputStream);
        if (Arrays.contains(this.offeredCompressionMethods, uint17)) {
            this.tlsClient.notifySelectedCompressionMethod(uint17);
            this.serverExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
            if (this.serverExtensions != null) {
                final Enumeration<Integer> keys = (Enumeration<Integer>)this.serverExtensions.keys();
                while (keys.hasMoreElements()) {
                    final Integer n = keys.nextElement();
                    if (n.equals(TlsClientProtocol.EXT_RenegotiationInfo)) {
                        continue;
                    }
                    if (TlsUtils.getExtensionData(this.clientExtensions, n) == null) {
                        throw new TlsFatalAlert((short)110);
                    }
                    final boolean resumedSession = this.resumedSession;
                }
            }
            final byte[] extensionData = TlsUtils.getExtensionData(this.serverExtensions, TlsClientProtocol.EXT_RenegotiationInfo);
            if (extensionData != null) {
                this.secure_renegotiation = true;
                if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                    throw new TlsFatalAlert((short)40);
                }
            }
            this.tlsClient.notifySecureRenegotiation(this.secure_renegotiation);
            Hashtable clientExtensions = this.clientExtensions;
            Hashtable hashtable = this.serverExtensions;
            if (this.resumedSession) {
                if (uint16 != this.sessionParameters.getCipherSuite() || uint17 != this.sessionParameters.getCompressionAlgorithm()) {
                    throw new TlsFatalAlert((short)47);
                }
                clientExtensions = null;
                hashtable = this.sessionParameters.readServerExtensions();
            }
            this.securityParameters.cipherSuite = uint16;
            this.securityParameters.compressionAlgorithm = uint17;
            if (hashtable != null) {
                final boolean hasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable);
                if (hasEncryptThenMACExtension && !TlsUtils.isBlockCipherSuite(uint16)) {
                    throw new TlsFatalAlert((short)47);
                }
                this.securityParameters.encryptThenMAC = hasEncryptThenMACExtension;
                this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(hashtable);
                this.securityParameters.maxFragmentLength = this.processMaxFragmentLengthExtension(clientExtensions, hashtable, (short)47);
                this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable);
                this.allowCertificateStatus = (!this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsExtensionsUtils.EXT_status_request, (short)47));
                boolean expectSessionTicket = b;
                if (!this.resumedSession) {
                    expectSessionTicket = b;
                    if (TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsProtocol.EXT_SessionTicket, (short)47)) {
                        expectSessionTicket = true;
                    }
                }
                this.expectSessionTicket = expectSessionTicket;
            }
            if (clientExtensions != null) {
                this.tlsClient.processServerExtensions(hashtable);
            }
            this.securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(this.getContext(), this.securityParameters.getCipherSuite());
            this.securityParameters.verifyDataLength = 12;
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    protected void sendCertificateVerifyMessage(final DigitallySigned digitallySigned) throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)15);
        digitallySigned.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void sendClientHelloMessage() throws IOException {
        this.recordStream.setWriteVersion(this.tlsClient.getClientHelloRecordLayerVersion());
        final ProtocolVersion clientVersion = this.tlsClient.getClientVersion();
        if (!clientVersion.isDTLS()) {
            this.getContextAdmin().setClientVersion(clientVersion);
            byte[] array = TlsUtils.EMPTY_BYTES;
            Label_0089: {
                if (this.tlsSession != null) {
                    final byte[] sessionID = this.tlsSession.getSessionID();
                    if (sessionID != null) {
                        array = sessionID;
                        if (sessionID.length <= 32) {
                            break Label_0089;
                        }
                    }
                    array = TlsUtils.EMPTY_BYTES;
                }
            }
            final boolean fallback = this.tlsClient.isFallback();
            this.offeredCipherSuites = this.tlsClient.getCipherSuites();
            this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
            byte[] empty_BYTES = array;
            Label_0189: {
                if (array.length > 0) {
                    empty_BYTES = array;
                    if (this.sessionParameters != null) {
                        if (Arrays.contains(this.offeredCipherSuites, this.sessionParameters.getCipherSuite())) {
                            empty_BYTES = array;
                            if (Arrays.contains(this.offeredCompressionMethods, this.sessionParameters.getCompressionAlgorithm())) {
                                break Label_0189;
                            }
                        }
                        empty_BYTES = TlsUtils.EMPTY_BYTES;
                    }
                }
            }
            this.clientExtensions = this.tlsClient.getClientExtensions();
            final HandshakeMessage handshakeMessage = new HandshakeMessage((short)1);
            TlsUtils.writeVersion(clientVersion, handshakeMessage);
            handshakeMessage.write(this.securityParameters.getClientRandom());
            TlsUtils.writeOpaque8(empty_BYTES, handshakeMessage);
            final boolean b = TlsUtils.getExtensionData(this.clientExtensions, TlsClientProtocol.EXT_RenegotiationInfo) == null;
            final boolean contains = Arrays.contains(this.offeredCipherSuites, 255);
            if (b && (contains ^ true)) {
                this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, 255);
            }
            if (fallback && !Arrays.contains(this.offeredCipherSuites, 22016)) {
                this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, 22016);
            }
            TlsUtils.writeUint16ArrayWithUint16Length(this.offeredCipherSuites, handshakeMessage);
            TlsUtils.writeUint8ArrayWithUint8Length(this.offeredCompressionMethods, handshakeMessage);
            if (this.clientExtensions != null) {
                TlsProtocol.writeExtensions(handshakeMessage, this.clientExtensions);
            }
            handshakeMessage.writeToRecordStream();
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected void sendClientKeyExchangeMessage() throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)16);
        this.keyExchange.generateClientKeyExchange(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
}
