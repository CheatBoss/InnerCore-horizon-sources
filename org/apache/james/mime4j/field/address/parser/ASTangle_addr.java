package org.apache.james.mime4j.field.address.parser;

public class ASTangle_addr extends SimpleNode
{
    public ASTangle_addr(final int n) {
        super(n);
    }
    
    public ASTangle_addr(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
