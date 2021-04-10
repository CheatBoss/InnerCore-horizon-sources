package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class AdditionalInformationSyntax extends ASN1Object
{
    private DirectoryString information;
    
    public AdditionalInformationSyntax(final String s) {
        this(new DirectoryString(s));
    }
    
    private AdditionalInformationSyntax(final DirectoryString information) {
        this.information = information;
    }
    
    public static AdditionalInformationSyntax getInstance(final Object o) {
        if (o instanceof AdditionalInformationSyntax) {
            return (AdditionalInformationSyntax)o;
        }
        if (o != null) {
            return new AdditionalInformationSyntax(DirectoryString.getInstance(o));
        }
        return null;
    }
    
    public DirectoryString getInformation() {
        return this.information;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.information.toASN1Primitive();
    }
}
