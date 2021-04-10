package com.microsoft.xbox.service.model.sls;

import com.microsoft.xbox.toolkit.*;
import com.microsoft.xboxtcui.*;

public enum FeedbackType
{
    Comms911, 
    CommsAbusiveVoice, 
    CommsInappropriateVideo, 
    CommsMuted, 
    CommsPhishing, 
    CommsPictureMessage, 
    CommsSpam, 
    CommsTextMessage, 
    CommsUnmuted, 
    CommsVoiceMessage, 
    FairPlayBlock, 
    FairPlayCheater, 
    FairPlayConsoleBanRequest, 
    FairPlayKicked, 
    FairPlayKillsTeammates, 
    FairPlayQuitter, 
    FairPlayTampering, 
    FairPlayUnblock, 
    FairPlayUserBanRequest, 
    FairplayIdler, 
    FairplayUnsporting, 
    InternalAmbassadorScoreUpdated, 
    InternalEnforcementDataUpdated, 
    InternalReputationReset, 
    InternalReputationUpdated, 
    PositiveHelpfulPlayer, 
    PositiveHighQualityUGC, 
    PositiveSkilledPlayer, 
    Unknown, 
    UserContentActivityFeed, 
    UserContentGameDVR, 
    UserContentGamerpic, 
    UserContentGamertag, 
    UserContentInappropriateUGC, 
    UserContentPersonalInfo, 
    UserContentRealName, 
    UserContentReviewRequest, 
    UserContentScreenshot;
    
    public String getTitle() {
        switch (FeedbackType$1.$SwitchMap$com$microsoft$xbox$service$model$sls$FeedbackType[this.ordinal()]) {
            default: {
                XLEAssert.fail("No title implementation.");
                return "";
            }
            case 8: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_VoiceComm);
            }
            case 7: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_Unsporting);
            }
            case 6: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_QuitEarly);
            }
            case 5: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_PlayerPic);
            }
            case 4: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_PlayerName);
            }
            case 3: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_PlayerName);
            }
            case 2: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_Cheating);
            }
            case 1: {
                return XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_BioLoc);
            }
        }
    }
}
