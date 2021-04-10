package org.apache.james.mime4j.field.address.parser;

public class ASTgroup_body extends SimpleNode
{
    public ASTgroup_body(final int n) {
        super(n);
    }
    
    public ASTgroup_body(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
