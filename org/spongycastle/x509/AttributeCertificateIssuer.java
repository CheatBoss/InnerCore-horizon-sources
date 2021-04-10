package org.spongycastle.x509;

import org.spongycastle.util.*;
import javax.security.auth.x500.*;
import org.spongycastle.jce.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;

public class AttributeCertificateIssuer implements CertSelector, Selector
{
    final ASN1Encodable form;
    
    public AttributeCertificateIssuer(final X500Principal x500Principal) throws IOException {
        this(new X509Principal(x500Principal.getEncoded()));
    }
    
    public AttributeCertificateIssuer(final AttCertIssuer attCertIssuer) {
        this.form = attCertIssuer.getIssuer();
    }
    
    public AttributeCertificateIssuer(final X509Principal x509Principal) {
        this.form = new V2Form(GeneralNames.getInstance(new DERSequence(new GeneralName(x509Principal))));
    }
    
    private Object[] getNames() {
        final ASN1Encodable form = this.form;
        GeneralNames issuerName;
        if (form instanceof V2Form) {
            issuerName = ((V2Form)form).getIssuerName();
        }
        else {
            issuerName = (GeneralNames)form;
        }
        final GeneralName[] names = issuerName.getNames();
        final ArrayList list = new ArrayList<X500Principal>(names.length);
        for (int i = 0; i != names.length; ++i) {
            if (names[i].getTagNo() == 4) {
                try {
                    list.add(new X500Principal(names[i].getName().toASN1Primitive().getEncoded()));
                }
                catch (IOException ex) {
                    throw new RuntimeException("badly formed Name object");
                }
            }
        }
        return list.toArray(new Object[list.size()]);
    }
    
    private boolean matchesDN(final X500Principal p0, final GeneralNames p1) {
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
        //    27: new             Ljavax/security/auth/x500/X500Principal;
        //    30: dup            
        //    31: aload           5
        //    33: invokevirtual   org/spongycastle/asn1/x509/GeneralName.getName:()Lorg/spongycastle/asn1/ASN1Encodable;
        //    36: invokeinterface org/spongycastle/asn1/ASN1Encodable.toASN1Primitive:()Lorg/spongycastle/asn1/ASN1Primitive;
        //    41: invokevirtual   org/spongycastle/asn1/ASN1Primitive.getEncoded:()[B
        //    44: invokespecial   javax/security/auth/x500/X500Principal.<init>:([B)V
        //    47: aload_1        
        //    48: invokevirtual   javax/security/auth/x500/X500Principal.equals:(Ljava/lang/Object;)Z
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
        return new AttributeCertificateIssuer(AttCertIssuer.getInstance(this.form));
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AttributeCertificateIssuer && this.form.equals(((AttributeCertificateIssuer)o).form));
    }
    
    public Principal[] getPrincipals() {
        final Object[] names = this.getNames();
        final ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i != names.length; ++i) {
            if (names[i] instanceof Principal) {
                list.add(names[i]);
            }
        }
        return list.toArray(new Principal[list.size()]);
    }
    
    @Override
    public int hashCode() {
        return this.form.hashCode();
    }
    
    @Override
    public boolean match(final Object o) {
        return o instanceof X509Certificate && this.match((Certificate)o);
    }
    
    @Override
    public boolean match(final Certificate certificate) {
        final boolean b = certificate instanceof X509Certificate;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final X509Certificate x509Certificate = (X509Certificate)certificate;
        final ASN1Encodable form = this.form;
        if (form instanceof V2Form) {
            final V2Form v2Form = (V2Form)form;
            if (v2Form.getBaseCertificateID() != null) {
                boolean b3 = b2;
                if (v2Form.getBaseCertificateID().getSerial().getValue().equals(x509Certificate.getSerialNumber())) {
                    b3 = b2;
                    if (this.matchesDN(x509Certificate.getIssuerX500Principal(), v2Form.getBaseCertificateID().getIssuer())) {
                        b3 = true;
                    }
                }
                return b3;
            }
            if (this.matchesDN(x509Certificate.getSubjectX500Principal(), v2Form.getIssuerName())) {
                return true;
            }
        }
        else if (this.matchesDN(x509Certificate.getSubjectX500Principal(), (GeneralNames)form)) {
            return true;
        }
        return false;
    }
}
