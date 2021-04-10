package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.*;
import android.accounts.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public class AuthAccountRequestCreator implements Parcelable$Creator<AuthAccountRequest>
{
    public AuthAccountRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Object iBinder = null;
        Object integerObject;
        Object o = integerObject = iBinder;
        Object o2;
        Integer integerObject2 = (Integer)(o2 = integerObject);
        int int1 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(header)) {
                default: {
                    SafeParcelReader.skipUnknownField(parcel, header);
                    continue;
                }
                case 6: {
                    o2 = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 5: {
                    integerObject2 = SafeParcelReader.readIntegerObject(parcel, header);
                    continue;
                }
                case 4: {
                    integerObject = SafeParcelReader.readIntegerObject(parcel, header);
                    continue;
                }
                case 3: {
                    o = SafeParcelReader.createTypedArray(parcel, header, Scope.CREATOR);
                    continue;
                }
                case 2: {
                    iBinder = SafeParcelReader.readIBinder(parcel, header);
                    continue;
                }
                case 1: {
                    int1 = SafeParcelReader.readInt(parcel, header);
                    continue;
                }
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new AuthAccountRequest(int1, (IBinder)iBinder, (Scope[])o, (Integer)integerObject, integerObject2, (Account)o2);
    }
    
    public AuthAccountRequest[] newArray(final int n) {
        return new AuthAccountRequest[n];
    }
}
