package com.microsoft.aad.adal;

import android.content.pm.*;
import android.accounts.*;
import java.io.*;
import android.content.*;

interface IBrokerProxy
{
    BrokerProxy.SwitchToBroker canSwitchToBroker(final String p0);
    
    boolean canUseLocalCache(final String p0);
    
    AuthenticationResult getAuthTokenInBackground(final AuthenticationRequest p0, final BrokerEvent p1) throws AuthenticationException;
    
    String getBrokerAppVersion(final String p0) throws PackageManager$NameNotFoundException;
    
    UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException;
    
    String getCurrentActiveBrokerPackageName();
    
    String getCurrentUser();
    
    Intent getIntentForBrokerActivity(final AuthenticationRequest p0, final BrokerEvent p1) throws AuthenticationException;
    
    void removeAccounts();
    
    void saveAccount(final String p0);
    
    boolean verifyUser(final String p0, final String p1);
}
