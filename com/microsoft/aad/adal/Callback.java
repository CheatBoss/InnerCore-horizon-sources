package com.microsoft.aad.adal;

interface Callback<T>
{
    void onError(final Throwable p0);
    
    void onSuccess(final T p0);
}
