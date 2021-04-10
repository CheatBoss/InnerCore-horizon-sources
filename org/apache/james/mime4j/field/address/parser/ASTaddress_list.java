package org.apache.james.mime4j.field.address.parser;

public class ASTaddress_list extends SimpleNode
{
    public ASTaddress_list(final int n) {
        super(n);
    }
    
    public ASTaddress_list(final AddressListParser addressListParser, final int n) {
        super(addressListParser, n);
    }
    
    @Override
    public Object jjtAccept(final AddressListParserVisitor addressListParserVisitor, final Object o) {
        return addressListParserVisitor.visit(this, o);
    }
}
