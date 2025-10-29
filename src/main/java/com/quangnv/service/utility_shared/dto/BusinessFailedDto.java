package com.quangnv.service.utility_shared.dto;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessFailedDto {
    private String topicExchange;
    private String routingKey;
    private String queueName;
    private String message;
    private String errorMessage;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
