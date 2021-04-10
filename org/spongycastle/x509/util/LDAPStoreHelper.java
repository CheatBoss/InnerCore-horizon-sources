package org.spongycastle.x509.util;

import org.spongycastle.jce.*;
import java.sql.*;
import javax.security.auth.x500.*;
import java.security.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.x509.*;
import org.spongycastle.jce.provider.*;
import java.security.cert.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import javax.naming.directory.*;
import javax.naming.*;

public class LDAPStoreHelper
{
    private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
    private static String REFERRALS_IGNORE = "ignore";
    private static final String SEARCH_SECURITY_LEVEL = "none";
    private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
    private static int cacheSize = 32;
    private static long lifeTime = 60000L;
    private Map cacheMap;
    private X509LDAPCertStoreParameters params;
    
    public LDAPStoreHelper(final X509LDAPCertStoreParameters params) {
        this.cacheMap = new HashMap(LDAPStoreHelper.cacheSize);
        this.params = params;
    }
    
    private void addToCache(final String s, final List list) {
        synchronized (this) {
            final Date date = new Date(System.currentTimeMillis());
            final ArrayList<Date> list2 = new ArrayList<Date>();
            list2.add(date);
            list2.add((Date)list);
            Map map;
            if (this.cacheMap.containsKey(s)) {
                map = this.cacheMap;
            }
            else {
                if (this.cacheMap.size() >= LDAPStoreHelper.cacheSize) {
                    final Iterator<Map.Entry<K, List>> iterator = this.cacheMap.entrySet().iterator();
                    long time = date.getTime();
                    Object key = null;
                    while (iterator.hasNext()) {
                        final Map.Entry<K, List> entry = iterator.next();
                        final long time2 = entry.getValue().get(0).getTime();
                        if (time2 < time) {
                            key = entry.getKey();
                            time = time2;
                        }
                    }
                    this.cacheMap.remove(key);
                }
                map = this.cacheMap;
            }
            map.put(s, list2);
        }
    }
    
    private List attrCertSubjectSerialSearch(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector, final String[] array, final String[] array2, final String[] array3) throws StoreException {
        final ArrayList list = new ArrayList();
        final HashSet<String> set = new HashSet<String>();
        final AttributeCertificateHolder holder = x509AttributeCertStoreSelector.getHolder();
        final String s = null;
        Principal[] array4 = null;
        Label_0085: {
            if (holder != null) {
                if (x509AttributeCertStoreSelector.getHolder().getSerialNumber() != null) {
                    set.add(x509AttributeCertStoreSelector.getHolder().getSerialNumber().toString());
                }
                if (x509AttributeCertStoreSelector.getHolder().getEntityNames() != null) {
                    array4 = x509AttributeCertStoreSelector.getHolder().getEntityNames();
                    break Label_0085;
                }
            }
            array4 = null;
        }
        Principal[] array5 = array4;
        if (x509AttributeCertStoreSelector.getAttributeCert() != null) {
            if (x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames() != null) {
                array4 = x509AttributeCertStoreSelector.getAttributeCert().getHolder().getEntityNames();
            }
            set.add(x509AttributeCertStoreSelector.getAttributeCert().getSerialNumber().toString());
            array5 = array4;
        }
        int i = 0;
        String s2 = s;
        if (array5 != null) {
            if (array5[0] instanceof X500Principal) {
                s2 = ((X500Principal)array5[0]).getName("RFC1779");
            }
            else {
                s2 = array5[0].getName();
            }
        }
        if (x509AttributeCertStoreSelector.getSerialNumber() != null) {
            set.add(x509AttributeCertStoreSelector.getSerialNumber().toString());
        }
        if (s2 != null) {
            while (i < array3.length) {
                final String dn = this.parseDN(s2, array3[i]);
                final StringBuilder sb = new StringBuilder();
                sb.append("*");
                sb.append(dn);
                sb.append("*");
                list.addAll(this.search(array2, sb.toString(), array));
                ++i;
            }
        }
        if (set.size() > 0 && this.params.getSearchForSerialNumberIn() != null) {
            final Iterator<Object> iterator = set.iterator();
            while (iterator.hasNext()) {
                list.addAll(this.search(this.splitString(this.params.getSearchForSerialNumberIn()), iterator.next(), array));
            }
        }
        if (set.size() == 0 && s2 == null) {
            list.addAll(this.search(array2, "*", array));
        }
        return list;
    }
    
    private List cRLIssuerSearch(final X509CRLStoreSelector x509CRLStoreSelector, final String[] array, final String[] array2, final String[] array3) throws StoreException {
        final ArrayList list = new ArrayList();
        final HashSet<Principal> set = new HashSet<Principal>();
        if (x509CRLStoreSelector.getIssuers() != null) {
            set.addAll((Collection<?>)x509CRLStoreSelector.getIssuers());
        }
        if (x509CRLStoreSelector.getCertificateChecking() != null) {
            set.add(this.getCertificateIssuer(x509CRLStoreSelector.getCertificateChecking()));
        }
        if (x509CRLStoreSelector.getAttrCertificateChecking() != null) {
            final Principal[] principals = x509CRLStoreSelector.getAttrCertificateChecking().getIssuer().getPrincipals();
            for (int i = 0; i < principals.length; ++i) {
                if (principals[i] instanceof X500Principal) {
                    set.add(principals[i]);
                }
            }
        }
        final Iterator<X500Principal> iterator = set.iterator();
        String s = null;
        while (iterator.hasNext()) {
            final String name = iterator.next().getName("RFC1779");
            int n = 0;
            while (true) {
                s = name;
                if (n >= array3.length) {
                    break;
                }
                final String dn = this.parseDN(name, array3[n]);
                final StringBuilder sb = new StringBuilder();
                sb.append("*");
                sb.append(dn);
                sb.append("*");
                list.addAll(this.search(array2, sb.toString(), array));
                ++n;
            }
        }
        if (s == null) {
            list.addAll(this.search(array2, "*", array));
        }
        return list;
    }
    
    private List certSubjectSerialSearch(final X509CertStoreSelector x509CertStoreSelector, final String[] array, final String[] array2, final String[] array3) throws StoreException {
        final ArrayList list = new ArrayList();
        String s = this.getSubjectAsString(x509CertStoreSelector);
        String s2;
        if (x509CertStoreSelector.getSerialNumber() != null) {
            s2 = x509CertStoreSelector.getSerialNumber().toString();
        }
        else {
            s2 = null;
        }
        if (x509CertStoreSelector.getCertificate() != null) {
            s = x509CertStoreSelector.getCertificate().getSubjectX500Principal().getName("RFC1779");
            s2 = x509CertStoreSelector.getCertificate().getSerialNumber().toString();
        }
        if (s != null) {
            for (int i = 0; i < array3.length; ++i) {
                final String dn = this.parseDN(s, array3[i]);
                final StringBuilder sb = new StringBuilder();
                sb.append("*");
                sb.append(dn);
                sb.append("*");
                list.addAll(this.search(array2, sb.toString(), array));
            }
        }
        if (s2 != null && this.params.getSearchForSerialNumberIn() != null) {
            list.addAll(this.search(this.splitString(this.params.getSearchForSerialNumberIn()), s2, array));
        }
        if (s2 == null && s == null) {
            list.addAll(this.search(array2, "*", array));
        }
        return list;
    }
    
    private DirContext connectLDAP() throws NamingException {
        final Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", LDAPStoreHelper.LDAP_PROVIDER);
        properties.setProperty("java.naming.batchsize", "0");
        properties.setProperty("java.naming.provider.url", this.params.getLdapURL());
        properties.setProperty("java.naming.factory.url.pkgs", "com.sun.jndi.url");
        properties.setProperty("java.naming.referral", LDAPStoreHelper.REFERRALS_IGNORE);
        properties.setProperty("java.naming.security.authentication", "none");
        return new InitialDirContext(properties);
    }
    
    private Set createAttributeCertificates(List iterator, final X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        final HashSet<X509AttributeCertificate> set = new HashSet<X509AttributeCertificate>();
        iterator = ((List)iterator).iterator();
        final X509AttrCertParser x509AttrCertParser = new X509AttrCertParser();
        while (iterator.hasNext()) {
            try {
                x509AttrCertParser.engineInit(new ByteArrayInputStream(iterator.next()));
                final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)x509AttrCertParser.engineRead();
                if (!x509AttributeCertStoreSelector.match(x509AttributeCertificate)) {
                    continue;
                }
                set.add(x509AttributeCertificate);
            }
            catch (StreamParsingException ex) {}
        }
        return set;
    }
    
    private Set createCRLs(List iterator, final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final HashSet<X509CRL> set = new HashSet<X509CRL>();
        final X509CRLParser x509CRLParser = new X509CRLParser();
        iterator = ((List)iterator).iterator();
        while (iterator.hasNext()) {
            try {
                x509CRLParser.engineInit(new ByteArrayInputStream(iterator.next()));
                final X509CRL x509CRL = (X509CRL)x509CRLParser.engineRead();
                if (!x509CRLStoreSelector.match((Object)x509CRL)) {
                    continue;
                }
                set.add(x509CRL);
            }
            catch (StreamParsingException ex) {}
        }
        return set;
    }
    
    private Set createCerts(List iterator, final X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
        iterator = ((List)iterator).iterator();
        final X509CertParser x509CertParser = new X509CertParser();
        while (iterator.hasNext()) {
            try {
                x509CertParser.engineInit(new ByteArrayInputStream(iterator.next()));
                final X509Certificate x509Certificate = (X509Certificate)x509CertParser.engineRead();
                if (!x509CertStoreSelector.match((Object)x509Certificate)) {
                    continue;
                }
                set.add(x509Certificate);
            }
            catch (Exception ex) {}
        }
        return set;
    }
    
    private Set createCrossCertificatePairs(final List list, final X509CertPairStoreSelector x509CertPairStoreSelector) throws StoreException {
        final HashSet<X509CertificatePair> set = new HashSet<X509CertificatePair>();
        int n2;
        for (int i = 0; i < list.size(); i = n2 + 1) {
            int n = i;
            X509CertificatePair x509CertificatePair = null;
            try {
                try {
                    final X509CertPairParser x509CertPairParser = new X509CertPairParser();
                    n = i;
                    x509CertPairParser.engineInit(new ByteArrayInputStream(list.get(i)));
                    n = i;
                    x509CertificatePair = (X509CertificatePair)x509CertPairParser.engineRead();
                }
                catch (CertificateParsingException | IOException ex) {
                    n2 = n;
                }
            }
            catch (StreamParsingException ex2) {
                final byte[] array = list.get(i);
                final int n3 = i + 1;
                x509CertificatePair = new X509CertificatePair(new CertificatePair(Certificate.getInstance(new ASN1InputStream(array).readObject()), Certificate.getInstance(new ASN1InputStream(list.get(n3)).readObject())));
                i = n3;
            }
            n2 = i;
            if (x509CertPairStoreSelector.match(x509CertificatePair)) {
                set.add(x509CertificatePair);
                n2 = i;
            }
        }
        return set;
    }
    
    private List crossCertificatePairSubjectSearch(final X509CertPairStoreSelector x509CertPairStoreSelector, final String[] array, final String[] array2, final String[] array3) throws StoreException {
        final ArrayList list = new ArrayList();
        String subjectAsString;
        if (x509CertPairStoreSelector.getForwardSelector() != null) {
            subjectAsString = this.getSubjectAsString(x509CertPairStoreSelector.getForwardSelector());
        }
        else {
            subjectAsString = null;
        }
        String name = subjectAsString;
        if (x509CertPairStoreSelector.getCertPair() != null) {
            name = subjectAsString;
            if (x509CertPairStoreSelector.getCertPair().getForward() != null) {
                name = x509CertPairStoreSelector.getCertPair().getForward().getSubjectX500Principal().getName("RFC1779");
            }
        }
        if (name != null) {
            for (int i = 0; i < array3.length; ++i) {
                final String dn = this.parseDN(name, array3[i]);
                final StringBuilder sb = new StringBuilder();
                sb.append("*");
                sb.append(dn);
                sb.append("*");
                list.addAll(this.search(array2, sb.toString(), array));
            }
        }
        if (name == null) {
            list.addAll(this.search(array2, "*", array));
        }
        return list;
    }
    
    private X500Principal getCertificateIssuer(final X509Certificate x509Certificate) {
        return x509Certificate.getIssuerX500Principal();
    }
    
    private List getFromCache(final String s) {
        final List<Date> list = this.cacheMap.get(s);
        final long currentTimeMillis = System.currentTimeMillis();
        if (list == null) {
            return null;
        }
        if (list.get(0).getTime() < currentTimeMillis - LDAPStoreHelper.lifeTime) {
            return null;
        }
        return (List)list.get(1);
    }
    
    private String getSubjectAsString(final X509CertStoreSelector x509CertStoreSelector) {
        try {
            final byte[] subjectAsBytes = x509CertStoreSelector.getSubjectAsBytes();
            if (subjectAsBytes != null) {
                return new X500Principal(subjectAsBytes).getName("RFC1779");
            }
            return null;
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception processing name: ");
            sb.append(ex.getMessage());
            throw new StoreException(sb.toString(), ex);
        }
    }
    
    private String parseDN(String s, String substring) {
        final String lowerCase = s.toLowerCase();
        final StringBuilder sb = new StringBuilder();
        sb.append(substring.toLowerCase());
        sb.append("=");
        final int index = lowerCase.indexOf(sb.toString());
        if (index == -1) {
            return "";
        }
        s = s.substring(index + substring.length());
        while (true) {
            Label_0082: {
                int n;
                if ((n = s.indexOf(44)) == -1) {
                    break Label_0082;
                }
                while (s.charAt(n - 1) == '\\') {
                    if ((n = s.indexOf(44, n + 1)) == -1) {
                        break Label_0082;
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
    
    private List search(String[] connectLDAP, String s, final String[] returningAttributes) throws StoreException {
        final int n = 0;
        String string;
        if (connectLDAP == null) {
            string = null;
        }
        else {
            String s2 = s;
            if (s.equals("**")) {
                s2 = "*";
            }
            s = "";
            for (int i = 0; i < connectLDAP.length; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("(");
                sb.append(connectLDAP[i]);
                sb.append("=");
                sb.append(s2);
                sb.append(")");
                s = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("(|");
            sb2.append(s);
            sb2.append(")");
            string = sb2.toString();
        }
        s = "";
        for (int j = n; j < returningAttributes.length; ++j) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append("(");
            sb3.append(returningAttributes[j]);
            sb3.append("=*)");
            s = sb3.toString();
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("(|");
        sb4.append(s);
        sb4.append(")");
        s = sb4.toString();
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("(&");
        sb5.append(string);
        sb5.append("");
        sb5.append(s);
        sb5.append(")");
        final String string2 = sb5.toString();
        if (string != null) {
            s = string2;
        }
        final List fromCache = this.getFromCache(s);
        if (fromCache != null) {
            return fromCache;
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        Label_0526: {
            Context context;
            try {
                connectLDAP = (String[])(Object)this.connectLDAP();
                try {
                    final SearchControls searchControls = new SearchControls();
                    searchControls.setSearchScope(2);
                    searchControls.setCountLimit(0L);
                    searchControls.setReturningAttributes(returningAttributes);
                    final NamingEnumeration<SearchResult> search = ((DirContext)(Object)connectLDAP).search(this.params.getBaseDN(), s, searchControls);
                    while (search.hasMoreElements()) {
                        final NamingEnumeration<?> all = ((Attribute)search.next().getAttributes().getAll().next()).getAll();
                        while (all.hasMore()) {
                            list.add(all.next());
                        }
                    }
                    this.addToCache(s, list);
                    if (connectLDAP != null) {
                        break Label_0526;
                    }
                    return list;
                }
                catch (NamingException ex) {}
            }
            catch (NamingException ex2) {
                connectLDAP = null;
            }
            finally {
                context = null;
            }
            try {
                context.close();
                goto Label_0517;
            }
            catch (Exception ex3) {}
            if (connectLDAP == null) {
                return list;
            }
            try {
                ((Context)(Object)connectLDAP).close();
                return list;
            }
            catch (Exception ex4) {}
        }
        return list;
    }
    
    private String[] splitString(final String s) {
        return s.split("\\s+");
    }
    
    public Collection getAACertificates(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAACertificateAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAACertificateAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAACertificateSubjectAttributeName());
        final Set attributeCertificates = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (attributeCertificates.size() == 0) {
            attributeCertificates.addAll(this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return attributeCertificates;
    }
    
    public Collection getAttributeAuthorityRevocationLists(final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAttributeAuthorityRevocationListAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAttributeAuthorityRevocationListAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAttributeAuthorityRevocationListIssuerAttributeName());
        final Set crLs = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (crLs.size() == 0) {
            crLs.addAll(this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return crLs;
    }
    
    public Collection getAttributeCertificateAttributes(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAttributeCertificateAttributeAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAttributeCertificateAttributeAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAttributeCertificateAttributeSubjectAttributeName());
        final Set attributeCertificates = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (attributeCertificates.size() == 0) {
            attributeCertificates.addAll(this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return attributeCertificates;
    }
    
    public Collection getAttributeCertificateRevocationLists(final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAttributeCertificateRevocationListAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAttributeCertificateRevocationListAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAttributeCertificateRevocationListIssuerAttributeName());
        final Set crLs = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (crLs.size() == 0) {
            crLs.addAll(this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return crLs;
    }
    
    public Collection getAttributeDescriptorCertificates(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAttributeDescriptorCertificateAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAttributeDescriptorCertificateAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAttributeDescriptorCertificateSubjectAttributeName());
        final Set attributeCertificates = this.createAttributeCertificates(this.attrCertSubjectSerialSearch(x509AttributeCertStoreSelector, splitString, splitString2, splitString3), x509AttributeCertStoreSelector);
        if (attributeCertificates.size() == 0) {
            attributeCertificates.addAll(this.createAttributeCertificates(this.attrCertSubjectSerialSearch(new X509AttributeCertStoreSelector(), splitString, splitString2, splitString3), x509AttributeCertStoreSelector));
        }
        return attributeCertificates;
    }
    
    public Collection getAuthorityRevocationLists(final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getAuthorityRevocationListAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapAuthorityRevocationListAttributeName());
        final String[] splitString3 = this.splitString(this.params.getAuthorityRevocationListIssuerAttributeName());
        final Set crLs = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (crLs.size() == 0) {
            crLs.addAll(this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return crLs;
    }
    
    public Collection getCACertificates(final X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getCACertificateAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapCACertificateAttributeName());
        final String[] splitString3 = this.splitString(this.params.getCACertificateSubjectAttributeName());
        final Set certs = this.createCerts(this.certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (certs.size() == 0) {
            certs.addAll(this.createCerts(this.certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return certs;
    }
    
    public Collection getCertificateRevocationLists(final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getCertificateRevocationListAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapCertificateRevocationListAttributeName());
        final String[] splitString3 = this.splitString(this.params.getCertificateRevocationListIssuerAttributeName());
        final Set crLs = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (crLs.size() == 0) {
            crLs.addAll(this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return crLs;
    }
    
    public Collection getCrossCertificatePairs(final X509CertPairStoreSelector x509CertPairStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getCrossCertificateAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapCrossCertificateAttributeName());
        final String[] splitString3 = this.splitString(this.params.getCrossCertificateSubjectAttributeName());
        final Set crossCertificatePairs = this.createCrossCertificatePairs(this.crossCertificatePairSubjectSearch(x509CertPairStoreSelector, splitString, splitString2, splitString3), x509CertPairStoreSelector);
        if (crossCertificatePairs.size() == 0) {
            final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            final X509CertPairStoreSelector x509CertPairStoreSelector2 = new X509CertPairStoreSelector();
            x509CertPairStoreSelector2.setForwardSelector(x509CertStoreSelector);
            x509CertPairStoreSelector2.setReverseSelector(x509CertStoreSelector);
            crossCertificatePairs.addAll(this.createCrossCertificatePairs(this.crossCertificatePairSubjectSearch(x509CertPairStoreSelector2, splitString, splitString2, splitString3), x509CertPairStoreSelector));
        }
        return crossCertificatePairs;
    }
    
    public Collection getDeltaCertificateRevocationLists(final X509CRLStoreSelector x509CRLStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getDeltaRevocationListAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapDeltaRevocationListAttributeName());
        final String[] splitString3 = this.splitString(this.params.getDeltaRevocationListIssuerAttributeName());
        final Set crLs = this.createCRLs(this.cRLIssuerSearch(x509CRLStoreSelector, splitString, splitString2, splitString3), x509CRLStoreSelector);
        if (crLs.size() == 0) {
            crLs.addAll(this.createCRLs(this.cRLIssuerSearch(new X509CRLStoreSelector(), splitString, splitString2, splitString3), x509CRLStoreSelector));
        }
        return crLs;
    }
    
    public Collection getUserCertificates(final X509CertStoreSelector x509CertStoreSelector) throws StoreException {
        final String[] splitString = this.splitString(this.params.getUserCertificateAttribute());
        final String[] splitString2 = this.splitString(this.params.getLdapUserCertificateAttributeName());
        final String[] splitString3 = this.splitString(this.params.getUserCertificateSubjectAttributeName());
        final Set certs = this.createCerts(this.certSubjectSerialSearch(x509CertStoreSelector, splitString, splitString2, splitString3), x509CertStoreSelector);
        if (certs.size() == 0) {
            certs.addAll(this.createCerts(this.certSubjectSerialSearch(new X509CertStoreSelector(), splitString, splitString2, splitString3), x509CertStoreSelector));
        }
        return certs;
    }
}
