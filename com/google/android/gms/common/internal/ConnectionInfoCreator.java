package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.*;
import android.os.*;

public class ConnectionInfoCreator implements Parcelable$Creator<ConnectionInfo>
{
    public ConnectionInfo createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Bundle bundle = null;
        Feature[] array = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    SafeParcelReader.skipUnknownField(parcel, header);
                }
                else {
                    array = SafeParcelReader.createTypedArray(parcel, header, Feature.CREATOR);
                }
            }
            else {
                bundle = SafeParcelReader.createBundle(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new ConnectionInfo(bundle, array);
    }
    
    public ConnectionInfo[] newArray(final int n) {
        return new ConnectionInfo[n];
    }
}
