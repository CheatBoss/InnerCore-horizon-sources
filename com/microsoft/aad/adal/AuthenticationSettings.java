package com.microsoft.aad.adal;

import java.util.concurrent.atomic.*;

public enum AuthenticationSettings
{
    private static final int DEFAULT_EXPIRATION_BUFFER = 300;
    private static final int DEFAULT_READ_CONNECT_TIMEOUT = 30000;
    
    INSTANCE;
    
    private static final int SECRET_RAW_KEY_LENGTH = 32;
    private String mActivityPackageName;
    private String mBrokerPackageName;
    private String mBrokerSignature;
    private Class<?> mClazzDeviceCertProxy;
    private int mConnectTimeOut;
    private boolean mEnableHardwareAcceleration;
    private int mExpirationBuffer;
    private int mReadTimeOut;
    private AtomicReference<byte[]> mSecretKeyData;
    private String mSharedPrefPackageName;
    private boolean mUseBroker;
    
    private AuthenticationSettings() {
        this.mSecretKeyData = new AtomicReference<byte[]>();
        this.mBrokerPackageName = "com.microsoft.windowsintune.companyportal";
        this.mBrokerSignature = "1L4Z9FJCgn5c0VLhyAxC5O9LdlE=";
        this.mEnableHardwareAcceleration = true;
        this.mUseBroker = false;
        this.mExpirationBuffer = 300;
        this.mConnectTimeOut = 30000;
        this.mReadTimeOut = 30000;
    }
    
    public String getActivityPackageName() {
        return this.mActivityPackageName;
    }
    
    public String getBrokerPackageName() {
        return this.mBrokerPackageName;
    }
    
    public String getBrokerSignature() {
        return this.mBrokerSignature;
    }
    
    public int getConnectTimeOut() {
        return this.mConnectTimeOut;
    }
    
    public Class<?> getDeviceCertificateProxy() {
        return this.mClazzDeviceCertProxy;
    }
    
    public boolean getDisableWebViewHardwareAcceleration() {
        return this.mEnableHardwareAcceleration;
    }
    
    public int getExpirationBuffer() {
        return this.mExpirationBuffer;
    }
    
    public int getReadTimeOut() {
        return this.mReadTimeOut;
    }
    
    public byte[] getSecretKeyData() {
        return this.mSecretKeyData.get();
    }
    
    public String getSharedPrefPackageName() {
        return this.mSharedPrefPackageName;
    }
    
    @Deprecated
    public boolean getSkipBroker() {
        return this.mUseBroker ^ true;
    }
    
    public boolean getUseBroker() {
        return this.mUseBroker;
    }
    
    public void setActivityPackageName(final String mActivityPackageName) {
        if (!StringExtensions.isNullOrBlank(mActivityPackageName)) {
            this.mActivityPackageName = mActivityPackageName;
            return;
        }
        throw new IllegalArgumentException("activityPackageName cannot be empty or null");
    }
    
    public void setBrokerPackageName(final String mBrokerPackageName) {
        if (!StringExtensions.isNullOrBlank(mBrokerPackageName)) {
            this.mBrokerPackageName = mBrokerPackageName;
            return;
        }
        throw new IllegalArgumentException("packageName cannot be empty or null");
    }
    
    public void setBrokerSignature(final String mBrokerSignature) {
        if (!StringExtensions.isNullOrBlank(mBrokerSignature)) {
            this.mBrokerSignature = mBrokerSignature;
            return;
        }
        throw new IllegalArgumentException("brokerSignature cannot be empty or null");
    }
    
    public void setConnectTimeOut(final int mConnectTimeOut) {
        if (mConnectTimeOut >= 0) {
            this.mConnectTimeOut = mConnectTimeOut;
            return;
        }
        throw new IllegalArgumentException("Invalid timeOutMillis");
    }
    
    public void setDeviceCertificateProxyClass(final Class mClazzDeviceCertProxy) {
        if (IDeviceCertificate.class.isAssignableFrom(mClazzDeviceCertProxy)) {
            this.mClazzDeviceCertProxy = (Class<?>)mClazzDeviceCertProxy;
            return;
        }
        throw new IllegalArgumentException("clazz");
    }
    
    public void setDisableWebViewHardwareAcceleration(final boolean mEnableHardwareAcceleration) {
        this.mEnableHardwareAcceleration = mEnableHardwareAcceleration;
    }
    
    public void setExpirationBuffer(final int mExpirationBuffer) {
        this.mExpirationBuffer = mExpirationBuffer;
    }
    
    public void setReadTimeOut(final int mReadTimeOut) {
        if (mReadTimeOut >= 0) {
            this.mReadTimeOut = mReadTimeOut;
            return;
        }
        throw new IllegalArgumentException("Invalid timeOutMillis");
    }
    
    public void setSecretKey(final byte[] array) {
        if (array != null && array.length == 32) {
            this.mSecretKeyData.set(array);
            return;
        }
        throw new IllegalArgumentException("rawKey");
    }
    
    public void setSharedPrefPackageName(final String mSharedPrefPackageName) {
        this.mSharedPrefPackageName = mSharedPrefPackageName;
    }
    
    @Deprecated
    public void setSkipBroker(final boolean b) {
        this.mUseBroker = (b ^ true);
    }
    
    public void setUseBroker(final boolean mUseBroker) {
        this.mUseBroker = mUseBroker;
    }
}
