package org.reflections.scanners;

import javassist.bytecode.*;
import java.lang.reflect.*;
import com.google.common.base.*;
import org.reflections.adapters.*;
import java.util.*;

public class MethodParameterNamesScanner extends AbstractScanner
{
    @Override
    public void scan(final Object o) {
        final MetadataAdapter metadataAdapter = this.getMetadataAdapter();
        for (final MethodInfo next : metadataAdapter.getMethods(o)) {
            final String methodFullKey = metadataAdapter.getMethodFullKey(o, next);
            if (this.acceptResult(methodFullKey)) {
                final LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)next.getCodeAttribute().getAttribute("LocalVariableTable");
                final int tableLength = localVariableAttribute.tableLength();
                int i = (Modifier.isStatic(next.getAccessFlags()) ^ true) ? 1 : 0;
                if (i >= tableLength) {
                    continue;
                }
                final ArrayList list = new ArrayList<String>(tableLength - i);
                while (i < tableLength) {
                    list.add(next.getConstPool().getUtf8Info(localVariableAttribute.nameIndex(i)));
                    ++i;
                }
                this.getStore().put((Object)methodFullKey, (Object)Joiner.on(", ").join((Iterable)list));
            }
        }
    }
}
