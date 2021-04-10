package org.apache.james.mime4j.field;

public class DefaultFieldParser extends DelegatingFieldParser
{
    public DefaultFieldParser() {
        this.setFieldParser("Content-Transfer-Encoding", ContentTransferEncodingField.PARSER);
        this.setFieldParser("Content-Type", ContentTypeField.PARSER);
        this.setFieldParser("Content-Disposition", ContentDispositionField.PARSER);
        final FieldParser parser = DateTimeField.PARSER;
        this.setFieldParser("Date", parser);
        this.setFieldParser("Resent-Date", parser);
        final FieldParser parser2 = MailboxListField.PARSER;
        this.setFieldParser("From", parser2);
        this.setFieldParser("Resent-From", parser2);
        final FieldParser parser3 = MailboxField.PARSER;
        this.setFieldParser("Sender", parser3);
        this.setFieldParser("Resent-Sender", parser3);
        final FieldParser parser4 = AddressListField.PARSER;
        this.setFieldParser("To", parser4);
        this.setFieldParser("Resent-To", parser4);
        this.setFieldParser("Cc", parser4);
        this.setFieldParser("Resent-Cc", parser4);
        this.setFieldParser("Bcc", parser4);
        this.setFieldParser("Resent-Bcc", parser4);
        this.setFieldParser("Reply-To", parser4);
    }
}
