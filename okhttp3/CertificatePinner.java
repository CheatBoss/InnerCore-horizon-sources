package okhttp3;

import okhttp3.internal.tls.*;
import javax.annotation.*;
import java.security.cert.*;
import okio.*;
import javax.net.ssl.*;
import okhttp3.internal.*;
import java.util.*;

public final class CertificatePinner
{
    public static final CertificatePinner DEFAULT;
    @Nullable
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set<Pin> pins;
    
    static {
        DEFAULT = new Builder().build();
    }
    
    CertificatePinner(final Set<Pin> pins, @Nullable final CertificateChainCleaner certificateChainCleaner) {
        this.pins = pins;
        this.certificateChainCleaner = certificateChainCleaner;
    }
    
    public static String pin(final Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sha256/");
            sb.append(sha256((X509Certificate)certificate).base64());
            return sb.toString();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }
    
    static ByteString sha1(final X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha1();
    }
    
    static ByteString sha256(final X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha256();
    }
    
    public void check(final String s, final List<Certificate> list) throws SSLPeerUnverifiedException {
        final List<Pin> matchingPins = this.findMatchingPins(s);
        if (matchingPins.isEmpty()) {
            return;
        }
        final CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
        List<Certificate> clean = list;
        if (certificateChainCleaner != null) {
            clean = certificateChainCleaner.clean(list, s);
        }
        final int size = clean.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            final X509Certificate x509Certificate = clean.get(i);
            final int size2 = matchingPins.size();
            ByteString byteString2;
            ByteString byteString = byteString2 = null;
            for (int j = 0; j < size2; ++j) {
                final Pin pin = matchingPins.get(j);
                if (pin.hashAlgorithm.equals("sha256/")) {
                    ByteString sha256;
                    if ((sha256 = byteString) == null) {
                        sha256 = sha256(x509Certificate);
                    }
                    byteString = sha256;
                    if (pin.hash.equals(sha256)) {
                        return;
                    }
                }
                else {
                    if (!pin.hashAlgorithm.equals("sha1/")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unsupported hashAlgorithm: ");
                        sb.append(pin.hashAlgorithm);
                        throw new AssertionError((Object)sb.toString());
                    }
                    ByteString sha257;
                    if ((sha257 = byteString2) == null) {
                        sha257 = sha1(x509Certificate);
                    }
                    byteString2 = sha257;
                    if (pin.hash.equals(sha257)) {
                        return;
                    }
                }
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Certificate pinning failure!");
        sb2.append("\n  Peer certificate chain:");
        for (int size3 = clean.size(), k = 0; k < size3; ++k) {
            final X509Certificate x509Certificate2 = clean.get(k);
            sb2.append("\n    ");
            sb2.append(pin(x509Certificate2));
            sb2.append(": ");
            sb2.append(x509Certificate2.getSubjectDN().getName());
        }
        sb2.append("\n  Pinned certificates for ");
        sb2.append(s);
        sb2.append(":");
        for (int size4 = matchingPins.size(), l = n; l < size4; ++l) {
            final Pin pin2 = matchingPins.get(l);
            sb2.append("\n    ");
            sb2.append(pin2);
        }
        throw new SSLPeerUnverifiedException(sb2.toString());
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CertificatePinner) {
            final CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
            final CertificatePinner certificatePinner = (CertificatePinner)o;
            if (Util.equal(certificateChainCleaner, certificatePinner.certificateChainCleaner) && this.pins.equals(certificatePinner.pins)) {
                return true;
            }
        }
        return false;
    }
    
    List<Pin> findMatchingPins(final String s) {
        List<Pin> emptyList = Collections.emptyList();
        for (final Pin pin : this.pins) {
            if (pin.matches(s)) {
                List<Pin> list = emptyList;
                if (emptyList.isEmpty()) {
                    list = new ArrayList<Pin>();
                }
                list.add(pin);
                emptyList = list;
            }
        }
        return emptyList;
    }
    
    @Override
    public int hashCode() {
        final CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
        int hashCode;
        if (certificateChainCleaner != null) {
            hashCode = certificateChainCleaner.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode * 31 + this.pins.hashCode();
    }
    
    CertificatePinner withCertificateChainCleaner(@Nullable final CertificateChainCleaner certificateChainCleaner) {
        if (Util.equal(this.certificateChainCleaner, certificateChainCleaner)) {
            return this;
        }
        return new CertificatePinner(this.pins, certificateChainCleaner);
    }
    
    public static final class Builder
    {
        private final List<Pin> pins;
        
        public Builder() {
            this.pins = new ArrayList<Pin>();
        }
        
        public CertificatePinner build() {
            return new CertificatePinner(new LinkedHashSet<Pin>((Collection<? extends Pin>)this.pins), null);
        }
    }
    
    static final class Pin
    {
        final String canonicalHostname;
        final ByteString hash;
        final String hashAlgorithm;
        final String pattern;
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof Pin) {
                final String pattern = this.pattern;
                final Pin pin = (Pin)o;
                if (pattern.equals(pin.pattern) && this.hashAlgorithm.equals(pin.hashAlgorithm) && this.hash.equals(pin.hash)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.pattern.hashCode() + 527) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
        }
        
        boolean matches(final String s) {
            if (this.pattern.startsWith("*.")) {
                final int index = s.indexOf(46);
                if (s.length() - index - 1 == this.canonicalHostname.length()) {
                    final String canonicalHostname = this.canonicalHostname;
                    if (s.regionMatches(false, index + 1, canonicalHostname, 0, canonicalHostname.length())) {
                        return true;
                    }
                }
                return false;
            }
            return s.equals(this.canonicalHostname);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.hashAlgorithm);
            sb.append(this.hash.base64());
            return sb.toString();
        }
    }
}
