package com.google.firebase.messaging;

import java.util.*;

public final class SendException extends Exception
{
    private final int errorCode;
    
    SendException(String lowerCase) {
        super(lowerCase);
        int errorCode;
        final int n = errorCode = 0;
        if (lowerCase != null) {
            lowerCase = lowerCase.toLowerCase(Locale.US);
            int n2 = -1;
            switch (lowerCase.hashCode()) {
                case -95047692: {
                    if (lowerCase.equals("missing_to")) {
                        n2 = 1;
                        break;
                    }
                    break;
                }
                case -617027085: {
                    if (lowerCase.equals("messagetoobig")) {
                        n2 = 2;
                        break;
                    }
                    break;
                }
                case -920906446: {
                    if (lowerCase.equals("invalid_parameters")) {
                        n2 = 0;
                        break;
                    }
                    break;
                }
                case -1290953729: {
                    if (lowerCase.equals("toomanymessages")) {
                        n2 = 4;
                        break;
                    }
                    break;
                }
                case -1743242157: {
                    if (lowerCase.equals("service_not_available")) {
                        n2 = 3;
                        break;
                    }
                    break;
                }
            }
            if (n2 != 0 && n2 != 1) {
                if (n2 != 2) {
                    if (n2 != 3) {
                        if (n2 != 4) {
                            errorCode = n;
                        }
                        else {
                            errorCode = 4;
                        }
                    }
                    else {
                        errorCode = 3;
                    }
                }
                else {
                    errorCode = 2;
                }
            }
            else {
                errorCode = 1;
            }
        }
        this.errorCode = errorCode;
    }
}
