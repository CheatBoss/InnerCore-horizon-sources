package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class RoleSyntax extends ASN1Object
{
    private GeneralNames roleAuthority;
    private GeneralName roleName;
    
    public RoleSyntax(final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        this(new GeneralName(6, s2));
    }
    
    private RoleSyntax(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            for (int i = 0; i != asn1Sequence.size(); ++i) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
                final int tagNo = instance.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        throw new IllegalArgumentException("Unknown tag in RoleSyntax");
                    }
                    this.roleName = GeneralName.getInstance(instance, true);
                }
                else {
                    this.roleAuthority = GeneralNames.getInstance(instance, false);
                }
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public RoleSyntax(final GeneralName generalName) {
        this(null, generalName);
    }
    
    public RoleSyntax(final GeneralNames roleAuthority, final GeneralName roleName) {
        if (roleName != null && roleName.getTagNo() == 6 && !((ASN1String)roleName.getName()).getString().equals("")) {
            this.roleAuthority = roleAuthority;
            this.roleName = roleName;
            return;
        }
        throw new IllegalArgumentException("the role name MUST be non empty and MUST use the URI option of GeneralName");
    }
    
    public static RoleSyntax getInstance(final Object o) {
        if (o instanceof RoleSyntax) {
            return (RoleSyntax)o;
        }
        if (o != null) {
            return new RoleSyntax(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralNames getRoleAuthority() {
        return this.roleAuthority;
    }
    
    public String[] getRoleAuthorityAsString() {
        final GeneralNames roleAuthority = this.roleAuthority;
        int i = 0;
        if (roleAuthority == null) {
            return new String[0];
        }
        final GeneralName[] names = roleAuthority.getNames();
        final String[] array = new String[names.length];
        while (i < names.length) {
            final ASN1Encodable name = names[i].getName();
            if (name instanceof ASN1String) {
                array[i] = ((ASN1String)name).getString();
            }
            else {
                array[i] = name.toString();
            }
            ++i;
        }
        return array;
    }
    
    public GeneralName getRoleName() {
        return this.roleName;
    }
    
    public String getRoleNameAsString() {
        return ((ASN1String)this.roleName.getName()).getString();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.roleAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.roleAuthority));
        }
        asn1EncodableVector.add(new DERTaggedObject(true, 1, this.roleName));
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(this.getRoleNameAsString());
        sb.append(" - Auth: ");
        final StringBuffer sb2 = new StringBuffer(sb.toString());
        final GeneralNames roleAuthority = this.roleAuthority;
        if (roleAuthority != null && roleAuthority.getNames().length != 0) {
            final String[] roleAuthorityAsString = this.getRoleAuthorityAsString();
            sb2.append('[');
            sb2.append(roleAuthorityAsString[0]);
            for (int i = 1; i < roleAuthorityAsString.length; ++i) {
                sb2.append(", ");
                sb2.append(roleAuthorityAsString[i]);
            }
            sb2.append(']');
        }
        else {
            sb2.append("N/A");
        }
        return sb2.toString();
    }
}
