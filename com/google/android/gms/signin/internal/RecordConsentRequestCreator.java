package com.google.android.gms.signin.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.api.*;
import android.accounts.*;

public class RecordConsentRequestCreator implements Parcelable$Creator<RecordConsentRequest>
{
    public RecordConsentRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Account account = null;
        int int1 = 0;
        String string;
        Object o = string = null;
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
                        o = SafeParcelReader.createTypedArray(parcel, header, Scope.CREATOR);
                    }
                }
                else {
                    account = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                }
            }
            else {
                int1 = SafeParcelReader.readInt(parcel, header);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new RecordConsentRequest(int1, account, (Scope[])o, string);
    }
    
    public RecordConsentRequest[] newArray(final int n) {
        return new RecordConsentRequest[n];
    }
}
