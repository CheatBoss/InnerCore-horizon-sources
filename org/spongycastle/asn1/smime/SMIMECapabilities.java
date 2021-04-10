package org.spongycastle.asn1.smime;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.cms.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SMIMECapabilities extends ASN1Object
{
    public static final ASN1ObjectIdentifier aes128_CBC;
    public static final ASN1ObjectIdentifier aes192_CBC;
    public static final ASN1ObjectIdentifier aes256_CBC;
    public static final ASN1ObjectIdentifier canNotDecryptAny;
    public static final ASN1ObjectIdentifier cast5_CBC;
    public static final ASN1ObjectIdentifier dES_CBC;
    public static final ASN1ObjectIdentifier dES_EDE3_CBC;
    public static final ASN1ObjectIdentifier idea_CBC;
    public static final ASN1ObjectIdentifier preferSignedData;
    public static final ASN1ObjectIdentifier rC2_CBC;
    public static final ASN1ObjectIdentifier sMIMECapabilitesVersions;
    private ASN1Sequence capabilities;
    
    static {
        preferSignedData = PKCSObjectIdentifiers.preferSignedData;
        canNotDecryptAny = PKCSObjectIdentifiers.canNotDecryptAny;
        sMIMECapabilitesVersions = PKCSObjectIdentifiers.sMIMECapabilitiesVersions;
        aes256_CBC = NISTObjectIdentifiers.id_aes256_CBC;
        aes192_CBC = NISTObjectIdentifiers.id_aes192_CBC;
        aes128_CBC = NISTObjectIdentifiers.id_aes128_CBC;
        idea_CBC = new ASN1ObjectIdentifier("1.3.6.1.4.1.188.7.1.1.2");
        cast5_CBC = new ASN1ObjectIdentifier("1.2.840.113533.7.66.10");
        dES_CBC = new ASN1ObjectIdentifier("1.3.14.3.2.7");
        dES_EDE3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC;
        rC2_CBC = PKCSObjectIdentifiers.RC2_CBC;
    }
    
    public SMIMECapabilities(final ASN1Sequence capabilities) {
        this.capabilities = capabilities;
    }
    
    public static SMIMECapabilities getInstance(final Object o) {
        if (o == null || o instanceof SMIMECapabilities) {
            return (SMIMECapabilities)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SMIMECapabilities((ASN1Sequence)o);
        }
        if (o instanceof Attribute) {
            return new SMIMECapabilities((ASN1Sequence)((Attribute)o).getAttrValues().getObjectAt(0));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Vector getCapabilities(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final Enumeration objects = this.capabilities.getObjects();
        final Vector<SMIMECapability> vector = new Vector<SMIMECapability>();
        if (asn1ObjectIdentifier == null) {
            while (objects.hasMoreElements()) {
                vector.addElement(SMIMECapability.getInstance(objects.nextElement()));
            }
        }
        else {
            while (objects.hasMoreElements()) {
                final SMIMECapability instance = SMIMECapability.getInstance(objects.nextElement());
                if (asn1ObjectIdentifier.equals(instance.getCapabilityID())) {
                    vector.addElement(instance);
                }
            }
        }
        return vector;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.capabilities;
    }
}
