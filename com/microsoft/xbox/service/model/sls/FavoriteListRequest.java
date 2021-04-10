package com.microsoft.xbox.service.model.sls;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class FavoriteListRequest
{
    public ArrayList<String> xuids;
    
    public FavoriteListRequest(final ArrayList<String> xuids) {
        this.xuids = xuids;
    }
    
    public static String getFavoriteListRequestBody(final FavoriteListRequest favoriteListRequest) {
        return GsonUtil.toJsonString(favoriteListRequest);
    }
}
