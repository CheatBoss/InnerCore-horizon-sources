package org.spongycastle.jcajce.provider.asymmetric.x509;

import org.spongycastle.jcajce.util.*;
import java.security.*;
import javax.security.auth.x500.*;
import java.security.cert.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.util.io.pem.*;
import java.util.*;

public class PKIXCertPath extends CertPath
{
    static final List certPathEncodings;
    private List certificates;
    private final JcaJceHelper helper;
    
    static {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("PkiPath");
        list.add("PEM");
        list.add("PKCS7");
        certPathEncodings = Collections.unmodifiableList((List<?>)list);
    }
    
    PKIXCertPath(final InputStream inputStream, final String s) throws CertificateException {
        super("X.509");
        this.helper = new BCJcaJceHelper();
        try {
            if (s.equalsIgnoreCase("PkiPath")) {
                final ASN1Primitive object = new ASN1InputStream(inputStream).readObject();
                if (!(object instanceof ASN1Sequence)) {
                    throw new CertificateException("input stream does not contain a ASN1 SEQUENCE while reading PkiPath encoded data to load CertPath");
                }
                final Enumeration objects = ((ASN1Sequence)object).getObjects();
                this.certificates = new ArrayList();
                final CertificateFactory certificateFactory = this.helper.createCertificateFactory("X.509");
                while (objects.hasMoreElements()) {
                    this.certificates.add(0, certificateFactory.generateCertificate(new ByteArrayInputStream(objects.nextElement().toASN1Primitive().getEncoded("DER"))));
                }
            }
            else {
                if (!s.equalsIgnoreCase("PKCS7") && !s.equalsIgnoreCase("PEM")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unsupported encoding: ");
                    sb.append(s);
                    throw new CertificateException(sb.toString());
                }
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                this.certificates = new ArrayList();
                final CertificateFactory certificateFactory2 = this.helper.createCertificateFactory("X.509");
                while (true) {
                    final Certificate generateCertificate = certificateFactory2.generateCertificate(bufferedInputStream);
                    if (generateCertificate == null) {
                        break;
                    }
                    this.certificates.add(generateCertificate);
                }
            }
            this.certificates = this.sortCerts(this.certificates);
        }
        catch (NoSuchProviderException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("BouncyCastle provider not found while trying to get a CertificateFactory:\n");
            sb2.append(ex.toString());
            throw new CertificateException(sb2.toString());
        }
        catch (IOException ex2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("IOException throw while decoding CertPath:\n");
            sb3.append(ex2.toString());
            throw new CertificateException(sb3.toString());
        }
    }
    
    PKIXCertPath(final List list) {
        super("X.509");
        this.helper = new BCJcaJceHelper();
        this.certificates = this.sortCerts(new ArrayList(list));
    }
    
    private List sortCerts(final List list) {
        if (list.size() < 2) {
            return list;
        }
        X500Principal x500Principal = list.get(0).getIssuerX500Principal();
        int i = 1;
        while (true) {
            while (i != list.size()) {
                if (x500Principal.equals(list.get(i).getSubjectX500Principal())) {
                    x500Principal = list.get(i).getIssuerX500Principal();
                    ++i;
                }
                else {
                    final boolean b = false;
                    if (b) {
                        return list;
                    }
                    final ArrayList<X509Certificate> list2 = new ArrayList<X509Certificate>(list.size());
                    final ArrayList list3 = new ArrayList(list);
                    int j = 0;
                Label_0122:
                    while (j < list.size()) {
                        final X509Certificate x509Certificate = list.get(j);
                        final X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
                        while (true) {
                            for (int k = 0; k != list.size(); ++k) {
                                if (list.get(k).getIssuerX500Principal().equals(subjectX500Principal)) {
                                    final boolean b2 = true;
                                    if (!b2) {
                                        list2.add(x509Certificate);
                                        list.remove(j);
                                    }
                                    ++j;
                                    continue Label_0122;
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                    if (list2.size() > 1) {
                        return list3;
                    }
                    for (int l = 0; l != list2.size(); ++l) {
                        final X500Principal issuerX500Principal = list2.get(l).getIssuerX500Principal();
                        for (int n = 0; n < list.size(); ++n) {
                            final X509Certificate x509Certificate2 = list.get(n);
                            if (issuerX500Principal.equals(x509Certificate2.getSubjectX500Principal())) {
                                list2.add(x509Certificate2);
                                list.remove(n);
                                break;
                            }
                        }
                    }
                    if (list.size() > 0) {
                        return list3;
                    }
                    return list2;
                }
            }
            final boolean b = true;
            continue;
        }
    }
    
    private ASN1Primitive toASN1Object(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new ASN1InputStream(x509Certificate.getEncoded()).readObject();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception while encoding certificate: ");
            sb.append(ex.toString());
            throw new CertificateEncodingException(sb.toString());
        }
    }
    
    private byte[] toDEREncoded(final ASN1Encodable asn1Encodable) throws CertificateEncodingException {
        try {
            return asn1Encodable.toASN1Primitive().getEncoded("DER");
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception thrown: ");
            sb.append(ex);
            throw new CertificateEncodingException(sb.toString());
        }
    }
    
    @Override
    public List getCertificates() {
        return Collections.unmodifiableList((List<?>)new ArrayList<Object>(this.certificates));
    }
    
    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        final Iterator encodings = this.getEncodings();
        if (encodings.hasNext()) {
            final String next = encodings.next();
            if (next instanceof String) {
                return this.getEncoded(next);
            }
        }
        return null;
    }
    
    @Override
    public byte[] getEncoded(final String s) throws CertificateEncodingException {
        if (s.equalsIgnoreCase("PkiPath")) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            final List certificates = this.certificates;
            final ListIterator<X509Certificate> listIterator = certificates.listIterator(certificates.size());
            while (listIterator.hasPrevious()) {
                asn1EncodableVector.add(this.toASN1Object(listIterator.previous()));
            }
            return this.toDEREncoded(new DERSequence(asn1EncodableVector));
        }
        final boolean equalsIgnoreCase = s.equalsIgnoreCase("PKCS7");
        final int n = 0;
        int i = 0;
        if (equalsIgnoreCase) {
            final ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.data, null);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            while (i != this.certificates.size()) {
                asn1EncodableVector2.add(this.toASN1Object(this.certificates.get(i)));
                ++i;
            }
            return this.toDEREncoded(new ContentInfo(PKCSObjectIdentifiers.signedData, new SignedData(new ASN1Integer(1L), new DERSet(), contentInfo, new DERSet(asn1EncodableVector2), null, new DERSet())));
        }
        if (s.equalsIgnoreCase("PEM")) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
            int j = n;
            try {
                while (j != this.certificates.size()) {
                    pemWriter.writeObject(new PemObject("CERTIFICATE", ((X509Certificate)this.certificates.get(j)).getEncoded()));
                    ++j;
                }
                pemWriter.close();
                return byteArrayOutputStream.toByteArray();
            }
            catch (Exception ex) {
                throw new CertificateEncodingException("can't encode certificate for PEM encoded path");
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unsupported encoding: ");
        sb.append(s);
        throw new CertificateEncodingException(sb.toString());
    }
    
    @Override
    public Iterator getEncodings() {
        return PKIXCertPath.certPathEncodings.iterator();
    }
}
