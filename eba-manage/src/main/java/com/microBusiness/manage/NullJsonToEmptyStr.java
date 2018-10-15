package com.microBusiness.manage;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2017/11/20 上午11:55
 * Describe:
 * Update:
 */
public class NullJsonToEmptyStr extends JsonSerializer {
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value    Value to serialize; can <b>not</b> be null.
     * @param jgen     Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for
     */
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString("");
    }
}
