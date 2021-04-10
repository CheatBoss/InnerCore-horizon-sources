package com.microsoft.xbox.idp.services;

public class EndpointsFactory
{
    public static Endpoints get() {
        final int n = EndpointsFactory$1.$SwitchMap$com$microsoft$xbox$idp$services$Endpoints$Type[Config.endpointType.ordinal()];
        if (n == 1) {
            return new EndpointsProd();
        }
        if (n != 2) {
            return null;
        }
        return new EndpointsDnet();
    }
}
