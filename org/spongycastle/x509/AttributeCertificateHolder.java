package org.spongycastle.x509;

import org.spongycastle.util.*;
import javax.security.auth.x500.*;
import java.math.*;
import org.spongycastle.jce.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

public class AttributeCertificateHolder implements CertSelector, Selector
{
    final Holder holder;
    
    public AttributeCertificateHolder(final int n, final String s, final String s2, final byte[] array) {
        this.holder = new Holder(new ObjectDigestInfo(n, new ASN1ObjectIdentifier(s2), new AlgorithmIdentifier(new ASN1ObjectIdentifier(s)), Arrays.clone(array)));
    }
    
    public AttributeCertificateHolder(final X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            this.holder = new Holder(new IssuerSerial(this.generateGeneralNames(PrincipalUtil.getIssuerX509Principal(x509Certificate)), new ASN1Integer(x509Certificate.getSerialNumber())));
        }
        catch (Exception ex) {
            throw new CertificateParsingException(ex.getMessage());
        }
    }
    
    public AttributeCertificateHolder(final X500Principal x500Principal) {
        this(X509Util.convertPrincipal(x500Principal));
    }
    
    public AttributeCertificateHolder(final X500Principal x500Principal, final BigInteger bigInteger) {
        this(X509Util.convertPrincipal(x500Principal), bigInteger);
    }
    
    AttributeCertificateHolder(final ASN1Sequence asn1Sequence) {
        this.holder = Holder.getInstance(asn1Sequence);
    }
    
    public AttributeCertificateHolder(final X509Principal x509Principal) {
        this.holder = new Holder(this.generateGeneralNames(x509Principal));
    }
    
    public AttributeCertificateHolder(final X509Principal x509Principal, final BigInteger bigInteger) {
        this.holder = new Holder(new IssuerSerial(GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal))), new ASN1Integer(bigInteger)));
    }
    
    private GeneralNames generateGeneralNames(final X509Principal x509Principal) {
        return GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal)));
    }
    
    private Object[] getNames(final GeneralName[] array) {
        final ArrayList<X500Principal> list = new ArrayList<X500Principal>(array.length);
        for (int i = 0; i != array.length; ++i) {
            if (array[i].getTagNo() == 4) {
                try {
                    list.add(new X500Principal(array[i].getName().toASN1Primitive().getEncoded()));
                }
                catch (IOException ex) {
                    throw new RuntimeException("badly formed Name object");
                }
            }
        }
        return list.toArray(new Object[list.size()]);
    }
    
    private Principal[] getPrincipals(final GeneralNames generalNames) {
        final Object[] names = this.getNames(generalNames.getNames());
        final ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i != names.length; ++i) {
            if (names[i] instanceof Principal) {
                list.add(names[i]);
            }
        }
        return list.toArray(new Principal[list.size()]);
    }
    
    private boolean matchesDN(final X509Principal p0, final GeneralNames p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/spongycastle/asn1/x509/GeneralNames.getNames:()[Lorg/spongycastle/asn1/x509/GeneralName;
        //     4: astore_2       
        //     5: iconst_0       
        //     6: istore_3       
        //     7: iload_3        
        //     8: aload_2        
        //     9: arraylength    
        //    10: if_icmpeq       69
        //    13: aload_2        
        //    14: iload_3        
        //    15: aaload         
        //    16: astore          5
        //    18: aload           5
        //    20: invokevirtual   org/spongycastle/asn1/x509/GeneralName.getTagNo:()I
        //    23: iconst_4       
        //    24: if_icmpne       62
        //    27: new             Lorg/spongycastle/jce/X509Principal;
        //    30: dup            
        //    31: aload           5
        //    33: invokevirtual   org/spongycastle/asn1/x509/GeneralName.getName:()Lorg/spongycastle/asn1/ASN1Encodable;
        //    36: invokeinterface org/spongycastle/asn1/ASN1Encodable.toASN1Primitive:()Lorg/spongycastle/asn1/ASN1Primitive;
        //    41: invokevirtual   org/spongycastle/asn1/ASN1Primitive.getEncoded:()[B
        //    44: invokespecial   org/spongycastle/jce/X509Principal.<init>:([B)V
        //    47: aload_1        
        //    48: invokevirtual   org/spongycastle/jce/X509Principal.equals:(Ljava/lang/Object;)Z
        //    51: istore          4
        //    53: iload           4
        //    55: ifeq            62
        //    58: iconst_1       
        //    59: ireturn        
        //    60: astore          5
        //    62: iload_3        
        //    63: iconst_1       
        //    64: iadd           
        //    65: istore_3       
        //    66: goto            7
        //    69: iconst_0       
        //    70: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  27     53     60     62     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    @Override
    public Object clone() {
        return new AttributeCertificateHolder((ASN1Sequence)this.holder.toASN1Primitive());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AttributeCertificateHolder && this.holder.equals(((AttributeCertificateHolder)o).holder));
    }
    
    public String getDigestAlgorithm() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getDigestAlgorithm().getAlgorithm().getId();
        }
        return null;
    }
    
    public int getDigestedObjectType() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getDigestedObjectType().getValue().intValue();
        }
        return -1;
    }
    
    public Principal[] getEntityNames() {
        if (this.holder.getEntityName() != null) {
            return this.getPrincipals(this.holder.getEntityName());
        }
        return null;
    }
    
    public Principal[] getIssuer() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.getPrincipals(this.holder.getBaseCertificateID().getIssuer());
        }
        return null;
    }
    
    public byte[] getObjectDigest() {
        if (this.holder.getObjectDigestInfo() != null) {
            return this.holder.getObjectDigestInfo().getObjectDigest().getBytes();
        }
        return null;
    }
    
    public String getOtherObjectTypeID() {
        if (this.holder.getObjectDigestInfo() != null) {
            this.holder.getObjectDigestInfo().getOtherObjectTypeID().getId();
        }
        return null;
    }
    
    public BigInteger getSerialNumber() {
        if (this.holder.getBaseCertificateID() != null) {
            return this.holder.getBaseCertificateID().getSerial().getValue();
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.holder.hashCode();
    }
    
    @Override
    public boolean match(final Object o) {
        return o instanceof X509Certificate && this.match((Certificate)o);
    }
    
    @Override
    public boolean match(final Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        final X509Certificate x509Certificate = (X509Certificate)certificate;
        try {
            if (this.holder.getBaseCertificateID() != null) {
                if (this.holder.getBaseCertificateID().getSerial().getValue().equals(x509Certificate.getSerialNumber()) && this.matchesDN(PrincipalUtil.getIssuerX509Principal(x509Certificate), this.holder.getBaseCertificateID().getIssuer())) {
                    return true;
                }
            }
            else {
                if (this.holder.getEntityName() != null && this.matchesDN(PrincipalUtil.getSubjectX509Principal(x509Certificate), this.holder.getEntityName())) {
                    return true;
                }
                if (this.holder.getObjectDigestInfo() == null) {
                    return false;
                }
                try {
                    final MessageDigest instance = MessageDigest.getInstance(this.getDigestAlgorithm(), "SC");
                    final int digestedObjectType = this.getDigestedObjectType();
                    Label_0166: {
                        byte[] array;
                        if (digestedObjectType != 0) {
                            if (digestedObjectType != 1) {
                                break Label_0166;
                            }
                            array = certificate.getEncoded();
                        }
                        else {
                            array = certificate.getPublicKey().getEncoded();
                        }
                        instance.update(array);
                    }
                    Arrays.areEqual(instance.digest(), this.getObjectDigest());
                    return false;
                }
                catch (Exception ex) {
                    return false;
                }
            }
        }
        catch (CertificateEncodingException ex2) {}
        return false;
    }
}
