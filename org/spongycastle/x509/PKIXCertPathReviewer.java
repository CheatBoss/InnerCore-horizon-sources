package org.spongycastle.x509;

import org.spongycastle.jce.provider.*;
import org.spongycastle.util.*;
import java.math.*;
import java.security.*;
import org.spongycastle.i18n.filter.*;
import javax.security.auth.x500.*;
import java.io.*;
import java.net.*;
import org.spongycastle.asn1.x509.qualified.*;
import java.util.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import org.spongycastle.asn1.*;
import org.spongycastle.i18n.*;

public class PKIXCertPathReviewer extends CertPathValidatorUtilities
{
    private static final String AUTH_INFO_ACCESS;
    private static final String CRL_DIST_POINTS;
    private static final String QC_STATEMENT;
    private static final String RESOURCE_NAME = "org.spongycastle.x509.CertPathReviewerMessages";
    protected CertPath certPath;
    protected List certs;
    protected List[] errors;
    private boolean initialized;
    protected int n;
    protected List[] notifications;
    protected PKIXParameters pkixParams;
    protected PolicyNode policyTree;
    protected PublicKey subjectPublicKey;
    protected TrustAnchor trustAnchor;
    protected Date validDate;
    
    static {
        QC_STATEMENT = Extension.qCStatements.getId();
        CRL_DIST_POINTS = Extension.cRLDistributionPoints.getId();
        AUTH_INFO_ACCESS = Extension.authorityInfoAccess.getId();
    }
    
    public PKIXCertPathReviewer() {
    }
    
    public PKIXCertPathReviewer(final CertPath certPath, final PKIXParameters pkixParameters) throws CertPathReviewerException {
        this.init(certPath, pkixParameters);
    }
    
    private String IPtoString(final byte[] array) {
        try {
            return InetAddress.getByAddress(array).getHostAddress();
        }
        catch (Exception ex) {
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i != array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 0xFF));
                sb.append(' ');
            }
            return sb.toString();
        }
    }
    
    private void checkCriticalExtensions() {
        final List<PKIXCertPathChecker> certPathCheckers = this.pkixParams.getCertPathCheckers();
        final Iterator<PKIXCertPathChecker> iterator = certPathCheckers.iterator();
    Label_0455:
        while (true) {
            try {
            Label_0055_Outer:
                while (true) {
                    while (true) {
                        int n = 0;
                        Label_0468: {
                            try {
                                while (iterator.hasNext()) {
                                    iterator.next().init(false);
                                }
                                n = this.certs.size() - 1;
                                if (n < 0) {
                                    break;
                                }
                                final X509Certificate x509Certificate = this.certs.get(n);
                                final Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                                if (criticalExtensionOIDs == null) {
                                    break Label_0468;
                                }
                                if (criticalExtensionOIDs.isEmpty()) {
                                    break Label_0468;
                                }
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.KEY_USAGE);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.CERTIFICATE_POLICIES);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.POLICY_MAPPINGS);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.INHIBIT_ANY_POLICY);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.ISSUING_DISTRIBUTION_POINT);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.DELTA_CRL_INDICATOR);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.POLICY_CONSTRAINTS);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.BASIC_CONSTRAINTS);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.SUBJECT_ALTERNATIVE_NAME);
                                criticalExtensionOIDs.remove(PKIXCertPathReviewer.NAME_CONSTRAINTS);
                                if (criticalExtensionOIDs.contains(PKIXCertPathReviewer.QC_STATEMENT) && this.processQcStatements(x509Certificate, n)) {
                                    criticalExtensionOIDs.remove(PKIXCertPathReviewer.QC_STATEMENT);
                                }
                                final Iterator<PKIXCertPathChecker> iterator2 = certPathCheckers.iterator();
                                while (iterator2.hasNext()) {
                                    try {
                                        iterator2.next().check(x509Certificate, criticalExtensionOIDs);
                                        continue Label_0055_Outer;
                                    }
                                    catch (CertPathValidatorException ex) {
                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.criticalExtensionError", new Object[] { ex.getMessage(), ex, ex.getClass().getName() }), ex.getCause(), this.certPath, n);
                                    }
                                    break;
                                }
                                if (!criticalExtensionOIDs.isEmpty()) {
                                    final Iterator<String> iterator3 = criticalExtensionOIDs.iterator();
                                    while (iterator3.hasNext()) {
                                        this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.unknownCriticalExt", new Object[] { new ASN1ObjectIdentifier(iterator3.next()) }), n);
                                    }
                                }
                                break Label_0468;
                            }
                            catch (CertPathValidatorException ex2) {
                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certPathCheckerError", new Object[] { ex2.getMessage(), ex2, ex2.getClass().getName() }), ex2);
                            }
                            break Label_0455;
                        }
                        --n;
                        continue;
                    }
                    final CertPathReviewerException ex3;
                    this.addError(ex3.getErrorMessage(), ex3.getIndex());
                    break;
                }
                return;
            }
            catch (CertPathReviewerException ex4) {}
            final CertPathReviewerException ex4;
            final CertPathReviewerException ex3 = ex4;
            continue Label_0455;
        }
    }
    
    private void checkNameConstraints() {
        while (true) {
            final PKIXNameConstraintValidator pkixNameConstraintValidator = new PKIXNameConstraintValidator();
            while (true) {
                int n = 0;
                Label_0509: {
                    try {
                        n = this.certs.size() - 1;
                        if (n > 0) {
                            final X509Certificate x509Certificate = this.certs.get(n);
                            final boolean selfIssued = CertPathValidatorUtilities.isSelfIssued(x509Certificate);
                            final int n2 = 0;
                            Label_0382: {
                                if (selfIssued) {
                                    break Label_0382;
                                }
                                Object o = CertPathValidatorUtilities.getSubjectPrincipal(x509Certificate);
                                final ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(((X500Principal)o).getEncoded()));
                                try {
                                    final ASN1Sequence asn1Sequence = (ASN1Sequence)asn1InputStream.readObject();
                                    try {
                                        pkixNameConstraintValidator.checkPermittedDN(asn1Sequence);
                                        try {
                                            pkixNameConstraintValidator.checkExcludedDN(asn1Sequence);
                                            try {
                                                final ASN1Sequence asn1Sequence2 = (ASN1Sequence)CertPathValidatorUtilities.getExtensionValue(x509Certificate, PKIXCertPathReviewer.SUBJECT_ALTERNATIVE_NAME);
                                                if (asn1Sequence2 != null) {
                                                    int i = 0;
                                                    while (i < asn1Sequence2.size()) {
                                                        o = GeneralName.getInstance(asn1Sequence2.getObjectAt(i));
                                                        try {
                                                            pkixNameConstraintValidator.checkPermitted((GeneralName)o);
                                                            pkixNameConstraintValidator.checkExcluded((GeneralName)o);
                                                            ++i;
                                                        }
                                                        catch (PKIXNameConstraintValidatorException ex) {
                                                            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.notPermittedEmail", new Object[] { new UntrustedInput(o) }), ex, this.certPath, n);
                                                        }
                                                    }
                                                    break Label_0382;
                                                }
                                                break Label_0382;
                                            }
                                            catch (AnnotatedException ex3) {
                                                final String s = "org.spongycastle.x509.CertPathReviewerMessages";
                                                final String s2 = "CertPathReviewer.subjAltNameExtError";
                                                final ErrorBundle errorBundle = new ErrorBundle(s, s2);
                                                final AnnotatedException ex2 = ex3;
                                                final PKIXCertPathReviewer pkixCertPathReviewer = this;
                                                final CertPath certPath = pkixCertPathReviewer.certPath;
                                                final int n3 = n;
                                                throw new CertPathReviewerException(errorBundle, ex2, certPath, n3);
                                            }
                                        }
                                        catch (PKIXNameConstraintValidatorException ex6) {
                                            final String s3 = "org.spongycastle.x509.CertPathReviewerMessages";
                                            final String s4 = "CertPathReviewer.excludedDN";
                                            final int n4 = 1;
                                            final Object[] array = new Object[n4];
                                            final int n5 = 0;
                                            final GeneralName generalName = (GeneralName)o;
                                            final String s5 = ((X500Principal)generalName).getName();
                                            final UntrustedInput untrustedInput = new UntrustedInput(s5);
                                            array[n5] = untrustedInput;
                                            final ErrorBundle errorBundle2 = new ErrorBundle(s3, s4, array);
                                            final PKIXNameConstraintValidatorException ex5 = ex6;
                                            final PKIXCertPathReviewer pkixCertPathReviewer2 = this;
                                            final CertPath certPath2 = pkixCertPathReviewer2.certPath;
                                            final int n6 = n;
                                            throw new CertPathReviewerException(errorBundle2, ex5, certPath2, n6);
                                        }
                                    }
                                    catch (PKIXNameConstraintValidatorException ex9) {
                                        final String s6 = "org.spongycastle.x509.CertPathReviewerMessages";
                                        final String s7 = "CertPathReviewer.notPermittedDN";
                                        final int n7 = 1;
                                        final Object[] array2 = new Object[n7];
                                        final int n8 = 0;
                                        final GeneralName generalName2 = (GeneralName)o;
                                        final String s8 = ((X500Principal)generalName2).getName();
                                        final UntrustedInput untrustedInput2 = new UntrustedInput(s8);
                                        array2[n8] = untrustedInput2;
                                        final ErrorBundle errorBundle3 = new ErrorBundle(s6, s7, array2);
                                        final PKIXNameConstraintValidatorException ex8 = ex9;
                                        final PKIXCertPathReviewer pkixCertPathReviewer3 = this;
                                        final CertPath certPath3 = pkixCertPathReviewer3.certPath;
                                        final int n9 = n;
                                        throw new CertPathReviewerException(errorBundle3, ex8, certPath3, n9);
                                    }
                                }
                                catch (IOException ex12) {
                                    final String s9 = "org.spongycastle.x509.CertPathReviewerMessages";
                                    final String s10 = "CertPathReviewer.ncSubjectNameError";
                                    final int n10 = 1;
                                    final Object[] array3 = new Object[n10];
                                    final int n11 = 0;
                                    final GeneralName generalName3 = (GeneralName)o;
                                    final UntrustedInput untrustedInput3 = new UntrustedInput(generalName3);
                                    array3[n11] = untrustedInput3;
                                    final ErrorBundle errorBundle4 = new ErrorBundle(s9, s10, array3);
                                    final IOException ex11 = ex12;
                                    final PKIXCertPathReviewer pkixCertPathReviewer4 = this;
                                    final CertPath certPath4 = pkixCertPathReviewer4.certPath;
                                    final int n12 = n;
                                    throw new CertPathReviewerException(errorBundle4, ex11, certPath4, n12);
                                }
                                try {
                                    final String s = "org.spongycastle.x509.CertPathReviewerMessages";
                                    final String s2 = "CertPathReviewer.subjAltNameExtError";
                                    final ErrorBundle errorBundle = new ErrorBundle(s, s2);
                                    final AnnotatedException ex3;
                                    final AnnotatedException ex2 = ex3;
                                    final PKIXCertPathReviewer pkixCertPathReviewer = this;
                                    final CertPath certPath = pkixCertPathReviewer.certPath;
                                    final int n3 = n;
                                    throw new CertPathReviewerException(errorBundle, ex2, certPath, n3);
                                    try {
                                        final String s3 = "org.spongycastle.x509.CertPathReviewerMessages";
                                        final String s4 = "CertPathReviewer.excludedDN";
                                        final int n4 = 1;
                                        final Object[] array = new Object[n4];
                                        final int n5 = 0;
                                        final GeneralName generalName = (GeneralName)o;
                                        final String s5 = ((X500Principal)generalName).getName();
                                        final UntrustedInput untrustedInput = new UntrustedInput(s5);
                                        array[n5] = untrustedInput;
                                        final ErrorBundle errorBundle2 = new ErrorBundle(s3, s4, array);
                                        final PKIXNameConstraintValidatorException ex6;
                                        final PKIXNameConstraintValidatorException ex5 = ex6;
                                        final PKIXCertPathReviewer pkixCertPathReviewer2 = this;
                                        final CertPath certPath2 = pkixCertPathReviewer2.certPath;
                                        final int n6 = n;
                                        throw new CertPathReviewerException(errorBundle2, ex5, certPath2, n6);
                                        try {
                                            final String s6 = "org.spongycastle.x509.CertPathReviewerMessages";
                                            final String s7 = "CertPathReviewer.notPermittedDN";
                                            final int n7 = 1;
                                            final Object[] array2 = new Object[n7];
                                            final int n8 = 0;
                                            final GeneralName generalName2 = (GeneralName)o;
                                            final String s8 = ((X500Principal)generalName2).getName();
                                            final UntrustedInput untrustedInput2 = new UntrustedInput(s8);
                                            array2[n8] = untrustedInput2;
                                            final ErrorBundle errorBundle3 = new ErrorBundle(s6, s7, array2);
                                            final PKIXNameConstraintValidatorException ex9;
                                            final PKIXNameConstraintValidatorException ex8 = ex9;
                                            final PKIXCertPathReviewer pkixCertPathReviewer3 = this;
                                            final CertPath certPath3 = pkixCertPathReviewer3.certPath;
                                            final int n9 = n;
                                            throw new CertPathReviewerException(errorBundle3, ex8, certPath3, n9);
                                            try {
                                                final String s9 = "org.spongycastle.x509.CertPathReviewerMessages";
                                                final String s10 = "CertPathReviewer.ncSubjectNameError";
                                                final int n10 = 1;
                                                final Object[] array3 = new Object[n10];
                                                final int n11 = 0;
                                                final GeneralName generalName3 = (GeneralName)o;
                                                final UntrustedInput untrustedInput3 = new UntrustedInput(generalName3);
                                                array3[n11] = untrustedInput3;
                                                final ErrorBundle errorBundle4 = new ErrorBundle(s9, s10, array3);
                                                final IOException ex12;
                                                final IOException ex11 = ex12;
                                                final PKIXCertPathReviewer pkixCertPathReviewer4 = this;
                                                final CertPath certPath4 = pkixCertPathReviewer4.certPath;
                                                final int n12 = n;
                                                throw new CertPathReviewerException(errorBundle4, ex11, certPath4, n12);
                                                try {
                                                    final ASN1Sequence asn1Sequence3 = (ASN1Sequence)CertPathValidatorUtilities.getExtensionValue(x509Certificate, PKIXCertPathReviewer.NAME_CONSTRAINTS);
                                                    if (asn1Sequence3 == null) {
                                                        break Label_0509;
                                                    }
                                                    final NameConstraints instance = NameConstraints.getInstance(asn1Sequence3);
                                                    final GeneralSubtree[] permittedSubtrees = instance.getPermittedSubtrees();
                                                    if (permittedSubtrees != null) {
                                                        pkixNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                                                    }
                                                    final GeneralSubtree[] excludedSubtrees = instance.getExcludedSubtrees();
                                                    if (excludedSubtrees != null) {
                                                        for (int j = n2; j != excludedSubtrees.length; ++j) {
                                                            pkixNameConstraintValidator.addExcludedSubtree(excludedSubtrees[j]);
                                                        }
                                                    }
                                                    break Label_0509;
                                                }
                                                catch (AnnotatedException ex14) {
                                                    throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.ncExtError"), ex14, this.certPath, n);
                                                }
                                            }
                                            catch (CertPathReviewerException ex15) {
                                                this.addError(ex15.getErrorMessage(), ex15.getIndex());
                                            }
                                        }
                                        catch (CertPathReviewerException ex16) {}
                                    }
                                    catch (CertPathReviewerException ex17) {}
                                }
                                catch (CertPathReviewerException ex18) {}
                            }
                        }
                    }
                    catch (CertPathReviewerException ex19) {}
                    break;
                }
                --n;
                continue;
            }
        }
    }
    
    private void checkPathLength() {
        int n = this.n;
        int i = this.certs.size() - 1;
        int n2 = 0;
        while (i > 0) {
            final X509Certificate x509Certificate = this.certs.get(i);
            int n3 = n;
            int n4 = n2;
            if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate)) {
                if (n <= 0) {
                    this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.pathLengthExtended"));
                }
                n3 = n - 1;
                n4 = n2 + 1;
            }
            BasicConstraints instance;
            try {
                instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, PKIXCertPathReviewer.BASIC_CONSTRAINTS));
            }
            catch (AnnotatedException ex) {
                this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.processLengthConstError"), i);
                instance = null;
            }
            n = n3;
            if (instance != null) {
                final BigInteger pathLenConstraint = instance.getPathLenConstraint();
                n = n3;
                if (pathLenConstraint != null) {
                    final int intValue = pathLenConstraint.intValue();
                    if (intValue < (n = n3)) {
                        n = intValue;
                    }
                }
            }
            --i;
            n2 = n4;
        }
        this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.totalPathLength", new Object[] { Integers.valueOf(n2) }));
    }
    
    private void checkPolicy() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          12
        //     5: aload_0        
        //     6: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //     9: invokevirtual   java/security/cert/PKIXParameters.getInitialPolicies:()Ljava/util/Set;
        //    12: astore          11
        //    14: aload_0        
        //    15: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //    18: iconst_1       
        //    19: iadd           
        //    20: istore          7
        //    22: iload           7
        //    24: anewarray       Ljava/util/ArrayList;
        //    27: astore          19
        //    29: iconst_0       
        //    30: istore_1       
        //    31: iload_1        
        //    32: iload           7
        //    34: if_icmpge       55
        //    37: aload           19
        //    39: iload_1        
        //    40: new             Ljava/util/ArrayList;
        //    43: dup            
        //    44: invokespecial   java/util/ArrayList.<init>:()V
        //    47: aastore        
        //    48: iload_1        
        //    49: iconst_1       
        //    50: iadd           
        //    51: istore_1       
        //    52: goto            31
        //    55: new             Ljava/util/HashSet;
        //    58: dup            
        //    59: invokespecial   java/util/HashSet.<init>:()V
        //    62: astore          10
        //    64: aload           10
        //    66: ldc_w           "2.5.29.32.0"
        //    69: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //    74: pop            
        //    75: new             Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //    78: dup            
        //    79: new             Ljava/util/ArrayList;
        //    82: dup            
        //    83: invokespecial   java/util/ArrayList.<init>:()V
        //    86: iconst_0       
        //    87: aload           10
        //    89: aconst_null    
        //    90: new             Ljava/util/HashSet;
        //    93: dup            
        //    94: invokespecial   java/util/HashSet.<init>:()V
        //    97: ldc_w           "2.5.29.32.0"
        //   100: iconst_0       
        //   101: invokespecial   org/spongycastle/jce/provider/PKIXPolicyNode.<init>:(Ljava/util/List;ILjava/util/Set;Ljava/security/cert/PolicyNode;Ljava/util/Set;Ljava/lang/String;Z)V
        //   104: astore          10
        //   106: aload           19
        //   108: iconst_0       
        //   109: aaload         
        //   110: aload           10
        //   112: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   117: pop            
        //   118: aload_0        
        //   119: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //   122: invokevirtual   java/security/cert/PKIXParameters.isExplicitPolicyRequired:()Z
        //   125: ifeq            133
        //   128: iconst_0       
        //   129: istore_1       
        //   130: goto            140
        //   133: aload_0        
        //   134: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //   137: iconst_1       
        //   138: iadd           
        //   139: istore_1       
        //   140: aload_0        
        //   141: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //   144: invokevirtual   java/security/cert/PKIXParameters.isAnyPolicyInhibited:()Z
        //   147: ifeq            155
        //   150: iconst_0       
        //   151: istore_2       
        //   152: goto            162
        //   155: aload_0        
        //   156: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //   159: iconst_1       
        //   160: iadd           
        //   161: istore_2       
        //   162: aload_0        
        //   163: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //   166: invokevirtual   java/security/cert/PKIXParameters.isPolicyMappingInhibited:()Z
        //   169: ifeq            177
        //   172: iconst_0       
        //   173: istore_3       
        //   174: goto            184
        //   177: aload_0        
        //   178: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //   181: iconst_1       
        //   182: iadd           
        //   183: istore_3       
        //   184: aload_0        
        //   185: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certs:Ljava/util/List;
        //   188: invokeinterface java/util/List.size:()I
        //   193: iconst_1       
        //   194: isub           
        //   195: istore          8
        //   197: aconst_null    
        //   198: astore          15
        //   200: aconst_null    
        //   201: astore          13
        //   203: iload           8
        //   205: iflt            1896
        //   208: aload_0        
        //   209: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //   212: iload           8
        //   214: isub           
        //   215: istore          6
        //   217: aload_0        
        //   218: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certs:Ljava/util/List;
        //   221: iload           8
        //   223: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   228: checkcast       Ljava/security/cert/X509Certificate;
        //   231: astore          18
        //   233: aload           18
        //   235: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.CERTIFICATE_POLICIES:Ljava/lang/String;
        //   238: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //   241: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //   244: astore          20
        //   246: aload           20
        //   248: ifnull          2751
        //   251: aload           10
        //   253: ifnull          2751
        //   256: aload           20
        //   258: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjects:()Ljava/util/Enumeration;
        //   261: astore          13
        //   263: new             Ljava/util/HashSet;
        //   266: dup            
        //   267: invokespecial   java/util/HashSet.<init>:()V
        //   270: astore          17
        //   272: aload           13
        //   274: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //   279: ifeq            400
        //   282: aload           13
        //   284: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //   289: invokestatic    org/spongycastle/asn1/x509/PolicyInformation.getInstance:(Ljava/lang/Object;)Lorg/spongycastle/asn1/x509/PolicyInformation;
        //   292: astore          16
        //   294: aload           16
        //   296: invokevirtual   org/spongycastle/asn1/x509/PolicyInformation.getPolicyIdentifier:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   299: astore          14
        //   301: aload           17
        //   303: aload           14
        //   305: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   308: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   313: pop            
        //   314: ldc_w           "2.5.29.32.0"
        //   317: aload           14
        //   319: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   322: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   325: istore          9
        //   327: iload           9
        //   329: ifne            2664
        //   332: aload           16
        //   334: invokevirtual   org/spongycastle/asn1/x509/PolicyInformation.getPolicyQualifiers:()Lorg/spongycastle/asn1/ASN1Sequence;
        //   337: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getQualifierSet:(Lorg/spongycastle/asn1/ASN1Sequence;)Ljava/util/Set;
        //   340: astore          16
        //   342: iload           6
        //   344: aload           19
        //   346: aload           14
        //   348: aload           16
        //   350: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.processCertD1i:(I[Ljava/util/List;Lorg/spongycastle/asn1/ASN1ObjectIdentifier;Ljava/util/Set;)Z
        //   353: ifne            2664
        //   356: iload           6
        //   358: aload           19
        //   360: aload           14
        //   362: aload           16
        //   364: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.processCertD1ii:(I[Ljava/util/List;Lorg/spongycastle/asn1/ASN1ObjectIdentifier;Ljava/util/Set;)V
        //   367: goto            2664
        //   370: astore          10
        //   372: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //   375: dup            
        //   376: new             Lorg/spongycastle/i18n/ErrorBundle;
        //   379: dup            
        //   380: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //   382: ldc_w           "CertPathReviewer.policyQualifierError"
        //   385: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   388: aload           10
        //   390: aload_0        
        //   391: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //   394: iload           8
        //   396: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //   399: athrow         
        //   400: aload           12
        //   402: astore          13
        //   404: aload           11
        //   406: astore          14
        //   408: aload           10
        //   410: astore          16
        //   412: aload           15
        //   414: ifnull          2667
        //   417: aload           15
        //   419: ldc_w           "2.5.29.32.0"
        //   422: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //   427: ifeq            433
        //   430: goto            2667
        //   433: aload           15
        //   435: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   440: astore          12
        //   442: new             Ljava/util/HashSet;
        //   445: dup            
        //   446: invokespecial   java/util/HashSet.<init>:()V
        //   449: astore          11
        //   451: aload           11
        //   453: astore          10
        //   455: aload           12
        //   457: invokeinterface java/util/Iterator.hasNext:()Z
        //   462: ifeq            499
        //   465: aload           12
        //   467: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   472: astore          10
        //   474: aload           17
        //   476: aload           10
        //   478: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //   483: ifeq            451
        //   486: aload           11
        //   488: aload           10
        //   490: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   495: pop            
        //   496: goto            451
        //   499: iload_2        
        //   500: ifgt            534
        //   503: aload           10
        //   505: astore          11
        //   507: iload_2        
        //   508: istore          4
        //   510: iload           6
        //   512: aload_0        
        //   513: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //   516: if_icmpge       2700
        //   519: aload           10
        //   521: astore          11
        //   523: iload_2        
        //   524: istore          4
        //   526: aload           18
        //   528: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.isSelfIssued:(Ljava/security/cert/X509Certificate;)Z
        //   531: ifeq            2700
        //   534: aload           20
        //   536: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjects:()Ljava/util/Enumeration;
        //   539: astore          12
        //   541: aload           10
        //   543: astore          11
        //   545: iload_2        
        //   546: istore          4
        //   548: aload           12
        //   550: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //   555: ifeq            2700
        //   558: aload           12
        //   560: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //   565: invokestatic    org/spongycastle/asn1/x509/PolicyInformation.getInstance:(Ljava/lang/Object;)Lorg/spongycastle/asn1/x509/PolicyInformation;
        //   568: astore          11
        //   570: ldc_w           "2.5.29.32.0"
        //   573: aload           11
        //   575: invokevirtual   org/spongycastle/asn1/x509/PolicyInformation.getPolicyIdentifier:()Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   578: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   581: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   584: istore          9
        //   586: iload           9
        //   588: ifeq            541
        //   591: aload           11
        //   593: invokevirtual   org/spongycastle/asn1/x509/PolicyInformation.getPolicyQualifiers:()Lorg/spongycastle/asn1/ASN1Sequence;
        //   596: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getQualifierSet:(Lorg/spongycastle/asn1/ASN1Sequence;)Ljava/util/Set;
        //   599: astore          21
        //   601: aload           19
        //   603: iload           6
        //   605: iconst_1       
        //   606: isub           
        //   607: aaload         
        //   608: astore          17
        //   610: iconst_0       
        //   611: istore          5
        //   613: aload           10
        //   615: astore          12
        //   617: aload           12
        //   619: astore          11
        //   621: iload_2        
        //   622: istore          4
        //   624: iload           5
        //   626: aload           17
        //   628: invokeinterface java/util/List.size:()I
        //   633: if_icmpge       2700
        //   636: aload           17
        //   638: iload           5
        //   640: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   645: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //   648: astore          22
        //   650: aload           22
        //   652: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getExpectedPolicies:()Ljava/util/Set;
        //   655: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   660: astore          15
        //   662: aload           17
        //   664: astore          10
        //   666: aload           12
        //   668: astore          11
        //   670: aload           15
        //   672: invokeinterface java/util/Iterator.hasNext:()Z
        //   677: ifeq            2683
        //   680: aload           15
        //   682: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   687: astore          12
        //   689: aload           12
        //   691: instanceof      Ljava/lang/String;
        //   694: ifeq            707
        //   697: aload           12
        //   699: checkcast       Ljava/lang/String;
        //   702: astore          12
        //   704: goto            725
        //   707: aload           12
        //   709: instanceof      Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   712: ifeq            2680
        //   715: aload           12
        //   717: checkcast       Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   720: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   723: astore          12
        //   725: aload           22
        //   727: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getChildren:()Ljava/util/Iterator;
        //   730: astore          17
        //   732: iconst_0       
        //   733: istore          4
        //   735: aload           17
        //   737: invokeinterface java/util/Iterator.hasNext:()Z
        //   742: ifeq            772
        //   745: aload           12
        //   747: aload           17
        //   749: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   754: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //   757: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //   760: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   763: ifeq            2674
        //   766: iconst_1       
        //   767: istore          4
        //   769: goto            2674
        //   772: iload           4
        //   774: ifne            2677
        //   777: new             Ljava/util/HashSet;
        //   780: dup            
        //   781: invokespecial   java/util/HashSet.<init>:()V
        //   784: astore          17
        //   786: aload           17
        //   788: aload           12
        //   790: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   795: pop            
        //   796: new             Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //   799: dup            
        //   800: new             Ljava/util/ArrayList;
        //   803: dup            
        //   804: invokespecial   java/util/ArrayList.<init>:()V
        //   807: iload           6
        //   809: aload           17
        //   811: aload           22
        //   813: aload           21
        //   815: aload           12
        //   817: iconst_0       
        //   818: invokespecial   org/spongycastle/jce/provider/PKIXPolicyNode.<init>:(Ljava/util/List;ILjava/util/Set;Ljava/security/cert/PolicyNode;Ljava/util/Set;Ljava/lang/String;Z)V
        //   821: astore          12
        //   823: aload           22
        //   825: aload           12
        //   827: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.addChild:(Lorg/spongycastle/jce/provider/PKIXPolicyNode;)V
        //   830: aload           19
        //   832: iload           6
        //   834: aaload         
        //   835: aload           12
        //   837: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   842: pop            
        //   843: goto            2677
        //   846: astore          10
        //   848: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //   851: dup            
        //   852: new             Lorg/spongycastle/i18n/ErrorBundle;
        //   855: dup            
        //   856: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //   858: ldc_w           "CertPathReviewer.policyQualifierError"
        //   861: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   864: aload           10
        //   866: aload_0        
        //   867: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //   870: iload           8
        //   872: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //   875: athrow         
        //   876: iload           6
        //   878: iconst_1       
        //   879: isub           
        //   880: istore          4
        //   882: aload           16
        //   884: astore          10
        //   886: iload           4
        //   888: iflt            970
        //   891: aload           19
        //   893: iload           4
        //   895: aaload         
        //   896: astore          15
        //   898: iconst_0       
        //   899: istore          5
        //   901: aload           10
        //   903: astore          12
        //   905: iload           5
        //   907: aload           15
        //   909: invokeinterface java/util/List.size:()I
        //   914: if_icmpge       2719
        //   917: aload           15
        //   919: iload           5
        //   921: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   926: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //   929: astore          16
        //   931: aload           10
        //   933: astore          12
        //   935: aload           16
        //   937: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.hasChildren:()Z
        //   940: ifne            2706
        //   943: aload           10
        //   945: aload           19
        //   947: aload           16
        //   949: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.removePolicyNode:(Lorg/spongycastle/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/spongycastle/jce/provider/PKIXPolicyNode;)Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //   952: astore          10
        //   954: aload           10
        //   956: astore          12
        //   958: aload           10
        //   960: ifnonnull       2706
        //   963: aload           10
        //   965: astore          12
        //   967: goto            2719
        //   970: aload           18
        //   972: invokevirtual   java/security/cert/X509Certificate.getCriticalExtensionOIDs:()Ljava/util/Set;
        //   975: astore          12
        //   977: aload           12
        //   979: ifnull          2732
        //   982: aload           12
        //   984: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.CERTIFICATE_POLICIES:Ljava/lang/String;
        //   987: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //   992: istore          9
        //   994: aload           19
        //   996: iload           6
        //   998: aaload         
        //   999: astore          12
        //  1001: iconst_0       
        //  1002: istore          4
        //  1004: iload           4
        //  1006: aload           12
        //  1008: invokeinterface java/util/List.size:()I
        //  1013: if_icmpge       2732
        //  1016: aload           12
        //  1018: iload           4
        //  1020: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  1025: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  1028: iload           9
        //  1030: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.setCritical:(Z)V
        //  1033: iload           4
        //  1035: iconst_1       
        //  1036: iadd           
        //  1037: istore          4
        //  1039: goto            1004
        //  1042: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1045: dup            
        //  1046: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1049: dup            
        //  1050: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1052: ldc_w           "CertPathReviewer.noValidPolicyTree"
        //  1055: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1058: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;)V
        //  1061: athrow         
        //  1062: aload_0        
        //  1063: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //  1066: istore          4
        //  1068: iload           6
        //  1070: iload           4
        //  1072: if_icmpeq       2809
        //  1075: aload           18
        //  1077: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.POLICY_MAPPINGS:Ljava/lang/String;
        //  1080: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //  1083: astore          13
        //  1085: aload           13
        //  1087: ifnull          1233
        //  1090: aload           13
        //  1092: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1095: astore          14
        //  1097: iconst_0       
        //  1098: istore          4
        //  1100: iload           4
        //  1102: aload           14
        //  1104: invokevirtual   org/spongycastle/asn1/ASN1Sequence.size:()I
        //  1107: if_icmpge       1233
        //  1110: aload           14
        //  1112: iload           4
        //  1114: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1117: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1120: astore          17
        //  1122: aload           17
        //  1124: iconst_0       
        //  1125: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1128: checkcast       Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //  1131: astore          16
        //  1133: aload           17
        //  1135: iconst_1       
        //  1136: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1139: checkcast       Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //  1142: astore          17
        //  1144: ldc_w           "2.5.29.32.0"
        //  1147: aload           16
        //  1149: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //  1152: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1155: ifne            1207
        //  1158: ldc_w           "2.5.29.32.0"
        //  1161: aload           17
        //  1163: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //  1166: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1169: ifne            1181
        //  1172: iload           4
        //  1174: iconst_1       
        //  1175: iadd           
        //  1176: istore          4
        //  1178: goto            1100
        //  1181: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1184: dup            
        //  1185: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1188: dup            
        //  1189: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1191: ldc_w           "CertPathReviewer.invalidPolicyMapping"
        //  1194: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1197: aload_0        
        //  1198: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1201: iload           8
        //  1203: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  1206: athrow         
        //  1207: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1210: dup            
        //  1211: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1214: dup            
        //  1215: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1217: ldc_w           "CertPathReviewer.invalidPolicyMapping"
        //  1220: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1223: aload_0        
        //  1224: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1227: iload           8
        //  1229: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  1232: athrow         
        //  1233: aload           13
        //  1235: ifnull          2806
        //  1238: aload           13
        //  1240: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1243: astore          13
        //  1245: new             Ljava/util/HashMap;
        //  1248: dup            
        //  1249: invokespecial   java/util/HashMap.<init>:()V
        //  1252: astore          14
        //  1254: new             Ljava/util/HashSet;
        //  1257: dup            
        //  1258: invokespecial   java/util/HashSet.<init>:()V
        //  1261: astore          16
        //  1263: iconst_0       
        //  1264: istore          4
        //  1266: iload           4
        //  1268: aload           13
        //  1270: invokevirtual   org/spongycastle/asn1/ASN1Sequence.size:()I
        //  1273: if_icmpge       1395
        //  1276: aload           13
        //  1278: iload           4
        //  1280: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1283: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1286: astore          20
        //  1288: aload           20
        //  1290: iconst_0       
        //  1291: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1294: checkcast       Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //  1297: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //  1300: astore          17
        //  1302: aload           20
        //  1304: iconst_1       
        //  1305: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjectAt:(I)Lorg/spongycastle/asn1/ASN1Encodable;
        //  1308: checkcast       Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //  1311: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //  1314: astore          20
        //  1316: aload           14
        //  1318: aload           17
        //  1320: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //  1325: ifne            1372
        //  1328: new             Ljava/util/HashSet;
        //  1331: dup            
        //  1332: invokespecial   java/util/HashSet.<init>:()V
        //  1335: astore          21
        //  1337: aload           21
        //  1339: aload           20
        //  1341: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  1346: pop            
        //  1347: aload           14
        //  1349: aload           17
        //  1351: aload           21
        //  1353: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1358: pop            
        //  1359: aload           16
        //  1361: aload           17
        //  1363: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  1368: pop            
        //  1369: goto            2787
        //  1372: aload           14
        //  1374: aload           17
        //  1376: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  1381: checkcast       Ljava/util/Set;
        //  1384: aload           20
        //  1386: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  1391: pop            
        //  1392: goto            2787
        //  1395: aload           16
        //  1397: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  1402: astore          16
        //  1404: aload           16
        //  1406: invokeinterface java/util/Iterator.hasNext:()Z
        //  1411: ifeq            2803
        //  1414: aload           16
        //  1416: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1421: checkcast       Ljava/lang/String;
        //  1424: astore          17
        //  1426: iload_3        
        //  1427: ifle            1509
        //  1430: iload           6
        //  1432: aload           19
        //  1434: aload           17
        //  1436: aload           14
        //  1438: aload           18
        //  1440: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.prepareNextCertB1:(I[Ljava/util/List;Ljava/lang/String;Ljava/util/Map;Ljava/security/cert/X509Certificate;)V
        //  1443: aload           11
        //  1445: astore          13
        //  1447: goto            2796
        //  1450: astore          10
        //  1452: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1455: dup            
        //  1456: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1459: dup            
        //  1460: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1462: ldc_w           "CertPathReviewer.policyQualifierError"
        //  1465: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1468: aload           10
        //  1470: aload_0        
        //  1471: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1474: iload           8
        //  1476: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //  1479: athrow         
        //  1480: astore          11
        //  1482: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1485: dup            
        //  1486: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1489: dup            
        //  1490: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1492: aload           10
        //  1494: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1497: aload           11
        //  1499: aload_0        
        //  1500: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1503: iload           8
        //  1505: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //  1508: athrow         
        //  1509: aload           11
        //  1511: astore          13
        //  1513: iload_3        
        //  1514: ifgt            2796
        //  1517: iload           6
        //  1519: aload           19
        //  1521: aload           17
        //  1523: aload           11
        //  1525: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.prepareNextCertB2:(I[Ljava/util/List;Ljava/lang/String;Lorg/spongycastle/jce/provider/PKIXPolicyNode;)Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  1528: astore          13
        //  1530: goto            2796
        //  1533: aload           18
        //  1535: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.isSelfIssued:(Ljava/security/cert/X509Certificate;)Z
        //  1538: istore          9
        //  1540: iload_1        
        //  1541: istore          5
        //  1543: iload_3        
        //  1544: istore          6
        //  1546: iload           9
        //  1548: ifne            1591
        //  1551: iload_1        
        //  1552: istore          4
        //  1554: iload_1        
        //  1555: ifeq            1563
        //  1558: iload_1        
        //  1559: iconst_1       
        //  1560: isub           
        //  1561: istore          4
        //  1563: iload_3        
        //  1564: istore_1       
        //  1565: iload_3        
        //  1566: ifeq            1573
        //  1569: iload_3        
        //  1570: iconst_1       
        //  1571: isub           
        //  1572: istore_1       
        //  1573: iload           4
        //  1575: istore          5
        //  1577: iload_1        
        //  1578: istore          6
        //  1580: iload_2        
        //  1581: ifeq            1591
        //  1584: iload_2        
        //  1585: iconst_1       
        //  1586: isub           
        //  1587: istore_2       
        //  1588: goto            1598
        //  1591: iload           6
        //  1593: istore_1       
        //  1594: iload           5
        //  1596: istore          4
        //  1598: aload           18
        //  1600: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.POLICY_CONSTRAINTS:Ljava/lang/String;
        //  1603: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //  1606: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1609: astore          13
        //  1611: iload           4
        //  1613: istore          5
        //  1615: iload_1        
        //  1616: istore_3       
        //  1617: aload           13
        //  1619: ifnull          1723
        //  1622: aload           13
        //  1624: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjects:()Ljava/util/Enumeration;
        //  1627: astore          13
        //  1629: iload           4
        //  1631: istore          5
        //  1633: iload_1        
        //  1634: istore_3       
        //  1635: aload           13
        //  1637: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //  1642: ifeq            1723
        //  1645: aload           13
        //  1647: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //  1652: checkcast       Lorg/spongycastle/asn1/ASN1TaggedObject;
        //  1655: astore          14
        //  1657: aload           14
        //  1659: invokevirtual   org/spongycastle/asn1/ASN1TaggedObject.getTagNo:()I
        //  1662: istore_3       
        //  1663: iload_3        
        //  1664: ifeq            1698
        //  1667: iload_3        
        //  1668: iconst_1       
        //  1669: if_icmpeq       1675
        //  1672: goto            1629
        //  1675: aload           14
        //  1677: iconst_0       
        //  1678: invokestatic    org/spongycastle/asn1/ASN1Integer.getInstance:(Lorg/spongycastle/asn1/ASN1TaggedObject;Z)Lorg/spongycastle/asn1/ASN1Integer;
        //  1681: invokevirtual   org/spongycastle/asn1/ASN1Integer.getValue:()Ljava/math/BigInteger;
        //  1684: invokevirtual   java/math/BigInteger.intValue:()I
        //  1687: istore_3       
        //  1688: iload_3        
        //  1689: iload_1        
        //  1690: if_icmpge       1629
        //  1693: iload_3        
        //  1694: istore_1       
        //  1695: goto            1629
        //  1698: aload           14
        //  1700: iconst_0       
        //  1701: invokestatic    org/spongycastle/asn1/ASN1Integer.getInstance:(Lorg/spongycastle/asn1/ASN1TaggedObject;Z)Lorg/spongycastle/asn1/ASN1Integer;
        //  1704: invokevirtual   org/spongycastle/asn1/ASN1Integer.getValue:()Ljava/math/BigInteger;
        //  1707: invokevirtual   java/math/BigInteger.intValue:()I
        //  1710: istore_3       
        //  1711: iload_3        
        //  1712: iload           4
        //  1714: if_icmpge       1629
        //  1717: iload_3        
        //  1718: istore          4
        //  1720: goto            1629
        //  1723: aload           18
        //  1725: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.INHIBIT_ANY_POLICY:Ljava/lang/String;
        //  1728: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //  1731: checkcast       Lorg/spongycastle/asn1/ASN1Integer;
        //  1734: astore          13
        //  1736: iload_2        
        //  1737: istore_1       
        //  1738: aload           13
        //  1740: ifnull          1764
        //  1743: aload           13
        //  1745: invokevirtual   org/spongycastle/asn1/ASN1Integer.getValue:()Ljava/math/BigInteger;
        //  1748: invokevirtual   java/math/BigInteger.intValue:()I
        //  1751: istore          4
        //  1753: iload_2        
        //  1754: istore_1       
        //  1755: iload           4
        //  1757: iload_2        
        //  1758: if_icmpge       1764
        //  1761: iload           4
        //  1763: istore_1       
        //  1764: aload           11
        //  1766: astore          13
        //  1768: aload           10
        //  1770: astore          11
        //  1772: aload           13
        //  1774: astore          10
        //  1776: iload_1        
        //  1777: istore_2       
        //  1778: goto            2824
        //  1781: astore          10
        //  1783: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1786: dup            
        //  1787: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1790: dup            
        //  1791: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1793: ldc_w           "CertPathReviewer.policyInhibitExtError"
        //  1796: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1799: aload_0        
        //  1800: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1803: iload           8
        //  1805: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  1808: athrow         
        //  1809: astore          10
        //  1811: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1814: dup            
        //  1815: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1818: dup            
        //  1819: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1821: ldc_w           "CertPathReviewer.policyConstExtError"
        //  1824: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1827: aload_0        
        //  1828: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1831: iload           8
        //  1833: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  1836: athrow         
        //  1837: astore          10
        //  1839: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1842: dup            
        //  1843: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1846: dup            
        //  1847: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1849: ldc_w           "CertPathReviewer.policyMapExtError"
        //  1852: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1855: aload           10
        //  1857: aload_0        
        //  1858: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1861: iload           8
        //  1863: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //  1866: athrow         
        //  1867: astore          10
        //  1869: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  1872: dup            
        //  1873: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  1876: dup            
        //  1877: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  1879: aload           12
        //  1881: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1884: aload           10
        //  1886: aload_0        
        //  1887: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  1890: iload           8
        //  1892: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
        //  1895: athrow         
        //  1896: aload           13
        //  1898: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.isSelfIssued:(Ljava/security/cert/X509Certificate;)Z
        //  1901: istore          9
        //  1903: iload_1        
        //  1904: istore_2       
        //  1905: iload           9
        //  1907: ifne            1920
        //  1910: iload_1        
        //  1911: istore_2       
        //  1912: iload_1        
        //  1913: ifle            1920
        //  1916: iload_1        
        //  1917: iconst_1       
        //  1918: isub           
        //  1919: istore_2       
        //  1920: iload_2        
        //  1921: istore_1       
        //  1922: aload           13
        //  1924: getstatic       org/spongycastle/x509/PKIXCertPathReviewer.POLICY_CONSTRAINTS:Ljava/lang/String;
        //  1927: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //  1930: checkcast       Lorg/spongycastle/asn1/ASN1Sequence;
        //  1933: astore          12
        //  1935: iload_1        
        //  1936: istore_2       
        //  1937: aload           12
        //  1939: ifnull          2006
        //  1942: aload           12
        //  1944: invokevirtual   org/spongycastle/asn1/ASN1Sequence.getObjects:()Ljava/util/Enumeration;
        //  1947: astore          12
        //  1949: iload_1        
        //  1950: istore_2       
        //  1951: aload           12
        //  1953: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //  1958: ifeq            2006
        //  1961: aload           12
        //  1963: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //  1968: checkcast       Lorg/spongycastle/asn1/ASN1TaggedObject;
        //  1971: astore          13
        //  1973: aload           13
        //  1975: invokevirtual   org/spongycastle/asn1/ASN1TaggedObject.getTagNo:()I
        //  1978: ifeq            1984
        //  1981: goto            1949
        //  1984: aload           13
        //  1986: iconst_0       
        //  1987: invokestatic    org/spongycastle/asn1/ASN1Integer.getInstance:(Lorg/spongycastle/asn1/ASN1TaggedObject;Z)Lorg/spongycastle/asn1/ASN1Integer;
        //  1990: invokevirtual   org/spongycastle/asn1/ASN1Integer.getValue:()Ljava/math/BigInteger;
        //  1993: invokevirtual   java/math/BigInteger.intValue:()I
        //  1996: istore_2       
        //  1997: iload_2        
        //  1998: ifne            1949
        //  2001: iconst_0       
        //  2002: istore_1       
        //  2003: goto            1949
        //  2006: aload           10
        //  2008: ifnonnull       2053
        //  2011: aload_0        
        //  2012: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //  2015: invokevirtual   java/security/cert/PKIXParameters.isExplicitPolicyRequired:()Z
        //  2018: ifne            2027
        //  2021: aconst_null    
        //  2022: astore          10
        //  2024: goto            2919
        //  2027: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  2030: dup            
        //  2031: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  2034: dup            
        //  2035: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  2037: ldc_w           "CertPathReviewer.explicitPolicy"
        //  2040: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  2043: aload_0        
        //  2044: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  2047: iload           8
        //  2049: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  2052: athrow         
        //  2053: aload           11
        //  2055: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.isAnyPolicy:(Ljava/util/Set;)Z
        //  2058: ifeq            2332
        //  2061: aload_0        
        //  2062: getfield        org/spongycastle/x509/PKIXCertPathReviewer.pkixParams:Ljava/security/cert/PKIXParameters;
        //  2065: invokevirtual   java/security/cert/PKIXParameters.isExplicitPolicyRequired:()Z
        //  2068: ifeq            2884
        //  2071: aload           15
        //  2073: invokeinterface java/util/Set.isEmpty:()Z
        //  2078: ifne            2306
        //  2081: new             Ljava/util/HashSet;
        //  2084: dup            
        //  2085: invokespecial   java/util/HashSet.<init>:()V
        //  2088: astore          11
        //  2090: iconst_0       
        //  2091: istore_1       
        //  2092: iload_1        
        //  2093: iload           7
        //  2095: if_icmpge       2179
        //  2098: aload           19
        //  2100: iload_1        
        //  2101: aaload         
        //  2102: astore          12
        //  2104: iconst_0       
        //  2105: istore_3       
        //  2106: iload_3        
        //  2107: aload           12
        //  2109: invokeinterface java/util/List.size:()I
        //  2114: if_icmpge       2859
        //  2117: aload           12
        //  2119: iload_3        
        //  2120: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  2125: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2128: astore          13
        //  2130: ldc_w           "2.5.29.32.0"
        //  2133: aload           13
        //  2135: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //  2138: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2141: ifeq            2852
        //  2144: aload           13
        //  2146: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getChildren:()Ljava/util/Iterator;
        //  2149: astore          13
        //  2151: aload           13
        //  2153: invokeinterface java/util/Iterator.hasNext:()Z
        //  2158: ifeq            2852
        //  2161: aload           11
        //  2163: aload           13
        //  2165: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  2170: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  2175: pop            
        //  2176: goto            2151
        //  2179: aload           11
        //  2181: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  2186: astore          11
        //  2188: aload           11
        //  2190: invokeinterface java/util/Iterator.hasNext:()Z
        //  2195: ifeq            2222
        //  2198: aload           15
        //  2200: aload           11
        //  2202: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  2207: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2210: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //  2213: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //  2218: pop            
        //  2219: goto            2188
        //  2222: aload           10
        //  2224: ifnull          2884
        //  2227: aload_0        
        //  2228: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //  2231: istore_1       
        //  2232: iload_1        
        //  2233: iconst_1       
        //  2234: isub           
        //  2235: istore_1       
        //  2236: aload           10
        //  2238: astore          11
        //  2240: aload           11
        //  2242: astore          10
        //  2244: iload_1        
        //  2245: iflt            2919
        //  2248: aload           19
        //  2250: iload_1        
        //  2251: aaload         
        //  2252: astore          12
        //  2254: iconst_0       
        //  2255: istore_3       
        //  2256: iload_3        
        //  2257: aload           12
        //  2259: invokeinterface java/util/List.size:()I
        //  2264: if_icmpge       2877
        //  2267: aload           12
        //  2269: iload_3        
        //  2270: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  2275: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2278: astore          13
        //  2280: aload           11
        //  2282: astore          10
        //  2284: aload           13
        //  2286: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.hasChildren:()Z
        //  2289: ifne            2866
        //  2292: aload           11
        //  2294: aload           19
        //  2296: aload           13
        //  2298: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.removePolicyNode:(Lorg/spongycastle/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/spongycastle/jce/provider/PKIXPolicyNode;)Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2301: astore          10
        //  2303: goto            2866
        //  2306: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  2309: dup            
        //  2310: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  2313: dup            
        //  2314: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  2316: ldc_w           "CertPathReviewer.explicitPolicy"
        //  2319: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  2322: aload_0        
        //  2323: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  2326: iload           8
        //  2328: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  2331: athrow         
        //  2332: new             Ljava/util/HashSet;
        //  2335: dup            
        //  2336: invokespecial   java/util/HashSet.<init>:()V
        //  2339: astore          12
        //  2341: iconst_0       
        //  2342: istore_1       
        //  2343: iload_1        
        //  2344: iload           7
        //  2346: if_icmpge       2451
        //  2349: aload           19
        //  2351: iload_1        
        //  2352: aaload         
        //  2353: astore          13
        //  2355: iconst_0       
        //  2356: istore_3       
        //  2357: iload_3        
        //  2358: aload           13
        //  2360: invokeinterface java/util/List.size:()I
        //  2365: if_icmpge       2894
        //  2368: aload           13
        //  2370: iload_3        
        //  2371: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  2376: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2379: astore          14
        //  2381: ldc_w           "2.5.29.32.0"
        //  2384: aload           14
        //  2386: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //  2389: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2392: ifeq            2887
        //  2395: aload           14
        //  2397: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getChildren:()Ljava/util/Iterator;
        //  2400: astore          14
        //  2402: aload           14
        //  2404: invokeinterface java/util/Iterator.hasNext:()Z
        //  2409: ifeq            2887
        //  2412: aload           14
        //  2414: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  2419: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2422: astore          15
        //  2424: ldc_w           "2.5.29.32.0"
        //  2427: aload           15
        //  2429: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //  2432: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  2435: ifne            2402
        //  2438: aload           12
        //  2440: aload           15
        //  2442: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  2447: pop            
        //  2448: goto            2402
        //  2451: aload           12
        //  2453: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  2458: astore          13
        //  2460: aload           10
        //  2462: astore          12
        //  2464: aload           13
        //  2466: invokeinterface java/util/Iterator.hasNext:()Z
        //  2471: ifeq            2515
        //  2474: aload           13
        //  2476: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  2481: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2484: astore          10
        //  2486: aload           11
        //  2488: aload           10
        //  2490: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.getValidPolicy:()Ljava/lang/String;
        //  2493: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //  2498: ifne            2464
        //  2501: aload           12
        //  2503: aload           19
        //  2505: aload           10
        //  2507: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.removePolicyNode:(Lorg/spongycastle/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/spongycastle/jce/provider/PKIXPolicyNode;)Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2510: astore          12
        //  2512: goto            2464
        //  2515: aload           12
        //  2517: astore          10
        //  2519: aload           12
        //  2521: ifnull          2919
        //  2524: aload_0        
        //  2525: getfield        org/spongycastle/x509/PKIXCertPathReviewer.n:I
        //  2528: istore_1       
        //  2529: iload_1        
        //  2530: iconst_1       
        //  2531: isub           
        //  2532: istore_1       
        //  2533: aload           12
        //  2535: astore          10
        //  2537: iload_1        
        //  2538: iflt            2919
        //  2541: aload           19
        //  2543: iload_1        
        //  2544: aaload         
        //  2545: astore          11
        //  2547: iconst_0       
        //  2548: istore_3       
        //  2549: iload_3        
        //  2550: aload           11
        //  2552: invokeinterface java/util/List.size:()I
        //  2557: if_icmpge       2912
        //  2560: aload           11
        //  2562: iload_3        
        //  2563: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  2568: checkcast       Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2571: astore          13
        //  2573: aload           12
        //  2575: astore          10
        //  2577: aload           13
        //  2579: invokevirtual   org/spongycastle/jce/provider/PKIXPolicyNode.hasChildren:()Z
        //  2582: ifne            2901
        //  2585: aload           12
        //  2587: aload           19
        //  2589: aload           13
        //  2591: invokestatic    org/spongycastle/x509/PKIXCertPathReviewer.removePolicyNode:(Lorg/spongycastle/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/spongycastle/jce/provider/PKIXPolicyNode;)Lorg/spongycastle/jce/provider/PKIXPolicyNode;
        //  2594: astore          10
        //  2596: goto            2901
        //  2599: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  2602: dup            
        //  2603: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  2606: dup            
        //  2607: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  2609: ldc_w           "CertPathReviewer.invalidPolicy"
        //  2612: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  2615: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;)V
        //  2618: athrow         
        //  2619: astore          10
        //  2621: new             Lorg/spongycastle/x509/CertPathReviewerException;
        //  2624: dup            
        //  2625: new             Lorg/spongycastle/i18n/ErrorBundle;
        //  2628: dup            
        //  2629: ldc             "org.spongycastle.x509.CertPathReviewerMessages"
        //  2631: ldc_w           "CertPathReviewer.policyConstExtError"
        //  2634: invokespecial   org/spongycastle/i18n/ErrorBundle.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  2637: aload_0        
        //  2638: getfield        org/spongycastle/x509/PKIXCertPathReviewer.certPath:Ljava/security/cert/CertPath;
        //  2641: iload           8
        //  2643: invokespecial   org/spongycastle/x509/CertPathReviewerException.<init>:(Lorg/spongycastle/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
        //  2646: athrow         
        //  2647: astore          10
        //  2649: aload_0        
        //  2650: aload           10
        //  2652: invokevirtual   org/spongycastle/x509/CertPathReviewerException.getErrorMessage:()Lorg/spongycastle/i18n/ErrorBundle;
        //  2655: aload           10
        //  2657: invokevirtual   org/spongycastle/x509/CertPathReviewerException.getIndex:()I
        //  2660: invokevirtual   org/spongycastle/x509/PKIXCertPathReviewer.addError:(Lorg/spongycastle/i18n/ErrorBundle;I)V
        //  2663: return         
        //  2664: goto            272
        //  2667: aload           17
        //  2669: astore          10
        //  2671: goto            499
        //  2674: goto            735
        //  2677: goto            670
        //  2680: goto            670
        //  2683: iload           5
        //  2685: iconst_1       
        //  2686: iadd           
        //  2687: istore          5
        //  2689: aload           11
        //  2691: astore          12
        //  2693: aload           10
        //  2695: astore          17
        //  2697: goto            617
        //  2700: iload           4
        //  2702: istore_2       
        //  2703: goto            876
        //  2706: iload           5
        //  2708: iconst_1       
        //  2709: iadd           
        //  2710: istore          5
        //  2712: aload           12
        //  2714: astore          10
        //  2716: goto            901
        //  2719: aload           12
        //  2721: astore          10
        //  2723: iload           4
        //  2725: iconst_1       
        //  2726: isub           
        //  2727: istore          4
        //  2729: goto            886
        //  2732: aload           11
        //  2734: astore          15
        //  2736: aload           14
        //  2738: astore          12
        //  2740: aload           10
        //  2742: astore          11
        //  2744: aload           13
        //  2746: astore          10
        //  2748: goto            2767
        //  2751: aload           12
        //  2753: astore          13
        //  2755: aload           11
        //  2757: astore          12
        //  2759: aload           10
        //  2761: astore          11
        //  2763: aload           13
        //  2765: astore          10
        //  2767: aload           20
        //  2769: ifnonnull       2775
        //  2772: aconst_null    
        //  2773: astore          11
        //  2775: iload_1        
        //  2776: ifgt            1062
        //  2779: aload           11
        //  2781: ifnull          1042
        //  2784: goto            1062
        //  2787: iload           4
        //  2789: iconst_1       
        //  2790: iadd           
        //  2791: istore          4
        //  2793: goto            1266
        //  2796: aload           13
        //  2798: astore          11
        //  2800: goto            1404
        //  2803: goto            1533
        //  2806: goto            1533
        //  2809: aload           10
        //  2811: astore          13
        //  2813: aload           11
        //  2815: astore          10
        //  2817: iload_1        
        //  2818: istore          5
        //  2820: aload           13
        //  2822: astore          11
        //  2824: iload           8
        //  2826: iconst_1       
        //  2827: isub           
        //  2828: istore          8
        //  2830: aload           18
        //  2832: astore          13
        //  2834: aload           12
        //  2836: astore          14
        //  2838: aload           11
        //  2840: astore          12
        //  2842: aload           14
        //  2844: astore          11
        //  2846: iload           5
        //  2848: istore_1       
        //  2849: goto            203
        //  2852: iload_3        
        //  2853: iconst_1       
        //  2854: iadd           
        //  2855: istore_3       
        //  2856: goto            2106
        //  2859: iload_1        
        //  2860: iconst_1       
        //  2861: iadd           
        //  2862: istore_1       
        //  2863: goto            2092
        //  2866: iload_3        
        //  2867: iconst_1       
        //  2868: iadd           
        //  2869: istore_3       
        //  2870: aload           10
        //  2872: astore          11
        //  2874: goto            2256
        //  2877: iload_1        
        //  2878: iconst_1       
        //  2879: isub           
        //  2880: istore_1       
        //  2881: goto            2240
        //  2884: goto            2919
        //  2887: iload_3        
        //  2888: iconst_1       
        //  2889: iadd           
        //  2890: istore_3       
        //  2891: goto            2357
        //  2894: iload_1        
        //  2895: iconst_1       
        //  2896: iadd           
        //  2897: istore_1       
        //  2898: goto            2343
        //  2901: iload_3        
        //  2902: iconst_1       
        //  2903: iadd           
        //  2904: istore_3       
        //  2905: aload           10
        //  2907: astore          12
        //  2909: goto            2549
        //  2912: iload_1        
        //  2913: iconst_1       
        //  2914: isub           
        //  2915: istore_1       
        //  2916: goto            2533
        //  2919: iload_2        
        //  2920: ifgt            2663
        //  2923: aload           10
        //  2925: ifnull          2599
        //  2928: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  184    197    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  208    233    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  233    246    1867   1896   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  233    246    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  256    272    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  272    327    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  332    342    370    400    Ljava/security/cert/CertPathValidatorException;
        //  332    342    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  342    367    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  372    400    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  417    430    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  433    451    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  455    496    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  510    519    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  526    534    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  534    541    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  548    586    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  591    601    846    876    Ljava/security/cert/CertPathValidatorException;
        //  591    601    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  624    662    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  670    704    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  707    725    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  725    732    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  735    745    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  745    766    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  777    843    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  848    876    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  905    931    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  935    954    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  970    977    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  982    994    2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1004   1033   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1042   1062   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1062   1068   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1075   1085   1837   1867   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1075   1085   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1090   1097   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1100   1172   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1181   1207   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1207   1233   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1238   1263   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1266   1369   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1372   1392   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1395   1404   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1404   1426   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1430   1443   1480   1509   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1430   1443   1450   1480   Ljava/security/cert/CertPathValidatorException;
        //  1430   1443   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1452   1480   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1482   1509   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1517   1530   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1533   1540   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1598   1611   1809   1837   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1598   1611   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1622   1629   1809   1837   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1622   1629   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1635   1663   1809   1837   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1635   1663   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1675   1688   1809   1837   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1675   1688   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1698   1711   1809   1837   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1698   1711   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1723   1736   1781   1809   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1723   1736   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1743   1753   1781   1809   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1743   1753   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1783   1809   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1811   1837   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1839   1867   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1869   1896   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1896   1903   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1922   1935   2619   2647   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1922   1935   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1942   1949   2619   2647   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1942   1949   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1951   1981   2619   2647   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1951   1981   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  1984   1997   2619   2647   Lorg/spongycastle/jce/provider/AnnotatedException;
        //  1984   1997   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2011   2021   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2027   2053   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2053   2090   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2106   2151   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2151   2176   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2179   2188   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2188   2219   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2227   2232   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2256   2280   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2284   2303   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2306   2332   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2332   2341   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2357   2402   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2402   2448   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2451   2460   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2464   2512   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2524   2529   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2549   2573   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2577   2596   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2599   2619   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        //  2621   2647   2647   2663   Lorg/spongycastle/x509/CertPathReviewerException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_1509:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void checkSignatures() {
        this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certPathValidDate", new Object[] { new TrustedInput(this.validDate), new TrustedInput(new Date()) }));
        while (true) {
        Label_0512_Outer:
            while (true) {
                Object algorithmIdentifier = null;
                while (true) {
                    Label_0378: {
                        X500Principal x500Principal = null;
                        Label_0375: {
                            Label_1674: {
                                try {
                                    final X509Certificate x509Certificate = this.certs.get(this.certs.size() - 1);
                                    final Collection trustAnchors = this.getTrustAnchors(x509Certificate, this.pkixParams.getTrustAnchors());
                                    if (trustAnchors.size() > 1) {
                                        this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.conflictingTrustAnchors", new Object[] { Integers.valueOf(trustAnchors.size()), new UntrustedInput(x509Certificate.getIssuerX500Principal()) }));
                                        break Label_1674;
                                    }
                                    if (trustAnchors.isEmpty()) {
                                        this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noTrustAnchorFound", new Object[] { new UntrustedInput(x509Certificate.getIssuerX500Principal()), Integers.valueOf(this.pkixParams.getTrustAnchors().size()) }));
                                        break Label_1674;
                                    }
                                    final TrustAnchor trustAnchor = trustAnchors.iterator().next();
                                    try {
                                        PublicKey publicKey;
                                        if (trustAnchor.getTrustedCert() != null) {
                                            publicKey = trustAnchor.getTrustedCert().getPublicKey();
                                        }
                                        else {
                                            publicKey = trustAnchor.getCAPublicKey();
                                        }
                                        try {
                                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate, publicKey, this.pkixParams.getSigProvider());
                                            break Label_0375;
                                        }
                                        catch (Exception ex3) {
                                            break Label_0375;
                                        }
                                        catch (SignatureException ex4) {
                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustButInvalidCert"));
                                            break Label_0375;
                                        }
                                    }
                                    catch (CertPathReviewerException ex5) {}
                                }
                                catch (CertPathReviewerException algorithmIdentifier) {
                                    x500Principal = null;
                                }
                                finally {
                                    x500Principal = null;
                                }
                                break Label_1674;
                            Label_0420_Outer:
                                while (true) {
                                    while (true) {
                                        try {
                                            Object o = null;
                                            X500Principal subjectPrincipal = CertPathValidatorUtilities.getSubjectPrincipal((X509Certificate)o);
                                        Label_0515_Outer:
                                            while (true) {
                                                X500Principal x500Principal2 = subjectPrincipal;
                                                if (o != null) {
                                                    o = ((X509Certificate)o).getKeyUsage();
                                                    x500Principal2 = subjectPrincipal;
                                                    if (o != null) {
                                                        x500Principal2 = subjectPrincipal;
                                                        if (!o[5]) {
                                                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustKeyUsage"));
                                                            x500Principal2 = subjectPrincipal;
                                                        }
                                                    }
                                                }
                                                while (true) {
                                                    PublicKey publicKey2;
                                                    if (algorithmIdentifier != null) {
                                                        o = ((TrustAnchor)algorithmIdentifier).getTrustedCert();
                                                        if (o != null) {
                                                            publicKey2 = ((Certificate)o).getPublicKey();
                                                        }
                                                        else {
                                                            publicKey2 = ((TrustAnchor)algorithmIdentifier).getCAPublicKey();
                                                        }
                                                        try {
                                                            final AlgorithmIdentifier algorithmIdentifier2 = CertPathValidatorUtilities.getAlgorithmIdentifier(publicKey2);
                                                            algorithmIdentifier2.getAlgorithm();
                                                            algorithmIdentifier2.getParameters();
                                                        }
                                                        catch (CertPathValidatorException ex6) {
                                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustPubKeyError"));
                                                        }
                                                    }
                                                    else {
                                                        o = null;
                                                        publicKey2 = null;
                                                    }
                                                    final int size = this.certs.size();
                                                    final X500Principal x500Principal3 = x500Principal2;
                                                    int i = size - 1;
                                                    final TrustAnchor trustAnchor2 = (TrustAnchor)algorithmIdentifier;
                                                    algorithmIdentifier = publicKey2;
                                                    Serializable s = x500Principal3;
                                                    while (i >= 0) {
                                                        final int n = this.n - i;
                                                        final X509Certificate x509Certificate2 = this.certs.get(i);
                                                        Label_0721: {
                                                            ErrorBundle errorBundle = null;
                                                            Label_0714: {
                                                                if (algorithmIdentifier == null) {
                                                                    if (CertPathValidatorUtilities.isSelfIssued(x509Certificate2)) {
                                                                        try {
                                                                            CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, x509Certificate2.getPublicKey(), this.pkixParams.getSigProvider());
                                                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.rootKeyIsValidButNotATrustAnchor"), i);
                                                                            break Label_0721;
                                                                        }
                                                                        catch (GeneralSecurityException ex) {
                                                                            errorBundle = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.signatureNotVerified", new Object[] { ex.getMessage(), ex, ex.getClass().getName() });
                                                                            break Label_0714;
                                                                        }
                                                                    }
                                                                    final ErrorBundle errorBundle2 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.NoIssuerPublicKey");
                                                                    final byte[] extensionValue = x509Certificate2.getExtensionValue(Extension.authorityKeyIdentifier.getId());
                                                                    if (extensionValue != null) {
                                                                        final AuthorityKeyIdentifier instance = AuthorityKeyIdentifier.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets());
                                                                        final GeneralNames authorityCertIssuer = instance.getAuthorityCertIssuer();
                                                                        if (authorityCertIssuer != null) {
                                                                            final GeneralName generalName = authorityCertIssuer.getNames()[0];
                                                                            final BigInteger authorityCertSerialNumber = instance.getAuthorityCertSerialNumber();
                                                                            if (authorityCertSerialNumber != null) {
                                                                                errorBundle2.setExtraArguments(new Object[] { new LocaleString("org.spongycastle.x509.CertPathReviewerMessages", "missingIssuer"), " \"", generalName, "\" ", new LocaleString("org.spongycastle.x509.CertPathReviewerMessages", "missingSerial"), " ", authorityCertSerialNumber });
                                                                            }
                                                                        }
                                                                    }
                                                                    this.addError(errorBundle2, i);
                                                                    break Label_0721;
                                                                }
                                                                try {
                                                                    CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, (PublicKey)algorithmIdentifier, this.pkixParams.getSigProvider());
                                                                    break Label_0721;
                                                                }
                                                                catch (GeneralSecurityException ex2) {
                                                                    errorBundle = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.signatureNotVerified", new Object[] { ex2.getMessage(), ex2, ex2.getClass().getName() });
                                                                }
                                                            }
                                                            this.addError(errorBundle, i);
                                                        }
                                                        Label_1065: {
                                                            ErrorBundle errorBundle3;
                                                            try {
                                                                x509Certificate2.checkValidity(this.validDate);
                                                                break Label_1065;
                                                            }
                                                            catch (CertificateExpiredException ex7) {
                                                                errorBundle3 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certificateExpired", new Object[] { new TrustedInput(x509Certificate2.getNotAfter()) });
                                                            }
                                                            catch (CertificateNotYetValidException ex8) {
                                                                errorBundle3 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certificateNotYetValid", new Object[] { new TrustedInput(x509Certificate2.getNotBefore()) });
                                                            }
                                                            this.addError(errorBundle3, i);
                                                        }
                                                        Label_1364: {
                                                            if (this.pkixParams.isRevocationEnabled()) {
                                                                CRLDistPoint instance2 = null;
                                                                AuthorityInformationAccess instance3 = null;
                                                                Label_1169: {
                                                                    Label_1122: {
                                                                        try {
                                                                            final ASN1Primitive extensionValue2 = CertPathValidatorUtilities.getExtensionValue(x509Certificate2, PKIXCertPathReviewer.CRL_DIST_POINTS);
                                                                            if (extensionValue2 != null) {
                                                                                instance2 = CRLDistPoint.getInstance(extensionValue2);
                                                                                break Label_1122;
                                                                            }
                                                                        }
                                                                        catch (AnnotatedException ex9) {
                                                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlDistPtExtError"), i);
                                                                        }
                                                                        instance2 = null;
                                                                        try {
                                                                            final ASN1Primitive extensionValue3 = CertPathValidatorUtilities.getExtensionValue(x509Certificate2, PKIXCertPathReviewer.AUTH_INFO_ACCESS);
                                                                            if (extensionValue3 != null) {
                                                                                instance3 = AuthorityInformationAccess.getInstance(extensionValue3);
                                                                                break Label_1169;
                                                                            }
                                                                        }
                                                                        catch (AnnotatedException ex10) {
                                                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlAuthInfoAccError"), i);
                                                                        }
                                                                    }
                                                                    instance3 = null;
                                                                }
                                                                final Vector crlDistUrls = this.getCRLDistUrls(instance2);
                                                                final Vector ocspUrls = this.getOCSPUrls(instance3);
                                                                final Iterator<Object> iterator = crlDistUrls.iterator();
                                                                while (iterator.hasNext()) {
                                                                    this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlDistPoint", new Object[] { new UntrustedUrlInput(iterator.next()) }), i);
                                                                }
                                                                final Iterator<Object> iterator2 = ocspUrls.iterator();
                                                                while (iterator2.hasNext()) {
                                                                    this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.ocspLocation", new Object[] { new UntrustedUrlInput(iterator2.next()) }), i);
                                                                }
                                                                try {
                                                                    final PKIXParameters pkixParams = this.pkixParams;
                                                                    final Date validDate = this.validDate;
                                                                    final Serializable s2 = s;
                                                                    try {
                                                                        this.checkRevocation(pkixParams, x509Certificate2, validDate, (X509Certificate)o, (PublicKey)algorithmIdentifier, crlDistUrls, ocspUrls, i);
                                                                        s = s2;
                                                                        break Label_1364;
                                                                    }
                                                                    catch (CertPathReviewerException ex11) {}
                                                                }
                                                                catch (CertPathReviewerException o) {}
                                                                this.addError(((LocalizedException)o).getErrorMessage(), i);
                                                            }
                                                        }
                                                        o = x509Certificate2;
                                                        if (s != null && !((X509Certificate)o).getIssuerX500Principal().equals(s)) {
                                                            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certWrongIssuer", new Object[] { ((X500Principal)s).getName(), ((X509Certificate)o).getIssuerX500Principal().getName() }), i);
                                                        }
                                                        if (n != this.n) {
                                                            if (o != null && ((X509Certificate)o).getVersion() == 1) {
                                                                this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCACert"), i);
                                                            }
                                                            Label_1544: {
                                                                try {
                                                                    final BasicConstraints instance4 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)o, PKIXCertPathReviewer.BASIC_CONSTRAINTS));
                                                                    ErrorBundle errorBundle4;
                                                                    if (instance4 != null) {
                                                                        if (instance4.isCA()) {
                                                                            break Label_1544;
                                                                        }
                                                                        errorBundle4 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCACert");
                                                                    }
                                                                    else {
                                                                        errorBundle4 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noBasicConstraints");
                                                                    }
                                                                    this.addError(errorBundle4, i);
                                                                }
                                                                catch (AnnotatedException ex12) {
                                                                    this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.errorProcesingBC"), i);
                                                                }
                                                            }
                                                            final boolean[] keyUsage = ((X509Certificate)o).getKeyUsage();
                                                            if (keyUsage != null && !keyUsage[5]) {
                                                                this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCertSign"), i);
                                                            }
                                                        }
                                                        final X500Principal subjectX500Principal = ((X509Certificate)o).getSubjectX500Principal();
                                                        PublicKey nextWorkingKey;
                                                        try {
                                                            nextWorkingKey = CertPathValidatorUtilities.getNextWorkingKey(this.certs, i);
                                                            try {
                                                                algorithmIdentifier = CertPathValidatorUtilities.getAlgorithmIdentifier(nextWorkingKey);
                                                                ((AlgorithmIdentifier)algorithmIdentifier).getAlgorithm();
                                                                ((AlgorithmIdentifier)algorithmIdentifier).getParameters();
                                                            }
                                                            catch (CertPathValidatorException algorithmIdentifier) {}
                                                        }
                                                        catch (CertPathValidatorException ex13) {
                                                            nextWorkingKey = (PublicKey)algorithmIdentifier;
                                                        }
                                                        this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.pubKeyError"), i);
                                                        --i;
                                                        algorithmIdentifier = subjectX500Principal;
                                                        final PublicKey publicKey3 = nextWorkingKey;
                                                        s = (Serializable)algorithmIdentifier;
                                                        algorithmIdentifier = publicKey3;
                                                    }
                                                    this.trustAnchor = trustAnchor2;
                                                    this.subjectPublicKey = (PublicKey)algorithmIdentifier;
                                                    return;
                                                    this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustDNInvalid", new Object[] { new UntrustedInput(((TrustAnchor)algorithmIdentifier).getCAName()) }));
                                                    subjectPrincipal = null;
                                                    continue Label_0515_Outer;
                                                    algorithmIdentifier = null;
                                                    break Label_0378;
                                                    x500Principal2 = null;
                                                    continue Label_0420_Outer;
                                                }
                                                subjectPrincipal = new X500Principal(((TrustAnchor)algorithmIdentifier).getCAName());
                                                continue Label_0515_Outer;
                                            }
                                        }
                                        catch (IllegalArgumentException ex14) {}
                                        continue Label_0512_Outer;
                                    }
                                }
                            }
                            this.addError(((LocalizedException)algorithmIdentifier).getErrorMessage());
                        }
                        algorithmIdentifier = x500Principal;
                    }
                    if (algorithmIdentifier == null) {
                        continue;
                    }
                    break;
                }
                Object o = ((TrustAnchor)algorithmIdentifier).getTrustedCert();
                if (o != null) {
                    continue Label_0512_Outer;
                }
                break;
            }
            continue;
        }
    }
    
    private X509CRL getCRL(final String s) throws CertPathReviewerException {
        try {
            final URL url = new URL(s);
            if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
                final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {
                    return (X509CRL)CertificateFactory.getInstance("X.509", "SC").generateCRL(httpURLConnection.getInputStream());
                }
                throw new Exception(httpURLConnection.getResponseMessage());
            }
        }
        catch (Exception ex) {
            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.loadCrlDistPointError", new Object[] { new UntrustedInput(s), ex.getMessage(), ex, ex.getClass().getName() }));
        }
        return null;
    }
    
    private boolean processQcStatements(final X509Certificate x509Certificate, final int n) {
        try {
            final ASN1Sequence asn1Sequence = (ASN1Sequence)CertPathValidatorUtilities.getExtensionValue(x509Certificate, PKIXCertPathReviewer.QC_STATEMENT);
            int i = 0;
            boolean b = false;
            while (i < asn1Sequence.size()) {
                final QCStatement instance = QCStatement.getInstance(asn1Sequence.getObjectAt(i));
                Label_0332: {
                    ErrorBundle errorBundle;
                    if (QCStatement.id_etsi_qcs_QcCompliance.equals(instance.getStatementId())) {
                        errorBundle = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcEuCompliance");
                    }
                    else {
                        if (QCStatement.id_qcs_pkixQCSyntax_v1.equals(instance.getStatementId())) {
                            break Label_0332;
                        }
                        if (QCStatement.id_etsi_qcs_QcSSCD.equals(instance.getStatementId())) {
                            errorBundle = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcSSCD");
                        }
                        else {
                            if (QCStatement.id_etsi_qcs_LimiteValue.equals(instance.getStatementId())) {
                                final MonetaryValue instance2 = MonetaryValue.getInstance(instance.getStatementInfo());
                                instance2.getCurrency();
                                final double n2 = instance2.getAmount().doubleValue() * Math.pow(10.0, instance2.getExponent().doubleValue());
                                ErrorBundle errorBundle2;
                                if (instance2.getCurrency().isAlphabetic()) {
                                    errorBundle2 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueAlpha", new Object[] { instance2.getCurrency().getAlphabetic(), new TrustedInput(new Double(n2)), instance2 });
                                }
                                else {
                                    errorBundle2 = new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueNum", new Object[] { Integers.valueOf(instance2.getCurrency().getNumeric()), new TrustedInput(new Double(n2)), instance2 });
                                }
                                this.addNotification(errorBundle2, n);
                                break Label_0332;
                            }
                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcUnknownStatement", new Object[] { instance.getStatementId(), new UntrustedInput(instance) }), n);
                            b = true;
                            break Label_0332;
                        }
                    }
                    this.addNotification(errorBundle, n);
                }
                ++i;
            }
            return b ^ true;
        }
        catch (AnnotatedException ex) {
            this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.QcStatementExtError"), n);
            return false;
        }
    }
    
    protected void addError(final ErrorBundle errorBundle) {
        this.errors[0].add(errorBundle);
    }
    
    protected void addError(final ErrorBundle errorBundle, final int n) {
        if (n >= -1 && n < this.n) {
            this.errors[n + 1].add(errorBundle);
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    protected void addNotification(final ErrorBundle errorBundle) {
        this.notifications[0].add(errorBundle);
    }
    
    protected void addNotification(final ErrorBundle errorBundle, final int n) {
        if (n >= -1 && n < this.n) {
            this.notifications[n + 1].add(errorBundle);
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    protected void checkCRLs(final PKIXParameters pkixParameters, final X509Certificate certificateChecking, final Date date, final X509Certificate x509Certificate, final PublicKey publicKey, Vector crl, int n) throws CertPathReviewerException {
        final X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
        try {
            x509CRLStoreSelector.addIssuerName(CertPathValidatorUtilities.getEncodedIssuerPrincipal(certificateChecking).getEncoded());
            x509CRLStoreSelector.setCertificateChecking(certificateChecking);
            Iterator<X509CRL> iterator2;
            try {
                final Set crLs = PKIXCertPathReviewer.CRL_UTIL.findCRLs(x509CRLStoreSelector, pkixParameters);
                final Iterator<Object> iterator = (Iterator<Object>)(iterator2 = (Iterator<X509CRL>)crLs.iterator());
                if (crLs.isEmpty()) {
                    final Iterator iterator3 = PKIXCertPathReviewer.CRL_UTIL.findCRLs(new X509CRLStoreSelector(), pkixParameters).iterator();
                    final ArrayList<X500Principal> list = new ArrayList<X500Principal>();
                    while (iterator3.hasNext()) {
                        list.add(iterator3.next().getIssuerX500Principal());
                    }
                    this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCrlInCertstore", new Object[] { new UntrustedInput(x509CRLStoreSelector.getIssuerNames()), new UntrustedInput(list), Integers.valueOf(list.size()) }), n);
                    iterator2 = (Iterator<X509CRL>)iterator;
                }
            }
            catch (AnnotatedException ex) {
                this.addError(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlExtractionError", new Object[] { ex.getCause().getMessage(), ex.getCause(), ex.getCause().getClass().getName() }), n);
                iterator2 = new ArrayList<X509CRL>().iterator();
            }
            Object o = null;
            while (true) {
                while (iterator2.hasNext()) {
                    o = iterator2.next();
                    if (((X509CRL)o).getNextUpdate() != null && !pkixParameters.getDate().before(((X509CRL)o).getNextUpdate())) {
                        this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.localInvalidCRL", new Object[] { new TrustedInput(((X509CRL)o).getThisUpdate()), new TrustedInput(((X509CRL)o).getNextUpdate()) }), n);
                    }
                    else {
                        this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.localValidCRL", new Object[] { new TrustedInput(((X509CRL)o).getThisUpdate()), new TrustedInput(((X509CRL)o).getNextUpdate()) }), n);
                        int n2 = 1;
                        X509CRL x509CRL = (X509CRL)o;
                        int n3 = n2;
                        Label_0820: {
                            if (n2 == 0) {
                                final Iterator<String> iterator4 = ((Vector<String>)crl).iterator();
                                while (true) {
                                    x509CRL = (X509CRL)o;
                                    n3 = n2;
                                    if (!iterator4.hasNext()) {
                                        break;
                                    }
                                    while (true) {
                                        while (true) {
                                            Label_0799: {
                                                try {
                                                    final String s = iterator4.next();
                                                    crl = this.getCRL(s);
                                                    if (crl == null) {
                                                        break Label_0799;
                                                    }
                                                    if (!certificateChecking.getIssuerX500Principal().equals(crl.getIssuerX500Principal())) {
                                                        try {
                                                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.onlineCRLWrongCA", new Object[] { new UntrustedInput(crl.getIssuerX500Principal().getName()), new UntrustedInput(certificateChecking.getIssuerX500Principal().getName()), new UntrustedUrlInput(s) }), n);
                                                            break Label_0799;
                                                        }
                                                        catch (CertPathReviewerException ex10) {}
                                                    }
                                                    else {
                                                        Label_0711: {
                                                            if (crl.getNextUpdate() != null) {
                                                                try {
                                                                    if (this.pkixParams.getDate().before(crl.getNextUpdate())) {
                                                                        break Label_0711;
                                                                    }
                                                                    try {
                                                                        this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.onlineInvalidCRL", new Object[] { new TrustedInput(crl.getThisUpdate()), new TrustedInput(crl.getNextUpdate()), new UntrustedUrlInput(s) }), n);
                                                                    }
                                                                    catch (CertPathReviewerException crl) {}
                                                                }
                                                                catch (CertPathReviewerException ex11) {}
                                                                break;
                                                            }
                                                            try {
                                                                this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.onlineValidCRL", new Object[] { new TrustedInput(crl.getThisUpdate()), new TrustedInput(crl.getNextUpdate()), new UntrustedUrlInput(s) }), n);
                                                                n3 = 1;
                                                                break Label_0820;
                                                            }
                                                            catch (CertPathReviewerException crl) {}
                                                        }
                                                        n2 = 1;
                                                    }
                                                }
                                                catch (CertPathReviewerException crl) {}
                                                break;
                                            }
                                            continue;
                                        }
                                    }
                                    this.addNotification(((LocalizedException)crl).getErrorMessage(), n);
                                }
                            }
                            crl = x509CRL;
                        }
                        Label_1712: {
                            if (crl != null) {
                                if (x509Certificate != null) {
                                    final boolean[] keyUsage = x509Certificate.getKeyUsage();
                                    if (keyUsage != null) {
                                        if (keyUsage.length < 7 || !keyUsage[6]) {
                                            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noCrlSigningPermited"));
                                        }
                                    }
                                }
                                if (publicKey != null) {
                                    try {
                                        crl.verify(publicKey, "SC");
                                        final X509CRLEntry revokedCertificate = crl.getRevokedCertificate(certificateChecking.getSerialNumber());
                                        if (revokedCertificate != null) {
                                            String s2 = null;
                                            Label_0983: {
                                                if (revokedCertificate.hasExtensions()) {
                                                    try {
                                                        final ASN1Enumerated instance = ASN1Enumerated.getInstance(CertPathValidatorUtilities.getExtensionValue(revokedCertificate, Extension.reasonCode.getId()));
                                                        if (instance != null) {
                                                            s2 = PKIXCertPathReviewer.crlReasons[instance.getValue().intValue()];
                                                            break Label_0983;
                                                        }
                                                    }
                                                    catch (AnnotatedException ex2) {
                                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlReasonExtError"), ex2);
                                                    }
                                                }
                                                s2 = null;
                                            }
                                            String s3 = s2;
                                            if (s2 == null) {
                                                s3 = PKIXCertPathReviewer.crlReasons[7];
                                            }
                                            final LocaleString localeString = new LocaleString("org.spongycastle.x509.CertPathReviewerMessages", s3);
                                            if (!date.before(revokedCertificate.getRevocationDate())) {
                                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.certRevoked", new Object[] { new TrustedInput(revokedCertificate.getRevocationDate()), localeString }));
                                            }
                                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.revokedAfterValidation", new Object[] { new TrustedInput(revokedCertificate.getRevocationDate()), localeString }), n);
                                        }
                                        else {
                                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.notRevoked"), n);
                                        }
                                        if (crl.getNextUpdate() != null && crl.getNextUpdate().before(this.pkixParams.getDate())) {
                                            this.addNotification(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlUpdateAvailable", new Object[] { new TrustedInput(crl.getNextUpdate()) }), n);
                                        }
                                        n = 1;
                                        try {
                                            final ASN1Primitive extensionValue = CertPathValidatorUtilities.getExtensionValue(crl, PKIXCertPathReviewer.ISSUING_DISTRIBUTION_POINT);
                                            try {
                                                final ASN1Primitive extensionValue2 = CertPathValidatorUtilities.getExtensionValue(crl, PKIXCertPathReviewer.DELTA_CRL_INDICATOR);
                                                Label_1474: {
                                                    if (extensionValue2 != null) {
                                                        final X509CRLStoreSelector x509CRLStoreSelector2 = new X509CRLStoreSelector();
                                                        try {
                                                            x509CRLStoreSelector2.addIssuerName(CertPathValidatorUtilities.getIssuerPrincipal(crl).getEncoded());
                                                            x509CRLStoreSelector2.setMinCRLNumber(((ASN1Integer)extensionValue2).getPositiveValue());
                                                            try {
                                                                x509CRLStoreSelector2.setMaxCRLNumber(((ASN1Integer)CertPathValidatorUtilities.getExtensionValue(crl, PKIXCertPathReviewer.CRL_NUMBER)).getPositiveValue().subtract(BigInteger.valueOf(1L)));
                                                                try {
                                                                    for (final X509CRL x509CRL2 : PKIXCertPathReviewer.CRL_UTIL.findCRLs(x509CRLStoreSelector2, pkixParameters)) {
                                                                        Label_1380: {
                                                                            try {
                                                                                final ASN1Primitive extensionValue3 = CertPathValidatorUtilities.getExtensionValue(x509CRL2, PKIXCertPathReviewer.ISSUING_DISTRIBUTION_POINT);
                                                                                if (extensionValue == null) {
                                                                                    if (extensionValue3 == null) {
                                                                                        break Label_1380;
                                                                                    }
                                                                                    continue;
                                                                                }
                                                                                else {
                                                                                    if (extensionValue.equals(extensionValue3)) {
                                                                                        break Label_1380;
                                                                                    }
                                                                                    continue;
                                                                                }
                                                                            }
                                                                            catch (AnnotatedException ex3) {
                                                                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.distrPtExtError"), ex3);
                                                                            }
                                                                            break;
                                                                        }
                                                                        if (n != 0) {
                                                                            break Label_1474;
                                                                        }
                                                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noBaseCRL"));
                                                                    }
                                                                    n = 0;
                                                                }
                                                                catch (AnnotatedException ex4) {
                                                                    throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlExtractionError"), ex4);
                                                                }
                                                            }
                                                            catch (AnnotatedException ex5) {
                                                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlNbrExtError"), ex5);
                                                            }
                                                        }
                                                        catch (IOException ex6) {
                                                            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlIssuerException"), ex6);
                                                        }
                                                    }
                                                }
                                                if (extensionValue == null) {
                                                    break Label_1712;
                                                }
                                                final IssuingDistributionPoint instance2 = IssuingDistributionPoint.getInstance(extensionValue);
                                                try {
                                                    final BasicConstraints instance3 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(certificateChecking, PKIXCertPathReviewer.BASIC_CONSTRAINTS));
                                                    if (instance2.onlyContainsUserCerts() && instance3 != null && instance3.isCA()) {
                                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlOnlyUserCert"));
                                                    }
                                                    if (instance2.onlyContainsCACerts() && (instance3 == null || !instance3.isCA())) {
                                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlOnlyCaCert"));
                                                    }
                                                    if (!instance2.onlyContainsAttributeCerts()) {
                                                        break Label_1712;
                                                    }
                                                    throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlOnlyAttrCert"));
                                                }
                                                catch (AnnotatedException ex7) {
                                                    throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlBCExtError"), ex7);
                                                }
                                            }
                                            catch (AnnotatedException ex12) {
                                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.deltaCrlExtError"));
                                            }
                                        }
                                        catch (AnnotatedException ex13) {
                                            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.distrPtExtError"));
                                        }
                                    }
                                    catch (Exception ex8) {
                                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlVerifyFailed"), ex8);
                                    }
                                }
                                throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlNoIssuerPublicKey"));
                            }
                        }
                        if (n3 != 0) {
                            return;
                        }
                        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.noValidCrlFound"));
                    }
                }
                int n2 = 0;
                continue;
            }
        }
        catch (IOException ex9) {
            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.crlIssuerException"), ex9);
        }
    }
    
    protected void checkRevocation(final PKIXParameters pkixParameters, final X509Certificate x509Certificate, final Date date, final X509Certificate x509Certificate2, final PublicKey publicKey, final Vector vector, final Vector vector2, final int n) throws CertPathReviewerException {
        this.checkCRLs(pkixParameters, x509Certificate, date, x509Certificate2, publicKey, vector, n);
    }
    
    protected void doChecks() {
        if (this.initialized) {
            if (this.notifications == null) {
                final int n = this.n + 1;
                this.notifications = new List[n];
                this.errors = new List[n];
                int n2 = 0;
                while (true) {
                    final List[] notifications = this.notifications;
                    if (n2 >= notifications.length) {
                        break;
                    }
                    notifications[n2] = new ArrayList();
                    this.errors[n2] = new ArrayList();
                    ++n2;
                }
                this.checkSignatures();
                this.checkNameConstraints();
                this.checkPathLength();
                this.checkPolicy();
                this.checkCriticalExtensions();
            }
            return;
        }
        throw new IllegalStateException("Object not initialized. Call init() first.");
    }
    
    protected Vector getCRLDistUrls(final CRLDistPoint crlDistPoint) {
        final Vector<String> vector = new Vector<String>();
        if (crlDistPoint != null) {
            final DistributionPoint[] distributionPoints = crlDistPoint.getDistributionPoints();
            for (int i = 0; i < distributionPoints.length; ++i) {
                final DistributionPointName distributionPoint = distributionPoints[i].getDistributionPoint();
                if (distributionPoint.getType() == 0) {
                    final GeneralName[] names = GeneralNames.getInstance(distributionPoint.getName()).getNames();
                    for (int j = 0; j < names.length; ++j) {
                        if (names[j].getTagNo() == 6) {
                            vector.add(((DERIA5String)names[j].getName()).getString());
                        }
                    }
                }
            }
        }
        return vector;
    }
    
    public CertPath getCertPath() {
        return this.certPath;
    }
    
    public int getCertPathSize() {
        return this.n;
    }
    
    public List getErrors(final int n) {
        this.doChecks();
        return this.errors[n + 1];
    }
    
    public List[] getErrors() {
        this.doChecks();
        return this.errors;
    }
    
    public List getNotifications(final int n) {
        this.doChecks();
        return this.notifications[n + 1];
    }
    
    public List[] getNotifications() {
        this.doChecks();
        return this.notifications;
    }
    
    protected Vector getOCSPUrls(final AuthorityInformationAccess authorityInformationAccess) {
        final Vector<String> vector = new Vector<String>();
        if (authorityInformationAccess != null) {
            final AccessDescription[] accessDescriptions = authorityInformationAccess.getAccessDescriptions();
            for (int i = 0; i < accessDescriptions.length; ++i) {
                if (accessDescriptions[i].getAccessMethod().equals(AccessDescription.id_ad_ocsp)) {
                    final GeneralName accessLocation = accessDescriptions[i].getAccessLocation();
                    if (accessLocation.getTagNo() == 6) {
                        vector.add(((DERIA5String)accessLocation.getName()).getString());
                    }
                }
            }
        }
        return vector;
    }
    
    public PolicyNode getPolicyTree() {
        this.doChecks();
        return this.policyTree;
    }
    
    public PublicKey getSubjectPublicKey() {
        this.doChecks();
        return this.subjectPublicKey;
    }
    
    public TrustAnchor getTrustAnchor() {
        this.doChecks();
        return this.trustAnchor;
    }
    
    protected Collection getTrustAnchors(final X509Certificate x509Certificate, final Set set) throws CertPathReviewerException {
        final ArrayList<TrustAnchor> list = new ArrayList<TrustAnchor>();
        final Iterator<TrustAnchor> iterator = set.iterator();
        final X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(CertPathValidatorUtilities.getEncodedIssuerPrincipal(x509Certificate).getEncoded());
            final byte[] extensionValue = x509Certificate.getExtensionValue(Extension.authorityKeyIdentifier.getId());
            if (extensionValue != null) {
                final AuthorityKeyIdentifier instance = AuthorityKeyIdentifier.getInstance(ASN1Primitive.fromByteArray(((ASN1OctetString)ASN1Primitive.fromByteArray(extensionValue)).getOctets()));
                x509CertSelector.setSerialNumber(instance.getAuthorityCertSerialNumber());
                final byte[] keyIdentifier = instance.getKeyIdentifier();
                if (keyIdentifier != null) {
                    x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                }
            }
            while (iterator.hasNext()) {
                final TrustAnchor trustAnchor = iterator.next();
                if (trustAnchor.getTrustedCert() != null) {
                    if (!x509CertSelector.match(trustAnchor.getTrustedCert())) {
                        continue;
                    }
                }
                else if (trustAnchor.getCAName() == null || trustAnchor.getCAPublicKey() == null || !CertPathValidatorUtilities.getEncodedIssuerPrincipal(x509Certificate).equals(new X500Principal(trustAnchor.getCAName()))) {
                    continue;
                }
                list.add(trustAnchor);
            }
            return list;
        }
        catch (IOException ex) {
            throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.trustAnchorIssuerError"));
        }
    }
    
    public void init(final CertPath certPath, final PKIXParameters pkixParameters) throws CertPathReviewerException {
        if (this.initialized) {
            throw new IllegalStateException("object is already initialized!");
        }
        this.initialized = true;
        if (certPath == null) {
            throw new NullPointerException("certPath was null");
        }
        this.certPath = certPath;
        final List<? extends Certificate> certificates = certPath.getCertificates();
        this.certs = certificates;
        this.n = certificates.size();
        if (!this.certs.isEmpty()) {
            final PKIXParameters pkixParams = (PKIXParameters)pkixParameters.clone();
            this.pkixParams = pkixParams;
            this.validDate = CertPathValidatorUtilities.getValidDate(pkixParams);
            this.notifications = null;
            this.errors = null;
            this.trustAnchor = null;
            this.subjectPublicKey = null;
            this.policyTree = null;
            return;
        }
        throw new CertPathReviewerException(new ErrorBundle("org.spongycastle.x509.CertPathReviewerMessages", "CertPathReviewer.emptyCertPath"));
    }
    
    public boolean isValidCertPath() {
        this.doChecks();
        int n = 0;
        while (true) {
            final List[] errors = this.errors;
            if (n >= errors.length) {
                return true;
            }
            if (!errors[n].isEmpty()) {
                return false;
            }
            ++n;
        }
    }
}
