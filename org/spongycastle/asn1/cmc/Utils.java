package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

class Utils
{
    static BodyPartID[] clone(final BodyPartID[] array) {
        final BodyPartID[] array2 = new BodyPartID[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static Extension[] clone(final Extension[] array) {
        final Extension[] array2 = new Extension[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    static BodyPartID[] toBodyPartIDArray(final ASN1Sequence asn1Sequence) {
        final BodyPartID[] array = new BodyPartID[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            array[i] = BodyPartID.getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
}
