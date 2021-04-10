package org.spongycastle.jcajce.provider.asymmetric.x509;

import org.spongycastle.asn1.x500.*;
import javax.security.auth.x500.*;
import java.io.*;
import java.security.cert.*;
import java.util.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.util.*;

class X509CRLEntryObject extends X509CRLEntry
{
    private TBSCertList.CRLEntry c;
    private X500Name certificateIssuer;
    private int hashValue;
    private boolean isHashValueSet;
    
    protected X509CRLEntryObject(final TBSCertList.CRLEntry c) {
        this.c = c;
        this.certificateIssuer = null;
    }
    
    protected X509CRLEntryObject(final TBSCertList.CRLEntry c, final boolean b, final X500Name x500Name) {
        this.c = c;
        this.certificateIssuer = this.loadCertificateIssuer(b, x500Name);
    }
    
    private Extension getExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            return extensions.getExtension(asn1ObjectIdentifier);
        }
        return null;
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final Extensions extensions = this.c.getExtensions();
        if (extensions != null) {
            final HashSet<String> set = new HashSet<String>();
            final Enumeration oids = extensions.oids();
            while (oids.hasMoreElements()) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                if (b == extensions.getExtension(asn1ObjectIdentifier).isCritical()) {
                    set.add(asn1ObjectIdentifier.getId());
                }
            }
            return set;
        }
        return null;
    }
    
    private X500Name loadCertificateIssuer(final boolean b, X500Name instance) {
        if (!b) {
            return null;
        }
        final Extension extension = this.getExtension(Extension.certificateIssuer);
        if (extension == null) {
            return instance;
        }
        try {
            final GeneralName[] names = GeneralNames.getInstance(extension.getParsedValue()).getNames();
            for (int i = 0; i < names.length; ++i) {
                if (names[i].getTagNo() == 4) {
                    instance = X500Name.getInstance(names[i].getName());
                    return instance;
                }
            }
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof X509CRLEntryObject) {
            return this.c.equals(((X509CRLEntryObject)o).c);
        }
        return super.equals(this);
    }
    
    @Override
    public X500Principal getCertificateIssuer() {
        if (this.certificateIssuer == null) {
            return null;
        }
        try {
            return new X500Principal(this.certificateIssuer.getEncoded());
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    @Override
    public byte[] getEncoded() throws CRLException {
        try {
            return this.c.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final Extension extension = this.getExtension(new ASN1ObjectIdentifier(s));
        if (extension != null) {
            try {
                return extension.getExtnValue().getEncoded();
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception encoding: ");
                sb.append(ex.toString());
                throw new IllegalStateException(sb.toString());
            }
        }
        return null;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    @Override
    public Date getRevocationDate() {
        return this.c.getRevocationDate().getDate();
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.c.getUserCertificate().getValue();
    }
    
    @Override
    public boolean hasExtensions() {
        return this.c.getExtensions() != null;
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    @Override
    public int hashCode() {
        if (!this.isHashValueSet) {
            this.hashValue = super.hashCode();
            this.isHashValueSet = true;
        }
        return this.hashValue;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("      userCertificate: ");
        sb.append(this.getSerialNumber());
        sb.append(lineSeparator);
        sb.append("       revocationDate: ");
        sb.append(this.getRevocationDate());
        sb.append(lineSeparator);
        sb.append("       certificateIssuer: ");
        sb.append(this.getCertificateIssuer());
        sb.append(lineSeparator);
        final Extensions extensions = this.c.getExtensions();
        Label_0330: {
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                if (oids.hasMoreElements()) {
                    String s = "   crlEntryExtensions:";
                Label_0112:
                    while (true) {
                        while (true) {
                            sb.append(s);
                        Label_0118:
                            while (true) {
                                sb.append(lineSeparator);
                                while (oids.hasMoreElements()) {
                                    final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                                    final Extension extension = extensions.getExtension(asn1ObjectIdentifier);
                                    if (extension.getExtnValue() != null) {
                                        final ASN1InputStream asn1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                                        sb.append("                       critical(");
                                        sb.append(extension.isCritical());
                                        sb.append(") ");
                                        try {
                                            if (asn1ObjectIdentifier.equals(Extension.reasonCode)) {
                                                sb.append(CRLReason.getInstance(ASN1Enumerated.getInstance(asn1InputStream.readObject())));
                                            }
                                            else if (asn1ObjectIdentifier.equals(Extension.certificateIssuer)) {
                                                sb.append("Certificate issuer: ");
                                                sb.append(GeneralNames.getInstance(asn1InputStream.readObject()));
                                            }
                                            else {
                                                sb.append(asn1ObjectIdentifier.getId());
                                                sb.append(" value = ");
                                                sb.append(ASN1Dump.dumpAsString(asn1InputStream.readObject()));
                                            }
                                            sb.append(lineSeparator);
                                            continue;
                                        }
                                        catch (Exception ex) {
                                            sb.append(asn1ObjectIdentifier.getId());
                                            sb.append(" value = ");
                                            s = "*****";
                                            continue Label_0112;
                                        }
                                        break;
                                    }
                                    continue Label_0118;
                                }
                                break Label_0330;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }
}
