package com.google.android.gms.signin.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.*;

public class SignInResponseCreator implements Parcelable$Creator<SignInResponse>
{
    public SignInResponse createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        ConnectionResult connectionResult = null;
        int int1 = 0;
        ResolveAccountResponse resolveAccountResponse = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        SafeParcelReader.skipUnknownField(parcel, header);
                    }
                    else {
                        resolveAccountResponse = SafeParcelReader.createParcelable(parcel, header, ResolveAccountResponse.CREATOR);
                    }
                }
                else {
                    connectionResult = SafeParcelReader.createParcelable(parcel, header, ConnectionResult.CREATOR);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new SignInResponse(int1, connectionResult, resolveAccountResponse);
    }
    
    public SignInResponse[] newArray(final int n) {
        return new SignInResponse[n];
    }
}
