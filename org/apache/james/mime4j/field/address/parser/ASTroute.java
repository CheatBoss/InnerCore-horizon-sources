package org.apache.james.mime4j.field.address.parser;

public class ASTroute extends SimpleNode
{
    public ASTroute(final int n) {
        super(n);
    }
    
    public ASTroute(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
