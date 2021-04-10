package org.apache.http.entity.mime.content;

import org.apache.james.mime4j.message.*;
import org.apache.james.mime4j.descriptor.*;

public interface ContentBody extends Body, ContentDescriptor
{
    String getFilename();
}
