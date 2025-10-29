package com.quangnv.service.utility_shared.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangnv.service.utility_shared.exception.HttpException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HttpUtil {
    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    /**
     * Call post api
     *
     * @return response object
     * @author LoiDV
     */
    public static String callAPI(String url, String requestBody) throws HttpException {
        HttpHeaders httpHeaders = new HttpHeaders();
        log.info("url: " + url);
        log.info("requestBody: " + requestBody);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, httpEntity, String.class);
    }

    public <T> T callPostAPIAuth(String url, Object requestBody, Class<T> responseClass) throws HttpException {
        return callAPIAuth(url, HttpMethod.POST, requestBody, responseClass);
    }

    public <T> T callAPIAuth(String url, HttpMethod method, Object requestBody, Class<T> responseClass) throws HttpException {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + CommonUtil.getToken());
        return callAPI(url, header, method, requestBody, responseClass);
    }

    /**
     * Hàm gọi API chung
     *
     * @param url           endpoint cần call
     * @param mapHeader     header gửi kèm
     * @param method        GET/POST/PUT/DELETE
     * @param requestBody   body request (String, Map hoặc Object)
     * @param responseClass kiểu dữ liệu trả về
     * @return dữ liệu response parse sang responseClass
     */
    public <T> T callAPI(
            String url,
            Map<String, String> mapHeader,
            HttpMethod method,
            Object requestBody,
            Class<T> responseClass
    ) {
        try {
            // Chuẩn bị header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            if (mapHeader != null) {
                mapHeader.forEach(httpHeaders::set);
            }

            // Chuẩn bị body
            HttpEntity<String> httpEntity;
            if (requestBody != null) {
                String body;
                if (requestBody instanceof String) {
                    body = (String) requestBody;
                } else {
                    body = objectMapper.writeValueAsString(requestBody);
                }
                httpEntity = new HttpEntity<>(body, httpHeaders);
            } else {
                httpEntity = new HttpEntity<>(httpHeaders);
            }

            // Gọi API
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    url,
                    method,
                    httpEntity,
                    responseClass
            );

            T response = responseEntity.getBody();
            log.info("Call API {} {} -> {}", method, url, response);

            return response;

        } catch (Exception ex) {
            log.error("Call external API exception: {} {}", method, url, ex);
            throw new RuntimeException("Call API lỗi: " + url, ex);
        }
    }

    public static String callPostAPIByToken(String url, String requestBody, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, httpEntity, String.class);
    }

    public static String callPostAPIWithPublicKey(String url, String requestBody, String publicKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // Set public key vào header "key"
        httpHeaders.set("key", publicKey);

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(url, httpEntity, String.class);
    }
}
