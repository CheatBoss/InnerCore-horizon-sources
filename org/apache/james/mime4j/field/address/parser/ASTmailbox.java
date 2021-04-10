package org.apache.james.mime4j.field.address.parser;

public class ASTmailbox extends SimpleNode
{
    public ASTmailbox(final int n) {
        super(n);
    }
    
    public ASTmailbox(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
