package com.quangnv.service.utility_shared.constant;

public enum RoleValue {
    ADMIN,

    // Manga web
    STORY_MANAGE,

    // Shop MMO
    SHOP_MANAGE,
    SHOP_USER;

    public static RoleValue fromAuthority(String authority) {
        if (authority == null || authority.isBlank()) {
            throw new IllegalArgumentException("User role is missing");
        }
        return RoleValue.valueOf(authority.replace("ROLE_", ""));
    }
}
