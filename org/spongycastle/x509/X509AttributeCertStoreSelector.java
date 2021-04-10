package org.spongycastle.x509;

import org.spongycastle.util.*;
import java.math.*;
import java.io.*;
import java.util.*;
import java.security.cert.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class X509AttributeCertStoreSelector implements Selector
{
    private X509AttributeCertificate attributeCert;
    private Date attributeCertificateValid;
    private AttributeCertificateHolder holder;
    private AttributeCertificateIssuer issuer;
    private BigInteger serialNumber;
    private Collection targetGroups;
    private Collection targetNames;
    
    public X509AttributeCertStoreSelector() {
        this.targetNames = new HashSet();
        this.targetGroups = new HashSet();
    }
    
    private Set extractGeneralNames(final Collection collection) throws IOException {
        if (collection != null && !collection.isEmpty()) {
            final HashSet<byte[]> set = new HashSet<byte[]>();
            for (Object o : collection) {
                if (!(o instanceof GeneralName)) {
                    o = GeneralName.getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                set.add((byte[])o);
            }
            return set;
        }
        return new HashSet();
    }
    
    public void addTargetGroup(final GeneralName generalName) {
        this.targetGroups.add(generalName);
    }
    
    public void addTargetGroup(final byte[] array) throws IOException {
        this.addTargetGroup(GeneralName.getInstance(ASN1Primitive.fromByteArray(array)));
    }
    
    public void addTargetName(final GeneralName generalName) {
        this.targetNames.add(generalName);
    }
    
    public void addTargetName(final byte[] array) throws IOException {
        this.addTargetName(GeneralName.getInstance(ASN1Primitive.fromByteArray(array)));
    }
    
    @Override
    public Object clone() {
        final X509AttributeCertStoreSelector x509AttributeCertStoreSelector = new X509AttributeCertStoreSelector();
        x509AttributeCertStoreSelector.attributeCert = this.attributeCert;
        x509AttributeCertStoreSelector.attributeCertificateValid = this.getAttributeCertificateValid();
        x509AttributeCertStoreSelector.holder = this.holder;
        x509AttributeCertStoreSelector.issuer = this.issuer;
        x509AttributeCertStoreSelector.serialNumber = this.serialNumber;
        x509AttributeCertStoreSelector.targetGroups = this.getTargetGroups();
        x509AttributeCertStoreSelector.targetNames = this.getTargetNames();
        return x509AttributeCertStoreSelector;
    }
    
    public X509AttributeCertificate getAttributeCert() {
        return this.attributeCert;
    }
    
    public Date getAttributeCertificateValid() {
        if (this.attributeCertificateValid != null) {
            return new Date(this.attributeCertificateValid.getTime());
        }
        return null;
    }
    
    public AttributeCertificateHolder getHolder() {
        return this.holder;
    }
    
    public AttributeCertificateIssuer getIssuer() {
        return this.issuer;
    }
    
    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    public Collection getTargetGroups() {
        return Collections.unmodifiableCollection((Collection<?>)this.targetGroups);
    }
    
    public Collection getTargetNames() {
        return Collections.unmodifiableCollection((Collection<?>)this.targetNames);
    }
    
    @Override
    public boolean match(final Object o) {
        if (!(o instanceof X509AttributeCertificate)) {
            return false;
        }
        final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)o;
        final X509AttributeCertificate attributeCert = this.attributeCert;
        if (attributeCert != null && !attributeCert.equals(x509AttributeCertificate)) {
            return false;
        }
        if (this.serialNumber != null && !x509AttributeCertificate.getSerialNumber().equals(this.serialNumber)) {
            return false;
        }
        if (this.holder != null && !x509AttributeCertificate.getHolder().equals(this.holder)) {
            return false;
        }
        if (this.issuer != null && !x509AttributeCertificate.getIssuer().equals(this.issuer)) {
            return false;
        }
        final Date attributeCertificateValid = this.attributeCertificateValid;
        if (attributeCertificateValid != null) {
            try {
                x509AttributeCertificate.checkValidity(attributeCertificateValid);
            }
            catch (CertificateExpiredException | CertificateNotYetValidException ex) {
                return false;
            }
        }
        if (!this.targetNames.isEmpty() || !this.targetGroups.isEmpty()) {
            final byte[] extensionValue = x509AttributeCertificate.getExtensionValue(X509Extensions.TargetInformation.getId());
            if (extensionValue != null) {
                try {
                    final Targets[] targetsObjects = TargetInformation.getInstance(new ASN1InputStream(((DEROctetString)ASN1Primitive.fromByteArray(extensionValue)).getOctets()).readObject()).getTargetsObjects();
                    if (!this.targetNames.isEmpty()) {
                        int i = 0;
                        int n = 0;
                        while (i < targetsObjects.length) {
                            final Target[] targets = targetsObjects[i].getTargets();
                            int n2 = 0;
                            int n3;
                            while (true) {
                                n3 = n;
                                if (n2 >= targets.length) {
                                    break;
                                }
                                if (this.targetNames.contains(GeneralName.getInstance(targets[n2].getTargetName()))) {
                                    n3 = 1;
                                    break;
                                }
                                ++n2;
                            }
                            ++i;
                            n = n3;
                        }
                        if (n == 0) {
                            return false;
                        }
                    }
                    if (!this.targetGroups.isEmpty()) {
                        int j = 0;
                        int n4 = 0;
                        while (j < targetsObjects.length) {
                            final Target[] targets2 = targetsObjects[j].getTargets();
                            int n5 = 0;
                            int n6;
                            while (true) {
                                n6 = n4;
                                if (n5 >= targets2.length) {
                                    break;
                                }
                                if (this.targetGroups.contains(GeneralName.getInstance(targets2[n5].getTargetGroup()))) {
                                    n6 = 1;
                                    break;
                                }
                                ++n5;
                            }
                            ++j;
                            n4 = n6;
                        }
                        if (n4 == 0) {
                            return false;
                        }
                    }
                }
                catch (IOException | IllegalArgumentException ex2) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void setAttributeCert(final X509AttributeCertificate attributeCert) {
        this.attributeCert = attributeCert;
    }
    
    public void setAttributeCertificateValid(final Date date) {
        if (date != null) {
            this.attributeCertificateValid = new Date(date.getTime());
            return;
        }
        this.attributeCertificateValid = null;
    }
    
    public void setHolder(final AttributeCertificateHolder holder) {
        this.holder = holder;
    }
    
    public void setIssuer(final AttributeCertificateIssuer issuer) {
        this.issuer = issuer;
    }
    
    public void setSerialNumber(final BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public void setTargetGroups(final Collection collection) throws IOException {
        this.targetGroups = this.extractGeneralNames(collection);
    }
    
    public void setTargetNames(final Collection collection) throws IOException {
        this.targetNames = this.extractGeneralNames(collection);
    }
}
