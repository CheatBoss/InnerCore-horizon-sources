package com.google.android.gms.common;

import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class GoogleCertificatesQueryCreator implements Parcelable$Creator<GoogleCertificatesQuery>
{
    public GoogleCertificatesQuery createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String string = null;
        IBinder iBinder = null;
        boolean boolean1 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        SafeParcelReader.skipUnknownField(parcel, header);
                    }
                    else {
                        boolean1 = SafeParcelReader.readBoolean(parcel, header);
                    }
                }
                else {
                    iBinder = SafeParcelReader.readIBinder(parcel, header);
                }
            }
            else {
                string = SafeParcelReader.createString(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new GoogleCertificatesQuery(string, iBinder, boolean1);
    }
    
    public GoogleCertificatesQuery[] newArray(final int n) {
        return new GoogleCertificatesQuery[n];
    }
}
