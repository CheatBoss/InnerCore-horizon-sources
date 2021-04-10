package org.spongycastle.x509.extension;

import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;
import java.util.*;
import java.security.cert.*;
import org.spongycastle.asn1.x509.*;

public class X509ExtensionUtil
{
    public static ASN1Primitive fromExtensionValue(final byte[] array) throws IOException {
        return ASN1Primitive.fromByteArray(((ASN1OctetString)ASN1Primitive.fromByteArray(array)).getOctets());
    }
    
    private static Collection getAlternativeNames(byte[] array) throws CertificateParsingException {
        if (array == null) {
            return Collections.EMPTY_LIST;
        }
        while (true) {
            while (true) {
                Label_0270: {
                    try {
                        final ArrayList<List<Object>> list = new ArrayList<List<Object>>();
                        final Enumeration objects = ASN1Sequence.getInstance(fromExtensionValue(array)).getObjects();
                        while (objects.hasMoreElements()) {
                            final GeneralName instance = GeneralName.getInstance(objects.nextElement());
                            final ArrayList<byte[]> list2 = new ArrayList<byte[]>();
                            list2.add(Integers.valueOf(instance.getTagNo()));
                            switch (instance.getTagNo()) {
                                case 8: {
                                    array = (byte[])(Object)ASN1ObjectIdentifier.getInstance(instance.getName()).getId();
                                    break;
                                }
                                case 7: {
                                    array = ASN1OctetString.getInstance(instance.getName()).getOctets();
                                    break;
                                }
                                case 4: {
                                    array = (byte[])(Object)X500Name.getInstance(instance.getName()).toString();
                                    break;
                                }
                                case 1:
                                case 2:
                                case 6: {
                                    array = (byte[])(Object)((ASN1String)instance.getName()).getString();
                                    break;
                                }
                                case 0:
                                case 3:
                                case 5: {
                                    array = (byte[])(Object)instance.getName().toASN1Primitive();
                                    break;
                                }
                                default: {
                                    break Label_0270;
                                }
                            }
                            list2.add(array);
                            list.add((List<Object>)list2);
                        }
                        return Collections.unmodifiableCollection((Collection<?>)list);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Bad tag number: ");
                        GeneralName instance = null;
                        sb.append(instance.getTagNo());
                        throw new IOException(sb.toString());
                    }
                    catch (Exception ex) {
                        throw new CertificateParsingException(ex.getMessage());
                    }
                }
                continue;
            }
        }
    }
    
    public static Collection getIssuerAlternativeNames(final X509Certificate x509Certificate) throws CertificateParsingException {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.issuerAlternativeName.getId()));
    }
    
    public static Collection getSubjectAlternativeNames(final X509Certificate x509Certificate) throws CertificateParsingException {
        return getAlternativeNames(x509Certificate.getExtensionValue(X509Extension.subjectAlternativeName.getId()));
    }
}
