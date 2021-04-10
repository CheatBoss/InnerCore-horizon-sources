package com.microsoft.xbox.service.model.sls;

import com.microsoft.xbox.toolkit.*;

public class SubmitFeedbackRequest
{
    public String evidenceId;
    public FeedbackType feedbackType;
    public String sessionRef;
    public String textReason;
    public String voiceReasonId;
    public long xuid;
    
    public SubmitFeedbackRequest(final long xuid, final String sessionRef, final FeedbackType feedbackType, final String textReason, final String voiceReasonId, final String evidenceId) {
        this.xuid = xuid;
        this.sessionRef = sessionRef;
        this.feedbackType = feedbackType;
        this.textReason = textReason;
        this.voiceReasonId = voiceReasonId;
        this.evidenceId = evidenceId;
    }
    
    public static String getSubmitFeedbackRequestBody(final SubmitFeedbackRequest submitFeedbackRequest) {
        return GsonUtil.toJsonString(submitFeedbackRequest);
    }
}
