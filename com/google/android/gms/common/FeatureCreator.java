package com.google.android.gms.common;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class FeatureCreator implements Parcelable$Creator<Feature>
{
    public Feature createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String string = null;
        int int1 = 0;
        long long1 = -1L;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        SafeParcelReader.skipUnknownField(parcel, header);
                    }
                    else {
                        long1 = SafeParcelReader.readLong(parcel, header);
                    }
                }
                else {
                    int1 = SafeParcelReader.readInt(parcel, header);
                }
            }
            else {
                string = SafeParcelReader.createString(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new Feature(string, int1, long1);
    }
    
    public Feature[] newArray(final int n) {
        return new Feature[n];
    }
}
