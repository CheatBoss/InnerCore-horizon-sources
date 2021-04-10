package com.google.android.gms.common.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.auth.api.signin.*;
import android.accounts.*;

public class ResolveAccountRequestCreator implements Parcelable$Creator<ResolveAccountRequest>
{
    public ResolveAccountRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int int1 = 0;
        Account account = null;
        GoogleSignInAccount googleSignInAccount = null;
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
                            googleSignInAccount = SafeParcelReader.createParcelable(parcel, header, GoogleSignInAccount.CREATOR);
                        }
                    }
                    else {
                        int2 = SafeParcelReader.readInt(parcel, header);
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
        return new ResolveAccountRequest(int1, account, int2, googleSignInAccount);
    }
    
    public ResolveAccountRequest[] newArray(final int n) {
        return new ResolveAccountRequest[n];
    }
}
