package org.apache.james.mime4j.field.address.parser;

public class ASTname_addr extends SimpleNode
{
    public ASTname_addr(final int n) {
        super(n);
    }
    
    public ASTname_addr(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
