package com.quangnv.service.utility_shared.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class TemplateUtil {

    private final SpringTemplateEngine templateEngine;

    /**
     * Render nội dung email HTML từ template trong resources/templates
     * @param templateName tên file template (không kèm .html)
     * @param variables Map chứa các biến truyền vào template
     * @return Chuỗi HTML đã render
     */
        public String renderTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
