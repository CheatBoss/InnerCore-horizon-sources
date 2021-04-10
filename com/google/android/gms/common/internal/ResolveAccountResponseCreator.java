package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.*;
import android.os.*;

public class ResolveAccountResponseCreator implements Parcelable$Creator<ResolveAccountResponse>
{
    public ResolveAccountResponse createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        ConnectionResult connectionResult;
        Object iBinder = connectionResult = null;
        int int1 = 0;
        boolean boolean1 = false;
        boolean boolean2 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        if (fieldId != 4) {
                            if (fieldId != 5) {
                                SafeParcelReader.skipUnknownField(parcel, header);
                            }
                            else {
                                boolean2 = SafeParcelReader.readBoolean(parcel, header);
                            }
                        }
                        else {
                            boolean1 = SafeParcelReader.readBoolean(parcel, header);
                        }
                    }
                    else {
                        connectionResult = SafeParcelReader.createParcelable(parcel, header, ConnectionResult.CREATOR);
                    }
                }
                else {
                    iBinder = SafeParcelReader.readIBinder(parcel, header);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new ResolveAccountResponse(int1, (IBinder)iBinder, connectionResult, boolean1, boolean2);
    }
    
    public ResolveAccountResponse[] newArray(final int n) {
        return new ResolveAccountResponse[n];
    }
}
