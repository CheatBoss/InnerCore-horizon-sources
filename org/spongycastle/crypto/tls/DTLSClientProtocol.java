package org.spongycastle.crypto.tls;

import java.security.*;
import org.spongycastle.util.*;
import java.io.*;
import java.util.*;

public class DTLSClientProtocol extends DTLSProtocol
{
    public DTLSClientProtocol(final SecureRandom secureRandom) {
        super(secureRandom);
    }
    
    protected static byte[] patchClientHelloWithCookie(final byte[] array, final byte[] array2) throws IOException {
        final int n = TlsUtils.readUint8(array, 34) + 35;
        final int n2 = n + 1;
        final byte[] array3 = new byte[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, n);
        TlsUtils.checkUint8(array2.length);
        TlsUtils.writeUint8(array2.length, array3, n);
        System.arraycopy(array2, 0, array3, n2, array2.length);
        System.arraycopy(array, n2, array3, array2.length + n2, array.length - n2);
        return array3;
    }
    
    protected void abortClientHandshake(final ClientHandshakeState clientHandshakeState, final DTLSRecordLayer dtlsRecordLayer, final short n) {
        dtlsRecordLayer.fail(n);
        this.invalidateSession(clientHandshakeState);
    }
    
    protected DTLSTransport clientHandshake(final ClientHandshakeState clientHandshakeState, final DTLSRecordLayer dtlsRecordLayer) throws IOException {
        final SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        final DTLSReliableHandshake dtlsReliableHandshake = new DTLSReliableHandshake(clientHandshakeState.clientContext, dtlsRecordLayer);
        final byte[] generateClientHello = this.generateClientHello(clientHandshakeState, clientHandshakeState.client);
        dtlsRecordLayer.setWriteVersion(ProtocolVersion.DTLSv10);
        dtlsReliableHandshake.sendMessage((short)1, generateClientHello);
        while (true) {
            final DTLSReliableHandshake.Message receiveMessage = dtlsReliableHandshake.receiveMessage();
            if (receiveMessage.getType() == 3) {
                if (!dtlsRecordLayer.getReadVersion().isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
                    throw new TlsFatalAlert((short)47);
                }
                dtlsRecordLayer.setReadVersion(null);
                final byte[] patchClientHelloWithCookie = patchClientHelloWithCookie(generateClientHello, this.processHelloVerifyRequest(clientHandshakeState, receiveMessage.getBody()));
                dtlsReliableHandshake.resetHandshakeMessagesDigest();
                dtlsReliableHandshake.sendMessage((short)1, patchClientHelloWithCookie);
            }
            else {
                if (receiveMessage.getType() != 2) {
                    throw new TlsFatalAlert((short)10);
                }
                final ProtocolVersion readVersion = dtlsRecordLayer.getReadVersion();
                this.reportServerVersion(clientHandshakeState, readVersion);
                dtlsRecordLayer.setWriteVersion(readVersion);
                this.processServerHello(clientHandshakeState, receiveMessage.getBody());
                dtlsReliableHandshake.notifyHelloComplete();
                DTLSProtocol.applyMaxFragmentLengthExtension(dtlsRecordLayer, securityParameters.maxFragmentLength);
                if (clientHandshakeState.resumedSession) {
                    securityParameters.masterSecret = Arrays.clone(clientHandshakeState.sessionParameters.getMasterSecret());
                    dtlsRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                    this.processFinished(dtlsReliableHandshake.receiveMessageBody((short)20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "server finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dtlsReliableHandshake.getHandshakeHash(), null)));
                    dtlsReliableHandshake.sendMessage((short)20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "client finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dtlsReliableHandshake.getHandshakeHash(), null)));
                    dtlsReliableHandshake.finish();
                    clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                    clientHandshakeState.client.notifyHandshakeComplete();
                    return new DTLSTransport(dtlsRecordLayer);
                }
                this.invalidateSession(clientHandshakeState);
                if (clientHandshakeState.selectedSessionID.length > 0) {
                    clientHandshakeState.tlsSession = new TlsSessionImpl(clientHandshakeState.selectedSessionID, null);
                }
                DTLSReliableHandshake.Message message = dtlsReliableHandshake.receiveMessage();
                if (message.getType() == 23) {
                    this.processServerSupplementalData(clientHandshakeState, message.getBody());
                    message = dtlsReliableHandshake.receiveMessage();
                }
                else {
                    clientHandshakeState.client.processServerSupplementalData(null);
                }
                (clientHandshakeState.keyExchange = clientHandshakeState.client.getKeyExchange()).init(clientHandshakeState.clientContext);
                Certificate processServerCertificate;
                if (message.getType() == 11) {
                    processServerCertificate = this.processServerCertificate(clientHandshakeState, message.getBody());
                    message = dtlsReliableHandshake.receiveMessage();
                }
                else {
                    clientHandshakeState.keyExchange.skipServerCredentials();
                    processServerCertificate = null;
                }
                if (processServerCertificate == null || processServerCertificate.isEmpty()) {
                    clientHandshakeState.allowCertificateStatus = false;
                }
                DTLSReliableHandshake.Message message2 = message;
                if (message.getType() == 22) {
                    this.processCertificateStatus(clientHandshakeState, message.getBody());
                    message2 = dtlsReliableHandshake.receiveMessage();
                }
                if (message2.getType() == 12) {
                    this.processServerKeyExchange(clientHandshakeState, message2.getBody());
                    message2 = dtlsReliableHandshake.receiveMessage();
                }
                else {
                    clientHandshakeState.keyExchange.skipServerKeyExchange();
                }
                DTLSReliableHandshake.Message receiveMessage2 = message2;
                if (message2.getType() == 13) {
                    this.processCertificateRequest(clientHandshakeState, message2.getBody());
                    TlsUtils.trackHashAlgorithms(dtlsReliableHandshake.getHandshakeHash(), clientHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
                    receiveMessage2 = dtlsReliableHandshake.receiveMessage();
                }
                if (receiveMessage2.getType() != 14) {
                    throw new TlsFatalAlert((short)10);
                }
                if (receiveMessage2.getBody().length == 0) {
                    dtlsReliableHandshake.getHandshakeHash().sealHashAlgorithms();
                    final Vector clientSupplementalData = clientHandshakeState.client.getClientSupplementalData();
                    if (clientSupplementalData != null) {
                        dtlsReliableHandshake.sendMessage((short)23, DTLSProtocol.generateSupplementalData(clientSupplementalData));
                    }
                    if (clientHandshakeState.certificateRequest != null) {
                        clientHandshakeState.clientCredentials = clientHandshakeState.authentication.getClientCredentials(clientHandshakeState.certificateRequest);
                        Certificate certificate;
                        if (clientHandshakeState.clientCredentials != null) {
                            certificate = clientHandshakeState.clientCredentials.getCertificate();
                        }
                        else {
                            certificate = null;
                        }
                        Certificate empty_CHAIN = certificate;
                        if (certificate == null) {
                            empty_CHAIN = Certificate.EMPTY_CHAIN;
                        }
                        dtlsReliableHandshake.sendMessage((short)11, DTLSProtocol.generateCertificate(empty_CHAIN));
                    }
                    if (clientHandshakeState.clientCredentials != null) {
                        clientHandshakeState.keyExchange.processClientCredentials(clientHandshakeState.clientCredentials);
                    }
                    else {
                        clientHandshakeState.keyExchange.skipClientCredentials();
                    }
                    dtlsReliableHandshake.sendMessage((short)16, this.generateClientKeyExchange(clientHandshakeState));
                    final TlsHandshakeHash prepareToFinish = dtlsReliableHandshake.prepareToFinish();
                    securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, prepareToFinish, null);
                    TlsProtocol.establishMasterSecret(clientHandshakeState.clientContext, clientHandshakeState.keyExchange);
                    dtlsRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                    if (clientHandshakeState.clientCredentials != null && clientHandshakeState.clientCredentials instanceof TlsSignerCredentials) {
                        final TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials)clientHandshakeState.clientCredentials;
                        final SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(clientHandshakeState.clientContext, tlsSignerCredentials);
                        byte[] array;
                        if (signatureAndHashAlgorithm == null) {
                            array = securityParameters.getSessionHash();
                        }
                        else {
                            array = prepareToFinish.getFinalHash(signatureAndHashAlgorithm.getHash());
                        }
                        dtlsReliableHandshake.sendMessage((short)15, this.generateCertificateVerify(clientHandshakeState, new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(array))));
                    }
                    dtlsReliableHandshake.sendMessage((short)20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "client finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dtlsReliableHandshake.getHandshakeHash(), null)));
                    if (clientHandshakeState.expectSessionTicket) {
                        final DTLSReliableHandshake.Message receiveMessage3 = dtlsReliableHandshake.receiveMessage();
                        if (receiveMessage3.getType() != 4) {
                            throw new TlsFatalAlert((short)10);
                        }
                        this.processNewSessionTicket(clientHandshakeState, receiveMessage3.getBody());
                    }
                    this.processFinished(dtlsReliableHandshake.receiveMessageBody((short)20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "server finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dtlsReliableHandshake.getHandshakeHash(), null)));
                    dtlsReliableHandshake.finish();
                    if (clientHandshakeState.tlsSession != null) {
                        clientHandshakeState.sessionParameters = new SessionParameters.Builder().setCipherSuite(securityParameters.getCipherSuite()).setCompressionAlgorithm(securityParameters.getCompressionAlgorithm()).setMasterSecret(securityParameters.getMasterSecret()).setPeerCertificate(processServerCertificate).setPSKIdentity(securityParameters.getPSKIdentity()).setSRPIdentity(securityParameters.getSRPIdentity()).setServerExtensions(clientHandshakeState.serverExtensions).build();
                        clientHandshakeState.tlsSession = TlsUtils.importSession(clientHandshakeState.tlsSession.getSessionID(), clientHandshakeState.sessionParameters);
                        clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                    }
                    clientHandshakeState.client.notifyHandshakeComplete();
                    return new DTLSTransport(dtlsRecordLayer);
                }
                throw new TlsFatalAlert((short)50);
            }
        }
    }
    
    public DTLSTransport connect(final TlsClient client, final DatagramTransport datagramTransport) throws IOException {
        if (client == null) {
            throw new IllegalArgumentException("'client' cannot be null");
        }
        if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        }
        final SecurityParameters securityParameters = new SecurityParameters();
        securityParameters.entity = 1;
        final ClientHandshakeState clientHandshakeState = new ClientHandshakeState();
        clientHandshakeState.client = client;
        clientHandshakeState.clientContext = new TlsClientContextImpl(this.secureRandom, securityParameters);
        securityParameters.clientRandom = TlsProtocol.createRandomBlock(client.shouldUseGMTUnixTime(), clientHandshakeState.clientContext.getNonceRandomGenerator());
        client.init(clientHandshakeState.clientContext);
        final DTLSRecordLayer dtlsRecordLayer = new DTLSRecordLayer(datagramTransport, clientHandshakeState.clientContext, client, (short)22);
        final TlsSession sessionToResume = clientHandshakeState.client.getSessionToResume();
        Label_0152: {
            if (sessionToResume == null || !sessionToResume.isResumable()) {
                break Label_0152;
            }
            final SessionParameters exportSessionParameters = sessionToResume.exportSessionParameters();
            if (exportSessionParameters == null) {
                break Label_0152;
            }
            clientHandshakeState.tlsSession = sessionToResume;
            clientHandshakeState.sessionParameters = exportSessionParameters;
            try {
                try {
                    final DTLSTransport clientHandshake = this.clientHandshake(clientHandshakeState, dtlsRecordLayer);
                    securityParameters.clear();
                    return clientHandshake;
                }
                finally {}
            }
            catch (RuntimeException ex) {
                this.abortClientHandshake(clientHandshakeState, (DTLSRecordLayer)client, (short)80);
                throw new TlsFatalAlert((short)80, ex);
            }
            catch (IOException ex2) {
                this.abortClientHandshake(clientHandshakeState, (DTLSRecordLayer)client, (short)80);
                throw ex2;
            }
            catch (TlsFatalAlert tlsFatalAlert) {
                this.abortClientHandshake(clientHandshakeState, (DTLSRecordLayer)client, tlsFatalAlert.getAlertDescription());
                throw tlsFatalAlert;
            }
        }
        securityParameters.clear();
    }
    
    protected byte[] generateCertificateVerify(final ClientHandshakeState clientHandshakeState, final DigitallySigned digitallySigned) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        digitallySigned.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    protected byte[] generateClientHello(final ClientHandshakeState clientHandshakeState, final TlsClient tlsClient) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ProtocolVersion clientVersion = tlsClient.getClientVersion();
        if (clientVersion.isDTLS()) {
            final TlsClientContextImpl clientContext = clientHandshakeState.clientContext;
            clientContext.setClientVersion(clientVersion);
            TlsUtils.writeVersion(clientVersion, byteArrayOutputStream);
            byteArrayOutputStream.write(clientContext.getSecurityParameters().getClientRandom());
            byte[] array = TlsUtils.EMPTY_BYTES;
            Label_0103: {
                if (clientHandshakeState.tlsSession != null) {
                    final byte[] sessionID = clientHandshakeState.tlsSession.getSessionID();
                    if (sessionID != null) {
                        array = sessionID;
                        if (sessionID.length <= 32) {
                            break Label_0103;
                        }
                    }
                    array = TlsUtils.EMPTY_BYTES;
                }
            }
            TlsUtils.writeOpaque8(array, byteArrayOutputStream);
            TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, byteArrayOutputStream);
            final boolean fallback = tlsClient.isFallback();
            clientHandshakeState.offeredCipherSuites = tlsClient.getCipherSuites();
            clientHandshakeState.clientExtensions = tlsClient.getClientExtensions();
            final boolean b = TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo) == null;
            final boolean contains = Arrays.contains(clientHandshakeState.offeredCipherSuites, 255);
            if (b && (contains ^ true)) {
                clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, 255);
            }
            if (fallback && !Arrays.contains(clientHandshakeState.offeredCipherSuites, 22016)) {
                clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, 22016);
            }
            TlsUtils.writeUint16ArrayWithUint16Length(clientHandshakeState.offeredCipherSuites, byteArrayOutputStream);
            TlsUtils.writeUint8ArrayWithUint8Length(clientHandshakeState.offeredCompressionMethods = new short[] { 0 }, byteArrayOutputStream);
            if (clientHandshakeState.clientExtensions != null) {
                TlsProtocol.writeExtensions(byteArrayOutputStream, clientHandshakeState.clientExtensions);
            }
            return byteArrayOutputStream.toByteArray();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected byte[] generateClientKeyExchange(final ClientHandshakeState clientHandshakeState) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        clientHandshakeState.keyExchange.generateClientKeyExchange(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    protected void invalidateSession(final ClientHandshakeState clientHandshakeState) {
        if (clientHandshakeState.sessionParameters != null) {
            clientHandshakeState.sessionParameters.clear();
            clientHandshakeState.sessionParameters = null;
        }
        if (clientHandshakeState.tlsSession != null) {
            clientHandshakeState.tlsSession.invalidate();
            clientHandshakeState.tlsSession = null;
        }
    }
    
    protected void processCertificateRequest(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        if (clientHandshakeState.authentication != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            clientHandshakeState.certificateRequest = CertificateRequest.parse(clientHandshakeState.clientContext, byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            clientHandshakeState.keyExchange.validateCertificateRequest(clientHandshakeState.certificateRequest);
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
    
    protected void processCertificateStatus(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        if (clientHandshakeState.allowCertificateStatus) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            clientHandshakeState.certificateStatus = CertificateStatus.parse(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    protected byte[] processHelloVerifyRequest(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        final byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!version.isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
            throw new TlsFatalAlert((short)47);
        }
        if (ProtocolVersion.DTLSv12.isEqualOrEarlierVersionOf(version)) {
            return opaque8;
        }
        if (opaque8.length <= 32) {
            return opaque8;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    protected void processNewSessionTicket(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final NewSessionTicket parse = NewSessionTicket.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.client.notifyNewSessionTicket(parse);
    }
    
    protected Certificate processServerCertificate(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final Certificate parse = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.processServerCertificate(parse);
        (clientHandshakeState.authentication = clientHandshakeState.client.getAuthentication()).notifyServerCertificate(parse);
        return parse;
    }
    
    protected void processServerHello(final ClientHandshakeState clientHandshakeState, byte[] extensionData) throws IOException {
        final SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(extensionData);
        this.reportServerVersion(clientHandshakeState, TlsUtils.readVersion(byteArrayInputStream));
        securityParameters.serverRandom = TlsUtils.readFully(32, byteArrayInputStream);
        clientHandshakeState.selectedSessionID = TlsUtils.readOpaque8(byteArrayInputStream);
        if (clientHandshakeState.selectedSessionID.length > 32) {
            throw new TlsFatalAlert((short)47);
        }
        clientHandshakeState.client.notifySessionID(clientHandshakeState.selectedSessionID);
        final int length = clientHandshakeState.selectedSessionID.length;
        final boolean b = false;
        clientHandshakeState.resumedSession = (length > 0 && clientHandshakeState.tlsSession != null && Arrays.areEqual(clientHandshakeState.selectedSessionID, clientHandshakeState.tlsSession.getSessionID()));
        final int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (!Arrays.contains(clientHandshakeState.offeredCipherSuites, uint16) || uint16 == 0 || CipherSuite.isSCSV(uint16) || !TlsUtils.isValidCipherSuiteForVersion(uint16, clientHandshakeState.clientContext.getServerVersion())) {
            throw new TlsFatalAlert((short)47);
        }
        DTLSProtocol.validateSelectedCipherSuite(uint16, (short)47);
        clientHandshakeState.client.notifySelectedCipherSuite(uint16);
        final short uint17 = TlsUtils.readUint8(byteArrayInputStream);
        if (Arrays.contains(clientHandshakeState.offeredCompressionMethods, uint17)) {
            clientHandshakeState.client.notifySelectedCompressionMethod(uint17);
            clientHandshakeState.serverExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
            if (clientHandshakeState.serverExtensions != null) {
                final Enumeration<Integer> keys = (Enumeration<Integer>)clientHandshakeState.serverExtensions.keys();
                while (keys.hasMoreElements()) {
                    final Integer n = keys.nextElement();
                    if (n.equals(TlsProtocol.EXT_RenegotiationInfo)) {
                        continue;
                    }
                    if (TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, n) == null) {
                        throw new TlsFatalAlert((short)110);
                    }
                    final boolean resumedSession = clientHandshakeState.resumedSession;
                }
            }
            extensionData = TlsUtils.getExtensionData(clientHandshakeState.serverExtensions, TlsProtocol.EXT_RenegotiationInfo);
            if (extensionData != null) {
                clientHandshakeState.secure_renegotiation = true;
                if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                    throw new TlsFatalAlert((short)40);
                }
            }
            clientHandshakeState.client.notifySecureRenegotiation(clientHandshakeState.secure_renegotiation);
            Hashtable clientExtensions = clientHandshakeState.clientExtensions;
            Hashtable hashtable = clientHandshakeState.serverExtensions;
            if (clientHandshakeState.resumedSession) {
                if (uint16 != clientHandshakeState.sessionParameters.getCipherSuite() || uint17 != clientHandshakeState.sessionParameters.getCompressionAlgorithm()) {
                    throw new TlsFatalAlert((short)47);
                }
                clientExtensions = null;
                hashtable = clientHandshakeState.sessionParameters.readServerExtensions();
            }
            securityParameters.cipherSuite = uint16;
            securityParameters.compressionAlgorithm = uint17;
            if (hashtable != null) {
                final boolean hasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable);
                if (hasEncryptThenMACExtension && !TlsUtils.isBlockCipherSuite(securityParameters.getCipherSuite())) {
                    throw new TlsFatalAlert((short)47);
                }
                securityParameters.encryptThenMAC = hasEncryptThenMACExtension;
                securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(hashtable);
                securityParameters.maxFragmentLength = DTLSProtocol.evaluateMaxFragmentLengthExtension(clientHandshakeState.resumedSession, clientExtensions, hashtable, (short)47);
                securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable);
                clientHandshakeState.allowCertificateStatus = (!clientHandshakeState.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsExtensionsUtils.EXT_status_request, (short)47));
                boolean expectSessionTicket = b;
                if (!clientHandshakeState.resumedSession) {
                    expectSessionTicket = b;
                    if (TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsProtocol.EXT_SessionTicket, (short)47)) {
                        expectSessionTicket = true;
                    }
                }
                clientHandshakeState.expectSessionTicket = expectSessionTicket;
            }
            if (clientExtensions != null) {
                clientHandshakeState.client.processServerExtensions(hashtable);
            }
            securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(clientHandshakeState.clientContext, securityParameters.getCipherSuite());
            securityParameters.verifyDataLength = 12;
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    protected void processServerKeyExchange(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        clientHandshakeState.keyExchange.processServerKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }
    
    protected void processServerSupplementalData(final ClientHandshakeState clientHandshakeState, final byte[] array) throws IOException {
        clientHandshakeState.client.processServerSupplementalData(TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(array)));
    }
    
    protected void reportServerVersion(final ClientHandshakeState clientHandshakeState, final ProtocolVersion serverVersion) throws IOException {
        final TlsClientContextImpl clientContext = clientHandshakeState.clientContext;
        final ProtocolVersion serverVersion2 = clientContext.getServerVersion();
        if (serverVersion2 == null) {
            clientContext.setServerVersion(serverVersion);
            clientHandshakeState.client.notifyServerVersion(serverVersion);
            return;
        }
        if (serverVersion2.equals(serverVersion)) {
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    protected static class ClientHandshakeState
    {
        boolean allowCertificateStatus;
        TlsAuthentication authentication;
        CertificateRequest certificateRequest;
        CertificateStatus certificateStatus;
        TlsClient client;
        TlsClientContextImpl clientContext;
        TlsCredentials clientCredentials;
        Hashtable clientExtensions;
        boolean expectSessionTicket;
        TlsKeyExchange keyExchange;
        int[] offeredCipherSuites;
        short[] offeredCompressionMethods;
        boolean resumedSession;
        boolean secure_renegotiation;
        byte[] selectedSessionID;
        Hashtable serverExtensions;
        SessionParameters sessionParameters;
        SessionParameters.Builder sessionParametersBuilder;
        TlsSession tlsSession;
        
        protected ClientHandshakeState() {
            this.client = null;
            this.clientContext = null;
            this.tlsSession = null;
            this.sessionParameters = null;
            this.sessionParametersBuilder = null;
            this.offeredCipherSuites = null;
            this.offeredCompressionMethods = null;
            this.clientExtensions = null;
            this.serverExtensions = null;
            this.selectedSessionID = null;
            this.resumedSession = false;
            this.secure_renegotiation = false;
            this.allowCertificateStatus = false;
            this.expectSessionTicket = false;
            this.keyExchange = null;
            this.authentication = null;
            this.certificateStatus = null;
            this.certificateRequest = null;
            this.clientCredentials = null;
        }
    }
}
