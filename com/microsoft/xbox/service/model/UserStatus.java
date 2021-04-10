package com.microsoft.xbox.service.model;

import com.microsoft.xbox.toolkit.*;

public enum UserStatus
{
    Offline, 
    Online;
    
    public static UserStatus getStatusFromString(final String s) {
        if (JavaUtil.stringsEqualCaseInsensitive(s, UserStatus.Online.toString())) {
            return UserStatus.Online;
        }
        return UserStatus.Offline;
    }
}
