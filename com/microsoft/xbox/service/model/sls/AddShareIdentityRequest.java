package com.microsoft.xbox.service.model.sls;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class AddShareIdentityRequest
{
    public ArrayList<String> xuids;
    
    public AddShareIdentityRequest(final ArrayList<String> xuids) {
        this.xuids = xuids;
    }
    
    public static String getAddShareIdentityRequestBody(final AddShareIdentityRequest addShareIdentityRequest) {
        return GsonUtil.toJsonString(addShareIdentityRequest);
    }
}
