package com.quangnv.service.utility_shared.util;

import com.quangnv.service.utility_shared.dto.BusinessFailedDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonUtil {
    public static String getToken() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpServletRequest request = attrs.getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public static String getFullUrl(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();
        return queryString == null ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
    }

    public static String concatUserNameFromEmail(String email) {
        return email.split("@")[0];
    }

    public static List<String> convertStringArrayToList(String str) {
        return Arrays.asList(str.split(","));
    }

    public static String messageFailed(String message, String exchange, String queue, String error) {
        BusinessFailedDto businessFailedDto = new BusinessFailedDto();
        businessFailedDto.setMessage(message);
        businessFailedDto.setTopicExchange(exchange);
        businessFailedDto.setQueueName(queue);
        businessFailedDto.setErrorMessage(error);
        return businessFailedDto.toString();
    }

    /**
     * Chuyển 1 string thành object Map để đưa vào ApiResponse.data
     * @param key   key mô tả dữ liệu
     * @param value giá trị string
     * @return Map<String, Object>
     */
    public static Map<String, Object> stringToObject(String key, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
