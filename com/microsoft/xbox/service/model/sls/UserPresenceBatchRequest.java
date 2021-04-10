package com.microsoft.xbox.service.model.sls;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class UserPresenceBatchRequest
{
    public String level;
    public ArrayList<String> users;
    
    public UserPresenceBatchRequest(final ArrayList<String> users) {
        this.level = "all";
        this.users = users;
    }
    
    public static String getUserPresenceBatchRequestBody(final UserPresenceBatchRequest userPresenceBatchRequest) {
        return GsonUtil.toJsonString(userPresenceBatchRequest);
    }
}
