package org.spongycastle.asn1.eac;

import java.math.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class ECDSAPublicKey extends PublicKeyDataObject
{
    private static final int A = 2;
    private static final int B = 4;
    private static final int F = 64;
    private static final int G = 8;
    private static final int P = 1;
    private static final int R = 16;
    private static final int Y = 32;
    private byte[] basePointG;
    private BigInteger cofactorF;
    private BigInteger firstCoefA;
    private int options;
    private BigInteger orderOfBasePointR;
    private BigInteger primeModulusP;
    private byte[] publicPointY;
    private BigInteger secondCoefB;
    private ASN1ObjectIdentifier usage;
    
    public ECDSAPublicKey(final ASN1ObjectIdentifier usage, final BigInteger primeModulusP, final BigInteger firstCoefA, final BigInteger secondCoefB, final byte[] array, final BigInteger orderOfBasePointR, final byte[] array2, final int n) {
        this.usage = usage;
        this.setPrimeModulusP(primeModulusP);
        this.setFirstCoefA(firstCoefA);
        this.setSecondCoefB(secondCoefB);
        this.setBasePointG(new DEROctetString(array));
        this.setOrderOfBasePointR(orderOfBasePointR);
        this.setPublicPointY(new DEROctetString(array2));
        this.setCofactorF(BigInteger.valueOf(n));
    }
    
    public ECDSAPublicKey(final ASN1ObjectIdentifier usage, final byte[] array) throws IllegalArgumentException {
        this.usage = usage;
        this.setPublicPointY(new DEROctetString(array));
    }
    
    ECDSAPublicKey(final ASN1Sequence asn1Sequence) throws IllegalArgumentException {
        final Enumeration objects = asn1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        this.options = 0;
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject nextElement = objects.nextElement();
            if (!(nextElement instanceof ASN1TaggedObject)) {
                throw new IllegalArgumentException("Unknown Object Identifier!");
            }
            final ASN1TaggedObject asn1TaggedObject = nextElement;
            switch (asn1TaggedObject.getTagNo()) {
                default: {
                    this.options = 0;
                    throw new IllegalArgumentException("Unknown Object Identifier!");
                }
                case 7: {
                    this.setCofactorF(UnsignedInteger.getInstance(asn1TaggedObject).getValue());
                    continue;
                }
                case 6: {
                    this.setPublicPointY(ASN1OctetString.getInstance(asn1TaggedObject, false));
                    continue;
                }
                case 5: {
                    this.setOrderOfBasePointR(UnsignedInteger.getInstance(asn1TaggedObject).getValue());
                    continue;
                }
                case 4: {
                    this.setBasePointG(ASN1OctetString.getInstance(asn1TaggedObject, false));
                    continue;
                }
                case 3: {
                    this.setSecondCoefB(UnsignedInteger.getInstance(asn1TaggedObject).getValue());
                    continue;
                }
                case 2: {
                    this.setFirstCoefA(UnsignedInteger.getInstance(asn1TaggedObject).getValue());
                    continue;
                }
                case 1: {
                    this.setPrimeModulusP(UnsignedInteger.getInstance(asn1TaggedObject).getValue());
                    continue;
                }
            }
        }
        final int options = this.options;
        if (options == 32) {
            return;
        }
        if (options == 127) {
            return;
        }
        throw new IllegalArgumentException("All options must be either present or absent!");
    }
    
    private void setBasePointG(final ASN1OctetString asn1OctetString) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x8) == 0x0) {
            this.options = (options | 0x8);
            this.basePointG = asn1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Base Point G already set");
    }
    
    private void setCofactorF(final BigInteger cofactorF) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x40) == 0x0) {
            this.options = (options | 0x40);
            this.cofactorF = cofactorF;
            return;
        }
        throw new IllegalArgumentException("Cofactor F already set");
    }
    
    private void setFirstCoefA(final BigInteger firstCoefA) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x2) == 0x0) {
            this.options = (options | 0x2);
            this.firstCoefA = firstCoefA;
            return;
        }
        throw new IllegalArgumentException("First Coef A already set");
    }
    
    private void setOrderOfBasePointR(final BigInteger orderOfBasePointR) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x10) == 0x0) {
            this.options = (options | 0x10);
            this.orderOfBasePointR = orderOfBasePointR;
            return;
        }
        throw new IllegalArgumentException("Order of base point R already set");
    }
    
    private void setPrimeModulusP(final BigInteger primeModulusP) {
        final int options = this.options;
        if ((options & 0x1) == 0x0) {
            this.options = (options | 0x1);
            this.primeModulusP = primeModulusP;
            return;
        }
        throw new IllegalArgumentException("Prime Modulus P already set");
    }
    
    private void setPublicPointY(final ASN1OctetString asn1OctetString) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x20) == 0x0) {
            this.options = (options | 0x20);
            this.publicPointY = asn1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Public Point Y already set");
    }
    
    private void setSecondCoefB(final BigInteger secondCoefB) throws IllegalArgumentException {
        final int options = this.options;
        if ((options & 0x4) == 0x0) {
            this.options = (options | 0x4);
            this.secondCoefB = secondCoefB;
            return;
        }
        throw new IllegalArgumentException("Second Coef B already set");
    }
    
    public ASN1EncodableVector getASN1EncodableVector(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(asn1ObjectIdentifier);
        if (!b) {
            asn1EncodableVector.add(new UnsignedInteger(1, this.getPrimeModulusP()));
            asn1EncodableVector.add(new UnsignedInteger(2, this.getFirstCoefA()));
            asn1EncodableVector.add(new UnsignedInteger(3, this.getSecondCoefB()));
            asn1EncodableVector.add(new DERTaggedObject(false, 4, new DEROctetString(this.getBasePointG())));
            asn1EncodableVector.add(new UnsignedInteger(5, this.getOrderOfBasePointR()));
        }
        asn1EncodableVector.add(new DERTaggedObject(false, 6, new DEROctetString(this.getPublicPointY())));
        if (!b) {
            asn1EncodableVector.add(new UnsignedInteger(7, this.getCofactorF()));
        }
        return asn1EncodableVector;
    }
    
    public byte[] getBasePointG() {
        if ((this.options & 0x8) != 0x0) {
            return Arrays.clone(this.basePointG);
        }
        return null;
    }
    
    public BigInteger getCofactorF() {
        if ((this.options & 0x40) != 0x0) {
            return this.cofactorF;
        }
        return null;
    }
    
    public BigInteger getFirstCoefA() {
        if ((this.options & 0x2) != 0x0) {
            return this.firstCoefA;
        }
        return null;
    }
    
    public BigInteger getOrderOfBasePointR() {
        if ((this.options & 0x10) != 0x0) {
            return this.orderOfBasePointR;
        }
        return null;
    }
    
    public BigInteger getPrimeModulusP() {
        if ((this.options & 0x1) != 0x0) {
            return this.primeModulusP;
        }
        return null;
    }
    
    public byte[] getPublicPointY() {
        if ((this.options & 0x20) != 0x0) {
            return Arrays.clone(this.publicPointY);
        }
        return null;
    }
    
    public BigInteger getSecondCoefB() {
        if ((this.options & 0x4) != 0x0) {
            return this.secondCoefB;
        }
        return null;
    }
    
    @Override
    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }
    
    public boolean hasParameters() {
        return this.primeModulusP != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.getASN1EncodableVector(this.usage, this.hasParameters() ^ true));
    }
}
