package com.microsoft.xbox.service.model.sls;

import com.microsoft.xbox.toolkit.*;

public class NeverListRequest
{
    public long xuid;
    
    public NeverListRequest(final long xuid) {
        this.xuid = xuid;
    }
    
    public static String getNeverListRequestBody(final NeverListRequest neverListRequest) {
        return GsonUtil.toJsonString(neverListRequest);
    }
}
