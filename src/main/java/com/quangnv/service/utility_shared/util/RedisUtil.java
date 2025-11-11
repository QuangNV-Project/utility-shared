package com.quangnv.service.utility_shared.util;

import com.quangnv.service.utility_shared.constant.ServiceConstant;

public class RedisUtil {
    /**
     * Tạo key cho Redis, biệt lập theo TỪNG SERVICE VÀ TỪNG TENANT.
     *
     * @param serviceName Tên của microservice (ví dụ: "auth-service", "payment-service")
     * @param tenantId    ID của tenant
     * @param email       Email của người dùng
     * @return Key Redis duy nhất (ví dụ: "otp:auth-service:tenant_A:user@example.com")
     */
    private String buildRedisKey(ServiceConstant serviceName, String tenantId, String email) {
        return "otp:" + serviceName + ":" + tenantId + ":" + email;
    }
}
