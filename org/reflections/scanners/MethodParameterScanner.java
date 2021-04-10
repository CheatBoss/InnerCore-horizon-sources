package org.reflections.scanners;

import org.reflections.adapters.*;
import java.util.*;

public class MethodParameterScanner extends AbstractScanner
{
    @Override
    public void scan(final Object o) {
        final MetadataAdapter metadataAdapter = this.getMetadataAdapter();
        for (final Object next : metadataAdapter.getMethods(o)) {
            final String string = metadataAdapter.getParameterNames(next).toString();
            if (this.acceptResult(string)) {
                this.getStore().put((Object)string, (Object)metadataAdapter.getMethodFullKey(o, next));
            }
            final String returnTypeName = metadataAdapter.getReturnTypeName(next);
            if (this.acceptResult(returnTypeName)) {
                this.getStore().put((Object)returnTypeName, (Object)metadataAdapter.getMethodFullKey(o, next));
            }
            final List<String> parameterNames = metadataAdapter.getParameterNames(next);
            for (int i = 0; i < parameterNames.size(); ++i) {
                for (final String next2 : metadataAdapter.getParameterAnnotationNames(next, i)) {
                    if (this.acceptResult(next2)) {
                        this.getStore().put((Object)next2, (Object)metadataAdapter.getMethodFullKey(o, next));
                    }
                }
            }
        }
    }
}
