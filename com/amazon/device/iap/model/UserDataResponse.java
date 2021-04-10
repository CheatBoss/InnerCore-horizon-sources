package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;

public final class UserDataResponse
{
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String REQUEST_STATUS = "REQUEST_STATUS";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", requestStatus: \"%s\", userData: \"%s\")";
    private static final String USER_DATA = "USER_DATA";
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final UserData userData;
    
    public UserDataResponse(final UserDataResponseBuilder userDataResponseBuilder) {
        d.a(userDataResponseBuilder.getRequestId(), "requestId");
        d.a(userDataResponseBuilder.getRequestStatus(), "requestStatus");
        this.requestId = userDataResponseBuilder.getRequestId();
        this.requestStatus = userDataResponseBuilder.getRequestStatus();
        this.userData = userDataResponseBuilder.getUserData();
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public UserData getUserData() {
        return this.userData;
    }
    
    public JSONObject toJSON() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("REQUEST_ID", (Object)this.requestId);
        jsonObject.put("REQUEST_STATUS", (Object)this.requestStatus);
        final UserData userData = this.userData;
        Object json;
        if (userData != null) {
            json = userData.toJSON();
        }
        else {
            json = "";
        }
        jsonObject.put("USER_DATA", json);
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final String string = super.toString();
        final RequestId requestId = this.requestId;
        final RequestStatus requestStatus = this.requestStatus;
        String string2 = "null";
        String string3;
        if (requestStatus != null) {
            string3 = requestStatus.toString();
        }
        else {
            string3 = "null";
        }
        final UserData userData = this.userData;
        if (userData != null) {
            string2 = userData.toString();
        }
        return String.format("(%s, requestId: \"%s\", requestStatus: \"%s\", userData: \"%s\")", string, requestId, string3, string2);
    }
    
    public enum RequestStatus
    {
        FAILED, 
        NOT_SUPPORTED, 
        SUCCESSFUL;
    }
}
