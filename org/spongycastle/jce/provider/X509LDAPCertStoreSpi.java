package org.spongycastle.jce.provider;

import org.spongycastle.jce.*;
import java.security.*;
import javax.security.auth.x500.*;
import javax.naming.directory.*;
import java.io.*;
import java.security.cert.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;
import javax.naming.*;

public class X509LDAPCertStoreSpi extends CertStoreSpi
{
    private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
    private static String REFERRALS_IGNORE = "ignore";
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private X509LDAPCertStoreParameters params;
    
    public X509LDAPCertStoreSpi(final CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        if (certStoreParameters instanceof X509LDAPCertStoreParameters) {
            this.params = (X509LDAPCertStoreParameters)certStoreParameters;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(X509LDAPCertStoreSpi.class.getName());
        sb.append(": parameter must be a ");
        sb.append(X509LDAPCertStoreParameters.class.getName());
        sb.append(" object\n");
        sb.append(certStoreParameters.toString());
        throw new InvalidAlgorithmParameterException(sb.toString());
    }
    
    private Set certSubjectSerialSearch(final X509CertSelector x509CertSelector, final String[] array, String searchForSerialNumberIn, String dn) throws CertStoreException {
        while (true) {
            final HashSet set = new HashSet();
            while (true) {
                String s;
                try {
                    if (x509CertSelector.getSubjectAsBytes() == null && x509CertSelector.getSubjectAsString() == null && x509CertSelector.getCertificate() == null) {
                        set.addAll(this.search(searchForSerialNumberIn, "*", array));
                        return set;
                    }
                    if (x509CertSelector.getCertificate() != null) {
                        final String name = x509CertSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
                        final String string = x509CertSelector.getCertificate().getSerialNumber().toString();
                        dn = this.parseDN(name, dn);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("*");
                        sb.append(dn);
                        sb.append("*");
                        set.addAll(this.search(searchForSerialNumberIn, sb.toString(), array));
                        if (string != null && this.params.getSearchForSerialNumberIn() != null) {
                            searchForSerialNumberIn = this.params.getSearchForSerialNumberIn();
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("*");
                            sb2.append(string);
                            sb2.append("*");
                            set.addAll(this.search(searchForSerialNumberIn, sb2.toString(), array));
                        }
                        return set;
                    }
                    if (x509CertSelector.getSubjectAsBytes() != null) {
                        s = new X500Principal(x509CertSelector.getSubjectAsBytes()).getName("RFC1779");
                    }
                    else {
                        s = x509CertSelector.getSubjectAsString();
                    }
                }
                catch (IOException ex) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("exception processing selector: ");
                    sb3.append(ex);
                    throw new CertStoreException(sb3.toString());
                }
                final String s2 = null;
                final String name = s;
                final String string = s2;
                continue;
            }
        }
    }
    
    private DirContext connectLDAP() throws NamingException {
        final Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", X509LDAPCertStoreSpi.LDAP_PROVIDER);
        properties.setProperty("java.naming.batchsize", "0");
        properties.setProperty("java.naming.provider.url", this.params.getLdapURL());
        properties.setProperty("java.naming.factory.url.pkgs", "com.sun.jndi.url");
        properties.setProperty("java.naming.referral", X509LDAPCertStoreSpi.REFERRALS_IGNORE);
        properties.setProperty("java.naming.security.authentication", "none");
        return new InitialDirContext(properties);
    }
    
    private Set getCACertificates(final X509CertSelector x509CertSelector) throws CertStoreException {
        final String[] array = { this.params.getCACertificateAttribute() };
        final Set certSubjectSerialSearch = this.certSubjectSerialSearch(x509CertSelector, array, this.params.getLdapCACertificateAttributeName(), this.params.getCACertificateSubjectAttributeName());
        if (certSubjectSerialSearch.isEmpty()) {
            certSubjectSerialSearch.addAll(this.search(null, "*", array));
        }
        return certSubjectSerialSearch;
    }
    
    private Set getCrossCertificates(final X509CertSelector x509CertSelector) throws CertStoreException {
        final String[] array = { this.params.getCrossCertificateAttribute() };
        final Set certSubjectSerialSearch = this.certSubjectSerialSearch(x509CertSelector, array, this.params.getLdapCrossCertificateAttributeName(), this.params.getCrossCertificateSubjectAttributeName());
        if (certSubjectSerialSearch.isEmpty()) {
            certSubjectSerialSearch.addAll(this.search(null, "*", array));
        }
        return certSubjectSerialSearch;
    }
    
    private Set getEndCertificates(final X509CertSelector x509CertSelector) throws CertStoreException {
        return this.certSubjectSerialSearch(x509CertSelector, new String[] { this.params.getUserCertificateAttribute() }, this.params.getLdapUserCertificateAttributeName(), this.params.getUserCertificateSubjectAttributeName());
    }
    
    private String parseDN(String s, String substring) {
        s = s.substring(s.toLowerCase().indexOf(substring.toLowerCase()) + substring.length());
        while (true) {
            Label_0038: {
                int n;
                if ((n = s.indexOf(44)) == -1) {
                    break Label_0038;
                }
                while (s.charAt(n - 1) == '\\') {
                    if ((n = s.indexOf(44, n + 1)) == -1) {
                        break Label_0038;
                    }
                }
                s = s.substring(0, n);
                substring = (s = s.substring(s.indexOf(61) + 1));
                if (substring.charAt(0) == ' ') {
                    s = substring.substring(1);
                }
                substring = s;
                if (s.startsWith("\"")) {
                    substring = s.substring(1);
                }
                s = substring;
                if (substring.endsWith("\"")) {
                    s = substring.substring(0, substring.length() - 1);
                }
                return s;
            }
            int n = s.length();
            continue;
        }
    }
    
    private Set search(String s, String string, final String[] ex) throws CertStoreException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("=");
        sb.append(string);
        string = sb.toString();
        Serializable s2 = null;
        final String s3 = null;
        if (s == null) {
            string = null;
        }
        final HashSet<Object> set = new HashSet<Object>();
        s = s3;
        try {
            try {
                final DirContext connectLDAP = this.connectLDAP();
                try {
                    s2 = new SearchControls();
                    ((SearchControls)s2).setSearchScope(2);
                    ((SearchControls)s2).setCountLimit(0L);
                    for (int i = 0; i < ex.length; ++i) {
                        final String[] returningAttributes = { ex[i] };
                        ((SearchControls)s2).setReturningAttributes(returningAttributes);
                        s = (String)new StringBuilder();
                        ((StringBuilder)s).append("(&(");
                        ((StringBuilder)s).append(string);
                        ((StringBuilder)s).append(")(");
                        ((StringBuilder)s).append(returningAttributes[0]);
                        ((StringBuilder)s).append("=*))");
                        s = ((StringBuilder)s).toString();
                        if (string == null) {
                            s = (String)new StringBuilder();
                            ((StringBuilder)s).append("(");
                            ((StringBuilder)s).append(returningAttributes[0]);
                            ((StringBuilder)s).append("=*)");
                            s = ((StringBuilder)s).toString();
                        }
                        s = (String)connectLDAP.search(this.params.getBaseDN(), s, (SearchControls)s2);
                        while (((Enumeration)s).hasMoreElements()) {
                            final NamingEnumeration<?> all = ((Attribute)((NamingEnumeration<SearchResult>)s).next().getAttributes().getAll().next()).getAll();
                            while (all.hasMore()) {
                                set.add(all.next());
                            }
                        }
                    }
                    if (connectLDAP != null) {
                        try {
                            connectLDAP.close();
                            return set;
                        }
                        catch (Exception ex2) {}
                    }
                    return set;
                }
                catch (Exception ex) {}
            }
            finally {
                final String s4 = s;
                final Serializable s5;
                s = (String)s5;
            }
        }
        catch (Exception ex) {
            final Serializable s5 = s2;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Error getting results from LDAP directory ");
        sb2.append(ex);
        throw new CertStoreException(sb2.toString());
        String s4 = null;
        if (s4 != null) {
            try {
                ((Context)s4).close();
            }
            catch (Exception ex3) {}
        }
        throw s;
    }
    
    @Override
    public Collection engineGetCRLs(final CRLSelector crlSelector) throws CertStoreException {
        final String[] array = { this.params.getCertificateRevocationListAttribute() };
        if (crlSelector instanceof X509CRLSelector) {
            final X509CRLSelector x509CRLSelector = (X509CRLSelector)crlSelector;
            final HashSet<CRL> set = new HashSet<CRL>();
            final String ldapCertificateRevocationListAttributeName = this.params.getLdapCertificateRevocationListAttributeName();
            final HashSet<byte[]> set2 = new HashSet<byte[]>();
            if (x509CRLSelector.getIssuerNames() != null) {
                for (final byte[] next : x509CRLSelector.getIssuerNames()) {
                    String s;
                    String name;
                    if (next instanceof String) {
                        s = this.params.getCertificateRevocationListIssuerAttributeName();
                        name = (String)(Object)next;
                    }
                    else {
                        s = this.params.getCertificateRevocationListIssuerAttributeName();
                        name = new X500Principal(next).getName("RFC1779");
                    }
                    final String dn = this.parseDN(name, s);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("*");
                    sb.append(dn);
                    sb.append("*");
                    set2.addAll((Collection<?>)this.search(ldapCertificateRevocationListAttributeName, sb.toString(), array));
                }
            }
            else {
                set2.addAll((Collection<?>)this.search(ldapCertificateRevocationListAttributeName, "*", array));
            }
            set2.addAll((Collection<?>)this.search(null, "*", array));
            final Iterator<Object> iterator2 = set2.iterator();
            try {
                final CertificateFactory instance = CertificateFactory.getInstance("X.509", "SC");
                while (iterator2.hasNext()) {
                    final CRL generateCRL = instance.generateCRL(new ByteArrayInputStream(iterator2.next()));
                    if (x509CRLSelector.match(generateCRL)) {
                        set.add(generateCRL);
                    }
                }
                return set;
            }
            catch (Exception ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("CRL cannot be constructed from LDAP result ");
                sb2.append(ex);
                throw new CertStoreException(sb2.toString());
            }
        }
        throw new CertStoreException("selector is not a X509CRLSelector");
    }
    
    @Override
    public Collection engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        if (certSelector instanceof X509CertSelector) {
            certSelector = certSelector;
            final HashSet<Certificate> set = new HashSet<Certificate>();
            final Set endCertificates = this.getEndCertificates((X509CertSelector)certSelector);
            endCertificates.addAll(this.getCACertificates((X509CertSelector)certSelector));
            endCertificates.addAll(this.getCrossCertificates((X509CertSelector)certSelector));
            final Iterator<byte[]> iterator = endCertificates.iterator();
            try {
                final CertificateFactory instance = CertificateFactory.getInstance("X.509", "SC");
                while (iterator.hasNext()) {
                    final byte[] array = iterator.next();
                    if (array != null) {
                        if (array.length == 0) {
                            continue;
                        }
                        final ArrayList<byte[]> list = new ArrayList<byte[]>();
                        list.add(array);
                        try {
                            final CertificatePair instance2 = CertificatePair.getInstance(new ASN1InputStream(array).readObject());
                            list.clear();
                            if (instance2.getForward() != null) {
                                list.add(instance2.getForward().getEncoded());
                            }
                            if (instance2.getReverse() != null) {
                                list.add(instance2.getReverse().getEncoded());
                            }
                        }
                        catch (IOException ex2) {}
                        catch (IllegalArgumentException ex3) {}
                        final Iterator<Object> iterator2 = list.iterator();
                        while (iterator2.hasNext()) {
                            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(iterator2.next());
                            try {
                                final Certificate generateCertificate = instance.generateCertificate(byteArrayInputStream);
                                if (!((X509CertSelector)certSelector).match(generateCertificate)) {
                                    continue;
                                }
                                set.add(generateCertificate);
                            }
                            catch (Exception ex4) {}
                        }
                    }
                }
                return set;
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("certificate cannot be constructed from LDAP result: ");
                sb.append(ex);
                throw new CertStoreException(sb.toString());
            }
        }
        throw new CertStoreException("selector is not a X509CertSelector");
    }
}
