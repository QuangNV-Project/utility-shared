package com.quangnv.service.utility_shared.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueueSender {
    RabbitTemplate rabbitTemplate;

    public void send(String message, String exchange, String routing, int priority) {
        try {
            if (StringUtils.hasLength(exchange) && StringUtils.hasLength(routing)) {
                rabbitTemplate.convertAndSend(exchange, routing, message, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setPriority(priority);
                        return message;
                    }
                });
            }
        } catch (Exception ex) {
            log.error("Send message to queue Exception :", ex);
        }
    }

    public void resend(String topic, String route, String message, int priority) {
        try {
            if (StringUtils.hasLength(message)) {
                rabbitTemplate.convertAndSend(topic, route, message, message1 -> {
                    message1.getMessageProperties().setPriority(priority);
                    return message1;
                });
            }
        } catch (Exception ex) {
            log.error("Send message to queue exception :", ex);
        }
    }
}
