package org.apache.james.mime4j.field.address.parser;

public class ASTaddr_spec extends SimpleNode
{
    public ASTaddr_spec(final int n) {
        super(n);
    }
    
    public ASTaddr_spec(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
