package com.quangnv.service.utility_shared.constant;

public class ServiceConstant {
    public enum ServiceName {
        TENANT_SERVICE("tenant-service"),
        BLOG_SERVICE("blog-service"),
        EUREKA_SERVICE("eureka-service"),
        API_GATEWAY("api-gateway"),
        CONFIG_SERVICE("config-service"),
        PLATFORM_SERVICE("platform-service"),
        AUTH_SERVICE("auth-service"),
        UTILITY_SHARED("utility-shared"),
        PAYMENT_SERVICE("payment-service"),
        FIN_TRACK_SERVICE("fin-track-service");

        private final String service;

        ServiceName(String service) {
            this.service = service;
        }

        public String getService() {
            return service;
        }

        public String toLoadBalancedUri() {
            return "lb://" + service;
        }
    }
}
