package org.reflections.scanners;

import java.util.*;

public class MethodAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object o) {
        for (final Object next : this.getMetadataAdapter().getMethods(o)) {
            for (final String s : this.getMetadataAdapter().getMethodAnnotationNames(next)) {
                if (this.acceptResult(s)) {
                    this.getStore().put((Object)s, (Object)this.getMetadataAdapter().getMethodFullKey(o, next));
                }
            }
        }
    }
}
