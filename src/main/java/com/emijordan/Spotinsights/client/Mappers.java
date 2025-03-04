package com.emijordan.Spotinsights.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mappers {
    public static <T> T convertData(String json, Class<T> clase){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(json,clase);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
