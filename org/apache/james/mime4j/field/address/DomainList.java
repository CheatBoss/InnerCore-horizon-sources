package org.apache.james.mime4j.field.address;

import java.io.*;
import java.util.*;

public class DomainList extends AbstractList<String> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<String> domains;
    
    public DomainList(List<String> domains, final boolean b) {
        if (domains != null) {
            if (!b) {
                domains = new ArrayList<String>(domains);
            }
            this.domains = domains;
            return;
        }
        this.domains = Collections.emptyList();
    }
    
    @Override
    public String get(final int n) {
        return this.domains.get(n);
    }
    
    @Override
    public int size() {
        return this.domains.size();
    }
    
    public String toRouteString() {
        final StringBuilder sb = new StringBuilder();
        for (final String s : this.domains) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append("@");
            sb.append(s);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.toRouteString();
    }
}
