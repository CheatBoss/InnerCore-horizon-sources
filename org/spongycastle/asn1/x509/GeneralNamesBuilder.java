package org.spongycastle.asn1.x509;

import java.util.*;

public class GeneralNamesBuilder
{
    private Vector names;
    
    public GeneralNamesBuilder() {
        this.names = new Vector();
    }
    
    public GeneralNamesBuilder addName(final GeneralName generalName) {
        this.names.addElement(generalName);
        return this;
    }
    
    public GeneralNamesBuilder addNames(final GeneralNames generalNames) {
        final GeneralName[] names = generalNames.getNames();
        for (int i = 0; i != names.length; ++i) {
            this.names.addElement(names[i]);
        }
        return this;
    }
    
    public GeneralNames build() {
        final int size = this.names.size();
        final GeneralName[] array = new GeneralName[size];
        for (int i = 0; i != size; ++i) {
            array[i] = (GeneralName)this.names.elementAt(i);
        }
        return new GeneralNames(array);
    }
}
