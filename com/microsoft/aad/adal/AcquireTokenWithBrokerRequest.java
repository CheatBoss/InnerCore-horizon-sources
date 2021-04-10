package com.microsoft.aad.adal;

import android.content.pm.*;
import android.os.*;
import android.content.*;

final class AcquireTokenWithBrokerRequest
{
    private static final String TAG;
    private final AuthenticationRequest mAuthRequest;
    private final IBrokerProxy mBrokerProxy;
    
    static {
        TAG = AcquireTokenWithBrokerRequest.class.getSimpleName();
    }
    
    AcquireTokenWithBrokerRequest(final AuthenticationRequest mAuthRequest, final IBrokerProxy mBrokerProxy) {
        this.mAuthRequest = mAuthRequest;
        this.mBrokerProxy = mBrokerProxy;
    }
    
    private void logBrokerVersion(final BrokerEvent brokerEvent) {
        final String currentActiveBrokerPackageName = this.mBrokerProxy.getCurrentActiveBrokerPackageName();
        if (StringExtensions.isNullOrBlank(currentActiveBrokerPackageName)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenWithBrokerRequest.TAG);
            sb.append(":logBrokerVersion");
            Logger.i(sb.toString(), "Broker app package name is empty.", "");
            return;
        }
        brokerEvent.setBrokerAppName(currentActiveBrokerPackageName);
        String brokerAppVersion;
        try {
            brokerAppVersion = this.mBrokerProxy.getBrokerAppVersion(currentActiveBrokerPackageName);
        }
        catch (PackageManager$NameNotFoundException ex) {
            brokerAppVersion = "N/A";
        }
        brokerEvent.setBrokerAppVersion(brokerAppVersion);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Broker app is: ");
        sb2.append(currentActiveBrokerPackageName);
        sb2.append(";Broker app version: ");
        sb2.append(brokerAppVersion);
        final String string = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(AcquireTokenWithBrokerRequest.TAG);
        sb3.append(":logBrokerVersion");
        Logger.i(sb3.toString(), string, "");
    }
    
    private BrokerEvent startBrokerTelemetryRequest(final String s) {
        final BrokerEvent brokerEvent = new BrokerEvent(s);
        brokerEvent.setRequestId(this.mAuthRequest.getTelemetryRequestId());
        Telemetry.getInstance().startEvent(this.mAuthRequest.getTelemetryRequestId(), s);
        return brokerEvent;
    }
    
    void acquireTokenWithBrokerInteractively(final IWindowComponent windowComponent) throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenWithBrokerRequest.TAG);
        sb.append(":acquireTokenWithBrokerInteractively");
        Logger.v(sb.toString(), "Launch activity for interactive authentication via broker.");
        final BrokerEvent startBrokerTelemetryRequest = this.startBrokerTelemetryRequest("Microsoft.ADAL.broker_request_interactive");
        this.logBrokerVersion(startBrokerTelemetryRequest);
        final Intent intentForBrokerActivity = this.mBrokerProxy.getIntentForBrokerActivity(this.mAuthRequest, startBrokerTelemetryRequest);
        if (windowComponent == null) {
            throw new AuthenticationException(ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
        }
        if (intentForBrokerActivity != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenWithBrokerRequest.TAG);
            sb2.append(":acquireTokenWithBrokerInteractively");
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Calling activity. Pid:");
            sb3.append(Process.myPid());
            sb3.append(" tid:");
            sb3.append(Process.myTid());
            sb3.append("uid:");
            sb3.append(Process.myUid());
            Logger.v(string, sb3.toString());
            Telemetry.getInstance().stopEvent(startBrokerTelemetryRequest.getTelemetryRequestId(), startBrokerTelemetryRequest, "Microsoft.ADAL.broker_request_interactive");
            windowComponent.startActivityForResult(intentForBrokerActivity, 1001);
            return;
        }
        throw new AuthenticationException(ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
    }
    
    AuthenticationResult acquireTokenWithBrokerSilent() throws AuthenticationException {
        this.mAuthRequest.setVersion(AuthenticationContext.getVersionName());
        final AuthenticationRequest mAuthRequest = this.mAuthRequest;
        mAuthRequest.setBrokerAccountName(mAuthRequest.getLoginHint());
        final BrokerEvent startBrokerTelemetryRequest = this.startBrokerTelemetryRequest("Microsoft.ADAL.broker_request_silent");
        this.logBrokerVersion(startBrokerTelemetryRequest);
        AuthenticationResult authenticationResult;
        if (StringExtensions.isNullOrBlank(this.mAuthRequest.getBrokerAccountName()) && StringExtensions.isNullOrBlank(this.mAuthRequest.getUserId())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenWithBrokerRequest.TAG);
            sb.append(":acquireTokenWithBrokerSilent");
            Logger.v(sb.toString(), "User is not specified, skipping background(silent) token request.");
            authenticationResult = null;
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenWithBrokerRequest.TAG);
            sb2.append(":acquireTokenWithBrokerSilent");
            Logger.v(sb2.toString(), "User is specified for background(silent) token request, trying to acquire token silently.");
            final AuthenticationResult authTokenInBackground = this.mBrokerProxy.getAuthTokenInBackground(this.mAuthRequest, startBrokerTelemetryRequest);
            if ((authenticationResult = authTokenInBackground) != null) {
                authenticationResult = authTokenInBackground;
                if (authTokenInBackground.getCliTelemInfo() != null) {
                    startBrokerTelemetryRequest.setSpeRing(authTokenInBackground.getCliTelemInfo().getSpeRing());
                    authenticationResult = authTokenInBackground;
                }
            }
        }
        Telemetry.getInstance().stopEvent(startBrokerTelemetryRequest.getTelemetryRequestId(), startBrokerTelemetryRequest, "Microsoft.ADAL.broker_request_silent");
        return authenticationResult;
    }
}
