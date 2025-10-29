package com.quangnv.service.utility_shared.dto;

import com.quangnv.service.utility_shared.constant.RoleValue;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class User {
    UUID id;
    String username;

    String email;


    String password;

    String firstName;

    String lastName;

    @Enumerated(EnumType.STRING)
    RoleValue role;

    boolean enabled = true;

    boolean accountNonExpired = true;

    boolean accountNonLocked = true;

    boolean credentialsNonExpired = true;

    String avatar;

    String ipValid;

    BigDecimal balance = BigDecimal.ZERO;
}
