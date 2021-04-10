package org.spongycastle.crypto.tls;

import java.util.*;
import java.io.*;
import org.spongycastle.util.*;

public abstract class AbstractTlsServer extends AbstractTlsPeer implements TlsServer
{
    protected TlsCipherFactory cipherFactory;
    protected short[] clientECPointFormats;
    protected Hashtable clientExtensions;
    protected ProtocolVersion clientVersion;
    protected TlsServerContext context;
    protected boolean eccCipherSuitesOffered;
    protected boolean encryptThenMACOffered;
    protected short maxFragmentLengthOffered;
    protected int[] namedCurves;
    protected int[] offeredCipherSuites;
    protected short[] offeredCompressionMethods;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;
    protected short[] serverECPointFormats;
    protected Hashtable serverExtensions;
    protected ProtocolVersion serverVersion;
    protected Vector supportedSignatureAlgorithms;
    protected boolean truncatedHMacOffered;
    
    public AbstractTlsServer() {
        this(new DefaultTlsCipherFactory());
    }
    
    public AbstractTlsServer(final TlsCipherFactory cipherFactory) {
        this.cipherFactory = cipherFactory;
    }
    
    protected boolean allowEncryptThenMAC() {
        return true;
    }
    
    protected boolean allowTruncatedHMac() {
        return false;
    }
    
    protected Hashtable checkServerExtensions() {
        return this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
    }
    
    @Override
    public CertificateRequest getCertificateRequest() throws IOException {
        return null;
    }
    
    @Override
    public CertificateStatus getCertificateStatus() throws IOException {
        return null;
    }
    
    @Override
    public TlsCipher getCipher() throws IOException {
        return this.cipherFactory.createCipher(this.context, TlsUtils.getEncryptionAlgorithm(this.selectedCipherSuite), TlsUtils.getMACAlgorithm(this.selectedCipherSuite));
    }
    
    protected abstract int[] getCipherSuites();
    
    @Override
    public TlsCompression getCompression() throws IOException {
        if (this.selectedCompressionMethod == 0) {
            return new TlsNullCompression();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected short[] getCompressionMethods() {
        return new short[] { 0 };
    }
    
    protected ProtocolVersion getMaximumVersion() {
        return ProtocolVersion.TLSv11;
    }
    
    protected ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }
    
    @Override
    public NewSessionTicket getNewSessionTicket() throws IOException {
        return new NewSessionTicket(0L, TlsUtils.EMPTY_BYTES);
    }
    
    @Override
    public int getSelectedCipherSuite() throws IOException {
        final Vector usableSignatureAlgorithms = TlsUtils.getUsableSignatureAlgorithms(this.supportedSignatureAlgorithms);
        final boolean supportsClientECCCapabilities = this.supportsClientECCCapabilities(this.namedCurves, this.clientECPointFormats);
        final int[] cipherSuites = this.getCipherSuites();
        for (int i = 0; i < cipherSuites.length; ++i) {
            final int selectedCipherSuite = cipherSuites[i];
            if (Arrays.contains(this.offeredCipherSuites, selectedCipherSuite) && (supportsClientECCCapabilities || !TlsECCUtils.isECCCipherSuite(selectedCipherSuite)) && TlsUtils.isValidCipherSuiteForVersion(selectedCipherSuite, this.serverVersion) && TlsUtils.isValidCipherSuiteForSignatureAlgorithms(selectedCipherSuite, usableSignatureAlgorithms)) {
                return this.selectedCipherSuite = selectedCipherSuite;
            }
        }
        throw new TlsFatalAlert((short)40);
    }
    
    @Override
    public short getSelectedCompressionMethod() throws IOException {
        final short[] compressionMethods = this.getCompressionMethods();
        for (int i = 0; i < compressionMethods.length; ++i) {
            if (Arrays.contains(this.offeredCompressionMethods, compressionMethods[i])) {
                return this.selectedCompressionMethod = compressionMethods[i];
            }
        }
        throw new TlsFatalAlert((short)40);
    }
    
    @Override
    public Hashtable getServerExtensions() throws IOException {
        if (this.encryptThenMACOffered && this.allowEncryptThenMAC() && TlsUtils.isBlockCipherSuite(this.selectedCipherSuite)) {
            TlsExtensionsUtils.addEncryptThenMACExtension(this.checkServerExtensions());
        }
        final short maxFragmentLengthOffered = this.maxFragmentLengthOffered;
        if (maxFragmentLengthOffered >= 0 && MaxFragmentLength.isValid(maxFragmentLengthOffered)) {
            TlsExtensionsUtils.addMaxFragmentLengthExtension(this.checkServerExtensions(), this.maxFragmentLengthOffered);
        }
        if (this.truncatedHMacOffered && this.allowTruncatedHMac()) {
            TlsExtensionsUtils.addTruncatedHMacExtension(this.checkServerExtensions());
        }
        if (this.clientECPointFormats != null && TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
            this.serverECPointFormats = new short[] { 0, 1, 2 };
            TlsECCUtils.addSupportedPointFormatsExtension(this.checkServerExtensions(), this.serverECPointFormats);
        }
        return this.serverExtensions;
    }
    
    @Override
    public Vector getServerSupplementalData() throws IOException {
        return null;
    }
    
    @Override
    public ProtocolVersion getServerVersion() throws IOException {
        if (this.getMinimumVersion().isEqualOrEarlierVersionOf(this.clientVersion)) {
            final ProtocolVersion maximumVersion = this.getMaximumVersion();
            if (this.clientVersion.isEqualOrEarlierVersionOf(maximumVersion)) {
                return this.serverVersion = this.clientVersion;
            }
            if (this.clientVersion.isLaterVersionOf(maximumVersion)) {
                return this.serverVersion = maximumVersion;
            }
        }
        throw new TlsFatalAlert((short)70);
    }
    
    @Override
    public void init(final TlsServerContext context) {
        this.context = context;
    }
    
    @Override
    public void notifyClientCertificate(final Certificate certificate) throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void notifyClientVersion(final ProtocolVersion clientVersion) throws IOException {
        this.clientVersion = clientVersion;
    }
    
    @Override
    public void notifyFallback(final boolean b) throws IOException {
        if (!b) {
            return;
        }
        if (!this.getMaximumVersion().isLaterVersionOf(this.clientVersion)) {
            return;
        }
        throw new TlsFatalAlert((short)86);
    }
    
    @Override
    public void notifyOfferedCipherSuites(final int[] offeredCipherSuites) throws IOException {
        this.offeredCipherSuites = offeredCipherSuites;
        this.eccCipherSuitesOffered = TlsECCUtils.containsECCCipherSuites(offeredCipherSuites);
    }
    
    @Override
    public void notifyOfferedCompressionMethods(final short[] offeredCompressionMethods) throws IOException {
        this.offeredCompressionMethods = offeredCompressionMethods;
    }
    
    @Override
    public void processClientExtensions(final Hashtable clientExtensions) throws IOException {
        this.clientExtensions = clientExtensions;
        if (clientExtensions != null) {
            this.encryptThenMACOffered = TlsExtensionsUtils.hasEncryptThenMACExtension(clientExtensions);
            final short maxFragmentLengthExtension = TlsExtensionsUtils.getMaxFragmentLengthExtension(clientExtensions);
            this.maxFragmentLengthOffered = maxFragmentLengthExtension;
            if (maxFragmentLengthExtension >= 0 && !MaxFragmentLength.isValid(maxFragmentLengthExtension)) {
                throw new TlsFatalAlert((short)47);
            }
            this.truncatedHMacOffered = TlsExtensionsUtils.hasTruncatedHMacExtension(clientExtensions);
            final Vector signatureAlgorithmsExtension = TlsUtils.getSignatureAlgorithmsExtension(clientExtensions);
            this.supportedSignatureAlgorithms = signatureAlgorithmsExtension;
            if (signatureAlgorithmsExtension != null && !TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.clientVersion)) {
                throw new TlsFatalAlert((short)47);
            }
            this.namedCurves = TlsECCUtils.getSupportedEllipticCurvesExtension(clientExtensions);
            this.clientECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(clientExtensions);
        }
    }
    
    @Override
    public void processClientSupplementalData(final Vector vector) throws IOException {
        if (vector == null) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    protected boolean supportsClientECCCapabilities(final int[] array, final short[] array2) {
        if (array == null) {
            return TlsECCUtils.hasAnySupportedNamedCurves();
        }
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i];
            if (NamedCurve.isValid(n) && (!NamedCurve.refersToASpecificNamedCurve(n) || TlsECCUtils.isSupportedNamedCurve(n))) {
                return true;
            }
        }
        return false;
    }
}
