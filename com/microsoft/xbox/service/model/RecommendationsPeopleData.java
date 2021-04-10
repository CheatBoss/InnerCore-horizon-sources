package com.microsoft.xbox.service.model;

import com.microsoft.xbox.service.network.managers.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.app.*;

public class RecommendationsPeopleData extends FollowersData
{
    private IPeopleHubResult.PeopleHubRecommendation recommendationInfo;
    
    public RecommendationsPeopleData(final IPeopleHubResult.PeopleHubPersonSummary peopleHubPersonSummary) {
        super(peopleHubPersonSummary);
        XLEAssert.assertNotNull(peopleHubPersonSummary.recommendation);
        this.recommendationInfo = peopleHubPersonSummary.recommendation;
    }
    
    public RecommendationsPeopleData(final boolean b, final DummyType dummyType) {
        super(b, dummyType);
    }
    
    public boolean getIsFacebookFriend() {
        return this.recommendationInfo.getRecommendationType() == IPeopleHubResult.RecommendationType.FacebookFriend;
    }
    
    public String getRecommendationFirstReason() {
        if (XLEUtil.isNullOrEmpty(this.recommendationInfo.Reasons)) {
            return "";
        }
        return this.recommendationInfo.Reasons.get(0);
    }
    
    public IPeopleHubResult.RecommendationType getRecommendationType() {
        return this.recommendationInfo.getRecommendationType();
    }
}
