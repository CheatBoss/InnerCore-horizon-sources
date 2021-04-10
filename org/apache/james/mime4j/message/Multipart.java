package org.apache.james.mime4j.message;

import java.util.*;
import org.apache.james.mime4j.util.*;

public class Multipart implements Body
{
    private List<BodyPart> bodyParts;
    private ByteSequence epilogue;
    private transient String epilogueStrCache;
    private Entity parent;
    private ByteSequence preamble;
    private transient String preambleStrCache;
    private String subType;
    
    public Multipart(final String subType) {
        this.bodyParts = new LinkedList<BodyPart>();
        this.parent = null;
        this.preamble = ByteSequence.EMPTY;
        this.preambleStrCache = "";
        this.epilogue = ByteSequence.EMPTY;
        this.epilogueStrCache = "";
        this.subType = subType;
    }
    
    public Multipart(final Multipart multipart) {
        this.bodyParts = new LinkedList<BodyPart>();
        this.parent = null;
        this.preamble = multipart.preamble;
        this.preambleStrCache = multipart.preambleStrCache;
        this.epilogue = multipart.epilogue;
        this.epilogueStrCache = multipart.epilogueStrCache;
        final Iterator<BodyPart> iterator = multipart.bodyParts.iterator();
        while (iterator.hasNext()) {
            this.addBodyPart(new BodyPart(iterator.next()));
        }
        this.subType = multipart.subType;
    }
    
    public void addBodyPart(final BodyPart bodyPart) {
        if (bodyPart != null) {
            this.bodyParts.add(bodyPart);
            bodyPart.setParent(this.parent);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void addBodyPart(final BodyPart bodyPart, final int n) {
        if (bodyPart != null) {
            this.bodyParts.add(n, bodyPart);
            bodyPart.setParent(this.parent);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public void dispose() {
        final Iterator<BodyPart> iterator = this.bodyParts.iterator();
        while (iterator.hasNext()) {
            iterator.next().dispose();
        }
    }
    
    public List<BodyPart> getBodyParts() {
        return Collections.unmodifiableList((List<? extends BodyPart>)this.bodyParts);
    }
    
    public int getCount() {
        return this.bodyParts.size();
    }
    
    public String getEpilogue() {
        if (this.epilogueStrCache == null) {
            this.epilogueStrCache = ContentUtil.decode(this.epilogue);
        }
        return this.epilogueStrCache;
    }
    
    ByteSequence getEpilogueRaw() {
        return this.epilogue;
    }
    
    @Override
    public Entity getParent() {
        return this.parent;
    }
    
    public String getPreamble() {
        if (this.preambleStrCache == null) {
            this.preambleStrCache = ContentUtil.decode(this.preamble);
        }
        return this.preambleStrCache;
    }
    
    ByteSequence getPreambleRaw() {
        return this.preamble;
    }
    
    public String getSubType() {
        return this.subType;
    }
    
    public BodyPart removeBodyPart(final int n) {
        final BodyPart bodyPart = this.bodyParts.remove(n);
        bodyPart.setParent(null);
        return bodyPart;
    }
    
    public BodyPart replaceBodyPart(final BodyPart bodyPart, final int n) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        final BodyPart bodyPart2 = this.bodyParts.set(n, bodyPart);
        if (bodyPart != bodyPart2) {
            bodyPart.setParent(this.parent);
            bodyPart2.setParent(null);
            return bodyPart2;
        }
        throw new IllegalArgumentException("Cannot replace body part with itself");
    }
    
    public void setBodyParts(final List<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
        final Iterator<BodyPart> iterator = bodyParts.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParent(this.parent);
        }
    }
    
    public void setEpilogue(final String epilogueStrCache) {
        this.epilogue = ContentUtil.encode(epilogueStrCache);
        this.epilogueStrCache = epilogueStrCache;
    }
    
    void setEpilogueRaw(final ByteSequence epilogue) {
        this.epilogue = epilogue;
        this.epilogueStrCache = null;
    }
    
    @Override
    public void setParent(final Entity entity) {
        this.parent = entity;
        final Iterator<BodyPart> iterator = this.bodyParts.iterator();
        while (iterator.hasNext()) {
            iterator.next().setParent(entity);
        }
    }
    
    public void setPreamble(final String preambleStrCache) {
        this.preamble = ContentUtil.encode(preambleStrCache);
        this.preambleStrCache = preambleStrCache;
    }
    
    void setPreambleRaw(final ByteSequence preamble) {
        this.preamble = preamble;
        this.preambleStrCache = null;
    }
    
    public void setSubType(final String subType) {
        this.subType = subType;
    }
}
