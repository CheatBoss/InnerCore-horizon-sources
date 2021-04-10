package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class DisplayText extends ASN1Object implements ASN1Choice
{
    public static final int CONTENT_TYPE_BMPSTRING = 1;
    public static final int CONTENT_TYPE_IA5STRING = 0;
    public static final int CONTENT_TYPE_UTF8STRING = 2;
    public static final int CONTENT_TYPE_VISIBLESTRING = 3;
    public static final int DISPLAY_TEXT_MAXIMUM_SIZE = 200;
    int contentType;
    ASN1String contents;
    
    public DisplayText(final int contentType, final String s) {
        String substring = s;
        if (s.length() > 200) {
            substring = s.substring(0, 200);
        }
        ASN1String contents;
        if ((this.contentType = contentType) != 0) {
            if (contentType != 1) {
                if (contentType != 2) {
                    if (contentType != 3) {
                        contents = new DERUTF8String(substring);
                    }
                    else {
                        contents = new DERVisibleString(substring);
                    }
                }
                else {
                    contents = new DERUTF8String(substring);
                }
            }
            else {
                contents = new DERBMPString(substring);
            }
        }
        else {
            contents = new DERIA5String(substring);
        }
        this.contents = contents;
    }
    
    public DisplayText(final String s) {
        String substring = s;
        if (s.length() > 200) {
            substring = s.substring(0, 200);
        }
        this.contentType = 2;
        this.contents = new DERUTF8String(substring);
    }
    
    private DisplayText(final ASN1String contents) {
        this.contents = contents;
        int contentType;
        if (contents instanceof DERUTF8String) {
            contentType = 2;
        }
        else if (contents instanceof DERBMPString) {
            contentType = 1;
        }
        else if (contents instanceof DERIA5String) {
            contentType = 0;
        }
        else {
            if (!(contents instanceof DERVisibleString)) {
                throw new IllegalArgumentException("unknown STRING type in DisplayText");
            }
            contentType = 3;
        }
        this.contentType = contentType;
    }
    
    public static DisplayText getInstance(final Object o) {
        if (o instanceof ASN1String) {
            return new DisplayText((ASN1String)o);
        }
        if (o != null && !(o instanceof DisplayText)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("illegal object in getInstance: ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        return (DisplayText)o;
    }
    
    public static DisplayText getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public String getString() {
        return this.contents.getString();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return (ASN1Primitive)this.contents;
    }
}
