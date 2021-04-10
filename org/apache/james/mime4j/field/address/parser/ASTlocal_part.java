package org.apache.james.mime4j.field.address.parser;

public class ASTlocal_part extends SimpleNode
{
    public ASTlocal_part(final int n) {
        super(n);
    }
    
    public ASTlocal_part(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
