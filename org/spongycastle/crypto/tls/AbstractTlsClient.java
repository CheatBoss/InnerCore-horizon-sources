package org.spongycastle.crypto.tls;

import java.io.*;
import java.util.*;

public abstract class AbstractTlsClient extends AbstractTlsPeer implements TlsClient
{
    protected TlsCipherFactory cipherFactory;
    protected short[] clientECPointFormats;
    protected TlsClientContext context;
    protected int[] namedCurves;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;
    protected short[] serverECPointFormats;
    protected Vector supportedSignatureAlgorithms;
    
    public AbstractTlsClient() {
        this(new DefaultTlsCipherFactory());
    }
    
    public AbstractTlsClient(final TlsCipherFactory cipherFactory) {
        this.cipherFactory = cipherFactory;
    }
    
    protected boolean allowUnexpectedServerExtension(final Integer n, final byte[] array) throws IOException {
        if (n != 10) {
            return false;
        }
        TlsECCUtils.readSupportedEllipticCurvesExtension(array);
        return true;
    }
    
    protected void checkForUnexpectedServerExtension(final Hashtable hashtable, final Integer n) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, n);
        if (extensionData == null) {
            return;
        }
        if (this.allowUnexpectedServerExtension(n, extensionData)) {
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    @Override
    public TlsCipher getCipher() throws IOException {
        return this.cipherFactory.createCipher(this.context, TlsUtils.getEncryptionAlgorithm(this.selectedCipherSuite), TlsUtils.getMACAlgorithm(this.selectedCipherSuite));
    }
    
    @Override
    public Hashtable getClientExtensions() throws IOException {
        final boolean signatureAlgorithmsExtensionAllowed = TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.context.getClientVersion());
        Hashtable ensureExtensionsInitialised = null;
        if (signatureAlgorithmsExtensionAllowed) {
            this.supportedSignatureAlgorithms = TlsUtils.getDefaultSupportedSignatureAlgorithms();
            ensureExtensionsInitialised = TlsExtensionsUtils.ensureExtensionsInitialised(null);
            TlsUtils.addSignatureAlgorithmsExtension(ensureExtensionsInitialised, this.supportedSignatureAlgorithms);
        }
        Hashtable ensureExtensionsInitialised2 = ensureExtensionsInitialised;
        if (TlsECCUtils.containsECCCipherSuites(this.getCipherSuites())) {
            this.namedCurves = new int[] { 23, 24 };
            this.clientECPointFormats = new short[] { 0, 1, 2 };
            ensureExtensionsInitialised2 = TlsExtensionsUtils.ensureExtensionsInitialised(ensureExtensionsInitialised);
            TlsECCUtils.addSupportedEllipticCurvesExtension(ensureExtensionsInitialised2, this.namedCurves);
            TlsECCUtils.addSupportedPointFormatsExtension(ensureExtensionsInitialised2, this.clientECPointFormats);
        }
        return ensureExtensionsInitialised2;
    }
    
    @Override
    public ProtocolVersion getClientHelloRecordLayerVersion() {
        return this.getClientVersion();
    }
    
    @Override
    public Vector getClientSupplementalData() throws IOException {
        return null;
    }
    
    @Override
    public ProtocolVersion getClientVersion() {
        return ProtocolVersion.TLSv12;
    }
    
    @Override
    public TlsCompression getCompression() throws IOException {
        if (this.selectedCompressionMethod == 0) {
            return new TlsNullCompression();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public short[] getCompressionMethods() {
        return new short[] { 0 };
    }
    
    public ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }
    
    @Override
    public TlsSession getSessionToResume() {
        return null;
    }
    
    @Override
    public void init(final TlsClientContext context) {
        this.context = context;
    }
    
    @Override
    public boolean isFallback() {
        return false;
    }
    
    @Override
    public void notifyNewSessionTicket(final NewSessionTicket newSessionTicket) throws IOException {
    }
    
    @Override
    public void notifySelectedCipherSuite(final int selectedCipherSuite) {
        this.selectedCipherSuite = selectedCipherSuite;
    }
    
    @Override
    public void notifySelectedCompressionMethod(final short selectedCompressionMethod) {
        this.selectedCompressionMethod = selectedCompressionMethod;
    }
    
    @Override
    public void notifyServerVersion(final ProtocolVersion protocolVersion) throws IOException {
        if (this.getMinimumVersion().isEqualOrEarlierVersionOf(protocolVersion)) {
            return;
        }
        throw new TlsFatalAlert((short)70);
    }
    
    @Override
    public void notifySessionID(final byte[] array) {
    }
    
    @Override
    public void processServerExtensions(final Hashtable hashtable) throws IOException {
        if (hashtable != null) {
            this.checkForUnexpectedServerExtension(hashtable, TlsUtils.EXT_signature_algorithms);
            this.checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_elliptic_curves);
            if (TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
                this.serverECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
            }
            else {
                this.checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_ec_point_formats);
            }
            this.checkForUnexpectedServerExtension(hashtable, TlsExtensionsUtils.EXT_padding);
        }
    }
    
    @Override
    public void processServerSupplementalData(final Vector vector) throws IOException {
        if (vector == null) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
}
