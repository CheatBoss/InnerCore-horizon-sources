package org.spongycastle.asn1.util;

import java.io.*;
import org.spongycastle.asn1.*;

public class Dump
{
    public static void main(final String[] array) throws Exception {
        final ASN1InputStream asn1InputStream = new ASN1InputStream(new FileInputStream(array[0]));
        while (true) {
            final ASN1Primitive object = asn1InputStream.readObject();
            if (object == null) {
                break;
            }
            System.out.println(ASN1Dump.dumpAsString(object));
        }
    }
}
