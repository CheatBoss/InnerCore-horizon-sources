package com.google.android.gms.signin.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.internal.*;

public class SignInRequestCreator implements Parcelable$Creator<SignInRequest>
{
    public SignInRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        ResolveAccountRequest resolveAccountRequest = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    SafeParcelReader.skipUnknownField(parcel, header);
                }
                else {
                    resolveAccountRequest = SafeParcelReader.createParcelable(parcel, header, ResolveAccountRequest.CREATOR);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new SignInRequest(int1, resolveAccountRequest);
    }
    
    public SignInRequest[] newArray(final int n) {
        return new SignInRequest[n];
    }
}
