package com.google.android.gms.common.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class ValidateAccountRequestCreator implements Parcelable$Creator<ValidateAccountRequest>
{
    public ValidateAccountRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            if (SafeParcelReader.getFieldId(header) != 1) {
                SafeParcelReader.skipUnknownField(parcel, header);
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new ValidateAccountRequest(int1);
    }
    
    public ValidateAccountRequest[] newArray(final int n) {
        return new ValidateAccountRequest[n];
    }
}
