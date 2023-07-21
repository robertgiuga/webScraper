package com.example.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class ListSerializer extends JsonSerializer<List<String>> {
    @Override
    public void serialize(List<String> strings, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        StringBuilder b = new StringBuilder();
        for (String s: strings) {
            b.append(s).append(" | ");
        }
        if(b.length()>0)
            b.delete(b.length()-3,b.length());
        jsonGenerator.writeString(b.toString());
    }
}
