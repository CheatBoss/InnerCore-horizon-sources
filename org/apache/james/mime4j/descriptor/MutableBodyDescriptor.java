package org.apache.james.mime4j.descriptor;

import org.apache.james.mime4j.parser.*;

public interface MutableBodyDescriptor extends BodyDescriptor
{
    void addField(final Field p0);
}
