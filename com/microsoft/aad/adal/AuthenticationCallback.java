package com.microsoft.aad.adal;

public interface AuthenticationCallback<T>
{
    void onError(final Exception p0);
    
    void onSuccess(final T p0);
}
