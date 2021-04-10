package org.spongycastle.crypto.tls;

import java.security.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import java.util.*;
import java.io.*;

public abstract class TlsProtocol
{
    protected static final short ADS_MODE_0_N = 1;
    protected static final short ADS_MODE_0_N_FIRSTONLY = 2;
    protected static final short ADS_MODE_1_Nsub1 = 0;
    protected static final short CS_CERTIFICATE_REQUEST = 7;
    protected static final short CS_CERTIFICATE_STATUS = 5;
    protected static final short CS_CERTIFICATE_VERIFY = 12;
    protected static final short CS_CLIENT_CERTIFICATE = 10;
    protected static final short CS_CLIENT_FINISHED = 13;
    protected static final short CS_CLIENT_HELLO = 1;
    protected static final short CS_CLIENT_KEY_EXCHANGE = 11;
    protected static final short CS_CLIENT_SUPPLEMENTAL_DATA = 9;
    protected static final short CS_END = 16;
    protected static final short CS_SERVER_CERTIFICATE = 4;
    protected static final short CS_SERVER_FINISHED = 15;
    protected static final short CS_SERVER_HELLO = 2;
    protected static final short CS_SERVER_HELLO_DONE = 8;
    protected static final short CS_SERVER_KEY_EXCHANGE = 6;
    protected static final short CS_SERVER_SESSION_TICKET = 14;
    protected static final short CS_SERVER_SUPPLEMENTAL_DATA = 3;
    protected static final short CS_START = 0;
    protected static final Integer EXT_RenegotiationInfo;
    protected static final Integer EXT_SessionTicket;
    private ByteQueue alertQueue;
    protected boolean allowCertificateStatus;
    private volatile boolean appDataReady;
    private volatile boolean appDataSplitEnabled;
    private volatile int appDataSplitMode;
    private ByteQueue applicationDataQueue;
    protected boolean blocking;
    protected Hashtable clientExtensions;
    private volatile boolean closed;
    protected short connection_state;
    protected boolean expectSessionTicket;
    private byte[] expected_verify_data;
    private volatile boolean failedWithError;
    private ByteQueue handshakeQueue;
    protected ByteQueueInputStream inputBuffers;
    protected int[] offeredCipherSuites;
    protected short[] offeredCompressionMethods;
    protected ByteQueueOutputStream outputBuffer;
    protected Certificate peerCertificate;
    protected boolean receivedChangeCipherSpec;
    RecordStream recordStream;
    protected boolean resumedSession;
    protected SecureRandom secureRandom;
    protected boolean secure_renegotiation;
    protected SecurityParameters securityParameters;
    protected Hashtable serverExtensions;
    protected SessionParameters sessionParameters;
    private TlsInputStream tlsInputStream;
    private TlsOutputStream tlsOutputStream;
    protected TlsSession tlsSession;
    
    static {
        EXT_RenegotiationInfo = Integers.valueOf(65281);
        EXT_SessionTicket = Integers.valueOf(35);
    }
    
    public TlsProtocol(final InputStream inputStream, final OutputStream outputStream, final SecureRandom secureRandom) {
        this.applicationDataQueue = new ByteQueue(0);
        this.alertQueue = new ByteQueue(2);
        this.handshakeQueue = new ByteQueue(0);
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.appDataSplitEnabled = true;
        this.appDataSplitMode = 0;
        this.expected_verify_data = null;
        this.tlsSession = null;
        this.sessionParameters = null;
        this.securityParameters = null;
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.connection_state = 0;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
        this.blocking = true;
        this.recordStream = new RecordStream(this, inputStream, outputStream);
        this.secureRandom = secureRandom;
    }
    
    public TlsProtocol(final SecureRandom secureRandom) {
        this.applicationDataQueue = new ByteQueue(0);
        this.alertQueue = new ByteQueue(2);
        this.handshakeQueue = new ByteQueue(0);
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.appDataSplitEnabled = true;
        this.appDataSplitMode = 0;
        this.expected_verify_data = null;
        this.tlsSession = null;
        this.sessionParameters = null;
        this.securityParameters = null;
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.connection_state = 0;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
        this.blocking = false;
        this.inputBuffers = new ByteQueueInputStream();
        this.outputBuffer = new ByteQueueOutputStream();
        this.recordStream = new RecordStream(this, this.inputBuffers, this.outputBuffer);
        this.secureRandom = secureRandom;
    }
    
    protected static void assertEmpty(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (byteArrayInputStream.available() <= 0) {
            return;
        }
        throw new TlsFatalAlert((short)50);
    }
    
    protected static byte[] createRandomBlock(final boolean b, final RandomGenerator randomGenerator) {
        final byte[] array = new byte[32];
        randomGenerator.nextBytes(array);
        if (b) {
            TlsUtils.writeGMTUnixTime(array, 0);
        }
        return array;
    }
    
    protected static byte[] createRenegotiationInfo(final byte[] array) throws IOException {
        return TlsUtils.encodeOpaque8(array);
    }
    
    protected static void establishMasterSecret(final TlsContext tlsContext, TlsKeyExchange generatePremasterSecret) throws IOException {
        generatePremasterSecret = (TlsKeyExchange)(Object)generatePremasterSecret.generatePremasterSecret();
        try {
            tlsContext.getSecurityParameters().masterSecret = TlsUtils.calculateMasterSecret(tlsContext, (byte[])(Object)generatePremasterSecret);
        }
        finally {
            if (generatePremasterSecret != null) {
                Arrays.fill((byte[])(Object)generatePremasterSecret, (byte)0);
            }
        }
    }
    
    protected static byte[] getCurrentPRFHash(final TlsContext tlsContext, final TlsHandshakeHash tlsHandshakeHash, final byte[] array) {
        final Digest forkPRFHash = tlsHandshakeHash.forkPRFHash();
        if (array != null && TlsUtils.isSSL(tlsContext)) {
            forkPRFHash.update(array, 0, array.length);
        }
        final byte[] array2 = new byte[forkPRFHash.getDigestSize()];
        forkPRFHash.doFinal(array2, 0);
        return array2;
    }
    
    protected static int getPRFAlgorithm(final TlsContext tlsContext, final int n) throws IOException {
        final boolean tlSv12 = TlsUtils.isTLSv12(tlsContext);
        Label_0824: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            Label_0808: {
                                switch (n) {
                                    default: {
                                        switch (n) {
                                            default: {
                                                Label_0800: {
                                                    switch (n) {
                                                        default: {
                                                            switch (n) {
                                                                default: {
                                                                    switch (n) {
                                                                        default: {
                                                                            switch (n) {
                                                                                default: {
                                                                                    switch (n) {
                                                                                        default: {
                                                                                            switch (n) {
                                                                                                default: {
                                                                                                    switch (n) {
                                                                                                        default: {
                                                                                                            if (tlSv12) {
                                                                                                                return 1;
                                                                                                            }
                                                                                                            return 0;
                                                                                                        }
                                                                                                        case 65296:
                                                                                                        case 65297:
                                                                                                        case 65298:
                                                                                                        case 65299:
                                                                                                        case 65300:
                                                                                                        case 65301: {
                                                                                                            break Label_0824;
                                                                                                        }
                                                                                                    }
                                                                                                    break;
                                                                                                }
                                                                                                case 65280:
                                                                                                case 65281:
                                                                                                case 65282:
                                                                                                case 65283:
                                                                                                case 65284:
                                                                                                case 65285: {
                                                                                                    break Label_0824;
                                                                                                }
                                                                                            }
                                                                                            break;
                                                                                        }
                                                                                        case 52392:
                                                                                        case 52393:
                                                                                        case 52394:
                                                                                        case 52395:
                                                                                        case 52396:
                                                                                        case 52397:
                                                                                        case 52398: {
                                                                                            break Label_0824;
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                                case 49307: {
                                                                                    break Label_0800;
                                                                                }
                                                                                case 49308:
                                                                                case 49309:
                                                                                case 49310:
                                                                                case 49311:
                                                                                case 49312:
                                                                                case 49313:
                                                                                case 49314:
                                                                                case 49315:
                                                                                case 49316:
                                                                                case 49317:
                                                                                case 49318:
                                                                                case 49319:
                                                                                case 49320:
                                                                                case 49321:
                                                                                case 49322:
                                                                                case 49323:
                                                                                case 49324:
                                                                                case 49325:
                                                                                case 49326:
                                                                                case 49327: {
                                                                                    break Label_0824;
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                        case 49188:
                                                                        case 49190:
                                                                        case 49192:
                                                                        case 49194:
                                                                        case 49196:
                                                                        case 49198:
                                                                        case 49200:
                                                                        case 49202: {
                                                                            break Label_0808;
                                                                        }
                                                                        case 49187:
                                                                        case 49189:
                                                                        case 49191:
                                                                        case 49193:
                                                                        case 49195:
                                                                        case 49197:
                                                                        case 49199:
                                                                        case 49201: {
                                                                            break Label_0824;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 185: {
                                                                    break Label_0800;
                                                                }
                                                                case 186:
                                                                case 187:
                                                                case 188:
                                                                case 189:
                                                                case 190:
                                                                case 191:
                                                                case 192:
                                                                case 193:
                                                                case 194:
                                                                case 195:
                                                                case 196:
                                                                case 197: {
                                                                    break Label_0824;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 175:
                                                        case 177:
                                                        case 179:
                                                        case 181:
                                                        case 183:
                                                        case 49208:
                                                        case 49211:
                                                        case 49301:
                                                        case 49303:
                                                        case 49305: {
                                                            if (tlSv12) {
                                                                return 2;
                                                            }
                                                            return 0;
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                            case 49267:
                                            case 49269:
                                            case 49271:
                                            case 49273:
                                            case 49275:
                                            case 49277:
                                            case 49279:
                                            case 49281:
                                            case 49283:
                                            case 49285:
                                            case 49287:
                                            case 49289:
                                            case 49291:
                                            case 49293:
                                            case 49295:
                                            case 49297:
                                            case 49299: {
                                                break Label_0808;
                                            }
                                            case 49266:
                                            case 49268:
                                            case 49270:
                                            case 49272:
                                            case 49274:
                                            case 49276:
                                            case 49278:
                                            case 49280:
                                            case 49282:
                                            case 49284:
                                            case 49286:
                                            case 49288:
                                            case 49290:
                                            case 49292:
                                            case 49294:
                                            case 49296:
                                            case 49298: {
                                                break Label_0824;
                                            }
                                        }
                                        break;
                                    }
                                    case 157:
                                    case 159:
                                    case 161:
                                    case 163:
                                    case 165:
                                    case 167:
                                    case 169:
                                    case 171:
                                    case 173: {
                                        if (tlSv12) {
                                            return 2;
                                        }
                                        throw new TlsFatalAlert((short)47);
                                    }
                                    case 156:
                                    case 158:
                                    case 160:
                                    case 162:
                                    case 164:
                                    case 166:
                                    case 168:
                                    case 170:
                                    case 172: {
                                        break Label_0824;
                                    }
                                }
                            }
                            break;
                        }
                        case 103:
                        case 104:
                        case 105:
                        case 106:
                        case 107:
                        case 108:
                        case 109: {
                            break Label_0824;
                        }
                    }
                    break;
                }
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64: {
                    if (tlSv12) {
                        return 1;
                    }
                    throw new TlsFatalAlert((short)47);
                }
            }
        }
    }
    
    private void processAlertQueue() throws IOException {
        while (this.alertQueue.available() >= 2) {
            final byte[] removeData = this.alertQueue.removeData(2, 0);
            this.handleAlertMessage(removeData[0], removeData[1]);
        }
    }
    
    private void processApplicationDataQueue() {
    }
    
    private void processChangeCipherSpec(final byte[] array, final int n, final int n2) throws IOException {
        for (int i = 0; i < n2; ++i) {
            if (TlsUtils.readUint8(array, n + i) != 1) {
                throw new TlsFatalAlert((short)50);
            }
            if (this.receivedChangeCipherSpec || this.alertQueue.available() > 0 || this.handshakeQueue.available() > 0) {
                throw new TlsFatalAlert((short)10);
            }
            this.recordStream.receivedReadCipherSpec();
            this.receivedChangeCipherSpec = true;
            this.handleChangeCipherSpecMessage();
        }
    }
    
    private void processHandshakeQueue(final ByteQueue byteQueue) throws IOException {
        while (byteQueue.available() >= 4) {
            final byte[] array = new byte[4];
            boolean b = false;
            byteQueue.read(array, 0, 4, 0);
            final short uint8 = TlsUtils.readUint8(array, 0);
            final int uint9 = TlsUtils.readUint24(array, 1);
            final int n = uint9 + 4;
            if (byteQueue.available() < n) {
                return;
            }
            if (this.connection_state == 16 || uint8 == 20) {
                b = true;
            }
            this.checkReceivedChangeCipherSpec(b);
            if (uint8 != 0) {
                if (uint8 == 20) {
                    final TlsContext context = this.getContext();
                    if (this.expected_verify_data == null && context.getSecurityParameters().getMasterSecret() != null) {
                        this.expected_verify_data = this.createVerifyData(context.isServer() ^ true);
                    }
                }
                byteQueue.copyTo(this.recordStream.getHandshakeHashUpdater(), n);
            }
            byteQueue.removeData(4);
            this.handleHandshakeMessage(uint8, byteQueue.readFrom(uint9));
        }
    }
    
    protected static Hashtable readExtensions(ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (byteArrayInputStream.available() < 1) {
            return null;
        }
        final byte[] opaque16 = TlsUtils.readOpaque16(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        byteArrayInputStream = new ByteArrayInputStream(opaque16);
        final Hashtable<Integer, byte[]> hashtable = new Hashtable<Integer, byte[]>();
        while (byteArrayInputStream.available() > 0) {
            if (hashtable.put(Integers.valueOf(TlsUtils.readUint16(byteArrayInputStream)), TlsUtils.readOpaque16(byteArrayInputStream)) == null) {
                continue;
            }
            throw new TlsFatalAlert((short)47);
        }
        return hashtable;
    }
    
    protected static Vector readSupplementalDataMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        final byte[] opaque24 = TlsUtils.readOpaque24(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        byteArrayInputStream = new ByteArrayInputStream(opaque24);
        final Vector<SupplementalDataEntry> vector = new Vector<SupplementalDataEntry>();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement(new SupplementalDataEntry(TlsUtils.readUint16(byteArrayInputStream), TlsUtils.readOpaque16(byteArrayInputStream)));
        }
        return vector;
    }
    
    protected static void writeExtensions(final OutputStream outputStream, final Hashtable hashtable) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeSelectedExtensions(byteArrayOutputStream, hashtable, true);
        writeSelectedExtensions(byteArrayOutputStream, hashtable, false);
        TlsUtils.writeOpaque16(byteArrayOutputStream.toByteArray(), outputStream);
    }
    
    protected static void writeSelectedExtensions(final OutputStream outputStream, final Hashtable hashtable, final boolean b) throws IOException {
        final Enumeration<Integer> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final Integer n = keys.nextElement();
            final int intValue = n;
            final byte[] array = (Object)hashtable.get(n);
            if (b == (array.length == 0)) {
                TlsUtils.checkUint16(intValue);
                TlsUtils.writeUint16(intValue, outputStream);
                TlsUtils.writeOpaque16(array, outputStream);
            }
        }
    }
    
    protected static void writeSupplementalData(final OutputStream outputStream, final Vector vector) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < vector.size(); ++i) {
            final SupplementalDataEntry supplementalDataEntry = vector.elementAt(i);
            final int dataType = supplementalDataEntry.getDataType();
            TlsUtils.checkUint16(dataType);
            TlsUtils.writeUint16(dataType, byteArrayOutputStream);
            TlsUtils.writeOpaque16(supplementalDataEntry.getData(), byteArrayOutputStream);
        }
        TlsUtils.writeOpaque24(byteArrayOutputStream.toByteArray(), outputStream);
    }
    
    protected int applicationDataAvailable() {
        return this.applicationDataQueue.available();
    }
    
    protected void applyMaxFragmentLengthExtension() throws IOException {
        if (this.securityParameters.maxFragmentLength < 0) {
            return;
        }
        if (MaxFragmentLength.isValid(this.securityParameters.maxFragmentLength)) {
            this.recordStream.setPlaintextLimit(1 << this.securityParameters.maxFragmentLength + 8);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected void blockForHandshake() throws IOException {
        if (this.blocking) {
            while (this.connection_state != 16) {
                if (this.closed) {
                    throw new TlsFatalAlert((short)80);
                }
                this.safeReadRecord();
            }
        }
    }
    
    protected void checkReceivedChangeCipherSpec(final boolean b) throws IOException {
        if (b == this.receivedChangeCipherSpec) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    protected void cleanupHandshake() {
        final byte[] expected_verify_data = this.expected_verify_data;
        if (expected_verify_data != null) {
            Arrays.fill(expected_verify_data, (byte)0);
            this.expected_verify_data = null;
        }
        this.securityParameters.clear();
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
    }
    
    public void close() throws IOException {
        this.handleClose(true);
    }
    
    public void closeInput() throws IOException {
        if (this.blocking) {
            throw new IllegalStateException("Cannot use closeInput() in blocking mode!");
        }
        if (this.closed) {
            return;
        }
        if (this.inputBuffers.available() > 0) {
            throw new EOFException();
        }
        if (!this.appDataReady) {
            throw new TlsFatalAlert((short)40);
        }
        throw new TlsNoCloseNotifyException();
    }
    
    protected void completeHandshake() throws IOException {
        try {
            this.connection_state = 16;
            this.alertQueue.shrink();
            this.handshakeQueue.shrink();
            this.recordStream.finaliseHandshake();
            this.appDataSplitEnabled = (TlsUtils.isTLSv11(this.getContext()) ^ true);
            if (!this.appDataReady) {
                this.appDataReady = true;
                if (this.blocking) {
                    this.tlsInputStream = new TlsInputStream(this);
                    this.tlsOutputStream = new TlsOutputStream(this);
                }
            }
            if (this.tlsSession != null) {
                if (this.sessionParameters == null) {
                    this.sessionParameters = new SessionParameters.Builder().setCipherSuite(this.securityParameters.getCipherSuite()).setCompressionAlgorithm(this.securityParameters.getCompressionAlgorithm()).setMasterSecret(this.securityParameters.getMasterSecret()).setPeerCertificate(this.peerCertificate).setPSKIdentity(this.securityParameters.getPSKIdentity()).setSRPIdentity(this.securityParameters.getSRPIdentity()).setServerExtensions(this.serverExtensions).build();
                    this.tlsSession = new TlsSessionImpl(this.tlsSession.getSessionID(), this.sessionParameters);
                }
                this.getContextAdmin().setResumableSession(this.tlsSession);
            }
            this.getPeer().notifyHandshakeComplete();
        }
        finally {
            this.cleanupHandshake();
        }
    }
    
    protected byte[] createVerifyData(final boolean b) {
        final TlsContext context = this.getContext();
        String s;
        if (b) {
            s = "server finished";
        }
        else {
            s = "client finished";
        }
        byte[] array;
        if (b) {
            array = TlsUtils.SSL_SERVER;
        }
        else {
            array = TlsUtils.SSL_CLIENT;
        }
        return TlsUtils.calculateVerifyData(context, s, getCurrentPRFHash(context, this.recordStream.getHandshakeHash(), array));
    }
    
    protected void flush() throws IOException {
        this.recordStream.flush();
    }
    
    public int getAvailableInputBytes() {
        if (!this.blocking) {
            return this.applicationDataAvailable();
        }
        throw new IllegalStateException("Cannot use getAvailableInputBytes() in blocking mode! Use getInputStream().available() instead.");
    }
    
    public int getAvailableOutputBytes() {
        if (!this.blocking) {
            return this.outputBuffer.getBuffer().available();
        }
        throw new IllegalStateException("Cannot use getAvailableOutputBytes() in blocking mode! Use getOutputStream() instead.");
    }
    
    protected abstract TlsContext getContext();
    
    abstract AbstractTlsContext getContextAdmin();
    
    public InputStream getInputStream() {
        if (this.blocking) {
            return this.tlsInputStream;
        }
        throw new IllegalStateException("Cannot use InputStream in non-blocking mode! Use offerInput() instead.");
    }
    
    public OutputStream getOutputStream() {
        if (this.blocking) {
            return this.tlsOutputStream;
        }
        throw new IllegalStateException("Cannot use OutputStream in non-blocking mode! Use offerOutput() instead.");
    }
    
    protected abstract TlsPeer getPeer();
    
    protected void handleAlertMessage(final short n, final short n2) throws IOException {
        this.getPeer().notifyAlertReceived(n, n2);
        if (n == 1) {
            this.handleAlertWarningMessage(n2);
            return;
        }
        this.handleFailure();
        throw new TlsFatalAlertReceived(n2);
    }
    
    protected void handleAlertWarningMessage(final short n) throws IOException {
        if (n != 0) {
            return;
        }
        if (this.appDataReady) {
            this.handleClose(false);
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
    
    protected void handleChangeCipherSpecMessage() throws IOException {
    }
    
    protected void handleClose(final boolean b) throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (b && !this.appDataReady) {
                this.raiseAlertWarning((short)90, "User canceled handshake");
            }
            this.raiseAlertWarning((short)0, "Connection closed");
            this.recordStream.safeClose();
            if (!this.appDataReady) {
                this.cleanupHandshake();
            }
        }
    }
    
    protected void handleException(final short n, final String s, final Throwable t) throws IOException {
        if (!this.closed) {
            this.raiseAlertFatal(n, s, t);
            this.handleFailure();
        }
    }
    
    protected void handleFailure() {
        this.closed = true;
        this.failedWithError = true;
        this.invalidateSession();
        this.recordStream.safeClose();
        if (!this.appDataReady) {
            this.cleanupHandshake();
        }
    }
    
    protected abstract void handleHandshakeMessage(final short p0, final ByteArrayInputStream p1) throws IOException;
    
    protected void invalidateSession() {
        final SessionParameters sessionParameters = this.sessionParameters;
        if (sessionParameters != null) {
            sessionParameters.clear();
            this.sessionParameters = null;
        }
        final TlsSession tlsSession = this.tlsSession;
        if (tlsSession != null) {
            tlsSession.invalidate();
            this.tlsSession = null;
        }
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    public void offerInput(byte[] array) throws IOException {
        if (this.blocking) {
            throw new IllegalStateException("Cannot use offerInput() in blocking mode! Use getInputStream() instead.");
        }
        if (!this.closed) {
            this.inputBuffers.addBytes(array);
            while (this.inputBuffers.available() >= 5) {
                array = new byte[5];
                this.inputBuffers.peek(array);
                if (this.inputBuffers.available() < TlsUtils.readUint16(array, 3) + 5) {
                    this.safeCheckRecordHeader(array);
                    return;
                }
                this.safeReadRecord();
                if (!this.closed) {
                    continue;
                }
                if (this.connection_state == 16) {
                    return;
                }
                throw new TlsFatalAlert((short)80);
            }
            return;
        }
        throw new IOException("Connection is closed, cannot accept any more input");
    }
    
    public void offerOutput(final byte[] array, final int n, final int n2) throws IOException {
        if (this.blocking) {
            throw new IllegalStateException("Cannot use offerOutput() in blocking mode! Use getOutputStream() instead.");
        }
        if (this.appDataReady) {
            this.writeData(array, n, n2);
            return;
        }
        throw new IOException("Application data cannot be sent until the handshake is complete!");
    }
    
    protected void processFinishedMessage(final ByteArrayInputStream byteArrayInputStream) throws IOException {
        final byte[] expected_verify_data = this.expected_verify_data;
        if (expected_verify_data == null) {
            throw new TlsFatalAlert((short)80);
        }
        final byte[] fully = TlsUtils.readFully(expected_verify_data.length, byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        if (Arrays.constantTimeAreEqual(this.expected_verify_data, fully)) {
            return;
        }
        throw new TlsFatalAlert((short)51);
    }
    
    protected short processMaxFragmentLengthExtension(final Hashtable hashtable, final Hashtable hashtable2, final short n) throws IOException {
        final short maxFragmentLengthExtension = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable2);
        if (maxFragmentLengthExtension >= 0) {
            if (MaxFragmentLength.isValid(maxFragmentLengthExtension)) {
                if (this.resumedSession) {
                    return maxFragmentLengthExtension;
                }
                if (maxFragmentLengthExtension == TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable)) {
                    return maxFragmentLengthExtension;
                }
            }
            throw new TlsFatalAlert(n);
        }
        return maxFragmentLengthExtension;
    }
    
    protected void processRecord(final short n, final byte[] array, final int n2, final int n3) throws IOException {
        switch (n) {
            default: {
                throw new TlsFatalAlert((short)80);
            }
            case 23: {
                if (this.appDataReady) {
                    this.applicationDataQueue.addData(array, n2, n3);
                    this.processApplicationDataQueue();
                    return;
                }
                throw new TlsFatalAlert((short)10);
            }
            case 22: {
                if (this.handshakeQueue.available() > 0) {
                    this.handshakeQueue.addData(array, n2, n3);
                    this.processHandshakeQueue(this.handshakeQueue);
                    return;
                }
                final ByteQueue byteQueue = new ByteQueue(array, n2, n3);
                this.processHandshakeQueue(byteQueue);
                final int available = byteQueue.available();
                if (available > 0) {
                    this.handshakeQueue.addData(array, n2 + n3 - available, available);
                    return;
                }
                break;
            }
            case 21: {
                this.alertQueue.addData(array, n2, n3);
                this.processAlertQueue();
            }
            case 20: {
                this.processChangeCipherSpec(array, n2, n3);
                break;
            }
        }
    }
    
    protected void raiseAlertFatal(final short n, final String s, final Throwable t) throws IOException {
        this.getPeer().notifyAlertRaised((short)2, n, s, t);
        final byte b = (byte)n;
        try {
            this.recordStream.writeRecord((short)21, new byte[] { 2, b }, 0, 2);
        }
        catch (Exception ex) {}
    }
    
    protected void raiseAlertWarning(final short n, final String s) throws IOException {
        this.getPeer().notifyAlertRaised((short)1, n, s, null);
        this.safeWriteRecord((short)21, new byte[] { 1, (byte)n }, 0, 2);
    }
    
    protected int readApplicationData(final byte[] array, final int n, int min) throws IOException {
        if (min < 1) {
            return 0;
        }
        while (this.applicationDataQueue.available() == 0) {
            if (this.closed) {
                if (this.failedWithError) {
                    throw new IOException("Cannot read application data on failed TLS connection");
                }
                if (this.appDataReady) {
                    return -1;
                }
                throw new IllegalStateException("Cannot read application data until initial handshake completed.");
            }
            else {
                this.safeReadRecord();
            }
        }
        min = Math.min(min, this.applicationDataQueue.available());
        this.applicationDataQueue.removeData(array, n, min, 0);
        return min;
    }
    
    public int readInput(final byte[] array, int applicationData, final int n) {
        if (!this.blocking) {
            try {
                applicationData = this.readApplicationData(array, applicationData, Math.min(n, this.applicationDataAvailable()));
                return applicationData;
            }
            catch (IOException ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        throw new IllegalStateException("Cannot use readInput() in blocking mode! Use getInputStream() instead.");
    }
    
    public int readOutput(final byte[] array, final int n, int min) {
        if (!this.blocking) {
            min = Math.min(this.getAvailableOutputBytes(), min);
            this.outputBuffer.getBuffer().removeData(array, n, min, 0);
            return min;
        }
        throw new IllegalStateException("Cannot use readOutput() in blocking mode! Use getOutputStream() instead.");
    }
    
    protected void refuseRenegotiation() throws IOException {
        if (!TlsUtils.isSSL(this.getContext())) {
            this.raiseAlertWarning((short)100, "Renegotiation not supported");
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
    
    protected void safeCheckRecordHeader(final byte[] array) throws IOException {
        try {
            this.recordStream.checkRecordHeader(array);
        }
        catch (RuntimeException ex) {
            this.handleException((short)80, "Failed to read record", ex);
            throw new TlsFatalAlert((short)80, ex);
        }
        catch (IOException ex2) {
            this.handleException((short)80, "Failed to read record", ex2);
            throw ex2;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.handleException(tlsFatalAlert.getAlertDescription(), "Failed to read record", tlsFatalAlert);
            throw tlsFatalAlert;
        }
    }
    
    protected void safeReadRecord() throws IOException {
        try {
            if (this.recordStream.readRecord()) {
                return;
            }
            if (!this.appDataReady) {
                throw new TlsFatalAlert((short)40);
            }
            this.handleFailure();
            throw new TlsNoCloseNotifyException();
        }
        catch (RuntimeException ex) {
            this.handleException((short)80, "Failed to read record", ex);
            throw new TlsFatalAlert((short)80, ex);
        }
        catch (IOException ex2) {
            this.handleException((short)80, "Failed to read record", ex2);
            throw ex2;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.handleException(tlsFatalAlert.getAlertDescription(), "Failed to read record", tlsFatalAlert);
            throw tlsFatalAlert;
        }
        catch (TlsFatalAlertReceived tlsFatalAlertReceived) {
            throw tlsFatalAlertReceived;
        }
    }
    
    protected void safeWriteRecord(final short n, final byte[] array, final int n2, final int n3) throws IOException {
        try {
            this.recordStream.writeRecord(n, array, n2, n3);
        }
        catch (RuntimeException ex) {
            this.handleException((short)80, "Failed to write record", ex);
            throw new TlsFatalAlert((short)80, ex);
        }
        catch (IOException ex2) {
            this.handleException((short)80, "Failed to write record", ex2);
            throw ex2;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.handleException(tlsFatalAlert.getAlertDescription(), "Failed to write record", tlsFatalAlert);
            throw tlsFatalAlert;
        }
    }
    
    protected void sendCertificateMessage(final Certificate certificate) throws IOException {
        Certificate empty_CHAIN = certificate;
        if (certificate == null) {
            empty_CHAIN = Certificate.EMPTY_CHAIN;
        }
        if (empty_CHAIN.isEmpty() && !this.getContext().isServer()) {
            final ProtocolVersion serverVersion = this.getContext().getServerVersion();
            if (serverVersion.isSSL()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(serverVersion.toString());
                sb.append(" client didn't provide credentials");
                this.raiseAlertWarning((short)41, sb.toString());
                return;
            }
        }
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)11);
        empty_CHAIN.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void sendChangeCipherSpecMessage() throws IOException {
        this.safeWriteRecord((short)20, new byte[] { 1 }, 0, 1);
        this.recordStream.sentWriteCipherSpec();
    }
    
    protected void sendFinishedMessage() throws IOException {
        final byte[] verifyData = this.createVerifyData(this.getContext().isServer());
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)20, verifyData.length);
        handshakeMessage.write(verifyData);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void sendSupplementalDataMessage(final Vector vector) throws IOException {
        final HandshakeMessage handshakeMessage = new HandshakeMessage((short)23);
        writeSupplementalData(handshakeMessage, vector);
        handshakeMessage.writeToRecordStream();
    }
    
    protected void setAppDataSplitMode(final int appDataSplitMode) {
        if (appDataSplitMode >= 0 && appDataSplitMode <= 2) {
            this.appDataSplitMode = appDataSplitMode;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal appDataSplitMode mode: ");
        sb.append(appDataSplitMode);
        throw new IllegalArgumentException(sb.toString());
    }
    
    protected void writeData(final byte[] array, int n, int i) throws IOException {
        if (!this.closed) {
            while (i > 0) {
                int n2 = n;
                int n3 = i;
                Label_0086: {
                    if (this.appDataSplitEnabled) {
                        final int appDataSplitMode = this.appDataSplitMode;
                        if (appDataSplitMode != 1) {
                            if (appDataSplitMode != 2) {
                                this.safeWriteRecord((short)23, array, n, 1);
                                n2 = n + 1;
                                n3 = i - 1;
                                break Label_0086;
                            }
                            this.appDataSplitEnabled = false;
                        }
                        this.safeWriteRecord((short)23, TlsUtils.EMPTY_BYTES, 0, 0);
                        n3 = i;
                        n2 = n;
                    }
                }
                n = n2;
                if ((i = n3) > 0) {
                    i = Math.min(n3, this.recordStream.getPlaintextLimit());
                    this.safeWriteRecord((short)23, array, n2, i);
                    n = n2 + i;
                    i = n3 - i;
                }
            }
            return;
        }
        throw new IOException("Cannot write application data on closed/failed TLS connection");
    }
    
    protected void writeHandshakeMessage(final byte[] array, final int n, final int n2) throws IOException {
        if (n2 >= 4) {
            if (TlsUtils.readUint8(array, n) != 0) {
                this.recordStream.getHandshakeHashUpdater().write(array, n, n2);
            }
            int n3 = 0;
            int min;
            do {
                min = Math.min(n2 - n3, this.recordStream.getPlaintextLimit());
                this.safeWriteRecord((short)22, array, n + n3, min);
            } while ((n3 += min) < n2);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    class HandshakeMessage extends ByteArrayOutputStream
    {
        HandshakeMessage(final TlsProtocol tlsProtocol, final short n) throws IOException {
            this(tlsProtocol, n, 60);
        }
        
        HandshakeMessage(final short n, final int n2) throws IOException {
            super(n2 + 4);
            TlsUtils.writeUint8(n, this);
            this.count += 3;
        }
        
        void writeToRecordStream() throws IOException {
            final int n = this.count - 4;
            TlsUtils.checkUint24(n);
            TlsUtils.writeUint24(n, this.buf, 1);
            TlsProtocol.this.writeHandshakeMessage(this.buf, 0, this.count);
            this.buf = null;
        }
    }
}
