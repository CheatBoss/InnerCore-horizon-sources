package com.google.android.gms.common.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.api.*;

public class SignInButtonConfigCreator implements Parcelable$Creator<SignInButtonConfig>
{
    public SignInButtonConfig createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        Scope[] array = null;
        int int2 = 0;
        int int3 = 0;
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
                            array = SafeParcelReader.createTypedArray(parcel, header, Scope.CREATOR);
                        }
                    }
                    else {
                        int3 = SafeParcelReader.readInt(parcel, header);
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
        return new SignInButtonConfig(int1, int2, int3, array);
    }
    
    public SignInButtonConfig[] newArray(final int n) {
        return new SignInButtonConfig[n];
    }
}
