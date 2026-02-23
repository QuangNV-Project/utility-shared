package com.quangnv.service.utility_shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // Hỗ trợ Java 8 Date/Time
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // Không lỗi nếu thừa field
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Trả về dạng String cho ngày tháng

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Lỗi parse Object sang JSON: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Lỗi parse JSON sang Object: {}", e.getMessage());
            return null;
        }
    }
}