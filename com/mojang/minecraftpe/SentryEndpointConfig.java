package com.mojang.minecraftpe;

public class SentryEndpointConfig
{
    public String projectId;
    public String publicKey;
    public String url;
    
    public SentryEndpointConfig(final String url, final String projectId, final String publicKey) {
        this.url = url;
        this.projectId = projectId;
        this.publicKey = publicKey;
    }
}
