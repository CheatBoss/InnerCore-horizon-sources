package okhttp3.internal.tls;

import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;
import java.util.*;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner
{
    private final TrustRootIndex trustRootIndex;
    
    public BasicCertificateChainCleaner(final TrustRootIndex trustRootIndex) {
        this.trustRootIndex = trustRootIndex;
    }
    
    private boolean verifySignature(final X509Certificate x509Certificate, final X509Certificate x509Certificate2) {
        if (!x509Certificate.getIssuerDN().equals(x509Certificate2.getSubjectDN())) {
            return false;
        }
        try {
            x509Certificate.verify(x509Certificate2.getPublicKey());
            return true;
        }
        catch (GeneralSecurityException ex) {
            return false;
        }
    }
    
    @Override
    public List<Certificate> clean(final List<Certificate> list, final String s) throws SSLPeerUnverifiedException {
        final ArrayDeque<X509Certificate> arrayDeque = (ArrayDeque<X509Certificate>)new ArrayDeque<Certificate>(list);
        final ArrayList<X509Certificate> list2 = (ArrayList<X509Certificate>)new ArrayList<Certificate>();
        list2.add(arrayDeque.removeFirst());
        int i = 0;
        boolean b = false;
        while (i < 9) {
            final X509Certificate x509Certificate = list2.get(list2.size() - 1);
            final X509Certificate byIssuerAndSignature = this.trustRootIndex.findByIssuerAndSignature(x509Certificate);
            Label_0182: {
                if (byIssuerAndSignature != null) {
                    if (list2.size() > 1 || !x509Certificate.equals(byIssuerAndSignature)) {
                        list2.add(byIssuerAndSignature);
                    }
                    if (this.verifySignature(byIssuerAndSignature, byIssuerAndSignature)) {
                        return (List<Certificate>)list2;
                    }
                    b = true;
                }
                else {
                    final Iterator<X509Certificate> iterator = (Iterator<X509Certificate>)arrayDeque.iterator();
                    while (iterator.hasNext()) {
                        final X509Certificate x509Certificate2 = iterator.next();
                        if (this.verifySignature(x509Certificate, x509Certificate2)) {
                            iterator.remove();
                            list2.add(x509Certificate2);
                            break Label_0182;
                        }
                    }
                    if (b) {
                        return (List<Certificate>)list2;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to find a trusted cert that signed ");
                    sb.append(x509Certificate);
                    throw new SSLPeerUnverifiedException(sb.toString());
                }
            }
            ++i;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Certificate chain too long: ");
        sb2.append(list2);
        throw new SSLPeerUnverifiedException(sb2.toString());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof BasicCertificateChainCleaner && ((BasicCertificateChainCleaner)o).trustRootIndex.equals(this.trustRootIndex));
    }
    
    @Override
    public int hashCode() {
        return this.trustRootIndex.hashCode();
    }
}
