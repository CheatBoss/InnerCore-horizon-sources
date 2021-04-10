package okhttp3.internal.tls;

import okhttp3.internal.platform.*;
import java.util.*;
import java.security.cert.*;
import javax.net.ssl.*;

public abstract class CertificateChainCleaner
{
    public static CertificateChainCleaner get(final X509TrustManager x509TrustManager) {
        return Platform.get().buildCertificateChainCleaner(x509TrustManager);
    }
    
    public abstract List<Certificate> clean(final List<Certificate> p0, final String p1) throws SSLPeerUnverifiedException;
}
