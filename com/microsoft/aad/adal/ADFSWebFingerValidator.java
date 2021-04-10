package com.microsoft.aad.adal;

import java.net.*;
import java.util.*;

final class ADFSWebFingerValidator
{
    private static final String TAG;
    private static final URI TRUSTED_REALM_REL;
    
    static {
        TAG = ADFSWebFingerValidator.class.getSimpleName();
        try {
            TRUSTED_REALM_REL = new URI("http://schemas.microsoft.com/rel/trusted-realm");
        }
        catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private ADFSWebFingerValidator() {
    }
    
    static boolean realmIsTrusted(final URI uri, WebFingerMetadata iterator) {
        if (uri == null) {
            throw new IllegalArgumentException("Authority cannot be null");
        }
        if (iterator != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(ADFSWebFingerValidator.TAG);
            sb.append(":realmIsTrusted");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(uri.toString());
            sb2.append(iterator.toString());
            Logger.v(string, "Verifying trust authority. ", sb2.toString(), null);
            if (iterator.getLinks() != null) {
                iterator = (WebFingerMetadata)iterator.getLinks().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final Link link = ((Iterator<Link>)iterator).next();
                    try {
                        final URI uri2 = new URI(link.getHref());
                        final URI uri3 = new URI(link.getRel());
                        if (uri2.getScheme().equalsIgnoreCase(uri.getScheme()) && uri2.getAuthority().equalsIgnoreCase(uri.getAuthority()) && uri3.equals(ADFSWebFingerValidator.TRUSTED_REALM_REL)) {
                            return true;
                        }
                        continue;
                    }
                    catch (URISyntaxException ex) {
                        continue;
                    }
                    break;
                }
            }
            return false;
        }
        throw new IllegalArgumentException("WebFingerMetadata cannot be null");
    }
}
