package com.example.application.fullcalendar.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class TrimToNullSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String s = StringUtils.trimToNull(value);
        if (s == null) {
            gen.writeNull();
        } else {
            gen.writeString(s);
        }
    }
}