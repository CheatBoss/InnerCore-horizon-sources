package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.jce.exception.*;
import java.util.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.*;
import java.security.*;
import java.security.cert.*;
import org.spongycastle.asn1.x500.style.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x500.*;
import java.math.*;

class RFC3280CertPathUtilities
{
    public static final String ANY_POLICY = "2.5.29.32.0";
    public static final String AUTHORITY_KEY_IDENTIFIER;
    public static final String BASIC_CONSTRAINTS;
    public static final String CERTIFICATE_POLICIES;
    public static final String CRL_DISTRIBUTION_POINTS;
    public static final String CRL_NUMBER;
    protected static final int CRL_SIGN = 6;
    private static final PKIXCRLUtil CRL_UTIL;
    public static final String DELTA_CRL_INDICATOR;
    public static final String FRESHEST_CRL;
    public static final String INHIBIT_ANY_POLICY;
    public static final String ISSUING_DISTRIBUTION_POINT;
    protected static final int KEY_CERT_SIGN = 5;
    public static final String KEY_USAGE;
    public static final String NAME_CONSTRAINTS;
    public static final String POLICY_CONSTRAINTS;
    public static final String POLICY_MAPPINGS;
    public static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String[] crlReasons;
    
    static {
        CRL_UTIL = new PKIXCRLUtil();
        CERTIFICATE_POLICIES = Extension.certificatePolicies.getId();
        POLICY_MAPPINGS = Extension.policyMappings.getId();
        INHIBIT_ANY_POLICY = Extension.inhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = Extension.issuingDistributionPoint.getId();
        FRESHEST_CRL = Extension.freshestCRL.getId();
        DELTA_CRL_INDICATOR = Extension.deltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = Extension.policyConstraints.getId();
        BASIC_CONSTRAINTS = Extension.basicConstraints.getId();
        CRL_DISTRIBUTION_POINTS = Extension.cRLDistributionPoints.getId();
        SUBJECT_ALTERNATIVE_NAME = Extension.subjectAlternativeName.getId();
        NAME_CONSTRAINTS = Extension.nameConstraints.getId();
        AUTHORITY_KEY_IDENTIFIER = Extension.authorityKeyIdentifier.getId();
        KEY_USAGE = Extension.keyUsage.getId();
        CRL_NUMBER = Extension.cRLNumber.getId();
        crlReasons = new String[] { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
    }
    
    private static void checkCRL(final DistributionPoint p0, final PKIXExtendedParameters p1, final X509Certificate p2, final Date p3, final X509Certificate p4, final PublicKey p5, final CertStatus p6, final ReasonsMask p7, final List p8, final JcaJceHelper p9) throws AnnotatedException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokestatic    java/lang/System.currentTimeMillis:()J
        //     7: invokespecial   java/util/Date.<init>:(J)V
        //    10: astore          13
        //    12: aload_3        
        //    13: invokevirtual   java/util/Date.getTime:()J
        //    16: aload           13
        //    18: invokevirtual   java/util/Date.getTime:()J
        //    21: lcmp           
        //    22: ifgt            523
        //    25: aload_0        
        //    26: aload_2        
        //    27: aload           13
        //    29: aload_1        
        //    30: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getCompleteCRLs:(Lorg/spongycastle/asn1/x509/DistributionPoint;Ljava/lang/Object;Ljava/util/Date;Lorg/spongycastle/jcajce/PKIXExtendedParameters;)Ljava/util/Set;
        //    33: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    38: astore          18
        //    40: aload           7
        //    42: astore          14
        //    44: aconst_null    
        //    45: astore          15
        //    47: iconst_0       
        //    48: istore          10
        //    50: aload           18
        //    52: invokeinterface java/util/Iterator.hasNext:()Z
        //    57: ifeq            514
        //    60: aload           6
        //    62: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //    65: bipush          11
        //    67: if_icmpne       514
        //    70: aload           7
        //    72: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //    75: ifne            514
        //    78: aload           18
        //    80: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    85: checkcast       Ljava/security/cert/X509CRL;
        //    88: astore          19
        //    90: aload           19
        //    92: aload_0        
        //    93: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLD:(Ljava/security/cert/X509CRL;Lorg/spongycastle/asn1/x509/DistributionPoint;)Lorg/spongycastle/jce/provider/ReasonsMask;
        //    96: astore          20
        //    98: aload           20
        //   100: aload           14
        //   102: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.hasNewReasons:(Lorg/spongycastle/jce/provider/ReasonsMask;)Z
        //   105: istore          12
        //   107: iload           12
        //   109: ifne            115
        //   112: goto            50
        //   115: aload           13
        //   117: astore          14
        //   119: aload           19
        //   121: aload           19
        //   123: aload_2        
        //   124: aload           4
        //   126: aload           5
        //   128: aload_1        
        //   129: aload           8
        //   131: aload           9
        //   133: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLF:(Ljava/security/cert/X509CRL;Ljava/lang/Object;Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Lorg/spongycastle/jcajce/PKIXExtendedParameters;Ljava/util/List;Lorg/spongycastle/jcajce/util/JcaJceHelper;)Ljava/util/Set;
        //   136: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLG:(Ljava/security/cert/X509CRL;Ljava/util/Set;)Ljava/security/PublicKey;
        //   139: astore          17
        //   141: aload_1        
        //   142: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getDate:()Ljava/util/Date;
        //   145: astore          16
        //   147: aload           16
        //   149: ifnull          166
        //   152: aload_1        
        //   153: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getDate:()Ljava/util/Date;
        //   156: astore          16
        //   158: goto            170
        //   161: astore          14
        //   163: goto            498
        //   166: aload           14
        //   168: astore          16
        //   170: aload_1        
        //   171: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.isUseDeltasEnabled:()Z
        //   174: istore          12
        //   176: iload           12
        //   178: ifeq            206
        //   181: aload           16
        //   183: aload           19
        //   185: aload_1        
        //   186: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getCertStores:()Ljava/util/List;
        //   189: aload_1        
        //   190: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getCRLStores:()Ljava/util/List;
        //   193: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getDeltaCRLs:(Ljava/util/Date;Ljava/security/cert/X509CRL;Ljava/util/List;Ljava/util/List;)Ljava/util/Set;
        //   196: aload           17
        //   198: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLH:(Ljava/util/Set;Ljava/security/PublicKey;)Ljava/security/cert/X509CRL;
        //   201: astore          17
        //   203: goto            209
        //   206: aconst_null    
        //   207: astore          17
        //   209: aload_1        
        //   210: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getValidityModel:()I
        //   213: istore          11
        //   215: iload           11
        //   217: iconst_1       
        //   218: if_icmpeq       253
        //   221: aload_2        
        //   222: invokevirtual   java/security/cert/X509Certificate.getNotAfter:()Ljava/util/Date;
        //   225: invokevirtual   java/util/Date.getTime:()J
        //   228: aload           19
        //   230: invokevirtual   java/security/cert/X509CRL.getThisUpdate:()Ljava/util/Date;
        //   233: invokevirtual   java/util/Date.getTime:()J
        //   236: lcmp           
        //   237: iflt            243
        //   240: goto            253
        //   243: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   246: dup            
        //   247: ldc             "No valid CRL for current time found."
        //   249: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   252: athrow         
        //   253: aload_0        
        //   254: aload_2        
        //   255: aload           19
        //   257: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLB1:(Lorg/spongycastle/asn1/x509/DistributionPoint;Ljava/lang/Object;Ljava/security/cert/X509CRL;)V
        //   260: aload_0        
        //   261: aload_2        
        //   262: aload           19
        //   264: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLB2:(Lorg/spongycastle/asn1/x509/DistributionPoint;Ljava/lang/Object;Ljava/security/cert/X509CRL;)V
        //   267: aload           17
        //   269: aload           19
        //   271: aload_1        
        //   272: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLC:(Ljava/security/cert/X509CRL;Ljava/security/cert/X509CRL;Lorg/spongycastle/jcajce/PKIXExtendedParameters;)V
        //   275: aload_3        
        //   276: aload           17
        //   278: aload_2        
        //   279: aload           6
        //   281: aload_1        
        //   282: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLI:(Ljava/util/Date;Ljava/security/cert/X509CRL;Ljava/lang/Object;Lorg/spongycastle/jce/provider/CertStatus;Lorg/spongycastle/jcajce/PKIXExtendedParameters;)V
        //   285: aload_3        
        //   286: aload           19
        //   288: aload_2        
        //   289: aload           6
        //   291: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.processCRLJ:(Ljava/util/Date;Ljava/security/cert/X509CRL;Ljava/lang/Object;Lorg/spongycastle/jce/provider/CertStatus;)V
        //   294: aload           6
        //   296: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   299: bipush          8
        //   301: if_icmpne       311
        //   304: aload           6
        //   306: bipush          11
        //   308: invokevirtual   org/spongycastle/jce/provider/CertStatus.setCertStatus:(I)V
        //   311: aload           7
        //   313: astore          16
        //   315: aload           16
        //   317: aload           20
        //   319: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.addReasons:(Lorg/spongycastle/jce/provider/ReasonsMask;)V
        //   322: aload           19
        //   324: invokevirtual   java/security/cert/X509CRL.getCriticalExtensionOIDs:()Ljava/util/Set;
        //   327: astore          19
        //   329: aload           19
        //   331: ifnull          397
        //   334: new             Ljava/util/HashSet;
        //   337: dup            
        //   338: aload           19
        //   340: invokespecial   java/util/HashSet.<init>:(Ljava/util/Collection;)V
        //   343: astore          19
        //   345: aload           19
        //   347: getstatic       org/spongycastle/asn1/x509/Extension.issuingDistributionPoint:Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   350: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   353: invokeinterface java/util/Set.remove:(Ljava/lang/Object;)Z
        //   358: pop            
        //   359: aload           19
        //   361: getstatic       org/spongycastle/asn1/x509/Extension.deltaCRLIndicator:Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   364: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   367: invokeinterface java/util/Set.remove:(Ljava/lang/Object;)Z
        //   372: pop            
        //   373: aload           19
        //   375: invokeinterface java/util/Set.isEmpty:()Z
        //   380: ifeq            386
        //   383: goto            397
        //   386: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   389: dup            
        //   390: ldc_w           "CRL contains unsupported critical extensions."
        //   393: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   396: athrow         
        //   397: aload           17
        //   399: ifnull          477
        //   402: aload           17
        //   404: invokevirtual   java/security/cert/X509CRL.getCriticalExtensionOIDs:()Ljava/util/Set;
        //   407: astore          17
        //   409: aload           17
        //   411: ifnull          477
        //   414: new             Ljava/util/HashSet;
        //   417: dup            
        //   418: aload           17
        //   420: invokespecial   java/util/HashSet.<init>:(Ljava/util/Collection;)V
        //   423: astore          17
        //   425: aload           17
        //   427: getstatic       org/spongycastle/asn1/x509/Extension.issuingDistributionPoint:Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   430: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   433: invokeinterface java/util/Set.remove:(Ljava/lang/Object;)Z
        //   438: pop            
        //   439: aload           17
        //   441: getstatic       org/spongycastle/asn1/x509/Extension.deltaCRLIndicator:Lorg/spongycastle/asn1/ASN1ObjectIdentifier;
        //   444: invokevirtual   org/spongycastle/asn1/ASN1ObjectIdentifier.getId:()Ljava/lang/String;
        //   447: invokeinterface java/util/Set.remove:(Ljava/lang/Object;)Z
        //   452: pop            
        //   453: aload           17
        //   455: invokeinterface java/util/Set.isEmpty:()Z
        //   460: ifeq            466
        //   463: goto            477
        //   466: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   469: dup            
        //   470: ldc_w           "Delta CRL contains unsupported critical extension."
        //   473: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   476: athrow         
        //   477: aload           14
        //   479: astore          13
        //   481: iconst_1       
        //   482: istore          10
        //   484: aload           16
        //   486: astore          14
        //   488: goto            50
        //   491: astore          14
        //   493: goto            498
        //   496: astore          14
        //   498: aload           14
        //   500: astore          15
        //   502: aload           7
        //   504: astore          14
        //   506: goto            511
        //   509: astore          15
        //   511: goto            50
        //   514: iload           10
        //   516: ifeq            520
        //   519: return         
        //   520: aload           15
        //   522: athrow         
        //   523: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   526: dup            
        //   527: ldc_w           "Validation time is in future."
        //   530: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   533: athrow         
        //   534: astore          14
        //   536: goto            498
        //    Exceptions:
        //  throws org.spongycastle.jce.provider.AnnotatedException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  78     107    509    511    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  119    147    496    498    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  152    158    161    166    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  170    176    496    498    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  181    203    161    166    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  209    215    496    498    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  221    240    534    539    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  243    253    534    539    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  253    311    534    539    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  315    329    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  334    383    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  386    397    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  402    409    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  414    463    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  466    477    491    496    Lorg/spongycastle/jce/provider/AnnotatedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0243:
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
    
    protected static void checkCRLs(final PKIXExtendedParameters p0, final X509Certificate p1, final Date p2, final X509Certificate p3, final PublicKey p4, final List p5, final JcaJceHelper p6) throws AnnotatedException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       org/spongycastle/jce/provider/RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS:Ljava/lang/String;
        //     4: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getExtensionValue:(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/spongycastle/asn1/ASN1Primitive;
        //     7: invokestatic    org/spongycastle/asn1/x509/CRLDistPoint.getInstance:(Ljava/lang/Object;)Lorg/spongycastle/asn1/x509/CRLDistPoint;
        //    10: astore          10
        //    12: new             Lorg/spongycastle/jcajce/PKIXExtendedParameters$Builder;
        //    15: dup            
        //    16: aload_0        
        //    17: invokespecial   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.<init>:(Lorg/spongycastle/jcajce/PKIXExtendedParameters;)V
        //    20: astore          11
        //    22: aload           10
        //    24: aload_0        
        //    25: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.getNamedCRLStoreMap:()Ljava/util/Map;
        //    28: invokestatic    org/spongycastle/jce/provider/CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint:(Lorg/spongycastle/asn1/x509/CRLDistPoint;Ljava/util/Map;)Ljava/util/List;
        //    31: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    36: astore          12
        //    38: aload           12
        //    40: invokeinterface java/util/Iterator.hasNext:()Z
        //    45: ifeq            67
        //    48: aload           11
        //    50: aload           12
        //    52: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    57: checkcast       Lorg/spongycastle/jcajce/PKIXCRLStore;
        //    60: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.addCRLStore:(Lorg/spongycastle/jcajce/PKIXCRLStore;)Lorg/spongycastle/jcajce/PKIXExtendedParameters$Builder;
        //    63: pop            
        //    64: goto            38
        //    67: new             Lorg/spongycastle/jce/provider/CertStatus;
        //    70: dup            
        //    71: invokespecial   org/spongycastle/jce/provider/CertStatus.<init>:()V
        //    74: astore          12
        //    76: new             Lorg/spongycastle/jce/provider/ReasonsMask;
        //    79: dup            
        //    80: invokespecial   org/spongycastle/jce/provider/ReasonsMask.<init>:()V
        //    83: astore          13
        //    85: aload           11
        //    87: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters$Builder.build:()Lorg/spongycastle/jcajce/PKIXExtendedParameters;
        //    90: astore          14
        //    92: iconst_1       
        //    93: istore          9
        //    95: aload           10
        //    97: ifnull          207
        //   100: aload           10
        //   102: invokevirtual   org/spongycastle/asn1/x509/CRLDistPoint.getDistributionPoints:()[Lorg/spongycastle/asn1/x509/DistributionPoint;
        //   105: astore          11
        //   107: aload           11
        //   109: ifnull          207
        //   112: aconst_null    
        //   113: astore          10
        //   115: iconst_0       
        //   116: istore          8
        //   118: iconst_0       
        //   119: istore          7
        //   121: iload           8
        //   123: aload           11
        //   125: arraylength    
        //   126: if_icmpge       191
        //   129: aload           12
        //   131: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   134: bipush          11
        //   136: if_icmpne       191
        //   139: aload           13
        //   141: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   144: ifne            191
        //   147: aload           11
        //   149: iload           8
        //   151: aaload         
        //   152: astore          15
        //   154: aload           15
        //   156: aload           14
        //   158: aload_1        
        //   159: aload_2        
        //   160: aload_3        
        //   161: aload           4
        //   163: aload           12
        //   165: aload           13
        //   167: aload           5
        //   169: aload           6
        //   171: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.checkCRL:(Lorg/spongycastle/asn1/x509/DistributionPoint;Lorg/spongycastle/jcajce/PKIXExtendedParameters;Ljava/security/cert/X509Certificate;Ljava/util/Date;Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Lorg/spongycastle/jce/provider/CertStatus;Lorg/spongycastle/jce/provider/ReasonsMask;Ljava/util/List;Lorg/spongycastle/jcajce/util/JcaJceHelper;)V
        //   174: iconst_1       
        //   175: istore          7
        //   177: goto            182
        //   180: astore          10
        //   182: iload           8
        //   184: iconst_1       
        //   185: iadd           
        //   186: istore          8
        //   188: goto            121
        //   191: goto            213
        //   194: astore_0       
        //   195: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   198: dup            
        //   199: ldc_w           "Distribution points could not be read."
        //   202: aload_0        
        //   203: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   206: athrow         
        //   207: aconst_null    
        //   208: astore          10
        //   210: iconst_0       
        //   211: istore          7
        //   213: aload           10
        //   215: astore          11
        //   217: aload           12
        //   219: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   222: bipush          11
        //   224: if_icmpne       335
        //   227: aload           10
        //   229: astore          11
        //   231: aload           13
        //   233: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   236: ifne            335
        //   239: new             Lorg/spongycastle/asn1/ASN1InputStream;
        //   242: dup            
        //   243: aload_1        
        //   244: invokestatic    org/spongycastle/jce/provider/PrincipalUtils.getEncodedIssuerPrincipal:(Ljava/lang/Object;)Lorg/spongycastle/asn1/x500/X500Name;
        //   247: invokevirtual   org/spongycastle/asn1/x500/X500Name.getEncoded:()[B
        //   250: invokespecial   org/spongycastle/asn1/ASN1InputStream.<init>:([B)V
        //   253: invokevirtual   org/spongycastle/asn1/ASN1InputStream.readObject:()Lorg/spongycastle/asn1/ASN1Primitive;
        //   256: astore          11
        //   258: new             Lorg/spongycastle/asn1/x509/DistributionPoint;
        //   261: dup            
        //   262: new             Lorg/spongycastle/asn1/x509/DistributionPointName;
        //   265: dup            
        //   266: iconst_0       
        //   267: new             Lorg/spongycastle/asn1/x509/GeneralNames;
        //   270: dup            
        //   271: new             Lorg/spongycastle/asn1/x509/GeneralName;
        //   274: dup            
        //   275: iconst_4       
        //   276: aload           11
        //   278: invokespecial   org/spongycastle/asn1/x509/GeneralName.<init>:(ILorg/spongycastle/asn1/ASN1Encodable;)V
        //   281: invokespecial   org/spongycastle/asn1/x509/GeneralNames.<init>:(Lorg/spongycastle/asn1/x509/GeneralName;)V
        //   284: invokespecial   org/spongycastle/asn1/x509/DistributionPointName.<init>:(ILorg/spongycastle/asn1/ASN1Encodable;)V
        //   287: aconst_null    
        //   288: aconst_null    
        //   289: invokespecial   org/spongycastle/asn1/x509/DistributionPoint.<init>:(Lorg/spongycastle/asn1/x509/DistributionPointName;Lorg/spongycastle/asn1/x509/ReasonFlags;Lorg/spongycastle/asn1/x509/GeneralNames;)V
        //   292: aload_0        
        //   293: invokevirtual   org/spongycastle/jcajce/PKIXExtendedParameters.clone:()Ljava/lang/Object;
        //   296: checkcast       Lorg/spongycastle/jcajce/PKIXExtendedParameters;
        //   299: aload_1        
        //   300: aload_2        
        //   301: aload_3        
        //   302: aload           4
        //   304: aload           12
        //   306: aload           13
        //   308: aload           5
        //   310: aload           6
        //   312: invokestatic    org/spongycastle/jce/provider/RFC3280CertPathUtilities.checkCRL:(Lorg/spongycastle/asn1/x509/DistributionPoint;Lorg/spongycastle/jcajce/PKIXExtendedParameters;Ljava/security/cert/X509Certificate;Ljava/util/Date;Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Lorg/spongycastle/jce/provider/CertStatus;Lorg/spongycastle/jce/provider/ReasonsMask;Ljava/util/List;Lorg/spongycastle/jcajce/util/JcaJceHelper;)V
        //   315: iload           9
        //   317: istore          7
        //   319: goto            339
        //   322: astore_0       
        //   323: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   326: dup            
        //   327: ldc_w           "Issuer from certificate for CRL could not be reencoded."
        //   330: aload_0        
        //   331: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   334: athrow         
        //   335: aload           11
        //   337: astore          10
        //   339: iload           7
        //   341: ifne            368
        //   344: aload           10
        //   346: instanceof      Lorg/spongycastle/jce/provider/AnnotatedException;
        //   349: ifeq            355
        //   352: aload           10
        //   354: athrow         
        //   355: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   358: dup            
        //   359: ldc_w           "No valid CRL found."
        //   362: aload           10
        //   364: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   367: athrow         
        //   368: aload           12
        //   370: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   373: bipush          11
        //   375: if_icmpne       425
        //   378: aload           13
        //   380: invokevirtual   org/spongycastle/jce/provider/ReasonsMask.isAllReasons:()Z
        //   383: ifne            403
        //   386: aload           12
        //   388: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   391: bipush          11
        //   393: if_icmpne       403
        //   396: aload           12
        //   398: bipush          12
        //   400: invokevirtual   org/spongycastle/jce/provider/CertStatus.setCertStatus:(I)V
        //   403: aload           12
        //   405: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   408: bipush          12
        //   410: if_icmpeq       414
        //   413: return         
        //   414: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   417: dup            
        //   418: ldc_w           "Certificate status could not be determined."
        //   421: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   424: athrow         
        //   425: new             Ljava/text/SimpleDateFormat;
        //   428: dup            
        //   429: ldc_w           "yyyy-MM-dd HH:mm:ss Z"
        //   432: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;)V
        //   435: astore_0       
        //   436: aload_0        
        //   437: ldc_w           "UTC"
        //   440: invokestatic    java/util/TimeZone.getTimeZone:(Ljava/lang/String;)Ljava/util/TimeZone;
        //   443: invokevirtual   java/text/SimpleDateFormat.setTimeZone:(Ljava/util/TimeZone;)V
        //   446: new             Ljava/lang/StringBuilder;
        //   449: dup            
        //   450: invokespecial   java/lang/StringBuilder.<init>:()V
        //   453: astore_1       
        //   454: aload_1        
        //   455: ldc_w           "Certificate revocation after "
        //   458: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   461: pop            
        //   462: aload_1        
        //   463: aload_0        
        //   464: aload           12
        //   466: invokevirtual   org/spongycastle/jce/provider/CertStatus.getRevocationDate:()Ljava/util/Date;
        //   469: invokevirtual   java/text/SimpleDateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //   472: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   475: pop            
        //   476: aload_1        
        //   477: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   480: astore_0       
        //   481: new             Ljava/lang/StringBuilder;
        //   484: dup            
        //   485: invokespecial   java/lang/StringBuilder.<init>:()V
        //   488: astore_1       
        //   489: aload_1        
        //   490: aload_0        
        //   491: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   494: pop            
        //   495: aload_1        
        //   496: ldc_w           ", reason: "
        //   499: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   502: pop            
        //   503: aload_1        
        //   504: getstatic       org/spongycastle/jce/provider/RFC3280CertPathUtilities.crlReasons:[Ljava/lang/String;
        //   507: aload           12
        //   509: invokevirtual   org/spongycastle/jce/provider/CertStatus.getCertStatus:()I
        //   512: aaload         
        //   513: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   516: pop            
        //   517: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   520: dup            
        //   521: aload_1        
        //   522: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   525: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;)V
        //   528: athrow         
        //   529: astore_0       
        //   530: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   533: dup            
        //   534: ldc_w           "No additional CRL locations could be decoded from CRL distribution point extension."
        //   537: aload_0        
        //   538: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   541: athrow         
        //   542: astore_0       
        //   543: new             Lorg/spongycastle/jce/provider/AnnotatedException;
        //   546: dup            
        //   547: ldc_w           "CRL distribution point extension could not be read."
        //   550: aload_0        
        //   551: invokespecial   org/spongycastle/jce/provider/AnnotatedException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   554: athrow         
        //   555: astore          11
        //   557: goto            335
        //    Exceptions:
        //  throws org.spongycastle.jce.provider.AnnotatedException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  0      12     542    555    Ljava/lang/Exception;
        //  22     38     529    542    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  38     64     529    542    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  100    107    194    207    Ljava/lang/Exception;
        //  154    174    180    182    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  239    258    322    335    Ljava/lang/Exception;
        //  239    258    555    560    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  258    315    555    560    Lorg/spongycastle/jce/provider/AnnotatedException;
        //  323    335    555    560    Lorg/spongycastle/jce/provider/AnnotatedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0335:
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
    
    protected static PKIXPolicyNode prepareCertB(final CertPath certPath, final int n, final List[] array, PKIXPolicyNode removePolicyNode, final int n2) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(n);
        final int n3 = certificates.size() - n;
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_MAPPINGS));
            PKIXPolicyNode pkixPolicyNode = null;
            if (instance != null) {
                final HashMap<Object, HashSet<String>> hashMap = new HashMap<Object, HashSet<String>>();
                final HashSet<String> set = new HashSet<String>();
                for (int i = 0; i < instance.size(); ++i) {
                    final ASN1Sequence asn1Sequence = (ASN1Sequence)instance.getObjectAt(i);
                    final String id = ((ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0)).getId();
                    final String id2 = ((ASN1ObjectIdentifier)asn1Sequence.getObjectAt(1)).getId();
                    if (!hashMap.containsKey(id)) {
                        final HashSet<String> set2 = new HashSet<String>();
                        set2.add(id2);
                        hashMap.put(id, set2);
                        set.add(id);
                    }
                    else {
                        hashMap.get(id).add(id2);
                    }
                }
                final Iterator<Object> iterator = set.iterator();
            Label_0315_Outer:
                while (true) {
                    pkixPolicyNode = removePolicyNode;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final String s = iterator.next();
                    PKIXPolicyNode pkixPolicyNode6 = null;
                    Label_0803: {
                        if (n2 > 0) {
                            while (true) {
                                for (final PKIXPolicyNode pkixPolicyNode2 : array[n3]) {
                                    if (pkixPolicyNode2.getValidPolicy().equals(s)) {
                                        pkixPolicyNode2.expectedPolicies = hashMap.get(s);
                                        final boolean b = true;
                                        Label_0603: {
                                            if (!b) {
                                                for (final PKIXPolicyNode pkixPolicyNode3 : array[n3]) {
                                                    if ("2.5.29.32.0".equals(pkixPolicyNode3.getValidPolicy())) {
                                                        try {
                                                            final Enumeration objects = ((ASN1Sequence)CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES)).getObjects();
                                                        Label_0473:
                                                            while (true) {
                                                                while (objects.hasMoreElements()) {
                                                                    try {
                                                                        final PolicyInformation instance2 = PolicyInformation.getInstance(objects.nextElement());
                                                                        if (!"2.5.29.32.0".equals(instance2.getPolicyIdentifier().getId())) {
                                                                            continue Label_0315_Outer;
                                                                        }
                                                                        try {
                                                                            final Set qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                                                                            break Label_0473;
                                                                        }
                                                                        catch (CertPathValidatorException ex) {
                                                                            throw new ExtCertPathValidatorException("Policy qualifier info set could not be decoded.", ex, certPath, n);
                                                                        }
                                                                        continue Label_0315_Outer;
                                                                    }
                                                                    catch (Exception ex2) {
                                                                        throw new CertPathValidatorException("Policy information could not be decoded.", ex2, certPath, n);
                                                                    }
                                                                    break;
                                                                    final boolean b2 = x509Certificate.getCriticalExtensionOIDs() != null && x509Certificate.getCriticalExtensionOIDs().contains(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                                                    final PKIXPolicyNode pkixPolicyNode4 = (PKIXPolicyNode)pkixPolicyNode3.getParent();
                                                                    if ("2.5.29.32.0".equals(pkixPolicyNode4.getValidPolicy())) {
                                                                        final Set qualifierSet;
                                                                        final PKIXPolicyNode pkixPolicyNode5 = new PKIXPolicyNode(new ArrayList(), n3, hashMap.get(s), pkixPolicyNode4, qualifierSet, s, b2);
                                                                        pkixPolicyNode4.addChild(pkixPolicyNode5);
                                                                        array[n3].add(pkixPolicyNode5);
                                                                        pkixPolicyNode6 = removePolicyNode;
                                                                        break Label_0803;
                                                                    }
                                                                    break Label_0603;
                                                                }
                                                                final Set qualifierSet = null;
                                                                continue Label_0473;
                                                            }
                                                        }
                                                        catch (AnnotatedException ex3) {
                                                            throw new ExtCertPathValidatorException("Certificate policies extension could not be decoded.", ex3, certPath, n);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        pkixPolicyNode6 = removePolicyNode;
                                        break Label_0803;
                                    }
                                }
                                final boolean b = false;
                                continue;
                            }
                        }
                        pkixPolicyNode6 = removePolicyNode;
                        if (n2 <= 0) {
                            final Iterator iterator4 = array[n3].iterator();
                            while (true) {
                                pkixPolicyNode6 = removePolicyNode;
                                if (!iterator4.hasNext()) {
                                    break;
                                }
                                final PKIXPolicyNode pkixPolicyNode7 = iterator4.next();
                                PKIXPolicyNode pkixPolicyNode8 = removePolicyNode;
                                if (pkixPolicyNode7.getValidPolicy().equals(s)) {
                                    ((PKIXPolicyNode)pkixPolicyNode7.getParent()).removeChild(pkixPolicyNode7);
                                    iterator4.remove();
                                    int n4 = n3 - 1;
                                    while (true) {
                                        pkixPolicyNode8 = removePolicyNode;
                                        if (n4 < 0) {
                                            break;
                                        }
                                        final List list = array[n4];
                                        int n5 = 0;
                                        PKIXPolicyNode pkixPolicyNode9;
                                        while (true) {
                                            pkixPolicyNode9 = removePolicyNode;
                                            if (n5 >= list.size()) {
                                                break;
                                            }
                                            final PKIXPolicyNode pkixPolicyNode10 = list.get(n5);
                                            PKIXPolicyNode pkixPolicyNode11 = removePolicyNode;
                                            if (!pkixPolicyNode10.hasChildren()) {
                                                removePolicyNode = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode10);
                                                if ((pkixPolicyNode11 = removePolicyNode) == null) {
                                                    pkixPolicyNode9 = removePolicyNode;
                                                    break;
                                                }
                                            }
                                            ++n5;
                                            removePolicyNode = pkixPolicyNode11;
                                        }
                                        removePolicyNode = pkixPolicyNode9;
                                        --n4;
                                    }
                                }
                                removePolicyNode = pkixPolicyNode8;
                            }
                        }
                    }
                    removePolicyNode = pkixPolicyNode6;
                }
            }
            else {
                pkixPolicyNode = removePolicyNode;
            }
            return pkixPolicyNode;
        }
        catch (AnnotatedException ex4) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", ex4, certPath, n);
        }
    }
    
    protected static void prepareNextCertA(final CertPath certPath, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_MAPPINGS));
            if (instance != null) {
                int i = 0;
                while (i < instance.size()) {
                    try {
                        final ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObjectAt(i));
                        final ASN1ObjectIdentifier instance3 = ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(0));
                        final ASN1ObjectIdentifier instance4 = ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(1));
                        if ("2.5.29.32.0".equals(instance3.getId())) {
                            throw new CertPathValidatorException("IssuerDomainPolicy is anyPolicy", null, certPath, n);
                        }
                        if (!"2.5.29.32.0".equals(instance4.getId())) {
                            ++i;
                            continue;
                        }
                        throw new CertPathValidatorException("SubjectDomainPolicy is anyPolicy,", null, certPath, n);
                    }
                    catch (Exception ex) {
                        throw new ExtCertPathValidatorException("Policy mappings extension contents could not be decoded.", ex, certPath, n);
                    }
                    break;
                }
            }
        }
        catch (AnnotatedException ex2) {
            throw new ExtCertPathValidatorException("Policy mappings extension could not be decoded.", ex2, certPath, n);
        }
    }
    
    protected static void prepareNextCertG(final CertPath certPath, final int n, final PKIXNameConstraintValidator pkixNameConstraintValidator) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.NAME_CONSTRAINTS));
            NameConstraints instance2;
            if (instance != null) {
                instance2 = NameConstraints.getInstance(instance);
            }
            else {
                instance2 = null;
            }
            if (instance2 != null) {
                final GeneralSubtree[] permittedSubtrees = instance2.getPermittedSubtrees();
                if (permittedSubtrees != null) {
                    try {
                        pkixNameConstraintValidator.intersectPermittedSubtree(permittedSubtrees);
                    }
                    catch (Exception ex) {
                        throw new ExtCertPathValidatorException("Permitted subtrees cannot be build from name constraints extension.", ex, certPath, n);
                    }
                }
                final GeneralSubtree[] excludedSubtrees = instance2.getExcludedSubtrees();
                if (excludedSubtrees != null) {
                    int i = 0;
                    while (i != excludedSubtrees.length) {
                        try {
                            pkixNameConstraintValidator.addExcludedSubtree(excludedSubtrees[i]);
                            ++i;
                            continue;
                        }
                        catch (Exception ex2) {
                            throw new ExtCertPathValidatorException("Excluded subtrees cannot be build from name constraints extension.", ex2, certPath, n);
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception ex3) {
            throw new ExtCertPathValidatorException("Name constraints extension could not be decoded.", ex3, certPath, n);
        }
    }
    
    protected static int prepareNextCertH1(final CertPath certPath, final int n, final int n2) {
        int n3 = n2;
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && (n3 = n2) != 0) {
            n3 = n2 - 1;
        }
        return n3;
    }
    
    protected static int prepareNextCertH2(final CertPath certPath, final int n, final int n2) {
        int n3 = n2;
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && (n3 = n2) != 0) {
            n3 = n2 - 1;
        }
        return n3;
    }
    
    protected static int prepareNextCertH3(final CertPath certPath, final int n, final int n2) {
        int n3 = n2;
        if (!CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n)) && (n3 = n2) != 0) {
            n3 = n2 - 1;
        }
        return n3;
    }
    
    protected static int prepareNextCertI1(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
            if (instance != null) {
                final Enumeration objects = instance.getObjects();
                while (objects.hasMoreElements()) {
                    try {
                        final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                        if (instance2.getTagNo() != 0) {
                            continue;
                        }
                        final int intValue = ASN1Integer.getInstance(instance2, false).getValue().intValue();
                        if (intValue < n2) {
                            return intValue;
                        }
                    }
                    catch (IllegalArgumentException ex) {
                        throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", ex, certPath, n);
                    }
                    break;
                }
            }
            return n2;
        }
        catch (Exception ex2) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", ex2, certPath, n);
        }
    }
    
    protected static int prepareNextCertI2(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
            if (instance != null) {
                final Enumeration objects = instance.getObjects();
                while (objects.hasMoreElements()) {
                    try {
                        final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(objects.nextElement());
                        if (instance2.getTagNo() != 1) {
                            continue;
                        }
                        final int intValue = ASN1Integer.getInstance(instance2, false).getValue().intValue();
                        if (intValue < n2) {
                            return intValue;
                        }
                    }
                    catch (IllegalArgumentException ex) {
                        throw new ExtCertPathValidatorException("Policy constraints extension contents cannot be decoded.", ex, certPath, n);
                    }
                    break;
                }
            }
            return n2;
        }
        catch (Exception ex2) {
            throw new ExtCertPathValidatorException("Policy constraints extension cannot be decoded.", ex2, certPath, n);
        }
    }
    
    protected static int prepareNextCertJ(final CertPath certPath, int intValue, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(intValue);
        try {
            final ASN1Integer instance = ASN1Integer.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.INHIBIT_ANY_POLICY));
            if (instance != null) {
                intValue = instance.getValue().intValue();
                if (intValue < n) {
                    return intValue;
                }
            }
            return n;
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Inhibit any-policy extension cannot be decoded.", ex, certPath, intValue);
        }
    }
    
    protected static void prepareNextCertK(final CertPath certPath, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final BasicConstraints instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
            if (instance == null) {
                throw new CertPathValidatorException("Intermediate certificate lacks BasicConstraints");
            }
            if (instance.isCA()) {
                return;
            }
            throw new CertPathValidatorException("Not a CA certificate");
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", ex, certPath, n);
        }
    }
    
    protected static int prepareNextCertL(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        if (CertPathValidatorUtilities.isSelfIssued((X509Certificate)certPath.getCertificates().get(n))) {
            return n2;
        }
        if (n2 > 0) {
            return n2 - 1;
        }
        throw new ExtCertPathValidatorException("Max path length not greater than zero", null, certPath, n);
    }
    
    protected static int prepareNextCertM(CertPath pathLenConstraint, int intValue, final int n) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)pathLenConstraint.getCertificates().get(intValue);
        try {
            final BasicConstraints instance = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
            if (instance != null) {
                pathLenConstraint = (CertPath)instance.getPathLenConstraint();
                if (pathLenConstraint != null) {
                    intValue = ((BigInteger)pathLenConstraint).intValue();
                    if (intValue < n) {
                        return intValue;
                    }
                }
            }
            return n;
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Basic constraints extension cannot be decoded.", ex, pathLenConstraint, intValue);
        }
    }
    
    protected static void prepareNextCertN(final CertPath certPath, final int n) throws CertPathValidatorException {
        final boolean[] keyUsage = ((X509Certificate)certPath.getCertificates().get(n)).getKeyUsage();
        if (keyUsage == null) {
            return;
        }
        if (keyUsage[5]) {
            return;
        }
        throw new ExtCertPathValidatorException("Issuer certificate keyusage extension is critical and does not permit key signing.", null, certPath, n);
    }
    
    protected static void prepareNextCertO(final CertPath certPath, final int n, final Set set, final List list) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        final Iterator<PKIXCertPathChecker> iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().check(x509Certificate, set);
                continue;
            }
            catch (CertPathValidatorException ex) {
                throw new CertPathValidatorException(ex.getMessage(), ex.getCause(), certPath, n);
            }
            break;
        }
        if (set.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Certificate has unsupported critical extension: ");
        sb.append(set);
        throw new ExtCertPathValidatorException(sb.toString(), null, certPath, n);
    }
    
    protected static Set processCRLA1i(final Date date, final PKIXExtendedParameters pkixExtendedParameters, final X509Certificate x509Certificate, final X509CRL x509CRL) throws AnnotatedException {
        final HashSet set = new HashSet();
        if (pkixExtendedParameters.isUseDeltasEnabled()) {
            try {
                CRLDistPoint crlDistPoint;
                if ((crlDistPoint = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.FRESHEST_CRL))) == null) {
                    try {
                        crlDistPoint = CRLDistPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.FRESHEST_CRL));
                    }
                    catch (AnnotatedException ex) {
                        throw new AnnotatedException("Freshest CRL extension could not be decoded from CRL.", ex);
                    }
                }
                if (crlDistPoint != null) {
                    final ArrayList<PKIXCRLStore> list = new ArrayList<PKIXCRLStore>();
                    list.addAll((Collection<?>)pkixExtendedParameters.getCRLStores());
                    try {
                        list.addAll((Collection<?>)CertPathValidatorUtilities.getAdditionalStoresFromCRLDistributionPoint(crlDistPoint, pkixExtendedParameters.getNamedCRLStoreMap()));
                        try {
                            set.addAll(CertPathValidatorUtilities.getDeltaCRLs(date, x509CRL, pkixExtendedParameters.getCertStores(), list));
                            return set;
                        }
                        catch (AnnotatedException ex2) {
                            throw new AnnotatedException("Exception obtaining delta CRLs.", ex2);
                        }
                    }
                    catch (AnnotatedException ex3) {
                        throw new AnnotatedException("No new delta CRL locations could be added from Freshest CRL extension.", ex3);
                    }
                }
            }
            catch (AnnotatedException ex4) {
                throw new AnnotatedException("Freshest CRL extension could not be decoded from certificate.", ex4);
            }
        }
        return set;
    }
    
    protected static Set[] processCRLA1ii(Date date, final PKIXExtendedParameters pkixExtendedParameters, final X509Certificate certificateChecking, final X509CRL x509CRL) throws AnnotatedException {
        final HashSet set = new HashSet();
        final X509CRLSelector x509CRLSelector = new X509CRLSelector();
        x509CRLSelector.setCertificateChecking(certificateChecking);
        try {
            x509CRLSelector.addIssuerName(PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded());
            final PKIXCRLStoreSelector<? extends CRL> build = new PKIXCRLStoreSelector.Builder(x509CRLSelector).setCompleteCRLEnabled(true).build();
            if (pkixExtendedParameters.getDate() != null) {
                date = pkixExtendedParameters.getDate();
            }
            final Set crLs = RFC3280CertPathUtilities.CRL_UTIL.findCRLs(build, date, pkixExtendedParameters.getCertStores(), pkixExtendedParameters.getCRLStores());
            if (pkixExtendedParameters.isUseDeltasEnabled()) {
                try {
                    set.addAll(CertPathValidatorUtilities.getDeltaCRLs(date, x509CRL, pkixExtendedParameters.getCertStores(), pkixExtendedParameters.getCRLStores()));
                }
                catch (AnnotatedException ex) {
                    throw new AnnotatedException("Exception obtaining delta CRLs.", ex);
                }
            }
            return new Set[] { crLs, set };
        }
        catch (IOException ex2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot extract issuer from CRL.");
            sb.append(ex2);
            throw new AnnotatedException(sb.toString(), ex2);
        }
    }
    
    protected static void processCRLB1(final DistributionPoint distributionPoint, Object o, final X509CRL x509CRL) throws AnnotatedException {
        final ASN1Primitive extensionValue = CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
        int i = 0;
        boolean b;
        if (extensionValue != null && IssuingDistributionPoint.getInstance(extensionValue).isIndirectCRL()) {
            b = true;
        }
        else {
            b = false;
        }
        try {
            final byte[] encoded = PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded();
            int n;
            if (distributionPoint.getCRLIssuer() != null) {
                final GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
                n = 0;
                while (i < names.length) {
                    int n2 = n;
                    if (names[i].getTagNo() == 4) {
                        try {
                            final boolean equal = Arrays.areEqual(names[i].getName().toASN1Primitive().getEncoded(), encoded);
                            n2 = n;
                            if (equal) {
                                n2 = 1;
                            }
                        }
                        catch (IOException ex) {
                            throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", ex);
                        }
                    }
                    ++i;
                    n = n2;
                }
                if (n != 0 && !b) {
                    throw new AnnotatedException("Distribution point contains cRLIssuer field but CRL is not indirect.");
                }
                if (n == 0) {
                    throw new AnnotatedException("CRL issuer of CRL does not match CRL issuer of distribution point.");
                }
            }
            else if (PrincipalUtils.getIssuerPrincipal(x509CRL).equals(PrincipalUtils.getEncodedIssuerPrincipal(o))) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                return;
            }
            throw new AnnotatedException("Cannot find matching CRL issuer for certificate.");
        }
        catch (IOException ex2) {
            o = new StringBuilder();
            ((StringBuilder)o).append("Exception encoding CRL issuer: ");
            ((StringBuilder)o).append(ex2.getMessage());
            throw new AnnotatedException(((StringBuilder)o).toString(), ex2);
        }
    }
    
    protected static void processCRLB2(final DistributionPoint distributionPoint, final Object o, final X509CRL x509CRL) throws AnnotatedException {
        try {
            final IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
            if (instance != null) {
                if (instance.getDistributionPoint() != null) {
                    final DistributionPointName distributionPoint2 = IssuingDistributionPoint.getInstance(instance).getDistributionPoint();
                    final ArrayList<GeneralName> list = new ArrayList<GeneralName>();
                    final int type = distributionPoint2.getType();
                    final boolean b = false;
                    final boolean b2 = false;
                    if (type == 0) {
                        final GeneralName[] names = GeneralNames.getInstance(distributionPoint2.getName()).getNames();
                        for (int i = 0; i < names.length; ++i) {
                            list.add(names[i]);
                        }
                    }
                    if (distributionPoint2.getType() == 1) {
                        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                        try {
                            final Enumeration objects = ASN1Sequence.getInstance(PrincipalUtils.getIssuerPrincipal(x509CRL)).getObjects();
                            while (objects.hasMoreElements()) {
                                asn1EncodableVector.add(objects.nextElement());
                            }
                            asn1EncodableVector.add(distributionPoint2.getName());
                            list.add(new GeneralName(X500Name.getInstance(new DERSequence(asn1EncodableVector))));
                        }
                        catch (Exception ex) {
                            throw new AnnotatedException("Could not read CRL issuer.", ex);
                        }
                    }
                    if (distributionPoint.getDistributionPoint() != null) {
                        final DistributionPointName distributionPoint3 = distributionPoint.getDistributionPoint();
                        GeneralName[] names2 = null;
                        if (distributionPoint3.getType() == 0) {
                            names2 = GeneralNames.getInstance(distributionPoint3.getName()).getNames();
                        }
                        if (distributionPoint3.getType() == 1) {
                        Label_0301_Outer:
                            while (true) {
                                if (distributionPoint.getCRLIssuer() != null) {
                                    final GeneralName[] names3 = distributionPoint.getCRLIssuer().getNames();
                                    break Label_0268;
                                }
                                Label_0299: {
                                    Label_0271: {
                                        break Label_0271;
                                        break Label_0299;
                                    }
                                    final GeneralName[] names3 = { null };
                                    try {
                                        names3[0] = new GeneralName(X500Name.getInstance(PrincipalUtils.getEncodedIssuerPrincipal(o).getEncoded()));
                                        continue Label_0301_Outer;
                                        int n = 0;
                                    Label_0336_Outer:
                                        while (true) {
                                            break Label_0301;
                                            ASN1EncodableVector asn1EncodableVector2 = null;
                                            while (true) {
                                                final Enumeration objects2;
                                                asn1EncodableVector2.add(objects2.nextElement());
                                                continue;
                                                names2 = names3;
                                                objects2 = ASN1Sequence.getInstance(names3[n].getName().toASN1Primitive()).getObjects();
                                                asn1EncodableVector2 = new ASN1EncodableVector();
                                                continue;
                                            }
                                            Label_0362: {
                                                asn1EncodableVector2.add(distributionPoint3.getName());
                                            }
                                            names3[n] = new GeneralName(X500Name.getInstance(new DERSequence(asn1EncodableVector2)));
                                            ++n;
                                            continue Label_0336_Outer;
                                        }
                                    }
                                    // iftrue(Label_0362:, !objects2.hasMoreElements())
                                    // iftrue(Label_0414:, n >= names3.length)
                                    catch (Exception ex2) {
                                        throw new AnnotatedException("Could not read certificate issuer.", ex2);
                                    }
                                }
                                break;
                            }
                        }
                        boolean b3 = false;
                        Label_0414: {
                            b3 = b2;
                        }
                        if (names2 != null) {
                            int n2 = 0;
                            while (true) {
                                b3 = b2;
                                if (n2 >= names2.length) {
                                    break;
                                }
                                if (list.contains(names2[n2])) {
                                    b3 = true;
                                    break;
                                }
                                ++n2;
                            }
                        }
                        if (!b3) {
                            throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                        }
                    }
                    else {
                        if (distributionPoint.getCRLIssuer() == null) {
                            throw new AnnotatedException("Either the cRLIssuer or the distributionPoint field must be contained in DistributionPoint.");
                        }
                        final GeneralName[] names4 = distributionPoint.getCRLIssuer().getNames();
                        int n3 = 0;
                        boolean b4;
                        while (true) {
                            b4 = b;
                            if (n3 >= names4.length) {
                                break;
                            }
                            if (list.contains(names4[n3])) {
                                b4 = true;
                                break;
                            }
                            ++n3;
                        }
                        if (!b4) {
                            throw new AnnotatedException("No match for certificate CRL issuing distribution point name to cRLIssuer CRL distribution point.");
                        }
                    }
                }
                try {
                    final BasicConstraints instance2 = BasicConstraints.getInstance(CertPathValidatorUtilities.getExtensionValue((X509Extension)o, RFC3280CertPathUtilities.BASIC_CONSTRAINTS));
                    if (o instanceof X509Certificate) {
                        if (instance.onlyContainsUserCerts() && instance2 != null && instance2.isCA()) {
                            throw new AnnotatedException("CA Cert CRL only contains user certificates.");
                        }
                        if (instance.onlyContainsCACerts()) {
                            if (instance2 == null || !instance2.isCA()) {
                                throw new AnnotatedException("End CRL only contains CA certificates.");
                            }
                        }
                    }
                    if (!instance.onlyContainsAttributeCerts()) {
                        return;
                    }
                    throw new AnnotatedException("onlyContainsAttributeCerts boolean is asserted.");
                }
                catch (Exception ex3) {
                    throw new AnnotatedException("Basic constraints extension could not be decoded.", ex3);
                }
            }
        }
        catch (Exception ex4) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex4);
        }
    }
    
    protected static void processCRLC(final X509CRL x509CRL, final X509CRL x509CRL2, final PKIXExtendedParameters pkixExtendedParameters) throws AnnotatedException {
        if (x509CRL == null) {
            return;
        }
        try {
            final IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL2, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
            if (pkixExtendedParameters.isUseDeltasEnabled()) {
                if (PrincipalUtils.getIssuerPrincipal(x509CRL).equals(PrincipalUtils.getIssuerPrincipal(x509CRL2))) {
                    try {
                        final IssuingDistributionPoint instance2 = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
                        boolean b = false;
                        Label_0074: {
                            if (instance == null) {
                                if (instance2 != null) {
                                    break Label_0074;
                                }
                            }
                            else if (!instance.equals(instance2)) {
                                break Label_0074;
                            }
                            b = true;
                        }
                        if (b) {
                            try {
                                final ASN1Primitive extensionValue = CertPathValidatorUtilities.getExtensionValue(x509CRL2, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
                                try {
                                    final ASN1Primitive extensionValue2 = CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.AUTHORITY_KEY_IDENTIFIER);
                                    if (extensionValue == null) {
                                        throw new AnnotatedException("CRL authority key identifier is null.");
                                    }
                                    if (extensionValue2 == null) {
                                        throw new AnnotatedException("Delta CRL authority key identifier is null.");
                                    }
                                    if (extensionValue.equals(extensionValue2)) {
                                        return;
                                    }
                                    throw new AnnotatedException("Delta CRL authority key identifier does not match complete CRL authority key identifier.");
                                }
                                catch (AnnotatedException ex) {
                                    throw new AnnotatedException("Authority key identifier extension could not be extracted from delta CRL.", ex);
                                }
                            }
                            catch (AnnotatedException ex2) {
                                throw new AnnotatedException("Authority key identifier extension could not be extracted from complete CRL.", ex2);
                            }
                        }
                        throw new AnnotatedException("Issuing distribution point extension from delta CRL and complete CRL does not match.");
                    }
                    catch (Exception ex3) {
                        throw new AnnotatedException("Issuing distribution point extension from delta CRL could not be decoded.", ex3);
                    }
                }
                throw new AnnotatedException("Complete CRL issuer does not match delta CRL issuer.");
            }
        }
        catch (Exception ex4) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex4);
        }
    }
    
    protected static ReasonsMask processCRLD(final X509CRL x509CRL, final DistributionPoint distributionPoint) throws AnnotatedException {
        try {
            final IssuingDistributionPoint instance = IssuingDistributionPoint.getInstance(CertPathValidatorUtilities.getExtensionValue(x509CRL, RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT));
            if (instance != null && instance.getOnlySomeReasons() != null && distributionPoint.getReasons() != null) {
                return new ReasonsMask(distributionPoint.getReasons()).intersect(new ReasonsMask(instance.getOnlySomeReasons()));
            }
            if ((instance == null || instance.getOnlySomeReasons() == null) && distributionPoint.getReasons() == null) {
                return ReasonsMask.allReasons;
            }
            ReasonsMask allReasons;
            if (distributionPoint.getReasons() == null) {
                allReasons = ReasonsMask.allReasons;
            }
            else {
                allReasons = new ReasonsMask(distributionPoint.getReasons());
            }
            ReasonsMask allReasons2;
            if (instance == null) {
                allReasons2 = ReasonsMask.allReasons;
            }
            else {
                allReasons2 = new ReasonsMask(instance.getOnlySomeReasons());
            }
            return allReasons.intersect(allReasons2);
        }
        catch (Exception ex) {
            throw new AnnotatedException("Issuing distribution point extension could not be decoded.", ex);
        }
    }
    
    protected static Set processCRLF(final X509CRL x509CRL, final Object o, final X509Certificate x509Certificate, final PublicKey publicKey, final PKIXExtendedParameters pkixExtendedParameters, final List list, final JcaJceHelper jcaJceHelper) throws AnnotatedException {
        final X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded());
            final PKIXCertStoreSelector<? extends Certificate> build = new PKIXCertStoreSelector.Builder(x509CertSelector).build();
            try {
                final Collection certificates = CertPathValidatorUtilities.findCertificates(build, pkixExtendedParameters.getCertificateStores());
                certificates.addAll(CertPathValidatorUtilities.findCertificates(build, pkixExtendedParameters.getCertStores()));
                certificates.add(x509Certificate);
                final Iterator<X509Certificate> iterator = certificates.iterator();
                final ArrayList<X509Certificate> list2 = new ArrayList<X509Certificate>();
                final ArrayList<PublicKey> list3 = new ArrayList<PublicKey>();
                int i;
                while (true) {
                    final boolean hasNext = iterator.hasNext();
                    i = 0;
                    if (!hasNext) {
                        break;
                    }
                    final X509Certificate certificate = iterator.next();
                    if (!certificate.equals(x509Certificate)) {
                        try {
                            final PKIXCertPathBuilderSpi pkixCertPathBuilderSpi = new PKIXCertPathBuilderSpi();
                            final X509CertSelector x509CertSelector2 = new X509CertSelector();
                            x509CertSelector2.setCertificate(certificate);
                            final PKIXExtendedParameters.Builder setTargetConstraints = new PKIXExtendedParameters.Builder(pkixExtendedParameters).setTargetConstraints(new PKIXCertStoreSelector.Builder(x509CertSelector2).build());
                            if (list.contains(certificate)) {
                                setTargetConstraints.setRevocationEnabled(false);
                            }
                            else {
                                setTargetConstraints.setRevocationEnabled(true);
                            }
                            final List<? extends Certificate> certificates2 = pkixCertPathBuilderSpi.engineBuild(new PKIXExtendedBuilderParameters.Builder(setTargetConstraints.build()).build()).getCertPath().getCertificates();
                            list2.add(certificate);
                            list3.add(CertPathValidatorUtilities.getNextWorkingKey(certificates2, 0, jcaJceHelper));
                            continue;
                        }
                        catch (Exception ex) {
                            throw new AnnotatedException(ex.getMessage());
                        }
                        catch (CertPathValidatorException ex2) {
                            throw new AnnotatedException("Public key of issuer certificate of CRL could not be retrieved.", ex2);
                        }
                        catch (CertPathBuilderException ex3) {
                            throw new AnnotatedException("CertPath for CRL signer failed to validate.", ex3);
                        }
                        break;
                    }
                    list2.add(certificate);
                    list3.add(publicKey);
                }
                final HashSet<PublicKey> set = new HashSet<PublicKey>();
                Object o2 = null;
                while (i < list2.size()) {
                    final boolean[] keyUsage = list2.get(i).getKeyUsage();
                    if (keyUsage != null && (keyUsage.length < 7 || !keyUsage[6])) {
                        o2 = new AnnotatedException("Issuer certificate key usage extension does not permit CRL signing.");
                    }
                    else {
                        set.add(list3.get(i));
                    }
                    ++i;
                }
                if (set.isEmpty() && o2 == null) {
                    throw new AnnotatedException("Cannot find a valid issuer certificate.");
                }
                if (!set.isEmpty()) {
                    return set;
                }
                if (o2 == null) {
                    return set;
                }
                throw o2;
            }
            catch (AnnotatedException ex4) {
                throw new AnnotatedException("Issuer certificate for CRL cannot be searched.", ex4);
            }
        }
        catch (IOException ex5) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate for CRL could not be set.", ex5);
        }
    }
    
    protected static PublicKey processCRLG(final X509CRL x509CRL, final Set set) throws AnnotatedException {
        final Iterator<PublicKey> iterator = set.iterator();
        final Exception ex = null;
        while (iterator.hasNext()) {
            final PublicKey publicKey = iterator.next();
            try {
                x509CRL.verify(publicKey);
                return publicKey;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
        throw new AnnotatedException("Cannot verify CRL.", ex);
    }
    
    protected static X509CRL processCRLH(final Set set, final PublicKey publicKey) throws AnnotatedException {
        final Iterator<X509CRL> iterator = set.iterator();
        final Exception ex = null;
        while (iterator.hasNext()) {
            final X509CRL x509CRL = iterator.next();
            try {
                x509CRL.verify(publicKey);
                return x509CRL;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
        if (ex == null) {
            return null;
        }
        throw new AnnotatedException("Cannot verify delta CRL.", ex);
    }
    
    protected static void processCRLI(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus, final PKIXExtendedParameters pkixExtendedParameters) throws AnnotatedException {
        if (pkixExtendedParameters.isUseDeltasEnabled() && x509CRL != null) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, o, certStatus);
        }
    }
    
    protected static void processCRLJ(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus) throws AnnotatedException {
        if (certStatus.getCertStatus() == 11) {
            CertPathValidatorUtilities.getCertStatus(date, x509CRL, o, certStatus);
        }
    }
    
    protected static void processCertA(final CertPath certPath, final PKIXExtendedParameters pkixExtendedParameters, final int n, final PublicKey publicKey, final boolean b, final X500Name x500Name, final X509Certificate x509Certificate, final JcaJceHelper jcaJceHelper) throws ExtCertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate2 = (X509Certificate)certificates.get(n);
        if (!b) {
            try {
                CertPathValidatorUtilities.verifyX509Certificate(x509Certificate2, publicKey, pkixExtendedParameters.getSigProvider());
            }
            catch (GeneralSecurityException ex) {
                throw new ExtCertPathValidatorException("Could not validate certificate signature.", ex, certPath, n);
            }
        }
        try {
            x509Certificate2.checkValidity(CertPathValidatorUtilities.getValidCertDateFromValidityModel(pkixExtendedParameters, certPath, n));
            if (pkixExtendedParameters.isRevocationEnabled()) {
                try {
                    checkCRLs(pkixExtendedParameters, x509Certificate2, CertPathValidatorUtilities.getValidCertDateFromValidityModel(pkixExtendedParameters, certPath, n), x509Certificate, publicKey, certificates, jcaJceHelper);
                }
                catch (AnnotatedException ex2) {
                    Throwable cause;
                    if (ex2.getCause() != null) {
                        cause = ex2.getCause();
                    }
                    else {
                        cause = ex2;
                    }
                    throw new ExtCertPathValidatorException(ex2.getMessage(), cause, certPath, n);
                }
            }
            if (PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate2).equals(x500Name)) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("IssuerName(");
            sb.append(PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate2));
            sb.append(") does not match SubjectName(");
            sb.append(x500Name);
            sb.append(") of signing certificate.");
            throw new ExtCertPathValidatorException(sb.toString(), null, certPath, n);
        }
        catch (AnnotatedException ex3) {
            throw new ExtCertPathValidatorException("Could not validate time of certificate.", ex3, certPath, n);
        }
        catch (CertificateNotYetValidException ex4) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not validate certificate: ");
            sb2.append(ex4.getMessage());
            throw new ExtCertPathValidatorException(sb2.toString(), ex4, certPath, n);
        }
        catch (CertificateExpiredException ex5) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Could not validate certificate: ");
            sb3.append(ex5.getMessage());
            throw new ExtCertPathValidatorException(sb3.toString(), ex5, certPath, n);
        }
    }
    
    protected static void processCertBC(final CertPath certPath, final int n, final PKIXNameConstraintValidator pkixNameConstraintValidator) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(n);
        final int size = certificates.size();
        if (CertPathValidatorUtilities.isSelfIssued(x509Certificate) && size - n < size) {
            return;
        }
        final X500Name subjectPrincipal = PrincipalUtils.getSubjectPrincipal(x509Certificate);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(subjectPrincipal.getEncoded());
            try {
                pkixNameConstraintValidator.checkPermittedDN(instance);
                pkixNameConstraintValidator.checkExcludedDN(instance);
                try {
                    final GeneralNames instance2 = GeneralNames.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME));
                    final RDN[] rdNs = X500Name.getInstance(instance).getRDNs(BCStyle.EmailAddress);
                    final int n2 = 0;
                    int i = 0;
                    while (i != rdNs.length) {
                        final GeneralName generalName = new GeneralName(1, ((ASN1String)rdNs[i].getFirst().getValue()).getString());
                        try {
                            pkixNameConstraintValidator.checkPermitted(generalName);
                            pkixNameConstraintValidator.checkExcluded(generalName);
                            ++i;
                            continue;
                        }
                        catch (PKIXNameConstraintValidatorException ex) {
                            throw new CertPathValidatorException("Subtree check for certificate subject alternative email failed.", ex, certPath, n);
                        }
                        break;
                    }
                    if (instance2 != null) {
                        try {
                            final GeneralName[] names = instance2.getNames();
                            int j = n2;
                            while (j < names.length) {
                                try {
                                    pkixNameConstraintValidator.checkPermitted(names[j]);
                                    pkixNameConstraintValidator.checkExcluded(names[j]);
                                    ++j;
                                }
                                catch (PKIXNameConstraintValidatorException ex2) {
                                    throw new CertPathValidatorException("Subtree check for certificate subject alternative name failed.", ex2, certPath, n);
                                }
                            }
                        }
                        catch (Exception ex3) {
                            throw new CertPathValidatorException("Subject alternative name contents could not be decoded.", ex3, certPath, n);
                        }
                    }
                }
                catch (Exception ex4) {
                    throw new CertPathValidatorException("Subject alternative name extension could not be decoded.", ex4, certPath, n);
                }
            }
            catch (PKIXNameConstraintValidatorException ex5) {
                throw new CertPathValidatorException("Subtree check for certificate subject failed.", ex5, certPath, n);
            }
        }
        catch (Exception ex6) {
            throw new CertPathValidatorException("Exception extracting subject name when checking subtrees.", ex6, certPath, n);
        }
    }
    
    protected static PKIXPolicyNode processCertD(CertPath certPath, int i, Set criticalExtensionOIDs, final PKIXPolicyNode pkixPolicyNode, final List[] array, int n) throws CertPathValidatorException {
        final List<? extends Certificate> certificates = certPath.getCertificates();
        final X509Certificate x509Certificate = (X509Certificate)certificates.get(i);
        final int size = certificates.size();
        final int n2 = size - i;
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES));
            if (instance != null && pkixPolicyNode != null) {
                final Enumeration objects = instance.getObjects();
                final HashSet<String> set = new HashSet<String>();
                while (objects.hasMoreElements()) {
                    final PolicyInformation instance2 = PolicyInformation.getInstance(objects.nextElement());
                    final ASN1ObjectIdentifier policyIdentifier = instance2.getPolicyIdentifier();
                    set.add(policyIdentifier.getId());
                    if (!"2.5.29.32.0".equals(policyIdentifier.getId())) {
                        try {
                            final Set qualifierSet = CertPathValidatorUtilities.getQualifierSet(instance2.getPolicyQualifiers());
                            if (!CertPathValidatorUtilities.processCertD1i(n2, array, policyIdentifier, qualifierSet)) {
                                CertPathValidatorUtilities.processCertD1ii(n2, array, policyIdentifier, qualifierSet);
                                continue;
                            }
                            continue;
                        }
                        catch (CertPathValidatorException ex) {
                            throw new ExtCertPathValidatorException("Policy qualifier info set could not be build.", ex, certPath, i);
                        }
                        break;
                    }
                }
                if (!criticalExtensionOIDs.isEmpty() && !criticalExtensionOIDs.contains("2.5.29.32.0")) {
                    certPath = (CertPath)criticalExtensionOIDs.iterator();
                    final HashSet<String> set2 = new HashSet<String>();
                    while (((Iterator)certPath).hasNext()) {
                        final String next = ((Iterator<String>)certPath).next();
                        if (set.contains(next)) {
                            set2.add(next);
                        }
                    }
                    criticalExtensionOIDs.clear();
                    criticalExtensionOIDs.addAll(set2);
                }
                else {
                    criticalExtensionOIDs.clear();
                    criticalExtensionOIDs.addAll(set);
                }
                if (n > 0 || (n2 < size && CertPathValidatorUtilities.isSelfIssued(x509Certificate))) {
                    certPath = (CertPath)instance.getObjects();
                    while (((Enumeration)certPath).hasMoreElements()) {
                        final PolicyInformation instance3 = PolicyInformation.getInstance(((Enumeration<Object>)certPath).nextElement());
                        if ("2.5.29.32.0".equals(instance3.getPolicyIdentifier().getId())) {
                            certPath = (CertPath)CertPathValidatorUtilities.getQualifierSet(instance3.getPolicyQualifiers());
                            List list;
                            PKIXPolicyNode pkixPolicyNode2;
                            Iterator iterator;
                            String next2;
                            String id;
                            Iterator children;
                            HashSet<String> set3;
                            PKIXPolicyNode pkixPolicyNode3;
                            for (list = array[n2 - 1], i = 0; i < list.size(); ++i) {
                                pkixPolicyNode2 = list.get(i);
                                iterator = pkixPolicyNode2.getExpectedPolicies().iterator();
                                while (iterator.hasNext()) {
                                    next2 = iterator.next();
                                    if (next2 instanceof String) {
                                        id = next2;
                                    }
                                    else {
                                        if (!(next2 instanceof ASN1ObjectIdentifier)) {
                                            continue;
                                        }
                                        id = ((ASN1ObjectIdentifier)next2).getId();
                                    }
                                    children = pkixPolicyNode2.getChildren();
                                    n = 0;
                                    while (children.hasNext()) {
                                        if (id.equals(children.next().getValidPolicy())) {
                                            n = 1;
                                        }
                                    }
                                    if (n == 0) {
                                        set3 = new HashSet<String>();
                                        set3.add(id);
                                        pkixPolicyNode3 = new PKIXPolicyNode(new ArrayList(), n2, set3, pkixPolicyNode2, (Set)certPath, id, false);
                                        pkixPolicyNode2.addChild(pkixPolicyNode3);
                                        array[n2].add(pkixPolicyNode3);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                i = n2 - 1;
                certPath = (CertPath)pkixPolicyNode;
                while (i >= 0) {
                    final List list2 = array[i];
                    n = 0;
                    CertPath certPath2;
                    while (true) {
                        certPath2 = certPath;
                        if (n >= list2.size()) {
                            break;
                        }
                        final PKIXPolicyNode pkixPolicyNode4 = list2.get(n);
                        CertPath certPath3 = certPath;
                        if (!pkixPolicyNode4.hasChildren()) {
                            certPath = (CertPath)CertPathValidatorUtilities.removePolicyNode((PKIXPolicyNode)certPath, array, pkixPolicyNode4);
                            if ((certPath3 = certPath) == null) {
                                certPath2 = certPath;
                                break;
                            }
                        }
                        ++n;
                        certPath = certPath3;
                    }
                    certPath = certPath2;
                    --i;
                }
                criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                if (criticalExtensionOIDs != null) {
                    final boolean contains = criticalExtensionOIDs.contains(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                    List list3;
                    for (list3 = array[n2], i = 0; i < list3.size(); ++i) {
                        list3.get(i).setCritical(contains);
                    }
                }
                return (PKIXPolicyNode)certPath;
            }
            return null;
        }
        catch (AnnotatedException ex2) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", ex2, certPath, i);
        }
    }
    
    protected static PKIXPolicyNode processCertE(final CertPath certPath, final int n, PKIXPolicyNode pkixPolicyNode) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            if (ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.CERTIFICATE_POLICIES)) == null) {
                pkixPolicyNode = null;
            }
            return pkixPolicyNode;
        }
        catch (AnnotatedException ex) {
            throw new ExtCertPathValidatorException("Could not read certificate policies extension from certificate.", ex, certPath, n);
        }
    }
    
    protected static void processCertF(final CertPath certPath, final int n, final PKIXPolicyNode pkixPolicyNode, final int n2) throws CertPathValidatorException {
        if (n2 > 0) {
            return;
        }
        if (pkixPolicyNode != null) {
            return;
        }
        throw new ExtCertPathValidatorException("No valid policy tree found when one expected.", null, certPath, n);
    }
    
    protected static int wrapupCertA(final int n, final X509Certificate x509Certificate) {
        int n2 = n;
        if (!CertPathValidatorUtilities.isSelfIssued(x509Certificate) && (n2 = n) != 0) {
            n2 = n - 1;
        }
        return n2;
    }
    
    protected static int wrapupCertB(final CertPath certPath, final int n, final int n2) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        try {
            final ASN1Sequence instance = ASN1Sequence.getInstance(CertPathValidatorUtilities.getExtensionValue(x509Certificate, RFC3280CertPathUtilities.POLICY_CONSTRAINTS));
            if (instance != null) {
                final Enumeration objects = instance.getObjects();
                while (objects.hasMoreElements()) {
                    final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
                    if (asn1TaggedObject.getTagNo() != 0) {
                        continue;
                    }
                    try {
                        if (ASN1Integer.getInstance(asn1TaggedObject, false).getValue().intValue() == 0) {
                            return 0;
                        }
                        continue;
                    }
                    catch (Exception ex) {
                        throw new ExtCertPathValidatorException("Policy constraints requireExplicitPolicy field could not be decoded.", ex, certPath, n);
                    }
                    break;
                }
            }
            return n2;
        }
        catch (AnnotatedException ex2) {
            throw new ExtCertPathValidatorException("Policy constraints could not be decoded.", ex2, certPath, n);
        }
    }
    
    protected static void wrapupCertF(final CertPath certPath, final int n, final List list, final Set set) throws CertPathValidatorException {
        final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(n);
        final Iterator<PKIXCertPathChecker> iterator = list.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().check(x509Certificate, set);
                continue;
            }
            catch (CertPathValidatorException ex) {
                throw new ExtCertPathValidatorException("Additional certificate path checker failed.", ex, certPath, n);
            }
            break;
        }
        if (set.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Certificate has unsupported critical extension: ");
        sb.append(set);
        throw new ExtCertPathValidatorException(sb.toString(), null, certPath, n);
    }
    
    protected static PKIXPolicyNode wrapupCertG(final CertPath certPath, final PKIXExtendedParameters pkixExtendedParameters, final Set set, int i, final List[] array, PKIXPolicyNode removePolicyNode, final Set set2) throws CertPathValidatorException {
        int size = certPath.getCertificates().size();
        if (removePolicyNode != null) {
            PKIXPolicyNode pkixPolicyNode;
            if (CertPathValidatorUtilities.isAnyPolicy(set)) {
                pkixPolicyNode = removePolicyNode;
                if (pkixExtendedParameters.isExplicitPolicyRequired()) {
                    if (set2.isEmpty()) {
                        throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, i);
                    }
                    final HashSet<PKIXPolicyNode> set3 = new HashSet<PKIXPolicyNode>();
                    List list;
                    int j;
                    PKIXPolicyNode pkixPolicyNode2;
                    Iterator children;
                    for (i = 0; i < array.length; ++i) {
                        for (list = array[i], j = 0; j < list.size(); ++j) {
                            pkixPolicyNode2 = list.get(j);
                            if ("2.5.29.32.0".equals(pkixPolicyNode2.getValidPolicy())) {
                                children = pkixPolicyNode2.getChildren();
                                while (children.hasNext()) {
                                    set3.add(children.next());
                                }
                            }
                        }
                    }
                    final Iterator<Object> iterator = set3.iterator();
                    while (iterator.hasNext()) {
                        set2.contains(iterator.next().getValidPolicy());
                    }
                    if ((pkixPolicyNode = removePolicyNode) != null) {
                        while (true) {
                            --size;
                            pkixPolicyNode = removePolicyNode;
                            if (size < 0) {
                                break;
                            }
                            List list2;
                            PKIXPolicyNode pkixPolicyNode3;
                            PKIXPolicyNode removePolicyNode2;
                            for (list2 = array[size], i = 0; i < list2.size(); ++i, removePolicyNode = removePolicyNode2) {
                                pkixPolicyNode3 = list2.get(i);
                                removePolicyNode2 = removePolicyNode;
                                if (!pkixPolicyNode3.hasChildren()) {
                                    removePolicyNode2 = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode3);
                                }
                            }
                        }
                    }
                }
            }
            else {
                final HashSet<PKIXPolicyNode> set4 = new HashSet<PKIXPolicyNode>();
                List list3;
                int k;
                PKIXPolicyNode pkixPolicyNode4;
                Iterator children2;
                PKIXPolicyNode pkixPolicyNode5;
                for (i = 0; i < array.length; ++i) {
                    for (list3 = array[i], k = 0; k < list3.size(); ++k) {
                        pkixPolicyNode4 = list3.get(k);
                        if ("2.5.29.32.0".equals(pkixPolicyNode4.getValidPolicy())) {
                            children2 = pkixPolicyNode4.getChildren();
                            while (children2.hasNext()) {
                                pkixPolicyNode5 = children2.next();
                                if (!"2.5.29.32.0".equals(pkixPolicyNode5.getValidPolicy())) {
                                    set4.add(pkixPolicyNode5);
                                }
                            }
                        }
                    }
                }
                for (final PKIXPolicyNode pkixPolicyNode6 : set4) {
                    if (!set.contains(pkixPolicyNode6.getValidPolicy())) {
                        removePolicyNode = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode6);
                    }
                }
                if ((pkixPolicyNode = removePolicyNode) != null) {
                    while (true) {
                        --size;
                        pkixPolicyNode = removePolicyNode;
                        if (size < 0) {
                            break;
                        }
                        List list4;
                        PKIXPolicyNode pkixPolicyNode7;
                        PKIXPolicyNode removePolicyNode3;
                        for (list4 = array[size], i = 0; i < list4.size(); ++i, removePolicyNode = removePolicyNode3) {
                            pkixPolicyNode7 = list4.get(i);
                            removePolicyNode3 = removePolicyNode;
                            if (!pkixPolicyNode7.hasChildren()) {
                                removePolicyNode3 = CertPathValidatorUtilities.removePolicyNode(removePolicyNode, array, pkixPolicyNode7);
                            }
                        }
                    }
                }
            }
            return pkixPolicyNode;
        }
        if (!pkixExtendedParameters.isExplicitPolicyRequired()) {
            return null;
        }
        throw new ExtCertPathValidatorException("Explicit policy requested but none available.", null, certPath, i);
    }
}
