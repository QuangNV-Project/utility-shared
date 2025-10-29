package com.quangnv.service.utility_shared.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParsedPaymentDescription {
    String userName;
    String code;

    public ParsedPaymentDescription(String userName, String code) {
        this.userName = userName;
        this.code = code;
    }

}
