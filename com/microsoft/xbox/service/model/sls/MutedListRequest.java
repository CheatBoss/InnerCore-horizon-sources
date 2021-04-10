package com.microsoft.xbox.service.model.sls;

import com.microsoft.xbox.toolkit.*;

public class MutedListRequest
{
    public long xuid;
    
    public MutedListRequest(final long xuid) {
        this.xuid = xuid;
    }
    
    public static String getNeverListRequestBody(final MutedListRequest mutedListRequest) {
        return GsonUtil.toJsonString(mutedListRequest);
    }
}
