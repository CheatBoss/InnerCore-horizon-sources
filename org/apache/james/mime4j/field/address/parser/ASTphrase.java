package org.apache.james.mime4j.field.address.parser;

public class ASTphrase extends SimpleNode
{
    public ASTphrase(final int n) {
        super(n);
    }
    
    public ASTphrase(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
