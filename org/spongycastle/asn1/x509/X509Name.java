package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.pkcs.*;
import java.util.*;
import org.spongycastle.util.encoders.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class X509Name extends ASN1Object
{
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier C;
    public static final ASN1ObjectIdentifier CN;
    public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
    public static final ASN1ObjectIdentifier DC;
    public static final ASN1ObjectIdentifier DMD_NAME;
    public static final ASN1ObjectIdentifier DN_QUALIFIER;
    public static final Hashtable DefaultLookUp;
    public static boolean DefaultReverse;
    public static final Hashtable DefaultSymbols;
    public static final ASN1ObjectIdentifier E;
    public static final ASN1ObjectIdentifier EmailAddress;
    private static final Boolean FALSE;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final ASN1ObjectIdentifier L;
    public static final ASN1ObjectIdentifier NAME;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier O;
    public static final Hashtable OIDLookUp;
    public static final ASN1ObjectIdentifier OU;
    public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
    public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
    public static final ASN1ObjectIdentifier POSTAL_CODE;
    public static final ASN1ObjectIdentifier PSEUDONYM;
    public static final Hashtable RFC1779Symbols;
    public static final Hashtable RFC2253Symbols;
    public static final ASN1ObjectIdentifier SERIALNUMBER;
    public static final ASN1ObjectIdentifier SN;
    public static final ASN1ObjectIdentifier ST;
    public static final ASN1ObjectIdentifier STREET;
    public static final ASN1ObjectIdentifier SURNAME;
    public static final Hashtable SymbolLookUp;
    public static final ASN1ObjectIdentifier T;
    public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
    private static final Boolean TRUE;
    public static final ASN1ObjectIdentifier UID;
    public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
    public static final ASN1ObjectIdentifier UnstructuredAddress;
    public static final ASN1ObjectIdentifier UnstructuredName;
    private Vector added;
    private X509NameEntryConverter converter;
    private int hashCodeValue;
    private boolean isHashCodeCalculated;
    private Vector ordering;
    private ASN1Sequence seq;
    private Vector values;
    
    static {
        C = new ASN1ObjectIdentifier("2.5.4.6");
        O = new ASN1ObjectIdentifier("2.5.4.10");
        OU = new ASN1ObjectIdentifier("2.5.4.11");
        T = new ASN1ObjectIdentifier("2.5.4.12");
        CN = new ASN1ObjectIdentifier("2.5.4.3");
        SN = new ASN1ObjectIdentifier("2.5.4.5");
        STREET = new ASN1ObjectIdentifier("2.5.4.9");
        SERIALNUMBER = X509Name.SN;
        L = new ASN1ObjectIdentifier("2.5.4.7");
        ST = new ASN1ObjectIdentifier("2.5.4.8");
        SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
        GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
        INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
        GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
        UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
        BUSINESS_CATEGORY = new ASN1ObjectIdentifier("2.5.4.15");
        POSTAL_CODE = new ASN1ObjectIdentifier("2.5.4.17");
        DN_QUALIFIER = new ASN1ObjectIdentifier("2.5.4.46");
        PSEUDONYM = new ASN1ObjectIdentifier("2.5.4.65");
        DATE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.1");
        PLACE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.2");
        GENDER = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.3");
        COUNTRY_OF_CITIZENSHIP = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.4");
        COUNTRY_OF_RESIDENCE = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.5");
        NAME_AT_BIRTH = new ASN1ObjectIdentifier("1.3.36.8.3.14");
        POSTAL_ADDRESS = new ASN1ObjectIdentifier("2.5.4.16");
        DMD_NAME = new ASN1ObjectIdentifier("2.5.4.54");
        TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
        NAME = X509ObjectIdentifiers.id_at_name;
        EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
        UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
        UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
        E = X509Name.EmailAddress;
        DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        X509Name.DefaultReverse = false;
        DefaultSymbols = new Hashtable();
        RFC2253Symbols = new Hashtable();
        RFC1779Symbols = new Hashtable();
        final Hashtable symbolLookUp = DefaultLookUp = new Hashtable();
        OIDLookUp = X509Name.DefaultSymbols;
        SymbolLookUp = symbolLookUp;
        TRUE = new Boolean(true);
        FALSE = new Boolean(false);
        X509Name.DefaultSymbols.put(X509Name.C, "C");
        X509Name.DefaultSymbols.put(X509Name.O, "O");
        X509Name.DefaultSymbols.put(X509Name.T, "T");
        X509Name.DefaultSymbols.put(X509Name.OU, "OU");
        X509Name.DefaultSymbols.put(X509Name.CN, "CN");
        X509Name.DefaultSymbols.put(X509Name.L, "L");
        X509Name.DefaultSymbols.put(X509Name.ST, "ST");
        X509Name.DefaultSymbols.put(X509Name.SN, "SERIALNUMBER");
        X509Name.DefaultSymbols.put(X509Name.EmailAddress, "E");
        X509Name.DefaultSymbols.put(X509Name.DC, "DC");
        X509Name.DefaultSymbols.put(X509Name.UID, "UID");
        X509Name.DefaultSymbols.put(X509Name.STREET, "STREET");
        X509Name.DefaultSymbols.put(X509Name.SURNAME, "SURNAME");
        X509Name.DefaultSymbols.put(X509Name.GIVENNAME, "GIVENNAME");
        X509Name.DefaultSymbols.put(X509Name.INITIALS, "INITIALS");
        X509Name.DefaultSymbols.put(X509Name.GENERATION, "GENERATION");
        X509Name.DefaultSymbols.put(X509Name.UnstructuredAddress, "unstructuredAddress");
        X509Name.DefaultSymbols.put(X509Name.UnstructuredName, "unstructuredName");
        X509Name.DefaultSymbols.put(X509Name.UNIQUE_IDENTIFIER, "UniqueIdentifier");
        X509Name.DefaultSymbols.put(X509Name.DN_QUALIFIER, "DN");
        X509Name.DefaultSymbols.put(X509Name.PSEUDONYM, "Pseudonym");
        X509Name.DefaultSymbols.put(X509Name.POSTAL_ADDRESS, "PostalAddress");
        X509Name.DefaultSymbols.put(X509Name.NAME_AT_BIRTH, "NameAtBirth");
        X509Name.DefaultSymbols.put(X509Name.COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
        X509Name.DefaultSymbols.put(X509Name.COUNTRY_OF_RESIDENCE, "CountryOfResidence");
        X509Name.DefaultSymbols.put(X509Name.GENDER, "Gender");
        X509Name.DefaultSymbols.put(X509Name.PLACE_OF_BIRTH, "PlaceOfBirth");
        X509Name.DefaultSymbols.put(X509Name.DATE_OF_BIRTH, "DateOfBirth");
        X509Name.DefaultSymbols.put(X509Name.POSTAL_CODE, "PostalCode");
        X509Name.DefaultSymbols.put(X509Name.BUSINESS_CATEGORY, "BusinessCategory");
        X509Name.DefaultSymbols.put(X509Name.TELEPHONE_NUMBER, "TelephoneNumber");
        X509Name.DefaultSymbols.put(X509Name.NAME, "Name");
        X509Name.RFC2253Symbols.put(X509Name.C, "C");
        X509Name.RFC2253Symbols.put(X509Name.O, "O");
        X509Name.RFC2253Symbols.put(X509Name.OU, "OU");
        X509Name.RFC2253Symbols.put(X509Name.CN, "CN");
        X509Name.RFC2253Symbols.put(X509Name.L, "L");
        X509Name.RFC2253Symbols.put(X509Name.ST, "ST");
        X509Name.RFC2253Symbols.put(X509Name.STREET, "STREET");
        X509Name.RFC2253Symbols.put(X509Name.DC, "DC");
        X509Name.RFC2253Symbols.put(X509Name.UID, "UID");
        X509Name.RFC1779Symbols.put(X509Name.C, "C");
        X509Name.RFC1779Symbols.put(X509Name.O, "O");
        X509Name.RFC1779Symbols.put(X509Name.OU, "OU");
        X509Name.RFC1779Symbols.put(X509Name.CN, "CN");
        X509Name.RFC1779Symbols.put(X509Name.L, "L");
        X509Name.RFC1779Symbols.put(X509Name.ST, "ST");
        X509Name.RFC1779Symbols.put(X509Name.STREET, "STREET");
        X509Name.DefaultLookUp.put("c", X509Name.C);
        X509Name.DefaultLookUp.put("o", X509Name.O);
        X509Name.DefaultLookUp.put("t", X509Name.T);
        X509Name.DefaultLookUp.put("ou", X509Name.OU);
        X509Name.DefaultLookUp.put("cn", X509Name.CN);
        X509Name.DefaultLookUp.put("l", X509Name.L);
        X509Name.DefaultLookUp.put("st", X509Name.ST);
        X509Name.DefaultLookUp.put("sn", X509Name.SN);
        X509Name.DefaultLookUp.put("serialnumber", X509Name.SN);
        X509Name.DefaultLookUp.put("street", X509Name.STREET);
        X509Name.DefaultLookUp.put("emailaddress", X509Name.E);
        X509Name.DefaultLookUp.put("dc", X509Name.DC);
        X509Name.DefaultLookUp.put("e", X509Name.E);
        X509Name.DefaultLookUp.put("uid", X509Name.UID);
        X509Name.DefaultLookUp.put("surname", X509Name.SURNAME);
        X509Name.DefaultLookUp.put("givenname", X509Name.GIVENNAME);
        X509Name.DefaultLookUp.put("initials", X509Name.INITIALS);
        X509Name.DefaultLookUp.put("generation", X509Name.GENERATION);
        X509Name.DefaultLookUp.put("unstructuredaddress", X509Name.UnstructuredAddress);
        X509Name.DefaultLookUp.put("unstructuredname", X509Name.UnstructuredName);
        X509Name.DefaultLookUp.put("uniqueidentifier", X509Name.UNIQUE_IDENTIFIER);
        X509Name.DefaultLookUp.put("dn", X509Name.DN_QUALIFIER);
        X509Name.DefaultLookUp.put("pseudonym", X509Name.PSEUDONYM);
        X509Name.DefaultLookUp.put("postaladdress", X509Name.POSTAL_ADDRESS);
        X509Name.DefaultLookUp.put("nameofbirth", X509Name.NAME_AT_BIRTH);
        X509Name.DefaultLookUp.put("countryofcitizenship", X509Name.COUNTRY_OF_CITIZENSHIP);
        X509Name.DefaultLookUp.put("countryofresidence", X509Name.COUNTRY_OF_RESIDENCE);
        X509Name.DefaultLookUp.put("gender", X509Name.GENDER);
        X509Name.DefaultLookUp.put("placeofbirth", X509Name.PLACE_OF_BIRTH);
        X509Name.DefaultLookUp.put("dateofbirth", X509Name.DATE_OF_BIRTH);
        X509Name.DefaultLookUp.put("postalcode", X509Name.POSTAL_CODE);
        X509Name.DefaultLookUp.put("businesscategory", X509Name.BUSINESS_CATEGORY);
        X509Name.DefaultLookUp.put("telephonenumber", X509Name.TELEPHONE_NUMBER);
        X509Name.DefaultLookUp.put("name", X509Name.NAME);
    }
    
    protected X509Name() {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
    }
    
    public X509Name(final String s) {
        this(X509Name.DefaultReverse, X509Name.DefaultLookUp, s);
    }
    
    public X509Name(final String s, final X509NameEntryConverter x509NameEntryConverter) {
        this(X509Name.DefaultReverse, X509Name.DefaultLookUp, s, x509NameEntryConverter);
    }
    
    public X509Name(final Hashtable hashtable) {
        this(null, hashtable);
    }
    
    public X509Name(final Vector vector, final Hashtable hashtable) {
        this(vector, hashtable, new X509DefaultEntryConverter());
    }
    
    public X509Name(final Vector vector, final Hashtable hashtable, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        final boolean b = false;
        int i;
        if (vector != null) {
            int n = 0;
            while (true) {
                i = (b ? 1 : 0);
                if (n == vector.size()) {
                    break;
                }
                this.ordering.addElement(vector.elementAt(n));
                this.added.addElement(X509Name.FALSE);
                ++n;
            }
        }
        else {
            final Enumeration<Object> keys = hashtable.keys();
            while (true) {
                i = (b ? 1 : 0);
                if (!keys.hasMoreElements()) {
                    break;
                }
                this.ordering.addElement(keys.nextElement());
                this.added.addElement(X509Name.FALSE);
            }
        }
        while (i != this.ordering.size()) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = this.ordering.elementAt(i);
            if (hashtable.get(asn1ObjectIdentifier) == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("No attribute for object id - ");
                sb.append(asn1ObjectIdentifier.getId());
                sb.append(" - passed to distinguished name");
                throw new IllegalArgumentException(sb.toString());
            }
            this.values.addElement(hashtable.get(asn1ObjectIdentifier));
            ++i;
        }
    }
    
    public X509Name(final Vector vector, final Vector vector2) {
        this(vector, vector2, new X509DefaultEntryConverter());
    }
    
    public X509Name(final Vector vector, final Vector vector2, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        if (vector.size() == vector2.size()) {
            for (int i = 0; i < vector.size(); ++i) {
                this.ordering.addElement(vector.elementAt(i));
                this.values.addElement(vector2.elementAt(i));
                this.added.addElement(X509Name.FALSE);
            }
            return;
        }
        throw new IllegalArgumentException("oids vector must be same length as values.");
    }
    
    public X509Name(ASN1Sequence instance) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.seq = instance;
        final Enumeration objects = instance.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Set instance2 = ASN1Set.getInstance(objects.nextElement().toASN1Primitive());
            int i = 0;
            while (i < instance2.size()) {
                instance = ASN1Sequence.getInstance(instance2.getObjectAt(i).toASN1Primitive());
                if (instance.size() == 2) {
                    this.ordering.addElement(ASN1ObjectIdentifier.getInstance(instance.getObjectAt(0)));
                    final ASN1Encodable object = instance.getObjectAt(1);
                    Label_0288: {
                        if (object instanceof ASN1String && !(object instanceof DERUniversalString)) {
                            String s = ((ASN1String)object).getString();
                            Vector vector;
                            if (s.length() > 0 && s.charAt(0) == '#') {
                                vector = this.values;
                                final StringBuilder sb = new StringBuilder();
                                sb.append("\\");
                                sb.append(s);
                                s = sb.toString();
                            }
                            else {
                                vector = this.values;
                            }
                            vector.addElement(s);
                            break Label_0288;
                        }
                        try {
                            final Vector values = this.values;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("#");
                            sb2.append(this.bytesToString(Hex.encode(object.toASN1Primitive().getEncoded("DER"))));
                            values.addElement(sb2.toString());
                            final Vector added = this.added;
                            Boolean b;
                            if (i != 0) {
                                b = X509Name.TRUE;
                            }
                            else {
                                b = X509Name.FALSE;
                            }
                            added.addElement(b);
                            ++i;
                            continue;
                        }
                        catch (IOException ex) {
                            throw new IllegalArgumentException("cannot encode value");
                        }
                    }
                }
                throw new IllegalArgumentException("badly sized pair");
            }
        }
    }
    
    public X509Name(final boolean b, final String s) {
        this(b, X509Name.DefaultLookUp, s);
    }
    
    public X509Name(final boolean b, final String s, final X509NameEntryConverter x509NameEntryConverter) {
        this(b, X509Name.DefaultLookUp, s, x509NameEntryConverter);
    }
    
    public X509Name(final boolean b, final Hashtable hashtable, final String s) {
        this(b, hashtable, s, new X509DefaultEntryConverter());
    }
    
    public X509Name(final boolean b, final Hashtable hashtable, String nextToken, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        final X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(nextToken);
        while (x509NameTokenizer.hasMoreTokens()) {
            nextToken = x509NameTokenizer.nextToken();
            if (nextToken.indexOf(43) > 0) {
                final X509NameTokenizer x509NameTokenizer2 = new X509NameTokenizer(nextToken, '+');
                String s = x509NameTokenizer2.nextToken();
                Boolean b2 = X509Name.FALSE;
                while (true) {
                    this.addEntry(hashtable, s, b2);
                    if (!x509NameTokenizer2.hasMoreTokens()) {
                        break;
                    }
                    s = x509NameTokenizer2.nextToken();
                    b2 = X509Name.TRUE;
                }
            }
            else {
                this.addEntry(hashtable, nextToken, X509Name.FALSE);
            }
        }
        if (b) {
            final Vector<Object> ordering = new Vector<Object>();
            final Vector<Object> values = new Vector<Object>();
            final Vector<Object> added = new Vector<Object>();
            int i = 0;
            int n = 1;
            while (i < this.ordering.size()) {
                if (this.added.elementAt(i)) {
                    ordering.insertElementAt(this.ordering.elementAt(i), n);
                    values.insertElementAt(this.values.elementAt(i), n);
                    added.insertElementAt(this.added.elementAt(i), n);
                    ++n;
                }
                else {
                    ordering.insertElementAt(this.ordering.elementAt(i), 0);
                    values.insertElementAt(this.values.elementAt(i), 0);
                    added.insertElementAt(this.added.elementAt(i), 0);
                    n = 1;
                }
                ++i;
            }
            this.ordering = ordering;
            this.values = values;
            this.added = added;
        }
    }
    
    private void addEntry(final Hashtable hashtable, String nextToken, final Boolean b) {
        final X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(nextToken, '=');
        nextToken = x509NameTokenizer.nextToken();
        if (x509NameTokenizer.hasMoreTokens()) {
            final String nextToken2 = x509NameTokenizer.nextToken();
            this.ordering.addElement(this.decodeOID(nextToken, hashtable));
            this.values.addElement(this.unescape(nextToken2));
            this.added.addElement(b);
            return;
        }
        throw new IllegalArgumentException("badly formatted directory string");
    }
    
    private void appendValue(final StringBuffer sb, final Hashtable hashtable, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        String id = hashtable.get(asn1ObjectIdentifier);
        if (id == null) {
            id = asn1ObjectIdentifier.getId();
        }
        sb.append(id);
        sb.append('=');
        final int length = sb.length();
        sb.append(s);
        final int length2 = sb.length();
        int i = length;
        int n = length2;
        if (s.length() >= 2) {
            i = length;
            n = length2;
            if (s.charAt(0) == '\\') {
                i = length;
                n = length2;
                if (s.charAt(1) == '#') {
                    i = length + 2;
                    n = length2;
                }
            }
        }
        int n2;
        while (i < (n2 = n)) {
            n2 = n;
            if (sb.charAt(i) != ' ') {
                break;
            }
            sb.insert(i, "\\");
            i += 2;
            ++n;
        }
        int n3;
        int j;
        while (true) {
            --n2;
            if ((n3 = n2) <= (j = i)) {
                break;
            }
            j = i;
            n3 = n2;
            if (sb.charAt(n2) != ' ') {
                break;
            }
            sb.insert(n2, '\\');
        }
        while (j <= n3) {
            final char char1 = sb.charAt(j);
            if (char1 != '\"' && char1 != '\\' && char1 != '+' && char1 != ',') {
                switch (char1) {
                    default: {
                        ++j;
                        continue;
                    }
                    case 59:
                    case 60:
                    case 61:
                    case 62: {
                        break;
                    }
                }
            }
            sb.insert(j, "\\");
            j += 2;
            ++n3;
        }
    }
    
    private String bytesToString(final byte[] array) {
        final int length = array.length;
        final char[] array2 = new char[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = (char)(array[i] & 0xFF);
        }
        return new String(array2);
    }
    
    private String canonicalize(String s) {
        final String s2 = s = Strings.toLowerCase(s.trim());
        if (s2.length() > 0) {
            s = s2;
            if (s2.charAt(0) == '#') {
                final ASN1Primitive decodeObject = this.decodeObject(s2);
                s = s2;
                if (decodeObject instanceof ASN1String) {
                    s = Strings.toLowerCase(((ASN1String)decodeObject).getString().trim());
                }
            }
        }
        return s;
    }
    
    private ASN1ObjectIdentifier decodeOID(String trim, final Hashtable hashtable) {
        trim = trim.trim();
        if (Strings.toUpperCase(trim).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(trim.substring(4));
        }
        if (trim.charAt(0) >= '0' && trim.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(trim);
        }
        final ASN1ObjectIdentifier asn1ObjectIdentifier = hashtable.get(Strings.toLowerCase(trim));
        if (asn1ObjectIdentifier != null) {
            return asn1ObjectIdentifier;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown object id - ");
        sb.append(trim);
        sb.append(" - passed to distinguished name");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private ASN1Primitive decodeObject(final String s) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(s.substring(1)));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown encoding in name: ");
            sb.append(ex);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    private boolean equivalentStrings(String canonicalize, String canonicalize2) {
        canonicalize = this.canonicalize(canonicalize);
        canonicalize2 = this.canonicalize(canonicalize2);
        return canonicalize.equals(canonicalize2) || this.stripInternalSpaces(canonicalize).equals(this.stripInternalSpaces(canonicalize2));
    }
    
    public static X509Name getInstance(final Object o) {
        if (o == null || o instanceof X509Name) {
            return (X509Name)o;
        }
        if (o instanceof X500Name) {
            return new X509Name(ASN1Sequence.getInstance(((X500Name)o).toASN1Primitive()));
        }
        if (o != null) {
            return new X509Name(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static X509Name getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private String stripInternalSpaces(final String s) {
        final StringBuffer sb = new StringBuffer();
        if (s.length() != 0) {
            final char char1 = s.charAt(0);
            sb.append(char1);
            int i = 1;
            char c = char1;
            while (i < s.length()) {
                final char char2 = s.charAt(i);
                if (c != ' ' || char2 != ' ') {
                    sb.append(char2);
                }
                ++i;
                c = char2;
            }
        }
        return sb.toString();
    }
    
    private String unescape(final String s) {
        if (s.length() != 0 && (s.indexOf(92) >= 0 || s.indexOf(34) >= 0)) {
            final char[] charArray = s.toCharArray();
            final StringBuffer sb = new StringBuffer(s.length());
            int n;
            if (charArray[0] == '\\' && charArray[1] == '#') {
                n = 2;
                sb.append("\\#");
            }
            else {
                n = 0;
            }
            int n2 = 0;
            int n3 = 0;
            int length = 0;
            boolean b = false;
            int n4;
            for (int i = n; i != charArray.length; ++i, n2 = n4) {
                final char c = charArray[i];
                if (c != ' ') {
                    b = true;
                }
                Label_0196: {
                    if (c == '\"') {
                        if (n2 == 0) {
                            n3 ^= 0x1;
                            break Label_0196;
                        }
                    }
                    else {
                        if (c == '\\' && n2 == 0 && n3 == 0) {
                            length = sb.length();
                            n4 = 1;
                            continue;
                        }
                        if (c == ' ' && n2 == 0 && !b) {
                            n4 = n2;
                            continue;
                        }
                    }
                    sb.append(c);
                }
                n4 = 0;
            }
            if (sb.length() > 0) {
                while (sb.charAt(sb.length() - 1) == ' ' && length != sb.length() - 1) {
                    sb.setLength(sb.length() - 1);
                }
            }
            return sb.toString();
        }
        return s.trim();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509Name) && !(o instanceof ASN1Sequence)) {
            return false;
        }
        if (this.toASN1Primitive().equals(((ASN1Encodable)o).toASN1Primitive())) {
            return true;
        }
        try {
            final X509Name instance = getInstance(o);
            final int size = this.ordering.size();
            if (size != instance.ordering.size()) {
                return false;
            }
            final boolean[] array = new boolean[size];
            final boolean equals = this.ordering.elementAt(0).equals(instance.ordering.elementAt(0));
            int n = -1;
            int i;
            int n2;
            if (equals) {
                n = size;
                i = 0;
                n2 = 1;
            }
            else {
                i = size - 1;
                n2 = -1;
            }
        Label_0129:
            while (i != n) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = this.ordering.elementAt(i);
                final String s = this.values.elementAt(i);
                while (true) {
                    for (int j = 0; j < size; ++j) {
                        if (!array[j]) {
                            if (asn1ObjectIdentifier.equals(instance.ordering.elementAt(j)) && this.equivalentStrings(s, (String)instance.values.elementAt(j))) {
                                array[j] = true;
                                final boolean b = true;
                                if (!b) {
                                    return false;
                                }
                                i += n2;
                                continue Label_0129;
                            }
                        }
                    }
                    final boolean b = false;
                    continue;
                }
            }
            return true;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o, final boolean b) {
        if (!b) {
            return this.equals(o);
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509Name) && !(o instanceof ASN1Sequence)) {
            return false;
        }
        if (this.toASN1Primitive().equals(((ASN1Encodable)o).toASN1Primitive())) {
            return true;
        }
        try {
            final X509Name instance = getInstance(o);
            final int size = this.ordering.size();
            if (size != instance.ordering.size()) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                if (!((ASN1ObjectIdentifier)this.ordering.elementAt(i)).equals(instance.ordering.elementAt(i))) {
                    return false;
                }
                if (!this.equivalentStrings((String)this.values.elementAt(i), (String)instance.values.elementAt(i))) {
                    return false;
                }
            }
            return true;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }
    
    public Vector getOIDs() {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.ordering.size(); ++i) {
            vector.addElement(this.ordering.elementAt(i));
        }
        return vector;
    }
    
    public Vector getValues() {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.values.size(); ++i) {
            vector.addElement(this.values.elementAt(i));
        }
        return vector;
    }
    
    public Vector getValues(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final Vector<String> vector = new Vector<String>();
        for (int i = 0; i != this.values.size(); ++i) {
            if (this.ordering.elementAt(i).equals(asn1ObjectIdentifier)) {
                String substring;
                final String s = substring = this.values.elementAt(i);
                if (s.length() > 2) {
                    substring = s;
                    if (s.charAt(0) == '\\') {
                        substring = s;
                        if (s.charAt(1) == '#') {
                            substring = s.substring(1);
                        }
                    }
                }
                vector.addElement(substring);
            }
        }
        return vector;
    }
    
    @Override
    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        for (int i = 0; i != this.ordering.size(); ++i) {
            final String stripInternalSpaces = this.stripInternalSpaces(this.canonicalize(this.values.elementAt(i)));
            final int hashCodeValue = this.hashCodeValue ^ this.ordering.elementAt(i).hashCode();
            this.hashCodeValue = hashCodeValue;
            this.hashCodeValue = (stripInternalSpaces.hashCode() ^ hashCodeValue);
        }
        return this.hashCodeValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.seq == null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            ASN1ObjectIdentifier asn1ObjectIdentifier = null;
            ASN1ObjectIdentifier asn1ObjectIdentifier2;
            for (int i = 0; i != this.ordering.size(); ++i, asn1ObjectIdentifier = asn1ObjectIdentifier2) {
                final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
                asn1ObjectIdentifier2 = this.ordering.elementAt(i);
                asn1EncodableVector3.add(asn1ObjectIdentifier2);
                asn1EncodableVector3.add(this.converter.getConvertedValue(asn1ObjectIdentifier2, this.values.elementAt(i)));
                DERSequence derSequence;
                if (asn1ObjectIdentifier != null && !(boolean)this.added.elementAt(i)) {
                    asn1EncodableVector.add(new DERSet(asn1EncodableVector2));
                    asn1EncodableVector2 = new ASN1EncodableVector();
                    derSequence = new DERSequence(asn1EncodableVector3);
                }
                else {
                    derSequence = new DERSequence(asn1EncodableVector3);
                }
                asn1EncodableVector2.add(derSequence);
            }
            asn1EncodableVector.add(new DERSet(asn1EncodableVector2));
            this.seq = new DERSequence(asn1EncodableVector);
        }
        return this.seq;
    }
    
    @Override
    public String toString() {
        return this.toString(X509Name.DefaultReverse, X509Name.DefaultSymbols);
    }
    
    public String toString(final boolean b, final Hashtable hashtable) {
        final StringBuffer sb = new StringBuffer();
        final Vector<StringBuffer> vector = new Vector<StringBuffer>();
        StringBuffer sb2 = null;
        for (int i = 0; i < this.ordering.size(); ++i) {
            if (this.added.elementAt(i)) {
                sb2.append('+');
                this.appendValue(sb2, hashtable, (ASN1ObjectIdentifier)this.ordering.elementAt(i), (String)this.values.elementAt(i));
            }
            else {
                sb2 = new StringBuffer();
                this.appendValue(sb2, hashtable, (ASN1ObjectIdentifier)this.ordering.elementAt(i), (String)this.values.elementAt(i));
                vector.addElement(sb2);
            }
        }
        final boolean b2 = true;
        int n = 1;
        if (b) {
            for (int j = vector.size() - 1; j >= 0; --j) {
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append(',');
                }
                sb.append(vector.elementAt(j).toString());
            }
        }
        else {
            int k = 0;
            int n2 = b2 ? 1 : 0;
            while (k < vector.size()) {
                if (n2 != 0) {
                    n2 = 0;
                }
                else {
                    sb.append(',');
                }
                sb.append(vector.elementAt(k).toString());
                ++k;
            }
        }
        return sb.toString();
    }
}
