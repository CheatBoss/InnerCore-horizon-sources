package org.spongycastle.asn1.util;

import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class ASN1Dump
{
    private static final int SAMPLE_SIZE = 32;
    private static final String TAB = "    ";
    
    static void _dumpAsString(String string, final boolean b, ASN1Primitive externalContent, final StringBuffer sb) {
        final String lineSeparator = Strings.lineSeparator();
        if (externalContent instanceof ASN1Sequence) {
            final Enumeration objects = ((ASN1Sequence)externalContent).getObjects();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("    ");
            final String string2 = sb2.toString();
            sb.append(string);
            if (externalContent instanceof BERSequence) {
                string = "BER Sequence";
            }
            else if (externalContent instanceof DERSequence) {
                string = "DER Sequence";
            }
            else {
                string = "Sequence";
            }
        Label_0178:
            while (true) {
                sb.append(string);
                sb.append(lineSeparator);
                while (objects.hasMoreElements()) {
                    final ASN1Primitive nextElement = objects.nextElement();
                    if (nextElement == null || nextElement.equals(DERNull.INSTANCE)) {
                        sb.append(string2);
                        string = "NULL";
                        continue Label_0178;
                    }
                    ASN1Primitive asn1Primitive;
                    if (nextElement instanceof ASN1Primitive) {
                        asn1Primitive = nextElement;
                    }
                    else {
                        asn1Primitive = nextElement.toASN1Primitive();
                    }
                    _dumpAsString(string2, b, asn1Primitive, sb);
                }
                break;
            }
        }
        else if (externalContent instanceof ASN1TaggedObject) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("    ");
            final String string3 = sb3.toString();
            sb.append(string);
            if (externalContent instanceof BERTaggedObject) {
                string = "BER Tagged [";
            }
            else {
                string = "Tagged [";
            }
            sb.append(string);
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)externalContent;
            sb.append(Integer.toString(asn1TaggedObject.getTagNo()));
            sb.append(']');
            if (!asn1TaggedObject.isExplicit()) {
                sb.append(" IMPLICIT ");
            }
            sb.append(lineSeparator);
            if (asn1TaggedObject.isEmpty()) {
                sb.append(string3);
                sb.append("EMPTY");
                sb.append(lineSeparator);
                return;
            }
            _dumpAsString(string3, b, asn1TaggedObject.getObject(), sb);
        }
        else if (externalContent instanceof ASN1Set) {
            final Enumeration objects2 = ((ASN1Set)externalContent).getObjects();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string);
            sb4.append("    ");
            final String string4 = sb4.toString();
            sb.append(string);
            if (externalContent instanceof BERSet) {
                string = "BER Set";
            }
            else {
                string = "DER Set";
            }
        Label_0453:
            while (true) {
                sb.append(string);
                sb.append(lineSeparator);
                while (objects2.hasMoreElements()) {
                    final ASN1Primitive nextElement2 = objects2.nextElement();
                    if (nextElement2 == null) {
                        sb.append(string4);
                        string = "NULL";
                        continue Label_0453;
                    }
                    ASN1Primitive asn1Primitive2;
                    if (nextElement2 instanceof ASN1Primitive) {
                        asn1Primitive2 = nextElement2;
                    }
                    else {
                        asn1Primitive2 = nextElement2.toASN1Primitive();
                    }
                    _dumpAsString(string4, b, asn1Primitive2, sb);
                }
                break;
            }
        }
        else if (externalContent instanceof ASN1OctetString) {
            final ASN1OctetString asn1OctetString = (ASN1OctetString)externalContent;
            StringBuilder sb5;
            int n;
            if (externalContent instanceof BEROctetString) {
                sb5 = new StringBuilder();
                sb5.append(string);
                sb5.append("BER Constructed Octet String[");
                n = asn1OctetString.getOctets().length;
            }
            else {
                sb5 = new StringBuilder();
                sb5.append(string);
                sb5.append("DER Octet String[");
                n = asn1OctetString.getOctets().length;
            }
            sb5.append(n);
            sb5.append("] ");
            sb.append(sb5.toString());
            if (b) {
                sb.append(dumpBinaryDataAsString(string, asn1OctetString.getOctets()));
                return;
            }
            sb.append(lineSeparator);
        }
        else {
            if (externalContent instanceof ASN1ObjectIdentifier) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(string);
                sb6.append("ObjectIdentifier(");
                sb6.append(((ASN1ObjectIdentifier)externalContent).getId());
                sb6.append(")");
                sb6.append(lineSeparator);
                sb.append(sb6.toString());
                return;
            }
            if (externalContent instanceof ASN1Boolean) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(string);
                sb7.append("Boolean(");
                sb7.append(((ASN1Boolean)externalContent).isTrue());
                sb7.append(")");
                sb7.append(lineSeparator);
                sb.append(sb7.toString());
                return;
            }
            if (externalContent instanceof ASN1Integer) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(string);
                sb8.append("Integer(");
                sb8.append(((ASN1Integer)externalContent).getValue());
                sb8.append(")");
                sb8.append(lineSeparator);
                sb.append(sb8.toString());
                return;
            }
            if (externalContent instanceof DERBitString) {
                final DERBitString derBitString = (DERBitString)externalContent;
                final StringBuilder sb9 = new StringBuilder();
                sb9.append(string);
                sb9.append("DER Bit String[");
                sb9.append(derBitString.getBytes().length);
                sb9.append(", ");
                sb9.append(derBitString.getPadBits());
                sb9.append("] ");
                sb.append(sb9.toString());
                if (b) {
                    sb.append(dumpBinaryDataAsString(string, derBitString.getBytes()));
                    return;
                }
                sb.append(lineSeparator);
            }
            else {
                if (externalContent instanceof DERIA5String) {
                    final StringBuilder sb10 = new StringBuilder();
                    sb10.append(string);
                    sb10.append("IA5String(");
                    sb10.append(((DERIA5String)externalContent).getString());
                    sb10.append(") ");
                    sb10.append(lineSeparator);
                    sb.append(sb10.toString());
                    return;
                }
                if (externalContent instanceof DERUTF8String) {
                    final StringBuilder sb11 = new StringBuilder();
                    sb11.append(string);
                    sb11.append("UTF8String(");
                    sb11.append(((DERUTF8String)externalContent).getString());
                    sb11.append(") ");
                    sb11.append(lineSeparator);
                    sb.append(sb11.toString());
                    return;
                }
                if (externalContent instanceof DERPrintableString) {
                    final StringBuilder sb12 = new StringBuilder();
                    sb12.append(string);
                    sb12.append("PrintableString(");
                    sb12.append(((DERPrintableString)externalContent).getString());
                    sb12.append(") ");
                    sb12.append(lineSeparator);
                    sb.append(sb12.toString());
                    return;
                }
                if (externalContent instanceof DERVisibleString) {
                    final StringBuilder sb13 = new StringBuilder();
                    sb13.append(string);
                    sb13.append("VisibleString(");
                    sb13.append(((DERVisibleString)externalContent).getString());
                    sb13.append(") ");
                    sb13.append(lineSeparator);
                    sb.append(sb13.toString());
                    return;
                }
                if (externalContent instanceof DERBMPString) {
                    final StringBuilder sb14 = new StringBuilder();
                    sb14.append(string);
                    sb14.append("BMPString(");
                    sb14.append(((DERBMPString)externalContent).getString());
                    sb14.append(") ");
                    sb14.append(lineSeparator);
                    sb.append(sb14.toString());
                    return;
                }
                if (externalContent instanceof DERT61String) {
                    final StringBuilder sb15 = new StringBuilder();
                    sb15.append(string);
                    sb15.append("T61String(");
                    sb15.append(((DERT61String)externalContent).getString());
                    sb15.append(") ");
                    sb15.append(lineSeparator);
                    sb.append(sb15.toString());
                    return;
                }
                if (externalContent instanceof DERGraphicString) {
                    final StringBuilder sb16 = new StringBuilder();
                    sb16.append(string);
                    sb16.append("GraphicString(");
                    sb16.append(((DERGraphicString)externalContent).getString());
                    sb16.append(") ");
                    sb16.append(lineSeparator);
                    sb.append(sb16.toString());
                    return;
                }
                if (externalContent instanceof DERVideotexString) {
                    final StringBuilder sb17 = new StringBuilder();
                    sb17.append(string);
                    sb17.append("VideotexString(");
                    sb17.append(((DERVideotexString)externalContent).getString());
                    sb17.append(") ");
                    sb17.append(lineSeparator);
                    sb.append(sb17.toString());
                    return;
                }
                if (externalContent instanceof ASN1UTCTime) {
                    final StringBuilder sb18 = new StringBuilder();
                    sb18.append(string);
                    sb18.append("UTCTime(");
                    sb18.append(((ASN1UTCTime)externalContent).getTime());
                    sb18.append(") ");
                    sb18.append(lineSeparator);
                    sb.append(sb18.toString());
                    return;
                }
                if (externalContent instanceof ASN1GeneralizedTime) {
                    final StringBuilder sb19 = new StringBuilder();
                    sb19.append(string);
                    sb19.append("GeneralizedTime(");
                    sb19.append(((ASN1GeneralizedTime)externalContent).getTime());
                    sb19.append(") ");
                    sb19.append(lineSeparator);
                    sb.append(sb19.toString());
                    return;
                }
                if (externalContent instanceof BERApplicationSpecific) {
                    sb.append(outputApplicationSpecific("BER", string, b, externalContent, lineSeparator));
                    return;
                }
                if (externalContent instanceof DERApplicationSpecific) {
                    sb.append(outputApplicationSpecific("DER", string, b, externalContent, lineSeparator));
                    return;
                }
                if (externalContent instanceof ASN1Enumerated) {
                    final ASN1Enumerated asn1Enumerated = (ASN1Enumerated)externalContent;
                    final StringBuilder sb20 = new StringBuilder();
                    sb20.append(string);
                    sb20.append("DER Enumerated(");
                    sb20.append(asn1Enumerated.getValue());
                    sb20.append(")");
                    sb20.append(lineSeparator);
                    sb.append(sb20.toString());
                    return;
                }
                if (externalContent instanceof DERExternal) {
                    final DERExternal derExternal = (DERExternal)externalContent;
                    final StringBuilder sb21 = new StringBuilder();
                    sb21.append(string);
                    sb21.append("External ");
                    sb21.append(lineSeparator);
                    sb.append(sb21.toString());
                    final StringBuilder sb22 = new StringBuilder();
                    sb22.append(string);
                    sb22.append("    ");
                    string = sb22.toString();
                    if (derExternal.getDirectReference() != null) {
                        final StringBuilder sb23 = new StringBuilder();
                        sb23.append(string);
                        sb23.append("Direct Reference: ");
                        sb23.append(derExternal.getDirectReference().getId());
                        sb23.append(lineSeparator);
                        sb.append(sb23.toString());
                    }
                    if (derExternal.getIndirectReference() != null) {
                        final StringBuilder sb24 = new StringBuilder();
                        sb24.append(string);
                        sb24.append("Indirect Reference: ");
                        sb24.append(derExternal.getIndirectReference().toString());
                        sb24.append(lineSeparator);
                        sb.append(sb24.toString());
                    }
                    if (derExternal.getDataValueDescriptor() != null) {
                        _dumpAsString(string, b, derExternal.getDataValueDescriptor(), sb);
                    }
                    final StringBuilder sb25 = new StringBuilder();
                    sb25.append(string);
                    sb25.append("Encoding: ");
                    sb25.append(derExternal.getEncoding());
                    sb25.append(lineSeparator);
                    sb.append(sb25.toString());
                    externalContent = derExternal.getExternalContent();
                    try {
                        _dumpAsString(string, b, externalContent, sb);
                        return;
                    }
                    finally {}
                }
                final StringBuilder sb26 = new StringBuilder();
                sb26.append(string);
                sb26.append(externalContent.toString());
                sb26.append(lineSeparator);
                sb.append(sb26.toString());
            }
        }
    }
    
    private static String calculateAscString(final byte[] array, final int n, final int n2) {
        final StringBuffer sb = new StringBuffer();
        for (int i = n; i != n + n2; ++i) {
            if (array[i] >= 32 && array[i] <= 126) {
                sb.append((char)array[i]);
            }
        }
        return sb.toString();
    }
    
    public static String dumpAsString(final Object o) {
        return dumpAsString(o, false);
    }
    
    public static String dumpAsString(final Object o, final boolean b) {
        final StringBuffer sb = new StringBuffer();
        ASN1Primitive asn1Primitive;
        if (o instanceof ASN1Primitive) {
            asn1Primitive = (ASN1Primitive)o;
        }
        else {
            if (!(o instanceof ASN1Encodable)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("unknown object type ");
                sb2.append(o.toString());
                return sb2.toString();
            }
            asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
        }
        _dumpAsString("", b, asn1Primitive, sb);
        return sb.toString();
    }
    
    private static String dumpBinaryDataAsString(String s, final byte[] array) {
        final String lineSeparator = Strings.lineSeparator();
        final StringBuffer sb = new StringBuffer();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("    ");
        final String string = sb2.toString();
        sb.append(lineSeparator);
        for (int i = 0; i < array.length; i += 32) {
            if (array.length - i > 32) {
                sb.append(string);
                sb.append(Strings.fromByteArray(Hex.encode(array, i, 32)));
                sb.append("    ");
                s = calculateAscString(array, i, 32);
            }
            else {
                sb.append(string);
                sb.append(Strings.fromByteArray(Hex.encode(array, i, array.length - i)));
                for (int j = array.length - i; j != 32; ++j) {
                    sb.append("  ");
                }
                sb.append("    ");
                s = calculateAscString(array, i, array.length - i);
            }
            sb.append(s);
            sb.append(lineSeparator);
        }
        return sb.toString();
    }
    
    private static String outputApplicationSpecific(final String s, final String s2, final boolean b, ASN1Primitive asn1Primitive, final String s3) {
        final ASN1ApplicationSpecific instance = ASN1ApplicationSpecific.getInstance(asn1Primitive);
        asn1Primitive = (ASN1Primitive)new StringBuffer();
        if (instance.isConstructed()) {
            try {
                final ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObject(16));
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append(s);
                sb.append(" ApplicationSpecific[");
                sb.append(instance.getApplicationTag());
                sb.append("]");
                sb.append(s3);
                ((StringBuffer)asn1Primitive).append(sb.toString());
                final Enumeration objects = instance2.getObjects();
                while (objects.hasMoreElements()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s2);
                    sb2.append("    ");
                    _dumpAsString(sb2.toString(), b, objects.nextElement(), (StringBuffer)asn1Primitive);
                }
            }
            catch (IOException ex) {
                ((StringBuffer)asn1Primitive).append(ex);
            }
            return ((StringBuffer)asn1Primitive).toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s2);
        sb3.append(s);
        sb3.append(" ApplicationSpecific[");
        sb3.append(instance.getApplicationTag());
        sb3.append("] (");
        sb3.append(Strings.fromByteArray(Hex.encode(instance.getContents())));
        sb3.append(")");
        sb3.append(s3);
        return sb3.toString();
    }
}
