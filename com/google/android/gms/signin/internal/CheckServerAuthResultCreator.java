package com.google.android.gms.signin.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.api.*;
import java.util.*;

public class CheckServerAuthResultCreator implements Parcelable$Creator<CheckServerAuthResult>
{
    public CheckServerAuthResult createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        List<Scope> typedList = null;
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
                        typedList = SafeParcelReader.createTypedList(parcel, header, Scope.CREATOR);
                    }
                }
                else {
                    boolean1 = SafeParcelReader.readBoolean(parcel, header);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new CheckServerAuthResult(int1, boolean1, typedList);
    }
    
    public CheckServerAuthResult[] newArray(final int n) {
        return new CheckServerAuthResult[n];
    }
}
