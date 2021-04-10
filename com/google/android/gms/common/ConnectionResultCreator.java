package com.google.android.gms.common;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.app.*;

public class ConnectionResultCreator implements Parcelable$Creator<ConnectionResult>
{
    public ConnectionResult createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        String string;
        Object o = string = null;
        int int2 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        if (fieldId != 4) {
                            SafeParcelReader.skipUnknownField(parcel, header);
                        }
                        else {
                            string = SafeParcelReader.createString(parcel, header);
                        }
                    }
                    else {
                        o = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<PendingIntent>)PendingIntent.CREATOR);
                    }
                }
                else {
                    int2 = SafeParcelReader.readInt(parcel, header);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new ConnectionResult(int1, int2, (PendingIntent)o, string);
    }
    
    public ConnectionResult[] newArray(final int n) {
        return new ConnectionResult[n];
    }
}
