package org.spongycastle.asn1.est;

class Utils
{
    static AttrOrOID[] clone(final AttrOrOID[] array) {
        final AttrOrOID[] array2 = new AttrOrOID[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
}
