package org.spongycastle.jce;

import org.spongycastle.x509.*;
import java.security.cert.*;

public class X509LDAPCertStoreParameters implements CertStoreParameters, X509StoreParameters
{
    private String aACertificateAttribute;
    private String aACertificateSubjectAttributeName;
    private String attributeAuthorityRevocationListAttribute;
    private String attributeAuthorityRevocationListIssuerAttributeName;
    private String attributeCertificateAttributeAttribute;
    private String attributeCertificateAttributeSubjectAttributeName;
    private String attributeCertificateRevocationListAttribute;
    private String attributeCertificateRevocationListIssuerAttributeName;
    private String attributeDescriptorCertificateAttribute;
    private String attributeDescriptorCertificateSubjectAttributeName;
    private String authorityRevocationListAttribute;
    private String authorityRevocationListIssuerAttributeName;
    private String baseDN;
    private String cACertificateAttribute;
    private String cACertificateSubjectAttributeName;
    private String certificateRevocationListAttribute;
    private String certificateRevocationListIssuerAttributeName;
    private String crossCertificateAttribute;
    private String crossCertificateSubjectAttributeName;
    private String deltaRevocationListAttribute;
    private String deltaRevocationListIssuerAttributeName;
    private String ldapAACertificateAttributeName;
    private String ldapAttributeAuthorityRevocationListAttributeName;
    private String ldapAttributeCertificateAttributeAttributeName;
    private String ldapAttributeCertificateRevocationListAttributeName;
    private String ldapAttributeDescriptorCertificateAttributeName;
    private String ldapAuthorityRevocationListAttributeName;
    private String ldapCACertificateAttributeName;
    private String ldapCertificateRevocationListAttributeName;
    private String ldapCrossCertificateAttributeName;
    private String ldapDeltaRevocationListAttributeName;
    private String ldapURL;
    private String ldapUserCertificateAttributeName;
    private String searchForSerialNumberIn;
    private String userCertificateAttribute;
    private String userCertificateSubjectAttributeName;
    
    private X509LDAPCertStoreParameters(final Builder builder) {
        this.ldapURL = builder.ldapURL;
        this.baseDN = builder.baseDN;
        this.userCertificateAttribute = builder.userCertificateAttribute;
        this.cACertificateAttribute = builder.cACertificateAttribute;
        this.crossCertificateAttribute = builder.crossCertificateAttribute;
        this.certificateRevocationListAttribute = builder.certificateRevocationListAttribute;
        this.deltaRevocationListAttribute = builder.deltaRevocationListAttribute;
        this.authorityRevocationListAttribute = builder.authorityRevocationListAttribute;
        this.attributeCertificateAttributeAttribute = builder.attributeCertificateAttributeAttribute;
        this.aACertificateAttribute = builder.aACertificateAttribute;
        this.attributeDescriptorCertificateAttribute = builder.attributeDescriptorCertificateAttribute;
        this.attributeCertificateRevocationListAttribute = builder.attributeCertificateRevocationListAttribute;
        this.attributeAuthorityRevocationListAttribute = builder.attributeAuthorityRevocationListAttribute;
        this.ldapUserCertificateAttributeName = builder.ldapUserCertificateAttributeName;
        this.ldapCACertificateAttributeName = builder.ldapCACertificateAttributeName;
        this.ldapCrossCertificateAttributeName = builder.ldapCrossCertificateAttributeName;
        this.ldapCertificateRevocationListAttributeName = builder.ldapCertificateRevocationListAttributeName;
        this.ldapDeltaRevocationListAttributeName = builder.ldapDeltaRevocationListAttributeName;
        this.ldapAuthorityRevocationListAttributeName = builder.ldapAuthorityRevocationListAttributeName;
        this.ldapAttributeCertificateAttributeAttributeName = builder.ldapAttributeCertificateAttributeAttributeName;
        this.ldapAACertificateAttributeName = builder.ldapAACertificateAttributeName;
        this.ldapAttributeDescriptorCertificateAttributeName = builder.ldapAttributeDescriptorCertificateAttributeName;
        this.ldapAttributeCertificateRevocationListAttributeName = builder.ldapAttributeCertificateRevocationListAttributeName;
        this.ldapAttributeAuthorityRevocationListAttributeName = builder.ldapAttributeAuthorityRevocationListAttributeName;
        this.userCertificateSubjectAttributeName = builder.userCertificateSubjectAttributeName;
        this.cACertificateSubjectAttributeName = builder.cACertificateSubjectAttributeName;
        this.crossCertificateSubjectAttributeName = builder.crossCertificateSubjectAttributeName;
        this.certificateRevocationListIssuerAttributeName = builder.certificateRevocationListIssuerAttributeName;
        this.deltaRevocationListIssuerAttributeName = builder.deltaRevocationListIssuerAttributeName;
        this.authorityRevocationListIssuerAttributeName = builder.authorityRevocationListIssuerAttributeName;
        this.attributeCertificateAttributeSubjectAttributeName = builder.attributeCertificateAttributeSubjectAttributeName;
        this.aACertificateSubjectAttributeName = builder.aACertificateSubjectAttributeName;
        this.attributeDescriptorCertificateSubjectAttributeName = builder.attributeDescriptorCertificateSubjectAttributeName;
        this.attributeCertificateRevocationListIssuerAttributeName = builder.attributeCertificateRevocationListIssuerAttributeName;
        this.attributeAuthorityRevocationListIssuerAttributeName = builder.attributeAuthorityRevocationListIssuerAttributeName;
        this.searchForSerialNumberIn = builder.searchForSerialNumberIn;
    }
    
    private int addHashCode(final int n, final Object o) {
        int hashCode;
        if (o == null) {
            hashCode = 0;
        }
        else {
            hashCode = o.hashCode();
        }
        return n * 29 + hashCode;
    }
    
    private boolean checkField(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static X509LDAPCertStoreParameters getInstance(final LDAPCertStoreParameters ldapCertStoreParameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append("ldap://");
        sb.append(ldapCertStoreParameters.getServerName());
        sb.append(":");
        sb.append(ldapCertStoreParameters.getPort());
        return new Builder(sb.toString(), "").build();
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    public boolean equal(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509LDAPCertStoreParameters)) {
            return false;
        }
        final X509LDAPCertStoreParameters x509LDAPCertStoreParameters = (X509LDAPCertStoreParameters)o;
        return this.checkField(this.ldapURL, x509LDAPCertStoreParameters.ldapURL) && this.checkField(this.baseDN, x509LDAPCertStoreParameters.baseDN) && this.checkField(this.userCertificateAttribute, x509LDAPCertStoreParameters.userCertificateAttribute) && this.checkField(this.cACertificateAttribute, x509LDAPCertStoreParameters.cACertificateAttribute) && this.checkField(this.crossCertificateAttribute, x509LDAPCertStoreParameters.crossCertificateAttribute) && this.checkField(this.certificateRevocationListAttribute, x509LDAPCertStoreParameters.certificateRevocationListAttribute) && this.checkField(this.deltaRevocationListAttribute, x509LDAPCertStoreParameters.deltaRevocationListAttribute) && this.checkField(this.authorityRevocationListAttribute, x509LDAPCertStoreParameters.authorityRevocationListAttribute) && this.checkField(this.attributeCertificateAttributeAttribute, x509LDAPCertStoreParameters.attributeCertificateAttributeAttribute) && this.checkField(this.aACertificateAttribute, x509LDAPCertStoreParameters.aACertificateAttribute) && this.checkField(this.attributeDescriptorCertificateAttribute, x509LDAPCertStoreParameters.attributeDescriptorCertificateAttribute) && this.checkField(this.attributeCertificateRevocationListAttribute, x509LDAPCertStoreParameters.attributeCertificateRevocationListAttribute) && this.checkField(this.attributeAuthorityRevocationListAttribute, x509LDAPCertStoreParameters.attributeAuthorityRevocationListAttribute) && this.checkField(this.ldapUserCertificateAttributeName, x509LDAPCertStoreParameters.ldapUserCertificateAttributeName) && this.checkField(this.ldapCACertificateAttributeName, x509LDAPCertStoreParameters.ldapCACertificateAttributeName) && this.checkField(this.ldapCrossCertificateAttributeName, x509LDAPCertStoreParameters.ldapCrossCertificateAttributeName) && this.checkField(this.ldapCertificateRevocationListAttributeName, x509LDAPCertStoreParameters.ldapCertificateRevocationListAttributeName) && this.checkField(this.ldapDeltaRevocationListAttributeName, x509LDAPCertStoreParameters.ldapDeltaRevocationListAttributeName) && this.checkField(this.ldapAuthorityRevocationListAttributeName, x509LDAPCertStoreParameters.ldapAuthorityRevocationListAttributeName) && this.checkField(this.ldapAttributeCertificateAttributeAttributeName, x509LDAPCertStoreParameters.ldapAttributeCertificateAttributeAttributeName) && this.checkField(this.ldapAACertificateAttributeName, x509LDAPCertStoreParameters.ldapAACertificateAttributeName) && this.checkField(this.ldapAttributeDescriptorCertificateAttributeName, x509LDAPCertStoreParameters.ldapAttributeDescriptorCertificateAttributeName) && this.checkField(this.ldapAttributeCertificateRevocationListAttributeName, x509LDAPCertStoreParameters.ldapAttributeCertificateRevocationListAttributeName) && this.checkField(this.ldapAttributeAuthorityRevocationListAttributeName, x509LDAPCertStoreParameters.ldapAttributeAuthorityRevocationListAttributeName) && this.checkField(this.userCertificateSubjectAttributeName, x509LDAPCertStoreParameters.userCertificateSubjectAttributeName) && this.checkField(this.cACertificateSubjectAttributeName, x509LDAPCertStoreParameters.cACertificateSubjectAttributeName) && this.checkField(this.crossCertificateSubjectAttributeName, x509LDAPCertStoreParameters.crossCertificateSubjectAttributeName) && this.checkField(this.certificateRevocationListIssuerAttributeName, x509LDAPCertStoreParameters.certificateRevocationListIssuerAttributeName) && this.checkField(this.deltaRevocationListIssuerAttributeName, x509LDAPCertStoreParameters.deltaRevocationListIssuerAttributeName) && this.checkField(this.authorityRevocationListIssuerAttributeName, x509LDAPCertStoreParameters.authorityRevocationListIssuerAttributeName) && this.checkField(this.attributeCertificateAttributeSubjectAttributeName, x509LDAPCertStoreParameters.attributeCertificateAttributeSubjectAttributeName) && this.checkField(this.aACertificateSubjectAttributeName, x509LDAPCertStoreParameters.aACertificateSubjectAttributeName) && this.checkField(this.attributeDescriptorCertificateSubjectAttributeName, x509LDAPCertStoreParameters.attributeDescriptorCertificateSubjectAttributeName) && this.checkField(this.attributeCertificateRevocationListIssuerAttributeName, x509LDAPCertStoreParameters.attributeCertificateRevocationListIssuerAttributeName) && this.checkField(this.attributeAuthorityRevocationListIssuerAttributeName, x509LDAPCertStoreParameters.attributeAuthorityRevocationListIssuerAttributeName) && this.checkField(this.searchForSerialNumberIn, x509LDAPCertStoreParameters.searchForSerialNumberIn);
    }
    
    public String getAACertificateAttribute() {
        return this.aACertificateAttribute;
    }
    
    public String getAACertificateSubjectAttributeName() {
        return this.aACertificateSubjectAttributeName;
    }
    
    public String getAttributeAuthorityRevocationListAttribute() {
        return this.attributeAuthorityRevocationListAttribute;
    }
    
    public String getAttributeAuthorityRevocationListIssuerAttributeName() {
        return this.attributeAuthorityRevocationListIssuerAttributeName;
    }
    
    public String getAttributeCertificateAttributeAttribute() {
        return this.attributeCertificateAttributeAttribute;
    }
    
    public String getAttributeCertificateAttributeSubjectAttributeName() {
        return this.attributeCertificateAttributeSubjectAttributeName;
    }
    
    public String getAttributeCertificateRevocationListAttribute() {
        return this.attributeCertificateRevocationListAttribute;
    }
    
    public String getAttributeCertificateRevocationListIssuerAttributeName() {
        return this.attributeCertificateRevocationListIssuerAttributeName;
    }
    
    public String getAttributeDescriptorCertificateAttribute() {
        return this.attributeDescriptorCertificateAttribute;
    }
    
    public String getAttributeDescriptorCertificateSubjectAttributeName() {
        return this.attributeDescriptorCertificateSubjectAttributeName;
    }
    
    public String getAuthorityRevocationListAttribute() {
        return this.authorityRevocationListAttribute;
    }
    
    public String getAuthorityRevocationListIssuerAttributeName() {
        return this.authorityRevocationListIssuerAttributeName;
    }
    
    public String getBaseDN() {
        return this.baseDN;
    }
    
    public String getCACertificateAttribute() {
        return this.cACertificateAttribute;
    }
    
    public String getCACertificateSubjectAttributeName() {
        return this.cACertificateSubjectAttributeName;
    }
    
    public String getCertificateRevocationListAttribute() {
        return this.certificateRevocationListAttribute;
    }
    
    public String getCertificateRevocationListIssuerAttributeName() {
        return this.certificateRevocationListIssuerAttributeName;
    }
    
    public String getCrossCertificateAttribute() {
        return this.crossCertificateAttribute;
    }
    
    public String getCrossCertificateSubjectAttributeName() {
        return this.crossCertificateSubjectAttributeName;
    }
    
    public String getDeltaRevocationListAttribute() {
        return this.deltaRevocationListAttribute;
    }
    
    public String getDeltaRevocationListIssuerAttributeName() {
        return this.deltaRevocationListIssuerAttributeName;
    }
    
    public String getLdapAACertificateAttributeName() {
        return this.ldapAACertificateAttributeName;
    }
    
    public String getLdapAttributeAuthorityRevocationListAttributeName() {
        return this.ldapAttributeAuthorityRevocationListAttributeName;
    }
    
    public String getLdapAttributeCertificateAttributeAttributeName() {
        return this.ldapAttributeCertificateAttributeAttributeName;
    }
    
    public String getLdapAttributeCertificateRevocationListAttributeName() {
        return this.ldapAttributeCertificateRevocationListAttributeName;
    }
    
    public String getLdapAttributeDescriptorCertificateAttributeName() {
        return this.ldapAttributeDescriptorCertificateAttributeName;
    }
    
    public String getLdapAuthorityRevocationListAttributeName() {
        return this.ldapAuthorityRevocationListAttributeName;
    }
    
    public String getLdapCACertificateAttributeName() {
        return this.ldapCACertificateAttributeName;
    }
    
    public String getLdapCertificateRevocationListAttributeName() {
        return this.ldapCertificateRevocationListAttributeName;
    }
    
    public String getLdapCrossCertificateAttributeName() {
        return this.ldapCrossCertificateAttributeName;
    }
    
    public String getLdapDeltaRevocationListAttributeName() {
        return this.ldapDeltaRevocationListAttributeName;
    }
    
    public String getLdapURL() {
        return this.ldapURL;
    }
    
    public String getLdapUserCertificateAttributeName() {
        return this.ldapUserCertificateAttributeName;
    }
    
    public String getSearchForSerialNumberIn() {
        return this.searchForSerialNumberIn;
    }
    
    public String getUserCertificateAttribute() {
        return this.userCertificateAttribute;
    }
    
    public String getUserCertificateSubjectAttributeName() {
        return this.userCertificateSubjectAttributeName;
    }
    
    @Override
    public int hashCode() {
        return this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(this.addHashCode(0, this.userCertificateAttribute), this.cACertificateAttribute), this.crossCertificateAttribute), this.certificateRevocationListAttribute), this.deltaRevocationListAttribute), this.authorityRevocationListAttribute), this.attributeCertificateAttributeAttribute), this.aACertificateAttribute), this.attributeDescriptorCertificateAttribute), this.attributeCertificateRevocationListAttribute), this.attributeAuthorityRevocationListAttribute), this.ldapUserCertificateAttributeName), this.ldapCACertificateAttributeName), this.ldapCrossCertificateAttributeName), this.ldapCertificateRevocationListAttributeName), this.ldapDeltaRevocationListAttributeName), this.ldapAuthorityRevocationListAttributeName), this.ldapAttributeCertificateAttributeAttributeName), this.ldapAACertificateAttributeName), this.ldapAttributeDescriptorCertificateAttributeName), this.ldapAttributeCertificateRevocationListAttributeName), this.ldapAttributeAuthorityRevocationListAttributeName), this.userCertificateSubjectAttributeName), this.cACertificateSubjectAttributeName), this.crossCertificateSubjectAttributeName), this.certificateRevocationListIssuerAttributeName), this.deltaRevocationListIssuerAttributeName), this.authorityRevocationListIssuerAttributeName), this.attributeCertificateAttributeSubjectAttributeName), this.aACertificateSubjectAttributeName), this.attributeDescriptorCertificateSubjectAttributeName), this.attributeCertificateRevocationListIssuerAttributeName), this.attributeAuthorityRevocationListIssuerAttributeName), this.searchForSerialNumberIn);
    }
    
    public static class Builder
    {
        private String aACertificateAttribute;
        private String aACertificateSubjectAttributeName;
        private String attributeAuthorityRevocationListAttribute;
        private String attributeAuthorityRevocationListIssuerAttributeName;
        private String attributeCertificateAttributeAttribute;
        private String attributeCertificateAttributeSubjectAttributeName;
        private String attributeCertificateRevocationListAttribute;
        private String attributeCertificateRevocationListIssuerAttributeName;
        private String attributeDescriptorCertificateAttribute;
        private String attributeDescriptorCertificateSubjectAttributeName;
        private String authorityRevocationListAttribute;
        private String authorityRevocationListIssuerAttributeName;
        private String baseDN;
        private String cACertificateAttribute;
        private String cACertificateSubjectAttributeName;
        private String certificateRevocationListAttribute;
        private String certificateRevocationListIssuerAttributeName;
        private String crossCertificateAttribute;
        private String crossCertificateSubjectAttributeName;
        private String deltaRevocationListAttribute;
        private String deltaRevocationListIssuerAttributeName;
        private String ldapAACertificateAttributeName;
        private String ldapAttributeAuthorityRevocationListAttributeName;
        private String ldapAttributeCertificateAttributeAttributeName;
        private String ldapAttributeCertificateRevocationListAttributeName;
        private String ldapAttributeDescriptorCertificateAttributeName;
        private String ldapAuthorityRevocationListAttributeName;
        private String ldapCACertificateAttributeName;
        private String ldapCertificateRevocationListAttributeName;
        private String ldapCrossCertificateAttributeName;
        private String ldapDeltaRevocationListAttributeName;
        private String ldapURL;
        private String ldapUserCertificateAttributeName;
        private String searchForSerialNumberIn;
        private String userCertificateAttribute;
        private String userCertificateSubjectAttributeName;
        
        public Builder() {
            this("ldap://localhost:389", "");
        }
        
        public Builder(final String ldapURL, final String baseDN) {
            this.ldapURL = ldapURL;
            if (baseDN == null) {
                this.baseDN = "";
            }
            else {
                this.baseDN = baseDN;
            }
            this.userCertificateAttribute = "userCertificate";
            this.cACertificateAttribute = "cACertificate";
            this.crossCertificateAttribute = "crossCertificatePair";
            this.certificateRevocationListAttribute = "certificateRevocationList";
            this.deltaRevocationListAttribute = "deltaRevocationList";
            this.authorityRevocationListAttribute = "authorityRevocationList";
            this.attributeCertificateAttributeAttribute = "attributeCertificateAttribute";
            this.aACertificateAttribute = "aACertificate";
            this.attributeDescriptorCertificateAttribute = "attributeDescriptorCertificate";
            this.attributeCertificateRevocationListAttribute = "attributeCertificateRevocationList";
            this.attributeAuthorityRevocationListAttribute = "attributeAuthorityRevocationList";
            this.ldapUserCertificateAttributeName = "cn";
            this.ldapCACertificateAttributeName = "cn ou o";
            this.ldapCrossCertificateAttributeName = "cn ou o";
            this.ldapCertificateRevocationListAttributeName = "cn ou o";
            this.ldapDeltaRevocationListAttributeName = "cn ou o";
            this.ldapAuthorityRevocationListAttributeName = "cn ou o";
            this.ldapAttributeCertificateAttributeAttributeName = "cn";
            this.ldapAACertificateAttributeName = "cn o ou";
            this.ldapAttributeDescriptorCertificateAttributeName = "cn o ou";
            this.ldapAttributeCertificateRevocationListAttributeName = "cn o ou";
            this.ldapAttributeAuthorityRevocationListAttributeName = "cn o ou";
            this.userCertificateSubjectAttributeName = "cn";
            this.cACertificateSubjectAttributeName = "o ou";
            this.crossCertificateSubjectAttributeName = "o ou";
            this.certificateRevocationListIssuerAttributeName = "o ou";
            this.deltaRevocationListIssuerAttributeName = "o ou";
            this.authorityRevocationListIssuerAttributeName = "o ou";
            this.attributeCertificateAttributeSubjectAttributeName = "cn";
            this.aACertificateSubjectAttributeName = "o ou";
            this.attributeDescriptorCertificateSubjectAttributeName = "o ou";
            this.attributeCertificateRevocationListIssuerAttributeName = "o ou";
            this.attributeAuthorityRevocationListIssuerAttributeName = "o ou";
            this.searchForSerialNumberIn = "uid serialNumber cn";
        }
        
        public X509LDAPCertStoreParameters build() {
            if (this.ldapUserCertificateAttributeName != null && this.ldapCACertificateAttributeName != null && this.ldapCrossCertificateAttributeName != null && this.ldapCertificateRevocationListAttributeName != null && this.ldapDeltaRevocationListAttributeName != null && this.ldapAuthorityRevocationListAttributeName != null && this.ldapAttributeCertificateAttributeAttributeName != null && this.ldapAACertificateAttributeName != null && this.ldapAttributeDescriptorCertificateAttributeName != null && this.ldapAttributeCertificateRevocationListAttributeName != null && this.ldapAttributeAuthorityRevocationListAttributeName != null && this.userCertificateSubjectAttributeName != null && this.cACertificateSubjectAttributeName != null && this.crossCertificateSubjectAttributeName != null && this.certificateRevocationListIssuerAttributeName != null && this.deltaRevocationListIssuerAttributeName != null && this.authorityRevocationListIssuerAttributeName != null && this.attributeCertificateAttributeSubjectAttributeName != null && this.aACertificateSubjectAttributeName != null && this.attributeDescriptorCertificateSubjectAttributeName != null && this.attributeCertificateRevocationListIssuerAttributeName != null && this.attributeAuthorityRevocationListIssuerAttributeName != null) {
                return new X509LDAPCertStoreParameters(this, null);
            }
            throw new IllegalArgumentException("Necessary parameters not specified.");
        }
        
        public Builder setAACertificateAttribute(final String aaCertificateAttribute) {
            this.aACertificateAttribute = aaCertificateAttribute;
            return this;
        }
        
        public Builder setAACertificateSubjectAttributeName(final String aaCertificateSubjectAttributeName) {
            this.aACertificateSubjectAttributeName = aaCertificateSubjectAttributeName;
            return this;
        }
        
        public Builder setAttributeAuthorityRevocationListAttribute(final String attributeAuthorityRevocationListAttribute) {
            this.attributeAuthorityRevocationListAttribute = attributeAuthorityRevocationListAttribute;
            return this;
        }
        
        public Builder setAttributeAuthorityRevocationListIssuerAttributeName(final String attributeAuthorityRevocationListIssuerAttributeName) {
            this.attributeAuthorityRevocationListIssuerAttributeName = attributeAuthorityRevocationListIssuerAttributeName;
            return this;
        }
        
        public Builder setAttributeCertificateAttributeAttribute(final String attributeCertificateAttributeAttribute) {
            this.attributeCertificateAttributeAttribute = attributeCertificateAttributeAttribute;
            return this;
        }
        
        public Builder setAttributeCertificateAttributeSubjectAttributeName(final String attributeCertificateAttributeSubjectAttributeName) {
            this.attributeCertificateAttributeSubjectAttributeName = attributeCertificateAttributeSubjectAttributeName;
            return this;
        }
        
        public Builder setAttributeCertificateRevocationListAttribute(final String attributeCertificateRevocationListAttribute) {
            this.attributeCertificateRevocationListAttribute = attributeCertificateRevocationListAttribute;
            return this;
        }
        
        public Builder setAttributeCertificateRevocationListIssuerAttributeName(final String attributeCertificateRevocationListIssuerAttributeName) {
            this.attributeCertificateRevocationListIssuerAttributeName = attributeCertificateRevocationListIssuerAttributeName;
            return this;
        }
        
        public Builder setAttributeDescriptorCertificateAttribute(final String attributeDescriptorCertificateAttribute) {
            this.attributeDescriptorCertificateAttribute = attributeDescriptorCertificateAttribute;
            return this;
        }
        
        public Builder setAttributeDescriptorCertificateSubjectAttributeName(final String attributeDescriptorCertificateSubjectAttributeName) {
            this.attributeDescriptorCertificateSubjectAttributeName = attributeDescriptorCertificateSubjectAttributeName;
            return this;
        }
        
        public Builder setAuthorityRevocationListAttribute(final String authorityRevocationListAttribute) {
            this.authorityRevocationListAttribute = authorityRevocationListAttribute;
            return this;
        }
        
        public Builder setAuthorityRevocationListIssuerAttributeName(final String authorityRevocationListIssuerAttributeName) {
            this.authorityRevocationListIssuerAttributeName = authorityRevocationListIssuerAttributeName;
            return this;
        }
        
        public Builder setCACertificateAttribute(final String caCertificateAttribute) {
            this.cACertificateAttribute = caCertificateAttribute;
            return this;
        }
        
        public Builder setCACertificateSubjectAttributeName(final String caCertificateSubjectAttributeName) {
            this.cACertificateSubjectAttributeName = caCertificateSubjectAttributeName;
            return this;
        }
        
        public Builder setCertificateRevocationListAttribute(final String certificateRevocationListAttribute) {
            this.certificateRevocationListAttribute = certificateRevocationListAttribute;
            return this;
        }
        
        public Builder setCertificateRevocationListIssuerAttributeName(final String certificateRevocationListIssuerAttributeName) {
            this.certificateRevocationListIssuerAttributeName = certificateRevocationListIssuerAttributeName;
            return this;
        }
        
        public Builder setCrossCertificateAttribute(final String crossCertificateAttribute) {
            this.crossCertificateAttribute = crossCertificateAttribute;
            return this;
        }
        
        public Builder setCrossCertificateSubjectAttributeName(final String crossCertificateSubjectAttributeName) {
            this.crossCertificateSubjectAttributeName = crossCertificateSubjectAttributeName;
            return this;
        }
        
        public Builder setDeltaRevocationListAttribute(final String deltaRevocationListAttribute) {
            this.deltaRevocationListAttribute = deltaRevocationListAttribute;
            return this;
        }
        
        public Builder setDeltaRevocationListIssuerAttributeName(final String deltaRevocationListIssuerAttributeName) {
            this.deltaRevocationListIssuerAttributeName = deltaRevocationListIssuerAttributeName;
            return this;
        }
        
        public Builder setLdapAACertificateAttributeName(final String ldapAACertificateAttributeName) {
            this.ldapAACertificateAttributeName = ldapAACertificateAttributeName;
            return this;
        }
        
        public Builder setLdapAttributeAuthorityRevocationListAttributeName(final String ldapAttributeAuthorityRevocationListAttributeName) {
            this.ldapAttributeAuthorityRevocationListAttributeName = ldapAttributeAuthorityRevocationListAttributeName;
            return this;
        }
        
        public Builder setLdapAttributeCertificateAttributeAttributeName(final String ldapAttributeCertificateAttributeAttributeName) {
            this.ldapAttributeCertificateAttributeAttributeName = ldapAttributeCertificateAttributeAttributeName;
            return this;
        }
        
        public Builder setLdapAttributeCertificateRevocationListAttributeName(final String ldapAttributeCertificateRevocationListAttributeName) {
            this.ldapAttributeCertificateRevocationListAttributeName = ldapAttributeCertificateRevocationListAttributeName;
            return this;
        }
        
        public Builder setLdapAttributeDescriptorCertificateAttributeName(final String ldapAttributeDescriptorCertificateAttributeName) {
            this.ldapAttributeDescriptorCertificateAttributeName = ldapAttributeDescriptorCertificateAttributeName;
            return this;
        }
        
        public Builder setLdapAuthorityRevocationListAttributeName(final String ldapAuthorityRevocationListAttributeName) {
            this.ldapAuthorityRevocationListAttributeName = ldapAuthorityRevocationListAttributeName;
            return this;
        }
        
        public Builder setLdapCACertificateAttributeName(final String ldapCACertificateAttributeName) {
            this.ldapCACertificateAttributeName = ldapCACertificateAttributeName;
            return this;
        }
        
        public Builder setLdapCertificateRevocationListAttributeName(final String ldapCertificateRevocationListAttributeName) {
            this.ldapCertificateRevocationListAttributeName = ldapCertificateRevocationListAttributeName;
            return this;
        }
        
        public Builder setLdapCrossCertificateAttributeName(final String ldapCrossCertificateAttributeName) {
            this.ldapCrossCertificateAttributeName = ldapCrossCertificateAttributeName;
            return this;
        }
        
        public Builder setLdapDeltaRevocationListAttributeName(final String ldapDeltaRevocationListAttributeName) {
            this.ldapDeltaRevocationListAttributeName = ldapDeltaRevocationListAttributeName;
            return this;
        }
        
        public Builder setLdapUserCertificateAttributeName(final String ldapUserCertificateAttributeName) {
            this.ldapUserCertificateAttributeName = ldapUserCertificateAttributeName;
            return this;
        }
        
        public Builder setSearchForSerialNumberIn(final String searchForSerialNumberIn) {
            this.searchForSerialNumberIn = searchForSerialNumberIn;
            return this;
        }
        
        public Builder setUserCertificateAttribute(final String userCertificateAttribute) {
            this.userCertificateAttribute = userCertificateAttribute;
            return this;
        }
        
        public Builder setUserCertificateSubjectAttributeName(final String userCertificateSubjectAttributeName) {
            this.userCertificateSubjectAttributeName = userCertificateSubjectAttributeName;
            return this;
        }
    }
}
