package org.apache.james.mime4j.field;

import org.apache.james.mime4j.util.*;

public interface FieldParser
{
    ParsedField parse(final String p0, final String p1, final ByteSequence p2);
}
