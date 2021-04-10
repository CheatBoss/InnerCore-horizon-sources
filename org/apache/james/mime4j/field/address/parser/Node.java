package org.apache.james.mime4j.field.address.parser;

public interface Node
{
    Object jjtAccept(final AddressListParserVisitor p0, final Object p1);
    
    void jjtAddChild(final Node p0, final int p1);
    
    void jjtClose();
    
    Node jjtGetChild(final int p0);
    
    int jjtGetNumChildren();
    
    Node jjtGetParent();
    
    void jjtOpen();
    
    void jjtSetParent(final Node p0);
}
