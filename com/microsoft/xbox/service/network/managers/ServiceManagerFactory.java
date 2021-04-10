package com.microsoft.xbox.service.network.managers;

import com.microsoft.xbox.service.network.managers.xblshared.*;

public class ServiceManagerFactory
{
    private static ServiceManagerFactory instance;
    private ISLSServiceManager slsServiceManager;
    
    static {
        ServiceManagerFactory.instance = new ServiceManagerFactory();
    }
    
    private ServiceManagerFactory() {
    }
    
    public static ServiceManagerFactory getInstance() {
        return ServiceManagerFactory.instance;
    }
    
    public ISLSServiceManager getSLSServiceManager() {
        if (this.slsServiceManager == null) {
            this.slsServiceManager = new SLSXsapiServiceManager();
        }
        return this.slsServiceManager;
    }
}
