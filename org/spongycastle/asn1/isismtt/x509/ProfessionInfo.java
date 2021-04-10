package org.spongycastle.asn1.isismtt.x509;

import java.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class ProfessionInfo extends ASN1Object
{
    public static final ASN1ObjectIdentifier Notar;
    public static final ASN1ObjectIdentifier Notariatsverwalter;
    public static final ASN1ObjectIdentifier Notariatsverwalterin;
    public static final ASN1ObjectIdentifier Notarin;
    public static final ASN1ObjectIdentifier Notarvertreter;
    public static final ASN1ObjectIdentifier Notarvertreterin;
    public static final ASN1ObjectIdentifier Patentanwalt;
    public static final ASN1ObjectIdentifier Patentanwltin;
    public static final ASN1ObjectIdentifier Rechtsanwalt;
    public static final ASN1ObjectIdentifier Rechtsanwltin;
    public static final ASN1ObjectIdentifier Rechtsbeistand;
    public static final ASN1ObjectIdentifier Steuerberater;
    public static final ASN1ObjectIdentifier Steuerberaterin;
    public static final ASN1ObjectIdentifier Steuerbevollmchtigte;
    public static final ASN1ObjectIdentifier Steuerbevollmchtigter;
    public static final ASN1ObjectIdentifier VereidigteBuchprferin;
    public static final ASN1ObjectIdentifier VereidigterBuchprfer;
    public static final ASN1ObjectIdentifier Wirtschaftsprfer;
    public static final ASN1ObjectIdentifier Wirtschaftsprferin;
    private ASN1OctetString addProfessionInfo;
    private NamingAuthority namingAuthority;
    private ASN1Sequence professionItems;
    private ASN1Sequence professionOIDs;
    private String registrationNumber;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb.append(".1");
        Rechtsanwltin = new ASN1ObjectIdentifier(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb2.append(".2");
        Rechtsanwalt = new ASN1ObjectIdentifier(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb3.append(".3");
        Rechtsbeistand = new ASN1ObjectIdentifier(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb4.append(".4");
        Steuerberaterin = new ASN1ObjectIdentifier(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb5.append(".5");
        Steuerberater = new ASN1ObjectIdentifier(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb6.append(".6");
        Steuerbevollmchtigte = new ASN1ObjectIdentifier(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb7.append(".7");
        Steuerbevollmchtigter = new ASN1ObjectIdentifier(sb7.toString());
        final StringBuilder sb8 = new StringBuilder();
        sb8.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb8.append(".8");
        Notarin = new ASN1ObjectIdentifier(sb8.toString());
        final StringBuilder sb9 = new StringBuilder();
        sb9.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb9.append(".9");
        Notar = new ASN1ObjectIdentifier(sb9.toString());
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb10.append(".10");
        Notarvertreterin = new ASN1ObjectIdentifier(sb10.toString());
        final StringBuilder sb11 = new StringBuilder();
        sb11.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb11.append(".11");
        Notarvertreter = new ASN1ObjectIdentifier(sb11.toString());
        final StringBuilder sb12 = new StringBuilder();
        sb12.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb12.append(".12");
        Notariatsverwalterin = new ASN1ObjectIdentifier(sb12.toString());
        final StringBuilder sb13 = new StringBuilder();
        sb13.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb13.append(".13");
        Notariatsverwalter = new ASN1ObjectIdentifier(sb13.toString());
        final StringBuilder sb14 = new StringBuilder();
        sb14.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb14.append(".14");
        Wirtschaftsprferin = new ASN1ObjectIdentifier(sb14.toString());
        final StringBuilder sb15 = new StringBuilder();
        sb15.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb15.append(".15");
        Wirtschaftsprfer = new ASN1ObjectIdentifier(sb15.toString());
        final StringBuilder sb16 = new StringBuilder();
        sb16.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb16.append(".16");
        VereidigteBuchprferin = new ASN1ObjectIdentifier(sb16.toString());
        final StringBuilder sb17 = new StringBuilder();
        sb17.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb17.append(".17");
        VereidigterBuchprfer = new ASN1ObjectIdentifier(sb17.toString());
        final StringBuilder sb18 = new StringBuilder();
        sb18.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb18.append(".18");
        Patentanwltin = new ASN1ObjectIdentifier(sb18.toString());
        final StringBuilder sb19 = new StringBuilder();
        sb19.append(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern);
        sb19.append(".19");
        Patentanwalt = new ASN1ObjectIdentifier(sb19.toString());
    }
    
    private ProfessionInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        ASN1Encodable asn1Encodable2;
        final ASN1Encodable asn1Encodable = asn1Encodable2 = objects.nextElement();
        if (asn1Encodable instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Encodable;
            if (asn1TaggedObject.getTagNo() != 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Bad tag number: ");
                sb2.append(asn1TaggedObject.getTagNo());
                throw new IllegalArgumentException(sb2.toString());
            }
            this.namingAuthority = NamingAuthority.getInstance(asn1TaggedObject, true);
            asn1Encodable2 = objects.nextElement();
        }
        this.professionItems = ASN1Sequence.getInstance(asn1Encodable2);
        if (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable3 = objects.nextElement();
            if (asn1Encodable3 instanceof ASN1Sequence) {
                this.professionOIDs = ASN1Sequence.getInstance(asn1Encodable3);
            }
            else if (asn1Encodable3 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(asn1Encodable3).getString();
            }
            else {
                if (!(asn1Encodable3 instanceof ASN1OctetString)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Bad object encountered: ");
                    sb3.append(asn1Encodable3.getClass());
                    throw new IllegalArgumentException(sb3.toString());
                }
                this.addProfessionInfo = ASN1OctetString.getInstance(asn1Encodable3);
            }
        }
        if (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable4 = objects.nextElement();
            if (asn1Encodable4 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(asn1Encodable4).getString();
            }
            else {
                if (!(asn1Encodable4 instanceof DEROctetString)) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Bad object encountered: ");
                    sb4.append(((DEROctetString)asn1Encodable4).getClass());
                    throw new IllegalArgumentException(sb4.toString());
                }
                this.addProfessionInfo = (DEROctetString)asn1Encodable4;
            }
        }
        if (!objects.hasMoreElements()) {
            return;
        }
        final ASN1Encodable asn1Encodable5 = objects.nextElement();
        if (asn1Encodable5 instanceof DEROctetString) {
            this.addProfessionInfo = (DEROctetString)asn1Encodable5;
            return;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Bad object encountered: ");
        sb5.append(((DEROctetString)asn1Encodable5).getClass());
        throw new IllegalArgumentException(sb5.toString());
    }
    
    public ProfessionInfo(final NamingAuthority namingAuthority, final DirectoryString[] array, final ASN1ObjectIdentifier[] array2, final String registrationNumber, final ASN1OctetString addProfessionInfo) {
        this.namingAuthority = namingAuthority;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final int n = 0;
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.professionItems = new DERSequence(asn1EncodableVector);
        if (array2 != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int j = n; j != array2.length; ++j) {
                asn1EncodableVector2.add(array2[j]);
            }
            this.professionOIDs = new DERSequence(asn1EncodableVector2);
        }
        this.registrationNumber = registrationNumber;
        this.addProfessionInfo = addProfessionInfo;
    }
    
    public static ProfessionInfo getInstance(final Object o) {
        if (o == null || o instanceof ProfessionInfo) {
            return (ProfessionInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ProfessionInfo((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1OctetString getAddProfessionInfo() {
        return this.addProfessionInfo;
    }
    
    public NamingAuthority getNamingAuthority() {
        return this.namingAuthority;
    }
    
    public DirectoryString[] getProfessionItems() {
        final DirectoryString[] array = new DirectoryString[this.professionItems.size()];
        final Enumeration objects = this.professionItems.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = DirectoryString.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    public ASN1ObjectIdentifier[] getProfessionOIDs() {
        final ASN1Sequence professionOIDs = this.professionOIDs;
        int n = 0;
        if (professionOIDs == null) {
            return new ASN1ObjectIdentifier[0];
        }
        final ASN1ObjectIdentifier[] array = new ASN1ObjectIdentifier[professionOIDs.size()];
        final Enumeration objects = this.professionOIDs.getObjects();
        while (objects.hasMoreElements()) {
            array[n] = ASN1ObjectIdentifier.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    public String getRegistrationNumber() {
        return this.registrationNumber;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.namingAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.namingAuthority));
        }
        asn1EncodableVector.add(this.professionItems);
        final ASN1Sequence professionOIDs = this.professionOIDs;
        if (professionOIDs != null) {
            asn1EncodableVector.add(professionOIDs);
        }
        if (this.registrationNumber != null) {
            asn1EncodableVector.add(new DERPrintableString(this.registrationNumber, true));
        }
        final ASN1OctetString addProfessionInfo = this.addProfessionInfo;
        if (addProfessionInfo != null) {
            asn1EncodableVector.add(addProfessionInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
