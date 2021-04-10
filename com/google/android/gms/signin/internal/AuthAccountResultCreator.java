package com.google.android.gms.signin.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.content.*;

public class AuthAccountResultCreator implements Parcelable$Creator<AuthAccountResult>
{
    public AuthAccountResult createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        Intent intent = null;
        int int2 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            final int fieldId = SafeParcelReader.getFieldId(header);
            if (fieldId != 1) {
                if (fieldId != 2) {
                    if (fieldId != 3) {
                        SafeParcelReader.skipUnknownField(parcel, header);
                    }
                    else {
                        intent = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<Intent>)Intent.CREATOR);
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
        return new AuthAccountResult(int1, int2, intent);
    }
    
    public AuthAccountResult[] newArray(final int n) {
        return new AuthAccountResult[n];
    }
}
