package org.reflections.scanners;

import org.reflections.vfs.*;

@Deprecated
public class TypesScanner extends AbstractScanner
{
    @Override
    public Object scan(final Vfs.File file, final Object o) {
        final Object scan = super.scan(file, o);
        final String className = this.getMetadataAdapter().getClassName(scan);
        this.getStore().put((Object)className, (Object)className);
        return scan;
    }
    
    @Override
    public void scan(final Object o) {
        throw new UnsupportedOperationException("should not get here");
    }
}
