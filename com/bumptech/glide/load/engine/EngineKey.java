package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.*;
import java.security.*;
import java.nio.*;
import java.io.*;

class EngineKey implements Key
{
    private static final String EMPTY_LOG_STRING = "";
    private final ResourceDecoder cacheDecoder;
    private final ResourceDecoder decoder;
    private final ResourceEncoder encoder;
    private int hashCode;
    private final int height;
    private final String id;
    private Key originalKey;
    private final Key signature;
    private final Encoder sourceEncoder;
    private String stringKey;
    private final ResourceTranscoder transcoder;
    private final Transformation transformation;
    private final int width;
    
    public EngineKey(final String id, final Key signature, final int width, final int height, final ResourceDecoder cacheDecoder, final ResourceDecoder decoder, final Transformation transformation, final ResourceEncoder encoder, final ResourceTranscoder transcoder, final Encoder sourceEncoder) {
        this.id = id;
        this.signature = signature;
        this.width = width;
        this.height = height;
        this.cacheDecoder = cacheDecoder;
        this.decoder = decoder;
        this.transformation = transformation;
        this.encoder = encoder;
        this.transcoder = transcoder;
        this.sourceEncoder = sourceEncoder;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final EngineKey engineKey = (EngineKey)o;
        return this.id.equals(engineKey.id) && this.signature.equals(engineKey.signature) && this.height == engineKey.height && this.width == engineKey.width && !(this.transformation == null ^ engineKey.transformation == null) && (this.transformation == null || this.transformation.getId().equals(engineKey.transformation.getId())) && !(this.decoder == null ^ engineKey.decoder == null) && (this.decoder == null || this.decoder.getId().equals(engineKey.decoder.getId())) && !(this.cacheDecoder == null ^ engineKey.cacheDecoder == null) && (this.cacheDecoder == null || this.cacheDecoder.getId().equals(engineKey.cacheDecoder.getId())) && !(this.encoder == null ^ engineKey.encoder == null) && (this.encoder == null || this.encoder.getId().equals(engineKey.encoder.getId())) && !(this.transcoder == null ^ engineKey.transcoder == null) && (this.transcoder == null || this.transcoder.getId().equals(engineKey.transcoder.getId())) && !(this.sourceEncoder == null ^ engineKey.sourceEncoder == null) && (this.sourceEncoder == null || this.sourceEncoder.getId().equals(engineKey.sourceEncoder.getId()));
    }
    
    public Key getOriginalKey() {
        if (this.originalKey == null) {
            this.originalKey = new OriginalKey(this.id, this.signature);
        }
        return this.originalKey;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.id.hashCode();
            this.hashCode = this.hashCode * 31 + this.signature.hashCode();
            this.hashCode = this.hashCode * 31 + this.width;
            this.hashCode = this.hashCode * 31 + this.height;
            final int hashCode = this.hashCode;
            final ResourceDecoder cacheDecoder = this.cacheDecoder;
            final int n = 0;
            int hashCode2;
            if (cacheDecoder != null) {
                hashCode2 = this.cacheDecoder.getId().hashCode();
            }
            else {
                hashCode2 = 0;
            }
            this.hashCode = hashCode * 31 + hashCode2;
            final int hashCode3 = this.hashCode;
            int hashCode4;
            if (this.decoder != null) {
                hashCode4 = this.decoder.getId().hashCode();
            }
            else {
                hashCode4 = 0;
            }
            this.hashCode = hashCode3 * 31 + hashCode4;
            final int hashCode5 = this.hashCode;
            int hashCode6;
            if (this.transformation != null) {
                hashCode6 = this.transformation.getId().hashCode();
            }
            else {
                hashCode6 = 0;
            }
            this.hashCode = hashCode5 * 31 + hashCode6;
            final int hashCode7 = this.hashCode;
            int hashCode8;
            if (this.encoder != null) {
                hashCode8 = this.encoder.getId().hashCode();
            }
            else {
                hashCode8 = 0;
            }
            this.hashCode = hashCode7 * 31 + hashCode8;
            final int hashCode9 = this.hashCode;
            int hashCode10;
            if (this.transcoder != null) {
                hashCode10 = this.transcoder.getId().hashCode();
            }
            else {
                hashCode10 = 0;
            }
            this.hashCode = hashCode9 * 31 + hashCode10;
            final int hashCode11 = this.hashCode;
            int hashCode12 = n;
            if (this.sourceEncoder != null) {
                hashCode12 = this.sourceEncoder.getId().hashCode();
            }
            this.hashCode = hashCode11 * 31 + hashCode12;
        }
        return this.hashCode;
    }
    
    @Override
    public String toString() {
        if (this.stringKey == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("EngineKey{");
            sb.append(this.id);
            sb.append('+');
            sb.append(this.signature);
            sb.append("+[");
            sb.append(this.width);
            sb.append('x');
            sb.append(this.height);
            sb.append("]+");
            sb.append('\'');
            String id;
            if (this.cacheDecoder != null) {
                id = this.cacheDecoder.getId();
            }
            else {
                id = "";
            }
            sb.append(id);
            sb.append('\'');
            sb.append('+');
            sb.append('\'');
            String id2;
            if (this.decoder != null) {
                id2 = this.decoder.getId();
            }
            else {
                id2 = "";
            }
            sb.append(id2);
            sb.append('\'');
            sb.append('+');
            sb.append('\'');
            String id3;
            if (this.transformation != null) {
                id3 = this.transformation.getId();
            }
            else {
                id3 = "";
            }
            sb.append(id3);
            sb.append('\'');
            sb.append('+');
            sb.append('\'');
            String id4;
            if (this.encoder != null) {
                id4 = this.encoder.getId();
            }
            else {
                id4 = "";
            }
            sb.append(id4);
            sb.append('\'');
            sb.append('+');
            sb.append('\'');
            String id5;
            if (this.transcoder != null) {
                id5 = this.transcoder.getId();
            }
            else {
                id5 = "";
            }
            sb.append(id5);
            sb.append('\'');
            sb.append('+');
            sb.append('\'');
            String id6;
            if (this.sourceEncoder != null) {
                id6 = this.sourceEncoder.getId();
            }
            else {
                id6 = "";
            }
            sb.append(id6);
            sb.append('\'');
            sb.append('}');
            this.stringKey = sb.toString();
        }
        return this.stringKey;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
        final byte[] array = ByteBuffer.allocate(8).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        messageDigest.update(this.id.getBytes("UTF-8"));
        messageDigest.update(array);
        String id;
        if (this.cacheDecoder != null) {
            id = this.cacheDecoder.getId();
        }
        else {
            id = "";
        }
        messageDigest.update(id.getBytes("UTF-8"));
        String id2;
        if (this.decoder != null) {
            id2 = this.decoder.getId();
        }
        else {
            id2 = "";
        }
        messageDigest.update(id2.getBytes("UTF-8"));
        String id3;
        if (this.transformation != null) {
            id3 = this.transformation.getId();
        }
        else {
            id3 = "";
        }
        messageDigest.update(id3.getBytes("UTF-8"));
        String id4;
        if (this.encoder != null) {
            id4 = this.encoder.getId();
        }
        else {
            id4 = "";
        }
        messageDigest.update(id4.getBytes("UTF-8"));
        String id5;
        if (this.sourceEncoder != null) {
            id5 = this.sourceEncoder.getId();
        }
        else {
            id5 = "";
        }
        messageDigest.update(id5.getBytes("UTF-8"));
    }
}
