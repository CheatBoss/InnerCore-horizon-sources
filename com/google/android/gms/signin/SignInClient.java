package com.google.android.gms.signin;

import com.google.android.gms.common.api.*;
import com.google.android.gms.signin.internal.*;

public interface SignInClient extends Client
{
    void connect();
    
    void signIn(final ISignInCallbacks p0);
}
