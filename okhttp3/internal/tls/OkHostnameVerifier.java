package okhttp3.internal.tls;

import java.security.cert.*;
import java.util.*;
import okhttp3.internal.*;
import javax.net.ssl.*;

public final class OkHostnameVerifier implements HostnameVerifier
{
    public static final OkHostnameVerifier INSTANCE;
    
    static {
        INSTANCE = new OkHostnameVerifier();
    }
    
    private OkHostnameVerifier() {
    }
    
    public static List<String> allSubjectAltNames(final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        final List<String> subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
        final ArrayList list = new ArrayList<Object>(subjectAltNames.size() + subjectAltNames2.size());
        list.addAll((Collection<?>)subjectAltNames);
        list.addAll((Collection<?>)subjectAltNames2);
        return (List<String>)list;
    }
    
    private static List<String> getSubjectAltNames(final X509Certificate x509Certificate, final int n) {
        final ArrayList<String> list = new ArrayList<String>();
        try {
            final Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                return Collections.emptyList();
            }
            for (final List<Integer> list2 : subjectAlternativeNames) {
                if (list2 != null) {
                    if (list2.size() < 2) {
                        continue;
                    }
                    final Integer n2 = list2.get(0);
                    if (n2 == null) {
                        continue;
                    }
                    if (n2 != n) {
                        continue;
                    }
                    final String s = (String)list2.get(1);
                    if (s == null) {
                        continue;
                    }
                    list.add(s);
                }
            }
            return list;
        }
        catch (CertificateParsingException ex) {
            return Collections.emptyList();
        }
    }
    
    private boolean verifyHostname(String lowerCase, final X509Certificate x509Certificate) {
        lowerCase = lowerCase.toLowerCase(Locale.US);
        final Iterator<String> iterator = getSubjectAltNames(x509Certificate, 2).iterator();
        while (iterator.hasNext()) {
            if (this.verifyHostname(lowerCase, iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean verifyIpAddress(final String s, final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        for (int size = subjectAltNames.size(), i = 0; i < size; ++i) {
            if (s.equalsIgnoreCase(subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public boolean verify(final String s, final X509Certificate x509Certificate) {
        if (Util.verifyAsIpAddress(s)) {
            return this.verifyIpAddress(s, x509Certificate);
        }
        return this.verifyHostname(s, x509Certificate);
    }
    
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        try {
            return this.verify(s, (X509Certificate)sslSession.getPeerCertificates()[0]);
        }
        catch (SSLException ex) {
            return false;
        }
    }
    
    public boolean verifyHostname(String s, final String s2) {
        if (s != null && s.length() != 0 && !s.startsWith(".")) {
            if (s.endsWith("..")) {
                return false;
            }
            if (s2 != null && s2.length() != 0 && !s2.startsWith(".")) {
                if (s2.endsWith("..")) {
                    return false;
                }
                String string = s;
                if (!s.endsWith(".")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append('.');
                    string = sb.toString();
                }
                s = s2;
                if (!s2.endsWith(".")) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s2);
                    sb2.append('.');
                    s = sb2.toString();
                }
                s = s.toLowerCase(Locale.US);
                if (!s.contains("*")) {
                    return string.equals(s);
                }
                if (s.startsWith("*.")) {
                    if (s.indexOf(42, 1) != -1) {
                        return false;
                    }
                    if (string.length() < s.length()) {
                        return false;
                    }
                    if ("*.".equals(s)) {
                        return false;
                    }
                    s = s.substring(1);
                    if (!string.endsWith(s)) {
                        return false;
                    }
                    final int n = string.length() - s.length();
                    return n <= 0 || string.lastIndexOf(46, n - 1) == -1;
                }
            }
        }
        return false;
    }
}
