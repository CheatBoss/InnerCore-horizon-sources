package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.util.*;
import javax.security.auth.x500.*;
import java.util.*;
import org.spongycastle.jce.exception.*;
import java.io.*;
import org.spongycastle.jcajce.*;
import java.security.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.x509.*;
import java.security.cert.*;

class RFC3281CertPathUtilities
{
    private static final String AUTHORITY_INFO_ACCESS;
    private static final String CRL_DISTRIBUTION_POINTS;
    private static final String NO_REV_AVAIL;
    private static final String TARGET_INFORMATION;
    
    static {
        TARGET_INFORMATION = Extension.targetInformation.getId();
        NO_REV_AVAIL = Extension.noRevAvail.getId();
        CRL_DISTRIBUTION_POINTS = Extension.cRLDistributionPoints.getId();
        AUTHORITY_INFO_ACCESS = Extension.authorityInfoAccess.getId();
    }
    
    protected static void additionalChecks(final X509AttributeCertificate x509AttributeCertificate, final Set set, final Set set2) throws CertPathValidatorException {
        for (final String s : set) {
            if (x509AttributeCertificate.getAttributes(s) == null) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Attribute certificate contains prohibited attribute: ");
            sb.append(s);
            sb.append(".");
            throw new CertPathValidatorException(sb.toString());
        }
        for (final String s2 : set2) {
            if (x509AttributeCertificate.getAttributes(s2) != null) {
                continue;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Attribute certificate does not contain necessary attribute: ");
            sb2.append(s2);
            sb2.append(".");
            throw new CertPathValidatorException(sb2.toString());
        }
    }
    
    private static void checkCRL(final DistributionPoint distributionPoint, final X509AttributeCertificate x509AttributeCertificate, final PKIXExtendedParameters pkixExtendedParameters, final Date date, X509Certificate iterator, final CertStatus certStatus, final ReasonsMask reasonsMask, final List list, final JcaJceHelper jcaJceHelper) throws AnnotatedException {
        if (x509AttributeCertificate.getExtensionValue(X509Extensions.NoRevAvail.getId()) != null) {
            return;
        }
        final Date date2 = new Date(System.currentTimeMillis());
        if (date.getTime() > date2.getTime()) {
            throw new AnnotatedException("Validation time is in future.");
        }
        iterator = (X509Certificate)CertPathValidatorUtilities.getCompleteCRLs(distributionPoint, x509AttributeCertificate, date2, pkixExtendedParameters).iterator();
        final AnnotatedException ex = null;
        boolean b = false;
        while (((Iterator)iterator).hasNext() && certStatus.getCertStatus() == 11 && !reasonsMask.isAllReasons()) {
            while (true) {
                while (true) {
                    Label_0321: {
                        try {
                            final X509CRL x509CRL = ((Iterator<X509CRL>)iterator).next();
                            final ReasonsMask processCRLD = RFC3280CertPathUtilities.processCRLD(x509CRL, distributionPoint);
                            if (processCRLD.hasNewReasons(reasonsMask)) {
                                try {
                                    final PublicKey processCRLG = RFC3280CertPathUtilities.processCRLG(x509CRL, RFC3280CertPathUtilities.processCRLF(x509CRL, x509AttributeCertificate, null, null, pkixExtendedParameters, list, jcaJceHelper));
                                    if (!pkixExtendedParameters.isUseDeltasEnabled()) {
                                        break Label_0321;
                                    }
                                    final X509CRL processCRLH = RFC3280CertPathUtilities.processCRLH(CertPathValidatorUtilities.getDeltaCRLs(date2, x509CRL, pkixExtendedParameters.getCertStores(), pkixExtendedParameters.getCRLStores()), processCRLG);
                                    if (pkixExtendedParameters.getValidityModel() != 1 && x509AttributeCertificate.getNotAfter().getTime() < x509CRL.getThisUpdate().getTime()) {
                                        throw new AnnotatedException("No valid CRL for current time found.");
                                    }
                                    RFC3280CertPathUtilities.processCRLB1(distributionPoint, x509AttributeCertificate, x509CRL);
                                    RFC3280CertPathUtilities.processCRLB2(distributionPoint, x509AttributeCertificate, x509CRL);
                                    RFC3280CertPathUtilities.processCRLC(processCRLH, x509CRL, pkixExtendedParameters);
                                    RFC3280CertPathUtilities.processCRLI(date, processCRLH, x509AttributeCertificate, certStatus, pkixExtendedParameters);
                                    RFC3280CertPathUtilities.processCRLJ(date, x509CRL, x509AttributeCertificate, certStatus);
                                    if (certStatus.getCertStatus() == 8) {
                                        certStatus.setCertStatus(11);
                                    }
                                    reasonsMask.addReasons(processCRLD);
                                    b = true;
                                    break;
                                }
                                catch (AnnotatedException ex) {}
                            }
                        }
                        catch (AnnotatedException ex2) {}
                        break;
                    }
                    final X509CRL processCRLH = null;
                    continue;
                }
            }
        }
        if (b) {
            return;
        }
        throw ex;
    }
    
    protected static void checkCRLs(final X509AttributeCertificate p0, final PKIXExtendedParameters p1, final X509Certificate p2, final Date p3, final List p4, final JcaJceHelper p5) throws CertPathValidatorException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.isRevocationEnabled:()Z
        //     4: ifeq            612
        //     7: aload_0        
        //     8: getstatic       org/spongycastle/jce/provider/RFC3281CertPathUtilities.NO_REV_AVAIL:Ljava/lang/String;
        //    11: invokeinterface org/spongycastle/x509/X509AttributeCertificate.getExtensionValue:(Ljava/lang/String;)[B
        //    16: ifnonnull       576
        //    19: aload_0        
        //    20: getstatic       org/spongycastle/jce/provider/RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS:Ljava/lang/String;
        //    23: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //    26: invokestatic    org/spongycastle/asn1/x509/CRLDistPoint.getInstance:(Ljava/lang/Object;)Lorg/spongycastle/asn1/x509/CRLDistPoint;
        //    29: astore          11
        //    31: new             Ljava/util/ArrayList;
        //    34: dup            
        //    35: invokespecial   java/util/ArrayList.<init>:()V
        //    38: astore          8
        //    40: aload           8
        //    42: aload           11
        //    44: aload_1        
        //    45: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getNamedCRLStoreMap:()Ljava/util/Map;
        //    48: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint:(Lorg/spongycastle/asn1/x509/CRLDistPoint;Ljava/util/Map;)Ljava/util/List;
        //    51: invokeinterface java/util/List.addAll:(Ljava/util/Collection;)Z
        //    56: pop            
        //    57: new             Lorg/spongycastle/jcajce/PKIXExtendedParameters$Builder;
        //    60: dup            
        //    61: aload_1        
        //    62: invokespecial   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.<init>:(Lorg/spongycastle/jcajce/PKIXExtendedParameters;)V
        //    65: astore_1       
        //    66: aload           8
        //    68: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    73: astore          9
        //    75: aload           9
        //    77: invokeinterface java/util/Iterator.hasNext:()Z
        //    82: ifeq            98
        //    85: aload_1        
        //    86: aload           8
        //    88: checkcast       Lorg/spongycastle/jcajce/PKIXCRLStore;
        //    91: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.addCRLStore:(Lorg/spongycastle/jcajce/PKIXCRLStore;)Lorg/spongycastle/jcajce/PKIXExtendedParameters$Builder;
        //    94: pop            
        //    95: goto            75
        //    98: aload_1        
        //    99: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.build:()Lorg/spongycastle/jcajce/PKIXExtendedParameters;
        //   102: astore          10
        //   104: new             Lorg/spongycastle/jce/provider/CertStatus;
        //   107: dup            
        //   108: invokespecial   org/spongycastle/jce/provider/CertStatus.<init>:()V
        //   111: astore          8
        //   113: new             Lorg/spongycastle/jce/provider/ReasonsMask;
        //   116: dup            
        //   117: invokespecial   org/spongycastle/jce/provider/ReasonsMask.<init>:()V
        //   120: astore          9
        //   122: aload           11
        //   124: ifnull          251
        //   127: aload           11
        //   129: invokevirtual   org/spongycastle/asn1/x509/CRLDistPoint.getDistributionPoints:()[Lorg/spongycastle/asn1/x509/DistributionPoint;
        //   132: astore_1       
        //   133: iconst_0       
        //   134: istore          7
        //   136: iconst_0       
        //   137: istore          6
        //   139: iload           7
        //   141: aload_1        
        //   142: arraylength    
        //   143: if_icmpge       214
        //   146: aload           8
        //   148: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   151: bipush          11
        //   153: if_icmpne       214
        //   156: aload           9
        //   158: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   161: ifne            214
        //   164: aload           10
        //   166: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.clone:()Ljava/lang/Object;
        //   169: checkcast       Lorg/spongycastle/jcajce/PKIXExtendedParameters;
        //   172: astore          11
        //   174: aload_1        
        //   175: iload           7
        //   177: aaload         
        //   178: astore          12
        //   180: aload           12
        //   182: aload_0        
        //   183: aload           11
        //   185: aload_3        
        //   186: aload_2        
        //   187: aload           8
        //   189: aload           9
        //   191: aload           4
        //   193: aload           5
        //   195: invokestatic    org/spongycastle/jce/provider/RFC3281CertPathUtilities.checkCRL:(Lorg/spongycastle/asn1/x509/DistributionPoint;Lorg/spongycastle/x509/X509AttributeCertificate;Lorg/spongycastle/jcajce/PKIXExtendedParameters;Ljava/util/Date;Ljava/security/cert/X509Certificate;Lorg/spongycastle/jce/provider/CertStatus;Lorg/spongycastle/jce/provider/ReasonsMask;Ljava/util/List;Lorg/spongycastle/jcajce/util/JcaJceHelper;)V
        //   198: iload           7
        //   200: iconst_1       
        //   201: iadd           
        //   202: istore          7
        //   204: iconst_1       
        //   205: istore          6
        //   207: goto            139
        //   210: astore_1       
        //   211: goto            223
        //   214: aconst_null    
        //   215: astore_1       
        //   216: goto            256
        //   219: astore_1       
        //   220: goto            211
        //   223: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   226: dup            
        //   227: ldc_w           "No valid CRL for distribution point found."
        //   230: aload_1        
        //   231: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   234: astore_1       
        //   235: goto            256
        //   238: astore_0       
        //   239: new             Lorg/spongycastle/jce/exception/ExtCertPathValidatorException;
        //   242: dup            
        //   243: ldc_w           "Distribution points could not be read."
        //   246: aload_0        
        //   247: invokespecial   org/spongycastle/jce/exception/ExtCertPathValidatorException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   250: athrow         
        //   251: aconst_null    
        //   252: astore_1       
        //   253: iconst_0       
        //   254: istore          6
        //   256: aload           8
        //   258: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   261: bipush          11
        //   263: if_icmpne       395
        //   266: aload           9
        //   268: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   271: ifne            395
        //   274: new             Lorg/spongycastle/asn1/ASN1InputStream;
        //   277: dup            
        //   278: aload_0        
        //   279: invokeinterface org/spongycastle/x509/X509AttributeCertificate.getIssuer:()Lorg/spongycastle/x509/AttributeCertificateIssuer;
        //   284: invokevirtual   org/spongycastle/x509/AttributeCertificateIssuer.getPrincipals:()[Ljava/security/Principal;
        //   287: iconst_0       
        //   288: aaload         
        //   289: checkcast       Ljavax/security/auth/x500/X500Principal;
        //   292: invokevirtual   javax/security/auth/x500/X500Principal.getEncoded:()[B
        //   295: invokespecial   org/spongycastle/asn1/ASN1InputStream.<init>:([B)V
        //   298: invokevirtual   org/spongycastle/asn1/ASN1InputStream.readObject:()Lorg/spongycastle/asn1/ASN1Primitive;
        //   301: astore          11
        //   303: new             Lorg/spongycastle/asn1/x509/DistributionPoint;
        //   306: dup            
        //   307: new             Lorg/spongycastle/asn1/x509/DistributionPointName;
        //   310: dup            
        //   311: iconst_0       
        //   312: new             Lorg/spongycastle/asn1/x509/GeneralNames;
        //   315: dup            
        //   316: new             Lorg/spongycastle/asn1/x509/GeneralName;
        //   319: dup            
        //   320: iconst_4       
        //   321: aload           11
        //   323: invokespecial   org/spongycastle/asn1/x509/GeneralName.<init>:(ILorg/spongycastle/asn1/ASN1Encodable;)V
        //   326: invokespecial   org/spongycastle/asn1/x509/GeneralNames.<init>:(Lorg/spongycastle/asn1/x509/GeneralName;)V
        //   329: invokespecial   org/spongycastle/asn1/x509/DistributionPointName.<init>:(ILorg/spongycastle/asn1/ASN1Encodable;)V
        //   332: aconst_null    
        //   333: aconst_null    
        //   334: invokespecial   org/spongycastle/asn1/x509/DistributionPoint.<init>:(Lorg/spongycastle/asn1/x509/DistributionPointName;Lorg/spongycastle/asn1/x509/ReasonFlags;Lorg/spongycastle/asn1/x509/GeneralNames;)V
        //   337: aload_0        
        //   338: aload           10
        //   340: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.clone:()Ljava/lang/Object;
        //   343: checkcast       Lorg/spongycastle/jcajce/PKIXExtendedParameters;
        //   346: aload_3        
        //   347: aload_2        
        //   348: aload           8
        //   350: aload           9
        //   352: aload           4
        //   354: aload           5
        //   356: invokestatic    org/spongycastle/jce/provider/RFC3281CertPathUtilities.checkCRL:(Lorg/spongycastle/asn1/x509/DistributionPoint;Lorg/spongycastle/x509/X509AttributeCertificate;Lorg/spongycastle/jcajce/PKIXExtendedParameters;Ljava/util/Date;Ljava/security/cert/X509Certificate;Lorg/spongycastle/jce/provider/CertStatus;Lorg/spongycastle/jce/provider/ReasonsMask;Ljava/util/List;Lorg/spongycastle/jcajce/util/JcaJceHelper;)V
        //   359: aload_1        
        //   360: astore_0       
        //   361: iconst_1       
        //   362: istore          6
        //   364: goto            397
        //   367: astore_0       
        //   368: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   371: dup            
        //   372: ldc_w           "Issuer from certificate for CRL could not be reencoded."
        //   375: aload_0        
        //   376: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   379: athrow         
        //   380: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   383: dup            
        //   384: ldc_w           "No valid CRL for distribution point found."
        //   387: aload_0        
        //   388: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   391: astore_0       
        //   392: goto            397
        //   395: aload_1        
        //   396: astore_0       
        //   397: iload           6
        //   399: ifeq            538
        //   402: aload           8
        //   404: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   407: bipush          11
        //   409: if_icmpne       459
        //   412: aload           9
        //   414: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   417: ifne            437
        //   420: aload           8
        //   422: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   425: bipush          11
        //   427: if_icmpne       437
        //   430: aload           8
        //   432: bipush          12
        //   434: invokevirtual   org/spongycastle/jce/provider/CertStatus.setCertStatus:(I)V
        //   437: aload           8
        //   439: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   442: bipush          12
        //   444: if_icmpeq       448
        //   447: return         
        //   448: new             Ljava/security/cert/CertPathValidatorException;
        //   451: dup            
        //   452: ldc_w           "Attribute certificate status could not be determined."
        //   455: invokespecial   java/security/cert/CertPathValidatorException.<init>:(Ljava/lang/String;)V
        //   458: athrow         
        //   459: new             Ljava/lang/StringBuilder;
        //   462: dup            
        //   463: invokespecial   java/lang/StringBuilder.<init>:()V
        //   466: astore_0       
        //   467: aload_0        
        //   468: ldc_w           "Attribute certificate revocation after "
        //   471: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   474: pop            
        //   475: aload_0        
        //   476: aload           8
        //   478: invokevirtual   org/spongycastle/jce/provider/CertStatus.getRevocationDate:()Ljava/util/Date;
        //   481: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   484: pop            
        //   485: aload_0        
        //   486: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   489: astore_0       
        //   490: new             Ljava/lang/StringBuilder;
        //   493: dup            
        //   494: invokespecial   java/lang/StringBuilder.<init>:()V
        //   497: astore_1       
        //   498: aload_1        
        //   499: aload_0        
        //   500: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   503: pop            
        //   504: aload_1        
        //   505: ldc_w           ", reason: "
        //   508: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   511: pop            
        //   512: aload_1        
        //   513: getstatic       org/spongycastle/jce/provider/RFC3280CertPathUtilities.crlReasons:[Ljava/lang/String;
        //   516: aload           8
        //   518: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   521: aaload         
        //   522: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   525: pop            
        //   526: new             Ljava/security/cert/CertPathValidatorException;
        //   529: dup            
        //   530: aload_1        
        //   531: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   534: invokespecial   java/security/cert/CertPathValidatorException.<init>:(Ljava/lang/String;)V
        //   537: athrow         
        //   538: new             Lorg/spongycastle/jce/exception/ExtCertPathValidatorException;
        //   541: dup            
        //   542: ldc_w           "No valid CRL found."
        //   545: aload_0        
        //   546: invokespecial   org/spongycastle/jce/exception/ExtCertPathValidatorException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   549: athrow         
        //   550: astore_0       
        //   551: new             Ljava/security/cert/CertPathValidatorException;
        //   554: dup            
        //   555: ldc_w           "No additional CRL locations could be decoded from CRL distribution point extension."
        //   558: aload_0        
        //   559: invokespecial   java/security/cert/CertPathValidatorException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   562: athrow         
        //   563: astore_0       
        //   564: new             Ljava/security/cert/CertPathValidatorException;
        //   567: dup            
        //   568: ldc_w           "CRL distribution point extension could not be read."
        //   571: aload_0        
        //   572: invokespecial   java/security/cert/CertPathValidatorException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   575: athrow         
        //   576: aload_0        
        //   577: getstatic       org/spongycastle/jce/provider/RFC3281CertPathUtilities.CRL_DISTRIBUTION_POINTS:Ljava/lang/String;
        //   580: invokeinterface org/spongycastle/x509/X509AttributeCertificate.getExtensionValue:(Ljava/lang/String;)[B
        //   585: ifnonnull       601
        //   588: aload_0        
        //   589: getstatic       org/spongycastle/jce/provider/RFC3281CertPathUtilities.AUTHORITY_INFO_ACCESS:Ljava/lang/String;
        //   592: invokeinterface org/spongycastle/x509/X509AttributeCertificate.getExtensionValue:(Ljava/lang/String;)[B
        //   597: ifnonnull       601
        //   600: return         
        //   601: new             Ljava/security/cert/CertPathValidatorException;
        //   604: dup            
        //   605: ldc_w           "No rev avail extension is set, but also an AC revocation pointer."
        //   608: invokespecial   java/security/cert/CertPathValidatorException.<init>:(Ljava/lang/String;)V
        //   611: athrow         
        //   612: return         
        //   613: astore_0       
        //   614: goto            380
        //    Exceptions:
        //  throws java.security.cert.CertPathValidatorException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  19     31     563    576    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  40     57     550    563    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  127    133    238    251    Ljava/lang/Exception;
        //  139    174    219    223    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  180    198    210    211    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  274    303    367    380    Ljava/lang/Exception;
        //  274    303    613    395    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  303    359    613    395    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  368    380    613    395    Lorg/spongycastle/jce/provider/AnnotatedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0380:
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
    
    protected static CertPath processAttrCert1(X509AttributeCertificate build, final PKIXExtendedParameters pkixExtendedParameters) throws CertPathValidatorException {
        final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
        final Principal[] issuer = build.getHolder().getIssuer();
        final int n = 0;
        if (issuer != null) {
            final X509CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setSerialNumber(build.getHolder().getSerialNumber());
            final Principal[] issuer2 = build.getHolder().getIssuer();
            int i = 0;
            while (i < issuer2.length) {
                try {
                    if (issuer2[i] instanceof X500Principal) {
                        x509CertSelector.setIssuer(((X500Principal)issuer2[i]).getEncoded());
                    }
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder(x509CertSelector).build(), pkixExtendedParameters.getCertStores()));
                    ++i;
                    continue;
                }
                catch (IOException ex) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", ex);
                }
                catch (AnnotatedException ex2) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", ex2);
                }
                break;
            }
            if (set.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in base certificate ID for attribute certificate cannot be found.");
            }
        }
        if (build.getHolder().getEntityNames() != null) {
            final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            final Principal[] entityNames = build.getHolder().getEntityNames();
            int j = n;
            while (j < entityNames.length) {
                try {
                    if (entityNames[j] instanceof X500Principal) {
                        x509CertStoreSelector.setIssuer(((X500Principal)entityNames[j]).getEncoded());
                    }
                    set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(new PKIXCertStoreSelector.Builder(x509CertStoreSelector).build(), pkixExtendedParameters.getCertStores()));
                    ++j;
                    continue;
                }
                catch (IOException ex3) {
                    throw new ExtCertPathValidatorException("Unable to encode X500 principal.", ex3);
                }
                catch (AnnotatedException ex4) {
                    throw new ExtCertPathValidatorException("Public key certificate for attribute certificate cannot be searched.", ex4);
                }
                break;
            }
            if (set.isEmpty()) {
                throw new CertPathValidatorException("Public key certificate specified in entity name for attribute certificate cannot be found.");
            }
        }
        final PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder(pkixExtendedParameters);
        final Iterator<Object> iterator = set.iterator();
        Object o = null;
        build = null;
        while (iterator.hasNext()) {
            final X509CertStoreSelector x509CertStoreSelector2 = new X509CertStoreSelector();
            x509CertStoreSelector2.setCertificate(iterator.next());
            builder.setTargetConstraints(new PKIXCertStoreSelector.Builder(x509CertStoreSelector2).build());
            try {
                final CertPathBuilder instance = CertPathBuilder.getInstance("PKIX", "SC");
                try {
                    build = (X509AttributeCertificate)instance.build(new PKIXExtendedBuilderParameters.Builder(builder.build()).build());
                }
                catch (InvalidAlgorithmParameterException ex5) {
                    throw new RuntimeException(ex5.getMessage());
                }
                catch (CertPathBuilderException ex6) {
                    o = new ExtCertPathValidatorException("Certification path for public key certificate of attribute certificate could not be build.", ex6);
                }
            }
            catch (NoSuchAlgorithmException ex7) {
                throw new ExtCertPathValidatorException("Support class could not be created.", ex7);
            }
            catch (NoSuchProviderException ex8) {
                throw new ExtCertPathValidatorException("Support class could not be created.", ex8);
            }
            break;
        }
        if (o == null) {
            return ((CertPathBuilderResult)build).getCertPath();
        }
        throw o;
    }
    
    protected static CertPathValidatorResult processAttrCert2(final CertPath certPath, final PKIXExtendedParameters pkixExtendedParameters) throws CertPathValidatorException {
        try {
            final CertPathValidator instance = CertPathValidator.getInstance("PKIX", "SC");
            try {
                return instance.validate(certPath, pkixExtendedParameters);
            }
            catch (InvalidAlgorithmParameterException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            catch (CertPathValidatorException ex2) {
                throw new ExtCertPathValidatorException("Certification path for issuer certificate of attribute certificate could not be validated.", ex2);
            }
        }
        catch (NoSuchAlgorithmException ex3) {
            throw new ExtCertPathValidatorException("Support class could not be created.", ex3);
        }
        catch (NoSuchProviderException ex4) {
            throw new ExtCertPathValidatorException("Support class could not be created.", ex4);
        }
    }
    
    protected static void processAttrCert3(final X509Certificate x509Certificate, final PKIXExtendedParameters pkixExtendedParameters) throws CertPathValidatorException {
        if (x509Certificate.getKeyUsage() != null && !x509Certificate.getKeyUsage()[0] && !x509Certificate.getKeyUsage()[1]) {
            throw new CertPathValidatorException("Attribute certificate issuer public key cannot be used to validate digital signatures.");
        }
        if (x509Certificate.getBasicConstraints() == -1) {
            return;
        }
        throw new CertPathValidatorException("Attribute certificate issuer is also a public key certificate issuer.");
    }
    
    protected static void processAttrCert4(final X509Certificate x509Certificate, final Set set) throws CertPathValidatorException {
        final Iterator<TrustAnchor> iterator = set.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final TrustAnchor trustAnchor = iterator.next();
            if (x509Certificate.getSubjectX500Principal().getName("RFC2253").equals(trustAnchor.getCAName()) || x509Certificate.equals(trustAnchor.getTrustedCert())) {
                b = true;
            }
        }
        if (b) {
            return;
        }
        throw new CertPathValidatorException("Attribute certificate issuer is not directly trusted.");
    }
    
    protected static void processAttrCert5(final X509AttributeCertificate x509AttributeCertificate, final PKIXExtendedParameters pkixExtendedParameters) throws CertPathValidatorException {
        try {
            x509AttributeCertificate.checkValidity(CertPathValidatorUtilities.getValidDate(pkixExtendedParameters));
        }
        catch (CertificateNotYetValidException ex) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", ex);
        }
        catch (CertificateExpiredException ex2) {
            throw new ExtCertPathValidatorException("Attribute certificate is not valid.", ex2);
        }
    }
    
    protected static void processAttrCert7(final X509AttributeCertificate x509AttributeCertificate, final CertPath certPath, final CertPath certPath2, final PKIXExtendedParameters pkixExtendedParameters, final Set set) throws CertPathValidatorException {
        final Set<String> criticalExtensionOIDs = x509AttributeCertificate.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs.contains(RFC3281CertPathUtilities.TARGET_INFORMATION)) {
            try {
                TargetInformation.getInstance(CertPathValidatorUtilities.getExtensionValue(x509AttributeCertificate, RFC3281CertPathUtilities.TARGET_INFORMATION));
            }
            catch (IllegalArgumentException ex) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", ex);
            }
            catch (AnnotatedException ex2) {
                throw new ExtCertPathValidatorException("Target information extension could not be read.", ex2);
            }
        }
        criticalExtensionOIDs.remove(RFC3281CertPathUtilities.TARGET_INFORMATION);
        final Iterator<PKIXAttrCertChecker> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next().check(x509AttributeCertificate, certPath, certPath2, criticalExtensionOIDs);
        }
        if (criticalExtensionOIDs.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Attribute certificate contains unsupported critical extensions: ");
        sb.append(criticalExtensionOIDs);
        throw new CertPathValidatorException(sb.toString());
    }
}
